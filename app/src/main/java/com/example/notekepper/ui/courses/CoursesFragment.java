package com.example.notekepper.ui.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekepper.R;
import com.example.notekepper.data.DataManager;
import com.example.notekepper.model.CourseInfo;
import com.example.notekepper.ui.notes.NotesRecyclerAdapter;

import java.util.List;

public class CoursesFragment extends Fragment {

    private CoursesViewModel mCoursesViewModel;
    private RecyclerView mRecyclerView;
    private View mRoot;
    private CoursesRecyclerAdapter mCoursesRecyclerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCoursesViewModel =
                ViewModelProviders.of(this).get(CoursesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_courses, container, false);

        initDisplayContent(new GridLayoutManager(getContext(), 2));
        return mRoot;
    }
    @Override
    public void onResume() {
        super.onResume();
        mCoursesRecyclerAdapter.notifyDataSetChanged();
    }

    private void initDisplayContent(GridLayoutManager gridLayoutManager) {
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        mCoursesRecyclerAdapter = new CoursesRecyclerAdapter(getContext(), courses);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mCoursesRecyclerAdapter);
    }
}

