<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 07-03-2005 - Inicio
-->		

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="com.siga.beans.FacSerieFacturacionBean"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String dato[] = new String[1];
	Long	LSerieFacturacion			= null;
	String sDescripcion				= "";
	String sSerieFacturacionDesc	= "";
	String sFInicialProducto 		= ""; 
	String sFFinalProducto				= "";	
	String sFInicialServicio 		= "";
	String sFFinalServicio 			= "";
	String sFProgramacion 				= "";
	String sFPrevistaGeneracion 	= "";
	String sFRealGeneracion 	= "";
	String sFPrevistaConfirmacion 	= "";	
	String sHorasConfirmacion 	= "";
	String sHorasGeneracion 	= "";
	String sMinutosConfirmacion 	= "";
	String sMinutosGeneracion 	= "";
	String sFCargoFicheroBanco 	= "";
	String sHorasCargoFicheroBanco 	= "";
	String sMinutosCargoFicheroBanco 	= "";
	String enviarFacturas 	= "";
	String idTipoPlantilla = "";
	String generarPDF 	= "";
	boolean bEditableConfirmacion = true;
	boolean bEditableGeneracion = true;
	boolean bEditable = true;
	boolean nuevo = true;
	String auxFcargo ="";
	String idTipoEnvioCorreoElectronico = ""+EnvEnviosAdm.TIPO_CORREO_ELECTRONICO;
	String parametrosCmbPlantillaEnvios[] = {usr.getLocation(),idTipoEnvioCorreoElectronico};
	String parametrosPlantillasMail [] = {"-1",usr.getLocation(),"1"};
	ArrayList plantillaEnviosSeleccionada = new ArrayList();
	ArrayList plantillaSeleccionada = new ArrayList();
	
	String modoAction=(String) ses.getAttribute("ModoAction");

	if(request.getSession().getAttribute("DATABACKUP")!= null){		
			// Se trata de modificacion
			Enumeration en = ((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
			Hashtable hash = (Hashtable)en.nextElement();
			
			sDescripcion = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_DESCRIPCION);
			LSerieFacturacion = UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION);
			sSerieFacturacionDesc = UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_NOMBREABREVIADO);
			sFInicialProducto = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS));
			sFFinalProducto = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS));	
			sFInicialServicio = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS));
			sFFinalServicio = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS));
			sFProgramacion = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPROGRAMACION));
			sFRealGeneracion = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAREALGENERACION);

			sFPrevistaGeneracion = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION));
			String aux = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION);
			sHorasGeneracion = aux.substring(11,13);
			sMinutosGeneracion = aux.substring(14,16);		

			sFPrevistaConfirmacion = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM));
			aux = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM);
			if (!aux.equals("")) {
				sHorasConfirmacion = aux.substring(11,13);
				sMinutosConfirmacion = aux.substring(14,16);		
			}
			
			sFCargoFicheroBanco =com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHACARGO));
			auxFcargo = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHACARGO);
			if (!auxFcargo.equals("")) {
				sHorasCargoFicheroBanco = auxFcargo.substring(11,13);
				sMinutosCargoFicheroBanco = auxFcargo.substring(14,16);		
			}
					
			enviarFacturas = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_ENVIO);
			generarPDF = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_GENERAPDF);
			
			if (hash.get(FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL) != null && !hash.get(FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL).equals("")){
				idTipoPlantilla = (UtilidadesHash.getInteger(hash, FacFacturacionProgramadaBean.C_IDTIPOPLANTILLAMAIL)).toString();
				if(idTipoPlantilla != null && !idTipoPlantilla.equals("")){	
					plantillaEnviosSeleccionada.add(idTipoPlantilla+","+usr.getLocation() +",1");
				}
			}
			
			nuevo	= false;				
			
			if (!sFRealGeneracion.trim().equals("")||modoAction.trim().equals("ver")) {
				bEditable=false;
				bEditableGeneracion=false;
			}
	} else {
		
		if (sFProgramacion.trim().equals("")) {
			sFProgramacion = UtilidadesString.formatoFecha(new Date(),"dd/MM/yyyy");
		}
		
	}

		
	
		
	String iconos="E,B";
	String botones="N"; 
	
	
	
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
  <script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="programarFacturacionForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->

