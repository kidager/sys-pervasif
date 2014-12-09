package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FileUtils {

  public static final String PROPERTY_FILENAME = "";
  public static final String PROPERTY_TAG      = "systeme.pervasif";

  public static boolean saveProperty(String key, String value) {
    return saveProperty(key, value, "");
  }

  public static boolean saveProperty(String key, String value, String comment) {
    FileOutputStream fos;
    Properties prop = new Properties();

    try {
      fos = new FileOutputStream(PROPERTY_FILENAME);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
      return false;
    }

    prop.put(PROPERTY_TAG + "." + key, value);

    try {
      prop.store(fos, comment);
      fos.flush();
      fos.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      return false;
    }

    return true;
  }

  public static String loadProperty(String key) {
    FileInputStream fis;
    Properties prop = new Properties();
    String returnValue = null;

    try {
      fis = new FileInputStream(PROPERTY_FILENAME);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
      return null;
    }

    try {
      prop.load(fis);
      returnValue = (String) prop.get(key);
      fis.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

    return returnValue;
  }
}
