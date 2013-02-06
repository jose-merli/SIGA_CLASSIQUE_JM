<!-- operarEJG.jsp --> 
<!-- CABECERA JSP  -->
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>

<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	int ejisActivo = 0;
	if (request.getAttribute("EJIS_ACTIVO")!=null){
		ejisActivo = Integer.parseInt(request.getAttribute("EJIS_ACTIVO").toString());
	}		
	
	Integer PCAJG_ACTIVADO =(Integer) (request.getAttribute("PCAJG_ACTIVO"));
	String pintarAsterisco="&nbsp;(*)";
	boolean tipoEJGColegioObligatorio = PCAJG_ACTIVADO!=null && (PCAJG_ACTIVADO.intValue() == 2 || PCAJG_ACTIVADO.intValue() == 3 || PCAJG_ACTIVADO.intValue() == 4 || PCAJG_ACTIVADO.intValue() == 5);  
	String nombreDesignacion="";

	// Validamos si es una consulta o no.
	String modo = (String) request.getAttribute("MODO");
	String NOMBREESTADO = (String) request.getAttribute("NOMBREESTADO");
	String[] dato = { usr.getLocation() };
	String idInstitucion = usr.getLocation();
	if(usr.isComision()) modo="ver";
	
	String numtramidesig = "";
	String nombretramidesig = "";
	boolean designaExiste = false;
	// Obtenemos el resultado
	String ANIO = "", CODIGO = "", NUMERO = "", TIPOEJG = "", IDTIPOEJG = "", IDTIPOEJGCOLEGIO = "", TURNO = "", GUARDIA = "", NIFASISTIDO = "", NOMBREASISTIDO = "", APELLIDO1ASISTIDO = "", APELLIDO2ASISTIDO = "", NUMEROCOLEGIADO = "", NOMBRELETRADO = "", APELLIDO1LETRADO = "", APELLIDO2LETRADO = "", ANIOSOJ = "", NUMEROSOJ = "", TIPOSOJ = "", IDTIPOSOJ = "", FECHAAPERTURA = "", DESIGNA_ANIO = "", DESIGNA_NUMERO = "", DESIGNA_TURNO_NOMBRE = "", NOMBRETURNO = "", FECHAAPERTURASOJ = "", TIPOASISTENCIA = "", NUMEROASISTENCIA = "", FECHAPRESENTACION = "", FECHALIMITEPRESENTACION = "", OBSERVACIONES = "", DELITOS = "", TIPOEJGCOLEGIO = "", PROCURADOR = "", IDGUARDIA = "", CREADODESDE = "", ASISTENCIA_ANIO = "", ASISTENCIA_NUMERO = "", ASISTENCIAFECHA = "", FECHAENTRADADESIGNA = "", FECHARATIFICACION = "", PROCURADORNECESARIO = "", idProcurador = "", idInstitucionProcurador = "", numeroCAJG = "", anioCAJG = "", calidad = "", DESIGNA_CODIGO = "", CODIGOSOJ = "", IDPERSONA = "", procuradorNombreCompleto = "";
    String procuradorNumColegiado = "", procuradorSel = "", nombreCompleto = "", ESTADO="", SUFIJO="", NIG="";
	String idPretension = "", idPretensionInstitucion = "", pretension = "";

	ArrayList TIPOEJGCOLEGIOSEL = new ArrayList();
	ArrayList pretensionesSel = new ArrayList();
	Hashtable hash = (Hashtable) request.getSession().getAttribute(
			"DATABACKUP");
	
	
	try {
		ESTADO = hash.get("ESTADO").toString();
		ANIO = hash.get("ANIO").toString();
		CODIGO = hash.get("NUMEJG").toString();
		NUMERO = hash.get("NUMERO").toString();
		SUFIJO = hash.get("SUFIJO").toString();
		IDTIPOEJG = hash.get("IDTIPOEJG").toString();
		TURNO = hash.get("NOMBRETURNO").toString();
		GUARDIA = hash.get("NOMBREGUARDIA").toString();
		FECHAAPERTURA = GstDate.getFormatedDateShort("", hash.get(
			"FECHAAPERTURA").toString());
		TIPOEJG = hash.get("TIPOEJG").toString();
		NUMEROCOLEGIADO = hash.get("NCOLEGIADO").toString();
		IDPERSONA = hash.get("IDPERSONA").toString();
		PROCURADORNECESARIO = hash.get("PROCURADORNECESARIO")
				.toString();
		IDGUARDIA = hash.get("IDGUARDIA").toString();
		NIG = hash.get("NIG").toString();
		
		calidad = hash.get("CALIDAD").toString();	
		if (hash.containsKey("IDTIPOEJGCOLEGIO"))
			IDTIPOEJGCOLEGIO = hash.get("IDTIPOEJGCOLEGIO").toString();
		if (hash.containsKey("PROCURADOR"))
			PROCURADOR = hash.get("PROCURADOR").toString();
		if (hash.containsKey("NOMBRELETRADO"))
			NOMBRELETRADO = hash.get("NOMBRELETRADO").toString();
		if (hash.containsKey("APELLIDO1LETRADO"))
			APELLIDO1LETRADO = hash.get("APELLIDO1LETRADO").toString();
		if (hash.containsKey("APELLIDO2LETRADO"))
			APELLIDO2LETRADO = hash.get("APELLIDO2LETRADO").toString();
		if (hash.containsKey("CREADODESDE"))
			CREADODESDE = hash.get("CREADODESDE").toString();
		if (hash.containsKey("NUMERO_CAJG"))
			numeroCAJG = hash.get("NUMERO_CAJG").toString();
		if (hash.containsKey("ANIOCAJG"))
			anioCAJG = hash.get("ANIOCAJG").toString();
		if (hash.containsKey("NIFASISTIDO"))
			NIFASISTIDO = hash.get("NIFASISTIDO").toString();
		if (hash.containsKey("NOMBREASISTIDO"))
			NOMBREASISTIDO = hash.get("NOMBREASISTIDO").toString();
		if (hash.containsKey("APELLIDO1ASISTIDO"))
			APELLIDO1ASISTIDO = hash.get("APELLIDO1ASISTIDO")
					.toString();
		if (hash.containsKey("APELLIDO2ASISTIDO"))
			APELLIDO2ASISTIDO = hash.get("APELLIDO2ASISTIDO")
					.toString();
		if (hash.containsKey("ANIOSOJ"))
			ANIOSOJ = hash.get("ANIOSOJ").toString();
		if (hash.containsKey("NUMEROSOJ"))
			NUMEROSOJ = hash.get("NUMEROSOJ").toString();
		if (hash.containsKey("TIPOSOJ"))
			TIPOSOJ = hash.get("TIPOSOJ").toString();
		if (hash.containsKey("IDTIPOSOJ"))
			IDTIPOSOJ = hash.get("IDTIPOSOJ").toString();
		if (hash.containsKey("FECHAAPERTURASOJ"))
			FECHAAPERTURASOJ = GstDate.getFormatedDateShort("", hash
					.get("FECHAAPERTURASOJ").toString());
		if (hash.containsKey("FECHARATIFICACION"))
			FECHAAPERTURASOJ = (String) hash.get("FECHARATIFICACION");
		if (hash.containsKey("DESIGNA_ANIO"))
			DESIGNA_ANIO = hash.get("DESIGNA_ANIO").toString();
		if (hash.containsKey("DESIGNA_NUMERO"))
			DESIGNA_NUMERO = hash.get("DESIGNA_NUMERO").toString();
		if (hash.containsKey("DESIGNA_TURNO_NOMBRE"))
			DESIGNA_TURNO_NOMBRE = hash.get("DESIGNA_TURNO_NOMBRE")
					.toString();

		if (hash.containsKey("CODIGO"))
			DESIGNA_CODIGO = hash.get("CODIGO").toString();
		
		if (hash.containsKey("NOMBRETURNO"))
			NOMBRETURNO = hash.get("NOMBRETURNO").toString();
		if (hash.containsKey("FECHAENTRADADESIGNA"))
			FECHAENTRADADESIGNA = GstDate.getFormatedDateShort("", hash
					.get("FECHAENTRADADESIGNA").toString());
		if (hash.containsKey("ANIOASISTENCIA"))
			ASISTENCIA_ANIO = hash.get("ANIOASISTENCIA").toString();
		if (hash.containsKey("TIPOASISTENCIA"))
			TIPOASISTENCIA = hash.get("TIPOASISTENCIA").toString();
		if (hash.containsKey("ASISTENCIANUMERO"))
			NUMEROASISTENCIA = hash.get("ASISTENCIANUMERO").toString();
		if (hash.containsKey("CODIGOSOJ"))
			CODIGOSOJ = hash.get("CODIGOSOJ").toString();
		nombreCompleto = NOMBRELETRADO + " " + APELLIDO1LETRADO + " "
				+ APELLIDO2LETRADO;
		TIPOEJGCOLEGIO = hash.get("TIPOEJGCOLEGIO").toString();
		if (hash.containsKey("FECHAPRESENTACION"))
			FECHAPRESENTACION = GstDate.getFormatedDateShort("", hash
					.get("FECHAPRESENTACION").toString());
		if (hash.containsKey("FECHALIMITEPRESENTACION")
				&& (hash.get("FECHALIMITEPRESENTACION") != ""))
			FECHALIMITEPRESENTACION = GstDate.getFormatedDateShort("",
					hash.get("FECHALIMITEPRESENTACION").toString());
		if (hash.containsKey("OBSERVACIONES"))
			OBSERVACIONES = hash.get("OBSERVACIONES").toString();
		if (hash.containsKey("DELITOS"))
			DELITOS = hash.get("DELITOS").toString();
		if (hash.containsKey("PROCURADOR"))
			PROCURADOR = hash.get("PROCURADOR").toString();
		if (hash.containsKey("FECHARATIFICACION"))
			FECHARATIFICACION = hash.get("FECHARATIFICACION")
					.toString();

		// Datos pretensiones seleccionado:
		if (hash.containsKey(ScsEJGBean.C_IDPRETENSION))
			idPretension = hash.get(ScsEJGBean.C_IDPRETENSION)
					.toString();
		if (hash.containsKey(ScsEJGBean.C_IDPRETENSIONINSTITUCION))
			idPretensionInstitucion = hash.get(
					ScsEJGBean.C_IDPRETENSIONINSTITUCION).toString();

		// Datos del procurador seleccionado:
		if (hash.containsKey("IDPROCURADOR"))
			idProcurador = hash.get("IDPROCURADOR").toString();
		if (hash.containsKey("IDINSTITUCION_PROC"))
			idInstitucionProcurador = hash.get("IDINSTITUCION_PROC")
					.toString();
		if (hash.containsKey("IDPROCURADOR")
				&& hash.containsKey("IDINSTITUCION_PROC"))
			procuradorSel = idProcurador + ","
					+ idInstitucionProcurador;
		if ((String) request.getAttribute("PROCURADOR_NUM_COLEGIADO") != null)
			procuradorNumColegiado = (String) request
					.getAttribute("PROCURADOR_NUM_COLEGIADO");
		if ((String) request.getAttribute("PROCURADOR_NOMBRE_COMPLETO") != null)
			procuradorNombreCompleto = (String) request
					.getAttribute("PROCURADOR_NOMBRE_COMPLETO");
	} catch (Exception e) {
	}
	;

	// Datos de la designa
	String designaAnio = hash.get("DESIGNA_ANIO").toString();
	String designaNumero = hash.get("DESIGNA_NUMERO").toString();
	String designaIdTurno = hash.get("DESIGNA_IDTURNO").toString();

	if (IDTIPOEJGCOLEGIO != null && !IDTIPOEJGCOLEGIO.equals(""))
		TIPOEJGCOLEGIOSEL.add(IDTIPOEJGCOLEGIO);
	else	
		TIPOEJGCOLEGIOSEL.add("");

	NOMBRELETRADO += " " + APELLIDO1LETRADO + " " + APELLIDO2LETRADO;
	String NOMBRECOMPLETOASISTIDO = NOMBREASISTIDO+ " " + APELLIDO1ASISTIDO + " " + APELLIDO2ASISTIDO;

	// RGG 17-03-2006

	String nombreTurnoAsistencia = (String) request
			.getAttribute("nombreTurnoAsistencia");
	String nombreGuardiaAsistencia = (String) request
			.getAttribute("nombreGuardiaAsistencia");

	if (idPretension != null && idPretensionInstitucion != null)
		pretensionesSel.add(0, idPretension + ","
				+ idPretensionInstitucion);

	//Obtenemos el letrado tramitador de la designa


	ScsDesignaAdm scsDesignaAdm = new ScsDesignaAdm(usr);

	if (!(designaAnio == null || designaAnio.equals("")
			|| designaNumero == null || designaNumero.equals("")
			|| designaIdTurno == null || designaIdTurno.equals(""))) {
		Hashtable hastramitadordesigna = scsDesignaAdm
				.obtenerLetradoDesigna(usr.getLocation(),
						designaIdTurno, designaAnio, designaNumero);
		numtramidesig = (String) hastramitadordesigna.get("NCOLEGIADO");
		nombretramidesig = (String) hastramitadordesigna.get("NOMBRE");
		designaExiste=true;
	}

	Object obj = null;

	String idTurno = (String) hash.get("IDTURNO");
	if (idTurno == null)
		idTurno = new String("");
	String[] datosJuz={usr.getLocation(),idTurno,"-1"};
	String[] parametroComisarias = { usr.getLocation(), "-1"};

	ArrayList estadoSel = new ArrayList();
	ArrayList juzgadoSel = new ArrayList();
	ArrayList comisariaSel = new ArrayList();

	String numeroDiligenciaAsi = (String) hash
			.get(ScsEJGBean.C_NUMERODILIGENCIA);
	String numeroProcedimientoAsi = (String) hash
			.get(ScsEJGBean.C_NUMEROPROCEDIMIENTO);
	String anioProcedimientoAsi = (String) hash
			.get(ScsEJGBean.C_ANIOPROCEDIMIENTO);	

	// Datos del Juzgado seleccionado:
	String juzgadoAsi = (String) hash.get(ScsEJGBean.C_JUZGADO);
	String juzgadoInstitucionAsi = (String) hash
			.get(ScsEJGBean.C_JUZGADOIDINSTITUCION);
	if (juzgadoAsi != null && juzgadoInstitucionAsi != null){
		juzgadoSel.add(0, juzgadoAsi + "," + juzgadoInstitucionAsi);
		if(!juzgadoAsi.equals(""))
			datosJuz[2] = juzgadoAsi;
	}
	// Datos de la comisaria seleccionado:
	String comisariaAsi = (String) hash.get(ScsEJGBean.C_COMISARIA);
	String comisariaInstitucionAsi = (String) hash
			.get(ScsEJGBean.C_COMISARIAIDINSTITUCION);
	if (comisariaAsi != null && comisariaInstitucionAsi != null){
		comisariaSel.add(0, comisariaAsi + ","+ comisariaInstitucionAsi);
		if(!comisariaAsi.equals(""))
			parametroComisarias[1] = comisariaAsi;
	}

	String estilo = "box", readOnly = "false", estiloCombo = "boxCombo";

	if (modo.equals("ver")) {
		estilo = "boxConsulta";
		estiloCombo = "boxComboConsulta";
		readOnly = "true";
	}
	String t_nombreD = "", t_apellido1D = "", t_apellido2D = "", t_idpersonaD = "", t_ncolegiadoD= "";
	ScsDesignaAdm admD = new ScsDesignaAdm(usr);

	if (!(designaAnio == null || designaAnio.equals("")
			|| designaNumero == null || designaNumero.equals("")
			|| designaIdTurno == null || designaIdTurno.equals(""))) {
		Hashtable hTituloD = admD.obtenerLetradoDesigna(usr
				.getLocation(), designaIdTurno, designaAnio,
				designaNumero);
		//String t_nombreD = "", t_apellido1D = "", t_apellido2D = "";
		if (hTituloD != null) {
			t_nombreD = (String) hTituloD.get(CenPersonaBean.C_NOMBRE);
			t_apellido1D = (String) hTituloD
					.get(CenPersonaBean.C_APELLIDOS1);
			t_apellido2D = (String) hTituloD
					.get(CenPersonaBean.C_APELLIDOS2);
			t_nombreD = (String) hTituloD.get(CenPersonaBean.C_NOMBRE);
			t_idpersonaD = (String) hTituloD.get(CenPersonaBean.C_IDPERSONA);
			t_ncolegiadoD = (String) hTituloD.get("ncolegiado");
			

		}
	}
	ArrayList vIntFDict = new ArrayList();
	if (hash.containsKey("IDTIPODICTAMENEJG")) {
		try {
			obj = hash.get("IDTIPODICTAMENEJG");
			vIntFDict.add(obj.equals("") ? "0" : obj.toString());
		} catch (Exception e) {
		}
	}
	
	
	ArrayList vOrigenCAJGSel = new ArrayList();
	String vOrigenCAJG = (String) hash.get(ScsEJGBean.C_IDORIGENCAJG);
	if (vOrigenCAJG != null && vOrigenCAJG != null){
		vOrigenCAJGSel.add(0, vOrigenCAJG);
	}	
	
	String[] datos = { usr.getLocation(), idTurno };
	//String[] datos2 = { usr.getLocation(), usr.getLanguage() };
	
	//datos2 es para idPresentacion
	String[] datos2={usr.getLocation(),"-1","-1","-1"};
	
	if (ejisActivo>0 || PCAJG_ACTIVADO.intValue() == 4){
		
		if(!juzgadoAsi.equals(""))
			datos2[0] = juzgadoAsi;
		
			datos2[1]= usr.getLocation();
			datos2[2]= usr.getLocation();
		
		if(idPretension!=null && (!idPretension.equals("")))
			datos2[3]= idPretension;
		
	}else{
		
		if(idPretension!=null && (!idPretension.equals("")))
			datos2[1]= idPretension;
	}
	
	boolean obligatorioFechaPresentacion = false;
	if (PCAJG_ACTIVADO!=null && PCAJG_ACTIVADO==4){
		obligatorioFechaPresentacion =true;
	}
	
	String anioExpe = (String)request.getAttribute("anioExpe");
	String codigoExpe = (String)request.getAttribute("codigoExpe");

	String idtipoExpe = (String)request.getAttribute("idtipoExpe");

	String idInstiExpe = (String) request.getAttribute("idInstiExpe");

	Boolean tienePermisos = ((Boolean) request.getAttribute(
	"tienePermisos")).booleanValue();
	Boolean tipoExpedienteRepetido = ((Boolean) request.getAttribute(
	"tipoExpedienteRepetido")).booleanValue();


	
	
