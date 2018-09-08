package com.java.niuchenhao;

import android.content.Context;
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
import com.java.niuchenhao.bean.FeedItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedsAdapter extends BaseAdapter<FeedItem, FeedsAdapter.FeedViewHolder> {
    private Context context;
    public FeedsAdapter(Context context, ArrayList<FeedItem>feedItems){
        super(feedItems);
        this.context=context;
    }
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custum_row_news_item, parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder holder, int position) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        holder.current=datas.get(position);
        holder.Title.setText(holder.current.getTitle());
        holder.Description.setText(Html.fromHtml(holder.current.getDescription()));
        View.OnClickListener clickOpenUrl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.current.setHadRead(true);
                refreshHadRead(holder);
                NewsContentActivity.actionStart(context, holder.current);
            }
        };
        holder.Description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.Description.getMaxLines() < 10)
                    holder.Description.setMaxLines(100000);
                else {
                    holder.Description.setMaxLines(3);
                    holder.current.setHadReadDescription(true);
                }
                refreshHadRead(holder);
            }
        });
        holder.Date.setText(holder.current.getPubDate());
        Picasso.with(context).load(holder.current.getThumbnailUrl()).into(holder.Thumbnail);
        holder.Thumbnail.setOnClickListener(clickOpenUrl);
        holder.Title.setOnClickListener(clickOpenUrl);
        refreshHadRead(holder);
    }

    private void refreshHadRead(final FeedViewHolder holder){
        if(holder.current.hadRead()) {
            holder.Title.setTextColor(0xdeb7b7b7);
        } else {
            holder.Title.setTextColor(0xe2ffffff);
        }
        if(holder.current.hadReadDescription())
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

//    class MyCardView extends CardView{
//
//        public MyCardView(@NonNull final Context context) {
//            super(context);
//            setOnClickListener(new OnClickListener() {
//                @SuppressLint("ShowToast")
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "click!", Toast.LENGTH_LONG);
//                }
//            });
//        }
//    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Description,Date;
        ImageView Thumbnail;
        CardView cardView;
        FeedItem current;
        public FeedViewHolder(View itemView) {
            super(itemView);
            Title= (TextView) itemView.findViewById(R.id.title_text);
            Description= (TextView) itemView.findViewById(R.id.description_text);
            Date= (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail= (ImageView) itemView.findViewById(R.id.thumb_img);
            cardView= (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}