
package com.siga.administracion;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;

import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.administracion.form.SIGAAdmGenericMTForm;

import javax.transaction.SystemException;

/**
 * <p>Title: Propulsion Support System Core</p>
 * <p>Description: General tables Maintenance</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Schlumberger Sema
 * @version 1.0
 */

public class SIGAAdmGenericMasterTables extends Action {


//  private SIGAMasterTable manager = null;

  /**
   * Administration Generic Master Tables
   */

  public SIGAAdmGenericMasterTables() {
  }

  /**
   * @param mapping ActionMapping
   * @param form ActionForm
   * @param request provide request information for HTTP servlets
   * @param response provide HTTP-specific functionality in sending a response
   * @return ActionForward the operation to carry out
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	Hashtable table = null;
	String forward = "";
	Vector v = new Vector ();
	Hashtable ht = new Hashtable();
	String user="";

	try {
	  try{
//Obtain connection and user transaction
		HttpSession ses= request.getSession();
		UsrBean usrbean = (UsrBean)ses.getAttribute("USRBEAN");
		UserTransaction tx = usrbean.getTransaction();

//Obtain acces type
		if (usrbean != null){
		  String access = usrbean.getAccessType();
		  request.setAttribute("accessType",access);
		  request.setAttribute("language",usrbean.getLanguage());
		  user = usrbean.getUserName();
		}

		String tabName = request.getParameter("tableName");
        SIGAMasterTable manager = new SIGAMasterTable();
        manager.setUsrbean(usrbean);
		table = loadMasterTable(tabName,request, manager);

		ClsLogging.writeFileLog("MODE "+request.getParameter("mode"),request,10);
		ClsLogging.writeFileLog("tableName "+request.getParameter("tableName"),request,10);

// Obtain the max length of code and description to generic form
		String maxCodeLength = manager.getCodeLength();
		request.setAttribute("codeLength",maxCodeLength);
		String maxDescLength = manager.getDescLength();
		request.setAttribute("descLength",maxDescLength);
		String tipoCodigo = manager.getTipoCodigo();
		request.setAttribute("tipoCodigo", tipoCodigo);
		String strutsKey = manager.getStrutsKey();
		request.setAttribute("sKey",strutsKey);
		
//Set the table name to use in insert, delete, update operation forms
		request.setAttribute("table",tabName);
		if (table == null) {
		  forward = "exception";
		} else
		{
		  boolean loadLanguages = false;
		  if (form == null){
			form = new SIGAAdmGenericMTForm();
		  }
		  ((SIGAAdmGenericMTForm) form).loadTableData(table);

// the "mode" flag, drives the action!

// mode = query   Shows a record data
		  if (request.getParameter("mode").equals("query")) {
			forward=listAll(request, (SIGAAdmGenericMTForm)form, manager);
			request.setAttribute("mode", "query");
		  }

// mode = listing  List all  defined possible change status of an item
		  if (request.getParameter("mode").equals("listing")) {
			forward = listAll(request, (SIGAAdmGenericMTForm)form, manager);
		  }
// mode = new Shows the form to insert a new record
		  if (request.getParameter("mode").equals("new")) {
			request.setAttribute("mode", "insert");
			((SIGAAdmGenericMTForm)form).setsLanguage(usrbean.getLanguage());
			forward = "openReg";
			loadLanguages = ((SIGAAdmGenericMTForm)form).getUsesLang();
		  }
// mode = insert It is called when the user press button "ADD" to insert a new record
		  if (request.getParameter("mode").equals("insert")) {
			try{
			  tx.begin();
			  forward = insert(request, (SIGAAdmGenericMTForm)form, manager);
			  if (forward.equals("insertedOk")) {
				request.setAttribute("descOperation","messages.inserted.success");
				if (((SIGAAdmGenericMTForm)form).getUsesLang()) { // masterDetail!

				  if (updateMasterRecord(request,table, (SIGAAdmGenericMTForm)form).equals("updatedOk")) {
					tx.commit();
					request.setAttribute("mode", "update");
				  } else {
					tx.rollback();
					request.setAttribute("mode", "insert");
					forward = "openReg";
				  }
				} else { //no master-detail
				  tx.commit();
				  request.setAttribute("mode", "update");
				}
			  } else {
				tx.rollback();
				request.setAttribute("mode", "insert");
				forward = "openReg";
			  }
			  loadLanguages = ((SIGAAdmGenericMTForm)form).getUsesLang();
			}catch(ClsExceptions ex){
			  tx.rollback();
			  ex.setProcess("0");
			  throw ex;
			} catch (Exception ex){
			  throw new ClsExceptions (ex,ex.toString(),"","0","GEN00","15");
			}
		  }
// mode= edit Shows the "form" to update a record
		  if (request.getParameter("mode").equals("edit")) {
			((SIGAAdmGenericMTForm)form).setsLanguage(usrbean.getLanguage());
			forward = listAll(request, (SIGAAdmGenericMTForm)form, manager);
			loadLanguages = ((SIGAAdmGenericMTForm)form).getUsesLang();
			request.setAttribute("mode", "update");
		  }
// mode= update It is called when the user press button "UPDATE" to update a record
		  if (request.getParameter("mode").equals("update")) {
			try {
			  tx.begin();
			  request.setAttribute("mode", "update");
			  forward = update(request, (SIGAAdmGenericMTForm)form, manager,user);
			  ClsLogging.writeFileLog("foward "+forward,request,10);

			  if (forward.equals("updatedOk")) {
				request.setAttribute("descOperation","messages.updated.success");
				if (((SIGAAdmGenericMTForm)form).getUsesLang()) { //is master/Detail
				  if (updateMasterRecord(request,table,(SIGAAdmGenericMTForm)form).equals("updatedOk")) {
					tx.commit();
				  }
				  else {
					tx.rollback();
				  }
				}
				else { // no Master-Detail
				  tx.commit();
				}
			  }
			  else {
				tx.rollback();
				forward = "updatedOk"; // that's for opening the same window!
			  }
			  loadLanguages = ((SIGAAdmGenericMTForm)form).getUsesLang();
			} catch(ClsExceptions ex){
			  tx.rollback();
			  ex.setProcess("0");
			  throw ex;
			} catch (Exception ex){
			  throw new ClsExceptions (ex,ex.toString(),"","0","GEN00","15");
			}
		  }

// mode = delete It is called when the user prss button "DELETE" to delete a specific record
		  if (request.getParameter("mode").equals("delete")) {
			try{
			  tx.begin();
			  forward = deleteTraductionRecord(request, (SIGAAdmGenericMTForm)form, manager);
			  if (forward.equals("deletedOk")) {
				request.setAttribute("descOperation","messages.deleted.success");
				if (((SIGAAdmGenericMTForm)form).getUsesLang()) { //is master/Detail
				  if (updateMasterRecord(request,table, (SIGAAdmGenericMTForm)form).equals("updatedOk")) {
					tx.commit();
				  } else {
					tx.rollback();
				  }
				} else { // no Master-Detail
				  tx.commit();
				}
			  } else {
				tx.rollback();
			  }
			} catch(ClsExceptions ex){
			  tx.rollback();
			  ex.setProcess("0");
			  throw ex;
			} catch (Exception ex){
			  tx.rollback();
			  throw new ClsExceptions (ex,ex.toString(),"","0","GEN00","15");
			}
		  }
// Call method search
		  if (request.getParameter("mode").equals("search")){
			forward = "openReg";
			((SIGAAdmGenericMTForm)form).setsLanguage(usrbean.getLanguage());
			request.setAttribute("mode", "searching");
			loadLanguages = ((SIGAAdmGenericMTForm)form).getUsesLang();
			request.setAttribute("descOperation","OK");
		  }
//Search records
		  if (request.getParameter("mode").equals("searching")){
			ClsLogging.writeFileLog("Inside searching ",request,10);

			ht =((SIGAAdmGenericMTForm)form).getAllregisterData();
			manager.search(ht);
			if (manager.getAllRecords() != null) {
			  request.setAttribute("container", manager.getAllRecords());
			}
			else {
			  request.setAttribute("descOperation","messages.noRecordsFound");
			}
			forward = "listOk";
		  }

//Use language
		  if (loadLanguages) {
			Vector languages = ClsConstants.loadCDHandlerCollection("SIGA_LANGUAGES", "ID_LANGUAGE", "DESC_LANGUAGE", null, false, true);
			request.setAttribute("languages", languages);
		  }
		}
	  }catch (SystemException ex){
		throw new ClsExceptions(ex.getMessage(),"","0","GEN00","15");
	  }
	} catch (ClsExceptions pex) {
	  pex.setProcess("0");
	  pex.prepare(request);
	  forward = "exception";
	} finally {
	  
	  ClsLogging.writeFileLog("forward "+forward,request,10);
	}
	return mapping.findForward(forward);
  }

  /**
   * To create classes which contain queries
   * @param tableName Table Name
   * @param req provide request information for HTTP servlets
   * @param manager Manager Bean
   * @return Hastable which contains the table
   * @throws ClsExceptions SIGA exception type
   */
  private Hashtable loadMasterTable (String tableName, HttpServletRequest req, SIGAMasterTable manager) throws ClsExceptions {
	Hashtable table = null;
    if (manager == null) {
        manager = new SIGAMasterTable();
        manager.setUsrbean((UsrBean)req.getSession().getAttribute("USRBEAN"));
    }
	manager.searchMasterTable(tableName);
	table = manager.getRecord();
	return table;
  }

