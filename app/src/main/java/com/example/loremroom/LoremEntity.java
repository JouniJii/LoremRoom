package com.example.loremroom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LoremEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String file;
    public String owner;
    public String license;
}

