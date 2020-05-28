package com.study.studyspace.ui.chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.studyspace.Message;
import com.study.studyspace.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.AddsHolder> {
    private List<Message> msgs = new ArrayList<>();
    private OnItemClickListener listener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    public ChatMessagesAdapter() {

    }

    @NonNull
    @Override
    public AddsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new AddsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddsHolder holder, int position) {

        Message currentuser2 = msgs.get(position);
        holder.user2name.setText(currentuser2.getMessage());
        if(currentuser2.getSenderId().equals(user.getUid()))
        {
            holder.rel.setBackgroundResource(R.drawable.messagebg1);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,100);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.rel.setLayoutParams(params);
        }else{
            holder.rel.setBackgroundResource(R.drawable.messagebg2);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,100);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.rel.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }
    public void setMessages(List<Message> msgs)
    {
        this.msgs = msgs;
        notifyDataSetChanged();
    }
    public Message getAddAt(int position) {
        return msgs.get(position);
    }
    class AddsHolder extends RecyclerView.ViewHolder
    {
        private TextView user2name;
        private RelativeLayout rel;
        public AddsHolder(View itemView)
        {
            super(itemView);
            user2name = itemView.findViewById(R.id.messagechat);
            rel = itemView.findViewById(R.id.rellayoutChatItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(msgs.get(position));
                    }
                }
            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(Message add);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
