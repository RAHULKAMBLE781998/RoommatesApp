package com.study.studyspace.ui.roommates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.studyspace.R;
import com.study.studyspace.Roommate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RoommatesFragmentAds extends Fragment {

    private RoommatesViewModel roommatesViewModel;
    NavController navController;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        roommatesViewModel = ViewModelProviders.of(getActivity()).get(RoommatesViewModel.class);

        return inflater.inflate(R.layout.fragment_roommatesads, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_viewroommates);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setHasFixedSize(true);
            final RAddsAdapter adapter = new RAddsAdapter();
            recyclerView.setAdapter(adapter);
        try {
            roommatesViewModel.getAds().observe(getViewLifecycleOwner(), new Observer<List<Roommate>>() {
                @Override
                public void onChanged(List<Roommate> adds) {
                    Log.i("home","observing");
                    adapter.setAds(adds);

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    adapter.setOnItemClickListener(new RAddsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Roommate add) {
           /* Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.ADD,add);
            startActivityForResult(intent, EDIT_NOTE_REQUEST);
            */
            if(!add.getUsername().equals("NoAds")) {
           if(!add.getUserID().equals(user.getUid())) {
               Bundle bundle = new Bundle();
               bundle.putSerializable("user2fromroommate", add);
               bundle.putString("caller", "roommate");

                   navController.getGraph().findNode(R.id.chatFragment).setLabel(add.getUsername());
                   if (navController.getCurrentDestination().getId() == R.id.nav_roommates) {
                       navController.navigate(R.id.action_nav_roommates_to_chatFragment, bundle);
                   }
               }
           }
        }
    });
    }

}