  /**
   * List query results
   * @param req provide request information for HTTP servlets
   * @param form SIGAAdmGenericMtForm
   * @param manager Manager Bean
   * @return String the operation to carry out
   * @throws ClsExceptions SIGA exception type
   */
  private String listAll(HttpServletRequest req, SIGAAdmGenericMTForm form, SIGAMasterTable manager) throws ClsExceptions {
	String result = "listOk";
	HttpSession ses= req.getSession();
	if (req.getParameter("mode").equals("edit") || req.getParameter("mode").equals("query")){
	  manager.setData(form.getAllregisterData());
	}
	manager.listAll(req.getParameter("mode"));
	if (manager.getAllRecords() != null) {
	  req.setAttribute("container", manager.getAllRecords());
	} else {
	  if (manager.getRecord() != null) {
		form.loadRegisterData(manager.getRecord());
		Hashtable htr = form.getAllregisterData();
		ses.setAttribute("htrbackup",htr);
		ClsLogging.writeFileLog("htr.size(): "+htr.size(),req,10);
		result = "openReg";
	  } else {
		result = "exception";
		throw new ClsExceptions("No data found","GEN","0","GEN00","21");
	  }
	}
	return result;
  }

  /**
   * Set data
   * @param req provide request information for HTTP servlets
   * @param form SIGAAdmGenericMTForm
   * @param manager Manager Bean
   * @param user modification
   * @return String the operation to carry out
   * @throws ClsExceptions SIGA exception type
   */
  private String update(HttpServletRequest req, SIGAAdmGenericMTForm form, SIGAMasterTable manager, String user) throws ClsExceptions {
	String result = "updatedOk";
	HttpSession ses = req.getSession();
	com.atos.utils.UsrBean usrbean =(com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
//	UserTransaction tx = usrbean.getTransaction();
	String access = usrbean.getAccessType();
	String tabName = req.getParameter("tableName");
	boolean updatedOk;
	ClsLogging.writeFileLog("Inside updating ",req,10);
	ClsLogging.writeFileLog("htr.size(): "+ form.getAllregisterData().size(),req,10);

	try {
	  if (usrbean!=null){
		String rec="";
		// set data
		manager.setData(form.getAllregisterData());
		manager.setUser(user);
		Hashtable htrb = (Hashtable)ses.getAttribute("htrbackup");
		if (htrb==null)	 ClsLogging.writeFileLog("htrb IS NULL",req,10);
		else ClsLogging.writeFileLog("htrb.size(): "+htrb.size(),req,10);
		manager.setDataBackup(htrb);
		updatedOk=manager.update();
		Vector ve = (Vector)ses.getAttribute("vectRecords");
		ClsLogging.writeFileLog("ve = "+ve,req,10);
		if (ve!=null){
		  if (ve.size()>0)
		  {
			ClsLogging.writeFileLog("ve = "+ve.size(),req,10);
			java.util.Enumeration en=ve.elements();
			while (en.hasMoreElements()){
			  com.atos.utils.PairsKeys obj = (com.atos.utils.PairsKeys)en.nextElement();
			  rec=rec+", "+obj.getIdObj();
			}
		  }
		}
		if (updatedOk){
		  switch(manager.getAffectedRecords()) {
			case 0: result = "notFound";
//			  tx.rollback();
			  break;
			case 1:
			  result = "updatedOk";
			  break;
			default: result = "tooManyRows";
//			  tx.rollback();
			  break;
		  }
		}else{
		  result ="updatedNotOk";
		  req.setAttribute("descOperation","error.messages.noupdated");
		}
	  }
	} catch (ClsExceptions ex) {
	  ex.setProcess("0");
	  ClsLogging.writeFileLogError(ex.getMessage(),ex,3);
	  throw ex;
	} catch (Exception ex) {
		ClsLogging.writeFileLogError(ex.getMessage(),ex,3);
	  throw new ClsExceptions (ex,ex.toString(), tabName,"0","GEN00","15");
	}
	return result;
  }


  /**
   * Set data
   * @param req provide request information for HTTP servlets
   * @param form SIGAAdmGenericMTForm
   * @param manager Manager Bean
   * @return String the operation to carry out
   * @throws ClsExceptions SIGA exception type
   */

  private String insert(HttpServletRequest req, SIGAAdmGenericMTForm form, SIGAMasterTable manager) throws ClsExceptions{
	String result = "insertedOk";
	HttpSession ses = req.getSession();
	com.atos.utils.UsrBean usrbean =(com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
	UserTransaction tx = usrbean.getTransaction();
	String access = usrbean.getAccessType();
	String tabName = req.getParameter("tableName");
	try {
	  if (usrbean!=null){
		manager.setData(form.getAllregisterData());
		boolean ok = manager.add();
		if(ok){
		  req.setAttribute("msg","messages.inserted.success");
		} else {
		  req.setAttribute("msg","messages.inserted.error");
		  result = "exception";
		  tx.rollback();
		}
	  }else if (access.equals("READ")){
		req.setAttribute("disabled","true");
	  }
	}
	catch (ClsExceptions ex) {
	  ex.setProcess("0");
	  throw ex;
	}
	catch (Exception ex) {
	  throw new ClsExceptions (ex,ex.toString(), tabName,"0","GEN00","15");
	}
	return result;
  }

  /**
   * Only for physical deleting on tables with flag FLAG_USES_LANG set to '1' .
   * @param req provide request information for HTTP servlets
   * @param form SIGAAdmGenericMTForm
   * @param manager Manager Bean
   * @throws ClsExceptions SIGA exception type
   * @return String
   */
  public String deleteTraductionRecord (HttpServletRequest req, SIGAAdmGenericMTForm form, SIGAMasterTable manager) throws ClsExceptions {
	String result = "deletedOk";
	HttpSession ses = req.getSession();
	com.atos.utils.UsrBean usrbean =(com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
	UserTransaction tx = usrbean.getTransaction();
	String access = usrbean.getAccessType();
	String tabName = req.getParameter("tableName");

	try {
	  if (usrbean!=null){
		manager.setData(form.getAllregisterData());
		int i = manager.delete();
		
		if (i != 1) { // // Record has been deleted
		  //result = "exception";
		  req.setAttribute("descOperation","error.messages.deleted");
		  result = "refreshDelete";
		} 
	  }
	} catch (ClsExceptions ex) {
	  throw ex;
	} catch (Exception ex) {
	  throw new ClsExceptions (ex,ex.toString(), tabName,"0","GEN00","15");
	}
	return result;
  }


  /**   
   * @param req provide request information for HTTP servlets
   * @param form SIGAAdmGenericMTForm
   * @param table Hashtable with the table
   * @return String the operation to carry out
   * @throws ClsExceptions SIGA exception type
   */
  private String updateMasterRecord(HttpServletRequest req, Hashtable table, SIGAAdmGenericMTForm form) throws ClsExceptions {
	String ok = "updatedOk";
	Row row = new Row();

	if (form.getLogicDelete()) {
	  Hashtable data = form.getAllregisterData();
	  data.put("DELETED", "0");
	  //if exists any NOT DELETED traduction record
	  String delete = null;
      if (ClsConstants.exists( (String)table.get("ID_TRADUCTION_TABLE"), new String[] {(String)table.get("ID_CODE_FIELD"), "DELETED"}, data)) {
// if the master record is DELETED
		if (!ClsConstants.exists((String)table.get("ID_MASTER_TABLE"), new String[] {(String)table.get("ID_CODE_FIELD"), "DELETED"}, data))
		{
		  //activate MASTER
		  delete = "0";
		}
	  } else{ //all are deleted!
		//if master record is Active
		if (ClsConstants.exists( (String)table.get("ID_MASTER_TABLE"), new String[] {(String)table.get("ID_CODE_FIELD"), "DELETED"}, data))
		{
		  //delete MASTER
		  delete = "1";
		}
	  }
	  if (delete != null) {
		data.put("DELETE", delete);
		row.load(data);
		row.setValue("USER_MODIFICATION", "SIGA");
		row.setValue("DATE_MODIFICATION", "SYSDATE");

		String[] updfields = new String[] {"DELETED",
		  "USER_MODIFICATION",
		  "DATE_MODIFICATION"};
		  String[] keyfields = new String[] {(String)table.get("ID_CODE_FIELD")};

		  if (row.update((String) table.get("ID_MASTER_TABLE"), keyfields, updfields) != 1) {
			ok = "exception";
		  }
	  }
	} else {  // not logicDelete
	  if (!ClsConstants.exists((String)table.get("ID_TRADUCTION_TABLE"), new String[] {(String)table.get("ID_CODE_FIELD")}, form.getAllregisterData()))
	  {
		row.load(form.getAllregisterData());
		row.delete((String) table.get("ID_MASTER_TABLE"), new String[]{(String)table.get("ID_CODE_FIELD")});
	  }
	}
	return ok;
  }
}

