
package com.paulograbin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class ClienteCC extends javax.swing.JFrame {
   
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
    
    
    public final ImageIcon fotoEmma = new ImageIcon(getClass().getResource("/images/emma_watson.jpg"));
//    public final ImageIcon fotoObama = new javax.swing.ImageIcon(getClass().getResource("/images/obama.jpg"));
    public final ImageIcon fotoEmilia = new ImageIcon(getClass().getResource("/images/emilia.jpg"));
    public final ImageIcon fotoNatalie = new ImageIcon(getClass().getResource("/images/natalie.jpg"));
    public final ImageIcon fotoMila = new ImageIcon(getClass().getResource("/images/mila.jpg"));
    public final ImageIcon fotoScarlett = new ImageIcon(getClass().getResource("/images/scarlett.jpg"));
    public final ImageIcon fotoEu = new ImageIcon(getClass().getResource("/images/eu.jpg"));
    
    public String nomeJogadorServer;
    public String nomeJogadorClient;
    
    private Personagem fichas[] = new Personagem[5];
    
    public String escolhidoServer;
    public String escolhidoClient;
    
//    public int quantidadeServidor;
//    public int quantidadeClient;
    
    private String ip;
    private int porta;

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    private boolean continua;
       
    public ClienteCC() {

        initComponents();
        iniciaPersonagens();

//        quantidadeServidor = 5;
//        quantidadeClient = 5;

        //ip = "10.250.5.116";
        ip = JOptionPane.showInputDialog("Informe o IP...");
        porta = 1111;
        
        jTextArea1.setText("Bem vindo...");

//        labelQntServidor.setText(String.valueOf(quantidadeServidor));
//        labelQntClient.setText(String.valueOf(quantidadeClient));

        conecta();
    }

    private void iniciaPersonagens() {
        fichas[0] = new Personagem(fotoEmma, "Emma Watson");
        fichas[1] = new Personagem(fotoEmilia, "Emilia Clarke");
        fichas[2] = new Personagem(fotoNatalie, "Natalie Portman");
        fichas[3] = new Personagem(fotoScarlett, "Scarlett Johansson");
        fichas[4] = new Personagem(fotoMila, "Mila Kunis");
        jLabel2.setIcon(fichas[0].getFoto());
        jLabel3.setIcon(fichas[1].getFoto());
        jLabel4.setIcon(fichas[2].getFoto());
        jLabel5.setIcon(fichas[3].getFoto());
        jLabel6.setIcon(fichas[4].getFoto());

        int numero = (int) (Math.random() * 5);
        escolhidoClient = fichas[numero].getNome();
        jLabel1.setText("Escolhido: " + escolhidoClient);
    }
    
    public void fimDeJogo(String codigo) {
       
        if (codigo.equals(String.valueOf(CLIENTVENCEU))) {
            JOptionPane.showMessageDialog(null, "CLIENT VENCEU");
            
        } else if (codigo.equals(String.valueOf(SERVERVENCEU))) {
            JOptionPane.showMessageDialog(null, "SERVIDOR VENCEU");
            
        }
    }
    
    public void conecta() {
        //ip = JOptionPane.showInputDialog("Informe o IP do servidor");
        //porta = Integer.parseInt(JOptionPane.showInputDialog("Insira a porta"));

        try {
            socket = new Socket(ip, porta);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            System.err.println("Client: erro ao iniciar streams");
        }

        System.out.println("Client: conectado!");
        mandaMensagem(montaMensagem(1, escolhidoClient));
        System.out.println("Client: Enviou mensagem!");
    }
    	
    public void mandaMensagem(String m) {
        try {
            System.out.println("Client: mensagem enviada: " + m);
            output.writeObject(m);
            output.flush();
        } catch (IOException e) {
            System.out.println("Client: mensagem NÃO enviada: " + m);
        }
    }

    public void recebeMensagens() {

        System.out.println("RECEBENDO MENSAGENS........................");

        while (continua == true) {
            recebeMensagem();
        }
        System.out.println("Encerrando laço...");
    }

    public void recebeMensagem() {
        try {
            String mensagem = (String) input.readObject();

            System.out.println("MENSAGEM RECEBIDA: " + mensagem);

            this.trataMensagemRecebida(mensagem);
            
            
        } catch (IOException e) {
            System.err.println("Server: IOException no recebimento de mensagem");
        } catch (ClassNotFoundException ex) {
            System.err.println("Server: ClassNotFoundException no recebimento de mensagem");
        }
    }

    public void trataMensagemRecebida(String mensagem) throws IOException {
        String buffer[] = mensagem.split(",");

        if ((Integer.parseInt(buffer[0]) == ENCERRALAÇO)) {
            // Encerra laço
            continua = false;
            System.out.println("Codigo -1 - Encerrando a porra toda.");
        }
        else if ((Integer.parseInt(buffer[0]) == DEFINEPERSONAGEMSERVER)) {
            escolhidoServer = buffer[1];
            System.out.println("Codigo 0 - Personagem do server: " + escolhidoServer);


        } else if (Integer.parseInt(buffer[0]) == DEFINEPERSONAGEMCLIENT) {
            //Define personagem escolhido pelo client
            escolhidoClient = buffer[1];
            System.out.println("Codigo 1 - Personagem do client: " + escolhidoClient);

        } else if (Integer.parseInt(buffer[0]) == PALPITECLIENT) {
            // Trata palpite recebido
            System.out.println("Codigo 2 - Recebe palpite do client. Palpite: " + buffer[1]);
            trataPalpiteRecebido(buffer[1]);

        } else if (Integer.parseInt(buffer[0]) == RECEBEUMENSAGEMCHAT) {
            // Trata mensagem de chat recebida
            System.out.println("Código 3 - recebeu mensagem chat");
            adicionaMensagemClientChat();
            
        } else if (Integer.parseInt(buffer[0]) == SINCRONIZA) {
            // Trata sincroniza chat
            sincronizaChat(buffer[1]);
            System.out.println("Codigo 6 - sincroniza chat");


        } else if (Integer.parseInt(buffer[0]) == PALPITESERVIDOR) {
            // Trata SERVER levantou personagem
            trataPalpiteRecebido(buffer[1]);


        } else if (Integer.parseInt(buffer[0]) == SERVERVENCEU) {
            // Trata SERVER levantou personagem
            fimDeJogo(buffer[0]);


        } else if (Integer.parseInt(buffer[0]) == CLIENTVENCEU) {
            // Trata SERVER levantou personagem
            fimDeJogo(buffer[0]);
        }

        }
    
   
    public void sincronizaChat(String mensagens) {
        jTextArea1.setText(mensagens);
    }


    public void adicionaMensagemClientChat() {
        
        String mensagem = "Client disse: " + jTextField1.getText();
        
        String textoSalvo = jTextArea1.getText();
        
        textoSalvo = textoSalvo + '\n' +mensagem;
        
        jTextArea1.setText(textoSalvo);

        mandaMensagem(montaMensagem(RECEBEUMENSAGEMCHAT, jTextField1.getText()));
        jTextField1.setText("");

        recebeMensagem();
        
    }
    
    public void adicionaMensagemConsole(String mensagem) {
        String textoSalvo = jTextArea1.getText();
        textoSalvo = textoSalvo + '\n' + "CONSOLE: " + mensagem;

        jTextArea1.setText(textoSalvo);
    }

    public void recebeEnviaPalpite() {
        String palpite = JOptionPane.showInputDialog("Client, qual é o seu palpite?");

        mandaMensagem(montaMensagem(PALPITECLIENT, palpite.toUpperCase().trim()));
        System.out.println("************* ENVIOU AGORA ESPERA **********");
        recebeMensagem();
    }

    public String montaMensagem(int codigo, String mensagem) {
        StringBuilder sb = new StringBuilder();

        sb.append(codigo).append(",").append(mensagem);

        return sb.toString();
    }

    public void trataPalpiteRecebido(String palpite) {
        System.out.println("Iniciando tratamento do palpite recebido do server");

        System.out.println("Palpite recebido: " + palpite);
        System.out.println("Personagem do server: " + escolhidoServer);

        if (palpite.trim().equalsIgnoreCase(escolhidoClient.trim())) {
            JOptionPane.showMessageDialog(null, "SERVER VENCEU, ADVINHOU CORRETAMENTE");
            
        } else {
            JOptionPane.showMessageDialog(null, "CLIENT VENCEU, SERVER ERROU PALPITE");
            
        }
    }
    
    public void fechaConexao() {
        System.out.println("Fechando conexões...");
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("Client: problema enquanto tentava fechar a conexão.");
            System.exit(1);
        }
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client");
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

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
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

        jButton4.setText("Finalizou jogada");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btTerminaJogada(evt);
            }
        });

        jButton2.setText("Recebe mensagens");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleresperamensagens(evt);
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
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(46, 46, 46)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jButton3.getAccessibleContext().setAccessibleName("btPalpite");

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

    private void trocaFotoEmma(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trocaFotoEmma
        fichas[0].setEstado();
        if (fichas[0].getEstado()) {
            jLabel2.setIcon(fotoEmma);
//            levantaClient();
        } else {
            jLabel2.setIcon(fotoEu);
//            abaixaClient();
        }
    }//GEN-LAST:event_trocaFotoEmma

    private void trocaFotoObama(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trocaFotoObama
        fichas[1].setEstado();
        if (fichas[1].getEstado()) {
            jLabel3.setIcon(fotoEmilia);
//            levantaClient();
        } else {
            jLabel3.setIcon(fotoEu);
//            abaixaClient();
        }
    }//GEN-LAST:event_trocaFotoObama

    private void trocaFotoNatalie(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trocaFotoNatalie
        fichas[2].setEstado();
        if (fichas[2].getEstado()) {
            jLabel4.setIcon(fotoNatalie);
//            levantaClient();
        } else {
            jLabel4.setIcon(fotoEu);
//            abaixaClient();
        }
    }//GEN-LAST:event_trocaFotoNatalie

    private void trocaFotoScarlett(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trocaFotoScarlett
        fichas[3].setEstado();
        if (fichas[3].getEstado()) {
            jLabel5.setIcon(fotoScarlett);
//            levantaClient();
        } else {
            jLabel5.setIcon(fotoEu);
//            abaixaClient();
        }
    }//GEN-LAST:event_trocaFotoScarlett

    private void trocaFotoMila(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trocaFotoMila
        fichas[4].setEstado();
        if (fichas[4].getEstado()) {
            jLabel6.setIcon(fotoMila);
//            levantaClient();
        } else {
            jLabel6.setIcon(fotoEu);
//            abaixaClient();
        }
    }//GEN-LAST:event_trocaFotoMila

    private void handlerClose(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_handlerClose
        fechaConexao();
    }//GEN-LAST:event_handlerClose

    private void handlerBtEnviar(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handlerBtEnviar
        adicionaMensagemClientChat();
    }//GEN-LAST:event_handlerBtEnviar

    private void handlerBtPalpite(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handlerBtPalpite
        recebeEnviaPalpite();
    }//GEN-LAST:event_handlerBtPalpite

    private void btTerminaJogada(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btTerminaJogada
        mandaMensagem(montaMensagem(ENCERRALAÇO, null));
        continua = true;
    }//GEN-LAST:event_btTerminaJogada

    private void handleresperamensagens(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleresperamensagens
        continua = true;
        recebeMensagens();
    }//GEN-LAST:event_handleresperamensagens

    public static void main(String args[]) {
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClienteCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClienteCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClienteCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClienteCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ClienteCC().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables
}
