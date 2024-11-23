package com.paulograbin;

import javax.swing.ImageIcon;

public class Personagem {
    private ImageIcon foto;
    private String nome;
    private boolean estado;
    
    public Personagem(ImageIcon foto, String nome)
    {
        this.foto = foto;
        this.nome = nome;
        this.estado = true;
    }
    
    /**
     * @return the foto
     */
    public ImageIcon getFoto() {
        return foto;
    }
    
    public boolean getEstado()
    {
        return estado;
    }
    
    public void setEstado() {
        if (estado == true)
        {
            estado = false;
        }
        else
        {
            estado = true;
        }
    };

    /**
     * @param foto the foto to set
     */
    public void setFoto(ImageIcon foto) {
        this.foto = foto;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
