package com.siga.generalRequirements.accessControl.users.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
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

import com.siga.Utilidades.SIGAReferences;
import com.siga.generalRequirements.accessControl.SIGAGrDDBBObject;
import com.siga.generalRequirements.accessControl.users.SIGAUser;
import com.siga.generalRequirements.accessControl.users.SIGAUserMng;
import com.siga.generalRequirements.accessControl.users.form.SIGAUserMngForm;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ColumnConstants;
import com.atos.utils.Crypter;
import com.atos.utils.ReadProperties;
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

public class SIGAUserMngAction extends Action {
  static final public String ENABLE = "enable";
  static final public String DISABLE = "disable";

  /**
   * atributtes for setting up the DB object
   * @param form It is the ActionForm bean, which has got some of the needed
   * @param user Modification user
   * @returns: A set up DB object
   */
  public Hashtable setDBValues(SIGAUserMngForm form){
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
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest req,
                               HttpServletResponse res)
      throws ServletException  {

    Hashtable htrbackup=null;
    UserTransaction tx ;

    Hashtable hash=new Hashtable(); // Hashtable de profiles
    String mylevelprofile="100";
    String access="DENY";
    String user="";

    String result ="";
    SIGAUserMngForm statusForm = (SIGAUserMngForm) form;
    try{
      SIGAUser status = new SIGAUser(req);
      HttpSession ses= req.getSession();
      com.atos.utils.UsrBean usrbean = (com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
      if (usrbean==null)
        throw new ClsExceptions("usrbean is null");

      access= usrbean.getAccessType();
      req.setAttribute("accessType",access);
      tx = usrbean.getTransaction();
      user = usrbean.getUserName();

      hash = getLevelProfiles(req);

      req.setAttribute("levelprofiles",hash);
      mylevelprofile = (String)hash.get(usrbean.getProfile());
      req.setAttribute("myprofilelevel",mylevelprofile);

      String mode=req.getParameter("mode");
      if (mode==null)
        throw new ClsExceptions("parameter mode is null");

      ClsLogging.writeFileLog("mode  "+req.getParameter("mode"),req,3);
      if (mode.equalsIgnoreCase("listing")) {
        return performListing(mapping, form, req,res,ses);
      } else if (mode.equalsIgnoreCase("enabled")) {
        return performEnable(mapping, form, req,res,"refresh",ENABLE,tx);
      } else if (mode.equalsIgnoreCase("disabled")) {
        return performEnable(mapping, form, req,res,"refresh",DISABLE,tx);
      } else if (mode.equalsIgnoreCase("import")) {
        req.setAttribute("mode","import");
        req.setAttribute("descOperation","OK");
        return mapping.findForward("importOk");
      } else if (mode.equalsIgnoreCase("importing")) {
        return performImport(mapping, form, req,res,ses);
      } else if (mode.equalsIgnoreCase("search")) {
        return performSearch(mapping, form, req,res,"searchOk");
      } else if (mode.equalsIgnoreCase("searching")) {
        return performSearching(mapping, form, req,res,ses,"listOk",mylevelprofile,
                                user,access,hash);
      } else if (mode.equalsIgnoreCase("allenable")) {
        return performAllenabledisable(mapping, form, req,res,ses,ENABLE,tx);
      } else if (mode.equalsIgnoreCase("alldisable")) {
        return performAllenabledisable(mapping, form, req,res,ses,DISABLE,tx);
      } else if (mode.equalsIgnoreCase("allimport")) {
        return performAllImport(mapping, form, req,res,ses);
      }
    } catch (ClsExceptions ex){
      ClsLogging.writeFileLogError("EXCEPTION in perform method",req,3);
      ex.prepare(req);
      result = "exception";
      ClsLogging.writeFileLog("result "+result,req,3);
    } catch (Exception e) {
      ClsExceptions exc= new ClsExceptions(e.toString(),
          "User Management","514","GEN00","9");
      exc.prepare(req);
      result = "exception";
      ClsLogging.writeFileLog("result "+result,req,3);
    }
    return mapping.findForward(result);
  }

  protected ActionForward performListing(ActionMapping mapping,
      ActionForm form, HttpServletRequest req,
      HttpServletResponse res,
      HttpSession ses)
      throws ClsExceptions {
    try {
      String result="listOk";
      Vector vec= fillUsers(req,ses);
      req.setAttribute("container",vec);
      req.setAttribute("descOperation","OK");
      return mapping.findForward(result);
    } catch (ClsExceptions ec) {
      throw ec;
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),
                                  "User Management","514","GEN00","9");
    }
  }

  protected void importUser(HttpServletRequest req,String userId,
                            HttpSession ses, String airForce, String location)
      throws Exception {
    com.atos.utils.LdapUserEJ ldapUser=null;

    //ldapUser=new PSSCLdapUser(userId,null,req);

    /******** RELEVANTE ********/
    com.atos.utils.Mngjndi jnd = new com.atos.utils.Mngjndi();
    com.atos.utils.LdapUserEJHome home = jnd.lookupHome();
    Object obje = home.create(userId,null,req.getHeader("user-agent"),
                              req.getRemoteAddr());
    ldapUser = (com.atos.utils.LdapUserEJ)
               jnd.narrow(obje, com.atos.utils.LdapUserEJ.class);
    /******** RELEVANTE ********/
    String INITCTX = "";
    String MY_HOST[] = {""};
    String MGR_DN = "";
    String MGR_PW = "";
    String MY_SEARCHBASE = "";

    ReadProperties ldapProperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.LDAP);
