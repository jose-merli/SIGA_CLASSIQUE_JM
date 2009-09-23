package com.siga.generalRequirements.accessControl.processMngt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Table;
import com.atos.utils.TableConstants;
import com.atos.utils.ColumnConstants;
import com.siga.generalRequirements.accessControl.SIGAGrDDBBObject;
import com.siga.gui.processTree.SIGAProcessHier;
import com.siga.gui.processTree.SIGAProcessObj;
import com.siga.gui.processTree.SIGAPTConstants;
import javax.transaction.UserTransaction;
import com.siga.generalRequirements.accessControl.SIGAProcessBase;

public class SIGAProcessMngt extends SIGAProcessBase
{
  protected HttpServletRequest req;

  public SIGAProcessMngt()  {
  }

  public Vector loadProcess(HttpServletRequest _req)
      throws ClsExceptions {
    req=_req;
    Table gtTable=null;
    gtTable = new Table(req, TableConstants.TABLE_PROCESS,"com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
    Vector vec=gtTable.search();
    SIGAProcessHier hier=null;
    Vector hiers=new Vector();
    Vector toRet=new Vector();
    Hashtable has=new Hashtable();
    Hashtable has2=new Hashtable();

    for (int h=0;h<vec.size();h++) {
      SIGAGrDDBBObject obj=(SIGAGrDDBBObject)vec.elementAt(h);
      String idProcess=(String)obj.getString(ColumnConstants.FN_PROCESS_ID);
      SIGAProcessHier hierAct = new SIGAProcessHier(idProcess,(String)obj.getString(ColumnConstants.FN_PROCESS_ID_PARENT));
      hiers.add(hierAct);
      has.put(idProcess,hierAct);
      SIGAProcessObj object=new SIGAProcessObj();
      object.put(SIGAProcessObj.NAME,obj.getString(ColumnConstants.FN_PROCESS_DESC));
      object.put(SIGAProcessObj.OID,obj.getString(ColumnConstants.FN_PROCESS_ID));
      object.put(SIGAProcessObj.PARENT,obj.getString(ColumnConstants.FN_PROCESS_ID_PARENT));
      object.put(SIGAProcessObj.USERMOD,obj.getString(ColumnConstants.FN_PROCESS_USERMOD));
      object.put(SIGAProcessObj.DATEMOD,obj.getString(ColumnConstants.FN_PROCESS_DATEMOD));
      object.setActionMove(SIGAPTConstants.MOVE_PROCESS);
      has2.put(idProcess,object);
    }
    HttpSession ses = req.getSession();
    ses.setAttribute("htrbackup",has2);
    hier=getHier(has,"",has2);
    ses.setAttribute("hierbckp",hier);
    toRet.add(hier);
    toRet.add(has2);
    return toRet;
  }

  public Vector moveProcess(String[] oids,HttpServletRequest _req) {
    req=_req;
    Vector vec=new Vector();
    UserTransaction tx =null;
    try {
      HttpSession ses = req.getSession();
      com.atos.utils.UsrBean usrbean = (com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
      if (usrbean==null)
        throw new ClsExceptions("usrbean is null");

      tx = usrbean.getTransaction();
      tx.begin();
      for (int h=0;h<oids.length;h++) {
        StringTokenizer tok=new StringTokenizer(oids[h],"-");
        moveProcess(tok.nextToken(), tok.nextToken(),_req);
      }
      tx.commit();
      vec.add("OK");
      vec.add(this.loadProcess(req));
    } catch (Exception e) {
      e.printStackTrace();
      if (tx!=null)
        try {
        tx.rollback();
        } catch (Exception es) {}
        vec.add("Exception");
        vec.add(e.getMessage());
        return vec;
    }
    return vec;
  }

  public void moveProcess(String oidMoved, String oidTarget,HttpServletRequest _req)
      throws Exception {
    req=_req;
    String rec="";
    HttpSession ses=req.getSession();

    // hay que comprobar la consistencia de las ramas que se quieren modificar
    SIGAProcessHier hier=(SIGAProcessHier)ses.getAttribute("hierbckp");
    String query_pro="select " +
                     ColumnConstants.FN_PROCESS_ID +
                     ", " + ColumnConstants.FN_PROCESS_DESC +
                     ", " + ColumnConstants.FN_PROCESS_ID_PARENT + " from " +
                     TableConstants.TABLE_PROCESS;

    Table gtTable=null;
    gtTable = new Table(req, TableConstants.TABLE_PROCESS, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
    Vector processHier=gtTable.search(query_pro);
    Hashtable hiers=new Hashtable();
    for (int h=0;h<processHier.size();h++) {
      SIGAGrDDBBObject obj=(SIGAGrDDBBObject)processHier.elementAt(h);
      SIGAProcessHier hierAct=new SIGAProcessHier((String)obj.getString(ColumnConstants.FN_PROCESS_ID),(String)obj.getString(ColumnConstants.FN_PROCESS_ID_PARENT));
      hiers.put(hierAct.getId(),hierAct);
    }
    SIGAProcessHier actHier=getHier(hiers,"",null);
    
    if (!actHier.comparePath(hier,oidTarget)) {
      throw new Exception(SIGAPTConstants.STRUC_CHANGED);
    }

    if (!actHier.comparePath(hier,oidMoved)) {
      throw new Exception(SIGAPTConstants.STRUC_CHANGED);
    }

    // Hay que modificar la lista de accesos
    Hashtable hash2= (Hashtable)ses.getAttribute("htrbackup");

    com.atos.utils.UsrBean usrbean = (com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
    if (usrbean==null)
      throw new ClsExceptions("usrbean is null");

    String access= usrbean.getAccessType();
    req.setAttribute("accessType",access);
    String user = usrbean.getUserName();

    gtTable = new Table(req, TableConstants.TABLE_PROCESS,"com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
    gtTable.addFilter(ColumnConstants.FN_PROCESS_ID,oidMoved);
    Vector vecRows=gtTable.search();
    if (vecRows.size()==0) {
      throw new Exception(SIGAPTConstants.REG_DELETED);
    }
    Hashtable hasbk=(Hashtable)hash2.get(oidMoved);
    Hashtable hasbkUpd=new Hashtable();
    Hashtable hasNewUpd=new Hashtable();
    hasbkUpd.put(ColumnConstants.FN_PROCESS_ID,hasbk.get(SIGAProcessObj.OID));
    hasbkUpd.put(ColumnConstants.FN_PROCESS_ID_PARENT,hasbk.get(SIGAProcessObj.PARENT));
    hasbkUpd.put(ColumnConstants.FN_PROCESS_USERMOD,hasbk.get(SIGAProcessObj.USERMOD));
    hasbkUpd.put(ColumnConstants.FN_PROCESS_DATEMOD,hasbk.get(SIGAProcessObj.DATEMOD));

    hasNewUpd.put(ColumnConstants.FN_PROCESS_ID,oidMoved);
    hasNewUpd.put(ColumnConstants.FN_PROCESS_ID_PARENT,oidTarget);
    hasNewUpd.put(ColumnConstants.FN_PROCESS_USERMOD,user);
    hasNewUpd.put(ColumnConstants.FN_PROCESS_DATEMOD,"SYSDATE");

    SIGAGrDDBBObject process = (SIGAGrDDBBObject)vecRows.elementAt(0);
    process.setDataBackup(hasbkUpd);
    process.setData(hasNewUpd);


    process.update(TableConstants.TABLE_PROCESS,
                   new String[]{ColumnConstants.FN_PROCESS_ID} ,
                   new String[]{ColumnConstants.FN_PROCESS_ID_PARENT,
        ColumnConstants.FN_PROCESS_USERMOD, ColumnConstants.FN_PROCESS_DATEMOD});

                   Vector ve = (Vector)ses.getAttribute("vectRecords");

                   ClsLogging.writeFileLog("ve = "+ve,req,3);
                   if (ve!=null && ve.size()>0)
                   {
                     ClsLogging.writeFileLog("ve = "+ve.size(),req,3);
                     java.util.Enumeration en=ve.elements();
                     while (en.hasMoreElements()){
                       com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
                       rec=rec+", "+obj.getIdObj();
                     }
                     throw new Exception(SIGAPTConstants.REG_CHANGED);
                   }
  }
}