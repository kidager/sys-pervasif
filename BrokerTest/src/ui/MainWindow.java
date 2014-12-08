package ui;

import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import utils.NetworkUtil;

public class MainWindow {

  private JFrame                      frame;
  private JTabbedPane                 tabbedPane;
  private JPanel                      panel;
  private JPanel                      panel_1;
  private JMenuBar                    menuBar;
  private JMenuItem                   mntmFichier;
  private JMenu                       mnNewMenu;
  private JComboBox<NetworkInterface> interfacesList;

  public static void main(String[] args) throws Exception {
    // Force JVM to use only IPv4
    System.setProperty("java.net.preferIPv4Stack", "true");

    // Set the UI look and feel
    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

    // Start the UI
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        MainWindow window = new MainWindow();
        window.frame.setVisible(true);
      }
    });
  }

  public MainWindow() {
    initialize();
  }

  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    {
      menuBar = new JMenuBar();
      menuBar.setBounds(0, 0, 600, 20);
      frame.getContentPane().add(menuBar);
      {
        mnNewMenu = new JMenu("Fichier");
        menuBar.add(mnNewMenu);
        {
          mntmFichier = new JMenuItem("Exit");
          mnNewMenu.add(mntmFichier);
        }
      }
    }
    {
      tabbedPane = new JTabbedPane(JTabbedPane.TOP);
      tabbedPane.setBounds(10, 30, 580, 315);
      //tabbedPane.addTab("Hello", frame);

      frame.getContentPane().add(tabbedPane);
      {
        panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(null);
        {
          interfacesList = new JComboBox<NetworkInterface>();
          interfacesList.setBounds(123, 10, 200, 25);
          List<NetworkInterface> list = NetworkUtil.getNetworkInterfacesNames();
          for (NetworkInterface elem : list) {
            interfacesList.addItem(elem);
          }
          interfacesList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
              if (e.getStateChange() == ItemEvent.SELECTED) {
                NetworkInterface netIn = (NetworkInterface) e.getItem();
                String list = "";

                for (InterfaceAddress i : netIn.getInterfaceAddresses()) {
                  list += i.getAddress().toString() + "\n";
                }
                JOptionPane.showMessageDialog(frame, "Hello:\n" + list);
              }
            }
          });
          panel.add(interfacesList);
        }
      }
      {
        panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
        panel_1.setLayout(null);
      }
    }
  }
}