<SCRIPT LANGUAGE="JavaScript">
function IsNum( numstr ) {
	
	// Return immediately if an invalid value was passed in
	if (numstr+"" == "undefined" || numstr+"" == "null" || numstr+"" == "")	
		return false;

	var isValid = true;
	var decCount = 0;		// number of decimal points in the string

	// convert to a string for performing string comparisons.
	numstr += "";	

	// Loop through string and test each character. If any
	// character is not a number, return a false result.
 	// Include special cases for negative numbers (first char == '-')
	// and a single decimal point (any one char in string == '.').   
	for (i = 0; i < numstr.length; i++) {
		// track number of decimal points
		if (numstr.charAt(i) == ".")
			decCount++;

    	if (!((numstr.charAt(i) >= "0") && (numstr.charAt(i) <= "9") || 
				(numstr.charAt(i) == "-") || (numstr.charAt(i) == "."))) {
       	isValid = false;
       	break;
		} else if ((numstr.charAt(i) == "-" && i != 0) ||
				(numstr.charAt(i) == "." && numstr.length == 1) ||
			  (numstr.charAt(i) == "." && decCount > 1)) {
       	isValid = false;
       	break;
      }         	
               	       
   } // END for   
   
   	return isValid;
}  // end IsNum
</SCRIPT>

	<script language="JavaScript">

		//<!-- Asociada al boton Volver -->
		function accionCerrar(){ 			
			window.top.close();
		}			
		
		//<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {	

			var fechaConf = trim(document.programarFacturacionForm.fechaPrevistaConfirmacion.value);
			var fechaGen = trim(document.programarFacturacionForm.fechaPrevistaGeneracion.value);
			var fechaCargo = trim(document.programarFacturacionForm.fechaCargo.value);
			f=document.programarFacturacionForm;			
				
			sub();
			
			if (trim(fechaGen)!="") {
				//Para la validacion no tengo en cuenta si empieza por 0 y tiene 2 digitos (tanto hora como minuto)
				var horas = trim(document.programarFacturacionForm.horasGeneracion.value);
				var minutos = trim(document.programarFacturacionForm.minutosGeneracion.value);

				if (horas.length==1) {
					document.programarFacturacionForm.horasGeneracion.value = "0" + horas;
				}
				if (minutos.length==1) {
					document.programarFacturacionForm.minutosGeneracion.value = "0" + minutos;
				}
				if (horas!="" && (horas>23 || horas<0)) {
					
					alert('<siga:Idioma key="messages.programarFacturacionForm.mensajeHoras"/>');
					fin();
					return false;
				}
				if (minutos!="" && (minutos>59 || minutos<0)) {
					alert('<siga:Idioma key="messages.programarFacturacionForm.mensajeMinutos"/>');
					fin();
					return false;
				}
			}	
			
			if (trim(fechaConf)!="") {
				horas = trim(document.programarFacturacionForm.horasConfirmacion.value);
				minutos = trim(document.programarFacturacionForm.minutosConfirmacion.value);
				if (horas.length==1) {
					document.programarFacturacionForm.horasConfirmacion.value = "0" + horas;
				}
				if (minutos.length==1) {
					document.programarFacturacionForm.minutosConfirmacion.value = "0" + minutos;
				}
				if (horas!="" && (horas>23 || horas<0)) {
					alert('<siga:Idioma key="messages.programarFacturacionForm.mensajeHoras"/>');
					fin();
					return false;
				}
				if (minutos!="" && (minutos>59 || minutos<0)) {
					alert('<siga:Idioma key="messages.programarFacturacionForm.mensajeMinutos"/>');
					fin();
					return false;
				}
			}

			
			
		
				var valor = "";				
				
			if (trim(fechaGen)!="") {
				valor = trim(f.horasGeneracion.value);
	            if (!IsNum(valor)) {
	            	alert ("<siga:Idioma key='facturacion.seriesFacturacion.literal.horasGeneracion'/>");
	            	fin();
	            	return false;
				}
				valor = trim(f.minutosGeneracion.value);
	            if (!IsNum(valor)) {
	            	alert ("<siga:Idioma key='facturacion.seriesFacturacion.literal.minutosGeneracion'/>");
	            	fin();
	            	return false;
				}
			}
			
			if (trim(fechaConf)!="") {
			
				valor = trim(f.horasConfirmacion.value);
	            if (!IsNum(valor)) {
	            	alert ("<siga:Idioma key='facturacion.seriesFacturacion.literal.horasConfirmacion'/>");
	            	fin();
	            	return false;
				}
				valor = trim(f.minutosConfirmacion.value);
	            if (!IsNum(valor)) {
	            	alert ("<siga:Idioma key='facturacion.seriesFacturacion.literal.minutosConfirmacion'/>");
	            	fin();
	            	return false;
				}
			}

			if (!validateProgramarFacturacionForm(document.programarFacturacionForm)){	
				fin();
				return false;
			}


			// Comprobamos las fechas
			if (compararFecha (f.fechaInicialProducto, f.fechaFinalProducto) == 1) {
				alert ("<siga:Idioma key='messages.fechas.rangoFechas'/>");
				fin();
				return false;
			}
			if (compararFecha (f.fechaInicialServicio, f.fechaFinalServicio) == 1) {
				alert ("<siga:Idioma key='messages.fechas.rangoFechas'/>");
				fin();
				return false;
			}	
			var iguales = compararFecha (f.fechaPrevistaGeneracion, f.fechaPrevistaConfirmacion);
			if (iguales==1) {
				// no valen las fechas
				alert ("<siga:Idioma key='messages.fechas.rangoFechasPrevistas'/>");
				fin();
				return false;
			} else {
				if (iguales==0) {
					// son iguales, comparamos las horas
					var horasConf = trim(document.programarFacturacionForm.horasConfirmacion.value);
					var minutosConf = trim(document.programarFacturacionForm.minutosConfirmacion.value);
					var horasGen = trim(document.programarFacturacionForm.horasGeneracion.value);
					var minutosGen = trim(document.programarFacturacionForm.minutosGeneracion.value);
					if (parseInt(horasConf)<parseInt(horasGen)) {
						alert ("<siga:Idioma key='messages.fechas.rangoHorasPrevistas'/>");
						fin();
						return false;
					} else if (parseInt(horasConf)==parseInt(horasGen)) {
						if (parseInt(minutosConf)<parseInt(minutosGen)) {
							alert ("<siga:Idioma key='messages.fechas.rangoHorasPrevistas'/>");
							fin();
							return false;
						}
					}
				}
			}

			if(document.programarFacturacionForm.enviarFacturas.checked){
				if(document.programarFacturacionForm.idTipoPlantillaMail.value == ""){
					alert('<siga:Idioma key="Facturacion.mensajes.obligatorio.plantillaMail"/>');
					fin();
					return false;
				}
			}
			
			if(<%=nuevo%>){
				f.modo.value = "insertar";
			}
			else{
				f.modo.value = "modificar";
			}							
			document.all.programarFacturacionForm.submit();						
		}			
	
			function actualiza() 
			{
				if (document.forms[0].enviarFacturas.checked==true) {
					document.forms[0].generarPDF.checked=true;
				   	jQuery("#generarPDF").attr("disabled","disabled");
					jQuery("#idTipoPlantillaMail").removeAttr("disabled");					
				} else {
					jQuery("#generarPDF").removeAttr("disabled");	
					jQuery("#idTipoPlantillaMail").attr("disabled","disabled");
				}
				return false;
			}

			function inicio() 
			{
				if (document.forms[0].enviarFacturas.checked==true) {
					if(document.forms[0].idTipoPlantillaMail)
						jQuery("#idTipoPlantillaMail").removeAttr("disabled");
				} else {
					if(document.forms[0].idTipoPlantillaMail)
					   	jQuery("#idTipoPlantillaMail").attr("disabled","disabled");
				}
			}
			

			function cambiaSerie() 
			{
				document.forms[0].modo.value="buscarPor";
				document.forms[0].submit();
			}


			function Calcularvalor(){
				document.forms[0].fechaCargo.value =document.forms[0].fechaPrevistaConfirmacion.value;
			} 

			function accionCalendario() {
				// Abrimos el calendario 				
				var resultado = showModalDialog("<html:rewrite page='/html/jsp/general/calendarGeneral.jsp'/>?valor="+document.programarFacturacionForm.fechaPrevistaConfirmacion.value,document.programarFacturacionForm.fechaPrevistaConfirmacion,"dialogHeight:275px;dialogWidth:400px;help:no;scroll:no;status:no;");
				window.top.focus();
				if (resultado) {
					document.programarFacturacionForm.fechaPrevistaConfirmacion.value = resultado;
					document.programarFacturacionForm.horasConfirmacion.value = "20";//ponemos que la hora por defecto para confirmacion seran las 20:00
					document.programarFacturacionForm.minutosConfirmacion.value = "00";					 
					document.programarFacturacionForm.fechaCargo.value=resultado;							
				}
			}

			 
		</script>
