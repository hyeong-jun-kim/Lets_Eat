package org.techtown.letseat;

import android.util.Log;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import org.techtown.letseat.login.Login;

public class KaKaoCallBack implements ISessionCallback {
    Login loginActivity  = new Login();

    @Override
    public void onSessionOpened() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int result = errorResult.getErrorCode();

                if (result == ApiErrorCode.CLIENT_ERROR_CODE) loginActivity.kakaoError("네트워크 연결이 불안정합니다. 다시 시도해 주세요.");
                else loginActivity.kakaoError("로그인 도중 오류가 발생했습니다.");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                loginActivity.kakaoError("세션이 닫혔습니다. 다시 시도해 주세요.");
            }

            @Override
            public void onSuccess(MeV2Response result) {

            }
        });
    }

    @Override
    public void onSessionOpenFailed (KakaoException e){
        loginActivity.kakaoError("로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요.");
    }
}
