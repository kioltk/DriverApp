package com.driverapp.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.driverapp.android.BuildConfig;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKLoginUtil {
    private final Activity activity;
    private final VKLoginListener loginListener;
    private final int buttonResId;
    public String email;
    public String userId;
    public String token;
    public String secret;
    public String userFirstName;
    public String userLastName;
    public String userPhotoUrl;

    public VKLoginUtil(Activity activity, final VKLoginListener loginListener, int buttonResId) {
        this.activity = activity;
        this.loginListener = loginListener;
        this.buttonResId = buttonResId;

        activity.findViewById(buttonResId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VKSdkListener listener = new VKSdkListener() {
                    @Override
                    public void onReceiveNewToken(VKAccessToken newToken) {
                        userId = newToken.userId;
                        email = newToken.email;
                        token = newToken.accessToken;
                        secret = newToken.secret;
                        new VKApiUsers().get(new VKParameters(){{ put("fields","photo_200"); }}).executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                VKApiUser user = ((VKList<VKApiUserFull>)response.parsedModel).get(0);
                                userFirstName = user.first_name;
                                userLastName = user.last_name;
                                userPhotoUrl = user.photo_200;
                                if(loginListener!=null){
                                    loginListener.vkAuthorized();
                                }
                            }

                            @Override
                            public void onError(VKError error) {

                            }
                        });

                    }

                    @Override
                    public void onCaptchaError(VKError captchaError) {

                    }

                    @Override
                    public void onTokenExpired(VKAccessToken expiredToken) {

                    }

                    @Override
                    public void onAccessDenied(VKError authorizationError) {

                    }
                };
                if(BuildConfig.DEBUG) {
                    VKSdk.initialize(listener, "4896116");
                } else {
                    VKSdk.initialize(listener, "4895639");
                }

                VKSdk.authorize(VKScope.OFFLINE, VKScope.WALL, VKScope.NOHTTPS);

            }
        });

    }

    public static void onCreate(Activity activity) {
        VKUIHelper.onCreate(activity);
    }

    public static void onResume(Activity activity) {
        VKUIHelper.onResume(activity);
    }

    public static void onDestroy(Activity activity) {
        VKUIHelper.onDestroy(activity);
    }

    public boolean onActivityResult(int requestCode, int responseCode, Intent intent) {
        return VKUIHelper.onActivityResult(requestCode, responseCode, intent);
    }

    public static interface VKLoginListener{
        void vkAuthorized();
    }
}
