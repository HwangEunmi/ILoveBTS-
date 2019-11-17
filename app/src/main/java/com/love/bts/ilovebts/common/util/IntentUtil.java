package com.love.bts.ilovebts.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Intent 관련 유틸 클래스
 */
public class IntentUtil {
    /**
     * Activity가 현재 존재하는 경우(살아있음)
     *
     * @param context : Context 객체
     * @param intent  : Intent 객체
     * @return
     */
    public static boolean isActivityAvailable(final Context context, final Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        return !activities.isEmpty();
    }

    /**
     * Activity를 안전하게 호출하기
     *
     * @param context : Context 객체
     * @param intent  : Intent 객체
     * @return
     */
    public static boolean safeStartActivity(final Context context, final Intent intent) {
        if (isActivityAvailable(context, intent)) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}