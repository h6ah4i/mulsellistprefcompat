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

import com.h6ah4i.android.compat.utils.SharedPreferencesJsonStringSetWrapperUtils;

import android.preference.Preference;
import android.util.Log;

//Implementation for Gingerbread
/**
 * @hide
 */
class PreferenceCompatImplGB extends PreferenceCompatImpl {
    private static final String TAG = "PreferenceCompatImplGB";

    private Method mShouldPersist;

    private boolean shouldPersist(Preference pref) {
        try {
            synchronized (this) {
                if (mShouldPersist == null) {
                    mShouldPersist = Preference.class.getDeclaredMethod("shouldPersist");
                    mShouldPersist.setAccessible(true);

                }
            }

            return (Boolean) mShouldPersist.invoke(pref);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "shouldPersist", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "shouldPersist", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "shouldPersist", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "shouldPersist", e);
        } catch (RuntimeException e) {
            Log.e(TAG, "shouldPersist", e);
        }

        return false;
    }

    @Override
    public Set<String> getPersistedStringSet(
            Preference pref, Set<String> defaultReturnValue) {

        if (!shouldPersist(pref)) {
            return defaultReturnValue;
        }

        return SharedPreferencesJsonStringSetWrapperUtils.getStringSet(
                pref.getSharedPreferences(), pref.getKey(), defaultReturnValue);
    }

    @Override
    public boolean persistStringSet(Preference pref, Set<String> values) {
        if (shouldPersist(pref)) {
            // Shouldn't store null
            if (values.equals(getPersistedStringSet(pref, null))) {
                // It's already there, so the same as persisting
                return true;
            }

            return SharedPreferencesJsonStringSetWrapperUtils.putStringSet(
                    pref.getEditor(), pref.getKey(), values);
        }
        return false;
    }
}
