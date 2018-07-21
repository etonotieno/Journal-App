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

package com.edoubletech.journalapp.data.model;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name = "note_tile")
    private String mTitle;
    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "date")
    private Date mDate;


    public Note(String title, String description, Date date) {
        mDate = date;
        mDescription = description;
        mTitle = title;
    }

    @Ignore
    public Note(Note note) {
        this.id = note.getId();
        this.mTitle = note.getTitle();
        this.mDescription = note.getDescription();
        this.mDate = note.getDate();
    }

    @Ignore
    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }


    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj instanceof Note) {
            Note note = (Note) obj;
            return note.getDescription().equals(mDescription) ||
                    note.getTitle().equals(mTitle);
        }
        return false;
    }
}
