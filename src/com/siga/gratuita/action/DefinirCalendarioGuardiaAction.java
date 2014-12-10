package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FcsFactApunteAdm;
import com.siga.beans.HelperInformesAdm;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsCalendarioGuardiasAdm;
import com.siga.beans.ScsCalendarioGuardiasBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsPermutaCabeceraAdm;
import com.siga.beans.ScsPermutaGuardiasAdm;
import com.siga.beans.ScsPermutaGuardiasBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirCalendarioGuardiaForm;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

/**
 * Maneja las acciones que se pueden realizar sobre las tablas SCS_CALENDARIOGUARDIAS. <br>
 * Implementa la parte de control del caso de uso de Definir Incompatibilidades de Guardias.
 * 
 * @author david.sanchezp
 * @since 19/1/2005
 * @version adrian.ayala 16/05/2008: Modificacion de los metodos 
 * 		editar() y modalNuevoCalendario() para sustituir el tipodias
 */

public class DefinirCalendarioGuardiaAction extends MasterAction
{
	/** 
	 * M�todo que atiende a las peticiones.
	 * Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request,
			HttpServletResponse response)
	throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;

		try
		{ 
			miForm = (MasterForm) formulario;
			String accion = miForm.getModo();

			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				mapDestino = abrir(mapping, miForm, request, response);				
			} else if (accion.equalsIgnoreCase("insertarCalendario")){
				mapDestino = insertarCalendario(mapping, miForm, request, response);				
			} else if (accion.equalsIgnoreCase("insertarCalendarioAutomaticamente")){
				mapDestino = insertarCalendarioAutomaticamente(mapping, miForm, request, response);				
			} else if (accion.equalsIgnoreCase("insertarGuardiaManual")){
				mapDestino = insertarGuardiaManual(mapping, miForm, request, response);				
			} else if (accion.equalsIgnoreCase("modalNuevoCalendario")){
				mapDestino = modalNuevoCalendario(mapping, miForm, request, response);				
			} else if (accion.equalsIgnoreCase("modalNuevaGuardia")){
				mapDestino = modalNuevaGuardia(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("anular")){
				mapDestino = anular(mapping, miForm, request, response);	
			} else if (accion.equalsIgnoreCase("realizarAnulacion")){
				mapDestino = realizarAnulacion(mapping, miForm, request, response);						
			} else if (accion.equalsIgnoreCase("descargarLog")){
				mapDestino = descargarLog(mapping, miForm, request, response);
			} else {			
				return super.executeInternal(mapping,
						formulario,
						request, 
						response);
			}
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		return mapping.findForward(mapDestino);
	} //executeInternal ()

	/**
	 * Hace una busqueda del calendario de guardias. 
	 * Se corresponde con la busqueda de la primera pantalla del caso de uso.
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String buscar (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response)
	throws SIGAException
	{
		//Controles globales
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(this.getUserBean(request));
		UsrBean usr = null;

		//Variables
		Vector salida = new Vector();
		String idinstitucion="", idturno="", idguardia="";

		try
		{
			//obteniendo informacion del usuario
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			//obteniendo valores del formulario
			idinstitucion = miForm.getIdInstitucionPestanha();
			idturno = miForm.getIdTurnoPestanha();
			idguardia = miForm.getIdGuardiaPestanha();			

			//SELECT
			salida = admCalendarioGuardia.selectGenerico(admCalendarioGuardia.getCalendarios(idinstitucion,idturno,idguardia));

			//obteniendo y pasando por sesion los nombres de turno y guardia
			Hashtable hashTurno = new Hashtable ();
			hashTurno.put (ScsTurnoBean.C_IDINSTITUCION, miForm.getIdInstitucionPestanha ());
			hashTurno.put (ScsTurnoBean.C_IDTURNO, miForm.getIdTurnoPestanha ());
			ScsTurnoBean beanTurno = (ScsTurnoBean) (new ScsTurnoAdm (usr).select (hashTurno)).get (0);
			request.setAttribute ("NOMBRETURNO", beanTurno.getNombre ());
			Hashtable hashGuardia = new Hashtable ();
			hashGuardia.put (ScsGuardiasTurnoBean.C_IDINSTITUCION, miForm.getIdInstitucionPestanha ());
			hashGuardia.put (ScsGuardiasTurnoBean.C_IDTURNO, miForm.getIdTurnoPestanha ());
			hashGuardia.put (ScsGuardiasTurnoBean.C_IDGUARDIA, (String) miForm.getIdGuardiaPestanha ());
			ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) (new ScsGuardiasTurnoAdm (usr).select (hashGuardia)).get (0);
			request.setAttribute ("NOMBREGUARDIA", beanGuardia.getNombre ());
			request.setAttribute("resultado",salida);			
			request.setAttribute("modo",miForm.getModo());

		} catch (Exception e) {
			throwExcp("messages.select.error",e,null);	
		}

		return "buscarInicio";
	} //buscar ()
	
	
	public static String getNumGuardias(String idInstitucion, String idTurno, String idGuardia, String idCalendario, UsrBean usr) {
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(usr);
		try {
			return "" + admGuardiasColegiado.getNumeroGuardias(idInstitucion, idTurno, idGuardia, idCalendario);
		} catch (ClsExceptions e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Abre la modal de Mantenimiento del Calendario de Guardias
	 * con el iframe del calendario correspondiente.
	 * Gestiona la creacion autom�tica y manual del calendario. 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String editar (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response)
	throws SIGAException
	{
		//Controles generales
		DefinirCalendarioGuardiaForm miForm = 
			(DefinirCalendarioGuardiaForm) formulario;
		ScsCalendarioGuardiasAdm admCalendarioGuardia = 
			new ScsCalendarioGuardiasAdm(this.getUserBean(request));
		ScsGuardiasTurnoAdm admGuardiasTurno = 
			new ScsGuardiasTurnoAdm(this.getUserBean(request));
		ScsGuardiasColegiadoAdm admGuardiasColegiado = 
			new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		UsrBean usr = null;

		//Variables
		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();

		String forward = "modalMantenimiento";
		Vector registros = new Vector();
		Vector ocultos = new Vector();
		String idcalendarioguardias="", idinstitucion="", idturno="", idguardia="";
		String tipodias="", diasguardia="", num_letrados="", num_sustitutos="", diasACobrar="";
		String fechaDesde="", fechaHasta="", observaciones="";
		String diasSeparacionGuardias="";
		String tipoDiasGuardia="";
		int numeroGuardias=0;
		
		
		
		try
		{
			//obteniendo informacion del usuario
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			if(miForm.getAccion()!=null && (miForm.getAccion().equals("consultaDesdeProgramacion")||miForm.getAccion().equals("edicionDesdeProgramacion"))){
				idcalendarioguardias = miForm.getIdCalendarioGuardias();
				idturno = miForm.getIdTurno();
				idguardia = miForm.getIdGuardia();
				idinstitucion = usr.getLocation();
				
			}else{
				//obteniendo los datos del formulario
				ocultos = miForm.getDatosTablaOcultos(0);
				idcalendarioguardias = (String)ocultos.get(0);
				idturno = (String)ocultos.get(1);
				idguardia = (String)ocultos.get(2);
				idinstitucion = (String)ocultos.get(3);
			}

			//obteniendo los datos del calendario:
			StringBuffer where = new StringBuffer();
			where.append("WHERE "+ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idinstitucion);
			where.append(" AND "+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idturno);
			where.append(" AND "+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idguardia);
			where.append(" AND "+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idcalendarioguardias);
			Vector vCalendario = admCalendarioGuardia.select(where.toString());
			ScsCalendarioGuardiasBean beanCalendario = (ScsCalendarioGuardiasBean)vCalendario.get(0); 
			fechaDesde = beanCalendario.getFechaInicio();
			fechaHasta = beanCalendario.getFechaFin();
			observaciones = beanCalendario.getObservaciones();

			//consultando los datos de la modal en la tabla SCS_GUARDIASTURNO
			registros.clear();
			registros = admGuardiasTurno.ejecutaSelect(admGuardiasTurno.
					getDatosCalendario(idinstitucion,idturno,idguardia));
			tipodias = ScsGuardiasTurnoAdm.obtenerTipoDia(
					(String)((Hashtable)registros.get(0)).get("SELECCIONLABORABLES"), 
					(String)((Hashtable)registros.get(0)).get("SELECCIONFESTIVOS"), 
					usr);
			diasguardia = (String)((Hashtable)registros.get(0)).get("DIASGUARDIA");		
			num_letrados = (String)((Hashtable)registros.get(0)).get("NUMEROLETRADOS");
			num_sustitutos = (String)((Hashtable)registros.get(0)).get("NUMEROSUSTITUTOS");
			diasACobrar = (String)((Hashtable)registros.get(0)).get("DIASACOBRAR");

			diasSeparacionGuardias = (String)((Hashtable)registros.get(0)).get("DIASSEPARACIONGUARDIAS");
			tipoDiasGuardia = (String)((Hashtable)registros.get(0)).get("TIPODIASGUARDIA");
			
			// Recuperamos el numero de guardias en el calendario para saber si tenemos que pintar el boton de generar calendario
			numeroGuardias = admGuardiasColegiado.getNumeroGuardias(idinstitucion, idturno, idguardia, idcalendarioguardias);

			//almacenando en sesion el registro para futuras modificaciones
			backupHash.put("IDCALENDARIOGUARDIAS", idcalendarioguardias);
			backupHash.put("IDINSTITUCION", idinstitucion);
			backupHash.put("IDTURNO", idturno);
			backupHash.put("IDGUARDIA", idguardia);
			backupHash.put("FECHADESDE", fechaDesde);
			backupHash.put("FECHAHASTA", fechaHasta);
			backupHash.put("OBSERVACIONES", observaciones);
			request.getSession().setAttribute("DATABACKUP",backupHash);

			//metiendo los datos en una tabla hash y en el request como DATOSINICIALES:
			miHash.put("IDCALENDARIOGUARDIAS",idcalendarioguardias);
			miHash.put("IDINSTITUCION",idinstitucion);
			miHash.put("IDTURNO",idturno);
			miHash.put("IDGUARDIA",idguardia);
			miHash.put("modo",miForm.getModo());
			miHash.put("TIPODIAS",tipodias);
			miHash.put("DIASGUARDIA",diasguardia);
			miHash.put("DIASACOBRAR",diasACobrar);
			miHash.put("NUMEROLETRADOS",num_letrados);
			miHash.put("NUMEROSUSTITUTOS",num_sustitutos);
			miHash.put("FECHADESDE", GstDate.getFormatedDateShort(usr.getLanguage(), fechaDesde));
			miHash.put("FECHAHASTA", GstDate.getFormatedDateShort(usr.getLanguage(), fechaHasta));
			miHash.put("OBSERVACIONES",observaciones);		
			miHash.put("DIASSEPARACIONGUARDIAS", diasSeparacionGuardias);
			miHash.put("TIPODIASGUARDIA", tipoDiasGuardia);
			miHash.put("NUMEROGUARDIAS", String.valueOf(numeroGuardias));

			request.setAttribute("DATOSINICIALES",miHash);

			forward = "modalMantenimiento";
		}
		catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}
		return forward;
	} //editar ()

	/**
	 * Ejecuta el editar()
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida opara la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsCabeceraGuardiasAdm cabecerasAdm = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm  permutasAdm= new ScsPermutaGuardiasAdm (this.getUserBean(request));

		String forward = "";
		Vector visibles = new Vector();
		Vector ocultos = new Vector();
		Vector temporal = new Vector();
		Vector letradoSustituido = new Vector();
		String fechaInicio="", fechaFin="", numeroColegiado="";
	
		try {
			if (miForm.getAccion()!=null && (miForm.getAccion().equals("modalReserva")||miForm.getAccion().equals("modalGuardia")||miForm.getAccion().equals("modalGuardiaVolantes")||miForm.getAccion().equals("modalConsultaCenso"))) {		

				//Datos del registro seleccionado
				visibles = miForm.getDatosTablaVisibles(0);
				ocultos = miForm.getDatosTablaOcultos(0);

				String idCalendario = "";
				String idTurno = "";
				String idGuardia = "";
				String idInstitucion = "";
				String idPersona = "";

				if(!miForm.getAccion().equals("modalGuardiaVolantes")&&!miForm.getAccion().equals("modalConsultaCenso")){
					//Recogemos los datos del formulario
					fechaInicio = (String)visibles.get(1);
					fechaFin = (String)visibles.get(2);			
					numeroColegiado = (String)visibles.get(3);

					//Almacenamos los datos necesarios en el request
					miForm.setFechaInicio(fechaInicio);
					miForm.setFechaFin(fechaFin);
					request.setAttribute("NUMEROCOLEGIADO",numeroColegiado);
					request.setAttribute("NOMBRE",(String)visibles.get(4));

					//Datos para consultar las guardias:			
					idCalendario = (String)ocultos.get(0);
					idTurno = (String)ocultos.get(1);
					idGuardia = (String)ocultos.get(2);
					idInstitucion = (String)ocultos.get(3);
					idPersona = (String)ocultos.get(5);
				}else if(miForm.getAccion().equals("modalConsultaCenso")){
					fechaInicio = (String)ocultos.get(3);
					fechaInicio=GstDate.getFormatedDateShort(this.getUserBean(request).getLanguage(),fechaInicio);
					fechaFin = (String)visibles.get(2);		
					//Almacenamos los datos necesarios en el request
					miForm.setFechaInicio(fechaInicio);
					miForm.setFechaFin(fechaFin);
					//Datos para consultar las guardias:			
					idCalendario = (String)ocultos.get(0);
					idTurno = (String)ocultos.get(1);
					idGuardia = (String)ocultos.get(2);
					idInstitucion = miForm.getIdInstitucion();
					idPersona = (String)miForm.getIdPersona();

					String sql="Select p.nombre || ' ' || p.apellidos1 || ' ' || p.apellidos2 as NOMBRE," +
					"						c.ncolegiado as NUMERO" +
					"					  from cen_persona p, cen_colegiado c" +
					"					 where p.idpersona = "+idPersona+
					"					 and p.idpersona=c.idpersona" +
					"					 and c.idinstitucion="+idInstitucion;							

					temporal=cabecerasAdm.selectGenerico(sql);
					if(temporal!=null && temporal.size()>0){
						request.setAttribute("NUMEROCOLEGIADO",(String)((Hashtable)temporal.get(0)).get("NUMERO"));
						request.setAttribute("NOMBRE",(String)((Hashtable)temporal.get(0)).get("NOMBRE"));							
					}
				}else{ //Para el caso de que vengamos desde Volantes
					//Recogemos los datos del formulario
					fechaInicio = (String)visibles.get(6);
					fechaFin = (String)visibles.get(7);			
					numeroColegiado = (String)visibles.get(3);

					//Almacenamos los datos necesarios en el request
					miForm.setFechaInicio(fechaInicio);
					miForm.setFechaFin(fechaFin);
					request.setAttribute("NUMEROCOLEGIADO",numeroColegiado);
					request.setAttribute("NOMBRE",(String)visibles.get(4));

					//Datos para consultar las guardias:			
					idCalendario = (String)ocultos.get(0);
					idTurno = (String)ocultos.get(1);
					idGuardia = (String)ocultos.get(2);
					idInstitucion = (String)ocultos.get(3);
					idPersona = (String)ocultos.get(5);
				}

				//Consulto las guardias del periodo:
				StringBuffer where = new StringBuffer();
				where.append(" WHERE "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idInstitucion);
				where.append(" AND "+ScsGuardiasColegiadoBean.C_IDTURNO +"="+idTurno);
				where.append(" AND "+ScsGuardiasColegiadoBean.C_IDGUARDIA +"="+idGuardia);
				where.append(" AND "+ScsGuardiasColegiadoBean.C_FECHAINICIO+"=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')");
				where.append(" AND "+ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idPersona);

				Vector vGuardiasLetrado = admGuardiasColegiado.select(where.toString());

				StringBuffer guardiasPeriodo = new StringBuffer();
				Iterator iter = vGuardiasLetrado.iterator();
				int i=1;
				while (iter.hasNext()) {
					ScsGuardiasColegiadoBean beanGuardiasColegiado = (ScsGuardiasColegiadoBean)iter.next();
					guardiasPeriodo.append(GstDate.getFormatedDateShort("ES",beanGuardiasColegiado.getFechaFin()));
					if (i%2==0)
						guardiasPeriodo.append("\n");
					else
						guardiasPeriodo.append(" - ");
					i++;
				}
				miForm.setGuardiasPeriodo(guardiasPeriodo.toString());
				
			
				//Datos necesarios para sacar el letrado sustituido, el letrado permutado, ademas de el letrado que sustituye.
				//Datos del Solicitante
				Hashtable solicitanteHash = new Hashtable();	
				Hashtable solicitupermutaHash = new Hashtable();
				Vector registros = new Vector();	
				Vector registroPermuta = new Vector();
				String nombresustituto="";
				String ncolegiadoSustituto="";
				String comesustitutucion="";
				String fechaSustitucion="";
				String idletradoConfir="";
				
				solicitanteHash.put("IDINSTITUCION",idInstitucion);
				solicitanteHash.put("IDTURNO",idTurno);
				solicitanteHash.put("IDGUARDIA",idGuardia);
				solicitanteHash.put("IDCALENDARIOGUARDIAS",idCalendario);
				solicitanteHash.put("IDPERSONA",idPersona);
				solicitanteHash.put("FECHAINICIO",fechaInicio);				
				registros.clear();	
				//busco los datos del solicitante
				registros = cabecerasAdm.selectGenerico(cabecerasAdm.getDatosColegiadoFormateadoNombre(solicitanteHash));
				solicitanteHash.put("NOMBREYAPELLIDOS",UtilidadesHash.getString((Hashtable)registros.get(0),"NOMBRE"));
				solicitanteHash.put("NUMEROCOLEGIADO",UtilidadesHash.getString((Hashtable)registros.get(0),CenColegiadoBean.C_NCOLEGIADO));				
				request.setAttribute("SOLICITANTE",solicitanteHash);
				String nombreSolicitante=UtilidadesHash.getString((Hashtable)registros.get(0),"NOMBRE");
				String ncolegiadoSolicitante=UtilidadesHash.getString((Hashtable)registros.get(0),CenColegiadoBean.C_NCOLEGIADO);
				String datosSolicitante = ncolegiadoSolicitante+"  "+ nombreSolicitante;
				
				if ((nombreSolicitante!=null)&&(ncolegiadoSolicitante!=null)){
						miForm.setDatosPersSolicitante(datosSolicitante);				
				 }else 
					miForm.setDatosPersSolicitante(" ");				
				
				//letrado sustituido
				registros = cabecerasAdm.selectGenerico(cabecerasAdm.getDatosSustituto(solicitanteHash));
				if(registros!=null && registros.size()>0){
					if((String)((Hashtable)registros.get(0)).get("LETRADOSUSTITUIDO")!=null && !((String)((Hashtable)registros.get(0)).get("LETRADOSUSTITUIDO")).equals("")){					
						String nombresustitucion=UtilidadesHash.getString((Hashtable)registros.get(0),"LETRADOSUSTITUIDO");						                       
						 letradoSustituido= cabecerasAdm.selectGenerico(cabecerasAdm.getnombresustituto(nombresustitucion, idInstitucion));						
						if(letradoSustituido!=null && letradoSustituido.size()>0){
							nombresustituto=(String)(((Hashtable)(letradoSustituido.get(0))).get("NOMBRE"));
							ncolegiadoSustituto=(String)(((Hashtable)(letradoSustituido.get(0))).get("NCOLEGIADOSUSTITUTO"));
							request.setAttribute("LETRADOSUSTITUIDO",nombresustituto);							
						}else{
							request.setAttribute("LETRADOSUSTITUIDO","-");
						}
					}else{
						request.setAttribute("LETRADOSUSTITUIDO","-");
					}
					request.setAttribute("COMENTARIO",(String)((Hashtable)registros.get(0)).get("COMENSUSTITUCION"));
					request.setAttribute("FECHASUSTITUCION",GstDate.getFormatedDateShort(this.getLenguaje(request),(String)((Hashtable)registros.get(0)).get("FECHASUSTITUCION")));				
					comesustitutucion=UtilidadesHash.getString((Hashtable)registros.get(0),"COMENSUSTITUCION");
					fechaSustitucion=UtilidadesHash.getString((Hashtable)registros.get(0),"FECHASUSTITUCION");
					fechaSustitucion=GstDate.getFormatedDateShort(this.getLenguaje(request),fechaSustitucion);		
					
					//Informacion de la Anulacion 
					if (miForm.getAccion()!=null && (miForm.getAccion().equals("modalConsultaCenso") || miForm.getAccion().equals("modalGuardia"))) {
						String sMotivosAnulacion = UtilidadesHash.getString((Hashtable)registros.get(0), "OBSERVACIONESANULACION");
						miForm.setComenAnulacion(sMotivosAnulacion);
					}					
				}				
				
				//Aqui guardamos en nuestro formulario los datos que necesitamos que salga en nuestra jsp.	
				//Ademas verificamos si es una PERMUTA o si es una sustituci�n
				if (comesustitutucion.equals("Permuta")){
					miForm.setDatosPersSustituto(" ");
					miForm.setComentarioSustituto(" ");
					miForm.setFechaSustituto("-");					
				}else{
					String datosSustituto="";
					datosSustituto+=ncolegiadoSustituto+"  "+nombresustituto;
					miForm.setDatosPersSustituto(datosSustituto);										
					miForm.setComentarioSustituto(comesustitutucion);
					miForm.setFechaSustituto(fechaSustitucion);
				}
				
				//informacion de letrado sustituido.				
				Hashtable<String, Object> htPermutas = permutasAdm.buscarPermutaConfirmadorSolicitante(idInstitucion,idTurno,idPersona,idGuardia, idCalendario, fechaInicio);				
				ScsPermutaGuardiasBean permutaBean = (ScsPermutaGuardiasBean) permutasAdm.hashTableToBean(htPermutas);
				miForm.setPermutaGuardias(permutaBean.getPermutaGuardiasForm());
				//Datos del formulario
				String comunitario = UtilidadesHash.getString(htPermutas,"COMUNITARIO");	
				String nombrePermutado = UtilidadesHash.getString(htPermutas,"NOMBRECONFIRMADOR");				
				String motivoConfirmador = UtilidadesHash.getString(htPermutas,"MOTIVOSCONFIRMADOR");
				String numeroConfirmador = UtilidadesHash.getString(htPermutas,"NUMEROCONFIRMADOR");
				String fechaInicioConfirmador=UtilidadesHash.getString(htPermutas,"FECHAINICIO_CONFIRMADOR");
				String fechaConfirmacion=UtilidadesHash.getString(htPermutas,"FECHACONFIRMACION");
				String fechaSolicitud=UtilidadesHash.getString(htPermutas,"FECHASOLICITUD");
				fechaInicioConfirmador = GstDate.getFormatedDateShort(this.getLenguaje(request),fechaInicioConfirmador); 
				fechaConfirmacion=GstDate.getFormatedDateShort(this.getLenguaje(request),fechaConfirmacion);
				fechaSolicitud=GstDate.getFormatedDateShort(this.getLenguaje(request),fechaSolicitud);
				idletradoConfir=UtilidadesHash.getString(htPermutas,"IDPERSONA");
				//IDconfirmador, se necesita para saber el rango que tiene permutado.
				if (comunitario!=null){					
					solicitupermutaHash.put("IDINSTITUCION",idInstitucion);
					solicitupermutaHash.put("IDTURNO",idTurno);
					solicitupermutaHash.put("IDGUARDIA",idGuardia);
					solicitupermutaHash.put("IDCALENDARIOGUARDIAS",idCalendario);
					solicitupermutaHash.put("IDPERSONA",idletradoConfir);
					solicitupermutaHash.put("FECHAINICIO",fechaInicioConfirmador);				
					registroPermuta.clear();				
					registroPermuta= cabecerasAdm.selectGenerico(cabecerasAdm.getRangopermutas(solicitupermutaHash));					
					if (registroPermuta.size()>0){
						String fechainiciopermuta= GstDate.getFormatedDateShort(this.getLenguaje(request),UtilidadesHash.getString((Hashtable)registroPermuta.get(0),"FECHAINICIO"));
						String fechafinpermuta= GstDate.getFormatedDateShort(this.getLenguaje(request),UtilidadesHash.getString((Hashtable)registroPermuta.get(0),"FECHA_FIN"));				
						String fechas=fechainiciopermuta+" - " + fechafinpermuta;				
						miForm.setPeriodoPermuta(fechas);
					}else 
						miForm.setPeriodoPermuta(" ");					
				}else
					miForm.setPeriodoPermuta(" ");
				
				if ((fechaInicio!=null)&&(fechaFin!=null)){
					String fechaguardia=fechaInicio+" - " +fechaFin;				
					miForm.setPeriodos(fechaguardia);	
				}else 
					miForm.setPeriodos(" ");
				
				if (fechaSolicitud!=""){
					miForm.setFechaSolicitud(fechaSolicitud);
				}else
					miForm.setFechaSolicitud("-");
				
				if (fechaInicioConfirmador!=""){
					miForm.setFechaConfirmacion(fechaConfirmacion);
				}else
					miForm.setFechaConfirmacion("");
				
				
				if ((numeroConfirmador!=null)&&(nombrePermutado!=null)){
					String datosConfirmador="";
					       datosConfirmador+=numeroConfirmador+"  "+ nombrePermutado;
					miForm.setDatosPersPermutada(datosConfirmador);
				}else 
					miForm.setDatosPersPermutada(" ");
				
				if (motivoConfirmador!=null){
					miForm.setMotivosSolicitante(motivoConfirmador);
				}else 
            	   miForm.setMotivosSolicitante(" ");				
			
				request.setAttribute("permutaGuardias", permutaBean);				
			
				forward = "modalConsulta";
			} else
				forward = this.editar(mapping,formulario,request,response);
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null);	
		}			
		return forward;
	}


	/**
	 * Inserta un Calendario desde una modal al pulsar: guardar y cerrar en la pantalla modal de insertar un calendario nuevo.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	private String insertarCalendario(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Controles
		UsrBean usr = (UsrBean) this.getUserBean(request);
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		UserTransaction tx = null;
		String texto;
		CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
		texto = "messages.inserted.error";
		try {

			// INSERT (INICIO TRANSACCION)
			tx = usr.getTransaction();
			tx.begin();
			if (calendarioSJCS.crearCalendario(new Integer(miForm.getIdInstitucionPestanha()),new Integer( miForm.getIdTurnoPestanha()), 
					new Integer(miForm.getIdGuardiaPestanha()), GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaDesde()), GstDate.getApplicationFormatDate(usr.getLanguage(), miForm.getFechaHasta()), 
					miForm.getObservaciones(), null, null, null, usr) > 0)
				texto = "messages.inserted.success";
			tx.commit();

			request.setAttribute("modo", miForm.getModo());
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}
		return exitoModal(texto, request);
	}
	
	
	/**
	 * Guarda los datos de un Calendario de Guardias en Base de Datos.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(this.getUserBean(request));

		Hashtable miHash = new Hashtable();
		Hashtable backupHash = new Hashtable();
		UsrBean usr = null;
		String idcalendarioguardias = "", idinstitucion="", idguardia="", idturno="";
		UserTransaction tx = null;
		Vector registros = new Vector();
		String mensaje="";

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx=usr.getTransaction();				
			//Valores obtenidos de la pestanha
			idinstitucion = miForm.getIdInstitucion();
			idturno = miForm.getIdTurno();
			idguardia = miForm.getIdGuardia();
			idcalendarioguardias = miForm.getIdCalendarioGuardias();

			backupHash = (Hashtable)request.getSession().getAttribute("DATABACKUP");

			miHash.put(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS,idcalendarioguardias);
			miHash.put(ScsCalendarioGuardiasBean.C_IDINSTITUCION,idinstitucion);
			miHash.put(ScsCalendarioGuardiasBean.C_IDGUARDIA,idguardia);
			miHash.put(ScsCalendarioGuardiasBean.C_IDTURNO,idturno);
			//Obtengo las fechas, la convierto a formato BD y la inserto en la hash
			miHash.put(ScsCalendarioGuardiasBean.C_FECHAFIN,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaHasta()));
			miHash.put(ScsCalendarioGuardiasBean.C_FECHAINICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaDesde()));
			miHash.put(ScsCalendarioGuardiasBean.C_OBSERVACIONES,miForm.getObservaciones());
			miHash.put(ScsCalendarioGuardiasBean.C_USUMODIFICACION,usr.getUserName());
			miHash.put(ScsCalendarioGuardiasBean.C_FECHAMODIFICACION,"sysdate");

			//INSERT (INICIO TRANSACCION)
			tx=usr.getTransaction();
			tx.begin();                 
			if (admCalendarioGuardia.update(miHash,backupHash))  
				mensaje = "messages.updated.success";
			else 
				mensaje = "messages.updated.error";				
			tx.commit();

			//Almaceno en sesion el registro para futuras modificaciones
			registros.clear();
			registros = admCalendarioGuardia.selectGenerico(admCalendarioGuardia.getDatosCalendario(idinstitucion,idturno,idguardia,idcalendarioguardias));
			backupHash.put("IDCALENDARIOGUARDIAS",(String)((Hashtable)registros.get(0)).get("IDCALENDARIOGUARDIAS"));
			backupHash.put("IDINSTITUCION",(String)((Hashtable)registros.get(0)).get("IDINSTITUCION"));
			backupHash.put("IDTURNO",(String)((Hashtable)registros.get(0)).get("IDTURNO"));
			backupHash.put("IDGUARDIA",(String)((Hashtable)registros.get(0)).get("IDGUARDIA"));
			backupHash.put("FECHAINICIO",(String)((Hashtable)registros.get(0)).get("FECHAINICIO"));
			backupHash.put("FECHAFIN",(String)((Hashtable)registros.get(0)).get("FECHAFIN"));
			backupHash.put("OBSERVACIONES",(String)((Hashtable)registros.get(0)).get("OBSERVACIONES"));
			request.getSession().setAttribute("DATABACKUP",backupHash);	            

			request.setAttribute("modo",miForm.getModo());
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}				
		return exitoModal(mensaje,request);
	}		

	/**
	 * Contempla 2 casos de borrado:
	 * - Caso 1: en la pantalla modal de Mantenimiento, en la parte del listado de Colegiados de Guardias o de <br>
	 * Reservas borra una guardia del Calendario.
	 * - Caso 2: en la pantalla inicial de listado de calendarios borra un Calendario.  
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = this.getUserBean(request);
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;

		ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(usr);
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(usr);
		ScsPermutaGuardiasAdm admPermutaGuardias = new ScsPermutaGuardiasAdm(usr);
		ScsPermutaCabeceraAdm admPermutasCabeceras = new ScsPermutaCabeceraAdm(usr);
		ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(usr);

		String forward = "exito"; //retorno
		UserTransaction tx = null;
		boolean borradosOK = false;

		try {
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			
			if (miForm.getAccion()!=null && miForm.getAccion().equalsIgnoreCase("modalGuardia")) {
				//
				//Caso1: modal Mantenimiento, parte del listado de Colegiados de Guardias
				//Se borran la guardia de este calendario y sus permutas.
				//

				//obteniendo los datos del formulario
				String idCalendarioGuardias = (String)ocultos.get(0);
				String idTurno = (String)ocultos.get(1);
				String idGuardia = (String)ocultos.get(2);
				String idInstitucion = (String)ocultos.get(3);
				String idPersona = (String)ocultos.get(5);
				String fechaInicio = GstDate.getFormatedDateShort(usr.getLanguage(), (String)ocultos.get(6));
				String numero = ((String)ocultos.get(7)).trim();
				String fechaFin = GstDate.getFormatedDateShort(usr.getLanguage(), (String)ocultos.get(8));
				String fechaInicioPermuta = GstDate.getFormatedDateShort(usr.getLanguage(), (String)ocultos.get(9));
				String fechaFinPermuta = GstDate.getFormatedDateShort(usr.getLanguage(), (String)ocultos.get(10));
				
				/*
				 * Si la fecha final es igual que la fecha final de la permuta, entonces esta en alguno de los siguientes estados:
				 * - Pendiente de realizar
				 * - Permutada
				 * ... Pero no est� en Pendiente de Confirmar
				 * 
				 * Entonces tomamos como fecha inicial, la inicial de la permuta, ya que fechaInicio es la fecha original.
				 */
				if (fechaFin.equals(fechaFinPermuta)) {
					fechaInicio = fechaInicioPermuta;
				}
				
