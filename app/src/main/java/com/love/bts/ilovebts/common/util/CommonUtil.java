package com.love.bts.ilovebts.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * 공통 유틸
 */
public class CommonUtil {

    /**
     * 기기에 해당 앱이 설치되어 있는지 확인
     * (만약 설치되어있지 않은 경우 구글마켓으로 이동)
     *
     * @param context : Context 객체
     * @param :       해당 앱의 패키지명
     */
    public static void checkInstallTwitter(final Context context, final String packageName) {
        final PackageManager pm = context.getPackageManager();
        Intent intent = null;
        try {
            pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            intent = pm.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) { // 설치되어있지 않은 경우 구글마켓으로 이동
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

}
