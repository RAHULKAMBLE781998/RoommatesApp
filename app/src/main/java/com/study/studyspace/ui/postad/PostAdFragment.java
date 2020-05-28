package com.study.studyspace.ui.postad;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.studyspace.DataHouseViewModel;
import com.study.studyspace.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PostAdFragment extends Fragment {
    private static final String TAG = "PostAdFragment";
    private PostAdViewModel mViewModel;
    private Button selectimages,postad;
    private TextView txt;
    private EditText title,price,address,description;
    private RadioGroup acctype,accfor;
    private DataHouseViewModel dataHouseViewModel;
    private List<Uri> imguris;
    private ProgressBar progressBar;
    private ScrollView scV;
    NavController navController;
    public static PostAdFragment newInstance() {
        return new PostAdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataHouseViewModel = ViewModelProviders.of(this).get(DataHouseViewModel.class);


        return inflater.inflate(R.layout.fragment_post_ad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        progressBar= view.findViewById(R.id.progressBarrr);
        selectimages = view.findViewById(R.id.btn_selectimages);
        postad = view.findViewById(R.id.btn_postad);
        txt = view.findViewById(R.id.textView);
        title = view.findViewById(R.id.title);
        price = view.findViewById(R.id.price);
        address = view.findViewById(R.id.address);
        description = view.findViewById(R.id.drescription);
        acctype = view.findViewById(R.id.radiogroup);
        accfor = view.findViewById(R.id.radiogroup2);
        scV = view.findViewById(R.id.scview);
        selectimages.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        postad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String titleS  = title.getText().toString();
                    String priceS =  price.getText().toString();
                    String addressS = address.getText().toString();
                    String descriptions = description.getText().toString();
                    String selimg = txt.getText().toString();
                    String acctypes="";
                    String gen = "";
                    switch (acctype.getCheckedRadioButtonId())
                    {
                        case R.id.hostel:
                                acctypes = "Hostel";
                                break;
                        case R.id.pg:
                                acctypes = "PG";
                                break;
                        case R.id.flathouse:
                                acctypes = "Flat/House";
                                break;
                        default:
                                acctypes="";
                                break;
                    }
                switch (accfor.getCheckedRadioButtonId())
                {
                    case R.id.male:
                        gen = "Male";
                        break;
                    case R.id.female:
                        gen = "Female";
                        break;
                    case R.id.both:
                        gen = "Male/Female";
                        break;
                    default:
                        gen="";
                        break;
                }

                    if(checkInputs(titleS,priceS,addressS,descriptions,acctypes,gen,selimg))
                    {
                        //upload to firebase
                        boolean task = dataHouseViewModel.uploadAd(titleS,priceS,addressS,descriptions,acctypes,gen,imguris,progressBar,navController);
                        scV.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        postad.setVisibility(View.GONE);

                        Toast.makeText(getContext(), "Your Ads will soon get Uploaded Please Wait", Toast.LENGTH_LONG).show();

                    }


            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            imguris = new ArrayList<>();
            ClipData clipData = data.getClipData();
            String ss = "";
            if(clipData!=null)
            {
                for(int i =0 ; i < clipData.getItemCount() ; i++)
                {
                    if(i==3)
                    {
                        break;
                    }
                    Uri imageuri = clipData.getItemAt(i).getUri();
                    Cursor returnCursor =
                            getActivity().getContentResolver().query(imageuri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    returnCursor.moveToFirst();
                    ss =  ss + "\n"+ returnCursor.getString(nameIndex) ;
                    imguris.add(imageuri);
                }
                Log.i("izzimg", "1 : onActivityResult: "+imguris.size() );
                txt.setText(ss);
            }else{
                Uri imageuri = data.getData();
                Cursor returnCursor =
                        getActivity().getContentResolver().query(imageuri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                ss =  ss + "\n"+ returnCursor.getString(nameIndex) ;
                imguris.add(imageuri);
                txt.setText(ss);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean checkInputs(String title, String price, String address , String description , String acctype,String gen, String img){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(title.equals("") || price.equals("") || address.equals("") || description.equals("") || acctype.equals("") || img.equals("Selected Images") || gen.equals("")){
            Toast.makeText(getContext(), "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PostAdViewModel.class);
        // TODO: Use the ViewModel
    }

}
