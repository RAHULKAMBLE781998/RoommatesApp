package com.study.studyspace.ui.myads;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.study.studyspace.Add;
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

public class MyAddsViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private MutableLiveData<String> mText;
    private MutableLiveData<String> collegeid = new MutableLiveData<String>();
    private MutableLiveData<List<Add>> ads = new MutableLiveData<>();
    List<Add> addd;
    Context context;
    public MyAddsViewModel(Application application) {
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

    public void getAdsFromFirebase()
    {

        Log.i("myadswowz" , "getAds running");
       // String clgid = collegeid.getValue();
       // Log.i("dataz" , clgid);
        DatabaseReference myreff = FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(collegeid.getValue()).child("adds");
        myreff.keepSynced(true);
        myreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ads.setValue(new ArrayList<Add>());
                addd = new ArrayList<>();
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();

                while (items.hasNext())
                {

                    DataSnapshot item = items.next();
                    Add adz = new Add();
                    try {

                        //Log.i("dataz", item.child("Title").getValue().toString());
                        adz.setTitle(item.child("Title").getValue().toString());
                        adz.setPrice(item.child("Price").getValue().toString());
                        adz.setAccomodationtype(item.child("AccommodationType").getValue().toString());
                        adz.setAddress(item.child("Address").getValue().toString());
                        adz.setDescription(item.child("Description").getValue().toString());
                        adz.setAccomodationfor(item.child("AccommodationFor").getValue().toString());
                        adz.setAddID(item.child("AddId").getValue().toString());
                        adz.setUploader(item.child("Owner").getValue().toString());
                        adz.setClgid(collegeid.getValue());
                    }catch (Exception e) { e.printStackTrace(); }
                    try {
                        if(item.child("Images").child("Image0").getValue().toString() != null)
                        {
                            adz.setImg1(item.child("Images").child("Image0").getValue().toString());
                            Log.i("okokok", "got it : " + item.child("Images").child("Image0").getValue().toString());
                            Log.i("okokokyyy",adz.getImg1());
                            //Bitmap bmp = imageLoader.loadImageSync(adz.getImg1());
                            // adz.setImg1bitmap(bmp);
                            // new DownLoadImageTask(adz).execute();
                        }   }catch (Exception e) { e.printStackTrace(); }
                    try {
                        if(item.child("Images").child("Image1").getValue().toString() != null)
                        {
                            adz.setImg2(item.child("Images").child("Image1").getValue().toString());
                        }
                    }catch (Exception e) { e.printStackTrace(); }
                    try{
                        if(item.child("Images").child("Image2").getValue().toString() != null)
                        {
                            adz.setImg3(item.child("Images").child("Image2").getValue().toString());
                        }
                    }catch (Exception e) { e.printStackTrace(); }
                    try{
                        if(item.child("Images").child("Image3").getValue().toString() != null)
                        {
                            adz.setImg4(item.child("Images").child("Image3").getValue().toString());
                        }
                    }catch (Exception e) { e.printStackTrace(); }
                    //
                    try{
                    Log.i("uploader",adz.getUploader());
                    Log.i("uploaderuser",mAuth.getCurrentUser().getUid());
                    String up = adz.getUploader();
                    String us = mAuth.getCurrentUser().getUid();
                    if (up.equals(us)) {
                        addd.add(adz);
                    }}catch (Exception e){}
                }
                if(addd.isEmpty())
                {
                    Add a = new Add();
                    a.setTitle("NoAds");
                    addd.add(a);
                }
                ads.setValue(addd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public MutableLiveData<List<Add>> getAds()
    {
        return ads;
    }
    public LiveData<String> getText() {
        return mText;
    }
}