package com.study.studyspace.ui.roommates;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.study.studyspace.Roommate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoommatesViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private MutableLiveData<String> mText;
    DatabaseReference dr=null;
    private MutableLiveData<String> collegeid = new MutableLiveData<String>();
    private MutableLiveData<List<Roommate>> ads = new MutableLiveData<>();
    private MutableLiveData<Roommate> userrequest = new MutableLiveData<>();
    List<Roommate> addd;
    Context context;
    public RoommatesViewModel(Application application) {
        super(application);
         context = application.getApplicationContext();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        getclg();

    }

    private void getclg() {
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("College");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                collegeid.setValue(value);
                getAdsFromFirebase();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void uploadRequest(String username,String year,String branch,String requirements,String gender)
    {
        dr= FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(collegeid.getValue()).child("Radds");
        String key = dr.push().getKey();
        dr= dr.child(key);
        dr.child("username").setValue(username);
        dr.child("year").setValue(year);
        dr.child("branch").setValue(branch);
        dr.child("requirements").setValue(requirements);
        dr.child("gender").setValue(gender);
        dr.child("raddid").setValue(key);
        dr.child("clgid").setValue(collegeid.getValue());
        dr.child("userid").setValue(user.getUid());
    }
    public void getAdsFromFirebase()
    {

        Log.i("wowz" , "getAds running");
       // String clgid = collegeid.getValue();
       // Log.i("dataz" , clgid);
        DatabaseReference myreff = FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(collegeid.getValue()).child("Radds");
        myreff.keepSynced(true);
        myreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ads.setValue(new ArrayList<Roommate>());
                userrequest.setValue(new Roommate());
                addd = new ArrayList<>();
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();

                while (items.hasNext())
                {

                    DataSnapshot item = items.next();
                    Roommate adz = new Roommate();
                    try {
                        adz.setUsername(item.child("username").getValue().toString());
                        adz.setUserID(item.child("userid").getValue().toString());
                        adz.setBranch(item.child("branch").getValue().toString());
                        adz.setYear(item.child("year").getValue().toString());
                        adz.setRAddID(item.child("raddid").getValue().toString());
                        adz.setCollegeid(item.child("clgid").getValue().toString());
                        adz.setNeeded(item.child("requirements").getValue().toString());
                        adz.setGender(item.child("gender").getValue().toString());
                    }catch (Exception e) { e.printStackTrace(); }
                    addd.add(adz);
                    try{
                    if(adz.getUserID().equals(user.getUid()))
                    { userrequest.setValue(adz); }}catch (Exception e){}
                }
                if(addd.isEmpty())
                {
                    Roommate a = new Roommate();
                    a.setUsername("NoAds");
                    addd.add(a);
                }
                ads.setValue(addd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public MutableLiveData<List<Roommate>> getAds()
    {

        return ads;
    }
    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<Roommate> getUserRequest() {
        return userrequest;
    }

    public void updateRequest(String us, String yr, String br, String req, String gen,Roommate r) {
        dr= FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(collegeid.getValue()).child("Radds");
        String key = r.getRAddID();
        dr= dr.child(key);
        dr.child("username").setValue(us);
        dr.child("year").setValue(yr);
        dr.child("branch").setValue(br);
        dr.child("requirements").setValue(req);
        dr.child("gender").setValue(gen);

    }
    public void deleterequest(Roommate add) {
        dr= FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(add.getCollegeid()).child("Radds");
        String key = add.getRAddID();
        dr= dr.child(key);
        dr.removeValue();

    }
}