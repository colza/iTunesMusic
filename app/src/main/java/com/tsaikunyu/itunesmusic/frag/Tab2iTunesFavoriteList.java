package com.tsaikunyu.itunesmusic.frag;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tsaikunyu.itunesmusic.adapater.AdapterMusicList;
import com.tsaikunyu.itunesmusic.constant.BundleKey;

public class Tab2iTunesFavoriteList extends Fragment {
    private ListView mListView;
    private AdapterMusicList mAdapter;
    private Parcelable mStateOfListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null ){
            mStateOfListView = savedInstanceState.getParcelable(BundleKey.LIST_INSTANCE_STATE);
        }

        mAdapter = new AdapterMusicList(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleKey.LIST_INSTANCE_STATE, mListView.onSaveInstanceState());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if( mListView == null){
            mListView = new ListView(getActivity());
            mListView.setAdapter(mAdapter);
        }

        return mListView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter.updateDataFromDB();

        if( mStateOfListView != null ){
            mListView.onRestoreInstanceState(mStateOfListView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStateOfListView = mListView.onSaveInstanceState();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
