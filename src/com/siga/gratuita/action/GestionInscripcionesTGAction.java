package com.siga.gratuita.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenComponentesAdm;
import com.siga.beans.CenComponentesBean;
import com.siga.beans.CenDireccionTipoDireccionAdm;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsRetencionesAdm;
import com.siga.beans.ScsRetencionesBean;
import com.siga.beans.ScsRetencionesIRPFAdm;
import com.siga.beans.ScsRetencionesIRPFBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.InscripcionTurno;
import com.siga.gratuita.form.InscripcionTGForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

public class GestionInscripcionesTGAction extends MasterAction {
	private final int tipoActualizacionBaja=0;
	private final int tipoActualizacionEdicion=1;
	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		InscripcionTGForm miForm = null;
		try { 
			do {
				miForm = (InscripcionTGForm) formulario;
				if (miForm != null) {
					miForm.setUsrBean(this.getUserBean(request));
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
						
					System.out.println(" SESSION: " + request.getSession().getAttributeNames()); 
					if(modo!=null)
						accion = modo;
					
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxGuardias")){
						getAjaxGuardias (mapping, miForm, request, response);
						return null;
						
					}else if ( accion.equalsIgnoreCase("getAjaxColegiado")){					
						getAjaxColegiado(mapping, miForm, request, response);
						return null;
						
					}else if ( accion.equalsIgnoreCase("getAjaxBusqueda")){
						mapDestino = getAjaxBusqueda (mapping, miForm, request, response);
						
					} else if (accion.equalsIgnoreCase("sitConsultaTurno")){
						mapDestino = sitConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sitConsultaGuardias")){
						mapDestino = sitConsultaGuardias(mapping, miForm, request, response);

					}else if (accion.equalsIgnoreCase("sitDatos")){											
						mapDestino = sitDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sitEditarTelefonosGuardia")){
						mapDestino = sitEditarTelefonosGuardia(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sitInsertar")){
						// Insertamos la solicitud
						// forward : exito
						mapDestino = sitInsertar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbtConsultaTurno")){
						mapDestino = sbtConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbtConsultaGuardias")){
						mapDestino = sbtConsultaGuardias(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbtDatos")){						
						mapDestino = sbtDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbtComprobarInsertar")){
						mapDestino = sbtComprobarInsertar(mapping, miForm, request, response);
						// Insertamos la solicitud
						// forward : exito
						
					}else if (accion.equalsIgnoreCase("sbtInsertar")){
						mapDestino = sbtInsertar(mapping, miForm, request, response);
						// Insertamos la solicitud
						// forward : exito
						
					}else if (accion.equalsIgnoreCase("sigConsultaTurno")){
						mapDestino = sigConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sigConsultaGuardias")){
						mapDestino = sigConsultaGuardias(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sigDatos")){						
						mapDestino = sigDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sigInsertar")){
						// Insertamos la solicitud
						// forward : exito
						mapDestino = sigInsertar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbgConsultaTurno")){
						mapDestino = sbgConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbgConsultaGuardias")){
						mapDestino = sbgConsultaGuardias(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbgDatos")){
						mapDestino = sbgDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbgInsertar")){
						mapDestino = sbgInsertar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("sbgComprobarInsertar")){
						mapDestino = sbgComprobarInsertar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vitConsultaTurno")){
						mapDestino = vitConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vitConsultaGuardias")){
						mapDestino = vitConsultaGuardias(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vitDatos")){						
						mapDestino = vitDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vitValidar")){						
						mapDestino = vitValidar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vigConsultaTurno")){
						mapDestino = vigConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vigConsultaGuardias")){
						mapDestino = vigConsultaGuardias(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vigDatos")){						
						mapDestino = vigDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vigValidar")){
						mapDestino = vigValidar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbtConsultaTurno")){
						mapDestino = vbtConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbtConsultaGuardias")){
						mapDestino = vbtConsultaGuardias(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbtDatos")){						
						mapDestino = vbtDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbtValidar")){
						mapDestino = vbtValidar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbtComprobarValidar")){
						mapDestino = vbtComprobarValidar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbgConsultaTurno")){
						mapDestino = vbgConsultaTurno(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbgConsultaGuardias")){
						mapDestino = vbgConsultaGuardias(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbgDatos")){						
						mapDestino = vbgDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbgComprobarValidar")){
						mapDestino = vbgComprobarValidar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("vbgValidar")){
						mapDestino = vbgValidar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("smitDatos")){						
						mapDestino = smitDatos(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("smitDatosBaja")){						
						mapDestino = smitDatosBaja(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("smitEditarTelefonosGuardia")){
						mapDestino = smitEditarTelefonosGuardia(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("smitInsertar")){
						mapDestino = smitInsertar(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("smbtInsertarBaja")){
						mapDestino = smbtInsertarBaja(mapping, miForm, request, response);
						
					}else if (accion.equalsIgnoreCase("smbtInsertar")){
						mapDestino = smbtInsertar(mapping, miForm, request, response);						
						
					}else if(accion.equalsIgnoreCase("solicitudesMasivas")){
						mapDestino =  solicitudesMasivas(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("vmitValidar")){
						mapDestino =  vmitValidar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("vmigValidar")){
						mapDestino =  vmigValidar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("vmbtComprobarValidar")){
						mapDestino =  vmbtComprobarValidar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("vmbtValidar")){
						mapDestino =  vmbtValidar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("vmbgComprobarValidar")){
						mapDestino =  vmbgComprobarValidar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("vmbgValidar")){
						mapDestino =  vmbgValidar(mapping,  miForm,  request,  response);
						
					}else if (accion.equalsIgnoreCase("editarFechaValidacion")){
						// buscarPersona
						mapDestino = editarFechaValidacion(mapping, miForm, request, response);
						
					}else if(accion.equalsIgnoreCase("editarFechaBaja")){
						mapDestino =  editarFechaBaja(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("actualizarFechaValidacion")){
						mapDestino =  actualizarFechaValidacion(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("afvtModificar")){
						mapDestino =  afvtModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("comprobarAmfvtModificar")){
						mapDestino =  comprobarAmfvtModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("amfvtModificar")){
						mapDestino =  amfvtModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("afbgModificar")){
						mapDestino =  afbgModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("comprobarAmfvgModificar")){
						mapDestino =  comprobarAmfvgModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("amfvgModificar")){
						mapDestino =  amfvgModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("comprobarAmfbtModificar")){
						mapDestino =  comprobarAmfbtModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("amfbtModificar")){
						mapDestino =  amfbtModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("comprobarAmfbgModificar")){
						mapDestino =  comprobarAmfbgModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("amfbgModificar")){
						mapDestino =  amfbgModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("actualizarFechaBaja")){
						mapDestino =  actualizarFechaBaja(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("afbtModificar")){
						mapDestino =  afbtModificar(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("consultaInscripcion")){
						mapDestino =  consultaInscripcion(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("busquedaTurnosDisponibles")){
						mapDestino =  busquedaTurnosDisponibles(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("busquedaTurnosDisponiblesBaja")){
						mapDestino =  busquedaTurnosDisponiblesBaja(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("listadoTurnosDisponibles")){
						mapDestino =  listadoTurnosDisponibles(mapping,  miForm,  request,  response);
						
					}else if(accion.equalsIgnoreCase("listadoTurnosDisponiblesBaja")){
						mapDestino =  listadoTurnosDisponiblesBaja(mapping,  miForm,  request,  response);
					
					}else if(accion.equalsIgnoreCase("borrarTurno")){
						mapDestino =  borrarTurno(mapping,  miForm,  request,  response);						
						
					}else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			
			return mapping.findForward(mapDestino);
			
		} catch (SIGAException es) {
			throw es;
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
	/**
	 * Paso 1 - Alta Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sitConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		try {
			UsrBean usrBean = this.getUserBean(request);
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.reset(true,true);
			miForm.setIdGuardia(null);
			miForm.setIdRetencion(null);

			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = null;
			if(miForm.getFechaSolicitud()!=null && !miForm.getFechaSolicitud().equals("")){
				inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), 
						new Long(miForm.getIdPersona()), 
						miForm.getFechaSolicitud(),
						true);
				//seteamos el paso siguiente
				miForm.setModo("consultaGuardiasValidarInscripcionTurno");

			}else{
				inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), 
						new Long(miForm.getIdPersona()));
				//seteamos el paso siguiente
				miForm.setModo("sitConsultaGuardias");
			}
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			forward = "consultaTurnoInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 2 - Alta Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sitConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			
			Integer idGuardia=null;			
			if (miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && 
					Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = Integer.parseInt(miForm.getIdGuardia());
			
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasParaInscripcion(
				new Integer(miForm.getIdInstitucion()),
				new Integer(miForm.getIdTurno()), 
				new Long(miForm.getIdPersona()),
				idGuardia);
			
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			miForm.setModo("sitDatos");
			forward = "consultaGuardiasInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 3 - Alta Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sitDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;

			ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(this.getUserBean(request));

			/* JPT: Se hacen comprobaciones cuando se termina la operación de alta de inscripcion de turno

			// comprobando si la ultima inscripcion tiene fecha de baja para que se pueda solicitar alta nueva
			ScsInscripcionTurnoBean insUltimaConBaja = admInsTurno.getInscripcion(
					new Integer(miForm.getIdInstitucion()), 
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null);
			
			boolean existeInscConBaja = insUltimaConBaja != null && insUltimaConBaja.getFechaBaja() != null && !insUltimaConBaja.getFechaBaja().equals("");			
			
			if (existeInscConBaja) {
				miForm.setFechaBajaTurno(insUltimaConBaja.getFechaBaja());
			} else {
				miForm.setFechaBajaTurno(null);
			}
			*/

			// comprobando si alguna de las guardias es por grupo
			boolean isAlgunaGuardiaPorGrupo = false;
			String guardiasSeleccionadas = miForm.getGuardiasSel();
			if (guardiasSeleccionadas != null && !guardiasSeleccionadas.equals("")) {
				guardiasSeleccionadas = guardiasSeleccionadas.substring(0, guardiasSeleccionadas.lastIndexOf("@"));
				List<String> guardiasSeleccionadasList = null;
				if (guardiasSeleccionadas != null && !guardiasSeleccionadas.equals("")) {
					String[] guardiasSel = guardiasSeleccionadas.split("@");
					guardiasSeleccionadasList = Arrays.asList(guardiasSel);
				}
				if (guardiasSeleccionadasList != null && guardiasSeleccionadasList.size() > 0) {
					for (ScsInscripcionGuardiaBean insGuardia : miForm.getInscripcionesGuardia()) {
						if (guardiasSeleccionadasList.contains(insGuardia.getGuardia().getIdGuardia().toString())
								&& insGuardia.getGuardia().getPorGrupos() != null
								&& insGuardia.getGuardia().getPorGrupos().equals("1")) {
							isAlgunaGuardiaPorGrupo = true;
							break;
						}
					}
				}
			}
			
			if (isAlgunaGuardiaPorGrupo) {
				miForm.setPorGrupos("1");
			} else {
				miForm.setPorGrupos("0");
			}
			
			// guardando la retencion seleccionada en el formulario (o "0" si no existe)
			comprobarRetencion(miForm, this.getUserBean(request));

			// guardando valores en formulario
			miForm.setSolicitudAlta(true);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(true);
			miForm.setValidacionBaja(false);
			miForm.setMasivo(false);

			// FIXME AAAÑADIR SELECCIÓN DE GRUPO sitDatos ok
			// COMPROBAR SI EXISTE ALGUNA GUARDIA DEL TURNO QUE SEA DE GRUPO Y EN TAL CASO
			// SACAR UN LISTADO CON LAS GRUPOS DE LA GUARDIA PARA QUE SEA OBLIGATORIO LA ELECCION DE UNO
			miForm.setModo("sitEditarTelefonosGuardia");

		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}

		return "validarInscripcion";
	}

	/**
	 * Paso 4 - Alta Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sitEditarTelefonosGuardia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		String forward = "";
		
		try {
			// INICIALIZAMOS EL DATABUCKUP
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			
			CenDireccionTipoDireccionAdm tipoDirAdm = new CenDireccionTipoDireccionAdm (this.getUserBean(request));
			CenDireccionesAdm dirAdm=new CenDireccionesAdm(this.getUserBean(request));
			
			Hashtable claves=new Hashtable();
			//BUSCAMOS SI EL CLIENTE TIENE ALGUNA DIRECCION DE TIPO GUARDIA
			claves.put(CenDireccionTipoDireccionBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			claves.put(CenDireccionTipoDireccionBean.C_IDPERSONA, miForm.getIdPersona());
			Vector v = tipoDirAdm.select(claves);
			Hashtable direccion=new Hashtable();
			
			if(v != null && v.size() != 0) {
				for(int i=0;i<v.size();i++) {
					CenDireccionTipoDireccionBean beanTipoDirec=new CenDireccionTipoDireccionBean();
					beanTipoDirec=(CenDireccionTipoDireccionBean)v.elementAt(i);
					int tipoDirec=((Integer)beanTipoDirec.getIdTipoDireccion()).intValue();
					Long indicadorDirec=(Long)beanTipoDirec.getIdDireccion();
					if(tipoDirec == ClsConstants.TIPO_DIRECCION_GUARDIA){
						//obtenemos los datos de la dirección
						direccion=dirAdm.selectDirecciones(new Long(miForm.getIdPersona()),new Integer(miForm.getIdInstitucion()),indicadorDirec);
						if (direccion !=null){
							//ponemos los datos de la dirección de guardia en el form
							miForm.setTelefono1((String)direccion.get(CenDireccionesBean.C_TELEFONO1));
							miForm.setTelefono2((String)direccion.get(CenDireccionesBean.C_TELEFONO2));
							miForm.setMovil((String)direccion.get(CenDireccionesBean.C_MOVIL));
							miForm.setFax1((String)direccion.get(CenDireccionesBean.C_FAX1));
							miForm.setFax2((String)direccion.get(CenDireccionesBean.C_FAX2));
							miForm.setIdDireccion(indicadorDirec.toString());
							request.getSession().setAttribute("ORIGINALDIR", direccion);
						}
					}
				}
			}
			forward = "editarTelefonosGuardia";
			miForm.setModo("sitInsertar");
			
		} catch(Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		return forward;
	}
	
	/**
	 * Paso 2 - Alta Inscripción Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String smitEditarTelefonosGuardia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		String forward = sitEditarTelefonosGuardia(mapping, formulario, request, response);
		miForm.setModo("smitInsertar");
		return forward;
	}
	
	/**
	 * Paso 5 (FINAL) - Alta Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sitInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		try {
			// Hago una serie de comprobaciones previas al alta de la inscripcion del turno
			String sPreAlta = this.preAltaTurno(miForm, usr);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreAlta != "") {
				request.setAttribute("mensaje", sPreAlta);
				return ClsConstants.ERROR_AVISO;	
			}			
			
			tx = usr.getTransaction();
			tx.begin();
			
			// Solicito el alta de la inscripcion de turno
			InscripcionTurno inscripcion = new InscripcionTurno(new ScsInscripcionTurnoBean());
			inscripcion.solicitarAlta(miForm, usr);
			
			Hashtable original = (Hashtable) request.getSession().getAttribute("ORIGINALDIR");
			CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);			
			dirAdm.insertarDireccionGuardia(
				new Integer(miForm.getIdInstitucion()),
				new Long(miForm.getIdPersona()),
				miForm.getIdDireccion(),
				miForm.getFax1(),
				miForm.getFax2(),
				miForm.getMovil(),
				miForm.getTelefono1(),
				miForm.getTelefono2(),
				original);
			
			tx.commit();
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
	        request.getSession().removeAttribute("ORIGINALDIR");
			
		} catch (Exception e){
			try {
				tx.rollback();
			} catch (Exception ex) {
			}
			throw new SIGAException("messages.general.error", e, new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 4 (FINAL) - Alta Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vitValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		// Para inscripciones automaticas, se realiza como si no lo fueran
		miForm.setValidarInscripciones("S");
		
		try {
			if (miForm.getFechaValidacion() != null && !miForm.getFechaValidacion().equals("")) {
				// Hago una serie de comprobaciones previas al alta de la inscripcion del turno
				String sPreAlta = this.preAltaTurno(miForm, usr);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreAlta != "") {
					request.setAttribute("mensaje", sPreAlta);
					return ClsConstants.ERROR_AVISO;	
				}				
				
			} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
				request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
				return ClsConstants.ERROR_AVISO;					
			}
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
			ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
			insTurnoBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insTurnoBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insTurnoBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insTurnoBean.setFechaSolicitud(miForm.getFechaSolicitud());
			
			InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);		
			
			tx = usr.getTransaction();
			tx.begin();	
		
			if(miForm.getFechaDenegacion() != null && !miForm.getFechaDenegacion().equals("")) {
				inscripcion.denegarInscripcionTurno(
					miForm.getFechaDenegacion(), 
					miForm.getObservacionesDenegacion(), 
					usr);	
					
			} else if(miForm.getFechaValidacion() != null && !miForm.getFechaValidacion().equals("")) {
				inscripcion.validarAlta(
					miForm.getFechaValidacion(), 
					miForm.getObservacionesValidacion(), 
					usr);
			}
			
			tx.commit();
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
	        
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}				
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 5 (FINAL) - Baja Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbtValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		/* Datos necesarios:
		 * - miForm.idInstitucion..........: viene el formulario con el dato incluido
		 * - miForm.idTurno................: viene el formulario con el dato incluido
		 * - miForm.idPersona..............: viene el formulario con el dato incluido
		 * - miForm.fechaSolicitud.........: viene el formulario con el dato incluido
		 * - miForm.tipoActualizacionSyC...: viene el formulario con el dato incluido
		 * 
		 * - miForm.observacionesValBaja...: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion (Observaciones Validacion)
		 * - miForm.fechaBaja..............: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion
		 * - miForm.observacionesDenegacion: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion (Observaciones Validacion)
		 * - miForm.fechaDenegacion........: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion
		*/
		
		try {			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
			ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
			insTurnoBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insTurnoBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insTurnoBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insTurnoBean.setFechaSolicitud(miForm.getFechaSolicitud());
			
			InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);		
			
			tx = usr.getTransaction();
			tx.begin();	
			
			if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")){
				inscripcion.denegarBajaInscripcionTurno(
					miForm.getFechaDenegacion(), 
					miForm.getObservacionesDenegacion(), 
					usr);
				
			} else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals("")) {
				inscripcion.validarBaja(
					miForm.getFechaBaja(),
					null,
					miForm.getObservacionesValBaja(), 
					miForm.getTipoActualizacionSyC(), 
					usr);
			}
			
			tx.commit();
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
	        
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}	
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 4 - Baja Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbtComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// Para inscripciones automaticas, se realiza como si no lo fueran
		miForm.setValidarInscripciones("S");	
		
		try {		
			if (miForm.getFechaBaja() != null && !miForm.getFechaBaja().equals("")) {
				
				// Comprobaciones previas a dar de baja una inscripcion de turno
				String sPreBaja = this.preBajaTurno(miForm, usr, false);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreBaja != "") {
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}				
				
			} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
				request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
				return ClsConstants.ERROR_AVISO;					
			}
						
			if ((miForm.getFechaDenegacion()==null || miForm.getFechaDenegacion().equals("")) && 
					miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals("")){
				
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
					usr, 
					Long.valueOf(miForm.getIdPersona()), 
					new Integer(miForm.getIdInstitucion()), 
					new Integer(miForm.getIdTurno()),
					null, 
					null,
					miForm.getFechaBaja(),
					false,
					this.tipoActualizacionBaja); 
				
				miForm.setEstadoPendientes(estadoPendientes);
			}
			
			if(miForm.getEstadoPendientes() != null && !miForm.getEstadoPendientes().equals("")) {
				miForm.setModo("vbtValidar");
				forward = ClsConstants.SMS_AVISO_ESTADO;
				return forward;
				
			} else {
				return vbtValidar(mapping,  formulario,  request,  response);
			}
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 	
	}
	
	/**
	 * Paso 1 - Baja Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbtConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
 		String forward = "";
		try {
			UsrBean usrBean = this.getUserBean(request);
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setFechaValidacionTurno(miForm.getFechaValidacion());
			miForm.reset(false,true);
			miForm.setIdGuardia(null);
//			String estadoPendientes = getEstadoGuardiasDesignasPendientes(
//						  usrBean, 
//						Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),null,null,null); 
//			miForm.setEstadoPendientes(estadoPendientes);
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null,
					true);
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());			
			miForm.setModo("sbtConsultaGuardias");
			forward = "consultaTurnoInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 2 - Baja Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbtConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && 
					miForm.getTipoGuardias()!=null && Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					new Integer(miForm.getIdGuardia()));
				
			} else {
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null);
			}
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			miForm.setModo("sbtDatos");
			forward = "consultaGuardiasInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}

	/**
	 * Paso 3 - Baja Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbtDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(false);
			miForm.setSolicitudBaja(true);
			miForm.setValidacionAlta(false);
			miForm.setValidacionBaja(true);
			miForm.setMasivo(false);
			miForm.setModo("sbtComprobarInsertar");
			forward = "validarInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 4 - Baja Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbtComprobarInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		try {			
			// Comprobaciones previas a dar de baja una inscripcion de turno
			String sPreBaja = this.preBajaTurno(miForm, usr, false);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreBaja != "") {
				request.setAttribute("mensaje", sPreBaja);
				return ClsConstants.ERROR_AVISO;	
			}
			
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")) {								
				
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
				  usr, 
				  Long.valueOf(miForm.getIdPersona()), 
				  new Integer(miForm.getIdInstitucion()), 
				  new Integer(miForm.getIdTurno()),
				  null,
				  null,
				  miForm.getFechaBaja(),
				  false,
				  this.tipoActualizacionBaja); 
				
				miForm.setEstadoPendientes(estadoPendientes);				
				
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("sbtInsertar");
					forward = ClsConstants.SMS_AVISO_ESTADO;
					return forward;
					
				}else{
					return sbtInsertar(mapping,  formulario,  request,  response);
				}
				
			}else{				
				return sbtInsertar(mapping,  formulario,  request,  response);				
			}

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
	}
	
	/**
	 * Paso 5 (FINAL) - Baja Inscripción Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbtInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		/* La baja de inscripcion se realiza desde la ficha de un colegiado.
		 * 
		 * Datos necesarios:
		 * - miForm.idInstitucion.......: viene el formulario con el dato incluido
		 * - miForm.idTurno.............: viene el formulario con el dato incluido
		 * - miForm.idPersona...........: viene el formulario con el dato incluido
		 * - miForm.fechaSolicitud......: viene el formulario con el dato incluido
		 * - miForm.fechaValidacionTurno: viene el formulario con el dato incluido
		 * - miForm.validarInscripciones: viene el formulario con el dato incluido
		 * - miForm.tipoActualizacionSyC: viene el formulario con el dato incluido
		 * 
		 * - miForm.observacionesBaja...: viene el formulario con el dato incluido de la jsp (Motivos Baja)
		 * - miForm.observacionesValBaja: viene el formulario con el dato incluido de la jsp (Observaciones Validacion)
		 * - miForm.fechaBaja...........: viene el formulario con el dato incluido de la jsp
		 * 
		 * - miForm.fechaSolicitudBaja..: sysdate
		*/			
		
		try {			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
			ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
			insTurnoBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insTurnoBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insTurnoBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insTurnoBean.setFechaSolicitud(miForm.getFechaSolicitud());
			
			InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);			
			
			miForm.setFechaSolicitudBaja("sysdate");	
			
			tx = usr.getTransaction();
			tx.begin();
			
			inscripcion.solicitarBaja(
				miForm.getFechaSolicitudBaja(),
				miForm.getObservacionesBaja(),
				miForm.getFechaBaja(),
				miForm.getObservacionesValBaja(), 
				miForm.getFechaValidacionTurno(),
				miForm.getTipoActualizacionSyC(), 
				usr);
			
			tx.commit();
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
	        
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}	
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * 
	 * @param usr
	 * @param idPersona
	 * @param idInstitucion
	 * @param idTurno
	 * @param idGuardia
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param isGuardia
	 * @param tipoActualizacion
	 * @return
	 * @throws SIGAException
	 */
	private String getEstadoGuardiasDesignasPendientes (
			UsrBean usr,
			Long idPersona,
			Integer idInstitucion,
			Integer idTurno,
			Integer idGuardia,
			String fechaDesde,
			String fechaHasta,
			boolean isGuardia,
			int tipoActualizacion) throws SIGAException {
		
//			Devuelve el nivel de error:
//			0: NO hay nada pendiente
//			1: Error, tiene guardias pendientes
//			2: Error, tiene designas pendientes
//			3: Excepcion
			CenClienteAdm admCliente = new CenClienteAdm(usr);
			String estado = null;
			int nivelError = 0;
			if(isGuardia)
				nivelError = admCliente.tieneGuardiasSJCSPendientes(idPersona, idInstitucion, idTurno,idGuardia,fechaDesde,fechaHasta);
			else
				nivelError = admCliente.tieneTrabajosSJCSPendientes(idPersona, idInstitucion, idTurno,fechaDesde,fechaHasta);
			
			switch (nivelError) {
				case 0:
					estado= null;
					break;
					
				case 1: 
					if(tipoActualizacion==this.tipoActualizacionBaja){
						estado = "gratuita.gestionInscripciones.aviso.baja.guardiasPendientes";
					}
					else if(tipoActualizacion==this.tipoActualizacionEdicion){
						estado = "gratuita.gestionInscripciones.aviso.modificacion.guardiasPendientes";
					} 					
					break;
					
				case 2:	
					if(tipoActualizacion==this.tipoActualizacionBaja){
						estado = "gratuita.gestionInscripciones.aviso.baja.designasPendientes";
					}
					else if(tipoActualizacion==this.tipoActualizacionEdicion){
						estado = "gratuita.gestionInscripciones.aviso.modificacion.designasPendientes";
					}  
					break;
					
				case 3: 
					throw new SIGAException(admCliente.getError());					
			}
		

		if(estado!=null)
			estado = UtilidadesString.getMensajeIdioma(	usr, estado);
		return estado; // No hay nada pendiente
	} //comprobarGuardiasDesignasPendientes()
	
	/**
	 * Paso 3 (FINAL) - Alta Inscripción Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String smitInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		try {			
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;		
			
			String sFechaValidacion = miForm.getFechaValidacion(); // Guardo la fecha de validacion inicial
			miForm.setTipoGuardias(String.valueOf(ScsTurnoBean.TURNO_GUARDIAS_OBLIGATORIAS)); // Las masivas solo pueden ser guardias obligatorias
			miForm.setGuardiasSel(null);
			
			tx = usr.getTransaction();
			tx.begin();			
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idTurnoSel=d[0];
				String validarInscripciones=d[1];
				
				/* La alta de inscripcion se realiza desde la ficha de un colegiado.
				 * 
				 * Datos necesarios para PreAlta:
				 * - miForm.idInstitucion.......: viene el formulario con el dato incluido
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: viene el formulario con el dato incluido
				 * - miForm.validarInscripciones: obtiene su valor de los datos masivos
				 * - miForm.fechaValidacion.....: viene el formulario con el dato incluido para validarInscripciones igual a "S" (para validarInscripciones igual a "N" da igual)
				 * 
				 * 					
				 *  form.getObservacionesSolicitud(),
					new Integer(form.getTipoGuardias()),
					form.getGuardiasSel(),
					form.getIdRetencion(),
				 * 
				*/
				miForm.setIdTurno(idTurnoSel);
				miForm.setValidarInscripciones(validarInscripciones);
				miForm.setFechaValidacion(sFechaValidacion); // Copio la fecha de validacion inicial, ya que se puede momdificar en preAltaTurno
				
				try {
					// Hago una serie de comprobaciones previas al alta de la inscripcion del turno
					String sPreAlta = this.preAltaTurno(miForm, usr);
					
					// Compruebo si existe algun error en las comprobaciones
					if (sPreAlta != "") {
						request.setAttribute("mensaje", sPreAlta);
						return ClsConstants.ERROR_AVISO;	
					}						
					
					// crea la inscripcion
					InscripcionTurno inscripcion = new InscripcionTurno(new ScsInscripcionTurnoBean());
					inscripcion.solicitarAlta(miForm, usr);									
					
				} catch (Exception e) {
					existenErrores = true;
				}				
			}
			
			CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);
			Hashtable original = (Hashtable) request.getSession ().getAttribute ("ORIGINALDIR");
			dirAdm.insertarDireccionGuardia(
				new Integer(miForm.getIdInstitucion()),
				new Long(miForm.getIdPersona()),
				miForm.getIdDireccion(),
				miForm.getFax1(),
				miForm.getFax2(),
				miForm.getMovil(),
				miForm.getTelefono1(),
				miForm.getTelefono2(),
				original);
		
			if(existenErrores){
				request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.masivo.solapamiento"));
				//tx.rollback();
				tx.commit();
				
			}else{
				request.setAttribute("mensaje","messages.updated.success");		
				tx.commit();
			}
			
			forward = "exito";
			request.setAttribute("modal", "1");
			request.getSession().removeAttribute("ORIGINALDIR");
			
		} catch (Exception e){
			try {
				tx.rollback();
			} catch (Exception ex) {
			}			
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 4 - Baja Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbgComprobarInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		try {			
			// Comprobaciones previas a dar de baja una inscripcion de guardia
			String sPreBaja = this.preBajaGuardia(miForm, usr, false);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreBaja != "") {
				request.setAttribute("mensaje", sPreBaja);
				return ClsConstants.ERROR_AVISO;	
			}		
			
			//solo dejaremos validar una a una cuando sean a elegir
			Integer idGuardia = null;
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				idGuardia = new Integer(miForm.getIdGuardia());			
			}
			
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
						usr, 
						Long.valueOf(miForm.getIdPersona()), 
						new Integer(miForm.getIdInstitucion()), 
						new Integer(miForm.getIdTurno()),
						idGuardia,
						null,
						miForm.getFechaBaja(),
						true,
						this.tipoActualizacionBaja); 
				
				miForm.setEstadoPendientes(estadoPendientes);				
				
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("sbgInsertar");
					forward = ClsConstants.SMS_AVISO_ESTADO;
					return forward;
					
				} else {
					return sbgInsertar(mapping,  formulario,  request,  response);
				}
				
			} else {
				return sbgInsertar(mapping,  formulario,  request,  response);				
			}
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 		
	}
	
	/**
	 * Paso 5 (FINAL) - Baja Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbgInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias

		try {	
			Integer idGuardia = null;
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				idGuardia = new Integer(miForm.getIdGuardia());				
			}
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
			ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
			insGuardiaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));					
			insGuardiaBean.setIdTurno(new Integer(miForm.getIdTurno()));					
			insGuardiaBean.setIdGuardia(idGuardia);					
			insGuardiaBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insGuardiaBean.setFechaSuscripcion(miForm.getFechaSolicitud());
			
			InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);
			
			miForm.setFechaSolicitudBaja("sysdate");
			
			inscripcion.setBajas(
				miForm.getObservacionesBaja(), 
				miForm.getFechaSolicitudBaja(), 
				miForm.getFechaBaja(),
				miForm.getObservacionesValBaja());
			
			inscripcion.solicitarBaja(usr,miForm.getTipoActualizacionSyC());
			
			forward = "exito";
			request.setAttribute("mensaje","messages.updated.success");
			request.setAttribute("modal","1");
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}	
	
	/**
	 * Paso 4 - Baja Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbgComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// Para inscripciones automaticas, se realiza como si no lo fueran
		miForm.setValidarInscripciones("S");
		
		try {
			if (miForm.getFechaBaja() != null && !miForm.getFechaBaja().equals("")) {
				// Comprobaciones previas a dar de baja una inscripcion de guardia
				String sPreBaja = this.preBajaGuardia(miForm, usr, false);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreBaja != "") {
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}
				
			} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
				request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
				return ClsConstants.ERROR_AVISO;					
			}
			
			Integer idGuardia = null;
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = new Integer(miForm.getIdGuardia());

			if(miForm.getFechaDenegacion()==null || miForm.getFechaDenegacion().equals("")){				
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
					  usr, 
					  Long.valueOf(miForm.getIdPersona()), 
					  new Integer(miForm.getIdInstitucion()), 
					  new Integer(miForm.getIdTurno()),
					  idGuardia,
					  null,
					  miForm.getFechaBaja(),
					  true,
					  this.tipoActualizacionBaja); 
				
				miForm.setEstadoPendientes(estadoPendientes);
			}
			
			if(miForm.getEstadoPendientes()!=null&&!miForm.getEstadoPendientes().equals("")){
					miForm.setModo("vbgValidar");
					forward = ClsConstants.SMS_AVISO_ESTADO;
					return forward;
				
			} else {
				return vbgValidar(mapping,  formulario,  request,  response);
			}
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 		
	}
	
	/**
	 * Paso 5 (FINAL) - Baja Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbgValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		try {		
			Integer idGuardia = null;
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = new Integer(miForm.getIdGuardia());
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
			ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
			insGuardiaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));					
			insGuardiaBean.setIdTurno(new Integer(miForm.getIdTurno()));					
			insGuardiaBean.setIdGuardia(idGuardia);					
			insGuardiaBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insGuardiaBean.setFechaSuscripcion(miForm.getFechaSolicitud());
			
			InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);	
			
			if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")) {
				inscripcion.setDenegacion(miForm.getObservacionesDenegacion(), miForm.getFechaDenegacion());
				
				inscripcion.denegarBajaGuardia(usr);
				
			} else {
				if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals("")) {
					inscripcion.setBajas(null, null, miForm.getFechaBaja(), miForm.getObservacionesValBaja());
					
					inscripcion.validarBaja(usr, miForm.getTipoActualizacionSyC());
				}
			}
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 4 (FINAL) - Alta Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sigInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		try {
			// Hago una serie de comprobaciones previas al alta de la inscripcion de guardia
			String sPreAlta = this.preAltaGuardia(miForm, usr);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreAlta != "") {
				request.setAttribute("mensaje", sPreAlta);
				return ClsConstants.ERROR_AVISO;	
			}		
		
			Integer idGuardia = null;		
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = new Integer(miForm.getIdGuardia());
			
			miForm.setFechaSolicitud("sysdate");
			
			ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
			insGuardiaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insGuardiaBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insGuardiaBean.setIdGuardia(idGuardia);
			insGuardiaBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insGuardiaBean.setFechaSuscripcion(miForm.getFechaSolicitud());
			insGuardiaBean.setObservacionesSuscripcion(miForm.getObservacionesSolicitud());
			insGuardiaBean.setFechaValidacion(miForm.getFechaValidacion());
			insGuardiaBean.setObservacionesValidacion(miForm.getObservacionesValidacion());	
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud + Observaciones Solicitud + FechaValidacion + ObservacionesValidacion
			InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);
			
			if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("") &&
				(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals(""))) {
				
				if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")) {
					inscripcion.setDatosGrupo(miForm.getNumeroGrupo(), new Integer(miForm.getOrdenGrupo()));
				}
			}
			
			inscripcion.solicitarAlta(usr);
			
			request.setAttribute("mensaje", "messages.updated.success");
			forward = "exito";
			request.setAttribute("modal", "1");
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 4 (FINAL) - Alta Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vigValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		// Para inscripciones automaticas, se realiza como si no lo fueran
		miForm.setValidarInscripciones("S");
		
		try {
			if (miForm.getFechaValidacion() != null && !miForm.getFechaValidacion().equals("")) {
				// Hago una serie de comprobaciones previas al alta de la inscripcion de guardia
				String sPreAlta = this.preAltaGuardia(miForm, usr);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreAlta != "") {
					request.setAttribute("mensaje", sPreAlta);
					return ClsConstants.ERROR_AVISO;	
				}
				
			} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
				request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
				return ClsConstants.ERROR_AVISO;					
			}				
				
			Integer idGuardia = null;
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = new Integer(miForm.getIdGuardia());
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
			ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
			insGuardiaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));					
			insGuardiaBean.setIdTurno(new Integer(miForm.getIdTurno()));					
			insGuardiaBean.setIdGuardia(idGuardia);					
			insGuardiaBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insGuardiaBean.setFechaSuscripcion(miForm.getFechaSolicitud());
			
			InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);			
				
			if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("") &&
				(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals(""))) {
				
				inscripcion.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
				
				if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
					inscripcion.setDatosGrupo(miForm.getNumeroGrupo(), new Integer(miForm.getOrdenGrupo()));
				}
					
				inscripcion.validarAlta(usr);
					
			} else {
				if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")) {
					
					inscripcion.setDenegacion(miForm.getObservacionesDenegacion(), miForm.getFechaDenegacion());
					
					inscripcion.denegarAltaGuardia(usr);
				}
			}
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 1 - Cambiar fecha efectiva de alta de inscripciones de turno
	 * Paso 1 - Cambiar fecha efectiva de alta de inscripciones de guardia
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	private String editarFechaValidacion(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException ,ClsExceptions{
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		miForm.setFechaBaja(null);
		
		// JPT: Obtiene la fecha de validacion previa
		miForm.setFechaValidacion(GstDate.getFormatedDateShort("",miForm.getFechaValidacion()));
		
		// JPT: Ya que la fecha de validacion previa se va a modificar lo guardo
		miForm.setFechaValidacionPrevia(miForm.getFechaValidacion());
		
		miForm.setModo("actualizarFechaValidacion");		
		return "editarFechaValidacion";
	}
	
	/**
	 * Paso 1 - Cambiar fecha efectiva de baja de inscripciones de turno
	 * Paso 1 - Cambiar fecha efectiva de baja de inscripciones de guardia
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	private String editarFechaBaja(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException ,ClsExceptions{
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		// JPT: Obtiene la fecha de baja previa
		miForm.setFechaBaja(GstDate.getFormatedDateShort("", miForm.getFechaBaja()));
		
		// JPT: Ya que la fecha de validacion previa se va a modificar lo guardo
		miForm.setFechaBajaPrevia(miForm.getFechaBaja());
		
		//Obtiene la fecha de validacion
		miForm.setFechaValidacion(miForm.getFechaValidacion());
		
		miForm.setModo("actualizarFechaBaja");
		return "editarFechaBaja";
	}
		
	@SuppressWarnings("unchecked")
	private String inicio (ActionMapping mapping, 		
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws ClsExceptions, SIGAException 
				{
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		miForm.setIdInstitucion(usrBean.getLocation());
		miForm.setClase("T");
		List<ScsTurnoBean> alTurnos = null;
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(usrBean);
		alTurnos = admTurnos.getTurnos(miForm.getIdInstitucion());
		if(alTurnos==null){
			alTurnos = new ArrayList<ScsTurnoBean>();
		}
		miForm.setTurnosInscripcion(alTurnos);
		miForm.setGuardiasInscripcion(new ArrayList<ScsGuardiasTurnoBean>());
		return "inicio";
	}

	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws Exception
	 */
	private String getAjaxBusqueda (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,Exception {
			InscripcionTGForm validaTurnosForm = (InscripcionTGForm) formulario;
			validaTurnosForm.reset(true, true);
			UsrBean usrBean = this.getUserBean(request);
			String forward = "exception";
			String clase = validaTurnosForm.getClase();
			try {
				if(clase.equals("T")){
					ScsInscripcionTurnoAdm inscTurno = new ScsInscripcionTurnoAdm(usrBean);
					List<ScsInscripcionTurnoBean> alIncripciones = inscTurno.getInscripcionesTurno(validaTurnosForm);
					validaTurnosForm.setInscripcionesTurno(alIncripciones);
					forward = "listadoInscripcionTurnos";
					
				}else if(clase.equals("G")){
					ScsInscripcionGuardiaAdm inscGuardia = new ScsInscripcionGuardiaAdm(usrBean);
					List<ScsInscripcionGuardiaBean> alIncripciones = inscGuardia.getInscripcionesGuardias(validaTurnosForm);
					validaTurnosForm.setInscripcionesGuardia(alIncripciones);
					forward = "listadoInscripcionGuardias";
				}				
			} catch (ClsExceptions e) {
				validaTurnosForm.setInscripcionesGuardia(new ArrayList<ScsInscripcionGuardiaBean>());
				validaTurnosForm.setInscripcionesTurno(new ArrayList<ScsInscripcionTurnoBean>());
				String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
				throw e;
		
			}catch (Exception e){
				validaTurnosForm.setInscripcionesGuardia(new ArrayList<ScsInscripcionGuardiaBean>());
				validaTurnosForm.setInscripcionesTurno(new ArrayList<ScsInscripcionTurnoBean>());
				String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
				throw e;
				
			}
			return forward;
		}
		
		/**
		 * 
		 * @param mapping
		 * @param formulario
		 * @param request
		 * @param response
		 * @throws ClsExceptions
		 * @throws SIGAException
		 * @throws Exception
		 */
		private void getAjaxGuardias (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, Exception {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			UsrBean usrBean = this.getUserBean(request);
			//Sacamos las guardias si hay algo selccionado en el turno
			List<ScsGuardiasTurnoBean> alGuardias = null;
			if(miForm.getIdTurno()!= null && !miForm.getIdTurno().equals("-1")&& !miForm.getIdTurno().equals("")){
				ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usrBean);
				alGuardias = admGuardias.getGuardiasTurnos(new Integer(miForm.getIdTurno()),new Integer(miForm.getIdInstitucion()),true);
			}
			if(alGuardias==null){
				alGuardias = new ArrayList<ScsGuardiasTurnoBean>();				
			}
			respuestaAjax(new AjaxCollectionXmlBuilder<ScsGuardiasTurnoBean>(), alGuardias,response);			
		}
		
		/**
		 * 
		 * @param mapping
		 * @param formulario
		 * @param request
		 * @param response
		 * @throws ClsExceptions
		 * @throws SIGAException
		 * @throws Exception
		 */
		private void getAjaxColegiado (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, Exception {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			//Sacamos las guardias si hay algo selccionado en el turno
			Hashtable<String, Object> htCliente = null;
			String colegiadoNumero = miForm.getColegiadoNumero();
			UsrBean usrBean = this.getUserBean(request);
			if(colegiadoNumero!= null && !colegiadoNumero.equals("")){
				CenClienteAdm admCli = new CenClienteAdm(usrBean);
				Vector<Hashtable<String, Object>> vClientes = admCli.getClientePorNColegiado(miForm.getIdInstitucion(),miForm.getColegiadoNumero());
				if(vClientes!=null &&vClientes.size()>0)
					htCliente = vClientes.get(0);
			}
			String colegiadoNombre = null;
			String idPersona = null;
			
			if(htCliente!=null){
				colegiadoNombre = (String)htCliente.get("NOMCOLEGIADO");
				idPersona = (String)htCliente.get("IDPERSONA");
				
			}else{
				colegiadoNombre = "";
				idPersona = "";
				colegiadoNumero = "";
			}
			List listaParametros = new ArrayList();
			listaParametros.add(idPersona);
			listaParametros.add(colegiadoNumero);
			listaParametros.add(colegiadoNombre);
			respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
		}
				
		/**
		 * Paso 3 - Alta Inscripción Guardias (individual)
		 * @param mapping
		 * @param formulario
		 * @param request
		 * @param response
		 * @return
		 * @throws SIGAException
		 */
		private String sigDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			String forward = "";
			
			try {
				InscripcionTGForm miForm = (InscripcionTGForm) formulario;
				miForm.setSolicitudAlta(true);
				miForm.setSolicitudBaja(false);
				miForm.setValidacionAlta(true);
				miForm.setValidacionBaja(false);
				miForm.setMasivo(false);
				
				if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
					ArrayList<LetradoInscripcion> letradosColaGuardiaList = InscripcionGuardia.getColaGuardia(
							new Integer(miForm.getIdInstitucion()),
							new Integer(miForm.getIdTurno()), 
							new Integer(miForm.getIdGuardia()), 
							"sysdate",
							"sysdate", 
							this.getUserBean(request));
					
					if(letradosColaGuardiaList!=null && !letradosColaGuardiaList.isEmpty()){
						miForm.setGruposGuardiaLetrado(letradosColaGuardiaList);
					}else{
						miForm.setGruposGuardiaLetrado(new ArrayList<LetradoInscripcion>());
					}
				}			
					
				//seteamos el paso siguiente
//				FIXME AAAÑADIR SELECCIÓN DE GRUPO OK SIGDATOS
				//COMPROBAR SI EXISTE ALGUNA GUARDIA DEL TURNO QUE SEA DE GRUPO Y EN TAL CASO
				//SACAR UN LISTADO CON LAS GRUPOS DE LA GUARDIA PARA QUE SEA OBLIGATORIO LA ELECCION DE UNO
				miForm.setModo("sigInsertar");
				forward = "validarInscripcion";
				
			} catch (Exception e) {
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
			return forward;
		}

	/**
	 * Paso 1 - Alta Inscripción Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String smitDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;

			// guardando la retencion seleccionada en el formulario (o "0" si no existe)
			comprobarRetencion(miForm, this.getUserBean(request));

			// guardando valores en formulario
			miForm.setSolicitudAlta(true);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(true);
			miForm.setValidacionBaja(false);
			miForm.setMasivo(true);
			miForm.reset(true, true);
			miForm.setModo("smitEditarTelefonosGuardia");
			miForm.setPorGrupos("1");
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}

		return "validarInscripcion";
	}
	
	/**
	 * Paso 1 - Baja Inscripción Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String smitDatosBaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(false);
			miForm.setSolicitudBaja(true);
			miForm.setValidacionAlta(false);
			miForm.setValidacionBaja(true);
			miForm.setMasivo(true);
			miForm.reset(true,true);
			miForm.setModo("smbtInsertarBaja");
			miForm.setPorGrupos("1");
			forward = "validarInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
		
	/**
	 * Paso 1 - Alta Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vitConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward ="";
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.reset(false,true);
			miForm.setIdGuardia(null);
			
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = null;
			if(miForm.getFechaSolicitud()!=null && !miForm.getFechaSolicitud().equals("")){
				inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), 
						new Long(miForm.getIdPersona()), 
						miForm.getFechaSolicitud(),
						true);
				
				//seteamos el paso siguiente
				miForm.setModo("vitConsultaGuardias");			
			}
			
			miForm.setObservacionesValidacion("");
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			forward = "consultaTurnoInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 2 - Alta Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vitConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && !miForm.getIdGuardia().equals("-1") && 
					miForm.getInscripcionTurno().getTurno().getGuardias()!=null && miForm.getInscripcionTurno().getTurno().getGuardias().intValue()==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR) {
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					new Integer(miForm.getIdGuardia()));
				
			} else {
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null);
			}
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			miForm.setModo("vitDatos");
			forward = "consultaGuardiasInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 3 - Alta Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vitDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(false);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(true);
			miForm.setValidacionBaja(false);
			miForm.setMasivo(false);
			//seteamos el paso siguiente
			miForm.setModo("vitValidar");
			boolean isAlgunaGuardiaPorGrupo = false;
			String guardiasSeleccionadas = miForm.getGuardiasSel();
			if(guardiasSeleccionadas!=null&&!guardiasSeleccionadas.equals("")){
				guardiasSeleccionadas = guardiasSeleccionadas.substring(0,guardiasSeleccionadas.lastIndexOf("@"));
				List<String> guardiasSeleccionadasList = null;
				if(guardiasSeleccionadas!=null && !guardiasSeleccionadas.equals("")){
					String[] guardiasSel = guardiasSeleccionadas.split("@");
					guardiasSeleccionadasList= Arrays.asList(guardiasSel);
				}
				if(guardiasSeleccionadasList!=null && guardiasSeleccionadasList.size()>0){
					for(ScsInscripcionGuardiaBean insGuardia:miForm.getInscripcionesGuardia()){
						if(guardiasSeleccionadasList.contains(insGuardia.getGuardia().getIdGuardia().toString())&& insGuardia.getGuardia().getPorGrupos()!=null && insGuardia.getGuardia().getPorGrupos().equals("1")){
							isAlgunaGuardiaPorGrupo = true;
							break;
						}
					}
				}
			}
			
			if(isAlgunaGuardiaPorGrupo){
				miForm.setPorGrupos("1");
			}else{
				miForm.setPorGrupos("0");
				
			}
			
//			FIXME AAAÑADIR SELECCIÓN DE GRUPO vitDatos ok
			//COMPROBAR SI EXISTE ALGUNA GUARDIA DEL TURNO QUE SEA DE GRUPO Y EN TAL CASO
			//SACAR UN LISTADO CON LAS GRUPOS DE LA GUARDIA PARA QUE SEA OBLIGATORIO LA ELECCION DE UNO
			forward = "validarInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 3 - Alta Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vigDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(false);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(true);
			miForm.setValidacionBaja(false);
			miForm.setMasivo(false);
			//seteamos el paso siguiente
			miForm.setModo("vigValidar");
			
			if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
				ArrayList<LetradoInscripcion> letradosColaGuardiaList = InscripcionGuardia.getColaGuardia(
						new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), 
						new Integer(miForm.getIdGuardia()), 
						"sysdate",
						"sysdate", 
						this.getUserBean(request));
				
				if(letradosColaGuardiaList!=null && !letradosColaGuardiaList.isEmpty()){
					miForm.setGruposGuardiaLetrado(letradosColaGuardiaList);
					
				}else{
					miForm.setGruposGuardiaLetrado(new ArrayList<LetradoInscripcion>());
				}
			}
			
//			FIXME AAAÑADIR SELECCIÓN DE GRUPO OK vigDatos
			//COMPROBAR SI EXISTE ALGUNA GUARDIA DEL TURNO QUE SEA DE GRUPO Y EN TAL CASO
			//SACAR UN LISTADO CON LAS GRUPOS DE LA GUARDIA PARA QUE SEA OBLIGATORIO LA ELECCION DE UNO
			forward = "validarInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 3 - Baja Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbgDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(false);
			miForm.setSolicitudBaja(true);
			miForm.setValidacionAlta(false);
			miForm.setValidacionBaja(true);
			miForm.setMasivo(false);
			//seteamos el paso siguiente
			miForm.setModo("sbgComprobarInsertar");
			forward = "validarInscripcion";			
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 2 - Alta Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vigConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			Integer idGuardia=null;
			
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && 
					Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = Integer.parseInt(miForm.getIdGuardia());
			
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasParaInscripcion(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()),
					idGuardia);
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			
			miForm.setModo("vigDatos");
			forward = "consultaGuardiasInscripcion";			
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 2 - Alta Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sigConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			
			Integer idGuardia=null;			
			if (miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && 
					Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = Integer.parseInt(miForm.getIdGuardia());
			
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasParaInscripcion(
				new Integer(miForm.getIdInstitucion()),
				new Integer(miForm.getIdTurno()), 
				new Long(miForm.getIdPersona()),
				idGuardia);
			
			miForm.setInscripcionesGuardia(alInscripcionGuardia);			
			miForm.setModo("sigDatos");
			forward = "consultaGuardiasInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 1 - Baja Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbgConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
 		String forward = "";
		
		try {
			UsrBean usrBean = this.getUserBean(request);
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setFechaValidacionTurno(miForm.getFechaValidacion());
			miForm.reset(false,false);
//			String estadoPendientes = getEstadoGuardiasDesignasPendientes(
//						  usrBean, 
//						Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),new Integer(miForm.getIdGuardia()),null,null); 
//			miForm.setEstadoPendientes(estadoPendientes);
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
				new Integer(miForm.getIdInstitucion()),
				new Integer(miForm.getIdTurno()), 
				new Long(miForm.getIdPersona()), 
				null,
				true);		
						
			miForm.setInscripcionTurno(inscripcionTurno);			
			miForm.setObservacionesValidacion("");
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			
			miForm.setModo("vbgConsultaGuardias");
			forward = "consultaTurnoInscripcion";				
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 1 - Baja Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbtConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
 		String forward = "";
		
		try {
			UsrBean usrBean = this.getUserBean(request);
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setFechaValidacionTurno(miForm.getFechaValidacion());
			miForm.reset(false,false);
			miForm.setIdGuardia(null);
//			String estadoPendientes = getEstadoGuardiasDesignasPendientes(
//						  usrBean, 
//						Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),null,null,null); 
//			miForm.setEstadoPendientes(estadoPendientes);
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null,true);
						
						
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());			
			miForm.setModo("vbtConsultaGuardias");
			forward = "consultaTurnoInscripcion";				
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 2 - Baja Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbtConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && 
					miForm.getTipoGuardias()!=null && Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					new Integer(miForm.getIdGuardia()));
				
			} else {
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null);
			}
						
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			//seteamos el paso siguiente
			miForm.setModo("vbtDatos");

			forward = "consultaGuardiasInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 2 - Baja Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */	
	private String vbgConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && 
					miForm.getTipoGuardias()!=null && Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					new Integer(miForm.getIdGuardia()));
				
			} else {
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null);
			}
														
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			
			//seteamos el paso siguiente			
			miForm.setModo("vbgDatos");			
			forward = "consultaGuardiasInscripcion";			
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		
		return forward;
	}
	
	/**
	 * Paso 3 - Baja Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbgDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(false);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(false);
			miForm.setValidacionBaja(true);
			miForm.setMasivo(false);
			
			//seteamos el paso siguiente
			miForm.setModo("vbgComprobarValidar");
			forward = "validarInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 3 - Baja Inscripción Pendiente Turnos (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vbtDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(false);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(false);
			miForm.setValidacionBaja(true);
			miForm.setMasivo(false);
			//seteamos el paso siguiente
			miForm.setModo("vbtComprobarValidar");
			forward = "validarInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 1 - Alta Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sigConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.reset(true, true);
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
				new Integer(miForm.getIdInstitucion()),
				new Integer(miForm.getIdTurno()), 
				new Long(miForm.getIdPersona()), 
				miForm.getFechaSolicitudTurno(),
				true);			
						
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());			
			miForm.setModo("sigConsultaGuardias");
			forward = "consultaTurnoInscripcion";			
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 1 - Alta Inscripción Guardias Pendientes (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vigConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null,
					true);	
						
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			
			miForm.setModo("vigConsultaGuardias");
			forward = "consultaTurnoInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/**
	 * Paso 1 - Baja Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbgConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
 		String forward = "";
		
		try {
			UsrBean usrBean = this.getUserBean(request);
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setFechaValidacionTurno(miForm.getFechaValidacion());
			miForm.reset(false,true);
//			String estadoPendientes = getEstadoGuardiasDesignasPendientes(
//						  usrBean, 
//						Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),new Integer(miForm.getIdGuardia()),null,null); 
//			miForm.setEstadoPendientes(estadoPendientes);
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null,
					true);						
						
			miForm.setInscripcionTurno(inscripcionTurno);
			
			miForm.setObservacionesValidacion("");
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			
			miForm.setModo("sbgConsultaGuardias");
			forward = "consultaTurnoInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Paso 2 - Baja Inscripción Guardias (individual)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String sbgConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("") && 
					miForm.getTipoGuardias()!=null && Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					new Integer(miForm.getIdGuardia()));
				
			} else {
				alInscripcionGuardia = inscripcionGuardiaAdm.getInscripcionesGuardias(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null);
			}
						
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			//seteamos el paso siguiente
			miForm.setModo("sbgDatos");
			forward = "consultaGuardiasInscripcion";
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * 
	 * @param miForm
	 * @param usrBean
	 * @throws SIGAException
	 * @throws ClsExceptions
	 */
	private void comprobarRetencion(InscripcionTGForm miForm,UsrBean usrBean) throws SIGAException, ClsExceptions {
		// Comprobamos si el letrado actua como sociedad.
		
		// Si es 0, el letrado actua en modo propio
		String where = " where "+	ScsRetencionesIRPFBean.T_NOMBRETABLA+"."+ScsRetencionesIRPFBean.C_IDINSTITUCION +" = " + usrBean.getLocation()+
			" and "+ScsRetencionesIRPFBean.T_NOMBRETABLA+"."+ScsRetencionesIRPFBean.C_IDPERSONA+" = " + miForm.getIdPersona();
		
		ScsRetencionesIRPFAdm irpf = new ScsRetencionesIRPFAdm(usrBean);
		Vector vIrpf = irpf.selectTabla(where);
		if(vIrpf!=null && vIrpf.size()>0){
			miForm.setIrpf(String.valueOf(vIrpf.size()));
			
		} else {
			miForm.setIrpf("0");
			CenComponentesAdm cenComponentesAdm = new CenComponentesAdm(usrBean);
			where = " where CEN_CLIENTE_IDINSTITUCION ="+usrBean.getLocation()+
			" and CEN_CLIENTE_IDPERSONA = "+miForm.getIdPersona() +
			" and SOCIEDAD = " + ClsConstants.DB_TRUE;
			Vector vCenComponentes = cenComponentesAdm.select(where);
			
			if(vCenComponentes.size() == 0) {
				ScsRetencionesAdm admRetenciones = new ScsRetencionesAdm(usrBean);
				where = " where letranifsociedad is null or letranifsociedad = ''";				
				
				List vTipos = admRetenciones.getRetenciones(where,usrBean.getLanguage());
				if(vTipos!=null&&vTipos.size()>0)
					miForm.setRetenciones(vTipos);
				else
					throw new SIGAException("gratuita.retencionesIRPF.mensaje.error2");				
			
			// Si es > 0, el letrado actua como sociedad
			} else {
				CenComponentesBean sociedad = (CenComponentesBean) vCenComponentes.get(0);
				CenPersonaAdm cenPersonaAdm = new CenPersonaAdm(usrBean);
				where = " where IDPERSONA = "+sociedad.getIdPersona();
				Vector vCenPersona = cenPersonaAdm.select(where);
				
				if(vCenPersona.size()>0) {
					String letra = ((CenPersonaBean) vCenPersona.get(0)).getNIFCIF().substring(0,1);
					// Miramos si la letra tiene rentencion asociada.
					where = " where "+	ScsRetencionesBean.T_NOMBRETABLA+"."+ScsRetencionesBean.C_LETRANIFSOCIEDAD +" = '" + letra+"'";
					ScsRetencionesAdm irpf2 = new ScsRetencionesAdm(usrBean);
					List lIrpf2 = irpf2.getRetenciones(where,usrBean.getLanguage());
					
					if(lIrpf2!=null && lIrpf2.size()>0) {
						miForm.setRetenciones(lIrpf2);
						
					} else {
						throw new SIGAException("gratuita.retencionesIRPF.mensaje.error1");
					}
					
				} else {
					throw new SIGAException("messages.general.error");
				}				
			}
		}	
	}
	
	/**
	 * Paso 2 - Baja Inscripción Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String smbtInsertarBaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean hayEstadosPendientes = false;
			
			String sFechaBaja = miForm.getFechaBaja(); // Guardo la fecha de baja inicial
			
			//dando de baja todos los turnos
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idTurnoSel=d[0];
				String validarInscripciones=d[1];
				String fechaSolicitud=d[2];
				String fechaValidacionTurno=d[3];
				
				/* La baja de inscripcion se realiza desde la ficha de un colegiado.
				 * 
				 * Datos necesarios para preBajaTurno:
				 * - miForm.idInstitucion.......: viene el formulario con el dato incluido
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: viene el formulario con el dato incluido
				 * - miForm.fechaValidacionTurno: obtiene su valor de los datos masivos
				 * - miForm.validarInscripciones: obtiene su valor de los datos masivos
				 * - miForm.fechaBaja...........: viene el formulario con el dato incluido (para validarInscripciones igual a "N" da igual) 
				*/
				
				miForm.setIdTurno(idTurnoSel);				
				miForm.setFechaValidacionTurno(fechaValidacionTurno);
				miForm.setValidarInscripciones(validarInscripciones);
				
				miForm.setFechaBaja(sFechaBaja); // Copio la fecha de baja inicial, ya que se puede momdificar en preBajaTurno
				
				// Comprobaciones previas a dar de baja una inscripcion de turno
				String sPreBaja = this.preBajaTurno(miForm, usr, false);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreBaja != "") {
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}	
				
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
					usr, 
					Long.valueOf(miForm.getIdPersona()), 
					new Integer(miForm.getIdInstitucion()), 
					new Integer(miForm.getIdTurno()),
					null, 
					null,
					miForm.getFechaBaja(),
					false,
					this.tipoActualizacionBaja); 
				
				miForm.setEstadoPendientes(estadoPendientes);
				
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					hayEstadosPendientes = true;
				}	
			}
			
