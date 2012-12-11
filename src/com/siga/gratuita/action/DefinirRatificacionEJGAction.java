//Clase: DefinirRatificacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.pcajg.resoluciones.ResolucionesFicheroAbstract;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirRatificacionEJGAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		else return super.executeInternal(mapping, formulario, request, response); 
	}
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		Hashtable nuevos = new Hashtable();
		UserTransaction tx=null;		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		DefinirEJGForm miForm = (DefinirEJGForm)formulario;		
		nuevos = miForm.getDatos();
		ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));
		
		try {
			
			//Se realiza el nuevo parseo de IDTIPORATIFICACIONEJG
			if (!nuevos.get("IDTIPORATIFICACIONEJG").equals("")&&nuevos.get("IDTIPORATIFICACIONEJG").toString().contains(",")) {
				// Ponemos el IDTIPORATIFICACIONEJG en el formato correcto
				String[] idTipoRatificacionEjg = nuevos.get("IDTIPORATIFICACIONEJG").toString().split(",");
				nuevos.put("IDTIPORATIFICACIONEJG", idTipoRatificacionEjg[0] );
			}			
			
			// Ponemos la fecha en el formato correcto
			if (nuevos.get("FECHARATIFICACION")!=null && !nuevos.get("FECHARATIFICACION").equals(""))
				nuevos.put("FECHARATIFICACION", GstDate.getApplicationFormatDate("",nuevos.get("FECHARATIFICACION").toString()));

			if (nuevos.get("FECHARESOLUCIONCAJG")!=null && !nuevos.get("FECHARESOLUCIONCAJG").equals(""))
				nuevos.put("FECHARESOLUCIONCAJG", GstDate.getApplicationFormatDate("",nuevos.get("FECHARESOLUCIONCAJG").toString()));
			
			if (nuevos.get("FECHANOTIFICACION")!=null && !nuevos.get("FECHANOTIFICACION").equals(""))
				nuevos.put("FECHANOTIFICACION", GstDate.getApplicationFormatDate("",nuevos.get("FECHANOTIFICACION").toString()));
			
			if (nuevos.get("FECHAPRESENTACIONPONENTE")!=null && !nuevos.get("FECHAPRESENTACIONPONENTE").equals(""))
				nuevos.put("FECHAPRESENTACIONPONENTE", GstDate.getApplicationFormatDate("",nuevos.get("FECHAPRESENTACIONPONENTE").toString()));
			
			nuevos.put("TURNADORATIFICACION",(nuevos.containsKey("TURNADORATIFICACION")?nuevos.get("TURNADORATIFICACION"):ClsConstants.DB_FALSE));
			nuevos.put("REQUIERENOTIFICARPROC",(nuevos.containsKey("REQUIERENOTIFICARPROC")?nuevos.get("REQUIERENOTIFICARPROC"):ClsConstants.DB_FALSE));
			// Actualizamos en la base de datos
			tx=usr.getTransaction();
			tx.begin();
			ejgAdm.update(nuevos,(Hashtable)request.getSession().getAttribute("DATABACKUP"));
			tx.commit();
			// En DATABACKUP almacenamos los datos más recientes por si se vuelve a actualizar seguidamente
			nuevos.put("FECHAMODIFICACION", "sysdate");
			request.getSession().setAttribute("DATABACKUP",nuevos);			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoRefresco("messages.updated.success",request);
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		/* "DATABACKUP" se usa habitualmente así que en primer lugar borramos esta variable */		
		request.getSession().removeAttribute("DATABACKUP");
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");		
		
		miHash.put("ANIO",request.getParameter("ANIO").toString());
		miHash.put("NUMERO",request.getParameter("NUMERO").toString());
		miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
		miHash.put("IDINSTITUCION",usr.getLocation().toString());
		
		// Comprobamos que el usuario tenga acceso a las actas de la comision
		// Si no lo tiene habra que mostrar la fechaResolucionCAJG
		// Si lo tiene podrá seleccionar el acta
		/*
		String accesoActaSt=usr.getPermisoProceso("JGR_ActasComision");
		boolean accesoActa = accesoActaSt!=null && (accesoActaSt.equalsIgnoreCase(SIGAConstants.ACCESS_FULL));
		request.setAttribute("accesoActa", accesoActa?"true":"false");
		*/
		GenParametrosAdm paramAdm = new GenParametrosAdm(usr); 
		try {
			String accesoActaSt = paramAdm.getValor(usr.getLocation(), "SCS", "HABILITA_ACTAS_COMISION", "N");
			request.setAttribute("accesoActa", accesoActaSt.equalsIgnoreCase("S")?"true":"false");
		} catch (Exception e) {
			throwExcp("Error al recuperar el parametro HABILITA_ACTAS_COMISION",e,null);
		}
		
		ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));
		
		try {			
			v = ejgAdm.selectPorClave(miHash);
			try{
				request.getSession().setAttribute("DATABACKUP",ejgAdm.beanToHashTable((ScsEJGBean)v.get(0)));
			}catch (Exception e) {
				throwExcp("error.general.yanoexiste",e,null);
			}
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "inicio";		
	}
	
	protected String download (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		DefinirEJGForm miForm = (DefinirEJGForm)formulario;			
		
		File file = getFicheroPDF(getIDInstitucion(request).toString(), miForm.getDocResolucion());

		if (file == null) {								
			throw new SIGAException("messages.general.error.ficheroNoExiste");
		}				
		
		request.setAttribute("nombreFichero", file.getName());
		request.setAttribute("rutaFichero", file.getAbsolutePath());

		return "descargaFichero";
	}
	
	private File getFicheroPDF(String idInstitucion, String docResolucion) {
		File file = new File(ResolucionesFicheroAbstract.getDirectorioArchivos(idInstitucion));
		file = new File(file, docResolucion + "." + ResolucionesFicheroAbstract.getExtension(idInstitucion));
		if (!file.exists()) {
			file = null;
		}		
		return file;
	}
	
	
}