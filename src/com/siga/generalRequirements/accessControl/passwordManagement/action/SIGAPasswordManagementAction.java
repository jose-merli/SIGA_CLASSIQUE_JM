package com.siga.generalRequirements.accessControl.passwordManagement.action;

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
import com.siga.generalRequirements.accessControl.passwordManagement.SIGAPasswordManagement;
import com.siga.generalRequirements.accessControl.passwordManagement.form.SIGAPasswordManagementForm;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ColumnConstants;
import com.atos.utils.Crypter;
import com.atos.utils.Table;
import com.atos.utils.TableConstants;
import com.atos.utils.UsrBean;

public class SIGAPasswordManagementAction extends Action {

  public SIGAPasswordManagementAction()
  {
  }

  public Hashtable setDBValues(SIGAPasswordManagementForm form, String user) {
    Hashtable htRecord = form.getData();
    return htRecord;
  }

  public Hashtable setDBValuesAdd(SIGAPasswordManagementForm form, String key) {
    Hashtable htRecord = form.getData();
    htRecord.put(ColumnConstants.FN_User_ID_USER, key.trim());
    return htRecord;
  }

  public Hashtable setDBValuesPassword(SIGAPasswordManagementForm form, String key) {
    Hashtable htRecord = form.getData();
    htRecord.put(ColumnConstants.FN_User_PASSWORD, key.trim());
    return htRecord;
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ServletException {
    String result = "";
    SIGAPasswordManagementForm passwordForm = (SIGAPasswordManagementForm)form;
    String user = "";
    try  {
      SIGAPasswordManagement status = new SIGAPasswordManagement(req);
      HttpSession ses = req.getSession();
      UsrBean usrbean = (UsrBean)ses.getAttribute("USRBEAN");
      if(usrbean == null)
        throw new ClsExceptions("usrbean is null");
      String access = usrbean.getAccessType();
      req.setAttribute("accessType", access);
      UserTransaction tx = usrbean.getTransaction();
      user = usrbean.getUserName();
      Hashtable hash = getLevelProfiles(req);
      req.setAttribute("myuser", user);
      req.setAttribute("levelprofiles", hash);
      req.setAttribute("myprofilelevel", (String)hash.get(usrbean.getProfile()));
      req.setAttribute("myprofileid", usrbean.getProfile());
      String mode = req.getParameter("mode");
      if(mode == null)
        throw new ClsExceptions("parameter mode is null");
      ClsLogging.writeFileLog("mode  "+req.getParameter("mode"),req,3);
      if(mode.equalsIgnoreCase("listing")) {
        ActionForward actionforward = performListing(mapping, form, req, res);
        return actionforward;
      }
      if(mode.equalsIgnoreCase("updating")) {
        ActionForward actionforward1 = performUpdating(mapping, form, req, res, tx);
        return actionforward1;
      }

      if(mode.equalsIgnoreCase("edit")) {
        ActionForward actionforward3 = performEdit(mapping, form, req, res);
        return actionforward3;
      }

    } catch(ClsExceptions ex) {
      ex.printStackTrace();
      ex.setProcess("517");
      ex.setErrorCategory("GEN00");
      ClsLogging.writeFileLogError("EXCEPTION in perform method", req, 3);
      ex.prepare(req);
      result = "exception";
    }
    return mapping.findForward(result);
  }

  protected ActionForward performListing(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ClsExceptions {
    try {
      String result = "";
      Table gtTable = null;
      gtTable = new Table(req, TableConstants.TABLE_USER, "com.com.siga.generalRequirements.accessControl.passwordManagement.SIGAPasswordManagement");
      Vector order = new Vector();
      order.add(ColumnConstants.FN_User_ID_USER);
      Vector vec = gtTable.search(order);
      req.setAttribute("container", vec);
      req.setAttribute("descOperation", "OK");
      result = "listOk";
      ClsLogging.writeFileLog("result = "+result,req,3);
      ActionForward actionforward = mapping.findForward(result);
      return actionforward;
    } catch(ClsExceptions ev) {
      ev.setErrorType("22");
      throw ev;
    } catch(Exception e) {
      throw new ClsExceptions(e.getMessage(),"Password Management","517","GEN00","22");
    }
  }

  protected ActionForward performUpdating(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res, UserTransaction tx)
      throws ClsExceptions {
    String result = "updateOK";
    ClsLogging.writeFileLog("SIGAPasswordManagementAction: mode=updating ", req, 3);
    SIGAPasswordManagementForm passwordForm = (SIGAPasswordManagementForm)form;
    String myUser = ((String)req.getAttribute("myuser"));
    HttpSession ses = req.getSession();
    SIGAPasswordManagement password = new SIGAPasswordManagement(req);
    String key = (String)ses.getAttribute("key");
    password.setData(setDBValuesAdd(passwordForm, key));
    Hashtable htrb = (Hashtable)ses.getAttribute("htrbackup");
    if(htrb == null)
      ClsLogging.writeFileLog("htrb IS NULL", req, 3);
    else
      ClsLogging.writeFileLog("htrb.size(): "+htrb.size(),req,3);
    password.setDataBackup(htrb);
    String passwor = req.getParameter("password");
    Crypter cryp = new Crypter();
    passwor = cryp.doIt(passwor);
    Table gtTable = new Table(req, TableConstants.TABLE_USER, "com.com.siga.generalRequirements.accessControl.users.SIGAUser");
    gtTable.addFilter(ColumnConstants.FN_User_ID_USER, myUser);
    gtTable.addFilter(ColumnConstants.FN_User_PASSWORD, passwor);
    Vector v = gtTable.search();
    if(v == null) {
      result = "recNoExist";
      req.setAttribute("descOperation", "error.psscpassword.mypass");
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);
    }
    String newPassword = req.getParameter("newPassword");
    String confPassword = req.getParameter("confPassword");
    newPassword = cryp.doIt(newPassword);
    confPassword = cryp.doIt(confPassword);
    if(!newPassword.equals(confPassword)) {
      result = "recNoExist";
      req.setAttribute("descOperation", "error.psscpassword.conf");
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);

    }
    password.setData(setDBValuesPassword(passwordForm, newPassword));
    String rec = "";
    try  {
      tx.begin();
      boolean updatedOk = password.update();
      Vector ve = (Vector)ses.getAttribute("vectRecords");
      ClsLogging.writeFileLog("ve = "+ve,req,3);
      if(ve != null)
        if(ve.size() > 0)  {
      ClsLogging.writeFileLog("ve = "+ve.size(),req,3);
      java.util.Enumeration en=ve.elements();
      while (en.hasMoreElements()){
        com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
        rec=rec+", "+obj.getIdObj();
      }
      ClsExceptions psscEx = new ClsExceptions("");
      throw psscEx;
        } else  {
          ActionForward actionforward1 = mapping.findForward(result);
          return actionforward1;
        }
        if(updatedOk) {
          tx.commit();
          req.setAttribute("descOperation", "messages.updated.success");
        } else {
          req.setAttribute("descOperation", "error.messages.noupdated");
        }
        result = "updateOK";
    } catch (ClsExceptions e) {
      try {
        tx.rollback();
      } catch(Exception ex) {
        ClsLogging.writeFileLog("Password Management - exception in rollback: " +ex.getMessage(), req,3);
      }
      e.setErrorType("9");
      req.setAttribute("descOperation", "error.messages.noupdated");
      throw e;
    } catch(Exception ex)  {
      try {
        tx.rollback();
      } catch(Exception e) {
        ClsLogging.writeFileLog("Password Management - exception in rollback : "+e.getMessage(), req,3);
      }
      result = "recNoExist";
      req.setAttribute("descOperation", "error.messages.noupdated");
      throw new ClsExceptions(ex,ex.toString(),"Password Management","517","GEN00","9");
    }
    return mapping.findForward(result);
  }

