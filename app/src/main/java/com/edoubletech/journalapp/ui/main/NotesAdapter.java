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

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edoubletech.journalapp.R;
import com.edoubletech.journalapp.data.model.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

public class NotesAdapter extends ListAdapter<Note, NotesAdapter.NotesViewHolder> {

    private static DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem == newItem;
        }
    };
    private final NoteClickListener listener;

    public NotesAdapter(NoteClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note currentNote = getItem(position);

        String title = currentNote.getTitle();
        holder.titleTextView.setText(title);
        String first = String.valueOf(title.charAt(0));
        holder.nameTextView.setText(first);

        Date date = currentNote.getDate();
        GradientDrawable drawable = (GradientDrawable) holder.nameTextView.getBackground();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int backgroundColor = getBackgroundColor(dayOfTheWeek);
        drawable.setColor(backgroundColor);

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        holder.dateTextView.setText(formattedDate);


        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String formattedTime = timeFormat.format(date);
        holder.timeTextView.setText(formattedTime);
        holder.itemView.setTag(currentNote);
    }

    private int getBackgroundColor(int dayOfWeek) {
        int backgroundColor;
        switch (dayOfWeek) {
            case SUNDAY:
                backgroundColor = R.color.sunday;
                break;
            case MONDAY:
                backgroundColor = R.color.monday;
                break;
            case TUESDAY:
                backgroundColor = R.color.tuesday;
                break;
            case WEDNESDAY:
                backgroundColor = R.color.wednesday;
                break;
            case THURSDAY:
                backgroundColor = R.color.thursday;
                break;
            case FRIDAY:
                backgroundColor = R.color.friday;
                break;
            case SATURDAY:
                backgroundColor = R.color.saturday;
                break;
            default:
                backgroundColor = R.color.colorAccent;
                break;
        }
        return backgroundColor;
    }

    public interface NoteClickListener {

        void OnNoteItemClick(int noteId);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateTextView, titleTextView, timeTextView, nameTextView;

        NotesViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            titleTextView = itemView.findViewById(R.id.noteTitleTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            nameTextView = itemView.findViewById(R.id.nameCircleTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Note note = (Note) itemView.getTag();
            listener.OnNoteItemClick(note.getId());
        }
    }
}
