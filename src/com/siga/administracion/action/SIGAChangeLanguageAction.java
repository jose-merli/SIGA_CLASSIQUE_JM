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
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenInstitucionLenguajesAdm;
import com.siga.beans.CenInstitucionLenguajesBean;

import javax.transaction.*;

/**
 * <p>T�tulo: </p>
 * <p>Descripci�n: </p>
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
		String idiomaUsuario="";
		
		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
		String lenguajeExt = "es";
		try {
			lenguajeExt = a.getLenguajeExt(opt);
		} catch (Exception e) {}
		
		ClsLogging.writeFileLog("Language to change: "+lenguajeExt,request,3);

		// RGG 27/02/2007 cambio para que el cambio de idioma se haga para la instituci�n
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
		//aalg. INC_10707_SIGA. 
		AdmUsuariosAdm admUsu = null;
		AdmUsuariosBean ub = null;
        
		try {
			Vector vLen = null;
			if (usrbean.getIdPersona() == -1){
				admUsu = new AdmUsuariosAdm(usrbean);
				Hashtable hash = new Hashtable();
				UtilidadesHash.set(hash, AdmUsuariosBean.C_IDINSTITUCION, usrbean.getLocation());
				UtilidadesHash.set(hash, AdmUsuariosBean.C_IDUSUARIO,usrbean.getUserName());
				Vector v = admUsu.selectByPK(hash);
				//Vector v = admUsu.getDatosUsuario(usrbean.getUserName(), usrbean.getLocation());
				ub = (AdmUsuariosBean)v.get(0);
				idiomaUsuario = ub.getIdLenguaje();
			}
			else{
				CenClienteAdm cliAdm=new CenClienteAdm (usrbean);
		        idiomaUsuario=cliAdm.getLenguaje(usrbean.getLocation(), String.valueOf(usrbean.getIdPersona()));
			}
			Hashtable h = new Hashtable();
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDINSTITUCION, usrbean.getLocation());
			UtilidadesHash.set(h, CenInstitucionLenguajesBean.C_IDLENGUAJE,idiomaUsuario);
			vLen = admLen.selectByPK(h);
			
			if (vLen == null || vLen.size() != 1) {
				// El idoma del lenguaje no esta traducido, ponemos el idioma de la institucion
				idiomaUsuario = opt;
			}
		

		if (idiomaUsuario!=null) {
			Hashtable ht1 = new Hashtable();
			ht1.put(AdmLenguajesBean.C_IDLENGUAJE,idiomaUsuario);
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
		
		//aalg. INC_10707_SIGA. a�adir el update del usuario
		if (usrbean.getIdPersona() == -1){
			String idiomaAdmUsuario = (String)request.getParameter("idioma");
			ub.setIdLenguaje(idiomaAdmUsuario);
			admUsu.update(ub);
		}
		tx.commit();
			
		} catch (Exception e) {
			try { tx.rollback(); } catch (Exception ee) {}
		}
		
		usrbean.setLanguage(idiomaUsuario);
		usrbean.setLanguageExt(lenguajeExt);
		usrbean.setLanguageInstitucion(opt);
		ses.setAttribute("USRBEAN",usrbean);
		ses.setAttribute(Globals.LOCALE_KEY, new java.util.Locale(lanEnd, countryEnd));

		ClsLogging.writeFileLog("LOCALE CHANGED TO:  Language/Country:"+lanEnd+"/"+countryEnd,request,3);
		
		request.setAttribute("descOperation","messages.updated.successInstitucion");

		return mapping.findForward(forward);
	}
}