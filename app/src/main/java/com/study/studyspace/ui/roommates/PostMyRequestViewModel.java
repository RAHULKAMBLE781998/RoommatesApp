package com.study.studyspace.ui.roommates;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.study.studyspace.Roommate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostMyRequestViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private MutableLiveData<String> collegeid = new MutableLiveData<String>();
    private MutableLiveData<List<Roommate>> ads = new MutableLiveData<>();
    public PostMyRequestViewModel(@NonNull Application application) {
        super(application);
        getclg();
    }
    private void getclg() {
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("College");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                collegeid.setValue(value);
               // getAdsFromFirebase();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
