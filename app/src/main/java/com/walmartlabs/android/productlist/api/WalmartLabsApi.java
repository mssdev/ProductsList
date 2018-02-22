package com.walmartlabs.android.productlist.api;

import android.os.Handler;
import android.os.Looper;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import com.walmartlabs.android.productlist.api.retrofit_services.WalmartLabsRestServices;
import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.util.List;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WalmartLabsApi {
    private static final String TAG = WalmartLabsApi.class.getSimpleName();
    private ApiUtils apiUtils;
    private String baseUrl;
    private Handler handler;

    public WalmartLabsApi(ApiUtils apiUtils) {
        this.apiUtils = apiUtils;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public String getBaseUrl() {
        return apiUtils.getBaseUrl();
    }

    public interface ProductListener {

        public void onFeedSuccessFromOnResume(ProductsResponse productsResponse);

        public void onFeedSuccess(ProductsResponse productsResponse);
        public void onFailure(ApiError apiError);
    }


    public void getProducts(int pageNumber,int pageSize,final ProductListener listener) {

        Log.d(TAG, "getProducts()");


        WalmartLabsRestServices service = apiUtils.getService();

        if( service == null) {
            Log.d(TAG, "service is null");
        }
        Log.d(TAG, "service");

        Call<ProductsResponse> call = service.getProducts(apiUtils.getSharedPreferences().getString("etag",""),pageNumber,pageSize);

        Log.d(TAG, "call enqueue" + call.toString());

        call.enqueue(new Callback<ProductsResponse>() {
             @Override
             public void onResponse(final Call<ProductsResponse> call,
                 final Response<ProductsResponse> response) {


                     Log.d(TAG, "response is Successful=" + response.isSuccessful() + ",code=" + response.code());

                     // i would put this into an Interceptor

                     if (response.code() >= HttpURLConnection.HTTP_OK && response.code() < HttpURLConnection.HTTP_MULT_CHOICE) {
                         Log.d(TAG, "in <>");
                         final ProductsResponse productsResponse = response.body();


                         if (productsResponse == null) {
                             Log.d(TAG, "productsREsponse is null");
                         }

                         Log.d(TAG, "pro" + productsResponse.getPageNumber());
                         Log.d(TAG, "pro" + productsResponse.getProducts().size());

                         Log.d(TAG, "Thread=" + Thread.currentThread().getName());

                         handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 Log.d(TAG, "in run() Thread=" + Thread.currentThread().getName());
                                 Log.d(TAG, "onFeedSuccess");
                                 if (listener == null) {
                                     Log.d(TAG, "listener is null");
                                 }
                                 listener.onFeedSuccess(productsResponse);
                             }
                         });
                     } else {
                         Log.d(TAG, "Failure" + response.code());

                         handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 listener.onFailure(new ApiError(Integer.valueOf(response.code()),
                                     response.message(), false));
                             }
                         });
                     }

             }

             @Override
             public void onFailure(final Call<ProductsResponse> call, final Throwable t) {

                 Log.d(TAG, "onFailure()");


             }
         });


    }


}
