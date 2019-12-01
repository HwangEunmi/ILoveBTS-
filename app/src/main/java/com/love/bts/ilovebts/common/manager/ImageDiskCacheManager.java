package com.love.bts.ilovebts.common.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// TODO : https://m.blog.naver.com/pistolcaffe/220611993889
// TODO : https://developer.android.com/topic/performance/graphics/cache-bitmap.html#java
public class ImageDiskCacheManager {

    private Context mContext;

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    private static final String DISK_CACHE_SUBDIR = "i_love_bts_thumbnails"; // Disk Cache 폴더명

    private static final int APP_VERSION = 1; // App Version

    private static final int VALUE_COUNT = 1; // 캐시할 Value의 갯수 (우리는 Bitmap만 하니까 1개)

    private DiskLruCache mDiskLruCache;

    private final Object mDiskCacheLock = new Object();

    private boolean mNeedInitializing = true; // Disk Cache Manager의 초기화 필요 여부 플래그

    public ImageDiskCacheManager(final Context context) {
        this.mContext = context;

        // Initialize Disk Cache on Background Thread
        File cacheDir = getDiskCacheDir(mContext, DISK_CACHE_SUBDIR);
        new InitDiskCacheTask().execute(cacheDir);
    }

    /**
     * Disk Cache를 사용하기 위한 초기 셋팅을 한다.
     */
    private class InitDiskCacheTask extends AsyncTask<File, Void, Void> {

        @Override
        protected Void doInBackground(final File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                try {
                    mDiskLruCache = DiskLruCache.open(cacheDir, APP_VERSION, VALUE_COUNT, DISK_CACHE_SIZE);
                    mNeedInitializing = false;
                    mDiskCacheLock.notifyAll(); // Wake any waiting threads
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(final Integer... params) {
            final String imageKey = String.valueOf(params[0]);

            // Check disk cache in background thread
            // Bitmap bitmap =
            return null;
        }
    }

    /**
     * 지정된 App Cache 디렉토리의 고유한 하위 디렉토리를 만든다.
     */
    private File getDiskCacheDir(final Context context, final String uniqueName) {
        // 외부 저장소가 단말에 내장되어 있다면 (또는 마운트) 외부 캐시 디렉토리를 사용하고
        // 그렇지 않으면 내부 캐시 디렉토리를 사용한다.
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                                 || !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath()
                                                                              : context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    private Bitmap getBitmapFromDiskCache(final String key) {
        Bitmap bitmap = null;
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while(mNeedInitializing) { // Disk cache가 아직 초기화 되지 않은 경우
                try {
                    mDiskCacheLock.wait(); // 초기화 끝날때까지 기다리기
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                // 초기화 작업이 완료된 후
                if(mDiskLruCache != null) {
                    DiskLruCache.Snapshot snapshot = null; // 해당 라이브러리에서 Bitmap 대신 사용하는 데이터 모델
                    try {
                        snapshot = mDiskLruCache.get(key);
                        if(snapshot == null) {
                            return null;
                        }
                        // I/O 처리처럼 오래 걸리는 작업은 Background에서 처리 하는것이 좋다.
                        final InputStream in = snapshot.getInputStream(0);
                        if(in != null) {
                            final BufferedInputStream buffInput = new BufferedInputStream(in, 8 * 1024);
                            bitmap = BitmapFactory.decodeStream(buffInput);
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(snapshot != null) {
                            snapshot.close();
                        }
                    }
                }
            }
        }
        return bitmap;
    }

}

// synchronized(object) 설명 : https://tourspace.tistory.com/54
// object.notifyAll() 설명 : https://everysw.tistory.com/entry/notifyAll-%EA%B3%BC-wait-%EC%82%AC%EC%9A%A9%EC%8B%9C-%EC%A3%BC%EC%9D%98%EC%A0%90
//                           https://github.com/HwangEunmi/-MultiThreading/blob/master/%EC%93%B0%EB%A0%88%EB%93%9C%20%EA%B0%9C%EB%85%90_3.md#notify()%EB%A9%94%EC%86%8C%EB%93%9C-vs-notifyAll()%EB%A9%94%EC%86%8C%EB%93%9C
// Environment.getExternalStorageState() 설명 : http://www.gisdeveloper.co.kr/?p=1272