%>

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.maskedinput.js"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
  
	
	<script type="text/javascript">

		jQuery.noConflict();
		
		jQuery(function($){
			var defaultValue = $("#nig2").val();
			jQuery("#nig2").mask("***** ** * **** *******",{placeholder:" "}); //10037 41 1 2012 0022668
			if($("#nig2").val() == "" || defaultValue.length > 19){
				$("#nig2").val(defaultValue);
			}
				
		});	
		
		

		function cargarComboTipoColegio(){
			
		}
	
		function refrescarLocal()
		{
			document.forms[0].modo.value = "abrir";
			document.forms[0].target = "mainPestanas";
			document.forms[0].submit();
			
		}
		
		function buscarProcurador() 
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
			if((resultado != undefined) && (resultado[0] != undefined) && (resultado[1] != undefined)) {
				document.all.DefinirMantenimientoEJGForm.procurador.value     = resultado[0];
				document.all.DefinirMantenimientoEJGForm.nombreCompleto.value = resultado[1];
				document.all.DefinirMantenimientoEJGForm.nColegiadoProcurador.value     = resultado[2];
//				alert ("Procurador: " + resultado[0] + ", Nombre: " + resultado[1] + ", Ncolegiado: " + resultado[2]);
			}			 	
		}
		
		function generarExpediente(){
			if(<%=tipoExpedienteRepetido%>)
				alert("Tiene repetidos el tipo de Expediente de Insostenibilidad.");
			else if(!<%=designaExiste%>)
				alert("No existe una designación relacionada.");
			else 	
				document.ExpDatosGeneralesForm.submit();
	
		} 		
			
		function generarCarta() {
			// sub();
			
			//idInstitucion  = document.MaestroDesignasForm.idInstitucion;
			var idInstitucion  = <%=idInstitucion%>;
			
			var anio  = <%=ANIO%>;
			var idTipo  = <%=IDTIPOEJG%>;
			var numero = <%=NUMERO%>;
			var datos = "idinstitucion=="+idInstitucion + "##idtipo==" +idTipo+"##anio=="+anio +"##numero==" +numero+"##idTipoInforme==EJG%%%";
			
			
			document.InformesGenericosForm.datosInforme.value=datos;
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
	</script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.busquedaEJG.datosGenerales" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>


<body onload="cargarComboTipoColegio();">
	<html:form action = "/JGR_MantenimientoEJG.do" method="POST" target="mainWorkArea">
	
		<html:hidden property = "modo" value = ""/>	
		<html:hidden property = "idTipoEJG" value = "<%=IDTIPOEJG%>"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "numero" value = "<%=NUMERO%>"/>
		<html:hidden property = "anio" value = "<%=ANIO%>"/>		
		
		
		<html:hidden property = "designa_anio"    		value= "<%=designaAnio%>"/>
		<html:hidden property = "designa_numero"  		value= "<%=designaNumero%>"/>
		<html:hidden property = "designa_turno" 		value= "<%=designaIdTurno%>"/>
		<html:hidden property = "designa_idInstitucion" value= "<%=usr.getLocation()%>"/>
		<html:hidden property = "idPersona" value= "<%=IDPERSONA%>"/>
		<input type="hidden" name = "flagSalto" value=""/>
		<input type="hidden" name = "flagCompensacion" value=""/>	
		<html:hidden property = "idTurnoEJG" value = "<%=idTurno%>"/>
		<html:hidden property = "idGuardiaEJG" value = "<%=IDGUARDIA%>"/>	
		<html:hidden property = "tipoLetrado" value = "M"/>	<!-- En insertar.jsp está tambien por defecto puesto tipoLetrado='M'-->
		<!-- html:hidden property = "fechaAperturaEJG" value = "<%=FECHAAPERTURA%>"/-->

	<tr>				
	<td width="100%" align="center">

		<table class="tablaTitulo" cellspacing="0" heigth="38">
			<tr>
				<td id="titulo" class="titulitosDatos">
				<%
					String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG = "",t_sufijo="";
						ScsEJGAdm adm = new ScsEJGAdm(usr);
						Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(),
								ANIO, NUMERO, IDTIPOEJG);

						if (hTitulo != null) {
							t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String) hTitulo
									.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String) hTitulo
									.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio = (String) hTitulo.get(ScsEJGBean.C_ANIO);
							t_numero = (String) hTitulo.get(ScsEJGBean.C_NUMEJG);
							t_sufijo = (String) hTitulo.get(ScsEJGBean.C_SUFIJO);
							if (t_sufijo!=null && !t_sufijo.equals("")){
								t_numero=t_numero+"-"+t_sufijo;
							}
							
							t_tipoEJG = (String) hTitulo.get("TIPOEJG");
						}
				%> <%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%>
				<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%></td>
				
				<%if (!modo.equalsIgnoreCase("ver")) {
					if (tienePermisos) {
				
				%>
					
					<td>
						<input 	type="button" 
				alt="UtilidadesString.getMensajeIdioma(usrbean,gratuita.EJG.expInsostenibilidad)"    
		       	id="idButton"  
		       	onclick="return generarExpediente();" 
		       	class="button" 
		       	value='<%=UtilidadesString.getMensajeIdioma(usr,"gratuita.EJG.expInsostenibilidad")%>' />
						
					</td>
				<% }else{
				%>	
				<td ></td>
				<% }
				%>
					<td >
						<input 	type="button" 
				alt="UtilidadesString.getMensajeIdioma(usrbean,general.boton.cartaInteresados)"  
		       	id="idButton"  
		       	onclick="return generarCarta();" 
		       	class="button" 
		       	value='<%=UtilidadesString.getMensajeIdioma(usr,"gratuita.EJG.botonComunicaciones")%>' />
					</td>

				<%}%>
				
			</tr>
		</table>

	
