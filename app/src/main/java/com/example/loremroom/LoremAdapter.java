package com.example.loremroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class LoremAdapter extends ArrayAdapter<LoremObject> {

    private Context context;
    private LayoutInflater inflater;
    private int item_layout;
    //private LoremDatabase database = LoremDatabase.getInstance(getContext());
    //private LoremEntity loremEntity;

    public LoremAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LoremObject> objects) {
        super(context, resource, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        item_layout = resource;
        this.context = context;

    }

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        ImageView imageView;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final LoremObject loremObject = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(item_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.textView1 = convertView.findViewById(R.id.textView_owner);
            viewHolder.textView2 = convertView.findViewById(R.id.textView_license);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //new getLoremById().execute(position+1);

        viewHolder.textView1.setText(loremObject.getOwner());
        viewHolder.textView2.setText(loremObject.getLicense());

        /**/
        Uri imgUri = Uri.parse("file:///data/data/com.example.loremroom/files/"+loremObject.getUrl());
        viewHolder.imageView.setImageURI(imgUri);
        /**/


        // Kuvanlataus tiedostosta
/*
        File imgFile = context.getFileStreamPath ( loremObject.getUrl());
        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewHolder.imageView.setImageBitmap(myBitmap);
        }
 */
/*
        String imagepath = context.getFilesDir().toString();
        //File imgFile = context.getFileStreamPath ( loremObject.getUrl());

        Picasso.get()
                .load("file://" + imagepath + "/" + loremObject.getUrl())
                .placeholder(android.R.drawable.picture_frame)
                .error(android.R.drawable.stat_notify_error)
                .noFade()
                .fit()
                .into(viewHolder.imageView);
*/
        return convertView;
    }

    /*
    private class getLoremById extends AsyncTask<Integer, Void, LoremEntity> {
        private LoremDao loremDao;
        public getLoremById() {
        }

        @Override
        protected LoremEntity doInBackground(Integer... integers) {
            LoremEntity loremEntity;
            loremEntity = LoremDatabase.getInstance(context).loremDao().findLoremById(integers[0]);
            return loremEntity;
        }

        protected void onPostExecute(LoremEntity result) {
            loremEntity = result;
        }
    }
    */

}
