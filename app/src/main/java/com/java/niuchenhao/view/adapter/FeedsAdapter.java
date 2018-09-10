package com.java.niuchenhao.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.java.niuchenhao.R;
import com.java.niuchenhao.model.DatabaseModel;
import com.java.niuchenhao.model.bean.ChannelItem;
import com.java.niuchenhao.model.bean.FeedItem;
import com.java.niuchenhao.presenter.FeedsPresenter;
import com.java.niuchenhao.view.activity.NewsContentActivity;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedsAdapter extends BaseAdapter<FeedItem, FeedsAdapter.FeedViewHolder> {
    final private Context context;
    final private ChannelItem channelItem;

    public FeedsAdapter(Context context, List<FeedItem> feedItems, ChannelItem channelItem) {
        super(feedItems);
        this.context = context;
        this.channelItem = channelItem;
        FeedsPresenter.registerAdapter(channelItem, this);
    }

    @Override
    protected void finalize() throws Throwable {
        FeedsPresenter.unregisterAdapter(channelItem, this);
        super.finalize();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custum_row_news_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder holder, int position) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        holder.current = datas.get(position);
        holder.Title.setText(holder.current.getTitle());
        holder.Description.setText(Html.fromHtml(holder.current.getDescription()));
        View.OnClickListener clickOpenUrl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.current.setHadRead(true);
                refreshHadRead(holder);
                FeedsPresenter.addClick(holder.current);
                NewsContentActivity.actionStart(context, holder.current);
            }
        };
        holder.Description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.Description.getMaxLines() < 10)
                    holder.Description.setMaxLines(100000);
                else {
                    holder.Description.setMaxLines(3);
                    holder.current.setHadReadDescription(true);
//                    FeedsPresenter.update(holder.current);
                }
                refreshHadRead(holder);
            }
        });
        holder.Date.setText(holder.current.getPubDate());

        final ImageView thumbnails = holder.Thumbnail;
        final String currentId = holder.getId();


        if (holder.current.getThumbnailUrl() == null) {
            holder.Thumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.rss_logo));
        } else if (!DatabaseModel.getIsOnline()) {
            // ref: https://stackoverflow.com/questions/23391523/load-images-from-disk-cache-with-picasso-if-offline
            Picasso.with(context)
                    .load(Uri.parse(holder.current.getThumbnailUrl()))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .error(R.drawable.rss_logo)
                    .into(holder.Thumbnail);
        } else {
            Picasso.with(context)
                    .load(Uri.parse(holder.current.getThumbnailUrl()))
                    .placeholder(R.drawable.rss_logo)
                    .error(R.drawable.rss_logo)
                    .into(holder.Thumbnail);
        }

        holder.Thumbnail.setOnClickListener(clickOpenUrl);
        holder.Title.setOnClickListener(clickOpenUrl);
        refreshHadRead(holder);
    }

    private void refreshHadRead(final FeedViewHolder holder) {
        if (holder.current.hadRead()) {
            holder.Title.setTextColor(0xdeb7b7b7);
        } else {
            holder.Title.setTextColor(0xe2ffffff);
        }
        if (holder.current.hadReadDescription())
            holder.Description.setTextColor(0xe94e4e4e);
        else
            holder.Description.setTextColor(0xe9131313);
        holder.Title.refreshDrawableState();
        holder.Description.refreshDrawableState();
    }

    @Override
    protected boolean areItemsTheSame(FeedItem oldItem, FeedItem newItem) {
        return oldItem.getLink().equals(newItem.getLink());
    }

    @Override
    protected boolean areContentsTheSame(FeedItem oldItem, FeedItem newItem) {
        return true;
    }


    public class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Description, Date;
        ImageView Thumbnail;
        CardView cardView;
        FeedItem current;
        public FeedViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.title_text);
            Description = itemView.findViewById(R.id.description_text);
            Date = itemView.findViewById(R.id.date_text);
            Thumbnail = itemView.findViewById(R.id.thumb_img);
            cardView = itemView.findViewById(R.id.cardview);
        }

        public ImageView getThumbnail() {
            return Thumbnail;
        }

        public String getId() {
            return current.getFilename();
        }

        public FeedItem getCurrent() {
            return current;
        }
    }
}