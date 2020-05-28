package com.study.studyspace.ui.roommates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.study.studyspace.R;
import com.study.studyspace.Roommate;

public class PostMyRequestFragment extends Fragment{
    private static final String TAG = "PostMyRequestFragment";
    private EditText username,year,branch,requirements;
    private RadioGroup gender;
    private Button uploadrequest,deleterequest;
    private RoommatesViewModel postMyRequestViewModel;
    private Roommate userad;
    RadioButton malee,femalee;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postMyRequestViewModel = ViewModelProviders.of(this).get(RoommatesViewModel.class);

        return inflater.inflate(R.layout.fragment_postmyrequest, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.usernamer);
        year = view.findViewById(R.id.yearr);
        branch = view.findViewById(R.id.branchr);
        requirements = view.findViewById(R.id.requirementsr);
        gender = view.findViewById(R.id.radiogroup2r);
        uploadrequest = view.findViewById(R.id.btn_postrequest);
        malee = view.findViewById(R.id.maler);
        femalee = view.findViewById(R.id.femaler);
        deleterequest = view.findViewById(R.id.btn_deleterr);
        deleterequest.setVisibility(Button.GONE);
        postMyRequestViewModel.getUserRequest().observe(getViewLifecycleOwner(), new Observer<Roommate>() {
            @Override
            public void onChanged(Roommate roommate) {
                userad = roommate;
                username.setText(roommate.getUsername());
                year.setText(roommate.getYear());
                branch.setText(roommate.getBranch());
                requirements.setText(roommate.getNeeded());
                try{
                if(roommate.getGender().equals("Male"))
                {
                    malee.setChecked(true);
                }else if(roommate.getGender().equals("Female")){
                    femalee.setChecked(true);
                }
                if(!roommate.getRAddID().equals(""))
                {
                    uploadrequest.setText("Update request");
                    deleterequest.setVisibility(Button.VISIBLE);
                }}catch (Exception e){}
            }
        });
        uploadrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String us = username.getText().toString();
                String yr = year.getText().toString();
                String br = branch.getText().toString();
                String req = requirements.getText().toString();
                String gen="";
                switch (gender.getCheckedRadioButtonId())
                {
                    case R.id.maler:
                            gen="Male";
                            break;

                    case R.id.femaler:
                            gen="Female";
                            break;

                    default:
                            gen="";
                            break;
                }
                if(checkInputs(us,yr,br,req,gen)) {
                    if(uploadrequest.getText().equals("Post Request")) {
                        postMyRequestViewModel.uploadRequest(us, yr, br, req, gen);
                    }else{
                        postMyRequestViewModel.updateRequest(us, yr, br, req, gen,userad);
                    }
                    Toast.makeText(getActivity(), "Post Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleterequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMyRequestViewModel.deleterequest(userad);
                deleterequest.setVisibility(Button.GONE);
            }
        });
    }
    private boolean checkInputs(String title, String price, String address , String description , String acctype){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(title.equals("") || price.equals("") || address.equals("") || description.equals("") || acctype.equals("")){
            Toast.makeText(getContext(), "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
