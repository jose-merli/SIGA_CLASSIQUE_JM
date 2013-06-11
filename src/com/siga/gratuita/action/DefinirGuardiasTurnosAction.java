package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.ScsTiposguardias;
import org.redabogacia.sigaservices.app.autogen.model.ScsTiposguardiasExample;
import org.redabogacia.sigaservices.app.services.scs.ScsTiposGuardiasService;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsHitoFacturableGuardiaAdm;
import com.siga.beans.ScsHitoFacturableGuardiaBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirGuardiasTurnosForm;

import es.satec.businessManager.BusinessManager;


/**
 * En el caso de que no haya ningun turno almacenado como variable de sesion, es porque 
 * estan pidiendo todos las guardias en las que está inscrita una persona
 * 
 * @author ruben.fernandez
 * @version 08/03/2006 david.sanchezp: nuevos campos para el calendario de guardias.
 */

public class DefinirGuardiasTurnosAction extends MasterAction {
	/**
	 * Desde las pestanhas no es necesario pasar un formulario.
	 * Este execute lanza el abrir de este action en caso de que no se reciba ningun formulario.
	 * En caso contrario, lanza el execute normal del MasterAction.
	 */
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		try {			
			MasterForm miForm = null;
			miForm = (MasterForm) formulario;
			String parametro = mapping.getParameter();
			if(parametro!=null && parametro.equals("abrirAvanzada")){
				return mapping.findForward(this.abrirAvanzada(mapping,miForm,request,response));
			}else{
				if (miForm != null) {
					
					if(miForm.getModo()!=null && miForm.getModo().equalsIgnoreCase("sustituir")) {
						return mapping.findForward(sustituir(mapping, miForm, request, response));
					} else if(miForm.getModo()!=null && miForm.getModo().equalsIgnoreCase("insertarSustitucion")) {
						return mapping.findForward(insertarSustitucion(mapping, miForm, request, response));
					} if ( miForm.getModo()!=null && miForm.getModo().equalsIgnoreCase("getAjaxGuardias")){
						getAjaxGuardias (mapping, miForm, request, response);
						return null;
					}else
						return super.executeInternal(mapping, formulario,request,response);
				} else {
					return mapping.findForward(this.abrirAvanzada(mapping,miForm,request,response));
				}
			}
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
	}

		
	private String insertarSustitucion(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws SIGAException{
		DefinirGuardiasTurnosForm guardiasForm = (DefinirGuardiasTurnosForm)miForm;
		
		//--------------------------------------------------------------------------------------------
		// Obtencion de datos del form. Saliente indica al letrado que va a ser sustituido y entrante el que va
		// a sustituir al saliente
		//-----------------------------------------------------------------------------------------------
		
		String idInstitucion = guardiasForm.getIdInstitucion();
		
		String idTurno = guardiasForm.getIdTurno();
		String idGuardia = guardiasForm.getIdGuardia();
		String idCalendarioGuardias = "";
		String idPersonaSaliente = guardiasForm.getIdPersonaSolicitante();
		String fechaInicio = "";
		String fechaFin = "";
		String idPersonaEntrante = guardiasForm.getIdPersona();
		String salto = request.getParameter("checkSalto");
		String compensacion = request.getParameter("checkCompensacion");
		String sustituta = guardiasForm.getSustituta();
		
		
		UserTransaction tx = null;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		try {
			
			tx=usr.getTransaction();
			UsrBean user = this.getUserBean(request);
			ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(user);
			ScsGuardiasColegiadoAdm guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(user);
			
			
			//-------------------------------------------------------------------------------------------------------
			// Obtenemos todas las guardias del letrado que solicita la sustitucion(saliente) para las que la fecha 
			// de inicio sea mayor que la actual. La informacion se saca de la tabla SCS_CABECERAGUARDIAS
			//
			//-------------------------------------------------------------------------------------------------------
			
			String select = "SELECT * FROM " + ScsCabeceraGuardiasBean.T_NOMBRETABLA 
						  + " WHERE " + ScsCabeceraGuardiasBean.C_IDINSTITUCION +"=" + usr.getLocation() 
						  + "   AND " + ScsCabeceraGuardiasBean.C_IDTURNO +"=" + idTurno
						  + "   AND " + ScsCabeceraGuardiasBean.C_IDGUARDIA +"=" + idGuardia
						  + "   AND " + ScsCabeceraGuardiasBean.C_IDPERSONA + "=" + idPersonaSaliente
						  + "   AND " + ScsCabeceraGuardiasBean.C_FECHA_INICIO + " > SYSDATE";
			
			Vector result = admCabeceraGuardias.selectGenerico(select);
			
			
			
			if(result != null && result.size() > 0)
			  {
				tx.begin();
				
				for(int j=0;j<result.size();j++)
				{
					//-----------------------------------------------------------------------------------------------
					// Para cada registro de cabecera de guardias vamos a realizar el proceso de sustitucion de una 
					//guardia puntual.
					//-----------------------------------------------------------------------------------------------
					Hashtable cabecera = (Hashtable)result.elementAt(j);
					idTurno = ((String)cabecera.get(ScsCabeceraGuardiasBean.C_IDTURNO)).toString();
					idGuardia = ((String)cabecera.get(ScsCabeceraGuardiasBean.C_IDGUARDIA)).toString();
					idCalendarioGuardias = ((String)cabecera.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS)).toString();
					fechaInicio = (String)cabecera.get(ScsCabeceraGuardiasBean.C_FECHA_INICIO);
					fechaFin = (String)cabecera.get(ScsCabeceraGuardiasBean.C_FECHA_FIN);
					
					fechaInicio = GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio);
					fechaFin = GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin);
					
					
					
					String mensaje = guardiasColegiadoAdm.validacionesSustitucionGuardia(usr, idInstitucion, idTurno, idGuardia, idCalendarioGuardias, fechaInicio,fechaFin,idPersonaEntrante,idPersonaSaliente);
					if(!mensaje.equalsIgnoreCase("OK"))
					{
						return exito(mensaje,request);
					}
					else
					{     
						CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usr);
						//comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
						Map<String,CenBajasTemporalesBean> mBajasTemporalesConfirmador =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(idPersonaEntrante), new Integer(idInstitucion));
						if(mBajasTemporalesConfirmador.containsKey(fechaInicio))
							throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");

						String comenSustitucion = guardiasForm.getComenSustitucion();
						guardiasColegiadoAdm.sustitucionLetradoGuardiaPuntual(usr, request,idInstitucion, idTurno,idGuardia,idCalendarioGuardias,idPersonaSaliente,fechaInicio,fechaFin,idPersonaEntrante,salto,compensacion,sustituta,comenSustitucion);
						
					}
						
				}
				
				tx.commit();
			}
			
			
			
		}catch (SIGAException e) {
			request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));	
			return "errorConAviso"; 
		}
		catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
