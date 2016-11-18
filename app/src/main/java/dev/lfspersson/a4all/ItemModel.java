package dev.lfspersson.a4all;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LFSPersson on 17/11/16.
 */

public class ItemModel {
    @SerializedName("id")
    private String id;
    @SerializedName("cidade")
    private String cidade;
    @SerializedName("bairro")
    private String bairro;
    @SerializedName("urlFoto")
    private String urlFoto;
    @SerializedName("urlLogo")
    private String urlLogo;
    @SerializedName("titulo")
    private String titulo;
    @SerializedName("telefone")
    private String telefone;
    @SerializedName("texto")
    private String texto;
    @SerializedName("endereco")
    private String endereco;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    //comentarios <-----

    public ItemModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
