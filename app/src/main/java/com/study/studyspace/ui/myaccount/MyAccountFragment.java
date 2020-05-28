package com.study.studyspace.ui.myaccount;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.study.studyspace.DataHouseViewModel;
import com.study.studyspace.FirebaseOperations;
import com.study.studyspace.Login.LoginActivity;
import com.study.studyspace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyAccountFragment extends Fragment  {


    TextView email,userName;
    Button selectCollegebtn,deleteaccount;
    private FirebaseAuth mAuth;
    FirebaseUser userr;
    FirebaseOperations fbo;
    private DataHouseViewModel viewModel;
     NavController navController;
     FragmentManager fm;
     View v;
    public static MyAccountFragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        fbo = new FirebaseOperations(mAuth);
        userr= mAuth.getCurrentUser();
        View root = inflater.inflate(R.layout.my_account_fragment, container, false);
            email = root.findViewById(R.id.u_email);
            email.setEnabled(false);
            userName = root.findViewById(R.id.u_username);
            userName.setEnabled(false);
            selectCollegebtn = root.findViewById(R.id.u_college);
            deleteaccount = root.findViewById(R.id.u_delete);
        setAttributes();
        return root;
    }

    public void setAttributes()
    {
        email.setText(userr.getEmail());
        userName.setText(userr.getDisplayName());
       // selectCollegebtn.setText(fbo.getCollegeName(getContext()));

       // Log.i("gt", "setAttributes: " + clg );
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         navController = Navigation.findNavController(view);
                  fm=this.getFragmentManager();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(DataHouseViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String string) {
               // editText.setText(charSequence);
                selectCollegebtn.setText(string);
            }
        });
        selectCollegebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navController.getCurrentDestination().getId() == R.id.nav_myaccount) {
                    navController.navigate(R.id.action_nav_myaccount_to_getCollegeName2);
                }else {
                }
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).removeValue();
                                }
                                Intent i1 = new Intent(getActivity(), LoginActivity.class);
                                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i1);
                                Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_LONG).show();
                                getActivity().finish();

                            }
                        });
            }
        });
    }



}
