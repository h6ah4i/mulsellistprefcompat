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

import org.json.JSONArray;
import org.json.JSONException;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.Preference;
import android.util.Log;

public class PreferenceCompat {
    private static final String TAG = "PreferenceCompat";

    private static abstract class PreferenceCompatImpl {
        public abstract Set<String> getPersistedStringSet(
                Preference pref, Set<String> defaultReturnValue);

        public abstract boolean persistStringSet(Preference pref, Set<String> values);
    }

    // Implementation for Honeycomb or later
    private static final class PreferenceCompatImplHoneycomb extends PreferenceCompatImpl {
        private static final String TAG = "PreferenceCompatImpl_Honeycomb";

        private Method mGetPersistedStringSet;
        private Method mPersistStringSet;

        @SuppressWarnings("unchecked")
        @Override
        public Set<String> getPersistedStringSet(
                Preference pref, Set<String> defaultReturnValue) {
            try {
                if (mGetPersistedStringSet == null) {
                    mGetPersistedStringSet = Preference.class.getDeclaredMethod(
                            "getPersistedStringSet", Set.class);
                    mGetPersistedStringSet.setAccessible(true);
                }

                return (Set<String>) (mGetPersistedStringSet.invoke(pref, new Object[] {
                        defaultReturnValue
                }));
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (RuntimeException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            }

            return defaultReturnValue;
        }

        @Override
        public boolean persistStringSet(Preference pref, Set<String> values) {
            try {
                if (mPersistStringSet == null) {
                    mPersistStringSet = Preference.class.getDeclaredMethod(
                            "persistStringSet", Set.class);
                    mPersistStringSet.setAccessible(true);
                }

                return (Boolean) (mPersistStringSet.invoke(pref, values));
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            } catch (RuntimeException e) {
                Log.e(TAG, "getPersistedStringSetCompat", e);
            }

            return false;
        }
    }

    // Implementation for Gingerbread
    private static final class PreferenceCompatImplGB extends PreferenceCompatImpl {
        // Original idea:
        // http://stackoverflow.com/questions/7361627/how-can-write-code-to-make-sharedpreferences-for-array-in-android
        @Override
        public Set<String> getPersistedStringSet(
                Preference pref, Set<String> defaultReturnValue) {

            final String key = pref.getKey();
            final String json = pref.getSharedPreferences().getString(key, null);
            final Set<String> values = new HashSet<String>();

            if (json != null) {
                try {
                    final JSONArray a = new JSONArray(json);
                    for (int i = 0; i < a.length(); i++) {
                        final String value = a.optString(i);
                        values.add(value);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "getPersistedStringSet", e);
                }
            }

            return values;
        }

        public boolean persistStringSet(Preference pref, Set<String> values) {
            try {
                final String key = pref.getKey();
                final SharedPreferences.Editor editor = pref.getEditor();
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
                Log.e(TAG, "persistStringSetCompat_ImplGB()", e);

                return false;
            }
        }
    }

    private static final PreferenceCompatImpl IMPL;

    static {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.HONEYCOMB) {
            IMPL = new PreferenceCompatImplHoneycomb();
        } else {
            IMPL = new PreferenceCompatImplGB();
        }
    }

    /**
     * Attempts to get a persisted set of Strings from the
     * {@link android.content.SharedPreferences}.
     * <p>
     * This will check if this Preference is persistent, get the
     * SharedPreferences from the {@link PreferenceManager}, and get the value.
     * 
     * @param pref Preference to attempts to get a persisted set of Strings
     * @param defaultReturnValue The default value to return if either the
     *            Preference is not persistent or the Preference is not in the
     *            shared preferences.
     * @return The value from the SharedPreferences or the default return value.
     */
    public static boolean persistStringSet(Preference pref, Set<String> values) {
        return IMPL.persistStringSet(pref, values);
    }

    /**
     * Attempts to get a persisted set of Strings from the
     * {@link android.content.SharedPreferences}.
     * <p>
     * This will check if this Preference is persistent, get the
     * SharedPreferences from the {@link PreferenceManager}, and get the value.
     * 
     * @param pref Preference to attempts to get a persisted set of Strings
     * @param defaultReturnValue The default value to return if either the
     *            Preference is not persistent or the Preference is not in the
     *            shared preferences.
     * @return The value from the SharedPreferences or the default return value.
     */
    public static Set<String> getPersistedStringSet(
            Preference pref, Set<String> defaultReturnValue) {
        return IMPL.getPersistedStringSet(pref, defaultReturnValue);
    }
}
