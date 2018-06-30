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

package com.edoubletech.journalapp.data;

import com.edoubletech.journalapp.data.dao.NotesDao;
import com.edoubletech.journalapp.data.dao.UserDao;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.data.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

public class MainRepository {

    private final UserDao mUserDao;
    private NotesDao mNotesDao;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    public MainRepository(NotesDao notesDao, UserDao userDao) {
        mNotesDao = notesDao;
        mUserDao = userDao;
    }

    public User getUser() {
        return mUserDao.getUser();
    }

    public LiveData<List<Note>> getListOfNotes() {
        return mNotesDao.getListOfNotes();
    }

    public List<Note> getNotes(String childId){
        return mNotesDao.getNotes(childId);
    }

    public LiveData<Note> getNoteById(int id) {
        return mNotesDao.getNote(id);
    }

    public void addNote(Note note) {
        mNotesDao.insertData(note);
    }

    public void updateNote(Note note) {
        mNotesDao.updateData(note);
    }

    public void deleteNote(Note note) {
        mNotesDao.deleteData(note);
    }
}