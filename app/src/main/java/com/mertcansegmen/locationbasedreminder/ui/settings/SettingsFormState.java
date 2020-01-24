package com.mertcansegmen.locationbasedreminder.ui.settings;

import androidx.annotation.Nullable;

/**
 * Data validation state of the settings form.
 */
class SettingsFormState {

    @Nullable
    private Integer defaultRangeError;
    private boolean defaultRangeChanged;
    private boolean isDataValid;

    SettingsFormState(@Nullable Integer defaultRangeError) {
        this.defaultRangeError = defaultRangeError;
        this.defaultRangeChanged = true;
        this.isDataValid = false;
    }

    SettingsFormState(boolean defaultRangeChanged, boolean isDataValid) {
        this.defaultRangeError = null;
        this.defaultRangeChanged = defaultRangeChanged;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getDefaultRangeError() {
        return defaultRangeError;
    }

    public boolean isDefaultRangeChanged() {
        return defaultRangeChanged;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
