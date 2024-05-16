package com.example.bytebills;

import static com.example.bytebills.Preferences.DEFAULT_LANGUAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bytebills.databinding.ActivityMainBinding;
import com.example.bytebills.databinding.IdentificationBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class Identification extends AppCompatActivity {
    private IdentificationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLocale();
        super.onCreate(savedInstanceState);

        binding = IdentificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    private void setLocale() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.contains("selected_language")) {
            preferences.edit().putString("selected_language", DEFAULT_LANGUAGE).apply();
        }
        String selectedLanguage = preferences.getString("selected_language", DEFAULT_LANGUAGE);
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}