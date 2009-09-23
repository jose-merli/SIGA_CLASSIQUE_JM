package com.siga.generalRequirements.accessControl.users;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import com.siga.generalRequirements.accessControl.SIGAGrDDBB;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ColumnConstants;
import com.atos.utils.TableConstants;

/**
 * <p>Tittle: Propulsion Support System Core</p>
 * <p>Description: This class represents the different Status that use PSSC</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SchlumbergerSema </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

/* esta clase es diferente a todas las demas de mantenimientos. Principalmente
 actúa sobre LDAP y busca en BBDD, por lo que no es clásica */

public class SIGAUserMng extends SIGAGrDDBB
{
  /**
   * Constructor
   * @roseuid 3DABCDD5003C
   * Identify the different Status
   */
  public SIGAUserMng()
  {
    super();
  }


  /**
   * Constructor
   * Identify the different Status
   * @param req provide request information for HTTP servlets
   */

  public SIGAUserMng (HttpServletRequest req){
    super(req);
  }

  /**
   * Constructor
   * Identify the different Status
   * @param req provide request information for HTTP servlets
   * @param htRec Hashtable
   */

  public SIGAUserMng (HttpServletRequest req, Hashtable htRec){
    super(req,htRec);
  }
  public boolean add() throws ClsExceptions {
    return super.add(TableConstants.TABLE_USER);
  }
  public int delete() throws ClsExceptions {
    String[] keyFields = {ColumnConstants.FN_User_ID_USER};
    return super.delete(TableConstants.TABLE_USER, keyFields);
  }
  public boolean update() throws ClsExceptions {
    String[] upDatableFields = loadFields();
    String[] fields = {ColumnConstants.FN_User_ID_USER};
    boolean ret=super.update(TableConstants.TABLE_USER,fields,upDatableFields);
    return ret;
  }
}