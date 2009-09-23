package com.siga.generalRequirements.accessControl.profiles.action;

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
import com.siga.generalRequirements.accessControl.profiles.SIGAProfile;
import com.siga.generalRequirements.accessControl.profiles.form.SIGAProfileForm;
import com.atos.utils.*;

public class SIGAProfileAction extends Action {

  public SIGAProfileAction()   {
    Hashtable htrbackup = null;
    String user = "";
  }
  public Hashtable setDBValues(SIGAProfileForm form, String user)  {
    Hashtable htRecord = form.getData();
    return htRecord;
  }

  public Hashtable setDBValuesAdd(SIGAProfileForm form, String key)  {
    Hashtable htRecord = form.getData();
    htRecord.put(ColumnConstants.FN_PROFILE_ID_PROFILE, key.trim());
    return htRecord;
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ServletException {
    String result = "";
    SIGAProfileForm profileForm = (SIGAProfileForm)form;
    String user = "";
    try {
      SIGAProfile status = new SIGAProfile(req);
      HttpSession ses = req.getSession();
      UsrBean usrbean = (UsrBean)ses.getAttribute("USRBEAN");
      if(usrbean == null)
        throw new ClsExceptions("usrbean is null");
      String access = usrbean.getAccessType();
      req.setAttribute("accessType", access);
      UserTransaction tx = usrbean.getTransaction();
      user = usrbean.getUserName();
      Hashtable hash = getLevelProfiles(req);
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
      if(mode.equalsIgnoreCase("delete")) {
        ActionForward actionforward2 = performDelete(mapping, form, req, res, tx);
        return actionforward2;
      }
      if(mode.equalsIgnoreCase("edit")) {
        ActionForward actionforward3 = performEdit(mapping, form, req, res);
        return actionforward3;
      }
      if(mode.equalsIgnoreCase("new")) {
        ActionForward actionforward4 = performNew(mapping, form, req, res);
        return actionforward4;
      }
      if(mode.equalsIgnoreCase("search")) {
        ActionForward actionforward5 = performNew(mapping, form, req, res);
        return actionforward5;
      }
      if(mode.equalsIgnoreCase("searching")) {
        ActionForward actionforward6 = performSearching(mapping, form, req, res);
        return actionforward6;
      }
      if(mode.equalsIgnoreCase("adding")) {
        ActionForward actionforward7 = performAdding(mapping, form, req, res,user, tx);
        return actionforward7;
      }
    } catch(ClsExceptions ex){
      ex.printStackTrace();
      ex.setProcess("113");
      ClsLogging.writeFileLogError("EXCEPTION in perform method", req, 3);
      ex.prepare(req);
      result = "exception";
      ClsLogging.writeFileLog("result ".concat(String.valueOf(String.valueOf(result))), req, 3);
    }
    return mapping.findForward(result);
  }

  protected ActionForward performListing(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ClsExceptions {
    try {
      String result = "";
      Table gtTable = null;
      gtTable = new Table(req, TableConstants.TABLE_PROFILE, "com.com.siga.generalRequirements.accessControl.profiles.SIGAProfile");
      Vector ord = new Vector();
      ord.addElement(ColumnConstants.FN_PROFILE_ID_PROFILE);
      Vector vec = gtTable.search(ord);
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
      throw new ClsExceptions(e,e.getMessage(),"Profiles","113","GEN00","22");
    }
  }

  protected ActionForward performUpdating(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res, UserTransaction tx)
      throws ClsExceptions {
    String result = "updateOK";
    SIGAProfileForm profileForm = (SIGAProfileForm)form;
    HttpSession ses = req.getSession();
    SIGAProfile profile = new SIGAProfile(req);
    String key = (String)ses.getAttribute("key");
    profile.setData(setDBValuesAdd(profileForm, key));
    Hashtable htrb = (Hashtable)ses.getAttribute("htrbackup");
    if(htrb == null)
      ClsLogging.writeFileLog("htrb IS NULL", req, 3);
    else
      ClsLogging.writeFileLog("htrb.size(): "+htrb.size(),req,3);
    profile.setDataBackup(htrb);
    Table gtTable = new Table(req, TableConstants.TABLE_PROFILE, "com.com.siga.generalRequirements.accessControl.profiles.SIGAProfile");
    gtTable.addFilter(ColumnConstants.FN_PROFILE_ID_PROFILE, key);
    Vector v = gtTable.search();
    if(v == null) {
      result = "recNoExist";
      req.setAttribute("descOperation", "error.messages.deleted");
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);
    }
    String rec = "";
    try {
      tx.begin();
      boolean updatedOk = profile.update();
      Vector ve = (Vector)ses.getAttribute("vectRecords");
      ClsLogging.writeFileLog("ve = "+ve,req,3);
      if(ve != null)
        if(ve.size() > 0) {
      ClsLogging.writeFileLog("ve = "+ve.size(),req,3);
      java.util.Enumeration en=ve.elements();
      while (en.hasMoreElements()){
        com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
        rec=rec+", "+obj.getIdObj();
      }
      ClsExceptions psscEx = new ClsExceptions("");
      throw psscEx;
        } else {
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
        ClsLogging.writeFileLog("Updating Profiles - exception in rollback:" +ex.getMessage(),req,3);
      }
      req.setAttribute("descOperation", "error.messages.noupdated");
      e.setErrorType("9");
      throw e;
    } catch(Exception ex) {
      try {
        tx.rollback();
      } catch(Exception e) {
        ClsLogging.writeFileLog("Updating Profiles - exception in rollback:" +e.getMessage(),req,3);
      }
      result = "recNoExist";
      req.setAttribute("descOperation", "error.messages.noupdated");
      ClsLogging.writeFileLog("result = "+result,req,3);
      ActionForward actionforward = mapping.findForward(result);
      throw new ClsExceptions(ex, ex.toString(), "Profiles","113","GEN00","9");
    }
    return mapping.findForward(result);
  }

  protected ActionForward performEdit(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ClsExceptions {
    String result = "editOK";
    try {
      SIGAProfileForm profileForm = (SIGAProfileForm)form;
      String key = req.getParameter("profileId");
      String desc = req.getParameter("profileDesc");
      String level = req.getParameter("profileLevel");
      HttpSession ses = req.getSession();
      ses.setAttribute("key", key);
      Table gtTable = new Table(req, TableConstants.TABLE_PROFILE, "com.com.siga.generalRequirements.accessControl.profiles.SIGAProfile");
      gtTable.addFilter(ColumnConstants.FN_PROFILE_ID_PROFILE, key);
      Vector v = gtTable.search();
      ClsLogging.writeFileLog("Inside update "+v.size(),req,3);
      if(v != null) {
        String[] compareFields = {ColumnConstants.FN_PROFILE_ID_PROFILE,
          ColumnConstants.FN_PROFILE_DESC_PROFILE,
          ColumnConstants.FN_PROFILE_PROFILE_LEVEL};
        Row row2 = new Row();
        Hashtable profHT = profileForm.getData();
        profHT.put(ColumnConstants.FN_PROFILE_DESC_PROFILE,desc);
        profHT.put(ColumnConstants.FN_PROFILE_PROFILE_LEVEL,level);
        row2.load(profHT);
        Vector vRow = row2.compareBeforeOperation(TableConstants.TABLE_PROFILE,compareFields,req,profileForm.getData());
        if (vRow!=null) { // Data is different so grid is refreshed
          req.setAttribute("descOperation","messages.refreshGrid");
          req.setAttribute("doOperation","/PSSC/executeProfile.do");
          result = "refresh";
        } else {
          Hashtable htr = ((SIGAProfile)v.firstElement()).getData();
          ClsLogging.writeFileLog("htr.size(): "+htr.size(),req,3);
          profileForm.setData(htr);
          ses.setAttribute("htrbackup", htr);
          ClsLogging.writeFileLog("result =  "+result,req,3);
          req.setAttribute("descOperation", "OK");
        }
      } else {
        result = "recNoExist";
        req.setAttribute("descOperation", "error.messages.deleted");
        ClsLogging.writeFileLog("result = "+result,req,3);
      }
    } catch(ClsExceptions ex) {
      ex.setErrorType("22");
      throw ex;
    } catch(Exception ex) {
      throw new ClsExceptions(ex,ex.toString(),"Profiles", "113", "GEN00","22");
    }
    return mapping.findForward(result);
  }

  protected ActionForward performNew(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ClsExceptions {
    String result = "";
    result = "newOk";
    req.setAttribute("descOperation", "OK");
    ClsLogging.writeFileLog("result = "+result,req,3);
    return mapping.findForward(result);
  }

  protected ActionForward performSearching(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
      throws ClsExceptions {
    SIGAProfileForm profileForm = (SIGAProfileForm)form;
    Table mlTable = new Table(req, TableConstants.TABLE_PROFILE, "com.com.siga.generalRequirements.accessControl.profiles.SIGAProfile", profileForm.getData());
    Vector param =  new Vector();
    param.add(ColumnConstants.FN_PROFILE_ID_PROFILE);
    Vector vml = mlTable.search(param);
    if(vml == null)  {
      req.setAttribute("descOperation", "messages.noRecordsFound");
    } else {
      req.setAttribute("container", vml);
      req.setAttribute("descOperation", "OK");
    }
    String result = "listOk";
    return mapping.findForward(result);
  }

  protected ActionForward performAdding(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res, String user, UserTransaction tx)
      throws ClsExceptions {
    String result = "";
    SIGAProfile profile = new SIGAProfile(req);
    SIGAProfileForm profileForm = (SIGAProfileForm)form;
    profile.setData(setDBValues(profileForm, user));
    Vector v=new Vector();
    for(int i=0;i<v.size();i++) {
      SIGAProfile p = (SIGAProfile)v.elementAt(i);
      if(req.getParameter("profileId").equals(p.getProfileId())) {
        req.setAttribute("descOperation", "messages.inserted.duplicated");
        result = "recNoExist";
        return mapping.findForward(result);
      }
    }
    try {
      tx.begin();
      profile.add();
      tx.commit();
      result = "insertOk";
      req.setAttribute("descOperation", "messages.inserted.success");
    } catch (ClsExceptions e) {
      try {
        tx.rollback();
      } catch(Exception ex) {
        ClsLogging.writeFileLog("Adding Profiles - exception in rollback : "+ex.getMessage(), req,3);
      }
      e.setErrorType("7");
      req.setAttribute("descOperation", "error.messages.inserted.error");
      throw e;
    } catch(Exception ex) {
      try {
        tx.rollback();
      } catch(Exception e) {
        ClsLogging.writeFileLog("Adding Profiles - exception in rollback : "+e.getMessage(), req,3);
      }
      throw new ClsExceptions(ex.toString(), TableConstants.TABLE_PROFILE, "113  ", "GEN00", "7");
    }
    return mapping.findForward(result);
  }

  protected ActionForward performDelete(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res, UserTransaction tx)
      throws ClsExceptions {
    String result = "";
    SIGAProfile profile = new SIGAProfile(req);
    SIGAProfileForm profileForm = (SIGAProfileForm)form;
    String pk = req.getParameter("profileId");
    profile.setProfileId(pk);
    SIGAProfile auxProfile = new SIGAProfile(req);
    Table gtTable = new Table(req, TableConstants.TABLE_PROFILE, "com.com.siga.generalRequirements.accessControl.profiles.SIGAProfile");
    gtTable.addFilter(ColumnConstants.FN_PROFILE_ID_PROFILE, pk);
    Vector v = gtTable.search();
    if(v == null) {
      result = "recNoExist";
      req.setAttribute("descOperation", "error.messages.deleted");
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);
    } else {
      try {
        String[] compareFields = {ColumnConstants.FN_PROFILE_ID_PROFILE,
          ColumnConstants.FN_PROFILE_DESC_PROFILE,
          ColumnConstants.FN_PROFILE_PROFILE_LEVEL};
        Row row2 = new Row();
        Hashtable profHT = profileForm.getData();
        row2.load(profHT);
        Vector vRow = row2.compareBeforeOperation(TableConstants.TABLE_PROFILE,compareFields,req,profileForm.getData());
        if (vRow!=null) { // Data is different so grid is refreshed
          req.setAttribute("descOperation","messages.refreshGrid");
          req.setAttribute("doOperation","/PSSC/executeProfile.do");
          result = "refresh";
        } else {
          tx.begin();
          String[] keyFields = {ColumnConstants.FN_ACCESS_RIGHT_PROFILE};
          Row deletedRow = new Row();
          Hashtable hashaccess= new Hashtable();
          hashaccess.put(ColumnConstants.FN_ACCESS_RIGHT_PROFILE,pk);
          deletedRow.load(hashaccess);
          try {
            deletedRow.delete(TableConstants.TABLE_ACCESS_RIGHT,keyFields);
          } catch (ClsExceptions exc) {
            if (!exc.getErrorType().equals("12")) {
              throw exc;
            }
          }
          profile.delete();
          tx.commit();
          result = "deleteOK";
          req.setAttribute("descOperation", "messages.deleted.success");
        }
      } catch (ClsExceptions e) {
        e.setProcess("113");
        req.setAttribute("descOperation", "error.psscprofile.deleted");
        try  {
          tx.rollback();
        } catch(Exception es)  {
          ClsLogging.writeFileLog("Deleting Profiles - exception in rollback : "+es.getMessage(),
                                      req,3);
        }
        throw e;
      } catch(Exception ex)  {
        result = "recNoExist";
        req.setAttribute("descOperation", "error.psscprofile.deleted");
        try {
          tx.rollback();
        } catch(Exception e) {
          ClsLogging.writeFileLog("Deleting Profiles - exception in rollback : "+e.getMessage(),
                                      req,3);
        }
        throw new ClsExceptions(ex,ex.toString(), "Profile", "113", "GEN00", "12");
      }
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
      for(int i = 0; i < vec.size(); i++)   {
        SIGAGrDDBBObject row = (SIGAGrDDBBObject)vec.elementAt(i);
        hash.put(row.getString(ColumnConstants.FN_PROFILE_ID_PROFILE),
                 row.getString(ColumnConstants.FN_PROFILE_PROFILE_LEVEL));
      }
    } catch(ClsExceptions e) {
      e.setErrorType("22");
      throw e;
    } catch(Exception e) {
      throw new ClsExceptions(e,e.toString(),"Profiles", "113","GEN00","22");
    }
    return hash;
  }

}