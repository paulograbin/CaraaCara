package com.paulograbin;

import javax.swing.JOptionPane;

public class Initial extends javax.swing.JFrame {
    
        private ServidorCC s;
        private ClienteCC cn;
        
        private int cont;
    
    /** Creates new form Initial */
    public Initial() {
        initComponents();
        cont = 0;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Launcher...");
        setResizable(false);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/entrada.png"))); // NOI18N

        jButton1.setText("Abrir conexão..");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlerAbrirConexao(evt);
            }
        });

        jButton2.setText("Conectar...");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlerConectar(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("O que você deseja fazer?");

        jLabel3.setText("Paulo Henrique Grolli Gräbin");

        jLabel4.setText("Nathália Maria Marini Karnas");

        jButton3.setText("Regras");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(18, 18, 18)
                                .add(jLabel1))
                            .add(layout.createSequentialGroup()
                                .add(53, 53, 53)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .add(0, 16, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel4)
                            .add(jLabel3))
                        .add(30, 30, 30))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel2))
                    .add(layout.createSequentialGroup()
                        .add(34, 34, 34)
                        .add(jLabel1)
                        .add(18, 18, 18)
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton3)
                        .add(33, 33, 33)
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel4)
                        .add(20, 20, 20)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void handlerAbrirConexao(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handlerAbrirConexao
        if (cont == 0) {
            s = new ServidorCC();
            s.setVisible(true);
            cont++;
        }
        else {
            JOptionPane.showMessageDialog(null, "Programa já em execução." + '\n' + "Feche e inicie o programa novamente.");
        }
    }//GEN-LAST:event_handlerAbrirConexao

    private void handlerConectar(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handlerConectar
        if (cont == 0) {
            cn = new ClienteCC();
            cn.setVisible(true);
            cont++;
        }
        else {
            JOptionPane.showMessageDialog(null, "Programa já em execução." + '\n' + "Feche e inicie o programa novamente.");
        }
    }

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
    }//GEN-LAST:event_jButton3ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Initial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Initial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Initial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Initial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Initial().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
    
}
