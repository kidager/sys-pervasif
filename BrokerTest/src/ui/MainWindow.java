package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import server.event.Event;
import server.event.EventListener;
import server.event.EventSender;
import server.event.callback.OnEventReception;
import utils.NetworkUtil;

public class MainWindow implements ActionListener {
  private static JFrame     mainFrame;
  private static JTextField destinationIp;
  private static JTextField title;
  private static JLabel     lblTitle;
  private static JTextField type;
  private static JLabel     lblType;
  private static JLabel     lblMessage;
  private static JLabel     lblDestionationIp;
  private static JButton    sendBtn;
  private static JTextArea  message;
  private static JLabel     myIp;
  private static JButton    exitBtn;
  private static JTextArea  console;
  private static JLabel     lblConsole;
  private static String     ip = "My IP : ";

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
    } catch (Throwable e) {
      e.printStackTrace();
    }
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ip = NetworkUtil.getCurrentEnvironmentNetworkIp();
          MainWindow window = new MainWindow();
          window.setIp(ip);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        EventListener eventListener = new EventListener(9000);
        eventListener.setOnEventReception(new OnEventReception() {
          @Override
          public void eventReceived(Event event) {
            console.setText(console.getText() + "\n" + event.getName() + " : " + event.getSenderIP());
          }
        });
        new Thread(eventListener).start();
      }
    });
  }

  public MainWindow() {
    initialize();
  }

  private void initialize() {
    mainFrame = new JFrame();
    mainFrame.setTitle("Send example");
    mainFrame.setResizable(false);
    mainFrame.setBounds(0, 0, 550, 500);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.getContentPane().setLayout(null);
    mainFrame.setVisible(true);

    {
      myIp = new JLabel("My IP : XXX.XXX.XXX.XXX");
      myIp.setBounds(143, 10, 276, 15);
      myIp.setHorizontalAlignment(SwingConstants.CENTER);
      mainFrame.getContentPane().add(myIp);
    }
    {
      {
        lblDestionationIp = new JLabel("Send to");
        lblDestionationIp.setBounds(35, 52, 70, 15);
        mainFrame.getContentPane().add(lblDestionationIp);
      }
      {
        destinationIp = new JTextField();
        destinationIp.setBounds(155, 50, 200, 21);
        destinationIp.setText("172.16.1.208");
        mainFrame.getContentPane().add(destinationIp);
      }
    }

    {
      {
        lblTitle = new JLabel("Titre");
        lblTitle.setBounds(35, 89, 70, 15);
        mainFrame.getContentPane().add(lblTitle);
      }
      {
        title = new JTextField();
        title.setColumns(10);
        title.setBounds(155, 87, 200, 21);
        mainFrame.getContentPane().add(title);
      }
    }

    {
      {
        lblType = new JLabel("Type");
        lblType.setBounds(35, 130, 70, 15);
        mainFrame.getContentPane().add(lblType);
      }
      {
        type = new JTextField();
        type.setColumns(10);
        type.setBounds(155, 128, 200, 21);
        mainFrame.getContentPane().add(type);
      }
    }

    {
      {
        lblMessage = new JLabel("Message");
        lblMessage.setBounds(35, 172, 70, 15);
        mainFrame.getContentPane().add(lblMessage);
      }
      {
        message = new JTextArea();
        message.setBounds(155, 172, 200, 95);
        message.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        mainFrame.getContentPane().add(message);
      }
    }
    {
      {
        lblConsole = new JLabel("Console");
        lblConsole.setBounds(10, 278, 72, 17);
        mainFrame.getContentPane().add(lblConsole);
      }
    }

    {
      {
        sendBtn = new JButton("Send");
        sendBtn.addActionListener(this);
        sendBtn.setBounds(410, 45, 117, 25);
        mainFrame.getContentPane().add(sendBtn);
      }
      {
        exitBtn = new JButton("Exit");
        exitBtn.addActionListener(this);
        exitBtn.setBounds(410, 84, 117, 25);
        mainFrame.getContentPane().add(exitBtn);
      }
    }
    {
      console = new JTextArea();
      console.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
      console.setBounds(10, 300, 525, 145);
      console.setEditable(false);
      mainFrame.getContentPane().add(console);
    }
  }

  public void setIp(String ip) {
    myIp.setText("My IP : " + ip);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == sendBtn) {
      do_sendBtn_actionPerformed(e);
    }
    if (e.getSource() == exitBtn) {
      do_exitBtn_actionPerformed(e);
    }
  }

  protected static void do_sendBtn_actionPerformed(ActionEvent e) {
    Event event = new Event(
        title.getText(),
        type.getText(),
        message.getText(),
        ip);
    EventSender sender = new EventSender(destinationIp.getText(), 9000, event);
    new Thread(sender).start();
  }

  protected static void do_exitBtn_actionPerformed(ActionEvent e) {
    mainFrame.dispose();
  }
}