				//generando el hash para tratar la guardia del calendario
				Hashtable miHash = new Hashtable();
				miHash.clear();
				miHash.put(ScsGuardiasColegiadoBean.C_IDTURNO, idTurno);
				miHash.put(ScsGuardiasColegiadoBean.C_IDGUARDIA, idGuardia);
				miHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION, idInstitucion);				
				miHash.put(ScsGuardiasColegiadoBean.C_IDPERSONA, idPersona);
				miHash.put(ScsGuardiasColegiadoBean.C_FECHAINICIO, fechaInicio);
				miHash.put(ScsGuardiasColegiadoBean.C_FECHAFIN, fechaFin);

				//generando el hash para tratar la permuta
				Hashtable hashPermuta = new Hashtable();
				hashPermuta.clear();
				hashPermuta.put(ScsPermutaGuardiasBean.C_IDINSTITUCION, idInstitucion);
				hashPermuta.put(ScsPermutaGuardiasBean.C_NUMERO, numero);

				//comprobando que no hay ninguna guardia realizada
				if (admGuardiasColegiado.validarBorradoGuardia(idInstitucion, idTurno, idGuardia, fechaInicio, fechaFin)) {
					//empezando transaccion
					tx = usr.getTransaction();
					tx.begin();
					
					borradosOK = false;
					
					if (!numero.equals("NINGUNO")) {
						//borrando permutas
						if (admPermutaGuardias.delete(hashPermuta))
							// revisando SCS_PERMUTA_CABECERA 
							if (admPermutasCabeceras.revisarPermutasCalendario(idInstitucion)) 
								//borrando los dias de la guardia
								if (admGuardiasColegiado.deleteGuardiaCalendario(miHash))
									//borrando las cabeceras de la guardia
									if (admCabeceraGuardias.deleteCabeceraGuardias(miHash))
										borradosOK = true;
					} else {
						//borrando los dias de la guardia
						if (admGuardiasColegiado.deleteGuardiaCalendario(miHash))
							//borrando las cabeceras de la guardia
							if (admCabeceraGuardias.deleteCabeceraGuardias(miHash))
								borradosOK = true;
					}

					//devolviendo Exito o Fracaso
					if (borradosOK) {
						tx.commit();
						forward = exitoRefresco("messages.deleted.success", request);
					} else {
						tx.rollback();
						forward = exito("error.messages.deleted", request);
					}
				} else
					forward = exito("error.messagess.borrarGuardiaCalendario", request);
			}
			else {
				
				//
				//Caso2: pantalla inicial de listado de calendarios
				//Hay que borrar el calendario completo eliminando previamente todas sus guardias
				//
				
				
				
				//obteniendo valores del formulario
				String idInstitucion = "";
				String idTurno = "";
				String idGuardia = "";
				String idCalendarioGuardias = "";
				
				if(miForm.getAccion()!=null && miForm.getAccion().equals("borrarDesdeProgramacion")){
					idCalendarioGuardias = miForm.getIdCalendarioGuardias();
					idTurno = miForm.getIdTurno();
					idGuardia = miForm.getIdGuardia();
					idInstitucion = usr.getLocation();
				}else{
					idInstitucion = (String)ocultos.get(3);
					idTurno = (String)ocultos.get(1);
					idGuardia = (String)ocultos.get(2);
					idCalendarioGuardias = (String)ocultos.get(0);
				}
				CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
				calendarioSJCS.inicializaParaBorrarCalendarios(new Integer(idInstitucion),new Integer( idTurno),new Integer( idGuardia),new Integer( idCalendarioGuardias), usr);
				try {
					calendarioSJCS.borrarCalendario();
					forward = exitoRefresco("messages.deleted.success", request);
				} catch (SIGAException e) {
					forward = exito(e.getLiteral(), request);
				}
				
			}
		} catch (Exception e){
			throwExcp("error.messages.deleted",e,tx);
		}
		
		return forward;
	} //borrar()
	

	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsCabeceraGuardiasAdm admCabeceraGuardia = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
		ScsGuardiasColegiadoAdm admGuardiaColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutaguardias = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		FcsFactApunteAdm admApuntes = new FcsFactApunteAdm(this.getUserBean(request));


		Hashtable miHash = new Hashtable();
		UsrBean usr = null;
		String forward = "error";
		String idcalendarioguardias="", idinstitucion="", idturno="", idguardia="";
		String idpersona = "", fechaInicio="",  fechaPermuta="", numeroColegiado="";
		String observaciones="";//,nombre=""nombre=""; 
		String  fechaDesde="", fechaHasta="", diasguardia="", diasacobrar="", tipodias="";
		String fechaInicioPermuta="", fechaFinPermuta="";
		//Vector v_resultado = new Vector ();
		Vector resultado = new Vector();
		Vector v_guardias = new Vector ();
		String numero="", modoOriginal="";
		String buscarFechaDesde="";
		String buscarFechaHasta="";
		String buscarNcolegiado="";
		String pl = ""; //Valor devuelto por el PL de Permutas
		String orden = "";
		HelperInformesAdm helperInformes = new HelperInformesAdm();

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");			

			//Datos necesarios para la consulta
			idinstitucion = miForm.getIdInstitucion();
			idturno = miForm.getIdTurno();
			idguardia = miForm.getIdGuardia();
			idcalendarioguardias = miForm.getIdCalendarioGuardias();
			fechaDesde = miForm.getFechaDesde();
			fechaHasta = miForm.getFechaHasta();
			diasguardia = miForm.getDiasGuardia();
			diasacobrar = miForm.getDiasACobrar();
			tipodias = miForm.getTipoDias();
			modoOriginal = miForm.getModoOriginal();
			buscarFechaDesde=miForm.getBuscarFechaDesde();
			buscarFechaHasta=miForm.getBuscarFechaHasta();
			buscarNcolegiado=miForm.getBuscarColegiado();
			miHash.put("IDINSTITUCION",idinstitucion);
			miHash.put("IDTURNO",idturno);
			miHash.put("IDGUARDIA",idguardia);
			miHash.put("IDCALENDARIOGUARDIAS",idcalendarioguardias);

			if ((buscarFechaDesde==null || buscarFechaDesde.trim().equals(""))&&(buscarFechaHasta==null || buscarFechaHasta.trim().equals(""))&& (buscarNcolegiado==null || buscarNcolegiado.trim().equals("")) ){
				//Busqueda de colegiados. Obtengo el nombre, numero de colegiado, observaciones y las fechas de inicio y fin
				RowsContainer rc = admGuardiaColegiado.findNLS(admCabeceraGuardia.buscarColegiados(miHash));

				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();

					String nombre = (String)registro.get(CenPersonaBean.C_NOMBRE);
					String fechaInicioPK = (String)registro.get("FECHA_INICIO_PK");
					String fechaFin = (String)registro.get(ScsCabeceraGuardiasBean.C_FECHA_FIN);
					idturno = (String)registro.get(ScsCabeceraGuardiasBean.C_IDTURNO);
					idguardia = (String)registro.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
					idpersona = (String)registro.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
					String idCalendarioGuardias = (String)registro.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
					orden = (String)registro.get(ScsCabeceraGuardiasBean.C_POSICION);
					String fechaInicioPKBind = GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPK);
					
					//Se a�aden los campos de validacion
					String validado = (String)registro.get(ScsCabeceraGuardiasBean.C_VALIDADO);

					Hashtable htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idturno);
					htCodigo.put(new Integer(3), idguardia);
					htCodigo.put(new Integer(4), idCalendarioGuardias);
					htCodigo.put(new Integer(5), idpersona);
					htCodigo.put(new Integer(6), fechaInicioPKBind);

					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_TIENE_ACTS_VALIDADAS", "ACT_VALIDADAS"));
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_ES_MODIFICABLE_GUARDIAS", "ESMODIFICABLE"));

					htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idturno);
					htCodigo.put(new Integer(3), idguardia);
					htCodigo.put(new Integer(4), idpersona);
					htCodigo.put(new Integer(5), fechaInicioPKBind);
					htCodigo.put(new Integer(6), idCalendarioGuardias);

					//FECHAINICIOPERMUTA
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAINISOLICITANTE", "FECHAINICIOPERMUTA"));
					String fInicioPermuta = (String)registro.get("FECHAINICIOPERMUTA");
					//Si la fecha de inicio del solicitante es nula miramos  la fecha de inicio del confirmador
					if(fInicioPermuta==null||fInicioPermuta.trim().equals("")){
						helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAINICONFIRMADOR", "FECHAINICIOPERMUTA"));
						fInicioPermuta = (String)registro.get("FECHAINICIOPERMUTA");
						//Si la fecha de inicio del confirmador es nula ponemos como fecha de inicio de la permuta 
						//la fecha de inicio real
						if(fInicioPermuta==null||fInicioPermuta.trim().equals("")){
							fInicioPermuta = fechaInicioPK;
							registro.put("FECHAINICIOPERMUTA", fInicioPermuta);
						}

					}
					//FECHAFINPERMUTA
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAFINSOLICITANTE", "FECHAFINPERMUTA"));
					String fFinPermuta = (String)registro.get("FECHAFINPERMUTA");
					//Si la fecha de fin del solicitante es nula miramos  la fecha de fin del confirmador
					if(fFinPermuta==null||fFinPermuta.trim().equals("")){
						helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAFINCONFIRMADOR", "FECHAFINPERMUTA"));
						fFinPermuta = (String)registro.get("FECHAFINPERMUTA");
						//Si la fecha de fin del confirmador es nula ponemos como fecha de fin de la permuta 
						//la fecha de fin real
						if(fFinPermuta==null||fFinPermuta.trim().equals("")){
							fFinPermuta = fechaFin;
							registro.put("FECHAFINPERMUTA", fFinPermuta);
						}

					}

					//FECHAINICIO
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAORIGSOLICITANTE", "FECHAINICIO"));
					String fInicio = (String)registro.get("FECHAINICIO");
					//Si la fecha de origen del solicitante es nula miramos  la fecha de origen del confirmador
					if(fInicio==null||fInicio.trim().equals("")){
						helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAORIGCONFIRMADOR", "FECHAINICIO"));
						fInicio = (String)registro.get("FECHAINICIO");
						//Si la fecha de origen del confirmador es nula ponemos como fecha de origen de la permuta 
						//la fecha de origen real
						if(fInicio==null||fInicio.trim().equals("")){
							fInicio = fechaInicioPK;
							registro.put("FECHAINICIO", fInicio);
						}

					}
					//NUMEROPERMUTA 
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_NUMERO", "NUMEROPERMUTA"));



					htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idpersona);

					//F_SIGA_CALCULONCOLEGIADO(coleg.IDINSTITUCION, coleg.IDPERSONA) as NCOLEGIADO
					//NCOLEGIADO
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_CALCULONCOLEGIADO", "NCOLEGIADO"));

					numeroColegiado = (String)registro.get(CenColegiadoBean.C_NCOLEGIADO);

					String numeroPermuta = (String)registro.get("NUMEROPERMUTA");
					if (numeroPermuta==null || numeroPermuta.equals("")){
						numeroPermuta="NINGUNO";
					}




					//Chequeo si existe una permuta de esa persona como solicitante o como confirmador
					//Consulta como solicitante

					//Ejecuto el PL de Permutas que me dice el tipo de Permuta posible:				
					pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio));
					if (pl.equals("5")){//si buscando por la fecha de inicio (para el caso en el que todavia no se haya confirmado la solicitud de la permuta) devuelve el valor "5" (pendiente de permutar) volvemos a ejecutar
						// el procedimiento pasando la fecha de inicio de permuta (para el caso en el que ya se haya 
						// confirmado la permuta) por si sigue devolviendo "5" o devuelve otro
						// valor como "3" (guardia permutada), como "2" (Permuta solicitada) o "4" (Pendiente de confirmar).

						pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fInicioPermuta));
					} 

					
					String guardiaFacturada = admApuntes.exiteApunteGuardia(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio))?"true":"false";

					//Inserto los datos a visualizar en el JSP
					Hashtable nueva = new Hashtable();
					nueva.put("FECHAINICIO",fInicio);
					nueva.put("FECHA_INICIO_PK",fechaInicioPK);
					nueva.put("FECHAFIN",fechaFin);			
					nueva.put("FECHAINICIOPERMUTA",fInicioPermuta);
					nueva.put("FECHAFINPERMUTA",fFinPermuta);
					/*nueva.put("FECHAINICIO",fechaInicio);
					nueva.put("FECHAFIN",fechaFin);			
					nueva.put("FECHAINICIOPERMUTA",fechaInicioPermuta);
					nueva.put("FECHAFINPERMUTA",fechaFinPermuta);				
					nueva.put("FECHAPERMUTA",fechaPermuta);*/
					nueva.put("NUMEROPERMUTA",numeroPermuta);
					nueva.put("NUMEROCOLEGIADO",numeroColegiado);
					nueva.put("NOMBRE",nombre);
					nueva.put("IDPERSONA",idpersona);
					nueva.put("IDINSTITUCION",idinstitucion);
					nueva.put("IDTURNO",idturno);
					nueva.put("IDGUARDIA",idguardia);
					nueva.put("IDCALENDARIOGUARDIAS",idcalendarioguardias);
					nueva.put("OBSERVACIONES",observaciones);
					nueva.put("PL",pl);
					nueva.put("GUARDIAFACTURADA",guardiaFacturada);
					nueva.put("ORDEN", orden);
					nueva.put("VALIDADO", validado);
				
					ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
					if (admGuardiasColegiado.validarBorradoGuardia(idinstitucion,idturno,idguardia,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio),GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin))){
						nueva.put("PINTARBOTONBORRAR", "1");

					}else{
						nueva.put("PINTARBOTONBORRAR", "0");
					}

					resultado.add(nueva);	
				}//Fin del while
			}else{//con criterios de b�squeda
				if (buscarNcolegiado!=null && !buscarNcolegiado.trim().equals("")){
					miHash.put("NUMCOLEGIADO",buscarNcolegiado);	
				}
				if (buscarFechaDesde!=null && !buscarFechaDesde.trim().equals("")){
					UtilidadesHash.set(miHash, "FECHA_INICIO", buscarFechaDesde);
				}
				if (buscarFechaHasta!=null && !buscarFechaHasta.trim().equals("")){
					UtilidadesHash.set(miHash, "FECHA_FIN", buscarFechaHasta);
				}



				RowsContainer rc = admGuardiaColegiado.findNLS(admCabeceraGuardia.buscarColegiados(miHash));

				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					String nombre = (String)registro.get(CenPersonaBean.C_NOMBRE);

					String fechaInicioPK = (String)registro.get("FECHA_INICIO_PK");
					String fechaFin = (String)registro.get(ScsCabeceraGuardiasBean.C_FECHA_FIN);
					idturno = (String)registro.get(ScsCabeceraGuardiasBean.C_IDTURNO);
					idguardia = (String)registro.get(ScsCabeceraGuardiasBean.C_IDGUARDIA);
					idpersona = (String)registro.get(ScsCabeceraGuardiasBean.C_IDPERSONA);
					String idCalendarioGuardias = (String)registro.get(ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
					orden = (String)registro.get(ScsCabeceraGuardiasBean.C_POSICION);

					String fechaInicioPKBind = GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPK); 
					
					//Se a�aden los campos de validacion
					String validado = (String)registro.get(ScsCabeceraGuardiasBean.C_VALIDADO);

					Hashtable htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idturno);
					htCodigo.put(new Integer(3), idguardia);
					htCodigo.put(new Integer(4), idCalendarioGuardias);
					htCodigo.put(new Integer(5), idpersona);
					htCodigo.put(new Integer(6), fechaInicioPKBind);

					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_TIENE_ACTS_VALIDADAS", "ACT_VALIDADAS"));
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_ES_MODIFICABLE_GUARDIAS", "ESMODIFICABLE"));

					htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idturno);
					htCodigo.put(new Integer(3), idguardia);
					htCodigo.put(new Integer(4), idpersona);
					htCodigo.put(new Integer(5), fechaInicioPKBind);
					htCodigo.put(new Integer(6), idCalendarioGuardias);

					//FECHAINICIOPERMUTA
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAINISOLICITANTE", "FECHAINICIOPERMUTA"));
					String fInicioPermuta = (String)registro.get("FECHAINICIOPERMUTA");
					//Si la fecha de inicio del solicitante es nula miramos  la fecha de inicio del confirmador
					if(fInicioPermuta==null||fInicioPermuta.trim().equals("")){
						helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAINICONFIRMADOR", "FECHAINICIOPERMUTA"));
						fInicioPermuta = (String)registro.get("FECHAINICIOPERMUTA");
						//Si la fecha de inicio del confirmador es nula ponemos como fecha de inicio de la permuta 
						//la fecha de inicio real
						if(fInicioPermuta==null||fInicioPermuta.trim().equals("")){
							fInicioPermuta = fechaInicioPK;
							registro.put("FECHAINICIOPERMUTA", fInicioPermuta);
						}

					}
					//FECHAFINPERMUTA
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAFINSOLICITANTE", "FECHAFINPERMUTA"));
					String fFinPermuta = (String)registro.get("FECHAFINPERMUTA");
					//Si la fecha de fin del solicitante es nula miramos  la fecha de fin del confirmador
					if(fFinPermuta==null||fFinPermuta.trim().equals("")){
						helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAFINCONFIRMADOR", "FECHAFINPERMUTA"));
						fFinPermuta = (String)registro.get("FECHAFINPERMUTA");
						//Si la fecha de fin del confirmador es nula ponemos como fecha de fin de la permuta 
						//la fecha de fin real
						if(fFinPermuta==null||fFinPermuta.trim().equals("")){
							fFinPermuta = fechaFin;
							registro.put("FECHAFINPERMUTA", fFinPermuta);
						}

					}

					//FECHAINICIO
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_FECHAORIGSOLICITANTE", "FECHAINICIO"));
					String fInicio = (String)registro.get("FECHAINICIO");
					//Si la fecha de origen del solicitante es nula miramos  la fecha de origen del confirmador
					if(fInicio==null||fInicio.trim().equals("")){
						helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
								htCodigo, "F_SIGA_FECHAORIGCONFIRMADOR", "FECHAINICIO"));
						fInicio = (String)registro.get("FECHAINICIO");
						//Si la fecha de origen del confirmador es nula ponemos como fecha de origen de la permuta 
						//la fecha de origen real
						if(fInicio==null||fInicio.trim().equals("")){
							fInicio = fechaInicioPK;
							registro.put("FECHAINICIO", fInicio);
						}

					}
					//NUMEROPERMUTA 
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_NUMERO", "NUMEROPERMUTA"));



					htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idpersona);

					//F_SIGA_CALCULONCOLEGIADO(coleg.IDINSTITUCION, coleg.IDPERSONA) as NCOLEGIADO
					//NCOLEGIADO
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
							htCodigo, "F_SIGA_CALCULONCOLEGIADO", "NCOLEGIADO"));

					numeroColegiado = (String)registro.get(CenColegiadoBean.C_NCOLEGIADO);

					String numeroPermuta = (String)registro.get("NUMEROPERMUTA");
					if (numeroPermuta==null || numeroPermuta.equals("")){
						numeroPermuta="NINGUNO";
					}




					//Chequeo si existe una permuta de esa persona como solicitante o como confirmador
					//Consulta como solicitante

					//Ejecuto el PL de Permutas que me dice el tipo de Permuta posible:				
					pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio));
					if (pl.equals("5")){//si buscando por la fecha de inicio (para el caso en el que todavia no se haya confirmado la solicitud de la permuta) devuelve el valor "5" (pendiente de permutar) volvemos a ejecutar
						// el procedimiento pasando la fecha de inicio de permuta (para el caso en el que ya se haya 
						// confirmado la permuta) por si sigue devolviendo "5" o devuelve otro
						// valor como "3" (guardia permutada), como "2" (Permuta solicitada) o "4" (Pendiente de confirmar).

						pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fInicioPermuta));
					} 

					//Inserto los datos a visualizar en el JSP
					Hashtable nueva = new Hashtable();
					nueva.put("FECHAINICIO",fInicio);
					nueva.put("FECHA_INICIO_PK",fechaInicioPK);
					nueva.put("FECHAFIN",fechaFin);			
					nueva.put("FECHAINICIOPERMUTA",fInicioPermuta);
					nueva.put("FECHAFINPERMUTA",fFinPermuta);



					/*nueva.put("FECHAINICIO",fechaInicio);
					nueva.put("FECHAFIN",fechaFin);			
					nueva.put("FECHAINICIOPERMUTA",fechaInicioPermuta);
					nueva.put("FECHAFINPERMUTA",fechaFinPermuta);				
					nueva.put("FECHAPERMUTA",fechaPermuta);*/
					nueva.put("NUMEROPERMUTA",numeroPermuta);
					nueva.put("NUMEROCOLEGIADO",numeroColegiado);
					nueva.put("NOMBRE",nombre);
					nueva.put("IDPERSONA",idpersona);
					nueva.put("IDINSTITUCION",idinstitucion);
					nueva.put("IDTURNO",idturno);
					nueva.put("IDGUARDIA",idguardia);
					nueva.put("IDCALENDARIOGUARDIAS",idcalendarioguardias);
					nueva.put("OBSERVACIONES",observaciones);
					nueva.put("PL",pl);
					nueva.put("ORDEN", orden);
					nueva.put("VALIDADO", validado);

					ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
					if (admGuardiasColegiado.validarBorradoGuardia(idinstitucion,idturno,idguardia,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio),GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin))){
						nueva.put("PINTARBOTONBORRAR", "1");

					}else{
						nueva.put("PINTARBOTONBORRAR", "0");
					}

					resultado.add(nueva);	
				}//Fin del while
			}

			//Busco los colegiado de guardias
			forward = "buscarModalGuardia";

			request.setAttribute("resultado",resultado);
			request.setAttribute("MODOORIGINAL",modoOriginal);
			request.setAttribute("OBSERVACIONES",observaciones);
			request.setAttribute("FECHADESDE",fechaDesde);
			request.setAttribute("FECHAHASTA",fechaHasta);
			request.setAttribute("IDINSTITUCION",idinstitucion);
			request.setAttribute("IDTURNO",idturno);
			request.setAttribute("IDGUARDIA",idguardia);
			request.setAttribute("IDCALENDARIOGUARDIAS",idcalendarioguardias);
			request.setAttribute("DIASGUARDIA",diasguardia);
			request.setAttribute("DIASACOBRAR",diasacobrar);
			request.setAttribute("TIPODIAS",tipodias);
			request.setAttribute("modo",miForm.getModo());
			request.setAttribute("NUMEROLETRADOS",miForm.getNumeroLetrados());
			request.setAttribute("NUMEROSUSTITUTOS",miForm.getNumeroSustitutos());
			if (buscarNcolegiado!=null && !buscarNcolegiado.trim().equals("") && resultado.size()>0){
				request.setAttribute("TIENE_COLEGIADO","1");	
			}else{
				request.setAttribute("TIENE_COLEGIADO","0");
			}
		} 
		catch (Exception e){
			throwExcp("messages.select.error",e,null);			
		}

		return forward;
	}


	/**
	 * Modal de consulta de datos de un registro de la tabla de colegiados de guardias o de reservas
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;

		try {
			miForm.setIdInstitucionPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDINSTITUCION));
			miForm.setIdTurnoPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDTURNO));
			miForm.setIdGuardiaPestanha(request.getParameter(ScsGuardiasTurnoBean.C_IDGUARDIA));
			//Paso el modo de la pestanha:
			request.setAttribute("MODOPESTANA", request.getParameter("MODOPESTANA"));
		} catch (Exception e) {
			throwExcp("error.messages.editar",e,null);
		}
		return "inicio";	


	}	

	/**
	 * Modal de consulta de datos de un registro de la tabla de colegiados de guardias o de reservas
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));

		Vector visibles = new Vector();
		Vector ocultos = new Vector();
		String fechaInicio="", fechaFin="", numeroColegiado="";

		try {
			//Datos del registro seleccionado
			visibles = miForm.getDatosTablaVisibles(0);
			ocultos = miForm.getDatosTablaOcultos(0);
			//Recogemos los datos del formulario
			fechaInicio = (String)visibles.get(0);
			fechaFin = (String)visibles.get(1);			
			numeroColegiado = (String)visibles.get(2);

			//Almacenamos los datos necesarios en el request
			miForm.setFechaInicio(fechaInicio);
			miForm.setFechaFin(fechaFin);
			request.setAttribute("NUMEROCOLEGIADO",numeroColegiado);
			request.setAttribute("NOMBRE",(String)visibles.get(3));

			//Datos para consultar las guardias:			
			String idCalendario = (String)ocultos.get(0);
			String idTurno = (String)ocultos.get(1);
			String idGuardia = (String)ocultos.get(2);
			String idInstitucion = (String)ocultos.get(3);
			String idPersona = (String)ocultos.get(5);

			//Consulto las guardias del periodo:
			StringBuffer where = new StringBuffer();
			where.append(" WHERE "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idInstitucion);
			where.append(" AND "+ScsGuardiasColegiadoBean.C_IDTURNO +"="+idTurno);
			where.append(" AND "+ScsGuardiasColegiadoBean.C_IDGUARDIA +"="+idGuardia);
			where.append(" AND "+ScsGuardiasColegiadoBean.C_FECHAINICIO+"=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')");
			where.append(" AND "+ScsGuardiasColegiadoBean.C_IDPERSONA+"="+idPersona);

			Vector vGuardiasLetrado = admGuardiasColegiado.select(where.toString());

			StringBuffer guardiasPeriodo = new StringBuffer();
			Iterator iter = vGuardiasLetrado.iterator();
			int i=1;
			while (iter.hasNext()) {
				ScsGuardiasColegiadoBean beanGuardiasColegiado = (ScsGuardiasColegiadoBean)iter.next();
				guardiasPeriodo.append(GstDate.getFormatedDateShort("ES",beanGuardiasColegiado.getFechaFin()));
				if (i%2==0)
					guardiasPeriodo.append("\n");
				else
					guardiasPeriodo.append(" - ");
				i++;
			}
			miForm.setGuardiasPeriodo(guardiasPeriodo.toString());				
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null);		
		}			
		return "modalConsulta";
	}

	/**
	 * Inserta los datos de 1 Calendario de Guardias Automaticamente al pulsar "Generar Autom�ticamente" en la <br>
	 * modal de Mantenimiento del Calendario.
	 * Usado para generar el Calendario de Guardias de Titulares y de Reservas.
	 */
	private synchronized String insertarCalendarioAutomaticamente(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Controles generales
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		UsrBean usr = this.getUserBean(request);
				String forward = "";


		
		// obteniendo valores del formulario
		String idInstitucion = miForm.getIdInstitucion();
		String idTurno = miForm.getIdTurno();
		String idGuardia = miForm.getIdGuardia();
		String idCalendarioGuardias = miForm.getIdCalendarioGuardias();
		String fechaDesde = miForm.getFechaDesde();
		String fechaHasta = miForm.getFechaHasta();

		try {
			
			CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
			calendarioSJCS.inicializaParaGenerarCalendario(new Integer(idInstitucion), new Integer(idTurno),
					new Integer(idGuardia),new Integer(idCalendarioGuardias), GstDate.getApplicationFormatDate("", fechaDesde) ,GstDate.getApplicationFormatDate("", fechaHasta), usr);
			calendarioSJCS.generarCalendario();
			
			
			forward = exitoRefresco("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.calendarioGenerado",
					request);
		} catch (SIGAException e) {
			forward = exitoRefresco(e.getLiteral(), request);
		} catch (Exception e) {
			//throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
			forward = exitoRefresco("messages.general.error.calendarioNoGrupoDefinido", request);
		} 
		
		return forward;
	} // insertarCalendarioAutomaticamente()

	
	
	
	/**
	 * Contempla el siguiente caso: 
	 * Pulso nuevo en pantalla inicial y me abre una modal pequenha al guardar, inserta los datos basicos de un <br>
	 * nuevo calendario. Para este caso el parametro accion="modalNuevo".
	 * 
	 * Voy arrastrando los parametros de la pestanha (guardia editada): IDINSTITUCION, IDTURNO, IDGUARDIA
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String modalNuevoCalendario(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.getUserBean(request));

		Hashtable miHash = new Hashtable();
		UsrBean usr;
		Vector registros = new Vector();
		String idinstitucion_pestanha="", idturno_pestanha="", idguardia_pestanha="";
		String tipodias="", diasguardia="", num_letrados="", num_sustitutos="";

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			//Esta modal me permite insertar un nuevo calendario de guardias.
			//Obtengo los datos del formulario:
			idinstitucion_pestanha = miForm.getIdInstitucionPestanha();
			idturno_pestanha = miForm.getIdTurnoPestanha();
			idguardia_pestanha = miForm.getIdGuardiaPestanha();

			//Consulto los datos de la modal en la tabla scs_guardiasturno:
			registros = admGuardiasTurno.ejecutaSelect(admGuardiasTurno.getDatosCalendario(idinstitucion_pestanha,idturno_pestanha,idguardia_pestanha));
			tipodias = ScsGuardiasTurnoAdm.obtenerTipoDia((String)((Hashtable)registros.get(0)).get("SELECCIONLABORABLES"),(String)((Hashtable)registros.get(0)).get("SELECCIONFESTIVOS"),usr);
			diasguardia = (String)((Hashtable)registros.get(0)).get("DIASGUARDIA");		
			num_letrados = (String)((Hashtable)registros.get(0)).get("NUMEROLETRADOS");
			num_sustitutos = (String)((Hashtable)registros.get(0)).get("NUMEROSUSTITUTOS");

			//Meto los datos en una tabla hash y en el request como DATOSINICIALES:
			miHash.put("IDINSTITUCIONPESTA�A",idinstitucion_pestanha);
			miHash.put("IDTURNOPESTA�A",idturno_pestanha);
			miHash.put("IDGUARDIAPESTA�A",idguardia_pestanha);
			miHash.put("modo",miForm.getModo());
			miHash.put("TIPODIAS",tipodias);
			miHash.put("DIASGUARDIA",diasguardia);
			miHash.put("NUMEROLETRADOS",num_letrados);
			miHash.put("NUMEROSUSTITUTOS",num_sustitutos);
			request.setAttribute("DATOSINICIALES",miHash);
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}							
		return "modalNuevo";
	}


	/**
	 * Contempla el siguiente caso: 
	 * Pulso nuevo en la modal grande (se abre al pulsar ver/editar en la pantalla inicial) y me abre una modal <br>
	 * pequenha para insertar un registro en el calendario. Para este caso el parametro accion="modalRegistro".
	 * 
	 * Voy arrastrando los parametros de la pestanha (guardia editada): IDINSTITUCION, IDTURNO, IDGUARDIA
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String modalNuevaGuardia(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Controles
		UsrBean usr = (UsrBean) this.getUserBean(request);
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		
		String idInstitucion, idTurno, idGuardia, idCalendarioGuardias;

		try {
			idInstitucion = miForm.getIdInstitucion();
			idTurno = miForm.getIdTurno();
			idGuardia = miForm.getIdGuardia();
			idCalendarioGuardias = miForm.getIdCalendarioGuardias();

			CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
			calendarioSJCS.inicializaParaMatriz(new Integer(idInstitucion), new Integer(idTurno),
					new Integer(idGuardia), new Integer(idCalendarioGuardias), null, usr, null);
			calendarioSJCS.calcularMatrizPeriodosDiasGuardia();

			// Nota: El array arrayPeriodosSJCS es un array periodos y cada periodo es un arra de dias.
			ArrayList arrayPeriodosSJCS = calendarioSJCS.getArrayPeriodosDiasGuardiaSJCS();

			// Mando el array de periodos para construir el combo del mismo:
			request.setAttribute("ARRAYPERIODOS", arrayPeriodosSJCS);
		} catch (Exception e) {
			throwExcp("messages.select.error", e, null);
		}
		return "modalRegistro";
	}
	
	protected String anular(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		UsrBean usr = (UsrBean) this.getUserBean(request);
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(usr);
		String idInstitucion, idTurno, idGuardia, fechaInicio, idPersona;
		String observaciones = "";
		
		try {
			idInstitucion = miForm.getIdInstitucion();
			idTurno = miForm.getIdTurno();
			idGuardia = miForm.getIdGuardia();
			fechaInicio = UtilidadesString.formatoFecha(miForm.getFechaInicio(), "yyyy/MM/dd hh:mm:ss", "dd/MM/yyyy");
			idPersona = miForm.getIdPersona();
			
			String select = "SELECT * FROM " + ScsCabeceraGuardiasBean.T_NOMBRETABLA 
						  + " WHERE " + ScsCabeceraGuardiasBean.C_IDINSTITUCION 		+ "=" + idInstitucion 
						  + "   AND " + ScsCabeceraGuardiasBean.C_IDTURNO 				+ "=" + idTurno
						  + "   AND " + ScsCabeceraGuardiasBean.C_IDGUARDIA				+ "=" + idGuardia
						  + "   AND " + ScsCabeceraGuardiasBean.C_IDPERSONA 			+ "=" + idPersona
						  +	"   AND trunc("+ScsCabeceraGuardiasBean.C_FECHA_INICIO+")=TO_DATE('"+fechaInicio+"','DD/MM/YYYY')";
			
			Vector result = cabeceraGuardiasAdm.selectGenerico(select);	
			Hashtable cabecera = new Hashtable();

			if (result!=null && !result.isEmpty()) {
				cabecera = (Hashtable)result.firstElement();
				observaciones = (String)cabecera.get("OBSERVACIONESANULACION");
				miForm.setComenAnulacion(observaciones);
			}
			
			request.setAttribute("DefinirCalendarioGuardiaForm", miForm);
			
		} catch (Exception e) {
			throwExcp("messages.select.error", e, null);
		}
		return "anular";
	}	

	private String realizarAnulacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException{		
		UsrBean usr = (UsrBean) this.getUserBean(request);
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsCabeceraGuardiasAdm admCabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(usr);		
		Hashtable cabecera = new Hashtable();
		
		try {
			//CLAVES
			cabecera.put("IDINSTITUCION",miForm.getIdInstitucion());
			cabecera.put("IDTURNO",miForm.getIdTurno());
			cabecera.put("IDGUARDIA",miForm.getIdGuardia());			
			cabecera.put("FECHAINICIO",miForm.getFechaInicio());
			cabecera.put("IDPERSONA",miForm.getIdPersona());
			
			//CAMPOS ACTUALIZABLES
			cabecera.put("VALIDADO",ClsConstants.DB_FALSE);
			cabecera.put("FECHAVALIDACION","");
			cabecera.put("OBSERVACIONESANULACION",miForm.getComenAnulacion());
			
			String claves[] = {"IDINSTITUCION","IDTURNO","IDGUARDIA","IDPERSONA","FECHAINICIO"};
			String campos[] = {"VALIDADO", "FECHAVALIDACION", "OBSERVACIONESANULACION","USUMODIFICACION","FECHAMODIFICACION"};
				
			admCabeceraGuardiasAdm.updateDirect(cabecera, claves, campos);
			 
		} catch (Exception e) {
			throwExcp("messages.select.error", e, null); 
		}

		return exitoModal("messages.updated.success",request);
	}	
	
	/**
	 * Contempla el siguiente caso: 
	 * Pulso guardar en la modal para insertar una/s guardia/s de forma manual para el periodo y letrado seleccionado. 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la informaci�n.
	 * @param HttpServletRequest request: informaci�n de entrada de la pagina original.
	 * @param HttpServletResponse response: informaci�n de salida para la pagina destino. 
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String insertarGuardiaManual (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response)
	throws SIGAException
	{
		//Controles globales
		DefinirCalendarioGuardiaForm miForm;
		ScsGuardiasColegiadoAdm guardiasColegiadoAdm;

		//Variables
		UsrBean usr;
		Hashtable miHash;
		UserTransaction tx;
		String idInstitucion, idTurno, idGuardia, idCalendarioGuardias,
		idPersona, fechaInicio, fechaFin;
		int indicePeriodo;
		ArrayList arrayPeriodoSeleccionado = new ArrayList();
		LetradoInscripcion letrado;
		boolean validacionOk;
		int numeroError;
		String forward;

		//Valores necesariamente externos
		tx = null;
		forward = exitoModal("messages.inserted.success", request);
		usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		try {
			//Controles globales
			
			guardiasColegiadoAdm = new ScsGuardiasColegiadoAdm(this.getUserBean(request));

			//Valores obtenidos del formulario
			miForm = (DefinirCalendarioGuardiaForm) formulario;
			idInstitucion = miForm.getIdInstitucion();
			idTurno = miForm.getIdTurno();
			idGuardia = miForm.getIdGuardia();
			idCalendarioGuardias = miForm.getIdCalendarioGuardias();
			idPersona = miForm.getIdPersona();
			//Periodo:
			fechaInicio = miForm.getFechaInicio();
			fechaFin = miForm.getFechaFin();

			CenBajasTemporalesAdm bajasTemporalescioneAdm = new CenBajasTemporalesAdm(usr);
			//comprobamos que el confirmador no esta de vacaciones la fecha que del solicitante
			Map<String,CenBajasTemporalesBean> mBajasTemporalesConfirmador =  bajasTemporalescioneAdm.getDiasBajaTemporal(new Long(idPersona), new Integer(usr.getLocation()));
			if(mBajasTemporalesConfirmador.containsKey(fechaInicio))
				throw new SIGAException("censo.bajastemporales.messages.colegiadoEnVacaciones");
			
			indicePeriodo = Integer.parseInt(miForm.getIndicePeriodo());

			guardiasColegiadoAdm.insertarGuardiaManual(idInstitucion,idTurno,
					idGuardia, idPersona, idCalendarioGuardias,
					indicePeriodo,fechaInicio,fechaFin, usr);
			

			
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
	 * Descarga el fichero de Log.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String descargarLog(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		String forward = "descargaFichero";
		String sFicheroLog=null, sIdInstitucion=null, sIdEnvio=null;
		ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(this.getUserBean(request));
		try {
			DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm)formulario;
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
			String idInstitucion = "";
			String idTurno = "";
			String idGuardia = "";
			String idCalendarioGuardias = "";
			String fechaDesde = "";
			String fechaHasta = "";
			if(miForm.getAccion()!=null && miForm.getAccion().equals("descargaDesdeProgramacion")){
				idCalendarioGuardias = miForm.getIdCalendarioGuardias();
				idTurno = miForm.getIdTurno();
				idGuardia = miForm.getIdGuardia();
				idInstitucion = user.getLocation();
			}else{
				Vector ocultos;		
				ocultos = miForm.getDatosTablaOcultos(0);
				idCalendarioGuardias = (String)ocultos.get(0);
				idTurno = (String)ocultos.get(1);
				idGuardia = (String)ocultos.get(2);
				idInstitucion = (String)ocultos.get(3);
			}
			
			//obteniendo los datos del calendario:
			StringBuffer where = new StringBuffer();
			where.append("WHERE "+ScsCalendarioGuardiasBean.C_IDINSTITUCION+"="+idInstitucion);
			where.append(" AND "+ScsCalendarioGuardiasBean.C_IDTURNO+"="+idTurno);
			where.append(" AND "+ScsCalendarioGuardiasBean.C_IDGUARDIA+"="+idGuardia);
			where.append(" AND "+ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS+"="+idCalendarioGuardias);
			Vector vCalendario = admCalendarioGuardia.select(where.toString());
			ScsCalendarioGuardiasBean beanCalendario = (ScsCalendarioGuardiasBean)vCalendario.get(0); 
			fechaDesde = UtilidadesString.formatoFecha(beanCalendario.getFechaInicio(),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			fechaHasta = UtilidadesString.formatoFecha(beanCalendario.getFechaFin(),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			
			ScsGuardiasColegiadoAdm envioAdm = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
			sFicheroLog = rp.returnProperty("sjcs.directorioFisicoGeneracionCalendarios") + File.separator
					+ idInstitucion + File.separator
					+ calendarioSJCS.getNombreFicheroLogCalendario(new Integer(idTurno), new Integer(idGuardia),new Integer( idCalendarioGuardias), fechaDesde, fechaHasta)
					+ ".log.xls";
			File fichero = new File(sFicheroLog);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());

		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		return forward;
	}

	

}