package com.love.bts.ilovebts.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.love.bts.ilovebts.R;

/**
 * 다음 카페 화면
 */
public class DaumCafeFragment extends Fragment {

    public static DaumCafeFragment newInstance() {
        final DaumCafeFragment fragment = new DaumCafeFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daum_cafe, container, false);

        return view;
    }

}
