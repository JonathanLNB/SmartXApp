package com.lynxes.jonathanlnb.smartx.TDA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categoria {

    @SerializedName("idcategoria")
    @Expose
    private Integer idcategoria;
    @SerializedName("categoria")
    @Expose
    private String categoria;

    public Integer getIdcategoria() {
        return idcategoria;
    }

    public String getCategoria() {
        return categoria;
    }
}
