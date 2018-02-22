package com.walmartlabs.android.productlist.api;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.walmartlabs.android.productlist.Constants;
import com.walmartlabs.android.productlist.TheApplication;
import com.walmartlabs.android.productlist.api.retrofit_services.WalmartLabsRestServices;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import com.walmartlabs.android.productlist.ui.product.ProductListContract;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static junit.framework.Assert.fail;

public class ApiFragment extends Fragment {

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

    public void getProductList(int pageNumber,int pageSize,ProductListContract.View view) {
        Log.d(TAG, "getProductList");
        if(reference == null ) {
            Log.d(TAG, "new weak ref");
            reference = new WeakReference<ProductListContract.View>(view);
        }

           ApiManager apiManager = ((TheApplication)getActivity().getApplication()).getApiManager();

        Log.d(TAG, "apiManager=" + apiManager.toString());

            apiManager.getApi().getProducts(pageNumber, pageSize, new WalmartLabsApi.ProductListener() {
                @Override
                public void onFeedSuccess(final ProductsResponse theProductsResponse) {
                    Log.d(TAG, "theProducts" + theProductsResponse.getProducts().size());
                    if( reference != null) {
                        Log.d(TAG, "reference is not null");
                        ProductListContract.View v = reference.get();
                        if( v != null) {
                         v.onProductList(theProductsResponse);
                        }
                    }else {
                        Log.d(TAG, "reference is  null");

                    }

                }

                @Override
                public void onFailure(final ApiError apiError) {

                }
            });

    }
}
