package com.paulograbin;

import javax.swing.ImageIcon;


public class Personagem {

    private ImageIcon foto;
    private String nome;
    private boolean visivel;

    public Personagem(ImageIcon foto, String nome) {
        this.foto = foto;
        this.nome = nome;
        this.visivel = true;
    }

    public ImageIcon getFoto() {
        return foto;
    }

    public boolean getVisivel() {
        return visivel;
    }

    public void toggleVisivel() {
        this.visivel = !this.visivel;
        System.out.println("Personagem " + nome + " estado " + visivel);
    }

    public void setFoto(ImageIcon foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
