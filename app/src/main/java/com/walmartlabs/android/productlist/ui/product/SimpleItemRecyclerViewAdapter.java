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

    private int position;
    private ClickListener clickListener;


    SimpleItemRecyclerViewAdapter(ProductListActivity parentActivity, List<Product> products,
        ClickListener clickListener) {
        this.products = products;
        this.parentActivity = parentActivity;

        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.product_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Product product = products.get(position);

        holder.productName.setText(Html.fromHtml(product.getProductName()));

        if( product.getShortDescription() != null) {
            holder.shortDescription.setText(Html.fromHtml(product.getShortDescription()));
        }else {
            holder.shortDescription.setText("");
        }

        String price = product.getPrice();
        if( price != null) {
            holder.price.setText(product.getPrice());
        }
        final String url = product.getProductImageUrl();
        Log.d("ronj", "url=" + url);
        if( url != null) {
            Picasso.with(parentActivity).load(Uri.parse(url)).fit().into(holder.image);
        }

        holder.itemView.setTag(product);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                clickListener.onProductClicked(product,position);
            }
        });
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


    public interface ClickListener {
        public void onProductClicked(Product product,int position);
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