package com.lynxes.jonathanlnb.smartx.Adaptadores;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lynxes.jonathanlnb.smartx.R;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {
    private ArrayList<String> categorias;
    private Activity context;

    public CategoriaAdapter(ArrayList<String> categorias, Activity context) {
        this.categorias = categorias;
        this.context = context;
    }

    @Override
    public CategoriaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_categoria, parent, false);
        CategoriaAdapter.ViewHolder viewHolder = new CategoriaAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoriaAdapter.ViewHolder holder, int position) {
        String f = categorias.get(position);
        holder.vTexto.setText(f);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vTexto)
        TextView vTexto;
        private Typeface TF;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            TF = Typeface.createFromAsset(context.getAssets(), "GoogleSans-Regular.ttf");
            vTexto.setTypeface(TF);
        }

        @OnClick(R.id.lCategoria)
        public void buscarCategoria() {

        }
    }
}