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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.Const;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.ui.NavHostActivity;
import com.edoubletech.journalapp.ui.ViewModelFactory;
import com.edoubletech.journalapp.ui.add.AddFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment implements NotesAdapter.NoteClickListener {


    public MainFragment() {
    }

    private MainViewModel mViewModel;
    private NotesAdapter mAdapter = new NotesAdapter(this);
    private TextView mEmptyView;
    private RecyclerView recyclerView;

    @Inject
    ViewModelFactory factory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmptyView = view.findViewById(R.id.emptyView);
        recyclerView = view.findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyJournal) getActivity().getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        mViewModel.getListOfNotes().observe(this, notes -> {
            if (notes != null) {
                if (notes.isEmpty()) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                mAdapter.submitList(notes);
            }
        });
    }

    @Override
    public void OnNoteItemClick(int noteId) {
        Bundle args = new Bundle();
        AddFragment fragment = new AddFragment();
        args.putInt(Const.NOTE_ID_KEY, noteId);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        ((NavHostActivity) getActivity()).bottomNav.setSelectedItemId(R.id.addNote);
    }
}