</head>

<body onLoad="inicio();">
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="facturacion.programarFacturacion.literal.cabecera"/> 
				</td>
			</tr>
		</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	
	<html:form action="/FAC_ProgramarFacturacion.do" method="POST" target="submitArea">
		<!-- INICIO: CAMPOS -->
			<html:hidden property="modo" value="cerrar"/>			

	<siga:ConjCampos leyenda="facturacion.asignacionDeConceptosFacturables.titulo">
						<table class="tablaCampos" align="center" cellspacing="0" cellpadding="0">
						
							<tr align="center"> 
								<td class="labelText">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.descripcion"/>&nbsp;(*)
								</td>
								<td colspan="4" align="left">
								<%if (modoAction.equals("editar")){%>
									<input type ="text"  value="<%=sDescripcion%>" id="descripcionProgramacion" name="descripcionProgramacion" class="box" style="width:'300px';">
								<%								
									} else {%>
										<input type ="text"  value="<%=sDescripcion%>" id="descripcionProgramacion" name="descripcionProgramacion" class="boxConsulta" style="width:'300px';">
									 <%}%>	
								</td>
							</tr>
						
							<tr>
								<td class="labelText" width="180">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.serieFacturacion"/>&nbsp;(*)
								</td>
								<td COLSPAN=4>
									<%if(nuevo){
											dato[0] = usr.getLocation().toString();
									%>
								    <siga:ComboBD nombre="serieFacturacion" tipo="cmbSerieFacturacion" parametro="<%=dato%>" clase="boxCombo" accion="cambiaSerie();" obligatorio="true"/>
									<%								
									} else {
									%>
										<html:hidden property="serieFacturacion" value='<%=LSerieFacturacion.toString()%>'/>
										<html:text property="serieFacturacionDes" value='<%=sSerieFacturacionDesc%>' size="60" styleClass="boxConsulta" readOnly="true"></html:text>
									<%}%>								
								</td>
							</tr>
							<!-- FILA -->
							<tr>
								<td class="labelText" width="180">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fechaProgramacion"/>&nbsp;(*)
								</td>
								<td colspan="3">
									<html:text name="programarFacturacionForm" property="fechaProgramacion" value="<%=sFProgramacion%>" size="10" styleClass="boxConsulta" readonly="true"></html:text>									
								</td>
							</tr>
						</table>
	</siga:ConjCampos>
	<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.periodosFacturacion">

						<table class="tablaCampos" align="center" cellspacing="0" cellpadding="0">
							<!-- FILA -->
							<tr>
								<td class="labelText" width="230px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fechasProducto"/>(*)
								</td>
								<td class="labelText"  width="100px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fInicio"/>
								</td>
								<td>
									<% if (bEditable) { %>
										<siga:Fecha  nombreCampo="fechaInicialProducto" valorInicial="<%=sFInicialProducto%>"/>
									<% } else { %>
										<siga:Fecha  nombreCampo="fechaInicialProducto" valorInicial="<%=sFInicialProducto%>" disabled="true" readOnly="true"/>
									<% }  %>
								</td>
								<td class="labelText"  width="100px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fFin"/>
								</td>
								<td>									
								<% if (bEditable) { %>
									<siga:Fecha  nombreCampo="fechaFinalProducto" valorInicial="<%=sFFinalProducto%>"/>
								<% } else { %>
									<siga:Fecha  nombreCampo="fechaFinalProducto" valorInicial="<%=sFFinalProducto%>" disabled="true" readOnly="true"/>
								<% }  %>
								</td>
							</tr>
							<!-- FILA -->
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fechasServicio"/>(*)
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fInicio"/>
								</td>	
								<td width="120px">
									<% if (bEditable) { %>
										<siga:Fecha  nombreCampo="fechaInicialServicio" valorInicial="<%=sFInicialServicio%>"/>
									<% } else { %>
										<siga:Fecha  nombreCampo="fechaInicialServicio" valorInicial="<%=sFInicialServicio%>" disabled="true" readOnly="true"/>
									<% }  %>
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fFin"/>
								</td>
								<td  width="120px">
								<% if (bEditable) { %>
									<siga:Fecha  nombreCampo="fechaFinalServicio" valorInicial="<%=sFFinalServicio%>"/>
								<% } else { %>
									<siga:Fecha  nombreCampo="fechaFinalServicio" valorInicial="<%=sFFinalServicio%>" disabled="true" readOnly="true"/>
								<% }  %>
								</td>
							</tr>
						</table>

	</siga:ConjCampos>
	<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.programacionGeneracion">

						<table class="tablaCampos" align="center" cellspacing="0" cellpadding="0">
							<!-- FILA -->
							<tr>
								<td class="labelText"  width="260px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fechaPrevistaGeneracion"/>&nbsp;(*)
								</td>
								<td   width="120px">
									<% if (bEditable) { %>
										<siga:Fecha  nombreCampo="fechaPrevistaGeneracion" valorInicial="<%=sFPrevistaGeneracion%>"/>
									<% } else { %>
										<siga:Fecha  nombreCampo="fechaPrevistaGeneracion" valorInicial="<%=sFPrevistaGeneracion%>" disabled="true" readOnly="true"/>
									<% }  %>
								</td>
								<td class="labelText"   width="40px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.hora"/>
								</td>
								<td class="boxConsulta">
								<%if (bEditableGeneracion){%>
									<html:text name="programarFacturacionForm" property="horasGeneracion"  value="<%=sHorasGeneracion%>" size="1" maxlength="2" styleClass="box" readonly="<%=!bEditableGeneracion%>"></html:text>					
									:
									<html:text name="programarFacturacionForm" property="minutosGeneracion"  value="<%=sMinutosGeneracion%>" size="1" maxlength="2" styleClass="box" readonly="<%=!bEditableGeneracion%>"></html:text>	
								<% } else {%>
									<html:text name="programarFacturacionForm" property="horasGeneracion"  value="<%=sHorasGeneracion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="<%=!bEditableGeneracion%>"></html:text>					
									:
									<html:text name="programarFacturacionForm" property="minutosGeneracion"  value="<%=sMinutosGeneracion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="<%=!bEditableGeneracion%>"></html:text>	
								<%}%>
								</td>
								
							</tr>
						</table>

	</siga:ConjCampos>
	<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.programacionConfirmacion">

						<table class="tablaCampos" align="center" cellspacing="0" cellpadding="0">
							<!-- FILA -->
							<tr>
								<td class="labelText"   width="260px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fechaPrevistaConfirmacion"/>
								</td>
								<td   width="120px">
								<% if (modoAction.equals("editar")) { %>
									<siga:Fecha  nombreCampo="fechaPrevistaConfirmacion" valorInicial="<%=sFPrevistaConfirmacion%>"/>
								<% } else { %>
										<siga:Fecha  nombreCampo="fechaPrevistaConfirmacion" valorInicial="<%=sFPrevistaConfirmacion%>" disabled="true" readOnly="true"/>
								<%}%>
								</td>
								<td class="labelText"  width="40px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.hora"/>
								</td>
								<td class="boxConsulta">
									<%if (modoAction.equals("editar")){%>
									<html:text name="programarFacturacionForm" property="horasConfirmacion"  value="<%=sHorasConfirmacion%>" size="1" maxlength="2" styleClass="box" readonly="false"></html:text>					
									:
									<html:text name="programarFacturacionForm" property="minutosConfirmacion"   value="<%=sMinutosConfirmacion%>" size="1" maxlength="2" styleClass="box" readonly="false"></html:text>	
								<% } else {%>
									<html:text name="programarFacturacionForm" property="horasConfirmacion"  value="<%=sHorasConfirmacion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true"></html:text>					
									:
									<html:text name="programarFacturacionForm" property="minutosConfirmacion"   value="<%=sMinutosConfirmacion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true"></html:text>	
								<%}%>
								</td>
								
							</tr>
								
							<tr>
								<td class="labelText"   width="260px">
									<siga:Idioma key="facturacion.seriesFacturacion.literal.fechaCargoenficheroBancario"/>
								</td>
								<td   width="120px">
								<% if (modoAction.equals("editar")) { %>
										<siga:Fecha  nombreCampo="fechaCargo" valorInicial="<%=sFCargoFicheroBanco%>"/>
								<% } else { %>
										<siga:Fecha  nombreCampo="fechaCargo" valorInicial="<%=sFCargoFicheroBanco%>" disabled="true" readOnly="true"/>
								<% } %>
								</td>					
							</tr>
						</table>

	</siga:ConjCampos>

	<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.configuracionCheck">

						<table class="tablaCampos" align="center" cellspacing="0" cellpadding="0">
							<!-- FILA -->
								<tr>
									<td class="labelText" style="text-align:left" colspan="2">
										<siga:Idioma key="facturacion.datosGenerales.literal.generaPDF"/>&nbsp;&nbsp;
										<% if (modoAction.equals("editar")) {%>
										<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
												<input type="checkbox" id="generarPDF" name="generarPDF" checked disabled>
										<%} else if ((generarPDF != null) && (generarPDF.equals("1"))) { %>
												<input type="checkbox" id="generarPDF" name="generarPDF" checked>
										<%} else { %>
												<input type="checkbox" id="generarPDF" name="generarPDF" >
										<%}%>
										<%}else{ %>
											<%if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) {%>
												<input type="checkbox" id="generarPDF" name="generarPDF" checked disabled>
										<%} else if ((generarPDF != null) && (generarPDF.equals("1"))) { %>
												<input type="checkbox" id="generarPDF" name="generarPDF" checked disabled>
										<% }else { %>
												<input type="checkbox" id="generarPDF" name="generarPDF" disabled >
										<% } %>
										<%}%>
									</td>
									<td class="labelText"  style="text-align:left" colspan="2">
										<siga:Idioma key="facturacion.datosGenerales.literal.envioFacturas"/>&nbsp;&nbsp;
										<% if (modoAction.equals("editar")) { %>
											<%  if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
													<input type="checkbox" id="enviarFacturas" name="enviarFacturas" onclick="actualiza();" checked>
											<% } else { %>
													<input type="checkbox" id="enviarFacturas" name="enviarFacturas" onclick="actualiza();">
											<% } %>
										<%} else{ %>
										<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
												<input type="checkbox" id="enviarFacturas"  name="enviarFacturas" onclick="actualiza();" checked disabled>
										<% } else { %>
												<input type="checkbox" id="enviarFacturas"  name="enviarFacturas" onclick="actualiza();" disabled>
										<% } %>
										<%} %>
									</td>
										<td id="titulo" class="labelText">
											<siga:Idioma key="envios.plantillas.literal.plantilla"/> 
										</td>
										<td>
										<% if (modoAction.equals("editar")) { %>
											<siga:ComboBD nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=plantillaEnviosSeleccionada%>" ancho="300" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>"/>
										<%} else{ %>
											<siga:ComboBD nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=plantillaEnviosSeleccionada%>" ancho="300" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>" readonly="true"/>
										<% } %>
										</td>
								</tr>
						</table>

	</siga:ConjCampos>
	
		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<% if (modoAction.equals("editar")) { %>
			<siga:ConjBotonesAccion botones="C,Y" modal="M"/>	
		<%}else{%>
			<siga:ConjBotonesAccion botones="C" modal="M"/>
		<%}%>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	</html:form>
</body>
</html>
		
					