<siga:ConjCampos leyenda="gratuita.operarEJG.literal.expedienteEJG">

	<table width="100%" style="table-layout:fixed" border=0>

		<tr>
		    <td class="labelText">	
				<siga:Idioma key='gratuita.operarEJG.literal.anio'/> / <siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
			</td>
			<% String codigoEjg="";
			  if (SUFIJO!=null && !SUFIJO.equals("")){ 
			    codigoEjg= CODIGO+"-"+SUFIJO; 
			    
			  }else{
				  codigoEjg= CODIGO;  
			  }%>
			<td  class="labelText" width="200">	
				<input type="text" class="boxConsulta" value="<%=ANIO%>" readOnly="true" size="4" > / <input size="4" type="text" class="boxConsulta" value="<%=codigoEjg%>" readOnly="true" >
			</td>
			
			<td class="labelText">	
				<siga:Idioma key='gratuita.operarEJG.literal.interesado'/>
			</td>
			<td colspan="3" class="labelTextValue">	
				<input type="text" style="width:100px" class="boxConsulta" value="<%=NIFASISTIDO%>"    readOnly="true">
				<input type="text" style="width:300px" class="boxConsulta" value="<%=NOMBRECOMPLETOASISTIDO%>" readOnly="true">
			</td>
			
		</tr>

		<tr>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.busquedaEJG.literal.fechaApertura'/>
				
			</td>
			
				<!-- JBD 16/2/2009 INC-5682-SIGA -->
				<% if (!modo.equalsIgnoreCase("ver")) { %>
					<td>	
						<siga:Fecha nombreCampo="fechaAperturaEJG"   valorInicial="<%=FECHAAPERTURA%>" />						
					</td >	
				<%}else{%>
					<td>	
						<siga:Fecha nombreCampo="fechaAperturaEJG"   valorInicial="<%=FECHAAPERTURA%>" disabled="true" readOnly="true" />						
					</td >	
				<%}%>
				<!-- JBD 16/2/2009 INC-5682-SIGA -->
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.busquedaEJG.literal.estadoEJG'/>
			</td>
			<td colspan="3">	
				<input type="text" class="boxConsulta" value="<%=NOMBREESTADO%>" readOnly="true" style="width:500">
			</td>
			
		</tr>
				
		<tr>
			<td class="labelText" width="130px">	
				<siga:Idioma key='gratuita.operarEJG.literal.tipo'/>
			</td>
			<td colspan="5">	
				<input type="text" class="boxConsulta" value="<%=TIPOEJG%>" readOnly="true" style="width:140">
			</td>
		</tr>
		<tr>
			<td class="labelText">
			   
				 <siga:Idioma key='gratuita.busquedaEJG.literal.EJGColegio'/><%=tipoEJGColegioObligatorio?pintarAsterisco:""%>
				
			</td>
			<td>	
			<%if (modo.equals("ver")) {%>
				<siga:ComboBD nombre="idTipoEJGColegio" tipo="tipoEJGColegio"  seleccionMultiple="false" obligatorio="false"  elementoSel="<%=TIPOEJGCOLEGIOSEL%>" parametro="<%=dato%>" clase="boxConsulta" readOnly="true" />
			<%} else {%>
				<siga:ComboBD nombre="idTipoEJGColegio" tipo="tipoEJGColegio" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=TIPOEJGCOLEGIOSEL%>" parametro="<%=dato%>" clase="boxCombo" />
			<%}%>
			</td>
			<td class="labelText" nowrap>
				<siga:Idioma key='gratuita.operarEJG.literal.fechaPresentacion'/>&nbsp;<%=obligatorioFechaPresentacion?pintarAsterisco:""%>
			</td>
			<td>	
			<%if (modo.equals("ver")) {%>
				<siga:Fecha nombreCampo="fechaPresentacion"   valorInicial="<%=FECHAPRESENTACION%>" disabled="true" readOnly="true" />				
			<%} else {%>
				<siga:Fecha nombreCampo="fechaPresentacion"   valorInicial="<%=FECHAPRESENTACION%>"/>				
			<%}%>
			</td>
			<td class="labelText" nowrap>
				<siga:Idioma key='gratuita.operarEJG.literal.fechaLimitePresentacion'/>
			</td>
			<td>	
			<%if (modo.equals("ver")) {%>
				<siga:Fecha nombreCampo="fechaLimitePresentacion"   valorInicial="<%=FECHALIMITEPRESENTACION%>" disabled="true" readOnly="true" />				
			<%} else {%>
				<siga:Fecha nombreCampo="fechaLimitePresentacion"   valorInicial="<%=FECHALIMITEPRESENTACION%>" />				
			<%}%>
			</td>
		</tr>
		<tr>
			
			 <td class="labelText">	
				<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/> <siga:Idioma key='gratuita.operarEJG.literal.anio'/> / <siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
			</td>
			
			
			<% if (modo.equals("ver")) {%>
			   <td  class="labelText">	
				  <input size="4" type="text" class="boxConsulta" value="<%=anioCAJG%>" readOnly="true" > / <input type="text" class="boxConsulta" size="10" value="<%=numeroCAJG%>" readOnly="true" >
			   </td>
				
			<%} else {%>
			  <td  class="labelText">	
					<html:text name="DefinirMantenimientoEJGForm"  onkeypress="filterChars(this,false,true);"
                             onkeyup="filterCharsUp(this);"  onblur="filterCharsNaN(this);" property="anioCAJG" size="4" maxlength="4" styleClass="boxNumber"  value="<%=anioCAJG%>" ></html:text> / <html:text name="DefinirMantenimientoEJGForm" property="numeroCAJG" size="10" maxlength="20" 
                             styleClass="boxNumber" value="<%=numeroCAJG%>"></html:text>
			  </td>
				
			<%}%>
			
			
			<td class="labelText" colspan="2">	
				<siga:Idioma key='gratuita.operarEJG.literal.origen'/> &nbsp;
			<% if (modo.equals("ver")) {%> 
				<siga:ComboBD nombre="idOrigenCAJG" tipo="origenCAJG" clase="boxConsulta" ancho="230"  filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=vOrigenCAJGSel%>" readOnly="true"/>
			<%}else{ %>
		    	<siga:ComboBD nombre="idOrigenCAJG" tipo="origenCAJG" clase="boxCombo" ancho="230" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=vOrigenCAJGSel%>" />
		 	<%}%>		
			</td>
			
			<td class="labelText" colspan="2">	
				<siga:Idioma key='gratuita.busquedaEJG.dictamen'/> &nbsp;
				<siga:ComboBD nombre="idTipoDictamenEJG" ancho="210" tipo="dictamenEJG" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vIntFDict%>" readOnly="true"/>	
			</td>
			
		</tr>
	</table>	
	<siga:ConjCampos leyenda="pestana.justiciagratuitadesigna.defensajuridica">
		<table width="100%" style="table-layout:fixed" border=0>
			<tr>
				<td class="labelText" ><siga:Idioma key='gratuita.mantAsistencias.literal.numeroDiligencia'/></td>
				<td>
					<input name="numeroDilegencia" size="20" maxlength="20" type="text" value="<%=numeroDiligenciaAsi%>" class="boxConsulta" readonly="true"/>
				</td> 
				<td class="labelText" ><siga:Idioma key='gratuita.mantAsistencias.literal.centroDetencion'/></td>
				<td  colspan="3">	
					<siga:ComboBD nombre="vistaComisaria" tipo="comboComisariasTurno" ancho="450" obligatorio="false" parametro="<%=parametroComisarias%>" elementoSel="<%=comisariaSel%>" clase="boxConsulta" hijo="t" readonly="true"/>
				</td>
				
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.numeroProcedimiento'/></td>
	 <%if (ejisActivo>0){%>							
				<td >
					<input name="numeroProcedimiento" type="text" value="<%=numeroProcedimientoAsi%>" size="7" class="boxConsulta" readonly="true"/>/
					<input name="anioProcedimiento" type="text" value="<%=anioProcedimientoAsi%>" size="4" class="boxConsulta" readonly="true"/>
				</td>
	 <%}else{%>			
	 			<td class="labelText">
					<input name="numeroProcedimiento" type="text" value="<%=numeroProcedimientoAsi%>" class="boxConsulta" readonly="true"/>
				</td>
	  <%}%>	
				
				<td class="labelText"  width="15%">	
				 <siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>
				 <%if (!modo.equals("ver")) {%>
				 	&nbsp;/&nbsp;<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
				 <%}%>
				</td>
				
			<%if (ejisActivo>0 || PCAJG_ACTIVADO.intValue() == 4){%>		
					 
				<td  colspan="3">	
					<siga:ComboBD nombre="vistaJuzgado" tipo="comboJuzgadosEJG" ancho="480" clase="boxComboConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuz%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="true" accion="Hijo:pretensiones2"/>          	   
				</td>	
				
			<%}else{%>	
				<td  colspan="3">	
					<siga:ComboBD nombre="vistaJuzgado" tipo="comboJuzgadosEJG" ancho="480" clase="boxComboConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuz%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="true"/>          	   
				</td>
			
			 <%}%>	
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key='gratuita.operarEJG.literal.observacionesAsunto'/>
				</td>
				<td  colspan="2">	
						<html:textarea name="DefinirMantenimientoEJGForm" property="observaciones" cols="60" rows="2" style="overflow:auto" styleClass="boxConsulta" value="<%=OBSERVACIONES%>" readonly="true"></html:textarea>
				</td>
				<td class="labelText">
					<siga:Idioma key='gratuita.general.literal.comentariosDelitos'/>
				</td>
				<td  colspan="2">	
					<html:textarea name="DefinirMantenimientoEJGForm" property="delitos" cols="60" rows="2" style="overflow:auto" styleClass="boxConsulta" value="<%=DELITOS%>" readonly="true"></html:textarea>
				</td>		
			</tr>			
			<tr>
				<td class="labelText">	
					<siga:Idioma key='gratuita.personaJG.literal.calidad'/>
				</td>		
				
				<td class="labelText">					
					<input type="text" style="width:500px" class="boxConsulta" value="<%=calidad%>" readOnly="true">										
				</td>	
				<td  style="display:none">
				  <html:text name="DefinirMantenimientoEJGForm" property="calidad" value="<%=calidad%>" readOnly="true"/>				
				</td> 	
			
				<td class="labelText">	
					<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>
				</td>
				
				<%if (ejisActivo>0 || PCAJG_ACTIVADO.intValue() == 4){%>	
	
					<td colspan="3">
						<siga:ComboBD nombre="pretensiones2" tipo="comboPretensionesEjis" ancho="500" clase="boxComboConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos2%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="true"/>           	   
					</td>
	
				<%}else{%>	
						
					<td colspan="3">
						<siga:ComboBD nombre="pretensiones2" tipo="comboPretensiones" ancho="500" clase="boxComboConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos2%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="true"/>           	   
					</td>
				<%}%>
			</tr>
			<tr>
				<td class="labelText">	
					<siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/>
				</td>		
				
				<td class="labelText" colspan="3">					
					<input id="nig2" type="text" style="width:500px" class="boxConsulta" value="<%=NIG%>" readOnly="true">										
				</td>	
			</tr>
			<tr>
				<td colspan="6">
					<siga:ConjCampos leyenda="gratuita.datosProcurador.literal.procurador">
						<table  width="100%" border="0">
							<tr>
								<td class="labelText" width="20%">
									<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/>
								</td>
								<td width="20%">
									<html:hidden property = "procurador" value="<%=procuradorSel%>"/>
									<input type="text" name="nColegiadoProcurador" id="nColegiadoProcurador" size="15" maxlength="100" class="boxConsulta" readOnly="true" value="<%=procuradorNumColegiado%>"/>
								</td>
								<td class="labelText" width="20%">
									<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
								</td>
								<td width="30%">
									<input type="text" name="nombreCompleto" id="nombreCompleto" size="60" maxlength="100" class="boxConsulta" readOnly="true" value="<%=procuradorNombreCompleto%>"/>
								</td>
								<td width="10%">
	
								</td>
							</tr>
						</table>
					</siga:ConjCampos>				
				</td>			
			</tr>
		</table>
	</siga:ConjCampos>
