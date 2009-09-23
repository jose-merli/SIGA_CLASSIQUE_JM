package com.siga.generalRequirements.accessControl.users.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.siga.generalRequirements.accessControl.SIGAGrDDBBObject;
import com.siga.generalRequirements.accessControl.users.SIGAUser;
import com.siga.generalRequirements.accessControl.users.form.SIGAUserForm;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ColumnConstants;
import com.atos.utils.Table;
import com.atos.utils.TableConstants;


/**
 * <p>Tittle: Propulsion Support System Core</p>
 * <p>Description: This class represents the different Status that use PSSC</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SchlumbergerSema </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

public class SIGAUserAction extends Action {

  /**
   * atributtes for setting up the DB object
   * @param form It is the ActionForm bean, which has got some of the needed
   * @param user Modification user
   * @returns: A set up DB object
   */
  public Hashtable setDBValues(SIGAUserForm form){
    Hashtable htRecord = form.getData();
    htRecord.put(ColumnConstants.FN_User_DATE_LAST_MODIFICATION,"SYSDATE");
    return htRecord;
  }

  /**
   * @param mapping .
   * @param form Status Form
   * @param req provide request information for HTTP servlets
   * @param res provide HTTP-specific functionality in sending a response
   * @throws ServletException
   * @return Resulting action
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
                               HttpServletResponse res) throws ServletException  {
    String result ="";
    Hashtable htrbackup=null;
    UserTransaction tx ;
    com.atos.utils.UsrBean usrbean=null;
    Hashtable hash=new Hashtable();

    SIGAUserForm statusForm = (SIGAUserForm) form;
    String user="";

    try{
      SIGAUser status = new SIGAUser(req);
      HttpSession ses= req.getSession();
      usrbean = (com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
      if (usrbean==null)
        throw new ClsExceptions("usrbean is null");

      String access= usrbean.getAccessType();
      req.setAttribute("accessType",access);
      tx = usrbean.getTransaction();
      user = usrbean.getUserName();

      hash = getLevelProfiles(req);

      req.setAttribute("levelprofiles",hash);
      req.setAttribute("myprofilelevel",(String)hash.get(usrbean.getProfile()));

      String mode=req.getParameter("mode");
      if (mode==null)
        throw new ClsExceptions("parameter mode is null");

      ClsLogging.writeFileLog("mode  "+req.getParameter("mode"),req,3);

      if (mode.equalsIgnoreCase("listing")) {
        return performListing(mapping, form, req,res);
      } else if (mode.equalsIgnoreCase("read")) {
        return performRead(mapping, form, req,res,"readOk",usrbean,hash);
      } else if (mode.equalsIgnoreCase("update")) {
        req.setAttribute("mode","update");
        return performRead(mapping, form, req,res,"editOk",usrbean,hash);
      } else if (mode.equalsIgnoreCase("adding")) {
      } else if (mode.equalsIgnoreCase("deleting")) {
      } else if (mode.equalsIgnoreCase("update")) {
      } else if (mode.equalsIgnoreCase("updating")) {
        return performUpdating(mapping, form, req,res,"editOk",tx);
      } else if (mode.equalsIgnoreCase("search")) {
        return performSearch(mapping, form, req,res,"searchOk");
      } else if (mode.equalsIgnoreCase("searching")) {
        return performSearching(mapping, form, req,res,"listOk");
      }
    } catch (ClsExceptions ex){
      ex.printStackTrace();
      ClsLogging.writeFileLogError("EXCEPTION in perform method",req,3);
      if (ex.getMsg()==null) ex.setMsg(" user management ");
      if (ex.getParam()==null) ex.setParam(" unkown ");
      if (ex.getErrorCategory()==null) ex.setErrorCategory("GEN00");
      if (ex.getErrorType()==null) ex.setErrorType("0");
      ex.setProcess("112");
      ex.prepare(req);
      result = "exception";
      ClsLogging.writeFileLog("result "+result,req,3);
    } catch (Exception e) {
      e.printStackTrace();
      ClsLogging.writeFileLogError("EXCEPTION in perform method",req,3);
      ClsExceptions exc=
          new ClsExceptions(e.getMessage(),
          "Attribute User Management ","112","GEN00","9");
      exc.prepare(req);
      result = "exception";
      ClsLogging.writeFileLog("result "+result,req,3);
    }

    return mapping.findForward(result);
  }

  protected ActionForward performListing(ActionMapping mapping,
      ActionForm form, HttpServletRequest req,
      HttpServletResponse res)
      throws ClsExceptions {
    try {
      String result="";
      Table gtTable=null;
      gtTable = new Table(req, TableConstants.TABLE_USER,
                              "com.com.siga.generalRequirements.accessControl.users.SIGAUser");

//      gtTable.addFilter(ColumnConstants.FN_User_ACTIVATED,"0");
      Vector ord = new Vector();
      ord.addElement(ColumnConstants.FN_User_ID_USER + " ASC");
      String query = " select * from " + TableConstants.TABLE_USER+
                     " where " + ColumnConstants.FN_User_ACTIVATED + "=0 ORDER BY " +
                     ColumnConstants.FN_User_ID_USER +  " ASC";
      Vector vec=gtTable.search(query);
      if (vec!=null) {
        for (int i=vec.size();i>0;i--) {
          if (((SIGAUser)vec.elementAt(i-1)).getLDAPuid()==null)
            vec.remove(i-1);
        }
      }
      req.setAttribute("container",vec);
      req.setAttribute("descOperation","OK");
      result = "listOk";
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);
    } catch (ClsExceptions ev) {
      throw ev;
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),
                                  "Attribute User Management ","112","GEN00","9");
    }
  }

  protected ActionForward performRead(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest req,
                                      HttpServletResponse res,
                                      String resultOk,
                                      com.atos.utils.UsrBean usrbean,
                                      Hashtable hash)
      throws ClsExceptions {
	  	/*
    try {
      String desoper="OK";
      String result="";
      Table gtTable=null;

      String userId=(String)req.getParameter("userId");
      if (userId==null)
        throw new ClsExceptions("Parameter userId is null");

      userId=userId.toUpperCase();
      gtTable = new Table(req, TableConstants.TABLE_USER,
                              "com.com.siga.generalRequirements.accessControl.users.SIGAUser");

//      gtTable.addFilter(ColumnConstants.FN_User_ID_USER,userId);
      String sql="select * from " + TableConstants.TABLE_USER + " where UPPER(" +
               ColumnConstants.FN_User_ID_USER + ")='"+userId+"'";
      Vector vec = gtTable.search(sql);
      ClsLogging.writeFileLog("UserMant : Inside update "+vec.size(),req,3);
      if (vec==null) {
        resultOk = "recNoExist";
        desoper="error.messages.deleted";
      } else {
        SIGAUser user=(SIGAUser)vec.elementAt(0);
        Hashtable htr = user.getData();
        ((SIGAUserForm)form).putHas(htr);
		
		*/
		
      /* chequeo de datos anteriores */
	  
	  /*
        HttpSession ses= req.getSession();

        String queryLO = "SELECT ID_LOCATION, SHORT_NAME_LOC FROM SIGA_LOCATION ";
        Vector comboLO = ClsConstants.loadCDHandlerCollection(queryLO,false);
        req.setAttribute("comboLO",comboLO);

        String queryAF="select " + ColumnConstants.FN_AirForce_ID + " , " +
                   ColumnConstants.FN_AirForce_SHORTNAME +
                   " from " + TableConstants.TABLE_AirForce;
        Vector comboAF = ClsConstants.loadCDHandlerCollection(queryAF,false);
        req.setAttribute("comboAF",comboAF);

        String queryLA="select " + ColumnConstants.FN_LANG_ID_LANGUAGE + " , " +
           ColumnConstants.FN_LANG_DESC_LANGUAGE +
             " from " + TableConstants.TABLE_LANGUAJE;
        Vector comboLA = ClsConstants.loadCDHandlerCollection(queryLA,false);
        req.setAttribute("comboLA",comboLA);

        String queryPR="select " + ColumnConstants.FN_PROFILE_ID_PROFILE + " , " +
           ColumnConstants.FN_PROFILE_ID_PROFILE +
             " from " + TableConstants.TABLE_PROFILE +
                       " where " + ColumnConstants.FN_PROFILE_PROFILE_LEVEL +
                       " >= " + (String)hash.get(usrbean.getProfile());
        Vector comboPR = ClsConstants.loadCDHandlerCollection(queryPR,false);
        req.setAttribute("comboPR",comboPR);

        ses.setAttribute("htrbackup",htr);
        req.setAttribute("descOperation",desoper);
        ClsLogging.writeFileLog("result = "+resultOk,req,3);
        String sameUser = (usrbean.getUserName().equalsIgnoreCase(user.getUserId())?"Y":"N");
        req.setAttribute("sameuser",sameUser);
      }
	  */
      return mapping.findForward(resultOk);
	  /*
    } catch (ClsExceptions ev) {
      throw ev;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ClsExceptions(e.getMessage(),
                                  "Attribute User Management ","112","GEN00","9");
    }
	*/
  }

  protected ActionForward performSearch(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest req,
                                        HttpServletResponse res,
                                        String resultOk)
      throws ClsExceptions {
	  	/*
    try {
      String result="";
      ((SIGAUserForm)form).putHas(null);
      String queryLO = "SELECT ID_LOCATION, SHORT_NAME_LOC FROM SIGA_LOCATION ";
      Vector comboLO = ClsConstants.loadCDHandlerCollection(queryLO,true);
      req.setAttribute("comboLO",comboLO);

      String queryAF="select " + ColumnConstants.FN_AirForce_ID + " , " +
                 ColumnConstants.FN_AirForce_SHORTNAME +
                 " from " + TableConstants.TABLE_AirForce;
      Vector comboAF = ClsConstants.loadCDHandlerCollection(queryAF,true);
      req.setAttribute("comboAF",comboAF);

      String queryLA="select " + ColumnConstants.FN_LANG_ID_LANGUAGE + " , " +
         ColumnConstants.FN_LANG_DESC_LANGUAGE +
           " from " + TableConstants.TABLE_LANGUAJE;
      Vector comboLA = ClsConstants.loadCDHandlerCollection(queryLA,true);
      req.setAttribute("comboLA",comboLA);

      String queryPR="select " + ColumnConstants.FN_PROFILE_ID_PROFILE + " , " +
         ColumnConstants.FN_PROFILE_ID_PROFILE +
             " from " + TableConstants.TABLE_PROFILE;
      Vector comboPR = ClsConstants.loadCDHandlerCollection(queryPR,true);
      req.setAttribute("comboPR",comboPR);

      req.setAttribute("descOperation","OK");
      String sameUser = "N";
      req.setAttribute("sameuser",sameUser);
	  */
      return mapping.findForward(resultOk);
	  /*
    } catch (ClsExceptions ev) {
      throw ev;
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),
                                  "Attribute User Management ","112","GEN00","9");
    }
*/
  }

  protected ActionForward performUpdating(ActionMapping mapping, ActionForm form,
      HttpServletRequest req,
      HttpServletResponse res,
      String resultOk,
      UserTransaction tx)
      throws ClsExceptions {
    String result=resultOk;
    HttpSession ses = req.getSession();
    Hashtable htrb = (Hashtable)ses.getAttribute("htrbackup");
    if (htrb==null)	 ClsLogging.writeFileLog("htrb IS NULL",req,3);
    else ClsLogging.writeFileLog("htrb.size(): "+htrb.size(),req,3);

    SIGAUser user = new SIGAUser(req);
    user.setData(setDBValues((SIGAUserForm)form));
    user.setDataBackup(htrb);
    //Obtain the primary key to search the record

    String key = ((SIGAUserForm)form).getUserId();
    key=key.toUpperCase();
    Table gtTable = new Table (req,TableConstants.TABLE_USER,
                                       "com.com.siga.generalRequirements.accessControl.users.SIGAUser");

//    gtTable.addFilter(ColumnConstants.FN_User_ID_USER,key);
    String sql="select * from " + TableConstants.TABLE_USER + " where UPPER(" +
               ColumnConstants.FN_User_ID_USER + ")='"+key+"'";
    Vector v= gtTable.search(sql);

// the record was deleted by other user
    if (v==null){
      result = "recNoExist";
      req.setAttribute("descOperation","error.messages.deleted");
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);      //There are some validation error
    }  else{
      String rec="";
      try{
        tx.begin();
        user.update();
        Vector ve = (Vector)ses.getAttribute("vectRecords");
        if (ve!=null){
          ClsLogging.writeFileLog("ve = "+ve,req,3);
          if (ve.size()>0)
          {
            ClsLogging.writeFileLog("ve = "+ve.size(),req,3);
            java.util.Enumeration en=ve.elements();
            while (en.hasMoreElements()){
              com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
              rec=rec+", "+obj.getIdObj();
            }
            ClsExceptions psscEx = new ClsExceptions("");
            throw psscEx;
          }
        }
        tx.commit();
        result ="updating";
        req.setAttribute("descOperation","messages.updated.success");
      }catch (ClsExceptions ev){
        try {
          tx.rollback();
        } catch (Exception e) {
          ClsLogging.writeFileLog("UserMngt : exception in rollback : "+e.getMessage(),
                                      req,3);
        }
        throw ev;
      }catch (Exception ex){
        try {
          tx.rollback();
        } catch (Exception e) {
          ClsLogging.writeFileLog("UserMngt : exception in rollback : "+e.getMessage(),
                                      req,3);
        }
  /*
        throw new ClsExceptions(ex.toString(),
                                    TableConstants.TABLE_STATUS,"112","GEN00","9");
									*/
      }
    }
    return mapping.findForward(result);
  }

  protected ActionForward performSearching(ActionMapping mapping, ActionForm form,
      HttpServletRequest req,
      HttpServletResponse res,
      String resultOk)
      throws ClsExceptions {
    try {
      String result="";
      Table gtTable=null;
      gtTable = new Table(req, TableConstants.TABLE_USER,
                              "com.com.siga.generalRequirements.accessControl.users.SIGAUser");

      SIGAUser user = new SIGAUser(req);
      user.setData(setDBValues((SIGAUserForm)form));

      Hashtable has=((SIGAUserForm)form).getData();

      String[] campos = user.loadFields(SIGAUser.DDBB);
      for (int i=0;i<campos.length;i++) {
        String value=(String)has.get(campos[i]);
        if (ColumnConstants.FN_User_DATE_LAST_MODIFICATION.equals(campos[i]))
          continue;
        if (value!=null && !value.equals("") && !value.equals(" ")) {
          gtTable.addFilter(campos[i],value);
        }
      }
      gtTable.addFilter(ColumnConstants.FN_User_ACTIVATED,"0");
      Vector vec=gtTable.search();
      if (vec!=null) {
        for (int i=vec.size();i>0;i--) {
          if (((SIGAUser)vec.elementAt(i-1)).getLDAPuid()==null)
            vec.remove(i-1);
        }
      }
      req.setAttribute("container",vec);
      req.setAttribute("descOperation","OK");
      result = "listOk";
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);
    } catch (ClsExceptions ev) {
      throw ev;
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),
                                  "Attribute User Management ","112","GEN00","9");
    }
  }

  protected Hashtable getLevelProfiles(HttpServletRequest req)
  throws ClsExceptions {
    Hashtable hash = new Hashtable();
    try {
      Table tabProf=new Table(req, TableConstants.TABLE_PROFILE,
                                      "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
      String sql="select " + ColumnConstants.FN_PROFILE_ID_PROFILE + " , " +
                 ColumnConstants.FN_PROFILE_PROFILE_LEVEL +
                 " from " + TableConstants.TABLE_PROFILE;
      Vector vec=tabProf.search(sql);
      for (int i=0;i<vec.size();i++) {
        SIGAGrDDBBObject row=(SIGAGrDDBBObject)vec.elementAt(i);
        hash.put(row.getString(ColumnConstants.FN_PROFILE_ID_PROFILE),
                 row.getString(ColumnConstants.FN_PROFILE_PROFILE_LEVEL));
      }
    } catch (ClsExceptions ed) {
      throw ed;
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),
                                  "Attribute User Management ","112","GEN00","9");
    }
    return hash;
  }
}