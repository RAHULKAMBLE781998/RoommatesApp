package com.study.studyspace.ui.myads;

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

import com.study.studyspace.Add;
import com.study.studyspace.R;

import java.util.List;

public class MyAdsFragment extends Fragment {

    private MyAddsViewModel dataHouseViewModel;
    NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dataHouseViewModel = ViewModelProviders.of(getActivity()).get(MyAddsViewModel.class);

        return inflater.inflate(R.layout.fragment_my_ads, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_viewww);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setHasFixedSize(true);
            final MyAddsAdapter adapter = new MyAddsAdapter();
            recyclerView.setAdapter(adapter);
        try {
            dataHouseViewModel.getAds().observe(getViewLifecycleOwner(), new Observer<List<Add>>() {
                @Override
                public void onChanged(List<Add> adds) {
                    Log.i("myads","observing");
                    adapter.setAds(adds);

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    adapter.setOnItemClickListener(new MyAddsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Add add) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("Add", add);
            if(!add.getTitle().equals("NoAds")) {
                if (navController.getCurrentDestination().getId() == R.id.nav_myAds) {
                    navController.navigate(R.id.action_nav_myAds_to_nav_ViewMyAddFragment, bundle);
                }
            }

        }
    });
    }

}

