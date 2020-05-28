package com.study.studyspace.ui.myads;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.study.studyspace.Add;
import com.study.studyspace.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ViewMyAddFragment extends Fragment {
    private static final String TAG = "ViewMyAddFragment";
    private static final String ARG_PARAM1 = "Add";
    private Add add;
    private List<Bitmap> imgbitmaps = new ArrayList<>();
    private List<String> imgrefs = new ArrayList<>();
    private ViewPager viewPager;
    private PropertyImagesAdapter propertyImagesAdapter;
    private EditText title,price,address,description;
    private RadioGroup accomodationtype,accomodationfor;
    private RadioButton checked,type;
    private Button selectimages,updatead,deleteadd;
    private List<Uri> imguris = new ArrayList<>();
    private TextView txt;
    NavController navController;
    private ViewMyAddViewModel viewMyAddViewModel;
    public ViewMyAddFragment() {
        // Required empty public constructor
    }
    public static ViewMyAddFragment newInstance(String param1, String param2) {
        ViewMyAddFragment fragment = new ViewMyAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            add = (Add) getArguments().getSerializable(ARG_PARAM1);
            Log.i("ooooooo" , "bundel received" + add.getImg4());
        }
        imgrefs.add(add.getImg1());
        imgrefs.add(add.getImg2());
        imgrefs.add(add.getImg3());

        propertyImagesAdapter= new PropertyImagesAdapter(this.getContext(),imgrefs);
        viewMyAddViewModel = ViewModelProviders.of(getActivity()).get(ViewMyAddViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_myadd, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt = view.findViewById(R.id.textView);
        navController = Navigation.findNavController(view);
        viewPager= view.findViewById(R.id.property_images_viewpager);
        viewPager.setAdapter(propertyImagesAdapter);
        title = view.findViewById(R.id.addTitle1);
        title.setText(add.getTitle());
        price = view.findViewById(R.id.addPrice1);
        price.setText(add.getPrice());
        address = view.findViewById(R.id.addaddress1);
        address.setText(add.getAddress());
        description = view.findViewById(R.id.addDescription1);
        description.setText(add.getDescription());
        accomodationfor = view.findViewById(R.id.addaccomodationFor1);
        selectimages = view.findViewById(R.id.btn_selectimages);
        updatead = view.findViewById(R.id.btn_update);
        deleteadd = view.findViewById(R.id.btn_deleteAdd);
        accomodationtype = view.findViewById(R.id.addaccomodationtype1);
        if(add.getAccomodationfor().equals("Male"))
        {
            checked = view.findViewById(R.id.myaddMale);
            checked.setChecked(true);
        }else if(add.getAccomodationfor().equals("Female")){

            checked = view.findViewById(R.id.myaddFemale);
            checked.setChecked(true);
        }else {
            checked = view.findViewById(R.id.myaddBoth);
            checked.setChecked(true);
        }

        if(add.getAccomodationtype().equals("Hostel"))
        {
            type = view.findViewById(R.id.myaddhostel);
            type.setChecked(true);
        }else if(add.getAccomodationtype().equals("PG")){

            type = view.findViewById(R.id.myaddpg);
            type.setChecked(true);
        }else {
            type = view.findViewById(R.id.myaddFlatHouse);
            type.setChecked(true);
        }

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


        updatead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleS  = title.getText().toString();
                String priceS =  price.getText().toString();
                String addressS = address.getText().toString();
                String descriptions = description.getText().toString();
                String selimg = txt.getText().toString();
                String acctypes="";
                String gen = "";
                switch (accomodationtype.getCheckedRadioButtonId())
                {
                    case R.id.myaddhostel:
                        acctypes = "Hostel";
                        break;
                    case R.id.myaddpg:
                        acctypes = "PG";
                        break;
                    case R.id.myaddFlatHouse:
                        acctypes = "Flat/House";
                        break;
                    default:
                        acctypes="";
                        break;
                }
                switch (accomodationfor.getCheckedRadioButtonId())
                {
                    case R.id.myaddMale:
                        gen = "Male";
                        break;
                    case R.id.myaddFemale:
                        gen = "Female";
                        break;
                    case R.id.myaddBoth:
                        gen = "Male/Female";
                        break;
                    default:
                        gen="";
                        break;
                }
                
                if(checkInputs(titleS,priceS,addressS,descriptions,acctypes,gen))
                {
                    //upload to firebase
                     viewMyAddViewModel.updateadd(titleS,priceS,addressS,descriptions,acctypes,gen,imguris,add.getClgid(),add.getAddID(),add);


                    Toast.makeText(getContext(), "Your Ads will soon get Uploaded", Toast.LENGTH_LONG).show();

                    navController.navigateUp();
                }


            }
        });
        deleteadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMyAddViewModel.deleteadd(add);
                navController.navigateUp();
            }
        });
        
        super.onViewCreated(view, savedInstanceState);
    }



    private boolean checkInputs(String title, String price, String address , String description , String acctype,String gen){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(title.equals("") || price.equals("") || address.equals("") || description.equals("") || acctype.equals("") || gen.equals("")){
            Toast.makeText(getContext(), "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            ClipData clipData = data.getClipData();
            String ss = "";
            if(clipData!=null)
            {
                for(int i =0 ; i < clipData.getItemCount() ; i++)
                {
                    if(i==4)
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
}
