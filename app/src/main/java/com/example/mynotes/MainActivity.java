    package com.example.mynotes;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.view.animation.Animation;
    import android.view.animation.AnimationUtils;
    import android.widget.Button;
    import android.widget.ImageView;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.Observer;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.recyclerview.widget.StaggeredGridLayoutManager;
    import androidx.room.Room;

    import java.util.ArrayList;
    import java.util.List;

    public class MainActivity extends AppCompatActivity {
        private static final String TAG = "MainActivity";
        RecyclerView recyclerView;
        NotesAdapter notesAdapter;
        List<Notes> notesList = new ArrayList<>();

        boolean isGridLayout = true;
        ImageView toggleButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

            // Initialize the adapter
            notesAdapter = new NotesAdapter(this, notesList);
            recyclerView.setAdapter(notesAdapter);
            toggleButton = findViewById(R.id.toggleButton);

            // Fetch data from the database using LiveData
            fetchData();
        }

        private void fetchData() {
            // Access the database
            NotesDatabase db = Room.databaseBuilder(getApplicationContext(),
                    NotesDatabase.class, "Notes_db").build();

            NotesDao notesDao = db.notesDao();

            // Observe changes in the notes data
            notesDao.getAllLiveData().observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    // Update the UI with the fetched data
                    notesAdapter.updateData(notes);
                    // Log message to indicate data update
                    Log.d(TAG, "Notes data updated successfully");
                }
            });
        }


        public void toggle(View view) {
            // Load the animation
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
            // Apply the animation to the toggle button
            toggleButton.startAnimation(rotation);
            if(isGridLayout) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                ((ImageView)view).setImageResource(R.drawable.grid);

            }else{
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

                ((ImageView)view).setImageResource(R.drawable.img);


            }
            isGridLayout = !isGridLayout;

        }

        public void AddNote(View view) {
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }
    }
