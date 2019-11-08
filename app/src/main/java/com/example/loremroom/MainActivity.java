package com.example.loremroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager cm;
    private Context context;
    private Boolean isNet;
    private final static int BUTTON_ID = 1234;
    private final static int EDITTEXT_ID = 5678;

    public RequestQueue queue = null;
    private Gson gson = new Gson();

    private LoremAdapter loremAdapter;
    private ArrayList<LoremObject> lorems = new ArrayList<>();

    private LoremDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (context == null) {
            context = getApplicationContext();
        }

        database = LoremDatabase.getInstance(context);
        new getAllLorems().execute();

        isNet = isAnyNetwork(context);
        if(isNet == false) {
            Toast.makeText(context, "No network available.", Toast.LENGTH_SHORT).show();
        }

        buildToolbar();

        loremAdapter = new LoremAdapter(this, R.layout.listview_item, lorems);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(loremAdapter);
    }

    public void buildToolbar () {
        LinearLayout toolbarLayout = findViewById(R.id.toolbar);

        final EditText editText = new EditText(this);
        editText.setId(EDITTEXT_ID);
        editText.setHint(R.string.editText_hint);
        final LinearLayout.LayoutParams editText_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        editText.setBackgroundColor(Color.LTGRAY);
        editText.setSingleLine(true);
        toolbarLayout.addView(editText, editText_params);

        Button button = new Button(this);
        button.setId(BUTTON_ID);
        button.setText(R.string.button_text);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f);
        toolbarLayout.addView(button, button_params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keyword = editText.getText().toString();

                if (keyword.length() == 0) {
                    finish();
                }
                doJsonQuery(keyword);
            }
        });

    }

     private void doJsonQuery(String keyword) {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        String url = "https://loremflickr.com/json/320/240/" + keyword.trim();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LoremObject loremObject = gson.fromJson(response.toString(), LoremObject.class);

                        final LoremEntity loremEntity = new LoremEntity();
                        loremEntity.owner = loremObject.getOwner();
                        loremEntity.license = loremObject.getLicense();
                        String url = loremObject.getUrl();
                        loremEntity.file = url.substring( url.lastIndexOf('/')+1, url.length());

                        // set url to local file name
                        loremObject.setUrl(loremEntity.file);
                        lorems.add(loremObject);

                        File imgFile = context.getFileStreamPath(loremEntity.file);
                        if (!imgFile.exists()) {
                            new DownloadImageTask().execute(url);
                        }
                        else {
                            loremAdapter.notifyDataSetChanged();
                        }

                        // insert to database
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                database.loremDao().InsertEntity(loremEntity);
                            }
                        });

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        String filename;
        public DownloadImageTask() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            filename = url.substring( url.lastIndexOf('/')+1, url.length());
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            saveImageToFile(result, filename);
            loremAdapter.notifyDataSetChanged();
        }
    }

    public void saveImageToFile(Bitmap bitmap, String filename) {

        FileOutputStream out;
        // Files are saved to /data/user/0/com.example.loremroom/files

        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.close();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Error saving image to file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private class getAllLorems extends AsyncTask<Void, Void, List<LoremEntity>> {
        public getAllLorems() {
        }

        @Override
        protected List<LoremEntity> doInBackground(Void... voids) {
            List<LoremEntity> loremEntities;
            loremEntities = LoremDatabase.getInstance(context).loremDao().getAllInDescendingOrder();
            return loremEntities;
        }

        @Override
        protected void onPostExecute(List<LoremEntity> loremEntities) {
            super.onPostExecute(loremEntities);
            lorems.clear();

            for (int i=0; i < loremEntities.size(); i++) {
                LoremEntity loremEntity = loremEntities.get(i);
                LoremObject loremObject = new LoremObject(loremEntity.file, loremEntity.license, loremEntity.owner, 0, 0, null, null, null);
                lorems.add(loremObject);
            }
        }
    }

    /*
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }*/

    private boolean isAnyNetwork(Context context) {

        final Network[] allNetworks;
        cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        allNetworks = cm.getAllNetworks();
        return (allNetworks != null);
    }

}
