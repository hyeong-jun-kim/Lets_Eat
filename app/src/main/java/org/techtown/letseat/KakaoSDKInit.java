package org.techtown.letseat;

import android.app.Application;

import com.kakao.auth.KakaoSDK;

public class KakaoSDKInit extends Application {
    private static volatile KakaoSDKInit instance = null;

    public static KakaoSDKInit getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
