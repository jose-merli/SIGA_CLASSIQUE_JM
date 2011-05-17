package com.siga.gratuita.action;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.siga.Utilidades.UtilidadesHash;
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
import com.siga.beans.ScsGrupoGuardiaColegiadoAdm;
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



/**
 * @author jorgeta
 */

public class GestionInscripcionesTGAction extends MasterAction {
	private final int tipoActualizacionBaja=0;
	private final int tipoActualizacionEdicion=1;
	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
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
					}
					
					else if (accion.equalsIgnoreCase("sitConsultaTurno")){
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
					}
					
					else if (accion.equalsIgnoreCase("sigConsultaTurno")){
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
					}
					else if (accion.equalsIgnoreCase("vbgValidar")){
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
					}else if(accion.equalsIgnoreCase("comprobarBajaEnTodosLosTurnos")){
						mapDestino =  comprobarBajaEnTodosLosTurnos(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("solicitarBajaEnTodosLosTurnos")){
						mapDestino =  solicitarBajaEnTodosLosTurnos(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("solicitudesMasivas")){
						mapDestino =  solicitudesMasivas(mapping,  miForm,  request,  response);
					}
					else if(accion.equalsIgnoreCase("vmitValidar")){
						mapDestino =  vmitValidar(mapping,  miForm,  request,  response);
					}
					
					else if(accion.equalsIgnoreCase("vmigValidar")){
						mapDestino =  vmigValidar(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("vmbtComprobarValidar")){
						mapDestino =  vmbtComprobarValidar(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("vmbtValidar")){
						mapDestino =  vmbtValidar(mapping,  miForm,  request,  response);
					}
					else if(accion.equalsIgnoreCase("vmbgComprobarValidar")){
						mapDestino =  vmbgComprobarValidar(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("vmbgValidar")){
						mapDestino =  vmbgValidar(mapping,  miForm,  request,  response);
					}
					else if (accion.equalsIgnoreCase("editarFechaValidacion")){
						// buscarPersona
						mapDestino = editarFechaValidacion(mapping, miForm, request, response);
					}else if(accion.equalsIgnoreCase("editarFechaBaja")){
						mapDestino =  editarFechaBaja(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("actualizarFechaValidacion")){
						mapDestino =  actualizarFechaValidacion(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("afvtModificar")){
						mapDestino =  afvtModificar(mapping,  miForm,  request,  response);
					}
					else if(accion.equalsIgnoreCase("comprobarAmfvtModificar")){
						mapDestino =  comprobarAmfvtModificar(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("amfvtModificar")){
						mapDestino =  amfvtModificar(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("afbgModificar")){
						mapDestino =  afbgModificar(mapping,  miForm,  request,  response);
					}else if(accion.equalsIgnoreCase("comprobarAmfvgModificar")){
						mapDestino =  comprobarAmfvgModificar(mapping,  miForm,  request,  response);
					}
					else if(accion.equalsIgnoreCase("amfvgModificar")){
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
					}
					
					else if(accion.equalsIgnoreCase("afbtModificar")){
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
					}
					
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
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
				inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), miForm.getFechaSolicitud(),true);
				//seteamos el paso siguiente
				miForm.setModo("consultaGuardiasValidarInscripcionTurno");

			}else{
				inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()));
				comprobarRetencion(miForm, usrBean);
				//seteamos el paso siguiente
				miForm.setModo("sitConsultaGuardias");
			}
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			forward = "consultaTurnoInscripcion";

		}catch (SIGAException e) 
		{
			request.setAttribute("modal","1");
			request.setAttribute("mensaje",e.getLiteral());
			forward = "exito";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String sitConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			Integer idGuardia=null;
			if(miForm.getIdGuardia()!=null &&!miForm.getIdGuardia().equals("")&&Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = Integer.parseInt(miForm.getIdGuardia());
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasParaInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()),idGuardia);
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			miForm.setModo("sitDatos");
			forward = "consultaGuardiasInscripcion";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String sitDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;

			ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean insUltimaConBaja = admInsTurno.getInscripcion(new Integer(miForm.getIdInstitucion()),new Integer( miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
			//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
			Boolean existeInscConBaja = insUltimaConBaja!=null && insUltimaConBaja.getFechaBaja()!=null && !insUltimaConBaja.getFechaBaja().equals("");
			if(existeInscConBaja){
				miForm.setFechaBajaTurno(insUltimaConBaja.getFechaBaja());
				
			}else{
				miForm.setFechaBajaTurno(null);
				
			}
			boolean isAlgunaGuardiaPorGrupo = false;
			String guardiasSeleccionadas = miForm.getGuardiasSel();
			if(guardiasSeleccionadas!=null &&!guardiasSeleccionadas.equals("")){
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
			
			miForm.setSolicitudAlta(true);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(true);
			miForm.setValidacionBaja(false);
			miForm.setMasivo(false);
			
//			FIXME AAAÑADIR SELECCIÓN DE GRUPO sitDatos ok
			//COMPROBAR SI EXISTE ALGUNA GUARDIA DEL TURNO QUE SEA DE GRUPO Y EN TAL CASO
			//SACAR UN LISTADO CON LAS GRUPOS DE LA GUARDIA PARA QUE SEA OBLIGATORIO LA ELECCION DE UNO
			miForm.setModo("sitEditarTelefonosGuardia");
			forward = "validarInscripcion";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
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
			if(v != null && v.size() != 0)
			{
				for(int i=0;i<v.size();i++){
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
						}
					}
				}
			}
			forward = "editarTelefonosGuardia";
			miForm.setModo("sitInsertar");
		}
		catch(Exception e){
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		return forward;
	}
	private String smitEditarTelefonosGuardia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		String forward = sitEditarTelefonosGuardia(mapping, formulario, request, response);
		miForm.setModo("smitInsertar");
		return forward;
	}
	private String sitInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
			{
				ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
				ScsInscripcionTurnoBean insUltimaConBaja = admInsTurno.getInscripcion(new Integer(miForm.getIdInstitucion()),new Integer( miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
				//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
				Boolean existeInscConBaja = insUltimaConBaja!=null && insUltimaConBaja.getFechaBaja()!=null && !insUltimaConBaja.getFechaBaja().equals("");
				if(existeInscConBaja){
					Date fechaBajaUltima = GstDate.convertirFechaHora(insUltimaConBaja.getFechaBaja());
					Date fechaValidacion = GstDate.convertirFecha(miForm.getFechaValidacion(),"dd/MM/yyyy");
					
					
					if(fechaValidacion.compareTo(fechaBajaUltima)<0){
						String fechaDeBajaUltima = GstDate.getFormatedDateShort("", insUltimaConBaja.getFechaBaja()); 
						request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.valida.menor.baja.ia")+" "+fechaDeBajaUltima);
						//request.setAttribute("mensaje","La fecha de validacion debe ser superior a la fecha de baja de la inscripcion anterior "+fechaDeBajaInscActiva);
						return "errorConAviso";
						
					}
					
				}
				
				
			}
			
			
			
			if(miForm.getFechaSolicitud()!=null && !miForm.getFechaSolicitud().equals("")){
				InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()), Long.valueOf(miForm.getIdPersona()),
						miForm.getFechaSolicitud(), usr, false);
				
				if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
				{
					inscripcion.validarAlta(miForm.getFechaValidacion(),
							miForm.getObservacionesValidacion(), usr);
				}
//				else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
//				{
//					inscripcion.validarBaja(miForm.getFechaBaja(),null,null,null, usr);
//				}
			}else{
				miForm.setFechaSolicitud("sysdate");
				InscripcionTurno inscripcion = new InscripcionTurno(new ScsInscripcionTurnoBean());
				inscripcion.solicitarAlta(miForm, usr);
				CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);			
				dirAdm.insertarDireccionGuardia(new Integer(miForm.getIdInstitucion()),new Long(miForm.getIdPersona()),
				miForm.getIdDireccion(),miForm.getFax1(),miForm.getFax2(),miForm.getMovil(),miForm.getTelefono1(),miForm.getTelefono2());

			}
			//miForm.re
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String vitValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
			{
				ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
				ScsInscripcionTurnoBean insUltimaConBaja = admInsTurno.getInscripcion(new Integer(miForm.getIdInstitucion()),new Integer( miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
				//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
				Boolean existeInscConBaja = insUltimaConBaja!=null && insUltimaConBaja.getFechaBaja()!=null && !insUltimaConBaja.getFechaBaja().equals("");
				if(existeInscConBaja){
					Date fechaBajaUltima= GstDate.convertirFechaHora(insUltimaConBaja.getFechaBaja());
					Date fechaValidacion = GstDate.convertirFecha(miForm.getFechaValidacion(),"dd/MM/yyyy");
					if(fechaValidacion.compareTo(fechaBajaUltima)<0){
						String fechaDeBajaInscActiva = GstDate.getFormatedDateShort("", insUltimaConBaja.getFechaBaja()); 
						request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.valida.menor.baja.ia")+" "+fechaDeBajaInscActiva);
						//request.setAttribute("mensaje","La fecha de validacion debe ser superior a la fecha de baja de la inscripcion anterior "+fechaDeBajaInscActiva);
						return "errorConAviso";
						
					}
					
				}
				
				
			}
				InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()), Long.valueOf(miForm.getIdPersona()),
						miForm.getFechaSolicitud(), usr, false);
				if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")){
					inscripcion.denegarInscripcionTurno(miForm.getFechaDenegacion(),miForm.getObservacionesDenegacion(), usr);	
					
				}else if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
				{
					inscripcion.validarAlta(miForm.getFechaValidacion(),
							miForm.getObservacionesValidacion(), usr);
				}
			
			//miForm.re
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String vbtValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
					new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()), Long.valueOf(miForm.getIdPersona()),
					miForm.getFechaSolicitud(), usr, false);
			if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")){
				//denegar
				inscripcion.denegarBajaInscripcionTurno(miForm.getFechaDenegacion(),miForm.getObservacionesDenegacion(),usr);
			}else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
			{
				inscripcion.validarBaja(miForm.getFechaBaja(),null,miForm.getObservacionesValBaja(), miForm.getTipoActualizacionSyC(), usr);


			}
			
			//miForm.re
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String vbtComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			if(miForm.getFechaDenegacion()==null || miForm.getFechaDenegacion().equals("")){
				Date fechaBaja = GstDate.convertirFecha(miForm.getFechaBaja(),"dd/MM/yyyy");
				if(miForm.getFechaValidacionTurno()!=null&& !miForm.getFechaValidacionTurno().equals("")){
					Date fechaValidacion= GstDate.convertirFechaHora(miForm.getFechaValidacionTurno());
					if(fechaBaja.compareTo(fechaValidacion)<0){
						throw new SIGAException("gratuita.gestionInscripciones.error.baja.menor.valida");
						
					}
				}
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
					  usr, Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),null,
						  null,miForm.getFechaBaja(),false,this.tipoActualizacionBaja); 
				miForm.setEstadoPendientes(estadoPendientes);
			}
			if(miForm.getEstadoPendientes()!=null&&!miForm.getEstadoPendientes().equals("")){
				miForm.setModo("vbtValidar");
				forward = "msjAvisoEstado";
				return forward;
			}else{
				return vbtValidar(mapping,  formulario,  request,  response);
			}
			
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	
	
	
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
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null,true);
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());			
			miForm.setModo("sbtConsultaGuardias");
			forward = "consultaTurnoInscripcion";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String sbtConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")&& miForm.getTipoGuardias()!=null &&Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), new Integer(miForm.getIdGuardia()));
			}else{
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
			}
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			miForm.setModo("sbtDatos");
			forward = "consultaGuardiasInscripcion";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

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
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String sbtComprobarInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			miForm.setFechaSolicitudBaja("sysdate");
			if(miForm.getValidarInscripciones().equals("N"))
				miForm.setFechaBaja("sysdate");
			
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
						  usr, Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),
						  null,null,miForm.getFechaBaja(),false,this.tipoActualizacionBaja); 
				miForm.setEstadoPendientes(estadoPendientes);
				
				
				if(miForm.getEstadoPendientes()!=null&&!miForm.getEstadoPendientes().equals("")){
					miForm.setModo("sbtInsertar");
					forward = "msjAvisoEstado";
					return forward;
				}else{
					return sbtInsertar(mapping,  formulario,  request,  response);
				}
			}else{
				return sbtInsertar(mapping,  formulario,  request,  response);
				
			}


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
	}
	
	
	
	private String sbtInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
					new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()), Long.valueOf(miForm.getIdPersona()),
					miForm.getFechaSolicitud(), usr, false);
			miForm.setFechaSolicitudBaja("sysdate");
			if(miForm.getValidarInscripciones().equals("N"))
				miForm.setFechaBaja("sysdate");
			
			
			
			inscripcion.solicitarBaja(miForm.getFechaSolicitudBaja(),miForm.getObservacionesBaja(),miForm.getFechaBaja(),miForm.getObservacionesValBaja(), miForm.getFechaValidacionTurno(),miForm.getValidarInscripciones(),miForm.getTipoActualizacionSyC(), usr);
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");

		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	private String getEstadoGuardiasDesignasPendientes (
			UsrBean usr,
			Long idPersona,
			Integer idInstitucion,
			Integer idTurno,Integer idGuardia,String fechaDesde,String fechaHasta,boolean isGuardia,int tipoActualizacion)
	throws SIGAException 
	{
		
//			Devuelve el nivel de error:
//			0: NO hay nada pendiente
//			1: Error, tiene guardias pendientes
//			2: Error, tiene designas pendientes
//			3: Excepcion
			CenClienteAdm admCliente = new CenClienteAdm(usr);
			String estado = null;
			int nivelError = 0;
			if(isGuardia)
				nivelError = admCliente.tieneGuardiasSJCSPendientes(
						idPersona, idInstitucion, idTurno,idGuardia,fechaDesde,fechaHasta);
			else
				nivelError = admCliente.tieneTrabajosSJCSPendientes(
						idPersona, idInstitucion, idTurno,fechaDesde,fechaHasta);
			switch (nivelError)
					{
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
					case 3: throw new SIGAException(admCliente.getError());
						
					}
		

		if(estado!=null)
			estado = UtilidadesString.getMensajeIdioma(	usr, estado);
		return estado; // No hay nada pendiente
	} //comprobarGuardiasDesignasPendientes()
	
	private String smitInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			miForm.setFechaSolicitud("sysdate");
			miForm.setTipoGuardias(String.valueOf(ScsTurnoBean.TURNO_GUARDIAS_OBLIGATORIAS));
			miForm.setGuardiasSel(null);
			boolean existenErrores = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idTurnoSel=d[0];
				String validarInscripciones=d[1];
				
				miForm.setIdTurno(idTurnoSel);
				miForm.setValidarInscripciones(validarInscripciones);
				try {
					if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
					{
						ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
						ScsInscripcionTurnoBean insUltimaConBaja = admInsTurno.getInscripcion(new Integer(miForm.getIdInstitucion()),new Integer( miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
						//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
						Boolean existeInscConBaja = insUltimaConBaja!=null && insUltimaConBaja.getFechaBaja()!=null && !insUltimaConBaja.getFechaBaja().equals("");
						if(existeInscConBaja){
							Date fechaBajaUltimaInscripcion = GstDate.convertirFechaHora(insUltimaConBaja.getFechaBaja());
							Date fechaValidacion = GstDate.convertirFecha(miForm.getFechaValidacion(),"dd/MM/yyyy");
							
							
							if(fechaValidacion.compareTo(fechaBajaUltimaInscripcion)<0){
								//String fechaDeBajaInscActiva = GstDate.getFormatedDateShort("", insUltimaConBaja.getFechaBaja()); 
								//request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));
								//request.setAttribute("mensaje","Existen turnos donde la fecha de validacion es inferior a la fecha de baja de la inscripcion activa con fecha de baja ");
								//return "errorConAviso";
								throw new SIGAException("gratuita.gestionInscripciones.error.valida.menor.baja.ia");
								
							}
							
						}
						
						
					}
					InscripcionTurno inscripcion = new InscripcionTurno(new ScsInscripcionTurnoBean());
					inscripcion.solicitarAlta(miForm, usr);

					
				} catch (Exception e) {
					existenErrores = true;
				}
				
			}
			
			CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);
			
			dirAdm.insertarDireccionGuardia(new Integer(miForm.getIdInstitucion()),new Long(miForm.getIdPersona()),
			miForm.getIdDireccion(),miForm.getFax1(),miForm.getFax2(),miForm.getMovil(),miForm.getTelefono1(),miForm.getTelefono2());
		
			if(existenErrores){
				request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.masivo.solapamiento"));
			}else{
				request.setAttribute("mensaje","messages.updated.success");				
			}
			
			forward = "exito";
			request.setAttribute("modal", "1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String sbgComprobarInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;			
		String forward = "error";

		try {
			UsrBean usr = this.getUserBean(request);
			
			//solo dejaremos validar una a una cuando sean a elegir
			Integer idGuardia = null;
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				idGuardia = new Integer(miForm.getIdGuardia());
				
			}
			
			miForm.setFechaSolicitudBaja("sysdate");
			if(miForm.getValidarInscripciones().equals("N"))
				miForm.setFechaBaja("sysdate");
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equalsIgnoreCase("")){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
						  usr, Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia,
						  null,miForm.getFechaBaja(),true,this.tipoActualizacionBaja); 
				miForm.setEstadoPendientes(estadoPendientes);
				
				
				if(miForm.getEstadoPendientes()!=null&&!miForm.getEstadoPendientes().equals("")){
					miForm.setModo("sbgInsertar");
					forward = "msjAvisoEstado";
					return forward;
				}else{
					return sbgInsertar(mapping,  formulario,  request,  response);
				}
			}else{
				return sbgInsertar(mapping,  formulario,  request,  response);
				
			}
			
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	private String sbgInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;			
		String forward = "error";

		try {
			UsrBean usr = this.getUserBean(request);
			
			Integer idGuardia = null;
			if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				idGuardia = new Integer(miForm.getIdGuardia());
				
			}
			InscripcionGuardia 	inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia(
					new Integer(usr.getLocation()), new Integer(miForm.getIdTurno()),idGuardia, 
					new Long(miForm.getIdPersona()),miForm.getFechaSolicitud(), usr, false);

			
			miForm.setFechaSolicitudBaja("sysdate");
			if(miForm.getValidarInscripciones().equals("N"))
				miForm.setFechaBaja("sysdate");
			
			inscripcionGuardia.setBajas(miForm.getObservacionesBaja(), miForm.getFechaSolicitudBaja(), miForm.getFechaBaja(),miForm.getObservacionesValBaja());
			inscripcionGuardia.solicitarBaja(usr,miForm.getTipoActualizacionSyC());
			forward = "exito";
			request.setAttribute("mensaje","messages.updated.success");
			request.setAttribute("modal","1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	
	private String vbgComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;	
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			if(miForm.getFechaDenegacion()==null || miForm.getFechaDenegacion().equals("")){
				Date fechaBaja = GstDate.convertirFecha(miForm.getFechaBaja(),"dd/MM/yyyy");
				//En la fecha de validacion turno no se sete al fechad evalidacion de la guardia
				Date fechaValidacion= GstDate.convertirFechaHora(miForm.getFechaValidacionTurno());
				if(fechaBaja.compareTo(fechaValidacion)<0){
					throw new SIGAException("gratuita.gestionInscripciones.error.baja.menor.valida");
					
				}
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(
					  usr, Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia,
					  	null,miForm.getFechaBaja(),true,this.tipoActualizacionBaja); 
				miForm.setEstadoPendientes(estadoPendientes);
			}
			if(miForm.getEstadoPendientes()!=null&&!miForm.getEstadoPendientes().equals("")){
					miForm.setModo("vbgValidar");
					forward = "msjAvisoEstado";
					return forward;
				
			}else{
				return vbgValidar(mapping,  formulario,  request,  response);
			}
			
		
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	
	private String vbgValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		String forward = "error";
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		
		try {
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			//solo dejaremos validar una a una cuando sean a elegir
			InscripcionGuardia inscripcion = InscripcionGuardia.getInscripcionGuardia(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia
								, Long.valueOf(miForm.getIdPersona()),	miForm.getFechaSolicitud(), usr, false);
				
				if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals(""))
				{
					inscripcion.setDenegacion(miForm.getObservacionesDenegacion(),miForm.getFechaDenegacion());
					inscripcion.denegarBajaGuardia(usr);
				}else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
				{
					inscripcion.setBajas(null, null, miForm.getFechaBaja(),miForm.getObservacionesValBaja());
					inscripcion.validarBaja(usr, miForm.getTipoActualizacionSyC());
				}
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	private String sigInsertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		String forward = "error";
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		
		try {
			
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsInscripcionTurnoBean insTurnoActiva = null;
			ScsInscripcionGuardiaBean insGuardiaSiguiente = null;
			if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
			{
				ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
				insTurnoActiva = admInsTurno.getInscripcion(new Integer(miForm.getIdInstitucion()),new Integer( miForm.getIdTurno()), new Long(miForm.getIdPersona()), miForm.getFechaValidacion());
				//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
				if(insTurnoActiva!=null){
					String fechaBajaTurno = null;
					if(insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
						Date dateFechaBajaTurno = GstDate.convertirFechaHora(insTurnoActiva.getFechaBaja());
						Date fechaHoy = GstDate.convertirFecha(GstDate.getHoyJsp(),"dd/MM/yyyy");
						
						fechaBajaTurno = GstDate.getFormatedDateShort("", insTurnoActiva.getFechaBaja());
						
						if(dateFechaBajaTurno.compareTo(fechaHoy)<0){
							request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.inscripcionturno.menor.hoy"));
							return "errorConAviso";
						}
					}
					ScsInscripcionGuardiaAdm admInsguardia = new ScsInscripcionGuardiaAdm(usr);
					Vector inscripcionGuardia = admInsguardia.getRegistrosInscripcionGuardiaPendientes(miForm.getIdInstitucion(),miForm.getIdTurno(), miForm.getIdPersona(),new Integer(miForm.getIdGuardia()), miForm.getFechaValidacion());
					if(inscripcionGuardia!=null && inscripcionGuardia.size()>0){
						request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.existe"));
						return "errorConAviso";
						
					
					}
					//comprobamos si hay una inscripcion de guardia posterior dentro de las fechas del turno
					insGuardiaSiguiente = admInsguardia.getSiguienteInscripcion(miForm.getIdInstitucion(),miForm.getIdTurno(), miForm.getIdPersona(),new Integer(miForm.getIdGuardia()),fechaBajaTurno, miForm.getFechaValidacion());
					
					
					

					
					
				}else{
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.sin.turno"));
					return "errorConAviso";
					
				}
				
			}
			
			InscripcionGuardia inscripcion = null;
			//solo dejaremos validar una a una cuando sean a elegir
			if(miForm.getFechaSolicitud()!=null && !miForm.getFechaSolicitud().equals("")){
				inscripcion = InscripcionGuardia.getInscripcionGuardia(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia
								, Long.valueOf(miForm.getIdPersona()),	miForm.getFechaSolicitud(), usr, false);
				
				if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
				{
					inscripcion.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
					if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
						inscripcion.setDatosGrupo(miForm.getNumeroGrupo(), new Integer(miForm.getOrdenGrupo()));
					}
					inscripcion.validarAlta(usr);
				}else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
				{
					inscripcion.setBajas(null, null, miForm.getFechaBaja(),miForm.getObservacionesValBaja());
					inscripcion.validarBaja(usr, null);
				}
			}else{
				miForm.setFechaSolicitud("sysdate");
				if(miForm.getValidarInscripciones()!=null && miForm.getValidarInscripciones().equalsIgnoreCase("N"))
				{
					miForm.setFechaValidacion("sysdate");
					miForm.setObservacionesValidacion(".");
				}
				 
				inscripcion = InscripcionGuardia.getInscripcionGuardia(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia
						, Long.valueOf(miForm.getIdPersona()),	miForm.getFechaSolicitud(), usr, false);
				
				
				inscripcion.setAltas(miForm.getObservacionesSolicitud(), miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
				if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
				{
					if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
						inscripcion.setDatosGrupo(miForm.getNumeroGrupo(), new Integer(miForm.getOrdenGrupo()));
					}
				}
				if(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")){
						if(insGuardiaSiguiente!=null ){
							inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insGuardiaSiguiente.getFechaValidacion()),miForm.getObservacionesValBaja());
						}else if(insTurnoActiva!=null&&insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
							inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insTurnoActiva.getFechaBaja()),miForm.getObservacionesValBaja());
					}
				}
				
				inscripcion.solicitarAlta(usr);
			}
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	
	
	private String vigValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		String forward = "error";
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		
		
		
		try {
			
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsInscripcionTurnoBean insTurnoActiva = null;
			ScsInscripcionGuardiaBean insGuardiaSiguiente = null;
			if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
			{

				ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
				insTurnoActiva = admInsTurno.getInscripcion(new Integer(miForm.getIdInstitucion()),new Integer( miForm.getIdTurno()), new Long(miForm.getIdPersona()), miForm.getFechaValidacion());
				//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
				if(insTurnoActiva!=null){
					String fechaBajaTurno = null;
					if(insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
						Date dateFechaBajaTurno = GstDate.convertirFechaHora(insTurnoActiva.getFechaBaja());
						Date fechaHoy = GstDate.convertirFecha(GstDate.getHoyJsp(),"dd/MM/yyyy");
						
						fechaBajaTurno = GstDate.getFormatedDateShort("", insTurnoActiva.getFechaBaja());
						
						if(dateFechaBajaTurno.compareTo(fechaHoy)<0){
							request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.inscripcionturno.menor.hoy"));
							return "errorConAviso";
						}
					}
					ScsInscripcionGuardiaAdm admInsguardia = new ScsInscripcionGuardiaAdm(usr);
					
					//comprobamos si hay una inscripcion de guardia posterior dentro de las fechas del turno
					insGuardiaSiguiente = admInsguardia.getSiguienteInscripcion(miForm.getIdInstitucion(),miForm.getIdTurno(), miForm.getIdPersona(),new Integer(miForm.getIdGuardia()),fechaBajaTurno, miForm.getFechaValidacion());
					
				}else{
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.sin.turno"));
					return "errorConAviso";
					
				}
				
			}
			
			
			
			
			
			//solo dejaremos validar una a una cuando sean a elegir
				InscripcionGuardia inscripcion = InscripcionGuardia.getInscripcionGuardia(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia
								, Long.valueOf(miForm.getIdPersona()),	miForm.getFechaSolicitud(), usr, false);
				
				if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
				{
					inscripcion.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
					if(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")){
						if(insGuardiaSiguiente!=null ){
							inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insGuardiaSiguiente.getFechaValidacion()),miForm.getObservacionesValBaja());
						}else if(insTurnoActiva!=null&&insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
							inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insTurnoActiva.getFechaBaja()),miForm.getObservacionesValBaja());
					}
				}
					if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
						inscripcion.setDatosGrupo(miForm.getNumeroGrupo(), new Integer(miForm.getOrdenGrupo()));
					}
					
					inscripcion.validarAlta(usr);
				}else if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals(""))
				{
					inscripcion.setDenegacion(miForm.getObservacionesDenegacion(),miForm.getFechaDenegacion());
					inscripcion.denegarBajaGuardia(usr);
				}
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String editarFechaValidacion(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException ,ClsExceptions{
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		miForm.setFechaBaja(null);
		miForm.setFechaValidacion(GstDate.getFormatedDateShort("",miForm.getFechaValidacion()));
		miForm.setModo("actualizarFechaValidacion");
		if(miForm.getIdGuardia()!=null||!miForm.getIdGuardia().equals("")){
			ScsInscripcionTurnoAdm itAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscTurno = itAdm.getInscripcionSinBaja(new Integer(miForm.getIdInstitucion()),new Integer(miForm.getIdTurno()),new Long(miForm.getIdPersona()));
			miForm.setFechaValidacionTurno(inscTurno.getFechaValidacion());
		}
		
		return "editarFechaValidacion";
	}
	private String editarFechaBaja(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException ,ClsExceptions{
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		miForm.setFechaBaja(GstDate.getFormatedDateShort("",miForm.getFechaBaja()));
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

	private String getAjaxBusqueda (ActionMapping mapping, 		
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws ClsExceptions,Exception 
				{
			InscripcionTGForm validaTurnosForm = (InscripcionTGForm) formulario;
			validaTurnosForm.reset(true, true);
			UsrBean usrBean = this.getUserBean(request);
			String forward = "exception";
			String clase = validaTurnosForm.getClase();
			try {
				if(clase.equals("T")){
					ScsInscripcionTurnoAdm inscTurno = new ScsInscripcionTurnoAdm(usrBean);
					List<ScsInscripcionTurnoBean> alIncripciones = inscTurno.getInscripcionesTurno(validaTurnosForm,false);
					validaTurnosForm.setInscripcionesTurno(alIncripciones);
					forward = "listadoInscripcionTurnos";
					
				}else if(clase.equals("G")){
					ScsInscripcionGuardiaAdm inscGuardia = new ScsInscripcionGuardiaAdm(usrBean);
					List<ScsInscripcionGuardiaBean> alIncripciones = inscGuardia.getInscripcionesGuardia(validaTurnosForm,false);
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
		
		private void getAjaxGuardias (ActionMapping mapping, 		
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
				{
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
		private void getAjaxColegiado (ActionMapping mapping, 		
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
				{
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
		
	private String sigDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			String forward = "";
			try {
				InscripcionTGForm miForm = (InscripcionTGForm) formulario;
				miForm.setSolicitudAlta(true);
				miForm.setSolicitudBaja(false);
				miForm.setValidacionAlta(true);
				miForm.setValidacionBaja(false);
				miForm.setMasivo(false);
				
//				if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
//					ScsGrupoGuardiaColegiadoAdm grupoGuardiaColegiadoAdm = new ScsGrupoGuardiaColegiadoAdm(this.getUserBean(request));
//					List<LetradoGuardia> grupoGuardiaLetradoList = grupoGuardiaColegiadoAdm.getGruposColegiados(miForm.getIdInstitucion(), miForm.getIdTurno(), miForm.getIdGuardia());
//					miForm.setGruposGuardiaLetrado(grupoGuardiaLetradoList);
//				}
				
				if(miForm.getPorGrupos()!=null && miForm.getPorGrupos().equals("1")){
					ArrayList<LetradoInscripcion> letradosColaGuardiaList = InscripcionGuardia.getColaGuardia(new Integer(miForm.getIdInstitucion()),new Integer(miForm.getIdTurno()), new Integer(miForm.getIdGuardia()), "sysdate","sysdate", this.getUserBean(request));
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
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
			return forward;
		}
	private String smitDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		try {

			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.setSolicitudAlta(true);
			miForm.setSolicitudBaja(false);
			miForm.setValidacionAlta(true);
			miForm.setValidacionBaja(false);
			miForm.setMasivo(true);
			miForm.reset(true,true);
			miForm.setModo("smitEditarTelefonosGuardia");
			miForm.setPorGrupos("1");
			forward = "validarInscripcion";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
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
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

		
	private String vitConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward ="";
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.reset(false,true);
			miForm.setIdGuardia(null);
			
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = null;
			if(miForm.getFechaSolicitud()!=null && !miForm.getFechaSolicitud().equals("")){
				inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), miForm.getFechaSolicitud(),true);
				//seteamos el paso siguiente
				miForm.setModo("vitConsultaGuardias");
				
			}
			miForm.setObservacionesValidacion("");
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			forward = "consultaTurnoInscripcion";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	private String vitConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
				try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")&& !miForm.getIdGuardia().equals("-1")&& miForm.getInscripcionTurno().getTurno().getGuardias()!=null &&miForm.getInscripcionTurno().getTurno().getGuardias().intValue()==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), new Integer(miForm.getIdGuardia()));
			}else{
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
			}
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			miForm.setModo("vitDatos");
			forward = "consultaGuardiasInscripcion";
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
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
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
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
				ArrayList<LetradoInscripcion> letradosColaGuardiaList = InscripcionGuardia.getColaGuardia(new Integer(miForm.getIdInstitucion()),new Integer(miForm.getIdTurno()), new Integer(miForm.getIdGuardia()), "sysdate","sysdate", this.getUserBean(request));
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
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
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
			
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	
	private String vigConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			Integer idGuardia=null;
			if(miForm.getIdGuardia()!=null &&!miForm.getIdGuardia().equals("")&&Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = Integer.parseInt(miForm.getIdGuardia());
			
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasParaInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()),idGuardia);
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			
			miForm.setModo("vigDatos");
			forward = "consultaGuardiasInscripcion";
			
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String sigConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			Integer idGuardia=null;
			if(miForm.getIdGuardia()!=null &&!miForm.getIdGuardia().equals("")&&Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
				idGuardia = Integer.parseInt(miForm.getIdGuardia());
			
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasParaInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()),idGuardia);
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			
				miForm.setModo("sigDatos");
			forward = "consultaGuardiasInscripcion";
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
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
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null,true);
						
						
			miForm.setInscripcionTurno(inscripcionTurno);
			
			miForm.setObservacionesValidacion("");
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			
			miForm.setModo("vbgConsultaGuardias");
			forward = "consultaTurnoInscripcion";

				
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
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

				
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	private String vbtConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")&& miForm.getTipoGuardias()!=null &&Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), new Integer(miForm.getIdGuardia()));
			}else{
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
			}
			
						
						
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			//seteamos el paso siguiente
			miForm.setModo("vbtDatos");

			forward = "consultaGuardiasInscripcion";
			
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String vbgConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")&& miForm.getTipoGuardias()!=null &&Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), new Integer(miForm.getIdGuardia()));
			}else{
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
			}
			
						
						
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			//seteamos el paso siguiente
			
			miForm.setModo("vbgDatos");
			
			forward = "consultaGuardiasInscripcion";
			
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
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
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
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
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	
	
	private String sigConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			miForm.reset(true,true);
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), miForm.getFechaSolicitudTurno(),true);
			
						
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			
			miForm.setModo("sigConsultaGuardias");
			forward = "consultaTurnoInscripcion";
			
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	private String vigConsultaTurno(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionTurnoAdm inscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null,true);
						
						
			miForm.setInscripcionTurno(inscripcionTurno);
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			
			miForm.setModo("vigConsultaGuardias");
			forward = "consultaTurnoInscripcion";
			
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	
	

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
			ScsInscripcionTurnoBean inscripcionTurno = inscripcionTurnoAdm.getInscripcionTurno(new Integer(miForm.getIdInstitucion()),
					new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null,true);
						
						
			miForm.setInscripcionTurno(inscripcionTurno);
			
			miForm.setObservacionesValidacion("");
			miForm.setTipoGuardias(miForm.getInscripcionTurno().getTurno().getGuardias().toString());
			miForm.setValidarInscripciones(miForm.getInscripcionTurno().getTurno().getValidarInscripciones().toString());
			
			miForm.setModo("sbgConsultaGuardias");
			forward = "consultaTurnoInscripcion";

				
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	private String sbgConsultaGuardias(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		

		try {
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
			ScsInscripcionGuardiaAdm inscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			List <ScsInscripcionGuardiaBean> alInscripcionGuardia = null;
			//si nos encontramos con con guardias que son de tipo todas o ninguna tendremos que validar todas sus guardias no de forma independiente
			if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")&& miForm.getTipoGuardias()!=null &&Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), new Integer(miForm.getIdGuardia()));
			}else{
				alInscripcionGuardia = inscripcionGuardiaAdm.getGuardiasInscripcion(new Integer(miForm.getIdInstitucion()),
						new Integer(miForm.getIdTurno()), new Long(miForm.getIdPersona()), null);
			}
			
						
						
			miForm.setInscripcionesGuardia(alInscripcionGuardia);
			//seteamos el paso siguiente
			miForm.setModo("sbgDatos");
			forward = "consultaGuardiasInscripcion";
			
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	

	
	
	private void comprobarRetencion(InscripcionTGForm miForm,UsrBean usrBean)throws SIGAException,ClsExceptions{
		// Comprobamos si el letrado actua como sociedad.
		
		// Si es 0, el letrado actua en modo propio
		String where = 
			" where "+	ScsRetencionesIRPFBean.T_NOMBRETABLA+"."+ScsRetencionesIRPFBean.C_IDINSTITUCION +" = " + usrBean.getLocation()+
			" and "+ScsRetencionesIRPFBean.T_NOMBRETABLA+"."+ScsRetencionesIRPFBean.C_IDPERSONA+" = " + miForm.getIdPersona();
		ScsRetencionesIRPFAdm irpf = new ScsRetencionesIRPFAdm(usrBean);
		Vector vIrpf = irpf.selectTabla(where);
		if(vIrpf!=null && vIrpf.size()>0){
			miForm.setIrpf(String.valueOf(vIrpf.size()));
		}else
		{
			miForm.setIrpf("0");
			CenComponentesAdm cenComponentesAdm = new CenComponentesAdm(usrBean);
			where = " where CEN_CLIENTE_IDINSTITUCION ="+usrBean.getLocation()+
			" and CEN_CLIENTE_IDPERSONA = "+miForm.getIdPersona() +
			" and SOCIEDAD = " + ClsConstants.DB_TRUE;
			Vector vCenComponentes = cenComponentesAdm.select(where);
			if(vCenComponentes.size() == 0)
			{
				ScsRetencionesAdm admRetenciones = new ScsRetencionesAdm(usrBean);
				where = " where letranifsociedad is null or letranifsociedad = ''";
				
				
				List vTipos = admRetenciones.getRetenciones(where,usrBean.getLanguage());
				if(vTipos!=null&&vTipos.size()>0)
					miForm.setRetenciones(vTipos);
				else
					throw new SIGAException("gratuita.retencionesIRPF.mensaje.error2");

				
			}
			// Si es > 0, el letrado actua como sociedad
			else
			{
				CenComponentesBean sociedad = (CenComponentesBean) vCenComponentes.get(0);
				CenPersonaAdm cenPersonaAdm = new CenPersonaAdm(usrBean);
				where = " where IDPERSONA = "+sociedad.getIdPersona();
				Vector vCenPersona = cenPersonaAdm.select(where);
				if(vCenPersona.size()>0)
				{
					String letra = ((CenPersonaBean) vCenPersona.get(0)).getNIFCIF().substring(0,1);
					// Miramos si la letra tiene rentencion asociada.
					where = " where "+	ScsRetencionesBean.T_NOMBRETABLA+"."+ScsRetencionesBean.C_LETRANIFSOCIEDAD +" = '" + letra+"'";
					ScsRetencionesAdm irpf2 = new ScsRetencionesAdm(usrBean);
					List lIrpf2 = irpf2.getRetenciones(where,usrBean.getLanguage());
					if(lIrpf2!=null && lIrpf2.size()>0)
					{
						miForm.setRetenciones(lIrpf2);
					}
					else
					{
						throw new SIGAException("gratuita.retencionesIRPF.mensaje.error1");
					}
				}else{
					throw new SIGAException("messages.general.error");
				}
				
			}
		}
		
		
	}
	private String comprobarBajaEnTodosLosTurnos (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
	throws SIGAException 
	{
//		String forward = "";
//		try {
//			//Controles generales
//			UsrBean usr = this.getUserBean(request);
//			InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
//			//obteniendo los turnos
//			ScsInscripcionTurnoAdm itAdm = new ScsInscripcionTurnoAdm(usr);
//			List vTurno = itAdm.getInscripcionesTurnoParaBaja(miForm.getIdInstitucion(), miForm.getIdPersona());
//			if (vTurno != null &&vTurno.size()>0){ 
//
//				//comprobando si hay alguna guardia pendiente, si hay alguna pendiente avisamos
//				for (int i = 0; i < vTurno.size(); i++) {
//					Hashtable turnoHash = (Hashtable) vTurno.get(i);
//					Integer idTurno = UtilidadesHash.getInteger(turnoHash, "IDTURNO");
//					String estadoPendientes = getEstadoGuardiasDesignasPendientes(
//							usr, Long.valueOf(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()), idTurno,null,null,null,false); 
//					if(estadoPendientes!=null){
//						miForm.setEstadoPendientes(estadoPendientes);
//						break;
//					}else{
//						miForm.setEstadoPendientes(null);
//						
//					}
//				}
//				miForm.setEstadoPendientes(null);
//				if(miForm.getEstadoPendientes()!=null&&!miForm.getEstadoPendientes().equals("")){
//					miForm.setModo("solicitarBajaEnTodosLosTurnos");
//					forward = "msjAvisoEstado";
//				}else{
//					return solicitarBajaEnTodosLosTurnos(mapping,  formulario,  request,  response);
//					
//				}
//			}else{
//				forward="exito";
//				
//			}
//			
//			
//			
//
//			//finalizando transaccion
//		}
//		catch (Exception e) 
//		{
//			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
//		}
//
//		return forward;
		return solicitarBajaEnTodosLosTurnos(mapping,  formulario,  request,  response);
	} 
	
	private String solicitarBajaEnTodosLosTurnos (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
	throws SIGAException 
	{
		String forward = "";

		try {
			//Controles generales
			UsrBean usr = this.getUserBean(request);
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
			String observaciones      = UtilidadesString.getMensajeIdioma(
					usr, "censo.sjcs.turnos.bajaEnTodosLosTurnos.observacion.literal");

			//obteniendo los turnos
			ScsInscripcionTurnoAdm itAdm = new ScsInscripcionTurnoAdm(usr);
			List vTurno = itAdm.getInscripcionesTurnoParaBaja(miForm.getIdInstitucion(), miForm.getIdPersona());
			
			if (vTurno != null &&vTurno.size()>0){
				//dando de baja todos los turnos
				InscripcionTurno inscripcion;
				for (int i = 0; i < vTurno.size(); i++) {
					Hashtable turnoHash = (Hashtable)vTurno.get(i);
					Integer idTurno = UtilidadesHash.getInteger(turnoHash, "IDTURNO");
					String validarInscripciones = UtilidadesHash.getString(turnoHash, "VALIDARINSCRIPCIONES");
//					String tipoGuardias = UtilidadesHash.getString(turnoHash, "GUARDIAS");
					inscripcion = InscripcionTurno.getInscripcionTurno(
							new Integer(miForm.getIdInstitucion()), idTurno, Long.valueOf(miForm.getIdPersona()),
							(String) turnoHash.get("FECHASOLICITUD"), usr, false);
					
					miForm.setFechaSolicitudBaja("sysdate");
					if(validarInscripciones.equals("N"))
						miForm.setFechaBaja("sysdate");
					else
						miForm.setFechaBaja(null);
					inscripcion.solicitarBaja(miForm.getFechaSolicitudBaja(),observaciones,miForm.getFechaBaja(),null,(String) turnoHash.get("FECHAVALIDACION"),validarInscripciones, null, usr);
	
				}
				forward = this.exitoRefresco("messages.updated.success", request);
			}else{
				forward="exito";
				
			} //for

			//finalizando transaccion
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		}
		return forward;
		

	}
	private String smbtInsertarBaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
		///comprobarInsertar
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			miForm.setFechaSolicitudBaja("sysdate");
			//dando de baja todos los turnos
			InscripcionTurno inscripcion;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idTurnoSel=d[0];
				String validarInscripciones=d[1];
				String fechaSolicitud=d[2];
				miForm.setIdTurno(idTurnoSel);
				miForm.setValidarInscripciones(validarInscripciones);

				Integer idTurno = new Integer(idTurnoSel);

				inscripcion = InscripcionTurno.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()), idTurno, Long.valueOf(miForm.getIdPersona()),
						 fechaSolicitud, usr, false);

				if(validarInscripciones.equals("N"))
					miForm.setFechaBaja("sysdate");
				
				inscripcion.solicitarBaja(miForm.getFechaSolicitudBaja(),miForm.getObservacionesBaja(),miForm.getFechaBaja(),miForm.getObservacionesValBaja(), miForm.getFechaValidacionTurno(),validarInscripciones, miForm.getTipoActualizacionSyC(), usr);

			}
			forward = "exito";
			request.setAttribute("modal", "1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	private String solicitudesMasivas (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
	throws SIGAException 
	{
		String forward = "";

		try {
			//Controles generales
			InscripcionTGForm miForm = (InscripcionTGForm) formulario;
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
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		}
		return forward;
		

	}
	
	private String vmitValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
//				String idGuardia= d[3];
				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
				String tipoGuardias = d[6];
				
				try {
					if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
					{
						ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
						ScsInscripcionTurnoBean insUltimaConBaja = admInsTurno.getInscripcion(new Integer(idInstitucion),new Integer( idTurno), new Long(idPersona), null);
						//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
						Boolean existeInscConBaja = insUltimaConBaja!=null && insUltimaConBaja.getFechaBaja()!=null && !insUltimaConBaja.getFechaBaja().equals("");
						if(existeInscConBaja){
							Date fechaBajaIscripcionActiva = GstDate.convertirFechaHora(insUltimaConBaja.getFechaBaja());
							Date fechaValidacion = GstDate.convertirFecha(miForm.getFechaValidacion(),"dd/MM/yyyy");
							
							
							if(fechaValidacion.compareTo(fechaBajaIscripcionActiva)<0){
								//String fechaDeBajaInscActiva = GstDate.getFormatedDateShort("", insUltimaConBaja.getFechaBaja()); 
								//request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));
								//request.setAttribute("mensaje","Existen turnos donde la fecha de validacion es inferior a la fecha de baja de la inscripcion activa con fecha de baja ");
								throw new Exception("gratuita.gestionInscripciones.error.valida.menor.baja.ia");
								
							}
							
						}
							
						
						
					}
					InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
							new Integer(idInstitucion), new Integer(idTurno), Long.valueOf(idPersona),
							fechaSolicitud, usr, false);
					
					if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")){
						inscripcion.denegarInscripcionTurno(miForm.getFechaDenegacion(),miForm.getObservacionesDenegacion(), usr);	
						
					}else if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
					{
						inscripcion.validarAlta(miForm.getFechaValidacion(),
								miForm.getObservacionesValidacion(), usr);
					}
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


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	
	private String vmigValidarBaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String fechaSolicitud= d[4];
				String tipoGuardias = d[6];				
				Integer idGuardia = null;
				if(Integer.parseInt(tipoGuardias)==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
					idGuardia = new Integer(d[3]);
				
//				String validarInscripciones = d[5];

				try {
					ScsInscripcionTurnoBean insTurnoActiva = null;
					ScsInscripcionGuardiaBean insGuardiaSiguiente = null;
					if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
					{

						ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
						insTurnoActiva = admInsTurno.getInscripcion(new Integer(idInstitucion),new Integer( idTurno), new Long(idPersona), miForm.getFechaValidacion());
						//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
						if(insTurnoActiva!=null){
							String fechaBajaTurno = null;
							if(insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
								Date dateFechaBajaTurno = GstDate.convertirFechaHora(insTurnoActiva.getFechaBaja());
								Date fechaHoy = GstDate.convertirFecha(GstDate.getHoyJsp(),"dd/MM/yyyy");
								
								fechaBajaTurno = GstDate.getFormatedDateShort("", insTurnoActiva.getFechaBaja());
								
								if(dateFechaBajaTurno.compareTo(fechaHoy)<0){
									request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.inscripcionturno.menor.hoy"));
									return "errorConAviso";
								}
							}
							ScsInscripcionGuardiaAdm admInsguardia = new ScsInscripcionGuardiaAdm(usr);
							
							//comprobamos si hay una inscripcion de guardia posterior dentro de las fechas del turno
							insGuardiaSiguiente = admInsguardia.getSiguienteInscripcion(idInstitucion,idTurno, idPersona,new Integer(d[3]),fechaBajaTurno, miForm.getFechaValidacion());
							
						}else{
							request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.sin.turno"));
							return "errorConAviso";
							
						}
						
					}
					
					
					
					
					
					//solo dejaremos validar una a una cuando sean a elegir
						InscripcionGuardia inscripcion = InscripcionGuardia.getInscripcionGuardia(
								new Integer(idInstitucion), new Integer(idTurno),idGuardia
										, Long.valueOf(idPersona),	fechaSolicitud, usr, false);
						
						if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
						{
							inscripcion.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
							if(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")){
								if(insGuardiaSiguiente!=null ){
									inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insGuardiaSiguiente.getFechaValidacion()),miForm.getObservacionesValBaja());
								}else if(insTurnoActiva!=null&&insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
									inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insTurnoActiva.getFechaBaja()),miForm.getObservacionesValBaja());
							}
						}
							
							
							inscripcion.validarAlta(usr);
						}else if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals(""))
						{
							inscripcion.setDenegacion(miForm.getObservacionesDenegacion(),miForm.getFechaDenegacion());
							inscripcion.denegarBajaGuardia(usr);
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


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	private String vmigValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String fechaSolicitud= d[4];
				String tipoGuardias = d[6];				
				Integer idGuardia = null;
				if(Integer.parseInt(tipoGuardias)==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
					idGuardia = new Integer(d[3]);
				
//				String validarInscripciones = d[5];

				try {
					ScsInscripcionTurnoBean insTurnoActiva = null;
					ScsInscripcionGuardiaBean insGuardiaSiguiente = null;
					if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
					{

						ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
						insTurnoActiva = admInsTurno.getInscripcion(new Integer(idInstitucion),new Integer( idTurno), new Long(idPersona), miForm.getFechaValidacion());
						//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
						if(insTurnoActiva!=null){
							String fechaBajaTurno = null;
							if(insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
								Date dateFechaBajaTurno = GstDate.convertirFechaHora(insTurnoActiva.getFechaBaja());
								Date fechaHoy = GstDate.convertirFecha(GstDate.getHoyJsp(),"dd/MM/yyyy");
								
								fechaBajaTurno = GstDate.getFormatedDateShort("", insTurnoActiva.getFechaBaja());
								
								if(dateFechaBajaTurno.compareTo(fechaHoy)<0){
									request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.inscripcionturno.menor.hoy"));
									return "errorConAviso";
								}
							}
							ScsInscripcionGuardiaAdm admInsguardia = new ScsInscripcionGuardiaAdm(usr);
							
							//comprobamos si hay una inscripcion de guardia posterior dentro de las fechas del turno
							insGuardiaSiguiente = admInsguardia.getSiguienteInscripcion(idInstitucion,idTurno, idPersona,new Integer(d[3]),fechaBajaTurno, miForm.getFechaValidacion());
							
						}else{
							request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.guardia.sin.turno"));
							return "errorConAviso";
							
						}
						
					}
					
					
					
					
					
					//solo dejaremos validar una a una cuando sean a elegir
						InscripcionGuardia inscripcion = InscripcionGuardia.getInscripcionGuardia(
								new Integer(idInstitucion), new Integer(idTurno),idGuardia
										, Long.valueOf(idPersona),	fechaSolicitud, usr, false);
						
						if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals("")&&(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")))
						{
							inscripcion.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
							if(miForm.getFechaBaja()==null || miForm.getFechaBaja().equals("")){
								if(insGuardiaSiguiente!=null ){
									inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insGuardiaSiguiente.getFechaValidacion()),miForm.getObservacionesValBaja());
								}else if(insTurnoActiva!=null&&insTurnoActiva.getFechaBaja()!=null && !insTurnoActiva.getFechaBaja().equals("")){
									inscripcion.setBajas(null, null, GstDate.getFormatedDateShort("",insTurnoActiva.getFechaBaja()),miForm.getObservacionesValBaja());
							}
						}
							
							
							inscripcion.validarAlta(usr);
						}else if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals(""))
						{
							inscripcion.setDenegacion(miForm.getObservacionesDenegacion(),miForm.getFechaDenegacion());
							inscripcion.denegarBajaGuardia(usr);
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


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	private String vmbtComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
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
//				String fechaSolicitudBaja = d[8];
				
//				Date fechaSolicitudBajaDate = GstDate.convertirFecha(GstDate.getFormatedDateShort("", fechaSolicitudBaja));
//				Date fechaValidacionDate = GstDate.convertirFecha(miForm.getFechaBaja(),"dd/MM/yyyy");
//				

//				if(fechaSolicitudBajaDate.compareTo(fechaValidacionDate)>0){
//					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.error.masivo.fBajaMenorfSolBaja"));
//					//request.setAttribute("mensaje","Existen fechas de baja anteriores a la fecha de solicitud de baja ");
//					return "errorConAviso";
//					
//				}
				
					
//				Si estamos dando de baja y no denegandola comprobamos que la fecha de baja sea mayor que la fecha de validacion
					if(fechaValidacion!=null&&miForm.getFechaBaja()!=null&&!miForm.getFechaBaja().equals("")){
						Date fechaBaja = GstDate.convertirFecha(miForm.getFechaBaja(),"dd/MM/yyyy");
						Date dateFechaValidacion= GstDate.convertirFechaHora(fechaValidacion);
						if(fechaBaja.compareTo(dateFechaValidacion)<0){
							continue;
								
						}
					}
					String estadoPendientes = getEstadoGuardiasDesignasPendientes(
							  usr, Long.valueOf(idPersona), new Integer(idInstitucion), 
							  new Integer(idTurno),null,null, miForm.getFechaBaja(),false,this.tipoActualizacionBaja);
					miForm.setEstadoPendientes(estadoPendientes);
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
						break;
					}

			}
			
			if(hayEstadosPendientes){
				miForm.setModo("vmbtValidar");
				return "msjAvisoEstado";
			}else{
				return vmbtValidar(mapping, formulario, request, response);
								
			}


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	
	
	
	
	private String vmbtValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		miForm.setEstadoPendientes(null);
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
//				String idGuardia= d[3];
				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
