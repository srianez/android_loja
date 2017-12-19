package br.com.silas.breja.model;

import java.io.Serializable;

public class Item implements Serializable {

    private String id;
    private String nome;
    private String tipo;
    private String fabricante;
    private String descricao;
    private String valor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Item() {

    }

    public Item(String id, String nome, String tipo, String fabricante, String descricao, String valor) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.fabricante = fabricante;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Item(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Item(String id, String nome, String tipo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Breja{" +
                "nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                '}';
    }

}