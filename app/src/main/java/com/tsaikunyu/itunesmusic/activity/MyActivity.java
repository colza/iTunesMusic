package com.tsaikunyu.itunesmusic.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.tsaikunyu.itunesmusic.R;
import com.tsaikunyu.itunesmusic.constant.BundleKey;
import com.tsaikunyu.itunesmusic.frag.Tab1iTunesMusicList;
import com.tsaikunyu.itunesmusic.frag.Tab2iTunesFavoriteList;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab1 = ab.newTab().setText(R.string.tab1_title)
                .setTabListener(new MainTabListener<Tab1iTunesMusicList>(this, Tab1iTunesMusicList.class.getName(), Tab1iTunesMusicList.class
                ));
        ab.addTab(tab1);

        ActionBar.Tab tab2 = ab.newTab().setText(R.string.tab2_title)
                .setTabListener(new MainTabListener<Tab2iTunesFavoriteList>(this, Tab2iTunesFavoriteList.class.getName(), Tab2iTunesFavoriteList.class));
        ab.addTab(tab2);

        if( savedInstanceState != null ){
            ab.setSelectedNavigationItem(savedInstanceState.getInt(BundleKey.TAB_POSITION,0));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BundleKey.TAB_POSITION, getActionBar().getSelectedNavigationIndex());
    }

    public static class MainTabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFrag;
        private Activity mAct;
        private String mTag;
        private Class<T> mClass;

        public MainTabListener(Activity act, String tag, Class<T> cls) {
            mAct = act;
            mTag = tag;
            mClass = cls;

            mFrag = mAct.getFragmentManager().findFragmentByTag(mTag);
            if( mFrag != null && !mFrag.isDetached() ){
                FragmentTransaction ft = mAct.getFragmentManager().beginTransaction();
                ft.detach(mFrag);
                ft.commit();
            }
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            if (mFrag == null) {
                mFrag = Fragment.instantiate(mAct, mClass.getName());
                fragmentTransaction.add(android.R.id.content, mFrag, mTag);
            } else {
                fragmentTransaction.attach(mFrag);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            if (mFrag != null) {
                fragmentTransaction.detach(mFrag);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
