package com.siga.gratuita.action;


import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.scs.EjgService;
import org.redabogacia.sigaservices.app.vo.scs.EjgVo;
import org.redabogacia.sigaservices.app.vo.scs.EstadoEjgVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEstadoEJGAdm;
import com.siga.beans.ScsEstadoEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEstadosEJGForm;

import es.satec.businessManager.BusinessManager;


/**
 * Maneja las acciones que se pueden realizar sobre la tabla SCS_ESTADOSEJG
 * 
 * @since 14-02-2005
 * @version adrianag - 10-09-2008:
*/
public class DefinirEstadosEJGAction extends MasterAction
{
	
	//////////////////// METODOS GENERICOS DE ACTION ////////////////////
	protected ActionForward executeInternal (ActionMapping mapping,
											 ActionForm formulario,
											 HttpServletRequest request,
											 HttpServletResponse response)
			throws SIGAException
	{
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null) {
			try {
				return mapping.findForward (this.abrir (mapping, miForm, request, response));
			} catch (Exception e) {
				return mapping.findForward ("exception");
			}
		}else {
			
				String accion = miForm.getModo();
				if (accion != null && accion.equalsIgnoreCase("consultarEstadoPericles")){
					return  mapping.findForward (consultarEstadoPericles(mapping, miForm, request, response));
				}else
					return super.executeInternal (mapping, formulario, request, response);
			
		}
			
				 
	} //executeInternal ()
	protected String consultarEstadoPericles (ActionMapping mapping,
			   MasterForm formulario,
			   HttpServletRequest request,
					   HttpServletResponse response) throws SIGAException
		
		{
		
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario; 
		
		try {
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			EjgVo ejgVo = new EjgVo(Short.valueOf( miForm.getIdInstitucion()), 
					 Short.valueOf(miForm.getAnio ()),Short.valueOf(miForm.getIdTipoEJG() ),Long.valueOf(miForm.getNumero ()));
			
			
			ejgService.consultarEstadoPericles(ejgVo);
		} catch (BusinessException e) {
			throwExcp ("messages.general.error", e, null);
		}
		return exitoRefresco("messages.gratuita.actualizadonResoluciones", request);
	}
			
	//////////////////// METODOS DE ACCIONES ////////////////////
	/** No implementado */
	protected String buscarPor (ActionMapping mapping,
								MasterForm formulario,
								HttpServletRequest request,
								HttpServletResponse response)
			throws SIGAException
	{		
		return null;
	} //buscarPor ()
	
	/** No implementado */
	protected String editar (ActionMapping mapping,
							 MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
			throws SIGAException
	{   Vector v=new Vector();
	   try{
		Hashtable miHash = new Hashtable();
		Vector ocultos = formulario.getDatosTablaOcultos (0);			
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario; 
		miHash.put (ScsEstadoEJGBean.C_IDESTADOPOREJG, ocultos.get(0));
		miHash.put (ScsEstadoEJGBean.C_IDTIPOEJG, miForm.getIdTipoEJG ());
		miHash.put (ScsEstadoEJGBean.C_ANIO, miForm.getAnio ());
		miHash.put (ScsEstadoEJGBean.C_NUMERO, miForm.getNumero ());
		miHash.put (ScsEstadoEJGBean.C_IDINSTITUCION, miForm.getIdInstitucion());
		ScsEstadoEJGAdm  estadoEJGAdm=new ScsEstadoEJGAdm(this.getUserBean(request));
		v=estadoEJGAdm.selectByPK(miHash);
		request.getSession ().setAttribute ("EJG", miHash);
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
		if(ejgService.isColegioZonaComun(Short.valueOf(usr.getLocation())) && ejgService.isColegioConfiguradoEnvioPericles(Short.valueOf(usr.getLocation()))){
			request.setAttribute("envioPericles","true");
		}else {
			request.setAttribute("envioPericles","false");
		}
		request.setAttribute ("resultado", v);
		request.setAttribute("modo","editar");
	   }catch (Exception e){
	   	throwExcp ("messages.general.error", e, null);
	   	
	   }
		return "editar";
	} //editar ()
	
	/** No implementado */
	protected String ver (ActionMapping mapping,
						  MasterForm formulario,
						  HttpServletRequest request,
						  HttpServletResponse response)
			throws SIGAException
	{		
		Vector v=new Vector();
	   try{
		Hashtable miHash = new Hashtable();
		Vector ocultos = formulario.getDatosTablaOcultos (0);			
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario; 
		miHash.put (ScsEstadoEJGBean.C_IDESTADOPOREJG, ocultos.get(0));
		miHash.put (ScsEstadoEJGBean.C_IDTIPOEJG, miForm.getIdTipoEJG ());
		miHash.put (ScsEstadoEJGBean.C_ANIO, miForm.getAnio ());
		miHash.put (ScsEstadoEJGBean.C_NUMERO, miForm.getNumero ());
		miHash.put (ScsEstadoEJGBean.C_IDINSTITUCION, miForm.getIdInstitucion());
		ScsEstadoEJGAdm  estadoEJGAdm=new ScsEstadoEJGAdm(this.getUserBean(request));
		v=estadoEJGAdm.selectByPK(miHash);
		request.getSession ().setAttribute ("EJG", miHash);
		request.setAttribute("envioPericles","false");
		request.setAttribute ("resultado", v);
		request.setAttribute("modo","consulta");
	   }catch (Exception e){
	   	throwExcp ("messages.general.error", e, null);
	   	
	   }
		return "ver";
	} //ver ()
	
	/**
	 * Devuelve los datos necesarios para la insercion de un nuevo estado
	 */
	protected String nuevo (ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		Hashtable miHash = new Hashtable();
		
		miHash.put ("ANIO", formulario.getDatos ().get ("ANIO"));
		miHash.put ("NUMERO", formulario.getDatos ().get ("NUMERO"));
		miHash.put ("IDTIPOEJG", formulario.getDatos ().get ("IDTIPOEJG"));
		miHash.put ("IDINSTITUCION", formulario.getDatos ().get ("IDINSTITUCION"));
		
		request.getSession ().setAttribute ("EJG", miHash);
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
		if(ejgService.isColegioZonaComun(Short.valueOf(usr.getLocation())) && ejgService.isColegioConfiguradoEnvioPericles(Short.valueOf(usr.getLocation()))){
			request.setAttribute("envioPericles","true");
		}else {
			request.setAttribute("envioPericles","false");
		}
		request.setAttribute("modo","nuevo");
		return "insertar";
	} //nuevo ()
	
	/**
	 * Rellena un hash con los valores recogidos del formulario
	 * y los inserta en la base de datos
	 */
	protected String insertar (ActionMapping mapping,
							   MasterForm formulario,
							   HttpServletRequest request,
							   HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		UserTransaction tx = null;
		
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario;		
		
		try {
			
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			EstadoEjgVo estadoEjg = miForm.getEstadoEjgVo(miForm);
			estadoEjg.setAutomatico(AppConstants.DB_FALSE);
			estadoEjg.setUsumodificacion(new Integer (usr.getUserName()));
			if(usr.isComision())
				estadoEjg.setPropietariocomision(ClsConstants.DB_TRUE);
			
			ejgService.insertEstadoEjg(estadoEjg,usr.getLanguageInstitucion());
			
			
		} catch (Exception e) {
			throwExcp ("messages.general.error",
					new String[] {"modulo.gratuita"}, e, tx);
		}
		
		return exitoModal ("messages.inserted.success", request);
	} //insertar ()
	
	
	
	/**
	 * No implementado
	 */
	protected String modificar (ActionMapping mapping,
								MasterForm formulario,
								HttpServletRequest request,
								HttpServletResponse response)
			throws SIGAException
	{ 
		
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario;		
		try {
			
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			EstadoEjgVo estadoEjg = miForm.getEstadoEjgVo(miForm);
			estadoEjg.setUsumodificacion(new Integer (usr.getUserName()));
			if(usr.isComision())
				estadoEjg.setPropietariocomision(ClsConstants.DB_TRUE);
			
			ejgService.updateEstadoEjg(estadoEjg,usr.getLanguageInstitucion());
			
			
		} catch (Exception e) {
			throwExcp ("messages.general.error",
					new String[] {"modulo.gratuita"}, e,null);
		}
		
		return exitoModal ("messages.updated.success", request);
	} //modificar ()
	
	/**
	 * Rellena un hash con los valores recogidos del formulario
	 * y los borra de la base de datos
	 */
	protected String borrar (ActionMapping mapping,
							 MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		ScsEstadoEJGAdm admBean =  new ScsEstadoEJGAdm (usr);
		UserTransaction tx = null;
		
		Vector ocultos = formulario.getDatosTablaOcultos (0);			
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario; 
		
		Hashtable miHash = new Hashtable ();
		
		try {				
			miHash.put (ScsEstadoEJGBean.C_IDESTADOPOREJG, ocultos.get(0));
			miHash.put (ScsEstadoEJGBean.C_IDTIPOEJG, miForm.getIdTipoEJG ());
			miHash.put (ScsEstadoEJGBean.C_ANIO, miForm.getAnio ());
			miHash.put (ScsEstadoEJGBean.C_NUMERO, miForm.getNumero ());
			miHash.put (ScsEstadoEJGBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			miHash.put ("FECHABAJA", "SYSDATE");
			String[]campos = new String[]{"FECHABAJA"};
			admBean.updateDirect(miHash,null,campos);		    
			
		} catch (Exception e) {
			   throwExcp("messages.deleted.error",e,tx);
		}
		
		return exitoRefresco ("messages.deleted.success", request);
	} //borrar ()

	/** 
	 * No implementado
	 */	
	protected String buscar (ActionMapping mapping,
							 MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
			throws SIGAException
	{
		return null;		
	} //buscar ()
	
	/**
	 * Devuelve la lista de Estados EJG
	 */
	protected String abrir (ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		ScsEstadoEJGAdm admBean =  new ScsEstadoEJGAdm (usr);		
		
		//borrando atributos de sesion por si acaso		
		request.getSession().removeAttribute("DATABACKUP");
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable ();
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario;
		String accion = request.getSession ().getAttribute ("accion").toString ();
		
		
		String idInstitucion = null;
		//rellenando el hash de consulta
		if(request.getParameter("ANIO")!=null){
			UtilidadesHash.set(miHash,"ANIO",request.getParameter("ANIO").toString());
			UtilidadesHash.set(miHash,"NUMERO",request.getParameter("NUMERO").toString());
			UtilidadesHash.set(miHash,"IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
			UtilidadesHash.set(miHash,"IDINSTITUCION",request.getParameter("IDINSTITUCION").toString());
			idInstitucion = request.getParameter("IDINSTITUCION").toString();
		}else{
		
			miHash.put ("ANIO", miForm.getAnio ());
			miHash.put ("NUMERO", miForm.getNumero ());
			miHash.put ("IDTIPOEJG", miForm.getIdTipoEJG ());
			miHash.put (ScsEstadoEJGBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			idInstitucion = miForm.getIdInstitucion();
			
		}
		request.setAttribute ("DATOSEJG", miHash);
		
		
		String consulta = 
			"SELECT estado.*, " + 
			"       "+UtilidadesMultidioma.getCampoMultidioma 
							("estadoejg.DESCRIPCION", usr.getLanguage ())+" " +
			"  FROM SCS_ESTADOEJG estado, SCS_MAESTROESTADOSEJG estadoejg " +
			" WHERE estado.IDESTADOEJG = estadoejg.IDESTADOEJG " +
			"   AND estado.IDINSTITUCION = "+UtilidadesHash.getString(miHash,"IDINSTITUCION")+" " +
			"   AND estado.IDTIPOEJG = "+UtilidadesHash.getString(miHash,"IDTIPOEJG")+" " +
			"   AND estado.ANIO = "+UtilidadesHash.getString(miHash,"ANIO")+" " +
			"   AND estado.NUMERO = "+UtilidadesHash.getString(miHash,"NUMERO")+" " ;
		if(miForm.getVerHistorico()!=null && miForm.getVerHistorico().equalsIgnoreCase(ClsConstants.DB_TRUE)){
			request.setAttribute("verHistorico", miForm.getVerHistorico());
			consulta += " ORDER BY ESTADO.FECHAMODIFICACION asc , ESTADO.IDESTADOPOREJG asc ";
		}else{
			request.setAttribute("verHistorico", ClsConstants.DB_FALSE);
			consulta += " and fechabaja is null "+
			" ORDER BY trunc(ESTADO.FECHAINICIO) asc, ESTADO.IDESTADOPOREJG asc";
		}
//			" ";
		
		try {
			GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
			
			
			String prefijoExpedienteCajg = paramAdm.getValor (idInstitucion, ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
			request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);
			
			v = admBean.selectGenerico (consulta);
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			boolean isConfiguradoEnvioPericles = ejgService.isColegioZonaComun(Short.valueOf(idInstitucion)) && ejgService.isColegioConfiguradoEnvioPericles(Short.valueOf(idInstitucion));
			if( v != null && v.size()>0) {
				//miramos si el ultimo estado es Listo remitir a comision. si es asi miramos el estado del ultimo envio de eCOM
				Hashtable fila = (Hashtable)v.get(v.size()-1);
				String idEstado = (String)fila.get(ScsEstadoEJGBean.C_IDESTADOEJG);
				if(isConfiguradoEnvioPericles && (Short.parseShort(idEstado) == AppConstants.ESTADOS_EJG.REMITIDO_COMISION.getCodigo() || Short.parseShort(idEstado) == AppConstants.ESTADOS_EJG.ACEPTADO_EXP_COMISION.getCodigo()) ) {
					fila.put("botonEnvio", "1");
				}
				
			}
			
			
			
			request.setAttribute ("resultado", v);
			request.getSession ().setAttribute ("accion", accion);
		} catch (Exception e) {
			   throwExcp ("messages.general.error", e, null);
		}
		
		return "inicio";
	} //abrir ()
	
	
}