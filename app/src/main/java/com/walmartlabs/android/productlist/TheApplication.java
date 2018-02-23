package com.walmartlabs.android.productlist;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.walmartlabs.android.productlist.api.ApiManager;
import com.walmartlabs.android.productlist.api.ApiUtils;


public class TheApplication extends Application {

    private ApiManager apiManager;
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {

            return;

        }
        LeakCanary.install(this);

        apiManager = ApiManager.INSTANCE;
        apiManager.init(new ApiUtils(Constants.BASE_URL,this));

    }

    /***
     * so the fragments and the activities can get at the api
     * @return ApiManager so you can do good stuff
     */
    public ApiManager getApiManager() {
        return apiManager;
    }
}
