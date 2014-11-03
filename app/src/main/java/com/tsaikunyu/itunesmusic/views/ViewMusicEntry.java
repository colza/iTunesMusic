package com.tsaikunyu.itunesmusic.views;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tsaikunyu.itunesmusic.R;
import com.tsaikunyu.itunesmusic.adapater.AdapterMusicList;
import com.tsaikunyu.itunesmusic.db.MusicDB;

import co.herxun.library.singleton.StResol;

/**
 * Created by TsaiKunYu on 30/10/2014.
 */
public class ViewMusicEntry extends RelativeLayout {
    private StResol mResol;
    private ImageView mImage;
    private TextView mSongInfo;
    private TextView mPrice;
    private TextView mType;
    private ImageButton mButtonAddFavorite;
    private ImageButton mButtonDownLoadPic;

    public ViewMusicEntry(Context context) {
        super(context);
        mResol = StResol.getInstance(context);

        mImage = new ImageView(context);
        mImage.setId(mResol.id++);

        mButtonAddFavorite = mResol.mUI.mImg.imgBtn(context, R.drawable.sel_add_fav, mResol.id++);
        mButtonDownLoadPic = mResol.mUI.mImg.imgBtn(context, R.drawable.sel_download_pic, mResol.id++);

        mSongInfo = mResol.mUI.mTv.textInit(context, 25, Color.BLACK, null, mResol.id++, "");
        mPrice = mResol.mUI.mTv.textInit(context, 25, Color.BLACK, null, mResol.id++, "");
        mType = mResol.mUI.mTv.textInit(context, 25, Color.BLACK, null, mResol.id++, "");

        addView(mImage, mResol.mUI.mLayout.relParam(120, 120, new int[]{RelativeLayout.CENTER_VERTICAL, RelativeLayout.ALIGN_PARENT_LEFT}, new int[]{20, 20, 20, 20}));

        addView(mButtonAddFavorite, mResol.mUI.mLayout.relParam(50, 50, new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL}, new int[]{0, 0, 20, 0}));
        addView(mButtonDownLoadPic, mResol.mUI.mLayout.relParam(45, 45, new int[]{RelativeLayout.LEFT_OF, mButtonAddFavorite.getId(), RelativeLayout.CENTER_VERTICAL}, new int[]{10, 0, 30, 0}));

        LinearLayout layoutText = new LinearLayout(context);
        layoutText.setOrientation(LinearLayout.VERTICAL);
        layoutText.addView(mSongInfo);
        layoutText.addView(mPrice);
        layoutText.addView(mType);
        addView(layoutText, mResol.mUI.mLayout.relParam(-2, -2, new int[]{RelativeLayout.CENTER_VERTICAL, RelativeLayout.RIGHT_OF, mImage.getId(), RelativeLayout.LEFT_OF, mButtonDownLoadPic.getId()}));
    }


    public void setAppearance(MusicDB.MusicEntryData data){
        if (data != null) {
            mSongInfo.setText(data.songName + " - " + data.singerName);
            mType.setText("Type: " + data.type);
            mPrice.setText("Price: " + data.price);
            Picasso.with(getContext()).load(data.imageUrl).into(mImage);

            if( MusicDB.isDataExist(data) == true ){
                mButtonAddFavorite.setSelected(true);
            }else{
                mButtonAddFavorite.setSelected(false);
            }
        }
    }

    public void setClickEvent(final MusicDB.MusicEntryData data, final Boolean isFavList){
        mButtonAddFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mButtonAddFavorite.isSelected() ){
                    MusicDB.removeDataFromDB(data);
                    mButtonAddFavorite.setSelected(false);

                    if( isFavList == true ){
                        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                        anim.setDuration(500);
                        anim.setRepeatCount(0);
                        anim.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(
                                    Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                pointToListViewAndUpdateDBData();
                            }
                        });

                        ViewMusicEntry.this.startAnimation(anim);
                    }

                }else{
                    MusicDB.saveDataIntoDB(data);
                    mButtonAddFavorite.setSelected(true);
                }
            }
        });

        mButtonDownLoadPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImageFromUrl(view.getContext(), data);
            }
        });
    }

    private void downloadImageFromUrl(Context ct, MusicDB.MusicEntryData data){
        DownloadManager mgr = (DownloadManager) ct.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri dlUri = Uri.parse(data.imageUrl);
        DownloadManager.Request req = new DownloadManager.Request(dlUri);

        String name = data.songName+" - " + data.singerName;
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI)
                .setTitle(name)
                .setAllowedOverRoaming(false)
                .setDescription("Image Downloading")
                .setDestinationInExternalFilesDir( ct , Environment.DIRECTORY_DOWNLOADS, name);

        mgr.enqueue(req);
    }

    private void pointToListViewAndUpdateDBData(){
        ViewParent parent = ViewMusicEntry.this.getParent();
        if( parent != null && parent instanceof ListView){
            ListView listView = (ListView)parent;
            AdapterMusicList ada = (AdapterMusicList) listView.getAdapter();
            ada.updateDataFromDB();
        }
    }

    public void setDataByMusicData(final MusicDB.MusicEntryData data) {
        if (data != null) {
//            final MusicDB.MusicEntryData data = transferJsonToData(jobj);

            mSongInfo.setText(data.songName + " - " + data.singerName);
            mType.setText("Type: " + data.type);
            mPrice.setText("Price: " + data.price);
            Picasso.with(getContext()).load(data.imageUrl).into(mImage);

            if( MusicDB.isDataExist(data) == true ){
                mButtonAddFavorite.setSelected(true);
            }else{
                mButtonAddFavorite.setSelected(false);
            }

            mButtonAddFavorite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( mButtonAddFavorite.isSelected() ){
                        MusicDB.removeDataFromDB(data);
                        mButtonAddFavorite.setSelected(false);
                    }else{
                        MusicDB.saveDataIntoDB(data);
                        mButtonAddFavorite.setSelected(true);
                    }
                }
            });
        }
    }


}
