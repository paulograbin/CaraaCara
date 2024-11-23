
package com.paulograbin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class ServidorCC extends javax.swing.JFrame {

    public final int ENCERRALAÇO = -1;
    public final int DEFINEPERSONAGEMSERVER = 0;
    public final int DEFINEPERSONAGEMCLIENT = 1;
    public final int PALPITECLIENT = 2;
    public final int RECEBEUMENSAGEMCHAT = 3;
    //    public final int CLIENTBAIXOU = 4;
//    public final int CLIENTLEVANTOU = 5;
    public final int SINCRONIZA = 6;
    //    public final int SERVERBAIXOU = 7;
//    public final int SERVERLEVANTOU = 8;
    public final int PALPITESERVIDOR = 9;
    public final int SERVERVENCEU = 10;
    public final int CLIENTVENCEU = 11;


    public final ImageIcon fotoEmma = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_EMMA_WATSON)));
    public final ImageIcon fotoObama = new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_OBAMA)));
    public final ImageIcon fotoEmilia = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_EMILIA)));
    public final ImageIcon fotoNatalie = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_NATALIE)));
    public final ImageIcon fotoMila = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_MILA)));
    public final ImageIcon fotoScarlett = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_SCARLETT)));
    public final ImageIcon fotoEu = new ImageIcon(Objects.requireNonNull(getClass().getResource(Assets.RESOURCE_EU)));

    // Variables declaration - do not modify
    private javax.swing.JButton btAbrir;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration


    public String nomeJogadorServer;
    public String nomeJogadorClient;

    private Personagem fichas[] = new Personagem[5];

    public String escolhidoServer;
    public String escolhidoClient;

    private String ip;
    private int porta;

    private ServerSocket socketRecepcao;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private boolean continua;
    private boolean debugMode = true;

    public ServidorCC() {
        try {
            initComponents();
            iniciaPersonagens();

            continua = true;

            ip = String.valueOf(InetAddress.getLocalHost());
            String s[] = ip.split("/");
            ip = s[1];
            porta = 1111;

            jTextArea1.setText("Bem vindo...");

            socketRecepcao = new ServerSocket(porta);

            esperaConexao();


            new Thread(() -> {
                while (true) {
                    if (input != null) {
                        recebeMensagens();
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
        jLabel2.setIcon(fichas[0].getFoto());
        jLabel3.setIcon(fichas[1].getFoto());
        jLabel4.setIcon(fichas[2].getFoto());
        jLabel5.setIcon(fichas[3].getFoto());
        jLabel6.setIcon(fichas[4].getFoto());

        int numero = (int) (Math.random() * 5);
        escolhidoServer = fichas[numero].getNome();

        jLabel1.setText("Escolhido: " + escolhidoServer);
    }

    public void esperaConexao() throws IOException {
        adicionaMensagemConsole("Esperando conexão no endereço " + ip + " porta " + porta);

        JOptionPane.showMessageDialog(null, "Esperando conexão no IP " + ip);


        socket = socketRecepcao.accept();
        adicionaMensagemConsole("Server: conexão bem sucedida");

        btAbrir.setText("Conectado!");

        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());

        mandaMensagem(montaMensagem(DEFINEPERSONAGEMSERVER, escolhidoServer));
    }

    public void mandaMensagem(String m) {
        adicionaMensagemConsole("Mensagem enviada: " + m);

        try {
            output.writeObject(m);
            output.flush();
        } catch (Exception ex) {
            adicionaMensagemConsole("Server: não conseguiu enviar a mensagem: " + m);
        }
    }

    public void recebeMensagem() {
        try {
            String mensagem = (String) input.readObject();

            adicionaMensagemConsole("MENSAGEM RECEBIDA: " + mensagem);

            this.trataMensagemRecebida(mensagem);
        } catch (IOException e) {
            System.err.println("Server: IOException no recebimento de mensagem");
        } catch (ClassNotFoundException ex) {
            System.err.println("Server: ClassNotFoundException no recebimento de mensagem");
        }
    }

    public void trataMensagemRecebida(String mensagem) {
        String[] buffer = mensagem.split(",");

        if ((Integer.parseInt(buffer[0]) == ENCERRALAÇO)) {
            // Encerra laço
            continua = false;
            adicionaMensagemConsole("Codigo -1 - Encerrando a porra toda.");

        } else if (Integer.parseInt(buffer[0]) == DEFINEPERSONAGEMCLIENT) {
            //Define personagem escolhido pelo client
            escolhidoClient = buffer[1];
            adicionaMensagemConsole("Codigo 1 - Personagem do client: " + escolhidoClient);

        } else if (Integer.parseInt(buffer[0]) == PALPITECLIENT) {
            // Trata palpite recebido
            adicionaMensagemConsole("Codigo 2 - Recebe palpite do client. Palpite: " + buffer[1]);
            trataPalpiteRecebido(buffer[1]);

        } else if (Integer.parseInt(buffer[0]) == RECEBEUMENSAGEMCHAT) {
            // Trata mensagem de chat recebida
            adicionaMensagemConsole("Código 3 - recebeu mensagem chat");
            adicionaMensagemClientChat(buffer[1]);
        } else if (Integer.parseInt(buffer[0]) == SINCRONIZA) {
            // Trata sincroniza chat
            adicionaMensagemConsole("Codigo 6 - sincroniza chat");
        } else if (Integer.parseInt(buffer[0]) == PALPITESERVIDOR) {
            // Trata SERVER levantou personagem
        }
    }

    public void sincronizaChat() {
        mandaMensagem(montaMensagem(SINCRONIZA, jTextArea1.getText()));
    }


    public void adicionaMensagemPropriaChat(String mensagem) {
        String textoSalvo = jTextArea1.getText();
        textoSalvo = textoSalvo + '\n' + "Servidor disse: " + mensagem;

        jTextArea1.setText(textoSalvo);

        sincronizaChat();
    }

    public void adicionaMensagemClientChat(String mensagem) {
        String textoSalvo = jTextArea1.getText();
        textoSalvo = textoSalvo + '\n' + "Client disse: " + mensagem;

        jTextArea1.setText(textoSalvo);

        sincronizaChat();
    }

    public void adicionaMensagemConsole(String mensagem) {
        if (debugMode) {
            String textoSalvo = jTextArea1.getText();
            textoSalvo = textoSalvo + '\n' + "CONSOLE: " + mensagem;

            jTextArea1.setText(textoSalvo);
        }
    }

    public void recebeEnviaPalpite() {
        String palpite = JOptionPane.showInputDialog("Servidor, qual é o seu palpite?");

        mandaMensagem(montaMensagem(PALPITESERVIDOR, palpite.toUpperCase().trim()));

    }

    public String montaMensagem(int codigo, String mensagem) {
        StringBuilder sb = new StringBuilder();

        sb.append(codigo).append(",").append(mensagem);

        return sb.toString();
    }

    public void trataPalpiteRecebido(String palpite) {
        adicionaMensagemConsole("Iniciando tratamento do palpite recebido do client");

        adicionaMensagemConsole("Palpite recebido: " + palpite);
        adicionaMensagemConsole("Personagem do server: " + escolhidoServer);

        if (palpite.trim().equalsIgnoreCase(escolhidoServer.trim())) {
            JOptionPane.showMessageDialog(null, "CLIENT VENCEU, ADVINHOU CORRETAMENTE");
            mandaMensagem(montaMensagem(CLIENTVENCEU, null));
        } else {
            JOptionPane.showMessageDialog(null, "SERVIDOR VENCEU, CLIENT ERROU PALPITE");
            mandaMensagem(montaMensagem(SERVERVENCEU, null));
        }

    }

    public void fechaConexao() {
        adicionaMensagemConsole("Fechando conexões...");
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("Server: problema enquanto tentava fechar a conexão.");
            System.exit(1);
        }
    }

//    public void exibe() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(nomeJogadorServer).append('\n');
//        sb.append(nomeJogadorClient).append('\n');
//
//        sb.append(ip).append('\n');
//        sb.append(porta).append('\n');
//
//        sb.append(quantidadeServidor).append('\n');
//        sb.append(quantidadeClient).append('\n');
//
//        JOptionPane.showMessageDialog(null, sb);
//    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btAbrir = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                handlerClose(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setBackground(new java.awt.Color(153, 153, 153));
        jLabel2.setToolTipText("Emma Watson");
        jLabel2.setPreferredSize(new java.awt.Dimension(175, 250));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoEmma(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setToolTipText("Emilia Clarke");
        jLabel3.setPreferredSize(new java.awt.Dimension(175, 250));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoObama(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(0, 255, 102));
        jLabel4.setForeground(new java.awt.Color(51, 204, 0));
        jLabel4.setToolTipText("Natalie Portman");
        jLabel4.setPreferredSize(new java.awt.Dimension(175, 250));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoNatalie(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(0, 255, 102));
        jLabel5.setForeground(new java.awt.Color(51, 204, 0));
        jLabel5.setToolTipText("Scarlett Johansson");
        jLabel5.setPreferredSize(new java.awt.Dimension(175, 250));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoScarlett(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(0, 255, 102));
        jLabel6.setForeground(new java.awt.Color(51, 204, 0));
        jLabel6.setToolTipText("Mila Kunis");
        jLabel6.setPreferredSize(new java.awt.Dimension(175, 250));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trocaFotoMila(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(3);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Enviar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlerBtEnviar(evt);
            }
        });

        jTextField1.setText("Digite aqui sua mensagem...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
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
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12))))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton3.setText("Palpite");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlerBtPalpite(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("jLabel1");

        btAbrir.setText("Abrir conexão");
        btAbrir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlerBtAbrirConexao(evt);
            }
        });

        jButton4.setText("Finalizou jogada");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                limpa(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btAbrir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(btAbrir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addGap(5, 5, 5)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel2, 0, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void trocaFotoEmma(java.awt.event.MouseEvent evt) {
        fichas[0].toggleVisivel();
        if (fichas[0].getVisivel()) {
            jLabel2.setIcon(fotoEmma);
//            levantaServidor();
        } else {
            jLabel2.setIcon(fotoEu);
//            abaixaServidor();
        }
    }

    private void trocaFotoObama(java.awt.event.MouseEvent evt) {
        fichas[1].toggleVisivel();
        if (fichas[1].getVisivel()) {
            jLabel3.setIcon(fotoEmilia);
//           levantaServidor();
        } else {
            jLabel3.setIcon(fotoEu);
//            abaixaServidor();
        }
    }

    private void trocaFotoNatalie(java.awt.event.MouseEvent evt) {
        fichas[2].toggleVisivel();
        if (fichas[2].getVisivel()) {
            jLabel4.setIcon(fotoNatalie);
//            levantaServidor();
        } else {
            jLabel4.setIcon(fotoEu);
//            abaixaServidor();
        }
    }

    private void trocaFotoScarlett(java.awt.event.MouseEvent evt) {
        fichas[3].toggleVisivel();
        if (fichas[3].getVisivel()) {
            jLabel5.setIcon(fotoScarlett);
//            levantaServidor();
        } else {
            jLabel5.setIcon(fotoEu);
//            abaixaServidor();
        }
    }

    private void trocaFotoMila(java.awt.event.MouseEvent evt) {
        fichas[4].toggleVisivel();
        if (fichas[4].getVisivel()) {
            jLabel6.setIcon(fotoMila);
//            levantaServidor();
        } else {
            jLabel6.setIcon(fotoEu);
//            abaixaServidor();
        }
    }

    private void handlerClose(java.awt.event.WindowEvent evt) {
        fechaConexao();
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
        String mensagemAEnviar = jTextField1.getText();

        if (mensagemAEnviar.trim().equalsIgnoreCase("")) {
            // Campo vaziu, não manda nada
        } else {
            adicionaMensagemPropriaChat(mensagemAEnviar);
            jTextField1.setText("");
        }


    }

    private void handlerBtPalpite(java.awt.event.MouseEvent evt) {
        recebeEnviaPalpite();
    }

    private void limpa(java.awt.event.MouseEvent evt) {
        mandaMensagem(montaMensagem(ENCERRALAÇO, null));
    }

    public static void main(String[] args) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServidorCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(() -> {
            ServidorCC jogo = new ServidorCC();
            jogo.setVisible(true);


        });
    }


}
