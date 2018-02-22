package com.walmartlabs.android.productlist.ui.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.walmartlabs.android.productlist.R;
import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import com.walmartlabs.android.productlist.ui.DummyContent;
import com.walmartlabs.android.productlist.ui.product_detail.ProductDetailActivity;
import com.walmartlabs.android.productlist.ui.product_detail.ProductDetailFragment;
import java.util.List;



public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = SimpleItemRecyclerViewAdapter.class.getSimpleName();
    private final ProductListActivity mParentActivity;
    private final List<Product> mValues;
    private final boolean mTwoPane;
    private int position;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Product item = (Product) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ProductDetailFragment.ARG_ITEM_ID, item.getProductId());
                ProductDetailFragment fragment = new ProductDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.product_detail_container, fragment)
                    .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(ProductDetailFragment.ARG_ITEM_ID, item.getProductId());

                context.startActivity(intent);
            }
        }
    };


    SimpleItemRecyclerViewAdapter(ProductListActivity parent, List<Product> items,
        boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.product_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).getProductId());
        holder.mContentView.setText(mValues.get(position).getProductName());

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void update(final ProductsResponse response) {
        int curSize = mValues.size();
        mValues.addAll(response.getProducts());

        if( mValues.size() <= 1) {
            notifyDataSetChanged();
        }else {
            notifyItemRangeInserted(curSize, mValues.size() - 1);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}