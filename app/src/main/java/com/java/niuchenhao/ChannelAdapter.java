package com.java.niuchenhao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends BaseAdapter<ChannelItem, ChannelAdapter.ChannelViewHolder> {
    private Context context;
    public ChannelAdapter(Context context, List<ChannelItem> channelItems){
        super(channelItems);
        this.context=context;
        Log.d("channel adapter", channelItems.toString());
        ChannelsPresenter.registAdapter(this);
    }
    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.channel_label, parent,false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelViewHolder holder, int position) {
        YoYo.with(Techniques.BounceIn).playOn(holder.cardView);
        holder.current = datas.get(position);
        holder.labelText.setText(holder.current.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.current.toggleChecked();
                ChannelsPresenter.toggleCheck(holder.current);
            }
        });
    }

    @Override
    protected boolean areItemsTheSame(ChannelItem oldItem, ChannelItem newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(ChannelItem oldItem, ChannelItem newItem) {
        return true;
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder {
        TextView labelText;
        CardView cardView;
        ChannelItem current;
        public ChannelViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.channel_label_card);
            labelText = itemView.findViewById(R.id.channel_label_text);
        }
    }
}