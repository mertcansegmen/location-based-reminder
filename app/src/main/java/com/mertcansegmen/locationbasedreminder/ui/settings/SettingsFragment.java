package com.mertcansegmen.locationbasedreminder.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mertcansegmen.locationbasedreminder.BuildConfig;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.ui.BaseFragment;
import com.mertcansegmen.locationbasedreminder.util.ConfigUtils;
import com.mertcansegmen.locationbasedreminder.util.DevicePrefs;
import com.mertcansegmen.locationbasedreminder.viewmodel.SettingsFragmentViewModel;

import static com.mertcansegmen.locationbasedreminder.ui.addeditplace.AddEditPlaceFragment.PREF_KEY_DEFAULT_RANGE;

public class SettingsFragment extends BaseFragment {

    private static final String PLAY_STORE_URL = "market://details?id=com.mertcansegmen.locationbasedreminder";
    private static final String GITHUB_URL = "https://github.com/mertcansegmen/location-based-reminder";
    private static final String EMAIL_ADDRESS = "mertcan.segmen@gmail.com";

    private TextInputLayout defaultRangeTextInputLayout;
    private TextInputEditText defaultRangeEditText;
    private MaterialButton saveButton, rateUsButton, sendFeedbackButton, seeOnGithubButton;
    private TextView appVersionTextView;

    private SettingsFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews(view);
        initViewModel();
        setDefaultRange();
        initValidationObserver();
        setDefaultRangeTextChangeListener();
        setSaveButtonClickListener();
        setRateUsButtonClickListener();
        setSendFeedbackButtonClickListener();
        setSeeOnGithubButtonClickListener();
        setAppVersion();
    }

    private void initViews(View view) {
        defaultRangeTextInputLayout = view.findViewById(R.id.text_input_layout_default_range);
        defaultRangeEditText = view.findViewById(R.id.edit_text_default_range);
        saveButton = view.findViewById(R.id.btn_save);
        rateUsButton = view.findViewById(R.id.btn_rate_us);
        sendFeedbackButton = view.findViewById(R.id.btn_send_feedback);
        seeOnGithubButton = view.findViewById(R.id.btn_see_on_github);
        appVersionTextView = view.findViewById(R.id.txt_app_version);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SettingsFragmentViewModel.class);
    }

    private void setDefaultRange() {
        String range = String.valueOf(viewModel.getSavedRange());

        defaultRangeEditText.setText(range);
        viewModel.rangeValueChanged(range);
    }

    private void initValidationObserver() {
        viewModel.getSettingsFormState().observe(this, settingsFormState -> {
            if (settingsFormState == null) return;

            saveButton.setEnabled(
                    settingsFormState.isDataValid() && settingsFormState.isDefaultRangeChanged());

            defaultRangeTextInputLayout.setErrorEnabled(settingsFormState.isDataValid());

            if (settingsFormState.getDefaultRangeError() != null) {
                defaultRangeEditText.setError(getString(settingsFormState.getDefaultRangeError()));
            }
        });
    }

    private void setDefaultRangeTextChangeListener() {
        defaultRangeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.rangeValueChanged(s.toString());
            }
        });
    }

    private void setSaveButtonClickListener() {
        saveButton.setOnClickListener(v -> {
            String range = defaultRangeEditText.getText().toString();
            DevicePrefs.setPrefs(requireContext(), PREF_KEY_DEFAULT_RANGE, Integer.parseInt(range));
            viewModel.rangeValueChanged(range);
        });
    }

    private void setRateUsButtonClickListener() {
        rateUsButton.setOnClickListener(v ->
                goToUrl(PLAY_STORE_URL)
        );
    }

    private void setSendFeedbackButtonClickListener() {
        sendFeedbackButton.setOnClickListener(v ->
                sendEmail()
        );
    }

    private void setSeeOnGithubButtonClickListener() {
        seeOnGithubButton.setOnClickListener(v ->
                goToUrl(GITHUB_URL)
        );
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void sendEmail() {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADDRESS});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
        emailIntent.setSelector(selectorIntent);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(Intent.createChooser(emailIntent, getString(R.string.email)));
    }

    private void setAppVersion() {
        appVersionTextView.setText(getString(R.string.app_name_and_version, BuildConfig.VERSION_NAME));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
                ConfigUtils.closeKeyboard(requireActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
