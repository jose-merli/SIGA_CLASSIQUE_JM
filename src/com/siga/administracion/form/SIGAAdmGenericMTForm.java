package com.siga.administracion.form;

import java.util.Hashtable;
import com.atos.utils.ColumnConstants;
import com.atos.utils.ClsConstants;
import com.siga.administracion.SIGAMasterTable;

import org.apache.struts.action.ActionForm;

/**
 * <p>Title: Propulsion Support System Core</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

public class SIGAAdmGenericMTForm extends ActionForm {
  private Hashtable table = null;
  private Hashtable reg = null;
  private String sLanguage;

  public String getTableName() {
    return ((table == null) ? "" : (String)table.get(SIGAMasterTable.FN_ID_MASTER_TABLE));
  }

  public void setTableName(String name) {
    if (table == null)
      table = new Hashtable();
    table.put(SIGAMasterTable.FN_ID_MASTER_TABLE, name);
  }
  
  public String getTableDescription() {
  	return ((table == null) ? "" : (String)table.get(SIGAMasterTable.FN_DESC_TABLE_ALIAS));
  }

  public void setTableDescription(String desc) {
	if (table == null)
	  table = new Hashtable();
	table.put(SIGAMasterTable.FN_DESC_TABLE_ALIAS, desc);
  }

  public String getTipoCodigo() {
  	return ((table == null) ? "" : (String)table.get(SIGAMasterTable.FN_TIPO_CODIGO));
  }

  public void setTipoCodigo(String desc) {
	if (table == null)
	  table = new Hashtable();
	table.put(SIGAMasterTable.FN_TIPO_CODIGO, desc);
  }

  public boolean getLogicDelete() {
    boolean b = false;
    if (table != null)
      b = table.get(SIGAMasterTable.FN_FLAG_LOGIC_DELETE).toString().equals(ClsConstants.DB_TRUE);
    return b;
  }

  public void setLogicDelete(boolean flag) {
    if (table == null)
      table = new Hashtable();
    table.put(SIGAMasterTable.FN_FLAG_LOGIC_DELETE, (flag) ? ClsConstants.DB_TRUE : ClsConstants.DB_FALSE);
  }

  public boolean getUsesLang() {
    boolean b = false;
    if (table != null)
      b = table.get(SIGAMasterTable.FN_FLAG_USES_LANG).toString().equals(ClsConstants.DB_TRUE);
    return b;
  }

  public void setUsesLang(boolean flag) {
    if (table == null)
      table = new Hashtable();
    table.put(SIGAMasterTable.FN_FLAG_USES_LANG, (flag) ? ClsConstants.DB_TRUE : ClsConstants.DB_FALSE);
  }

  public String getCode() {
	return ((reg == null) ? "" : String.valueOf(reg.get(SIGAMasterTable.ALIAS_CODE_FIELD)));
  }

  public void setCode(String code) {
    if (reg == null)
      reg = new Hashtable();
    reg.put(SIGAMasterTable.ALIAS_CODE_FIELD, code);
  }

  public String getLanguageCode() {
    String lang = "";
    if (getUsesLang())
      lang = ((reg == null) ? "" : (String)reg.get(ColumnConstants.FN_LANG_ID_LANGUAGE));
    return lang;
  }

  public void setLanguageCode(String code) {
    if (reg == null)
      reg = new Hashtable();
    reg.put(ColumnConstants.FN_LANG_ID_LANGUAGE, code);
  }

  public String getDescription() {
    return ((reg == null) ? "" : (String)reg.get(SIGAMasterTable.ALIAS_DESC_FIELD));
  }

  public void setDescription(String desc) {
    if (reg == null)
      reg = new Hashtable();
	reg.put(SIGAMasterTable.ALIAS_DESC_FIELD, desc);
  }

  public boolean getDeleted() {
    boolean b = false;
    if (reg != null && reg.get(ColumnConstants.FIELD_DELETED) != null)
      b = reg.get(ColumnConstants.FIELD_DELETED).toString().equals(ClsConstants.DB_TRUE);
    return b;
  }

  public void setDeleted(boolean flag) {
    if (reg == null)
      reg = new Hashtable();
    reg.put(ColumnConstants.FIELD_DELETED, (flag) ? ClsConstants.DB_TRUE : ClsConstants.DB_FALSE);
  }

  public Hashtable getData(){
	if (reg == null) reg = new Hashtable();
	return this.reg;
  }

  public void setData(Hashtable data){
	if (reg == null) reg = new Hashtable();
	this.reg = data;
  }


  public void loadTableData(Hashtable data) {
    this.table = data;
  }

  public void loadRegisterData(Hashtable data) {
    this.reg = data;
  }
  
  public String getsLanguage(){
  	return this.sLanguage;
  }

  public void setsLanguage(String aLanguage){
    this.sLanguage = aLanguage;
  }

  /**
   * Returns a Hastable with column names as Keys
   * @return Hashtable
   */
  public Hashtable getAllregisterData () {
    Hashtable register = new Hashtable();
    register.put(table.get(SIGAMasterTable.FN_ID_CODE_FIELD), getCode());
    if (reg.get(SIGAMasterTable.ALIAS_DESC_FIELD) != null)
      register.put(table.get(SIGAMasterTable.FN_ID_DESCRIPTION_FIELD), getDescription());
    if (getUsesLang())
      register.put(ColumnConstants.FN_LANG_ID_LANGUAGE, getLanguageCode());

    if (getLogicDelete())
      register.put(ColumnConstants.FIELD_DELETED, (getDeleted()) ? ClsConstants.DB_TRUE : ClsConstants.DB_FALSE);

    return register;
  }

  public SIGAAdmGenericMTForm() {
  }
}