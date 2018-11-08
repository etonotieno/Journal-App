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

package com.edoubletech.journalapp.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.Const;
import com.edoubletech.journalapp.data.MainRepository;
import com.edoubletech.journalapp.ui.add.AddFragment;
import com.edoubletech.journalapp.ui.main.NotesAdapter;

import java.util.Date;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedFragment extends Fragment implements NotesAdapter.NoteClickListener {

    private NotesAdapter adapter = new NotesAdapter(this);

    @Inject
    MainRepository mRepo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyJournal) getActivity().getApplication()).getAppComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_selected, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.selectedRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        Bundle args = getArguments();
        if (args != null) {
            long dateInMs = args.getLong(Const.DATE_LONG_KEY);
            Date selectedDate = new Date(dateInMs);
            mRepo.getListOfNotesByDate(selectedDate).observe(this, notes -> {
                if (notes != null) {
                    adapter.submitList(notes);
                }
            });
        }
        return view;
    }

    @Override
    public void OnNoteItemClick(int noteId) {
        Bundle args = new Bundle();
        args.putInt(Const.NOTE_ID_KEY, noteId);
        AddFragment fragment = new AddFragment();
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}