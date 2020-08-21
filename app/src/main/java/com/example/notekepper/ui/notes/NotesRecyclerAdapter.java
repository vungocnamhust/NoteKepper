package com.example.notekepper.ui.notes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekepper.R;
import com.example.notekepper.model.NoteInfo;
import com.example.notekepper.ui.note.NoteActivity;

import static android.provider.BaseColumns._ID;
import static com.example.notekepper.data.local.NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID;
import static com.example.notekepper.data.local.NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TEXT;
import static com.example.notekepper.data.local.NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private Cursor mCursor;
    private final LayoutInflater mLayoutInflater;
    private int mCourseIdPos;
    private int mNoteTitlePos;
    private int mIdPos;

    public NotesRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(mContext);
        populateColumnPosition();
    }

    private void populateColumnPosition() {
        if (mCursor == null) return;
        mCourseIdPos = mCursor.getColumnIndex(COLUMN_COURSE_ID);
        mNoteTitlePos = mCursor.getColumnIndex(COLUMN_NOTE_TITLE);
        mIdPos = mCursor.getColumnIndex(_ID);
    }

    public void changeCursor(Cursor cursor) {
        if (mCursor != null) mCursor.close();
        mCursor = cursor;
        populateColumnPosition();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String noteTitle = mCursor.getString(mNoteTitlePos);
        String courseId = mCursor.getString(mCourseIdPos);
        int mId = mCursor.getInt(mIdPos);


        holder.mTextCourse.setText(courseId);
        holder.mTextTitle.setText(noteTitle);
        holder.mId = mId;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public final TextView mTextTitle;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("CURRENT_POSITION: ", String.valueOf(mId));
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
