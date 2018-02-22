package com.walmartlabs.android.productlist.ui.product;

import com.walmartlabs.android.productlist.api.ApiError;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;

public interface ProductListContract {
    public interface View {
        public void onProductList(ProductsResponse response);
        public void onProductListFromOnResume(ProductsResponse response);
        public void onLoadingFailed(ApiError apiError);
    }
}
