package com.walmartlabs.android.productlist.ui.product;

public class Presenter {
    private ProductListContract.View view;
    public Presenter(ProductListContract.View view) {
        this.view = view;
    }

    protected void getProductList() {

    }
}
