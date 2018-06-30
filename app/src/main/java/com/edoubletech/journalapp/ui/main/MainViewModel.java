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

package com.edoubletech.journalapp.ui.main;

import com.edoubletech.journalapp.data.MainRepository;
import com.edoubletech.journalapp.data.model.Note;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MainRepository mRepo;

    public MainViewModel(MainRepository repository) {
        mRepo = repository;
    }

    public LiveData<List<Note>> getListOfNotes() {
        return mRepo.getListOfNotes();
    }

    public void deleteNote(Note note) {
        mRepo.deleteNote(note);
    }
}
