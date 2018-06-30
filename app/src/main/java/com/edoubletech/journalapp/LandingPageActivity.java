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

import android.content.Intent;
import android.os.Bundle;

import com.edoubletech.journalapp.ui.LoginActivity;
import com.edoubletech.journalapp.ui.NavHostActivity;

import androidx.appcompat.app.AppCompatActivity;

public class LandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        findViewById(R.id.skipBtn).setOnClickListener(view -> {
            Intent intent = new Intent(LandingPageActivity.this,
                    JournalSettings.isUserLoggedIn() ? NavHostActivity.class : LoginActivity.class);
            startActivity(intent);
            finish();
            JournalSettings.setIsFirstTimeToLaunch(false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!JournalSettings.isFirstTimeToLaunch()) {
            Intent intent = new Intent(LandingPageActivity.this,
                    JournalSettings.isUserLoggedIn() ? NavHostActivity.class : LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
