package com.siga.generalRequirements.accessControl.users.form;

import org.apache.struts.action.*;
import java.util.Hashtable;
import com.atos.utils.ColumnConstants;
import com.siga.generalRequirements.accessControl.users.SIGAUser;

/**
 * <p>Title: Propulsion Support System Core</p>
 * <p>Description: Status Set Rules</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

public class SIGAUserForm extends ActionForm {
  private Hashtable htRecord;

//getter and setter methods

  public void putHas(Hashtable has) {
    htRecord=has;
  }

  public int getRowIndex(){
    if (this.htRecord == null) return 0;
    String ret=(String)this.htRecord.get("index");
    if (ret==null) return 0;
    return Integer.parseInt(ret);
  }

  public void setRowIndex(int index)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put("index", String.valueOf(index));
  }

  public String getLdapcn()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(SIGAUser.LDAP_CN));
  }
  public void setLdapcn(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(SIGAUser.LDAP_CN, cn.trim());
  }

  public String getLdapprofile()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(SIGAUser.LDAP_PROFILE));
  }
  public void setLdapprofile(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(SIGAUser.LDAP_PROFILE, cn.trim());
  }

  public String getLdaplang()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(SIGAUser.LDAP_LANG));
  }
  public void setLdaplang(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(SIGAUser.LDAP_LANG, cn.trim());
  }

  public String getLdapuid()
  {
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(SIGAUser.LDAP_UID));
  }
  public void setLdapuid(String cn)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(SIGAUser.LDAP_UID, cn.trim());
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
    this.htRecord.put(ColumnConstants.FN_User_ID_USER, userId.trim());
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
  public void setLanguaje(String language)
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


  public Hashtable getData(){
    return this.htRecord;
  }

  public void setData(Hashtable data){
    this.htRecord = data;
  }
}