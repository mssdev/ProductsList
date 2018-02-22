package com.walmartlabs.android.productlist.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @Expose
    private String productId;
    @Expose
    private String productName;
    @Expose
    private String shortDescription;
    @Expose
    private String longDescription;
    @Expose
    private String price;
    @Expose
    @SerializedName("productImage") // bad name
    private String productImageUrl;
    @Expose
    private Double reviewRating;
    @Expose
    private Integer reviewCount;
    @Expose
    private Boolean inStock;

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(final String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(final String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public Double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(final Double reviewRating) {
        this.reviewRating = reviewRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(final Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(final Boolean inStock) {
        this.inStock = inStock;
    }
}
