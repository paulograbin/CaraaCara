package com.paulograbin;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensagem implements Serializable {

    private final int messageCode;
    private final String texto;
    private LocalDateTime dataHora;
    private final String sender;

    public Mensagem(int messageCode, String sender, String texto) {
        this.messageCode = messageCode;
        this.dataHora = LocalDateTime.now();
        this.sender = sender;
        this.texto = texto;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getSender() {
        return sender;
    }

    public String getTexto() {
        return texto;
    }

    public int getMessageCode() {
        return messageCode;
    }

    @Override
    public String toString() {
        return "Mensagem{" +
                " messageCode=" + messageCode +
                ", dataHora=" + dataHora +
                ", texto='" + texto + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
