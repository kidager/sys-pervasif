package ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import server.event.Event;
import server.event.EventSender;
import utils.NetworkUtil;

public class MainWindow implements ActionListener {
  private JFrame     frmSendExample;
  private JTextField textField;
  private JTextField textField_1;
  private JLabel     label_1;
  private JTextField textField_2;
  private JLabel     label_2;
  private JLabel     label_3;
  private JButton    button;
  private JTextArea  textArea;

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Throwable e) {
      e.printStackTrace();
    }
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MainWindow window = new MainWindow();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public MainWindow() {
    initialize();
  }

  private void initialize() {
    frmSendExample = new JFrame();
    frmSendExample.setTitle("Send example");
    frmSendExample.setResizable(false);
    frmSendExample.setBounds(0, 0, 600, 400);
    frmSendExample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmSendExample.getContentPane().setLayout(null);
    frmSendExample.setVisible(true);

    textField = new JTextField();
    textField.setBounds(155, 50, 200, 21);
    textField.setText("172.16.1.208");
    frmSendExample.getContentPane().add(textField);
    textField.setColumns(10);

    JLabel label = new JLabel("Send to");
    label.setBounds(35, 52, 70, 15);
    frmSendExample.getContentPane().add(label);

    textField_1 = new JTextField();
    textField_1.setColumns(10);
    textField_1.setBounds(155, 87, 200, 21);
    frmSendExample.getContentPane().add(textField_1);

    label_1 = new JLabel("Titre");
    label_1.setBounds(35, 89, 70, 15);
    frmSendExample.getContentPane().add(label_1);

    textField_2 = new JTextField();
    textField_2.setColumns(10);
    textField_2.setBounds(155, 128, 200, 21);
    frmSendExample.getContentPane().add(textField_2);

    label_2 = new JLabel("Type");
    label_2.setBounds(35, 130, 70, 15);
    frmSendExample.getContentPane().add(label_2);

    label_3 = new JLabel("Message");
    label_3.setBounds(35, 172, 70, 15);
    frmSendExample.getContentPane().add(label_3);

    textArea = new JTextArea();
    textArea.setBorder(UIManager.getBorder("TextField.border"));
    textArea.setBounds(155, 172, 200, 95);
    frmSendExample.getContentPane().add(textArea);

    button = new JButton("Send");
    button.addActionListener(this);
    button.setBounds(238, 302, 117, 25);
    frmSendExample.getContentPane().add(button);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button) {
      String ip = NetworkUtil.getCurrentEnvironmentNetworkIp();
      System.out.println("IP : " + ip);
      Event event = new Event(
          textField_1.getText(),
          textField_2.getText(),
          textArea.getText(),
          ip);
      EventSender sender = new EventSender(textField.getText(), 9000, event);
      new Thread(sender).start();
    }
  }

}