// RGG 22/09/2006 Cambio para mostrar mensaje de sustitución
//		return exitoModal("messages.updated.success",request);
		return exitoModal("messages.sjcs.sustitucionGuardiasRealizada",request);
	}

	protected String abrir(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Controles generales
		UsrBean usr = this.getUserBean(request);
		ScsTurnoAdm turnoAdm = new ScsTurnoAdm(usr);
		ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(usr);
		ScsOrdenacionColasAdm ordenacionAdm = new ScsOrdenacionColasAdm(usr);
		ScsHitoFacturableGuardiaAdm hitoFacturableAdm = new ScsHitoFacturableGuardiaAdm(usr);
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm) formulario;

		// Variables generales
		String idInstitucion, idTurno, idGuardia;
		String forward = null;

		try {
			
			String pestanasG = (String) request.getSession().getAttribute("pestanasG");
	
			if (pestanasG == null) {
				request.removeAttribute("DATABACKUPPESTANA");
				forward = super.abrir(mapping, formulario, request, response);
			}
			else {
				// obteniendo el modo de la pestanha
				String idTurnoPestanha = request.getParameter(ScsGuardiasTurnoBean.C_IDTURNO);
				miForm.setIdInstitucionPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDINSTITUCION));
				miForm.setIdTurnoPestanha(idTurnoPestanha);
				miForm.setIdGuardiaPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDGUARDIA));

				// pasando el modo de la pestanha
				request.setAttribute("MODOPESTANA", request.getParameter("MODOPESTANA"));

				// obteniendo los campos de identificacion de la guardia
				idInstitucion = request.getParameter(ScsGuardiasTurnoBean.C_IDINSTITUCION);
				idTurno = request.getParameter(ScsGuardiasTurnoBean.C_IDTURNO);
				idGuardia = request.getParameter(ScsGuardiasTurnoBean.C_IDGUARDIA);
				List guardiasVinculadasList = guardiaAdm.getGuardiasTurnosVinculadas(new Integer(idTurno),new Integer(idGuardia),new Integer(idInstitucion));
				miForm.setGuardiasVinculadas(guardiasVinculadasList);
				
				// mostrando datos del Turno
				Hashtable miTurno = turnoAdm.getDatosTurno(idInstitucion, idTurnoPestanha);
				request.getSession().setAttribute("turnoElegido", miTurno);

				// mostrando datos generales de la Guardia
				Hashtable hashGuardia = new Hashtable();
				hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, idInstitucion);
				hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, idTurno);
				hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardia);
				ScsGuardiasTurnoBean beanGuardiasTurno = (ScsGuardiasTurnoBean) guardiaAdm.select(hashGuardia).get(0);
				request.getSession().setAttribute("DATABACKUPPESTANA", beanGuardiasTurno);

				// mostrando Control de Periodo de la guardia
				if (beanGuardiasTurno == null)
					miForm.setCheckDiasPeriodo(ClsConstants.DB_FALSE);
				else if (beanGuardiasTurno.getDiasPeriodo() == null)
					miForm.setCheckDiasPeriodo(ClsConstants.DB_FALSE);
				else if (beanGuardiasTurno.getDiasPeriodo().equals("0"))
					miForm.setCheckDiasPeriodo(ClsConstants.DB_FALSE);
				else
					miForm.setCheckDiasPeriodo(ClsConstants.DB_TRUE);

				// mostrando la configuracion de los dias
				this.procesarSeleccionLaborables(beanGuardiasTurno.getSeleccionLaborables(), miForm);
				this.procesarSeleccionFestivos(beanGuardiasTurno.getSeleccionFestivos(), miForm);

				// mostrando ordenacion
				String condicion =
					" where " + ScsOrdenacionColasBean.C_IDORDENACIONCOLAS + "=" + beanGuardiasTurno.getIdOrdenacionColas() + " ";
				ScsOrdenacionColasBean orden = (ScsOrdenacionColasBean) ordenacionAdm.select(condicion).get(0);
				miForm.setAlfabeticoApellidos(orden.getAlfabeticoApellidos().toString());
				miForm.setAntiguedad(orden.getNumeroColegiado().toString());
				miForm.setAntiguedadEnCola(orden.getAntiguedadCola().toString());
				miForm.setEdad(orden.getFechaNacimiento().toString());

				// mostrando dias a separar
				List lDiasASeparar = hitoFacturableAdm.getDiasASeparar(new Integer(idInstitucion),
						new Integer(idTurno), new Integer(idGuardia));
				if (lDiasASeparar != null && lDiasASeparar.size() > 0) {
					StringBuffer sbDiasASeparar = new StringBuffer("[ ");
					for (int i = 0; i < lDiasASeparar.size(); i++) {
						Hashtable htRegistro = (Hashtable) lDiasASeparar.get(i);
						String diasSemana = (String) htRegistro.get(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES);
						sbDiasASeparar.append(diasSemana);
					}
					sbDiasASeparar.append(" ]");
					
					miForm.setHayDiasASeparar(ClsConstants.DB_TRUE);
					miForm.setDiasASeparar(sbDiasASeparar.toString());
				} else {
					miForm.setHayDiasASeparar(ClsConstants.DB_FALSE);
					miForm.setDiasASeparar("");
				}
				List<ScsTurnoBean> alTurnos = null;
				ScsTurnoAdm admTurnos = new ScsTurnoAdm(usr);
				alTurnos = admTurnos.getTurnos(usr.getLocation());
				if(alTurnos==null){
					alTurnos = new ArrayList<ScsTurnoBean>();
				}
				miForm.setTurnosPrincipales(alTurnos);

				Integer idTurnoPrincipal = beanGuardiasTurno.getIdTurnoPrincipal();
				List<ScsGuardiasTurnoBean> guardiasPrincipalesList = null;
				if(idTurnoPrincipal!=null){
					
					
					ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usr);
					guardiasPrincipalesList = admGuardias.getGuardiasTurnos(new Integer(idTurnoPrincipal),new Integer(usr.getLocation()),true);

					if(guardiasPrincipalesList==null){
						guardiasPrincipalesList = new ArrayList<ScsGuardiasTurnoBean>();
					}
				}
				else{
					guardiasPrincipalesList = new ArrayList<ScsGuardiasTurnoBean>();
				}
				if(beanGuardiasTurno.getIdGuardiaPrincipal()!=null)
					miForm.setIdGuardiaPrincipal(beanGuardiasTurno.getIdGuardiaPrincipal().toString());
				if(beanGuardiasTurno.getIdTurnoPrincipal()!=null)
					miForm.setIdTurnoPrincipal(beanGuardiasTurno.getIdTurnoPrincipal().toString());
				
				miForm.setGuardiasPrincipales(guardiasPrincipalesList);			
				
				
				forward = "edicion";
			}

			// Control de excepciones
		} catch (Exception e) {
			throwExcp("error.messages.editar", e, null);
		}

		return forward;
	} // abrir()//abrir()
	
	/**
	 * Esta clase se encarga de hacer una consulta para sacar todas las guardias
	 * para un turno seleccionado, o todas las guardias en las que está o ha estado
	 * inscrito el letrado logado. 
	 * El resutado lo manda como variable del request a la jsp que se encargará de mostrar una tabla.
	 * No se espera un formulario porque los dato de consulta se obtienen del UsrBean y de una variable de sesion
	 * que correspondería con el turno seleccionado.
	 */
	protected String abrirAvanzada (ActionMapping mapping,
									MasterForm formulario,
									HttpServletRequest request,
									HttpServletResponse response)
			throws SIGAException
	{
		String consulta="";
		ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		
		try
		{
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			request.removeAttribute("DATABACKUPPESTANA");
			
			Hashtable turno =(Hashtable) request.getSession().getAttribute("turnoElegido");
			String idTurno = (String)turno.get("IDTURNO");
			String idInstitucion = usr.getLocation();
			
			String entrada = (String)request.getSession().getAttribute("entrada");  //esta variable solo es temporal , en el momento de tener acceso desde el menu, se debe consultar por Session.EsLetrado()
			
			if (entrada.equalsIgnoreCase("1")) {
				consulta =
					" SELECT "+guardias.getCamposTabla(1)+ " , " +
							"Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES" +
							"(SCS_TURNO.IDINSTITUCION,SCS_TURNO.IDSUBZONA, SCS_TURNO.IDZONA) PARTIDOS " +
					" ,SCS_TURNO.GUARDIAS GUARDIAS FROM SCS_GUARDIASTURNO, SCS_TURNO" +
					" WHERE"+
					" SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION AND " +
					" SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO AND " +
					" SCS_TURNO.IDINSTITUCION = "+idInstitucion+//la del turno del que procedemos
					" AND SCS_TURNO.IDTURNO = "+idTurno+//la del turno del que procedemos
					" ORDER BY SCS_GUARDIASTURNO.NOMBRE ASC";
				request.getSession().setAttribute("IDTURNOSESION",(String)turno.get("IDTURNO"));			
				Vector resultado = (Vector)guardias.ejecutaSelect(consulta);
				request.getSession().setAttribute("resultado",resultado);
			}
			else {//Es letrado
				
				String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
				String fechaConsultaTurno =  (String)request.getSession().getAttribute("fechaConsultaInscripcionTurno");
				miForm.setFechaConsulta(fechaConsultaTurno);
				consulta =
					" SELECT "+guardias.getCamposTabla(3)+ " , " +
							"Pkg_Siga_Sjcs.FUN_SJ_PARTIDOSJUDICIALES" +
							"(SCS_TURNO.IDINSTITUCION,SCS_TURNO.IDSUBZONA, SCS_TURNO.IDZONA) PARTIDOS "+ 
				  	" ,SCS_TURNO.GUARDIAS GUARDIAS FROM SCS_GUARDIASTURNO, SCS_TURNO "+
				 	" WHERE "+
				 	" SCS_GUARDIASTURNO.IDINSTITUCION = SCS_TURNO.IDINSTITUCION AND " +
					" SCS_GUARDIASTURNO.IDTURNO = SCS_TURNO.IDTURNO AND " +
					" SCS_TURNO.IDINSTITUCION = "+idInstitucion+//la del turno del que procedemos
					" AND SCS_TURNO.IDTURNO = "+idTurno+//la del turno del que procedemos
					" ORDER BY SCS_GUARDIASTURNO.NOMBRE ASC";
				ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
				String fechaSolicitudTurno = (String)request.getSession().getAttribute("FECHASOLICITUDTURNOSESION");
				ScsInscripcionTurnoBean inscripcionTurnoSeleccionada = admInsTurno.getInscripcionTurno(new Integer(idInstitucion), new Integer( idTurno), new Long(idPersona), fechaSolicitudTurno, false);				
				//miramos si tiene fecha de baja para que puedan solictar altas nuevas de inscripciones de guardia
				//Boolean isFechaBajaInscTurnoActiva = insTurnoBeanActiva!=null && insTurnoBeanActiva.getFechaBaja()!=null && !insTurnoBeanActiva.getFechaBaja().equals("");
				
				request.setAttribute("inscripcionTurnoSeleccionada", inscripcionTurnoSeleccionada);
				request.getSession().setAttribute("IDTURNOSESION",(String)turno.get("IDTURNO"));			
				Vector resultado = (Vector)guardias.ejecutaSelect(consulta);
				
				ScsInscripcionGuardiaAdm admInsGua = new ScsInscripcionGuardiaAdm(usr);
				ScsInscripcionTurnoBean inscripcionHoy = null;
				if(inscripcionTurnoSeleccionada.getFechaBaja()!=null&&!inscripcionTurnoSeleccionada.getFechaBaja().equals("")){
					inscripcionHoy = admInsTurno.getInscripcionTurno(new Integer(idInstitucion),new Integer( idTurno), new Long(idPersona), "sysdate");
				}else{
					inscripcionHoy = inscripcionTurnoSeleccionada;
					
				}
				
				Boolean isPermitidoInscripcionGuardia = false;				
				if (inscripcionHoy != null) {
					isPermitidoInscripcionGuardia = inscripcionHoy.equals(inscripcionTurnoSeleccionada) || 
						inscripcionTurnoSeleccionada.getFechaBaja() == null || inscripcionTurnoSeleccionada.getFechaBaja().equals("") || 
						inscripcionTurnoSeleccionada.getFechaBaja().compareTo(inscripcionHoy.getFechaValidacion()) > 0;
				}
				
				request.setAttribute("isPermitidoInscripcionGuardia", isPermitidoInscripcionGuardia);
				if(resultado!=null && resultado.size()>0){
					Iterator iteResultado = resultado.iterator();
					while (iteResultado.hasNext()) {
						Hashtable htGuardia = (Hashtable) iteResultado.next();
						String idGuardia = (String)htGuardia.get("IDGUARDIA");
											
						Hashtable inscripcionGuardia = admInsGua.getInscripcionGuardiaInscripcionTurno(idInstitucion, idTurno, idPersona, new Integer(idGuardia), inscripcionTurnoSeleccionada);
						
						if (inscripcionGuardia.size() > 0) {
							htGuardia.put("INSCRIPCIONGUARDIA",inscripcionGuardia);
						}
					}					
				}
				
				request.getSession().setAttribute("resultado",resultado);
			}
			
		} catch(Exception e){
			throwExcp("error.messages.editar",e,null);
		}
		
		return "listado";
	} //abrirAvanzada ()
	
	/**
	 * Antes de editar se hace una consulta a bbddm para recuperar todos los campos de la
	 * guardia seleccionada.
	 */
	protected String editar (ActionMapping mapping, 
							 MasterForm formulario, 
							 HttpServletRequest request, 
							 HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
		ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		Hashtable hash = new Hashtable();
		
		try
		{
			//Obteniendo datos ocultos que pueda haber en el formulario
			Vector ocultos = (Vector) miForm.getDatosTablaOcultos(0);
			
			//Obteniendo los datos de la guardia que se esta editando
			Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
			hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
			hash.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)turno.get("IDTURNO"));
			String idGuardia = null;
			if (ocultos!=null)idGuardia = (String)ocultos.get(1);
			else idGuardia = (String)miForm.getGuardia();
			hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA,idGuardia);
			//Aqui seleccionamos la guardia que queremos editar
			ScsGuardiasTurnoBean beanGuardiasTurno = (ScsGuardiasTurnoBean)((Vector)guardias.select(hash)).get(0);
			request.getSession().setAttribute("DATABACKUPPESTANA",beanGuardiasTurno);
			request.getSession().setAttribute("modo","editar");
			
