/*
 * Copyright (C) 2012 Haruki Hasegawa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.multiselectlistpreferencecompatexample;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.h6ah4i.android.compat.content.SharedPreferenceCompat;

public class MainActivity
        extends Activity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    // fields
    private String mKeyText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKeyText = getText(R.string.prefs_key_text).toString();

        // make launcher button
        final Button launchButton = (Button) findViewById(R.id.settings_button);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPreferenceActivity();
            }
        });

        // make toggle button
        final Button toggleButton = (Button) findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSettingValues();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCurrentSettingsText();
        getPrefs().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        getPrefs().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings: {
                launchPreferenceActivity();
            }
                break;
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateCurrentSettingsText();
    }

    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    private Set<String> getDefaultValues() {
        final Set<String> defValues = new HashSet<String>();
        defValues.addAll(Arrays.asList(
                getResources().getStringArray(
                        R.array.multisellistpref_defvalues)));
        return defValues;
    }

    private void updateCurrentSettingsText() {
        // ex. SharedPreferenceCompat.getStringSet()
        final SharedPreferences prefs = getPrefs();
        final Set<String> values =
                SharedPreferenceCompat.getStringSet(prefs, mKeyText, getDefaultValues());

        if (values == null)
            return;

        final TextView text = (TextView) findViewById(R.id.text_settings_values);
        text.setText(Utils.sortedToString(values));
    }

    private void launchPreferenceActivity() {
        final Intent intent = new Intent(this, MainPreferenceActivity.class);
        startActivity(intent);
    }

    private void toggleSettingValues() {
        // ex. SharedPreferenceCompat.getStringSet()
        final SharedPreferences prefs = getPrefs();
        final Set<String> values =
                SharedPreferenceCompat.getStringSet(
                        prefs, mKeyText, getDefaultValues());

        if (values == null)
            return;

        final Set<String> toggledValues = new HashSet<String>();

        final String[] allValues = getResources().getStringArray(
                R.array.multisellistpref_entryValues);

        for (String value : allValues) {
            if (!values.contains(value)) {
                toggledValues.add(value);
            }
        }

        // ex. SharedPreferenceCompat.EditorCompat.putStringSet()
        // SharedPreferenceCompat.EditorCompat
        // .putStringSet(prefs.edit(), mKeyText, toggledValues)
        // .commit();

        // ex. SharedPreferenceCompat.EditorCompatWrapper
        final SharedPreferenceCompat.EditorCompatWrapper wrappedEditor =
                new SharedPreferenceCompat.EditorCompatWrapper(prefs.edit());

        wrappedEditor.putStringSet(mKeyText, toggledValues).commit();
    }
}
