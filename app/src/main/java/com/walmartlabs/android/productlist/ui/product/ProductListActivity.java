package com.walmartlabs.android.productlist.ui.product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.walmartlabs.android.productlist.R;
import com.walmartlabs.android.productlist.api.ApiFragment;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import com.walmartlabs.android.productlist.ui.DummyContent;
import com.walmartlabs.android.productlist.ui.EndlessRecyclerViewScrollListener;

public class ProductListActivity extends AppCompatActivity implements ProductListContract.View {

    private static final String TAG = ProductListActivity.class.getSimpleName();

    private boolean twoPane;

    private final static String TAG_HEADLESS_FRAGMENT  =  "headless_fragment";
    @BindView(R.id.wm_progress_vg)
    ViewGroup progress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.product_list)
    RecyclerView recyclerView;
    private Presenter presenter;
    private int pageNumber = 0 ;
    private int pageSize = 30;
    private SimpleItemRecyclerViewAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    private

    ApiFragment api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.product_detail_container) != null) {
            twoPane = true;
        }


        adapter = new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new  EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(final int page, final int totalItemsCount,
                final RecyclerView view) {


                    Log.d(TAG, "page=" + page + ", totalItemsCount=" + totalItemsCount);
                    api.getProductList(page+1,pageSize,ProductListActivity.this);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);


        api =
            (ApiFragment)getSupportFragmentManager()
                .findFragmentByTag( TAG_HEADLESS_FRAGMENT);

        if(api == null) {
            api = new ApiFragment();
            getSupportFragmentManager().beginTransaction()
                .add(api, TAG_HEADLESS_FRAGMENT).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        if( api == null)
        {
            Log.d(TAG, "api is null");
        }
        Log.d(TAG, "calling product list");
        api.getProductList(pageNumber,pageSize,this);

    }

    @Override
    public void onProductList(final ProductsResponse response) {
        Log.d(TAG, "onProductList complete" + response.getProducts().size());
        adapter.update(response);
    }
}
