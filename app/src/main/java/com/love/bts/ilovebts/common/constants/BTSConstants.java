package com.love.bts.ilovebts.common.constants;

/*공통 상수*/
public class BTSConstants {

    // Url Value
    public class Url {
        public static final String BASE_URL = "https://api.twitter.com/1.1";
        // TimeLine 조회
        public static final String GET_USER_TIMELINE = "statuses/mentions_timeline.json";
    }

    // ViewPager 상수
    public class Home {
        public static final int PAGE_TWITTER = 0;
        public static final int PAGE_YOUTUBE = 1;
        public static final int PAGE_DAUM_CAFE = 2;
        public static final int PAGE_COUNT = 3;
    }

    public class TAG {
        public static final String PICASSO_TAG = "picasso_tag";
    }


}
