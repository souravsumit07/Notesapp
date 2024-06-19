package com.example.mynotes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface NotesDao {
    @Insert
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY uid DESC")
    LiveData<List<Notes>> getAllLiveData();
    @Query("DELETE FROM notes WHERE uid = :noteId")
    void deleteById(long noteId);

    @Update
        // Add Update annotation
    void update(Notes notes); // Method to update a note
    @Query("SELECT * FROM notes WHERE uid = :noteId")
    Notes getNoteById(long noteId); // Method to retrieve a note by its ID

}