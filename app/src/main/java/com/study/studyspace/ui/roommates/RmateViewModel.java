package com.study.studyspace.ui.roommates;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class RmateViewModel extends AndroidViewModel {
    RoommatesFragmentAds roommatesFragmentAds = new RoommatesFragmentAds();
    PostMyRequestFragment postMyRequestFragment = new PostMyRequestFragment();
    public RmateViewModel(@NonNull Application application) {
        super(application);

    }

    public RoommatesFragmentAds getfrag1() {
        return roommatesFragmentAds;
    }

    public PostMyRequestFragment getfrag2() {
        return postMyRequestFragment;
    }
}