//			List guardiasVinculadasList = guardias.getGuardiasTurnosVinculadas(new Integer((String)turno.get("IDTURNO")),new Integer(idGuardia),new Integer(usr.getLocation()));
//			miForm.setGuardiasVinculadas(guardiasVinculadasList);
			//Para pasar los parametros de la pestanha: idinstitucion. idturno, idguardia:
			Hashtable hashPestanha = new Hashtable();
			hashPestanha.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
			hashPestanha.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)turno.get("IDTURNO"));
			if (ocultos!=null)
				hashPestanha.put(ScsGuardiasTurnoBean.C_IDGUARDIA,(String)ocultos.get(1));
			else 
				hashPestanha.put(ScsGuardiasTurnoBean.C_IDGUARDIA,(String)miForm.getGuardia());
			hashPestanha.put("MODOPESTANA","EDITAR");
						
			hashPestanha.put(ScsGuardiasTurnoBean.C_TIPOGUARDIA, miForm.getIdTipoGuardiaSeleccionado()==null?"":miForm.getIdTipoGuardiaSeleccionado());
			
			request.setAttribute("HASHGUARDIA",hashPestanha);
			
//			List<ScsTurnoBean> alTurnos = null;
//			ScsTurnoAdm admTurnos = new ScsTurnoAdm(usr);
//			alTurnos = admTurnos.getTurnos(usr.getLocation());
//			if(alTurnos==null){
//				alTurnos = new ArrayList<ScsTurnoBean>();
//			}
//			miForm.setTurnosPrincipales(alTurnos);
//
//			Integer idTurnoPrincipal = beanGuardiasTurno.getIdTurnoPrincipal();
//			List<ScsGuardiasTurnoBean> guardiasPrincipalesList = null;
//			if(idTurnoPrincipal!=null){
//				
//				
//				ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usr);
//				guardiasPrincipalesList = admGuardias.getGuardiasTurnos(new Integer(idTurnoPrincipal),new Integer(usr.getLocation()),true);
//
//				if(guardiasPrincipalesList==null){
//					guardiasPrincipalesList = new ArrayList<ScsGuardiasTurnoBean>();
//				}
//			}
//			else{
//				guardiasPrincipalesList = new ArrayList<ScsGuardiasTurnoBean>();
//			}
//			if(beanGuardiasTurno.getIdGuardiaPrincipal()!=null)
//				miForm.setIdGuardiaPrincipal(beanGuardiasTurno.getIdGuardiaPrincipal().toString());
//			if(beanGuardiasTurno.getIdTurnoPrincipal()!=null)
//				miForm.setIdTurnoPrincipal(beanGuardiasTurno.getIdTurnoPrincipal().toString());
//			
//			miForm.setGuardiasPrincipales(guardiasPrincipalesList);			
						
		//Control de excepciones
		} catch(Exception e){
			throwExcp("error.messages.editar",e,null);
		}
		
		return "editar";
	} //editar ()
	
	/**
	 * Antes de ver se hace una consulta a bbddm para recuperar todos los campos de la
	 * guardia seleccionada.
	 */
	protected String ver (ActionMapping mapping,
						  MasterForm formulario,
						  HttpServletRequest request,
						  HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String entrada = (String)request.getSession().getAttribute("entrada");
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
		ScsGuardiasTurnoAdm guardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		Hashtable hash = new Hashtable();
		
		String forward="exception";
      
		try
		{
			//Obteniendo datos ocultos que pueda haber en el formulario
			Vector ocultos = (Vector) miForm.getDatosTablaOcultos(0);
			
			//Obteniendo los datos de la guardia que se esta viendo
			hash.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
			hash.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)ocultos.get(0));
			hash.put(ScsGuardiasTurnoBean.C_IDGUARDIA,(String)ocultos.get(1));
			
			//Aqui seleccionamos la guardia que queremos ver
			ScsGuardiasTurnoBean beanGuardiasTurno = (ScsGuardiasTurnoBean)((Vector)guardias.select(hash)).get(0);
			request.getSession().setAttribute("DATABACKUPPESTANA",beanGuardiasTurno);
			request.getSession().setAttribute("modo","ver");
			if (entrada.equalsIgnoreCase("1"))forward="ver";
			else forward="verLetrado";
			
			//Para pasar los parametros de la pestanha: idinstitucion. idturno, idguardia:
			Hashtable hashPestanha = new Hashtable();
			hashPestanha.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,(String)usr.getLocation());
			hashPestanha.put(ScsGuardiasTurnoBean.C_IDTURNO,(String)ocultos.get(0));
			hashPestanha.put(ScsGuardiasTurnoBean.C_IDGUARDIA,(String)ocultos.get(1));
			hashPestanha.put("MODOPESTANA","VER");
			request.setAttribute("HASHGUARDIA",hashPestanha);
