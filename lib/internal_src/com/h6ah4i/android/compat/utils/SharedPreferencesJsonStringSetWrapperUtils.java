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

package com.h6ah4i.android.compat.utils;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.SharedPreferences;
import android.util.Log;

public final class SharedPreferencesJsonStringSetWrapperUtils {
    private static final String TAG = "SharedPreferencesJsonStringSetWrapperUtils";

    // Original idea:
    // http://stackoverflow.com/questions/7361627/how-can-write-code-to-make-sharedpreferences-for-array-in-android

    public static Set<String> getStringSet(
            SharedPreferences prefs, String key, Set<String> defaultReturnValue) {
        final String json = prefs.getString(key, null);
        Set<String> values = null;

        if (json != null) {
            try {
                final JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    final String value = a.optString(i);

                    if (values == null) {
                        values = new HashSet<String>();
                    }

                    values.add(value);
                }
            } catch (JSONException e) {
                Log.e(TAG, "getStringSet", e);
                values = null;
            }
        }

        return (values != null) ? values : defaultReturnValue;
    }

    public static boolean putStringSet(
            SharedPreferences.Editor editor, String key, Set<String> values) {
        try {
            final JSONArray a = new JSONArray();
            final String[] strValues = (String[]) values.toArray(new String[values.size()]);

            for (int i = 0; i < values.size(); i++) {
                a.put(strValues[i]);
            }

            if (!values.isEmpty()) {
                editor.putString(key, a.toString());
            } else {
                editor.putString(key, null);
            }

            return editor.commit();
        } catch (RuntimeException e) {
            Log.e(TAG, "putStringSet()", e);

            return false;
        }
    }

}
