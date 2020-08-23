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

import com.example.notekepper.NoteKeeperProviderContract;
import com.example.notekepper.data.DataManager;
import com.example.notekepper.data.local.NoteKeeperDatabaseContract;
import com.example.notekepper.data.local.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.example.notekepper.data.local.NoteKeeperDatabaseContract.NoteInfoEntry;
import com.example.notekepper.data.local.NoteKeeperOpenHelper;
import com.example.notekepper.model.NoteInfo;
import com.example.notekepper.R;
import com.example.notekepper.ui.note.NoteActivity;

import java.util.List;

import static com.example.notekepper.NoteKeeperProviderContract.*;

public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private NotesViewModel mNotesViewModel;
    private RecyclerView mRecyclerView;
    private View mRoot;
    private NotesRecyclerAdapter mNoteRecyclerAdapter;
    public static final int LOADER_NOTES = 0;
    private NoteKeeperOpenHelper mDBOpenHelper;
    private LinearLayoutManager mNoteLayoutManager;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBOpenHelper.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mNotesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_notes, container, false);
        mDBOpenHelper = new NoteKeeperOpenHelper(getContext());
        mNoteLayoutManager = new LinearLayoutManager(getContext());
        initializeDisplayContent();
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(NotesFragment.LOADER_NOTES, null, this);
    }

    private void initializeDisplayContent() {
        DataManager.loadFromDatabase(mDBOpenHelper);
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_notes);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NotesRecyclerAdapter(getContext(), null);

        displayNote();
    }

    private void displayNote() {
        mRecyclerView.setLayoutManager(mNoteLayoutManager);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
//        CursorLoader loader = null;
//        if (id == LOADER_NOTES) {
//            final String[] noteColumns = {
//                    Notes._ID,
//                    Notes.COLUMN_NOTE_TITLE,
//                    Notes.COLUMN_COURSE_TITLE
//            };
//            final String noteOrderBy = Notes.COLUMN_COURSE_TITLE +
//                    "," + Notes.COLUMN_NOTE_TITLE;
//
//            loader = new CursorLoader(getContext(), Notes.CONTENT_EXPANDED_URI, noteColumns,
//                    null, null, noteOrderBy);
//
//        }
//        return loader;
        return new CursorLoader(getContext()) {
            @Override
            public Cursor loadInBackground() {
                NoteKeeperOpenHelper dbOpenHelper = new NoteKeeperOpenHelper(getContext());
                SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
                final String[] noteColumns = {
                        NoteInfoEntry.COLUMN_NOTE_TITLE,
                        NoteInfoEntry.getQName(NoteInfoEntry._ID),
                        CourseInfoEntry.COLUMN_COURSE_TITLE};

                String tableNameWithJoin = NoteInfoEntry.TABLE_NAME + " JOIN " +
                        CourseInfoEntry.TABLE_NAME + " ON "+
                        NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID)+ " = "+
                        CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID);

                String noteOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE + "," +
                        NoteInfoEntry.COLUMN_NOTE_TITLE;

                return db.query(tableNameWithJoin, noteColumns,
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
        if (loader.getId() == LOADER_NOTES)
            mNoteRecyclerAdapter.changeCursor(null);

    }
}