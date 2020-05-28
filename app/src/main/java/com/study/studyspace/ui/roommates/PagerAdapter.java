package com.study.studyspace.ui.roommates;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numoftabs;
    RoommatesFragmentAds roommatesFragmentAds;
    PostMyRequestFragment postMyRequestFragment;
    public PagerAdapter(@NonNull FragmentManager fm, int numoftabss,RoommatesFragmentAds rfa , PostMyRequestFragment prf) {
        super(fm);
        this.numoftabs =  numoftabss;
        roommatesFragmentAds =rfa;
        postMyRequestFragment = prf;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return roommatesFragmentAds;
            case 1:
                return postMyRequestFragment;
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Students looking for accomodation";
            case 1:
                return "My Request";
        }
        return null;
    }
}
