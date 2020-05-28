package com.study.studyspace.ui.myads;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PropertyImagesAdapter extends PagerAdapter {
    private List<String> imgrefs;
    Context context;
    //LayoutInflater layoutInflater;
    PropertyImagesAdapter(Context cn, List<String> imgrefs)
    {
        this.imgrefs = imgrefs;

        this.context = cn;
        Log.i("ooooo","proig refs received  " + this.imgrefs.get(0));
      //  layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Log.i("Loading" , String.valueOf(position) + "   "+imgrefs.get(position) );
        Picasso.get().load(imgrefs.get(position)).into(imageView);
        container.addView(imageView);
       /* View itemView = layoutInflater.inflate(R.layout.imgitem, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgrefs.get(position),imageView);
        if(!imgbitmaps.isEmpty()) {
            imageView.setImageBitmap(imgbitmaps.get(position));
            Log.i("used" , "Bitmaps");
        }else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imgrefs.get(position),imageView);
            Log.i("used" , "url");
        }*/



        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getCount()
    {
        return imgrefs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
