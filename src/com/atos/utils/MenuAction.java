/**
 * <p>Title: MenuAction </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.siga.gui.processTree.SIGAPTConstants;

public class MenuAction extends Action {
  ClsExceptions ex = null;

  public MenuAction() {
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response){
    String forward = "";

    try {
         forward=loadAll(request);
    }
    catch (ClsExceptions ex) {
      ex.prepare(request);
      forward = "exception";
   } finally{
   }
   return mapping.findForward(forward);
  }


  private String loadAll(HttpServletRequest req) throws ClsExceptions{
    String result = "success";
    HttpSession ses = req.getSession();
    UsrBean usrbean =(UsrBean)ses.getAttribute("USRBEAN");
    String access = usrbean.getAccessType();
    String prof[] = usrbean.getProfile();
    String institucion = usrbean.getLocation();
 try{
    if (usrbean!=null){
       if(access.equals(SIGAPTConstants.ACCESS_FULL)){

       }else if (access.equals(SIGAPTConstants.ACCESS_READ)){
          req.setAttribute("disabled","true");
        }
      }

      Menu tableMenu = new Menu(req);
      tableMenu.searchMenu(prof, institucion);
      if (tableMenu.getAllRecords() != null) {
        req.setAttribute("container", tableMenu.getAllRecords());
      } else {
        throw new ClsExceptions("No tables Found.", "","","","");
}


    } catch (ClsExceptions pex) {
      //result="exception";
      throw new ClsExceptions (ex.toString(), "","0","GEN00","15");
    } finally {
    }
    return result;
  }



}