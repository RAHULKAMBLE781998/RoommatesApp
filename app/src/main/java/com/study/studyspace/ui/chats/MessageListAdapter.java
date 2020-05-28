package com.study.studyspace.ui.chats;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.studyspace.R;
import com.study.studyspace.UserObject;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.AddsHolder> {
    private List<UserObject> ads = new ArrayList<>();
    private OnItemClickListener listener;
    public MessageListAdapter() {

    }

    @NonNull
    @Override
    public AddsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagelist_item, parent, false);
        return new AddsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddsHolder holder, int position) {
        UserObject currentuser2 = ads.get(position);
        holder.user2name.setText(currentuser2.getUser2name());
        Log.i("listmsg", "onBindViewHolder: " + currentuser2.getUser2id());

    }

    @Override
    public int getItemCount() {
        return ads.size();
    }
    public void setAds(List<UserObject> ads)
    {
        this.ads = ads;
        Log.i("listmsg", "onViewCreated:  size " + this.ads.size());
        notifyDataSetChanged();
    }
    public UserObject getAddAt(int position) {
        return ads.get(position);
    }
    class AddsHolder extends RecyclerView.ViewHolder
    {
        private TextView user2name;
        public AddsHolder(View itemView)
        {
            super(itemView);
            user2name = itemView.findViewById(R.id.user2name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(ads.get(position));
                    }
                }
            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(UserObject add);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
