package com.lynxes.jonathanlnb.smartx.TDA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Producto {
    @SerializedName("idproducto")
    @Expose
    private Integer idproducto;
    @SerializedName("producto")
    @Expose
    private String producto;
    @SerializedName("imagen")
    @Expose
    private String imagen;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("precioventa")
    @Expose
    private Double precioventa;
    @SerializedName("preciocompra")
    @Expose
    private Double preciocompra;
    @SerializedName("marca")
    @Expose
    private String marca;
    @SerializedName("categoria")
    @Expose
    private String categoria;
    @SerializedName("proveedor")
    @Expose
    private String proveedor;

    public Integer getIdproducto() {
        return idproducto;
    }

    public String getProducto() {
        return producto;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getPrecioventa() {
        return precioventa;
    }

    public Double getPreciocompra() {
        return preciocompra;
    }

    public String getMarca() {
        return marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProveedor() {
        return proveedor;
    }
}
