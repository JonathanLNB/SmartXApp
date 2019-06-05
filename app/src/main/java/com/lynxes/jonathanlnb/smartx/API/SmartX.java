package com.lynxes.jonathanlnb.smartx.API;

import com.lynxes.jonathanlnb.smartx.API.Modelos.Categorias;
import com.lynxes.jonathanlnb.smartx.API.Modelos.Productos;
import com.lynxes.jonathanlnb.smartx.TDA.Producto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SmartX {
    @GET("reportes/total/productos")
    public Call<Productos> allProducts();

    @GET("categorias")
    public Call<Categorias> allCategories();
}
