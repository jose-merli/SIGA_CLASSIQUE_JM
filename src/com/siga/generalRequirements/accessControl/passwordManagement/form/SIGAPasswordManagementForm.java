package com.siga.generalRequirements.accessControl.passwordManagement.form;

import java.util.Hashtable;
import org.apache.struts.action.ActionForm;
import com.atos.utils.ColumnConstants;

public class SIGAPasswordManagementForm extends ActionForm {

   private Hashtable htRecord;

    public void putHas(Hashtable has) {
        htRecord = has;
    }

    public int getRowIndex() {
        if(htRecord == null)
            return 0;
        String ret = (String)htRecord.get("index");
        if(ret == null)
            return 0;
        else
            return Integer.parseInt(ret);
    }


    public String getUserId() {
        return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_USER));
    }

    public void setUserId(String idUser) {
      if (this.htRecord == null) this.htRecord = new Hashtable();
      this.htRecord.put(ColumnConstants.FN_User_ID_USER, idUser.trim());
    }

    public String getProfileId() {
        return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_User_ID_PROFILE));
    }

    public void setProfileId(String idProfile) {
      if (this.htRecord == null) this.htRecord = new Hashtable();
      this.htRecord.put(ColumnConstants.FN_User_ID_PROFILE, idProfile.trim());
    }


    public String getPassword() {
      return ((this.htRecord == null) ? "":
        (String)this.htRecord.get(ColumnConstants.FN_User_PASSWORD));

    }

    public void setPassword(String oldPassword) {
      if (this.htRecord == null) this.htRecord = new Hashtable();
      this.htRecord.put(ColumnConstants.FN_User_PASSWORD, oldPassword.trim());
    }

    public String getNewPassword()   {
      return "";
    }

    public void setNewPassword(String newPassword) {
      if (this.htRecord == null) this.htRecord = new Hashtable();
      this.htRecord.put(ColumnConstants.FN_User_PASSWORD, newPassword.trim());
    }

    public String getConfPassword() {
      return "";
    }

    public void setConfPassword(String oldPassword)  {
      if (this.htRecord == null) this.htRecord = new Hashtable();
      this.htRecord.put(ColumnConstants.FN_User_PASSWORD, oldPassword.trim());
    }

    public Hashtable getData() {
        return htRecord;
    }

    public void setData(Hashtable data) {
        htRecord = data;
    }
}
