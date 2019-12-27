package com.mertcansegmen.locationbasedreminder.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteId() == newItem.getNoteId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getBody().equals(newItem.getBody());
        }
    };

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = getItem(position);

        if(currentNote.getTitle() != null)
            holder.titleTextView.setText(currentNote.getTitle());
        else
            holder.titleTextView.setVisibility(View.GONE);

        if(currentNote.getBody() != null)
            holder.noteTextView.setText(currentNote.getBody());
        else
            holder.noteTextView.setVisibility(View.GONE);
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView noteTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txt_title);
            noteTextView = itemView.findViewById(R.id.txt_note);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
