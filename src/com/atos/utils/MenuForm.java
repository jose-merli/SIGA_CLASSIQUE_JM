/**
 * <p>Title: MenuForm </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.util.Hashtable;
import org.apache.struts.action.ActionForm;


public class MenuForm extends ActionForm {
  private Hashtable table = null;
  private Hashtable reg = null;

  public String getNivel() {
    return ((table == null) ? "" : (String)table.get("NIVEL"));
  }

  public void setNivel(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("NIVEL", name);
  }

  public String getMenu() {
    return ((table == null) ? "" : (String)table.get("ID_MENU"));
  }

  public void setMenu(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("ID_MENU", name);
  }

  public String getdMenu() {
    return ((table == null) ? "" : (String)table.get("ID_RESOURCE"));
  }

  public void setdMenu(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("ID_RESOURCE", name);
  }

  public String getOrder() {
    return ((table == null) ? "" : (String)table.get("IN_ORDER"));
  }

  public void setOrder(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("IN_ORDER", name);
  }

  public String getImage() {
    return ((table == null) ? "" : (String)table.get("URI_IMAGE"));
  }

  public void setImage(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("URI_IMAGE", name);
  }


  public String getIdProcess() {
    return ((table == null) ? "" : (String)table.get("ID_PROCESS"));
  }

  public void setIdProcess(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("ID_PROCESS", name);
  }


  public String getTransaction() {
    return ((table == null) ? "" : (String)table.get("TRANSACTION"));
  }

  public void setTransaction(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("TRANSACTION", name);
  }


  public String getTarget() {
    return ((table == null) ? "" : (String)table.get("TARGET"));
  }

  public void setTarget(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("TARGET", name);
  }

    public String getSize() {
    return ((table == null) ? "" : (String)table.get("WINDOWS_SIZE"));
  }

  public void setSize(String name) {
    if (table == null)
      table = new Hashtable();
    table.put("WINDOWS_SIZE", name);
  }

  public MenuForm() {
  }
}