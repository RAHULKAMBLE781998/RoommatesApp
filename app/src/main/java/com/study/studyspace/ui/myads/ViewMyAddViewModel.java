package com.study.studyspace.ui.myads;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.study.studyspace.Add;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ViewMyAddViewModel extends AndroidViewModel {
    private static final String TAG = "ViewMyAddViewModel";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference dr=null;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference  = storage.getReference();
    private List<Uri> imgstorageref = new ArrayList<>();
    public ViewMyAddViewModel(@NonNull Application application) {
        super(application);
    }

    public void updateadd(String titleS, String priceS, String addressS, String descriptions, String acctypes, String gen, List<Uri> imguris ,String clgid,String keyy, Add add) {

        if(imguris.isEmpty())
        {
            Log.i(TAG ,"upLoadAd: started...");
            dr= FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(clgid).child("adds");
            String key = keyy;
            dr= dr.child(key);

                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("adds").child("add").setValue(key);

            dr.child("Title").setValue(titleS);
            dr.child("Price").setValue(priceS);
            dr.child("Address").setValue(addressS);
            dr.child("Description").setValue(descriptions);
            dr.child("AccommodationType").setValue(acctypes);
            dr.child("AccommodationFor").setValue(gen);
            //deleteold(dr);

            return ;

        }else{
            Log.i(TAG ,"upLoadAd: started...");
            dr= FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(clgid).child("adds");
            String key = keyy;
            dr= dr.child(key);

            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("adds").child("add").setValue(key);

            dr.child("Title").setValue(titleS);
            dr.child("Price").setValue(priceS);
            dr.child("Address").setValue(addressS);
            dr.child("Description").setValue(descriptions);
            dr.child("AccommodationType").setValue(acctypes);
            dr.child("AccommodationFor").setValue(gen);
            ////deleteold(dr);

            try{deleteold(add.getImg1());}catch(Exception e){}
            try{deleteold(add.getImg2());}catch(Exception e){}
            try{deleteold(add.getImg3());}catch(Exception e){}
            try{deleteold(add.getImg4());}catch(Exception e){}
            upImg(imguris);

        }
    }
    private void upImg(List<Uri> filePathlist) {
        new uploadImageAsyncTask().execute(filePathlist);
    }
    private void deleteold(String uri)
    {
        StorageReference photoRef = storage.getReferenceFromUrl(uri);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "onFailure: did not delete file");
            }
        });
    }

    public void deleteadd(Add add) {
        dr= FirebaseDatabase.getInstance().getReference().child("1MdqimzS8Fn__yk500Lx1B9xyS64Bxyzbk4Fh204y-xg").child("Sheet1").child(add.getClgid()).child("adds");
        String key = add.getAddID();
        dr= dr.child(key);
        try{deleteold(add.getImg1());}catch(Exception e){}
        try{deleteold(add.getImg2());}catch(Exception e){}
        try{deleteold(add.getImg3());}catch(Exception e){}
        try{deleteold(add.getImg4());}catch(Exception e){}
        dr.removeValue();

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
                                                Toast.makeText(getApplication().getApplicationContext(), "Add Updated successfully", Toast.LENGTH_SHORT).show();
                                                //navigate
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
        }}
}

