package com.mycompany.hiChatJpa;

import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.hiChatJpa.view.MainFrame;

/**
 * clase main
 * 
 * @author erwbyel
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println("no fue posible inicializar el lookandfeels " + ex.getMessage());
        }
        
        FlatDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
