/**
 * <p>Title: ClsExceptions </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public class ClsExceptions extends ClsExcBase {

  private String param="NO PARAM";
  private String mesg="NO MESSAGE";
  private String process="NO PROCESS";
  private String errorCategory="NO ERROR CATEGORY";
  private String errortype="NO ERROR TYPE";

  public ClsExceptions(Exception e, String msg, String param,
                           String process, String errorCategory, String errortype) {
    super (msg);
    if (param!=null) this.param = param;
    if (msg!=null) this.mesg = msg;
    if (process!=null) this.process=process;
    if (errorCategory!=null) this.errorCategory=errorCategory;
    if (errortype!=null) this.errortype=errortype;
    next=e;
  }


  public ClsExceptions(String msg, String param, String process, String errorCategory, String errortype)
  {
    super (msg);
    if (param!=null) this.param = param;
    if (msg!=null) this.mesg = msg;
    if (process!=null) this.process=process;
    if (errorCategory!=null) this.errorCategory=errorCategory;
    if (errortype!=null) this.errortype=errortype;
  }

  /**
   * Initialize the excetion parameters with default values
   * @param msg String
   */
  public ClsExceptions(String msg) {
    super (msg);
    this.param = "";
    this.mesg = msg;
    this.process="0";
    this.errorCategory="GEN00";
    this.errortype="0";
  }

  public ClsExceptions(Exception e, String msg) {
    super (msg);
    this.param = "";
    this.mesg = msg;
    this.process="0";
    this.errorCategory="GEN00";
    this.errortype="0";
    next=e;
  }


  public String getParam() {
    return param;
  }

  public String getProcess() {
    return process;
  }

  public String getErrorCategory() {
    return errorCategory;
  }

  public String getErrorType() {
    return errortype;
  }

  public String getMsg() {
  	if (mesg==null || mesg.length()==0)
  		return this.getMessage();
    return mesg;
  }

  public void setParam(String s) {
    param = s;
  }

  public void setProcess(String s) {
    process = s;
  }

  public void setErrorCategory(String s) {
    errorCategory = s;
  }

  public void setErrorType(String s) {
    errortype = s;
  }

  public void setMsg(String s) {
    mesg = s;
  }
}