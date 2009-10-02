package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsContrariosAsistenciaAdm;
import com.siga.beans.ScsContrariosAsistenciaBean;
import com.siga.beans.ScsDelitosAsistenciaAdm;
import com.siga.beans.ScsDelitosAsistenciaBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.certificados.Plantilla;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AsistenciasForm;
import com.siga.informes.InformeBusquedaAsistencias;


/**
 * @author carlos.vidal
 * @since 3/2/2005
 */

public class BusquedaAsistenciasLetradoAction extends MasterAction {

	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
							      HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
					String accion = miForm.getModo();
					
	//				 La primera vez que se carga el formulario 
					// Abrir
					if (accion == null || accion.equalsIgnoreCase("")){
						mapDestino = "inicio";					
					}else if (accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("generarCarta")){
						mapDestino = generarCarta(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("finalizarCarta")){
						mapDestino = finalizarCarta(mapping, miForm, request, response);
					} else if (accion.equalsIgnoreCase("buscarInit")){
						// buscarPersona
						borrarPaginador(request, paginadorPenstania);
						mapDestino = buscarPor(mapping, miForm, request, response);	
					}
					
					else {
						return super.executeInternal(mapping,
								      formulario,
								      request, 
								      response);
					}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}	
	
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		
		AsistenciasForm miForm =(AsistenciasForm)formulario;
		
		// RGG Obligo a que sea ficha colegial
		String esFichaColegial = "1";
		
		if ((esFichaColegial != null) && (esFichaColegial.equalsIgnoreCase("1"))) {
			return buscarPor(mapping, formulario, request, response);						
		}
		// DCG fin
		
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			CenPersonaAdm persona = new CenPersonaAdm(this.getUserBean(request));
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				request.setAttribute("nColegiado",numeroColegiado);
				request.setAttribute("nombreColegiado",persona.obtenerNombreApellidos(new Long(usr.getIdPersona()).toString()));
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		
		
		
			
			
		String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
		miForm.setIdPersonaColegiado(idPersona);
		//request.setAttribute("letrado",idPersona);
		
		String modoPestanha = 	(String)request.getSession().getAttribute("modoPestanha");
		
		// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
		request.setAttribute("MODOPESTANA", modoPestanha);
		
		
		return "inicio";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return null;
	}
	
	/**Funcion que transforma los datos de entrada para poder hacer la insercion a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se necesitan meter en BBDD
	 */
	protected Hashtable prepararHash (Hashtable datos){
		return datos;
	}
	
	
	/** 
	 *  Funcion que atiende la accion buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, 		
			  MasterForm formulario, 
			  HttpServletRequest request, 
			  HttpServletResponse response) throws ClsExceptions, SIGAException{

		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Vector vResultado = new Vector();
		String where =	" WHERE " + ScsAsistenciasBean.C_IDINSTITUCION + " = " + usr.getLocation();;
		if (usr.isLetrado())
		{
			// Solamente sus asistencias
			where += " AND " + ScsAsistenciasBean.C_IDPERSONACOLEGIADO + " = " + usr.getUserName();
		}
		
		ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
		try{
			vResultado=(Vector)asistencias.select(where);
			request.setAttribute("resultado",vResultado);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
        return "listado";
	}

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");

		//String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		Hashtable hash = new Hashtable();
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		hash.put("ANIO",ocultos.get(0));
		hash.put("NUMERO",ocultos.get(1));
		hash.put("MODO","editar");
		
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
		
		// CONCEPTO
		hash.put("conceptoE",PersonaJGAction.ASISTENCIA_ASISTIDO);
		// clave ASI
		hash.put("idInstitucionASI",usr.getLocation());
		hash.put("anioASI",ocultos.get(0));
		hash.put("numeroASI",ocultos.get(1));
		// titulo
		hash.put("tituloE","gratuita.mantAsistencias.literal.asistido");
		hash.put("localizacionE","gratuita.mantAsistencias.literal.localizacion");
		// accion
		hash.put("accionE","editar");
		// action
		hash.put("actionE","/JGR_AsistidoAsistenciaLetrado.do");
		
		hash.put("esFichaColegial","true");
		
		request.setAttribute("asistencia",hash);
		
		
		AsistenciasForm miForm = (AsistenciasForm) formulario;
		//String esFichaColegial = miForm.getEsFichaColegial();
		request.setAttribute("esFichaColegial","1");

		return "editar";
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions  
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
			ScsAsistenciasBean obj = (ScsAsistenciasBean)resultadoObj.get(0);
			if (obj.getIdPersonaJG()==null) {
				hash.put("idPersonaJG","");
			} else {
				hash.put("idPersonaJG",obj.getIdPersonaJG().toString());
			}
			hash.put("idInstitucionJG",usr.getLocation());
			hash.put("anioASI",miForm.getAnio());
			hash.put("numeroASI",miForm.getNumero());
		
		}
		else 		
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
			hash.put("esFichaColegial","true");
			
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		
		
		
		AsistenciasForm miForm =(AsistenciasForm)formulario;
		
			String nColegiado = (String)request.getParameter("nColegiado");
		if(nColegiado!=null)
			request.setAttribute("nColegiado",""+nColegiado);
		else{
		
	
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
		}
		String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
		
		miForm.setIdPersona(idPersona);
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
	/*protected String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		
			return "buscarPor";
	}*/

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
				return "refresh";
	}
	
	
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException  {

		HttpSession ses = request.getSession();
		UsrBean usr 	= (UsrBean)ses.getAttribute("USRBEAN");
		ScsAsistenciasAdm admAsistencia = new ScsAsistenciasAdm(this.getUserBean(request));
		ScsActuacionAsistenciaAdm actuaciones = new ScsActuacionAsistenciaAdm(this.getUserBean(request));
		ScsDelitosAsistenciaAdm delitos = new ScsDelitosAsistenciaAdm(this.getUserBean(request));
		ScsContrariosAsistenciaAdm contrarios = new ScsContrariosAsistenciaAdm(this.getUserBean(request));

		ScsCabeceraGuardiasAdm admCabeceraG = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
		ScsGuardiasColegiadoAdm admGuarciasColg = new ScsGuardiasColegiadoAdm(this.getUserBean(request));

		UserTransaction tx = null;
		Hashtable hash = null;
		boolean result = true;	

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0); 
			String anio 	= (String)ocultos.get(0);
			String numero 	= (String)ocultos.get(1);

