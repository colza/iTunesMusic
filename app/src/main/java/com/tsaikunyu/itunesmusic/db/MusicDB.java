package com.tsaikunyu.itunesmusic.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by TsaiKunYu on 30/10/2014.
 */
public class MusicDB {

    public static List<MusicEntryData> getAll() {
        return new Select().from(MusicEntryData.class).execute();
    }

    public static void removeDataFromDB(MusicEntryData data) {
        MusicEntryData dataFromDB = fetchDataFromDB(data);
        if( dataFromDB != null)
            dataFromDB.delete();
    }

    public static MusicEntryData fetchDataFromDB(MusicEntryData data){
        MusicEntryData dataFromDB = new Select().from(MusicEntryData.class).where("SongName = ? AND SingerName = ?", data.songName, data.singerName).executeSingle();
        return dataFromDB;
    }

    public static Boolean isDataExist(MusicEntryData data) {
        if ( fetchDataFromDB(data) == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void saveDataIntoDB(MusicEntryData data) {
        if (isDataExist(data) == false) {
            data.save();
        }
    }

    @Table(name = "MusicEntryList")
    public static class MusicEntryData extends Model {
        @Column(name = "SingerName")
        public String singerName;

        @Column(name = "SongName")
        public String songName;

        @Column(name = "ImageUrl")
        public String imageUrl;

        @Column(name = "Price")
        public String price;

        @Column(name = "Type")
        public String type;
    }
}