</siga:ConjCampos>
		
	
	<%if ( ((DESIGNA_NUMERO != null) && (!DESIGNA_NUMERO.equals("")))) {%>
		<!-- Pinta relacion con designa -->
        <siga:ConjCampos leyenda="gratuita.operarEJG.literal.relacionado">

	
			<%if ((DESIGNA_NUMERO != null)&& (!DESIGNA_NUMERO.equals(""))) {%>
			
	 		<!--<fieldset>-->
			<table width="100%" border="0"> 
			
			<tr>	

				<td class="labelText">
					<siga:Idioma key='gratuita.operarEJG.literal.designa'/>
				</td>
				<td class="labelText" >
					<siga:Idioma key='gratuita.operarEJG.literal.turno'/>
				</td>
				<td>
					<input type="text" cols="20" class="boxConsulta" value="<%=DESIGNA_TURNO_NOMBRE%>" readOnly="true">
				</td>
				<%if(ESTADO!=null && ESTADO.trim().equals("A")){ %>
				<td style="color:red" class="labelText">
					<siga:Idioma key='gratuita.designa.estado.anulado'/>
				</td>
				<%}%>

				
				<!--<td class="labelText">
					<siga:Idioma key='gratuita.operarEJG.literal.anio'/>
				</td>-->
				<td class="labelTextValue" >
	
				<%=UtilidadesString.mostrarDatoJSP(DESIGNA_ANIO)%>/<%=UtilidadesString.mostrarDatoJSP(DESIGNA_CODIGO)%> - <%=UtilidadesString.mostrarDatoJSP(t_nombreD)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1D)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2D)%>
					
				</td>
				<td class="labelText" style="display:none">	
					<siga:Idioma key='gratuita.operarEJG.literal.numer'/>
				</td>
				<td style="display:none">
					<input type="text" cols="5" class="boxConsulta" value="<%=DESIGNA_NUMERO%>" readOnly="true">
				</td>
				<%if (modo.equalsIgnoreCase("ver")) {%>
					<td colspan="3" align="right">
						<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarDesigna'/>" name="consultarDesigna" border="0" onclick="consultarDesignaFuncion('ver')">
					</td>
				<%} else {%>
					<td colspan="3" align="right" >
						<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarDesigna'/>" name="consultarDesigna" border="0" onclick="consultarDesignaFuncion('ver')">
						<img src="<%=app%>/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.boton.EditarDesigna'/>" name="" border="0" onclick="consultarDesignaFuncion('<%=modo%>')">
						<img src="<%=app%>/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.boton.BorrarDesigna'/>" name="" border="0" onclick="borrarRelacionConDesigna()">
					</td>
				<%}%>
				</tr>			
		 		
			</table>
			
			<%}%>

	</siga:ConjCampos>
	<%}%>

