package com.walmartlabs.android.productlist.ui.product_detail;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import com.walmartlabs.android.productlist.Constants;
import com.walmartlabs.android.productlist.R;
import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.ui.DummyContent;
import com.walmartlabs.android.productlist.ui.product.ProductListActivity;

/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a {@link ProductListActivity}
 * in two-pane mode (on tablets) or a {@link ProductDetailActivity}
 * on handsets.
 */
public class ProductDetailFragment extends Fragment {

    private Product product;
    private int currentPage;
    private ViewPager pager;
    private int adapterPosition;

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.ARG_CURRENT_PAGE)) {

            product = getArguments().getParcelable(Constants.ARG_CURRENT_PRODUCT);
            currentPage = getArguments().getInt(Constants.ARG_CURRENT_PAGE);
            adapterPosition = getArguments().getInt(Constants.ARG_ADAPTER_POSITION);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        pager = rootView.findViewById(R.id.pager);
        pager.setAdapter(new DetailPagerAdapter(currentPage,getActivity().getSharedPreferences("data",
            Context.MODE_PRIVATE),getResources(),getFragmentManager()));
        pager.setCurrentItem(adapterPosition);



        return rootView;
    }
}
