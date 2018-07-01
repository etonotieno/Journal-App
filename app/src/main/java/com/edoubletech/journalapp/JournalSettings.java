/*
 * Copyright (C) 2018 Eton Otieno Oboch
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edoubletech.journalapp;

import android.content.Context;
import android.content.SharedPreferences;

public class JournalSettings {

    private static final String SHARED_PREF_NAME = "journal";
    private static final String IS_USER_LOGGED_IN = "userIsLoggedIn";

    private static SharedPreferences preferences;

    static void initialize(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setUserLoginStatus(boolean loginStatus) {
        preferences.edit()
                .putBoolean(IS_USER_LOGGED_IN, loginStatus)
                .apply();
    }

    public static boolean userIsLoggedIn() {
        return preferences.getBoolean(IS_USER_LOGGED_IN, false);
    }
}
