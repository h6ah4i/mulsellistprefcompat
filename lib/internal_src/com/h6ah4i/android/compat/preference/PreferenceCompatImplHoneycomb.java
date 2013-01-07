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

package com.h6ah4i.android.compat.preference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

import com.h6ah4i.android.compat.utils.SharedPreferencesJsonStringSetWrapperUtils;

// Implementation for Honeycomb or later
/**
 * @hide
 */
@TargetApi(11)
class PreferenceCompatImplHoneycomb extends PreferenceCompatImpl {
    private static final String TAG = "PreferenceCompatImplHoneycomb";

    private Method mGetPersistedStringSet;
    private Method mPersistStringSet;

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getPersistedStringSet(
            Preference pref, Set<String> defaultReturnValue) {
        try {
            checkAndUpgradeToNativeStringSet(pref.getSharedPreferences(), pref.getKey());

            synchronized (this) {
                if (mGetPersistedStringSet == null) {
                    mGetPersistedStringSet = Preference.class.getDeclaredMethod(
                            "getPersistedStringSet", Set.class);
                    mGetPersistedStringSet.setAccessible(true);
                }
            }

            return (Set<String>) (mGetPersistedStringSet.invoke(pref, defaultReturnValue));
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "getPersistedStringSet", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "getPersistedStringSet", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "getPersistedStringSet", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "getPersistedStringSet", e);
        } catch (RuntimeException e) {
            Log.e(TAG, "getPersistedStringSet", e);
        }

        return defaultReturnValue;
    }

    @Override
    public boolean persistStringSet(Preference pref, Set<String> values) {
        try {
            checkAndUpgradeToNativeStringSet(pref.getSharedPreferences(), pref.getKey());

            synchronized (this) {
                if (mPersistStringSet == null) {
                    mPersistStringSet = Preference.class.getDeclaredMethod(
                            "persistStringSet", Set.class);
                    mPersistStringSet.setAccessible(true);
                }
            }

            return (Boolean) (mPersistStringSet.invoke(pref, values));
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "persistStringSet", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "persistStringSet", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "persistStringSet", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "persistStringSet", e);
        } catch (RuntimeException e) {
            Log.e(TAG, "persistStringSet", e);
        }

        return false;
    }

    public static void checkAndUpgradeToNativeStringSet(SharedPreferences prefs, String key) {
        try {
            // Do test whether the preference is String one
            prefs.getString(key, null);

            // Parse current values
            Set<String> values =
                    SharedPreferencesJsonStringSetWrapperUtils.getStringSet(
                            prefs, key, null);

            if (values == null) {
                values = new HashSet<String>();
            }

            // Replace as Set<String> values
            prefs.edit()
                    .remove(key)
                    .putStringSet(key, values)
                    .apply();
        } catch (ClassCastException e) {
            return;
        } catch (RuntimeException e) {
            Log.e(TAG, "checkAndUpgradeToNativeStringSet", e);
        }
    }
}
