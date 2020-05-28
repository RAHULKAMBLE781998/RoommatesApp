package com.study.studyspace.ui.myads;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.studyspace.Add;
import com.study.studyspace.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAddsAdapter extends RecyclerView.Adapter<MyAddsAdapter.AddsHolder> {
    private List<Add> ads = new ArrayList<>();
    private OnItemClickListener listener;
    public MyAddsAdapter() {

    }

    @NonNull
    @Override
    public AddsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_itemm, parent, false);
        return new AddsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddsHolder holder, int position) {
        Add currentAdd = ads.get(position);
        Log.i("dataa",currentAdd.getPrice() + currentAdd.getTitle()  +currentAdd.getAccomodationtype());
        holder.price.setText(currentAdd.getPrice());
        holder.title.setText(currentAdd.getTitle());
        holder.type.setText(currentAdd.getAccomodationtype());
        holder.accfor.setText(currentAdd.getAccomodationfor());
        if(currentAdd.getImg1()!= null)
        {
           // holder.img.setImageBitmap(currentAdd.getImg1bitmap());
           // final ImageLoader imageLoader = ImageLoader.getInstance();
            Picasso.get().load(currentAdd.getImg1()).into( holder.img);

           // imageLoader.displayImage(currentAdd.getImg1(), holder.img);
             //new DownLoadImageTask(holder.img).execute(currentAdd.getImg1());
            //holder.img.setImageBitmap(currentAdd.getImg1bitmap());
            Log.i("ccc",currentAdd.getImg1());
        }

        //holder.img.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/studyspace-dbe55.appspot.com/o/images%2Ffce1b84d-5a41-4f8f-bf1f-137064f18253?alt=media&token=a4baa55f-5c6b-4695-aba7-5b4862df7363"));
        //holder.img.setImageURI();

    }

    @Override
    public int getItemCount() {
        return ads.size();
    }
    public void setAds(List<Add> ads)
    {
        Log.i("myads","adapter runnung");
        this.ads = ads;
        notifyDataSetChanged();
    }
    public Add getAddAt(int position) {
        return ads.get(position);
    }
    class AddsHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView price;
        private TextView accfor;
        private TextView type;
        private ImageView img;
        public AddsHolder(View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.titleadd);
            price = itemView.findViewById(R.id.priceadd);
            img = itemView.findViewById(R.id.imageViewadd);
            type = itemView.findViewById(R.id.typeadd);
            accfor = itemView.findViewById(R.id.accgender);

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
        void onItemClick(Add add);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
