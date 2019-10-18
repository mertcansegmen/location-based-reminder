package com.mertcansegmen.locationbasedreminder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mertcansegmen.locationbasedreminder.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        configureBottomNavigation();
    }

    private void configureBottomNavigation() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.remindersFragment, R.id.notesFragment, R.id.placesFragment,
                R.id.placeGroupsFragment, R.id.settingsFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);

    }
}
