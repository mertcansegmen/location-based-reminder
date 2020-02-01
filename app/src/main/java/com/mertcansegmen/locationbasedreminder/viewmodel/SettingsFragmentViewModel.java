package com.mertcansegmen.locationbasedreminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.ui.settings.SettingsFormState;
import com.mertcansegmen.locationbasedreminder.util.DevicePrefs;

import org.apache.commons.lang3.StringUtils;

import static com.mertcansegmen.locationbasedreminder.ui.addeditplace.AddEditPlaceFragment.DEFAULT_RANGE;
import static com.mertcansegmen.locationbasedreminder.ui.addeditplace.AddEditPlaceFragment.PREF_KEY_DEFAULT_RANGE;

public class SettingsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<SettingsFormState> settingsFormState = new MutableLiveData<>();

    public SettingsFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Creates new form state for settings depending on the range value retrieved from edit text
     *
     * @param newRange range value retrieved from default range edit text
     */
    public void rangeValueChanged(String newRange) {
        if (isRangeValueValid(newRange)) {
            // Range value is valid
            if (getSavedRange() == Integer.parseInt(newRange)) {
                // Range value is valid but it is the same as the saved one
                settingsFormState.setValue(new SettingsFormState(false, true));
            } else {
                // Range value is valid and different from the saved one
                settingsFormState.setValue(new SettingsFormState(true, true));
            }
        } else {
            // Range value is not valid
            settingsFormState.setValue(new SettingsFormState(R.string.enter_valid_range));
        }
    }

    /**
     * Checks if string range value is numeric and between 10 and 1000.
     *
     * @return true if valid, false otherwise
     */
    private boolean isRangeValueValid(String range) {
        return StringUtils.isNumeric(range) && range.length() < 5 &&
                Integer.parseInt(range) >= 10 && Integer.parseInt(range) <= 1000;
    }

    /**
     * @return default range saved in shared preferences
     */
    public int getSavedRange() {
        return DevicePrefs.getPrefs(getApplication(), PREF_KEY_DEFAULT_RANGE, DEFAULT_RANGE);
    }

    public LiveData<SettingsFormState> getSettingsFormState() {
        return settingsFormState;
    }
}
