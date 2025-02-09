package com.paulograbin;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;


public class Initial extends javax.swing.JFrame {

    public Initial() {
        initComponents();
    }

    public static void main(String[] args) {
        System.out.println("Starting with " + args.length + " arguments");

        for (String arg : args) {
            System.out.println("Arg " + arg);
        }

        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager.getInstalledLookAndFeels();
            for (javax.swing.UIManager.LookAndFeelInfo installedLookAndFeel : installedLookAndFeels) {

                if ("Nimbus".equals(installedLookAndFeel.getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeel.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            System.err.println("Found not find nimbus look and feel, fallback to default...");
            java.util.logging.Logger.getLogger(Initial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("client")) {
            java.awt.EventQueue.invokeLater(() -> new ClienteCC().setVisible(true));
        } else {
            java.awt.EventQueue.invokeLater(() -> new ServidorCC().setVisible(true));
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        JLabel labelEntrada = new JLabel();

        JButton buttonAbrirConexao = new JButton("Abrir conexão..");
        JButton buttonConectar = new JButton("Conectar...");
        JButton buttonRegras = new JButton("Regras");

        JLabel jLabel1 = new JLabel("O que você deseja fazer?");
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        JLabel labelNomes = new JLabel("Paulo Henrique Grolli Gräbin");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Launcher...");
        setResizable(false);

        labelEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/entrada.png"))); // NOI18N

        buttonRegras.addActionListener(this::jButton3ActionPerformed);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(labelEntrada)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(layout.createSequentialGroup()
                                                                .add(18, 18, 18)
                                                                .add(jLabel1))
                                                        .add(layout.createSequentialGroup()
                                                                .add(53, 53, 53)
                                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                                                        .add(buttonAbrirConexao, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .add(buttonConectar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .add(buttonRegras, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                                .add(0, 16, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(labelNomes))
                                                .add(30, 30, 30))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(labelEntrada))
                                        .add(layout.createSequentialGroup()
                                                .add(34, 34, 34)
                                                .add(jLabel1)
                                                .add(18, 18, 18)
                                                .add(buttonAbrirConexao)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(buttonConectar)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(buttonRegras)
                                                .add(33, 33, 33)
                                                .add(labelNomes)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(20, 20, 20)))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>


    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        String regras = """
                1. Um jogador deve abrir o servidor e informar o IP ao outro, para que este conecte.
                
                2. O personagem que deve ser adivinhado é selecionado automaticamente para ambos os jogadores. Ele é secreto, não revele ao seu adversario.
                
                3. Faça perguntas para ir descobrindo as características do personagem que você tem que adivinhar.
                              IMPORTANTE: cada um dos jogadores faz só uma pergunta de cada vez. Na hora de responder, cuidado para não falar demais! Diga só sim ou não.
                
                Pergunte por exemplo: “Tem olhos azuis?” Se a resposta for “não”, abaixe todas as molduras com caras que tiverem olhos azuis, para eliminá-las da partida.
                Se a resposta for “sim”, abaixe todas as caras que não tiverem olhos azuis. Depois, é a vez de seu adversário fazer uma pergunta e assim por diante.
                
                4. Se você acha que sabe de quem é a cara do seu adversário, pode tentar adivinhar a qualquer momento.
                Se você adivinhar errado, perderá a partida. Se você adivinhar corretamente, então você ganha a partida.
                """;

        JOptionPane.showMessageDialog(null, regras);
    }
}
