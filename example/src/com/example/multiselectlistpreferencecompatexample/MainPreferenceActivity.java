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
import java.util.Set;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.example.multiselectlistpreferencecompat.R;
import com.h6ah4i.android.compat.preference.MultiSelectListPreferenceCompat;

public class MainPreferenceActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {
    private String mOrigSummaryText;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final MultiSelectListPreferenceCompat multiselpref = (MultiSelectListPreferenceCompat) findPreference("key_text");
        multiselpref.setOnPreferenceChangeListener(this);

        mOrigSummaryText = multiselpref.getSummary().toString();
        multiselpref.setSummary(makeSummaryText(mOrigSummaryText, multiselpref.getValues()));
    }

    @SuppressWarnings("unchecked")
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        if (key.equals("key_text")) {
            final MultiSelectListPreferenceCompat multiselpref = (MultiSelectListPreferenceCompat) preference;

            multiselpref.setSummary(makeSummaryText(mOrigSummaryText, (Set<String>) newValue));

            return true;
        } else {
            return false;
        }
    }

    public static String makeSummaryText(String baseText, Set<String> values) {
        // sort items
        String[] sorted = new String[values.size()];
        values.toArray(sorted);
        Arrays.sort(sorted);
        
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < sorted.length; i++) {
            if (i > 0)
                builder.append(", ");
            
            builder.append(sorted[i]);
        }
        builder.append("]");
        
        return baseText + " " + builder.toString();
    }

}
