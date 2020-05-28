package com.study.studyspace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import com.study.studyspace.CollegeNameSelector.GetCollegeName;
import com.study.studyspace.Login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity  implements GetCollegeName.FragmentAListener , FeedBackDialog.ExampleDialogListener , HelpDialog.ExampleDialogListener{

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DataHouseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid());
        FirebaseMessaging.getInstance().subscribeToTopic("globalnotifications");
        setupFirebaseAuth();
        //

        ///
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_chat,R.id.nav_postad,R.id.nav_myAds, R.id.nav_roommates)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        if(menuItem.getItemId() == R.id.nav_logout)
                        {
                            navigationView.setCheckedItem(R.id.nav_home);
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(mAuth.getCurrentUser().getUid());
                            logout();

                        }
                        if(menuItem.getItemId() == R.id.nav_feedback)
                        {
                            openDialog();
                            drawer.closeDrawers();
                        }
                        if(menuItem.getItemId() == R.id.nav_help)
                        {
                            openhelpDialog();
                            drawer.closeDrawers();
                        }
                       // Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();

                        // You need this line to handle the navigation
                        boolean handled = NavigationUI.onNavDestinationSelected(menuItem, navController);
                        if (handled) {
                            ViewParent parent = navigationView.getParent();
                            if (parent instanceof DrawerLayout) {
                                ((DrawerLayout) parent).closeDrawer(navigationView);
                            }
                        }

                        return handled;
                    }
                });
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.headerText);
        navUsername.setText(mAuth.getCurrentUser().getDisplayName());
        TextView navUserEmail =  headerView.findViewById(R.id.headerEmail);
        navUserEmail.setText(mAuth.getCurrentUser().getEmail());
        Log.d(TAG, "onCreate: "+mAuth.getCurrentUser().getDisplayName());

        //////////////////////////////////////////////////ViewModelCode
       viewModel = ViewModelProviders.of(this).get(DataHouseViewModel.class);
        viewModel.getText().observe(this, new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {
                // editText.setText(charSequence);
            }
        });


    }

    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        Intent i1 = new Intent(MainActivity.this, LoginActivity.class);
        i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i1);
        Toast.makeText(MainActivity.this, "Logout Successfully!", Toast.LENGTH_LONG).show();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                if (user != null) {
                    // User is signed in

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    finish();
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
    public void onInputASent(Colleges clg) {
        viewModel.setText(clg.getName());
        viewModel.setCollege(clg);
    }
    public void openDialog() {
        FeedBackDialog feedBackDialog = new FeedBackDialog();
        feedBackDialog.show(getSupportFragmentManager(), "dialog");
    }
    public void openhelpDialog() {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.show(getSupportFragmentManager(), "dialog");
    }
    @Override
    public void applyTexts(String feedback) {
        //upload feedback to firebase
        DatabaseReference myreff = FirebaseDatabase.getInstance().getReference().child("feedBack");
        String key = myreff.getKey();
        myreff.child(key).child("feedback").setValue(feedback);
        myreff.child(key).child("user").setValue(mAuth.getCurrentUser().getEmail());
        //toastmsg
        Toast.makeText(this,"Feedback uploaded",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
