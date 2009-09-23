package com.atos.utils;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


/**
 * <p>Title: PSSCLdapUser </p>
 * <p>Description: EJB, Retrieves user information from LDAP and DDBB</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class LdapUserEJBean implements SessionBean {
  private SessionContext sessionContext;

  private String INITCTX = "";
  private String MY_HOST[] = {""};
  private String  MGR_DN = "";
  private String MGR_PW = "";
  private String MY_SEARCHBASE = "";
  final private String CONTENT_TYPE = "text/html";
  private boolean certificado=false;
  private String userAgent =null;
  private String remoteAddr=null;
  PSSCLdapU ldapUser = new PSSCLdapU();
  PSSCddbbU ddbbUser = new PSSCddbbU();

  public void ejbCreate(String _DN, String _userAgent,
                        String _remoteAddr) {
    ldapUser.setDN(_DN);
    certificado=true;
    userAgent=_userAgent;
    remoteAddr=_remoteAddr;
    com.atos.utils.ClsLogging.writeFileLog("EJB create: _DN, _userAgent, _remoteAddr",null,3);

  }
  public void ejbCreate(String _uid,String _ldapPassword, String _userAgent,
                        String _remoteAddr) {
    userAgent=_userAgent;
    remoteAddr=_remoteAddr;
    ldapUser.setUid(_uid);
    ddbbUser.setUserPassword(_uid,_ldapPassword);
    com.atos.utils.ClsLogging.writeFileLog("EJB create: _uid, _ldapPassword _userAgent, _remoteAddr",null,3);
  }

  public void ejbCreate() {
  }
  public void ejbRemove() {
  }
  public void ejbActivate() {
  }
  public void ejbPassivate() {
  }
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  /**************************/


  public void updateLangAndProfile(String _lang,String _profile)
      throws ClsExceptions {
    try {
      ldapUser.updateLangAndProfile(_lang,_profile);
    } catch (Exception e) {
      throw new ClsExceptions(e.getMessage());
    }
  }

  public void serLDAPConf(String _INITCTX ,String _MY_HOST[],String  _MGR_DN ,
                          String _MGR_PW, String _MY_SEARCHBASE) {
    ldapUser.serLDAPConf(_INITCTX ,_MY_HOST,_MGR_DN ,
                         _MGR_PW, _MY_SEARCHBASE);
  }

  public void fillinData()
      throws Exception {
    fillinLdapData();
    fillinDdbbData();
  }

  public void fillinLdapData()
      throws Exception, RemoteException {
    ldapUser.getLDAPUser();
  }

  public void fillinDdbbData()
      throws Exception {
    ddbbUser.getDDBBUser();
  }

  public Hashtable getAllPsscUser(String[] profiles
                                  )
      throws Exception {
    return ldapUser.getAllPsscUser(profiles);
  }

  public UsrBean createUserSession()
      throws Exception {
    fillinData();
    checkParams();
    if (!certificado)
      ddbbUser.comparePssw();
    return userSession();
  }

  protected void checkParams()
      throws Exception {
    boolean profileOk=true;
    boolean langOk=true;
    boolean ddbblang=true;
    boolean ddbbprofile=true;

/*
    Table table=new Table(
        null, TableConstants.SIGA_LANGUAJE,
        "pssc.generalRequirements.accessControl.SIGAGrDDBBObject");

    table.addFilter(ColumnConstants.SIGA_LANG_ID_LANGUAGE,ldapUser.lang.toUpperCase());
    Vector vec=table.search();

    Table table2=new Table(
        null, TableConstants.SIGA_PROFILE,
        "pssc.generalRequirements.accessControl.SIGAGrDDBBObject");

    table2.addFilter(ColumnConstants.SIGA_PROFILE_ID_PROFILE,ldapUser.profile.toUpperCase());
    Vector vec2=table.search();

    if (vec==null || vec.size()<1) {
      langOk=false;
    } else {
      ddbblang=ldapUser.lang.equalsIgnoreCase(ddbbUser.lang);
    }

    if (vec2==null || vec2.size()<1) {
      profileOk=false;
    } else {
      ddbbprofile=ldapUser.profile.equalsIgnoreCase(ddbbUser.profile);
    }

    if (!profileOk || !langOk)
      throw new Exception("User parameter does not exist in DDBB :" +
                          (!profileOk?" Invalid profile ":"") +
                          (!langOk?" Invalid lang":""));
    if (!ddbblang || !ddbbprofile) {
      throw new Exception("User parameter: different value from LDAP and DDBB :" +
                          (!ddbbprofile?" Invalid profile ":"") +
                          (!ddbblang?" Invalid lang":""));
						  
    }
	*/
  }

  protected UsrBean userSession()
      throws Exception {
    UsrBean usrbean=null;
    /*
    UserTransaction tx = null;
    try {
      Context ctx = new InitialContext();
      tx = (UserTransaction) ctx.lookup(
                                        "javax.transaction.UserTransaction");
    }
    catch (NamingException ex) {
      ex.printStackTrace();
      if (userAgent!=null)  {
        ClsLogging.writeFileLogError("Error searching TX: "+ex.toString(),
            userAgent,
            "NO ID - NO SESSION SCOPE ",null,remoteAddr,1);
      } else {
        ClsLogging.writeFileLogError("Error searching TX: "+ex.toString(),null,1);
      }
    }
    
    usrbean= new UsrBean();
    usrbean.setCertificationType(0);
    if (this.certificado) {
      Table table = new Table(
          null, TableConstants.SIGA_Parameters,
          "pssc.generalRequirements.accessControl.SIGAGrDDBBObject");

      String sql = "select " + ColumnConstants.SIGA_Parameters_VALUE +
          " from " + TableConstants.SIGA_Parameters +
          " where " + ColumnConstants.SIGA_Parameters_PARAMETER +
          "='PKI_SIGN' and " + ColumnConstants.SIGA_Parameters_STREAM +
          "='GEN'";

      Vector vec = table.search(sql);
      if (vec == null || vec.size() < 1) {
            usrbean.setCertificationType(2);
      } else {
        SIGAGrDDBBObject obj = (SIGAGrDDBBObject) vec.firstElement();
        if (obj == null) {
          usrbean.setCertificationType(2);
        } else {
          String certType = obj.getString(ColumnConstants.
                                          SIGA_Parameters_VALUE);
          try {
            usrbean.setCertificationType(Integer.parseInt(certType));
          } catch (Exception e) {
            usrbean.setCertificationType(2);
          }
        }
      }
    }
    usrbean.setUserName(ddbbUser.userID.toUpperCase());
    usrbean.setProfile((ldapUser.profile==null?"NOPROFILE":ldapUser.profile.toUpperCase()));
    usrbean.setLanguage((ldapUser.lang==null?"en":ldapUser.lang.toLowerCase()));
    //usrbean.setTransaction(tx);
    usrbean.setPki(this.certificado);
    usrbean.setUserDescription(ddbbUser.userDescription==null?"":ddbbUser.userDescription);
    if (userAgent!=null)  {
      ClsLogging.writeFileLog("Stored in session: USR: "+usrbean.getUserName()+
                                  ", DESCRIPTION: "+usrbean.getUserDescription()+
                                  ", PROFILE: "+usrbean.getProfile()+
                                  ", LANG: "+usrbean.getLanguage()+
                                  " and TRANSACTION: "+(usrbean.getTransaction()==null?"NULL":usrbean.getTransaction().toString()),
                                  userAgent,
                                  "NO ID - NO SESSION SCOPE ",null,remoteAddr,3);
    }
	
	*/
    return usrbean;
  }

