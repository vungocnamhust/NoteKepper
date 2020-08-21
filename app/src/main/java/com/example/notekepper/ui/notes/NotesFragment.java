package com.example.notekepper.ui.notes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekepper.data.DataManager;
import com.example.notekepper.data.local.NoteKeeperDatabaseContract;
import com.example.notekepper.data.local.NoteKeeperOpenHelper;
import com.example.notekepper.model.NoteInfo;
import com.example.notekepper.R;
import com.example.notekepper.ui.note.NoteActivity;

import java.util.List;

public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private NotesViewModel mNotesViewModel;
    private RecyclerView mRecyclerView;
    private View mRoot;
    private NotesRecyclerAdapter mNoteRecyclerAdapter;
    public static final int LOADER_NOTES = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mNotesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_notes, container, false);
        initializeDisplayContent(new LinearLayoutManager(getContext()));
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(NotesFragment.LOADER_NOTES, null, this);
    }

    private void initializeDisplayContent(LinearLayoutManager noteLayoutManager) {
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_notes);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NotesRecyclerAdapter(getContext(), null);
//        Display note
        mRecyclerView.setLayoutManager(noteLayoutManager);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext()) {
            @Override
            public Cursor loadInBackground() {
                NoteKeeperOpenHelper dbOpenHelper = new NoteKeeperOpenHelper(getContext());
                SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
                final String[] noteColumns = {
                        NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE,
                        NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TEXT,
                        NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID,
                        NoteKeeperDatabaseContract.NoteInfoEntry._ID};
                String noteOrderBy = NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID + "," + NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE;
                return db.query(NoteKeeperDatabaseContract.NoteInfoEntry.TABLE_NAME, noteColumns,
                        null, null, null, null, noteOrderBy);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_NOTES) {
            mNoteRecyclerAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNoteRecyclerAdapter.changeCursor(null);

    }
}