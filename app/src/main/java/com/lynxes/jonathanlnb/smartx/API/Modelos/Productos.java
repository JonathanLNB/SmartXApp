package com.lynxes.jonathanlnb.smartx.API.Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lynxes.jonathanlnb.smartx.TDA.Producto;

import java.util.List;

public class Productos {
    @SerializedName("productos")
    @Expose
    private List<Producto> productos = null;

    public List<Producto> getProductos() {
        return productos;
    }
}
