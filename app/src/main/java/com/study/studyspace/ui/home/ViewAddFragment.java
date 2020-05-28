package com.study.studyspace.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.study.studyspace.Add;
import com.study.studyspace.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewAddFragment extends Fragment {
    private static final String ARG_PARAM1 = "Add";
    private Add add;
    private List<Bitmap> imgbitmaps = new ArrayList<>();
    private List<String> imgrefs = new ArrayList<>();
    private ViewPager viewPager;
    private PropertyImagesAdapter propertyImagesAdapter;
    private TextView title,price,address,description,accomodationtype,accomodationfor;
    private Button message;
    NavController navController;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    public ViewAddFragment() {
        // Required empty public constructor
    }
    public static ViewAddFragment newInstance(String param1, String param2) {
        ViewAddFragment fragment = new ViewAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            add = (Add) getArguments().getSerializable(ARG_PARAM1);
            Log.i("ooooooo" , "bundel received" + add.getImg4());
        }
        if(add.getImg1() != null)
            imgrefs.add(add.getImg1());

        if(add.getImg2() != null)
        imgrefs.add(add.getImg2());

        if(add.getImg3() != null)
        imgrefs.add(add.getImg3());


        propertyImagesAdapter= new PropertyImagesAdapter(this.getContext(),imgrefs);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewPager= view.findViewById(R.id.property_images_viewpager);
        message = view.findViewById(R.id.addChat1);
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
        accomodationfor.setText(add.getAccomodationfor());
        accomodationtype = view.findViewById(R.id.addaccomodationtype1);
        accomodationtype.setText(add.getAccomodationtype());


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new roomid add to user and sending user
                //add unique roomid of chat to roomlist of user
                //lauch chat fragment pass roomid,user,other user to fragment
                //call chat frag
                if(!add.getUserid().equals(user.getUid())) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user2fromhome", add);
                    bundle.putString("caller", "home");

                    navController.getGraph().findNode(R.id.chatFragment).setLabel(add.getUsername());
                    if (navController.getCurrentDestination().getId() == R.id.nav_ViewAddFragment) {
                        navController.navigate(R.id.action_nav_ViewAddFragment_to_chatFragment, bundle);
                    }
                    //notification code
                }
            }
        });

    }
}
