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

package com.h6ah4i.android.compat.content;

import java.util.Set;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.h6ah4i.android.compat.utils.SharedPreferencesJsonStringSetWrapperUtils;

//Implementation for Honeycomb or later
/**
 * @hide
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
final class SharedPreferencesCompatImplHoneycomb
        extends SharedPreferencesCompatImpl {

    private static final String TAG = "SharedPreferenceCompatImplHoneycomb";

    @Override
    public Set<String> getStringSet(
            SharedPreferences prefs, String key, Set<String> defValues) {

        checkAndUpgradeToNativeStringSet(prefs, key);
        
        return prefs.getStringSet(key, defValues);
    }

    public static void checkAndUpgradeToNativeStringSet(SharedPreferences prefs, String key) {
        try {
            // Do test whether the preference is String one
            prefs.getString(key, null);

            // Parse current values
            final Set<String> values =
                    SharedPreferencesJsonStringSetWrapperUtils.getStringSet(
                            prefs, key, null);

            // Replace as Set<String> values
            prefs.edit().remove(key).apply();
            if (values != null) {
                prefs.edit().putStringSet(key, values).apply();
            }
        } catch (ClassCastException e) {
            return;
        } catch (RuntimeException e) {
            Log.e(TAG, "checkAndUpgradeToNativeStringSet", e);
        }
    }
}
