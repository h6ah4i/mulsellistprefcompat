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
import java.util.Set;

import android.preference.Preference;
import android.util.Log;

// Implementation for Honeycomb or later
/**
 * @hide
 */
class PreferenceCompatImplHoneycomb extends PreferenceCompatImpl {
    private static final String TAG = "PreferenceCompatImplHoneycomb";

    private Method mGetPersistedStringSet;
    private Method mPersistStringSet;

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getPersistedStringSet(
            Preference pref, Set<String> defaultReturnValue) {
        try {
            synchronized (this) {
                if (mGetPersistedStringSet == null) {
                    mGetPersistedStringSet = Preference.class.getDeclaredMethod(
                            "getPersistedStringSet", Set.class);
                    mGetPersistedStringSet.setAccessible(true);
                }
            }

            return (Set<String>) (mGetPersistedStringSet.invoke(pref, defaultReturnValue));
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
            synchronized (this) {
                if (mPersistStringSet == null) {
                    mPersistStringSet = Preference.class.getDeclaredMethod(
                            "persistStringSet", Set.class);
                    mPersistStringSet.setAccessible(true);
                }
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
