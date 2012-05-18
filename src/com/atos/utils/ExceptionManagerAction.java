/**
 * <p>Title: ExceptionManagerAction </p>
 * <p>Description: class that handles a ClsException </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import javax.servlet.http.*;
import org.apache.struts.action.*;

import com.siga.general.SIGAException;

public class ExceptionManagerAction extends Action {

  public ExceptionManagerAction() {
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) {
    String forward = "success";
    if (req.getAttribute("hiddenFrame") != null) {
      if (req.getAttribute("hiddenFrame").equals("1")) {
        forward="hSucces";
      }
    }
	if (req.getAttribute("exceptionTarget") != null) {
        //req.setAttribute("exceptionTarget", req.getAttribute("exceptionTarget"));
	    req.getSession().setAttribute("exceptionTarget", req.getAttribute("exceptionTarget"));
    }else{
	    req.getSession().removeAttribute("exceptionTarget");
	}
	
    Exception exc = (Exception)req.getAttribute("exception");

    HttpSession ses = req.getSession();
    UsrBean usrbean =(UsrBean)ses.getAttribute("USRBEAN");
    String languageCode = usrbean==null?"":usrbean.getLanguage();
    String userCode = usrbean==null?"":usrbean.getUserName();
    String institucion = usrbean==null?"":usrbean.getLocation();

    if (exc==null){
    	if(mapping!=null)
    		ClsLogging.writeFileLogError("@@@@ Excepcion NULA @@@@"+mapping.getPath(),req , 1);
      ClsLogging.writeFileLogError("@@@@ Excepcion NULA @@@@", req, 1);
      ClsLogging.writeFileLogError("@@@@ La excepcion no ha sido preparada @@@@", req, 1);
    }
    else
    {
      
      try {
        ExceptionManager mgr = new ExceptionManager(exc, languageCode, userCode, req, institucion);
        boolean cls = false;
        if (exc!=null && exc instanceof SIGAException) {
			cls = ((SIGAException)exc).getClsException();
		}
        if (cls) {
        	ClsLogging.writeFileLogError(mgr.getLogCompleteMessage(usrbean.getLanguage()), req, 1);
        } else {
        	ClsLogging.writeFileLog(mgr.getLogCompleteMessage(usrbean.getLanguage()), req, 1);
        }
        //req.setAttribute("manager", mgr);
        req.getSession().setAttribute("manager", mgr);
      } catch (ClsExceptions ex) { //don't capture this exception, but send original without treating
      	ex.printStackTrace();
        forward = "exception";
        //req.setAttribute("exc2", ex);
        req.getSession().setAttribute("exc2", ex);
      }
    }
    return mapping.findForward(forward);
  }
}