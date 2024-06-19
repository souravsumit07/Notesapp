package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Notes> notesList;
    private Context context;

    public NotesAdapter(Context context, List<Notes> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        int pos = position;
        Notes note = notesList.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewContent.setText(note.getContent());

        int randomColorResourceId = getRandomColor(holder.itemView.getContext());
        holder.notesContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), randomColorResourceId));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesDatabase db = Room.databaseBuilder(holder.itemView.getContext(),
                        NotesDatabase.class, "Notes_db").allowMainThreadQueries().build();
                NotesDao notesDao = db.notesDao();
                notesDao.deleteById(note.getUid());
                notesList.remove(pos);
                notifyDataSetChanged();
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("note_id", note.getUid());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void updateData(List<Notes> newNotesList) {
        notesList.addAll(newNotesList);
        notifyDataSetChanged();
    }

    private int getRandomColor(Context context) {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random random = new Random();
        int randomIndex = random.nextInt(colorCode.size());
        return colorCode.get(randomIndex);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        CardView notesContainer;
        TextView textViewTitle, textViewContent;
        ImageButton delete,update;

        NoteViewHolder(View itemView) {
            super(itemView);

            notesContainer = itemView.findViewById(R.id.notescontainer);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }
    }
}
