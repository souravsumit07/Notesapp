package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {
    EditText title, content;
    Button update;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        title = findViewById(R.id.updateeditTextTitle);
        content = findViewById(R.id.updateeditTextContent);
        update = findViewById(R.id.buttonUpdate);
        noteId = getIntent().getIntExtra("note_id", -1);

        // Load animations
        Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.edittexttitle_anim);
        Animation contentAnim = AnimationUtils.loadAnimation(this, R.anim.edittextcontent_anim);
        Animation buttonAnim = AnimationUtils.loadAnimation(this, R.anim.buttonsave_anim);

        // Apply animations
        title.startAnimation(titleAnim);
        content.startAnimation(contentAnim);
        update.startAnimation(buttonAnim);

        Notes note = getNoteFromDatabase(noteId);
        if (note != null) {
            title.setText(note.getTitle());
            content.setText(note.getContent());
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTitle = title.getText().toString().trim();
                String updatedContent = content.getText().toString().trim();

                if (updateNoteInDatabase(note, updatedTitle, updatedContent)) {
                    Toast.makeText(EditNoteActivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditNoteActivity.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Notes getNoteFromDatabase(int noteId) {
        NotesDatabase db = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class, "Notes_db").allowMainThreadQueries().build();

        NotesDao notesDao = db.notesDao();
        return notesDao.getNoteById(noteId);
    }

    private boolean updateNoteInDatabase(Notes note, String updatedTitle, String updatedContent) {
        if (note != null) {
            note.setTitle(updatedTitle);
            note.setContent(updatedContent);

            NotesDatabase db = Room.databaseBuilder(getApplicationContext(),
                    NotesDatabase.class, "Notes_db").allowMainThreadQueries().build();
            NotesDao notesDao = db.notesDao();
            notesDao.update(note);
            return true;
        }
        return false;
    }
}
