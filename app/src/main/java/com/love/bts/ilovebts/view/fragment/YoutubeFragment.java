package com.love.bts.ilovebts.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.love.bts.ilovebts.R;

/**
 * 유투브 화면
 */
public class YoutubeFragment extends Fragment {

    public static YoutubeFragment newInstance() {
        final YoutubeFragment fragment = new YoutubeFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        return view;
    }

}
