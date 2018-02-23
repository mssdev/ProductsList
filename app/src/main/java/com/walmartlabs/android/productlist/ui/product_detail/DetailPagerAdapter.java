package com.walmartlabs.android.productlist.ui.product_detail;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import com.google.gson.Gson;
import com.walmartlabs.android.productlist.Constants;
import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.File;



public class DetailPagerAdapter extends FragmentPagerAdapter {


    private static final String TAG = DetailPagerAdapter.class.getSimpleName();
    private SharedPreferences prefs;
    private List<Product> productList;
    public DetailPagerAdapter(int pageNumber,SharedPreferences prefs,Resources resources, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.prefs = prefs;
        productList = loadProductList(pageNumber, prefs);

    }

    @Override
    public int getCount() {
        return productList.size();
    }



    @Override
    public Fragment getItem(int position) {

        Log.d(TAG, "position=" + position);
        NextFragment nextFragment = new NextFragment();

        Product product = productList.get(position);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_CURRENT_PRODUCT,product);

        nextFragment.setArguments(bundle);

        return nextFragment;
    }

    private List<Product> loadProductList(final int pageNumber, final SharedPreferences prefs) {
        Gson gson = new Gson();
        productList = new ArrayList<Product>();
        for(int i=0;i<=pageNumber;i++) {
            String json = prefs.getString("" + i,null);
            ProductsResponse productsResponse = gson.fromJson(json, ProductsResponse.class);
            productList.addAll(productsResponse.getProducts());
        }
        return productList;
    }

}