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

package com.edoubletech.journalapp.data.dao;

import com.edoubletech.journalapp.data.model.Note;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class NotesDao implements BaseDao<Note> {

    @Query("SELECT * FROM notes_table ORDER BY date DESC")
    public abstract LiveData<List<Note>> getListOfNotes();

    @Query("SELECT * FROM notes_table WHERE id = :notes_id")
    public abstract LiveData<Note> getNote(int notes_id);

    @Query("SELECT * FROM notes_table WHERE date = :date")
    public abstract LiveData<List<Note>> getNotesByDate(Date date);
}