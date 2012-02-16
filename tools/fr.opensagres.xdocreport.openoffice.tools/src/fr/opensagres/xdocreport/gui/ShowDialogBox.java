/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.opensagres.xdocreport.gui;

import javax.swing.*;
import java.awt.event.*;

public class ShowDialogBox {

    JFrame frame;

    public static void main(String[] args) {
        ShowDialogBox db = new ShowDialogBox();
    }

    public ShowDialogBox() {
        frame = new JFrame("Show Message Dialog");
        JButton button = new JButton("Click Me Pascalou !!!");
        button.addActionListener(new MyAction());
        frame.add(button);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class MyAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, "Roseindia.net");
        }
    }
}
