package com.walmartlabs.android.productlist.ui.product_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.walmartlabs.android.productlist.Constants;
import com.walmartlabs.android.productlist.R;
import com.walmartlabs.android.productlist.data.models.Product;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by rjawanda on 2/15/18.
 */

public class NextFragment extends Fragment  {

    private static final String TAG = NextFragment.class.getSimpleName();


    private Product product;

    @BindView(R.id.product_iv) ImageView productIv;
    @BindView(R.id.product_name_tv) TextView productNameTv;
    @BindView(R.id.price_tv) TextView priceTv;
    @BindView(R.id.product_long_description_tv) TextView longDescriptionTv;

    private int screenWidth;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = getArguments().getParcelable(Constants.ARG_CURRENT_PRODUCT);
        screenWidth = getArguments().getInt(Constants.ARG_SCREEN_WIDTH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_next, null);
        ButterKnife.bind(this, view);

        final String url = product.getProductImageUrl();
        Log.d("ronj", "url=" + url);

        if( url != null) {
            Picasso.with(getActivity()).load(Uri.parse(url)).centerInside().resize(screenWidth,screenWidth).into(productIv);
        }


        String productName = product.getProductName();
        if( productName != null) {
            productNameTv.setText(Html.fromHtml(product.getProductName()));
        }
        String price = product.getPrice();
        if( price != null) {
            priceTv.setText(product.getPrice());
        }
        String longDescription = product.getLongDescription();
        if( longDescription != null) {
            longDescriptionTv.setText(Html.fromHtml(product.getLongDescription()));
        }
        return view;
    }

    public boolean isToday(DateTime time) {
        return LocalDate.now().compareTo(new LocalDate(time)) == 0;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }



}
