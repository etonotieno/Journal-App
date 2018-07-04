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
import android.widget.CalendarView;
import android.widget.TextView;

import com.edoubletech.journalapp.MyJournal;
import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.Const;
import com.edoubletech.journalapp.ui.ViewModelFactory;
import com.edoubletech.journalapp.ui.add.AddFragment;
import com.edoubletech.journalapp.ui.main.MainViewModel;
import com.edoubletech.journalapp.ui.main.NotesAdapter;

import java.util.Date;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener, NotesAdapter.NoteClickListener {

    public CalendarFragment() {
    }

    CalendarView calendarView;
    private TextView mEmptyView;
    @Inject
    ViewModelFactory factory;
    MainViewModel mViewModel;
    RecyclerView mRecyclerView;
    private NotesAdapter adapter = new NotesAdapter(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        mRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        mEmptyView = view.findViewById(R.id.emptyViewCalendar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        calendarView.setOnDateChangeListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyJournal) getActivity().getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
        Date date = new Date(calendarView.getDate());
        mViewModel.getListOfNotesByDate(date).observe(this, notes -> {
            if (notes != null) {
                if (notes.isEmpty()) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                adapter.submitList(notes);
            }
        });
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
