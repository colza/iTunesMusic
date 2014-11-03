package com.tsaikunyu.itunesmusic.datahandle;

import android.content.Context;

import com.tsaikunyu.itunesmusic.R;
import com.tsaikunyu.itunesmusic.constant.JsonMusic;
import com.tsaikunyu.itunesmusic.db.MusicDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TsaiKunYu on 31/10/2014.
 */
public class JsonHandle {


    public static MusicDB.MusicEntryData transferJsonToData(Context ct, JSONObject jobj) {
        MusicDB.MusicEntryData data = new MusicDB.MusicEntryData();
        try {
            if( jobj != null ){
                data.imageUrl = jobj.getJSONArray(ct.getResources().getString(R.string.itunes_image)).getJSONObject(JsonMusic.LARGE_IMAGE).getString(ct.getResources().getString(R.string.label));
                data.singerName = jobj.getJSONObject(ct.getResources().getString(R.string.itunes_artist)).getString(ct.getResources().getString(R.string.label));
                data.songName = jobj.getJSONObject(ct.getResources().getString(R.string.itunes_name)).getString(ct.getResources().getString(R.string.label));
                data.price = jobj.getJSONObject(ct.getResources().getString(R.string.itunes_price)).getString(ct.getResources().getString(R.string.label));
                data.type = jobj.getJSONObject(ct.getResources().getString(R.string.itunes_collection)).getJSONObject(ct.getResources().getString(R.string.attributes)).getString(ct.getResources().getString(R.string.term));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static JSONArray extractMusicJArrFromRawData(Context ct, String rawStr){
        try {

            JSONObject rawJson = new JSONObject(rawStr);
            JSONArray musicEntryArr = rawJson.getJSONObject(ct.getResources().getString(R.string.feed))
                    .getJSONArray(ct.getResources().getString(R.string.entry));

            return musicEntryArr;
        } catch (JSONException e) {
            e.printStackTrace();

            return new JSONArray();
        }
    }

    public static List<MusicDB.MusicEntryData> transferJArrToDataList(Context ct, JSONArray array){
        List<MusicDB.MusicEntryData> list = new ArrayList<MusicDB.MusicEntryData>();
        for( int i = 0 ; i < array.length(); i++){
            try {
                JSONObject obj = array.getJSONObject(i);
                MusicDB.MusicEntryData data = transferJsonToData(ct, obj);
                if( data != null ){
                    list.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
