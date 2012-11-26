package com.siga.generalRequirements.accessControl.users;

import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

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

public class SIGAUser extends SIGAGrDDBB
{
  static public final int DDBB=1;
  static public final int LDAP=2;
  static public final String LDAP_CN="LDAP_CN";
  static public final String LDAP_PROFILE="LDAP_PROFILE";
  static public final String LDAP_LANG="LDAP_LANG";
  static public final String LDAP_UID="LDAP_UID";

  com.atos.utils.LdapUserEJ ldapUser=null;
  /**
   * Constructor
   * @roseuid 3DABCDD5003C
   * Identify the different Status
   */
  public SIGAUser()
  {
    super();
  }

  /**
   * Constructor
   * Identify the different Status
   * @param req provide request information for HTTP servlets
   */

  public SIGAUser (HttpServletRequest req){
    super(req);
  }

  /**
   * Constructor
   * Identify the different Status
   * @param req provide request information for HTTP servlets
   * @param htRec Hashtable
   */

  public SIGAUser (HttpServletRequest req, Hashtable htRec){
    super(req,htRec);
  }

  /**
   * Access method for the sIdStatus property.
   * @return   the current value of the sIdStatus property
   */
  public String getUserId(){
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_USER));
  }

  /**
   * Sets the value of the sIdStatus property.
   * @param aSIdStatus the new value of the sIdStatus property
   */

  public void setUserId(String userId)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_USER, userId.trim().toUpperCase());
  }

  /**
   * Access method for the sNameStatus property.
   * @return   the current value of the sNameStatus property
   */
  public String getUserDesc()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_DESC_USER));
  }

  /**
   * Sets the value of the sNameStatus property.
   * @param aSNameStatus the new value of the sNameStatus property
   */
  public void setUserDesc(String userDesc)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_DESC_USER, userDesc.trim());
  }

  /**
   * Access method for the sDescStatus property.
   * @return   the current value of the sDescStatus property
   */
  public String getPassword()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_PASSWORD));
  }

  /**
   * Sets the value of the sDescStatus property.
   * @param aSDescStatus the new value of the sDescStatus property
   */
  public void setPassword(String password)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_PASSWORD, password.trim());
  }

  /**
   * Access method for the sVisualizationType property.
   * @return   the current value of the sVisualizationType property
   */
  public String getLastModification()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_DATE_LAST_MODIFICATION));
  }

  /**
   * Sets the value of the sVisualizationType property.
   * @param aSVisualizationType the new value of the sVisualizationType property
   */
  public void setLastModification(String lastModification)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_DATE_LAST_MODIFICATION,
                      lastModification.trim());
  }

  /**
   * Access method for the sAllowInstallation property.
   * @return   the current value of the sAllowInstallation property
   */
  public String getLanguaje()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_LANGUAGE));
  }

  /**
   * Sets the value of the sAllowInstallation property.
   * @param aSAllowInstallation the new value of the sAllowInstallation property
   */
  public void setLanguage(String language)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_LANGUAGE,
                      language.trim());
  }

  /**
   * Access method for the sAllowOperation property.
   * @return   the current value of the sAllowOperation property
   */
  public String getProfile()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_PROFILE));
  }

  /**
   * Sets the value of the sAllowOperation property.
   * @param aSAllowOperation the new value of the sAllowOperation property
   */
  public void setProfile(String profile)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_PROFILE, profile.trim());
  }

  /**
   * Access method for the sAllowOperation property.
   * @return   the current value of the sAllowOperation property
   */
  public String getAirForce()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_AIRFORCE));
  }

  /**
   * Sets the value of the sAllowOperation property.
   * @param aSAllowOperation the new value of the sAllowOperation property
   */
  public void setAirForce(String airForce)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_AIRFORCE, airForce.trim());
  }

  /**
   * Access method for the sAllowOperation property.
   * @return   the current value of the sAllowOperation property
   */
  public String getLocation()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_LOCATION));
  }

  /**
   * Sets the value of the sAllowOperation property.
   * @param aSAllowOperation the new value of the sAllowOperation property
   */
  public void setLocation(String location)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_LOCATION, location.trim());
  }

/* fin de accesores DDBB */

