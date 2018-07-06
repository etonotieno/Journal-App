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

package com.edoubletech.journalapp.injection;

import android.app.Application;

import com.edoubletech.journalapp.data.MainRepository;
import com.edoubletech.journalapp.data.dao.NotesDao;
import com.edoubletech.journalapp.data.dao.UserDao;
import com.edoubletech.journalapp.data.db.NotesDatabase;
import com.edoubletech.journalapp.ui.ViewModelFactory;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application mApp;

    public ApplicationModule(Application application) {
        mApp = application;
    }

    @Singleton
    @Provides
    NotesDatabase provideNotesDatabase() {
        return Room.databaseBuilder(mApp.getApplicationContext(),
                NotesDatabase.class, "journal.db")
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    NotesDao provideNotesDao(NotesDatabase db) {
        return db.notesDao();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(NotesDatabase db){
        return db.userDao();
    }

    @Singleton
    @Provides
    MainRepository provideNotesRepository(NotesDao notesDao, UserDao userDao) {
        return new MainRepository(notesDao, userDao);
    }

    @Singleton
    @Provides
    ViewModelFactory provideViewModelFactory(MainRepository repository) {
        return new ViewModelFactory(repository);
    }
}
