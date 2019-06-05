package com.lynxes.jonathanlnb.smartx.API.Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lynxes.jonathanlnb.smartx.TDA.Categoria;

import java.util.List;

public class Categorias {
    @SerializedName("categorias")
    @Expose
    private List<Categoria> categorias = null;
    @SerializedName("valid")
    @Expose
    private Integer valid;

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Integer getValid() {
        return valid;
    }
}
