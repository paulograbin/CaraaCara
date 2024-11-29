package com.paulograbin;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.paulograbin.Assets.Codigos.CLIENT_WON;
import static com.paulograbin.Assets.Codigos.CLIENT_CHAR_SELECTED;
import static com.paulograbin.Assets.Codigos.SERVER_CHAR_SELECTED;
import static com.paulograbin.Assets.Codigos.TERMINATE;
import static com.paulograbin.Assets.Codigos.CLIENT_GUESS;
import static com.paulograbin.Assets.Codigos.SERVER_GUESS;
import static com.paulograbin.Assets.Codigos.RECEBEUMENSAGEMCHAT;
import static com.paulograbin.Assets.Codigos.SERVER_WON;
import static com.paulograbin.Assets.Codigos.SINCRONIZA;


public class ServidorCC extends javax.swing.JFrame {

    public final ImageIcon fotoEmma = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_EMMA_WATSON)));
    public final ImageIcon fotoObama = new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_OBAMA)));
    public final ImageIcon fotoEmilia = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_EMILIA)));
    public final ImageIcon fotoNatalie = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_NATALIE)));
    public final ImageIcon fotoMila = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_MILA)));
    public final ImageIcon fotoScarlett = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_SCARLETT)));
    public final ImageIcon fotoEu = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_EU)));

    private javax.swing.JButton buttonDoesNothing;
    private javax.swing.JButton buttonPalpite;
    private javax.swing.JButton buttonFinalizouJogada;
    private javax.swing.JButton buttonAlsoNothing;

    private javax.swing.JLabel labelPersonalEscolhido;
    private javax.swing.JLabel labelPersonagemUm;
    private javax.swing.JLabel labelPersonagemDois;
    private javax.swing.JLabel labelPersonagemTres;
    private javax.swing.JLabel labelPersonagemQuatro;
    private javax.swing.JLabel labelPersonagemCinco;

    private javax.swing.JPanel panelCards;
    private javax.swing.JPanel panelChat;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JTextArea textAreaCampoMensagem;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton buttonEnviarMensagem;
    private javax.swing.JTextField jTextField1;

    private final Personagem[] fichas = new Personagem[5];

    public String escolhidoServer;
    public String escolhidoClient;

    private int porta;

    private ServerSocket socketRecepcao;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ServidorCC() {
        try {
            initComponents();
            iniciaPersonagens();

            porta = 1111;

            textAreaCampoMensagem.setText("Bem vindo..." + '\n');

            socketRecepcao = new ServerSocket(porta);

            esperaConexao();

            new Thread(() -> {
                while (true) {
                    if (input != null) {
                        recebeMensagem();
                    }
                }
            }).start();
        } catch (IOException ex) {
            System.err.println("Server: merda no construtor 2");
            System.exit(1);
        }
    }

    private void iniciaPersonagens() {
        fichas[0] = new Personagem(fotoEmma, Assets.Names.EMMA_WATSON);
        fichas[1] = new Personagem(fotoEmilia, Assets.Names.EMILIA_CLARKE);
        fichas[2] = new Personagem(fotoNatalie, Assets.Names.NATALIE);
        fichas[3] = new Personagem(fotoScarlett, Assets.Names.SCARLETT);
        fichas[4] = new Personagem(fotoMila, Assets.Names.MILA);
        labelPersonagemUm.setIcon(fichas[0].getFoto());
        labelPersonagemDois.setIcon(fichas[1].getFoto());
        labelPersonagemTres.setIcon(fichas[2].getFoto());
        labelPersonagemQuatro.setIcon(fichas[3].getFoto());
        labelPersonagemCinco.setIcon(fichas[4].getFoto());

        int numero = (int) (Math.random() * 5);
        escolhidoServer = fichas[numero].getNome();

        labelPersonalEscolhido.setText("Escolhido: " + escolhidoServer);
    }

    public void esperaConexao() throws IOException {
        System.out.println("Esperando conexão...");

        String localMachineIP = String.valueOf(InetAddress.getLocalHost());
        String[] s = localMachineIP.split("/");
        localMachineIP = s[1];

        System.out.println("Esperando conexão no endereço " + localMachineIP + " porta " + porta);

//        JOptionPane.showMessageDialog(null, "Esperando conexão no IP " + localMachineIP);

        socket = socketRecepcao.accept();
        System.out.println("Server-client connection established!");

        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());

        mandaMensagem(SERVER_CHAR_SELECTED, escolhidoServer);
    }

    public void mandaMensagem(int code, String m) {
        System.out.println("Mensagem enviada: " + m);

        Mensagem novaMensagem = new Mensagem(code, "Servidor", m);

        if (code == RECEBEUMENSAGEMCHAT) {
            textAreaCampoMensagem.append(novaMensagem.getSender() + ": " + novaMensagem.getTexto() + '\n');
        }

        try {
            output.writeObject(novaMensagem);
            output.flush();
        } catch (Exception ex) {
            System.out.println("Server: não conseguiu enviar a mensagem: " + m);
        }
    }

    public void recebeMensagem() {
        try {
            Mensagem mensagem = (Mensagem) input.readObject();

            System.out.println("MENSAGEM RECEBIDA: " + mensagem);

            trataMensagemRecebida(mensagem);
        } catch (IOException e) {
            System.err.println("Server: IOException no recebimento de mensagem:" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("Server: ClassNotFoundException no recebimento de mensagem");
        }
    }

    private void trataMensagemRecebida(Mensagem mensagem) {
        System.out.println("Tratando nova mensagem...");

        int messageCode = mensagem.getMessageCode();
        String messageText = mensagem.getTexto();
        String sender = mensagem.getSender();

        if (messageCode == TERMINATE) {
            System.out.println("Codigo -1 - Encerrando a porra toda.");
        } else if (messageCode == CLIENT_CHAR_SELECTED) {
            escolhidoClient = messageText;
            System.out.println("Codigo 1 - Personagem do client: " + escolhidoClient);

        } else if (messageCode == CLIENT_GUESS) {
            System.out.println("Codigo 2 - Recebe palpite do client. Palpite: " + messageText);
            trataPalpiteRecebido(messageText);

        } else if (messageCode == RECEBEUMENSAGEMCHAT) {
            System.out.println("Código 3 - recebeu mensagem chat");
            textAreaCampoMensagem.append(sender + ": " + messageText + "\n");
        } else if (messageCode == SINCRONIZA) {
            System.out.println("Codigo 6 - sincroniza chat");
        } else if (messageCode == SERVER_GUESS) {
            // Trata SERVER levantou personagem
        }
    }

    public void recebeEnviaPalpite() {
        String names = Arrays.stream(fichas).map(Personagem::getNome).collect(Collectors.joining(","));

        String palpite = JOptionPane.showInputDialog("Servidor, qual é o seu palpite? " + names);

        mandaMensagem(SERVER_GUESS, palpite.toUpperCase());
    }

    public void trataPalpiteRecebido(String palpite) {
        System.out.println("Iniciando tratamento do palpite recebido do client");

        System.out.println("Palpite recebido: " + palpite);
        System.out.println("Personagem do server: " + escolhidoServer);

        if (palpite.trim().equalsIgnoreCase(escolhidoServer.trim())) {
            JOptionPane.showMessageDialog(null, "CLIENT VENCEU, ADVINHOU CORRETAMENTE");
            mandaMensagem(CLIENT_WON, null);
        } else {
            JOptionPane.showMessageDialog(null, "SERVIDOR VENCEU, CLIENT ERROU PALPITE");
            mandaMensagem(SERVER_WON, null);
        }
    }

    public void shutdownGracefully() {
        System.out.println("Closing connection...");

        try {
            if (Objects.nonNull(socket) && socket.isClosed()) {
                socket.close();
            } else {
                System.out.println("Connection not established, do not need to close it");
            }
        } catch (IOException ex) {
            System.err.println("Could not close connection");
            System.exit(1);
        }

        System.out.println("Done closing connection.");
        System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        FlatLightLaf.setup();

        panelCards = new javax.swing.JPanel();
        panelChat = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();

        labelPersonagemUm = new javax.swing.JLabel();
        labelPersonagemDois = new javax.swing.JLabel();
        labelPersonagemTres = new javax.swing.JLabel();
        labelPersonagemQuatro = new javax.swing.JLabel();
        labelPersonagemCinco = new javax.swing.JLabel();


        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaCampoMensagem = new javax.swing.JTextArea();

        buttonEnviarMensagem = new javax.swing.JButton("Enviar");
        jTextField1 = new javax.swing.JTextField();
        buttonPalpite = new javax.swing.JButton();
        labelPersonalEscolhido = new javax.swing.JLabel();

        buttonFinalizouJogada = new javax.swing.JButton();

        buttonDoesNothing = new javax.swing.JButton("aaaaaaaaaaaaaaaaaaaa");
        buttonAlsoNothing = new javax.swing.JButton("bbbbbbbbb");


        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println(e);

                handlerButtonClose(e);
            }
        });

        panelCards.setBorder(javax.swing.BorderFactory.createEtchedBorder(Color.BLUE, Color.BLACK));
        panelChat.setBorder(javax.swing.BorderFactory.createEtchedBorder(Color.RED, Color.BLACK));
        panelButtons.setBorder(javax.swing.BorderFactory.createEtchedBorder(Color.GREEN, Color.BLACK));

        labelPersonagemUm.setBackground(new java.awt.Color(153, 153, 153));
        labelPersonagemUm.setToolTipText("Emma Watson");
        labelPersonagemUm.setPreferredSize(new java.awt.Dimension(175, 250));
        labelPersonagemUm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoEmma(evt);
            }
        });

        labelPersonagemDois.setBackground(new java.awt.Color(51, 51, 51));
        labelPersonagemDois.setToolTipText("Emilia Clarke");
        labelPersonagemDois.setPreferredSize(new java.awt.Dimension(175, 250));
        labelPersonagemDois.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoObama(evt);
            }
        });

        labelPersonagemTres.setBackground(new java.awt.Color(0, 255, 102));
        labelPersonagemTres.setForeground(new java.awt.Color(51, 204, 0));
        labelPersonagemTres.setToolTipText("Natalie Portman");
        labelPersonagemTres.setPreferredSize(new java.awt.Dimension(175, 250));
        labelPersonagemTres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoNatalie(evt);
            }
        });

        labelPersonagemQuatro.setBackground(new java.awt.Color(0, 255, 102));
        labelPersonagemQuatro.setForeground(new java.awt.Color(51, 204, 0));
        labelPersonagemQuatro.setToolTipText("Scarlett Johansson");
        labelPersonagemQuatro.setPreferredSize(new java.awt.Dimension(175, 250));
        labelPersonagemQuatro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoScarlett(evt);
            }
        });

        labelPersonagemCinco.setBackground(new java.awt.Color(0, 255, 102));
        labelPersonagemCinco.setForeground(new java.awt.Color(51, 204, 0));
        labelPersonagemCinco.setToolTipText("Mila Kunis");
        labelPersonagemCinco.setPreferredSize(new java.awt.Dimension(175, 250));
        labelPersonagemCinco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoMila(evt);
            }
        });


        textAreaCampoMensagem.setEditable(false);
        textAreaCampoMensagem.setColumns(20);
        textAreaCampoMensagem.setRows(3);
        jScrollPane1.setViewportView(textAreaCampoMensagem);

        buttonEnviarMensagem.setText("Enviar");
        buttonEnviarMensagem.setToolTipText("Enviar");
        buttonEnviarMensagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlerBtEnviar(evt);
            }
        });

        jTextField1.setToolTipText("Digite aqui sua mensagem...");


        buttonPalpite.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buttonPalpite.setText("Palpite");
        buttonPalpite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlerBtPalpite(evt);
            }
        });

        labelPersonalEscolhido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelPersonalEscolhido.setText("jLabel1");

        buttonFinalizouJogada.setText("Finalizou jogada");
        buttonFinalizouJogada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                limpa(evt);
            }
        });


        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(panelCards);
        panelCards.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelPersonagemUm, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPersonagemDois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPersonagemTres, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPersonagemQuatro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPersonagemCinco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelPersonagemCinco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelPersonagemQuatro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelPersonagemTres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelPersonagemDois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelPersonagemUm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panelChat);
        panelChat.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jScrollPane1)
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonEnviarMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12))))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonEnviarMensagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );


        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(panelButtons);
        panelButtons.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonPalpite, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonDoesNothing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(labelPersonalEscolhido)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(buttonFinalizouJogada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonAlsoNothing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelPersonalEscolhido)
                                .addGap(18, 18, 18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonFinalizouJogada)
                                .addGap(5, 5, 5)
                                .addComponent(buttonAlsoNothing)
                                .addGap(5, 5, 5)
                                .addComponent(buttonPalpite)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonDoesNothing)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(panelChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(panelButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelChat, 0, 0, Short.MAX_VALUE)
                                        .addComponent(panelButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void trocaFotoEmma(java.awt.event.MouseEvent evt) {
        fichas[0].toggleVisivel();
        if (fichas[0].getVisivel()) {
            labelPersonagemUm.setIcon(fotoEmma);
        } else {
            labelPersonagemUm.setIcon(fotoEu);
        }
    }

    private void trocaFotoObama(java.awt.event.MouseEvent evt) {
        fichas[1].toggleVisivel();
        if (fichas[1].getVisivel()) {
            labelPersonagemDois.setIcon(fotoEmilia);
        } else {
            labelPersonagemDois.setIcon(fotoEu);
        }
    }

    private void trocaFotoNatalie(java.awt.event.MouseEvent evt) {
        fichas[2].toggleVisivel();
        if (fichas[2].getVisivel()) {
            labelPersonagemTres.setIcon(fotoNatalie);
        } else {
            labelPersonagemTres.setIcon(fotoEu);
        }
    }

    private void trocaFotoScarlett(java.awt.event.MouseEvent evt) {
        fichas[3].toggleVisivel();
        if (fichas[3].getVisivel()) {
            labelPersonagemQuatro.setIcon(fotoScarlett);
        } else {
            labelPersonagemQuatro.setIcon(fotoEu);
        }
    }

    private void trocaFotoMila(java.awt.event.MouseEvent evt) {
        fichas[4].toggleVisivel();
        if (fichas[4].getVisivel()) {
            labelPersonagemCinco.setIcon(fotoMila);
        } else {
            labelPersonagemCinco.setIcon(fotoEu);
        }
    }

    private void handlerButtonClose(java.awt.event.WindowEvent evt) {
        System.out.println("Handling button close");

        shutdownGracefully();
    }

    private void handlerBtAbrirConexao(java.awt.event.MouseEvent evt) {
        System.out.println("Handler - Botão abrir conexão");

        try {
            this.esperaConexao();
        } catch (IOException ex) {
            System.err.println("Server: deu problema enquanto esperava a conexão");
            System.exit(1);
        }
    }

    private void handlerBtEnviar(java.awt.event.MouseEvent evt) {
        System.out.println("Handle botão enviar....");
        String mensagemAEnviar = jTextField1.getText();

        if (mensagemAEnviar.trim().equalsIgnoreCase("")) {
            System.out.println("Mensagem vazia, nada a enviar...");
        } else {
            mandaMensagem(RECEBEUMENSAGEMCHAT, mensagemAEnviar);
            jTextField1.setText("");
        }
    }

    private void handlerBtPalpite(java.awt.event.MouseEvent evt) {
        recebeEnviaPalpite();
    }

    private void limpa(java.awt.event.MouseEvent evt) {
        mandaMensagem(TERMINATE, null);
    }
}
