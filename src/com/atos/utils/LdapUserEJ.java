package com.atos.utils;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
/**
 * <p>Title: LdapUserEJ </p>
 * <p>Description: EJB, Retrieves user information from LDAP and DDBB</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public interface LdapUserEJ extends EJBObject {
  public void updateLangAndProfile(String _lang,String _profile)
    throws ClsExceptions, RemoteException;
  public void serLDAPConf(String _INITCTX ,String _MY_HOST[],String  _MGR_DN ,
                        String _MGR_PW, String _MY_SEARCHBASE) throws RemoteException;
  public void fillinData()
    throws Exception, RemoteException;
  public UsrBean createUserSession()
    throws Exception, RemoteException;
  public String getLang() throws RemoteException;
  public String getProfile() throws RemoteException;
  public String getUid() throws RemoteException;
  public void fillinLdapData() throws Exception, RemoteException;
  public String getCn() throws RemoteException;
  public java.util.Hashtable getAllPsscUser(String[] profiles)
      throws Exception, RemoteException;
}