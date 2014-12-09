package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import server.event.Event;
import server.event.EventListener;
import server.event.EventSender;
import server.event.callback.OnEventReception;
import utils.FileUtils;
import utils.NetworkUtils;
import common.config.Configuration;

public class MainWindow implements ActionListener {
  private static MainWindow                 window;
  private static JFrame                     mainFrame;
  private static JTextField                 destinationIp;
  private static JTextField                 title;
  private static JLabel                     lblTitle;
  private static JTextField                 type;
  private static JLabel                     lblType;
  private static JLabel                     lblMessage;
  private static JLabel                     lblDestionationIp;
  private static JButton                    sendBtn;
  private static JTextArea                  message;
  private static JLabel                     myIp;
  private static JButton                    exitBtn;
  private static JTextArea                  console;
  private static JLabel                     lblConsole;
  private static String                     ip                    = "My IP : ";
  private static JMenuBar                   menuBar;
  private static JMenu                      menuFile;
  private static JMenuItem                  menuItemExit;
  private static JMenu                      menuConfig;
  private static JMenu                      menuNetwork;
  private static List<JRadioButtonMenuItem> netInterfaceList      = new ArrayList<JRadioButtonMenuItem>();

  private static EventListener              eventListener;
  private static NetworkInterface           mainNetworkInterface;
  private static String                     savedNetworkInterface = "";

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Throwable e) {
      e.printStackTrace();
    }
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          savedNetworkInterface = FileUtils.loadProperty("defaultNetworkAdapter");
          window = new MainWindow();
          ip = NetworkUtils.getMyIP(mainNetworkInterface).getHostAddress();
          window.setIp(ip);
        } catch (NullPointerException ex) {
          JOptionPane.showMessageDialog(mainFrame, "No network interface found!",
              "Error", JOptionPane.ERROR_MESSAGE);
          do_exitBtn_actionPerformed(null);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        // This is just to test connection
        // TODO: to be changed
        eventListener = new EventListener(Configuration.CLIENT_RECEIVE_PORT);
        eventListener.setOnEventReception(new OnEventReception() {
          @Override
          public void eventReceived(Event event) {
            console.setText(console.getText() + event.getName() + " : " + event.getSenderIP() + "\n");
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
    mainFrame.setBounds(0, 0, 550, 550);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.getContentPane().setLayout(null);
    mainFrame.setVisible(true);
    {
      menuBar = new JMenuBar();
      menuBar.setBounds(0, 0, 550, 29);
      mainFrame.getContentPane().add(menuBar);
      {
        {
          menuFile = new JMenu("File");
          menuBar.add(menuFile);
          {
            menuItemExit = new JMenuItem("Exit");
            menuItemExit.addActionListener(this);
            menuFile.add(menuItemExit);
          }
        }
        {
          menuConfig = new JMenu("Config");
          menuBar.add(menuConfig);
          {
            menuNetwork = new JMenu("Network Interface");
            menuConfig.add(menuNetwork);
            {
              List<NetworkInterface> interfaces = NetworkUtils.getNetworkInterfacesNames();
              for (NetworkInterface in : interfaces) {
                JRadioButtonMenuItem menuItemInterface = new JRadioButtonMenuItem(in.getName());
                menuItemInterface.setSelected(false);
                if (mainNetworkInterface == null) {
                  mainNetworkInterface = in;
                  menuItemInterface.setSelected(true);
                }
                menuItemInterface.addActionListener(this);
                System.out.println(menuItemInterface.getText());
                netInterfaceList.add(menuItemInterface);
                menuNetwork.add(menuItemInterface);
              }
            }
          }
        }
      }
    }

    {
      myIp = new JLabel("My IP : 0.0.0.0");
      myIp.setBounds(143, 41, 276, 15);
      myIp.setHorizontalAlignment(SwingConstants.CENTER);
      mainFrame.getContentPane().add(myIp);
    }
    {
      {
        lblDestionationIp = new JLabel("Send to");
        lblDestionationIp.setBounds(35, 83, 70, 15);
        mainFrame.getContentPane().add(lblDestionationIp);
      }
    }
    sendBtn = new JButton("Send");
    sendBtn.addActionListener(this);
    sendBtn.setBounds(410, 76, 117, 25);
    mainFrame.getContentPane().add(sendBtn);

    {
      {
        lblTitle = new JLabel("Titre");
        lblTitle.setBounds(35, 120, 70, 15);
        mainFrame.getContentPane().add(lblTitle);
      }
    }
    exitBtn = new JButton("Exit");
    exitBtn.addActionListener(this);
    exitBtn.setBounds(410, 115, 117, 25);
    mainFrame.getContentPane().add(exitBtn);

    {
      {
        lblType = new JLabel("Type");
        lblType.setBounds(35, 161, 70, 15);
        mainFrame.getContentPane().add(lblType);
      }
      {
        type = new JTextField();
        type.setColumns(10);
        type.setBounds(155, 159, 200, 21);
        mainFrame.getContentPane().add(type);
      }
    }

    {
      {
        lblMessage = new JLabel("Message");
        lblMessage.setBounds(35, 203, 70, 15);
        mainFrame.getContentPane().add(lblMessage);
      }
      {
        message = new JTextArea();
        message.setBounds(155, 203, 200, 95);
        message.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        mainFrame.getContentPane().add(message);
      }
    }
    {}

    {
      {
        {
          destinationIp = new JTextField();
          destinationIp.setBounds(155, 81, 200, 21);
          mainFrame.getContentPane().add(destinationIp);
        }
      }
      {
        {
          title = new JTextField();
          title.setBounds(155, 118, 200, 21);
          mainFrame.getContentPane().add(title);
        }
      }
    }
    {
      lblConsole = new JLabel("Console");
      lblConsole.setBounds(10, 309, 72, 17);
      mainFrame.getContentPane().add(lblConsole);
    }
    {
      console = new JTextArea();
      console.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
      console.setBounds(10, 331, 525, 164);
      console.setEditable(false);
      mainFrame.getContentPane().add(console);
    }
  }

  public void setIp(String ip) {
    myIp.setText("My IP : " + ip);
  }

  public void actionPerformed(ActionEvent e) {

    if (e.getSource().getClass().equals(JRadioButtonMenuItem.class)) {
      do_networkInterface_actionPerformed(e);
    }

    if (e.getSource() == sendBtn) {
      do_sendBtn_actionPerformed(e);
    }
    if (e.getSource() == exitBtn || e.getSource() == menuItemExit) {
      do_exitBtn_actionPerformed(e);
    }
  }

  protected static void do_networkInterface_actionPerformed(ActionEvent e) {
    // TODO: make this really work (UI not updating in a relevant way)
    // XXX: And also you did nothing to check if itreally works or not !!! 
    JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) e.getSource();
    if (!menuItem.isSelected()) {
      try {
        mainNetworkInterface = NetworkInterface.getByName(menuItem.getText());
        for (JRadioButtonMenuItem mi : netInterfaceList) {
          mi.setSelected(false);
        }
        menuItem.setSelected(true);
        FileUtils.saveProperty("defaultNetworkAdapter", menuItem.getText());
        JOptionPane.showMessageDialog(mainFrame, "Network changed to : "
            + mainNetworkInterface.getDisplayName());
      } catch (SocketException ex) {
        ex.printStackTrace();
      }
    }
  }

  protected static void do_sendBtn_actionPerformed(ActionEvent e) {
    Event event = new Event(
        title.getText(),
        type.getText(),
        message.getText(),
        ip);
    EventSender sender = new EventSender(destinationIp.getText(), Configuration.CLIENT_RECEIVE_PORT, event);
    new Thread(sender).start();
  }

  protected static void do_exitBtn_actionPerformed(ActionEvent e) {
    eventListener.terminate();
    mainFrame.dispose();
  }
}
