package com.study.studyspace.CollegeNameSelector;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.study.studyspace.Colleges;
import com.study.studyspace.R;

import java.util.List;

public class GetCollegeNm extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    View inf;
    Colleges clg;
    List<Colleges> clgnames;
    View v;
    private FragmentAListener listener;
    public interface FragmentAListener {
        void onInputASent(Colleges clg);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        inf = inflater.inflate(R.layout.get_college_name,container,false);
        clgnames = databaseAccess.getColleges();
        adapter = new ListViewAdapter(getContext(), clgnames);
        return inf;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        list =  getView().findViewById(R.id.listvieww);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        editsearch =   getView().findViewById(R.id.search);
        editsearch.setSubmitButtonEnabled(true);
        editsearch.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clg = clgnames.get(position);
        Log.i("selectedd",clg.getName());

        getActivity().getIntent().putExtra("ok", (Parcelable) clg);
        getActivity().getFragmentManager().popBackStack();
        listener.onInputASent(clg);

        this.getFragmentManager().popBackStack();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentAListener) {
            listener = (FragmentAListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
