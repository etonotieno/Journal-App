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

package com.edoubletech.journalapp.ui.add;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;

import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.ui.ViewModelFactory;
import com.edoubletech.journalapp.ui.main.MainFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {


    public AddFragment() {
    }

    private AddViewModel mViewModel;
    MultiAutoCompleteTextView descriptionEditText;
    TextInputEditText titleEditText;
    FloatingActionButton saveFab;
    @Inject
    ViewModelFactory factory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        saveFab = view.findViewById(R.id.saveFab);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        titleEditText = view.findViewById(R.id.titleEditText);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyJournal) getActivity().getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, factory).get(AddViewModel.class);
        Bundle bundle = getArguments();

        if (bundle != null) {
            int noteId = bundle.getInt("NOTE_ID");
            mViewModel.getNoteById(noteId).observe(this, noteData -> {
                if (noteData != null) {
                    titleEditText.setText(noteData.getTitle());
                    descriptionEditText.setText(noteData.getDescription());
                }
            });
        }

        Date date = Calendar.getInstance().getTime();

        saveFab.setOnClickListener(v -> {
            if (titleEditText.getText().length() > 0 && descriptionEditText.getText().length() > 0) {
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                Note note = new Note(title, description, date);
                mViewModel.addNote(note);

                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragment_container, new MainFragment())
                        .commit();
            }
        });
    }
}
