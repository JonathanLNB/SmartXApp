package com.lynxes.jonathanlnb.smartx.Adaptadores;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lynxes.jonathanlnb.smartx.R;
import com.lynxes.jonathanlnb.smartx.TDA.Producto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private ArrayList<Producto> productos;
    private Activity context;
    private Typeface TF;
    private Typeface TF2;

    public ProductoAdapter(ArrayList<Producto> productos, Activity context) {
        this.productos = productos;
        this.context = context;
        TF = Typeface.createFromAsset(context.getAssets(), "GoogleSans-Regular.ttf");
        TF2 = Typeface.createFromAsset(context.getAssets(), "GoogleSans-Bold.ttf");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_producto, parent, false);
        ProductoAdapter.ViewHolder viewHolder = new ProductoAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto p = productos.get(position);
        holder.vProducto.setText(p.getProducto());
        holder.vPrecio.setText("$ "+p.getPrecioventa());
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iProducto)
        ImageView iProducto;
        @BindView(R.id.vProducto)
        TextView vProducto;
        @BindView(R.id.vPrecio)
        TextView vPrecio;

        private boolean seleccionado = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            vProducto.setTypeface(TF);
            vPrecio.setTypeface(TF2);
        }
    }
}
