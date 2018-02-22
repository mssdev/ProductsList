package com.walmartlabs.android.productlist.api;

import android.content.Context;
import com.walmartlabs.android.productlist.Constants;
import java.lang.ref.SoftReference;

/***
 * the api manager, if we had more than one Api it would make more sense.
 * if the app is sitting around doing nothing the garbage collector can
 * let go of the api due to the soft reference. When we need it again we lazy load it
 */
public enum ApiManager {
    INSTANCE;
    private static final String TAG = ApiManager.class.getSimpleName();
    // this ApiManager is going to hang around forever so holding the app context is ok


    private ApiUtils apiUtils ;


    private SoftReference<WalmartLabsApi> walmartLabsApiRef;

    public WalmartLabsApi getApi() {


        if (walmartLabsApiRef == null || walmartLabsApiRef.get() == null) {

            final WalmartLabsApi walmartLabsApi = new WalmartLabsApi(apiUtils);
            walmartLabsApiRef = new SoftReference<WalmartLabsApi>(walmartLabsApi);
        }else if( !apiUtils.getBaseUrl().equals(walmartLabsApiRef.get().getBaseUrl())){
            // the base url is different, we need to create a new api
            final WalmartLabsApi walmartLabsApi = new WalmartLabsApi(apiUtils);
            walmartLabsApiRef = new SoftReference<WalmartLabsApi>(walmartLabsApi);

        }
        return walmartLabsApiRef.get();
    }

    public void init(ApiUtils apiUtils) {

        this.apiUtils = apiUtils;




    }
}
