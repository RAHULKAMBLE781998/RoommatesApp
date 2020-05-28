package com.study.studyspace;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.study.studyspace.CollegeNameSelector.DatabaseAccess;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DataHouseViewModel extends AndroidViewModel {
    private static final String TAG = "DataHouseViewModel";
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String userCollegeName;
    private MutableLiveData<String> collegeName = new MutableLiveData<>();
    private MutableLiveData<Colleges> college = new MutableLiveData<>();
    private MutableLiveData<String> collegeid = new MutableLiveData<String>();
    private FirebaseOperations fbo = new FirebaseOperations(mAuth);
    private MutableLiveData<List<Add>> ads = new MutableLiveData<>();
    private ProgressBar pgbar;
    private NavController navv;
    List<Add> addd = new ArrayList<>();
    DatabaseReference dr=null;
    String acc;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<Uri> imgstorageref = new ArrayList<>();
    public DataHouseViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        getCollegeName();
        getaddCount();

    }

    private void getaddCount() {
        Log.i(TAG, "getaddCount: running");
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        //
        //DatabaseReference newPostRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("addds");
        //String key = newPostRef.push().getKey();
       // newPostRef.child(key).setValue("okay");
        //
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                     acc = (String) dataSnapshot.child("addCount").getValue();
                    Log.i("adct", "onDataChange ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try
                {
                    if (acc == null) {
                        Log.i("adctt", "ad does not exist ");
                    }else{
                        Log.i("adctt", "ad  exist ");
                    }
                } catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean uploadAd(String title, String price, String address, String description, String acctype, String gen, List<Uri> img, ProgressBar progressBar, NavController navController)
    {
        pgbar=progressBar;
        navv=navController;
        Log.i(TAG ,"upLoadAd: started...");
        String clgid = collegeid.getValue();
         dr= FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(clgid).child("adds");
        String key = dr.push().getKey();
        dr= dr.child(key);
        if(acc == null)
        {
            //its a first add
            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("adds").child("add1").setValue(key);
            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("addCount").setValue("1");
            Log.i("yy", "first time running ");
        }else {
            int addCount = Integer.valueOf(acc) + 1;
            String adcn = String.valueOf(addCount);
            Log.i("addcount", String.valueOf(addCount));
            Log.i("addcount", "adcn" + adcn);
            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("adds").child("add"+adcn).setValue(key);
            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("addCount").setValue(adcn);
            Log.i("yy", "2nd time running ");
        }
        dr.child("Title").setValue(title);
        dr.child("Price").setValue(price);
        dr.child("Address").setValue(address);
        dr.child("Description").setValue(description);
        dr.child("AccommodationType").setValue(acctype);
        dr.child("AccommodationFor").setValue(gen);
        dr.child("AddId").setValue(key);
        dr.child("Owner").setValue(user.getUid());
        dr.child("Ownername").setValue(user.getDisplayName());
        //deleteold(dr);
        upImg(img);
        addd.clear();
        ads.setValue(addd);
        return true;
    }
    public void upImg(List<Uri> filePathlist) {
        new uploadImageAsyncTask().execute(filePathlist);
    }




    private class uploadImageAsyncTask extends AsyncTask<List<Uri>, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("img", "post exe: ");

        }

        @Override
        protected Void doInBackground(List<Uri>... fp) {
            final List<Uri> filePathlist = fp[0];
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final int[] k = {0};
            for(int i =0 ; i< filePathlist.size() ; i++)
            {
                if (filePathlist.get(i) != null) {
                    Uri filePath = filePathlist.get(i);
                    final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                    ref.putFile(filePath)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    //imgstorageref.add(task.getStorage().getDownloadUrl().getResult());
                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            imgstorageref.add(task.getResult());
                                            k[0]++;
                                            if(k[0]== filePathlist.size())
                                            {
                                                for(int i = 0 ; i< imgstorageref.size() ; i++)
                                                {
                                                    Log.i("img", "adding2: ");
                                                    String imgnum = String.valueOf(i);
                                                    dr.child("Images").child("Image" +imgnum).setValue(imgstorageref.get(i).toString());

                                                }
                                                Toast.makeText(getApplication().getApplicationContext(), "Add Uploaded successfully", Toast.LENGTH_SHORT).show();
                                                pgbar.setVisibility(View.GONE);
                                                navv.navigateUp();
                                            }

                                            Log.i("img", "adding: ");
                                        }
                                    });
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getApplication().getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }

            return null;
        }
    }
    private void getCollegeName() {
        final List<Colleges> clgnames;
        final String[] userclgname = new String[1];

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        clgnames = databaseAccess.getColleges();

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("College");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String value = dataSnapshot.getValue().toString();
                    collegeid.setValue(value);
                    userclgname[0] = clgnames.get(Integer.parseInt(value) - 1).getName();
                    userCollegeName = userclgname[0];
                    Log.i("New User", "College name=" + userclgname[0]);
                    collegeName.setValue(userCollegeName);
                    getAdsFromFirebase();
                }catch (Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public LiveData<String> getText() {
        return collegeName;
    }
    public void setCollege(Colleges clg) {
        college.setValue(clg);
        collegeid.setValue(String.valueOf(clg.getId()));
        fbo.updateCollege(Integer.valueOf(collegeid.getValue()) ,context);
    }
    public void setText(String input) {
        collegeName.setValue(input); }




        public void getAdsFromFirebase()
        {       addd.clear();

            Log.i("wowz" , "getAds running");
            String clgid = collegeid.getValue();
            Log.i("dataz" , clgid);
            DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(clgid).child("adds");

            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();

                    while (items.hasNext())
                    {
                        Add adz = new Add();
                        DataSnapshot item = items.next();
                            try {

                                //Log.i("dataz", item.child("Title").getValue().toString());
                                adz.setTitle(item.child("Title").getValue().toString());
                                adz.setPrice(item.child("Price").getValue().toString());
                                adz.setAccomodationtype(item.child("AccommodationType").getValue().toString());
                                adz.setAddress(item.child("Address").getValue().toString());
                                adz.setDescription(item.child("Description").getValue().toString());
                                adz.setAccomodationfor(item.child("AccommodationFor").getValue().toString());
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


                        addd.add(adz);
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

}