			miForm.setFechaBaja(sFechaBaja); // Copio la fecha de baja inicial, ya que se puede momdificar en preBajaTurno
			
			if(hayEstadosPendientes){
				miForm.setModo("smbtInsertar");
				return ClsConstants.SMS_AVISO_ESTADO;
				
			} else {
				return smbtInsertar(mapping, formulario, request, response);							
			}			
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
	}
	
	/**
	 * Paso 3 (FINAL) - Baja Inscripción Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String smbtInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;	
			
			miForm.setFechaSolicitudBaja("sysdate");
			String sFechaBaja = miForm.getFechaBaja(); // Guardo la fecha de baja inicial
			
			tx = usr.getTransaction();
			tx.begin();	
			
			//dando de baja todos los turnos
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idTurnoSel=d[0];
				String validarInscripciones=d[1];
				String fechaSolicitud=d[2];
				String fechaValidacionTurno=d[3];
				
				/* La baja de inscripcion se realiza desde la ficha de un colegiado.
				 * 
				 * Datos necesarios:
				 * - miForm.idInstitucion.......: viene el formulario con el dato incluido
				 * - miForm.idPersona...........: viene el formulario con el dato incluido
				 * - miForm.tipoActualizacionSyC: viene el formulario con el dato incluido
				 * 
				 * - miForm.observacionesBaja...: viene el formulario con el dato incluido de la jsp (Motivos Baja)
				 * - miForm.observacionesValBaja: viene el formulario con el dato incluido de la jsp (Observaciones Validacion)
				 * - miForm.fechaBaja...........: viene el formulario con el dato incluido de la jsp (para validarInscripciones igual a "N" da igual) 
				 * 
				 * - idTurnoSel.............: obtiene su valor de los datos masivos
				 * - validarInscripciones...: obtiene su valor de los datos masivos
				 * - fechaSolicitud.........: obtiene su valor de los datos masivos
				 * - fechaValidacionTurno...: obtiene su valor de los datos masivos
				 * 
				 * - miForm.fechaSolicitudBaja..: sysdate
				*/
				
				miForm.setIdTurno(idTurnoSel);				
				miForm.setFechaValidacionTurno(fechaValidacionTurno);
				miForm.setValidarInscripciones(validarInscripciones);
				
				miForm.setFechaBaja(sFechaBaja); // Copio la fecha de baja inicial, ya que se puede momdificar en preBajaTurno
				
				try {									
					// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
					ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
					insTurnoBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
					insTurnoBean.setIdTurno(new Integer(idTurnoSel));
					insTurnoBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
					insTurnoBean.setFechaSolicitud(fechaSolicitud);
					
					InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);					
					
					// Compruebo que es un turno con validaciones y bajas de inscripciones automaticas, entonces calculo la fecha de baja
					if(validarInscripciones.equals("N")) {
						
						// Calculo la fecha de baja para inscripciones automaticas
						this.preBajaTurno(miForm, usr, false);
					}
					
					inscripcion.solicitarBaja(
						miForm.getFechaSolicitudBaja(),
						miForm.getObservacionesBaja(),
						miForm.getFechaBaja(),
						miForm.getObservacionesValBaja(), 
						fechaValidacionTurno,
						miForm.getTipoActualizacionSyC(), 
						usr);
				
				} catch (Exception e) {
					existenErrores = true;
				}	
			}
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje", "gratuita.gestionInscripciones.error.solapamiento");
				//tx.rollback();
				tx.commit();
				
			} else {
				request.setAttribute("mensaje", "messages.updated.success");		
				tx.commit();
			}				
			
			forward = "exito";
			request.setAttribute("modal", "1");
			
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}	
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}	
	
	/**
	 * Paso 1 - Alta Inscripción Pendiente Turnos (masivo)
	 * Paso 1 - Baja Inscripción Pendiente Turnos (masivo)
	 * 
	 * Paso 1 - Alta Inscripción Pendiente Guardias (masivo)
	 * Paso 1 - Baja Inscripción Pendiente Guardias (masivo)
	 * 
	 * Paso 1 - Cambio Fecha Efectiva Alta Inscripción Turnos (masivo)
	 * Paso 1 - Cambio Fecha Efectiva Baja Inscripción Turnos (masivo)
	 * 
	 * Paso 1 - Cambio Fecha Efectiva Alta Inscripción Guardias (masivo)
	 * Paso 1 - Cambio Fecha Efectiva Baja Inscripción Guardias (masivo)
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String solicitudesMasivas (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";

		try {
			//Controles generales
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.reset(false,true);
			
			String clase = miForm.getClase();
			String tipo = miForm.getTipo();
			String estado = miForm.getEstado();
			miForm.setMasivo(true);
			miForm.setFechaBajaTurno(null);
			miForm.setFechaValidacionTurno(null);
			if(tipo.equals("A")){
				if(clase.equals("T")){
					
					if(estado.equals("P")){
						//validacion de turnos pendientes
						miForm.setSolicitudAlta(false);
						miForm.setSolicitudBaja(false);
						miForm.setValidacionAlta(true);
						miForm.setValidacionBaja(false);
						miForm.setModo("vmitValidar");
						miForm.setPorGrupos("1");
						
//						FIXME AAAÑADIR SELECCIÓN DE GRUPO solicitudesMasivas ok
						//COMPROBAR SI EXISTE ALGUNA GUARDIA DEL TURNO QUE SEA DE GRUPO Y EN TAL CASO
						//SACAR UN LISTADO CON LAS GRUPOS DE LA GUARDIA PARA QUE SEA OBLIGATORIO LA ELECCION DE UNO
						forward = "validarInscripcion";
						
					}else{
						miForm.setFechaValidacion(null);
						miForm.setObservacionesValidacion(null);
						miForm.setModo("comprobarAmfvtModificar");
						forward = "editarFechaValidacion";
					}
					
				}else{
					if(estado.equals("P")){
						//validacion de guardias pendientes
						//validacion de turnos pendientes
						miForm.setSolicitudAlta(false);
						miForm.setSolicitudBaja(false);
						miForm.setValidacionAlta(false);
						miForm.setValidacionBaja(true);
						miForm.setModo("vmigValidar");
						miForm.setPorGrupos("1");
						
//						FIXME AAAÑADIR SELECCIÓN DE GRUPO solicitudesMasivas ok
						//COMPROBAR SI EXISTE ALGUNA GUARDIA DEL TURNO QUE SEA DE GRUPO Y EN TAL CASO
						//SACAR UN LISTADO CON LAS GRUPOS DE LA GUARDIA PARA QUE SEA OBLIGATORIO LA ELECCION DE UNO
						forward = "validarInscripcion";
						
					}else{
						miForm.setFechaValidacion(null);
						miForm.setObservacionesValidacion(null);
						miForm.setModo("comprobarAmfvgModificar");
						forward = "editarFechaValidacion";						
					}					
				}
				
			}else{
				if(clase.equals("T")){
					if(estado.equals("P")){
						miForm.setSolicitudAlta(false);
						miForm.setSolicitudBaja(false);
						miForm.setValidacionAlta(false);
						miForm.setValidacionBaja(true);
						
						//validacion de turnos pendientes
						miForm.setModo("vmbtComprobarValidar");
						forward = "validarInscripcion";
						
					}else{
						//cambio de fecha efectiva de baja de turno
						miForm.setFechaBaja(null);
						miForm.setModo("comprobarAmfbtModificar");
						forward = "editarFechaBaja";
					}
					
				}else{
					if(estado.equals("P")){
						miForm.setSolicitudAlta(false);
						miForm.setSolicitudBaja(false);
						miForm.setValidacionAlta(false);
						miForm.setValidacionBaja(true);
						miForm.setFechaValidacion(null);
						miForm.setObservacionesValidacion(null);						
						// bajas de guardias pendientes
						miForm.setModo("vmbgComprobarValidar");
						forward = "validarInscripcion";
						
					}else{
						//cambio de fecha efectiva de baja de guardia
						miForm.setFechaBaja(null);
						miForm.setModo("comprobarAmfbgModificar");
						forward = "editarFechaBaja";
					}
				}				
			}

			//finalizando transaccion
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		}
		
		return forward;
	}
	
	/**
	 * Paso 2 (FINAL) - Alta Inscripción Pendiente Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vmitValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		try {			
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			
			tx = usr.getTransaction();
			tx.begin();	
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
//				String idGuardia= d[3];
				String fechaSolicitud= d[4];
				String validarInscripciones = d[5];
				//String tipoGuardias = d[6];
				
				/* La confirmacion de inscripciones de turno pendientes no se realizan desde la ficha de un colegiado.
				 * 
				 * Datos necesarios para PreAlta:
				 * - miForm.idInstitucion.......: obtiene su valor de los datos masivos
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: obtiene su valor de los datos masivos
				 * - miForm.validarInscripciones: "S" (Para inscripciones automaticas, se realiza como si no lo fueran)
				 * - miForm.fechaValidacion.....: viene incluido si quiere validar la inscripcion
				 * - miForm.fechaDenegacion.....: viene incluido si quiere denegar la inscripcion
				*/
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdPersona(idPersona);
				miForm.setIdTurno(idTurno);
								
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");
				
				try {
					if (miForm.getFechaValidacion() != null && !miForm.getFechaValidacion().equals("")) {
						// Hago una serie de comprobaciones previas al alta de la inscripcion del turno
						String sPreAlta = this.preAltaTurno(miForm, usr);
						
						// Compruebo si existe algun error en las comprobaciones
						if (sPreAlta != "") {
							request.setAttribute("mensaje", sPreAlta);
							return ClsConstants.ERROR_AVISO;	
						}				
						
					} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
						request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
						return ClsConstants.ERROR_AVISO;					
					}							
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
					ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
					insTurnoBean.setIdInstitucion(new Integer(idInstitucion));
					insTurnoBean.setIdTurno(new Integer(idTurno));
					insTurnoBean.setIdPersona(Long.valueOf(idPersona));
					insTurnoBean.setFechaSolicitud(fechaSolicitud);
					
					InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);
					
					if(miForm.getFechaDenegacion() != null && !miForm.getFechaDenegacion().equals("")) {
						inscripcion.denegarInscripcionTurno(
							miForm.getFechaDenegacion(), 
							miForm.getObservacionesDenegacion(), 
							usr);	
						
					} else if(miForm.getFechaValidacion() != null && !miForm.getFechaValidacion().equals("")) {
						inscripcion.validarAlta(
							miForm.getFechaValidacion(), 
							miForm.getObservacionesValidacion(), 
							usr);
					}
					
				} catch (Exception e) {
					existenErrores = true;
				}
			}
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.masivo.solapamiento");
				//tx.rollback();
				tx.commit();
				
			} else {
				request.setAttribute("mensaje","messages.updated.success");			
				tx.commit();
			}
			
			forward = "exito";
			request.setAttribute("modal", "1");

		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}				
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 2 (FINAL) - Alta Inscripción Pendiente Guardias (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vmigValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		try {	
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;			
			boolean isTodasOninguna = false;
			List<String> turnosTodosoNingunoList = new ArrayList<String>();
			
			//cuando es todas o ninguna solo es necesario llamar al proceso de validadcion una vez ya que este se encraga de validarlas todas a la vez
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String idGuardia = d[3];
				String fechaSolicitud= d[4];
				//String validarInscripciones = d[5];				
				String tipoGuardias = d[6];						
				
				if(Integer.parseInt(tipoGuardias)==ScsTurnoBean.TURNO_GUARDIAS_TODAS0NINGUNA){
					if(turnosTodosoNingunoList.contains(idTurno))
						continue;
					else
						turnosTodosoNingunoList.add(idTurno);
				}
				
				/* Datos necesarios para preAltaGuardia:
				 * - miForm.idInstitucion.......: obtiene su valor de los datos masivos
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: obtiene su valor de los datos masivos
				 * - miForm.idGuardia...........: obtiene su valor de los datos masivos
				 * - miForm.validarInscripciones: "S" (Para inscripciones automaticas, se realiza como si no lo fueran)
				 * 
				 * - miForm.observacionesValidacion: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion (Observaciones Validacion)
				 * - miForm.observacionesValBaja...: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaValidacion........: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion
				 * - miForm.observacionesDenegacion: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaDenegacion........: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion
				*/				
				
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(idTurno);				
				miForm.setIdPersona(idPersona);				
				miForm.setIdGuardia(idGuardia);				
				
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");
				
				try {
					if (miForm.getFechaValidacion() != null && !miForm.getFechaValidacion().equals("")) {
						// Hago una serie de comprobaciones previas al alta de la inscripcion de guardia
						String sPreAlta = this.preAltaGuardia(miForm, usr);
						
						// Compruebo si existe algun error en las comprobaciones
						if (sPreAlta != "") {
							request.setAttribute("mensaje", sPreAlta);
							return ClsConstants.ERROR_AVISO;	
						}				
						
					} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
						request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
						return ClsConstants.ERROR_AVISO;					
					}					
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
					ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
					insGuardiaBean.setIdInstitucion(new Integer(idInstitucion));					
					insGuardiaBean.setIdTurno(new Integer(idTurno));					
					insGuardiaBean.setIdGuardia(new Integer(idGuardia));					
					insGuardiaBean.setIdPersona(Long.valueOf(idPersona));
					insGuardiaBean.setFechaSuscripcion(fechaSolicitud);
					
					InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);					
						
					if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("") &&
						(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals(""))) {
						
						inscripcion.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());					
						
						inscripcion.validarAlta(usr);
						
					} else { 
						if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")) {
							inscripcion.setDenegacion(miForm.getObservacionesDenegacion(), miForm.getFechaDenegacion());
							
							inscripcion.denegarAltaGuardia(usr);
						}
					}
					
				} catch (Exception e) {
					existenErrores = true;
				}								
			} 
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.solapamiento"));
				
			}else{
				request.setAttribute("mensaje","messages.updated.success");				
			}			
			
			forward = "exito";
			request.setAttribute("modal", "1");

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 2 - Baja Inscripción Pendiente Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vmbtComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);		
		String forward = "error";
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean hayEstadosPendientes = false;
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
//				String idGuardia= d[3];
//				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
//				String tipoGuardias = d[6];
				String fechaValidacion = d[7];
				if(fechaValidacion!=null && fechaValidacion.equals("-"))
					fechaValidacion = null;
				//String fechaSolicitudBaja = d[8];		
				
				/* La baja de inscripcion se realiza desde la ficha de un colegiado.
				 * 
				 * Datos necesarios para preBajaTurno:
				 * - miForm.idInstitucion.......: viene el formulario con el dato incluido
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: viene el formulario con el dato incluido
				 * - miForm.fechaValidacionTurno: obtiene su valor de los datos masivos
				 * - miForm.validarInscripciones: "S" (Para inscripciones automaticas, se realiza como si no lo fueran)
				 * - miForm.fechaBaja...........: viene el formulario con el dato incluido
				*/				
				
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(idTurno);
				miForm.setIdPersona(idPersona);
				miForm.setFechaValidacionTurno(fechaValidacion);
				
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");												
				
				if (miForm.getFechaBaja() != null && !miForm.getFechaBaja().equals("")) {
					// Comprobaciones previas a dar de baja una inscripcion de turno
					String sPreBaja = this.preBajaTurno(miForm, usr, false);
					
					// Compruebo si existe algun error en las comprobaciones
					if (sPreBaja != "") {
						request.setAttribute("mensaje", sPreBaja);
						return ClsConstants.ERROR_AVISO;	
					}	
					
				} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
					request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
					return ClsConstants.ERROR_AVISO;					
				}
				
				if ((miForm.getFechaDenegacion()==null || miForm.getFechaDenegacion().equals("")) && 
					miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals("")){
				
					String estadoPendientes = getEstadoGuardiasDesignasPendientes(
						usr, 
						Long.valueOf(miForm.getIdPersona()), 
						new Integer(miForm.getIdInstitucion()), 
						new Integer(miForm.getIdTurno()),
						null, 
						null,
						miForm.getFechaBaja(),
						false,
						this.tipoActualizacionBaja); 
					
					miForm.setEstadoPendientes(estadoPendientes);
				}
				
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					hayEstadosPendientes = true;
				}
			}
			
			if(hayEstadosPendientes){
				miForm.setModo("vmbtValidar");
				return ClsConstants.SMS_AVISO_ESTADO;
				
			} else {
				return vmbtValidar(mapping, formulario, request, response);							
			}

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 		
	}
	
	/**
	 * Paso 3 (FINAL) - Baja Inscripción Pendiente Turnos (masivo)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vmbtValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);		
		String forward = "error";
		UserTransaction tx = null;
		
		try {			
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;		
			
			tx = usr.getTransaction();
			tx.begin();	
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
//				String idGuardia= d[3];
				String fechaSolicitud= d[4];
				String validarInscripciones = d[5];
//				String tipoGuardias = d[6];
				String fechaValidacion = d[7];
				if(fechaValidacion!=null && fechaValidacion.equals("-"))
					fechaValidacion = null;
				//String fechaSolicitudBaja = d[8];		
				
				/* Datos necesarios:
				 * - miForm.observacionesValBaja...: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaBaja..............: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion
				 * - miForm.observacionesDenegacion: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaDenegacion........: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion
				 * 
				 * - idInstitucion..........: obtiene su valor de los datos masivos 
				 * - idTurno................: obtiene su valor de los datos masivos 
				 * - idPersona..............: obtiene su valor de los datos masivos
				 * - fechaSolicitud.........: obtiene su valor de los datos masivos 
				 * - validarInscripciones...: obtiene su valor de los datos masivos 
				 * - fechaValidacion........: obtiene su valor de los datos masivos 
				*/					
				
				try {					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud 
					ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
					insTurnoBean.setIdInstitucion(new Integer(idInstitucion));
					insTurnoBean.setIdTurno(new Integer(idTurno));
					insTurnoBean.setIdPersona(Long.valueOf(idPersona));
					insTurnoBean.setFechaSolicitud(fechaSolicitud);
					
					InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);
					
					if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")){
						inscripcion.denegarBajaInscripcionTurno(
							miForm.getFechaDenegacion(),
							miForm.getObservacionesDenegacion(),
							usr);
						
					} else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals("")) {						
						inscripcion.validarBaja(
							miForm.getFechaBaja(), 
							fechaValidacion, 
							miForm.getObservacionesValBaja(), 
							validarInscripciones, 
							usr);					
					}
					
				} catch (Exception e) {
					existenErrores = true;
				}							
			} 
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje", "gratuita.gestionInscripciones.error.solapamiento");
				//tx.rollback();
				tx.commit();
				
			} else {
				request.setAttribute("mensaje", "messages.updated.success");
				tx.commit();
			}				
			
			forward = "exito";
			request.setAttribute("modal", "1");
			
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;		
	}	
	
	/**
	 * Paso 2 - Baja Inscripción Guardias Pendientes (masiva)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vmbgComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);		
		String forward = "error";
		
		try {		
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean hayEstadosPendientes = false;

			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
			
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String idGuardiaStr= d[3];
				String validarInscripciones = d[5];	
				String tipoGuardias = d[6];								
				String fechaValidacion = d[7];
				if(fechaValidacion!=null && fechaValidacion.equals("-"))
					fechaValidacion = null;			
				
				/* Datos necesarios para preBajaGuardia:
				 * - miForm.idInstitucion.......: obtiene su valor de los datos masivos
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: obtiene su valor de los datos masivos
				 * - miForm.idGuardia...........: obtiene su valor de los datos masivos
				 * - miForm.validarInscripciones: "S" (Para inscripciones automaticas, se realiza como si no lo fueran)
				 * 
				 * - miForm.observacionesValBaja...: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaBaja..............: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion
				 * - miForm.observacionesDenegacion: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaDenegacion........: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion
				*/					
				
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(idTurno);
				miForm.setIdPersona(idPersona);				
				miForm.setIdGuardia(idGuardiaStr);
								
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");
				
				if (miForm.getFechaBaja() != null && !miForm.getFechaBaja().equals("")) {
					// Comprobaciones previas a dar de baja una inscripcion de guardia
					String sPreBaja = this.preBajaGuardia(miForm, usr, false);
					
					// Compruebo si existe algun error en las comprobaciones
					if (sPreBaja != "") {
						request.setAttribute("mensaje", sPreBaja);
						return ClsConstants.ERROR_AVISO;	
					}	
					
				} else if (miForm.getFechaDenegacion() == null || miForm.getFechaDenegacion().equals("")) {
					request.setAttribute("mensaje", UtilidadesString.getMensajeIdioma(usr, "gratuita.altaTurnos.literal.alerFeVa"));
					return ClsConstants.ERROR_AVISO;					
				}				
				
				Integer idGuardia = null;
				if(Integer.parseInt(tipoGuardias)==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
					idGuardia = new Integer(idGuardiaStr);
							
				if(miForm.getFechaBaja()!=null &&!miForm.getFechaBaja().equals("")){
					String estadoPendientes = getEstadoGuardiasDesignasPendientes(
						usr, 
						Long.valueOf(idPersona), 
						new Integer(idInstitucion), 
						new Integer(idTurno),
						idGuardia,
						null, 
						miForm.getFechaBaja(),
						true,
						this.tipoActualizacionBaja);
					
					miForm.setEstadoPendientes(estadoPendientes);
					
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
					}
				}
			}
			
			if(hayEstadosPendientes){
				miForm.setModo("vmbgValidar");
				return ClsConstants.SMS_AVISO_ESTADO;
				
			} else {
				return vmbgValidar(mapping, formulario, request, response);								
			}

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
	}
	
	/**
	 * Paso 3 (FINAL) - Baja Inscripción Guardias Pendientes (masiva)
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String vmbgValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);		
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		try {					
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String idGuardiaStr= d[3];
				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
				String tipoGuardias = d[6];
				
				/* Datos necesarios:
				 * - idInstitucion.......: obtiene su valor de los datos masivos
				 * - idTurno.............: obtiene su valor de los datos masivos
				 * - idPersona...........: obtiene su valor de los datos masivos
				 * - idGuardiaStr........: obtiene su valor de los datos masivos
				 * - fechaSolicitud......: obtiene su valor de los datos masivos
				 * 
				 * - miForm.observacionesValBaja...: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaBaja..............: viene el formulario con el dato incluido de la jsp si quiere validar la inscripcion
				 * - miForm.observacionesDenegacion: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion (Observaciones Validacion)
				 * - miForm.fechaDenegacion........: viene el formulario con el dato incluido de la jsp si quiere denegar la inscripcion
				*/					
				
				Integer idGuardia = null;
				if(Integer.parseInt(tipoGuardias)==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
					idGuardia = new Integer(idGuardiaStr);				
				
				try {					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud 					
					ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
					insGuardiaBean.setIdInstitucion(new Integer(idInstitucion));					
					insGuardiaBean.setIdTurno(new Integer(idTurno));					
					insGuardiaBean.setIdGuardia(idGuardia);					
					insGuardiaBean.setIdPersona(Long.valueOf(idPersona));
					insGuardiaBean.setFechaSuscripcion(fechaSolicitud);
					
					InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);
					
					if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")) {
						inscripcion.setDenegacion(miForm.getObservacionesDenegacion(),miForm.getFechaDenegacion());
						
						inscripcion.denegarBajaGuardia(usr);
						
					} else {
						if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals("")) {					
							inscripcion.setBajas(null,null, miForm.getFechaBaja(),miForm.getObservacionesValBaja());
							
							inscripcion.validarBaja(usr, miForm.getTipoActualizacionSyC());
						}
					}									
					
				} catch (Exception e) {
					existenErrores = true;
				}
			} 
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.solapamiento");
				
			} else {
				request.setAttribute("mensaje","messages.updated.success");				
			}	
			
			forward = "exito";
			request.setAttribute("modal", "1");

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 2 - Cambio Fecha Efectiva Alta Inscripción Turnos (masivo)	
	 * 
	 * @param mapping
	 * @param formulario (idInstitucion + idTurno + idPersona + idGuardia + validarInscripciones + tipoGuardias + fechaSolicitud + fechaValidacion + observacionesValidacion + fechaValidacionPrevia)
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String comprobarAmfvtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		try {			
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				// String idGuardia= d[3];
				// String fechaSolicitud= d[4];
				// String validarInscripciones = d[5];
				// String tipoGuardias = d[6];
				String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];				
				
				 /* Datos necesarios para preAltaTurno:
				 * - miForm.idInstitucion.......: obtiene su valor de los datos masivos
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: obtiene su valor de los datos masivos
				 * - miForm.fechaValidacion.....: viene el formulario con el dato incluido
				 * - miForm.validarInscripciones: "S"
				 * 
				 * Otros:
				 * - miForm.fechaValidacionPrevia: obtiene su valor de los datos masivos 
				*/		
				
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(idTurno);
				miForm.setIdPersona(idPersona);
				miForm.setFechaValidacionPrevia(GstDate.getFormatedDateShort("", fechaValidacion));
				
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");
				
				// Hago una serie de comprobaciones previas al alta de la inscripcion del turno
				String sPreAlta = this.preAltaTurno(miForm, usr);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreAlta != "") {
					request.setAttribute("mensaje", sPreAlta);
					return ClsConstants.ERROR_AVISO;	
				}	
	
				String sFechaValidacion = miForm.getFechaValidacion(); // Fecha con formato dd/MM/yyyy
				String sFechaValidacionTurnoPrevia = miForm.getFechaValidacionPrevia(); // Fecha con formato dd/MM/yyyy
				
				// Resultado de la comparacion de la fecha de validacion, con la fecha de validacion del turno					
				int comparacion = GstDate.compararFechas(sFechaValidacion, sFechaValidacionTurnoPrevia);
				
				// Si la fecha de validacion es mayor que la fecha de validacion del turno, muestro un error 
				if (comparacion > 0) {
					sPreAlta = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.menorigual") + " " + sFechaValidacionTurnoPrevia; 
					request.setAttribute("mensaje", sPreAlta);
					return ClsConstants.ERROR_AVISO;	
				}					
			}			
			
			return amfvtModificar(mapping, formulario, request, response);							
						
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 		
	}
	
	/**
	 * Paso 3 (FINAL) - Cambio Fecha Efectiva Alta Inscripción Turnos (masivo)	
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String amfvtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			
			tx = usr.getTransaction();
			tx.begin();	
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];				
				String idPersona= d[1];
				String idTurno= d[2];
				String fechaSolicitud= d[4];
				// String validarInscripciones = d[5];
				// String tipoGuardias = d[6];
				String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];			
				
				try {
					ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
					insTurnoBean.setIdInstitucion(new Integer(idInstitucion));
					insTurnoBean.setIdTurno(new Integer(idTurno));
					insTurnoBean.setIdPersona(Long.valueOf(idPersona));
					insTurnoBean.setFechaSolicitud(fechaSolicitud);
					insTurnoBean.setFechaValidacion(GstDate.getFormatedDateShort("", fechaValidacion)); // Fecha de validacion previa
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud + fechaValidacionPrevia
					InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);
					inscripcion.modificarFechaValidacion(miForm.getFechaValidacion(), miForm.getObservacionesValidacion(), usr);
					
				} catch (Exception e) {
					existenErrores = true;
				}					
			}			
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.masivo.solapamiento");
				//tx.rollback();
				tx.commit();
				
			}else{
				request.setAttribute("mensaje","messages.updated.success");			
				tx.commit();
			}
						
			forward = "exito";
			request.setAttribute("modal", "1");

		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}	
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 3 (FINAL) - Cambio Fecha Efectiva Alta Inscripción Guardias (masivo)
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String amfvgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String idGuardia= d[3];
				String fechaSolicitud= d[4];
				// String validarInscripciones = d[5];
				String tipoGuardias = d[6];
				// String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];
				
				Integer intGuardia = null;
				if(Integer.parseInt(tipoGuardias) == ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
					intGuardia = new Integer(idGuardia);
				
				try {
					ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
					insGuardiaBean.setIdInstitucion(new Integer(idInstitucion));
					insGuardiaBean.setIdTurno(new Integer(idTurno));
					insGuardiaBean.setIdGuardia(intGuardia);
					insGuardiaBean.setIdPersona(Long.valueOf(idPersona));
					insGuardiaBean.setFechaSuscripcion(fechaSolicitud);
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud
					InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);							
					inscripcion.modificarFechaValidacion(miForm.getFechaValidacion(), miForm.getObservacionesValidacion(), usr);
					
				} catch (Exception e) {
					existenErrores = true;
				}		
			}			
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.masivo.solapamiento");
				
			}else{
				request.setAttribute("mensaje","messages.updated.success");				
			}			
			
			forward = "exito";
			request.setAttribute("modal", "1");

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 2 - Cambiar fecha efectiva de alta de inscripciones de turno
	 * Paso 2 - Cambiar fecha efectiva de alta de inscripciones de guardia
	 * 
	 * @param mapping
	 * @param formulario (idInstitucion + idTurno + idPersona + idGuardia + validarInscripciones + tipoGuardias + fechaSolicitud + fechaValidacion + observacionesValidacion + fechaValidacionPrevia)
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws Exception
	 */
	protected String actualizarFechaValidacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, Exception {		
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);		
		
		if(miForm.getIdGuardia()!=null && miForm.getIdGuardia().equals("-1"))
			miForm.setIdGuardia("");
		
		// Compruebo si es un cambio de fecha efectiva de guardia
		if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")){
			
			// Para inscripciones automaticas, se realiza como si no lo fueran
			miForm.setValidarInscripciones("S");
			
			// Hago una serie de comprobaciones previas al alta de la inscripcion del turno
			String sPreAlta = this.preAltaGuardia(miForm, usr);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreAlta != "") {
				request.setAttribute("mensaje", sPreAlta);
				return ClsConstants.ERROR_AVISO;	
			}	
			
			String sFechaValidacion = miForm.getFechaValidacion(); // Fecha con formato dd/MM/yyyy
			
			// Obtiene la fecha de validacion previa de la guardia (no es el de la guardia anterior) 
			String sFechaValidacionGuardiaPrevia = miForm.getFechaValidacionPrevia(); // Fecha con formato dd/MM/yyyy
			
			// Resultado de la comparacion de la fecha de validacion, con la fecha de validacion de la guardia					
			int comparacion = GstDate.compararFechas(sFechaValidacion, sFechaValidacionGuardiaPrevia);
			
			// Si la fecha de validacion es mayor que la fecha de validacion del turno, muestro un error 
			if (comparacion > 0) {
				sPreAlta = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.menorigual") + " " + sFechaValidacionGuardiaPrevia; 
				request.setAttribute("mensaje", sPreAlta);
				return ClsConstants.ERROR_AVISO;	
			}				
			
			return afvgModificar(mapping, formulario, request, response);
		
		// Compruebo si es un cambio de fecha efectiva de turno
		} else {
			
			// Para inscripciones automaticas, se realiza como si no lo fueran
			miForm.setValidarInscripciones("S");
			
			// Hago una serie de comprobaciones previas al alta de la inscripcion del turno
			String sPreAlta = this.preAltaTurno(miForm, usr);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreAlta != "") {
				request.setAttribute("mensaje", sPreAlta);
				return ClsConstants.ERROR_AVISO;	
			}	

			String sFechaValidacion = miForm.getFechaValidacion(); // Fecha con formato dd/MM/yyyy
			String sFechaValidacionTurnoPrevia = miForm.getFechaValidacionPrevia(); // Fecha con formato dd/MM/yyyy
			
			// Resultado de la comparacion de la fecha de validacion, con la fecha de validacion del turno					
			int comparacion = GstDate.compararFechas(sFechaValidacion, sFechaValidacionTurnoPrevia);
			
			// Si la fecha de validacion es mayor que la fecha de validacion del turno, muestro un error 
			if (comparacion > 0) {
				sPreAlta = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.menorigual") + " " + sFechaValidacionTurnoPrevia; 
				request.setAttribute("mensaje", sPreAlta);
				return ClsConstants.ERROR_AVISO;	
			}	
			
			return afvtModificar(mapping, formulario, request, response);
		}	
	}
	
	/**
	 * Paso 2 - Cambiar fecha efectiva de baja de inscripciones de turno
	 * Paso 2 - Cambiar fecha efectiva de baja de inscripciones de guardia
	 * 
	 * @param mapping
	 * @param formulario (idInstitucion + idTurno + idPersona + idGuardia + validarInscripciones + fechaBaja + fechaBajaPrevia + tipoGuardias + fechaSolicitud + observacionesValBaja + fechaValidacion) 
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws Exception
	 */
	protected String actualizarFechaBaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, Exception {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);		
		
		if(miForm.getIdGuardia()!=null && miForm.getIdGuardia().equals("-1"))
			miForm.setIdGuardia("");
		
		// Compruebo si es un cambio de fecha efectiva de guardia
		if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")){
			// Para inscripciones automaticas, se realiza como si no lo fueran
			miForm.setValidarInscripciones("S");
			
			// Hago una serie de comprobaciones previas a la baja de la inscripcion de la guardia
			String sPreBaja = this.preBajaGuardia(miForm, usr, true);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreBaja != "") {
				request.setAttribute("mensaje", sPreBaja);
				return ClsConstants.ERROR_AVISO;	
			}	

			String sFechaBaja = miForm.getFechaBaja(); // Fecha con formato dd/MM/yyyy
			
			// Obtiene la fecha de baja previa de la guardia (no es el de la guardia anterior) 
			String sFechaBajaGuardia = miForm.getFechaBajaPrevia(); // Fecha con formato dd/MM/yyyy
			
			// Resultado de la comparacion de la fecha de baja, con la fecha de baja previa de la guardia				
			int comparacion = GstDate.compararFechas(sFechaBaja, sFechaBajaGuardia);
			
			// Si la fecha de baja es menor que la fecha de baja previa, muestro un error 
			if (comparacion < 0) {
				sPreBaja = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.mayorigual") + " " + sFechaBajaGuardia; 
				request.setAttribute("mensaje", sPreBaja);
				return ClsConstants.ERROR_AVISO;	
			}	
			
			// Obtengo los datos del turno actual
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
			ScsInscripcionTurnoBean insTurno = inscripcionTurnoAdm.getInscripcionTurnoUltimaValidada(
				new Integer(miForm.getIdInstitucion()),
				new Integer(miForm.getIdTurno()), 
				new Long(miForm.getIdPersona()));
			
			// Controlo que no supere la fecha de baja del turno actual					
			if (insTurno!=null && insTurno.getFechaBaja()!=null && !insTurno.getFechaBaja().equals("")) { 
				String sFechaBajaTurno = insTurno.getFechaBaja();				
				
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
				Date dFechaBajaTurno = GstDate.convertirFechaHora(sFechaBajaTurno);
				sFechaBajaTurno = sdf.format(dFechaBajaTurno);
				
				// Resultado de la comparacion de la fecha de baja, con la fecha de baja del turno					
				comparacion = GstDate.compararFechas(sFechaBaja, sFechaBajaTurno);
				
				// Si la fecha de baja es mayor que la fecha de baja del turno, muestro un error 
				if (comparacion > 0) {
					sPreBaja = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.menorigual") + " " + sFechaBajaTurno; 
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}	
			}
			
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")) {								
				
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
				  usr, 
				  Long.valueOf(miForm.getIdPersona()), 
				  new Integer(miForm.getIdInstitucion()), 
				  new Integer(miForm.getIdTurno()),
				  null,
				  null,
				  miForm.getFechaBaja(),
				  false,
				  this.tipoActualizacionBaja); 
				
				miForm.setEstadoPendientes(estadoPendientes);				
				
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("afbgModificar");
					return ClsConstants.SMS_AVISO_ESTADO;
					
				} else {
					return afbgModificar(mapping,  formulario,  request,  response);
				}
				
			} else {				
				return afbgModificar(mapping,  formulario,  request,  response);				
			}
			
		// Compruebo si es un cambio de fecha efectiva de turno	
		} else {
			// Para inscripciones automaticas, se realiza como si no lo fueran
			miForm.setValidarInscripciones("S");
			miForm.setFechaValidacionTurno(miForm.getFechaValidacion());
			
			// Hago una serie de comprobaciones previas a la baja de la inscripcion del turno
			String sPreBaja = this.preBajaTurno(miForm, usr, true);
			
			// Compruebo si existe algun error en las comprobaciones
			if (sPreBaja != "") {
				request.setAttribute("mensaje", sPreBaja);
				return ClsConstants.ERROR_AVISO;	
			}	

			String sFechaBaja = miForm.getFechaBaja(); // Fecha con formato dd/MM/yyyy
			String sFechaBajaTurno = miForm.getFechaBajaPrevia(); // Fecha con formato dd/MM/yyyy
			
			// Resultado de la comparacion de la fecha de baja, con la fecha de baja previa				
			int comparacion = GstDate.compararFechas(sFechaBaja, sFechaBajaTurno);
			
			// Si la fecha de baja es menor que la fecha de baja previa, muestro un error 
			if (comparacion < 0) {
				sPreBaja = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.mayorigual") + " " + sFechaBajaTurno; 
				request.setAttribute("mensaje", sPreBaja);
				return ClsConstants.ERROR_AVISO;	
			}	
			
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")) {								
				
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
				  usr, 
				  Long.valueOf(miForm.getIdPersona()), 
				  new Integer(miForm.getIdInstitucion()), 
				  new Integer(miForm.getIdTurno()),
				  null,
				  null,
				  miForm.getFechaBaja(),
				  false,
				  this.tipoActualizacionBaja); 
				
				miForm.setEstadoPendientes(estadoPendientes);				
				
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("afbtModificar");
					return ClsConstants.SMS_AVISO_ESTADO;
					
				} else {
					return afbtModificar(mapping,  formulario,  request,  response);
				}
				
			} else {				
				return afbtModificar(mapping,  formulario,  request,  response);				
			}
		}		
	}
	
	/**
	 * Consulta la inscripcion
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String consultaInscripcion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return "consultaInscripcion";
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String busquedaTurnosDisponibles(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		miForm.clear();
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("BUSQUEDAREALIZADA");
		request.getSession().removeAttribute("accionTurno");
		request.getSession().removeAttribute("pestanas");
		return "busquedaTurnosDisponibles";
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String busquedaTurnosDisponiblesBaja(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		miForm.clear();
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("BUSQUEDAREALIZADA");
		request.getSession().removeAttribute("accionTurno");
		request.getSession().removeAttribute("pestanas");
		return "busquedaTurnosDisponiblesBaja";
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String listadoTurnosDisponibles(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String forward = "listadoTurnosDisponibles";
		try {
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.getUserBean(request));
			Hashtable hash = (Hashtable)formulario.getDatos();

			request.getSession().setAttribute("DATOSFORMULARIO",hash);
			request.getSession().setAttribute("BUSQUEDAREALIZADA","SI");
			Vector vTurno = turnoAdm.getTurnosDisponibles(
				hash,
				new Long((String)request.getSession().getAttribute("idPersonaTurno")),
				new Integer(usr.getLocation()));
			request.setAttribute("resultado",vTurno);
			request.setAttribute("mantTurnos","1");
			request.setAttribute("idPersonaTurno",(String)request.getSession().getAttribute("idPersonaTurno"));

		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		
		return forward;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected String listadoTurnosDisponiblesBaja(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String forward = "listadoTurnosDisponiblesBaja";
		try {
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.getUserBean(request));
			Hashtable hash = (Hashtable)formulario.getDatos();

			request.getSession().setAttribute("DATOSFORMULARIO",hash);
			request.getSession().setAttribute("BUSQUEDAREALIZADA","SI");
			Vector vTurno = turnoAdm.getTurnosDisponiblesBaja(
					hash,
					new Long((String)request.getSession().getAttribute("idPersonaTurno")),
					new Integer(usr.getLocation()));
			request.setAttribute("resultado",vTurno);
			request.setAttribute("mantTurnos","1");
			request.setAttribute("idPersonaTurno",(String)request.getSession().getAttribute("idPersonaTurno"));

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		
		return forward;
	}

	/**
	 * Paso 3 (FINAL) - Cambiar fecha efectiva de alta de inscripciones de turno
	 * 
	 * @param mapping
	 * @param formulario (idInstitucion + idTurno + idPersona + fechaSolicitud + fechaValidacion + observacionesValidacion + fechaValidacionPrevia)
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String afvtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		try {
			ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
			insTurnoBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insTurnoBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insTurnoBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insTurnoBean.setFechaSolicitud(miForm.getFechaSolicitud());
			insTurnoBean.setFechaValidacion(miForm.getFechaValidacionPrevia()); // Fecha de validacion previa
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud + fechaValidacionPrevia
			InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);
			
			tx = usr.getTransaction();
			tx.begin();	
				
			inscripcion.modificarFechaValidacion(miForm.getFechaValidacion(), miForm.getObservacionesValidacion(), usr);
			
			tx.commit();
			
			miForm.reset(true,true);				
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
	        
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}	
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 2 - Cambio Fecha Efectiva Alta Inscripción Guardias (masivo)
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String comprobarAmfvgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");

			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String idGuardia= d[3];
				//String fechaSolicitud= d[4];
				//String validarInscripciones = d[5];
				//String tipoGuardias = d[6];
				String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];	
				
				 /* Datos necesarios para preAltaGuardia: 
				 * - miForm.idInstitucion.......: obtiene su valor de los datos masivos
				 * - miForm.idTurno.............: obtiene su valor de los datos masivos
				 * - miForm.idGuardia...........: obtiene su valor de los datos masivos
				 * - miForm.idPersona...........: obtiene su valor de los datos masivos
				 * - miForm.fechaValidacion.....: viene el formulario con el dato incluido
				 * - miForm.validarInscripciones: "S"
				 * 
				 * Otros:
				 * - miForm.fechaValidacionPrevia: obtiene su valor de los datos masivos 
				*/		
				
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(idTurno);
				miForm.setIdPersona(idPersona);
				miForm.setIdGuardia(idGuardia);
				miForm.setFechaValidacionPrevia(GstDate.getFormatedDateShort("", fechaValidacion));
				
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");
				
				// Hago una serie de comprobaciones previas al alta de la inscripcion de guardia
				String sPreAlta = this.preAltaGuardia(miForm, usr);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreAlta != "") {
					request.setAttribute("mensaje", sPreAlta);
					return ClsConstants.ERROR_AVISO;	
				}	
	
				String sFechaValidacion = miForm.getFechaValidacion(); // Fecha con formato dd/MM/yyyy
				String sFechaValidacionTurnoPrevia = miForm.getFechaValidacionPrevia(); // Fecha con formato dd/MM/yyyy
				
				// Resultado de la comparacion de la fecha de validacion, con la fecha de validacion previa				
				int comparacion = GstDate.compararFechas(sFechaValidacion, sFechaValidacionTurnoPrevia);
				
				// Si la fecha de validacion es mayor que la fecha de validacion previa, muestro un error 
				if (comparacion > 0) {
					sPreAlta = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.menorigual") + " " + sFechaValidacionTurnoPrevia; 
					request.setAttribute("mensaje", sPreAlta);
					return ClsConstants.ERROR_AVISO;	
				}		
			}
			
			return amfvgModificar(mapping, formulario, request, response);
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 		
	}
	
	/**
	 * Paso 3 (FINAL) - Cambiar fecha efectiva de alta de inscripciones de guardia
	 * 
	 * @param mapping
	 * @param formulario (idInstitucion + idTurno + idPersona + idGuardia + tipoGuardias + fechaSolicitud + fechaValidacion + observacionesValidacion)
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String afvgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		
		try {
			ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
			insGuardiaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insGuardiaBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insGuardiaBean.setIdGuardia(idGuardia);
			insGuardiaBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insGuardiaBean.setFechaSuscripcion(miForm.getFechaSolicitud());
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud
			InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);				
			
			inscripcion.modificarFechaValidacion(miForm.getFechaValidacion(), miForm.getObservacionesValidacion(), usr);
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 3 (FINAL) - Cambiar fecha efectiva de baja de inscripciones de turno
	 * @param mapping
	 * @param formulario (idInstitucion + idTurno + idPersona + fechaSolicitud + fechaBaja + observacionesValBaja + fechaBajaPrevia) 
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String afbtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;
		
		try {			
			ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
			insTurnoBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insTurnoBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insTurnoBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insTurnoBean.setFechaSolicitud(miForm.getFechaSolicitud());
			insTurnoBean.setFechaBaja(miForm.getFechaBajaPrevia());
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud + fechaBajaPrevia
			InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);
				
			tx = usr.getTransaction();
			tx.begin();
			inscripcion.modificarFechaBaja(miForm.getFechaBaja(), miForm.getObservacionesValBaja(), usr);
			tx.commit();
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
	        
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}
			
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;
	}
	
	/**
	 * Paso 3 (FINAL) - Cambiar fecha efectiva de baja de inscripciones de guardia
	 * 
	 * @param mapping
	 * @param formulario (idInstitucion + idTurno + idPersona + idGuardia + tipoGuardias + fechaSolicitud + fechaBaja + observacionesValBaja) 
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String afbgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		
		try {
			ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
			insGuardiaBean.setIdInstitucion(new Integer(miForm.getIdInstitucion()));
			insGuardiaBean.setIdTurno(new Integer(miForm.getIdTurno()));
			insGuardiaBean.setIdGuardia(idGuardia);
			insGuardiaBean.setIdPersona(Long.valueOf(miForm.getIdPersona()));
			insGuardiaBean.setFechaSuscripcion(miForm.getFechaSolicitud());
			
			// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud
			InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);					
				
			inscripcion.modificarFechaBaja(miForm.getFechaBaja(), miForm.getObservacionesValBaja(), usr);
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 2 - Cambio Fecha Efectiva Baja Inscripción Turnos (masivo)
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String comprobarAmfbtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean hayEstadosPendientes = false;
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				// String idGuardia= d[3];
				// String fechaSolicitud= d[4];
				// String validarInscripciones = d[5];
				// String tipoGuardias = d[6];
				String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];
				String fechaBaja = d[9];
				
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(idTurno);
				miForm.setIdPersona(idPersona);
				miForm.setFechaValidacionTurno(fechaValidacion);
				miForm.setFechaBajaPrevia(GstDate.getFormatedDateShort("", fechaBaja));
				
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");
				
				// Hago una serie de comprobaciones previas al alta de la inscripcion de turno
				String sPreBaja = this.preBajaTurno(miForm, usr, true);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreBaja != "") {
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}
				
				String sFechaBaja = miForm.getFechaBaja(); // Fecha con formato dd/MM/yyyy
				String sFechaBajaTurno = miForm.getFechaBajaPrevia(); // Fecha con formato dd/MM/yyyy
				
				// Resultado de la comparacion de la fecha de baja, con la fecha de baja previa				
				int comparacion = GstDate.compararFechas(sFechaBaja, sFechaBajaTurno);
				
				// Si la fecha de baja es menor que la fecha de baja previa, muestro un error 
				if (comparacion < 0) {
					sPreBaja = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.mayorigual") + " " + sFechaBajaTurno; 
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}	
				
				if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")) {								
					
					String estadoPendientes = getEstadoGuardiasDesignasPendientes(
					  usr, 
					  Long.valueOf(miForm.getIdPersona()), 
					  new Integer(miForm.getIdInstitucion()), 
					  new Integer(miForm.getIdTurno()),
					  null,
					  null,
					  miForm.getFechaBaja(),
					  false,
					  this.tipoActualizacionBaja); 
					
					miForm.setEstadoPendientes(estadoPendientes);			
					
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
					}								
				}
			}
				
			if(hayEstadosPendientes){
				miForm.setModo("amfbtModificar");
				return ClsConstants.SMS_AVISO_ESTADO;
				
			} else {
				return amfbtModificar(mapping, formulario, request, response);							
			}								
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 		
	}
	
	/**
	 * Paso 3 (FINAL) - Cambio Fecha Efectiva Baja Inscripción Turnos (masivo)
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String amfbtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		UserTransaction tx = null;

		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");			
			boolean existenErrores = false;
			
			tx = usr.getTransaction();
			tx.begin();
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				// String idGuardia= d[3];
				String fechaSolicitud= d[4];
				// String validarInscripciones = d[5];
				// String tipoGuardias = d[6];
				// String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];
				String fechaBaja = d[9];
				
				try {				
					ScsInscripcionTurnoBean insTurnoBean = new ScsInscripcionTurnoBean();
					insTurnoBean.setIdInstitucion(new Integer(idInstitucion));
					insTurnoBean.setIdTurno(new Integer(idTurno));
					insTurnoBean.setIdPersona(Long.valueOf(idPersona));
					insTurnoBean.setFechaSolicitud(fechaSolicitud);
					insTurnoBean.setFechaBaja(GstDate.getFormatedDateShort("", fechaBaja));
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idPersona + fechaSolicitud + fechaBajaPrevia
					InscripcionTurno inscripcion = new InscripcionTurno(insTurnoBean);					
					
					inscripcion.modificarFechaBaja(miForm.getFechaBaja(), miForm.getObservacionesValBaja(), usr);					
					
				} catch (Exception e) {					
					existenErrores = true;
				}	
			}
						
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.masivo.solapamiento");
				//tx.rollback();
				tx.commit();
				
			}else{
				request.setAttribute("mensaje","messages.updated.success");	
				tx.commit();
			}
						
			forward = "exito";
			request.setAttribute("modal", "1");

		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * Paso 2 - Cambio Fecha Efectiva Baja Inscripción Guardias (masivo)
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String comprobarAmfbgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean hayEstadosPendientes = false;
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String idGuardia= d[3];
				// String fechaSolicitud= d[4];
				// String validarInscripciones = d[5];
				// String tipoGuardias = d[6];
				// String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];
				String fechaBaja = d[9];
				
				miForm.setIdInstitucion(idInstitucion);
				miForm.setIdTurno(idTurno);
				miForm.setIdPersona(idPersona);
				miForm.setFechaBajaPrevia(GstDate.getFormatedDateShort("", fechaBaja));
				
				if(idGuardia!=null && idGuardia.equals("-1"))
					miForm.setIdGuardia("");
				else
					miForm.setIdGuardia(idGuardia);
				
				// Para inscripciones automaticas, se realiza como si no lo fueran
				miForm.setValidarInscripciones("S");
			
				// Hago una serie de comprobaciones previas a la baja de la inscripcion de la guardia
				String sPreBaja = this.preBajaGuardia(miForm, usr, true);
				
				// Compruebo si existe algun error en las comprobaciones
				if (sPreBaja != "") {
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}	

				String sFechaBaja = miForm.getFechaBaja(); // Fecha con formato dd/MM/yyyy
			
				// Obtiene la fecha de baja previa de la guardia (no es el de la guardia anterior) 
				String sFechaBajaGuardia = miForm.getFechaBajaPrevia(); // Fecha con formato dd/MM/yyyy
			
				// Resultado de la comparacion de la fecha de baja, con la fecha de baja previa de la guardia				
				int comparacion = GstDate.compararFechas(sFechaBaja, sFechaBajaGuardia);
			
				// Si la fecha de baja es menor que la fecha de baja previa, muestro un error 
				if (comparacion < 0) {
					sPreBaja = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.mayorigual") + " " + sFechaBajaGuardia; 
					request.setAttribute("mensaje", sPreBaja);
					return ClsConstants.ERROR_AVISO;	
				}	
			
				// Obtengo los datos del turno actual
				ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(usr);
				ScsInscripcionTurnoBean insTurno = inscripcionTurnoAdm.getInscripcionTurnoUltimaValidada(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()));
				
				// Controlo que no supere la fecha de baja del turno actual					
				if (insTurno!=null && insTurno.getFechaBaja()!=null && !insTurno.getFechaBaja().equals("")) { 
					String sFechaBajaTurno = insTurno.getFechaBaja();				
					
					SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
					Date dFechaBajaTurno = GstDate.convertirFechaHora(sFechaBajaTurno);
					sFechaBajaTurno = sdf.format(dFechaBajaTurno);
					
					// Resultado de la comparacion de la fecha de baja, con la fecha de baja del turno					
					comparacion = GstDate.compararFechas(sFechaBaja, sFechaBajaTurno);
					
					// Si la fecha de baja es mayor que la fecha de baja del turno, muestro un error 
					if (comparacion > 0) {
						sPreBaja = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.menorigual") + " " + sFechaBajaTurno; 
						request.setAttribute("mensaje", sPreBaja);
						return ClsConstants.ERROR_AVISO;	
					}	
				}
				
				if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")) {								
					
					String estadoPendientes = getEstadoGuardiasDesignasPendientes(
					  usr, 
					  Long.valueOf(miForm.getIdPersona()), 
					  new Integer(miForm.getIdInstitucion()), 
					  new Integer(miForm.getIdTurno()),
					  null,
					  null,
					  miForm.getFechaBaja(),
					  false,
					  this.tipoActualizacionBaja); 
					
					miForm.setEstadoPendientes(estadoPendientes);				
					
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
					}
				}				
			}
			
			if(hayEstadosPendientes){
				miForm.setModo("amfbgModificar");
				return ClsConstants.SMS_AVISO_ESTADO;
				
			} else {
				return amfbgModificar(mapping, formulario, request, response);							
			}	
			
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 		
	}
	
	/**
	 * Paso 3 (FINAL) - Cambio Fecha Efectiva Baja Inscripción Guardias (masivo)
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String amfbgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		
		// JPT: No hacen falta transaccion para las funcionalidades de guardias
		
		try {
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String idGuardia= d[3];
				String fechaSolicitud= d[4];
				// String validarInscripciones = d[5];
				String tipoGuardias = d[6];
				// String fechaValidacion = d[7];
				// String fechaSolicitudBaja = d[8];
				// String fechaBaja = d[9];
				
				Integer intGuardia = null;
				if(Integer.parseInt(tipoGuardias) == ScsTurnoBean.TURNO_GUARDIAS_ELEGIR &&
					idGuardia!=null && !idGuardia.equals("-1") && !idGuardia.equals(""))
						intGuardia = new Integer(idGuardia);
				
				try {
					ScsInscripcionGuardiaBean insGuardiaBean = new ScsInscripcionGuardiaBean();
					insGuardiaBean.setIdInstitucion(new Integer(idInstitucion));
					insGuardiaBean.setIdTurno(new Integer(idTurno));
					insGuardiaBean.setIdGuardia(intGuardia);
					insGuardiaBean.setIdPersona(Long.valueOf(idPersona));
					insGuardiaBean.setFechaSuscripcion(fechaSolicitud);
					
					// Creo el objeto inscripcion con idInstitucion + idTurno + idGuardia + idPersona + fechaSolicitud
					InscripcionGuardia inscripcion = new InscripcionGuardia(insGuardiaBean);					
					inscripcion.modificarFechaBaja(miForm.getFechaBaja(), miForm.getObservacionesValBaja(), usr);
					
				} catch (Exception e) {
					existenErrores = true;
				}
			}			
			
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.masivo.solapamiento");
				
			} else {
				request.setAttribute("mensaje","messages.updated.success");				
			}
						
			forward = "exito";
			request.setAttribute("modal", "1");

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return forward;		
	}
	
	/**
	 * JPT: Borrar un turno que está en estado de alta pendiente
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String borrarTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			UsrBean usr = this.getUserBean(request);
			ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
			
			tx = usr.getTransaction();
			tx.begin();				
			
			boolean resultado = admInsTurno.borrarInscripcionTurnoPendiente(miForm.getIdInstitucion(), miForm.getIdTurno(), miForm.getIdPersona(), miForm.getFechaSolicitud());
			
			if(!resultado){
				request.setAttribute("mensaje", "messages.updated.error");
				tx.rollback();
				
			} else {
				request.setAttribute("mensaje", "messages.updated.success");
				tx.commit();
			}

		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {
			}	
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}

		return "exito";
	}
	
	/**
	 * JPT: Comprobaciones previas a dar de alta una inscripcion de turno
	 * 
	 * Controles:
	 * 1. si automatico, entonces calculo la fecha de validación: fechaValidación = MAX(fechaBaja_UltimoTurno + 1, SYSDATE)
	 * 2. sino fechaValidacion > fechaBaja_UltimoTurno
	 * 
	 * @param miForm  (idInstitucion + idTurno + idPersona + validarInscripciones + fechaValidacion)
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	private String preAltaTurno(InscripcionTGForm miForm, UsrBean usr) throws SIGAException {
		String resultado = "";
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		
		try {			
			// Obtengo la ultima inscripcion de turno
			ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
			ScsInscripcionTurnoBean insTurnoUltimaConBaja = admInsTurno.getInscripcionTurno(
					new Integer(miForm.getIdInstitucion()), 
					new Integer( miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()), 
					null);
			
			// Compruebo que existe una ultima inscripcion de turno con baja
			boolean bInsTurnoUltimaConBaja = insTurnoUltimaConBaja!=null && 
				insTurnoUltimaConBaja.getFechaBaja()!=null && 
				!insTurnoUltimaConBaja.getFechaBaja().equals("");			
			
			// Compruebo que es un turno con validacion de inscripciones automaticas
			if(miForm.getValidarInscripciones().equals("N")) {
				
				// Es cecesario unas observaciones (antes estaba en InscripcionTurno.solicitarAlta)
				miForm.setObservacionesValidacion(".");
				
				// Obtengo la fecha actual y la convierto en el formato adecuado
				Date dFechaValidacion = new Date(); 										
				String sFechaValidacion = sdf.format(dFechaValidacion); // Fecha con formato dd/MM/yyyy				
				
				// Compruebo que tiene fecha de baja
				if (bInsTurnoUltimaConBaja) {
					
					// Convierto la fecha de baja en el formato adecuado
					Date dFechaBaja = GstDate.convertirFechaHora(insTurnoUltimaConBaja.getFechaBaja());
					String sFechaBaja = sdf.format(dFechaBaja); // Fecha con formato dd/MM/yyyy
					
					// Resultado de la comparacion de la fecha de validacion, con la fecha de baja					
					int comparacion = GstDate.compararFechas(sFechaValidacion, sFechaBaja);
					
					// Si hoy es menor que la ultima fecha de baja, entonces ponemos el dia siguiente a la fecha de baja la fecha de hoy
					if (comparacion <= 0) {
						
						// Le sumo un dia
						dFechaBaja.setTime(dFechaBaja.getTime() + ClsConstants.DATE_MORE);
						String fechaFin = sdf.format(dFechaBaja);
						miForm.setFechaValidacion(fechaFin);
					
					// Si hoy es mayor que la ultima fecha de baja, entonces ponemos la fecha de hoy
					} else {
						miForm.setFechaValidacion(sFechaValidacion);
					}
				
					
				// Si no tiene inscripciones, se pone la fecha de hoy
				} else {
					miForm.setFechaValidacion(sFechaValidacion);				
				}
				
			} else {
				
				// Compruebo que tiene fecha de validacion
				if (miForm.getFechaValidacion() != null && !miForm.getFechaValidacion().equals("") && 
						bInsTurnoUltimaConBaja) {
					
					// Convierto la fecha de baja en el formato adecuado
					Date dFechaBaja = GstDate.convertirFechaHora(insTurnoUltimaConBaja.getFechaBaja());
					String sFechaBaja = sdf.format(dFechaBaja); // Fecha con formato dd/MM/yyyy
					
					String sFechaValidacion = miForm.getFechaValidacion(); // Fecha con formato dd/MM/yyyy
					
					// Resultado de la comparacion de la fecha de validacion, con la fecha de baja					
					int comparacion = GstDate.compararFechas(sFechaValidacion, sFechaBaja);
					
					// Si la fecha de validacion es menor o igual que la ultima fecha de baja, entonces mostramos un error
					if (comparacion <= 0) {
						//resultado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.valida.menor.baja.ia") + " " + sFechaBaja;
						resultado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.mayor") + " " + sFechaBaja;
					}					
				}									
			}
			
		}catch (SIGAException e) {
			throw new SIGAException(e.getLiteral(), e, new String[] {"modulo.gratuita"});	
			
		}catch (Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return resultado;
	}		
	
	/**
	 * JPT: Comprobaciones previas a dar de baja una inscripcion de turno
	 * 
	 * Controles:
	 * 1. si automatico, entonces calculo la fecha de baja: fechaValidación = MAX(fechaBajaUltimaGuardia, fechaValidacionTurno, SYSDATE)
	 * 2. sino fechaBaja >= MAX(fechaBajaUltimaGuardia, fechaValidacionTurno)
	 * 
	 * @param miForm (idInstitucion + idTurno + idPersona + fechaValidacionTurno + fechaBaja + validarInscripciones)
	 * @param usr
	 * @param bFechaEfectiva
	 * @return
	 * @throws SIGAException
	 */
	private String preBajaTurno(InscripcionTGForm miForm, UsrBean usr, boolean bFechaEfectiva) throws SIGAException {
		String resultado = "";
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		
		try {
			String sFechaFinal = this.calcularFechaBajaInscripcionTurno(miForm, usr);
			
			// Compruebo que es un turno con validaciones y bajas de inscripciones automaticas
			if(miForm.getValidarInscripciones().equals("N")) {
				
				// Obtengo la fecha actual y la convierto en el formato adecuado
				Date dFechaHoy = new Date(); 										
				String sFechaHoy = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy		
				
				// Comparo la fecha actual, con la fecha de validacion del turno y obtengo la mayor
				int comparacion = GstDate.compararFechas(sFechaHoy, sFechaFinal);
					
				// Guardamos la fecha mayor
				if (comparacion < 0) {
					miForm.setFechaBaja(sFechaFinal);
					
				} else {
					miForm.setFechaBaja(sFechaHoy);
				}			
				
			} else {
				
				// Compruebo que tiene fecha de validacion de baja
				if (miForm.getFechaBaja() != null && !miForm.getFechaBaja().equals("")) {
					
					String sFechaBaja = miForm.getFechaBaja(); // Fecha con formato dd/MM/yyyy
					
					// Resultado de la comparacion de la fecha de baja del turno, con la fecha final			
					int comparacion = GstDate.compararFechas(sFechaBaja, sFechaFinal);
					
					// Si la fecha de baja es menor que la fecha final, mostramos un error.
					if (comparacion < 0) {
						if (bFechaEfectiva) {
							//UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.baja.turno") + " " + sFechaFinal;
							resultado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.mayorigual") + " " + sFechaFinal;
							
						} else {
							resultado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.mayorigual") + " " + sFechaFinal;
						}						
					}
				}
			}
			
		} catch (SIGAException e) {
			throw new SIGAException(e.getLiteral(), e, new String[] {"modulo.gratuita"});	
			
		} catch (Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return resultado;
	}			
	
	/**
	 * JPT: Comprobaciones previas a dar de baja una inscripcion de turno
	 * 
	 * Controles:
	 * 1. si automatico, entonces calculo la fecha de baja: fechaValidación = MAX(fechaBajaUltimaGuardia, fechaValidacionTurno, SYSDATE)
	 * 2. sino fechaBaja >= MAX(fechaBajaUltimaGuardia, fechaValidacionTurno)
	 * 
	 * @param miForm
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	public String calcularFechaBajaInscripcionTurno(InscripcionTGForm miForm, UsrBean usr) throws SIGAException {
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		String sFechaFinal = null;
		
		try {
		
			// 1. Obtiene la fecha de inscripciones de guardias que es neceseario comprobar para dar de baja una inscripcion de turno (yyyy/MM/dd HH:mm:ss)
			ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
			String sfechaBajaUltimaInscripcionGuardia = admInsTurno.getFechaGuardiasTurno( 	
					miForm.getIdInstitucion(), 
					miForm.getIdTurno(), 
					miForm.getIdPersona());
			
			// 2. Obtengo la fecha de validación del turno (yyyy/MM/dd HH:mm:ss)
			Date dFechaValidacionTurno = GstDate.convertirFechaHora(miForm.getFechaValidacionTurno());
			sFechaFinal = sdf.format(dFechaValidacionTurno); // Fecha con formato dd/MM/yyyy		
			
			// Compruebo que tiene alguna inscripcion con fecha de baja
			if (sfechaBajaUltimaInscripcionGuardia != null && !sfechaBajaUltimaInscripcionGuardia.equals("")) {
				
				// Convierto la fecha de baja de las guardias en el formato adecuado
				Date dFechaBajaGuardias = GstDate.convertirFechaHora(sfechaBajaUltimaInscripcionGuardia);
				String sFechaBajaGuardias = sdf.format(dFechaBajaGuardias); // Fecha con formato dd/MM/yyyy			
				
				// Comparo la fecha de validacion del turno, con la fecha de baja de guardias
				int comparacion = GstDate.compararFechas(sFechaFinal, sFechaBajaGuardias);
				
				// Obtengo la mayor
				if (comparacion < 0) {
					sFechaFinal = sFechaBajaGuardias;
				}	
			}
			
		} catch (SIGAException e) {
			throw new SIGAException(e.getLiteral(), e, new String[] {"modulo.gratuita"});	
			
		} catch (Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return sFechaFinal;
	}
	
	/**
	 * JPT: Comprobaciones previas a dar de alta una inscripcion de guardia
	 * 
	 * Controles:
	 * 1. si automatico, entonces calculo la fecha de validación: fechaValidación = MAX(fechaBaja_UltimaGuardia + 1, fechaValidacionTurno, SYSDATE)
	 * 2. sino fechaValidacion >= MAX(fechaBaja_UltimaGuardia + 1, fechaValidacionTurno)
	 * 
	 * @param miForm (idInstitucion + idTurno + idPersona + idGuardia + validarInscripciones + fechaValidacion)
	 * @param usr
	 * @return
	 * @throws SIGAException
	 */
	private String preAltaGuardia (InscripcionTGForm miForm, UsrBean usr) throws SIGAException {
		String resultado = "";
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		
		try {						
			// Obtiene los datos de la guardia, que han sido dados de baja por ultima vez			
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(usr);
			ScsInscripcionGuardiaBean insGuardiaBaja = inscripcionGuardiaAdm.getInscripcionGuardiaUltimaBaja(
					new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), 
					new Long(miForm.getIdPersona()),
					new Integer(miForm.getIdGuardia()));
			
			String sFechaFinal = "";						
			if (insGuardiaBaja!=null && insGuardiaBaja.getFechaBaja()!=null && !insGuardiaBaja.getFechaBaja().equals("")) { 
				String sFechaBajaGuardias = insGuardiaBaja.getFechaBaja();
				
				// Le sumo un dia
				Date dFechaBajaGuardias = GstDate.convertirFechaHora(sFechaBajaGuardias);
				dFechaBajaGuardias.setTime(dFechaBajaGuardias.getTime() + ClsConstants.DATE_MORE);				
				sFechaFinal = sdf.format(dFechaBajaGuardias);
			}
			
			// Obtiene la inscripcion de turno de la persona, que esta validada y sin dar de baja
			ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
			ScsInscripcionTurnoBean insTurno = admInsTurno.getInscripcionTurnoValidadaSinBaja(
				new Integer(miForm.getIdInstitucion()),
				new Integer( miForm.getIdTurno()), 
				new Long(miForm.getIdPersona()));
			
			if(insTurno!=null && insTurno.getFechaValidacion()!=null && !insTurno.getFechaValidacion().equals("")) {				
				Date dFechaValidacionTurno = GstDate.convertirFechaHora(insTurno.getFechaValidacion());
				String sFechaValidacionTurno = sdf.format(dFechaValidacionTurno);
				
				if (sFechaFinal.equals("")) {
					sFechaFinal = sFechaValidacionTurno;
					
				} else {
					if (GstDate.compararFechas(sFechaFinal, sFechaValidacionTurno) < 0) {
						 sFechaFinal = sFechaValidacionTurno;
					}					
				}				
			
			} else {
				resultado = UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.sin.turno");
				return resultado;
			}						
			
			// Compruebo que es un turno con validacion de inscripciones automaticas
			if(miForm.getValidarInscripciones().equals("N")) {
				
				// Es cecesario unas observaciones (antes estaba en InscripcionTurno.solicitarAlta)
				miForm.setObservacionesValidacion(".");
				
				// Obtengo la fecha actual y la convierto en el formato adecuado
				Date dFechaHoy = new Date(); 										
				String sFechaHoy = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy			
				
				if (!sFechaFinal.equals("")) { 
					// Si la fecha de validacion es menor que hoy, mostramos un error	
					if (GstDate.compararFechas(sFechaFinal, sFechaHoy) < 0) {
						sFechaFinal = sFechaHoy;
					}
					
				} else {
					sFechaFinal = sFechaHoy;
				}
				
				miForm.setFechaValidacion(sFechaFinal);
				
			} else {
				
				// Compruebo que tiene fecha de validacion
				if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")) { 					
					String sFechaValidacion = miForm.getFechaValidacion(); // Fecha con formato dd/MM/yyyy
					
					if (!sFechaFinal.equals("")) { 
						// Si la fecha de validacion es menor o igual que la fecha final, mostramos un error	
						if (GstDate.compararFechas(sFechaValidacion, sFechaFinal) < 0) {							
							resultado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.mayorigual") + " " + sFechaFinal; 
						}
					}
				}									
			}	
			
		} catch (SIGAException e) {
			throw new SIGAException(e.getLiteral(), e, new String[] {"modulo.gratuita"});	
			
		} catch (Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return resultado;
	}		
	
	/**
	 * JPT: Comprobaciones previas a dar de baja una inscripcion de guardia
	 * 
	 * Controles:
	 * 1. si automatico, entonces calculo la fecha de validación: fechaValidación = MAX(fechaValidacionGuardia, SYSDATE)
	 * 2. sino fechaValidacion >= fechaValidacionGuardia
	 * 
	 * @param miForm (idInstitucion + idTurno + idPersona + idGuardia + validarInscripciones + fechaBaja)
	 * @param usr
	 * @param bFechaEfectiva
	 * @return
	 * @throws SIGAException
	 */
	private String preBajaGuardia (InscripcionTGForm miForm, UsrBean usr, boolean bFechaEfectiva) throws SIGAException {
		String resultado = "";
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		
		try {																			
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(usr);
			ScsInscripcionGuardiaBean insGuardia = inscripcionGuardiaAdm.getInscripcionGuardiaUltimaSinBaja(
				new Integer(miForm.getIdInstitucion()),
				new Integer(miForm.getIdTurno()), 
				new Long(miForm.getIdPersona()),
				new Integer(miForm.getIdGuardia()));
			
			String sFechaFinal = "";						
			if (insGuardia!=null && insGuardia.getFechaValidacion()!=null && !insGuardia.getFechaValidacion().equals("")) { 
				String sFechaValidacionGuardia = insGuardia.getFechaValidacion();
				Date dFechaValidacionGuardia = GstDate.convertirFechaHora(sFechaValidacionGuardia);
				sFechaFinal = sdf.format(dFechaValidacionGuardia);
			}
			
			// Compruebo que es un turno con validacion de inscripciones automaticas
			if(miForm.getValidarInscripciones().equals("N")) {
				
				// Obtengo la fecha actual y la convierto en el formato adecuado
				Date dFechaHoy = new Date(); 										
				String sFechaHoy = sdf.format(dFechaHoy); // Fecha con formato dd/MM/yyyy			
				
				if (!sFechaFinal.equals("")) { 
					// Si la fecha de validacion es menor que hoy, mostramos un error	
					if (GstDate.compararFechas(sFechaFinal, sFechaHoy) < 0) {
						sFechaFinal = sFechaHoy;
					}
					
				} else {
					sFechaFinal = sFechaHoy;					
				}					
				
				miForm.setFechaBaja(sFechaFinal);								
				
			} else {
				
				// Compruebo que tiene fecha de validacion
				if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals("")) {					
					String sFechaBaja = miForm.getFechaBaja(); // Fecha con formato dd/MM/yyyy
					
					if (!sFechaFinal.equals("")) { 
						// Si la fecha de validacion es menor o igual que la fecha final, mostramos un error	
						if (GstDate.compararFechas(sFechaBaja, sFechaFinal) < 0) {
							if (bFechaEfectiva) {
								resultado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaValidacion.mayorigual") + " " + sFechaFinal;
								
							} else {
								resultado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.error.fechaBaja.mayorigual") + " " + sFechaFinal;
							}	
						}
					}
				}									
			}	
			
		} catch (Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		return resultado;
	}					
}