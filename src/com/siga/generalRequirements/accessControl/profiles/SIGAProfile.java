package com.siga.generalRequirements.accessControl.profiles;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import com.siga.generalRequirements.accessControl.SIGAGrDDBB;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ColumnConstants;
import com.atos.utils.TableConstants;

public class SIGAProfile extends SIGAGrDDBB {

  public SIGAProfile()
  {
  }

  public SIGAProfile(HttpServletRequest req) {
    super(req);
  }

  public SIGAProfile(HttpServletRequest req, Hashtable htRec) {
    super(req, htRec);
  }


  public String getProfileId() {
    return ((this.htRecord == null) ? "":(String)this.htRecord.get(ColumnConstants.FN_PROFILE_ID_PROFILE));
  }

  public void setProfileId(String profileId) {
    if(this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_PROFILE_ID_PROFILE, profileId.trim());
  }

  public String getProfileDesc() {
    return ((this.htRecord == null) ? "":(String)this.htRecord.get(ColumnConstants.FN_PROFILE_DESC_PROFILE));
  }

  public void setProfileDesc(String profileDesc) {
    if(this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_PROFILE_DESC_PROFILE, profileDesc.trim());
  }

  public String getProfileLevel() {
    return ((this.htRecord == null) ? "":(String)this.htRecord.get(ColumnConstants.FN_PROFILE_PROFILE_LEVEL));
  }

  public void setProfileLevel(String profileLevel) {
    if(this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_PROFILE_PROFILE_LEVEL, profileLevel.trim());
  }

  public Hashtable getData(){
    return this.htRecord;
  }

  public boolean add() throws ClsExceptions {
    return super.add(TableConstants.TABLE_PROFILE);
  }

  public int delete() throws ClsExceptions {
    String[] keyFields = {ColumnConstants.FN_PROFILE_ID_PROFILE};
    return super.delete(TableConstants.TABLE_PROFILE, keyFields);
  }

  public boolean update() throws ClsExceptions {
    String upDatableFields[] = loadFields();
    String[] fields = {ColumnConstants.FN_PROFILE_ID_PROFILE};
    return super.update(TableConstants.TABLE_PROFILE,fields,upDatableFields);
  }
}