//			Abrimos una transaccion ya que tenemos que eliminar registros en las tablas
//			SCS_EJG, SCS_ACTUACIONESASISTENCIA Y SCS_ASISTENCIA

//			Hay que comprobar si existen Expedientes de Justicia Gratuita para la asistencia

			String where="";
			Hashtable hashAsistencia = admAsistencia.getAsistencia(anio, numero, usr.getLocation());

			if(hashAsistencia!=null && 
					(hashAsistencia.get(ScsAsistenciasBean.C_EJGANIO)!=null && !hashAsistencia.get(ScsAsistenciasBean.C_EJGANIO).equals(""))
					&& (hashAsistencia.get(ScsAsistenciasBean.C_EJGNUMERO)!=null && !hashAsistencia.get(ScsAsistenciasBean.C_EJGNUMERO).equals(""))
					&& (hashAsistencia.get(ScsAsistenciasBean.C_EJGIDTIPOEJG)!=null && !hashAsistencia.get(ScsAsistenciasBean.C_EJGIDTIPOEJG).equals(""))  ) {
				request.setAttribute("sinrefresco","");
				request.setAttribute("mensaje","messages.deleted.errorEjg");
			}else { 
				if(hashAsistencia!=null && 
						(hashAsistencia.get(ScsAsistenciasBean.C_DESIGNA_ANIO)!=null && !hashAsistencia.get(ScsAsistenciasBean.C_DESIGNA_ANIO).equals(""))
						&& (hashAsistencia.get(ScsAsistenciasBean.C_DESIGNA_NUMERO)!=null && !hashAsistencia.get(ScsAsistenciasBean.C_DESIGNA_NUMERO).equals(""))
						&& (hashAsistencia.get(ScsAsistenciasBean.C_DESIGNA_TURNO)!=null && !hashAsistencia.get(ScsAsistenciasBean.C_DESIGNA_TURNO).equals(""))  ){
					request.setAttribute("sinrefresco","");
					request.setAttribute("mensaje","messages.deleted.errorDesigna");

				} else {
					tx = usr.getTransaction(); 
					tx.begin();	

//					Obtenemos la asistencia:


//					Eliminamos SCS_ACTUACIONESASITENCIA
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

//					Eliminamos scs_delitosasistencia
					if(result) {
						// Comprobamos si existen registros.
						where = " WHERE IDINSTITUCION = "+usr.getLocation()+" AND ANIO="+anio+" AND NUMERO="+numero;
						if(delitos.select(where).size()>0) {
							hash= new Hashtable();
							hash.put(ScsDelitosAsistenciaBean.C_IDINSTITUCION,usr.getLocation());
							hash.put(ScsDelitosAsistenciaBean.C_ANIO,			anio);
							hash.put(ScsDelitosAsistenciaBean.C_NUMERO,		numero);
							String [] claves = {ScsDelitosAsistenciaBean.C_IDINSTITUCION,
									ScsDelitosAsistenciaBean.C_ANIO,
									ScsDelitosAsistenciaBean.C_NUMERO};
							result = delitos.deleteDirect(hash,claves);
						}
					}

//					Eliminamos scs_contrariosasistencia
					if(result) {
						// Comprobamos si existen registros.
						where = " WHERE IDINSTITUCION = "+usr.getLocation()+" AND ANIO="+anio+" AND NUMERO="+numero;
						if(contrarios.select(where).size()>0) {
							hash= new Hashtable();
							hash.put(ScsContrariosAsistenciaBean.C_IDINSTITUCION,usr.getLocation());
							hash.put(ScsContrariosAsistenciaBean.C_ANIO,			anio);
							hash.put(ScsContrariosAsistenciaBean.C_NUMERO,		numero);
							String [] claves = {ScsContrariosAsistenciaBean.C_IDINSTITUCION,
									ScsContrariosAsistenciaBean.C_ANIO,
									ScsContrariosAsistenciaBean.C_NUMERO};
							result = contrarios.deleteDirect(hash,claves);
						}
					}

//					Eliminamos el asistido
					if(result) {

						String sqlDirect = " UPDATE SCS_ASISTENCIA SET IDPERSONAJG=NULL WHERE IDINSTITUCION = "+usr.getLocation()+" AND ANIO="+anio+" AND NUMERO="+numero;
						result = admAsistencia.deleteSQL(sqlDirect);
					}

//					Eliminamos SCS_ASISTENCIA
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
							result = admGuarciasColg.deleteGuardiasColegiado(hashCabecera);
							if(result){
								//2.- Eliminar SCS_CABECERAGUARDIAS por clave de Cabecera						
								result = admCabeceraG.deleteCabeceraGuardias(hashCabecera);
							}
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
			}
		} catch (Exception e) {
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return "exito";
	}

	
	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException  {


		AsistenciasForm miForm =(AsistenciasForm)formulario;
		String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
		miForm.setIdPersona(idPersona);
		//request.setAttribute("letrado",idPersona);
		
		String modoPestanha = 	(String)request.getSession().getAttribute("modoPestanha");
		
		// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
		request.setAttribute("MODOPESTANA", modoPestanha);
		
		
		
		request.setAttribute(ClsConstants.PARAM_PAGINACION,paginadorPenstania);
		try {
			HashMap databackup=getPaginador(request, paginadorPenstania);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				Vector datos=new Vector();
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	
				databackup=new HashMap();




				ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
				//vResultado=(Vector)asistencias.ejecutaSelect(sql);
				//request.setAttribute("resultado",vResultado);

				//idPersona = request.getParameter("idPersonaPestanha");
				PaginadorBind paginador = asistencias.getBusquedaAsistencias(miForm,idPersona);
				//Paginador paginador = new Paginador(sql);				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				if (totalRegistros==0){					
					paginador =null;
				}
				databackup.put("paginador",paginador);
				if (paginador!=null){ 
					Vector datos = paginador.obtenerPagina(1);
					databackup.put("datos",datos);
					setPaginador(request, paginadorPenstania, databackup);
				} 	

				//resultado = admBean.selectGenerico(consulta);
				//request.getSession().setAttribute("resultado",v);



			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 borrarPaginador(request, paginadorPenstania);
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "listado";
	}
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/*protected String buscarPorOld(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException  {
	    
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");

		try {
		
			// DCG ini
			boolean bEsFichaColegeial = true;
			String esFichaColegial = "1";

			String where = " AND A.IDINSTITUCION = " + usr.getLocation();
			Long idPers = new Long(request.getParameter("idPersonaPestanha"));
			where += " AND " + ScsAsistenciasBean.C_IDPERSONACOLEGIADO + " = " + idPers;
			where += " ORDER BY " + ScsAsistenciasBean.C_FECHAHORA;
			
			Vector vResultado = new Vector();
			String sql =
			"SELECT B.NOMBRE NOMBRETURNO, C.NOMBRE NOMBREGUARDIA, A.ANIO ANIO, A.NUMERO NUMERO, "+
			" A.FECHAHORA FECHAHORA, D.NOMBRE||' '||D.APELLIDO1||' '||D.APELLIDO2 NOMBRE, B.IDTURNO IDTURNO, C.IDGUARDIA IDGUARDIA, A.FACTURADO FACTURADO, A.IDFACTURACION IDFACTURACION, B." + ScsTurnoBean.C_LETRADOASISTENCIAS + ", " +
			
			" F_SIGA_ACTASIST_NOVALIDAR(a.idinstitucion,a.anio,a.numero) actnovalidadas"+
			
			" FROM SCS_ASISTENCIA A, SCS_TURNO B,  SCS_GUARDIASTURNO C, SCS_PERSONAJG D, CEN_COLEGIADO E "+
			" WHERE "+
			" B.IDTURNO = C.IDTURNO AND B.IDINSTITUCION = C.IDINSTITUCION AND "+
			" A.IDINSTITUCION = C.IDINSTITUCION AND A.IDTURNO = C.IDTURNO AND A.IDGUARDIA = C.IDGUARDIA AND "+
			" A.IDINSTITUCION = D.IDINSTITUCION(+) AND A.IDPERSONAJG = D.IDPERSONA(+) AND"+
			" A.IDPERSONACOLEGIADO = E.IDPERSONA AND A.IDINSTITUCION = E.IDINSTITUCION "+where;
			
        	ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
			vResultado=(Vector)asistencias.ejecutaSelect(sql);
			request.setAttribute("resultado",vResultado);
			
			// DCG ini
			if (bEsFichaColegeial) {
				request.setAttribute("esFichaColegial", esFichaColegial);						
			}
			// DCG fin
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
        return "listado";
	}
	*/
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String generarCarta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
	    
		try{
			AsistenciasForm miForm =(AsistenciasForm)formulario;
			
			// Guardo el formulario en sesión para poder fijar a quienes hay que enviar la carta
			Hashtable datos=miForm.getDatos();
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(this.getUserBean(request));
			datos.put("idioma",a.getLenguajeExt(this.getLenguaje(request)));
			request.getSession().setAttribute("DATABACKUP",datos);

		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
        return "recogidaDatos";
	}
	
	/**
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String finalizarCarta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  {
	    
		String resultado="exito";
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma = usr.getLanguage().toUpperCase();
		Vector vResultado= new Vector();
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;
			
		try {
			//obtener plantilla
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			//System.out.println("*************** 1: " + rp.returnProperty("sjcs.directorioFisicoCartaAsistenciaJava"));
			//System.out.println("*************** 2: " + rp.returnProperty("sjcs.directorioCartaAsistenciaJava"));
			//System.out.println("*************** 3: " + ClsConstants.FILE_SEP+institucion);
			
			String rutaPlantilla = Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoCartaAsistenciaJava")+rp.returnProperty("sjcs.directorioCartaAsistenciaJava"))+
					ClsConstants.FILE_SEP+institucion;

		    // RGG cambio de codigos 
		    String lenguajeExt ="es";
		    AdmLenguajesAdm al = new AdmLenguajesAdm(this.getUserBean(request));
		    lenguajeExt=al.getLenguajeExt(idioma);
		    
		    
		    String nombrePlantilla="plantillaCartaAsistencia_"+lenguajeExt+".fo";
		    InformeBusquedaAsistencias informe = new InformeBusquedaAsistencias(); 
		    String contenidoPlantilla = informe.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
			
		    //obtener la ruta de descarga
		    String rutaServidor =
		    	Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoSJCSJava")+rp.returnProperty("sjcs.directorioSJCSJava"))+
		    	ClsConstants.FILE_SEP+institucion;

		    rutaFin=new File(rutaServidor);
			if (!rutaFin.exists()){
				if(!rutaFin.mkdirs()){
					throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
				}
			}    
			String rutaServidorTmp=rutaServidor+ClsConstants.FILE_SEP+"tmp_asistencias_"+System.currentTimeMillis();
			rutaTmp=new File(rutaServidorTmp);
			if(!rutaTmp.mkdirs()){
				throw new SIGAException("facturacion.nuevoFichero.literal.errorAcceso");					
			}
			
		    //obtener los datos comunes
		    Hashtable datosComunes= this.obtenerDatosComunes(request);
			
			//buscar los registros seleccionados
			Hashtable miHash= (Hashtable) request.getSession().getAttribute("DATABACKUP");
		
			
			ScsAsistenciasAdm adm =  new ScsAsistenciasAdm(this.getUserBean(request));
			vResultado = adm.ejecutaSelect(this.obtenerConsultaCarta(institucion,miHash));
			
			if (vResultado!=null && !vResultado.isEmpty()){
				boolean correcto=true;
				Enumeration lista=vResultado.elements();
	    		
				while(correcto && lista.hasMoreElements()){
					Hashtable datosBase=(Hashtable)lista.nextElement();
					datosBase.putAll(datosComunes);
					File fPdf = informe.generarInforme(request,datosBase,rutaServidorTmp,contenidoPlantilla,rutaServidorTmp,"cartaAsistencia_" +numeroCarta);
					correcto=(fPdf!=null);
					if(correcto){
						ficherosPDF.add(fPdf);
						numeroCarta++;
					}
				}
				
				if(correcto){
					// Ubicacion de la carpeta donde se crean los ficheros PDF:
					String nombreFicheroZIP="cartasAsistencia_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
					String rutaServidorDescargasZip=rutaServidor + File.separator;
					
					Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF);
					request.setAttribute("nombreFichero", nombreFicheroZIP + ".zip");
					request.setAttribute("rutaFichero", rutaServidorDescargasZip+nombreFicheroZIP + ".zip");			
					request.setAttribute("borrarFichero", "true");			
					
					//resultado = "descargaFichero";				
					request.setAttribute("generacionOK","OK");			
				}else{
					request.setAttribute("generacionOK","ERROR");			
				}
				resultado = "descarga";
				
			}else{
				resultado=exitoModalSinRefresco("gratuita.retenciones.noResultados",request);
			}
			
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		} finally{ 
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return resultado;
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
			where +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"D.NOMBRE" );
		else
			if(nombre!=null && !nombre.equals("")) 	where+=" AND "+ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(),"D.NOMBRE" );

		if(ComodinBusquedas.hasComodin(apellido1))
			where +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(),"D.APELLIDO1");
		else
			if(apellido1!=null && !apellido1.equals("")) 	where+=" AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(),"D.APELLIDO1");
		
		if(ComodinBusquedas.hasComodin(apellido2))
			
			where +=" AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(),"D.APELLIDO2");
		else
			if(apellido2!=null && !apellido2.equals("")) 	where+=" AND "+ComodinBusquedas.prepararSentenciaCompleta(apellido2.trim(),"D.APELLIDO2");

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
	
	
	/**
	 * Este método reemplaza los valores comunes en las plantillas FO
	 * @param request Objeto HTTPRequest
	 * @param plantillaFO Plantilla FO con parametros 
	 * @return Plantilla FO en donde se han reemplazado los parámetros
	 * @throws ClsExceptions
	 */
	protected Hashtable obtenerDatosComunes(HttpServletRequest request) throws ClsExceptions{
		AsistenciasForm miForm =(AsistenciasForm)request.getSession().getAttribute("AsistenciasForm");
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		// RGG 26/02/2007 cambio en los codigos de lenguajes
		AdmLenguajesAdm a = new AdmLenguajesAdm(this.getUserBean(request));		
		String idioma = "es";
		try {
			idioma = a.getLenguajeExt(usr.getLanguage());
		} catch (Exception e) {
			
		}
		
		Hashtable datos= new Hashtable();
		UtilidadesHash.set(datos,"CABECERA_CARTA_EJG",miForm.getCabeceraCarta());
		UtilidadesHash.set(datos,"MOTIVO_CARTA_EJG",miForm.getMotivoCarta());
		UtilidadesHash.set(datos,"PIE_CARTA_EJG",miForm.getPieCarta());
		UtilidadesHash.set(datos,"FECHA",UtilidadesBDAdm.getFechaBD(""));
		UtilidadesHash.set(datos,"TEXTO_TRATAMIENTO_DESTINATARIO",UtilidadesString.getMensajeIdioma(idioma,"informes.cartaAsistencia.estimado"));
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");			
	    String rutaPlantilla = Plantilla.obtenerPathNormalizado(rp.returnProperty("sjcs.directorioFisicoCartaAsistenciaJava")+rp.returnProperty("sjcs.directorioCartaAsistenciaJava"))+ClsConstants.FILE_SEP+institucion;
	    UtilidadesHash.set(datos,"RUTA_LOGO",rutaPlantilla+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"logo.gif");

		return datos;
	 }
	

}