package dev.lfspersson.a4all.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LFSPersson on 18/11/16.
 */

public class ItemListModel {
    @SerializedName("lista")
    private List<String> lista;

    public List<String> getLista() {
        return lista;
    }

    public void setLista(List<String> lista) {
        this.lista = lista;
    }
}
