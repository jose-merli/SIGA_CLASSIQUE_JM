package com.siga.generalRequirements.accessControl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.siga.general.SIGAGeneric;
import java.io.Serializable;

/**
 * <p>Tittle: Propulsion Support System Core</p>
 * <p>Description: This class represents the different Status that use PSSC</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SchlumbergerSema </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

public class SIGAGrDDBBObject extends SIGAGeneric implements Serializable
{
  protected Hashtable htRecord;
  protected Hashtable htRecordBackup;
  protected HttpServletRequest req;


  /**
   * Constructor
   * @roseuid 3DABCDD5003C
   * Identify the different Status
   */
  public SIGAGrDDBBObject()
  {
    this.req = null;
    this.htRecord = null;
  }

  /**
   * Constructor
   * Identify the different Status
   * @param req provide request information for HTTP servlets
   */

  public SIGAGrDDBBObject  (HttpServletRequest req){
    this.req = req;
    this.htRecord = null;
  }

  /**
   * Constructor
   * Identify the different Status
   * @param req provide request information for HTTP servlets
   * @param htRec Hashtable
   */

  public SIGAGrDDBBObject (HttpServletRequest req, Hashtable htRec){
    this.req = req;
    this.htRecord = htRec;
  }
  public Hashtable getData(){
    return this.htRecord;
  }
  public void setData(Hashtable data){
    this.htRecord = data;
  }

  public String getString(String column){
    String ret=null;
    if (this.htRecord==null || (ret=(String)htRecord.get(column))==null)
      return "";
    return ret;
  }

  public void setDataBackup(Hashtable data){
    this.htRecordBackup = data;
  }
  protected String[] loadFields(){
    String[] columns = new String[htRecord.size()];
    int i=0;
    Enumeration e= htRecord.keys();
    while(e.hasMoreElements()){
      columns[i] =e.nextElement().toString();
      i++;
    }
    return columns;
  }

  public boolean update(String tablename, String[] fields, String[] updatableFields) throws ClsExceptions{
      //ClsLogging.writeFileLog("Inside Update method ", req, 3);
      Row updatedRow = new Row();
      int updatedRecords = 0;
      try {
        //ClsLogging.writeFileLog("htrecord:"+this.htRecord+", htrecordbackup:"+this.htRecordBackup, req, 3);
        if ((this.htRecord!=null)&&(this.htRecordBackup!=null)){
          //ClsLogging.writeFileLog("Before de load "+this.htRecord.size()+", "+this.htRecordBackup.size(), req, 3);
        }
        updatedRow.load(this.htRecord);
        if (this.htRecordBackup!=null)
        {
            //ClsLogging.writeFileLog("Before setcomparedata "+this.htRecordBackup.size(), req, 3);
        }
        updatedRow.setCompareData(this.htRecordBackup, req);
        updatedRecords = updatedRow.update(tablename, fields, updatableFields);
        //ClsLogging.writeFileLog("After update", req, 3);
        if (updatedRecords==0)
        {
          Vector v=updatedRow.getRecordsChanged();
          if(v.size()>0)
              {//Has had changes
            ClsLogging.writeFileLog("Hay cambios", req, 3);
            Enumeration en=v.elements();
            ClsLogging.writeFileLog("Elementos cambiados:", req, 3);
            ClsLogging.writeFileLog("*******************", req, 3);
            while (en.hasMoreElements())
            {
              com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
              ClsLogging.writeFileLog("Clave: "+obj.getIdObj(), req, 3);
              ClsLogging.writeFileLog("Valor: "+obj.getValueObj(), req, 3);
              ClsLogging.writeFileLog("*******************", req, 3);
            }
            HttpSession ses = req.getSession();
            ses.setAttribute("vectRecords", v);
          }
        }
        else
        {
          ClsLogging.writeFileLog("No hay cambios", req, 3);
        }
      } catch (ClsExceptions ex) {
        ClsLogging.writeFileLogError("EXCEPCION modificando un registro en la Base de Datos.", req, 3);
        ex.printStackTrace();
        throw ex;
      }
      if (updatedRecords == 0) return false;
      else return true;
  }

  public boolean add(String tablename) throws ClsExceptions{
    Row newRow = new Row();
    int insertedRecords=0;
    try {
      newRow.load(htRecord);
      String[] fields = loadFields();
      insertedRecords = newRow.add(tablename, fields);
    } catch (ClsExceptions ex) {
      if (req!=null){
        ClsLogging.writeFileLog("ERROR inserting a record at " + tablename, req, 2);
      }else{
        ClsLogging.writeFileLogWithoutSession("ERROR inserting a record at " + tablename, 2);
      }
      ex.printStackTrace();
      throw new ClsExceptions(ex.getMessage(), "", "", "", "4");
    }
    if (insertedRecords == 0) return false;
    else return true;
  }
}