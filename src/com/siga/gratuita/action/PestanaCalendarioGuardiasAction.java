package com.siga.gratuita.action;


import java.util.Hashtable;
import java.util.Map;
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
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsPermutaCabeceraAdm;
import com.siga.beans.ScsPermutaCabeceraBean;
import com.siga.beans.ScsPermutaGuardiasAdm;
import com.siga.beans.ScsPermutaGuardiasBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirPermutaGuardiasForm;
;/**
 * Maneja las acciones que se pueden realizar sobre las tablas SCS_PERMUTAGUARDIAS. <br>
 * Implementa la parte de control del caso de uso de Definir Permuta de Guardias.
 * 
 * @author david.sanchezp
 * @since 10/2/2005 
 * @version 1.0
 */
public class PestanaCalendarioGuardiasAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}
			else if(miForm.getModo().equalsIgnoreCase("sustituir"))
			{
				return mapping.findForward(sustituir(mapping, miForm, request, response));
			}
			else if(miForm.getModo().equalsIgnoreCase("insertarSustitucion"))
			{
				return mapping.findForward(insertarSustitucion(mapping, miForm, request, response));
			}
			else return super.executeInternal(mapping, formulario, request, response);
		} catch (SIGAException e) {
			throw e;
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
		}
	}
	
	private String insertarSustitucion(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		DefinirPermutaGuardiasForm permutasForm = (DefinirPermutaGuardiasForm)miForm;
		
		//--------------------------------------------------------------------------------------------
		// Obtencion de datos del form. Saliente indica al letrado que va a ser sustituido y entrante el que va
		// a sustituir al saliente
		//-----------------------------------------------------------------------------------------------
		
		String idInstitucion = permutasForm.getIdInstitucion();
		
		String idTurno = permutasForm.getIdTurno();
		String idGuardia = permutasForm.getIdGuardia();
		String idCalendarioGuardias = permutasForm.getIdCalendarioGuardias();
		String idPersonaSaliente = permutasForm.getIdPersonaSolicitante();
		String fechaInicio = permutasForm.getFechaInicio();
		String fechaFin = permutasForm.getFechaFin();
		String idPersonaEntrante = permutasForm.getIdPersona();		
		String salto = request.getParameter("checkSalto");
		String compensacion = request.getParameter("checkCompensacion");
		String sustituta = permutasForm.getSustituta(); //nos indica si el letrado proviene de una guardia de sustitucion
		String comenSustitucion = permutasForm.getComenSustitucion();
		UserTransaction tx = null;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		try {
			
			CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usr);
			Map<String,CenBajasTemporalesBean> mBajasTemporales =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(idPersonaEntrante), new Integer(idInstitucion));
			if(mBajasTemporales.containsKey(fechaInicio))
				throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
				
			
			
			
			tx=usr.getTransaction();
			
			ScsGuardiasColegiadoAdm guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
			
			
			String mensaje = guardiasColegiadoAdm.validacionesSustitucionGuardia(usr,idInstitucion, idTurno, idGuardia, fechaInicio,fechaFin,idPersonaEntrante, idPersonaSaliente);
			if(!mensaje.equalsIgnoreCase("OK"))
			{
				return exito(mensaje,request);
			}
			else
			{
				tx.begin();
				guardiasColegiadoAdm.sustitucionLetradoGuardiaPuntual(usr, request,idInstitucion, idTurno,idGuardia,idCalendarioGuardias,idPersonaSaliente,fechaInicio,fechaFin,idPersonaEntrante,salto,compensacion,sustituta,comenSustitucion);
				tx.commit();
			}
			
		}catch (SIGAException e) {
			request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));	
			return "errorConAviso"; 
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoModal("messages.updated.success",request);
	}

	private String sustituir(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		DefinirPermutaGuardiasForm permutasForm = (DefinirPermutaGuardiasForm)miForm;
		ScsTurnoAdm admTurno = new ScsTurnoAdm(this.getUserBean(request));
		ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		
		String nombreTurno = "", nombreGuardia = "";
		try {
			
			
			permutasForm.setIdInstitucion(permutasForm.getIdInstitucion());
			
			permutasForm.setIdTurno(permutasForm.getIdTurno()); 
			permutasForm.setIdGuardia(permutasForm.getIdGuardia());
			permutasForm.setIdCalendarioGuardias(permutasForm.getIdCalendarioGuardias());
			
			String descripTurnoSelect="SELECT " + ScsTurnoBean.C_NOMBRE + " FROM " + ScsTurnoBean.T_NOMBRETABLA +
									  " WHERE " + ScsTurnoBean.C_IDINSTITUCION + "=" +permutasForm.getIdInstitucion() +
									  "   AND " + ScsTurnoBean.C_IDTURNO + "=" + permutasForm.getIdTurno();
			Vector v = admTurno.ejecutaSelect(descripTurnoSelect);
			if(v != null && v.size() != 0)
				nombreTurno= (String)((Hashtable)v.get(0)).get(ScsTurnoBean.C_NOMBRE);
			
			String descripGuardiaSelect="SELECT " + ScsGuardiasTurnoBean.C_NOMBRE + " FROM " + ScsGuardiasTurnoBean.T_NOMBRETABLA +
			  							" WHERE " + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=" +permutasForm.getIdInstitucion() +
			  							"   AND " + ScsGuardiasTurnoBean.C_IDTURNO + "=" + permutasForm.getIdTurno()+
			  							"   AND " + ScsGuardiasTurnoBean.C_IDGUARDIA + "=" + permutasForm.getIdGuardia();
			Vector v2 = admGuardias.ejecutaSelect(descripGuardiaSelect);
			if(v2 != null && v2.size() != 0)
				nombreGuardia= (String)((Hashtable)v2.get(0)).get(ScsGuardiasTurnoBean.C_NOMBRE);
			
			request.setAttribute("nombreTurno",nombreTurno);
			request.setAttribute("nombreGuardia",nombreGuardia);
			request.setAttribute("fechaInicio",permutasForm.getFechaInicio());
			request.setAttribute("fechaFin",permutasForm.getFechaFin());
			request.setAttribute("origen","CALENDARIOGUARDIAS");
			
			request.setAttribute("idTurno",permutasForm.getIdTurno());
			request.setAttribute("idGuardia",permutasForm.getIdGuardia());
			request.setAttribute("idPersona",permutasForm.getIdPersona());
			
			permutasForm.setIdPersona("");
			
			request.setAttribute("action","/JGR_PestanaCalendarioGuardias.do");
			
		}
			catch (Exception e) {
				throwExcp("messages.select.error",e,null);
			}
			
			return "sustituir";
	}

	/**
	 * Hago la busqueda de la Pestanha del Calendario de Guardias 
	 *
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirPermutaGuardiasForm miForm = (DefinirPermutaGuardiasForm)formulario;
		ScsPermutaGuardiasAdm admPermuta = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		
		Vector registros = new Vector();
		String idInstitucion="", idPersona="", orden="";
		UsrBean usr;
		String numero = "";
		String nombre = "";
		String estado = "";
		CenColegiadoBean datosColegiales;		
		
			try {
				//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
				if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
					// Preparo para obtener la informacion del colegiado:
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
					CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
					
					try {
						Long idPers = new Long(request.getParameter("idPersonaPestanha"));
						Integer idInstPers = new Integer(request.getParameter("idInstitucionPestanha"));
						CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
			
						// Obtengo la informacion del colegiado:
						nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
						datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
						numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
						estado = clienteAdm.getEstadoColegial(String.valueOf(idPers), String.valueOf(idInstPers));
					} catch (Exception e1){
						nombre = miForm.getNombreColegiadoPestanha();
						numero = miForm.getNumeroColegiadoPestanha();
						estado = clienteAdm.getEstadoColegial(miForm.getIdPersona(), miForm.getIdInstitucion());
					}
				}
				// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
				request.setAttribute("NOMBRECOLEGPESTA�A", nombre);
				request.setAttribute("NUMEROCOLEGPESTA�A", numero);
				request.setAttribute("ESTADOCOLEGIAL", estado);
				
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

				//Recupero los datos de la pestanha:				
				idInstitucion = usr.getLocation();
				idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
				orden = miForm.getOrden()==null?"FECHA":miForm.getOrden();
				
				//------------------
				//DATOS SOLICITANTE
				//------------------
				//Hago la busqueda de los datos necesarios de esa guardia, los guardo en la hash mando en request:
				
				
				
				registros.clear();
				
				//Miramos si los colegiados tienen permiso para confirmar cabecera de guardias
				GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
				//Haria falta meter los parametros en con ClsConstants
				String validaGuardiasColegiado = paramAdm.getValor (usr.getLocation (), "SCS", ClsConstants.GEN_PARAM_VALIDA_GUARDIAS_COLEGIADO, "");
				miForm.setValidaGuardiasColegiado(validaGuardiasColegiado);
				// Miramos tambien el parametro VALIDA_VOLANTE
				String validarVolante = paramAdm.getValor (usr.getLocation (), "SCS", ClsConstants.GEN_PARAM_VALIDAR_VOLANTE, "");
				request.setAttribute(ClsConstants.GEN_PARAM_VALIDAR_VOLANTE, validarVolante);
				
				registros = admPermuta.selectGenerico(admPermuta.buscarSolicitantesPermuta(idInstitucion,idPersona,orden,usr));
				//Almaceno en el request el vector con los datos de la tabla:
				request.setAttribute("resultados",registros);
				
				//Datos comunes de la Pestanha:
				request.setAttribute("IDINSTITUCION",idInstitucion);
				request.setAttribute("IDPERSONA",idPersona);	
				
				
			}
			catch (Exception e) {
				throwExcp("messages.select.error",e,null);
			}		

			return "inicio";
	}
	
	/**
	 * No implementado 
	 *
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		return mapSinDesarrollar;		
	}

	/**
	 * No implentada
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		return mapSinDesarrollar;
	}

	/**
	 * No implmentada
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {		
		return mapSinDesarrollar;
	}

	/**
	 * No implementado 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
			return mapSinDesarrollar;
	}

	/**
	 * Inserta una permuta	 
	 *  
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 * @throws ClsExceptions
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usr);
		DefinirPermutaGuardiasForm miForm = (DefinirPermutaGuardiasForm) formulario;
		ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(usr);
		ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(usr);
		ScsGuardiasColegiadoAdm admGuardias = new ScsGuardiasColegiadoAdm(usr);
		ScsPermutaCabeceraAdm admPermutasCabeceras = new ScsPermutaCabeceraAdm(usr);
		ScsPermutaGuardiasAdm admPermutas = new ScsPermutaGuardiasAdm(usr);									
		ScsPermutaGuardiasBean beanPermutasGuardias = new ScsPermutaGuardiasBean();		
		
		String forward = "exito";
		
		UserTransaction tx = null;
		Hashtable miHash = new Hashtable();
		Hashtable solicitanteGuardiaHash = new Hashtable();
		Hashtable solicitanteCGHash = new Hashtable();
		Hashtable confirmadorGuardiaHash = new Hashtable();
		Hashtable confirmadorCGHash = new Hashtable();		
		String numero = "", texto = "";
				
		try {
			String sFechaInicioSolicitante = GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaInicioSolicitante());
			String sFechaInicioConfirmador = GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaInicioConfirmador());						
			
			//comprobamos que el solicitante no esta de vacaciones la fecha que del confirmador
			Map<String,CenBajasTemporalesBean> mBajasTemporalesSolicitante =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(miForm.getIdPersonaSolicitante()), new Integer(miForm.getIdInstitucion()));
			if( mBajasTemporalesSolicitante.containsKey(miForm.getFechaInicioConfirmador()))
				throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
			
			//comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
			Map<String,CenBajasTemporalesBean> mBajasTemporalesConfirmador =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(miForm.getIdPersonaConfirmador()), new Integer(miForm.getIdInstitucion()));
			if(mBajasTemporalesConfirmador.containsKey(miForm.getFechaInicioSolicitante()))
				throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
			
			//-----------------------------------------------------------------
			//Inicio de la transaccion
			//-----------------------------------------------------------------		
			tx=usr.getTransaction();
			tx.begin();				
			
			//Calculamos el nuevo identificador numero de la tabla scs_permutaguardias:
			numero = admPermutas.getNuevoNumero(miForm.getIdInstitucion());			

			//-------------------------------------------------------------
			//Recuperamos los datos de la permuta
			//-------------------------------------------------------------
			//Datos del solicitante:
			miHash.put(ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE,miForm.getIdTurnoSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE,miForm.getIdGuardiaSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE,miForm.getIdPersonaSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN,miForm.getIdCalendarioSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE, sFechaInicioSolicitante);			
			miHash.put(ScsPermutaGuardiasBean.C_MOTIVOS_SOLICITANTE,miForm.getMotivosSolicitante());
			
			// Consulta SCS_GUARDIASCOLEGIADO solicitante
			solicitanteGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			solicitanteGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDTURNO,miForm.getIdTurnoSolicitante());
			solicitanteGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,miForm.getIdGuardiaSolicitante());
			solicitanteGuardiaHash.put(ScsGuardiasColegiadoBean.C_FECHAINICIO, sFechaInicioSolicitante);
			solicitanteGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDPERSONA,miForm.getIdPersonaSolicitante());			
			Vector guardiasColegSolic = admGuardias.select(solicitanteGuardiaHash);
			
			// Consulta SCS_CABECERAGUARDIAS solicitante
			solicitanteCGHash.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			solicitanteCGHash.put(ScsCabeceraGuardiasBean.C_IDTURNO,miForm.getIdTurnoSolicitante());
			solicitanteCGHash.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,miForm.getIdGuardiaSolicitante());
			solicitanteCGHash.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO, sFechaInicioSolicitante);
			solicitanteCGHash.put(ScsCabeceraGuardiasBean.C_IDPERSONA,miForm.getIdPersonaSolicitante());					
			Vector cabeceraGuarSolicitante = admCabeceraGuardias.selectByPK(solicitanteCGHash);

			
			//Datos del confirmador:			
			miHash.put(ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR,miForm.getIdTurnoConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR,miForm.getIdGuardiaConfirmador());		
			miHash.put(ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR,miForm.getIdPersonaConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD,miForm.getIdCalendarioConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR, sFechaInicioConfirmador);					
			
			// Consulta SCS_GUARDIASCOLEGIADO confirmador
			confirmadorGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			confirmadorGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDTURNO,miForm.getIdTurnoConfirmador());
			confirmadorGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,miForm.getIdGuardiaConfirmador());
			confirmadorGuardiaHash.put(ScsGuardiasColegiadoBean.C_FECHAINICIO, sFechaInicioConfirmador);
			confirmadorGuardiaHash.put(ScsGuardiasColegiadoBean.C_IDPERSONA,miForm.getIdPersonaConfirmador());
			Vector guardiasColegiadoConfirmador = admGuardias.select(confirmadorGuardiaHash);
			
			// Consulta SCS_CABECERAGUARDIAS confirmador
			confirmadorCGHash.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			confirmadorCGHash.put(ScsCabeceraGuardiasBean.C_IDTURNO,miForm.getIdTurnoConfirmador());
			confirmadorCGHash.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,miForm.getIdGuardiaConfirmador());
			confirmadorCGHash.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO, sFechaInicioConfirmador);
			confirmadorCGHash.put(ScsCabeceraGuardiasBean.C_IDPERSONA,miForm.getIdPersonaConfirmador());			
			Vector cabeceraGuarConfirmador = admCabeceraGuardias.selectByPK(confirmadorCGHash);
			
			
			/*
			 * JPT: Aqui se comprueba las siguientes cuestiones:
			 * 1. Que tenga los mismos datos del solicitante que cuando se ha realizado la operacion.
			 * 2. Que tenga los mismos datos del confirmador que cuando se ha realizado la operacion.
			 * 3. Que no exista una permuta con los datos que se estan utilizando pendiente
			 */
			if (cabeceraGuarSolicitante==null || cabeceraGuarSolicitante.size()==0 || 
					cabeceraGuarConfirmador==null || cabeceraGuarConfirmador.size()==0 || 
					admPermutas.existeSolicitanteCompradorPermutadosPendiente((ScsCabeceraGuardiasBean)(cabeceraGuarSolicitante.elementAt(0)), (ScsCabeceraGuardiasBean)(cabeceraGuarConfirmador.elementAt(0)))) {
				request.setAttribute("sinrefresco","sinrefresco");				
				request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "error.messages.noupdated"));	
				return "exito"; 
			}
			
			ScsCabeceraGuardiasBean bCabeceraGuarSolicitante = (ScsCabeceraGuardiasBean) cabeceraGuarSolicitante.elementAt(0);
			ScsCabeceraGuardiasBean bCabeceraGuarConfirmador = (ScsCabeceraGuardiasBean) cabeceraGuarConfirmador.elementAt(0);
			
			//Antes de insertar el registro se comprueba si el letrado ya tiene una guardia en ese turno y periodo
			if (admCabeceraGuardias.validaGuardiaLetradoPeriodo(bCabeceraGuarSolicitante.getIdInstitucion(), bCabeceraGuarSolicitante.getIdTurno(), bCabeceraGuarSolicitante.getIdGuardia(), bCabeceraGuarSolicitante.getIdPersona(), miForm.getFechaInicioConfirmador(), miForm.getFechaFinConfirmador()))
				throw new SIGAException("gratuita.calendarios.guardias.mensaje.existe");
			
			if (admCabeceraGuardias.validaGuardiaLetradoPeriodo(bCabeceraGuarConfirmador.getIdInstitucion(), bCabeceraGuarConfirmador.getIdTurno(), bCabeceraGuarConfirmador.getIdGuardia(), bCabeceraGuarConfirmador.getIdPersona(), miForm.getFechaInicioSolicitante(), miForm.getFechaFinSolicitante()))
				throw new SIGAException("gratuita.calendarios.guardias.mensaje.existe");
			
			//Datos comunes:
			miHash.put(ScsPermutaGuardiasBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			miHash.put(ScsPermutaGuardiasBean.C_FECHASOLICITUD,"SYSDATE");
			miHash.put(ScsPermutaGuardiasBean.C_NUMERO,numero);
			miHash.put(ScsPermutaGuardiasBean.C_ANULADA,"0");	
						
			if (!usr.isLetrado()) {				
				beanPermutasGuardias.setIdInstitucion(new Integer(miForm.getIdInstitucion()));	
				
				// Trato los datos del solicitante, miro si existe porque habria que hacer un tratamiento
				Hashtable hashPermutasCabeceras = new Hashtable();
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDINSTITUCION, miForm.getIdInstitucion());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDTURNO, miForm.getIdTurnoSolicitante());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDGUARDIA, miForm.getIdGuardiaSolicitante());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS, miForm.getIdCalendarioSolicitante());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDPERSONA, miForm.getIdPersonaSolicitante());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_FECHA, sFechaInicioSolicitante);
				
				Vector vPermutasCabeceras = admPermutasCabeceras.select(hashPermutasCabeceras);				
				if (vPermutasCabeceras != null && vPermutasCabeceras.size() > 0) {				
					ScsPermutaCabeceraBean beanPermutasCabeceras = (ScsPermutaCabeceraBean)(vPermutasCabeceras.elementAt(0));
					beanPermutasGuardias.setIdPermutaCabeceraSolicitante(beanPermutasCabeceras.getIdPermutaCabecera());
					miHash.put(ScsPermutaGuardiasBean.C_IDPERCAB_SOLICITANTE, beanPermutasCabeceras.getIdPermutaCabecera());
				} else {
					beanPermutasGuardias.setIdPermutaCabeceraSolicitante(null);
				}
				
				// Trato los datos del confirmador, miro si existe porque habria que hacer un tratamiento
				hashPermutasCabeceras = new Hashtable();
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDINSTITUCION, miForm.getIdInstitucion());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDTURNO, miForm.getIdTurnoConfirmador());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDGUARDIA, miForm.getIdGuardiaConfirmador());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDCALENDARIOGUARDIAS, miForm.getIdCalendarioConfirmador());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_IDPERSONA, miForm.getIdPersonaConfirmador());
				hashPermutasCabeceras.put(ScsPermutaCabeceraBean.C_FECHA, sFechaInicioConfirmador);
				
				vPermutasCabeceras = admPermutasCabeceras.select(hashPermutasCabeceras);				
				if (vPermutasCabeceras != null && vPermutasCabeceras.size() > 0) {				
					ScsPermutaCabeceraBean beanPermutasCabeceras = (ScsPermutaCabeceraBean)(vPermutasCabeceras.elementAt(0));
					beanPermutasGuardias.setIdPermutaCabeceraConfirmador(beanPermutasCabeceras.getIdPermutaCabecera());
					miHash.put(ScsPermutaGuardiasBean.C_IDPERCAB_CONFIRMADOR, beanPermutasCabeceras.getIdPermutaCabecera());
				} else {
					beanPermutasGuardias.setIdPermutaCabeceraConfirmador(null);
				}
				
				// Hacemos las modificaciones previas a SCS_PERMUTA_CABECERA
				if ((beanPermutasGuardias.getIdPermutaCabeceraSolicitante()!=null && beanPermutasGuardias.getIdPermutaCabeceraSolicitante()>0) || 
					(beanPermutasGuardias.getIdPermutaCabeceraConfirmador()!=null && beanPermutasGuardias.getIdPermutaCabeceraConfirmador()>0)) {					
						if (!admPermutasCabeceras.modificarPrevioPermutasCalendario(beanPermutasGuardias)) {
							throw new ClsExceptions(admPermutas.getError());
						}
				}
																
				// Borramos los registros de guardias colegiado correspondiente al solicitante
				for (int i=0;i<guardiasColegSolic.size();i++){
					ScsGuardiasColegiadoBean guardiasColSolBean = (ScsGuardiasColegiadoBean)(guardiasColegSolic.elementAt(i));	
				
					if(!admGuardias.delete(guardiasColSolBean))
						throw new ClsExceptions(admGuardias.getError());
				}	
			
				// Borramos los registros de guardias colegiado correspondientes al confirmador
				for (int i=0;i<guardiasColegiadoConfirmador.size();i++){
					ScsGuardiasColegiadoBean guardiasColConfBean = (ScsGuardiasColegiadoBean)(guardiasColegiadoConfirmador.elementAt(i));	
				
					if (!admGuardias.delete(guardiasColConfBean))
						throw new ClsExceptions(admGuardias.getError());
				}	

				//Borramos el registro de cabecera de guardias correspondiente al solicitante
				ScsCabeceraGuardiasBean cabecerasGuarSol = (ScsCabeceraGuardiasBean)(cabeceraGuarSolicitante.elementAt(0));
				int posicionConfirmador = cabecerasGuarSol.getPosicion();
				
				if (!admCabeceraGuardias.delete(cabecerasGuarSol))
					throw new ClsExceptions(admCabeceraGuardias.getError());
				
				//Borramos el registro de cabecera de guardias correspondiente al confirmador
				ScsCabeceraGuardiasBean cabecerasGuarConf = (ScsCabeceraGuardiasBean)(cabeceraGuarConfirmador.elementAt(0));
				int posicionSolicitante = cabecerasGuarConf.getPosicion();
				if (!admCabeceraGuardias.delete(cabecerasGuarConf))
					throw new ClsExceptions(admCabeceraGuardias.getError());	
				
				cabecerasGuarSol.setIdPersona(new Long(miForm.getIdPersonaConfirmador()));
				cabecerasGuarSol.setLetradoSustituido(new Long(miForm.getIdPersonaSolicitante()));
				cabecerasGuarSol.setFechaSustitucion(GstDate.getApplicationFormatDate(usr.getLanguage(),UtilidadesBDAdm.getFechaBD(usr.getLanguage())));
				cabecerasGuarSol.setComenSustitucion(UtilidadesString.getMensajeIdioma(usr.getLanguage(),"gratuita.literal.letrado.permuta"));
		
				
				cabecerasGuarConf.setIdPersona(new Long(miForm.getIdPersonaSolicitante()));
				cabecerasGuarConf.setLetradoSustituido(new Long(miForm.getIdPersonaConfirmador()));
				cabecerasGuarConf.setFechaSustitucion(GstDate.getApplicationFormatDate(usr.getLanguage(),UtilidadesBDAdm.getFechaBD(usr.getLanguage())));
				cabecerasGuarConf.setComenSustitucion(UtilidadesString.getMensajeIdioma(usr.getLanguage(),"gratuita.literal.letrado.permuta"));
				

				//-----------------------------------------------------------------------------------------------------------
				// Insertamos los registros de cabecera de guardias con el id persona cambiado entre confirmador y solicitante
				//----------------------------------------------------------------------------------------------------------
				cabecerasGuarSol.setFechaAlta("SYSDATE");
				cabecerasGuarConf.setFechaAlta("SYSDATE");
				cabecerasGuarSol.setUsuAlta(new Integer(usr.getUserName()));
				cabecerasGuarConf.setUsuAlta(new Integer(usr.getUserName()));
				cabecerasGuarSol.setPosicion(posicionConfirmador);
				cabecerasGuarConf.setPosicion(posicionSolicitante);
				
				
				if(!admCabeceraGuardias.insert((cabecerasGuarSol)))
					throw new ClsExceptions(admCabeceraGuardias.getError());
				
				if(!admCabeceraGuardias.insert((cabecerasGuarConf)))
					throw new ClsExceptions(admCabeceraGuardias.getError());
				
				//-------------------------------------------------------------------------------------------------------
				// Insertamos los registros de guardias colegiado para confirmador y solicitante intercambiando los idpersona
				//--------------------------------------------------------------------------------
				for(int i=0;i<guardiasColegiadoConfirmador.size();i++){
					ScsGuardiasColegiadoBean guardiasColConfBean = (ScsGuardiasColegiadoBean)(guardiasColegiadoConfirmador.elementAt(i));
					guardiasColConfBean.setIdPersona(new Long(miForm.getIdPersonaSolicitante()));
					if(!admGuardias.insert(guardiasColConfBean))
						throw new ClsExceptions(admGuardias.getError());
				}
				
				for(int i=0;i<guardiasColegSolic.size();i++){
					ScsGuardiasColegiadoBean guardiasColSolBean = (ScsGuardiasColegiadoBean)(guardiasColegSolic.elementAt(i));
					guardiasColSolBean.setIdPersona(new Long(miForm.getIdPersonaConfirmador()));
					if(!admGuardias.insert(guardiasColSolBean))
						throw new ClsExceptions(admGuardias.getError());
				}
				
				//------------------------------------------------------------------------------------------------------
				//  Insertamos la permuta pero intercambiando las fechas de inicio entre confirmador y solicitante
				// y poniendo como fecha de confirmacion la fecha actual
				//------------------------------------------------------------------------------------------------------
			
				beanPermutasGuardias.setIdCalendarioGuardiasConfirmad(new Integer(miForm.getIdCalendarioSolicitante()));				
				beanPermutasGuardias.setFechaInicioConfirmador(sFechaInicioSolicitante);
				
				beanPermutasGuardias.setIdCalendarioGuardiasSolicitan(new Integer(miForm.getIdCalendarioConfirmador()));
				beanPermutasGuardias.setFechaInicioSolicitante(sFechaInicioConfirmador);
				beanPermutasGuardias.setMotivosConfirmador(null);			
				beanPermutasGuardias.setFechaConfirmacion("SYSDATE");
				
				// Creo la permuta de SCS_PERMUTASGUARDIAS y hacemos las modificaciones posteriores a SCS_PERMUTA_CABECERA
				if (!admPermutasCabeceras.modificarPosteriorPermutasCalendario(beanPermutasGuardias))
					throw new ClsExceptions(admPermutas.getError());	
				
				// Datos que se insertaran en la permuta
				miHash.put(ScsPermutaGuardiasBean.C_FECHACONFIRMACION,"SYSDATE");
				
				//Datos del solicitante:
				miHash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN, new Integer(miForm.getIdCalendarioConfirmador()));
				miHash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE, sFechaInicioConfirmador);
				
				//Datos del confirmador:			
				miHash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD, new Integer(miForm.getIdCalendarioSolicitante()));
				miHash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR, sFechaInicioSolicitante);
				miHash.put(ScsPermutaGuardiasBean.C_MOTIVOS_CONFIRMADOR,"");
				
				String idInstitucion = miForm.getIdInstitucion();
				String Idturno = miForm.getIdTurnoSolicitante();
				String idGuardia = miForm.getIdGuardiaSolicitante();
				String idPersonaSolicitante = miForm.getIdPersonaSolicitante();
				String fechaSolicitante = miForm.getFechaInicioSolicitante();
				String idPersonaConfirmador = miForm.getIdPersonaConfirmador();
				String fechaConfirmador = miForm.getFechaInicioConfirmador();
				
				// Cambia las asistencias del solicitante al confirmador
				if (!admAsistencias.updateAsistenciasAccionesGuardias(idInstitucion, Idturno, idGuardia, idPersonaSolicitante, fechaSolicitante, idPersonaConfirmador)) {
					throw new ClsExceptions(admAsistencias.getError());
				}
				
				// Cambia las asistencias del confirmador al solicitante
				if (!admAsistencias.updateAsistenciasAccionesGuardias(idInstitucion, Idturno, idGuardia, idPersonaConfirmador, fechaConfirmador, idPersonaSolicitante)) {
					throw new ClsExceptions(admAsistencias.getError());
				}
						
			} else {
				miHash.put(ScsPermutaGuardiasBean.C_MOTIVOS_CONFIRMADOR,miForm.getMotivosSolicitante());
			}
			
			// Insertamos la permuta
			if (admPermutas.insert(miHash)) {
				String resultado[] = EjecucionPLs.ejecutarPL_CrearPermutasCabeceras(miForm.getIdInstitucion(), numero);
				
				if (!resultado[0].equalsIgnoreCase("0")) {
					//Caso de error
					texto="messages.inserted.error";
					request.setAttribute("sinrefresco","sinrefresco");
					tx.rollback();
					//throw new Exception(texto);
				} else {				
					//Caso de exito
					texto="messages.inserted.success";
					request.setAttribute("modal","1");
				}
			} else {
				//Caso de error
				texto="messages.inserted.error";
				request.setAttribute("sinrefresco","sinrefresco");
				tx.rollback();
				//throw new Exception(texto);
			}
			
			
			tx.commit();
			
			request.setAttribute("mensaje",texto);
			forward = "exito";
			//-----------------------------------------------------------------
			//Fin de la transaccion
			//-----------------------------------------------------------------
		}catch (SIGAException e) {
			request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));	
			return "errorConAviso"; 
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return forward;
	}

	/**
	 * Modifica un registro de las permutas actualizando la fecha de confirmacion con la fecha actual.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 * 
	 * ESTE METODO CONFIRMA LA PERMUTA
	 * 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirPermutaGuardiasForm miForm = (DefinirPermutaGuardiasForm) formulario;
		ScsPermutaGuardiasAdm admPermutas = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		ScsPermutaCabeceraAdm admPermutasCabeceras = new ScsPermutaCabeceraAdm(this.getUserBean(request));	
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
		ScsGuardiasColegiadoAdm guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(this.getUserBean(request));
		
		UsrBean usr;
		UserTransaction tx = null;
		Hashtable cabeceraGuarSol = new Hashtable();
		Hashtable cabeceraGuarConf = new Hashtable();
		Hashtable guardiasColSol = new Hashtable();
		Hashtable guardiasColConf = new Hashtable();
		Hashtable permuta = new Hashtable();
		
		String idInstitucion = miForm.getIdInstitucion();
		
		String idTurnoSolicitante = miForm.getIdTurnoSolicitante();
		String idGuardiaSolicitante = miForm.getIdGuardiaSolicitante();
		String idCalendarioGuardiasSolicitante = miForm.getIdCalendarioSolicitante();
		String idPersonaSolicitante = miForm.getIdPersonaSolicitante();
		String fechaInicioSolicitante = miForm.getFechaInicioSolicitante();
		
		String idTurnoConfirmador = miForm.getIdTurnoConfirmador();
		String idGuardiaConfirmador = miForm.getIdGuardiaConfirmador();
		String idCalendarioGuardiasConfirmador = miForm.getIdCalendarioConfirmador();
		String idPersonaConfirmador = miForm.getIdPersonaConfirmador();
		String fechaInicioConfirmador = miForm.getFechaInicioConfirmador();
		String motivoConfirmador = miForm.getMotivosConfirmador();
		
		String numeroPermuta = miForm.getNumero();
		
				
		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx=usr.getTransaction();

			
			
			//-----------------------------------------------------------------------
			// 		Recuperameos los datos de cabeceraguardias y guardias colegiado tanto para el solicitante
			//	como para el confirmador. Lo hacemos por que posteriormente realizaremos el delete de dichos 
			//	registros que tendremos que volver a insertar pero con el idpersona cambiado entre solicitante
			//  y confirmador. Tambien obtenemos la permuta en funcion de los datos de confirmador y solicitante
			//  tambi�n por el mismo motivo anterior
			//-------------------------------------------------------------------------------
			
			cabeceraGuarSol.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,idInstitucion);
			cabeceraGuarSol.put(ScsCabeceraGuardiasBean.C_IDTURNO,idTurnoSolicitante);
			cabeceraGuarSol.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,idGuardiaSolicitante);
			cabeceraGuarSol.put(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardiasSolicitante);
			cabeceraGuarSol.put(ScsCabeceraGuardiasBean.C_IDPERSONA,idPersonaSolicitante);
			cabeceraGuarSol.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechaInicioSolicitante);
			
			Vector cabeceraGuarSolic = cabeceraGuardiasAdm.selectByPK(cabeceraGuarSol);
			
			cabeceraGuarConf.put(ScsCabeceraGuardiasBean.C_IDINSTITUCION,idInstitucion);
			cabeceraGuarConf.put(ScsCabeceraGuardiasBean.C_IDTURNO,idTurnoConfirmador);
			cabeceraGuarConf.put(ScsCabeceraGuardiasBean.C_IDGUARDIA,idGuardiaConfirmador);
			cabeceraGuarConf.put(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS,idCalendarioGuardiasConfirmador);
			cabeceraGuarConf.put(ScsCabeceraGuardiasBean.C_IDPERSONA,idPersonaConfirmador);
			cabeceraGuarConf.put(ScsCabeceraGuardiasBean.C_FECHA_INICIO,fechaInicioConfirmador);
			
			Vector cabeceraGuarConfir = cabeceraGuardiasAdm.selectByPK(cabeceraGuarConf);
			
			guardiasColSol.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
			guardiasColSol.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurnoSolicitante);
			guardiasColSol.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardiaSolicitante);
			guardiasColSol.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersonaSolicitante);
			guardiasColSol.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,fechaInicioSolicitante);
			
			Vector guardiasColegSolic = guardiasColegiadoAdm.select(guardiasColSol);

			guardiasColConf.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,idInstitucion);
			guardiasColConf.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurnoConfirmador);
			guardiasColConf.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardiaConfirmador);
			guardiasColConf.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersonaConfirmador);
			guardiasColConf.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,fechaInicioConfirmador);
			
			Vector guardiasColegConfir = guardiasColegiadoAdm.select(guardiasColConf);
			
			permuta.put(ScsPermutaGuardiasBean.C_IDINSTITUCION,idInstitucion);
			permuta.put(ScsPermutaGuardiasBean.C_NUMERO,numeroPermuta);
			
			Vector permutaActual = admPermutas.selectByPK(permuta);			
			ScsPermutaGuardiasBean beanPermuta = (ScsPermutaGuardiasBean)(permutaActual.elementAt(0));
			Integer idPermutaCabeceraSolicitante = beanPermuta.getIdPermutaCabeceraSolicitante();
			Integer idPermutaCabeceraConfirmador = beanPermuta.getIdPermutaCabeceraConfirmador();
			
			//-----------------------------------------------------------------
			//Inicio de la transaccion. Primero vamos a borrar los registros de la tabla SCS_PERMUTASGUARDIAS, 
			// SCS_GUARDIASCOLEGIADO Y SCS_CABECERAGUARDIAS tanto para el confirmador como para el solicitante.
			//-----------------------------------------------------------------				
			tx.begin();			
			
			// Borramos la permuta de SCS_PERMUTASGUARDIAS y hacemos las modificaciones previas a SCS_PERMUTA_CABECERA
			if (!admPermutas.delete(beanPermuta) || 
				!admPermutasCabeceras.modificarPrevioPermutasCalendario(beanPermuta))
				throw new ClsExceptions(admPermutas.getError());
			
			// Borramos los registros de guardias colegiado correspondiente al solicitante
			for(int i=0;i<guardiasColegSolic.size();i++){
				ScsGuardiasColegiadoBean guardiasColSolBean = (ScsGuardiasColegiadoBean)(guardiasColegSolic.elementAt(i));
				if(!guardiasColegiadoAdm.delete(guardiasColSolBean))
					throw new ClsExceptions(guardiasColegiadoAdm.getError());
			}
			
			// Borramos los registros de guardias colegiado correspondientes al confirmador
			for(int i=0;i<guardiasColegConfir.size();i++){
				ScsGuardiasColegiadoBean guardiasColConfBean = (ScsGuardiasColegiadoBean)(guardiasColegConfir.elementAt(i));
				if(!guardiasColegiadoAdm.delete(guardiasColConfBean))
					throw new ClsExceptions(guardiasColegiadoAdm.getError());
			}
			
			//Borramos el registro de cabecera de guardias correspondiente al solicitante
			ScsCabeceraGuardiasBean cabecerasGuarSol = (ScsCabeceraGuardiasBean)(cabeceraGuarSolic.elementAt(0));
			if(!cabeceraGuardiasAdm.delete(cabecerasGuarSol))
				throw new ClsExceptions(cabeceraGuardiasAdm.getError());
			
			//Borramos el registro de cabecera de guardias correspondiente al confirmador
			ScsCabeceraGuardiasBean cabecerasGuarConf = (ScsCabeceraGuardiasBean)(cabeceraGuarConfir.elementAt(0));
			if(!cabeceraGuardiasAdm.delete((cabecerasGuarConf)))
				throw new ClsExceptions(cabeceraGuardiasAdm.getError());
			
			//-----------------------------------------------------------------------------------------------------------
			// Insertamos los registros de cabecera de guardias con el id persona cambiado entre confirmador y solicitante
			//
			//----------------------------------------------------------------------------------------------------------
			
			cabecerasGuarSol.setIdPersona(new Long(idPersonaConfirmador));
			cabecerasGuarConf.setIdPersona(new Long(idPersonaSolicitante));
			cabecerasGuarSol.setFechaAlta("SYSDATE");
			cabecerasGuarConf.setFechaAlta("SYSDATE");
			cabecerasGuarSol.setUsuAlta(new Integer(usr.getUserName()));
			cabecerasGuarConf.setUsuAlta(new Integer(usr.getUserName()));
			
			if(!cabeceraGuardiasAdm.insert((cabecerasGuarConf)))
				throw new ClsExceptions(cabeceraGuardiasAdm.getError());
			if(!cabeceraGuardiasAdm.insert((cabecerasGuarSol)))
				throw new ClsExceptions(cabeceraGuardiasAdm.getError());
			
			//-------------------------------------------------------------------------------------------------------
			//
			// Insertamos los registros de guardias colegiado para confirmador y solicitante intercambiando los idpersona
			//--------------------------------------------------------------------------------
			
			for(int i=0;i<guardiasColegConfir.size();i++){
				ScsGuardiasColegiadoBean guardiasColConfBean = (ScsGuardiasColegiadoBean)(guardiasColegConfir.elementAt(i));
				guardiasColConfBean.setIdPersona(new Long(idPersonaSolicitante));
				if(!guardiasColegiadoAdm.insert(guardiasColConfBean))
					throw new ClsExceptions(guardiasColegiadoAdm.getError());
			}
			
			for(int i=0;i<guardiasColegSolic.size();i++){
				ScsGuardiasColegiadoBean guardiasColSolBean = (ScsGuardiasColegiadoBean)(guardiasColegSolic.elementAt(i));
				guardiasColSolBean.setIdPersona(new Long(idPersonaConfirmador));
				if(!guardiasColegiadoAdm.insert(guardiasColSolBean))
					throw new ClsExceptions(guardiasColegiadoAdm.getError());
			}
			
			//------------------------------------------------------------------------------------------------------
			//  Insertamos la permuta pero intercambiando las fechas de inicio entre confirmador y solicitante
			// y poniendo como fecha de confirmacion la fecha actual
			//------------------------------------------------------------------------------------------------------
			
			beanPermuta.setIdCalendarioGuardiasConfirmad(new Integer(idCalendarioGuardiasSolicitante));
			beanPermuta.setIdCalendarioGuardiasSolicitan(new Integer(idCalendarioGuardiasConfirmador));
			beanPermuta.setFechaInicioConfirmador(fechaInicioSolicitante);
			beanPermuta.setFechaInicioSolicitante(fechaInicioConfirmador);
			beanPermuta.setMotivosConfirmador(motivoConfirmador);			
			beanPermuta.setFechaConfirmacion("SYSDATE");
			
			// Creo la permuta de SCS_PERMUTASGUARDIAS y hacemos las modificaciones posteriores a SCS_PERMUTA_CABECERA
			if (!admPermutasCabeceras.modificarPosteriorPermutasCalendario(beanPermuta) ||
				!admPermutas.insert(beanPermuta))
				throw new ClsExceptions(admPermutas.getError());	
			
			String resultado[] = EjecucionPLs.ejecutarPL_CrearPermutasCabeceras(idInstitucion, numeroPermuta);				
			if (!resultado[0].equalsIgnoreCase("0")) {
				throw new ClsExceptions(resultado[1]);
			}							
			
			// Cambia las asistencias del solicitante al confirmador
			String sFechaInicioFormateadaSolicitante = GstDate.getFormatedDateShort(usr.getLanguage(), fechaInicioSolicitante);
			if (!admAsistencias.updateAsistenciasAccionesGuardias(idInstitucion, idTurnoSolicitante, idGuardiaSolicitante, idPersonaSolicitante, sFechaInicioFormateadaSolicitante, idPersonaConfirmador)) {
				throw new ClsExceptions(admAsistencias.getError());
			}
			
			// Cambia las asistencias del confirmador al solicitante
			String sFechaInicioFormateadaConfirmador = GstDate.getFormatedDateShort(usr.getLanguage(), fechaInicioConfirmador);
			if (!admAsistencias.updateAsistenciasAccionesGuardias(idInstitucion, idTurnoConfirmador, idGuardiaConfirmador, idPersonaConfirmador, sFechaInicioFormateadaConfirmador, idPersonaSolicitante)) {
				throw new ClsExceptions(admAsistencias.getError());
			}			
			
			tx.commit();
			
			
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return exitoModal("messages.updated.success",request);
	}		

	/**
	 * 
	 * No implementado
	 * 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirPermutaGuardiasForm miForm = (DefinirPermutaGuardiasForm) formulario;
		ScsPermutaGuardiasAdm admPermutas = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		ScsPermutaCabeceraAdm admPermutasCabeceras = new ScsPermutaCabeceraAdm(this.getUserBean(request));	
		
		String forward = "exito";
		UsrBean usr;
		UserTransaction tx = null;
		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		String numero = "", texto = "";
		boolean poolRW = false;	
				
		try {
			//-------------------------------------------------------------
			//Recuperamos los datos de la permuta
			//-------------------------------------------------------------
			//Datos del solicitante:
			miHash.put(ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE,miForm.getIdTurnoSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE,miForm.getIdGuardiaSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN,miForm.getIdCalendarioSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE,miForm.getIdPersonaSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE,miForm.getFechaInicioSolicitante());
			miHash.put(ScsPermutaGuardiasBean.C_MOTIVOS_SOLICITANTE,miForm.getMotivosSolicitante());			
			//Datos del confirmador:
			miHash.put(ScsPermutaGuardiasBean.C_IDTURNO_CONFIRMADOR,miForm.getIdTurnoConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_IDGUARDIA_CONFIRMADOR,miForm.getIdGuardiaConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_CONFIRMAD,miForm.getIdCalendarioConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_IDPERSONA_CONFIRMADOR,miForm.getIdPersonaConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_FECHAINICIO_CONFIRMADOR,miForm.getFechaInicioConfirmador());
			miHash.put(ScsPermutaGuardiasBean.C_MOTIVOS_CONFIRMADOR,miForm.getMotivosConfirmador());
			//Datos comunes:
			miHash.put(ScsPermutaGuardiasBean.C_IDINSTITUCION,miForm.getIdInstitucion());
			miHash.put(ScsPermutaGuardiasBean.C_FECHACONFIRMACION,"SYSDATE");
			miHash.put(ScsPermutaGuardiasBean.C_NUMERO,miForm.getNumero());
			miHash.put(ScsPermutaGuardiasBean.C_FECHASOLICITUD,miForm.getFechaSolicitud());
			miHash.put(ScsPermutaGuardiasBean.C_ANULADA,miForm.getAnulada());
			
			//-----------------------------------------------------------------
			//Inicio de la transaccion
			//-----------------------------------------------------------------		
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx=usr.getTransaction();
			tx.begin();			
												
			// JPT: 
			// 1. Eliminamos la permuta que no ha sido confirmada 
			// 2. Revisamos los registros de la tabla SCS_PERMUTA_CABECERA, y borramos los que no tengan relaciones con SCS_PERMUTAGUARDIAS
			if (admPermutas.delete(miHash) && admPermutasCabeceras.revisarPermutasCalendario(miForm.getIdInstitucion())) {
				//Caso de exito
				texto="messages.deleted.success";
				request.setAttribute("modal","1");
				
			} else {
				//Caso de error
				texto="messages.deleted.error";
				request.setAttribute("sinrefresco","sinrefresco");
				tx.rollback();
			}
			tx.commit();
			
			request.setAttribute("mensaje",texto);
			forward = "exito";
			//-----------------------------------------------------------------
			//Fin de la transaccion
			//-----------------------------------------------------------------
		}
		catch (Exception e){
			throwExcp("messages.deleted.error",e,tx);
		}		
		return forward;
	}


	/**
	 * Vamos a la modal de Cambiar de la Permuta 
	 * Su funcionamiento es el mismo que el m�todo nuevo() s�lo que recoge los datos de forma distinta ya que <br>
	 * provienen de distintas pantallas.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirPermutaGuardiasForm permutasForm = (DefinirPermutaGuardiasForm)formulario;
		ScsGuardiasColegiadoAdm admGuardiaColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
		
		Hashtable solicitanteHash = new Hashtable();
		Vector registros = new Vector();
		String idTurnoSolicitante="", idGuardiaSolicitante="", idCalendarioGuardiasSolicitante="", idPersonaSolicitante="", fechaInicioSolicitante="";
		String idInstitucion="", reserva ="", fechaFinSolicitante="";
		UsrBean usr;
		
			try {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				
				//Recupero los datos del formulario de la guardia a permutar (solicitante)
				idInstitucion = permutasForm.getIdInstitucion();
				idPersonaSolicitante = permutasForm.getIdPersona();
				idCalendarioGuardiasSolicitante = permutasForm.getIdCalendarioGuardias();
				idTurnoSolicitante = permutasForm.getIdTurno(); 
				idGuardiaSolicitante = permutasForm.getIdGuardia();								
				fechaInicioSolicitante = permutasForm.getFechaInicio();
				fechaFinSolicitante = permutasForm.getFechaFin();
				reserva = permutasForm.getReserva();
				
				//------------------
				//DATOS SOLICITANTE
				//------------------
				//Hago la busqueda de los datos necesarios de esa guardia, los guardo en la hash mando en request:
				solicitanteHash.put("IDINSTITUCION",idInstitucion);
				solicitanteHash.put("IDTURNO",idTurnoSolicitante);
				solicitanteHash.put("IDGUARDIA",idGuardiaSolicitante);
				solicitanteHash.put("IDCALENDARIOGUARDIAS",idCalendarioGuardiasSolicitante);
				solicitanteHash.put("IDPERSONA",idPersonaSolicitante);
				solicitanteHash.put("FECHAINICIO",fechaInicioSolicitante);
				//solicitanteHash.put("RESERVA",reserva);
				registros.clear();				
				//Busco los datos que me faltan del colegiado
				registros = cabeceraGuardiasAdm.selectGenerico(cabeceraGuardiasAdm.getDatosColegiado(solicitanteHash));
				solicitanteHash.put("NOMBREYAPELLIDOS",UtilidadesHash.getString((Hashtable)registros.get(0),"NOMBRE"));
				solicitanteHash.put("NUMEROCOLEGIADO",UtilidadesHash.getString((Hashtable)registros.get(0),CenColegiadoBean.C_NCOLEGIADO));
				solicitanteHash.put("FECHAFIN",fechaFinSolicitante);
				request.setAttribute("SOLICITANTE",solicitanteHash);
				
		
				//------------------
				//DATOS RESULTADOS
				//------------------
				//Hago la busqueda de los datos necesarios de esa guardia, los guardo en el vector y mando en request:
				registros.clear();
				registros = cabeceraGuardiasAdm.selectGenerico(cabeceraGuardiasAdm.buscarOtrosColegiados(solicitanteHash));
				request.setAttribute("resultados",registros);				
			}
			catch (Exception e) {
				throwExcp("messages.select.error",e,null);
			}
			
			return "modalCambiar";
	}

	/**
	 * Vamos a la modal de Confirmacion de la Permuta
	 *
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirPermutaGuardiasForm permutasForm = (DefinirPermutaGuardiasForm)formulario;
		ScsGuardiasColegiadoAdm admGuardiaColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutas = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		
		Hashtable solicitanteHash = new Hashtable();
		Hashtable confirmadorHash = new Hashtable();
		Hashtable temporalHash = new Hashtable();
		Vector registros = new Vector();
		Vector ocultos = new Vector();
		String idInstitucion="", reserva ="";
		UsrBean usr;
		
			try {
				usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				
				//Recupero los datos del formulario de la guardia a permutar (solicitante)
				idInstitucion = permutasForm.getIdInstitucion();
				reserva = permutasForm.getReserva();
				
				//------------------
				//DATOS CONFIRMADOR
				//------------------
				//Hago la busqueda de los datos necesarios de ese solicitante de la permuta de guardias
				confirmadorHash.put("IDINSTITUCION",idInstitucion);
				confirmadorHash.put("IDTURNO",permutasForm.getIdTurno());
				confirmadorHash.put("IDGUARDIA",permutasForm.getIdGuardia());
				confirmadorHash.put("IDCALENDARIOGUARDIAS",permutasForm.getIdCalendarioGuardias());
				confirmadorHash.put("IDPERSONA",permutasForm.getIdPersona());
				confirmadorHash.put("FECHAINICIO",permutasForm.getFechaInicio());
				confirmadorHash.put("RESERVA",permutasForm.getReserva());
				registros.clear();				
				registros = admPermutas.selectGenerico(admPermutas.buscarDatosConfirmador(confirmadorHash));
				temporalHash = (Hashtable)registros.get(registros.size()-1);
				confirmadorHash.put("NOMBREYAPELLIDOS",UtilidadesHash.getString(temporalHash,"NOMBRE"));
				confirmadorHash.put("NUMEROCOLEGIADO",UtilidadesHash.getString(temporalHash,CenColegiadoBean.C_NCOLEGIADO));
				confirmadorHash.put("FECHAFIN",UtilidadesHash.getString(temporalHash,ScsCabeceraGuardiasBean.C_FECHA_FIN));					
				request.setAttribute("CONFIRMADOR",confirmadorHash);

				//------------------
				//DATOS SOLICITANTE
				//------------------
				//Hago la busqueda de los datos del confirmador de la permuta a partir del solicitante de la permuta
				registros.clear();
				temporalHash.clear();
				registros = admPermutas.selectGenerico(admPermutas.buscarDatosSolicitanteDesdeConfirmador(confirmadorHash));
				temporalHash = (Hashtable)registros.get(registros.size()-1);
				solicitanteHash.put("IDINSTITUCION",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_IDINSTITUCION));
				solicitanteHash.put("IDTURNO",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_IDTURNO_SOLICITANTE));
				solicitanteHash.put("IDGUARDIA",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_IDGUARDIA_SOLICITANTE));
				solicitanteHash.put("IDCALENDARIOGUARDIAS",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_IDCALENDARIOGUARDIAS_SOLICITAN));
				solicitanteHash.put("IDPERSONA",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_IDPERSONA_SOLICITANTE));
				solicitanteHash.put("FECHAINICIO",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_FECHAINICIO_SOLICITANTE));
				solicitanteHash.put("NOMBREYAPELLIDOS",UtilidadesHash.getString(temporalHash,"NOMBRE"));
				solicitanteHash.put("NUMEROCOLEGIADO",UtilidadesHash.getString(temporalHash,CenColegiadoBean.C_NCOLEGIADO));
				solicitanteHash.put("FECHAFIN",UtilidadesHash.getString(temporalHash,"FECHA_FIN"));
				solicitanteHash.put("MOTIVOS",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_MOTIVOS_SOLICITANTE));
				request.setAttribute("SOLICITANTE",solicitanteHash);
				
				//Mando otros datos necesarios:
				request.setAttribute("NUMEROPERMUTA",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_NUMERO));
				request.setAttribute("FECHASOLICITUD",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_FECHASOLICITUD));
				request.setAttribute("ANULADA",UtilidadesHash.getString(temporalHash,ScsPermutaGuardiasBean.C_ANULADA));
				
				//Almaceno en sesion los datos obtenidos	
				request.getSession().setAttribute("DATABACKUP",temporalHash);
			}
			catch (Exception e) {
				throwExcp("messages.select.error",e,null);
			}
					
			return "modalConfirmar";	
	}
}