package com.brzimetrokliziretro.imagegallery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brzimetrokliziretro.imagegallery.OnImageItemClickListener;
import com.brzimetrokliziretro.imagegallery.R;
import com.brzimetrokliziretro.imagegallery.models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesHolder> {
    private OnImageItemClickListener listener;

    private List<Image> list;

    public ImagesAdapter(List<Image> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_image_item, parent, false);

        return new ImagesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesHolder holder, int position) {
        holder.imageName.setText(list.get(position).getImage_name());
        holder.url.setText(list.get(position).getExternal_url());

        Picasso.get().load(list.get(position).getLoad_image_url()).placeholder(R.drawable.blue_water345)
                .fit().centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ImagesHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView imageName, url;

        public ImagesHolder(View itemView){
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.image);
            imageName = (TextView)itemView.findViewById(R.id.tv_image_name);
            url = (TextView)itemView.findViewById(R.id.tv_image_url);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onImageItemClick(list.get(position).getExternal_url());
                    }
                }
            });

        }
    }

    public void setOnImageItemClickListener(OnImageItemClickListener listener){
        this.listener = listener;
    }
}
