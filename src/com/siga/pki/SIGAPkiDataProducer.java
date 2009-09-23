package com.siga.pki;


import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import com.atos.utils.ClsExceptions;

public abstract class SIGAPkiDataProducer
{
  protected HttpSession ses=null;

  public void setSession(HttpSession _ses) {
	  ses=_ses;
  }
  public static String INIT_NAME = "<!--KeyNameI-->";
  public static String END_NAME = "<!--KeyNameE-->";
  public static String INIT_VALUE = "<!--KeyValueI-->";
  public static String END_VALUE = "<!--KeyValueE-->";





  public abstract void setParameters(Hashtable params);
  public abstract void performAction() throws Exception;
  public abstract Hashtable getData();
  public abstract Vector getOrder();
  public abstract String getLiteral();

  public abstract String actionDone(String filename, HttpSession ses)throws ClsExceptions;
  public abstract String getFileName();
  public abstract void setFileName();




}