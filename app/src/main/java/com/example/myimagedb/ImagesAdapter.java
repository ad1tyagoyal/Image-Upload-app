package com.example.myimagedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>
{
    private Context context;
    private List<UploadFile> uploads;

    public ImagesAdapter(Context context, List<UploadFile> uploads)
    {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position)
    {
        UploadFile uploadCurrent = uploads.get(position);
        holder.textView.setText(uploadCurrent.getImageName());
        Picasso.with(context).load(uploadCurrent.getImageUrl()).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount()
    {
        return uploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView textView;

        public ImageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textView = itemView.findViewById(R.id.textView_Image_Name);
            imageView = itemView.findViewById(R.id.imageView_Image);
        }
    }




}
