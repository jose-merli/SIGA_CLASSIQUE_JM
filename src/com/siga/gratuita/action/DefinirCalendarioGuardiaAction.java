package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.HelperInformesAdm;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsCabeceraGuardiasBean;
import com.siga.beans.ScsCalendarioGuardiasAdm;
import com.siga.beans.ScsCalendarioGuardiasBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasColegiadoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsPermutaGuardiasAdm;
import com.siga.beans.ScsPermutaGuardiasBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirCalendarioGuardiaForm;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;
import com.siga.gratuita.util.calendarioSJCS.LetradoGuardia;

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
	 * Método que atiende a las peticiones.
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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String buscar (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response)
	throws SIGAException
	{
		//Controles globales
		DefinirCalendarioGuardiaForm miForm = 
			(DefinirCalendarioGuardiaForm) formulario;
		ScsCalendarioGuardiasAdm admCalendarioGuardia = 
			new ScsCalendarioGuardiasAdm(this.getUserBean(request));
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
			salida = admCalendarioGuardia.selectGenerico(
					admCalendarioGuardia.getCalendarios(
							idinstitucion,idturno,idguardia));

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

	/**
	 * Abre la modal de Mantenimiento del Calendario de Guardias
	 * con el iframe del calendario correspondiente.
	 * Gestiona la creacion automática y manual del calendario. 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
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

		try
		{
			//obteniendo informacion del usuario
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			//obteniendo los datos del formulario
			ocultos = miForm.getDatosTablaOcultos(0);
			idcalendarioguardias = (String)ocultos.get(0);
			idturno = (String)ocultos.get(1);
			idguardia = (String)ocultos.get(2);
			idinstitucion = (String)ocultos.get(3);

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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsCabeceraGuardiasAdm cabecerasAdm = new ScsCabeceraGuardiasAdm(this.getUserBean(request));

		String forward = "",nombre="";
		Vector visibles = new Vector();
		Vector ocultos = new Vector();
		Vector cabecera = new Vector();
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
					fechaInicio = (String)visibles.get(0);
					fechaFin = (String)visibles.get(1);			
					numeroColegiado = (String)visibles.get(2);

					//Almacenamos los datos necesarios en el request
					miForm.setFechaInicio(fechaInicio);
					miForm.setFechaFin(fechaFin);
					request.setAttribute("NUMEROCOLEGIADO",numeroColegiado);
					request.setAttribute("NOMBRE",(String)visibles.get(3));

					//Datos para consultar las guardias:			
					idCalendario = (String)ocultos.get(0);
					idTurno = (String)ocultos.get(1);
					idGuardia = (String)ocultos.get(2);
					idInstitucion = (String)ocultos.get(3);
					idPersona = (String)ocultos.get(5);
				}else if(miForm.getAccion().equals("modalConsultaCenso")){
					fechaInicio = (String)ocultos.get(3);
					fechaInicio=GstDate.getFormatedDateShort(this.getUserBean(request).getLanguage(),fechaInicio);

					//Almacenamos los datos necesarios en el request
					miForm.setFechaInicio(fechaInicio);

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
					idPersona = (String)ocultos.get(4);
				}


				String sql ="SELECT C.COMENSUSTITUCION as COMENSUSTITUCION,trunc(C.FECHASUSTITUCION) as FECHASUSTITUCION,C.LETRADOSUSTITUIDO as LETRADOSUSTITUIDO FROM SCS_CABECERAGUARDIAS C" +
				" WHERE IDPERSONA = "+idPersona+
				"   AND FECHAINICIO =" +
				"       TO_DATE('"+GstDate.getApplicationFormatDate(this.getUserBean(request).getLanguage(),fechaInicio)+"', 'YYYY/MM/DD HH24:MI:SS')" +
				"   AND IDGUARDIA = "+idGuardia+
				"   AND IDTURNO = "+idTurno+
				"   AND IDINSTITUCION ="+idInstitucion+
				"   and IDCALENDARIOGUARDIAS="+idCalendario+
				" ORDER BY IDINSTITUCION," +
				"          IDTURNO," +
				"          IDGUARDIA," +
				"          IDCALENDARIOGUARDIAS," +
				"          IDPERSONA," +
				"          FECHAINICIO";					

				cabecera=cabecerasAdm.selectGenerico(sql);
				if(cabecera!=null || cabecera.size()>0){
					if((String)((Hashtable)cabecera.get(0)).get("LETRADOSUSTITUIDO")!=null && !((String)((Hashtable)cabecera.get(0)).get("LETRADOSUSTITUIDO")).equals("")){
						String consulta="select p.nombre||' '||p.apellidos1||' '||p.apellidos2 as NOMBRE from cen_persona p where p.idpersona="+(String)((Hashtable)cabecera.get(0)).get("LETRADOSUSTITUIDO");
						letradoSustituido=cabecerasAdm.selectGenerico(consulta);
						if(letradoSustituido!=null  || letradoSustituido.size()>0){
							nombre=(String)(((Hashtable)(letradoSustituido.get(0))).get("NOMBRE"));
							request.setAttribute("LETRADOSUSTITUIDO",nombre);
						}else{
							request.setAttribute("LETRADOSUSTITUIDO","-");
						}
					}else{
						request.setAttribute("LETRADOSUSTITUIDO","-");
					}
					request.setAttribute("COMENTARIO",(String)((Hashtable)cabecera.get(0)).get("COMENSUSTITUCION"));
					request.setAttribute("FECHASUSTITUCION",GstDate.getFormatedDateShort(this.getLenguaje(request),(String)((Hashtable)cabecera.get(0)).get("FECHASUSTITUCION")));
				}

				//Consulto las guardias del periodo:
				StringBuffer where = new StringBuffer();
				where.append(" WHERE "+ScsGuardiasColegiadoBean.C_IDINSTITUCION+"="+idInstitucion);
				where.append(" AND "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS +"="+idCalendario);
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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	private String insertarCalendario(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(this.getUserBean(request));
		ScsGuardiasTurnoAdm admGuardiasTurno = new ScsGuardiasTurnoAdm(this.getUserBean(request));

		Hashtable miHash = new Hashtable();
		UsrBean usr;
		String idcalendarioguardias = "", idinstitucion_pestanha="", idguardia_pestanha="", idturno_pestanha="";
		UserTransaction tx = null;
		Vector registros = new Vector();
		String texto="", idPersonaUltimoAnterior="";

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			//Valores obtenidos de la pestanha
			idinstitucion_pestanha = miForm.getIdInstitucionPestanha();
			idturno_pestanha = miForm.getIdTurnoPestanha();
			idguardia_pestanha = miForm.getIdGuardiaPestanha();


			//Calculo el idPersonaUltimoAnterior de guardias turno:
			StringBuffer sql = new StringBuffer();
			sql.append("WHERE "+ScsGuardiasTurnoBean.C_IDINSTITUCION+"="+idinstitucion_pestanha);
			sql.append(" AND "+ScsGuardiasTurnoBean.C_IDTURNO+"="+idturno_pestanha);
			sql.append(" AND "+ScsGuardiasTurnoBean.C_IDGUARDIA+"="+idguardia_pestanha);
			Vector v = admGuardiasTurno.select(sql.toString());
			if (!v.isEmpty()) {
				if (((ScsGuardiasTurnoBean)v.get(0)).getIdPersona_Ultimo()!=null)
					idPersonaUltimoAnterior = ((ScsGuardiasTurnoBean)v.get(0)).getIdPersona_Ultimo().toString();
			}


			//Calculo el id de idcalendarioguardias
			registros = admCalendarioGuardia.selectGenerico(admCalendarioGuardia.getIdCalendarioGuardias(idinstitucion_pestanha,idturno_pestanha,idguardia_pestanha));
			idcalendarioguardias = (String)(((Hashtable)registros.get(0)).get("IDCALENDARIOGUARDIAS"));
			//Si no hay elementos le asigno el primero: 1
			if (idcalendarioguardias.equals("")) idcalendarioguardias = "1";
			miHash.put(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS,idcalendarioguardias);

			miHash.put(ScsCalendarioGuardiasBean.C_IDINSTITUCION,idinstitucion_pestanha);
			miHash.put(ScsCalendarioGuardiasBean.C_IDGUARDIA,idguardia_pestanha);
			miHash.put(ScsCalendarioGuardiasBean.C_IDTURNO,idturno_pestanha);
			//Obtengo las fechas, la convierto a formato BD y la inserto en la hash
			miHash.put(ScsCalendarioGuardiasBean.C_FECHAFIN,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaHasta()));
			miHash.put(ScsCalendarioGuardiasBean.C_FECHAINICIO,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaDesde()));
			miHash.put(ScsCalendarioGuardiasBean.C_OBSERVACIONES,miForm.getObservaciones());
			miHash.put(ScsCalendarioGuardiasBean.C_USUMODIFICACION,usr.getUserName());
			miHash.put(ScsCalendarioGuardiasBean.C_FECHAMODIFICACION,"sysdate");
			miHash.put(ScsCalendarioGuardiasBean.C_IDPERSONA_ULTIMOANTERIOR,idPersonaUltimoAnterior);


			//INSERT (INICIO TRANSACCION)
			tx=usr.getTransaction();
			tx.begin();                 
			if (admCalendarioGuardia.insert(miHash)) 
				texto = "messages.inserted.success";
			else
				texto = "messages.inserted.error";
			tx.commit();

			request.setAttribute("modo",miForm.getModo());
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}					
		return exitoModal(texto, request);
	}

	/**
	 * Guarda los datos de un Calendario de Guardias en Base de Datos.
	 * 
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;

		ScsCalendarioGuardiasAdm admCalendarioGuardia = new ScsCalendarioGuardiasAdm(this.getUserBean(request));
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutaGuardias = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
		ScsSaltosCompensacionesAdm admSaltosCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request)); 

		String forward = "exito";
		Hashtable miHash = new Hashtable();
		Hashtable hashPermuta = new Hashtable();
		Vector ocultos = new Vector();
		UsrBean usr = null;
		UserTransaction tx = null;
		String numero = "";
		boolean borradosOK = false;
		String fechaInicio="", fechaFin="";

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			//
			//Caso1: modal Mantenimiento, parte del listado de Colegiados de Guardias
			//Se borran la guardia de este calendario y sus permutas.
			//
			if (miForm.getAccion()!=null && miForm.getAccion().equalsIgnoreCase("modalGuardia")) {
				ocultos = miForm.getDatosTablaOcultos(0);					

				//Fechas del periodo de esta guardia:
				fechaInicio = GstDate.getFormatedDateShort(usr.getLanguage(), (String)ocultos.get(6));
				fechaFin = GstDate.getFormatedDateShort(usr.getLanguage(), (String)ocultos.get(8));

				miHash.clear();							
				miHash.put(ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS,(String)ocultos.get(0));
				miHash.put(ScsGuardiasColegiadoBean.C_IDTURNO,(String)ocultos.get(1));
				miHash.put(ScsGuardiasColegiadoBean.C_IDGUARDIA,(String)ocultos.get(2));
				miHash.put(ScsGuardiasColegiadoBean.C_IDINSTITUCION,(String)ocultos.get(3));				
				miHash.put(ScsGuardiasColegiadoBean.C_IDPERSONA,(String)ocultos.get(5));
				//Fechas del periodo para este letrado y esta guardia:
				miHash.put(ScsGuardiasColegiadoBean.C_FECHAINICIO,fechaInicio);//GstDate.getApplicationFormatDate(usr.getLanguage(), fechaInicio));
				miHash.put(ScsGuardiasColegiadoBean.C_FECHAFIN,fechaFin);//GstDate.getApplicationFormatDate(usr.getLanguage(), fechaFin));

				hashPermuta.clear();
				hashPermuta.put(ScsPermutaGuardiasBean.C_IDINSTITUCION,(String)ocultos.get(3));
				numero = ((String)ocultos.get(7)).trim();
				hashPermuta.put(ScsPermutaGuardiasBean.C_NUMERO,numero);

				tx=usr.getTransaction();

				//Compruebo que no hay ninguna guardia realizada:
				if (admGuardiasColegiado.validarBorradoGuardia((String)ocultos.get(3), (String)ocultos.get(0), (String)ocultos.get(1), (String)ocultos.get(2), fechaInicio, fechaFin)) {
					tx.begin();
					//DELETE EN TABLA SCS_PERMUTAGUARDIAS SI PROCEDE
					if (!numero.equals("NINGUNO")) {
						if (admPermutaGuardias.delete(hashPermuta))					
							//DELETE EN TABLA SCS_GUARDIASCOLEGIADO
							if (admGuardiasColegiado.deleteGuardiaCalendario(miHash))
								//DELETE EN TABLA SCS_CABECERAGUARDIAS
								borradosOK = admCabeceraGuardias.deleteCabeceraGuardiasCalendario(miHash);							
					} else {
						//DELETE EN TABLA SCS_GUARDIASCOLEGIADO
						if (admGuardiasColegiado.deleteGuardiaCalendario(miHash))
							//DELETE EN TABLA SCS_CABECERAGUARDIAS
							borradosOK = admCabeceraGuardias.deleteCabeceraGuardiasCalendario(miHash);							
					}
					tx.commit();

					//Si los 2 borrados han funcionado o al menos el borrado de la tabla padre
					if (borradosOK) 
						forward = exitoRefresco("messages.deleted.success",request);
					//ERROR EN EL DELETE EN TABLA SCS_GUARDIASCOLEGIADO
					else {
						forward = exito("error.messages.deleted",request);
						tx.rollback();
					}
				} else				    
					forward = exito("error.messagess.borrarGuardiaCalendario",request);
				//
				//Caso2: pantalla inicial de listado de calendarios
				//Borro el calendario completo eliminando previamente todas sus guardias.
				//
			} else {
				ocultos = miForm.getDatosTablaOcultos(0);					
				miHash.clear();							
				miHash.put(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS,ocultos.get(0));
				miHash.put(ScsCalendarioGuardiasBean.C_IDTURNO,ocultos.get(1));
				miHash.put(ScsCalendarioGuardiasBean.C_IDGUARDIA,ocultos.get(2));
				miHash.put(ScsCalendarioGuardiasBean.C_IDINSTITUCION,ocultos.get(3));


				tx=usr.getTransaction();

				//Compruebo antes de borrar que este es el ultimo calendario (mirando la fecha fin del calendario no hay ninguno para esta guardia con un fecha fin mayor)
				if (admCalendarioGuardia.validarBorradoCalendario((String)ocultos.get(3), (String)ocultos.get(0), (String)ocultos.get(1), (String)ocultos.get(2))) {
					//Compruebo que no hay ninguna guardia realizada:
					if (admGuardiasColegiado.validarBorradoGuardias((String)ocultos.get(3), (String)ocultos.get(0), (String)ocultos.get(1), (String)ocultos.get(2))) {
						tx.begin();
						//UPDATE: actualizo el ultimo de la guardia con el anterior del calendario:						
						if(admCalendarioGuardia.actualizarGuardia(miHash))							
							//Updates sobre las compensaciones:
							if(admSaltosCompensaciones.updateSaltosCumplidos(miHash))
								if(admSaltosCompensaciones.updateCompensacionesCumplidas(miHash))
									//DELETE EN TABLA SCS_PERMUTAGUARDIAS solicitante y confirmador
									if(admPermutaGuardias.deletePermutasCalendario(miHash))
										//DELETE EN TABLA SCS_GUARDIASCOLEGIADO
										if(admGuardiasColegiado.deleteGuardiasCalendario(miHash))
											//DELETE EN TABLA SCS_CABECERAGUARDIAS
											if(admCabeceraGuardias.deleteCabecerasGuardiasCalendario(miHash))
												//DELETE EN TABLA SCS_CALENDARIOGUARDIA
												if(admSaltosCompensaciones.deleteCompensacionesNoCumplidas(miHash))	
													//eliminar las compensaciones creadas en calendarios que ya no existen.
													if(admSaltosCompensaciones.deleteCompensacionesCalendariosInexistentes(miHash))	
														//DELETE EN TABLA SCS_CALENDARIOGUARDIA
														if(borradosOK = admCalendarioGuardia.delete(miHash))					

															tx.commit();

						//Si los borrados y updates han funcionado:
						if (borradosOK)
							forward = exitoRefresco("messages.deleted.success",request);
						//ERROR EN EL DELETE EN TABLA SCS_GUARDIASCOLEGIADO
						else {
							forward = exito("error.messages.deleted",request);
							tx.rollback();
						}
					} else
						forward = exito("error.messagess.borrarCalendario",request);
				} else
					forward = exito("error.message.hayCalendarioFechaFinMayor",request);
			}
		} catch (Exception e){
			throwExcp("error.messages.deleted",e,tx);
		}	
		return forward;
	}
	protected String buscarPorOld(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsGuardiasColegiadoAdm admGuardiaColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutaguardias = new ScsPermutaGuardiasAdm(this.getUserBean(request));


		Hashtable miHash = new Hashtable();
		UsrBean usr = null;
		String forward = "error";
		String idcalendarioguardias="", idinstitucion="", idturno="", idguardia="";
		String idpersona = "", fechaInicio="", fechaFin="", fechaPermuta="", numeroColegiado="";
		String observaciones="", nombre="", fechaDesde="", fechaHasta="", diasguardia="", diasacobrar="", tipodias="";
		String fechaInicioPermuta="", fechaFinPermuta="";
		Vector v_resultado = new Vector ();
		Vector v_guardias = new Vector ();
		String numero="", modoOriginal="";
		String buscarFechaDesde="";
		String buscarFechaHasta="";
		String buscarNcolegiado="";
		String pl = ""; //Valor devuelto por el PL de Permutas

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
				v_guardias = admGuardiaColegiado.selectGenerico(admGuardiaColegiado.buscarColegiadosOld(miHash));

				int i = 0;
				while (i < v_guardias.size()) {
					nombre = (String)((Hashtable)v_guardias.get(i)).get(CenPersonaBean.C_NOMBRE);
					//fechaInicio = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_FECHA_INICIO);
					fechaInicio = (String)((Hashtable)v_guardias.get(i)).get("FECHAINICIO");
					fechaFin = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_FECHA_FIN);
					idpersona = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_IDPERSONA);
					numeroColegiado = (String)((Hashtable)v_guardias.get(i)).get(CenColegiadoBean.C_NCOLEGIADO);

					fechaInicioPermuta = (String)((Hashtable)v_guardias.get(i)).get("FECHAINICIOPERMUTA");
					fechaFinPermuta = (String)((Hashtable)v_guardias.get(i)).get("FECHAFINPERMUTA");
					numero = (String)((Hashtable)v_guardias.get(i)).get("NUMEROPERMUTA");
					if (numero.equals("")){
						numero="NINGUNO";
					}

					//Ejecuto el PL de Permutas que me dice el tipo de Permuta posible:				
					pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio));
					if (pl.equals("5")){//si buscando por la fecha de inicio (para el caso en el que todavia no se haya confirmado la solicitud de la permuta) devuelve el valor "5" (pendiente de permutar) volvemos a ejecutar
						// el procedimiento pasando la fecha de inicio de permuta (para el caso en el que ya se haya 
						// confirmado la permuta) por si sigue devolviendo "5" o devuelve otro
						// valor como "3" (guardia permutada), como "2" (Permuta solicitada) o "4" (Pendiente de confirmar).

						pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPermuta));
					} 

					//Inserto los datos a visualizar en el JSP
					Hashtable nueva = new Hashtable();
					nueva.put("FECHAINICIO",fechaInicio);
					nueva.put("FECHAFIN",fechaFin);			
					nueva.put("FECHAINICIOPERMUTA",fechaInicioPermuta);
					nueva.put("FECHAFINPERMUTA",fechaFinPermuta);				
					nueva.put("FECHAPERMUTA",fechaPermuta);
					nueva.put("NUMEROPERMUTA",numero);
					nueva.put("NUMEROCOLEGIADO",numeroColegiado);
					nueva.put("NOMBRE",nombre);
					nueva.put("IDPERSONA",idpersona);
					nueva.put("IDINSTITUCION",idinstitucion);
					nueva.put("IDTURNO",idturno);
					nueva.put("IDGUARDIA",idguardia);
					nueva.put("IDCALENDARIOGUARDIAS",idcalendarioguardias);
					nueva.put("OBSERVACIONES",observaciones);
					nueva.put("PL",pl);
					v_resultado.add(i,nueva);	
					i++;	
				}//Fin del while
			}else{//con criterios de búsqueda
				if (buscarNcolegiado!=null && !buscarNcolegiado.trim().equals("")){
					miHash.put("NUMCOLEGIADO",buscarNcolegiado);	
				}
				if (buscarFechaDesde!=null && !buscarFechaDesde.trim().equals("")){
					UtilidadesHash.set(miHash, "FECHA_INICIO", buscarFechaDesde);
				}
				if (buscarFechaHasta!=null && !buscarFechaHasta.trim().equals("")){
					UtilidadesHash.set(miHash, "FECHA_FIN", buscarFechaHasta);
				}



				v_guardias = admGuardiaColegiado.selectGenerico(admGuardiaColegiado.buscarColegiadosOld(miHash));
				int i = 0;
				while (i < v_guardias.size()) {
					nombre = (String)((Hashtable)v_guardias.get(i)).get(CenPersonaBean.C_NOMBRE);
					//fechaInicio = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_FECHA_INICIO);
					fechaInicio = (String)((Hashtable)v_guardias.get(i)).get("FECHAINICIO");
					fechaFin = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_FECHA_FIN);
					idpersona = (String)((Hashtable)v_guardias.get(i)).get(ScsCabeceraGuardiasBean.C_IDPERSONA);
					numeroColegiado = (String)((Hashtable)v_guardias.get(i)).get(CenColegiadoBean.C_NCOLEGIADO);

					fechaInicioPermuta = (String)((Hashtable)v_guardias.get(i)).get("FECHAINICIOPERMUTA");
					fechaFinPermuta = (String)((Hashtable)v_guardias.get(i)).get("FECHAFINPERMUTA");
					numero = (String)((Hashtable)v_guardias.get(i)).get("NUMEROPERMUTA");
					if (numero.equals("")){
						numero="NINGUNO";
					}

					//Ejecuto el PL de Permutas que me dice el tipo de Permuta posible:				
					pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio));
					if (pl.equals("5")){//si buscando por la fecha de inicio (para el caso en el que todavia no se haya confirmado la solicitud de la permuta) devuelve el valor "5" (pendiente de permutar) volvemos a ejecutar
						// el procedimiento pasando la fecha de inicio de permuta (para el caso en el que ya se haya 
						// confirmado la permuta) por si sigue devolviendo "5" o devuelve otro
						// valor como "3" (guardia permutada), como "2" (Permuta solicitada) o "4" (Pendiente de confirmar).

						pl = admPermutaguardias.ejecutarFuncionPermutas(idinstitucion,idturno,idguardia,idpersona,GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPermuta));
					} 

					//Inserto los datos a visualizar en el JSP
					Hashtable nueva = new Hashtable();
					nueva.put("FECHAINICIO",fechaInicio);
					nueva.put("FECHAFIN",fechaFin);			
					nueva.put("FECHAINICIOPERMUTA",fechaInicioPermuta);
					nueva.put("FECHAFINPERMUTA",fechaFinPermuta);				
					nueva.put("FECHAPERMUTA",fechaPermuta);
					nueva.put("NUMEROPERMUTA",numero);
					nueva.put("NUMEROCOLEGIADO",numeroColegiado);
					nueva.put("NOMBRE",nombre);
					nueva.put("IDPERSONA",idpersona);
					nueva.put("IDINSTITUCION",idinstitucion);
					nueva.put("IDTURNO",idturno);
					nueva.put("IDGUARDIA",idguardia);
					nueva.put("IDCALENDARIOGUARDIAS",idcalendarioguardias);
					nueva.put("OBSERVACIONES",observaciones);
					nueva.put("PL",pl);
					v_resultado.add(i,nueva);	
					i++;	
				}//Fin del while
			}

			//Busco los colegiado de guardias
			forward = "buscarModalGuardia";

			request.setAttribute("resultado",v_resultado);
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
			if (buscarNcolegiado!=null && !buscarNcolegiado.trim().equals("") && v_guardias.size()>0){
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

	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		ScsGuardiasColegiadoAdm admGuardiaColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutaguardias = new ScsPermutaGuardiasAdm(this.getUserBean(request));


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
				RowsContainer rc = admGuardiaColegiado.findNLS(admGuardiaColegiado.buscarColegiados(miHash));

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

					String fechaInicioPKBind = GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPK); 

					Hashtable htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idturno);
					htCodigo.put(new Integer(3), idguardia);
					htCodigo.put(new Integer(4), idCalendarioGuardias);
					htCodigo.put(new Integer(5), idpersona);
					htCodigo.put(new Integer(6), fechaInicioPKBind);



					//consulta+= " ,F_SIGA_TIENE_ACTS_VALIDADAS(guard.IDINSTITUCION,guard.IDTURNO,guard.IDGUARDIA,guard.IDCALENDARIOGUARDIAS,guard.IDPERSONA,guard.FECHAINICIO) AS ACT_VALIDADAS";
					//ACT_VALIDADAS
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_TIENE_ACTS_VALIDADAS", "ACT_VALIDADAS"));

					//F_SIGA_ES_MODIFICABLE_GUARDIAS(guard.IDINSTITUCION,guard.IDTURNO, guard.IDGUARDIA, guard.IDCALENDARIOGUARDIAS,
					//      guard.IDPERSONA, guard.FECHAINICIO) as ESMODIFICABLE
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
					ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
					if (admGuardiasColegiado.validarBorradoGuardia(idinstitucion,idcalendarioguardias,idturno,idguardia,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio),GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin))){
						nueva.put("PINTARBOTONBORRAR", "1");

					}else{
						nueva.put("PINTARBOTONBORRAR", "0");
					}

					resultado.add(nueva);	
				}//Fin del while
			}else{//con criterios de búsqueda
				if (buscarNcolegiado!=null && !buscarNcolegiado.trim().equals("")){
					miHash.put("NUMCOLEGIADO",buscarNcolegiado);	
				}
				if (buscarFechaDesde!=null && !buscarFechaDesde.trim().equals("")){
					UtilidadesHash.set(miHash, "FECHA_INICIO", buscarFechaDesde);
				}
				if (buscarFechaHasta!=null && !buscarFechaHasta.trim().equals("")){
					UtilidadesHash.set(miHash, "FECHA_FIN", buscarFechaHasta);
				}



				RowsContainer rc = admGuardiaColegiado.findNLS(admGuardiaColegiado.buscarColegiados(miHash));

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

					String fechaInicioPKBind = GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPK); 

					Hashtable htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), idinstitucion);
					htCodigo.put(new Integer(2), idturno);
					htCodigo.put(new Integer(3), idguardia);
					htCodigo.put(new Integer(4), idCalendarioGuardias);
					htCodigo.put(new Integer(5), idpersona);
					htCodigo.put(new Integer(6), fechaInicioPKBind);





					//consulta+= " ,F_SIGA_TIENE_ACTS_VALIDADAS(guard.IDINSTITUCION,guard.IDTURNO,guard.IDGUARDIA,guard.IDCALENDARIOGUARDIAS,guard.IDPERSONA,guard.FECHAINICIO) AS ACT_VALIDADAS";
					//ACT_VALIDADAS
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_TIENE_ACTS_VALIDADAS", "ACT_VALIDADAS"));

					//F_SIGA_ES_MODIFICABLE_GUARDIAS(guard.IDINSTITUCION,guard.IDTURNO, guard.IDGUARDIA, guard.IDCALENDARIOGUARDIAS,
					//      guard.IDPERSONA, guard.FECHAINICIO) as ESMODIFICABLE
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

					ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
					if (admGuardiasColegiado.validarBorradoGuardia(idinstitucion,idcalendarioguardias,idturno,idguardia,GstDate.getFormatedDateShort(usr.getLanguage(),fInicio),GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin))){
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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
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
			where.append(" AND "+ScsGuardiasColegiadoBean.C_IDCALENDARIOGUARDIAS +"="+idCalendario);
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
	 * Inserta los datos de 1 Calendario de Guardias Automaticamente al pulsar "Generar Automáticamente" en la <br>
	 * modal de Mantenimiento del Calendario.
	 * Usado para generar el Calendario de Guardias de Titulares y de Reservas.
	 * 
	 * @param Vector letrados: vector de letrados de la tabla temporal
	 * @param int posicion: posicion en el vector de letrados del letrado a insertar
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return void 
	 */
	private synchronized String insertarCalendarioAutomaticamente(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		String idCalendarioGuardias = "", idInstitucion="", idGuardia="", idTurno="";
		UsrBean usr = null;
		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsPermutaGuardiasAdm admPermutaGuardias = new ScsPermutaGuardiasAdm(this.getUserBean(request));
		ScsCabeceraGuardiasAdm admCabeceraGuardias = new ScsCabeceraGuardiasAdm(this.getUserBean(request));
		ScsSaltosCompensacionesAdm admSaltosCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();

		UserTransaction tx = null;
		String forward = "";
		int salidaError = 0; //Variable para ver nivel de Error al generar el calendario. 0=OK.

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();

			//Valores obtenidos del formulario
			idInstitucion = miForm.getIdInstitucion();
			idTurno = miForm.getIdTurno();
			idGuardia = miForm.getIdGuardia();
			idCalendarioGuardias = miForm.getIdCalendarioGuardias();

			//Relleno los datos para borrar las guardias del calendario:
			miHash.put(ScsCalendarioGuardiasBean.C_IDCALENDARIOGUARDIAS, idCalendarioGuardias);
			miHash.put(ScsCalendarioGuardiasBean.C_IDTURNO, idTurno);
			miHash.put(ScsCalendarioGuardiasBean.C_IDGUARDIA, idGuardia);
			miHash.put(ScsCalendarioGuardiasBean.C_IDINSTITUCION, idInstitucion);	



			//Validamos que no haya ninguna guardia realizada:
			if (admGuardiasColegiado.validarBorradoGuardias(idInstitucion, idCalendarioGuardias, idTurno, idGuardia)) {			
				//
				//INICIO LA TRANSACCION:
				//
				tx.begin();
				//Borramos el calendario previamente:
				//DELETE EN TABLA SCS_PERMUTAGUARDIAS solicitante y confirmador
				if(admPermutaGuardias.deletePermutasCalendario(miHash)){
					//DELETE EN TABLA SCS_GUARDIASCOLEGIADO
					if(admGuardiasColegiado.deleteGuardiasCalendario(miHash)){
						//DELETE EN TABLA SCS_CABECERAGUARDIAS
						if(admCabeceraGuardias.deleteCabecerasGuardiasCalendario(miHash)){
							//Updates y Delete sobre las compensaciones:
							if(admSaltosCompensaciones.updateSaltosCumplidos(miHash)){
								if(admSaltosCompensaciones.updateCompensacionesCumplidas(miHash)){
									if(admSaltosCompensaciones.deleteCompensacionesNoCumplidas(miHash)) {
										//Si todos los borrados han ido bien genero el calendario de guardias:

										//
										//Generamos el Calendario de Guardias:
										//
										CalendarioSJCS calendarioSJCS = new CalendarioSJCS(new Integer(idInstitucion), new Integer(idTurno), new Integer(idGuardia) , new Integer(idCalendarioGuardias), usr);

										//Obtiene la Matriz de Periodos de Dias de Guardia:
										calendarioSJCS.calcularMatrizPeriodosDiasGuardia();

										//Pinta Datos del Calendario:
										//calendarioSJCS.pintarCalendarioSJCS();

										//Obtengo la matriz de letrados de Guardia para los periodos calculados:
										salidaError = calendarioSJCS.calcularMatrizLetradosGuardia();

										//Salida del resultado de ejecutar CalcularMatrizLetradosGuardia:
										//calendarioSJCS.pintarSalidaCalcularMatrizLetradosGuardia(salidaError);

										//				
										//FIN DEL CALENDARIO
										//

										//Controlo si debo hacer un rollback en caso de cualquier error:
										if (salidaError == 0)
											tx.commit();
										else
											tx.rollback();

										switch(salidaError) {
											//- 0: Todo Correcto
											case 0: forward = exitoRefresco("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.calendarioGenerado",request); break;
											//- 1: NO Hay Suficientes Letrados
											case 1: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetradosSuficientes",request); break;
											//- 2: Hay Incompatibilidad de Guardias
											case 2: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorIncompatibilidad",request); break;
											//- 3: NO hay Separacion suficiente
											case 3: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorSeparacion",request); break;
											//- 4: Error al provocarse una excepcion en el desarrollo del metodo.				
											case 4: forward = exito("messages.inserted.error",request); break;
										}
									}
								}	
							}	
						}	
					}	
				}else{
					tx.rollback();
				}
			} else
				forward = exito("error.messagess.borrarGuardiasGenerarCalendario",request);
		} catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return forward;
	}

	/**
	 * Contempla el siguiente caso: 
	 * Pulso nuevo en pantalla inicial y me abre una modal pequenha al guardar, inserta los datos basicos de un <br>
	 * nuevo calendario. Para este caso el parametro accion="modalNuevo".
	 * 
	 * Voy arrastrando los parametros de la pestanha (guardia editada): IDINSTITUCION, IDTURNO, IDGUARDIA
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
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
			miHash.put("IDINSTITUCIONPESTAÑA",idinstitucion_pestanha);
			miHash.put("IDTURNOPESTAÑA",idturno_pestanha);
			miHash.put("IDGUARDIAPESTAÑA",idguardia_pestanha);
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
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modalNuevaGuardia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		UsrBean usr;
		String idInstitucion, idTurno, idGuardia, idCalendarioGuardias;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			idInstitucion = miForm.getIdInstitucion();
			idTurno = miForm.getIdTurno();
			idGuardia = miForm.getIdGuardia();
			idCalendarioGuardias = miForm.getIdCalendarioGuardias();

			CalendarioSJCS calendarioSJCS = new CalendarioSJCS(new Integer(idInstitucion), new Integer(idTurno), new Integer(idGuardia) , new Integer(idCalendarioGuardias), usr);
			calendarioSJCS.calcularMatrizPeriodosDiasGuardia();

			//Nota: El array arrayPeriodosSJCS es un array periodos y cada periodo es un arra de dias.
			ArrayList arrayPeriodosSJCS = calendarioSJCS.getArrayPeriodosDiasGuardiaSJCS();

			//Mando el array de periodos para construir el combo del mismo:
			request.setAttribute("ARRAYPERIODOS", arrayPeriodosSJCS);
		} catch (Exception e) {
			throwExcp("messages.select.error",e,null);
		}							
		return "modalRegistro";
	}

	/**
	 * Contempla el siguiente caso: 
	 * Pulso guardar en la modal para insertar una/s guardia/s de forma manual para el periodo y letrado seleccionado. 
	 *
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String insertarGuardiaManual (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response)
	throws SIGAException
	{
		//Controles globales
		DefinirCalendarioGuardiaForm miForm;
		ScsGuardiasColegiadoAdm admGuardiasColegiado;

		//Variables
		UsrBean usr;
		Hashtable miHash;
		UserTransaction tx;
		String idInstitucion, idTurno, idGuardia, idCalendarioGuardias,
		idPersona, fechaInicio, fechaFin;
		int indicePeriodo;
		ArrayList arrayPeriodoSeleccionado = new ArrayList();
		LetradoGuardia letrado;
		boolean validacionOk;
		int numeroError;
		String forward;

		//Valores necesariamente externos
		tx = null;
		forward = exitoModal("messages.inserted.success", request);

		try {
			//Controles globales
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));

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
			indicePeriodo = Integer.parseInt(miForm.getIndicePeriodo());

			//Calculo los periodos de guardias:
			CalendarioSJCS calendarioSJCS = new CalendarioSJCS
			(new Integer(idInstitucion), new Integer(idTurno),
					new Integer(idGuardia), new Integer(idCalendarioGuardias),
					usr);
			calendarioSJCS.calcularMatrizPeriodosDiasGuardia();

			
			//Obtenemos los dias a Agrupar
			List lDiasASeparar = calendarioSJCS.getDiasASeparar(new Integer(idInstitucion), new Integer(idTurno), new Integer(idGuardia) , usr);
			
			
			
			//Nota: El array arrayPeriodosSJCS es un array periodos y cada periodo es un array de dias
			ArrayList arrayPeriodosSJCS = calendarioSJCS.getArrayPeriodosDiasGuardiaSJCS();

			//Selecciono el periodo de la lista de periodos:
			arrayPeriodoSeleccionado = (ArrayList)arrayPeriodosSJCS.get(indicePeriodo);

			//Creo el Letrado:
			letrado = new LetradoGuardia
			(new Long(idPersona), new Integer(idInstitucion),
					new Integer(idTurno), new Integer(idGuardia));			

			//VALIDACIONES:
			//Relleno una hash con los datos necesarios para validar:
			miHash = new Hashtable ();
			miHash.put("IDPERSONA",idPersona);
			miHash.put("IDINSTITUCION",idInstitucion);
			miHash.put("IDCALENDARIOGUARDIAS",idCalendarioGuardias);
			miHash.put("IDTURNO",idTurno);
			miHash.put("IDGUARDIA",idGuardia);
			miHash.put("FECHAINICIO",fechaInicio); //Del periodo
			miHash.put("FECHAFIN",fechaFin); //Del periodo

			//Numero de letrados: si supera el contador de letrados el permitido no puedo insertar
			validacionOk = true; numeroError = 0;

//			Se quita la validacion del numero de letrados INC_4866			
//			if (validacionOk && !admGuardiasColegiado.validarNumeroLetrados(miHash)) { 
			//validacionOk = false; numeroError=1;
			//}

//			El siguiente bloque de codigo se comenta de forma temporal para cuando 
//			tengamos tiempo de revisar y arreglar estas comprobaciones
//			//Que la persona elegida no haga una guardia incompatible el mismo dia para este periodo:
//			if (validacionOk && !admGuardiasColegiado.validarIncompatibilidadGuardia(idInstitucion, idTurno, idGuardia, arrayPeriodoSeleccionado, idPersona)) {
//			validacionOk = false; numeroError=2;
//			}
//			//Numero correcto de separacion entre guardias para este pediodo:
//			if (validacionOk && !admGuardiasColegiado.validarSeparacionGuardias(miHash)) { 
//			validacionOk = false; numeroError=3;
//			}

			//Si cumple todas las validaciones inserto
			if (validacionOk)  {
				//INSERT (INICIO TRANSACCION)
				tx=usr.getTransaction();
				tx.begin();   
				//Almaceno en BBDD la cabecera y las guardias colegiado para este letrado:
				calendarioSJCS.almacenarAsignacionGuardiaLetrado(letrado,arrayPeriodoSeleccionado,lDiasASeparar);
				tx.commit();

				forward = exitoModal("messages.inserted.success", request);
			} else {
				//Casos de error en la validacion. Aviso con un mensaje de error y no refresco.
				switch (numeroError){
				//1.Error: el número de letrados no es el permitido
				case 1: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorLetrados" ,request);break;
				//2.Error: el colegiado hace una guardia incompatible el mismo día	
				case 2: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorIncompatibilidad" ,request);break;
				//3.Error: numero incorrecto de separacion entre guardias
				case 3: forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.errorSeparacion" ,request);break;
				default:forward = exito("gratuita.modalRegistro_DefinirCalendarioGuardia.literal.seleccioneFecha" ,request);break;
				}
			}
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}		
		return forward;
	}

}