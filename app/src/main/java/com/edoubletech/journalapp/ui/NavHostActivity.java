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

package com.edoubletech.journalapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.edoubletech.journalapp.JournalSettings;
import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.dao.NotesDao;
import com.edoubletech.journalapp.data.dao.UserDao;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.data.model.User;
import com.edoubletech.journalapp.ui.add.AddFragment;
import com.edoubletech.journalapp.ui.calendar.CalendarFragment;
import com.edoubletech.journalapp.ui.main.MainFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class NavHostActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject
    ViewModelFactory factory;
    @Inject
    UserDao userDao;
    @Inject
    NotesDao notesDao;

    NavHostViewModel viewModel;
    private User user;
    private List<Note> notes;
    private GoogleApiClient mApiClient;
    public static BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MyJournal) getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_host);

        // If this is the first time opening the app, load the MainFragment
        if (savedInstanceState == null) loadFragment(new MainFragment());

        viewModel = ViewModelProviders.of(this, factory).get(NavHostViewModel.class);

        user = viewModel.getUser();
        String id = user.getId();
        notes = viewModel.getNote(id);

        bottomNav = findViewById(R.id.bottomNavView);
        bottomNav.setOnNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build();

        mApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem profileIcon = menu.findItem(R.id.profile_icon);
        Glide.with(this)
                .asBitmap()
                .load(user.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        profileIcon.setIcon(new BitmapDrawable(getResources(), resource));
                    }
                });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile_icon) {
            // Handle a click on the profile icon
            showLogoutConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to log out?");
        builder.setPositiveButton("Log Out", (dialog, id) -> logoutUser());
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logoutUser() {
        Auth.GoogleSignInApi.signOut(mApiClient).setResultCallback(status -> {
            if (status.isSuccess()) {

                JournalSettings.setUserLoginStatus(false);

                userDao.deleteData(user);

                notesDao.deleteNotes(notes);

                // Make the user go ic_background to the LoginActivity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (status.isCanceled()) {
                Toast.makeText(this, "Logging out was cancelled. Try again",
                        Toast.LENGTH_SHORT).show();

            } else if (status.isInterrupted())
                Toast.makeText(this, "Logging out was interrupted Try again",
                        Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_button:
                loadFragment(new MainFragment());
                return true;
            case R.id.calendar:
                loadFragment(new CalendarFragment());
                return true;
            case R.id.addNote:
                loadFragment(new AddFragment());
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
