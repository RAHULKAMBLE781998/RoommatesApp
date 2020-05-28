package com.study.studyspace.ui.chats;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.study.studyspace.Add;
import com.study.studyspace.Message;
import com.study.studyspace.NotificationManagement;
import com.study.studyspace.R;
import com.study.studyspace.Roommate;
import com.study.studyspace.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {
    /////////test


    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    List<Message> msg = new ArrayList<>();
    DatabaseReference mesgref;

    ////////
    private static final String TAG = "ChatFragment";
    private static final String ARG_PARAM1 = "Add";
    private MessageListViewModel mViewModel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser thisuser = mAuth.getCurrentUser();
    String caller=null;
    boolean checkmsglist=true;
    UserObject userObject = new UserObject();
    MutableLiveData<String> chatroom = new MutableLiveData<>();
    EditText textmsg;
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }
    Button send;
    RecyclerView.LayoutManager rvlm;
    NavController navController;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       mViewModel = ViewModelProviders.of(getActivity()).get(MessageListViewModel.class);
        rvlm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        if (getArguments() != null) {
            caller = getArguments().getString("caller");
            Log.i("chatfragment" , "bundel received" );
        }
       switch (caller)
       {
           case "home":
               Add add= (Add) getArguments().getSerializable("user2fromhome");
               userObject.setUser2id(add.getUserid());
               userObject.setUser2name(add.getUsername());
               checkmsglist=true;
               Log.i("msglogs", "1 : called by home");
               break;
           case "roommate":
               Roommate user2rm = (Roommate) getArguments().getSerializable("user2fromroommate");
               userObject.setUser2id(user2rm.getUserID());
               userObject.setUser2name(user2rm.getUsername());
               checkmsglist=true;
               Log.i("msglogs", "1 : called by roommate");
               break;
           case "messagelist" :
               UserObject user2ub = (UserObject) getArguments().getSerializable("userobj");
               userObject.setUser2id(user2ub.getUser2id());
               userObject.setUser2name(user2ub.getUser2name());
               userObject.setChatroom(user2ub.getChatroom());
               checkmsglist=false;
               Log.i("msglogs", "1 : called by messagelist");
               break;
       }

        if(checkmsglist) {
            String chatroomkey =  mViewModel.checkifchatexist(userObject);
            if (chatroomkey != null) {
                chatroom.setValue(chatroomkey);
                Log.i("msglogs", "2 : checked list found user  ");
            } else {
                String chatroomm = createnewchatroom(userObject);
                chatroom.setValue(chatroomm);
                Log.i("msglogs", "2 : checked list creating new user  ");
            }
        }else{
            Log.i("msglogs", "2 :  found user ");
            chatroom.setValue(userObject.getChatroom());
        }
        return inflater.inflate(R.layout.chat_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        send = view.findViewById(R.id.send);
        textmsg = view.findViewById(R.id.messageInput);
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_viewchatmsg);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(rvlm);
        final ChatMessagesAdapter adapter = new ChatMessagesAdapter();
        recyclerView.setAdapter(adapter);
        getMsgsfromFB();

        messages.observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setMessages(messages);
                rvlm.scrollToPosition(messages.size()-1);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msglogs", "4 : sending msg  ");
                //mViewModel.sendMessage(textmsg.getText().toString());
                sendMessage(textmsg.getText().toString());
                NotificationManagement nm = new NotificationManagement();
                nm.sendNotification(userObject.getUser2id(),textmsg.getText().toString(),thisuser.getDisplayName(),getActivity().getApplicationContext());
                textmsg.setText("");
            }
        });


    }


    //////////////////testttttttttttttttttttttttt
    public void sendMessage(String msg) {

        String messageId = mesgref.push().getKey();
        // mesgref.child(messageId).setValue("true");
        final DatabaseReference newMessageDb = mesgref.child(messageId);

        final Map newMessageMap = new HashMap<>();

        newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

        if(!msg.isEmpty())
        {
            newMessageMap.put("message",msg);
            newMessageDb.updateChildren(newMessageMap);
        }


    }
    public void getMsgsfromFB()
    {
        Log.i("msglogs", "3 : getMessages called  ");
        messages.setValue(new ArrayList<Message>());
        mesgref = FirebaseDatabase.getInstance().getReference().child("allchatrooms").child(chatroom.getValue());
        final String[] lastkey = new String[1];
        lastkey[0]="xyz";
        mesgref.keepSynced(true);
        mesgref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Log.i("msglogs", "onChildAdded: last one called"+lastkey[0] );
                    Log.i("msglogs", "onChildAdded: current one  called"+dataSnapshot.getKey());
                    if(lastkey[0].equals(dataSnapshot.getKey()))
                    {
                        return;
                    }
                    lastkey[0] = dataSnapshot.getKey();
                    Log.i("msglogs", "onChildAdded: called");
                    String  message = "",
                            creatorID = "";
                    if(dataSnapshot.child("message").getValue() != null)
                        message = dataSnapshot.child("message").getValue().toString();
                    if(dataSnapshot.child("creator").getValue() != null)
                        creatorID = dataSnapshot.child("creator").getValue().toString();

                    Message newmsg = new Message();
                    newmsg.setMessage(message);
                    newmsg.setSenderId(creatorID);
                    if(!newmsg.getMessage().equals("")) {
                        msg.add(newmsg);
                        messages.setValue(msg);
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public String createnewchatroom(UserObject userObject) {
        String key = FirebaseDatabase.getInstance().getReference().child("allchatrooms").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("allchatrooms").child(key).child("Info").setValue("info");
        Log.i(TAG, "createnewchatroom: key is  " +key);
        //setting in user1
        FirebaseDatabase.getInstance().getReference().child("users").child(thisuser.getUid()).child("chatrooms").child(key).child("user2name").setValue(userObject.getUser2name());
        FirebaseDatabase.getInstance().getReference().child("users").child(thisuser.getUid()).child("chatrooms").child(key).child("user2id").setValue(userObject.getUser2id());

        FirebaseDatabase.getInstance().getReference().child("users").child(userObject.getUser2id()).child("chatrooms").child(key).child("user2name").setValue(thisuser.getDisplayName());

        FirebaseDatabase.getInstance().getReference().child("users").child(userObject.getUser2id()).child("chatrooms").child(key).child("user2id").setValue(thisuser.getUid());

        Log.i("msglogs", "2 : setting chatroom in createnewchatroom ");
        chatroom.setValue(key);
        //user2obj=userObject;
        return key;
    }

}
