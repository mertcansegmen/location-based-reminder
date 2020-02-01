package com.mertcansegmen.locationbasedreminder.ui.places;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;

public class PlaceAdapter extends ListAdapter<Place, PlaceAdapter.PlaceViewHolder> {

    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<Place> DIFF_CALLBACK = new DiffUtil.ItemCallback<Place>() {
        @Override
        public boolean areItemsTheSame(@NonNull Place oldItem, @NonNull Place newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Place oldItem, @NonNull Place newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getRadius() == newItem.getRadius();
        }
    };

    public PlaceAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place currentNote = getItem(position);
        holder.placeTextView.setText(currentNote.getName());
        holder.radiusTextView.setText(holder.radiusTextView.getResources()
                .getString(R.string.radius_text, currentNote.getRadius()));
    }

    public Place getPlaceAt(int position) {
        return getItem(position);
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        private TextView placeTextView;
        private TextView radiusTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeTextView = itemView.findViewById(R.id.txt_name);
            radiusTextView = itemView.findViewById(R.id.txt_radius);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Place place);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