//    ReadProperties ldapProperties=new ReadProperties("ldap.properties");
    INITCTX = ldapProperties.returnProperty("LDAP.INITCTX");
    String hosts = ldapProperties.returnProperty("LDAP.MY_HOST");
    StringTokenizer tok=new StringTokenizer(hosts,";");
    int i=0;
    MY_HOST=new String[tok.countTokens()];
    while (tok.hasMoreTokens()) MY_HOST[i++]=tok.nextToken();
    MGR_DN = ldapProperties.returnProperty("LDAP.MGR_DN");
    MGR_PW = ldapProperties.returnProperty("LDAP.MGR_PW");
    MY_SEARCHBASE = ldapProperties.returnProperty("LDAP.MY_SEARCHBASE");

    ldapUser.serLDAPConf(INITCTX,MY_HOST,MGR_DN,MGR_PW,MY_SEARCHBASE);
    ldapUser.fillinLdapData();
    Hashtable has= new Hashtable();
// relleno los campos de importacion
    has.put(ColumnConstants.FN_User_ID_USER,ldapUser.getUid().toUpperCase());
    has.put(ColumnConstants.FN_User_ID_LANGUAGE, ldapUser.getLang().toUpperCase());
    has.put(ColumnConstants.FN_User_ID_PROFILE, ldapUser.getProfile().toUpperCase());
    has.put(ColumnConstants.FN_User_ACTIVATED,"0");
    has.put(ColumnConstants.FN_User_DATE_LAST_MODIFICATION,"SYSDATE");
    has.put(ColumnConstants.FN_User_DESC_USER,ldapUser.getCn());
    has.put(ColumnConstants.FN_User_ID_AIRFORCE,airForce);
    has.put(ColumnConstants.FN_User_ID_LOCATION,location);
    Crypter crypt= new Crypter();
    String pssw=crypt.doIt("pssc");
    has.put(ColumnConstants.FN_User_PASSWORD,pssw);
    SIGAUserMng user=new SIGAUserMng(req);
    user.setData(has);
    user.add();
  }

  protected ActionForward performAllImport(ActionMapping mapping,
      ActionForm form, HttpServletRequest req,
      HttpServletResponse res,
      HttpSession ses)
      throws ClsExceptions {
    try {
      String result="refresh";
      String desoper="";
      String userId=((SIGAUserMngForm)form).getUserId();
      if (userId==null)
        throw new ClsExceptions("Parameter userId is null");

      userId=userId.toUpperCase();

      StringTokenizer tok = new StringTokenizer(userId," ");
      try {
        while (tok.hasMoreTokens()) {
          importUser(req,tok.nextToken(),ses,
                     ((SIGAUserMngForm)form).getAirForce(),
                     ((SIGAUserMngForm)form).getLocation());
        }
      } catch (Exception e) {
        ClsExceptions exc= new ClsExceptions(e.toString(),
            "User Management : massive user import","514","GEN00","9");
        throw exc;
      }
      desoper="messages.import.success";
      req.setAttribute("descOperation",desoper);
      return mapping.findForward(result);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ClsExceptions(e.getMessage());
    }

  }

  protected ActionForward performImport(ActionMapping mapping,
                                        ActionForm form, HttpServletRequest req,
                                        HttpServletResponse res,
                                        HttpSession ses)
      throws ClsExceptions {
    try {
      String result="refresh";
      String desoper="";
      String userId=((SIGAUserMngForm)form).getUserId();
      if (userId==null)
        throw new ClsExceptions("Parameter userId is null");

      userId=userId.toUpperCase();
      importUser(req,userId,ses,
                 ((SIGAUserMngForm)form).getAirForce(),
                 ((SIGAUserMngForm)form).getLocation());

      desoper="messages.import.success";
      req.setAttribute("descOperation",desoper);
      return mapping.findForward(result);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ClsExceptions(e.getMessage(),
                                  "User Management : user import","514","GEN00","9");
    }
  }

  protected ActionForward performAllenabledisable(ActionMapping mapping,
      ActionForm form,
      HttpServletRequest req,
      HttpServletResponse res,
      HttpSession ses,
      String action,
      UserTransaction tx)
      throws ClsExceptions {
    try {
      String resultOk="ok";
      String userId=(String)req.getParameter("userId");
      String desoper="OK";
      String result="";
      if (userId==null)
        throw new ClsExceptions("Parameter userId is null");

      userId=userId.toUpperCase();
      StringTokenizer tok = new StringTokenizer(userId," ");
      while (tok.hasMoreTokens()) {
        result=updateDDBB(req, tok.nextToken(),resultOk, action,tx);
      }
      if (result.equals("updating")) {
        desoper="messages.updated.success";
        result="refresh";
      } else if (result.equals("recNoExist")) {
        desoper="error.messages.deleted";
      }
      req.setAttribute("descOperation",desoper);
      ClsLogging.writeFileLog("result = "+result,req,3);
      return mapping.findForward(result);
    } catch (ClsExceptions ev) {
      throw ev;
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage());
    }
  }

  protected String updateDDBB(HttpServletRequest req, String userId,
                              String resultOk, String action, UserTransaction tx)
      throws ClsExceptions {

    Table gtTable = new Table(req, TableConstants.TABLE_USER,
                                      "com.com.siga.generalRequirements.accessControl.users.SIGAUserMng");
/*    gtTable.addFilter(ColumnConstants.FN_User_ID_USER,userId);  */
    String sql="select * from " + TableConstants.TABLE_USER + " where UPPER(" +
               ColumnConstants.FN_User_ID_USER + ")='"+userId+"'";
    Vector vec = gtTable.search(sql);
    ClsLogging.writeFileLog("UserMant : Inside update "+vec.size(),req,3);
    HttpSession ses = req.getSession();

    if (vec==null) {
      resultOk = "recNoExist";
    } else {
      SIGAUserMng user=(SIGAUserMng)vec.elementAt(0);
      Hashtable htr = user.getData();
      user.setDataBackup(htr);
      Hashtable has2=new Hashtable();
      Enumeration el=htr.keys();
      while (el.hasMoreElements()) {
        String key=(String)el.nextElement();
        has2.put(key,htr.get(key));
      }
      if (ENABLE.equalsIgnoreCase(action)) {
        has2.put(ColumnConstants.FN_User_ACTIVATED,"0");
      } else {
        has2.put(ColumnConstants.FN_User_ACTIVATED,"1");
      }
      user.setData(has2);
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
            if (!rec.equals(""))
              psscEx.setMsg("CHANGES INTO: "+rec);
            psscEx.setErrorType("9");
            psscEx.setParam(" users ");
            psscEx.setProcess("514");
            throw psscEx;
          }
        }
        tx.commit();
        resultOk ="updating";
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
        throw new ClsExceptions(ex.toString(),
                                    "User management" ,"514","GEN00","9");
      }
    }
    return resultOk;
  }
  protected ActionForward performEnable(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest req,
                                        HttpServletResponse res,
                                        String resultOk,
                                        String action,
                                        UserTransaction tx)
      throws ClsExceptions {
    try {
      String desoper="OK";
      String result="";
      Table gtTable=null;

      String userId=(String)req.getParameter("userId");
      if (userId==null)
        throw new ClsExceptions("Parameter userId is null");

      userId=userId.toUpperCase();
      result=updateDDBB(req, userId,resultOk, action,tx);
      if (result.equals("updating")) {
        desoper="messages.updated.success";
      } else if (result.equals("recNoExist")) {
        desoper="error.messages.deleted";
      }
      req.setAttribute("descOperation",desoper);
      ClsLogging.writeFileLog("result = "+resultOk,req,3);
      return mapping.findForward(resultOk);
    } catch (ClsExceptions ev) {
      throw ev;
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),"User Management : user import","514","GEN00","9");
    }
  }

  protected ActionForward performSearch(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest req,
                                        HttpServletResponse res,
                                        String resultOk)
      throws ClsExceptions {
	  	/*
    try {
      String result="";
//      ((SIGAUserMngForm)form).putHas(null);
      req.setAttribute("descOperation","OK");
      String queryLO = "SELECT ID_LOCATION, SHORT_NAME_LOC FROM SIGA_LOCATION ";
      Vector comboLO = ClsConstants.loadCDHandlerCollection(queryLO,false);
      req.setAttribute("comboLO",comboLO);

      String queryAF="select " + ColumnConstants.FN_AirForce_ID + " , " +
                     ColumnConstants.FN_AirForce_SHORTNAME +
                     " from " + TableConstants.TABLE_AirForce;
      Vector comboAF = ClsConstants.loadCDHandlerCollection(queryAF,false);
      req.setAttribute("comboAF",comboAF);*/
      return mapping.findForward(resultOk);
	  /*
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),"User Management : user import","514","GEN00","9");
    }
*/
  }

  protected ActionForward performSearching(ActionMapping mapping, ActionForm form,
      HttpServletRequest req,
      HttpServletResponse res,
      HttpSession ses,
      String resultOk, String mylevelprofile,
      String user,
      String access,
      Hashtable hash)
      throws ClsExceptions {
    try {

      // El proceso de busqueda es complejo, porque debe buscar en ldap y en bbdd
      SIGAUserMngForm formMng=(SIGAUserMngForm)form;
      // Varios tipos de busquedas.
      // 1 - Solo en LDAP
      // 2 - Solo en DDBB
      // 3 - En ambos a la vez
      // 4 - En cualquiera

      // si existe en DDBB -> puede
      // 1 - Estar activo
      // 2 - Estar inactivo
      // 3 - cualquiera

      boolean checkLdap=false;
      boolean checkDDBB=false;
      boolean debeLdap=false;
      boolean debeDDBB=false;
      boolean checkStatus=false;
      boolean debeActive=false;

      String aux=formMng.getInldap();
      if (aux!=null && !aux.equals("") && !aux.equals(" ")) {
        checkLdap=true;
        if (aux.equals("1")) {
          debeLdap=true;
        }
      }


      aux=formMng.getInddbb();
      if (aux!=null && !aux.equals("") && !aux.equals(" ")) {
        checkDDBB=true;
        if (aux.equals("1")) {
          debeDDBB=true;
        }
      }
      if (debeDDBB)  {
        aux=formMng.getStatus();
        if (aux!=null && !aux.equals("") && !aux.equals(" ")) {
          checkStatus=true;
          if (aux.equals("1")) {
            debeActive=true;
          }
        }
      }
      int mynivel = Integer.parseInt(mylevelprofile);
      Vector usersvector = fillUsers(req,ses);
      for (int j=usersvector.size()-1;j>=0;j--) {
        Hashtable elemento=(Hashtable)usersvector.elementAt(j);
        if (checkLdap) {
          if (debeLdap) {
            if ("N".equalsIgnoreCase((String)elemento.get("inldap"))) {
              usersvector.remove(j);
              continue;
            }
          } else {
            if ("Y".equalsIgnoreCase((String)elemento.get("inldap"))) {
              usersvector.remove(j);
              continue;
            }
          }
        }

        if (checkDDBB) {
          if (debeDDBB) {
            if ("N".equalsIgnoreCase((String)elemento.get("inddbb"))) {
              usersvector.remove(j);
              continue;
            }
          } else {
            if ("Y".equalsIgnoreCase((String)elemento.get("inddbb"))) {
              usersvector.remove(j);
              continue;
            }
          }
        }
        if (checkStatus) {
          if (debeActive) {
            if ("0".equalsIgnoreCase((String)elemento.get("status"))) {
              usersvector.remove(j);
              continue;
            }
          } else {
            if ("1".equalsIgnoreCase((String)elemento.get("status"))) {
              usersvector.remove(j);
              continue;
            }
          }
        }
        // filtro de yo mismo y de nivel de profile
        if (!checkDDBB && !checkLdap)
          continue;
        if (user.equals((String)elemento.get("uid"))) {
          usersvector.remove(j);
          continue;
        }

        String profil = (String)hash.get((String)elemento.get("profile"));
        int hisprofile = Integer.parseInt(profil);
        if (mynivel>hisprofile) {
          usersvector.remove(j);
          continue;
        }
      }

      String action = null;
      if (access=="FULL") {
        if (debeDDBB && checkStatus) {
          if (debeActive)
            action="allactives";
          else
            action="allinactives";
        } else if (debeLdap && checkDDBB && !debeDDBB) {
          action="allimports";
        }
      }
      req.setAttribute("container",usersvector);
      req.setAttribute("action",action);
      req.setAttribute("descOperation","OK");
      return mapping.findForward("listOk");
    } catch (ClsExceptions ev) {
      ev.printStackTrace();
      throw ev;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ClsExceptions(e.getMessage(),"User Management : user import","514","GEN00","9");
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
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),"User Management : user import","514","GEN00","9");
    }
    return hash;
  }

  protected String[] getProfiles(HttpServletRequest req)
      throws ClsExceptions  {
    String[] toRet=null;
    try {
      Table tabProf=new Table(req, TableConstants.TABLE_PROFILE,
                                      "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
      String sql="select " + ColumnConstants.FN_PROFILE_ID_PROFILE +
                 " from " + TableConstants.TABLE_PROFILE;
      Vector vec=tabProf.search(sql);
      toRet=new String[vec.size()];
      for (int i=0;i<vec.size();i++) {
        SIGAGrDDBBObject row=(SIGAGrDDBBObject)vec.elementAt(i);
        toRet[i]=row.getString(ColumnConstants.FN_PROFILE_ID_PROFILE);
      }
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),"User Management : user import","514","GEN00","9");
    }
    return toRet;
  }

  protected Hashtable getDDBBUsers(HttpServletRequest req)
      throws ClsExceptions {
    Hashtable ret= new Hashtable();
    try {
      Table tabProf=new Table(req, TableConstants.TABLE_PROFILE,
                                      "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
      String sql="select " + ColumnConstants.FN_User_ID_USER +
                 " , " + ColumnConstants.FN_User_ACTIVATED +
                 " , " + ColumnConstants.FN_User_ID_PROFILE +
                 " from " + TableConstants.TABLE_USER;
      Vector vec=tabProf.search(sql);
      for (int i=0;i<vec.size();i++) {
        SIGAGrDDBBObject row=(SIGAGrDDBBObject)vec.elementAt(i);
        String[] values = {row.getString(ColumnConstants.FN_User_ACTIVATED),
          row.getString(ColumnConstants.FN_User_ID_PROFILE)};
        ret.put(row.getString(ColumnConstants.FN_User_ID_USER).toUpperCase(),
                values);
      }
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage(),"User Management : user import","514","GEN00","9");
    }
    return ret;
  }

  protected Vector fillUsers(HttpServletRequest req,HttpSession _ses)
      throws ClsExceptions {
    Vector vec = new Vector();
    try {
      com.atos.utils.LdapUserEJ ldapUser=null;
/*
      ldapUser=new PSSCLdapUser(null,null,req);
      */

      /******** RELEVANTE ********/
      com.atos.utils.Mngjndi jnd = new com.atos.utils.Mngjndi();
      com.atos.utils.LdapUserEJHome home = jnd.lookupHome();
      Object obje = home.create(null,null,req.getHeader("user-agent"),
                                req.getRemoteAddr());
      ldapUser = (com.atos.utils.LdapUserEJ)
                 jnd.narrow(obje, com.atos.utils.LdapUserEJ.class);
      /******** RELEVANTE ********/
      String INITCTX = "";
      String MY_HOST[] = {""};
      String MGR_DN = "";
      String MGR_PW = "";
      String MY_SEARCHBASE = "";

      ReadProperties ldapProperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.LDAP);
//      ReadProperties ldapProperties=new ReadProperties("ldap.properties");
      INITCTX = ldapProperties.returnProperty("LDAP.INITCTX");
      String hosts = ldapProperties.returnProperty("LDAP.MY_HOST");
      StringTokenizer tok=new StringTokenizer(hosts,";");
      int i=0;
      MY_HOST=new String[tok.countTokens()];
      while (tok.hasMoreTokens()) MY_HOST[i++]=tok.nextToken();
      MGR_DN = ldapProperties.returnProperty("LDAP.MGR_DN");
      MGR_PW = ldapProperties.returnProperty("LDAP.MGR_PW");
      MY_SEARCHBASE = ldapProperties.returnProperty("LDAP.MY_SEARCHBASE");

      ldapUser.serLDAPConf(INITCTX,MY_HOST,MGR_DN,MGR_PW,MY_SEARCHBASE);
      Hashtable has=ldapUser.getAllPsscUser(getProfiles(req));
      Hashtable ddbb=getDDBBUsers(req);
      Enumeration e=has.keys();
      while (e.hasMoreElements()) {
        String uid=(String)e.nextElement();
        String prof=(String)has.get(uid);
        Hashtable element=new Hashtable();
        element.put("uid",uid);
        element.put("inldap","Y");
        element.put("profile",prof);
        if (ddbb.containsKey(uid)) {
          String[] values = (String[])ddbb.get(uid);
          element.put("inddbb","Y");
          element.put("status",values[0]);
          ddbb.remove(uid);
        } else {
          element.put("inddbb","N");
          element.put("status","-");
        }
        vec.add(element);
      }
      Enumeration e2=ddbb.keys();
      while (e2.hasMoreElements()) {
        String uid=(String)e2.nextElement();
        Hashtable element=new Hashtable();
        String[] values = (String[])ddbb.get(uid);
        element.put("uid",uid);
        element.put("inldap","N");
        element.put("inddbb","Y");
        element.put("status",values[0]);
        element.put("profile",values[1]);
        vec.add(element);
      }


      boolean changes = true;
      while (changes) {
        changes = false;
        for (int h=0;h<vec.size()-1;h++) {
          Hashtable ele1=(Hashtable)vec.elementAt(h);
          Hashtable ele2=(Hashtable)vec.elementAt(h+1);
          String uno=(String)ele1.get("uid");
          String dos=(String)ele2.get("uid");
          int value = (uno.length()>dos.length()?dos.length():uno.length());
          uno=uno.substring(0,value);
          dos=dos.substring(0,value);
          if (uno.compareTo(dos)>0) {
            vec.set(h,ele2);
            vec.set(h+1,ele1);
            changes = true;
          }
        }
      }


    } catch (ClsExceptions exc) {
      throw exc;
    } catch (Exception el) {
      throw new ClsExceptions(el.getMessage(),"User Management : user import","514","GEN00","9");
    }
    return vec;
  }
}