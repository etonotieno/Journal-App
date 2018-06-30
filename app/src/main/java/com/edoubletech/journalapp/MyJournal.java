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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.edoubletech.journalapp.di.ApplicationComponent;
import com.edoubletech.journalapp.di.ApplicationModule;
import com.edoubletech.journalapp.di.DaggerApplicationComponent;
import com.google.firebase.FirebaseApp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class MyJournal extends MultiDexApplication {

    private ApplicationComponent appComponent;
    private static MutableLiveData<Boolean> hasNetwork;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        hasNetwork = new MutableLiveData<>();

        hasNetwork.setValue(hasNetwork());

        JournalSettings.initialize(this);

        FirebaseApp.initializeApp(this);

        appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public Boolean hasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = (cm != null) ? cm.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static LiveData<Boolean> getNetworkStatus() {
        return hasNetwork;
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}
