package com.love.bts.ilovebts.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.love.bts.ilovebts.common.constants.BTSConstants;
import com.love.bts.ilovebts.view.fragment.DaumCafeFragment;
import com.love.bts.ilovebts.view.fragment.TwitterFragment;
import com.love.bts.ilovebts.view.fragment.YoutubeFragment;

public class GroundViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public GroundViewPagerAdapter(final FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case BTSConstants.Home.PAGE_TWITTER:
                return TwitterFragment.newInstance();
            case BTSConstants.Home.PAGE_YOUTUBE:
                return YoutubeFragment.newInstance();
            case BTSConstants.Home.PAGE_DAUM_CAFE:
                return DaumCafeFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return BTSConstants.Home.PAGE_COUNT;
    }

    /**
     * 트위터 타임라인 갱신
     */
    private void refreshTwitterTimeline() {
        // TODO : TwitterFragment를 호출해야하는데 어떻게 호출할지 생각하기
    }
    // http://www.hardcopyworld.com/ngine/android/index.php/archives/164
    // https://itpangpang.xyz/284
}
