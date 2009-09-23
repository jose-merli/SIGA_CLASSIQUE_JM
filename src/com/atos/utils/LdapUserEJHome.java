package com.atos.utils;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <p>T�tulo: </p>
 * <p>Descripci�n: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

public interface LdapUserEJHome extends EJBHome {
  public LdapUserEJ create(String _DN, String _userAgent,String _remoteAddr)
      throws RemoteException, CreateException;
  public LdapUserEJ create(String _uid,String _ldapPassword,
                               String _userAgent,String _remoteAddr)
      throws RemoteException, CreateException;
  public LdapUserEJ create()
      throws RemoteException, CreateException;
}