//			List guardiasVinculadasList = guardias.getGuardiasTurnosVinculadas(new Integer((String)ocultos.get(0)),new Integer((String)ocultos.get(1)),new Integer(usr.getLocation()));
//			miForm.setGuardiasVinculadas(guardiasVinculadasList);
//			
//			List<ScsTurnoBean> alTurnos = null;
//			ScsTurnoAdm admTurnos = new ScsTurnoAdm(usr);
//			alTurnos = admTurnos.getTurnos(usr.getLocation());
//			if(alTurnos==null){
//				alTurnos = new ArrayList<ScsTurnoBean>();
//			}
//			miForm.setTurnosPrincipales(alTurnos);
//
//			Integer idTurnoPrincipal = beanGuardiasTurno.getIdTurnoPrincipal();
//			List<ScsGuardiasTurnoBean> guardiasPrincipalesList = null;
//			if(idTurnoPrincipal!=null){
//				
//				
//				ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usr);
//				guardiasPrincipalesList = admGuardias.getGuardiasTurnos(new Integer(idTurnoPrincipal),new Integer(usr.getLocation()),true);
//
//				if(guardiasPrincipalesList==null){
//					guardiasPrincipalesList = new ArrayList<ScsGuardiasTurnoBean>();
//				}
//			}
//			else{
//				guardiasPrincipalesList = new ArrayList<ScsGuardiasTurnoBean>();
//			}
//			if(beanGuardiasTurno.getIdGuardiaPrincipal()!=null)
//				miForm.setIdGuardiaPrincipal(beanGuardiasTurno.getIdGuardiaPrincipal().toString());
//			if(beanGuardiasTurno.getIdTurnoPrincipal()!=null)
//				miForm.setIdTurnoPrincipal(beanGuardiasTurno.getIdTurnoPrincipal().toString());
//			
//			miForm.setGuardiasPrincipales(guardiasPrincipalesList);			

			
			
			
		//Control de excepciones
		} catch(Exception e){
			throwExcp("error.messages.editar",e,null);
		}
		return forward;
	} //ver ()
	
	/**
	 * Este metodo se llama antes de mostrar la ventana de una nueva guardia
	 * y se encarga de rellenar el formulario de nueva guardia
	 * con los valores por defecto, antes de entregarlo
	 */
	protected String nuevo (ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		try
		{				
			DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm) formulario;
			
			//Control de los checkBox			
			miForm.setCheckDiasPeriodo(ClsConstants.DB_FALSE);
			
			//Escribiendo en el formulario la configuracion de los dias
			this.procesarSeleccionLaborables("LMXJVS",miForm);
			this.procesarSeleccionFestivos("LMXJVSD",miForm);
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			List<ScsTurnoBean> alTurnos = null;
			ScsTurnoAdm admTurnos = new ScsTurnoAdm(usr);
			alTurnos = admTurnos.getTurnos(usr.getLocation());
			if(alTurnos==null){
				alTurnos = new ArrayList<ScsTurnoBean>();
			}
			miForm.setTurnosPrincipales(alTurnos);
			
			// JPT: Obtiene los tipos de guardias
			List<ScsTiposguardias> listaTiposGuardias = this.obtenerListaTiposGuardias();
			miForm.setTiposGuardias(listaTiposGuardias);			
			
			miForm.setGuardiasPrincipales(new ArrayList<ScsGuardiasTurnoBean>());
		
		//Control de excepciones
		} catch (Exception e) {
			throwExcp("error.messages.editar",e,null);
		}
		return "nuevo";
	} //nuevo ()
	
	/**
	 * Inserta desde una modal una nueva Guardia para un Turno
	 */
	protected String insertar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		//Controles generales
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
		
		
		ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm (this.getUserBean(request));		
		UserTransaction tx = null;
		UsrBean usrbean =  (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		try
		{	
			if (UtilidadesString.stringToBoolean(miForm.getCheckGuardiaDeSustitucion())) {
				
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, ScsGuardiasTurnoBean.C_IDINSTITUCION, usr.getLocation());
				UtilidadesHash.set(h, ScsGuardiasTurnoBean.C_IDTURNO, UtilidadesHash.getString(turno, "IDTURNO"));
				UtilidadesHash.set(h, ScsGuardiasTurnoBean.C_IDGUARDIA, miForm.getGuardiaDeSustitucion());
				Vector v = guardiaAdm.selectByPK(h);
				if (v == null || v.size() != 1) {
					throw new SIGAException("No se han podido obtener los datos asociados a la guardia origen");
				}
				ScsGuardiasTurnoBean b = (ScsGuardiasTurnoBean)v.get(0);
//				b.getOriginalHash();
				b.setDescripcion(miForm.getDescripcion());
				b.setNombre(miForm.getGuardia());
//				b.setIdGuardia();
				b.setIdGuardiaSustitucion(new Integer(miForm.getGuardiaDeSustitucion()));
				b.setIdTurnoSustitucion(b.getIdTurno());
				
				// JPT: Combo seleccionable de tipo de guardia
				if (miForm.getIdTipoGuardiaSeleccionado()!=null && !miForm.getIdTipoGuardiaSeleccionado().equals("")) 
					b.setIdTipoGuardiaSeleccionado(new Integer(miForm.getIdTipoGuardiaSeleccionado()));
				
				guardiaAdm.insert(b);
			 	return exitoModal("messages.inserted.success",request);
			}
			
			String  idTurnoPrincipal = miForm.getIdTurnoPrincipal();
			String  idGuardiaPrincipal = miForm.getIdGuardiaPrincipal();
			if(idTurnoPrincipal!=null && !idTurnoPrincipal.equals("-1") && !idTurnoPrincipal.equals("")){
				Hashtable principalHashtable = new Hashtable();
				UtilidadesHash.set(principalHashtable, ScsGuardiasTurnoBean.C_IDINSTITUCION, usr.getLocation());
				UtilidadesHash.set(principalHashtable, ScsGuardiasTurnoBean.C_IDTURNO, idTurnoPrincipal);
				UtilidadesHash.set(principalHashtable, ScsGuardiasTurnoBean.C_IDGUARDIA, idGuardiaPrincipal);
				Vector principalVector = guardiaAdm.selectByPK(principalHashtable);
				if (principalVector == null || principalVector.size() != 1) {
					throw new SIGAException("No se han podido obtener los datos asociados a la guardia principal");
				}
				ScsGuardiasTurnoBean beanVinculado = (ScsGuardiasTurnoBean)principalVector.get(0);
				
				//mhg - Se le asigna el valor null a la persona_ultima para que se pueda insertar y una vez lanzado el trigger del insert pasamos a ponerle su valor.
				Long idPersonaUltima = beanVinculado.getIdPersona_Ultimo();
				beanVinculado.setIdPersona_Ultimo(null);
				
				beanVinculado.setDescripcion(miForm.getDescripcion());
				beanVinculado.setNombre(miForm.getGuardia());
				beanVinculado.setDescripcionFacturacion(miForm.getDescripcionFacturacion());
				beanVinculado.setDescripcionPago(miForm.getDescripcionPago());
				beanVinculado.setIdInstitucionPrincipal(new Integer(usr.getLocation()));
				beanVinculado.setIdTurnoPrincipal(new Integer(idTurnoPrincipal));
				beanVinculado.setIdGuardiaPrincipal(new Integer(idGuardiaPrincipal));
				beanVinculado.setIdInstitucion(new Integer(usr.getLocation()));
				beanVinculado.setIdTurno(new Integer(UtilidadesHash.getString(turno, "IDTURNO")));
				
				// JPT: Al vincular no se pasa el tipo de guardia
				//if (miForm.getIdTipoGuardiaSeleccionado()!=null && !miForm.getIdTipoGuardiaSeleccionado().equals("")) 
					//beanVinculado.setIdTipoGuardiaSeleccionado(new Integer(miForm.getIdTipoGuardiaSeleccionado()));
				
				ScsOrdenacionColasAdm ordenacion = 	new ScsOrdenacionColasAdm(this.getUserBean(request));
				Vector ordenacionVector = new Vector();
				String where =" where "+
				ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+miForm.getAlfabeticoApellidos()+" and "+
				ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+miForm.getAntiguedadEnCola()+" and "+
				ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+miForm.getEdad()+" and "+
				ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+miForm.getAntiguedad()+" ";
				ordenacionVector=ordenacion.select(where);
				Hashtable ordenacionHashtable = null;
				if (ordenacionVector.size()>0) {
					ScsOrdenacionColasBean ordenacioBean = (ScsOrdenacionColasBean)ordenacionVector.get(0);
					beanVinculado.setIdOrdenacionColas(ordenacioBean.getIdOrdenacionColas());
				} else {
					Hashtable beanVinculadoHashtable = guardiaAdm.beanToHashTable(beanVinculado); 
					ordenacion.prepararInsert(beanVinculadoHashtable);
					ordenacionHashtable = new Hashtable();
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,miForm.getAlfabeticoApellidos());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,miForm.getAntiguedadEnCola());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,miForm.getEdad());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,miForm.getAntiguedad());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_USUMODIFICACION,(String)usr.getUserName());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHAMODIFICACION,"sysdate");
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,beanVinculadoHashtable.get("IDORDENACIONCOLAS"));
				}
				tx = usrbean.getTransaction();
				tx.begin();
				if(ordenacionHashtable!=null)
					ordenacion.insert(ordenacionHashtable);
				
				//mhg - Actualizamos los datos del ultimo letrado un paso posterior a la inserción ya que esta lanza un trigger.
				Hashtable hashVinculado = guardiaAdm.prepararInsert(guardiaAdm.beanToHashTable(beanVinculado));
				beanVinculado = (ScsGuardiasTurnoBean) guardiaAdm.hashTableToBean(hashVinculado);				
				guardiaAdm.insert(beanVinculado);
				
				if(idPersonaUltima != null){
					beanVinculado.setIdPersona_Ultimo(idPersonaUltima);
					guardiaAdm.update(beanVinculado);
				}
				
				tx.commit();
				
			 	return exitoModal("messages.inserted.success",request);
			}
			
			
			//Controles generales
			
			Hashtable nuevo = (Hashtable)miForm.getDatos();
			
			//Se rellenan los campos que hacen falta para crear
			//una nueva guardia (que no se recuperan desde el formulario)
			nuevo.put("IDINSTITUCION",usr.getLocation());  
			nuevo.put("IDTURNO",(String)turno.get("IDTURNO"));
			
			ScsOrdenacionColasAdm ordenacion = 	new ScsOrdenacionColasAdm(this.getUserBean(request));
			Vector ordenacionVector = new Vector();
			String where =" where "+
			ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+miForm.getAlfabeticoApellidos()+" and "+
			ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+miForm.getAntiguedadEnCola()+" and "+
			ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+miForm.getEdad()+" and "+
			ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+miForm.getAntiguedad()+" ";
			ordenacionVector=ordenacion.select(where);
			Hashtable ordenacionHashtable = null;
			if (ordenacionVector.size()>0) {
				ScsOrdenacionColasBean ordenacioBean = (ScsOrdenacionColasBean)ordenacionVector.get(0);
				nuevo.put("IDORDENACIONCOLAS",((Integer)ordenacioBean.getIdOrdenacionColas()).toString());
			} else {
				ordenacion.prepararInsert(nuevo);
				ordenacionHashtable = new Hashtable();
				ordenacionHashtable.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,miForm.getAlfabeticoApellidos());
				ordenacionHashtable.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,miForm.getAntiguedadEnCola());
				ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,miForm.getEdad());
				ordenacionHashtable.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,miForm.getAntiguedad());
				ordenacionHashtable.put(ScsOrdenacionColasBean.C_USUMODIFICACION,(String)usr.getUserName());
				ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHAMODIFICACION,"sysdate");
				ordenacionHashtable.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,nuevo.get("IDORDENACIONCOLAS"));
			}
			
			//Preparando insercion
