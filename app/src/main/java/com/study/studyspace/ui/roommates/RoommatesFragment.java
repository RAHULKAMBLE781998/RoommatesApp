package com.study.studyspace.ui.roommates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.study.studyspace.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class RoommatesFragment extends Fragment {
    RmateViewModel rmateViewModel;
    public  TabLayout tabLayout;
     ViewPager viewPager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rmateViewModel = ViewModelProviders.of(this).get(RmateViewModel.class);
        return inflater.inflate(R.layout.fragment_roommates, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         tabLayout = view.findViewById(R.id.tabLayout);
        TabItem tabstudents = view.findViewById(R.id.students);
        TabItem tabmyrequest = view.findViewById(R.id.myrequest);

        RoommatesFragmentAds roommatesFragmentAds;
        PostMyRequestFragment postMyRequestFragment;
        roommatesFragmentAds = rmateViewModel.getfrag1();
        postMyRequestFragment = rmateViewModel.getfrag2();

         viewPager = view.findViewById(R.id.viewpager);
        FragmentManager fm = this.getChildFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(fm,tabLayout.getTabCount(),roommatesFragmentAds,postMyRequestFragment);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
