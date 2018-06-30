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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.ui.ViewModelFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {


    public AddFragment() {
    }

    private AddViewModel mViewModel;
    TextInputEditText descriptionEditText, titleEditText;
    TextInputLayout descriptionLayout, titleLayout;
    MaterialButton saveButton, updateButton;
    @Inject
    ViewModelFactory factory;

    private boolean mNoteHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mNoteHasChanged = true;
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        saveButton = view.findViewById(R.id.saveButton);
        updateButton = view.findViewById(R.id.updateButton);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        descriptionLayout = view.findViewById(R.id.descriptionLayout);
        titleEditText = view.findViewById(R.id.titleEditText);
        titleLayout = view.findViewById(R.id.titleLayout);

        descriptionEditText.setOnTouchListener(mTouchListener);
        titleEditText.setOnTouchListener(mTouchListener);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyJournal) getActivity().getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, factory).get(AddViewModel.class);
        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey("noteId")) {
            // TODO : Add an update functionality
            int noteId = bundle.getInt("NOTES_ID");
            mViewModel.getNoteById(noteId).observe(this, noteData -> {
                if (noteData != null) {
                    saveButton.setVisibility(View.GONE);
                    updateButton.setVisibility(View.VISIBLE);
                    titleEditText.setText(noteData.getTitle());
                    descriptionEditText.setText(noteData.getDescription());
                }
            });
        }

        Date date = Calendar.getInstance().getTime();

        saveButton.setOnClickListener(v -> {
            // TODO : Improve the data validation step
            if (titleEditText.getText().length() > 0 && descriptionEditText.getText().length() > 0) {
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                Note note = new Note(title, description, date);
                mViewModel.addNote(note);
                Navigation.findNavController(saveButton).navigate(R.id.backToSourceAction);
            }
        });
    }
}
