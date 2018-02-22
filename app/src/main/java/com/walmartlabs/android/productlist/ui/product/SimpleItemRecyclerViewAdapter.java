package com.walmartlabs.android.productlist.ui.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.walmartlabs.android.productlist.R;
import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import com.walmartlabs.android.productlist.ui.product_detail.ProductDetailActivity;
import com.walmartlabs.android.productlist.ui.product_detail.ProductDetailFragment;
import java.util.List;



public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = SimpleItemRecyclerViewAdapter.class.getSimpleName();
    private final ProductListActivity parentActivity;
    private final List<Product> products;
    private final boolean twoPane;
    private int position;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Product item = (Product) view.getTag();
            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ProductDetailFragment.ARG_ITEM_ID, item.getProductId());
                ProductDetailFragment fragment = new ProductDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager()
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


    SimpleItemRecyclerViewAdapter(ProductListActivity parentActivity, List<Product> products,
        boolean twoPane) {
        this.products = products;
        this.parentActivity = parentActivity;
        this.twoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.product_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Product product = products.get(position);

        holder.productName.setText(Html.fromHtml(product.getProductName()));

        if( product.getShortDescription() != null) {
            holder.shortDescription.setText(Html.fromHtml(product.getShortDescription()));
        }else {
            holder.shortDescription.setText("");
        }

        holder.price.setText(product.getPrice());
        final String url = product.getProductImageUrl();
        Log.d("ronj", "url=" + url);
        if( url != null) {
            Picasso.with(parentActivity).load(Uri.parse(url)).fit().into(holder.image);
        }

        holder.itemView.setTag(product);
        holder.itemView.setOnClickListener(mOnClickListener);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(final ProductsResponse response) {
        int curSize = products.size();
        products.addAll(response.getProducts());

        if( products.size() <= 1) {
            notifyDataSetChanged();
        }else {
            notifyItemRangeInserted(curSize, products.size() - 1);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView productName;
        final TextView shortDescription;
        final TextView price;
        final ImageView image;

        ViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.product_name_tv);
            shortDescription = (TextView) view.findViewById(R.id.product_short_description_tv);
            image = (ImageView) view.findViewById(R.id.product_iv);
            price = (TextView) view.findViewById(R.id.price_tv);

        }
    }
}