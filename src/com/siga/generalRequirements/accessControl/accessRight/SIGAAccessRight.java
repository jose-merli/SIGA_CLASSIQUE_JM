package com.siga.generalRequirements.accessControl.accessRight;

import java.util.*;
import com.atos.utils.*;

import javax.servlet.http.*;
import com.siga.gui.processTree.*;
import com.siga.generalRequirements.accessControl.*;
import javax.transaction.UserTransaction;

public class SIGAAccessRight extends SIGAProcessBase
{
  protected HttpServletRequest req;

  public SIGAAccessRight()  {
  }

  public Vector loadProcess(HttpServletRequest _req, String profile, String institucion, String nivel)
      throws ClsExceptions {
    req=_req;
    Table gtTable=null;
    gtTable = new Table(req, TableConstants.TABLE_ACCESS_RIGHT, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");

    String query_acc="select "+ ColumnConstants.FN_ACCESS_RIGHT_VALUE +
                     " , " + ColumnConstants.FN_ACCESS_RIGHT_PROCESS +
                     " , " + ColumnConstants.FN_ACCESS_RIGHT_PROFILE +
                     " , " + ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION +
                     " from " + TableConstants.TABLE_ACCESS_RIGHT +
                     " where " + ColumnConstants.FN_ACCESS_RIGHT_PROFILE +
                     " = '" + profile +"'"+
                     " and " + ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION +
                     " = " + institucion;

    String query_pro="select " +
                     ColumnConstants.FN_PROCESS_ID +
                     ", " + ColumnConstants.FN_PROCESS_DESC +
                     ", " + ColumnConstants.FN_PROCESS_ID_PARENT + " from " +
                     TableConstants.TABLE_PROCESS + 
                     " where " + ColumnConstants.FN_PROCESS_NIVEL + " >= " + nivel +
                     " ORDER BY " + ColumnConstants.FN_PROCESS_DESC;

    Vector reta=gtTable.searchForUpdate(query_acc);
    Vector ret=gtTable.searchForUpdate(query_pro);

    Hashtable hashaccess= new Hashtable();

// montamos la hash que va a contener a los access
    if (reta!=null) {
      for (int i=0;i<reta.size();i++) {
        SIGAGrDDBBObject obj=(SIGAGrDDBBObject)reta.elementAt(i);
        hashaccess.put(obj.getString(ColumnConstants.FN_ACCESS_RIGHT_PROCESS),obj);
      }
    }

    HttpSession ses = req.getSession();
    ses.setAttribute("vtrbackup",hashaccess);

    Vector hiers=new Vector();
    Vector toRet=new Vector();
    Hashtable has=new Hashtable();
    Hashtable has2=new Hashtable();

    for (int h=0;h<ret.size();h++) {
      SIGAGrDDBBObject obj=(SIGAGrDDBBObject)ret.elementAt(h);
      String idProcess=(String)obj.getString(ColumnConstants.FN_PROCESS_ID);
      SIGAProcessHier hierAct = new SIGAProcessHier(idProcess, (String)obj.getString(ColumnConstants.FN_PROCESS_ID_PARENT));
      hiers.add(hierAct);
      has.put(idProcess,hierAct);
      SIGAAccessObj object=new SIGAAccessObj();
      object.put(SIGABaseNode.NAME,obj.getString(ColumnConstants.FN_PROCESS_DESC));
      object.put(SIGABaseNode.OID,obj.getString(ColumnConstants.FN_PROCESS_ID));
      object.put(SIGABaseNode.PARENT,obj.getString(ColumnConstants.FN_PROCESS_ID_PARENT));
      object.put(SIGABaseNode.PROFILE,profile);
      object.put(SIGABaseNode.INSTITUCION,institucion);
      SIGAGrDDBBObject objAccess=(SIGAGrDDBBObject)hashaccess.get(obj.getString(ColumnConstants.FN_PROCESS_ID));
      if (objAccess!=null) {
        String access=objAccess.getString(ColumnConstants.FN_ACCESS_RIGHT_VALUE);
        if ("1".equals(access)) {
          object.put(SIGABaseNode.ACCESS,SIGABaseNode.ACCESS_DENY);
        } else if ("2".equals(access)) {
          object.put(SIGABaseNode.ACCESS,SIGABaseNode.ACCESS_READ);
        } else if ("3".equals(access)) {
          object.put(SIGABaseNode.ACCESS,SIGABaseNode.ACCESS_FULL);
        } else {
          object.put(SIGABaseNode.ACCESS,SIGABaseNode.ACCESS_NONE);
        }
      } else {
        object.put(SIGABaseNode.ACCESS,SIGABaseNode.ACCESS_NONE);
      }
      object.setOldState((String)object.get(SIGABaseNode.ACCESS));
      object.setActionMove(SIGAPTConstants.MODIFYACCESS);
      has2.put(idProcess,object);
    }

    SIGAProcessHier hier=getHier(has,"",has2);
    toRet.add(hier);
    toRet.add(has2);
    ses.setAttribute("hierbckp",hier);
    return toRet;
  }

  public Vector modify(HttpServletRequest _req,
                       String profile,
                       String institucion,
                       String nivel,
                       String[] changes) throws Exception {
    req=_req;
    Vector vec=new Vector();
    UserTransaction tx =null;
    try {
      HttpSession ses = req.getSession();
      com.atos.utils.UsrBean usrbean = (com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
      if (usrbean==null)
        throw new ClsExceptions("Sesión no válida.");

      tx = usrbean.getTransaction();
      //tx.begin();

      for (int h=0;h<changes.length;h++) {
      	
      	tx.begin();
      	
        StringTokenizer tok=new StringTokenizer(changes[h],"-");
        // cambios orientados a los campos hidden
        String oidTarget=tok.nextToken();
        String oidMoved=tok.nextToken();
        String select ="select "+ColumnConstants.FN_PROCESS_ID+
		  " from "+ TableConstants.TABLE_PROCESS +" where "+
		  ColumnConstants.FN_PROCESS_DESC+" like 'HIDDEN%' and "+
		  ColumnConstants.FN_PROCESS_ID_PARENT+"='"+oidTarget+"'";        
        // fin de cambios orientados a los campos hidden
        modify(_req,oidTarget,oidMoved, profile, institucion, false);

//      cambios orientados a los campos hidden
        Table gtTable=null;
        gtTable = new Table(req, TableConstants.TABLE_PROCESS,
        		"com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
        Vector procesos=gtTable.searchForUpdate(select);
        if (procesos!=null) {
        	for (int v=0;v<procesos.size();v++) {
        		SIGAGrDDBBObject obj=(SIGAGrDDBBObject)procesos.elementAt(v);
        		String processId=obj.getString(ColumnConstants.FN_PROCESS_ID);
        		modify(_req,processId,oidMoved, profile, institucion,true);
        	}
        }
        
        tx.commit();
        
        // fin de cambios orientados a los campos hidden        
      }

      //tx.commit();
      
      String mensaje = "El Usuario \"" + usrbean.getUserDescription() + "\" con ID=\"" + usrbean.getUserName() + "\" ha modificado los Permisos de Acceso del Grupo con ID=\"" + profile + "\"";	

      CLSAdminLog.escribirLogAdmin(_req,mensaje);

      vec.add("OK");
      vec.add(loadProcess(req, profile, institucion, nivel));
    } catch (Exception e) {
      e.printStackTrace();
      try {
        tx.rollback();
      } catch (Exception ee) {}
      
      vec.add("Exception");
      vec.add(e.getMessage());
      return vec;
    }
    return vec;
  }

  public void modify(HttpServletRequest _req, String oidTarget, 
  		String oidMoved, String profile, 
		String institucion, boolean hidden) throws Exception
  {
    req=_req;

    String access="0";
    if (SIGABaseNode.ACCESS_DENY.equals(oidMoved)) {
      access="1";
    } else if (SIGABaseNode.ACCESS_READ.equals(oidMoved)) {
      access="2";
    } else if (SIGABaseNode.ACCESS_FULL.equals(oidMoved)) {
      access="3";
    }

    Vector vec=new Vector();

    /*String selectProcessPater="select a." + ColumnConstants.FN_PROCESS_ID +
                 " from (select " +
                 ColumnConstants.FN_PROCESS_ID_PARENT +" from " + TableConstants.TABLE_PROCESS +
                 " where " +
                 ColumnConstants.FN_PROCESS_ID +"='"+oidTarget+"') l,  "+
                 TableConstants.TABLE_PROCESS + " a  " +
                 " where a."+ColumnConstants.FN_PROCESS_ID+"=l."+
                 ColumnConstants.FN_PROCESS_ID_PARENT;*/
 
/*    String padre="select " + ColumnConstants.FN_ACCESS_RIGHT_VALUE +
                 " from (select " +
                 ColumnConstants.FN_PROCESS_ID_PARENT +" from " + TableConstants.TABLE_PROCESS +
                 " where " +
                 ColumnConstants.FN_PROCESS_ID +"='"+oidTarget+"') l,  "+
                 TableConstants.TABLE_PROCESS + " a, " + TableConstants.TABLE_ACCESS_RIGHT +
                 " b where a."+ColumnConstants.FN_PROCESS_ID+"=l."+
                 ColumnConstants.FN_PROCESS_ID_PARENT +
                 " and a."+ColumnConstants.FN_PROCESS_ID+"=b."+
                 ColumnConstants.FN_ACCESS_RIGHT_PROCESS +
                 " and b."+ ColumnConstants.FN_ACCESS_RIGHT_PROFILE+"='"+profile+"'" +
                 " and b."+ ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION+"="+institucion; */

    String query_pro="select " +
                     ColumnConstants.FN_PROCESS_ID +
                     ", " + ColumnConstants.FN_PROCESS_DESC +
                     ", " + ColumnConstants.FN_PROCESS_ID_PARENT + " from " +
                     TableConstants.TABLE_PROCESS;

    Table gtTable=null;
    gtTable = new Table(req, TableConstants.TABLE_ACCESS_RIGHT, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");

    // primero se testea que el padre tiene permisos superiores, aunque esto se va a hacer en local
    // Existe pade?
    /*Vector pater=gtTable.search(selectProcessPater);
    if (pater!=null && pater.size()>0) {
      SIGAGrDDBBObject pat=(SIGAGrDDBBObject)pater.elementAt(0);
      pater=gtTable.search(padre);
      if (pater== null || pater.size()<1) {
        throw new Exception(SIGAPTConstants.PATER_RESTRIC);
      }

      String paterAccess=((SIGAGrDDBBObject)pater.elementAt(0)).getString("ACCESS_RIGHT");
      if (paterAccess==null || access.compareTo(paterAccess)>0) {
        throw new Exception(SIGAPTConstants.PATER_RESTRIC);
      }
    }*/

    // Luego se testea que no ha cambiado la estructura
    HttpSession ses = req.getSession();
    SIGAProcessHier hier=(SIGAProcessHier)ses.getAttribute("hierbckp");
    Vector processHier=gtTable.searchForUpdate(query_pro);
    Hashtable hiers=new Hashtable();
    Hashtable names=new Hashtable();
    for (int h=0;h<processHier.size();h++) {
      SIGAGrDDBBObject obj=(SIGAGrDDBBObject)processHier.elementAt(h);
      SIGAProcessHier hierAct=new SIGAProcessHier((String)obj.getString(ColumnConstants.FN_PROCESS_ID), (String)obj.getString(ColumnConstants.FN_PROCESS_ID_PARENT));
      hiers.put(hierAct.getId(),hierAct);

      SIGAAccessObj object=new SIGAAccessObj();
      object.put(SIGABaseNode.NAME,obj.getString(ColumnConstants.FN_PROCESS_DESC));
      object.put(SIGABaseNode.OID,obj.getString(ColumnConstants.FN_PROCESS_ID));

      names.put((String)obj.getString(ColumnConstants.FN_PROCESS_ID), object);
    }
    SIGAProcessHier actHier=getHier(hiers,"",names);
    if (!hidden && !actHier.comparePath(hier,oidTarget)) {
      throw new Exception(SIGAPTConstants.STRUC_CHANGED);
    }
    // Y al final se modifica el registro que sus hijos para cambiar los permisos a iguales o inferiores

    Hashtable hashBackup=(Hashtable)ses.getAttribute("vtrbackup");

    Vector ret=updateAccess(oidTarget, profile, institucion, access, ses, hashBackup, true);
    if (ret.size()!=0) {
      throw new Exception(SIGAPTConstants.REG_CHANGED);
    }

    /*Object paramsPL[]={oidTarget,
                         profile,
                         access};
    String namePL = "{call ?:=PKG_SIGA_PROCESS.accessRight(?,?,?)}";
    executePL(namePL,paramsPL);*/
  }

  protected Vector updateAccess(String process, String profile, String institucion,
                                String accessRight,HttpSession ses,
                                Hashtable hashBackup, boolean pater)
      throws ClsExceptions {
    String rec="";
    Vector vec=new Vector();
    Table gtTable=null;
    gtTable = new Table(req, TableConstants.TABLE_ACCESS_RIGHT, "com.siga.generalRequirements.accessControl.SIGAGrDDBBObject");
    gtTable.addFilter(ColumnConstants.FN_ACCESS_RIGHT_PROCESS,process);
    gtTable.addFilter(ColumnConstants.FN_ACCESS_RIGHT_PROFILE,profile);
    gtTable.addFilter(ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION,institucion);
    Vector vecRows=gtTable.search(true);
    Hashtable hasNewUpd=new Hashtable();
    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_PROCESS,process);
    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_PROFILE,profile);
    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION,institucion);
    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_VALUE,accessRight);
    
    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_DATEMOD ,"SYSDATE");
    hasNewUpd.put(ColumnConstants.FN_ACCESS_RIGHT_USERMOD ,((UsrBean)ses.getAttribute("USRBEAN")).getUserName());

