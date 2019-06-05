package com.lynxes.jonathanlnb.smartx.Adaptadores;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lynxes.jonathanlnb.smartx.Actividades.Principal;
import com.lynxes.jonathanlnb.smartx.Fragmentos.MenuLateral;
import com.lynxes.jonathanlnb.smartx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpcionesAdapter extends RecyclerView.Adapter<OpcionesAdapter.ViewHolder> {
    private ArrayList<String> opciones;
    private MenuLateral fragmeto;
    private Activity context;

    public OpcionesAdapter(ArrayList<String> opciones, MenuLateral fragmeto) {
        this.opciones = opciones;
        this.fragmeto = fragmeto;
        context = fragmeto.getActivity();
    }

    @Override
    public OpcionesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_opciones, parent, false);
        OpcionesAdapter.ViewHolder viewHolder = new OpcionesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OpcionesAdapter.ViewHolder holder, int position) {
        String f = opciones.get(position);
        holder.vTexto.setText(f);
        switch (position) {
            case 0:
                holder.iIcono.setImageResource(R.drawable.home_outline);
                break;
            case 1:
                holder.iIcono.setImageResource(R.drawable.gift);
                break;
            case 2:
                holder.iIcono.setImageResource(R.drawable.auto_fix);
                break;
            case 3:
                holder.iIcono.setImageResource(R.drawable.map_marker_radius);
                break;
            case 4:
                holder.iIcono.setImageResource(R.drawable.wallet_outline);
                break;
            case 5:
                holder.iIcono.setImageResource(R.drawable.death_star_variant);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return opciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iIcono)
        ImageView iIcono;
        @BindView(R.id.vTexto)
        TextView vTexto;
        @BindView(R.id.vLinea)
        View vLinea;
        private Typeface TF;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            TF = Typeface.createFromAsset(context.getAssets(), "GoogleSans-Regular.ttf");
            vTexto.setTypeface(TF);
        }

        @OnClick(R.id.lOpcion)
        public void cambiarFragmento() {
            switch (getAdapterPosition()) {
                case 0:
                    ((Principal) context).mostrarMenu();
                    break;
                case 1:
                    ((Principal) context).mostrarMenu();
                    break;
                case 2:
                    ((Principal) context).mostrarMenu();
                    break;
                case 3:
                    ((Principal) context).mostrarMenu();
                    break;
            }
            fragmeto.cerrar();
        }
    }
}
