package com.tsaikunyu.itunesmusic.frag;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tsaikunyu.itunesmusic.R;
import com.tsaikunyu.itunesmusic.adapater.AdapterMusicList;
import com.tsaikunyu.itunesmusic.constant.BundleKey;
import com.tsaikunyu.itunesmusic.datahandle.JsonHandle;

import org.json.JSONArray;

public class Tab1iTunesMusicList extends Fragment {
    private ListView mListView;
    private AdapterMusicList mAdapter;
    private RequestQueue mVolleyQueue;
    private Parcelable mStateOfListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( savedInstanceState != null ){
            mStateOfListView = savedInstanceState.getParcelable(BundleKey.LIST_INSTANCE_STATE);
        }

        mVolleyQueue = Volley.newRequestQueue(getActivity());
        mAdapter = new AdapterMusicList(false);

        updateJsonFromRemote(getResources().getString(R.string.fetch_itunes_address));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BundleKey.LIST_INSTANCE_STATE, mListView.onSaveInstanceState());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if( mListView == null ){
            mListView = new ListView(getActivity());
            mListView.setAdapter(mAdapter);
        }

        return mListView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.notifyDataSetChanged();

        if (mStateOfListView != null) {
            mListView.onRestoreInstanceState(mStateOfListView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStateOfListView = mListView.onSaveInstanceState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void updateJsonFromRemote(String remoteUrl){
        StringRequest strReq = new StringRequest(Request.Method.GET, remoteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray arr = JsonHandle.extractMusicJArrFromRawData(getActivity(), response);
                if( arr.length() != 0 ){

                    mAdapter.copyList(JsonHandle.transferJArrToDataList(getActivity(), arr));
                    mAdapter.notifyDataSetChanged();

                    if( mStateOfListView != null ){
                        mListView.onRestoreInstanceState(mStateOfListView);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Fetch iTunes data failed, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        mVolleyQueue.add(strReq);
    }

}
