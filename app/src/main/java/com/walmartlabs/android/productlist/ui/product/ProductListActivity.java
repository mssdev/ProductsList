package com.walmartlabs.android.productlist.ui.product;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.walmartlabs.android.productlist.Constants;
import com.walmartlabs.android.productlist.R;
import com.walmartlabs.android.productlist.TheApplication;
import com.walmartlabs.android.productlist.api.ApiError;
import com.walmartlabs.android.productlist.api.ApiFragment;
import com.walmartlabs.android.productlist.api.ApiManager;
import com.walmartlabs.android.productlist.api.WalmartLabsApi;
import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import com.walmartlabs.android.productlist.ui.DummyContent;
import com.walmartlabs.android.productlist.ui.EndlessRecyclerViewScrollListener;
import com.walmartlabs.android.productlist.ui.widgets.DividerDecoratorItem;
import com.walmartlabs.android.productlist.ui.widgets.ToastExt;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ProductListActivity extends AppCompatActivity implements ProductListContract.View {

    private static final String TAG = ProductListActivity.class.getSimpleName();
    private static final String LIST_STATE_KEY ="list_state_key";
    private final static String TAG_HEADLESS_FRAGMENT  =  "headless_fragment";

    @BindView(R.id.wm_progress_vg)
    ViewGroup progress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.product_list)
    RecyclerView recyclerView;


    private boolean twoPane;
    private ToastExt toastExt;


    private int pageNumber = 0 ;
    private int pageSize = 20;

    private SimpleItemRecyclerViewAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ApiManager apiManager;
    private int recyclerViewFirstVisiblePos;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable state;
    private ApiFragment api;


    /* lifecycle events
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        toastExt = new ToastExt(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.product_detail_container) != null) {
            twoPane = true;
        }

        apiManager = ((TheApplication)getApplication()).getApiManager();


        adapter = new SimpleItemRecyclerViewAdapter(this, new ArrayList<Product>(), twoPane);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d(TAG, "Activity instance= " + this.toString());
        scrollListener = new  EndlessRecyclerViewScrollListener(linearLayoutManager) {

            // handle the page scrolling to load more data.
            @Override
            public void onLoadMore(final int page, final int totalItemsCount,
                final RecyclerView view) {
                    progress.setVisibility(View.VISIBLE);
                    Log.d(TAG, "page=" + page + ", totalItemsCount=" + totalItemsCount);
                    pageNumber = page;
                    WeakReference<ProductListContract.View> wr = new WeakReference<ProductListContract.View>(ProductListActivity.this);
                    api.getProductList(page,pageSize,wr,false);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.addItemDecoration(
            new DividerDecoratorItem(this, R.drawable.list_separator));
        recyclerView.setAdapter(adapter);
        if( state != null) {
            linearLayoutManager.onRestoreInstanceState(state);
        }

        // try to find the api first. It has setRetainInstance to true
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
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putInt(Constants.SAVE_STATE_PAGE_NUMBER, pageNumber);

        // save the recycler view layout manager position and after data is reloaded
        // restore it to the same position
        outState.putInt(Constants.RECYCLER_VIEW_FIRST_VISIBILE_ITEM,
            linearLayoutManager.findFirstVisibleItemPosition());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recyclerViewFirstVisiblePos = savedInstanceState.getInt(Constants.RECYCLER_VIEW_FIRST_VISIBILE_ITEM,0);


        pageNumber = savedInstanceState.getInt(Constants.SAVE_STATE_PAGE_NUMBER);
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
        if( pageNumber != 0) {
            api.getProductList(0,
                pageNumber*pageSize,
                new WeakReference<ProductListContract.View>(this),true);
        }else {
            api.getProductList(pageNumber,
                pageSize,
                new WeakReference<ProductListContract.View>(this),true);
        }

    }

    /* end lifecycle events */


    // helper methods


    @Override
    public void onProductList(final ProductsResponse response) {
        adapter.update(response);
        progress.setVisibility(View.GONE);

    }

    /***
     * used only from onResume cause this could be a orientation change.
     * check the stored state and if it is > 0 then scroll the recycler view to the position it
     *  was on prior to the orientation change.
     * @param response
     */
    @Override
    public void onProductListFromOnResume(final ProductsResponse response) {
        onProductList(response);

        if( recyclerViewFirstVisiblePos > 0 ) {
            // orientation change possibly, set the position to the position retrieved
            // in restoreInstanceState
            recyclerView.scrollToPosition(recyclerViewFirstVisiblePos);
        }
        progress.setVisibility(View.GONE);

    }

    @Override
    public void onLoadingFailed(final ApiError apiError) {
        progress.setVisibility(View.GONE);
        toastExt.show("Api Error: httpStatus=" + apiError.getHttpStatusCode() + ", message=" + apiError.getmessage(),
            Toast.LENGTH_SHORT);

    }
}