/* accesores LDAP */

  public String getLDAPcn()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(LDAP_CN));
  }
  public void setLDAPcn(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(LDAP_CN, cn.trim());
  }

  public String getLDAPprofile()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(LDAP_PROFILE));
  }
  public void setLDAPprofile(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(LDAP_PROFILE, cn.trim());
  }

  public String getLDAPlang()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(LDAP_LANG));
  }
  public void setLDAPlang(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(LDAP_LANG, cn.trim());
  }

  public String getLDAPuid()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(LDAP_UID));
  }
  public void setLDAPuid(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(LDAP_UID, cn.trim());
  }

  public void setData(Hashtable data){
    boolean exist=true;
    try {
      if (data.get(LDAP_CN)==null || ((String)data.get(LDAP_CN)).equals("")) {
        if (ldapUser==null) {
/*

          ldapUser=new PSSCLdapUser((String)data.get(ColumnConstants.FN_User_ID_USER),
                                    null,req);
*/

/******** RELEVANTE ********/
          com.atos.utils.Mngjndi jnd = new com.atos.utils.Mngjndi();
          com.atos.utils.LdapUserEJHome home = jnd.lookupHome();
Object obje = home.create((String)data.get(ColumnConstants.FN_User_ID_USER),
                          null,req.getHeader("user-agent"),
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
//ReadProperties ldapProperties=new ReadProperties("ldap.properties");
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
ldapUser.fillinData();
        }
      } else {
        exist=false;
      }
    } catch (Exception e) {
      exist=false;
    }
    super.setData(data);
    try{
      if (exist) {
        setLDAPcn("CN");
        setLDAPlang(ldapUser.getLang());
        setLDAPprofile(ldapUser.getProfile());
        setLDAPuid(ldapUser.getUid());
      }
      }catch(Exception e)
      {
        /*********************/
        e.printStackTrace();
        /*********************/
      }
  }

  public boolean add() throws ClsExceptions {
    return super.add(TableConstants.TABLE_USER);
  }
  public int delete() throws ClsExceptions {
    String[] keyFields = {ColumnConstants.FN_User_ID_USER};
    return super.delete(TableConstants.TABLE_USER, keyFields);
  }
  public boolean update() throws ClsExceptions {

    String[] upDatableFields = loadFields(DDBB);
    String[] fields = {ColumnConstants.FN_User_ID_USER};
    boolean ret=super.update(TableConstants.TABLE_USER,fields,upDatableFields);
    try {

    /******** RELEVANTE ********/
      com.atos.utils.Mngjndi jnd = new com.atos.utils.Mngjndi();
      com.atos.utils.LdapUserEJHome home = jnd.lookupHome();

    Object obje = home.create(getUserId(),null,req.getHeader("user-agent"),
                              req.getRemoteAddr());
    com.atos.utils.LdapUserEJ ldapUser = (com.atos.utils.LdapUserEJ)
        jnd.narrow(obje, com.atos.utils.LdapUserEJ.class);
    /******** RELEVANTE ********/
    }catch(Exception e)
    {
      throw new com.atos.utils.ClsExceptions(e.getMessage());
    }

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

    try {
      ldapUser.serLDAPConf(INITCTX,MY_HOST,MGR_DN,MGR_PW,MY_SEARCHBASE);
    }
    catch (RemoteException ex) {
      throw new com.atos.utils.ClsExceptions(ex.getMessage());
    }
    try {
      ldapUser.fillinData();
    } catch (ClsExceptions e) {
      throw e;
    } catch (Exception ex) {
      throw new ClsExceptions(ex.getMessage());
    }
    setLDAPlang(getLanguaje());
    setLDAPprofile(getProfile());
    try {
      ldapUser.updateLangAndProfile(getLanguaje(), getProfile());
    }
    catch (RemoteException ex) {
      throw new com.atos.utils.ClsExceptions(ex.getMessage());
    }catch (ClsExceptions ex) {
      throw ex;
    }
    return ret;
  }

  public String[] loadFields(int cual){
    // hay que quitar todo lo de LDAP
    if (cual==DDBB) {
      Hashtable hash=new Hashtable();
      Enumeration e1=htRecord.keys();
      while (e1.hasMoreElements()) {
        String value=(String)e1.nextElement();
        if (value.equals(LDAP_CN) || value.equals(LDAP_PROFILE) ||
            value.equals(LDAP_LANG) || value.equals(LDAP_UID) ||
            value.equals("index"))
          continue;

        hash.put(value,htRecord.get(value));
      }
      String[] columns = new String[hash.size()];
      int i=0;
      Enumeration e= hash.keys();
      while(e.hasMoreElements()){
        columns[i] =e.nextElement().toString();
        i++;
      }
      return columns;
    } else if (cual==LDAP) {
      return new String[]{LDAP_PROFILE,LDAP_LANG,LDAP_UID};
    } else {
      return loadFields();
    }
  }

  public void setDataBackup(Hashtable data){
    Hashtable hash=new Hashtable();
    Enumeration e1=data.keys();
    while (e1.hasMoreElements()) {
      String value=(String)e1.nextElement();
      if (value.equals(LDAP_CN) || value.equals(LDAP_PROFILE) ||
          value.equals(LDAP_LANG) || value.equals(LDAP_UID) ||
          value.equals("index"))
        continue;

      hash.put(value,data.get(value));
    }
    htRecordBackup=hash;
  }


}