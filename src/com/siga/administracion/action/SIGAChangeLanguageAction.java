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
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenInstitucionLenguajesAdm;
import com.siga.beans.CenInstitucionLenguajesBean;
import com.sun.media.imageioimpl.plugins.clib.CLibImageReader;

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
		String idioma="";
		
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
		
		
		// Verficamos si el idioma del usuario esta traducido
		CenInstitucionLenguajesAdm admLen = new CenInstitucionLenguajesAdm (usrbean);
		
        
		try {
			CenClienteAdm cliAdm=new CenClienteAdm (usrbean);
	        idioma=cliAdm.getLenguaje(usrbean.getLocation(), String.valueOf(usrbean.getIdPersona()));
			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDINSTITUCION, usrbean.getLocation());
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDLENGUAJE,idioma);
			Vector vLen = admLen.selectByPK(h);
			
			if (vLen == null || vLen.size() != 1) {
				// El idoma del lenguaje no esta traducido, ponemos el idioma de la institucion
				idioma = opt;
			}
		

		if (idioma!=null) {
			Hashtable ht1 = new Hashtable();
			ht1.put(AdmLenguajesBean.C_IDLENGUAJE,idioma);
			AdmLenguajesAdm len = new AdmLenguajesAdm(usrbean);
			Vector v3 = len.selectByPK(ht1);
			if (v3!=null && v3.size()>0) {
				AdmLenguajesBean l = (AdmLenguajesBean) v3.get(0);
				lenguajeExt = l.getCodigoExt();
				
			}
		}
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
		
		usrbean.setLanguage(idioma);
		usrbean.setLanguageExt(lenguajeExt);
		ses.setAttribute("USRBEAN",usrbean);
		ses.setAttribute(Globals.LOCALE_KEY, new java.util.Locale(lanEnd, countryEnd));

		ClsLogging.writeFileLog("LOCALE CHANGED TO:  Language/Country:"+lanEnd+"/"+countryEnd,request,3);
		
		request.setAttribute("descOperation","messages.updated.successInstitucion");

		return mapping.findForward(forward);
	}
}