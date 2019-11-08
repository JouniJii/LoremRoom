package com.example.loremroom;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoremDao {

    @Query("SELECT * FROM LoremEntity ORDER BY id DESC")
    List<LoremEntity> getAllInDescendingOrder();

    @Query("SELECT * FROM LoremEntity")
    List<LoremEntity> getAll();

    @Query("SELECT * FROM LoremEntity WHERE id = :id")
    LoremEntity findLoremById(int id);

    @Insert
    void InsertEntity(LoremEntity loremEntity);

    @Delete
    void DeleteEntity(LoremEntity loremEntity);
}
