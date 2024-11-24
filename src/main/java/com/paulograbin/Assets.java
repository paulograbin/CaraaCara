package com.paulograbin;

public class Assets {

    public static String RESOURCE_EMMA_WATSON = "/images/emma_watson.jpg";
    public static String RESOURCE_EMILIA = "/images/emilia.jpg";
    public static String RESOURCE_NATALIE = "/images/natalie.jpg";
    public static String RESOURCE_MILA = "/images/mila.jpg";
    public static String RESOURCE_SCARLETT = "/images/scarlett.jpg";
    public static String RESOURCE_EU = "/images/eu.jpg";
    public static String RESOURCE_OBAMA = "/images/obama.jpg";


    public static class Codigos {
        public static final int TERMINATE = -1;
        public static final int SERVER_CHAR_SELECTED = 0;
        public static final int CLIENT_CHAR_SELECTED = 1;
        public static final int CLIENT_GUESS = 2;
        public static final int RECEBEUMENSAGEMCHAT = 3;
        public static final int SINCRONIZA = 6;
        public static final int SERVER_GUESS = 9;
        public static final int SERVER_WON = 10;
        public static final int CLIENT_WON = 11;
    }

    public static class Names {
        public static final String EMMA_WATSON = "Emma Watson";
        public static final String EMILIA_CLARKE = "Emilia Clarke";
        public static final String NATALIE = "Natalie Portman";
        public static final String SCARLETT = "Scarlett Johansson";
        public static final String MILA = "Mila Kunis";
    }
}
