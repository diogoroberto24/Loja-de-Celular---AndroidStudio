package br.edu.ifpr.lojadecelularessqlite;

public class Aparelho {

    private int id;
    private String modelo;
    private String marca;
    private double preco;
    private int estoque;

    public Aparelho() {
    }

    public Aparelho(String modelo, String marca, double preco, int estoque) {
        this.modelo = modelo;
        this.marca = marca;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
}