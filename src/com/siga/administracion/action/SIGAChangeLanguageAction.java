package com.siga.administracion.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.Globals;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import javax.transaction.*;

/**
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

public class SIGAChangeLanguageAction extends Action {

	public SIGAChangeLanguageAction() {
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		HttpSession ses = request.getSession();
		String forward = "success";
		
		UserTransaction tx = null;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		java.util.Locale currentLocale =(java.util.Locale) ses.getAttribute(Globals.LOCALE_KEY);
		String lanCurrent=currentLocale.getLanguage();
		String countryCurrent=currentLocale.getCountry();
		String lanEnd="";
		String countryEnd="";
		String opt=request.getParameter("opt");
		
		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
		String lenguajeExt = "es";
		try {
			lenguajeExt = a.getLenguajeExt(opt);
		} catch (Exception e) {}
		
		ClsLogging.writeFileLog("Language to change: "+lenguajeExt,request,3);

		// RGG 27/02/2007 cambio para que el cambio de idioma se haga para la institución
		com.atos.utils.UsrBean usrbean =(com.atos.utils.UsrBean)ses.getAttribute("USRBEAN");
		if (usrbean!=null) { 
			lanEnd=lenguajeExt.toUpperCase();  //Lenguaje
		}
		countryEnd="es";			//Pais siempre Espanha

		
		CenInstitucionAdm ai = new CenInstitucionAdm(usrbean);
		Hashtable ht = new Hashtable();
		ht.put(CenInstitucionBean.C_IDINSTITUCION,usrbean.getLocation());
		try {
			tx = usr.getTransaction();
			tx.begin();	

			Vector vv = ai.selectByPK(ht);
			if (vv!=null && vv.size()>0) {
				CenInstitucionBean bb = (CenInstitucionBean) vv.get(0);
				bb.setIdLenguaje(opt);
				ai.updateDirect(bb);
			}
			
			tx.commit();
			
		} catch (Exception e) {
			try { tx.rollback(); } catch (Exception ee) {}
		}
		
	
	  	
		ses.setAttribute(Globals.LOCALE_KEY, new java.util.Locale(lanEnd, countryEnd));

		ClsLogging.writeFileLog("LOCALE CHANGED TO:  Language/Country:"+lanEnd+"/"+countryEnd,request,3);
		usrbean.setLanguage(opt);
		usrbean.setLanguageExt(lenguajeExt);
		
		ses.setAttribute("USRBEAN",usrbean);
		request.setAttribute("descOperation","messages.updated.successInstitucion");

		return mapping.findForward(forward);
	}
}