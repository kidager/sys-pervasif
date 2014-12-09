package ui;

import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import utils.NetworkUtils;

public class MainWindow implements ItemListener {

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
        tabbedPane.addTab("Tab 1", null, panel, null);
        panel.setLayout(null);
        {
          interfacesList = new JComboBox<NetworkInterface>();
          interfacesList.setBounds(123, 10, 200, 25);
          List<NetworkInterface> list = NetworkUtils.getNetworkInterfacesNames();
          for (NetworkInterface elem : list) {
            interfacesList.addItem(elem);
          }
          interfacesList.setSelectedIndex(-1);
          interfacesList.addItemListener(this);
          interfacesList.setSelectedIndex(0);
          panel.add(interfacesList);
        }
      }
      {
        panel_1 = new JPanel();
        tabbedPane.addTab("Autre TAB", null, panel_1, null);
        panel_1.setLayout(null);
      }
    }
  }

  public void itemStateChanged(ItemEvent e) {
    if (e.getSource() == interfacesList) {
      do_interfacesList_itemStateChanged(e);
    }
  }

  protected void do_interfacesList_itemStateChanged(ItemEvent e) {
    //if (e.getStateChange() == ItemEvent.SELECTED) {
    final NetworkInterface netIn = (NetworkInterface) e.getItem();
    SwingWorker<List<InetAddress>, Void> w = new SwingWorker<List<InetAddress>, Void>() {
      List<InetAddress> addressesList = new ArrayList<InetAddress>();

      @Override
      protected List<InetAddress> doInBackground() throws Exception {
        try {
          addressesList = NetworkUtils.scanConnectedPeers(netIn);
        } catch (InterruptedException ex) {
          JOptionPane.showMessageDialog(frame, "Error scanning network!");
          ex.printStackTrace();
        }
        return null;
      }

      @Override
      protected void done() {
        String all = "";
        if (addressesList != null) {
          for (InetAddress addr : addressesList) {
            all += addr.getHostAddress() + "\n";
          }
        }
        JOptionPane.showMessageDialog(frame, "All addresses :\n" + all);
      }
    };
    w.execute();
    //}
  }
}
