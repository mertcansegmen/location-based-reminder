package com.mertcansegmen.locationbasedreminder.ui.reminders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.ReminderWithNotePlacePlaceGroup;
import com.mertcansegmen.locationbasedreminder.services.ReminderService;
import com.mertcansegmen.locationbasedreminder.ui.views.OutlineChip;

public class ReminderAdapter extends ListAdapter<ReminderWithNotePlacePlaceGroup, ReminderAdapter.ReminderViewHolder> {

    private OnItemClickListener listener;

    private ReminderAdapterViewModel viewModel;

    private static final DiffUtil.ItemCallback<ReminderWithNotePlacePlaceGroup> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<ReminderWithNotePlacePlaceGroup>() {
        @Override
        public boolean areItemsTheSame(@NonNull ReminderWithNotePlacePlaceGroup oldItem,
                                       @NonNull ReminderWithNotePlacePlaceGroup newItem) {
            return oldItem.getReminder().getReminderId() == newItem.getReminder().getReminderId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReminderWithNotePlacePlaceGroup oldItem,
                                          @NonNull ReminderWithNotePlacePlaceGroup newItem) {
            return oldItem.getNote().getTitle().equals(newItem.getNote().getTitle()) &&
                    oldItem.getNote().getBody().equals(newItem.getNote().getBody()) &&
                    oldItem.getReminder().isActive() == newItem.getReminder().isActive() &&
                    ((oldItem.getPlace() != null && newItem.getPlace() != null &&
                    oldItem.getPlace().getName().equals(newItem.getPlace().getName())) ||
                    (oldItem.getPlaceGroupWithPlaces() != null && newItem.getPlaceGroupWithPlaces() != null &&
                    oldItem.getPlaceGroupWithPlaces().getPlaceGroup().getName()
                                    .equals(newItem.getPlaceGroupWithPlaces().getPlaceGroup().getName())));
        }
    };

    public ReminderAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);
        return new ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderWithNotePlacePlaceGroup currentReminder = getItem(position);

        holder.switchMaterial.setChecked(currentReminder.getReminder().isActive());

        holder.titleTextView.setText(currentReminder.getNote().getTitle());
        holder.titleTextView.setVisibility(currentReminder.getNote().getTitle().isEmpty() ? View.GONE : View.VISIBLE);

        holder.noteTextView.setText(currentReminder.getNote().getBody());
        holder.noteTextView.setVisibility(currentReminder.getNote().getBody().isEmpty() ? View.GONE : View.VISIBLE);

        holder.chipGroup.removeAllViews();
        holder.placeGroupEmptyTextView.setVisibility(View.GONE);
        holder.placeRemovedTextView.setVisibility(View.GONE);

        Context context = holder.chipGroup.getContext();
        if(currentReminder.getPlace() != null) {
            addPlaceChip(context, currentReminder, holder.chipGroup);
        } else if(currentReminder.getPlaceGroupWithPlaces() != null) {
            addPlaceGroupChip(context, currentReminder, holder.chipGroup);
            if(currentReminder.getPlaceGroupWithPlaces().getPlaces().isEmpty()) {
                // If the place group attached to reminder does not contain any place
                holder.placeGroupEmptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            // If the place or place group attached to reminder was removed
            holder.placeRemovedTextView.setVisibility(View.VISIBLE);
        }
    }

    private void addPlaceChip(Context context, ReminderWithNotePlacePlaceGroup currentReminder,
                              ChipGroup chipGroup) {
        OutlineChip chip = new OutlineChip(context);
        chip.setChipIcon(context.getResources().getDrawable(R.drawable.ic_places));
        chip.setText(currentReminder.getPlace().getName());
        chipGroup.addView(chip);
    }

    private void addPlaceGroupChip(Context context, ReminderWithNotePlacePlaceGroup currentReminder,
                                   ChipGroup chipGroup) {
        OutlineChip chip = new OutlineChip(context);
        chip.setChipIcon(context.getResources().getDrawable(R.drawable.ic_place_groups_small));
        chip.setText(currentReminder.getPlaceGroupWithPlaces().getPlaceGroup().getName());
        chipGroup.addView(chip);
    }


    public ReminderWithNotePlacePlaceGroup getReminderAt(int position) {
        return getItem(position);
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView noteTextView;
        private ChipGroup chipGroup;
        private SwitchMaterial switchMaterial;
        private TextView placeRemovedTextView;
        private TextView placeGroupEmptyTextView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txt_title);
            noteTextView = itemView.findViewById(R.id.txt_note);
            chipGroup = itemView.findViewById(R.id.chip_group);
            switchMaterial = itemView.findViewById(R.id.switch_material);
            placeRemovedTextView = itemView.findViewById(R.id.txt_place_removed_error);
            placeGroupEmptyTextView = itemView.findViewById(R.id.txt_place_group_empty_error);

            viewModel = ViewModelProviders.of((FragmentActivity) itemView.getContext()).get(ReminderAdapterViewModel.class);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(getItem(position));
                }
            });

            switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
                viewModel.setActive(ReminderAdapter.this.getItem(getAdapterPosition()), isChecked);
                Intent serviceIntent = new Intent(buttonView.getContext(), ReminderService.class);
                ContextCompat.startForegroundService(buttonView.getContext(), serviceIntent);
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ReminderWithNotePlacePlaceGroup reminder);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