    if (vecRows==null || vecRows.size()==0) {
      SIGAGrDDBBObject processRight=new SIGAGrDDBBObject(req);
      processRight.setData(hasNewUpd);
      processRight.add(TableConstants.TABLE_ACCESS_RIGHT);
    } else  {
      if (!pater) {
        SIGAGrDDBBObject obj=(SIGAGrDDBBObject)vecRows.elementAt(0);
        if (accessRight.compareTo(obj.getString(ColumnConstants.FN_ACCESS_RIGHT_VALUE))>=0) {
          return vec;
        }
      }
      SIGAGrDDBBObject hasbk=(SIGAGrDDBBObject)hashBackup.get(process);
      SIGAGrDDBBObject processRight = (SIGAGrDDBBObject)vecRows.elementAt(0);
      processRight.setDataBackup(hasbk.getData());
      processRight.setData(hasNewUpd);

      processRight.update(TableConstants.TABLE_ACCESS_RIGHT, new String[]{ColumnConstants.FN_ACCESS_RIGHT_PROCESS,
          ColumnConstants.FN_ACCESS_RIGHT_PROFILE, ColumnConstants.FN_ACCESS_RIGHT_INSTITUCION} ,
          new String[]{ColumnConstants.FN_ACCESS_RIGHT_VALUE});

      Vector ve = (Vector)ses.getAttribute("vectRecords");
      if (ve!=null){
        ClsLogging.writeFileLog("ve = "+ve,req,3);
        if (ve.size()>0)
        {
          ClsLogging.writeFileLog("ve = "+ve.size(),req,3);
          java.util.Enumeration en=ve.elements();
          while (en.hasMoreElements()){
            com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
            rec=rec+", "+obj.getIdObj();
          }
        }
        vec.add("exception");
        vec.add("modified");
        vec.add(rec);
      }
    }
    return vec;
  }

  public void executePL(String namePL,Object params[]) throws Exception {
    String res=ClsMngBBDD.callPLFunction(namePL,params);
    if(!res.equals("0")){
      throw new Exception(SIGAPTConstants.PL_ERROR + " " + res);
    }
  }
}