//			guardiaAdm.prepararInsert(nuevo);
			nuevo.put("NOMBRE",(String)nuevo.get("GUARDIA"));
			
			//Calculando los dias de la semana seleccionados
			String laborables = this.obtenerSeleccionLaborables(miForm);
			nuevo.put(ScsGuardiasTurnoBean.C_SELECCIONLABORABLES, laborables);
			String festivos = this.obtenerSeleccionFestivos(miForm);
			nuevo.put(ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS, festivos);
			//Control de la seleccion
			if (laborables.equals("") && festivos.equals("")) {
				// No ha seleccionado nada
				throw new SIGAException("messages.gratuita.confGuardia.noSelecionTipoDia");
			}
			
			//Resto de datos de la guardia
			nuevo.put(ScsGuardiasTurnoBean.C_TIPODIASGUARDIA, miForm.getTipoDiasGuardia());
			nuevo.put(ScsGuardiasTurnoBean.C_DIASPERIODO, miForm.getDiasPeriodo());
			nuevo.put(ScsGuardiasTurnoBean.C_TIPODIASPERIODO, miForm.getTipoDiasPeriodo());
			
			// TODO // jbd // Temporalemente dejamos esto como "0" y ya se configurara en datos generales
			// Hay que cambiar la jsp para que se puedan meter estos parametros por interfaz
			nuevo.put(ScsGuardiasTurnoBean.C_PORGRUPOS, "0");
			nuevo.put(ScsGuardiasTurnoBean.C_ROTARCOMPONENTES, "0");
			
			// JPT: Combo seleccionable de tipo de guardia
			nuevo.put(ScsGuardiasTurnoBean.C_TIPOGUARDIA, miForm.getIdTipoGuardiaSeleccionado());
			
			//Iniciando la insercion
			tx = usrbean.getTransaction();
			tx.begin();
			if(ordenacionHashtable!=null)
				ordenacion.insert(ordenacionHashtable);
			guardiaAdm.insert(nuevo);
			tx.commit();
			
		//Control de excepciones
	 	} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
	 	}
		
	 	return exitoModal("messages.inserted.success",request);
	} //insertar ()
	
	/**
	 * Modifica los datos de una Guardia para un Turno
	 */
	protected String modificar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		//Controles generales
	    ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request.getSession().getAttribute("DATABACKUPPESTANA");
	    ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm (this.getUserBean(request));	    
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
		Hashtable nuevo = miForm.getDatos();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;

		try {
			//Controles generales
			
		    tx = usr.getTransaction();
		    String  idTurnoPrincipal = miForm.getIdTurnoPrincipal();
			String  idGuardiaPrincipal = miForm.getIdGuardiaPrincipal();
			
			//SI VIENE GUARDIA VINCULADA 
			if(idTurnoPrincipal!=null && !idTurnoPrincipal.equals("-1") && !idTurnoPrincipal.equals("")){
				Hashtable principalHashtable = new Hashtable();
				UtilidadesHash.set(principalHashtable, ScsGuardiasTurnoBean.C_IDINSTITUCION, usr.getLocation());
				UtilidadesHash.set(principalHashtable, ScsGuardiasTurnoBean.C_IDTURNO, miForm.getIdTurnoPestanha());
				UtilidadesHash.set(principalHashtable, ScsGuardiasTurnoBean.C_IDGUARDIA, miForm.getIdGuardiaPestanha());
				Vector principalVector = guardiaAdm.selectByPK(principalHashtable);
				if (principalVector == null || principalVector.size() != 1) {
					throw new SIGAException("No se han podido obtener los datos asociados a la guardia principal");
				}
				
				ScsGuardiasTurnoBean beanVinculado = (ScsGuardiasTurnoBean)principalVector.get(0);
				// CAso de que la guardia fuera vinculada
				if(beanVinculado.getIdTurnoPrincipal()!=null && beanVinculado.getIdGuardiaPrincipal()!=null){
				
					//Caso de que la guardia fuera vinculada y NO cambien el guardia turno principal
					//HAY QUE MODIFICAR SOLO LOS CAMPOS DE DESCRIPCION. NO SE HACE NADA CON LA ORDENACION
					if(beanVinculado.getIdTurnoPrincipal()!=null && beanVinculado.getIdTurnoPrincipal().compareTo(new Integer(miForm.getIdTurnoPrincipal()))==0
							&&beanVinculado.getIdGuardiaPrincipal()!=null && beanVinculado.getIdGuardiaPrincipal().compareTo(new Integer(miForm.getIdGuardiaPrincipal()))==0){
						//No hay cambios solo modificamos las descripciones
						String[] claves ={ScsGuardiasTurnoBean.C_IDINSTITUCION,ScsGuardiasTurnoBean.C_IDTURNO,ScsGuardiasTurnoBean.C_IDGUARDIA}; 
						String[] campos ={ScsGuardiasTurnoBean.C_DESCRIPCION,ScsGuardiasTurnoBean.C_NOMBRE,ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION,ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO};
						beanVinculado.setDescripcion(miForm.getDescripcion());
						beanVinculado.setNombre(miForm.getNombreGuardia());
						beanVinculado.setDescripcionFacturacion(miForm.getDescripcionFacturacion());
						beanVinculado.setDescripcionPago(miForm.getDescripcionPago());
						if (!guardiaAdm.updateDirect(beanVinculado,claves,campos)) 
							throw new ClsExceptions (guardiaAdm.getError());
						
					} else {
						//HAY QUE MODIFICAR TODOS LOS CAMPOS DE LA CONFIGURACION A LOS CAMPOS DEL BEAN.
						//TAMBIEN SE INSERTA LA NUEVA ORDENACION
						Hashtable nuevoPrincipalHashtable = new Hashtable();
						UtilidadesHash.set(nuevoPrincipalHashtable, ScsGuardiasTurnoBean.C_IDINSTITUCION, usr.getLocation());
						UtilidadesHash.set(nuevoPrincipalHashtable, ScsGuardiasTurnoBean.C_IDTURNO, miForm.getIdTurnoPrincipal());
						UtilidadesHash.set(nuevoPrincipalHashtable, ScsGuardiasTurnoBean.C_IDGUARDIA, miForm.getIdGuardiaPrincipal());
						Vector nuevoPrincipalVector = guardiaAdm.selectByPK(nuevoPrincipalHashtable);
						if (nuevoPrincipalVector == null || nuevoPrincipalVector.size() != 1) {
							throw new SIGAException("No se han podido obtener los datos asociados a la guardia principal");
						}
						
						ScsGuardiasTurnoBean nuevoBeanVinculado = (ScsGuardiasTurnoBean)nuevoPrincipalVector.get(0);
						nuevoBeanVinculado.setDescripcion(miForm.getDescripcion());
						nuevoBeanVinculado.setNombre(miForm.getNombreGuardia());
						nuevoBeanVinculado.setDescripcionFacturacion(miForm.getDescripcionFacturacion());
						nuevoBeanVinculado.setDescripcionPago(miForm.getDescripcionPago());
						nuevoBeanVinculado.setIdInstitucionPrincipal(nuevoBeanVinculado.getIdInstitucion());
						nuevoBeanVinculado.setIdTurnoPrincipal(nuevoBeanVinculado.getIdTurno());
						nuevoBeanVinculado.setIdGuardiaPrincipal(nuevoBeanVinculado.getIdGuardia());
						nuevoBeanVinculado.setIdInstitucion(guardia.getIdInstitucion());
						nuevoBeanVinculado.setIdTurno(guardia.getIdTurno());
						nuevoBeanVinculado.setIdGuardia(guardia.getIdGuardia());
						
						//HABRA CAMBIADO LA ORDENACION
						ScsOrdenacionColasAdm ordenacion = 	new ScsOrdenacionColasAdm(this.getUserBean(request));
						Vector ordenacionVector = new Vector();
						String where =" where "+
						ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+miForm.getAlfabeticoApellidos()+" and "+
						ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+miForm.getAntiguedadEnCola()+" and "+
						ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+miForm.getEdad()+" and "+
						ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+miForm.getAntiguedad()+" ";
						ordenacionVector=ordenacion.select(where);
						
						Hashtable ordenacionHashtable = null;
						if (ordenacionVector.size()>0) {
							ScsOrdenacionColasBean ordenacioBean = (ScsOrdenacionColasBean)ordenacionVector.get(0);
							nuevoBeanVinculado.setIdOrdenacionColas(ordenacioBean.getIdOrdenacionColas());
						} else {
							Hashtable beanVinculadoHashtable = guardiaAdm.beanToHashTable(nuevoBeanVinculado); 
							ordenacion.prepararInsert(beanVinculadoHashtable);
							ordenacionHashtable = new Hashtable();
							ordenacionHashtable.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,miForm.getAlfabeticoApellidos());
							ordenacionHashtable.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,miForm.getAntiguedadEnCola());
							ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,miForm.getEdad());
							ordenacionHashtable.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,miForm.getAntiguedad());
							ordenacionHashtable.put(ScsOrdenacionColasBean.C_USUMODIFICACION,(String)usr.getUserName());
							ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHAMODIFICACION,"sysdate");
							ordenacionHashtable.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,beanVinculadoHashtable.get("IDORDENACIONCOLAS"));
						}
						
						tx.begin();
					    if (ordenacionHashtable!=null) ordenacion.insert(ordenacionHashtable);
					    guardiaAdm.update(guardiaAdm.beanToHashTable(nuevoBeanVinculado), guardia.getOriginalHash());
					    tx.commit();
												
						//Caso de que la guardia fuera vinculada y SI cambien el guardia turno principal						
					}
					
				}else{
					//AHORA ES VINCULADA Y ANTES NO ERA VINCULADA
					Hashtable nuevoPrincipalHashtable = new Hashtable();
					UtilidadesHash.set(nuevoPrincipalHashtable, ScsGuardiasTurnoBean.C_IDINSTITUCION, usr.getLocation());
					UtilidadesHash.set(nuevoPrincipalHashtable, ScsGuardiasTurnoBean.C_IDTURNO, miForm.getIdTurnoPrincipal());
					UtilidadesHash.set(nuevoPrincipalHashtable, ScsGuardiasTurnoBean.C_IDGUARDIA, miForm.getIdGuardiaPrincipal());
					Vector nuevoPrincipalVector = guardiaAdm.selectByPK(nuevoPrincipalHashtable);
					if (nuevoPrincipalVector == null || nuevoPrincipalVector.size() != 1) {
						throw new SIGAException("No se han podido obtener los datos asociados a la guardia principal");
					}
					
					ScsGuardiasTurnoBean nuevoBeanVinculado = (ScsGuardiasTurnoBean)nuevoPrincipalVector.get(0);
					
					nuevoBeanVinculado.setDescripcion(miForm.getDescripcion());
					nuevoBeanVinculado.setNombre(miForm.getNombreGuardia());
					nuevoBeanVinculado.setDescripcionFacturacion(miForm.getDescripcionFacturacion());
					nuevoBeanVinculado.setDescripcionPago(miForm.getDescripcionPago());
					nuevoBeanVinculado.setIdInstitucionPrincipal(nuevoBeanVinculado.getIdInstitucion());
					nuevoBeanVinculado.setIdTurnoPrincipal(nuevoBeanVinculado.getIdTurno());
					nuevoBeanVinculado.setIdGuardiaPrincipal(nuevoBeanVinculado.getIdGuardia());
					nuevoBeanVinculado.setIdInstitucion(guardia.getIdInstitucion());
					nuevoBeanVinculado.setIdTurno(guardia.getIdTurno());
					nuevoBeanVinculado.setIdGuardia(guardia.getIdGuardia());
					
					//HABRA CAMBIADO LA ORDENACION
					ScsOrdenacionColasAdm ordenacion = 	new ScsOrdenacionColasAdm(this.getUserBean(request));
					Vector ordenacionVector = new Vector();
					String where =" where "+
					ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+miForm.getAlfabeticoApellidos()+" and "+
					ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+miForm.getAntiguedadEnCola()+" and "+
					ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+miForm.getEdad()+" and "+
					ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+miForm.getAntiguedad()+" ";
					ordenacionVector=ordenacion.select(where);
					
					Hashtable ordenacionHashtable = null;
					if (ordenacionVector.size()>0) {
						ScsOrdenacionColasBean ordenacioBean = (ScsOrdenacionColasBean)ordenacionVector.get(0);
						nuevoBeanVinculado.setIdOrdenacionColas(ordenacioBean.getIdOrdenacionColas());
					} else {
						Hashtable beanVinculadoHashtable = guardiaAdm.beanToHashTable(nuevoBeanVinculado); 
						ordenacion.prepararInsert(beanVinculadoHashtable);
						ordenacionHashtable = new Hashtable();
						ordenacionHashtable.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,miForm.getAlfabeticoApellidos());
						ordenacionHashtable.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,miForm.getAntiguedadEnCola());
						ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,miForm.getEdad());
						ordenacionHashtable.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,miForm.getAntiguedad());
						ordenacionHashtable.put(ScsOrdenacionColasBean.C_USUMODIFICACION,(String)usr.getUserName());
						ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHAMODIFICACION,"sysdate");
						ordenacionHashtable.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,beanVinculadoHashtable.get("IDORDENACIONCOLAS"));
					}
					
					tx.begin();
				    if (ordenacionHashtable!=null) ordenacion.insert(ordenacionHashtable);
				    guardiaAdm.update(guardiaAdm.beanToHashTable(nuevoBeanVinculado), guardia.getOriginalHash());
				    tx.commit();
										
					//Si cambia 
				}
					
			// CASO QUE NO SEA GUARDIA VINCULADA			 
			} else {			    
				nuevo.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, miForm.getIdInstitucionPestanha());
				nuevo.put(ScsGuardiasTurnoBean.C_IDTURNO, miForm.getIdTurnoPestanha());
				nuevo.put(ScsGuardiasTurnoBean.C_IDGUARDIA, miForm.getIdGuardiaPestanha());
				nuevo.put(ScsGuardiasTurnoBean.C_PORGRUPOS, (miForm.getPorGrupos() == null) ? "0" : "1");
				nuevo.put(ScsGuardiasTurnoBean.C_ROTARCOMPONENTES, (miForm.getRotarComponentes() == null) ? "0" : "1");
				nuevo.put(ScsGuardiasTurnoBean.C_USUMODIFICACION, usr.getUserName());
				nuevo.put(ScsGuardiasTurnoBean.C_FECHAMODIFICACION, "sysdate");
				nuevo.put(ScsGuardiasTurnoBean.C_TIPODIASGUARDIA, miForm.getTipoDiasGuardia());
				nuevo.put(ScsGuardiasTurnoBean.C_DIASPERIODO, miForm.getDiasPeriodo());
				nuevo.put(ScsGuardiasTurnoBean.C_TIPODIASPERIODO, miForm.getTipoDiasPeriodo());
				nuevo.put(ScsGuardiasTurnoBean.C_VALIDARJUSTIFICACIONES, (miForm.getValidarInscripciones() == null) ? "N" : "S");
				nuevo.put(ScsGuardiasTurnoBean.C_IDINSTITUCIONPRINCIPAL,"");
				nuevo.put(ScsGuardiasTurnoBean.C_IDTURNOPRINCIPAL, "");
				nuevo.put(ScsGuardiasTurnoBean.C_IDGUARDIAPRINCIPAL, "");
			
				// JPT: Combo seleccionable de tipo de guardia
				nuevo.put(ScsGuardiasTurnoBean.C_TIPOGUARDIA, (miForm.getIdTipoGuardiaSeleccionado() == null ? "" : miForm.getIdTipoGuardiaSeleccionado()));			
			
				// preparando el campo idOrdenacionColas (si no existe, se debe insertar el valor
				ScsOrdenacionColasAdm ordenacion = 	new ScsOrdenacionColasAdm(this.getUserBean(request));
				Vector ordenacionVector = new Vector();
				String where =" where "+
				ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+miForm.getAlfabeticoApellidos()+" and "+
				ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+miForm.getAntiguedadEnCola()+" and "+
				ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+miForm.getEdad()+" and "+
				ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+miForm.getAntiguedad()+" ";
				ordenacionVector=ordenacion.select(where);
				Hashtable ordenacionHashtable = null;
				if (ordenacionVector.size()>0) {
					ScsOrdenacionColasBean ordenacioBean = (ScsOrdenacionColasBean)ordenacionVector.get(0);
					nuevo.put("IDORDENACIONCOLAS",((Integer)ordenacioBean.getIdOrdenacionColas()).toString());
				} else {
					ordenacion.prepararInsert(nuevo);
					ordenacionHashtable = new Hashtable();
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,miForm.getAlfabeticoApellidos());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,miForm.getAntiguedadEnCola());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,miForm.getEdad());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,miForm.getAntiguedad());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_USUMODIFICACION,(String)usr.getUserName());
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_FECHAMODIFICACION,"sysdate");
					ordenacionHashtable.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,nuevo.get("IDORDENACIONCOLAS"));
				}

				// calculando los dias de la semana seleccionados
				String laborables = this.obtenerSeleccionLaborables(miForm);
				String festivos = this.obtenerSeleccionFestivos(miForm);
				// Control de la seleccion
				if (laborables.equals("") && festivos.equals("")) {
					// No ha seleccionado nada
					throw new SIGAException("messages.gratuita.confGuardia.noSelecionTipoDia");
				} else {
					nuevo.put(ScsGuardiasTurnoBean.C_SELECCIONLABORABLES, laborables);
					nuevo.put(ScsGuardiasTurnoBean.C_SELECCIONFESTIVOS, festivos);
				}
				List<ScsGuardiasTurnoBean> guardiasVinculadasList = guardiaAdm.getGuardiasTurnosVinculadas(new Integer(miForm.getIdTurnoPestanha()),new Integer(miForm.getIdGuardiaPestanha()),new Integer(usr.getLocation()));
				// iniciando la modificacion
				tx = usr.getTransactionPesada();
				tx.begin();
				if (ordenacionHashtable!=null)
					ordenacion.insert(ordenacionHashtable);
				guardiaAdm.update(nuevo, guardia.getOriginalHash());
				if(guardiasVinculadasList!=null && guardiasVinculadasList.size()>0){
					String[] claves ={ScsGuardiasTurnoBean.C_IDINSTITUCION,ScsGuardiasTurnoBean.C_IDTURNO,ScsGuardiasTurnoBean.C_IDGUARDIA}; 
					String[] campos ={ScsGuardiasTurnoBean.C_DESCRIPCION,ScsGuardiasTurnoBean.C_NOMBRE,ScsGuardiasTurnoBean.C_DESCRIPCIONFACTURACION,ScsGuardiasTurnoBean.C_DESCRIPCIONPAGO};
					for(ScsGuardiasTurnoBean guardiaTurno:guardiasVinculadasList){
						Hashtable guardiaTurnoHashtable = (Hashtable)nuevo.clone();
						ScsGuardiasTurnoBean guardiaTurnoBean = (ScsGuardiasTurnoBean) guardiaAdm.hashTableToBean(guardiaTurnoHashtable);
						guardiaTurnoBean.setIdInstitucionPrincipal(new Integer(miForm.getIdInstitucionPestanha()));
						guardiaTurnoBean.setIdTurnoPrincipal(new Integer(miForm.getIdTurnoPestanha()));
						guardiaTurnoBean.setIdGuardiaPrincipal(new Integer(miForm.getIdGuardiaPestanha()));
						guardiaTurnoBean.setIdInstitucion(guardiaTurno.getIdInstitucion());
						guardiaTurnoBean.setIdTurno(guardiaTurno.getIdTurno());
						guardiaTurnoBean.setIdGuardia(guardiaTurno.getIdGuardia());
						guardiaTurnoBean.setDescripcion(guardiaTurno.getDescripcion());
						guardiaTurnoBean.setNombre(guardiaTurno.getNombre());
						guardiaTurnoBean.setDescripcionFacturacion(guardiaTurno.getDescripcionFacturacion());
						guardiaTurnoBean.setDescripcionPago(guardiaTurno.getDescripcionPago());
						guardiaAdm.updateDirect(guardiaTurnoBean);
						
					}
				}
				
				
				tx.commit();
			}
			
		//Control de excepciones
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return exitoRefresco("messages.updated.success",request);
	} //modificar ()
	
	/**
	 * Borra una Guardia de un Turno
	 */
	protected String borrar (ActionMapping mapping, 
							 MasterForm formulario, 
							 HttpServletRequest request, 
							 HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm)formulario;
		Hashtable aBorrar = new Hashtable();
		UsrBean usr = null;
		UserTransaction tx =null;
		
		try
		{
			//Controles generales
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		    tx = usr.getTransaction();
			
		    //Obteniendo los campos necesarios para hacer el borrado de la guardia
			ScsGuardiasTurnoAdm guardiaAdm = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
			aBorrar.put("IDTURNO",(String)turno.get("IDTURNO"));
			Vector ocultos = (Vector)miForm.getDatosTablaOcultos(0);
			try 				{aBorrar.put("IDGUARDIA",(String)turno.get("IDGUARDIA"));}
			catch(Exception e)	{aBorrar.put("IDGUARDIA",(String)ocultos.get(1));}
			aBorrar.put("IDINSTITUCION",usr.getLocation());
			
			//Iniciando el borrado
			tx.begin();
			guardiaAdm.delete(aBorrar);
			tx.commit();
			
		//Control de excepciones
		} catch (Exception e){
			throwExcp("messages..deleted.error",e,tx);
		}
		return exitoRefresco("messages.deleted.success",request);
	} //borrar ()
	
	/** Recorriendo el String semana para setear los dias que tenga de la semana */
	private void procesarSeleccionLaborables (String semana,
											  DefinirGuardiasTurnosForm miForm)
	{
		for (int i=0; i < semana.length(); i++){
			switch (semana.charAt(i)){
				case 'L': miForm.setSeleccionLaborablesLunes(ClsConstants.DB_TRUE);		break;
				case 'M': miForm.setSeleccionLaborablesMartes(ClsConstants.DB_TRUE);	break;
				case 'X': miForm.setSeleccionLaborablesMiercoles(ClsConstants.DB_TRUE);	break;
				case 'J': miForm.setSeleccionLaborablesJueves(ClsConstants.DB_TRUE);	break;
				case 'V': miForm.setSeleccionLaborablesViernes(ClsConstants.DB_TRUE);	break;
				case 'S': miForm.setSeleccionLaborablesSabado(ClsConstants.DB_TRUE);	break;
			}
		}
	} //procesarSeleccionLaborables ()
	/** Recorriendo el String semana para setear los dias que tenga de la semana */
	private void procesarSeleccionFestivos (String semana,
											DefinirGuardiasTurnosForm miForm)
	{
		for (int i=0; i < semana.length(); i++){
			switch (semana.charAt(i)){
				case 'L': miForm.setSeleccionFestivosLunes(ClsConstants.DB_TRUE);		break;
				case 'M': miForm.setSeleccionFestivosMartes(ClsConstants.DB_TRUE);		break;
				case 'X': miForm.setSeleccionFestivosMiercoles(ClsConstants.DB_TRUE);	break;
				case 'J': miForm.setSeleccionFestivosJueves(ClsConstants.DB_TRUE);		break;
				case 'V': miForm.setSeleccionFestivosViernes(ClsConstants.DB_TRUE);		break;
				case 'S': miForm.setSeleccionFestivosSabado(ClsConstants.DB_TRUE);		break;
				case 'D': miForm.setSeleccionFestivosDomingo(ClsConstants.DB_TRUE);		break;
			}
		}
	} //procesarSeleccionFestivos ()
	
	/** Obtiene el String semana a partir de los dias marcados en el jsp */
	private String obtenerSeleccionLaborables (DefinirGuardiasTurnosForm miForm)
	{
		String semana = "";
		
		try
		{
			if (miForm.getSeleccionLaborablesLunes()!=null && 
				miForm.getSeleccionLaborablesLunes().equals(ClsConstants.DB_TRUE))
				semana += "L";
			if (miForm.getSeleccionLaborablesMartes()!=null && 
				miForm.getSeleccionLaborablesMartes().equals(ClsConstants.DB_TRUE))
				semana += "M";
			if (miForm.getSeleccionLaborablesMiercoles()!=null && 
				miForm.getSeleccionLaborablesMiercoles().equals(ClsConstants.DB_TRUE))
				semana += "X";
			if (miForm.getSeleccionLaborablesJueves()!=null && 
				miForm.getSeleccionLaborablesJueves().equals(ClsConstants.DB_TRUE))
				semana += "J";
			if (miForm.getSeleccionLaborablesViernes()!=null && 
				miForm.getSeleccionLaborablesViernes().equals(ClsConstants.DB_TRUE))
				semana += "V";
			if (miForm.getSeleccionLaborablesSabado()!=null && 
				miForm.getSeleccionLaborablesSabado().equals(ClsConstants.DB_TRUE))
				semana += "S";
		}
		catch (Exception e){
			semana = "";
		}
		
		return semana;
	} //obtenerSeleccionLaborables ()
	
	/** Obtiene el String semana a partir de los dias marcados en el jsp */
	private String obtenerSeleccionFestivos (DefinirGuardiasTurnosForm miForm)
	{
		String semana = "";
		
		try
		{
			if (miForm.getSeleccionFestivosLunes()!=null && 
				miForm.getSeleccionFestivosLunes().equals(ClsConstants.DB_TRUE))
				semana += "L";
			if (miForm.getSeleccionFestivosMartes()!=null && 
				miForm.getSeleccionFestivosMartes().equals(ClsConstants.DB_TRUE))
				semana += "M";
			if (miForm.getSeleccionFestivosMiercoles()!=null && 
				miForm.getSeleccionFestivosMiercoles().equals(ClsConstants.DB_TRUE))
				semana += "X";
			if (miForm.getSeleccionFestivosJueves()!=null && 
				miForm.getSeleccionFestivosJueves().equals(ClsConstants.DB_TRUE))
				semana += "J";
			if (miForm.getSeleccionFestivosViernes()!=null && 
				miForm.getSeleccionFestivosViernes().equals(ClsConstants.DB_TRUE))
				semana += "V";
			if (miForm.getSeleccionFestivosSabado()!=null && 
				miForm.getSeleccionFestivosSabado().equals(ClsConstants.DB_TRUE))
				semana += "S";
			if (miForm.getSeleccionFestivosDomingo()!=null && 
				miForm.getSeleccionFestivosDomingo().equals(ClsConstants.DB_TRUE))
				semana += "D";
		}
		catch (Exception e){
			semana = "";
		}
		
		return semana;
	} //obtenerSeleccionFestivos ()
	
	/** Obtiene la etiqueta correspondiente a cada tipo de periodo */
	public static String obtenerTipoDiaPeriodo (String dia, UsrBean usr)
	{
		String texto = "";
		
		if (dia!=null && !dia.equals("")) {
			switch (dia.charAt(0)){
				case 'D': texto = UtilidadesString.getMensajeIdioma(usr,"gratuita.combo.literal.diasNaturales");		break;
				case 'S': texto = UtilidadesString.getMensajeIdioma(usr,"gratuita.combo.literal.semanasNaturales");		break;
				case 'Q': texto = UtilidadesString.getMensajeIdioma(usr,"gratuita.combo.literal.quincenasNaturales");	break;
				case 'M': texto = UtilidadesString.getMensajeIdioma(usr,"gratuita.combo.literal.mesesNaturales");		break;
			}
		}
		return texto;
	} //obtenerTipoDiaPeriodo ()
	
	/***/
	private String sustituir (ActionMapping mapping,
							  MasterForm miForm,
							  HttpServletRequest request,
							  HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		DefinirGuardiasTurnosForm turnosForm = (DefinirGuardiasTurnosForm)miForm;
		ScsTurnoAdm admTurno = new ScsTurnoAdm(this.getUserBean(request));
		ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(this.getUserBean(request));
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		try
		{
			//Obteniendo valores ocultos del formulario
			Vector ocultos = (Vector) turnosForm.getDatosTablaOcultos(0);
			
			//Obteniendo el nombre del turno
			String descripTurnoSelect="SELECT " + ScsTurnoBean.C_NOMBRE + " FROM " + ScsTurnoBean.T_NOMBRETABLA +
									  " WHERE " + ScsTurnoBean.C_IDINSTITUCION + "=" +usr.getLocation() +
									  "   AND " + ScsTurnoBean.C_IDTURNO + "=" + ocultos.get(0);
			Vector v = admTurno.ejecutaSelect(descripTurnoSelect);
			String nombreTurno = "";
			if(v != null && v.size() != 0)
				nombreTurno = (String)((Hashtable)v.get(0)).get(ScsTurnoBean.C_NOMBRE);
			request.setAttribute("nombreTurno",nombreTurno);
			
			//Obteniendo el nombre de la guardia
			String descripGuardiaSelect="SELECT " + ScsGuardiasTurnoBean.C_NOMBRE + " FROM " + ScsGuardiasTurnoBean.T_NOMBRETABLA +
			  							" WHERE " + ScsGuardiasTurnoBean.C_IDINSTITUCION + "=" +usr.getLocation() +
			  							"   AND " + ScsGuardiasTurnoBean.C_IDTURNO + "=" + ocultos.get(0)+
			  							"   AND " + ScsGuardiasTurnoBean.C_IDGUARDIA + "=" + ocultos.get(1);
			Vector v2 = admGuardias.ejecutaSelect(descripGuardiaSelect);
			String nombreGuardia = "";
			if(v2 != null && v2.size() != 0)
				nombreGuardia= (String)((Hashtable)v2.get(0)).get(ScsGuardiasTurnoBean.C_NOMBRE);
			request.setAttribute("nombreGuardia",nombreGuardia);
			
			//Obteniendo demas campos de la guardia personal
			request.setAttribute("idTurno",ocultos.get(0).toString());
			request.setAttribute("idGuardia",ocultos.get(1).toString());
			request.setAttribute("idPersona",ocultos.get(2).toString());
			turnosForm.setIdInstitucion(usr.getLocation());
			
			//Dejando la sesion
			request.setAttribute("origen","GUARDIASTURNO");
			request.setAttribute("action","/DefinirGuardiasTurnosAction.do");
			
		//Control de excepciones
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}
		return "sustituir";
	} //sustituir ()
	private void getAjaxGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		DefinirGuardiasTurnosForm miForm = (DefinirGuardiasTurnosForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		//Sacamos las guardias si hay algo selccionado en el turno
		List<ScsGuardiasTurnoBean> guardiasPrincipalesList = null;
		if(miForm.getIdTurnoPrincipal()!= null && !miForm.getIdTurnoPrincipal().equals("-1")&& !miForm.getIdTurnoPrincipal().equals("")){
			ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usrBean);
			guardiasPrincipalesList = admGuardias.getGuardiasTurnos(new Integer(miForm.getIdTurnoPrincipal()),new Integer(usrBean.getLocation()),true);
		}
		if(guardiasPrincipalesList==null){
			guardiasPrincipalesList = new ArrayList<ScsGuardiasTurnoBean>();
			
		}
		miForm.setGuardiasPrincipales(guardiasPrincipalesList);
		respuestaAjax(new AjaxCollectionXmlBuilder<ScsGuardiasTurnoBean>(), guardiasPrincipalesList,response);
		
	}
	
	/**
	 * Obtiene la lista de tipos de guardias
	 * 
	 * @return
	 * @throws ClsExceptions
	 */
	public static List<ScsTiposguardias> obtenerListaTiposGuardias () throws ClsExceptions {
		try  {
			// JPT: Obtiene los tipos de guardias
			BusinessManager businessManager = BusinessManager.getInstance();
			ScsTiposGuardiasService servicioTiposGuardias = (ScsTiposGuardiasService) businessManager.getService(ScsTiposGuardiasService.class);
			ScsTiposguardiasExample excampleTiposGuardias = new ScsTiposguardiasExample();
			List<ScsTiposguardias> listaTiposGuardias = servicioTiposGuardias.getList(excampleTiposGuardias);
			
			return listaTiposGuardias;
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar obtenerListaTiposGuardias()");
		}	
	} //obtenerListaTiposGuardias ()	
}