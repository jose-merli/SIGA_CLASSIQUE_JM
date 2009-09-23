package com.siga.administracion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;

public class SIGAAdmMasterTablesLoader extends Action
{
  public SIGAAdmMasterTablesLoader() { }

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {  	
    String forward = "";
    try {
      forward=loadAllTables(request);
    } catch (ClsExceptions ex) {
      forward="exception";
      ex.prepare(request);
    } 
    finally {
    }
    return mapping.findForward(forward);
  }

  private String loadAllTables(HttpServletRequest req) throws ClsExceptions{
    String result = "success";
    try {
      SIGAMasterTable table = new SIGAMasterTable();
      table.setUsrbean((UsrBean)req.getSession().getAttribute("USRBEAN"));
      table.searchMasterTable(null);
      if (table.getAllRecords() != null) {
        req.setAttribute("container", table.getAllRecords());
      } else {
        throw new ClsExceptions("No master tables Found.", "","","","");
      }
    } catch (ClsExceptions pex) {
      result = "exception";
      pex.prepare(req);
    } finally {
    }
    return result;
  }

}