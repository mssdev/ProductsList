package com.walmartlabs.android.productlist.api;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.walmartlabs.android.productlist.TheApplication;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import com.walmartlabs.android.productlist.ui.product.ProductListContract;

import java.lang.ref.WeakReference;

public class ApiFragment extends Fragment {

    private static final String TAG = ApiFragment.class.getSimpleName();
    private WeakReference<ProductListContract.View> reference;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return null;
    }

    public void getProductList(int pageNumber, int pageSize,
        WeakReference<ProductListContract.View> wr, final boolean isOnResume) {
        Log.d(TAG, "getProductList");

        final ProductListContract.View view = wr.get();

        Log.d(TAG, " ((Activity) view)=" + ((Activity) view).toString());

        final ApiManager apiManager =
            ((TheApplication) ((Activity) view).getApplication()).getApiManager();

        apiManager.getApi().getProducts(pageNumber, pageSize, new WalmartLabsApi.ProductListener() {
            @Override
            public void onFeedSuccessFromOnResume(final ProductsResponse productsResponse) {
                storeFeed(productsResponse, apiManager);
                if (view != null) {
                    storeEtag(productsResponse, apiManager);
                    view.onProductListFromOnResume(productsResponse);


                }
            }

            @Override
            public void onFeedSuccess(final ProductsResponse productsResponse) {
                Log.d(TAG, "theProducts" + productsResponse.getProducts().size());
                storeFeed(productsResponse, apiManager);

                if (view != null) {
                    storeEtag(productsResponse, apiManager);
                    view.onProductList(productsResponse);
                }
            }

            @Override
            public void onFailure(final ApiError apiError) {
                view.onLoadingFailed(apiError);
            }
        });
    }

    private void storeFeed(final ProductsResponse productsResponse, final ApiManager apiManager) {
        Gson gson =new Gson();
        String json = gson.toJson(productsResponse);

        SharedPreferences.Editor editor = apiManager.getSharedPrefs().edit();
        editor.putString("" + productsResponse.getPageNumber(),json);
        editor.commit();
    }

    private void storeEtag(final ProductsResponse productsResponse, final ApiManager apiManager) {
        SharedPreferences.Editor editor = apiManager.getSharedPrefs().edit();
        Log.d(TAG, "----saving etag=" + productsResponse.getEtag());
        editor.putString("etag", productsResponse.getEtag());
        editor.commit();
    }
}