<siga:ConjCampos leyenda="gratuita.operarEJG.literal.ServicioTramitacion">
	<table width="100%" border="0">
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.operarEJG.literal.turno'/>
			</td>
			<td>	
				<%if (modo.equals("ver")) {%>
					<input type="text" class="boxConsulta" value="<%=TURNO%>" readOnly="true" style="width:140px">
				<%} else {
					ArrayList lista = new ArrayList();
					String cadena = usr.getLocation() + "," + idTurno;
					lista.add(cadena);
				%>
					<siga:ComboBD nombre = "identificador" tipo="turnos" clase="boxCombo" ancho="400" obligatorio="false" pestana="t" accion="Hijo:identificador2;" elementoSel="<%=lista%>" parametro="<%=dato%>"/>
				<%}%>
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.operarEJG.literal.guardia'/>
			</td>
			<td>	
				<%if (modo.equals("ver")) {%>
					<input type="text" class="boxConsulta" value="<%=GUARDIA%>" readOnly="true" style="width:140px">					
				<%} else {
					ArrayList lista = new ArrayList();
					String cadena = usr.getLocation() + "," + IDGUARDIA;
					lista.add(cadena);%>
					<siga:ComboBD nombre = "identificador2" tipo="guardias" clase="boxCombo" pestana="t" ancho="400" obligatorio="false" hijo="t" parametro="<%=datos%>" elementoSel="<%=lista%>"/>									
				<%}%>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<siga:BusquedaSJCS nombre="DefinirMantenimientoEJGForm" propiedad="buscaLetrado"
	 				   concepto="EJG" operacion="Asignacion" 
					   campoTurno="identificador" campoGuardia="identificador2" campoFecha="fechaAperturaEJG"
					   campoPersona="idPersona" campoColegiado="NColegiado" campoNombreColegiado="nomColegiado"  
					   campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" campoSalto="checkSalto" campoCompensacion="compensacion"
					   diaGuardia="false"
					   modo="<%=modo%>"
					   />							   
			</td>
		</tr>
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.operarEJG.literal.tramitador'/>
			</td>
			<td width="500" class="labelTextValue">
				<input type="text" name="NColegiado" class="boxConsulta" readOnly value="<%=NUMEROCOLEGIADO%>" style="width:'100px';">-<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="<%=nombreCompleto%>" style="width:'240px';">
			</td>
		</tr>		
	</table>	
