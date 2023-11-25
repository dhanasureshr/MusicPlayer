package com.superpowered.playerexample;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class Recycle_adapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    ArrayList<audio_data> audio_tracks_list;
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public Recycle_adapter(Context context, ArrayList<audio_data> audio_tracks_list) {
        this.context = context;
        this.audio_tracks_list = audio_tracks_list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.track_holder, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.song_name.setText(audio_tracks_list.get(position).title);
        audio_data currentItem = audio_tracks_list.get(position);
        Picasso.get().load(currentItem.getAlbumArtURI()).into(holder.Audio_album);
        holder.itemView.setOnClickListener(view -> {
            //send this path to the audio player to load and play the song
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(currentItem.getData(),currentItem.getTitle());
            }
        });
    }
    @Override
    public int getItemCount() {
        return audio_tracks_list.size();
    }
    public interface OnItemClickListener {
        void onItemClick(String path,String song_name);
    }
}
