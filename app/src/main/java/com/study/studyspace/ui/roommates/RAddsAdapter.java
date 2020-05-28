package com.study.studyspace.ui.roommates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.studyspace.R;
import com.study.studyspace.Roommate;

import java.util.ArrayList;
import java.util.List;

public class RAddsAdapter extends RecyclerView.Adapter<RAddsAdapter.AddsHolder> {
    private List<Roommate> ads = new ArrayList<>();
    private OnItemClickListener listener;
    public RAddsAdapter() {

    }

    @NonNull
    @Override
    public AddsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.roommateads_item, parent, false);
        return new AddsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddsHolder holder, int position) {
        Roommate currentAdd = ads.get(position);
        holder.username.setText(currentAdd.getUsername());
        holder.year.setText(currentAdd.getYear());
        holder.branch.setText(currentAdd.getBranch());
        holder.requirements.setText(currentAdd.getNeeded());
        if(currentAdd.getGender()!= null)
        {
          if(currentAdd.getGender().equals("Male")) {
              holder.img.setImageResource(R.drawable.malestudent);
          }else if(currentAdd.getGender().equals("Female")){
              holder.img.setImageResource(R.drawable.femalestudent);
          }

        }

        //holder.img.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/studyspace-dbe55.appspot.com/o/images%2Ffce1b84d-5a41-4f8f-bf1f-137064f18253?alt=media&token=a4baa55f-5c6b-4695-aba7-5b4862df7363"));
        //holder.img.setImageURI();

    }

    @Override
    public int getItemCount() {
        return ads.size();
    }
    public void setAds(List<Roommate> ads)
    {
        this.ads = ads;
        notifyDataSetChanged();
    }
    public Roommate getAddAt(int position) {
        return ads.get(position);
    }
    class AddsHolder extends RecyclerView.ViewHolder
    {
        private TextView username;
        private TextView year;
        private TextView branch;
        private TextView requirements;
        private ImageView img;
        public AddsHolder(View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            year = itemView.findViewById(R.id.year);
            img = itemView.findViewById(R.id.imageViewadd);
            branch = itemView.findViewById(R.id.branch);
            requirements = itemView.findViewById(R.id.requirements);

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
        void onItemClick(Roommate add);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
