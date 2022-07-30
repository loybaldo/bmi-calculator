package com.tp.bmicalculator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadSettings();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreference themePref = findPreference("themeKey");
            // ListPreference langPref = findPreference("langKey");

            assert themePref != null;
            themePref.setOnPreferenceChangeListener((pref, newVal) -> {
                boolean darkTheme = (boolean) newVal;
                if (darkTheme) { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); }
                else { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); }
                return true;
            });
        }
    }

    private void loadSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = sp.getBoolean("themeKey", false);
        if (darkTheme) { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); }
        else { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); }



    }


}