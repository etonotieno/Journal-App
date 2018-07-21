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

import android.os.Bundle;
import android.widget.TextView;

import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.ui.BaseActivity;
import com.edoubletech.journalapp.ui.ViewModelFactory;

import javax.inject.Inject;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity implements NotesAdapter.NoteClickListener {

    private MainViewModel mViewModel;
    private NotesAdapter mAdapter = new NotesAdapter(this);
    private TextView mEmptyView;
    private RecyclerView recyclerView;

    @Inject
    ViewModelFactory factory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEmptyView = findViewById(R.id.emptyView);
        recyclerView = findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Note note = (Note) viewHolder.itemView.getTag();
                mViewModel.deleteNote(note);
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void OnNoteItemClick(int noteId) {

    }
}
