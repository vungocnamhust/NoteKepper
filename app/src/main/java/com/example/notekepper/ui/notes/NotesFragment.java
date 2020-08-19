package com.example.notekepper.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekepper.data.DataManager;
import com.example.notekepper.model.NoteInfo;
import com.example.notekepper.R;

import java.util.List;

public class NotesFragment extends Fragment {

    private NotesViewModel mNotesViewModel;
    private RecyclerView mRecyclerView;
    private View mRoot;
    private NotesRecyclerAdapter mNoteRecyclerAdapter;

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
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent(LinearLayoutManager noteLayoutManager) {
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_notes);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NotesRecyclerAdapter(getContext(), notes);
//        Display note
        mRecyclerView.setLayoutManager(noteLayoutManager);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }
}