package com.example.kavi.mobileverification;

/**
 * Created by vignesh on 07-09-2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 2/12/2018.
 */

public class BannerAdapter {
   /* private static final String TAG = "DetailAdapter";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public BannerAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        Picasso.with(holder.image.getContext())
                .load(mImageUrls.get(position))
                .resize(100,100)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bannerimg);

        }
    }*/
}