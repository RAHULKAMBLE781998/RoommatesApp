package com.study.studyspace.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.study.studyspace.MainActivity;
import com.study.studyspace.R;
import com.study.studyspace.ui.forgotDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements forgotDialog.ExampleDialogListener{

    private static final String TAG = "LoginActivity";
    private static final Boolean CHECK_IF_VERIFIED = false;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = findViewById(R.id.progressBar);
        mEmail =  findViewById(R.id.input_email);
        mPassword =  findViewById(R.id.input_password);
        mContext = LoginActivity.this;
        Log.d(TAG, "onCreate: started.");


        mProgressBar.setVisibility(View.GONE);

        setupFirebaseAuth();
        init();

    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

     private void init(){

         Button btnLogin =  findViewById(R.id.btn_login);
         btnLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.d(TAG, "onClick: attempting to log in.");

                 String email = mEmail.getText().toString();
                 String password = mPassword.getText().toString();

                 if(isStringNull(email) && isStringNull(password)){
                     Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                 }else{
                     mProgressBar.setVisibility(View.VISIBLE);


                     mAuth.signInWithEmailAndPassword(email, password)
                             .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {


                                     Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                     FirebaseUser user = mAuth.getCurrentUser();

                                     if (!task.isSuccessful()) {
                                         Log.w(TAG, "signInWithEmail:failed", task.getException());

                                         Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                                 Toast.LENGTH_SHORT).show();
                                         mProgressBar.setVisibility(View.GONE);

                                     }
                                     else{
                                         try{
                                             if(CHECK_IF_VERIFIED){
                                                 if(user.isEmailVerified()){
                                                     Log.d(TAG, "onComplete: success. email is verified.");
                                                     Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                     startActivity(intent);
                                                     finish();
                                                     return;
                                                 }else{
                                                     Toast.makeText(mContext, "Email is not verified \n check your email inbox.", Toast.LENGTH_SHORT).show();
                                                     mProgressBar.setVisibility(View.GONE);

                                                     mAuth.signOut();
                                                 }
                                             }
                                             else{
                                                 Log.d(TAG, "onComplete: success. email is verified.");
                                                 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                 startActivity(intent);
                                                 finish();
                                                 startActivity(intent);
                                             }

                                         }catch (NullPointerException e){
                                             Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage() );
                                         }
                                     }

                                     // ...
                                 }
                             });
                 }

             }
         });
         TextView forgotpass = findViewById(R.id.forgot_password);
         forgotpass.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 forgotDialog forgotDialog = new forgotDialog();
                 forgotDialog.show(getSupportFragmentManager(), "forgot dialog");
             }
         });
         TextView linkSignUp =  findViewById(R.id.link_signup);
         linkSignUp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.d(TAG, "onClick: navigating to register screen");
                 Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                 startActivity(intent);
             }
         });

         if(mAuth.getCurrentUser() != null){
             Intent intent = new Intent(LoginActivity.this, MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(intent);
             finish();

         }
     }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void applyTexts(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Reset link sent to your email address",Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Email sent.");
                        }else{
                            Toast.makeText(getApplicationContext(),"Failed -" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
























