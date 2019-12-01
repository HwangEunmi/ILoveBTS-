package com.love.bts.ilovebts.common.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageMemoryCacheManager {

    private static Context mContext;

    /**
     * 메모리 캐시는 LruCache를 사용한다.
     */
    private LruCache<String, Bitmap> mMemoryCache;

    private final Object mMemoryCacheLock = new Object();

    private static class ImageMemoryCacheManagerHolder {

        public static final ImageMemoryCacheManager MANAGER = new ImageMemoryCacheManager();
    }

    public static ImageMemoryCacheManager getInstance(final Context context) {
        mContext = context;
        return ImageMemoryCacheManagerHolder.MANAGER;
    }

    public ImageMemoryCacheManager() {
        // 메모리 캐시가 사용할 수 있는 가용 메모리를 설정한다.
        final int memoryClass = ((ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int maxSize = 1024 * 1024 * memoryClass / 8; // 앱이 사용할 수 있는 메모리의 1/8 을 사용한다.

        mMemoryCache = new LruCache<String, Bitmap>(maxSize) {

            @Override
            protected int sizeOf(final String key, final Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * Bitmap을 Memory Cache에 저장한다.
     *
     * @param key : 키값 (LruCache는 내부적으로 LinkedHashMap 를 사용)
     * @param bitmap : Bitmap 객체
     */
    public void addBitmapToMemoryCache(final String key, final Bitmap bitmap) {
        if ((getBitmapFromMemoryCache(key) != null) || bitmap == null) {
            return;
        }
        synchronized (mMemoryCacheLock) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 해당하는 Key값의 Bitmap을 리턴한다.
     *
     * @param key : 키값
     * @return
     */
    public Bitmap getBitmapFromMemoryCache(final String key) {
        synchronized (mMemoryCacheLock) {
            return mMemoryCache.get(key);
        }
    }

}

// Singleton 패턴은 호출할때마다 동일한 인스턴스를 사용하기 위해 사용하는 것 (자원을 공유한다던지)
// 질문 1 : DB매니저처럼 모든 화면에서 공통으로 사용하는 객체는 무조건 Singleton 패턴으로 생성??
//          -> 보통 Singleton을 많이 사용함
// Multi Thread 환경에선 Singleton패턴을 사용하면 문제점이 있음 (Ex. 객체 여러개 생성 또는 같은 자원에 동시에 접근)
// 이러한 문제점은 MultiThread에 대응하는 Singleton 패턴(lazy initialize 등) 또는 synchronized 키워드를 사용하면 되지만,
// synchronized키워드는 여러 스레드가 동시에 접근할 경우 lock이 걸려 지연이 생김
// 질문 2 : 그러면 Multi Thread 환경에선 synchronized 키워드를 사용하는 것 보다 lazy initialize 같은 Singleton 패턴을 사용하는것이 좋음?
//          -> lazy initialize가 더 유연함
// 질문 4 : 어떤 글을 보면 Multi Thread 환경에서 공유할 객체 아니라면 Singleton 말고 new로 새로 객체를 생성해주는 것이 좋다는데,
//          그러면 Retrofit같은 통신 라이브러리는 enqueue() 호출해서 동작하니 따지고보면 Multi Thread, 그러면 이러한 매니저는 그때마다 new로 객체를 생성해주는 것이 좋음?
//          아니면 lazy initialize 같은 Singleton 패턴을 사용하는것이 좋음?
// 참고 URL : https://tourspace.tistory.com/55
//           -> 보통 Manager같은 Transaction은 Singleton을 많이 사용함
//              사실 8차선을 뚫어놓고 1차선만 사용할것인지 (synchronized 키워드), 1차선만 뚫어놓고 1차선만 사용할것인지(Singleton패턴) 그건 방법의 차이