//				String tipoGuardias = d[6];
				String fechaValidacion = d[7];
				if(fechaValidacion!=null && fechaValidacion.equals("-"))
					fechaValidacion = null;
//				String fechaSolicitudBaja = d[8];
				
				
				


				
				
				
				
				
				try {
					InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
							new Integer(idInstitucion), new Integer(idTurno), Long.valueOf(idPersona),
							fechaSolicitud, usr, false);
					if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals("")){
						//denegar
						inscripcion.denegarBajaInscripcionTurno(miForm.getFechaDenegacion(),miForm.getObservacionesDenegacion(),usr);
					}else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
					{
						if(fechaValidacion!=null){
							Date fechaBaja = GstDate.convertirFecha(miForm.getFechaBaja(),"dd/MM/yyyy");
							Date dateFechaValidacion= GstDate.convertirFechaHora(fechaValidacion);
							if(fechaBaja.compareTo(dateFechaValidacion)<0){
								throw new SIGAException("gratuita.gestionInscripciones.error.baja.menor.valida");
								
							}
						}
						
						inscripcion.validarBaja(miForm.getFechaBaja(),
							fechaValidacion,miForm.getObservacionesValBaja(), miForm.getValidarInscripciones(), usr);
					
					}
				} catch (Exception e) {
					existenErrores = true;
				}
				
				
			} 
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.solapamiento");
			}else{
				request.setAttribute("mensaje","messages.updated.success");				
			}	
			
			
			forward = "exito";
			request.setAttribute("modal", "1");


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	
	
	private String vmbgComprobarValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
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

				String tipoGuardias = d[6];
				
				Integer idGuardia = null;
				if(Integer.parseInt(tipoGuardias)==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
					idGuardia = new Integer(idGuardiaStr);
				String fechaValidacion = d[7];
				if(fechaValidacion!=null && fechaValidacion.equals("-"))
					fechaValidacion = null;
				
				
					if(fechaValidacion!=null&&miForm.getFechaBaja()!=null &&!miForm.getFechaBaja().equals("")){
						Date fechaBaja = GstDate.convertirFecha(miForm.getFechaBaja(),"dd/MM/yyyy");
						Date dateFechaValidacion= GstDate.convertirFechaHora(fechaValidacion);
						if(fechaBaja.compareTo(dateFechaValidacion)<0){
							continue;
								
						}
					}
					if(miForm.getFechaBaja()!=null &&!miForm.getFechaBaja().equals("")){
						String estadoPendientes = getEstadoGuardiasDesignasPendientes(
								  usr, Long.valueOf(idPersona), new Integer(idInstitucion), 
								  new Integer(idTurno),idGuardia,null, miForm.getFechaBaja(),true,this.tipoActualizacionBaja);
						miForm.setEstadoPendientes(estadoPendientes);
						if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
							hayEstadosPendientes = true;
							break;
						}
					}

			}
			
			if(hayEstadosPendientes){
				miForm.setModo("vmbgValidar");
				return "msjAvisoEstado";
			}else{
				return vmbgValidar(mapping, formulario, request, response);
								
			}


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	private String vmbgValidar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
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
//				String fechaValidacion = d[7];
				String fechaValidacion = d[7];
				Integer idGuardia = null;
				if(Integer.parseInt(tipoGuardias)==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
					idGuardia = new Integer(idGuardiaStr);
				
				
				if(fechaValidacion!=null && fechaValidacion.equals("-"))
					fechaValidacion = null;
				try {
					InscripcionGuardia inscripcion = InscripcionGuardia.getInscripcionGuardia(
							new Integer(idInstitucion), new Integer(idTurno),idGuardia
									, Long.valueOf(idPersona),	fechaSolicitud, usr, false);
					if(miForm.getFechaDenegacion()!=null && !miForm.getFechaDenegacion().equals(""))
					{
						inscripcion.setDenegacion(miForm.getObservacionesDenegacion(),miForm.getFechaDenegacion());
						inscripcion.denegarBajaGuardia(usr);
					}else if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
					{
						if(fechaValidacion!=null){
							Date fechaBaja = GstDate.convertirFecha(miForm.getFechaBaja(),"dd/MM/yyyy");
							Date dateFechaValidacion= GstDate.convertirFechaHora(fechaValidacion);
							if(fechaBaja.compareTo(dateFechaValidacion)<0){
								throw new SIGAException("gratuita.gestionInscripciones.error.baja.menor.valida");
									
							}
						}
					
					
					
						inscripcion.setBajas(null,null, miForm.getFechaBaja(),miForm.getObservacionesValBaja());
						inscripcion.validarBaja(usr, miForm.getTipoActualizacionSyC());
					}
					
					
					
				} catch (Exception e) {
					existenErrores = true;
				}
			} 
			miForm.reset(true,true);
			if(existenErrores){
				request.setAttribute("mensaje","gratuita.gestionInscripciones.error.solapamiento");
			}else{
				request.setAttribute("mensaje","messages.updated.success");				
			}	
			forward = "exito";
			request.setAttribute("modal", "1");


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	private String comprobarAmfvtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		try {
			UsrBean usr = this.getUserBean(request);
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
				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
//				String tipoGuardias = d[6];
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				try {
					comprobarActualizacionFechaValidacionTurno(miForm,inscripcion,usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
						break;
					}
					
				}
			}
			
			if(hayEstadosPendientes){
				miForm.setModo("amfvtModificar");
				return "msjAvisoEstado";
			}else{
				return amfvtModificar(mapping, formulario, request, response);
								
			}
			
			
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	private String amfvtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
//				String tipoGuardias = d[6];
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				try {
					comprobarActualizacionFechaValidacionTurno(miForm,inscripcion,this.getUserBean(request));
					InscripcionTurno inscripcionTurno = InscripcionTurno.getInscripcionTurno(
							new Integer(idInstitucion), new Integer(idTurno), Long.valueOf(idPersona),
							fechaSolicitud, usr, false);
					inscripcionTurno.modificarFechaValidacion(miForm.getFechaValidacion(),
								miForm.getObservacionesValidacion(), usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()==null || miForm.getEstadoPendientes().equals("")){
						existenErrores = true;
					}else{
						InscripcionTurno inscripcionTurno = InscripcionTurno.getInscripcionTurno(
								new Integer(idInstitucion), new Integer(idTurno), Long.valueOf(idPersona),
								fechaSolicitud, usr, false);
						inscripcionTurno.modificarFechaValidacion(miForm.getFechaValidacion(),
									miForm.getObservacionesValidacion(), usr);
					}
					
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


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	private String amfvgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
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
//				String validarInscripciones = d[5];
				String tipoGuardias = d[6];
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setIdGuardia(idGuardia);
				inscripcion.setTipoGuardias(tipoGuardias);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				
				try {
					comprobarActualizacionFechaValidacionGuardia(miForm,inscripcion,this.getUserBean(request));
					InscripcionGuardia inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia(
							new Integer(idInstitucion), new Integer(idTurno),new Integer(idGuardia)
									, Long.valueOf(idPersona),	fechaSolicitud, usr, false);
					inscripcionGuardia.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
					inscripcionGuardia.modificarFechaValidacion(usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()==null || miForm.getEstadoPendientes().equals("")){
						existenErrores = true;
					}else{
						InscripcionGuardia inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia(
								new Integer(idInstitucion), new Integer(idTurno),new Integer(idGuardia)
										, Long.valueOf(idPersona),	fechaSolicitud, usr, false);
						inscripcionGuardia.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
						inscripcionGuardia.modificarFechaValidacion(usr);
					}
					
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


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	
	
	
	
	protected String actualizarFechaValidacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, Exception {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		if(miForm.getIdGuardia()!=null && miForm.getIdGuardia().equals("-1"))
			miForm.setIdGuardia("");
		if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")){
			try {
				comprobarActualizacionFechaValidacionGuardia(miForm,miForm,this.getUserBean(request));
			} catch (SIGAException e) {
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("afvgModificar");
					return "msjAvisoEstado";
				}else{
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(this.getUserBean(request), e.getLiteral()));
					return "errorConAviso";
				}
			}
			return afvgModificar(mapping, formulario, request, response);
		}else{
			try {
				comprobarActualizacionFechaValidacionTurno(miForm,miForm,this.getUserBean(request));
			} catch (SIGAException e) {
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("afvtModificar");
					return "msjAvisoEstado";
				}else{
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(this.getUserBean(request), e.getLiteral()));
					return "errorConAviso";
				}
			}
			return afvtModificar(mapping, formulario, request, response);
		}
	
			
		
		
	}
	protected String actualizarFechaBaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, Exception {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		if(miForm.getIdGuardia()!=null && miForm.getIdGuardia().equals("-1"))
			miForm.setIdGuardia("");
		
		if(miForm.getIdGuardia()!=null && !miForm.getIdGuardia().equals("")){
			try {
				comprobarActualizacionFechaBajaGuardia(miForm,miForm, this.getUserBean(request));
			} catch (SIGAException e) {
				
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("afbgModificar");
					return "msjAvisoEstado";
				}else{
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(this.getUserBean(request), e.getLiteral()));
					return "errorConAviso";
				}
			}
			return afbgModificar(mapping, formulario, request, response);
		}
		else{
			try {
				comprobarActualizacionFechaBajaTurno(miForm,miForm, this.getUserBean(request));
			} catch (SIGAException e) {
				if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
					miForm.setModo("afbtModificar");
					return "msjAvisoEstado";
				}else{
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(this.getUserBean(request), e.getLiteral()));
					return "errorConAviso";
				}
			}
			
			return afbtModificar(mapping, formulario, request, response);
		}
		
	}
	protected void comprobarActualizacionFechaBajaGuardia(InscripcionTGForm formulario,InscripcionTGForm inscripcionActual,UsrBean usr)throws ClsExceptions,SIGAException,Exception{
		inscripcionActual.setTipo("B");
		ScsInscripcionGuardiaAdm admInsGuardia = new ScsInscripcionGuardiaAdm(usr);
		//Sacamios el idguardia para comprobar las guardias pendientes
		Integer idGuardia = null; 
		if(inscripcionActual.getTipoGuardias()!=null &&Integer.parseInt(inscripcionActual.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
			idGuardia = new Integer(inscripcionActual.getIdGuardia());
		}
		
		
		//hay que traerse el tipo de gurdias ya que si es todas o ninguna se modificarar a la vez
		
		List<ScsInscripcionGuardiaBean> inscripcionesList =  admInsGuardia.getInscripcionesGuardia(inscripcionActual,true);
		int orden =0;
		ScsInscripcionGuardiaBean inscripcion=null;
		for(ScsInscripcionGuardiaBean inscripcionBean:inscripcionesList){
			if(inscripcionBean.getFechaSuscripcion().equals(inscripcionActual.getFechaSolicitud())){
				inscripcion = inscripcionBean;
				break;
			}
			orden++;
		}
		//comprobamos que la fecha de baja es mayor que su fecha de validacion
		Date fechaBaja = GstDate.convertirFecha(formulario.getFechaBaja(),"dd/MM/yyyy");
		Date fechaValidacion= GstDate.convertirFechaHora(inscripcion.getFechaValidacion());
		
		
		if(fechaBaja.compareTo(fechaValidacion)<0){
			throw new SIGAException("gratuita.gestionInscripciones.error.baja.menor.valida");
			
		}
		//En el caso de que haya una inscripcion posterior, la fecha de baja debe ser anterior
		//a la fecha de validacion
		ScsInscripcionGuardiaBean inscripcionPosterior = null;
		try {
			inscripcionPosterior = inscripcionesList.get(orden+1);	
		} catch (Exception e) {
			//solo se comprueba si hay inscripcion porterior
			Date fechaBajaOld =GstDate.convertirFechaHora(inscripcion.getFechaBaja());
			if(fechaBajaOld.compareTo(fechaBaja)>0){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
					inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), idGuardia, 
					formulario.getFechaBaja(),GstDate.getFormatedDateShort("",inscripcion.getFechaBaja()),true,this.tipoActualizacionBaja);
				if(estadoPendientes!=null && !estadoPendientes.equals("")){
					formulario.setEstadoPendientes(estadoPendientes);
					throw new SIGAException(estadoPendientes);
				}
			}
			
			return;
		}
			Date fechaValidacionInscripcionPosterior = GstDate.convertirFechaHora(inscripcionPosterior.getFechaValidacion());
			if(fechaBaja.compareTo(fechaValidacionInscripcionPosterior)>0){
				throw new SIGAException("gratuita.gestionInscripciones.error.baja.mayor.valida.ip");
				
			}
			Date fechaBajaOld =GstDate.convertirFechaHora(inscripcion.getFechaBaja());
			if(fechaBajaOld.compareTo(fechaBaja)>0){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
					inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), idGuardia, 
					formulario.getFechaBaja(),GstDate.getFormatedDateShort("",inscripcion.getFechaBaja()),true,this.tipoActualizacionBaja);
				if(estadoPendientes!=null && !estadoPendientes.equals("")){
					formulario.setEstadoPendientes(estadoPendientes);
					throw new SIGAException(estadoPendientes);
				}
			}
		
		
		
		
	}
	protected void comprobarActualizacionFechaValidacionGuardia(InscripcionTGForm formulario,InscripcionTGForm inscripcionActual,UsrBean usr)throws ClsExceptions,SIGAException,Exception{
		inscripcionActual.setTipo("A");
		ScsInscripcionGuardiaAdm admInsGuardia = new ScsInscripcionGuardiaAdm(usr);
		Integer idGuardia = null; 
		if(inscripcionActual.getTipoGuardias()!=null &&Integer.parseInt(inscripcionActual.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR ){
			idGuardia = new Integer(inscripcionActual.getIdGuardia());
		}

		
		
		
		List<ScsInscripcionGuardiaBean> inscripcionesList =  admInsGuardia.getInscripcionesGuardia(inscripcionActual,true);
		int orden =0;
		ScsInscripcionGuardiaBean inscripcion=null;
		for(ScsInscripcionGuardiaBean inscripcionBean:inscripcionesList){
			if(inscripcionBean.getFechaSuscripcion().equals(inscripcionActual.getFechaSolicitud())){
				inscripcion = inscripcionBean;
				break;
			}
			orden++;
		}
		//comprobamos que la fecha de baja es mayor que su fecha de validacion
		Date fechaValidacion = GstDate.convertirFecha(formulario.getFechaValidacion(),"dd/MM/yyyy");
		if(inscripcionActual.getIdGuardia()!=null||!inscripcionActual.getIdGuardia().equals("")){
			ScsInscripcionTurnoAdm itAdm = new ScsInscripcionTurnoAdm(usr);
			ScsInscripcionTurnoBean inscTurno = itAdm.getInscripcionSinBaja(new Integer(inscripcionActual.getIdInstitucion()),new Integer(inscripcionActual.getIdTurno()),new Long(inscripcionActual.getIdPersona()));
			inscripcionActual.setFechaValidacionTurno(inscTurno.getFechaValidacion());
		}
		

		Date fechaValidacionTurno= GstDate.convertirFecha(GstDate.getFormatedDateShort("",inscripcionActual.getFechaValidacionTurno()),"dd/MM/yyyy");
		if(fechaValidacion.compareTo(fechaValidacionTurno)<0){
			throw new SIGAException("gratuita.gestionInscripciones.error.validaGuardia.menor.validaTurno");
			
		}
	//En el caso de que haya una inscripcion anterior, la fecha de validacion debe ser anterior
	//a la fecha de baja
	

		ScsInscripcionGuardiaBean inscripcionAnterior = null;
		try {
			inscripcionAnterior = inscripcionesList.get(orden-1);	
		} catch (Exception e) {
			Date fechaValidacionOld =GstDate.convertirFechaHora(inscripcion.getFechaValidacion());
			if(fechaValidacion.compareTo(fechaValidacionOld)>0){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
					inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), idGuardia, GstDate.getFormatedDateShort("",inscripcion.getFechaValidacion()),formulario.getFechaValidacion(),true,this.tipoActualizacionBaja);
				if(estadoPendientes!=null && !estadoPendientes.equals("")){
					formulario.setEstadoPendientes(estadoPendientes);
					throw new SIGAException(estadoPendientes);
					
				}
			}
			return;
		}
		
		
		Date fechaBajaInscripcionAnterior = GstDate.convertirFechaHora(inscripcionAnterior.getFechaBaja());
		if(fechaValidacion.compareTo(fechaBajaInscripcionAnterior)<0){
			throw new SIGAException("gratuita.gestionInscripciones.error.valida.menor.baja.ia");
			
		}
		Date fechaValidacionOld =GstDate.convertirFechaHora(inscripcion.getFechaValidacion());
		if(fechaValidacion.compareTo(fechaValidacionOld)>0){
			String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
				inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), idGuardia, GstDate.getFormatedDateShort("",inscripcion.getFechaValidacion()),formulario.getFechaValidacion(),true,this.tipoActualizacionEdicion);
			if(estadoPendientes!=null && !estadoPendientes.equals("")){
				formulario.setEstadoPendientes(estadoPendientes);
				throw new SIGAException(estadoPendientes);
				
			}
		}
		
	}
	
	
	protected void comprobarActualizacionFechaBajaTurno(InscripcionTGForm formulario,InscripcionTGForm inscripcionActual,UsrBean usr)throws ClsExceptions,SIGAException,Exception{
		
		inscripcionActual.setTipo("B");
		ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
		
		
		
		List<ScsInscripcionTurnoBean> inscripcionesList =  admInsTurno.getInscripcionesTurno(inscripcionActual,true);
		int orden =0;
		ScsInscripcionTurnoBean inscripcion=null;
		for(ScsInscripcionTurnoBean inscripcionBean:inscripcionesList){
			if(inscripcionBean.getFechaSolicitud().equals(inscripcionActual.getFechaSolicitud())){
				inscripcion = inscripcionBean;
				break;
			}
			orden++;
		}
		//comprobamos que la fecha de baja es mayor que su fecha de validacion
		Date fechaBaja = GstDate.convertirFecha(formulario.getFechaBaja(),"dd/MM/yyyy");
		Date fechaValidacion= GstDate.convertirFechaHora(inscripcion.getFechaValidacion());
		
		
		if(fechaBaja.compareTo(fechaValidacion)<=0){
			throw new SIGAException("gratuita.gestionInscripciones.error.baja.menor.valida");
			
		}
		//En el caso de que haya una inscripcion posterior, la fecha de baja debe ser anterior
		//a la fecha de validacion
		Date fechaBajaOld =GstDate.convertirFechaHora(inscripcion.getFechaBaja());
		ScsInscripcionTurnoBean inscripcionPosterior = null;
		try {
			inscripcionPosterior = inscripcionesList.get(orden+1);	
		} catch (Exception e) {
			
			if(fechaBajaOld.compareTo(fechaBaja)>0){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
						inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), null, formulario.getFechaBaja(),GstDate.getFormatedDateShort("",inscripcion.getFechaBaja()),false,this.tipoActualizacionEdicion);
					if(estadoPendientes!=null && !estadoPendientes.equals("")){
						formulario.setEstadoPendientes(estadoPendientes);
						throw new SIGAException(estadoPendientes);
						
					}
			}
			
			
			
			return;
		}
		
		
		Date fechaValidacionInscripcionPosterior = GstDate.convertirFechaHora(inscripcionPosterior.getFechaValidacion());
		if(fechaBaja.compareTo(fechaValidacionInscripcionPosterior)>0){
			throw new SIGAException("gratuita.gestionInscripciones.error.baja.mayor.valida.ip");
			
		}
		
			
			
			
			
			
			if(fechaBajaOld.compareTo(fechaBaja)>0){
				ScsInscripcionGuardiaAdm admInsGuardia = new ScsInscripcionGuardiaAdm(usr);
				boolean existenInscripcionesGuardiaActivas = admInsGuardia.existenInscripcionesGuardiaActivas(
						inscripcion.getIdInstitucion(), inscripcion.getIdTurno(),
						inscripcion.getIdPersona(), formulario.getFechaBaja(),  GstDate.getFormatedDateShort("",inscripcion.getFechaBaja()));
				if(existenInscripcionesGuardiaActivas){
					throw new SIGAException("gratuita.gestionInscripciones.error.masivo.bajaguardia.mayor.bajaturno");
				}
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
					inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), null, formulario.getFechaBaja(),GstDate.getFormatedDateShort("",inscripcion.getFechaBaja()),false,this.tipoActualizacionEdicion);
				if(estadoPendientes!=null && !estadoPendientes.equals("")){
					formulario.setEstadoPendientes(estadoPendientes);
					throw new SIGAException(estadoPendientes);
					
				}
			}
			
	}
	protected void comprobarActualizacionGuardiasTurno(InscripcionTGForm inscripcionTurno,UsrBean usr) throws SIGAException, Exception{
		ScsInscripcionGuardiaAdm admInsGuardia = new ScsInscripcionGuardiaAdm(usr);
		List<ScsInscripcionGuardiaBean> inscripcionesList =  admInsGuardia.getInscripcionesGuardia(inscripcionTurno,true);
		if(inscripcionesList!=null && inscripcionesList.size()>0){
			for(ScsInscripcionGuardiaBean inscripcionBean:inscripcionesList){
				InscripcionTGForm inscripcionform = inscripcionBean.getInscripcion();
				inscripcionform.setFechaValidacionTurno(inscripcionTurno.getFechaValidacion());
				
				//esto lo metemos por el formato
				inscripcionform.setFechaValidacion(inscripcionBean.getFechaValidacion());
				inscripcionform.setFechaSolicitud(inscripcionBean.getFechaSuscripcion());
				comprobarActualizacionFechaValidacionGuardia(inscripcionform,inscripcionform, usr);
			}
		}
		
	}
	
	protected void comprobarActualizacionFechaValidacionTurno(InscripcionTGForm formulario,InscripcionTGForm inscripcionActual,UsrBean usr)throws ClsExceptions,SIGAException,Exception{
		ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
		inscripcionActual.setTipo("A");
		
		List<ScsInscripcionTurnoBean> inscripcionesList =  admInsTurno.getInscripcionesTurno(inscripcionActual,true);
		int orden =0;
		ScsInscripcionTurnoBean inscripcion=null;
		for(ScsInscripcionTurnoBean inscripcionBean:inscripcionesList){
			if(inscripcionBean.getFechaSolicitud().equals(inscripcionActual.getFechaSolicitud())){
				inscripcion = inscripcionBean;
				break;
			}
			orden++;
		}

		Date fechaValidacion = GstDate.convertirFecha(formulario.getFechaValidacion(),"dd/MM/yyyy");

		//		Date fechaSolicitud= GstDate.convertirFecha(GstDate.getFormatedDateShort("",inscripcionActual.getFechaSolicitud()),"dd/MM/yyyy");
//		if(fechaSolicitud.compareTo(fechaValidacion)>0){
//			throw new SIGAException("gratuita.gestionInscripciones.error.fValMayorfSol");
//			
//		}
		//En el caso de que haya una inscripcion posterior, la fecha de baja debe ser anterior
		//a la fecha de validacion
			
		ScsInscripcionTurnoBean inscripcionAnterior = null;
		try {
			
			inscripcionAnterior = inscripcionesList.get(orden-1);
			
		} catch (Exception e) {
			Date fechaValidacionOld =GstDate.convertirFechaHora(inscripcion.getFechaValidacion());
			if(fechaValidacion.compareTo(fechaValidacionOld)>0){
				String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
					inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), null, GstDate.getFormatedDateShort("",inscripcion.getFechaValidacion()),formulario.getFechaValidacion(),false,this.tipoActualizacionEdicion);
				if(estadoPendientes!=null && !estadoPendientes.equals("")){
					formulario.setEstadoPendientes(estadoPendientes);
					throw new SIGAException(estadoPendientes);
					
				}
			}
			 
			return;
		}
		
		Date fechaBajaInscripcionAnterior = GstDate.convertirFechaHora(inscripcionAnterior.getFechaBaja());
		if(fechaValidacion.compareTo(fechaBajaInscripcionAnterior)<0){
			throw new SIGAException("gratuita.gestionInscripciones.error.valida.menor.baja.ia");
			
		}
		Date fechaValidacionOld =GstDate.convertirFechaHora(inscripcion.getFechaValidacion());
		if(fechaValidacion.compareTo(fechaValidacionOld)>0){
			String estadoPendientes = getEstadoGuardiasDesignasPendientes(usr, inscripcion.getIdPersona(),
				inscripcion.getIdInstitucion(), inscripcion.getIdTurno(), null, GstDate.getFormatedDateShort("",inscripcion.getFechaValidacion()),formulario.getFechaValidacion(),false,this.tipoActualizacionEdicion);
			if(estadoPendientes!=null && !estadoPendientes.equals("")){
				formulario.setEstadoPendientes(estadoPendientes);
				throw new SIGAException(estadoPendientes);
				
			}
		}
		
		
			
	}
	
	
	
	protected String consultaInscripcion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		return "consultaInscripcion";
	}
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
			Vector vTurno = turnoAdm.getTurnosDisponibles(hash,new Long((String)request.getSession().getAttribute("idPersonaTurno")),new Integer(usr.getLocation()));
			request.setAttribute("resultado",vTurno);
			request.setAttribute("mantTurnos","1");
			request.setAttribute("idPersonaTurno",(String)request.getSession().getAttribute("idPersonaTurno"));

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return forward;
}
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
			Vector vTurno = turnoAdm.getTurnosDisponiblesBaja(hash,new Long((String)request.getSession().getAttribute("idPersonaTurno")),new Integer(usr.getLocation()));
			request.setAttribute("resultado",vTurno);
			request.setAttribute("mantTurnos","1");
			request.setAttribute("idPersonaTurno",(String)request.getSession().getAttribute("idPersonaTurno"));

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return forward;
}

	private String afvtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			
				InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()), Long.valueOf(miForm.getIdPersona()),
						miForm.getFechaSolicitud(), usr, false);
				
			

				inscripcion.modificarFechaValidacion(miForm.getFechaValidacion(),miForm.getObservacionesValidacion(), usr);
			
				miForm.reset(true,true);
				
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	private String comprobarAmfvgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		try {
			UsrBean usr = this.getUserBean(request);
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
				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
				String tipoGuardias = d[6];
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				inscripcion.setIdGuardia(idGuardia);
				inscripcion.setTipoGuardias(tipoGuardias);
				try {
					comprobarActualizacionFechaValidacionGuardia(miForm,inscripcion,usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
						break;
					}
					
				}
			}
			
			if(hayEstadosPendientes){
				miForm.setModo("amfvgModificar");
				return "msjAvisoEstado";
			}else{
				return amfvgModificar(mapping, formulario, request, response);
								
			}
			
			
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	
	private String afvgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		String forward = "error";
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		
		
		
		try {
			
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			//solo dejaremos validar una a una cuando sean a elegir
			InscripcionGuardia inscripcion = InscripcionGuardia.getInscripcionGuardia(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia
								, Long.valueOf(miForm.getIdPersona()),	miForm.getFechaSolicitud(), usr, false);
				
				
			inscripcion.setAltas(null, miForm.getFechaValidacion(), miForm.getObservacionesValidacion());
			inscripcion.modificarFechaValidacion(usr);
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	
	private String afbtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;		
		UsrBean usr = this.getUserBean(request);
		String forward = "error";
		try {
			
			InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
						new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()), Long.valueOf(miForm.getIdPersona()),
						miForm.getFechaSolicitud(), usr, false);
				
			inscripcion.modificarFechaBaja(miForm.getFechaBaja(), usr);
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
	        request.setAttribute("modal", "1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	private String afbgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		String forward = "error";
		Integer idGuardia = null;
		if(Integer.parseInt(miForm.getTipoGuardias())==ScsTurnoBean.TURNO_GUARDIAS_ELEGIR)
			idGuardia = new Integer(miForm.getIdGuardia());
		
		
		
		try {
			
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			//solo dejaremos validar una a una cuando sean a elegir
			InscripcionGuardia inscripcion = InscripcionGuardia.getInscripcionGuardia(
					new Integer(miForm.getIdInstitucion()), new Integer(miForm.getIdTurno()),idGuardia
							, Long.valueOf(miForm.getIdPersona()),	miForm.getFechaSolicitud(), usr, false);
				
				
			inscripcion.setBajas(null, null,miForm.getFechaBaja(),miForm.getObservacionesValBaja());
			inscripcion.modificarFechaBaja(usr);
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			request.setAttribute("modal","1");
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}

	
	
	private String comprobarAmfbtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean hayEstadosPendientes = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String fechaSolicitud= d[4];
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				try {
					comprobarActualizacionFechaBajaTurno(miForm,inscripcion,usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
						break;
					}
					
				}
			}
			
			if(hayEstadosPendientes){
				miForm.setModo("amfbtModificar");
				return "msjAvisoEstado";
			}else{
				return amfbtModificar(mapping, formulario, request, response);
								
			}
			
			
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	private String amfbtModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
			String turnosSel = miForm.getTurnosSel();
			GstStringTokenizer st1 = new GstStringTokenizer(turnosSel,",");
			boolean existenErrores = false;
			while (st1.hasMoreTokens()) {
				String registro = st1.nextToken();
				String d[]= registro.split("##");
				String idInstitucion = d[0];
				String idPersona= d[1];
				String idTurno= d[2];
				String fechaSolicitud= d[4];
//				String validarInscripciones = d[5];
//				String tipoGuardias = d[6];
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				
				
				try {
					comprobarActualizacionFechaBajaTurno(miForm,inscripcion,this.getUserBean(request));
					InscripcionTurno inscripcionTurno = InscripcionTurno.getInscripcionTurno(
							new Integer(idInstitucion), new Integer(idTurno), Long.valueOf(idPersona),
							fechaSolicitud, usr, false);
					inscripcionTurno.modificarFechaBaja(miForm.getFechaBaja(), usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()==null || miForm.getEstadoPendientes().equals("")){
						existenErrores = true;
					}else{
						InscripcionTurno inscripcionTurno = InscripcionTurno.getInscripcionTurno(
								new Integer(idInstitucion), new Integer(idTurno), Long.valueOf(idPersona),
								fechaSolicitud, usr, false);
						inscripcionTurno.modificarFechaBaja(miForm.getFechaBaja(), usr);
					}
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


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	private String comprobarAmfbgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		try {
			UsrBean usr = this.getUserBean(request);
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
				String fechaSolicitud= d[4];
				String tipoGuardias = d[6];
				
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				inscripcion.setTipoGuardias(tipoGuardias);
				inscripcion.setIdGuardia(idGuardia);
				
				try {
					comprobarActualizacionFechaBajaGuardia(miForm,inscripcion,usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()!=null && !miForm.getEstadoPendientes().equals("")){
						hayEstadosPendientes = true;
						break;
					}
					
				}
			}
			
			if(hayEstadosPendientes){
				miForm.setModo("amfbgModificar");
				return "msjAvisoEstado";
			}else{
				return amfbgModificar(mapping, formulario, request, response);
								
			}
			
			
		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
	}
	
	
	private String amfbgModificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		InscripcionTGForm miForm = (InscripcionTGForm) formulario;
		
		String forward = "error";
		try {
			UsrBean usr = this.getUserBean(request);
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
//				String validarInscripciones = d[5];
				String tipoGuardias = d[6];
				InscripcionTGForm inscripcion = new InscripcionTGForm();
				inscripcion.setIdTurno(idTurno);
				inscripcion.setIdPersona(idPersona);
				inscripcion.setIdInstitucion(idInstitucion);
				inscripcion.setIdGuardia(idGuardia);
				inscripcion.setFechaSolicitud(fechaSolicitud);
				inscripcion.setTipoGuardias(tipoGuardias);
				
				
				try {
					comprobarActualizacionFechaBajaGuardia(miForm,inscripcion,this.getUserBean(request));

					InscripcionGuardia inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia(
							new Integer(idInstitucion), new Integer(idTurno),new Integer(idGuardia)
									, Long.valueOf(idPersona),	fechaSolicitud, usr, false);
					
					
					inscripcionGuardia.setBajas(null, null, miForm.getFechaBaja(),miForm.getObservacionesValBaja());
					inscripcionGuardia.modificarFechaBaja(usr);
				} catch (SIGAException e) {
					if(miForm.getEstadoPendientes()==null || miForm.getEstadoPendientes().equals("")){
						existenErrores = true;
					}else{

						InscripcionGuardia inscripcionGuardia = InscripcionGuardia.getInscripcionGuardia(
								new Integer(idInstitucion), new Integer(idTurno),new Integer(idGuardia)
										, Long.valueOf(idPersona),	fechaSolicitud, usr, false);
						
						
						inscripcionGuardia.setBajas(null, null, miForm.getFechaBaja(),miForm.getObservacionesValBaja());
						inscripcionGuardia.modificarFechaBaja(usr);
					}
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


		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
	}
	

	
	
	
	
}