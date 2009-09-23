package com.siga.generalRequirements.accessControl.profiles.form;

import java.util.Hashtable;
import org.apache.struts.action.ActionForm;
import com.atos.utils.ColumnConstants;

public class SIGAProfileForm extends ActionForm {
   private Hashtable htRecord;


    public void putHas(Hashtable has) {
        htRecord = has;
    }

    public int getRowIndex(){
        if(htRecord == null)
            return 0;
        String ret = (String)htRecord.get("index");
        if(ret == null)
            return 0;
        else
            return Integer.parseInt(ret);
    }

    public String getProfileId() {
        return ((this.htRecord == null) ? "":
            (String)this.htRecord.get(ColumnConstants.FN_PROFILE_ID_PROFILE));
    }

    public void setProfileId(String idProfile) {
      if (this.htRecord == null) this.htRecord = new Hashtable();
      this.htRecord.put(ColumnConstants.FN_PROFILE_ID_PROFILE, idProfile.trim());
    }

    public String getProfileDesc() {
        return ((this.htRecord == null) ? "": (String)this.htRecord.get(ColumnConstants.FN_PROFILE_DESC_PROFILE));
    }

    public void setProfileDesc(String descProfile) {
        if(this.htRecord == null) this.htRecord = new Hashtable();
        this.htRecord.put(ColumnConstants.FN_PROFILE_DESC_PROFILE, descProfile.trim());
    }

    public String getProfileLevel() {
      return ((this.htRecord == null) ? "": (String)this.htRecord.get(ColumnConstants.FN_PROFILE_PROFILE_LEVEL));
    }

    public void setProfileLevel(String profileLevel) {
        if(this.htRecord == null) this.htRecord = new Hashtable();
        this.htRecord.put(ColumnConstants.FN_PROFILE_PROFILE_LEVEL, profileLevel.trim());
    }

    public Hashtable getData() {
        return htRecord;
    }

    public void setData(Hashtable data) {
        htRecord = data;
    }

}
