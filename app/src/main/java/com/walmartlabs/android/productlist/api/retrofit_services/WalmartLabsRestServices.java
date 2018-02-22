package com.walmartlabs.android.productlist.api.retrofit_services;

import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WalmartLabsRestServices {
    @Headers({ "Content-Type:application/json", "charset:UTF-8" })
    @GET("walmartproducts/f545c4be-0f79-42c9-8616-3baff5c8aa5a/{pageNumber}/{pageSize}")
    Call<ProductsResponse> getProducts(@Path("pageNumber") int pageNumber,
        @Path("pageSize") int pageSize);
}
