/*
 * Created on Jan 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmCertificadosAdm;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CertificadosAction extends MasterAction{

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try
		{		
			String accion = (String)request.getParameter("accion");
			
			// RGG 02-02-2005 Para controlar si estamos en alta
			if (accion.equals("nuevo") || (request.getParameter("idPersona").equals("") && request.getParameter("idInstitucion").equals(""))) {
				return "clienteNoExiste";
			}

			Long idPersona = new Long(request.getParameter("idPersona"));
			Integer idInstitucionPersona = Integer.valueOf(request.getParameter("idInstitucion"));
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserName(request),usr,idInstitucionPersona.intValue(),idPersona.longValue());
			CenPersonaBean personaBean = personaAdm.getIdentificador(idPersona);
			AdmCertificadosAdm certificadosAdm = new AdmCertificadosAdm(this.getUserBean(request));
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserName(request),usr,idInstitucionPersona.intValue(), idPersona.longValue());
			
			Vector v = null;
			String nombre = null;
			String numero = "";
			String estadoColegial="";
			
			CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucionPersona);
			numero = colegiadoAdm.getIdentificadorColegiado(bean);
			nombre = personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();;
			estadoColegial = clienteAdm.getEstadoColegial(String.valueOf(idPersona), String.valueOf(idInstitucionPersona));
			
			v = certificadosAdm.getCertificadosUsuario(personaBean.getNIFCIF());
			request.setAttribute("vDatos", v);	
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("estadoColegial", estadoColegial);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"}, e, null);
		}
		return "inicio";
	}
}
