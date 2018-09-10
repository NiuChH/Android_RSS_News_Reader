package com.java.niuchenhao.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.java.niuchenhao.view.activity.NewsContentActivity;
import com.java.niuchenhao.R;
import com.java.niuchenhao.model.bean.ChannelItem;
import com.java.niuchenhao.model.bean.FeedItem;
import com.java.niuchenhao.presenter.FeedsPresenter;

import java.util.List;

public class SearchAdapter extends BaseAdapter<FeedItem, SearchAdapter.SearchViewHolder>  {
    private final ChannelItem channelItem;
    private final Context context;

    public SearchAdapter(Context context, List<FeedItem> datas, ChannelItem channelItem) {
        super(datas);
        this.context=context;
        this.channelItem = channelItem;
        FeedsPresenter.registerAdapter(channelItem, this);
    }

    @Override
    protected void finalize() throws Throwable {
        FeedsPresenter.unregisterAdapter(channelItem, this);
        super.finalize();
    }

    @Override
    protected boolean areItemsTheSame(FeedItem oldItem, FeedItem newItem) {
        return oldItem.getLink().equals(newItem.getLink());
    }

    @Override
    protected boolean areContentsTheSame(FeedItem oldItem, FeedItem newItem) {
        return oldItem.getTitle().equals(newItem.getTitle());
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_news_item, parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        YoYo.with(Techniques.BounceIn).playOn(holder.itemView);
        holder.current=datas.get(position);
        holder.Title.setText(holder.current.getTitle());
        holder.Date.setText(holder.current.getPubDate());
        holder.cardView.setOnClickListener(v -> {
            holder.current.setHadRead(true);
            refreshHadRead(holder);
            FeedsPresenter.addClick(holder.current);
            NewsContentActivity.actionStart(context, holder.current);
        });
    }

    private void refreshHadRead(final SearchAdapter.SearchViewHolder holder){
        if(holder.current.hadRead()) {
            holder.Title.setTextColor(0xdeb7b7b7);
            holder.Date.setTextColor(0xd0b7b7b7);
        } else {
            holder.Title.setTextColor(0xdc181818);
            holder.Date.setTextColor(0xde2e2e2e);
        }
        holder.Title.refreshDrawableState();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView Title, Date;
        FeedItem current;
        CardView cardView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.search_item);
            Title = itemView.findViewById(R.id.search_title);
            Date = itemView.findViewById(R.id.search_date);
        }
    }
}
