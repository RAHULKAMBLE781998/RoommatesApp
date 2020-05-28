package com.study.studyspace;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseOperations  {
    private static final String TAG = "FirebaseOperations";
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

   // private MutableLiveData<String> mText;

    public FirebaseOperations(FirebaseAuth mAuth)
    {
        this.mAuth=mAuth;
        user= mAuth.getCurrentUser();
    }
    public void uploadUserData(String name , String email , int collegeid , String gender )
    {
        Log.d(TAG, "uploadingUserData ");
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Name").setValue(name);
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Email").setValue(email);
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("College").setValue(collegeid);
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Gender").setValue(gender);
    }

    public  void updateCollege(int collegeid, final Context context)
    {
        Task<Void> xyz = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("College").setValue(collegeid);
            xyz.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(context, "College Changed", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