</siga:ConjCampos>


	</html:form>
	
	<html:form action = "/JGR_Designas.do" method="POST" target="submitArea">
		<html:hidden property ="modo"        value = ""/>
		<html:hidden property ="actionModal" value = ""/>
		<html:hidden property ="desdeEjg"    value = "si"/>
		<html:hidden property ="numeroEjg"   value = "<%=NUMERO%>"/>
		<html:hidden property ="idTipoEjg"   value = "<%=IDTIPOEJG%>"/>
		<html:hidden property ="anioEjg"     value = "<%=ANIO%>"/>
		<html:hidden property ="idTurnoEJG"  value= "<%=idTurno%>"/>
		<html:hidden property ="juzgadoAsi"  value= "<%=juzgadoAsi%>"/>
		<html:hidden property ="juzgadoInstitucionAsi"  value= "<%=juzgadoInstitucionAsi%>"/>
		<html:hidden property ="numProcedimiento"  value= "<%=numeroProcedimientoAsi%>"/>
		<html:hidden property ="anioProcedimiento"   value = "<%=anioProcedimientoAsi%>"/>
		<html:hidden property ="numero" value = ""/>
		<html:hidden property ="idTurno" value = ""/>
		<html:hidden property ="anio" value = ""/>
		<html:hidden property = "ncolegiado" value= "<%=NUMEROCOLEGIADO%>"/>		
		<html:hidden property = "nombre" value= "<%=NOMBRELETRADO%>"/>	
	</html:form>		

	<html:form action = "/JGR_MantenimientoDesignas.do" method="POST" target="mainWorkArea">
		<html:hidden property ="modo" 	  value= "<%=modo%>"/>
		<html:hidden property ="anio"     value= "<%=designaAnio%>"/>
		<html:hidden property ="numero"   value= "<%=designaNumero%>"/>
		<html:hidden property ="idTurno"  value= "<%=designaIdTurno%>"/>
		<html:hidden property ="desdeEjg" value= "si"/>
	</html:form>		
	
	<html:form action = "/JGR_Asistencia.do" method="POST" target="mainWorkArea">
		<html:hidden property ="modo" 	  value= "<%=modo%>"/>
		<html:hidden property ="anio"     value= "<%=ASISTENCIA_ANIO%>"/>
		<html:hidden property ="numero"   value= "<%=NUMEROASISTENCIA%>"/>
		<html:hidden property ="desdeEJG" value= "si"/>
	</html:form>		

	<html:form action = "/JGR_ExpedientesSOJ.do" method="POST" target="mainWorkArea">
		<html:hidden property ="modo" 	       value= "<%=modo%>"/>
		<html:hidden property ="anio"          value= "<%=ANIOSOJ%>"/>
		<html:hidden property ="numero"  	   value= "<%=NUMEROSOJ%>"/>
		<html:hidden property ="idTipoSOJ"     value= "<%=IDTIPOSOJ%>"/>
		<html:hidden property ="idInstitucion" value= "<%=usr.getLocation()%>"/>
		<html:hidden property ="desdeEJG"      value= "si"/>
	</html:form>		

	<!-- Para la busqueda del procurador -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrirBusquedaProcuradorModal">
	</html:form>

	<!-- formulario para buscar Tipo SJCS -->
	<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrir">
		<input type="hidden" name="tipo"        value="">
	</html:form>

	<!-- Para la eliminar vinculacion de un SOJ con el EJG -->
	<html:form action="/JGR_PestanaSOJDatosGenerales.do" method="POST" target="submitArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="borrarRelacionConEJG">
		<html:hidden property = "idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden property = "anioEJG" value="<%=ANIO%>"/>
		<html:hidden property = "numeroEJG" value="<%=NUMERO%>"/>
		<html:hidden property = "tipoEJG" value="<%=IDTIPOEJG%>"/>
		
	</html:form>	
	
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
	</html:form>	
	
	<html:form action = "/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarComisaria">
		<html:hidden property = "codigoExtBusqueda" value=""/>
	</html:form>	
	
	<html:form action="/EXP_Auditoria_DatosGenerales.do"  method="POST" target="mainWorkArea">


		<input type="hidden" name="soloSeguimiento" value = "false">		
		<input type="hidden" name="editable" value = "1" >
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "avanzada" value = ""/>
		<html:hidden property = "metodo" value = "abrirNuevoEjg"/>
		<html:hidden property ="numeroEjg"   value = "<%=NUMERO%>"/>
		<html:hidden property ="idTipoEjg"   value = "<%=IDTIPOEJG%>"/>
		<html:hidden property ="anioEjg"     value = "<%=ANIO%>"/>
		<html:hidden property ="nifSolicitante"     value = "<%=NIFASISTIDO%>" />
		<html:hidden property ="nombreSolicitante"     value = "<%=NOMBRECOMPLETOASISTIDO%>" />
		<html:hidden property ="idInstitucion_TipoExpediente" value= "<%=usr.getLocation()%>"/>	
		<html:hidden property ="numeroProcedimiento"   value = "<%=numeroProcedimientoAsi%>"/>
		<html:hidden property ="anioProcedimiento"   value = "<%=anioProcedimientoAsi%>"/>
		<html:hidden property ="asunto"     value = "<%=OBSERVACIONES%>"/>
		<html:hidden property ="juzgado"   value = "<%=juzgadoAsi%>"/>
		<html:hidden property ="juzgadoInstitucion"   value = "<%= juzgadoInstitucionAsi%>"/>
		<html:hidden property ="pretension"     value = "<%=idPretension%>" />
		<html:hidden property ="pretensionInstitucion"     value = "<%=idPretensionInstitucion%>" />
		<html:hidden property ="idturnoDesignado"     value = "<%=designaIdTurno%>" />
		<html:hidden property ="nombreDesignado" value= "<%=t_idpersonaD%>"/>	
		<html:hidden property ="numColDesignado" value= "<%=numtramidesig%>"/>
		<html:hidden property ="idclasificacion" value= "1"/>
		<html:hidden property ="solicitanteEjgNif"     value = "<%=NIFASISTIDO%>" />
		<html:hidden property ="solicitanteEjgNombre"     value = "<%=NOMBREASISTIDO%>" />
		<html:hidden property ="solicitanteEjgApellido1"     value = "<%=APELLIDO1ASISTIDO%>" />
		<html:hidden property ="solicitanteEjgApellido2"     value = "<%=APELLIDO2ASISTIDO%>" />
		
		
	
	
				
	</html:form>	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton limpiar
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		function accionVolver()
		{
			<% String modoVolverSOJ = (String) request.getSession().getAttribute("modoVolverSOJ");
			   if (modoVolverSOJ == null) { %>
					document.forms[0].action="./JGR_EJG.do";	
					document.forms[0].modo.value="buscar";
					document.forms[0].target="mainWorkArea"; 
					document.forms[0].submit(); 
			<% } 
			   else { 
			   		request.getSession().removeAttribute("modoVolverSOJ"); %>
					document.forms[4].modo.value="<%=modoVolverSOJ%>";
				   	document.forms[4].submit();
		   <% }%>
		}
		
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		function accionGuardar() 
		{	
			sub();
			var observaciones = document.forms[0].observaciones.value;
			
		 <%if (tipoEJGColegioObligatorio){%>
		   if (document.forms[0].idTipoEJGColegio.value==""){
		    fin();
		    alert('<siga:Idioma key="gratuita.operarEJG.message.requeridoTipoEJGColegio"/>');
		    return false;
		   }
		 <%}%> 
		 if( !((document.forms[0].anioCAJG.value!="" && document.forms[0].numeroCAJG.value!="" && document.forms[0].idOrigenCAJG.value!="")
		    || (document.forms[0].anioCAJG.value=="" && document.forms[0].numeroCAJG.value=="" && document.forms[0].idOrigenCAJG.value=="")) ){
		    fin();
		    alert('<siga:Idioma key="gratuita.operarEJG.message.anioNumeroOrigen.obligatorios"/>');
		    return false;
		   
		 } 
		 if(<%=obligatorioFechaPresentacion%> && document.forms[0].fechaPresentacion.value==""){
			 fin();
			 alert("<siga:Idioma key='errors.required' arg0='gratuita.operarEJG.literal.fechaPresentacion'/>");
			 return false;
		 } 
		  if (document.forms[0].anioCAJG.value.length!=0 && document.forms[0].anioCAJG.value.length<4){
			  fin();
			  alert('<siga:Idioma key="gratuita.operarEJG.message.longitudAnioCAJG"/>');
			  return false;
		  
		 }	  
		 	
		 if(document.forms[0].fechaAperturaEJG.value!=""){
			if (observaciones.length <= 1024) {
			     var datosTurno =  document.forms[0].identificador.value.split(",");
				 if (datosTurno!=""){
					document.forms[0].idTurnoEJG.value=datosTurno[1];
				}else{
				    document.forms[0].idTurnoEJG.value="";
				}
				document.forms[0].idGuardiaEJG.value=document.forms[0].identificador2.value;
				document.forms[0].modo.value = "modificar";
				document.forms[0].target = "submitArea";							
				document.forms[0].submit();
			}
			else  {
				alert('<siga:Idioma key="gratuita.operarEJG.message.lontigudObservaciones"/>');
				fin();
				return false;	
			}
	    }else{
			
			alert('<siga:Idioma key="gratuita.operarEJG.message.fechaApertura"/>');
			fin();
			return false;
			}
		} 
	
		function accionCrearDesignacion()
		{  
			document.forms[1].modo.value = "nuevo";
			
			var resultado=ventaModalGeneral(document.forms[1].name,"M");
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.BuscarDesignasForm){
					numero.value        = resultado[1];
					idTurno.value     = resultado[2];
					anio.value          = resultado[4];
					modo.value          = "editar";
					target				= "mainWorkArea";
			   		submit();
				}
			}
		}
		
		function redireccionar ()
		{
			document.forms[1].target="mainWorkArea";
			document.forms[1].modo.value="editar";
			document.forms[1].submit();
		}
		//Asociada al boton Consultar Designa
		function consultarDesignaFuncion(modo) {
		   	document.forms[2].modo.value = modo;
		   	document.forms[2].submit();
	 	}

		//Asociada al boton Consultar Asistencia
		function consultarAsistenciaFuncion(modo) {
		   	document.forms[3].modo.value = modo;
		   	document.forms[3].submit();
	 	}
	
		//Asociada al boton Ver SOJ
		function consultarSOJFuncion(_modo) {
		   	document.forms[4].modo.value = _modo;
		   	document.forms[4].submit();
	 	}

		function accionSolicitudAsistencia()
		{
			document.forms[0].modo.value = "editar";
			document.forms[0].target = "submitArea";							
			document.forms[0].submit();
		}
		
		function relacionarConDesigna() 
		{
			document.BusquedaPorTipoSJCSForm.tipo.value="DESIGNA";
			var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	

			if (resultado != null && resultado.length >= 4)
			{
				document.forms[0].designa_idInstitucion.value=resultado[0];
				document.forms[0].designa_anio.value=resultado[1];
				document.forms[0].designa_numero.value=resultado[2];
				document.forms[0].designa_turno.value=resultado[3];

				document.forms[0].modo.value= "relacionarConDesigna";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
			}
		}
		
		function borrarRelacionConDesigna() 
		{
			if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>"))
			{
				document.forms[0].modo.value="borrarRelacionConDesigna";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
			}
		}
		
		function borrarRelacionarConSOJ() 
		{
			if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>"))
			{
				document.forms[7].target = "submitArea";
				document.forms[7].submit();
			}
		}
		
		function borrarRelacionarConAsistencia() 
		{
		
			if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>"))
			{
				document.forms[0].modo.value="borrarRelacionConAsistencia";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
			}
		}
		
		

	</script>

