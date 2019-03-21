package com.brzimetrokliziretro.imagegallery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brzimetrokliziretro.imagegallery.OnItemClickListener;
import com.brzimetrokliziretro.imagegallery.R;
import com.brzimetrokliziretro.imagegallery.models.Album;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumsHolder> {
    private OnItemClickListener listener;

    private List<Album> list;


    public AlbumsAdapter(){

    }

    public AlbumsAdapter(List<Album> list){
        this.list = list;
    }


    @NonNull
    @Override
    public AlbumsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_album_item, parent, false);

        return new AlbumsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsHolder holder, int position) {
        holder.albumName.setText(list.get(position).getName());
        holder.userId.setText(String.valueOf(list.get(position).getUserId()));
        holder.id.setText(String.valueOf(list.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AlbumsHolder extends RecyclerView.ViewHolder{
        private TextView albumName, userId, id;

        public AlbumsHolder(View itemView){
            super(itemView);

            albumName = (TextView)itemView.findViewById(R.id.tv_album_name);
            userId = (TextView)itemView.findViewById(R.id.tv_user_id);
            id = (TextView)itemView.findViewById(R.id.tv_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(list.get(position).getId());
                    }
                }
            });

        }
    }

    public void setOnItemClickListner(OnItemClickListener listener){
        this.listener = listener;
    }
}
