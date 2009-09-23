package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsIncompatibilidadGuardiasAdm;
import com.siga.beans.ScsIncompatibilidadGuardiasBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BusquedaIncompatibilidadesGuardiasForm;

/**
 * @author adrian.ayala
 * @since 10/03/2008
 */

public class BusquedaIncompatibilidadesGuardiasAction extends MasterAction
{
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo
	 *  del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping,
											 ActionForm formulario,
											 HttpServletRequest request,
											 HttpServletResponse response)
		throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward (mapDestino);
			}
			
			String accion = miForm.getModo();
			
			//La primera vez que se carga el formulario 
			// Abrir
			if (accion != null && accion.equalsIgnoreCase ("abrir")) {
				mapDestino = abrir
					(mapping, miForm, request, response);	
			}
			else if (accion == null || accion.equals ("")) {
				mapDestino = abrirAvanzada
					(mapping, miForm, request, response);							
			}
			else {
				return super.executeInternal
					(mapping, formulario, request, response);
			}
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)
			{
				if (miForm.getModal ().equalsIgnoreCase ("TRUE"))
				{
					request.setAttribute("exceptionTarget", "parent.modal");
				}
				
				throw new ClsExceptions ("El ActionMapping no puede ser nulo",
										 "", "0", "GEN00", "15");
			}
		}
		catch (SIGAException es) {
			throw es; 
		} 
		catch (Exception e) {
			throw new SIGAException ("messages.general.error", e,
									 new String[] {"modulo.facturacion"});
									 // o el recurso del modulo que sea 
		}
		
		return mapping.findForward (mapDestino);
	} //executeInternal ()
	
	/** 
	 *  Funcion que atiende la accion abrir.
	 *  Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
		throws ClsExceptions, SIGAException
	{
		return "inicio";
	} //abrir ()
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada
	 * 						(org.apache.struts.action.ActionMapping,
	 * 						 com.siga.general.MasterForm,
	 * 						 javax.servlet.http.HttpServletRequest,
	 * 						 javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada (ActionMapping mapping,
									MasterForm formulario,
									HttpServletRequest request,
									HttpServletResponse response)
		throws ClsExceptions, SIGAException
	{
		request.getSession ().removeAttribute ("busqueda");
		return "inicio";
	} //abrirAvanzada ()
	
	/**
	 * Funcion que transforma los datos de entrada
	 * para poder hacer la insercion a BBDD
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se necesitan meter en BBDD
	 */
	protected Hashtable prepararHash (Hashtable datos)
	{
		return datos;
	} //prepararHash ()
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor
	 * 						(org.apache.struts.action.ActionMapping,
	 * 						 com.siga.general.MasterForm,
	 * 						 javax.servlet.http.HttpServletRequest,
	 * 						 javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor (ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request,
								HttpServletResponse response)
		throws SIGAException
	{
		try {
			//Controles globales
			UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
			BusquedaIncompatibilidadesGuardiasForm miForm =
				(BusquedaIncompatibilidadesGuardiasForm) formulario;
			ScsIncompatibilidadGuardiasAdm incompatibilidadesAdm =
				new ScsIncompatibilidadGuardiasAdm (this.getUserBean (request));
			
			//Obtenemos los parametros del form
			String idTurno1					= miForm.getIdTurno ();
			String idGuardia1				= miForm.getIdGuardia ();
			String idTurno2					= miForm.getIdTurno_incompatible ();
			String idGuardia2				= miForm.getIdGuardia_incompatible ();
			boolean soloIncompatibilidades	= miForm.getSoloIncompatibilidades ().
				equals ("1")? true : false;
			
			//Guardamos los datos de la busqueda para el boton volver
			Hashtable busqueda = new Hashtable ();
			busqueda.put ("idTurno1", idTurno1);
			busqueda.put ("idGuardia1", idGuardia1);
			busqueda.put ("idTurno2", idTurno2);
			busqueda.put ("idGuardia2", idGuardia2);
			request.getSession ().setAttribute ("busqueda", busqueda);
			
			// Preparamos el where de la consulta
			Vector vResultado = new Vector ();
			String sql = incompatibilidadesAdm.buscarIncompatibilidades
				(usr.getLocation (), idTurno1, idGuardia1, idTurno2, idGuardia2);
			vResultado = (Vector) incompatibilidadesAdm.selectGenerico (sql);
			
			request.setAttribute ("resultado", vResultado);
			request.setAttribute ("soloIncompatibilidades", new Boolean (soloIncompatibilidades));
		}
		catch (Exception e) 
		{
			throw new SIGAException ("messages.general.error", e,
					new String[] {"modulo.gratuita"});
		}
		
        return "resultado";
	} //buscarPor ()

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar (ActionMapping mapping, MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
		throws ClsExceptions,SIGAException  
	{
		try {
			//Controles globales
			BusquedaIncompatibilidadesGuardiasForm miForm =
				(BusquedaIncompatibilidadesGuardiasForm) formulario;
			
			//Obtenemos los parametros del form
			String idTurno1				= miForm.getIdTurno ();
			String idGuardia1			= miForm.getIdGuardia ();
			String idTurno2				= miForm.getIdTurno_incompatible ();
			String idGuardia2			= miForm.getIdGuardia_incompatible ();
			String incompParaAnyadir	= miForm.getIncompParaAnyadir ();
			String incompParaQuitar		= miForm.getIncompParaQuitar ();
			String diasSeparacion		= miForm.getDiasseparacionguardias ();
			String motivos				= miForm.getMotivos ();
			
			//Guardamos los datos de la busqueda para el boton volver
			Hashtable busqueda = new Hashtable ();
			busqueda.put ("idTurno1", idTurno1);
			busqueda.put ("idGuardia1", idGuardia1);
			busqueda.put ("idTurno2", idTurno2);
			busqueda.put ("idGuardia2", idGuardia2);
			request.getSession ().setAttribute ("busqueda", busqueda);
			
			//Tratamos las incompatibilidades
			editarIncompatibilidades (incompParaAnyadir, incompParaQuitar,
					diasSeparacion, motivos, request);
		}
		catch (Exception e) 
		{
			throw new SIGAException ("messages.general.error", e,
					new String[] {"modulo.gratuita"});
		}
		
		return this.exitoRefresco("messages.updated.success", request);
	} //editar ()
	
	private void editarIncompatibilidades (String s_incompParaAnyadir,
										   String s_incompParaQuitar,
										   String s_diasSeparacion,
										   String s_motivos,
										   HttpServletRequest request)
		throws ClsExceptions,SIGAException  
	{
		UserTransaction tx = null;
		
		try {
			//Controles globales
			UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
			ScsIncompatibilidadGuardiasAdm incompatibilidadesAdm =
				new ScsIncompatibilidadGuardiasAdm (this.getUserBean (request));
			
			//Variables
			ScsIncompatibilidadGuardiasBean listaCampos;
			String listaCampos_sql;
			
			//obteniendo las incompatibilidades a partir de los conjuntos de campos
			String [] incompParaAnyadir = s_incompParaAnyadir.split (";");
			String [] incompParaQuitar = s_incompParaQuitar.split (";");
			String [] incompatibilidad;
			
			//borrando las incompatibilidades que hay que borrar
			listaCampos_sql = "";
			//iniciando la transaccion antes de borrar
			tx = usr.getTransaction ();
			tx.begin ();
			if (s_incompParaQuitar!=null && !s_incompParaQuitar.equals("")){
				for (int i=0; i<incompParaQuitar.length; i++)
				{
					incompatibilidad = incompParaQuitar [i].split (",");
					
					if (incompatibilidad.length > 1)
					{
						if (listaCampos_sql.length () > 0)
							listaCampos_sql += ",";
						listaCampos_sql += "(";
						listaCampos_sql += incompatibilidad[0] + ",";
						listaCampos_sql += incompatibilidad[1] + ",";
						listaCampos_sql += incompatibilidad[2] + ",";
						listaCampos_sql += incompatibilidad[3];
						listaCampos_sql += ")";
						
						listaCampos_sql += ",";
						listaCampos_sql += "(";
						listaCampos_sql += incompatibilidad[2] + ",";
						listaCampos_sql += incompatibilidad[3] + ",";
						listaCampos_sql += incompatibilidad[0] + ",";
						listaCampos_sql += incompatibilidad[1];
						listaCampos_sql += ")";
					}
				}
				
				incompatibilidadesAdm.deleteIncompatibilidadesGuardias
				(usr.getLocation (), listaCampos_sql);
			}
			
			
			
			
			
			//borrando e insertando las incompatibilidades que hay que anyadir
			if (s_incompParaAnyadir!=null && !s_incompParaAnyadir.equals("")){
				for (int i=0; i<incompParaAnyadir.length; i++)
				{
					incompatibilidad = incompParaAnyadir [i].split (",");
					
					if (incompatibilidad.length > 1)
					{
						//en una direccion (de la bidireccion)
						listaCampos = new ScsIncompatibilidadGuardiasBean ();
						listaCampos.setIdInstitucion (new Integer (usr.getLocation ()));
						
						listaCampos.setIdTurno (new Integer (incompatibilidad[0]));
						listaCampos.setIdGuardia (new Integer (incompatibilidad[1]));
						listaCampos.setIdTurnoIncompatible (new Integer (incompatibilidad[2]));
						listaCampos.setIdGuardiaIncompatible (new Integer (incompatibilidad[3]));
						incompatibilidadesAdm.delete (listaCampos); //borrando
						listaCampos.setMotivos (s_motivos);
						listaCampos.setDiasseparacionguardias (new Integer (s_diasSeparacion));
						incompatibilidadesAdm.insert (listaCampos); //insertando
						
						//en la otra direccion (de la bidireccion)
						listaCampos = new ScsIncompatibilidadGuardiasBean ();
						listaCampos.setIdInstitucion (new Integer (usr.getLocation ()));
						
						listaCampos.setIdTurnoIncompatible (new Integer (incompatibilidad[0]));
						listaCampos.setIdGuardiaIncompatible (new Integer (incompatibilidad[1]));
						listaCampos.setIdTurno (new Integer (incompatibilidad[2]));
						listaCampos.setIdGuardia (new Integer (incompatibilidad[3]));
						incompatibilidadesAdm.delete (listaCampos); //borrando
						listaCampos.setMotivos (s_motivos);
						listaCampos.setDiasseparacionguardias (new Integer (s_diasSeparacion));
						incompatibilidadesAdm.insert (listaCampos); //insertando
					}
				}
			}
			
			//finalizando la transaccion
			tx.commit ();
			request.setAttribute ("mensaje","messages.deleted.success");
		}
		catch (Exception e) 
		{
			throwExcp ("messages.general.error", e, tx);
		}
	} //editarIncompatibilidades ()

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
/*	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions  
	{
		
		Hashtable hash = new Hashtable();
		AsistenciasForm miForm = (AsistenciasForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		String desdeDesigna = miForm.getDesdeDesigna();
		String desdeEJG = request.getParameter("desdeEJG");
		if ((desdeDesigna != null) && (desdeDesigna.equalsIgnoreCase("si"))) {
			UtilidadesHash.set(hash, "ANIO", miForm.getAnio());
			UtilidadesHash.set(hash, "NUMERO", miForm.getNumero());

			// 03-04-2006 RGG cambio en ventanas de Personas JG
			// Persona JG
			Hashtable miHash = new Hashtable();
			miHash.put("IDINSTITUCION",usr.getLocation());
			miHash.put("ANIO",miForm.getAnio());
			miHash.put("NUMERO",miForm.getNumero());
			ScsAsistenciasAdm admi = new ScsAsistenciasAdm(this.getUserBean(request)); 
			Vector resultadoObj = admi.selectByPK(miHash);
			
			ScsAsistenciasBean obj = null;
			if (resultadoObj!=null && !resultadoObj.isEmpty()) {
				obj = (ScsAsistenciasBean)resultadoObj.get(0);
				if (obj.getIdPersonaJG()==null) {
					hash.put("idPersonaJG","");
				} else {
					hash.put("idPersonaJG",obj.getIdPersonaJG().toString());
				}				
			} else
				hash.put("idPersonaJG","");
			
			hash.put("idInstitucionJG",usr.getLocation());
			hash.put("anioASI",miForm.getAnio());
			hash.put("numeroASI",miForm.getNumero());		
		} else 		
		if ((desdeEJG != null) && (desdeEJG.equalsIgnoreCase("si"))) {
			UtilidadesHash.set(hash, "ANIO", miForm.getAnio());
			UtilidadesHash.set(hash, "NUMERO", miForm.getNumero());

			// 03-04-2006 RGG cambio en ventanas de Personas JG
			// Persona JG
			Hashtable miHash = new Hashtable();
			miHash.put("IDINSTITUCION",usr.getLocation());
			miHash.put("ANIO",miForm.getAnio());
			miHash.put("NUMERO",miForm.getNumero());
			ScsAsistenciasAdm admi = new ScsAsistenciasAdm(this.getUserBean(request)); 
			Vector resultadoObj = admi.selectByPK(miHash);
			ScsAsistenciasBean obj = (ScsAsistenciasBean)resultadoObj.get(0);
			if (obj.getIdPersonaJG()==null) {
				hash.put("idPersonaJG","");
			} else {
				hash.put("idPersonaJG",obj.getIdPersonaJG().toString());
			}
			hash.put("idInstitucionJG",usr.getLocation());
			hash.put("anioASI",miForm.getAnio());
			hash.put("numeroASI",miForm.getNumero());
			
		
		} else {
			Vector ocultos = miForm.getDatosTablaOcultos(0); 
			hash.put("ANIO",ocultos.get(0));
			hash.put("NUMERO",ocultos.get(1));

			// 03-04-2006 RGG cambio en ventanas de Personas JG
			// Persona JG
			Hashtable miHash = new Hashtable();
			miHash.put("IDINSTITUCION",usr.getLocation());
			miHash.put("ANIO",ocultos.get(0));
			miHash.put("NUMERO",ocultos.get(1));
			ScsAsistenciasAdm admi = new ScsAsistenciasAdm(this.getUserBean(request)); 
			Vector resultadoObj = admi.selectByPK(miHash);
			ScsAsistenciasBean obj = (ScsAsistenciasBean)resultadoObj.get(0);
			if (obj.getIdPersonaJG()==null) {
				hash.put("idPersonaJG","");
			} else {
				hash.put("idPersonaJG",obj.getIdPersonaJG().toString());
			}
			hash.put("idInstitucionJG",usr.getLocation());
			
			hash.put("anioASI",ocultos.get(0));
			hash.put("numeroASI",ocultos.get(1));
			
		}
		// CONCEPTO
		hash.put("conceptoE",PersonaJGAction.ASISTENCIA_ASISTIDO);
		// clave ASI
		hash.put("idInstitucionASI",usr.getLocation());
		// titulo
		hash.put("tituloE","gratuita.mantAsistencias.literal.asistido");
		hash.put("localizacionE","gratuita.mantAsistencias.literal.localizacion");
		// accion
		hash.put("accionE","ver");
		// action
		hash.put("actionE","/JGR_AsistidoAsistencia.do");

		hash.put("MODO","ver");

		
		request.setAttribute("asistencia",hash);	
		return "editar";
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
/*	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				request.setAttribute("nColegiado",numeroColegiado);
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		return "nuevo";
	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
/*	protected String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		
			return "exito";
	}

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
/*	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
				return "refresh";
	}

	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
/*	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws SIGAException  {
			
	HttpSession ses = request.getSession();
	UsrBean usr 	= (UsrBean)ses.getAttribute("USRBEAN");
	ScsAsistenciasAdm admAsistencia = new ScsAsistenciasAdm(this.getUserBean(request));
	ScsActuacionAsistenciaAdm actuaciones = new ScsActuacionAsistenciaAdm(this.getUserBean(request));
	
	ScsCabeceraGuardiasAdm admCabeceraG = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
	ScsGuardiasColegiadoAdm admGuarciasColg = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
	
	UserTransaction tx = null;
	Hashtable hash = null;
	boolean result = true;	
	
	try {
			Vector ocultos = formulario.getDatosTablaOcultos(0); 
			String anio 	= (String)ocultos.get(0);
			String numero 	= (String)ocultos.get(1);
			
			// Abrimos una transaccion ya que tenemos que eliminar registros en las tablas
			// SCS_EJG, SCS_ACTUACIONESASISTENCIA Y SCS_ASISTENCIA

			// Hay que comprobar si existen Expedientes de Justicia Gratuita para la asistencia
			ScsEJGAdm ejg = new ScsEJGAdm(this.getUserBean(request));
			String where = " WHERE IDINSTITUCION="+usr.getLocation()+" AND"+
							" ANIO = "+anio+
							" AND ASISTENCIA_NUMERO ="+numero;
			Vector vEjg = ejg.select(where);
			
			if(vEjg!=null && vEjg.size()>0) {
				request.setAttribute("mensaje","messages.deleted.errorEjg");
			} else {
				tx = usr.getTransaction(); 
				tx.begin();	
				
				//Obtenemos la asistencia:
				Hashtable hashAsistencia = admAsistencia.getAsistencia(anio, numero, usr.getLocation());
				
				// Eliminamos SCS_ACTUACIONESASITENCIA
				if(result) {
					// Comprobamos si existen registros.
					where = " WHERE IDINSTITUCION = "+usr.getLocation()+" AND ANIO="+anio+" AND NUMERO="+numero;
					if(actuaciones.selectTabla(where).size()>0) {
						hash= new Hashtable();
						hash.put(ScsActuacionAsistenciaBean.C_IDINSTITUCION,usr.getLocation());
						hash.put(ScsActuacionAsistenciaBean.C_ANIO,			anio);
						hash.put(ScsActuacionAsistenciaBean.C_NUMERO,		numero);
						String [] claves = {ScsActuacionAsistenciaBean.C_IDINSTITUCION,
								ScsActuacionAsistenciaBean.C_ANIO,
								ScsActuacionAsistenciaBean.C_NUMERO};
						result = actuaciones.deleteDirect(hash,claves);
					}
				}
				
				// Eliminamos SCS_ASISTENCIA
				if(result) {
					hash= new Hashtable();
					hash.put(ScsAsistenciasBean.C_ANIO,anio);
					hash.put(ScsAsistenciasBean.C_NUMERO,numero);
					hash.put(ScsAsistenciasBean.C_IDINSTITUCION,usr.getLocation());
					result = admAsistencia.delete(hash);
				}
				
				if (result) {			
					//Obtener Cabecera de guardia a la que pertenece la Asistencia (AsistenciaAdm.getCabeceraGuardia)
					Hashtable hashCabecera = admAsistencia.getCabeceraGuardia(hashAsistencia);	
					
					if(hashCabecera!=null && admCabeceraG.esGuardiaPresencialEliminable(hashCabecera)){
						//1.- Eliminar todas las SCS_GUARDIASCOLEGIADO por clave de Cabecera
						admGuarciasColg.deleteGuardiasColegiado(hashCabecera);
						
						//2.- Eliminar SCS_CABECERAGUARDIAS por clave de Cabecera						
						admCabeceraG.deleteCabeceraGuardias(hashCabecera);
					}
				}
				
				if (result) {
					request.setAttribute("mensaje","messages.deleted.success");
					tx.commit();
				} else {
					request.setAttribute("mensaje","messages.deleted.error");
					tx.rollback();
				}
			}
		} catch (Exception e) {
			throwExcp("error.messages.buscar",e,tx); 
		} 
        return "exito";
	}
	
	protected String obtenerConsultaCarta(String institucion, Hashtable filtros)throws ClsExceptions{
		String idioma 		= (String)filtros.get("idioma");
		String anio 		= (String)filtros.get("anio");
		String numero 		= (String)filtros.get("numero");
		String fechaDesde 	= (String)filtros.get("fechaDesde");
		String fechaHasta 	= (String)filtros.get("fechaHasta");
		String idTurno 		= (String)filtros.get("idTurno");
		String idGuardia 	= (String)filtros.get("idGuardia");
		String nColegiado 	= (String)filtros.get("colegiado");
		String tAsistencia 			= (String)filtros.get("idTipoAsistencia");
		String tAsistenciaColegio 	= (String)filtros.get("idTipoAsistenciaColegio");
		String nif 			= (String)filtros.get("nif");
		String nombre 		= (String)filtros.get("nombre");
		String apellido1 	= (String)filtros.get("apellido1");
		String apellido2 	= (String)filtros.get("apellido2");
		
		// Se construye la select
		String where = " AND A.IDINSTITUCION = "+institucion;
		if(anio!=null && !anio.equals("")) 			where+=" AND ANIO = "+anio;
		if(numero!=null && !numero.equals("")) 			where+=" AND NUMERO = "+numero;
		if(idTurno!=null && !idTurno.equals("")) 		where+=" AND B.IDTURNO = "+idTurno;
		if(idGuardia!=null && !idGuardia.equals("")) 		where+=" AND C.IDGUARDIA = "+idGuardia;
		if(nColegiado!=null && !nColegiado.equals("")) 		where+=" AND E.NCOLEGIADO = "+nColegiado;
		if(tAsistencia!=null && !tAsistencia.equals("")) 	where+=" AND A.IDTIPOASISTENCIA = "+tAsistencia;
		if(tAsistenciaColegio!=null && !tAsistenciaColegio.equals("")) where+=" AND A.IDTIPOASISTENCIACOLEGIO = "+tAsistenciaColegio;
		if(fechaDesde!=null && !fechaDesde.equals("") && !fechaHasta.equals(""))
			where+=" AND FECHAHORA >= TO_DATE('"+GstDate.getApplicationFormatDate(idioma,fechaDesde)+"','YYYY/MM/DD hh24:mi:ss') AND FECHAHORA <= TO_DATE('"+GstDate.getApplicationFormatDate(idioma,fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
		else if(fechaDesde!=null && !fechaDesde.equals(""))
			where+=" AND FECHAHORA >= TO_DATE('"+GstDate.getApplicationFormatDate(idioma,fechaDesde)+"','YYYY/MM/DD hh24:mi:ss')";
		else if(fechaHasta!=null && !fechaHasta.equals(""))
			where+=" AND FECHAHORA <= TO_DATE('"+GstDate.getApplicationFormatDate(idioma,fechaHasta)+"','YYYY/MM/DD hh24:mi:ss')";
		if(nif!=null && !nif.equals("")) 	where+=" AND UPPER(D.NIF) = '"+nif.toUpperCase()+"'";
		// Comodines Nombre y Apellidos
		if(ComodinBusquedas.hasComodin(nombre))
			where +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"D.NOMBRE");
		else
			if(nombre!=null && !nombre.equals("")) 	where+=" AND UPPER(D.NOMBRE) = '"+nombre.toUpperCase()+"'";

		if(ComodinBusquedas.hasComodin(apellido1))
			where +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(),"D.APELLIDO1");
		else
			if(apellido1!=null && !apellido1.equals("")) 	where+=" AND UPPER(D.APELLIDO1) = '"+apellido1.toUpperCase()+"'";
		
		if(ComodinBusquedas.hasComodin(apellido2))
			where +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(),"D.APELLIDO2");
		else
			if(apellido2!=null && !apellido2.equals("")) 	where+=" AND UPPER(D.APELLIDO2) = '"+apellido2.toUpperCase()+"'";

		String sql =
		"SELECT A.ANIO, A.NUMERO, to_char(A.FECHAHORA,'DD/MM/YYYY') FECHAHORA,"+
		" B.IDTURNO, B.NOMBRE TURNO, C.IDGUARDIA, C.NOMBRE GUARDIA, A.OBSERVACIONES, "+
		" A.IDPERSONAJG IDASISTIDO, "+
		" D.NIF NIF_ASISTIDO, "+
		" D.APELLIDO1||' '||D.APELLIDO2||', '||D.NOMBRE NOMBRE_ASISTIDO, "+
		" D.DIRECCION||' '||D.CODIGOPOSTAL||', '||E.NOMBRE DIRECCION_ASISTIDO, "+
		" A.IDPERSONACOLEGIADO IDLETRADO "+
		" FROM SCS_ASISTENCIA A, SCS_TURNO B,  SCS_GUARDIASTURNO C, SCS_PERSONAJG D, CEN_POBLACIONES E "+
		" WHERE "+
		" B.IDTURNO = C.IDTURNO AND B.IDINSTITUCION = C.IDINSTITUCION AND "+
		" A.IDINSTITUCION = C.IDINSTITUCION AND A.IDTURNO = C.IDTURNO AND A.IDGUARDIA = C.IDGUARDIA AND "+
		" D.IDPOBLACION=E.IDPOBLACION (+) and"+
		" A.IDINSTITUCION = D.IDINSTITUCION(+) AND A.IDPERSONAJG = D.IDPERSONA(+) "+where;
		return sql;
	}	
*/
}