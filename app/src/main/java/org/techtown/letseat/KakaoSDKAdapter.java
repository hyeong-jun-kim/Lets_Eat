package org.techtown.letseat;

import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class KakaoSDKAdapter extends KakaoAdapter {
    public ISessionConfig getSessionConfig() {

        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                /**
                 * KAKAO_TALK: 카카오톡으로 로그인,
                 * KAKAO_STORY: 카카오스토리로 로그인,
                 * KAKAO_ACCOUNT: 웹뷰를 통한 로그인,
                 * KAKAO_TALK_EXCLUDE_NATIVE_LOGIN: 카카오톡으로만 로그인+계정 없으면 계정생성 버튼 제공
                 * KAKAO_LOGIN_ALL: 모든 로그인방식 사용 가능. 정확히는, 카카오톡이나 카카오스토리가 있으면 그 쪽으로 로그인 기능을 제공하고, 둘 다 없으면 웹뷰를 통한 로그인을 제공한다.
                 **/

            }

            @Override
            public boolean isUsingWebviewTimer() {
                return false;
                // SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약한다.
                // true 를 리턴할경우 webview로그인을 사용하는 화면서 모든 webview에 onPause와 onResume 시에 Timer를 설정해 주어야 한다.
                // 지정하지 않을 시 false로 설정된다.
            }

            @Override
            public boolean isSecureMode() {
                return false;
                // 로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정한다.
            }

            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
                // 일반 사용자가 아닌 Kakao와 제휴된 앱에서만 사용되는 값으로, 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.
            }

            @Override
            public boolean isSaveFormData() {
                return true;
                // Kakao SDK 에서 사용되는 WebView에서 email 입력폼의 데이터를 저장할지 여부를 결정한다.
                // true일 경우, 다음번에 다시 로그인 시 email 폼을 누르면 이전에 입력했던 이메일이 나타난다.
            }
        };
    }

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Context getApplicationContext() {
                return KakaoSDKInit.getGlobalApplicationContext();
            }
        };
    }
}
