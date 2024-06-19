package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class NotesActivity extends AppCompatActivity {
    EditText editTextTitle, editTextContent;
    Button buttonSave;
    NotesDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Finding references to XML elements
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);

        // Load animations
        Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.edittexttitle_anim);
        Animation contentAnim = AnimationUtils.loadAnimation(this, R.anim.edittextcontent_anim);
        Animation buttonAnim = AnimationUtils.loadAnimation(this, R.anim.buttonsave_anim);

        // Apply animations
        editTextTitle.startAnimation(titleAnim);
        editTextContent.startAnimation(contentAnim);
        buttonSave.startAnimation(buttonAnim);

        db = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class, "Notes_db").build();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new bgthread().start();
                // Finish NotesActivity
                finish();
                // Start MainActivity
                startActivity(new Intent(NotesActivity.this, MainActivity.class));
            }
        });
    }

    class bgthread extends Thread {
        public void run() {
            String title = editTextTitle.getText().toString();
            String content = editTextContent.getText().toString();

            if (title.isEmpty() || content.isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NotesActivity.this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            // Insert data into the database
            NotesDao notesDao = db.notesDao();
            notesDao.insert(new Notes(title, content));

            // Clear EditText fields
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editTextTitle.setText("");
                    editTextContent.setText("");
                }
            });
            // Inform the user that data has been saved
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(NotesActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
