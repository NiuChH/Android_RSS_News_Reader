package com.java.niuchenhao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.bean.FeedItem;

import java.util.ArrayList;
import java.util.List;


public class NewsListFragment extends Fragment {

//    private static final String ARG_XML_URL = "xmlUrl";

    private static final String ARG_CHANNEL_ITEM = "channel item";

    private ChannelItem channelItem;

//    private String xmlUrl;

//    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout swipeRefresh;

    private FeedsAdapter adapter = null;

    private List<FeedItem> feedItems;

    private RecyclerView recyclerView;

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static NewsListFragment newInstance(ChannelItem channelItem) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHANNEL_ITEM, channelItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NewsListFragment", "create");
        try{
            channelItem = (ChannelItem) getArguments().getSerializable(ARG_CHANNEL_ITEM);
            feedItems = FeedsPresenter.getFeedItemList(channelItem);
            adapter = new FeedsAdapter(getContext(), feedItems, channelItem);
            FeedsPresenter.queryFeedItemList(channelItem, null,10, Boolean.TRUE);
        } catch (ClassCastException e){
            Log.e("ARG_CHANNEL_ITEM", "not a channelIte!");
            e.printStackTrace();
        } catch (NullPointerException e){
            Log.e("ARG_CHANNEL_ITEM", "empty!");
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        final Context context = rootView.getContext();

        recyclerView = rootView.findViewById(R.id.recyclerview);
        swipeRefresh = rootView.findViewById(R.id.swipe_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpace(20));
        recyclerView.setAdapter(adapter);
        //Call Read rss async task to fetch rss

//        new ReadRss(channelItem, adapter, swipeRefresh, feedItems).execute(10, ReadRss.REFRESH);
        FeedsPresenter.setSwipeRefreshLayout(channelItem, swipeRefresh);
        FeedsPresenter.queryFeedItemList(channelItem, null,10, Boolean.FALSE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if(newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
//                    new ReadRss(channelItem, adapter, swipeRefresh, feedItems).execute(10, ReadRss.APPEND);
                    FeedsPresenter.queryFeedItemList(channelItem,null, 10, Boolean.TRUE);
                }
            }
        });


        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new ReadRss(channelItem, adapter, swipeRefresh, feedItems).execute(10, ReadRss.REFRESH);
                FeedsPresenter.queryFeedItemList(channelItem, null,10, Boolean.FALSE);
            }
        });

        return rootView;
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
