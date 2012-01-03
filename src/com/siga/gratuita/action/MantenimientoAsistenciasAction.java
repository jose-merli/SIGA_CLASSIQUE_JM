package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
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
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsCalendarioLaboralAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGDESIGNAAdm;
import com.siga.beans.ScsEJGDESIGNABean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AsistenciasForm;
import com.siga.ws.CajgConfiguracion;


/**
 * @author carlos.vidal
 * @since 3/2/2005
 */

public class MantenimientoAsistenciasAction extends MasterAction 
{
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		MasterForm miform = (MasterForm)formulario;
		String mapDestino = "exception";
		try{
			do {
				if (miform == null) {
					return mapping.findForward(this.abrir(mapping, miform, request, response));
				}
	
				String accion = miform.getModo();
				if (accion != null && !accion.equalsIgnoreCase("")) {
					if (accion.equalsIgnoreCase("relacionarConEJG")) {
						mapDestino = relacionarConEJGExt (true, miform, request);
						break;
					}
					if (accion.equalsIgnoreCase("borrarRelacionConEJG")) {
						mapDestino = relacionarConEJG (false, miform, request);
						break;
					}
					if (accion.equalsIgnoreCase("relacionarConDesigna")) {
						mapDestino = relacionarConDesignaExt (true, miform, request);
						break;
					}
					if (accion.equalsIgnoreCase("borrarRelacionConDesigna")) {
						mapDestino = relacionarConDesigna(false, miform, request);
						break;
					}
				}
				return super.executeInternal(mapping, formulario, request, response);

			} while (false);

			return mapping.findForward(mapDestino);

		}catch(SIGAException e){
			throw e;
		}
		catch(Exception es){
			throw new SIGAException("messages.general.error",es,new String[] {"modulo.gratuita"}); // o el recurso del modulo que sea
		}
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
	protected String abrir(ActionMapping mapping, 		
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws SIGAException 
    {
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		HttpSession ses = request.getSession();
		UsrBean usr 	= (UsrBean)ses.getAttribute("USRBEAN");
		String anio 	= (String) (request.getParameter("ANIO")!=null?request.getParameter("ANIO"):request.getSession().getAttribute("Asistencia_ANIO"));
		String numero 	= (String) (request.getParameter("NUMERO")!=null?request.getParameter("NUMERO"):request.getSession().getAttribute("Asistencia_NUMERO"));;
		String modo	 	= (String) (request.getParameter("MODO")!=null?request.getParameter("MODO"):request.getSession().getAttribute("MODO"));;;
		
		request.getSession().removeAttribute("accion");
		if(modo.equalsIgnoreCase("editar")) 
			request.getSession().setAttribute("accion","modificar");
		else
			request.getSession().setAttribute("accion","ver");
		// Obtenemos el turno seleccionado para la consulta
		String select = "SELECT  A.IDTURNO IDTURNO, C.IDGUARDIA IDGUARDIA, "+
							" A.ANIO ANIO, A.NUMERO NUMERO,A.FECHAANULACION, B.ABREVIATURA TURNO, BB.NOMBRE DES_DESIGNA_TURNO, C.NOMBRE GUARDIA,"+
							" D.FECHAENTRADA FECHA, E.NIF NIFASISTIDO, E.NOMBRE NOMBREAASISTIDO, E.IDPERSONA IDPERSONAJG, "+
							" E.APELLIDO1 APELLIDO1ASISTIDO, E.APELLIDO2 APELLIDO2ASISTIDO,"+
							" F.ANIO ANIOEJG, F.NUMERO NUMEROEJG, F.NUMEJG CODIGO_EJG, " + UtilidadesMultidioma.getCampoMultidiomaSimple("G.DESCRIPCION",this.getUserBean(request).getLanguage()) + " TIPOEJG, G.IDTIPOEJG IDTIPOEJG,"+
							" F.FECHAAPERTURA FECHAAPERTURA, A.DESIGNA_ANIO DESIGNA_ANIO, A.DESIGNA_TURNO DESIGNA_TURNO,"+
							" A.DESIGNA_NUMERO DESIGNA_NUMERO, B.NOMBRE NOMBRETURNO, D.FECHAENTRADA FECHAENTRADA,"+
							" A.IDTIPOASISTENCIA TIPOASISTENCIA,A.IDTIPOASISTENCIACOLEGIO TIPOASISTENCIACOLEGIO,"+ 
							" A.FECHAHORA FECHAHORA, A.FECHACIERRE FECHACIERRE, A.OBSERVACIONES OBSERVACIONES,"+
							" A.INCIDENCIAS INCIDENCIAS, I.NIFCIF NIFLETRADO, I.NOMBRE NOMBRELETRADO, I.APELLIDOS1 APELLIDO1LETRADO,"+
							" I.APELLIDOS2 APELLIDO2LETRADO, I.IDPERSONA IDPERSONACOLEGIADO, H.NCOLEGIADO NUMEROCOLEGIADO, D.CODIGO CODIGO, " +
							" A." + ScsAsistenciasBean.C_NUMERODILIGENCIA + " " + ScsAsistenciasBean.C_NUMERODILIGENCIA + ", " +
							" A." + ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO + " " + ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO + ", " +
							" A." + ScsAsistenciasBean.C_NIG + " " + ScsAsistenciasBean.C_NIG + ", " +
							" A." + ScsAsistenciasBean.C_JUZGADO + " " + ScsAsistenciasBean.C_JUZGADO + ", " +
							" A." + ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION + " " + ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION + ", " +
							" A." + ScsAsistenciasBean.C_COMISARIA + " " + ScsAsistenciasBean.C_COMISARIA + ", " +
							" A." + ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION + " " + ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION + ", " +
							" A." + ScsAsistenciasBean.C_IDESTADOASISTENCIA + " " + ScsAsistenciasBean.C_IDESTADOASISTENCIA + ", " +
							" A." + ScsAsistenciasBean.C_FECHAESTADOASISTENCIA+ " " + ScsAsistenciasBean.C_FECHAESTADOASISTENCIA +", " +
							" A." + ScsAsistenciasBean.C_IDFACTURACION+ " " + ScsAsistenciasBean.C_IDFACTURACION +
						" FROM "+
							" SCS_ASISTENCIA A, SCS_TURNO B, SCS_TURNO BB, SCS_GUARDIASTURNO C, SCS_DESIGNA D,"+
							" SCS_PERSONAJG E, SCS_EJG   F, SCS_TIPOEJG       G, CEN_COLEGIADO H, CEN_PERSONA I"+
						" WHERE"+
//		 CLAVES FORANEAS
//		A CON B
		" A.IDINSTITUCION = B.IDINSTITUCION AND A.IDTURNO = B.IDTURNO AND"+
//		A CON BB
		" D.IDINSTITUCION = BB.IDINSTITUCION (+) AND D.IDTURNO = BB.IDTURNO (+)  AND"+
//		A CON C
		" A.IDINSTITUCION = C.IDINSTITUCION AND A.IDTURNO = C.IDTURNO AND A.IDGUARDIA = C.IDGUARDIA AND"+
//		B CON C
		" B.IDINSTITUCION = C.IDINSTITUCION AND B.IDTURNO = C.IDTURNO AND"+
//		A CON D
		" A.IDINSTITUCION = D.IDINSTITUCION(+) AND A.Designa_Turno = D.IDTURNO(+) AND A.DESIGNA_ANIO = D.ANIO(+) AND A.DESIGNA_NUMERO = D.NUMERO(+) AND"+
//		A CON E
		" A.IDINSTITUCION = E.IDINSTITUCION(+) AND A.IDPERSONAJG = E.IDPERSONA(+) AND"+
//		A CON F
		" A.IDINSTITUCION = F.IDINSTITUCION(+) AND A.EJGANIO = F.ANIO(+) AND A.EJGNUMERO = F.NUMERO(+) AND A.EJGIDTIPOEJG = F.IDTIPOEJG(+) AND"+
//		F CON G
		" A.EJGIDTIPOEJG = G.IDTIPOEJG(+) AND "+
//		A CON H
		" A.IDINSTITUCION = H.IDINSTITUCION AND A.IDPERSONACOLEGIADO = H.IDPERSONA AND"+
//		H CON I
		" H.IDPERSONA = I.IDPERSONA"+
// clave de Asistencias
		" AND A.ANIO = "+anio+" AND A.NUMERO ="+numero+" AND A.IDINSTITUCION ="+usr.getLocation();
		ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
		try{
			
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			
			Vector resultado = null;
			resultado=(Vector)asistencias.ejecutaSelect(select);
			request.setAttribute("resultado",resultado);
			
			
			// Obtenemos el idJuzgado. Si solo tiene uno lo pasamos (para la creaion de Designas)
			String sql = "select distinct IDINSTITUCION_JUZG, IDJUZGADO" +
			              " from scs_actuacionasistencia " +
			             " where idinstitucion = " + usr.getLocation() +
						   " and anio = " + anio + 
			               " and numero = " + numero; 

			ScsActuacionAsistenciaAdm actuacionAdm = new ScsActuacionAsistenciaAdm (this.getUserBean(request));
			Vector v = actuacionAdm.selectGenerico(sql);
			if (v != null && v.size() == 1) {
				Hashtable h = (Hashtable) v.get(0);
				String idjuzgado = UtilidadesHash.getString(h, "IDJUZGADO") + "," + UtilidadesHash.getString(h, "IDINSTITUCION_JUZG"); 
				request.setAttribute("JUZGADO", idjuzgado);
			}
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "editar";
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
	 *  Funcion que atiende la accion editar
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
		String forward = "";
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			AsistenciasForm miForm = (AsistenciasForm) formulario;
			// Le quitamos al turno el idinstitucion
			String idTurno = miForm.getIdTurno();
			idTurno = idTurno.substring(idTurno.indexOf(",")+1);
			String sql = "SELECT cole.NCOLEGIADO,pers.NOMBRE||' '||pers.APELLIDOS1||' '||pers.APELLIDOS2 NOMBREYAPELLIDOS"+
			" FROM CEN_COLEGIADO cole, "+
			" SCS_GUARDIASCOLEGIADO guar,"+ 
			" CEN_PERSONA pers"+ 
			" WHERE cole.Idinstitucion = guar.idinstitucion"+
			" AND cole.idpersona = guar.idpersona"+
			" and pers.idpersona = cole.idpersona"+
			" and guar.idinstitucion = "+usr.getLocation()+
			" and guar.idturno = "+idTurno+
			" and guar.idguardia = "+miForm.getIdGuardia()+
			" AND GUAR.RESERVA = 'N'"+
			" AND trunc(SYSDATE) >= guar.FECHAINICIO"+ 
			" AND trunc(SYSDATE) <= guar.FECHAFIN"; 
			
			ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(usr);
			Vector obj = admBean.selectGenerico(sql);
			// Mandamos los datos que ya estan introducidos y el resultado de las guardias.
			forward = "nuevo";
			if(obj.size()==0)
			{
				request.setAttribute("mensaje","gratuita.nuevaAsistencia.mensaje.alert2");
				forward = "nuevo";
			}
			request.setAttribute("resultadoGuardias",obj);
			request.setAttribute("miForm",miForm);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
		
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
	protected String abrirBusquedaModal(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
		return null;
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
		AsistenciasForm miForm = (AsistenciasForm) formulario;
//		request.setAttribute("DATABACKUP", miForm.getDatos());

		request.setAttribute("anioAsistencia",  miForm.getAnio());
		request.setAttribute("nmeroAsistencia", miForm.getNumero());
		request.setAttribute("idInstitucionAsistencia", miForm.getIdInstitucion());
		request.setAttribute("idPersonaJGAsistencia", miForm.getIdPersonaJG());
		
		request.setAttribute("idTurnoAsistencia", this.getIDInstitucion(request) + "," +miForm.getIdTurno());
		request.setAttribute("idGuardiaAsistencia", miForm.getIdGuardia());
		request.setAttribute("nColegiadoAsistencia", miForm.getNumeroColegiado());
		request.setAttribute("nombreColegiado", miForm.getNombreColegiado());
		
		request.setAttribute("diligencia", miForm.getNumeroDilegencia());
		request.setAttribute("procedimiento", miForm.getNumeroProcedimiento());
		request.setAttribute("comisaria", miForm.getComisaria());
		request.setAttribute("juzgado", miForm.getJuzgado());
		//request.setAttribute("juzgadoAsistencia", miForm.getJuzgado()+ "," + this.getIDInstitucion(request));
		
		
		//si el usuario logado es letrado consultar en BBDD el nColegiado para mostrar en la jsp
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		if (usr.isLetrado()){
			CenColegiadoAdm colegiado = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean elegido = new CenColegiadoBean();
			try {
				String numeroColegiado = colegiado.getIdentificadorColegiado(usr);
				request.setAttribute("nColegiado",numeroColegiado);
			}
			catch (Exception e) 
			{
				throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
			} 
		}
		return "insertarEJG";
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
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			AsistenciasForm miForm = (AsistenciasForm) formulario;
			// Le quitamos al turno el idinstitucion
			String idTurno = miForm.getIdTurno();
			idTurno = idTurno.substring(idTurno.indexOf(",")+1);
			String sql="SELECT A.NCOLEGIADO,D.NOMBRE||' '||D.APELLIDOS1||' '||D.APELLIDOS2 NOMBREYAPELLIDOS FROM CEN_COLEGIADO A, "+
					" SCS_CALENDARIOGUARDIAS B, SCS_GUARDIASCOLEGIADO C, CEN_PERSONA D "+
					" WHERE "+
					" B.IDINSTITUCION = C.IDINSTITUCION AND B.IDTURNO = C.IDTURNO AND B.IDGUARDIA = C.IDGUARDIA "+ 
					" AND A.IDINSTITUCION = B.IDINSTITUCION AND A.IDPERSONA = C.IDPERSONA "+
					" AND A.IDPERSONA = D.IDPERSONA "+
					" AND SYSDATE >= B.FECHAINICIO AND SYSDATE <= B.FECHAFIN "+
					" AND B.IDINSTITUCION = "+usr.getLocation()+" AND B.IDTURNO = "+idTurno+
			  		" AND B.IDGUARDIA = "+miForm.getIdGuardia();
			ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(usr);
			Vector obj = admBean.selectGenerico(sql);
			// Mandamos los datos que ya estan introducidos y el resultado de las guardias.
			if(obj.size()==0)
				request.setAttribute("mensaje","No hay letrados con guardias planificadas para el dia seleccionado");
			request.setAttribute("miForm",miForm);
			request.getSession().setAttribute("resultadoGuardias",obj);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return "nuevo";
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
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
	protected synchronized String insertar(	ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException  {


		UsrBean usr = null;
		UserTransaction tx = null;
		
		try{
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			ScsSaltosCompensacionesAdm saltosCompAdm = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
			ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
			ScsGuardiasColegiadoAdm guardiasAdm = new ScsGuardiasColegiadoAdm(this.getUserBean(request));

			AsistenciasForm miForm = (AsistenciasForm) formulario;
			String idTurno = miForm.getIdTurno();
			// El turno viene con la institucion por delante.
			idTurno = idTurno.substring(idTurno.indexOf(",")+1);
			String idPersona = miForm.getIdPersona();
			String idGuardia = miForm.getIdGuardia();
			String salto = request.getParameter("checkSalto");
			String compensacion = request.getParameter("checkCompensacion");

			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String idTipoAsistencia  = rp.returnProperty("codigo.general.scs_tipoasistencia.tipoGeneral");
			//String idTipoAsistencia = miForm.getIdTipoAsistencia();
			String idTipoAsistenciaColegio = miForm.getIdTipoAsistenciaColegio();
			String anio = miForm.getFechaHora().substring(6);
			String fecha = GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaHora());
			boolean esFichaColegial  = UtilidadesString.stringToBoolean(request.getParameter("esFichaColegial").toString());


			//-------------------------------------------------------------------------------------------
			// Comprobamos si el asistente esta ya inscrito en alguna guardia en la fecha de asistencia.
			// si ya esta inscrito seguimos con el proceso e insertamos la asistencia. Si no lo está primero
			//  le inscribimos en la guardia, si procede insertaremos saltos y compensaciones y finalmente 
			// insertamos la asistencia.
			//-------------------------------------------------------------------------------------------

			Hashtable guardia = new Hashtable();
			guardia.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,usr.getLocation());
			guardia.put(ScsGuardiasColegiadoBean.C_IDTURNO,idTurno);
			guardia.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,idGuardia);
			guardia.put(ScsGuardiasColegiadoBean.C_IDPERSONA,idPersona);
			guardia.put(ScsGuardiasColegiadoBean.C_FECHAFIN,fecha);
			Vector guardiasVector = guardiasAdm.select(guardia);
			//lo pongo esto aqui porque todavia no me ha abioerto la transaccion
			if((guardiasVector == null || guardiasVector.isEmpty())&&!esFichaColegial){
				CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usr);
				//comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
				Map<String,CenBajasTemporalesBean> mBajasTemporalesConfirmador =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(idPersona), new Integer(usr.getLocation()));
				if(mBajasTemporalesConfirmador.containsKey(miForm.getFechaHora()))
					throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");

			}
			tx.begin();
			if(guardiasVector == null || guardiasVector.isEmpty())
			{ 
				if (!esFichaColegial){
					// EL ASISTENTE NO ESTÁ DE GUARDIA ESE DÍA. 

					//----------------------------------------------------------------------------------------------------------------------------------
					// Tenemos que inscribirle en la guardia.Primero intentamos obtener registros de 
					// ScsGuardiasColegiado en los que fechafin sea la fecha de asistencia. Si existe algun registro, obtenemos la cabecera de guardia
					// correspondiente a ese registro. Despues a partir de esa cabecera obtendremos las guardiascolegiado asociadas y daremos de alta al asistente
					// en ambas tablas con los registros obtenidos de ellas cambiando el idpersona de ese registro por el del asistente. Si existen varios registros en 
					// guardiascolegiado con fechafin = fecha asistencia entonces cogemos el primero. Si no existe ningún registro lanzaremos el mensaje de que
					// no hay calendario definido para ese periodo.
					//----------------------------------------------------------------------------------------------------------------------------------
					String truncFechaGuardia = GstDate.getFormatedDateShort("", fecha); 
					guardiasAdm.insertarGuardiaManual(usr.getLocation(), idTurno,
							idGuardia, idPersona,  
							null,null,truncFechaGuardia,usr);
				}else{
					// Si no cerramos la transaccion no vuelve correctamente
					tx.rollback();
					throw new SIGAException("gratuita.nuevaAsistencia.literal.letradoSinGuardia");
				}

			}
			// Insertamos la asistencia

			String numero = asistencias.getNumeroAsistencia(usr.getLocation(), Integer.parseInt(anio));
			String estadoAsistencia = "1";	// Activo
			asistencias.insertarNuevaAsistencia(usr.getLocation(), anio,numero, fecha, idTurno, idGuardia, idTipoAsistencia, idTipoAsistenciaColegio,idPersona, estadoAsistencia);

			// Si estamos clonando puede que necesitemos meter el juzgado y comisaria
			// Esto lo hacemos con un update por no modificar el insert, que podria dar problemas ya que se llama desde mas sitios
			Hashtable hash = new Hashtable();
			String juzgado = request.getParameter("juzgado");
			String comisaria = request.getParameter("comisaria");
			if((juzgado!=null&&!juzgado.equalsIgnoreCase("")) || comisaria!=null&&!comisaria.equalsIgnoreCase("")){
				UtilidadesHash.set(hash, ScsAsistenciasBean.C_IDINSTITUCION, usr.getLocation());
				UtilidadesHash.set(hash, ScsAsistenciasBean.C_ANIO, anio);
				UtilidadesHash.set(hash, ScsAsistenciasBean.C_NUMERO, numero);
				ArrayList<String> camposAct = new ArrayList<String>();
				if (juzgado != null && !juzgado.equals("")) {
					String a[] = juzgado.split(",");
					UtilidadesHash.set(hash, ScsAsistenciasBean.C_JUZGADO, a[0].trim());
					UtilidadesHash.set(hash, ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION, a[1].trim());
					camposAct.add(ScsAsistenciasBean.C_JUZGADO);
					camposAct.add(ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION);
				}

				if (comisaria != null && !comisaria.equals("")) {
					String a[] = comisaria.split(",");
					UtilidadesHash.set(hash, ScsAsistenciasBean.C_COMISARIA, a[0].trim());
					UtilidadesHash.set(hash, ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION, a[1].trim());
					camposAct.add(ScsAsistenciasBean.C_COMISARIA);
					camposAct.add(ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION);
				}

				String claves [] ={ScsAsistenciasBean.C_ANIO,ScsAsistenciasBean.C_NUMERO, ScsAsistenciasBean.C_IDINSTITUCION};

				asistencias.updateDirect(hash,claves,camposAct.toArray(new String[camposAct.size()]));
			}
			//		

			// Para abrir la ventana de la asistencia
			request.getSession().setAttribute("Asistencia_ANIO", anio);
			request.getSession().setAttribute("Asistencia_NUMERO", numero);
			request.getSession().setAttribute("MODO", "editar");

			//Insertamos saltos y compensaciones si procede
			String motivo = UtilidadesString.getMensajeIdioma(usr,"gratuita.literal.altaAsistencia.motivo");


			if (salto != null&&(salto.equals("on") || salto.equals("1")))
				saltosCompAdm.crearSaltoCompensacion(usr.getLocation(),idTurno,idGuardia,idPersona, motivo,ClsConstants.SALTOS);
			if (compensacion != null&&(compensacion.equals("on") || compensacion.equals("1")))
				saltosCompAdm.crearSaltoCompensacion(usr.getLocation(),idTurno,idGuardia,idPersona, motivo,ClsConstants.COMPENSACIONES);
			if(miForm.getModo()!=null && miForm.getModo().equals("insertar"))
				miForm.setModo("");

			tx.commit();

		}catch (SIGAException e) {
			request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr,e.getLiteral()));	
			return "errorConAviso"; 
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return exitoModal("messages.inserted.success",request);

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
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException  
	{
		String forward = "";
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		UserTransaction tx = null;
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx = usr.getTransaction();
			tx.begin();

			Hashtable hash = new Hashtable();
			// Dependiendo de donde vengamos tenemos que modificar unos campos u otros.
			ScsAsistenciasAdm scsAsistenciaAdm = new ScsAsistenciasAdm(usr);
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			//Modificamos el TipoAsistencia,TipoAsistenciaColegio y la FechaCierre.
			String[] campos = {ScsAsistenciasBean.C_IDTIPOASISTENCIA, 			ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO,
								ScsAsistenciasBean.C_FECHACIERRE,				ScsAsistenciasBean.C_OBSERVACIONES,
								ScsAsistenciasBean.C_INCIDENCIAS,				ScsAsistenciasBean.C_JUZGADO,
								ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION,		ScsAsistenciasBean.C_COMISARIA,
								ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION,	ScsAsistenciasBean.C_NUMERODILIGENCIA,
								ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO,		ScsAsistenciasBean.C_FECHAANULACION,
								ScsAsistenciasBean.C_IDESTADOASISTENCIA,		ScsAsistenciasBean.C_FECHAESTADOASISTENCIA,
								ScsAsistenciasBean.C_USUMODIFICACION,		    ScsAsistenciasBean.C_NIG,
								ScsAsistenciasBean.C_FECHAMODIFICACION};
			
			// Campos a modificar
			hash.put(ScsAsistenciasBean.C_IDTIPOASISTENCIA,miForm.getIdTipoAsistencia());
			hash.put(ScsAsistenciasBean.C_IDTIPOASISTENCIACOLEGIO,miForm.getIdTipoAsistenciaColegio());
			if(!miForm.getFechaCierre().equals(""))
				hash.put(ScsAsistenciasBean.C_FECHACIERRE,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaCierre()));
			hash.put(ScsAsistenciasBean.C_OBSERVACIONES,miForm.getObservaciones());
			hash.put(ScsAsistenciasBean.C_INCIDENCIAS,miForm.getIncidencias());
			
