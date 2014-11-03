package com.tsaikunyu.itunesmusic.adapater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tsaikunyu.itunesmusic.db.MusicDB;
import com.tsaikunyu.itunesmusic.views.ViewMusicEntry;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TsaiKunYu on 30/10/2014.
 */
public class AdapterMusicList extends BaseAdapter {
    private List<MusicDB.MusicEntryData> mDataList;
    private JSONArray mJarr;
    private Boolean mIsFavList;

    public AdapterMusicList(Boolean isFavList) {
        mIsFavList = isFavList;
        mJarr = new JSONArray();
        mDataList = new ArrayList<MusicDB.MusicEntryData>();
    }

    public void setJArr(JSONArray arr){
        mJarr = arr;
    }

    public void copyList(List<MusicDB.MusicEntryData> list){
        mDataList.clear();
        mDataList.addAll(list);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
//        try {
//            return mJarr.getJSONObject(i);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return null;
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewMusicEntry viewMusicEntry;
        if (view == null) {
            viewMusicEntry = new ViewMusicEntry(viewGroup.getContext());
        } else {
            viewMusicEntry = (ViewMusicEntry) view;
        }

        MusicDB.MusicEntryData data = (MusicDB.MusicEntryData)getItem(i);
        viewMusicEntry.setAppearance(data);
        viewMusicEntry.setClickEvent(data, mIsFavList);
//        viewMusicEntry.setDataByMusicData((MusicDB.MusicEntryData)getItem(i));

        return viewMusicEntry;
    }

    public void updateDataFromDB(){
        copyList(MusicDB.getAll());
        notifyDataSetChanged();
    }
}
