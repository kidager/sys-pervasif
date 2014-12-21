package tn.enis.bestbuy;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import server.event.callback.OnConsume;
import server.model.Consumer;
import server.model.Producer;
import server.model.Product;
import server.model.ProductRequest;
import tn.enis.tabbar.PagerSlidingTabStrip;
import utils.NetworkUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import common.config.Configuration;

public class MainActivity extends FragmentActivity {

  private PagerSlidingTabStrip tabs;
  private ViewPager            pager;
  private PagerAdapter         adapter;
  private List<FragmentPage>   fragments = new ArrayList<FragmentPage>();

  private static String        serverIP  = "0.0.0.0";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fragments.add(new FragmentPage("Paramètres", R.layout.fragment_config));
    fragments.add(new FragmentPage("Vendre", R.layout.fragment_produce));
    fragments.add(new FragmentPage("Acheter", R.layout.fragment_consume));

    String myIp = "0.0.0.0";
    try {
      myIp = NetworkUtils.getMyIP(NetworkInterface.getByName("wlan0")).getHostAddress();
    } catch (Exception e) {
      myIp = "0.0.0.0";
      List<NetworkInterface> variable = NetworkUtils.getNetworkInterfacesNames();
      for (NetworkInterface ni : variable) {
        Log.d("HAHA", ni.getName() + " - " + ni.getDisplayName());
      }
    }
    Toast.makeText(getApplicationContext(), "My IP : " + myIp, Toast.LENGTH_SHORT).show();
    // ((TextView) findViewById(R.id.paramMyIP)).setText("My IP: " + myIp);

    adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
    pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(adapter);
    tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    tabs.setViewPager(pager);

    Consumer<Product> consumer = new Consumer<Product>(Configuration.CLIENT_RECEIVE_PORT);
    consumer.setOnConsume(new OnConsume<Product>() {
      @Override
      public void onConsume(Product product, String sourceIP) {
        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(MainActivity.this);
        dialogAlert.setTitle("Offre reçu");
        dialogAlert.setMessage(
            "Produit demandé existe:" + "\n" +
                "Nom : " + product.getName() + "\n" +
                "Catégorie : " + product.getCategory() + "\n" +
                "Prix : " + product.getPrice() + "\n" +
                "Description : " + product.getDescription()
            );
        dialogAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            finish();
          }
        });
        dialogAlert.create().show();
      }
    });

    new Thread(consumer).start();
  }

  public void submitParameter(View v) {
    serverIP = ((TextView) findViewById(R.id.paramIP)).getText().toString();
    Toast.makeText(getApplicationContext(), "Adresse du serveur modifiée", Toast.LENGTH_SHORT).show();
  }

  public void submitProduct(View v) {
    String sName = ((TextView) findViewById(R.id.pName)).getText().toString();
    String sCat = ((TextView) findViewById(R.id.pCategory)).getText().toString();
    String sPrice = ((TextView) findViewById(R.id.pPrice)).getText().toString();
    String sDesc = ((TextView) findViewById(R.id.pDesc)).getText().toString();
    double price = Double.parseDouble(sPrice);
    Product product = new Product(sName, sCat, price, sDesc);

    Producer<Product> producer = new Producer<Product>(serverIP, Configuration.BROKER_RECEIVE_PORT, product);
    new Thread(producer).start();

    Toast.makeText(getApplicationContext(), "Produit en cours d'envoi", Toast.LENGTH_SHORT).show();
  }

  public void submitProductRequest(View v) {
    String sCat = ((TextView) findViewById(R.id.prCategory)).getText().toString();
    ProductRequest productRequest = new ProductRequest(sCat);

    Producer<ProductRequest> producer = new Producer<ProductRequest>(serverIP, Configuration.BROKER_RECEIVE_PORT,
        productRequest);
    new Thread(producer).start();

    Toast.makeText(getApplicationContext(), "Requête en cours d'envoi", Toast.LENGTH_SHORT).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
