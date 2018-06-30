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

import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.ui.ViewModelFactory;
import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    private MainViewModel mViewModel;
    private NotesAdapter mAdapter = new NotesAdapter();

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

        MaterialButton button = view.findViewById(R.id.addNoteButton);
        RecyclerView recyclerView = view.findViewById(R.id.notesRecyclerView);
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

        button.setOnClickListener(v ->
                Navigation.findNavController(button).navigate(R.id.mainToAddAction));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyJournal) getActivity().getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        mViewModel.getListOfNotes().observe(this, notes -> {
            if (notes != null) mAdapter.submitList(notes);
        });
    }

}
