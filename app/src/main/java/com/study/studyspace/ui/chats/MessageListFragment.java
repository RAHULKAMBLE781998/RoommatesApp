package com.study.studyspace.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.studyspace.R;
import com.study.studyspace.UserObject;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment extends Fragment {

    private MessageListViewModel messageListViewModel;
    NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        messageListViewModel = ViewModelProviders.of(getActivity()).get(MessageListViewModel.class);

        return inflater.inflate(R.layout.fragment_messagelist, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_viewmsglist);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setHasFixedSize(true);
            final MessageListAdapter adapter = new MessageListAdapter();
            recyclerView.setAdapter(adapter);
        try {
            final List<UserObject> azz = new ArrayList<>();
            UserObject gg = new UserObject();
            gg.setUser2name("no msgs");
            gg.setUser2id("no id");
            gg.setChatroom("anndjaw");
            azz.add(gg);
            messageListViewModel.getMessageList().observe(getViewLifecycleOwner(), new Observer<List<UserObject>>() {
                @Override
                public void onChanged(List<UserObject> adds) {
                    Log.i("home","observing");

                    adapter.setAds(azz);
                    adapter.setAds(adds);

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    adapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(UserObject add) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("userobj", add);
            bundle.putString("caller","messagelist");
            navController.getGraph().findNode(R.id.chatFragment).setLabel(add.getUser2name());
            if(navController.getCurrentDestination().getId() == R.id.nav_chat) {
                navController.navigate(R.id.action_nav_chat_to_chatFragment,bundle);
            }
        }
    });
    }

}

