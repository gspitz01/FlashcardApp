package com.gregspitz.flashcardapp.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gregspitz.flashcardapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
