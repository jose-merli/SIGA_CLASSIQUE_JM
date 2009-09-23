package com.siga.generalRequirements.accessControl.passwordManagement;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import com.siga.generalRequirements.accessControl.SIGAGrDDBB;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ColumnConstants;
import com.atos.utils.TableConstants;

public class SIGAPasswordManagement extends SIGAGrDDBB {

  public SIGAPasswordManagement()
  {
  }

  public SIGAPasswordManagement(HttpServletRequest req) {
    super(req);
  }

  public SIGAPasswordManagement(HttpServletRequest req, Hashtable htRec) {
    super(req, htRec);
  }


  public String getUserId() {
    return ((this.htRecord == null) ? "":(String)this.htRecord.get(ColumnConstants.FN_User_ID_USER));
  }

  public void setUserId(String userId) {
    if(this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_USER, userId.trim());
  }

  public String getProfileId() {
    return ((this.htRecord == null) ? "":(String)this.htRecord.get(ColumnConstants.FN_User_ID_PROFILE));
  }

  public void setProfileId(String profileId) {
    if(this.htRecord == null) this.htRecord = new Hashtable();
    this.htRecord.put(ColumnConstants.FN_User_ID_PROFILE, profileId.trim());
  }

  public Hashtable getData() {
    return this.htRecord;
  }

  public boolean add() throws ClsExceptions {
    return super.add(TableConstants.TABLE_USER);
  }

  public int delete() throws ClsExceptions {
    String[] keyFields = {ColumnConstants.FN_User_ID_USER};
    return super.delete(TableConstants.TABLE_USER, keyFields);
  }

  public boolean update() throws ClsExceptions {
    String upDatableFields[] = loadFields();
    String[] fields = {ColumnConstants.FN_User_ID_USER};
    return super.update(TableConstants.TABLE_USER,fields,upDatableFields);
  }
}