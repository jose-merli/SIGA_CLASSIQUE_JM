//Clase: DefinirRatificacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.scs.EjgResolucionService;
import org.redabogacia.sigaservices.app.services.scs.EjgService;
import org.redabogacia.sigaservices.app.vo.scs.EjgResolucionVo;
import org.redabogacia.sigaservices.app.vo.scs.EjgVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.pcajg.resoluciones.ResolucionesFicheroAbstract;

import es.satec.businessManager.BusinessManager;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirRatificacionEJGAction extends MasterAction {
	private static Logger log = Logger.getLogger(DefinirRatificacionEJGAction.class);
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
	
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
//		Accederemos al ejg por clave unica
		Hashtable nuevos = new Hashtable();
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		DefinirEJGForm miForm = (DefinirEJGForm)formulario;		
		nuevos = miForm.getDatos();
		
		EjgVo ejgVo = new EjgVo();
		EjgResolucionVo ejgResolucionVo = new EjgResolucionVo();
		ejgVo.setResolucion(ejgResolucionVo);
		
		try {

			ejgVo.setIdinstitucion(new Short(miForm.getIdInstitucion()));
			ejgVo.setAnio(new Short(miForm.getAnio()));
			ejgVo.setIdtipoejg(new Short(miForm.getIdTipoEJG()));
			ejgVo.setNumero(new Long(miForm.getNumero()));
			//Se realiza el nuevo parseo de IDTIPORATIFICACIONEJG
			if (!nuevos.get("IDTIPORATIFICACIONEJG").equals("")&&nuevos.get("IDTIPORATIFICACIONEJG").toString().contains(",")) {
				// Ponemos el IDTIPORATIFICACIONEJG en el formato correcto
				String[] idTipoRatificacionEjg = nuevos.get("IDTIPORATIFICACIONEJG").toString().split(",");
				ejgVo.setIdtiporatificacionejg(new Short(idTipoRatificacionEjg[0]));
				nuevos.put("IDTIPORATIFICACIONEJG", idTipoRatificacionEjg[0]);
			}			
			
			// Ponemos la fecha en el formato correcto
			if (nuevos.get("FECHARATIFICACION")!=null && !nuevos.get("FECHARATIFICACION").equals("")){
				ejgVo.setFecharatificacion(GstDate.convertirFechaDiaMesAnio(nuevos.get("FECHARATIFICACION").toString()));
				nuevos.put("FECHARATIFICACION", GstDate.getApplicationFormatDate("",nuevos.get("FECHARATIFICACION").toString()));
			}

			if (nuevos.get("FECHARESOLUCIONCAJG")!=null && !nuevos.get("FECHARESOLUCIONCAJG").equals("")){
				ejgVo.setFecharesolucioncajg(GstDate.convertirFechaDiaMesAnio(nuevos.get("FECHARESOLUCIONCAJG").toString()));
				nuevos.put("FECHARESOLUCIONCAJG", GstDate.getApplicationFormatDate("",nuevos.get("FECHARESOLUCIONCAJG").toString()));
			}
			
			if (nuevos.get("FECHANOTIFICACION")!=null && !nuevos.get("FECHANOTIFICACION").equals("")){
				ejgVo.setFechanotificacion(GstDate.convertirFechaDiaMesAnio(nuevos.get("FECHANOTIFICACION").toString()));
				nuevos.put("FECHANOTIFICACION", GstDate.getApplicationFormatDate("",nuevos.get("FECHANOTIFICACION").toString()));
			}
			
			if (nuevos.get("FECHAPRESENTACIONPONENTE")!=null && !nuevos.get("FECHAPRESENTACIONPONENTE").equals("")){
				ejgVo.setFechapresentacionponente(GstDate.convertirFechaDiaMesAnio(nuevos.get("FECHAPRESENTACIONPONENTE").toString()));
				nuevos.put("FECHAPRESENTACIONPONENTE", GstDate.getApplicationFormatDate("",nuevos.get("FECHAPRESENTACIONPONENTE").toString()));
			}
			
			nuevos.put("TURNADORATIFICACION",(nuevos.containsKey("TURNADORATIFICACION")?nuevos.get("TURNADORATIFICACION"):ClsConstants.DB_FALSE));
			ejgVo.setTurnadoratificacion((String) nuevos.get("TURNADORATIFICACION"));
			nuevos.put("REQUIERENOTIFICARPROC",(nuevos.containsKey("REQUIERENOTIFICARPROC")?nuevos.get("REQUIERENOTIFICARPROC"):ClsConstants.DB_FALSE));
			ejgVo.setRequierenotificarproc((String) nuevos.get("REQUIERENOTIFICARPROC"));
			
			if (nuevos.get("IDPONENTE")!=null && !nuevos.get("IDPONENTE").equals("")){
				nuevos.put("IDINSTITUCIONPONENTE", usr.getIdInstitucionComision());
				ejgVo.setIdinstitucionponente((Short)nuevos.get("IDINSTITUCIONPONENTE"));
				ejgVo.setIdponente(new Integer((String)nuevos.get("IDPONENTE")));
			}
			
			if (nuevos.get("ANIOACTA")!=null && !nuevos.get("ANIOACTA").equals("")){
				ejgVo.setAnioacta(new Short((String)nuevos.get("ANIOACTA")));
				ejgVo.setIdacta(new Long((String)nuevos.get("IDACTA")));
				ejgVo.setIdinstitucionacta(new Short((String)nuevos.get("IDINSTITUCIONACTA")));
			}
			if (nuevos.get("ANIOCAJG")!=null && !nuevos.get("ANIOCAJG").equals("")){
				ejgVo.setAniocajg(new Short((String)nuevos.get("ANIOCAJG")));
			}
			if (nuevos.get("NUMERO_CAJG")!=null && !nuevos.get("NUMERO_CAJG").equals("")){
				ejgVo.setNumeroCajg((String)nuevos.get("NUMERO_CAJG"));
			}
			if (nuevos.get("IDORIGENCAJG")!=null && !nuevos.get("IDORIGENCAJG").equals("")){
				ejgVo.setIdorigencajg(new Short((String)nuevos.get("IDORIGENCAJG")));
			}
			
			if (nuevos.get("DOCRESOLUCION")!=null && !nuevos.get("DOCRESOLUCION").equals("")){
				ejgVo.setDocresolucion((String)nuevos.get("DOCRESOLUCION"));
			}
			ejgVo.setUsumodificacion(new  Integer(usr.getUserName()));
			
			if (nuevos.get("REFAUTO")!=null && !nuevos.get("REFAUTO").equals("")){
				ejgVo.setRefauto((String)nuevos.get("REFAUTO"));
			}
			if (nuevos.get("IDFUNDAMENTOJURIDICO")!=null && !nuevos.get("IDFUNDAMENTOJURIDICO").equals("")){
				ejgVo.setIdfundamentojuridico(new Short((String)nuevos.get("IDFUNDAMENTOJURIDICO")));
			}
			
			if (nuevos.get("RATIFICACIONDICTAMEN")!=null && !nuevos.get("RATIFICACIONDICTAMEN").equals("")){
				ejgVo.setRatificaciondictamen((String)nuevos.get("RATIFICACIONDICTAMEN"));
			}
			if(usr.isComision()){
				if (miForm.getNotasCAJG()!=null && !miForm.getNotasCAJG().equals("")){
					ejgResolucionVo.setNotascajg(miForm.getNotasCAJG());
				}else
					ejgResolucionVo.setNotascajg("");
			}
			EjgService ejgService = (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			ejgService.update(ejgVo);
			
			// Actualizamos en la base de datos
//			ejgAdm.update(nuevos,(Hashtable)request.getSession().getAttribute("DATABACKUP"));

			// En DATABACKUP almacenamos los datos más recientes por si se vuelve a actualizar seguidamente
//			nuevos.put("FECHAMODIFICACION", "sysdate");
			request.getSession().setAttribute("DATABACKUP",nuevos);
			
		}catch (BusinessException e) {
			return errorRefresco(e.getMessage(),new ClsExceptions(e.getMessage()), request);
//			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		}
		
		return exitoRefresco("messages.updated.success",request);
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		/* "DATABACKUP" se usa habitualmente así que en primer lugar borramos esta variable */		
		request.getSession().removeAttribute("DATABACKUP");
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		
		
		DefinirEJGForm miForm = (DefinirEJGForm)formulario;
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();		
		String idInstitucion = null;		
		if(request.getParameter("ANIO")!=null){
			miHash.put("ANIO",request.getParameter("ANIO").toString());
			miHash.put("NUMERO",request.getParameter("NUMERO").toString());
			miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
			idInstitucion = request.getParameter("IDINSTITUCION").toString();
			miHash.put("IDINSTITUCION",idInstitucion);
			if(request.getParameter("modoActa")!=null)
				request.setAttribute("modoActa", request.getParameter("modoActa").toString());
		}else{
			miHash.put("ANIO",miForm.getAnio());
			miHash.put("NUMERO",miForm.getNumero());
			miHash.put("IDTIPOEJG",miForm.getIdTipoEJG());
			idInstitucion = miForm.getIdInstitucion().toString();
			miHash.put("IDINSTITUCION",idInstitucion);
			request.setAttribute("modoActa", miForm.getModoActa());
			
		}
		GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
		try {
			String accesoActaSt = paramAdm.getValor(idInstitucion, "SCS", "HABILITA_ACTAS_COMISION", "N");
			request.setAttribute("accesoActa", accesoActaSt.equalsIgnoreCase("S")?"true":"false");
			String validarObligatoriedadResolucion = paramAdm.getValor (idInstitucion, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_VALIDAR_OBLIGATORIEDAD_RESOLUCION, "0");
			request.setAttribute("ISOBLIGATORIORESOLUCION",validarObligatoriedadResolucion.equals(ClsConstants.DB_TRUE)?true:false);
			String prefijoExpedienteCajg = paramAdm.getValor (idInstitucion, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
			request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);
			
		} catch (Exception e) {
			throwExcp("Error al recuperar el parametro HABILITA_ACTAS_COMISION y VALIDAR_OBLIGATORIEDAD_RESOLUCION",e,null);
		}
		
		
		// Comprobamos que el usuario tenga acceso a las actas de la comision
		// Si no lo tiene habra que mostrar la fechaResolucionCAJG
		// Si lo tiene podrá seleccionar el acta
		/*
		String accesoActaSt=usr.getPermisoProceso("JGR_ActasComision");
		boolean accesoActa = accesoActaSt!=null && (accesoActaSt.equalsIgnoreCase(SIGAConstants.ACCESS_FULL));
		request.setAttribute("accesoActa", accesoActa?"true":"false");
		*/
		
		
		ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));
		
		try {		
			if(usr.isComision()){
				EjgResolucionService ejgResolucionService = (EjgResolucionService) BusinessManager.getInstance().getService(EjgResolucionService.class);
				EjgResolucionVo ejgResolucionVo = new EjgResolucionVo();
				ejgResolucionVo.setIdinstitucion(new Short(idInstitucion));
				ejgResolucionVo.setAnio(new Short((String)miHash.get("ANIO")));
				ejgResolucionVo.setIdtipoejg(new Short((String)miHash.get("IDTIPOEJG")));
				ejgResolucionVo.setNumero(new Long((String)miHash.get("NUMERO")));
				ejgResolucionVo = ejgResolucionService.get(ejgResolucionVo);
				if(ejgResolucionVo!=null)
					miForm.setNotasCAJG(ejgResolucionVo.getNotascajg());
				
			}
			
			v = ejgAdm.selectPorClave(miHash);
			try{
				Hashtable ejgBeanHashtable = ejgAdm.beanToHashTable((ScsEJGBean)v.get(0));
				if(request.getParameter("IDINSTITUCIONACTA")!=null){
					ejgBeanHashtable.put("IDINSTITUCIONACTA",(String)request.getParameter("IDINSTITUCIONACTA"));
					ejgBeanHashtable.put("IDACTA",(String)request.getParameter("IDACTA"));
					ejgBeanHashtable.put("NUMEROACTA", (String)request.getParameter("NUMEROACTA"));
					ejgBeanHashtable.put("ANIOACTA", (String)request.getParameter("ANIOACTA"));
					ejgBeanHashtable.put("FECHARESOLUCIONCAJG", (String)request.getParameter("FECHARESOLUCION"));
					ejgBeanHashtable.put("IDTIPORATIFICACIONEJG", (String)request.getParameter("IDTIPORATIFICACIONEJG"));
					ejgBeanHashtable.put("IDFUNDAMENTOJURIDICO", (String)request.getParameter("IDFUNDAMENTOJURIDICO"));
				}
				request.getSession().setAttribute("DATABACKUP",ejgBeanHashtable);
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
		ClsLogging.writeFileLog(" Empezamos la descarga",10);
		File file = UtilidadesFicheros.getFicheroResolucionPDF(getIDInstitucion(request).toString(), miForm.getDocResolucion());

		if (file == null) {								
			throw new SIGAException("messages.general.error.ficheroNoExiste");
		}				
		
		request.setAttribute("nombreFichero", file.getName());
		request.setAttribute("rutaFichero", file.getAbsolutePath());

		return "descargaFichero";
	}
	
	
	
}
