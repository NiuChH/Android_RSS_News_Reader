package com.java.niuchenhao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.bean.FeedItem;
import com.java.niuchenhao.utils.PicUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FeedsAdapter extends BaseAdapter<FeedItem, FeedsAdapter.FeedViewHolder> {
    final private Context context;
    final private ChannelItem channelItem;
    public FeedsAdapter(Context context, List<FeedItem> feedItems, ChannelItem channelItem){
        super(feedItems);
        this.context=context;
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
                FeedsPresenter.addClick(holder.current);
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
//                    FeedsPresenter.update(holder.current);
                }
                refreshHadRead(holder);
            }
        });
        holder.Date.setText(holder.current.getPubDate());

        final ImageView thumbnails = holder.Thumbnail;
        final String currentId = holder.getId();


        // ref: https://stackoverflow.com/questions/23391523/load-images-from-disk-cache-with-picasso-if-offline
        Picasso.with(context)
                .load(Uri.parse(holder.current.getThumbnailUrl()))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.Thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed
                        Picasso.with(context)
                                .load(Uri.parse(holder.current.getThumbnailUrl()))
                                .placeholder(R.drawable.ic_backup)
                                .error(R.drawable.ic_delete)
                                .into(holder.Thumbnail);
                    }
                });

//        Picasso.with(context).load(holder.current.getThumbnailUrl()).into(thumbnails);
//        if(DatabaseModel.getIsOnline())
//            Picasso.with(context).load(holder.current.getThumbnailUrl()).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    Log.i(TAG, "The image was obtained correctly from "+from);
//                    thumbnails.setImageBitmap(bitmap);
////                    PicUtils.savePic(context.getFilesDir().getPath(), currentId +".png", bitmap);
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//                    Log.e(TAG, "The image was not obtained");
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//                    Log.i(TAG, "Getting ready to get the image");
////                    thumbnails.setImageURI(Uri.parse("R.drawable.ic_comment.png"));
//                    //Here you should place a loading gif in the ImageView
//                    //while image is being obtained.
//                }
//            });
//        else
//            Picasso.with(context).load(context.getFilesDir().getPath()+"/"+ currentId+".png").into(thumbnails);

//        if(DatabaseModel.getIsOnline()) {
//            RequestCreator rc = Picasso.with(context).load(holder.current.getThumbnailUrl());
//            rc.into(holder.Thumbnail);
//            try {
//                PicUtils.savePic(context.getFilesDir().getPath(), holder.getId() +".png", rc.get());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Picasso.with(context).load(context.getFilesDir().getPath()+"/"+ holder.getId()+".png").into(holder.Thumbnail);
//        }
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

        public ImageView getThumbnail() {
            return Thumbnail;
        }

        public String getId(){
            return current.getFilename();
        }

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

        public FeedItem getCurrent() {
            return current;
        }
    }
}