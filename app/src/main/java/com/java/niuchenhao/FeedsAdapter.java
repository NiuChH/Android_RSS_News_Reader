package com.java.niuchenhao;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import org.jsoup.Connection;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

/**
 * Created by rishabh on 26-02-2016.
 */
public class FeedsAdapter extends BaseAdapter<FeedItem, FeedsAdapter.MyViewHolder> {
    Context context;
    public FeedsAdapter(Context context, ArrayList<FeedItem>feedItems){
        super(feedItems);
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custum_row_news_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        holder.current=datas.get(position);
        holder.Title.setText(holder.current.getTitle());
        holder.Description.setText(Html.fromHtml(holder.current.getDescription()));
        holder.Date.setText(holder.current.getPubDate());
        Picasso.with(context).load(holder.current.getThumbnailUrl()).into(holder.Thumbnail);
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("holder title", "click!");
//                Toast.makeText(context.getApplicationContext(), "click!", Toast.LENGTH_LONG).show();
            }
        });
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Description,Date;
        ImageView Thumbnail;
        CardView cardView;
        FeedItem current;
        public MyViewHolder(View itemView) {
            super(itemView);
            Title= (TextView) itemView.findViewById(R.id.title_text);
            Description= (TextView) itemView.findViewById(R.id.description_text);
            Date= (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail= (ImageView) itemView.findViewById(R.id.thumb_img);
            cardView= (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}