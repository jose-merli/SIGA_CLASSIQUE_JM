<!DOCTYPE html>
<html>
<head>
<!-- datosGenerales.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*,com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*,com.siga.expedientes.form.ExpDatosGeneralesForm,java.util.*"%>
<%@ page import="com.siga.beans.ExpExpedienteBean"%>
<%@ page import="com.siga.beans.GenTipoCampoBean"%>
<%@ page import="com.siga.beans.ExpCampoConfBean"%>
<%@ page import="com.siga.expedientes.form.ExpDatosGeneralesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="org.redabogacia.sigaservices.app.services.gen.SelectDataService"%>

<!-- JSP -->
<%
	HashMap<String, String> paramsJSON = new HashMap<String, String>();
	//Se pasa por defectola fecha de sistema al campo Fecha de Apertura
	String idinstitucion_tipoexpediente = (String) request
			.getParameter("idInstitucion_TipoExpediente");
	String fechaApertura = (String) request
			.getAttribute("fechaApertura");
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	String mostrarMinuta = (String) request
			.getAttribute("mostarMinuta");
	String mostrarMinutaFinal = (String) request
			.getAttribute("mostarMinutaFinal");
	String derechosColegiales = (String) request
			.getAttribute("derechosColegiales");
	String mostrarSolicitanteEJG = (String) request
	.getAttribute("mostrarSolicitanteEJG");
	String mostrarDenunciante = (String) request
			.getAttribute("mostrarDenunciante");
	Boolean tieneEjgRelacionado = false;
	if (request.getAttribute("tipoExpedienteEjg") != null)
		tieneEjgRelacionado = ((Boolean) request
				.getAttribute("tipoExpedienteEjg")).booleanValue();
	
	String tiempoCaducidad = (String) request
			.getAttribute("tiempoCaducidad");


	
	String totalMinuta = (String) request.getAttribute("totalMinuta");
	if (totalMinuta == null)
		totalMinuta = new String("");

	ArrayList<String> juzgadoSel = new ArrayList<String>();
	ArrayList<String> materiaSel = new ArrayList<String>();
	ArrayList pretensionSel = new ArrayList();
	String idJuzgado = "", idInstitucionJuzgado = "", idArea = "", idMateria = "", idPretension = "", codigoEjg = "", tipoExpD = "", anioExpD = "", numExpD = "", CODIGO = "", SUFIJO = "";
	// Datos del Juzgado seleccionado:
	
	ExpDatosGeneralesForm form = (ExpDatosGeneralesForm) request.getAttribute("ExpDatosGeneralesForm");
	
	//TIPO IDENTIFICACION
	String tipoIdentificacionDenunciante = "expedientes.auditoria.literal.nid";	
	if(form.getIdTipoIdentificacionDenunciante().equals("10")){
		tipoIdentificacionDenunciante = "expedientes.auditoria.literal.nif";
	} else if (form.getIdTipoIdentificacionDenunciante().equals("30")) {
		tipoIdentificacionDenunciante = "expedientes.auditoria.literal.pasaporte";
	} else if (form.getIdTipoIdentificacionDenunciante().equals("40")) {
		tipoIdentificacionDenunciante = "expedientes.auditoria.literal.nie";
	}
	
	String tipoIdentificacionDenunciado = "expedientes.auditoria.literal.nid";
	if(form.getIdTipoIdentificacionDenunciado().equals("10")){
		tipoIdentificacionDenunciado = "expedientes.auditoria.literal.nif";
	} else if (form.getIdTipoIdentificacionDenunciado().equals("30")) {
		tipoIdentificacionDenunciado = "expedientes.auditoria.literal.pasaporte";
	} else if (form.getIdTipoIdentificacionDenunciado().equals("40")) {
		tipoIdentificacionDenunciado = "expedientes.auditoria.literal.nie";
	}
	
	String nombreTamanio=""+(form.getNombreDenunciado().length() + 3);
	if(nombreTamanio.equals("0") || (form.getNombreDenunciado().length()+3) > 30)
		nombreTamanio="20";
	
	String ape1Tamanio=""+(form.getPrimerApellidoDenunciado().length() + 3);
	if(ape1Tamanio.equals("0") || (form.getPrimerApellidoDenunciado().length()+3) > 30)
		ape1Tamanio="25";
	
	String ape2Tamanio=""+form.getSegundoApellidoDenunciado().length();
	if(ape2Tamanio.equals("0") || (form.getSegundoApellidoDenunciado().length()+3) > 30)
		ape2Tamanio="10";

	String numColegiadoTamanio= "8";
	String nifTamanio = "12";
	
	String nombreDenuncianteTamanio=""+(form.getNombreDenunciante().length() + 3);
	if(nombreDenuncianteTamanio.equals("0") || (form.getNombreDenunciante().length()+3) > 30)
		nombreDenuncianteTamanio="20";
	
	String ape1DenuncianteTamanio=""+(form.getPrimerApellidoDenunciante().length() + 3);
	if(ape1DenuncianteTamanio.equals("0") || (form.getPrimerApellidoDenunciante().length()+3) > 30)
		ape1DenuncianteTamanio="25";
	
	String ape2DenuncianteTamanio=""+form.getSegundoApellidoDenunciante().length();
	if(ape2DenuncianteTamanio.equals("0") || (form.getSegundoApellidoDenunciante().length()+3) > 30)
		ape2DenuncianteTamanio="10";

	String nColDenuncianteTamanio = "8";
	String nifDenuncianteTamanio  = "12";
	
	String nombreRelacion ="";
	Boolean tieneExpeRelacionado= false;
	if (form.getRelacionExpediente() != null && !form.getRelacionExpediente().equalsIgnoreCase("")){
		tieneExpeRelacionado= true;
		nombreRelacion =form.getNombreRelacionExpediente();
	}
	
	if (form.getJuzgado() != null)
		idJuzgado = form.getJuzgado();
	if (form.getIdInstitucionJuzgado() != null)
		idInstitucionJuzgado = form.getIdInstitucionJuzgado();
	if (form.getIdPretension() != null){
		idPretension = form.getIdPretension();
		paramsJSON.put("idpretension",idPretension);
	}
	if (form.getIdArea() != null)
		idArea = form.getIdArea();
	if (form.getIdMateria() != null)
		idMateria = form.getIdMateria();

	if (form.getTipoExpDisciplinario() != null)
		tipoExpD = form.getTipoExpDisciplinario();
	if (form.getAnioExpDisciplinario() != null)
		anioExpD = form.getAnioExpDisciplinario();
	if (form.getNumExpDisciplinario() != null)
		numExpD = form.getNumExpDisciplinario();
	codigoEjg = form.getNumExpDisciplinarioCalc();
	
	paramsJSON.put("idarea", idArea);
	paramsJSON.put("idmateria", idMateria);
	String datosJuzgado = "{\"idarea\":\""+idArea+"\",\"idmateria\":\""+idMateria+"\"}";
	String[] datosMateria = { "-1", userBean.getLocation() };

	if (idJuzgado != null && idInstitucionJuzgado != null) {
		paramsJSON.put("idjuzgado", idJuzgado);
		paramsJSON.put("idinstitucion", idInstitucionJuzgado);
		paramsJSON.put(SelectDataService.IDINSTITUCION_JUZGADO_KEY, idInstitucionJuzgado);
		juzgadoSel.add("{\"idjuzgado\":\""+idJuzgado+"\",\"idinstitucion\":\""+idInstitucionJuzgado+"\"}");
		if (!idJuzgado.equals("")) {			
			datosJuzgado = datosJuzgado.substring(0, datosJuzgado.length() - 1);
			datosJuzgado += ",\"idjuzgado\":\""+idJuzgado+"\"}";
		}
	}
	if (idPretension != null) {
		pretensionSel.add(0, idPretension);
	}

	if (idMateria != null) {
		paramsJSON.put("idmateria", idMateria);
		paramsJSON.put("idarea", idArea);
		paramsJSON.put(SelectDataService.IDINSTITUCION_MATERIA_KEY, idinstitucion_tipoexpediente);
		materiaSel.add("{\"idmateria\":\""+idMateria+"\",\"idarea\":\""+idArea+"\",\"idinstitucion\":\""+idinstitucion_tipoexpediente+"\"}");		
	}

	//recupero los campos visibles para mostrar o no ciertas leyendas
	Hashtable camposVisibles = (Hashtable) request
			.getAttribute("camposVisibles");
	String sNumExpDisc = (String) camposVisibles.get("2");
	String sEstado = (String) camposVisibles.get("3");
	String sInstitucion = (String) camposVisibles.get("4");
	String sAsuntoJud = (String) camposVisibles.get("5");
	boolean bNumExpDisc = (sNumExpDisc != null && sNumExpDisc
			.equals("S"));
	boolean bEstado = (sEstado != null && sEstado.equals("S"));
	String recargarCombos = bEstado ? "recargarCombos()" : "";
	boolean bInstitucion = (sInstitucion != null && sInstitucion
			.equals("S"));
	boolean bAsuntoJud = (sAsuntoJud != null && sAsuntoJud.equals("S"));

	String idinst_idtipo_idfase = "";
	String idinst_idtipo_idfase_idestado = "";
	String idclasificacion = "";
	ArrayList<String> faseSel = new ArrayList<String>();
	String vFase = "";
	ArrayList<String> estadoSel = new ArrayList<String>();
	ArrayList<String> vClasif = new ArrayList<String>();

	
	String anioExpOrigen ="";
	String numExpOrigen ="";
	String accion = (String) request.getAttribute("accion");
	String copia = (String) request.getParameter("copia");
	if(copia==null)
		copia="";
	if (!accion.equals("nuevo")) { //pestanhas:edicion o consulta

		if (!copia.equals("s")) { //pestanhas:edicion o consulta
			idinst_idtipo_idfase = (String) request
					.getAttribute("idinst_idtipo_idfase");
			idinst_idtipo_idfase_idestado = (String) request
					.getAttribute("idinst_idtipo_idfase_idestado");
			idclasificacion = (String) request
					.getAttribute("idclasificacion");
			if (idinst_idtipo_idfase != null){
				String[] array_idinst_idtipo_idfase = idinst_idtipo_idfase.split(",");
				if (array_idinst_idtipo_idfase.length >= 3){
					paramsJSON.put("idfase", array_idinst_idtipo_idfase[2]);
				}
				if (array_idinst_idtipo_idfase.length >= 1){
					paramsJSON.put("idinstitucion", array_idinst_idtipo_idfase[0]);
					paramsJSON.put(SelectDataService.IDINSTITUCION_FASE_KEY, array_idinst_idtipo_idfase[0]);
				}
				if (array_idinst_idtipo_idfase.length >= 2){
					paramsJSON.put("idtipoexpediente", array_idinst_idtipo_idfase[1]);
				}
				if (array_idinst_idtipo_idfase.length >= 3){
					vFase = "{\"idfase\":\""+ array_idinst_idtipo_idfase[2]+"\",\"idinstitucion\":\""+ array_idinst_idtipo_idfase[0]+"\",\"idtipoexpediente\":\""+array_idinst_idtipo_idfase[1]+"\"}";
					faseSel.add(0, vFase);
				}
			}
			if(idinst_idtipo_idfase_idestado != null){
				String[] array_idinst_idtipo_idfase_idestado = idinst_idtipo_idfase_idestado.split(",");
				if (array_idinst_idtipo_idfase_idestado.length >= 4){
					paramsJSON.put("idestado", array_idinst_idtipo_idfase_idestado[3]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 3){
					paramsJSON.put("idfase", array_idinst_idtipo_idfase_idestado[2]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 1){
					paramsJSON.put("idinstitucion", array_idinst_idtipo_idfase_idestado[0]);
					paramsJSON.put(SelectDataService.IDINSTITUCION_ESTADO_EXPEDIENTE_KEY, array_idinst_idtipo_idfase_idestado[0]);
					paramsJSON.put(SelectDataService.IDINSTITUCION_CLASIFICACION_EXPEDIENTE_KEY, array_idinst_idtipo_idfase_idestado[0]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 2){
					paramsJSON.put("idtipoexpediente", array_idinst_idtipo_idfase_idestado[1]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 4){
					estadoSel.add("{\"idestado\":\""+ array_idinst_idtipo_idfase_idestado[3]+"\",\"idfase\":\""+ array_idinst_idtipo_idfase_idestado[2]+"\",\"idinstitucion\":\""+ array_idinst_idtipo_idfase_idestado[0]+"\",\"idtipoexpediente\":\""+array_idinst_idtipo_idfase_idestado[1]+"\"}");
					vClasif.add(idclasificacion);
				}
			}
		}
	}else{
		if (!copia.equals("s")) { //pestanhas:edicion o consulta
			idinst_idtipo_idfase = (String) request
					.getAttribute("idinst_idtipo_idfase");
			idinst_idtipo_idfase_idestado = (String) request
					.getAttribute("idinst_idtipo_idfase_idestado");
			idclasificacion = (String) request
					.getAttribute("idclasificacion");
			if (idinst_idtipo_idfase != null){
				String[] array_idinst_idtipo_idfase = idinst_idtipo_idfase.split(",");
				if (array_idinst_idtipo_idfase.length >= 3){
					paramsJSON.put("idfase", array_idinst_idtipo_idfase[2]);
				}
				if (array_idinst_idtipo_idfase.length >= 1){
					paramsJSON.put("idinstitucion", array_idinst_idtipo_idfase[0]);
					paramsJSON.put(SelectDataService.IDINSTITUCION_FASE_KEY, array_idinst_idtipo_idfase[0]);
				}
				if (array_idinst_idtipo_idfase.length >= 2){
					paramsJSON.put("idtipoexpediente", array_idinst_idtipo_idfase[1]);
				}
				if (array_idinst_idtipo_idfase.length >= 3){
					vFase = "{\"idfase\":\""+ array_idinst_idtipo_idfase[2]+"\",\"idinstitucion\":\""+ array_idinst_idtipo_idfase[0]+"\",\"idtipoexpediente\":\""+array_idinst_idtipo_idfase[1]+"\"}";
					faseSel.add(0, vFase);
				}
			}
			if(idinst_idtipo_idfase_idestado != null){
				String[] array_idinst_idtipo_idfase_idestado = idinst_idtipo_idfase_idestado.split(",");
				if (array_idinst_idtipo_idfase_idestado.length >= 4){
					paramsJSON.put("idestado", array_idinst_idtipo_idfase_idestado[3]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 3){
					paramsJSON.put("idfase", array_idinst_idtipo_idfase_idestado[2]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 1){
					paramsJSON.put("idinstitucion", array_idinst_idtipo_idfase_idestado[0]);
					paramsJSON.put(SelectDataService.IDINSTITUCION_ESTADO_EXPEDIENTE_KEY, array_idinst_idtipo_idfase_idestado[0]);
					paramsJSON.put(SelectDataService.IDINSTITUCION_CLASIFICACION_EXPEDIENTE_KEY, array_idinst_idtipo_idfase_idestado[0]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 2){
					paramsJSON.put("idtipoexpediente", array_idinst_idtipo_idfase_idestado[1]);
				}
				if (array_idinst_idtipo_idfase_idestado.length >= 4){
					estadoSel.add("{\"idestado\":\""+ array_idinst_idtipo_idfase_idestado[3]+"\",\"idfase\":\""+ array_idinst_idtipo_idfase_idestado[2]+"\",\"idinstitucion\":\""+ array_idinst_idtipo_idfase_idestado[0]+"\",\"idtipoexpediente\":\""+array_idinst_idtipo_idfase_idestado[1]+"\"}");
					vClasif.add(idclasificacion);
				}
			}			
		}
	}
	
	String tipoExp = (String) request.getParameter("idTipoExpediente");
	if(form.getIdTipoExpediente()!=null && !form.getIdTipoExpediente().trim().equalsIgnoreCase(""))
		tipoExp=form.getIdTipoExpediente();
	String editable = (String) request.getParameter("editable");
	String soloSeguimiento = (String) request
			.getParameter("soloSeguimiento");
	boolean bEditable = false;
	if (soloSeguimiento.equals("true")) {
		bEditable = false;
	} else {
		bEditable = editable.equals("1") ? true : false;
	}
	//boolean bEditable = editable.equals("1")? true : false;
	String boxStyle = bEditable ? "box" : "boxConsulta";
	String boxNumero = bEditable ? "boxNumber" : "boxConsultaNumber";
	// Estilo de los combos:

	String estiloCombo = null, readOnlyCombo = null;

	if (!bEditable) {
		estiloCombo = "boxConsulta";
		readOnlyCombo = "true";
	} else {
		estiloCombo = "boxCombo";
		readOnlyCombo = "false";
	}	

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute(
			"volverAuditoriaExpedientes");

	String seleccionarPersona = UtilidadesString.getMensajeIdioma(
			userBean, "general.boton.seleccionar");
	String nuevoNoCol = UtilidadesString.getMensajeIdioma(userBean,
			"general.new");
	String plazo = UtilidadesString.getMensajeIdioma(userBean,
			"general.boton.consultarplazo");

	ArrayList tipoIVASel = new ArrayList();
	ArrayList procedimientoSel = new ArrayList();
	String[] paramJuz = { userBean.getLocation() };

	String idTipoIVA = (String) request.getAttribute("idTipoIVA");
	String idProcedimiento = (String) request
			.getAttribute("idProcedimiento");
	String idInstitucionProcedimiento = (String) request
			.getAttribute("idInstitucionProcedimiento");

	String[] paramPro = new String[2];
	paramPro[0] = idJuzgado;
	paramPro[1] = idInstitucionJuzgado;

	String paramPretension = "{\"idpretension\":\"\"}";
	if (form.getIdPretension() != null
			&& (!form.getIdPretension().equals(""))){
		paramPretension = "{\"idpretension\":\""+form.getIdPretension()+"\"}";
		paramsJSON.put("idpretension", form.getIdPretension());
	}

	if (idProcedimiento != null && !"".equals(idProcedimiento) && idInstitucionProcedimiento != null && !"".equals(idInstitucionProcedimiento)) {
		paramsJSON.put("idprocedimiento", idProcedimiento);
		paramsJSON.put("idinstitucion", idInstitucionProcedimiento);
		paramsJSON.put(SelectDataService.IDINSTITUCION_PROCEDIMIENTO_KEY, idInstitucionProcedimiento);
		procedimientoSel.add(0, "{\"idprocedimiento\":\""+idProcedimiento+"\",\"idinstitucion\":\""+idInstitucionProcedimiento+"\"}");
	}

	if (idTipoIVA != null) {
		tipoIVASel.add(idTipoIVA);
	}
	String tituloDenunciado = (String) request
			.getAttribute("tituloDenunciado");
	String tituloDenunciadoMensajeError = "";
	if(tituloDenunciado == null || tituloDenunciado.equals(""))
		tituloDenunciadoMensajeError = "gratuita.busquedaEJG.interesado";
	else
		tituloDenunciadoMensajeError = tituloDenunciado;

	String tituloDenunciante = (String) request
			.getAttribute("tituloDenunciante");

	String nombreExpDisciplinario = (String) request
			.getAttribute("tituloExpDisciplinario");

	Vector vNombres= (Vector) request.getAttribute("nombres");	
	Vector vDatosCamposPestanas= (Vector) request.getAttribute("datosCamposPestanas");
	String nombreCampo = (String) request.getAttribute("nombreCampo");
	Vector vDatosCamposPestanasLongitud= (Vector) request.getAttribute("datosCamposPestanasLongitud");	
	Vector vNombresLongitud= (Vector) request.getAttribute("nombresLongitud");
	
	Vector vSaltoLinea= (Vector) request.getAttribute("saltoLinea");
	
	if(copia.equals("s")){
		numExpOrigen = request.getParameter("numeroExpediente");
		anioExpOrigen = request.getParameter("anioExpediente");		
	}
	
	if (idinst_idtipo_idfase_idestado != null && !"".equals(idinst_idtipo_idfase_idestado)){
		String[] array_idinst_idtipo_idfase_idestado = idinst_idtipo_idfase_idestado.split(",");
		paramsJSON.put("idinstitucion", idinstitucion_tipoexpediente);
		paramsJSON.put(SelectDataService.IDINSTITUCION_TIPO_EXPEDIENTE_KEY, idinstitucion_tipoexpediente);
		paramsJSON.put("idtipoexpediente", tipoExp);
		if (array_idinst_idtipo_idfase_idestado.length >= 3)
			paramsJSON.put("idfase", array_idinst_idtipo_idfase_idestado[2]);
		if (array_idinst_idtipo_idfase_idestado.length >= 4)
			paramsJSON.put("idestado", array_idinst_idtipo_idfase_idestado[3]);
	} else if (idinst_idtipo_idfase != null && !"".equals(idinst_idtipo_idfase)){
		String[] array_idinst_idtipo_idfase = idinst_idtipo_idfase.split(",");
		paramsJSON.put("idinstitucion", idinstitucion_tipoexpediente);
		paramsJSON.put(SelectDataService.IDINSTITUCION_TIPO_EXPEDIENTE_KEY, idinstitucion_tipoexpediente);
		paramsJSON.put("idtipoexpediente", tipoExp);
		if (array_idinst_idtipo_idfase.length >= 3)
			paramsJSON.put("idfase", array_idinst_idtipo_idfase[2]);
	} else if (idinstitucion_tipoexpediente != null && !"".equals(idinstitucion_tipoexpediente) && tipoExp != null && !"".equals(tipoExp)){
		paramsJSON.put("idinstitucion", idinstitucion_tipoexpediente);
		paramsJSON.put(SelectDataService.IDINSTITUCION_TIPO_EXPEDIENTE_KEY, idinstitucion_tipoexpediente);
		paramsJSON.put("idtipoexpediente", tipoExp);
	} else if (tipoExp != null && !"".equals(tipoExp)){
		paramsJSON.put("idtipoexpediente", tipoExp);
	}
	String paramJSONstring = UtilidadesString.createJsonString(paramsJSON);
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp" ></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="expedientes.auditoria.datosgenerales.literal.titulo" localizacion="expedientes.auditoria.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpDatosGeneralesForm" staticJavascript="false" />  
<%
  	if (bEstado) {
  %>
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	
<%
		} else {
	%>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
<%
	}
%>
	
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	
	
			<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		var jsEstadoViejo="";
		var jsEstadoNuevo="";
		
		//Asociada al boton Volver
		function accionVolver() 
		{
			<%if (busquedaVolver == null) {%>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<%} else if (busquedaVolver == null || busquedaVolver.equals("AB")) {%>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="buscarPor";
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE%>";
			<%} else if (busquedaVolver.equals("NB")) {%>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<%} else if (busquedaVolver.equals("AV")) {%>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="buscarPor";
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE%>";
			<%} else if (busquedaVolver.equals("N")) {%>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="abrir";	
			<%} else if (busquedaVolver.equals("A")) {%>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="abrirAvanzada";	 
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE%>";
			<%} else if (busquedaVolver.equals("Al")) {%>
				document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<%}%>
			document.forms[1].submit();	
		}

		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			var elemento=parent.document.getElementById('pestana.auditoriaexp.datosgenerales');
			parent.pulsar(elemento,'mainPestanas');
		}
		
		//Asociada al boton Guardar
		//aalg: añadida para quitar la validación con structs y poder sacar todos los mensajes juntos con un mensaje variable según el tipo de interesado
		function validarGuardar(){
			var mensajeError = "";
			
			//Comprobamos que exista el campo, que tenga valor y que tenga 4 digitos
			if(jQuery("#anioExpDisciplinario").length != 0 && jQuery("#anioExpDisciplinario").val()!="" && jQuery("#anioExpDisciplinario").val().length != 4){
				mensajeError = mensajeError + '<siga:Idioma key="fecha.error.anio"/> \n';
			}
			
			//Incidencia 177. Validacion del campo anioExpDisciplinario/numExpDisciplinario			
			if (jQuery("#numExpDisciplinario").length != 0 && jQuery("#anioExpDisciplinario").length != 0){
				if(jQuery("#numExpDisciplinario").val()!="" || jQuery("#anioExpDisciplinario").val()!=""){
					if (jQuery.isNumeric(jQuery("#numExpDisciplinario").val())==false || jQuery.isNumeric(jQuery("#anioExpDisciplinario").val())==false)
						mensajeError = mensajeError + '<siga:Idioma key="messages.general.aviso.valorCampo"/> <siga:Idioma key="<%=nombreExpDisciplinario%>" /> <siga:Idioma key="messages.general.aviso.numericoEntero"/> \n';
				}
			}
		
			if (jQuery("#clasificacion").val()=="")
				mensajeError = mensajeError + '<siga:Idioma key="expedientes.auditoria.literal.clasificacion"/> <siga:Idioma key="messages.campoObligatorio.error"/> \n';

			if (jQuery("#asunto").val()=="")
				mensajeError = mensajeError + '<siga:Idioma key="expedientes.auditoria.literal.asunto"/> <siga:Idioma key="messages.campoObligatorio.error"/> \n';
				
			if (jQuery("#comboFases").length != 0){
				if (jQuery("#comboFases").val()=="")
					mensajeError = mensajeError + '<siga:Idioma key="expedientes.auditoria.literal.fase"/> <siga:Idioma key="messages.campoObligatorio.error"/> \n';
			}
			
			if (jQuery("#comboEstados").length != 0){
				if (jQuery("#comboEstados").val()=="")
					mensajeError = mensajeError + '<siga:Idioma key="expedientes.auditoria.literal.estado"/> <siga:Idioma key="messages.campoObligatorio.error"/> \n';
			}
			
			if (jQuery("#fechaInicial").val()=="")
				mensajeError = mensajeError + '<siga:Idioma key="expedientes.auditoria.literal.fechainicial"/> <siga:Idioma key="messages.campoObligatorio.error"/> \n';
			
			if (jQuery("#idPersonaDenunciado").val()=="")
				mensajeError = mensajeError + '<siga:Idioma key="<%=tituloDenunciadoMensajeError%>"/> <siga:Idioma key="messages.campoObligatorio.error"/> \n';
			
			return mensajeError;
			
		}
		function accionGuardar() 
		{	
			sub();
			var mensajeError = validarGuardar();
			if (mensajeError == ""){
				if ((document.ExpDatosGeneralesForm.fechaInicial && document.ExpDatosGeneralesForm.fechaInicial.value != '') || (document.ExpDatosGeneralesForm.fechaFinal.value && document.ExpDatosGeneralesForm.fechaFinal.value!='')) {
					if (compararFecha (document.ExpDatosGeneralesForm.fechaInicial, document.ExpDatosGeneralesForm.fechaFinal) == 1) {
						mensaje = '<siga:Idioma key="messages.expediente.rangoFechasIniFin"/>';
						alert(mensaje);
						fin();
						return false;
					}
				}
				if ((document.ExpDatosGeneralesForm.fechaInicial && document.ExpDatosGeneralesForm.fechaInicial.value != '') || (document.ExpDatosGeneralesForm.fechaProrroga.value && document.ExpDatosGeneralesForm.fechaProrroga.value!='')) {
					if (compararFecha (document.ExpDatosGeneralesForm.fechaInicial, document.ExpDatosGeneralesForm.fechaProrroga) == 1) {
						mensaje = '<siga:Idioma key="messages.expediente.rangoFechasIniPro"/>';
						alert(mensaje);
						fin();
						return false;
					}
				}				
		
				if ((document.ExpDatosGeneralesForm.fechaFinal && document.ExpDatosGeneralesForm.fechaFinal.value != '') || (document.ExpDatosGeneralesForm.fechaProrroga.value && document.ExpDatosGeneralesForm.fechaProrroga.value!='')) {
					if (compararFecha (document.ExpDatosGeneralesForm.fechaFinal, document.ExpDatosGeneralesForm.fechaProrroga) == 1) {
						mensaje = '<siga:Idioma key="messages.expediente.rangoFechasFinPro"/>';
						alert(mensaje);
						fin();
						return false;
					}
				}
				if (document.forms[0].minuta){
					  document.forms[0].minuta.value = document.forms[0].minuta.value.replace(/,/,".");		
				}
				if (document.forms[0].importeTotal){
					  document.forms[0].importeTotal.value = document.forms[0].importeTotal.value.replace(/,/,".");
				}
				if (document.forms[0].importeIVA){
					  document.forms[0].importeIVA.value = document.forms[0].importeIVA.value.replace(/,/,".");
				}
				if (document.forms[0].minutaFinal){
					  document.forms[0].minutaFinal.value = document.forms[0].minutaFinal.value.replace(/,/,".");
				}
				if (document.forms[0].importeTotalFinal){
					  document.forms[0].importeTotalFinal.value = document.forms[0].importeTotalFinal.value.replace(/,/,".");
				}
				if (document.forms[0].solicitanteEJG){
					  document.forms[0].solicitanteEJG.value = document.forms[0].solicitanteEJG.value.replace(/,/,".");
				}
				if (document.forms[0].importeIVAFinal){
					  document.forms[0].importeIVAFinal.value = document.forms[0].importeIVAFinal.value.replace(/,/,".");
				}
				if (document.forms[0].porcentajeIVA){							
					 document.forms[0].porcentajeIVA.value = document.forms[0].porcentajeIVA.value.replace(/,/,".");	
					if (document.forms[0].porcentajeIVAFinal){	
					 	document.forms[0].porcentajeIVAFinal.value = document.forms[0].porcentajeIVA.value.replace(/,/,".");
					}							  
				}						

				if (document.forms[0].derechosColegiales){
					  document.forms[0].derechosColegiales.value = document.forms[0].derechosColegiales.value.replace(/,/,".");
				}
								
				<% 
				if (accion.equals("nuevo") || copia.equals("s")) { %>
					document.ExpDatosGeneralesForm.accion.value="nuevo";
					document.forms[0].modo.value="insertar";
					
				<%} else {%>
				document.ExpDatosGeneralesForm.accion.value="edicion";
					document.forms[0].modo.value="modificar";
				<%}%>
				
				document.forms[0].target="submitArea";	
				document.forms[0].submit();	
			}
			else{
				alert(mensajeError);
				fin();
				return false;
			}
		}		
		
		function relacionarConEJG() 
		{
			document.BusquedaPorTipoSJCSForm.tipo.value="EJG";
						
			var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	

			if (resultado != null && resultado.length >= 4)
			{
				//document.forms[0].ejgIdInstitucion.value=resultado[0];

				document.forms[0].anioExpDisciplinario.value=resultado[1];
				document.forms[0].numExpDisciplinario.value=resultado[2];
				document.forms[0].tipoExpDisciplinario.value=resultado[3];
				document.forms[0].modo.value="modificar";
				document.forms[0].submit();	
			}
		}
		
		function recargarComboJuzgado()
		{
			<%if (bEditable) {%> 
				<%if (bAsuntoJud) {
					String idJuzgadoJSON = "{\"idjuzgado\":\""+idJuzgado+"\",\"idinstitucion\":\""+idInstitucionJuzgado+"\"}";
				%>
			jQuery("#juzgado").val('<%=idJuzgadoJSON%>');
				<%}%>
			<%}%>
		}		
		
		function recargarCombos()
		{
			<%if (bEditable) {%> 
				<%if (bAsuntoJud) {
					String idMateriaJSON = "{\"idmateria\":\""+idMateria+"\",\"idarea\":\""+idArea+"\",\"idinstitucion\":\""+userBean.getLocation()+"\"}";
				%>
				//jQuery("#idMateria").val('<%=idMateriaJSON%>');
				//jQuery("#idMateria").change();
				<%}%>
				/*
				if(document.forms[0].idTipoIVA){// solo aparece el tipo IVA cuando está el campo minuta
					jQuery("#idTipoIVA").change();
				}
				*/
			<%}%>
		}
		
		function refrescarLocal()
		{	
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();
			
		
		}
		function seleccionarDenunciado()
		{
			//BNS DESCOMENTAR PARA PASAR LOS DATOS A LA MODAL Y PRECARGAR EL SELECCIONADO
			/*
			document.datosGeneralesForm.numIdentificacion.value=document.forms[0].nifDenunciado.value;
			document.datosGeneralesForm.colegiadoen.value=document.forms[0].idInstitucionOrigenDenunciado.value;
			document.datosGeneralesForm.idDireccion.value=document.forms[0].idDireccionDenunciado.value;
			*/
			var resultado=ventaModalGeneral("datosGeneralesForm","G");
			
			if (resultado!=undefined && resultado[0]!=undefined ){
				
				if(resultado[8]!=undefined && resultado[8] == 'Nuevo'){
					//Se cera desde cero
					document.forms[0].idPersonaDenunciado.value=resultado[0];
					document.forms[0].nColDenunciado.value=resultado[2];
					document.forms[0].nombreDenunciado.value=resultado[4];
					document.forms[0].primerApellidoDenunciado.value=resultado[5];
					document.forms[0].segundoApellidoDenunciado.value=resultado[6];
					document.forms[0].idInstitucionOrigenDenunciado.value=resultado[1];
					document.forms[0].nifDenunciado.value=resultado[3];
					document.forms[0].idDireccionDenunciado.value=resultado[7];
				
				}else{
					//Selecciona uno existente
					document.forms[0].idPersonaDenunciado.value=resultado[0];
					document.forms[0].nColDenunciado.value=resultado[1];
					document.forms[0].nombreDenunciado.value=resultado[2];
					document.forms[0].primerApellidoDenunciado.value=resultado[3];
					document.forms[0].segundoApellidoDenunciado.value=resultado[4];
					document.forms[0].idInstitucionOrigenDenunciado.value=resultado[5];
					document.forms[0].nifDenunciado.value=resultado[6];
					document.forms[0].idDireccionDenunciado.value=resultado[7];	
				}
			}
			//BNS DESCOMENTAR PARA PASAR LOS DATOS A LA MODAL Y PRECARGAR EL SELECCIONADO
			/*
			document.datosGeneralesForm.numIdentificacion.value="";
			document.datosGeneralesForm.colegiadoen.value="";
			document.datosGeneralesForm.idDireccion.value="";
			*/
		}
		function seleccionarDenunciante()
		{		
			//BNS DESCOMENTAR PARA PASAR LOS DATOS A LA MODAL Y PRECARGAR EL SELECCIONADO
			/*
			document.datosGeneralesForm.numIdentificacion.value=document.forms[0].nifDenunciante.value;
			document.datosGeneralesForm.colegiadoen.value=document.forms[0].idInstitucionOrigenDenunciante.value;
			document.datosGeneralesForm.idDireccion.value=document.forms[0].idDireccionDenunciante.value;
			*/
			var resultado=ventaModalGeneral("datosGeneralesForm","G");
			
			if (resultado!=undefined && resultado[0]!=undefined ){
				
				if(resultado[8]!=undefined && resultado[8] == 'Nuevo'){
					//Se cera desde cero
					document.forms[0].idPersonaDenunciante.value=resultado[0];
					document.forms[0].nColDenunciante.value=resultado[2];
					document.forms[0].nombreDenunciante.value=resultado[4];
					document.forms[0].primerApellidoDenunciante.value=resultado[5];
					document.forms[0].segundoApellidoDenunciante.value=resultado[6];
					document.forms[0].idInstitucionOrigenDenunciante.value=resultado[1];
					document.forms[0].nifDenunciante.value=resultado[3];
					document.forms[0].idDireccionDenunciante.value=resultado[7];
				
				}else{
					//Selecciona uno existente
					document.forms[0].idPersonaDenunciante.value=resultado[0];
					document.forms[0].nColDenunciante.value=resultado[1];
					document.forms[0].nombreDenunciante.value=resultado[2];
					document.forms[0].primerApellidoDenunciante.value=resultado[3];
					document.forms[0].segundoApellidoDenunciante.value=resultado[4];
					document.forms[0].idInstitucionOrigenDenunciante.value=resultado[5];
					document.forms[0].nifDenunciante.value=resultado[6];
					document.forms[0].idDireccionDenunciante.value=resultado[7];	
				}				
				
			}
			//BNS DESCOMENTAR PARA PASAR LOS DATOS A LA MODAL Y PRECARGAR EL SELECCIONADO
			/*
			document.datosGeneralesForm.numIdentificacion.value="";
			document.datosGeneralesForm.colegiadoen.value="";
			document.datosGeneralesForm.idDireccion.value="";
			*/
		}				
		/*	 		
		function traspasoDatos(resultado){
		 	if (resultado[0]==undefined)
		 		jQuery("#juzgado").val("");
			else
				jQuery("#juzgado").val(resultado[0]);
		 	jQuery("#juzgado").change();
		}
		*/
		
		function getPlazo(){
			if (validateExpDatosGeneralesForm(document.ExpDatosGeneralesForm)){
				document.forms[0].modo.value="abrirConParametros";
				document.forms[0].target="submitArea";	
				document.forms[0].submit();
			}
		}

		function formatNumber(value){
			if (!value) {
				value = 0;
			} else if (value.toString().indexOf(".", 0) == -1){
				value = value + ".00";
			} else if (value.toString().charAt(value.toString().length -1) == "."){
				value = value + "00";
			} else if (value.toString().charAt(value.toString().length -2) == "."){
				value = value + "0";
			}

			return value;
		}
		
		function calcularTotalMinuta () 
		{
			if(document.getElementById("minuta") != null){
				if(document.getElementById("minuta").value != ""){	
					iva = document.getElementById("porcentajeIVA").value.replace(/,/,".");
					if (!iva) {
						iva = 0;
					} 
					minuta = document.getElementById("minuta").value.replace(/,/,".");
					minuta = formatNumber(minuta);
		
					var b = eval(minuta) * eval(iva) / 100;
					var a = b + eval(minuta);
					a = Math.round(a*100)/100;
					if(document.getElementById("porcentajeIVA").value != ""){
						document.getElementById("importeIVA").value = b.toFixed(2).toString().replace(".",",");
					}else{						
						document.getElementById("importeIVA").value = "0,00";	
					}
					
					document.getElementById("importeTotal").value = a.toFixed(2).toString().replace(".",",");
					document.getElementById("minuta").value = minuta.replace(".",",");
				}else{
					document.getElementById("minuta").value = "";
					if(document.getElementById("porcentajeIVA").value != ""){
						document.getElementById("importeIVA").value = "0,00";
						document.getElementById("importeTotal").value = "0,00";
					}else{						
						document.getElementById("importeIVA").value = "";
						document.getElementById("importeTotal").value ="";
					}
				}					
			}
		}
		
		function calcularTotalMinuta2 () 
		{
			if(document.getElementById("porcentajeIVA") != null){
				if(document.getElementById("porcentajeIVA").value != ""){	
					iva = document.getElementById("porcentajeIVA").value.replace(/,/,".");
					iva = formatNumber(iva);
					minuta = document.getElementById("minuta").value.replace(/,/,".");
					minuta = formatNumber(minuta);
					
					var b = eval(minuta) * eval(iva) / 100;
					var a = b + eval(minuta);
					a = Math.round(a*100)/100;
					document.getElementById("porcentajeIVA").value = iva.replace(".",",");					
					calcularTotalMinuta ();
					if(document.getElementById("porcentajeIVAFinal") != null){
						document.getElementById("porcentajeIVAFinal").value = iva.replace(".",",");
						calcularTotalMinutaFinal ();
					}
				}else{
					
					document.getElementById("porcentajeIVA").value = "";
					if(document.getElementById("porcentajeIVAFinal") != null){
						document.getElementById("porcentajeIVAFinal").value = "";
					}
					
					if(document.getElementById("minuta").value != ""){							
						document.getElementById("importeIVA").value = "0,00";				
						document.getElementById("importeTotal").value = document.getElementById("minuta").value;
					}else{			
						document.getElementById("importeIVA").value = "";
						document.getElementById("importeTotal").value = "";
					}

					if(document.getElementById("minutaFinal") != null){
						if(document.getElementById("minutaFinal").value != ""){							
							document.getElementById("importeIVAFinal").value = "0,00";						
							document.getElementById("importeTotalFinal").value = document.getElementById("minutaFinal").value;
						}else{			
							document.getElementById("importeIVAFinal").value = "";
							document.getElementById("importeTotalFinal").value = "";
						}
					}						
				}
			}
		}

		function calcularTotalMinutaFinal () 
		{			
			if(document.getElementById("minutaFinal") != null){
				if(document.getElementById("minutaFinal").value != ""){
					iva = document.getElementById("porcentajeIVAFinal").value.replace(/,/,".");
					if (!iva) {
						iva = 0;
					} 
					minuta = document.getElementById("minutaFinal").value.replace(/,/,".");
					minuta = formatNumber(minuta);
					
					var b = eval(minuta) * eval(iva) / 100;
					var a = b + eval(minuta);
					a = Math.round(a*100)/100;	
						
					if(document.getElementById("porcentajeIVA") != null){
						if(document.getElementById("porcentajeIVA").value != ""){
							document.getElementById("importeIVAFinal").value = b.toFixed(2).toString().replace(".",",");
						}else{						
							document.getElementById("importeIVAFinal").value = "0,00";
						}
					}
					document.getElementById("importeIVAFinal").value = b.toFixed(2).toString().replace(".",",");
					document.getElementById("importeTotalFinal").value = a.toFixed(2).toString().replace(".",",");
					document.getElementById("minutaFinal").value = minuta.replace(".",",");
				}else{
					document.getElementById("minutaFinal").value = "";
					if(document.getElementById("porcentajeIVA") != null){						
						if(document.getElementById("porcentajeIVA").value != ""){	
							document.getElementById("importeIVAFinal").value = "0,00";
							document.getElementById("importeTotalFinal").value = "0,00";
						}else{						
							document.getElementById("importeIVAFinal").value = "";
							document.getElementById("importeTotalFinal").value = "";
						}
					}else{
						document.getElementById("importeTotalFinal").value = "";
						document.getElementById("importeIVAFinal").value = "";						
					}
				}				
			}
		}

		function formateoDerechos () 
		{			
			if(document.getElementById("derechosColegiales") != null){
				if( document.getElementById("derechosColegiales").value != ""){				
					derechos = document.getElementById("derechosColegiales").value.replace(/,/,".");
					derechos = formatNumber(derechos);					
					document.getElementById("derechosColegiales").value = derechos.replace(".",",");
				}
			}
		}

		function accionComunicar()
		{
			
			idPersonaDenunciado = '<%=form.getIdPersonaDenunciado()%>';
			idInstitucion= '<%=userBean.getLocation()%>';
			idInstitucionTipoExp= '<%=idinstitucion_tipoexpediente%>';
			idTipoExpediente= '<%=tipoExp%>';
			anioExpediente= '<%=form.getAnioExpediente()%>';
			numeroExpediente= '<%=form.getNumExpediente()%>';


		   	datos = "idInstitucion=="+idInstitucion +"##idInstitucionTipoExp=="+idInstitucionTipoExp +
 		   		 "##idTipoExp==" +idTipoExpediente+"##anioExpediente=="+anioExpediente 
 		   		 +"##numeroExpediente=="+numeroExpediente +"##idTipoInforme==EXP%%%";
			//"##idPersona=="+idPersonaDenunciado QUITO EL DENUNCIADO PRINCIPAL YA QUE solo sirve a efectos de interfaz de
			//la caratula del expediente, no de la gestion de informes y envios
			document.InformesGenericosForm.datosInforme.value =datos;
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			   		
		   	} 
		   	else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
		   		}
		   	}				
		}
		
		function limpiarFechas(){
			if(jsEstadoViejo==("")){
				jsEstadoViejo = document.ExpDatosGeneralesForm.comboEstados.value;
			}else{
				jsEstadoNuevo = document.ExpDatosGeneralesForm.comboEstados.value;
				if(jsEstadoNuevo!=jsEstadoViejo){
					document.getElementById("fechaInicial").value="";
					document.getElementById("fechaFinal").value="";
					jsEstadoViejo = document.ExpDatosGeneralesForm.comboEstados.value;
				}
			}
		}
		 function consultarEjg(){
			 //document.DefinirEJGForm.anio.value=document.getElementById("anioExpDisciplinario").value;
			 //document.DefinirEJGForm.numero.value=document.getElementById("numExpDisciplinario").value;
			// document.DefinirEJGForm.idTipoEJG.value=document.getElementById("tipoExpDisciplinario").value;
			 document.DefinirEJGForm.modo.value='ver';
			 document.DefinirEJGForm.submit();
			 	
		  }

		 function editarEjg(){
			 //document.DefinirEJGForm.anio.value=document.getElementById("anioExpDisciplinario").value;
			 //document.DefinirEJGForm.numero.value=document.getElementById("numExpDisciplinario").value;
			// document.DefinirEJGForm.idTipoEJG.value=document.getElementById("tipoExpDisciplinario").value;
			 document.DefinirEJGForm.modo.value='editar';
			 document.DefinirEJGForm.submit();
			 	
		  }
		 
			function accionAbrir() 
			{	
				sub();	
				var rel =document.ExpDatosGeneralesForm.relacionExpediente.value;
				var nombrerel =document.ExpDatosGeneralesForm.nombreRelacionExpediente.value;
				var numExpediente =document.ExpDatosGeneralesForm.numExpediente.value;
				var anioExpediente =document.ExpDatosGeneralesForm.anioExpediente.value;
				var tioExpedienteAnt ='<%=tipoExp%>';
						
				document.ExpDatosGeneralesForm.copia.value="s";	
				document.ExpDatosGeneralesForm.accion.value="nuevo";
				
				if (document.ExpDatosGeneralesForm.relacionExpediente.value != ""){
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&idInst=<%=idinstitucion_tipoexpediente%>"+"&idTipo="+rel+"&idNombreTipo="+nombrerel+"&numExp="+numExpediente+"&anioExp="+anioExpediente+"&tipoExpAnt="+tioExpedienteAnt+"&nombreTipo="+'<%=form.getTipoExpediente()%>';
					document.forms[1].modo.value="nuevoCopia";
					document.forms[1].copia.value="s";					
					document.forms[1].avanzada.value="<%=ClsConstants.DB_FALSE%>";
					document.forms[1].target="mainWorkArea";	
					document.forms[1].submit();			
					
				}
				
			}

		//Funcion asociada al cambio de foco de la fecha incial necesaria
		//para calcular la fecha de caducidad	
	
			
		function generarFechaCaducidad() {
			<%if (!(tiempoCaducidad.equals("")) && !(tiempoCaducidad.equals("0"))){%>
			
				if (document.forms[0].fecha.value.length==0){
					document.forms[0].fechaCaducidad.value = "";
				}  else {
	
						var fFecha = document.forms[0].fecha.value;
						
				var dt1  = fFecha.substring(0,2);
	
				var dt2  = fFecha.substring(3,5);
				var dt3  = fFecha.substring(6,10);
	
				var mes = parseInt (dt2) - 1;
	
	
				var Fecha= new Date(fFecha);
				var iYear = Fecha.getFullYear();	
				
				var hoy= new Date();
				hoy.setDate(dt1);
				hoy.setMonth(mes.toString());
				hoy.setFullYear(dt3);
	
	
				var sumarDias=parseInt('<%=tiempoCaducidad%>');
				var luego = hoy.addDays(sumarDias);
	
				
				if ((luego.getMonth()+1) < 10)
					iMonth = '0' + ((luego.getMonth()+1).toString());
					else iMonth = ((luego.getMonth()+1).toString()); 
				if (luego.getDate() < 10)
					iDay = '0' + luego.getDate().toString();
					else iDay = luego.getDate().toString();
	
				document.forms[0].fechaCaducidad.value = iDay + "/" + iMonth + "/" + luego.getFullYear();
	
				}

			<%}%>
		}

		Date.prototype.addDays = function (dias){
			var fecha1 = new Date(2011, 1,20);
			var fecha2 = new Date(2011, 1,21);
			var diferencia = fecha2.getTime() - fecha1.getTime();
			var luego = new Date();
			
			luego.setTime( this.getTime() + (dias * diferencia ) );
			return luego; 
		};
		jQuery(document).ready(function(){
			<%if (!(tiempoCaducidad.equals("")) && !(tiempoCaducidad.equals("0"))){%>
				generarFechaCaducidad();
				jQuery("#fechaCaducidad").attr("disabled", "disabled");
			<%}%>
			calcularTotalMinuta ();
			calcularTotalMinutaFinal ();
			<%=recargarCombos%>
		});

	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->	
</head>

<body class="detallePestanas">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCampos"  align="center" >
	
			<html:form action="/EXP_Auditoria_DatosGenerales" method="POST" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "tipoExpDisciplinario"/>
				<html:hidden property = "relacionExpediente"/>
				<html:hidden property = "nombreRelacionExpediente"/>
				<html:hidden property = "copia"/>
				<html:hidden property = "tipoExpediente"/>
				<input type="hidden" name="accion"	value="<%=accion%>">		

				<tr>				
					<td>	
						<siga:ConjCampos leyenda="expedientes.auditoria.literal.datosgenerales">
							<table class="tablaCampos" align="left" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="140px"></td>
									<td width="150px"></td>
									<td width="120px"></td>
									<td width="220px"></td>
									<td width="90px"></td>
									<td width="230px"></td>
								</tr>
													
								<tr>
<%
									if (!accion.equals("nuevo") && !copia.equals("s")) {
%>
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.nexpediente" />
										</td>
										<td class="labelTextValue">
											<!-- Se comenta por que no se quiere mostrar el número de Expediente  al usuario -->
											<!-- En caso de ser nuevo no se muestran ni el numExpediente ni el anioExpediente -->
	
											<bean:write name="ExpDatosGeneralesForm" property="anioExpediente" />&nbsp;/&nbsp;
											<bean:write	name="ExpDatosGeneralesForm" property="numExpediente" /> 
											<html:hidden name="ExpDatosGeneralesForm" property="numExpediente" /> 
											<html:hidden name="ExpDatosGeneralesForm" property="anioExpediente" />
										</td>
										
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.tipo" />
										</td>
										<td class="labelText">
											<html:text name="ExpDatosGeneralesForm" property="tipoExpediente" style="width:200px" styleClass="boxConsulta" readonly="true" styleId="tipoExpediente" />
										</td>
		
<%
									} else {
%>
										<html:hidden name="ExpDatosGeneralesForm" property="numExpediente" />
										<html:hidden name="ExpDatosGeneralesForm" property="anioExpediente" />
<%
									}
%>
									
									<td class="labelText">
										<siga:Idioma key="expedientes.gestionarExpedientes.fApert" />&nbsp(*)
									</td>
									<td class="labelText">
<%
										if (accion.equals("nuevo") || copia.equals("s")) {
%>
		 									<siga:Fecha nombreCampo="fecha" valorInicial="<%=fechaApertura%>"  postFunction="generarFechaCaducidad();" preFunction="generarFechaCaducidad();" />
			
<%
										} else {
%> 
											<html:text name="ExpDatosGeneralesForm" property="fecha" styleClass="boxConsulta" readonly="true" styleId="fecha" />
<%
										}
%>
									</td>
								</tr>
								
								<tr>		
<% 
									if (bNumExpDisc && !accion.equals("nuevo")) {
%>
										<td class="labelText">
											<siga:Idioma key="<%=nombreExpDisciplinario%>" />
										</td>
<%
										if (!bEditable) {
%>
											<td class="labelTextValue" styleClass="box" style="text-align: right">
												<bean:write name="ExpDatosGeneralesForm" property="anioExpDisciplinario" />&nbsp;/&nbsp;
<%
												if (codigoEjg != null && !codigoEjg.trim().equals("")) {
%> 
													<bean:write name="ExpDatosGeneralesForm" property="numExpDisciplinarioCalc" /> 
<%
												} else {
%> 
													<bean:write name="ExpDatosGeneralesForm" property="numExpDisciplinario" />
<%
												}

												if (codigoEjg != null && !codigoEjg.trim().equals("")) {
%>							
													<img id="iconoboton_consultar1" src="/SIGA/html/imagenes/bconsultar_off.gif" style="cursor: hand;" alt="Consultar" name="consultar_1" border="0" onClick="consultarEjg();">
<%
												}
%>
											</td>
<%
										} else {
											if (!tieneEjgRelacionado) {							
												if(copia.equals("s")) {
%>						
													<td class="labelTextValue">
														<html:text name="ExpDatosGeneralesForm" property="anioExpDisciplinario" styleId="anioExpDisciplinario" value="<%=anioExpOrigen%>" maxlength="4" styleClass="box" style="text-align:right;width:40px;" />
														&nbsp;/&nbsp;
														<html:text	name="ExpDatosGeneralesForm" property="numExpDisciplinario" styleId="numExpDisciplinario" value="<%=numExpOrigen%>" maxlength="6" styleClass="box" style="text-align:right;width:60px;" />
													</td>											
<%			
												} else { 
%>
			
													<td class="labelTextValue">
														<html:text name="ExpDatosGeneralesForm" property="anioExpDisciplinario" styleId="anioExpDisciplinario" maxlength="4" styleClass="box"	style="text-align:right;width:40px;" />
														&nbsp;/&nbsp;
														<html:text	name="ExpDatosGeneralesForm" property="numExpDisciplinario" styleId="numExpDisciplinario" maxlength="6" styleClass="box"	style="text-align:right;width:60px;" />
													</td>			
<%	
												}
													
											} else {
%>
												<td>
													<table>
														<tr>
															<td class="labelTextValue">
																<html:text name="ExpDatosGeneralesForm" property="anioExpDisciplinario" maxlength="4"
																	styleId="anioExpDisciplinario"
																	style="text-align:right;width:40px;" readonly="true"
																	styleClass="boxConsulta" />
																	&nbsp;/&nbsp;
																<html:text name="ExpDatosGeneralesForm" property="numExpDisciplinarioCalc" style="width:60px;"
																	styleId="numExpDisciplinarioCalc"
																	maxlength="6" readonly="true" styleClass="boxConsulta" /> 
																<html:hidden name="ExpDatosGeneralesForm" property="numExpDisciplinario" />
															</td>
															
<%
															if (codigoEjg != null && !codigoEjg.trim().equals("")) {
%>
															<td>
																<img id="iconoboton_consultar1" src="/SIGA/html/imagenes/bconsultar_off.gif"
																	style="cursor: hand;" alt="Consultar" name="consultar_1"
																	border="0" onClick="consultarEjg();"> 
																<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif"
																	style="cursor: hand;" alt="Editar" name="editar_1"
																	border="0" onClick="editarEjg();">
															</td>
<%
															}
%>
														</tr>
													</table>
												</td>
<%
											}
										}										
									} else {
%>
										<html:hidden name="ExpDatosGeneralesForm" property="numExpDisciplinario" /> <html:hidden name="ExpDatosGeneralesForm" property="anioExpDisciplinario" />
<%
									}
									
									if (bEstado) {
%>
										<td class="labelText">
											<siga:Idioma 	key="expedientes.auditoria.literal.clasificacion" />&nbsp;(*)
										</td>
										<td colspan="3" class="labelText">
											<siga:Select queryId="getClasificacionesExpediente" 
												id="clasificacion" 
												selectedIds="<%=vClasif%>" 
												params="<%=paramJSONstring%>"
												required="true"
												disabled="<%=String.valueOf(!bEditable)%>"/>		
										</td>
<%
									} else {
%>
										<html:hidden name="ExpDatosGeneralesForm" property="clasificacion" />
<%
									}
%>		
								</tr>
																
<%
								if (bInstitucion) {
%>
									<tr>
										<td class="labelText" style="display: none">
											<siga:Idioma key="expedientes.auditoria.literal.institucion" />
										</td>
										<td colspan="5" align="left" class="labelTextValue" style="display: none">
											<bean:write name="ExpDatosGeneralesForm" property="institucion" />
										</td>
									</tr>	
<%
								}
%>							
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.fechaCaducidad" />
									</td>
									<td class="labelText">
<%
										if (bEditable) {
											if 	(tiempoCaducidad == null || tiempoCaducidad.equals("") || tiempoCaducidad.equalsIgnoreCase("0")) { 
%>
												<siga:Fecha nombreCampo="fechaCaducidad" valorInicial="<%=form.getFechaCaducidad()%>" styleId="fechaCaducidad"/>
<%
											} else {
%>
												<html:text name="ExpDatosGeneralesForm" property="fechaCaducidad" styleId="fechaCaducidad" size="10" maxlength="10" styleClass="<%=boxStyle%>" readonly="true" tabindex="-1" />					 				 
<%
											}												
										} else {
%>
											<html:text name="ExpDatosGeneralesForm" property="fechaCaducidad" styleId="fechaCaducidad" size="10" maxlength="10" styleClass="<%=boxStyle%>" tabindex="-1" />				
<% 			
										}
%>	
									</td>
									
									<td class="labelText">
										<siga:Idioma 	key="expedientes.auditoria.literal.asunto" />&nbsp(*)
									</td>
									<td colspan="3" class="labelText">
										<html:text name="ExpDatosGeneralesForm" styleId="asunto" property="asunto" 
											maxlength="1024" style="width:520px" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>" />
									</td>
								</tr>

								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.observaciones" />
									</td>
									<td colspan="5" class="labelText">
										<html:textarea property="observaciones" 
											styleclass="<%=boxStyle%>" style="overflow-y:auto; overflow-x:hidden; width:800px; height:50px; resize:none;"
											onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" />											
									</td>
								</tr>								
							</table>
						</siga:ConjCampos>
	
<%
						if (bEstado) {							
%>
							<siga:ConjCampos leyenda="expedientes.auditoria.literal.estado">
								<table class="tablaCampos" align="center" border="0" cellpadding="0" cellspacing="0">
									<tr>					
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.fase"/>&nbsp(*)
										</td>				
										<td>
											<siga:Select queryId="getFasesExpediente" 
												id="comboFases" 
												queryParamId="idfase" 
												childrenIds="comboEstados" 
												disabled="<%=readOnlyCombo%>" 
												selectedIds="<%=faseSel%>" 
												params="<%=paramJSONstring%>"
												required="true"/>				
										</td>

										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.estadoyfechainicial"/>&nbsp(*)
										</td>			
										<td colspan="2">											
											<siga:Select queryId="getEstadosExpediente" 
												id="comboEstados" 
												queryParamId="idestado" 
												parentQueryParamIds="idfase" 
												params="<%=paramJSONstring%>"
												selectedIds="<%=estadoSel%>" 
												disabled="<%=readOnlyCombo%>" 
												required="true" 
												width="320"/>			
							 			</td>			
			
										<td>
<%
											if (bEditable) {
%>
												<siga:Fecha nombreCampo="fechaInicial" valorInicial="<%=form.getFechaInicial()%>"/>
												
<%
											} else {
%>
												<html:text name="ExpDatosGeneralesForm" property="fechaInicial" styleId="fechaInicial" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true" />
<%
											}
%>
										</td>			
									</tr>

									<tr>					
<%
										if (bEditable) {
%>
											<td class="labelText" colspan="2">
												<input type="button" class="button" alt="<%=plazo%>" id="searchDeadline"  name = "idButton" onclick="return getPlazo();" value="<%=plazo%>"/>&nbsp;
											</td>	
<%
										} else {
%>
											<td colspan="2">&nbsp;</td>
<%
										}
%>	
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.fechafinal"/>
										</td>
										<td>
<%
											if (bEditable) {
%>
												<siga:Fecha nombreCampo="fechaFinal" valorInicial="<%=form.getFechaFinal()%>"/>					
<%
											} else {
%>	
												<html:text name="ExpDatosGeneralesForm" property="fechaFinal" styleId="fechaFinal" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true" />
<%
											}
%>
										</td>		
			
										<td class="labelText" style="text-align:right; width:180px">
											<siga:Idioma key="expedientes.auditoria.literal.fechaprorroga"/>
										</td>
										<td>
<%
											if (bEditable) {
%>
												<siga:Fecha nombreCampo="fechaProrroga" valorInicial="<%=form.getFechaProrroga()%>"/>					
<%
											} else {
%>
												<html:text name="ExpDatosGeneralesForm" property="fechaProrroga" styleId="fechaProrroga" maxlength="10" size="10" styleClass="<%=boxStyle%>" readonly="true" />
<%
											}
%>
										</td>	
									</tr>
								</table>		
							</siga:ConjCampos>
<%
						} else {
%>	
							<html:hidden name="ExpDatosGeneralesForm" property="comboEstados"/>
							<html:hidden name="ExpDatosGeneralesForm" property="comboFases"/>
							<html:hidden name="ExpDatosGeneralesForm" property="estadoSel"/>
							<html:hidden name="ExpDatosGeneralesForm" property="faseSel"/>
							<html:hidden name="ExpDatosGeneralesForm" property="fechaInicial"/>
							<html:hidden name="ExpDatosGeneralesForm" property="fechaFinal"/>
							<html:hidden name="ExpDatosGeneralesForm" property="fechaProrroga"/>
<%
						}

						if (mostrarMinuta != null && mostrarMinuta.equalsIgnoreCase("S")) {
%>

							<siga:ConjCampos leyenda="expedientes.auditoria.literal.minuta">
								<table class="tablaCampos" border="0">
									<tr>
										<td class="labelText" width="150px">
											<siga:Idioma key="expedientes.auditoria.literal.minuta"/>
										</td>
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="minuta" styleId="minuta" size="10" maxlength="10" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterChars(this,false,true);" onkeyup="filterCharsUp(this);" onblur="calcularTotalMinuta();" /> &euro;
										</td>
										
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.porcentajeIVA"/>
										</td>				
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="porcentajeIVA" styleId="porcentajeIVA" size="6" maxlength="6" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterCharsNumberEs(this,false,true);" onkeyup="filterCharsUp(this);" onblur="calcularTotalMinuta2();" /> %
										</td>
										
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.importeIVA"/>
										</td>				
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="importeIVA" styleId="importeIVA" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true"  onblur="filterCharsNaN(this);" /> &euro;
										</td>
										
										<td class="labelText" width="10%">
											<siga:Idioma key="expedientes.auditoria.literal.totalMinuta"/>
										</td>				
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="importeTotal" styleId="importeTotal" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true" onblur="filterCharsNaN(this);" /> &euro;
										</td>
									</tr>
								</table>
							</siga:ConjCampos>	
<%
						}
						
						if (mostrarMinutaFinal != null && mostrarMinutaFinal.equalsIgnoreCase("S")) {
%>

							<siga:ConjCampos leyenda="expedientes.auditoria.literal.minutafinal">
								<table class="tablaCampos" border="0">
									<tr>
										<td class="labelText" width="150px">
											<siga:Idioma key="expedientes.auditoria.literal.minutafinal"/>
										</td>
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="minutaFinal" styleId="minutaFinal" size="10" maxlength="10" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterChars(this,false,true);" onkeyup="filterCharsUp(this);" onblur="calcularTotalMinutaFinal();" /> &euro;
										</td>
										
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.porcentajeIVA"/>
										</td>				
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="porcentajeIVAFinal" styleId="porcentajeIVAFinal" size="6" maxlength="6" styleClass="boxConsultaNumber" readonly="true" onblur="filterCharsNaN(this);" /> %
										</td>
										
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.importeIVA"/>
										</td>				
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="importeIVAFinal" styleId="importeIVAFinal" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true" onblur="filterCharsNaN(this);" /> &euro;
										</td>
										
										<td class="labelText" width="10%">
											<siga:Idioma key="expedientes.auditoria.literal.totalMinuta"/>
										</td>				
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="importeTotalFinal" styleId="importeTotalFinal" size="10" maxlength="10" styleClass="boxConsultaNumber" readonly="true" onblur="filterCharsNaN(this);" /> &euro;
										</td>
									</tr>
								
<%
								if (derechosColegiales != null && derechosColegiales.equalsIgnoreCase("S")) {
%>
									<tr>			
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.derechoscolegiales"/>
										</td>
										<td class="labelTextValue">
											<html:text name="ExpDatosGeneralesForm" property="derechosColegiales" styleId="derechosColegiales" size="10" maxlength="10" styleClass="<%=boxNumero%>" readonly="<%=!bEditable%>" onkeypress="filterCharsNumberEs(this,false,true);" onkeyup="filterCharsUp(this);" onblur="formateoDerechos();" /> &euro;
										</td>
									</tr>
<%
								}
%>								
								</table>
							</siga:ConjCampos>	
<%
						}
%>
	
						<siga:ConjCampos leyenda="<%=tituloDenunciado%>">
							<table class="tablaCampos" align="center" border="0">
								<tr>
									<td class="labelText" width="60px">
										<siga:Idioma key="<%=tipoIdentificacionDenunciado%>"/>&nbsp;&nbsp;(*)
									</td>				
									<td>				
										<html:text name="ExpDatosGeneralesForm" size="<%=nifTamanio%>" property="nifDenunciado" styleId="nifDenunciado" styleClass="boxConsulta" readonly="true" style="width:80px"  />
									</td>

									<td class="labelText" nowrap width="95px">
										<siga:Idioma key="expedientes.auditoria.literal.ncolegiado"/>			
									</td>
									<td>
										<html:text name="ExpDatosGeneralesForm" property="nColDenunciado" styleId="nColDenunciado" size="<%=numColegiadoTamanio%>" styleClass="boxConsulta" readonly="true" style="width:80px" />
									</td>

									<td class="labelText" width="60px">
										<html:hidden name="ExpDatosGeneralesForm" property = "idPersonaDenunciado" styleId="idPersonaDenunciado"/>
										<html:hidden name="ExpDatosGeneralesForm" property = "idInstitucionOrigenDenunciado" styleId="idInstitucionOrigenDenunciado"/>
										<html:hidden name="ExpDatosGeneralesForm" property = "idDireccionDenunciado" styleId="idDireccionDenunciado"/>
										
										<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
									</td>				
			
									<td>
										<html:text name="ExpDatosGeneralesForm" property="nombreDenunciado" styleId="nombreDenunciado" size="<%=nombreTamanio%>" styleClass="boxConsulta" readonly="true" style="width:120px"/>																								 				 																										
									</td>		
			
									<td>
										<html:text name="ExpDatosGeneralesForm" property="primerApellidoDenunciado" styleId="primerApellidoDenunciado" size="<%=ape1Tamanio%>" styleClass="boxConsulta" readonly="true" style="width:120px" />
									</td>
									
									<td>
										<html:text name="ExpDatosGeneralesForm" property="segundoApellidoDenunciado" styleId="segundoApellidoDenunciado" size="<%=ape2Tamanio%>"styleClass="boxConsulta" readonly="true" style="width:120px" />
									</td>			
							 			
<% 
									if (bEditable) { 
%>			
										<td align="right">				
											<input type="button" class="button" alt="<%=seleccionarPersona%>" id="newPerson" name = "idButton"  onclick="return seleccionarDenunciado();" value="<%=seleccionarPersona%>"/>				
										</td>	
<%
									}
%>			
								</tr>
							</table>		
						</siga:ConjCampos>
						
<%
						if ((!accion.equals("nuevo")) && mostrarSolicitanteEJG != null && mostrarSolicitanteEJG.equalsIgnoreCase("S")) {
%>
							<siga:ConjCampos leyenda="expedientes.auditoria.literal.solicitanteEJG">
								<table class="tablaCampos" align="center" border="0">
									<tr>					
										<td class="labelText" width="15%">
											<siga:Idioma key="expedientes.auditoria.literal.solicitanteEJG"/>
										</td>
										<td class="labelTextValor" width="85%">
											<html:text name="ExpDatosGeneralesForm" property="solicitanteEjgDescripcion" styleId="solicitanteEjgDescripcion" styleClass="boxConsulta" style="width:500px" readonly="true" />	
										</td>																						
									</tr>
								</table>
							</siga:ConjCampos>
<%
						} 

						if (mostrarDenunciante != null && mostrarDenunciante.equalsIgnoreCase("S")) {
%>
							<siga:ConjCampos leyenda="<%=tituloDenunciante%>">
								<table class="tablaCampos" align="center" border="0">
									<tr>
										<td class="labelText" width="60px">
											<siga:Idioma key="<%=tipoIdentificacionDenunciante%>"/>
										</td>															
										<td>				
											<html:text name="ExpDatosGeneralesForm" size="<%=nifDenuncianteTamanio%>"  property="nifDenunciante" styleId="nifDenunciante" styleClass="boxConsulta" readonly="true" style="width:80px" />
										</td>
							
										<td class="labelText" nowrap width="95px">
											<siga:Idioma key="expedientes.auditoria.literal.ncolegiado"/>			
										</td>
										<td>
											<html:text name="ExpDatosGeneralesForm" property="nColDenunciante" styleId="nColDenunciante" size="<%=nColDenuncianteTamanio%>" styleClass="boxConsulta" readonly="true" style="width:80px"  />
										</td>
							
										<td class="labelText" width="60px">
											<html:hidden name="ExpDatosGeneralesForm" property = "idPersonaDenunciante"/>
											<html:hidden name="ExpDatosGeneralesForm" property = "idInstitucionOrigenDenunciante"/>
											<html:hidden name="ExpDatosGeneralesForm" property = "idDireccionDenunciante"/>
											
											<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
										</td>				
										
										<td>
											<html:text name="ExpDatosGeneralesForm" property="nombreDenunciante" styleId="nombreDenunciante" size="<%=nombreDenuncianteTamanio%>" styleClass="boxConsulta" readonly="true" style="width:120px" />																								 				 																										
										</td>		
										
										
										<td>
											<html:text name="ExpDatosGeneralesForm" property="primerApellidoDenunciante" styleId="primerApellidoDenunciante" size="<%=ape1DenuncianteTamanio%>" styleClass="boxConsulta" readonly="true" style="width:120px" />
										</td>
										
										<td>
											<html:text name="ExpDatosGeneralesForm" property="segundoApellidoDenunciante" styleId="segundoApellidoDenunciante" size="<%=ape2DenuncianteTamanio%>"styleClass="boxConsulta" readonly="true" style="width:120px" />
										</td>
																								 			
<% 
										if (bEditable) { 
%>			
											<td align="right">				
												<input type="button" class="button" alt="<%=seleccionarPersona%>" id="newPerson" name = "idButton"  onclick="return seleccionarDenunciante();" value="<%=seleccionarPersona%>"/>				
											</td>	
<%
										}
%>			
									</tr>
								</table>						
							</siga:ConjCampos>
<%
						}

						if (bAsuntoJud) {
%>
							<siga:ConjCampos leyenda="expedientes.auditoria.literal.asuntojudicial">
								<table class="tablaCampos" align="center" border="0">
									<tr>					
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.materia"/>
										</td>				
										<td>
											<siga:Select queryId="getMateriaAreaExpediente" 
												id="idMateria"
												selectedIds="<%=materiaSel%>"
												params="<%=paramJSONstring%>"
												queryParamId="idmateria,idarea"
												childrenIds="juzgado"
												disabled="<%=readOnlyCombo%>"
												width="250"/>			
										</td>
										
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.juzgado"/>
										</td>				
										<td>										
											<siga:Select id="juzgado" 
												queryParamId="idjuzgado" 
												queryId="getJuzgadosMateria" 
												parentQueryParamIds="idmateria,idarea" 
												childrenIds="procedimiento" 
												params="<%=paramJSONstring%>"
												showSearchBox="true" 
												searchkey="CODIGOEXT2" 
												searchBoxMaxLength="10" 
												searchBoxWidth="10" 
												selectedIds="<%=juzgadoSel%>" 
												disabled="<%=readOnlyCombo%>" 
												width="230" />
										</td>
									</tr>		
												
									<tr>					
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.procedimiento"/>
										</td>
										<td>
											<siga:Select queryId="getProcedimientos" 
												id="procedimiento"
												parentQueryParamIds="idjuzgado"
												params="<%=paramJSONstring%>"
												selectedIds="<%=procedimientoSel%>"
												readOnly="<%=readOnlyCombo%>"
												width="250"/>
										</td>
									
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.nasunto"/>
										</td>
										<td>
											<html:text name="ExpDatosGeneralesForm" property="numAsunto" styleId="numAsunto" size="10" maxlength="20" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>" />
										</td>	
									</tr>
							
									<tr>														
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.pretensiones"/>
										</td>
										<td>
											<siga:Select queryId="getPretensiones" 
														id="idPretension"							
														selectedIds="<%=pretensionSel%>"
														params="<%=paramJSONstring%>"
														readOnly="<%=readOnlyCombo%>"
														width="250"/>
										</td>
										
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.otrasPretensiones"/>
										</td>
										<td>
											<html:text name="ExpDatosGeneralesForm" property="otrasPretensiones" styleId="otrasPretensiones" size="50" maxlength="500" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>" />
										</td>	
									</tr>
								</table>									
							</siga:ConjCampos>
<%
						} else {
%>	
							<html:hidden name="ExpDatosGeneralesForm" property="idMateria"/>
							<html:hidden name="ExpDatosGeneralesForm" property="juzgado"/>
							<html:hidden name="ExpDatosGeneralesForm" property="procedimiento"/>
							<html:hidden name="ExpDatosGeneralesForm" property="numAsunto"/>
<%
						}

						if (vNombres!=null && vNombres.size()>0) {	
%>	
							<siga:ConjCampos leyenda="<%=nombreCampo%>">
								<table class="tablaCampos" border="0">
<% 
									vSaltoLinea.set(4,"4");
									for (int k=0 ; k<vNombres.size(); ) {		
%>					
										<tr>		
											<td class="labelText">				
<%					
												for(;k < vNombres.size() && vSaltoLinea.elementAt(k).equals("");k++) {
													ExpCampoConfBean campoConf = (ExpCampoConfBean)vNombres.elementAt(k);
													if(campoConf.getNombre()!=null && !"".equals(campoConf.getNombre())) {
%>										
														<%= campoConf.getNombre()%>
														&nbsp;
<% 
														String sNombreCampo = "campo"+(k+1);
														if (GenTipoCampoBean.ID_TIPO_ALFANUMERICO.equals(campoConf.getTipo())) { 
															if (campoConf.getMaxLong() > 100) { 
%>
																<textarea name="<%=sNombreCampo%>" 
																	onKeyDown="cuenta(this,<%=campoConf.getMaxLong()%>)" onChange="cuenta(this,<%=campoConf.getMaxLong()%>)" 
																	class="<%=boxStyle%>"  style="overflow-y:auto; overflow-x:hidden; width:860px; height:50px; resize:none;"><%=vDatosCamposPestanas.elementAt(k)%></textarea>																
<%
															} else { 
%>
																<input type="text" name="<%=sNombreCampo%>" value="<%=vDatosCamposPestanas.elementAt(k)%>"  size="<%=vDatosCamposPestanasLongitud.elementAt(k)%>" class ="<%=boxStyle %>" maxlength="<%=campoConf.getMaxLong()%>"></input>
<%
															} 
														} else if (GenTipoCampoBean.ID_TIPO_NUMERICO.equals(campoConf.getTipo())) { 
%>
															<input type="text" name="<%=sNombreCampo%>" value="<%=vDatosCamposPestanas.elementAt(k)%>"  size="<%=vDatosCamposPestanasLongitud.elementAt(k)%>" class ="<%=boxStyle %>" maxlength="<%=campoConf.getMaxLong()%>" onkeypress="return soloDigitos(event)"></input>
<%
														} else if (GenTipoCampoBean.ID_TIPO_FECHA.equals(campoConf.getTipo())) { 
															if (!"".equals(vDatosCamposPestanas.elementAt(k))) { 
%>
																<siga:Fecha nombreCampo="<%=sNombreCampo%>" valorInicial="<%=(String)vDatosCamposPestanas.elementAt(k)%>" disabled="<%=readOnlyCombo%>"/>
<%
															} else { 
%>
																<siga:Fecha nombreCampo="<%=sNombreCampo%>" disabled="<%=readOnlyCombo%>"/>
<%
															} 
														} 
%>
														&nbsp;																										
<%
													}
    											}
    				
												if(k!=5)
    												vSaltoLinea.set(k,"");
%>													
											</td>
										</tr>	
<%
									} 
%>															
								</table>
							</siga:ConjCampos>
<%		
    					}
%>		
			
					</td>
				</tr>
			</html:form>
		</table>
		<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
<%
		if (accion.equals("nuevo")  || copia.equals("s")) {
%>
			<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle" />
<%
		} else if (bEditable) {
			if (tieneEjgRelacionado) {
				if (tieneExpeRelacionado) {//Se pone como inputs ya que no es posible darle un nombre exacto por que depende del tipo de expediente
%>
					<table class="botonesDetalle" align="center">
						<tr>
							<td class="tdBotones">
								<input type="button" alt="<siga:Idioma key="general.boton.volver"/>"  id="idButton" onclick="return accionVolver();" class="button" name="idButton" value="<siga:Idioma key="general.boton.volver"/>">
							</td>
							<td  style="width:900px;">
								&nbsp;
							</td>								
							<td class="tdBotones">
								<input type="button" alt="<%=nombreRelacion %>"  id="idButton" onclick="return accionAbrir();" class="button" name="idButton" value="Abrir <%=nombreRelacion %>">
							</td>
							
							<td class="tdBotones">
								<input type="button" alt="<siga:Idioma key="general.boton.relacionarEJG"/>"  id="idButton" onclick="return relacionarConEJG();" class="button" name="idButton" value="<siga:Idioma key="general.boton.relacionarEJG"/>">
							</td>							
							<td class="tdBotones">
								<input type="button" alt="<siga:Idioma key="general.boton.guardar"/>"  id="idButton" onclick="return accionGuardar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.guardar"/>">
							</td>
							<td class="tdBotones">
								<input type="button"  alt="<siga:Idioma key="general.boton.restablecer"/>"  id="idButton" onclick="return accionRestablecer();" class="button" name="idButton" value="<siga:Idioma key="general.boton.restablecer"/>">
							</td>
							<td class="tdBotones">
								<input type="button" alt="<siga:Idioma key="general.boton.comunicar"/>"  id="idButton" onclick="return accionComunicar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.comunicar"/>">
							</td>
						</tr>
					</table>
<%
				} else {
%>
					<siga:ConjBotonesAccion botones="V,R,G,COM,RE" clase="botonesDetalle" />
<%
				}
				
			} else {
				if (tieneExpeRelacionado) {//Se pone como inputs ya que no es posible darle un nombre exacto por que depende del tipo de expediente
%>
					<table class="botonesDetalle" align="center">
						<tr>
							<td class="tdBotones">
								<input type="button" alt="<siga:Idioma key="general.boton.volver"/>"  id="idButton" onclick="return accionVolver();" class="button" name="idButton" value="<siga:Idioma key="general.boton.volver"/>">
							</td>
							<td  style="width:900px;">
								&nbsp;
							</td>
							<td class="tdBotones">
								<input type="button" alt="<%=nombreRelacion %>"  id="idButton" onclick="return accionAbrir();" class="button" name="idButton" value="Abrir <%=nombreRelacion %>">
							</td>
							<td class="tdBotones">
								<input type="button" alt="<siga:Idioma key="general.boton.guardar"/>"  id="idButton" onclick="return accionGuardar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.guardar"/>">
							</td>
							<td class="tdBotones">
								<input type="button"  alt="<siga:Idioma key="general.boton.restablecer"/>"  id="idButton" onclick="return accionRestablecer();" class="button" name="idButton" value="<siga:Idioma key="general.boton.restablecer"/>">
							</td>
							<td class="tdBotones">
								<input type="button" alt="<siga:Idioma key="general.boton.comunicar"/>"  id="idButton" onclick="return accionComunicar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.comunicar"/>">
							</td>
						</tr>
					</table>
<%
				} else {
%>
					<siga:ConjBotonesAccion botones="V,R,G,COM" clase="botonesDetalle" />
<%
				}
			}
	
		} else {
%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
<%
		}
%>	
	</div>	

	<html:form action="/EXP_AuditoriaExpedientes" method="POST" target="mainWorkArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "avanzada" value = ""/>	
		<html:hidden property = "numeroExpediente" value = ""/>
		<html:hidden property = "anioExpediente" value = ""/>	
		<input type="hidden" name="copia"	value="">		
	</html:form>

	<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes"	value="1">
	</html:form>
		
	<html:form action="/EXP_Auditoria_Denunciado" method="POST" target="mainWorkArea">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "modo" value = "altaNoColegiado"/>
		<html:hidden property = "modal" value = "seleccion"/>
		<html:hidden property = "idPersona" />
		<html:hidden property = "idDireccion"/>
		<html:hidden property = "idInstitucion"/>
	</html:form>

	<html:form action = "/JGR_MantenimientoJuzgados" method="POST" target="submitArea33">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
	
	<html:form action="/CEN_DatosGenerales" method="POST" target="mainWorkArea">
		<input type="hidden" name="actionModal" value="1">
		<input type="hidden" name="modo" value="designarArt27">
		<input type="hidden" name="numIdentificacion">
		<input type="hidden" name="colegiadoen">
		<input type="hidden" name="idDireccion">
	</html:form>
	
	<html:form action="/JGR_EJG" method="POST" target="mainWorkArea">
		<html:hidden property = "modo"  />
		<html:hidden property = "anio"  value="<%=anioExpD%>"/>
		<html:hidden property = "numero"  value="<%=numExpD%>"/>
		<html:hidden property = "idTipoEJG" value="<%=tipoExpD%>"/>
		<html:hidden property = "idInstitucion" value="<%=userBean.getLocation()%>"/>
	</html:form>
	
	<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea"  style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrir">
		<input type="hidden" name="tipo"        value="">
	</html:form>
	
	<html:form action="/EXP_NuevoExpediente.do" method="POST" target="submitArea">
		<html:hidden property = "hiddenFrame" value = "1"/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idTipoExpediente"/>			
	</html:form>
		
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
		<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
		<html:hidden property="idTipoInforme" value="EXP"/>
		<html:hidden property="enviar" value="1" />
		<html:hidden property="descargar" value="1"/>
		<html:hidden property="datosInforme"/>
		<html:hidden property="modo" value = "preSeleccionInformes"/>
		<input type='hidden' name='actionModal'>
	</html:form>	
	
	<!-- Formulario para la edicion del envio -->
	<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="tablaDatosDinamicosD" value="">
	</form>

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<iframe name="submitArea33" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>