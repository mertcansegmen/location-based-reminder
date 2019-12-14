package com.mertcansegmen.locationbasedreminder.ui.placegroups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.util.PlaceChip;

public class PlaceGroupWithPlacesAdapter extends ListAdapter<PlaceGroupWithPlaces,
        PlaceGroupWithPlacesAdapter.PlaceGroupWithPlacesViewHolder> {

    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<PlaceGroupWithPlaces> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlaceGroupWithPlaces>() {
        @Override
        public boolean areItemsTheSame(@NonNull PlaceGroupWithPlaces oldItem, @NonNull PlaceGroupWithPlaces newItem) {
            return oldItem.getPlaceGroup().getPlaceGroupId() == newItem.getPlaceGroup().getPlaceGroupId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlaceGroupWithPlaces oldItem, @NonNull PlaceGroupWithPlaces newItem) {
            return oldItem.getPlaceGroup().getName().equals(newItem.getPlaceGroup().getName());
        }
    };

    public PlaceGroupWithPlacesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PlaceGroupWithPlacesAdapter.PlaceGroupWithPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_group_item, parent, false);
        return new PlaceGroupWithPlacesAdapter.PlaceGroupWithPlacesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceGroupWithPlacesViewHolder holder, int position) {
        PlaceGroupWithPlaces currentPlaceGroupWithPlaces = getItem(position);
        holder.placeGroupTextView.setText(currentPlaceGroupWithPlaces.getPlaceGroup().getName());
        for(final Place place : currentPlaceGroupWithPlaces.getPlaces()) {
            PlaceChip chip = new PlaceChip(holder.placeGroupTextView.getContext());
            chip.setPlace(place);
            chip.setText(place.getName());
            chip.setTextSize(Dimension.SP, 12);
            chip.setChipIcon(holder.placeGroupTextView.getContext().getResources().getDrawable(R.drawable.ic_places));
            holder.chipGroup.addView(chip);
        }
    }

    public PlaceGroupWithPlaces getPlaceGroupWithPlacesAt(int position) {
        return getItem(position);
    }

    class PlaceGroupWithPlacesViewHolder extends RecyclerView.ViewHolder {

        private TextView placeGroupTextView;
        private ChipGroup chipGroup;

        public PlaceGroupWithPlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeGroupTextView = itemView.findViewById(R.id.txt_place_group);
            chipGroup = itemView.findViewById(R.id.chip_group);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(PlaceGroupWithPlaces placeGroupWithPlaces);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
