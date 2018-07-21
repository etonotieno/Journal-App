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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;

import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.Const;
import com.edoubletech.journalapp.data.model.Note;
import com.edoubletech.journalapp.ui.BaseActivity;
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

public class AddFragment extends Fragment {


    public AddFragment() {
    }

    AddViewModel mViewModel;
    MultiAutoCompleteTextView descriptionEditText;
    TextInputEditText titleEditText;
    FloatingActionButton saveFab;
    private Note mNote;
    private Note mSavedNote;
    private int mNoteId;
    private String mAction;
    public static final String CREATE_ACTION = "create";
    public static final String EDIT_ACTION = "edit";
    public static final String DELETE_ACTION = "delete";
    @Inject
    ViewModelFactory factory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        saveFab = view.findViewById(R.id.saveFab);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        titleEditText = view.findViewById(R.id.titleEditText);

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mNote.setTitle(editable.toString());
            }
        });

        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mNote.setDescription(editable.toString());
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNote != null) {
            if (mAction.equals(CREATE_ACTION)) {
                // Insert a note
                mViewModel.addNote(mNote);
            } else {
                // Update Note
                if (!mNote.equals(mSavedNote)) mViewModel.updateNote(mNote);
            }
        }
    }

    private void populateUi() {
        if (mNote != null) {
            titleEditText.setText(mNote.getTitle());
            descriptionEditText.setText(mNote.getDescription());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyJournal) getActivity().getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, factory).get(AddViewModel.class);

        Date date = Calendar.getInstance().getTime();

        Bundle bundle = getArguments();

        mAction = (bundle == null) ? CREATE_ACTION : EDIT_ACTION;

        if (bundle != null && bundle.containsKey(Const.NOTE_ID_KEY)) {
            mNoteId = bundle.getInt(Const.NOTE_ID_KEY);

            mViewModel.getNoteById(mNoteId).observe(this, note -> {
                if (note == null) mNote = new Note();
                else {
                    mNote = note;
                    if (mAction.equals(EDIT_ACTION)) mSavedNote = new Note(note);
                }
                populateUi();
            });
        }

        saveFab.setOnClickListener(v -> {
            if (titleEditText.getText().length() > 0 && descriptionEditText.getText().length() > 0) {
                mNote.setDate(Calendar.getInstance().getTime());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MainFragment())
                        .commit();

                ((BaseActivity) getActivity()).bottomNav.setSelectedItemId(R.id.home_button);
            }
        });
    }
}