  protected ActionForward performEdit(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ClsExceptions {
    String result = "editOK";
    try {
      String key = req.getParameter("userId");
      HttpSession ses = req.getSession();
      ses.setAttribute("key", key);
      Table gtTable = new Table(req, TableConstants.TABLE_USER, "com.com.siga.generalRequirements.accessControl.passwordManagement.SIGAPasswordManagement");
      gtTable.addFilter(ColumnConstants.FN_User_ID_USER, key);
      Vector v = gtTable.search();
      ClsLogging.writeFileLog("Inside update "+v.size(),req,3);
      if(v != null) {
        Hashtable htr = ((SIGAPasswordManagement)v.firstElement()).getData();
        ClsLogging.writeFileLog("htr.size(): "+htr.size(),req,3);
        SIGAPasswordManagementForm passwordForm = (SIGAPasswordManagementForm)form;
        passwordForm.setData(htr);
        ses.setAttribute("htrbackup", htr);
        ClsLogging.writeFileLog("result =  "+result,req,3);
        req.setAttribute("descOperation", "OK");
      } else {
        result = "recNoExist";
        req.setAttribute("descOperation", "error.messages.deleted");
        ClsLogging.writeFileLog("result = "+result,req,3);
        return mapping.findForward(result);
      }
    } catch(ClsExceptions ex) {
      ex.setErrorType("22");
      throw ex;
    } catch(Exception ex) {
      throw new ClsExceptions(ex,ex.toString(),"Password Management","517","GEN00","22");
    }
    return mapping.findForward(result);
  }


  protected Hashtable getLevelProfiles(HttpServletRequest req) throws ClsExceptions {
    Hashtable hash = new Hashtable();
    try {
      Table tabProf = new Table(req, TableConstants.TABLE_PROFILE, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
      String sql="select " + ColumnConstants.FN_PROFILE_ID_PROFILE + " , " +
                 ColumnConstants.FN_PROFILE_PROFILE_LEVEL +
                 " from " + TableConstants.TABLE_PROFILE;
      Vector vec = tabProf.search(sql);
      for(int i = 0; i < vec.size(); i++)  {
        SIGAGrDDBBObject row = (SIGAGrDDBBObject)vec.elementAt(i);
        hash.put(row.getString(ColumnConstants.FN_PROFILE_ID_PROFILE),
                 row.getString(ColumnConstants.FN_PROFILE_PROFILE_LEVEL));
      }
    } catch(ClsExceptions ex) {
      ex.setErrorType("22");
      throw ex;
    } catch(Exception e) {
      throw new ClsExceptions(e,e.toString(),"Password Management","517","GEN00","22");
    }
    return hash;
  }
}