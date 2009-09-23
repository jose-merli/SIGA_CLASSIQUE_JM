package com.siga.generalRequirements.accessControl.users.form;

import java.util.Hashtable;

import org.apache.struts.action.ActionForm;

import com.atos.utils.ColumnConstants;

/**
 * <p>Title: User Management</p>
 * <p>Description: Import, enable and disable user</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

public class SIGAUserMngForm extends ActionForm {
  private Hashtable htRecord;

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
   * Access method for the sIdStatus property.
   * @return   the current value of the sIdStatus property
   */
  public String getLocation(){
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_LOCATION));
  }

  /**
   * Sets the value of the sIdStatus property.
   * @param aSIdStatus the new value of the sIdStatus property
   */

  public void setLocation(String userId)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_LOCATION, userId.trim());
  }

  /**
   * Access method for the sIdStatus property.
   * @return   the current value of the sIdStatus property
   */
  public String getAirForce(){
    return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_AIRFORCE));
  }

  /**
   * Sets the value of the sIdStatus property.
   * @param aSIdStatus the new value of the sIdStatus property
   */

  public void setAirForce(String userId)
  {
    if (this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_AIRFORCE, userId.trim());
  }

  /**
    * Access method for the sIdStatus property.
    * @return   the current value of the sIdStatus property
    */
   public String getStatus(){
     return ((this.htRecord == null) ? "":
             (String)this.htRecord.get(ColumnConstants.FN_User_ACTIVATED));
   }

   /**
    * Sets the value of the sIdStatus property.
    * @param aSIdStatus the new value of the sIdStatus property
    */

   public void setStatus(String userId)
   {
     if (this.htRecord == null) this.htRecord = new Hashtable();
     this.htRecord.put(ColumnConstants.FN_User_ACTIVATED, userId.trim());
  }

  /**
    * Access method for the sIdStatus property.
    * @return   the current value of the sIdStatus property
    */
   public String getInldap(){
     return ((this.htRecord == null) ? "":
             (String)this.htRecord.get("INLDAP"));
   }

   /**
    * Sets the value of the sIdStatus property.
    * @param aSIdStatus the new value of the sIdStatus property
    */

   public void setInldap(String userId)
   {
     if (this.htRecord == null) this.htRecord = new Hashtable();
     this.htRecord.put("INLDAP", userId.trim());
  }


  /**
    * Access method for the sIdStatus property.
    * @return   the current value of the sIdStatus property
    */
   public String getInddbb(){
     return ((this.htRecord == null) ? "":
             (String)this.htRecord.get("INDDBB"));
   }

   /**
    * Sets the value of the sIdStatus property.
    * @param aSIdStatus the new value of the sIdStatus property
    */

   public void setInddbb(String userId)
   {
     if (this.htRecord == null) this.htRecord = new Hashtable();
     this.htRecord.put("INDDBB", userId.trim());
  }


  public Hashtable getData(){
    return this.htRecord;
  }

  public void setData(Hashtable data){
    this.htRecord = data;
  }
}