<%
	/////////////////////////
	// DCG Si estan los tres campos a nulos o sin datos mostramos el boton
	boolean condicion = true;
	if ((modo != null)
			&& ((modo.equalsIgnoreCase("consulta")) || (modo
					.equalsIgnoreCase("ver"))))
		condicion &= false;
	else if ((designaAnio != null) && (!designaAnio.equals("")))
		condicion &= false;
	else if ((designaNumero != null) && (!designaNumero.equals("")))
		condicion &= false;
	else if ((designaIdTurno != null) && (!designaIdTurno.equals("")))
		condicion &= false;
	// DCG fin
	/////////////////////////
	String crearDesigna = "V";
	if (!modo.equalsIgnoreCase("ver")) {
		if (condicion) {
			crearDesigna = "V,G,R,CD,RD";
		} else {
			crearDesigna = "V,G,R";
		}
	}
%>
	<!-- INICIO: BOTONES BUSQUEDA -->	
	 	<siga:ConjBotonesAccion botones="<%=crearDesigna%>" clase="botonesDetalle"  />	
	<!-- FIN: BOTONES BUSQUEDA -->
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value='<%= usr.isComision() ?"CAJG":"EJG"%>'/>
	<html:hidden property="enviar" value = "1"/>
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
<!-- FIN: SUBMIT AREA -->

</body>
</html>