// accesores
  public String getLang() {
    return (ldapUser.lang!=null)?ldapUser.lang:"";
  }

  public String getProfile() {
    return (ldapUser.profile!=null)?ldapUser.profile:"";
  }

  public String getUid() {
    return (ddbbUser.userID!=null)?ddbbUser.userID:"";
  }

  public String getCn() {
    return (ldapUser.cn!=null)?ldapUser.cn:"";
  }

  /**************************/
  /* Inner classes */

  protected class PSSCLdapU {
    public String DN = null;
    public String lang = null;
    public String profile = null;
    public String uid = null;
    public String cn = null;

    public void setDN(String _DN) {
      DN=_DN;
    }
    public void setUid(String _Uid) {
      if (_Uid==null)
        uid=_Uid;
      else
        uid=_Uid.toUpperCase();
    }

    public void updateLangAndProfile(String _lang,String _profile)
        throws Exception {
      DirContext ctx = connectToLdap();

      String MY_ATTRS[] = {"preferredLanguage","SIGAProfile"};
      String cadbusq=DN;
      Attributes ar = null;
      BasicAttributes lengu=null;
      BasicAttributes prof=null;
      try{
        ar=ctx.getAttributes(cadbusq, MY_ATTRS);
        if (_lang!=null) {
          lengu = new BasicAttributes("preferredLanguage", _lang);
        }
        if (_profile!=null) {
          prof = new BasicAttributes("SIGAProfile", _profile);
        }
        ctx.modifyAttributes(DN,DirContext.REPLACE_ATTRIBUTE,lengu);
        ctx.modifyAttributes(DN,DirContext.REPLACE_ATTRIBUTE,prof);
      }catch(Exception e) {
        throw new Exception("Update LDAP : " + e.getMessage());
      }
    }

    public void serLDAPConf(String _INITCTX ,String _MY_HOST[],String  _MGR_DN ,
                            String _MGR_PW, String _MY_SEARCHBASE) {
      INITCTX = _INITCTX;
      MY_HOST=new String[_MY_HOST.length];
      for (int i=0;i<_MY_HOST.length;MY_HOST[i]=_MY_HOST[i++]);
      MGR_DN = _MGR_DN;
      MGR_PW = _MGR_PW;
      MY_SEARCHBASE = _MY_SEARCHBASE;
    }

    public DirContext connectToLdap()
        throws Exception {
      int j=0;

      Hashtable env = new Hashtable();
      env.put(Context.INITIAL_CONTEXT_FACTORY, INITCTX);
      env.put(Context.SECURITY_PRINCIPAL,MGR_DN);
      env.put(Context.SECURITY_CREDENTIALS, MGR_PW);

      DirContext ctx = null;
      for (j=0;j<MY_HOST.length;j++) {
        env.put(Context.PROVIDER_URL, MY_HOST[j]);
        try {
          ctx = new InitialDirContext(env);
          break;
          } catch (NamingException ex) { }
      }
      if (j==MY_HOST.length)
        throw new Exception("LDAP Configuration error : No LDAP available");
      return ctx;
    }

    public void getLDAPUser()
        throws Exception {
      String MY_FILTER = "";
      String ldapName = null;
      String ldapUser = null ;
      int j=0;

      ldapName = uid;
      DirContext ctx=connectToLdap();

      if (certificado) {
        // Poseo credenciales entrust
        String MY_ATTRS[] = {"uid","preferredLanguage","SIGAProfile","cn"};
        String cadbusq=DN;
        Attributes ar = null;
        try{
          ar=ctx.getAttributes(cadbusq, MY_ATTRS);
          uid = ((String)ar.get("uid").get(0)).toUpperCase();
          lang=((String)ar.get("preferredLanguage").get(0)).toUpperCase();
          profile=((String)ar.get("SIGAProfile").get(0)).toUpperCase();
          ClsLogging.writeFileLog("User " + DN + "found in LDAP",
                                      userAgent,
                                      "NO ID - NO SESSION SCOPE ",null,remoteAddr,3);
          try {
            cn=(String)ar.get("cn").get(0);
          } catch (Exception exc) {
            // cn puede no ser necesario, pero viene bien para la descripcion de
            // usuario
          }
        }catch(Exception e) {
          throw new Exception("User does not exist in LDAP: No DN");
        }
        ddbbUser.setUserPassword(uid,null);
      } else if ((ldapName!=null) && (ldapName.length()>0)) {
        // si no tengo credenciales de entrust, he de tener usuario y password
        String dn2="";
        uid=ldapName;
        try {
          SearchControls constraints = new SearchControls();
          constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
          MY_FILTER="uid="+ldapName;
          if (userAgent!=null) {
            ClsLogging.writeFileLog("LDAP : Looking for "+ MY_FILTER,
                                        userAgent,
                                        "NO ID - NO SESSION SCOPE ",null,remoteAddr,3);
          }
          NamingEnumeration results2 = ctx.search(MY_SEARCHBASE, MY_FILTER, constraints);
          if (!results2.hasMoreElements()) {
            throw new Exception("User does not exist in LDAP : No uid");
          }
          while ( results2 != null && results2.hasMore() )
          {
            SearchResult sr2 = (SearchResult) results2.next();
            DN = sr2.getName() + ", " +MY_SEARCHBASE;
            Attributes attrs2 = sr2.getAttributes();
            for (NamingEnumeration ne2 = attrs2.getAll(); ne2.hasMoreElements();)
            {
              Attribute attr2 = (Attribute)ne2.next();
              String attrID2 = attr2.getID();
              if (attrID2.equals("preferredLanguage")) {
                lang=((String)attr2.get(0));
                if (lang==null)
                  throw new ClsExceptions("LDAP lang cannot be null",
                                              "EJB authentication","-1","GEN0","0");
                lang=lang.toUpperCase();
              } else if (attrID2.equals("SIGAProfile")) {
                profile=((String)attr2.get(0));
                if (profile==null)
                  throw new ClsExceptions("LDAP profile cannot be null",
                                              "EJB authentication","-1","GEN0","0");

                profile=profile.toUpperCase();
              } else if (attrID2.equals("cn")) {
                cn=(String)attr2.get(0);
              }
            }
          }
        } catch (NamingException ex) {
          throw new Exception("Fatal error reading LDAP : " + ex.getMessage());
        }
      } else {
        throw new Exception("No USER param");
      }
    }

    protected Hashtable getAllPsscUser(String[] profile
                                       )
        throws Exception {
      Hashtable has = new Hashtable();
      DirContext ctx = connectToLdap();
      for (int i=0;i<profile.length;i++) {
        getAllPsscUser(profile[i],ctx,has);
      }
      return has;
    }

    protected Hashtable getAllPsscUser(String profile,
                                       DirContext ctx,
                                       Hashtable ret)
        throws Exception {
      String dn2="";
      String MY_FILTER="";
      try {
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        MY_FILTER="SIGAProfile="+profile;
        if (userAgent!=null) {
          ClsLogging.writeFileLog("LDAP : Looking for "+ MY_FILTER,
                                      userAgent,
                                      "NO ID - NO SESSION SCOPE ",null,remoteAddr,3);
        }
        NamingEnumeration results2 = ctx.search(MY_SEARCHBASE, MY_FILTER, constraints);
        if (!results2.hasMoreElements()) {
          ClsLogging.writeFileLog("LDAP : "+ MY_FILTER +
                                      ": does not found",
                                      userAgent,
                                      "NO ID - NO SESSION SCOPE ",null,remoteAddr,3);
          return ret;
        }
        while (results2 != null && results2.hasMore())
        {
          SearchResult sr2 = (SearchResult) results2.next();
          DN = sr2.getName() + "," +MY_SEARCHBASE;
          Attributes attrs2 = sr2.getAttributes();
          String uid=((String)attrs2.get("uid").get(0)).toUpperCase();
          ret.put(uid,profile);
        }
      } catch (NamingException ex) {
        throw new Exception("Fatal error reading LDAP : " + ex.getMessage());
      }
      return ret;
    }
  }

  protected class PSSCddbbU {
    public String userDescription = null;
    public String password = null;
    public String pswDDBB = null;
    public String userID=null;
    public String active=null;
    public String lang=null;
    public String location=null;
    public String airForce=null;
    public String profile=null;

    public void setUserPassword(String _user, String _password) {
      password = _password;
      if (password!=null) {
        Crypter cryp = new Crypter();
        password = cryp.doIt(password);
      }
      if (_user!=null)
        userID   = _user.toUpperCase();
      else
        userID=null;
    }

    protected void getDDBBUser()
        throws Exception {
	/*
      Table table=new Table(
          null, TableConstants.SIGA_USER,
          "pssc.generalRequirements.accessControl.SIGAGrDDBBObject");

      String sql="select * from " + TableConstants.SIGA_USER + " where UPPER(" +
                 ColumnConstants.SIGA_User_ID_USER + ")='"+userID+"' and " +
                 ColumnConstants.SIGA_User_ACTIVATED + " = 0";
      Vector vec=table.search(sql);
      if (vec==null || vec.size()<1) {
        throw new Exception("Authorization: User parameter does not exist in DDBB :" +
                            " No user");
      }

      SIGAGrDDBBObject obj=(SIGAGrDDBBObject)vec.firstElement();
      if (obj==null)
        throw new Exception("Authorization: User parameter does not exist in DDBB :" +
                            " No user");

      userDescription = obj.getString(ColumnConstants.SIGA_User_DESC_USER);
      pswDDBB = obj.getString(ColumnConstants.SIGA_User_PASSWORD);
      active=obj.getString(ColumnConstants.SIGA_User_ACTIVATED);
      lang=obj.getString(ColumnConstants.SIGA_User_ID_LANGUAGE);
      location=obj.getString(ColumnConstants.SIGA_User_ID_LOCATION);
      airForce=obj.getString(ColumnConstants.SIGA_User_ID_AIRFORCE);
      profile=obj.getString(ColumnConstants.SIGA_User_ID_PROFILE);
	  
	  */
	  
    }

    protected void comparePssw()
        throws Exception {
      if (password==null || pswDDBB==null)
        throw new Exception("Invalid Password");
      if (password.equals(pswDDBB)) {
        if (userAgent!=null) {
          ClsLogging.writeFileLog("PASSWORDs are identical",
                                      userAgent,
                                      "NO ID - NO SESSION SCOPE ",null,remoteAddr,3);
        } else {
          ClsLogging.writeFileLog("PASSWORDs are identical",null,3);
        }
      } else {
        throw new Exception("Authorization: Invalid Password");
      }
    }
  }
  /**************************/
}
