package com.walmartlabs.android.productlist.ui.product;

import com.walmartlabs.android.productlist.data.models.ProductsResponse;

public interface ProductListContract {
    public interface View {
        public void onProductList(ProductsResponse response);
    }
}