			String juzgado = miForm.getJuzgado();
			if (juzgado != null && !juzgado.equals("")) {
				String a[] = juzgado.split(",");
				UtilidadesHash.set(hash, ScsAsistenciasBean.C_JUZGADO, a[0].trim());
				UtilidadesHash.set(hash, ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION, a[1].trim());
			}

			String comisaria = miForm.getComisaria();
			if (comisaria != null && !comisaria.equals("")) {
				String a[] = comisaria.split(",");
				UtilidadesHash.set(hash, ScsAsistenciasBean.C_COMISARIA, a[0].trim());
				UtilidadesHash.set(hash, ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION, a[1].trim());
			}
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_NUMERODILIGENCIA,    miForm.getNumeroDilegencia());
			UtilidadesHash.set(hash, ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO, miForm.getNumeroProcedimiento());
			
			if (miForm.getNig() != null) {
			    UtilidadesHash.set(hash, ScsAsistenciasBean.C_NIG, miForm.getNig());
			}else{
				 UtilidadesHash.set(hash, ScsAsistenciasBean.C_NIG, "");
			}
			
			// Campos a clave
			hash.put(ScsAsistenciasBean.C_ANIO,miForm.getAnio());
			hash.put(ScsAsistenciasBean.C_NUMERO,miForm.getNumero());
			hash.put(ScsAsistenciasBean.C_IDINSTITUCION,usr.getLocation());

			String estado         = miForm.getEstadoAsintecia();
			String estadoAnterior = miForm.getEstadoAsinteciaAnterior();
			if (estado != null) {
				if (estadoAnterior == null || !estado.equals(estadoAnterior)) {
					UtilidadesHash.set(hash, ScsAsistenciasBean.C_IDESTADOASISTENCIA, estado);
					if (estado.equals(""))
						UtilidadesHash.set(hash, ScsAsistenciasBean.C_FECHAESTADOASISTENCIA, "");
					else 
						UtilidadesHash.set(hash, ScsAsistenciasBean.C_FECHAESTADOASISTENCIA, "SYSDATE");
					
					//////////////////////
					// Anulamos / Activamos las actuaciones de la asistencia
					boolean bAnular = false;
					if (estado.equals("2") || estado.equals("3")) { // Anulado o Venia
						bAnular = true;
						UtilidadesHash.set(hash, ScsAsistenciasBean.C_FECHAANULACION, "SYSDATE");
					} 
					else {
						UtilidadesHash.set(hash, ScsAsistenciasBean.C_FECHAANULACION, "");
					}
					
					Hashtable act = new Hashtable();
					UtilidadesHash.set (act, ScsActuacionAsistenciaBean.C_ANIO, miForm.getAnio());
					UtilidadesHash.set (act, ScsActuacionAsistenciaBean.C_NUMERO, miForm.getNumero());
					UtilidadesHash.set (act, ScsActuacionAsistenciaBean.C_IDINSTITUCION, usr.getLocation());

					ScsActuacionAsistenciaAdm actuacionAdm = new ScsActuacionAsistenciaAdm(this.getUserBean(request)); 
					Vector actuaciones = actuacionAdm.select(act);
					for(int i = 0; i < actuaciones.size();i++) {
						ScsActuacionAsistenciaBean actuacionAsistencia = (ScsActuacionAsistenciaBean) actuaciones.get(i);
						String facturado = actuacionAsistencia.getFacturado();
						if(facturado == null || facturado.trim().equals("")) {
							actuacionAsistencia.setAnulacion(bAnular?new Integer(ClsConstants.DB_TRUE): new Integer(ClsConstants.DB_FALSE));
							if(!actuacionAdm.updateDirect(actuacionAsistencia))
								throw new ClsExceptions(actuacionAdm.getError());
						}
					}
					//////////////////////
				}else{
					//NO HA CAMBIADO NADA
					UtilidadesHash.set(hash, ScsAsistenciasBean.C_IDESTADOASISTENCIA, estado);
					UtilidadesHash.set(hash, ScsAsistenciasBean.C_FECHAESTADOASISTENCIA, GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaEstadoAsistencia()));
				}
			}

			scsAsistenciaAdm.updateDirect(hash,null,campos);
			
			tx.commit();
			
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
		}
		catch (Exception e) 
		{
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return forward;
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
	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
			
		return "exito";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		try{
			HttpSession ses = request.getSession();
			UsrBean usr 	= (UsrBean)ses.getAttribute("USRBEAN");
			ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
			AsistenciasForm miForm = (AsistenciasForm) formulario;
			// Le quitamos al turno el idinstitucion
			String idTurno = miForm.getIdTurno();
			idTurno = idTurno.substring(idTurno.indexOf(",")+1);
			
			String sql = "SELECT cole.NCOLEGIADO,pers.NOMBRE||' '||pers.APELLIDOS1||' '||pers.APELLIDOS2 NOMBREYAPELLIDOS"+
			" FROM CEN_COLEGIADO cole, "+
			" SCS_GUARDIASCOLEGIADO guar,"+ 
			" CEN_PERSONA pers"+ 
			" WHERE cole.Idinstitucion = guar.idinstitucion"+
			" AND cole.idpersona = guar.idpersona"+
			" and pers.idpersona = cole.idpersona"+
			" and guar.idinstitucion = "+usr.getLocation()+
			" and guar.idturno = "+idTurno+
			" and guar.idguardia = "+miForm.getIdGuardia()+
			" AND GUAR.RESERVA = 'N'"+
			" AND trunc(SYSDATE) >= guar.FECHAINICIO"+ 
			" AND trunc(SYSDATE) <= guar.FECHAFIN"; 
			ScsCalendarioLaboralAdm admBean =  new ScsCalendarioLaboralAdm(this.getUserBean(request));
			Vector obj = admBean.selectGenerico(sql);
			Vector resultado = (Vector)asistencias.ejecutaSelect(sql);
			request.setAttribute("resultado",resultado);
		}
		catch (Exception e) 
		{
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "abrirBusquedaModal";
	}
	
	protected String relacionarConEJG (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
		UserTransaction tx = null;
		try {
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx=usr.getTransaction();
			
			Hashtable miHash = new Hashtable ();

			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDINSTITUCION, 	miForm.getEjg_idInstitucion());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_ANIO,			miForm.getAnio());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_NUMERO,			miForm.getNumero());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDTURNO,			miForm.getIdTurno());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDGUARDIA,			miForm.getIdGuardia());

			if (bCrear) {
				// Creamos la relacion
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGANIO,  	miForm.getEjg_anio());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGNUMERO,	miForm.getEjg_numero());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGIDTIPOEJG,		miForm.getEjg_idTipoEJG());
			}
			else {
				// Borramos la relacion
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGANIO,  	"");
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGNUMERO,	"");
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_EJGIDTIPOEJG,		"");
			}

			tx.begin();
			
			String [] campos = {ScsAsistenciasBean.C_EJGANIO, ScsAsistenciasBean.C_EJGNUMERO,ScsAsistenciasBean.C_EJGIDTIPOEJG,ScsAsistenciasBean.C_USUMODIFICACION,ScsAsistenciasBean.C_FECHAMODIFICACION};
			ScsAsistenciasAdm asiAdm = new ScsAsistenciasAdm(this.getUserBean(request));  
			if (!asiAdm.updateDirect(miHash, null, campos)) {
				throw new ClsExceptions ("Error de la relacion Asistencia - EJG: "+asiAdm.getError() );
			}
			tx.commit();
			
		}
		catch (Exception e) 
		{
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 

		return "exito";
	}

	
	protected String relacionarConEJGExt (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
		String mapDestino;
		UserTransaction tx = null;
		try {
			
			
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			mapDestino = this.relacionarConEJG(true, miForm, request);;
			
			if((mapDestino.equalsIgnoreCase("exito"))){
				if (miForm.getDesigna_anio()!=null && !miForm.getDesigna_anio().equals("")){
				
				Hashtable hashEjgDesigna=new Hashtable();
				ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();
				
				ejgDesignabean.setIdInstitucion(this.getIDInstitucion(request));
				ejgDesignabean.setAnioDesigna(new Integer(miForm.getDesigna_anio()));
				ejgDesignabean.setIdTurno(new Integer(miForm.getDesigna_turno()));
				ejgDesignabean.setNumeroDesigna(new Integer(miForm.getDesigna_numero()));
				ejgDesignabean.setAnioEJG(new Integer((String)miForm.getEjg_anio()));
				ejgDesignabean.setIdTipoEJG(new Integer((String)miForm.getEjg_idTipoEJG()));
				ejgDesignabean.setNumeroEJG(new Integer((String)miForm.getEjg_numero()));
				
				ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
				
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIODESIGNA, 			miForm.getDesigna_anio());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMERODESIGNA,  new Integer(miForm.getDesigna_numero()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTURNO, 		miForm.getDesigna_turno());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTIPOEJG, 		new Integer((String)miForm.getEjg_idTipoEJG()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMEROEJG, 		new Integer((String)miForm.getEjg_numero()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIOEJG, 		new Integer((String)miForm.getEjg_anio()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDINSTITUCION, 		this.getIDInstitucion(request));
				Vector existeRelacion = ejgDesignaAdm.select(hashEjgDesigna);
				
				tx=this.getUserBean(request).getTransaction();
				tx.begin();
				if (existeRelacion.size()==0){//Si no existe la relación, la creamos
					if(!ejgDesignaAdm.insert(ejgDesignabean))
						throw new ClsExceptions ("Error al crear la relacion entre EJG y la designa");
				}
				tx.commit();
				
				
				//Obtenemos las relaciones del ejg
				/*ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));
				Vector vRelacionados = new Vector(); 
				vRelacionados = ejgAdm.getRelacionadoCon (	miForm.getIdInstitucion(),miForm.getEjg_anio(), 
															miForm.getEjg_numero(), miForm.getEjg_idTipoEJG());
				// Recorremos el vector 
				if (vRelacionados.size()>0){
					// Solo nos quedaremos con la designacione relacionada si es unica
					Hashtable ejgRelacionado;
					int posAsistencia = 0; // Almacenara la posicion de la asistencia unica
					boolean encontradaAsistenciaUnica = false; // Nos indicará que realmente es unica
					for (int i=0;i<vRelacionados.size();i++){
						ejgRelacionado = (Hashtable) vRelacionados.get(i);
						String tipo = (String)ejgRelacionado.get("SJCS"); 
						// Comprobamos que sea asistencia y no se haya encontrado otra antes
						if((tipo.equalsIgnoreCase("DESIGNA"))&& (!encontradaAsistenciaUnica)){
							// Almacenamos la posicion
							encontradaAsistenciaUnica = true;
							posAsistencia = i;
						}
						else
							encontradaAsistenciaUnica = false;
					}
					// Si era una unica designa creamos la relacion usando el metodo relacionarConDesigna
					if (encontradaAsistenciaUnica){
						ejgRelacionado = (Hashtable) vRelacionados.get(posAsistencia);
						miForm.setDesigna_anio((String)ejgRelacionado.get("ANIO"));
						miForm.setDesigna_idInstitucion((String)ejgRelacionado.get("IDINSTITUCION"));
						miForm.setDesigna_turno((String)ejgRelacionado.get("IDTURNO"));
						miForm.setDesigna_numero((String)ejgRelacionado.get("NUMERO"));
						this.relacionarConDesigna(true, miForm, request);
					}
				}*/
			
			}else{
				//Obtenemos las relaciones del ejg
				ScsEJGAdm ejgAdm = new ScsEJGAdm(this.getUserBean(request));
				Vector vRelacionados = new Vector(); 
				vRelacionados = ejgAdm.getRelacionadoCon (	miForm.getIdInstitucion(),miForm.getEjg_anio(), 
															miForm.getEjg_numero(), miForm.getEjg_idTipoEJG());
				// Recorremos el vector 
				if (vRelacionados.size()>0){
					// Solo nos quedaremos con la designacione relacionada si es unica
					Hashtable ejgRelacionado;
					int posAsistencia = 0; // Almacenara la posicion de la asistencia unica
					boolean encontradaAsistenciaUnica = false; // Nos indicará que realmente es unica
					for (int i=0;i<vRelacionados.size();i++){
						ejgRelacionado = (Hashtable) vRelacionados.get(i);
						String tipo = (String)ejgRelacionado.get("SJCS"); 
						// Comprobamos que sea asistencia y no se haya encontrado otra antes
						if((tipo.equalsIgnoreCase("DESIGNA"))&& (!encontradaAsistenciaUnica)){
							// Almacenamos la posicion
							encontradaAsistenciaUnica = true;
							posAsistencia = i;
						}
						else
							encontradaAsistenciaUnica = false;
					}
					// Si era una unica designa creamos la relacion usando el metodo relacionarConDesigna
					if (encontradaAsistenciaUnica){
						ejgRelacionado = (Hashtable) vRelacionados.get(posAsistencia);
						miForm.setDesigna_anio((String)ejgRelacionado.get("ANIO"));
						miForm.setDesigna_idInstitucion((String)ejgRelacionado.get("IDINSTITUCION"));
						miForm.setDesigna_turno((String)ejgRelacionado.get("IDTURNO"));
						miForm.setDesigna_numero((String)ejgRelacionado.get("NUMERO"));
						this.relacionarConDesigna(true, miForm, request);
					}
				}
			}
			}		
		}
		catch (Exception e) 
		{
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 

		return "exito";
	}
	
	protected String relacionarConDesigna (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
	    UserTransaction tx = null;
		try {
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx=usr.getTransaction();
			
			Hashtable miHash = new Hashtable ();

			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_IDINSTITUCION, 	miForm.getIdInstitucion());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_NUMERO, 		miForm.getNumero());
			UtilidadesHash.set(miHash, ScsAsistenciasBean.C_ANIO, 			miForm.getAnio());

			if (bCrear) {
				// Creamos la relacion
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_ANIO, 	miForm.getDesigna_anio());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_NUMERO, miForm.getDesigna_numero());
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_TURNO, 	miForm.getDesigna_turno());
			}
			else {
				// Borramos la relacion
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_ANIO, 	"");
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_NUMERO, "");
				UtilidadesHash.set(miHash, ScsAsistenciasBean.C_DESIGNA_TURNO, 	"");
			}

			tx.begin();
			
			String [] campos = {ScsAsistenciasBean.C_DESIGNA_ANIO, ScsAsistenciasBean.C_DESIGNA_NUMERO, ScsAsistenciasBean.C_DESIGNA_TURNO};
			ScsAsistenciasAdm asisAdm = new ScsAsistenciasAdm(this.getUserBean(request));  
			if (!asisAdm.updateDirect(miHash, null, campos)) {
				throw new ClsExceptions ("Error de la relacion Asistencia - Designa");
			}
			tx.commit();
			
		}
		catch (Exception e) 
		{
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 

		} 

		return "exito";
	}
	
	protected String relacionarConDesignaExt (boolean bCrear, MasterForm formulario, HttpServletRequest request) throws ClsExceptions,SIGAException  
	{
	    
		String mapDestino;
		UserTransaction tx = null;
		try {
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			// Relacionamos la asistencia con la designa
			
			mapDestino = this.relacionarConDesigna(true, miForm, request);
			Vector datosEJG= new Vector();
			
			if((mapDestino.equalsIgnoreCase("exito"))){
			
				// Si la asistencia tiene ya un ejg relacionado, se crea automaticamente la relacion entre el
				// ejg y la designacion
				if	(!miForm.getEjg_numero().equalsIgnoreCase("")){
				UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
				
				tx=usr.getTransaction();
				
				
				Hashtable hashEjgDesigna=new Hashtable();
				ScsEJGDESIGNABean ejgDesignabean=new ScsEJGDESIGNABean();
				
				ejgDesignabean.setIdInstitucion(this.getIDInstitucion(request));
				ejgDesignabean.setAnioDesigna(new Integer(miForm.getDesigna_anio()));
				ejgDesignabean.setIdTurno(new Integer(miForm.getDesigna_turno()));
				ejgDesignabean.setNumeroDesigna(new Integer(miForm.getDesigna_numero()));
				ejgDesignabean.setAnioEJG(new Integer((String)miForm.getEjg_anio()));
				ejgDesignabean.setIdTipoEJG(new Integer((String)miForm.getEjg_idTipoEJG()));
				ejgDesignabean.setNumeroEJG(new Integer((String)miForm.getEjg_numero()));
				
				ScsEJGDESIGNAAdm ejgDesignaAdm = new ScsEJGDESIGNAAdm (this.getUserBean(request));
				
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIODESIGNA, 			miForm.getDesigna_anio());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMERODESIGNA,  new Integer(miForm.getDesigna_numero()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTURNO, 		miForm.getDesigna_turno());
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDTIPOEJG, 		new Integer((String)miForm.getEjg_idTipoEJG()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_NUMEROEJG, 		new Integer((String)miForm.getEjg_numero()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_ANIOEJG, 		new Integer((String)miForm.getEjg_anio()));
				UtilidadesHash.set(hashEjgDesigna, ejgDesignabean.C_IDINSTITUCION, 		this.getIDInstitucion(request));
				Vector existeRelacion = ejgDesignaAdm.select(hashEjgDesigna);
				
				
				tx.begin();
				if (existeRelacion.size()==0){//Si no existe la relación, la creamos
					if(!ejgDesignaAdm.insert(ejgDesignabean))
						throw new ClsExceptions ("Error al crear la relacion entre EJG y la designa");
				}
				tx.commit();
				
				
				
			}else{
				//Obtenemos las relaciones de la desgina
				ScsDesignaAdm desAdm = new ScsDesignaAdm(this.getUserBean(request));
				Vector vRelacionados = new Vector(); 
				vRelacionados = desAdm.getRelacionadoCon (	miForm.getIdInstitucion(), miForm.getDesigna_anio(), 
															miForm.getDesigna_numero(), miForm.getDesigna_turno());
				// Recorremos el vector 
				if (vRelacionados.size()>0){
					// Solo nos quedaremos con el relacionado si es unico
					Hashtable ejgRelacionado;
					int posAsistencia = 0; // Almacenara la posicion de la asistencia unica
					boolean encontradoEJGUnico = false; // Nos indicará que realmente es unica
					for (int i=0;i<vRelacionados.size();i++){
						ejgRelacionado = (Hashtable) vRelacionados.get(i);
						String tipo = (String)ejgRelacionado.get("SJCS"); 
						// Comprobamos que sea ejg y no se haya encontrado otra antes
						if((tipo.equalsIgnoreCase("EJG"))&& (!encontradoEJGUnico)){
							// Almacenamos la posicion
							encontradoEJGUnico = true;
							posAsistencia = i;
						}
						else
							encontradoEJGUnico = false;
					}
					// Si era un unico ejg creamos la relacion usando el metodo relacionarConEJG
					if (encontradoEJGUnico){
						ejgRelacionado = (Hashtable) vRelacionados.get(posAsistencia);
						miForm.setEjg_anio((String)ejgRelacionado.get("ANIO"));
						miForm.setEjg_idInstitucion((String)ejgRelacionado.get("IDINSTITUCION"));
						miForm.setEjg_idTipoEJG((String)ejgRelacionado.get("IDTIPO"));
						miForm.setEjg_numero((String)ejgRelacionado.get("NUMERO"));
						this.relacionarConEJG(true, miForm, request);
					}
				}
			}
			}
		}
		catch (Exception e) 
		{
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 

		} 

		return "exito";
	}


}