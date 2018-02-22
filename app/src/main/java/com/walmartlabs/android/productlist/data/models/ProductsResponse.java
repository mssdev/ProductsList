package com.walmartlabs.android.productlist.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductsResponse {
    @Expose
    private String id;
    @Expose
    private Integer totalProducts;
    @Expose
    private Integer pageNumber;
    @Expose
    private Integer pageSize;
    @Expose
    @SerializedName("status") // wow this is strange. Is it possible success returns failure?
    private Integer httpStatus;
    @Expose
    private String kind;
    @Expose
    private String etag;

    @Expose
    private List<Product> products;

    public String getId() {
        return id;
    }

    public Integer getTotalProducts() {
        return totalProducts;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public List<Product> getProducts() {
        return products;
    }
}
