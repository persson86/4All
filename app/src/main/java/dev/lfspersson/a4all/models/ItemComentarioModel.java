package dev.lfspersson.a4all.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LFSPersson on 23/11/16.
 */

public class ItemComentarioModel {
    @SerializedName("urlFoto")
    private String urlFoto;
    @SerializedName("nome")
    private String nome;
    @SerializedName("nota")
    private String nota;
    @SerializedName("titulo")
    private String titulo;
    @SerializedName("comentario")
    private String comentario;

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
