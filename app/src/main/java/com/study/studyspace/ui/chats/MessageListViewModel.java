package com.study.studyspace.ui.chats;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.study.studyspace.Message;
import com.study.studyspace.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageListViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private MutableLiveData<String> mText;
    private MutableLiveData<String> collegeid = new MutableLiveData<String>();
    private MutableLiveData<List<UserObject>> MessageList = new MutableLiveData<>();
    List<UserObject> msglist;
    Context context;
    private MutableLiveData<String> chatroom = new MutableLiveData<>();
    private static final String TAG = "MessageListViewModel";
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    List<Message> msg = new ArrayList<>();

    DatabaseReference mesgref;
    UserObject user2obj = new UserObject();
    public void setChatroom(MutableLiveData<String> chatroom , UserObject us) {

        Log.i("msglogs", "2 : setting chatroom in createnewchatroom ");
        this.chatroom = chatroom;
        user2obj = us;
    }

    public MessageListViewModel(Application application) {
        super(application);
         context = application.getApplicationContext();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        getclg();

    }

    private void getclg() {
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("College");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                String value = dataSnapshot.getValue().toString();
                collegeid.setValue(value);
                getMessageListFB();}catch (Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getMessageListFB()
    {
        Log.i("msglogs" , "getting msgs list");

        Log.i("listmsg" , "getMessageList running");
        msglist = new ArrayList<>();
        final DatabaseReference myreff = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("chatrooms");
        myreff.keepSynced(true);
        myreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    msglist.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        UserObject user2 = new UserObject();

                            String key = childSnapshot.getKey();
                            Log.i("listmsg" , "getMessageList key is " +key );
                            user2.setChatroom(key);
                           try {
                               String user2name = dataSnapshot.child(key).child("user2name").getValue().toString();
                               user2.setUser2name(user2name);
                                String user2id = dataSnapshot.child(key).child("user2id").getValue().toString();
                                user2.setUser2id(user2id);
                                msglist.add(user2);
                           }catch (Exception e){e.printStackTrace();}

                    }
                    MessageList.setValue(msglist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public MutableLiveData<List<UserObject>> getMessageList()
    {
        return MessageList;
    }
    public LiveData<String> getText() {
        return mText;
    }


    public String checkifchatexist(final UserObject userObject) {
        Log.i("msglogs", "2 :  checking if chat exist ");
        List<UserObject> list = MessageList.getValue();

        try {
            Log.i("msglogs", "2 :  checking if chat exist list size "+ list.size() );
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUser2id().equals(userObject.getUser2id())) {

                    Log.i("msglogs", "2 : setting chatroom in checkifchatexist ");
                    Log.i("msglogs", "msg userboj value " + list.get(i).getUser2id());
                    chatroom.setValue(list.get(i).getChatroom());
                    user2obj = userObject;
                    return list.get(i).getChatroom();

                }
            }
        }catch(Exception e){}
        return null;
    }

    public String createnewchatroom(UserObject userObject) {
        String key = FirebaseDatabase.getInstance().getReference().child("allchatrooms").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("allchatrooms").child(key).child("Info").setValue("info");
        Log.i(TAG, "createnewchatroom: key is  " +key);
        //setting in user1
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("chatrooms").child(key).child("user2name").setValue(userObject.getUser2name());
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("chatrooms").child(key).child("user2id").setValue(userObject.getUser2id());

        //setting in user2
        FirebaseDatabase.getInstance().getReference().child("users").child(userObject.getUser2id()).child("chatrooms").child(key).child("user2name").setValue(user.getDisplayName());

        FirebaseDatabase.getInstance().getReference().child("users").child(userObject.getUser2id()).child("chatrooms").child(key).child("user2id").setValue(user.getUid());

        Log.i("msglogs", "2 : setting chatroom in createnewchatroom ");
        chatroom.setValue(key);
        user2obj=userObject;
        return key;
    }

       public void getMsgsfromFB()
       {
           Log.i("msglogs", "3 : getMessages called  ");
           messages.setValue(new ArrayList<Message>());
           mesgref =FirebaseDatabase.getInstance().getReference().child("allchatrooms").child(chatroom.getValue());
           final String[] lastkey = new String[1];
           lastkey[0]="xyz";
           mesgref.keepSynced(true);
           mesgref.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                   if(dataSnapshot.exists()){
                       Log.i("msglogs", "onChildAdded: nnnn called"+lastkey[0] );
                       Log.i("msglogs", "onChildAdded: nnntt  called"+dataSnapshot.getKey() );
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
    public MutableLiveData<List<Message>> getMessages() {

        return messages;
    }

    public void sendMessage(String msg) {

        String messageId = mesgref.push().getKey();
       // mesgref.child(messageId).setValue("true");
        final DatabaseReference newMessageDb = mesgref.child(messageId);

        final Map newMessageMap = new HashMap<>();

        newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

        if(!msg.isEmpty())
        {
            newMessageMap.put("message",msg);
            updateDatabaseWithNewMessage(newMessageDb, newMessageMap);
        }


    }
    private void updateDatabaseWithNewMessage(DatabaseReference newMessageDb, Map newMessageMap){
        newMessageDb.updateChildren(newMessageMap);
        String message;
        if(newMessageMap.get("text") != null)
            message = newMessageMap.get("text").toString();
        else
            message = "Sent Media";

        /////////Send notification
        }

    public MutableLiveData<String> getmsgz() {
        Log.i("msg", "getmsgz: ");
        return chatroom;
    }

    public void clearit() {
        Log.i("lets", "clearit: ");

    }
}