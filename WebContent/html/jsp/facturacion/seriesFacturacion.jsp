<!DOCTYPE html>
<html>
<head>
	<!-- seriesFacturacion.jsp -->

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
	String idProgramacionSF 	= "";
	String idEstadoConfirmacion="";
	boolean bEditableConfirmacion = true;
	boolean bEditableGeneracion = true;
	boolean bEditable = true;
	boolean nuevo = true;
	String auxFcargo ="";
	String idTipoEnvioCorreoElectronico = ""+EnvTipoEnviosAdm.K_CORREO_ELECTRONICO;
	String parametrosCmbPlantillaEnvios[] = {usr.getLocation(),idTipoEnvioCorreoElectronico,"-1"};
	String parametrosPlantillasMail [] = {"-1",usr.getLocation(),"1"};
	ArrayList plantillaEnviosSeleccionada = new ArrayList();
	ArrayList plantillaSeleccionada = new ArrayList();
	
	// parametro para consultas de estados (Combo)
	String datoEstado[] = new String[1];
	datoEstado[0] = usr.getLanguage().toUpperCase();
	ArrayList estadoConfirmacionSel = new ArrayList();
	
	String modoAction=(String) ses.getAttribute("ModoAction");
	String desplegar = "true";
	String ocultarProgramacionConfirmacion = "true";
	String sFechaPresentacion = (String) request.getAttribute("fechaPresentacion");
	Vector datosInformeFac = (Vector) request.getAttribute("datosInformeFac");
	Vector datosInformeFacOriginal = (Vector) request.getAttribute("datosInformeFacOriginal");


	if(request.getSession().getAttribute("DATABACKUP")!= null){		
		// Se trata de modificacion
		Enumeration en = ((Vector)request.getSession().getAttribute("DATABACKUP")).elements();
		Hashtable hash = (Hashtable)en.nextElement();
		
		sDescripcion = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_DESCRIPCION);
		idProgramacionSF = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_IDPROGRAMACION);
		LSerieFacturacion = UtilidadesHash.getLong(hash, FacFacturacionProgramadaBean.C_IDSERIEFACTURACION);
		sSerieFacturacionDesc = UtilidadesHash.getString(hash, FacSerieFacturacionBean.C_NOMBREABREVIADO);
		sFInicialProducto = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS));
		sFFinalProducto = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS));	
		sFInicialServicio = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS));
		sFFinalServicio = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS));
		sFProgramacion = com.atos.utils.GstDate.getFormatedDateShort("", UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAPROGRAMACION));
		sFRealGeneracion = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_FECHAREALGENERACION);
		idEstadoConfirmacion = UtilidadesHash.getString(hash, FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION);
		estadoConfirmacionSel.add(idEstadoConfirmacion);

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
				parametrosCmbPlantillaEnvios[2] = idTipoPlantilla;
			}
		}
		
		nuevo	= false;				
		
		if (!sFRealGeneracion.trim().equals("")||modoAction.trim().equals("ver")) {
			bEditable=false;
			bEditableGeneracion=false;
		}
		
		if (modoAction.equals("ver")){
			desplegar = "false";
		}else{
			if(hash.get(FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION) != null &&
					(idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA.toString()) ||	idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA.toString()))){
				desplegar = "false";
			}
		}		
		
	} else {		
		if (sFProgramacion.trim().equals("")) {
			sFProgramacion = UtilidadesString.formatoFecha(new Date(),"dd/MM/yyyy");
		}	
	}
		
	if ((sFPrevistaConfirmacion!=null && !sFPrevistaConfirmacion.equals("")) || (sFechaPresentacion!=null && !sFechaPresentacion.equals(""))) {
		ocultarProgramacionConfirmacion = "false";
	}	
	
	boolean bObligatorioFechasSEPA = true; // Esta variable es necesaria para fechasFicheroBancario.jsp
	if ((modoAction!=null && modoAction.trim().equals("nuevaPrevision")) ||
		(modoAction!=null && modoAction.trim().equals("editar") && (
				idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERACION_PROGRAMADA.toString()) ||
				idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.ERROR_GENERACION.toString()) ||
				idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERADA.toString())))) {
		bObligatorioFechasSEPA = false;
	}
		
	String iconos="E,B";
	String botones="N"; 
	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
  	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="confirmarFacturacionForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->

	<script LANGUAGE="JavaScript">
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
			for (var i = 0; i < numstr.length; i++) {
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


		function refrescarLocal() {	
			document.confirmarFacturacionForm.modo.value="editarFechas";
			document.confirmarFacturacionForm.target = "mainWorkArea";
			document.confirmarFacturacionForm.submit();
		}
		
		// Asociada al boton GuardarCerrar
		function accionGuardar() {	

			var fechaConf = trim(document.confirmarFacturacionForm.fechaPrevistaConfirmacion.value);
			var fechaGen = trim(document.confirmarFacturacionForm.fechaPrevistaGeneracion.value);
			f=document.confirmarFacturacionForm;			
				
			sub();
			
			if (trim(fechaGen)!="") {
				//Para la validacion no tengo en cuenta si empieza por 0 y tiene 2 digitos (tanto hora como minuto)
				var horas = trim(document.confirmarFacturacionForm.horasGeneracion.value);
				var minutos = trim(document.confirmarFacturacionForm.minutosGeneracion.value);

				if (horas.length==1) {
					document.confirmarFacturacionForm.horasGeneracion.value = "0" + horas;
				}
				if (minutos.length==1) {
					document.confirmarFacturacionForm.minutosGeneracion.value = "0" + minutos;
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
				
				<% if (bEditable) { %>
					 
					fechaActual = getFechaActualDDMMYYYY();
					// LA FECHA COBRO ES OBLIGATORIA SI ESTA MARCADO EL CHECK DE COBRO
					if(compararFecha(trim(fechaGen),fechaActual) == 2){
						alert("La Fecha Prevista de Generacíon no puede ser anterior al día actual");
						fin();
					 	return false;
					 }						
				
				<% } %>
			}	
			
			if (trim(fechaConf)!="") {
				horas = trim(document.confirmarFacturacionForm.horasConfirmacion.value);
				minutos = trim(document.confirmarFacturacionForm.minutosConfirmacion.value);
				if (horas.length==1) {
					document.confirmarFacturacionForm.horasConfirmacion.value = "0" + horas;
				}
				if (minutos.length==1) {
					document.confirmarFacturacionForm.minutosConfirmacion.value = "0" + minutos;
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

			if (!validateConfirmarFacturacionForm(document.confirmarFacturacionForm)){	
				fin();
				return false;
			}
			
			if((f.fechaInicialProducto.value!= '' && f.fechaFinalProducto.value!= '') || (f.fechaInicialServicio.value!= '' && f.fechaFinalServicio.value!= '')){ 
				// Comprobamos las fechas
				if (compararFechaRuano (f.fechaInicialProducto, f.fechaFinalProducto) == -1 || compararFechaRuano (f.fechaInicialProducto, f.fechaFinalProducto) == 1) {
					alert ("<siga:Idioma key='messages.fechas.rangoFechas'/>");
					fin();
					return false;
				}
				
				if (compararFechaRuano (f.fechaInicialServicio, f.fechaFinalServicio) == -1 || compararFechaRuano (f.fechaInicialServicio, f.fechaFinalServicio) == 1) {
					alert ("<siga:Idioma key='messages.fechas.rangoFechas'/>");
					fin();
					return false;
				}
				
			}else{	
				alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="facturacion.seriesFacturacion.literal.fechasProducto"/> o <siga:Idioma key="facturacion.seriesFacturacion.literal.fechasServicio"/>');
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
					var horasConf = trim(document.confirmarFacturacionForm.horasConfirmacion.value);
					var minutosConf = trim(document.confirmarFacturacionForm.minutosConfirmacion.value);
					var horasGen = trim(document.confirmarFacturacionForm.horasGeneracion.value);
					var minutosGen = trim(document.confirmarFacturacionForm.minutosGeneracion.value);
					
					if (horasConf<horasGen) {
						alert ("<siga:Idioma key='messages.fechas.rangoHorasPrevistas'/>");
						fin();
						return false;
					} else if (horasConf==horasGen) {
						if (minutosConf<minutosGen) {
							alert ("<siga:Idioma key='messages.fechas.rangoHorasPrevistas'/>");
							fin();
							return false;
						}
					}
				}
			}			

			if(document.confirmarFacturacionForm.enviarFacturas.checked){
				if(document.confirmarFacturacionForm.idTipoPlantillaMail.value == ""){
					alert('<siga:Idioma key="Facturacion.mensajes.obligatorio.plantillaMail"/>');
					fin();
					return false;
				}
			}
			
			// JPT: Si se ha introducido 'F. Prevista Confirm.', se deben introducir las fechas de SEPA
			if (jQuery('#fechaPrevistaConfirmacion').val()!="" && jQuery('#fechaPresentacion').val()=="") {
				alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="facturacion.fechasficherobancario.fechapresentacion"/>');
				fin();
				return false;
			}
			
			// JPT: Si se han introducido las fechas de SEPA, se deben introducir 'F. Prevista Confirm.' 
			if (jQuery('#fechaPresentacion').val()!="" && jQuery('#fechaPrevistaConfirmacion').val()=="") {
				alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="facturacion.seriesFacturacion.literal.fechaPrevistaConfirmacion"/>');
				fin();
				return false;
			}			
						
			// JPT - Validacion de las fehas nuevas SEPA. Para nuevas previsiones no es obligatorio introducir las fechas
			if (!validarFechasSEPA()) {
				fin();
				return false;
			}
			
			if(<%=nuevo%>){				
				if (trim(fechaConf)!="") {
					var type = '<siga:Idioma key="facturacion.mantenimientoFacturacion.mensaje.alertaConfirmacion"/>';
					if(confirm(type)){
						f.modo.value = "insertar";
					}
					fin();
					
				}else{
					f.modo.value = "insertar";
				}
				
			} else{
				
				if(<%=desplegar%>){				
					if (trim(fechaConf)!="") {
						var type = '<siga:Idioma key="facturacion.mantenimientoFacturacion.mensaje.alertaConfirmacion"/>';
						if(confirm(type)){
							f.modo.value = "modificar";
						}
						fin();
					} else {
						f.modo.value = "modificar";
					}

				} else{
					f.modo.value = "modificar";
				}				
			}
			
			document.all.confirmarFacturacionForm.submit();					
		}			
	
		function actualiza() {
			if (document.forms[0].enviarFacturas.checked==true) {
				document.forms[0].generarPDF.checked=true;
			   	jQuery("#generarPDF").attr("disabled","disabled");
				jQuery("#idTipoPlantillaMail").removeAttr("disabled");					
			} else {
				jQuery("#generarPDF").removeAttr("disabled");	
				jQuery("#idTipoPlantillaMail").attr("disabled","disabled");
				jQuery("#idTipoPlantillaMail").val("");
			}
			return false;
		}

		function inicio() {
			if (document.forms[0].enviarFacturas.checked==true) {
				if(document.forms[0].idTipoPlantillaMail)
					jQuery("#idTipoPlantillaMail").removeAttr("disabled");
			} else {
				if(document.forms[0].idTipoPlantillaMail)
				   	jQuery("#idTipoPlantillaMail").attr("disabled","disabled");
			}
			
			<% if (modoAction.equals("nuevaPrevision")) { %>
				var fechaActual = getFechaActualDDMMYYYY();	
				jQuery('#fechaPrevistaGeneracion').val(fechaActual);	
				actualizarHoraFecha(fechaActual,'horasGeneracion','minutosGeneracion');
			<% } %>
		}
		
		function cambiaSerie() {
			document.forms[0].modo.value="actualizarDatosSerieFacturacion";
			document.forms[0].submit();
		}

		function Calcularvalor(){
			document.forms[0].fechaCargo.value =document.forms[0].fechaPrevistaConfirmacion.value;
		} 
		
		// JPT: Indica la hora y minuto actual en caso de indicar hoy
		function actualizarHoraFecha(fecha, idHorasFecha, idMinutosFecha){
			if (trim(fecha)=="") {
				jQuery("#"+idHorasFecha).val("");					
				jQuery("#"+idMinutosFecha).val("");
				
			} else {
				var fechaActual = getFechaActualDDMMYYYY();	
				if (compararFecha(trim(fecha), fechaActual) == 0){
					var date = new Date();
					hora = date.getHours();
					min = date.getMinutes();
					if (hora.toString().length==1) {
						hora = "0" + hora;
					}
					if (min.toString().length==1) {
						min = "0" + min;
					}
					jQuery("#"+idHorasFecha).val(hora);					
					jQuery("#"+idMinutosFecha).val(min);
					
				} else {
					jQuery("#"+idHorasFecha).val("00");					
					jQuery("#"+idMinutosFecha).val("00");									
				}
			}
		}
		
		function accionVolver() 
		{		
			document.confirmarFacturacionForm.modo.value="abrirVolver";
			document.confirmarFacturacionForm.target = "mainWorkArea";
			document.confirmarFacturacionForm.submit();
		}
		
		function accionRecalcularFacturacion() {
			sub();
			var type = '<siga:Idioma key="facturacion.mantenimientoFacturacion.mensaje.confirmacionRecalcular"/>';
			if(confirm(type)){
				document.confirmarFacturacionForm.modo.value = "recalcular";
				document.confirmarFacturacionForm.target = "mainWorkArea";
				document.confirmarFacturacionForm.submit();				
			}
			fin();		
		}		
		
	</script>
</head>

<body onLoad="inicio();">
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="facturacion.programarFacturacion.literal.cabecera"/> 
			</td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->	
	<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">
		<!-- INICIO: CAMPOS -->
		<html:hidden property="modo" value=""/>			
		<html:hidden property="idProgramacion" value='<%=idProgramacionSF%>'/>
		<html:hidden property="estadoConfirmacion" value='<%=idEstadoConfirmacion%>'/>
		
		<siga:ConjCampos leyenda="facturacion.asignacionDeConceptosFacturables.titulo">
			<table class="tablaCampos" align="center" border="0" cellspacing="0" cellpadding="0">						
				<tr align="center"> 
					<td class="labelText" width="180px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.descripcion"/>&nbsp;(*)
					</td>
					<td align="left">
						<% if (modoAction.equals("editar") || modoAction.equals("nuevaPrevision")) { %>
							<html:text name="confirmarFacturacionForm" styleId="descripcionProgramacion" property="descripcionProgramacion" value="<%=sDescripcion%>" size="50" maxlength="255" styleClass="box"/>
						<% } else { %>
							<html:text name="confirmarFacturacionForm" styleId="descripcionProgramacion" property="descripcionProgramacion" value="<%=sDescripcion%>" size="50" styleClass="boxConsulta" readonly="true" />
						<% } %>	
					</td>
					<% if (!modoAction.equals("nuevaPrevision")) { %>
						<td class="labelText"><siga:Idioma key="facturacion.estado"/></td>
						<td>
							<siga:ComboBD nombre = "estadoConfirmacion" tipo="cmbEstadoConfirmacion" ancho="200" parametro="<%=datoEstado%>" elementoSel="<%=estadoConfirmacionSel%>" clase="boxConsulta" readonly="true"/>					
						</td>		
					<% } %>				
				</tr>
						
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.serieFacturacion"/>&nbsp;(*)
					</td>
					<td>
						<% if(nuevo) {
								dato[0] = usr.getLocation().toString();
						%>
				    		<siga:ComboBD nombre="idSerieFacturacion" tipo="cmbSerieFacturacionAlta" parametro="<%=dato%>" clase="boxCombo" accion="cambiaSerie();" obligatorio="true"/>
						<% } else { %>
							<html:hidden property="idSerieFacturacion" value='<%=LSerieFacturacion.toString()%>'/>
							<html:text property="serieFacturacionDes" value='<%=sSerieFacturacionDesc%>' size="60" styleClass="boxConsulta" readonly="true" />
						<%}%>								
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fechaProgramacion"/>&nbsp;(*)
					</td>
					<td>
						<html:text name="confirmarFacturacionForm" property="fechaProgramacion" value="<%=sFProgramacion%>" size="10" styleClass="boxConsulta" readonly="true" />									
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	
		<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.periodosFacturacion">
			<table class="tablaCampos" align="center" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelText" width="350px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fechasProducto"/>
					</td>
					<td class="labelText" width="100px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fInicio"/>
					</td>
					<td width="120px">
						<% if (bEditable) { %>
							<siga:Fecha  nombreCampo="fechaInicialProducto" valorInicial="<%=sFInicialProducto%>"/>
						<% } else { %>
							<siga:Fecha  nombreCampo="fechaInicialProducto" valorInicial="<%=sFInicialProducto%>" disabled="true" readOnly="true"/>
						<% }  %>
					</td>
					
					<td class="labelText" width="100px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fFin"/>
					</td>
					<td width="120px">									
						<% if (bEditable) { %>
							<siga:Fecha  nombreCampo="fechaFinalProducto" valorInicial="<%=sFFinalProducto%>"/>
						<% } else { %>
							<siga:Fecha  nombreCampo="fechaFinalProducto" valorInicial="<%=sFFinalProducto%>" disabled="true" readOnly="true"/>
						<% }  %>
					</td>
				</tr>

				<tr>
					<td class="labelText" width="350px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fechasServicio"/>
					</td>
					<td class="labelText">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fInicio"/>
					</td>	
					<td>
						<% if (bEditable) { %>
							<siga:Fecha  nombreCampo="fechaInicialServicio" valorInicial="<%=sFInicialServicio%>"/>
						<% } else { %>
							<siga:Fecha  nombreCampo="fechaInicialServicio" valorInicial="<%=sFInicialServicio%>" disabled="true" readOnly="true"/>
						<% }  %>
					</td>
					<td class="labelText">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fFin"/>
					</td>
					<td>
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
			<table class="tablaCampos" align="center" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelText"  width="210px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fechaPrevistaGeneracion"/>&nbsp;(*)
					</td>
					<td width="120px">
						<% if (bEditable) { %>
							<siga:Fecha  nombreCampo="fechaPrevistaGeneracion" valorInicial="<%=sFPrevistaGeneracion%>" postFunction="actualizarHoraFecha(this.value,'horasGeneracion','minutosGeneracion');"/>
						<% } else { %>
							<siga:Fecha  nombreCampo="fechaPrevistaGeneracion" valorInicial="<%=sFPrevistaGeneracion%>" disabled="true" readOnly="true"/>
						<% }  %>
					</td>
					
					<td class="labelText" width="40px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.hora"/>
					</td>
					<td>
						<% if (bEditableGeneracion) { %>
							<html:text name="confirmarFacturacionForm" styleId="horasGeneracion" property="horasGeneracion"  value="<%=sHorasGeneracion%>" size="1" maxlength="2" styleClass="box" readonly="<%=!bEditableGeneracion%>" />					
							:
							<html:text name="confirmarFacturacionForm" styleId="minutosGeneracion" property="minutosGeneracion" value="<%=sMinutosGeneracion%>" size="1" maxlength="2" styleClass="box" readonly="<%=!bEditableGeneracion%>" />	
						<% } else {%>
							<html:text name="confirmarFacturacionForm" property="horasGeneracion"  value="<%=sHorasGeneracion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="<%=!bEditableGeneracion%>" />					
							:
							<html:text name="confirmarFacturacionForm" property="minutosGeneracion"  value="<%=sMinutosGeneracion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="<%=!bEditableGeneracion%>" />	
						<% } %>
					</td>					
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.programacionConfirmacion" oculto="<%=ocultarProgramacionConfirmacion%>" desplegable="<%=desplegar%>">
			<table class="tablaCampos" align="center" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelText" width="210px">
						<siga:Idioma key="facturacion.seriesFacturacion.literal.fechaPrevistaConfirmacion"/><%if(bObligatorioFechasSEPA){%>&nbsp;(*)<%}%>
					</td>
					<td width="120px">
						<% if (modoAction.equals("editar") || modoAction.equals("nuevaPrevision")) { %>
							<siga:Fecha nombreCampo="fechaPrevistaConfirmacion" valorInicial="<%=sFPrevistaConfirmacion%>" postFunction="actualizarHoraFecha(this.value,'horasConfirmacion','minutosConfirmacion');"/>
						<% } else { %>
							<siga:Fecha nombreCampo="fechaPrevistaConfirmacion" valorInicial="<%=sFPrevistaConfirmacion%>" disabled="true" readOnly="true"/>
						<% } %>
					</td>
					
					<td class="labelText" >
						<siga:Idioma key="facturacion.seriesFacturacion.literal.hora"/>&nbsp;&nbsp;&nbsp;

						<% if (modoAction.equals("editar") || modoAction.equals("nuevaPrevision")) { %>
							<html:text name="confirmarFacturacionForm" styleId="horasConfirmacion" property="horasConfirmacion"  value="<%=sHorasConfirmacion%>" size="1" maxlength="2" styleClass="box" readonly="false" />					
							:
							<html:text name="confirmarFacturacionForm" styleId="minutosConfirmacion" property="minutosConfirmacion"   value="<%=sMinutosConfirmacion%>" size="1" maxlength="2" styleClass="box" readonly="false" />	
						<% } else {%>
							<html:text name="confirmarFacturacionForm" property="horasConfirmacion"  value="<%=sHorasConfirmacion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true" />					
							:
							<html:text name="confirmarFacturacionForm" property="minutosConfirmacion"   value="<%=sMinutosConfirmacion%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true" />	
						<% } %>
					</td>									
				</tr>
			</table>
				
 			<%@ include file="/html/jsp/facturacion/fechasFicheroBancario.jsp"%>
<%-- 			<jsp:include page="/html/jsp/facturacion/fechasFicheroBancario.jsp"></jsp:include> --%>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.configuracionCheck" oculto="<%=desplegar%>" desplegable="<%=desplegar%>">
			<table class="tablaCampos" align="center" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelText" style="text-align:left">
						<siga:Idioma key="facturacion.datosGenerales.literal.generaPDF"/>&nbsp;
						<% if (modoAction.equals("editar") || modoAction.equals("nuevaPrevision")) { %>
							<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
								<input type="checkbox" id="generarPDF" name="generarPDF" checked disabled>
							<% } else if ((generarPDF != null) && (generarPDF.equals("1"))) { %>
								<input type="checkbox" id="generarPDF" name="generarPDF" checked>
							<% } else { %>
								<input type="checkbox" id="generarPDF" name="generarPDF" >
							<% } %>
						
						<% }else{ %>
							<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
								<input type="checkbox" id="generarPDF" name="generarPDF" checked disabled>
							<% } else if ((generarPDF != null) && (generarPDF.equals("1"))) { %>
								<input type="checkbox" id="generarPDF" name="generarPDF" checked disabled>
							<% } else { %>
								<input type="checkbox" id="generarPDF" name="generarPDF" disabled >
							<% } %>
						<%}%>
					</td>
					
				</tr>
				<tr>					
					<td class="labelText" style="text-align:left">
						<siga:Idioma key="facturacion.datosGenerales.literal.envioFacturas"/>&nbsp;
						<% if (modoAction.equals("editar") || modoAction.equals("nuevaPrevision")) { %>
							<%  if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
									<input type="checkbox" id="enviarFacturas" name="enviarFacturas" onclick="actualiza();" checked>
							<% } else { %>
									<input type="checkbox" id="enviarFacturas" name="enviarFacturas" onclick="actualiza();">
							<% } %>
							
						<% } else { %>
							<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
								<input type="checkbox" id="enviarFacturas"  name="enviarFacturas" onclick="actualiza();" checked disabled>
							<% } else { %>
								<input type="checkbox" id="enviarFacturas"  name="enviarFacturas" onclick="actualiza();" disabled>
							<% } %>
						<% } %>
					</td>
					
					<td id="titulo" class="labelText" rowspan="2">
						<siga:Idioma key="envios.plantillas.literal.plantilla"/> 
					</td>
					<td rowspan="2">
						<% if (modoAction.equals("editar") || modoAction.equals("nuevaPrevision")) { %>
							<siga:ComboBD nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=plantillaEnviosSeleccionada%>" ancho="300" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>"/>
						<% } else{ %>
							<siga:ComboBD nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxComboConsulta" elementoSel="<%=plantillaEnviosSeleccionada%>" ancho="300" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>" readonly="true"/>
						<% } %>
					</td>						
				</tr>
			</table>
		</siga:ConjCampos>
		
		<% if(idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA.toString())){ %>		
			<siga:ConjCampos leyenda="facturacion.mantenimnientoFacturacion.literal.informeConfirmacion" >
				<div align="center" >
					<td class="labelText">
						<siga:Idioma key="Información Actual"/>
					</td>	
					<table width="50%"  border="0" ><tr><td>
						<siga:Table 
							name="tablaDatos"
							border="1"
							columnNames="pys.solicitudCompra.literal.formaPago, facturacion.consultamorosos.literal.nfacturas,facturacion.datosFactura.literal.PagosAnticipado, facturacion.lineasFactura.literal.importeTotal"
							columnSizes="40,20,20,20"
							fixedHeight="10">
							   
						<!-- Campo obligatorio -->
						<% if (datosInformeFac==null || datosInformeFac.size()==0){%>
						 		<tr class="notFound">
									<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
								</tr>
						<%}else{%>
							<%	int recordNumber=1;
								while ((recordNumber) <= datosInformeFac.size()){	 
									Hashtable hash = (Hashtable)datosInformeFac.get(recordNumber-1);
									String formapago=(String)hash.get("FORMA_PAGO");
									String importe=(String)hash.get("IMPORTE");
									String numfac=(String)hash.get("NUM_FACTURAS");
									String anticipado=(String)hash.get("ANTICIPADO");
								%>	
									  	
							  	<siga:FilaConIconos fila="1" botones="" visibleEdicion="no" visibleConsulta="no" visibleBorrado="no" pintarEspacio="no" clase="listaNonEdit">
									<td>&nbsp;<%=formapago%></td>
									<td align="right">&nbsp;<%=numfac%></td>
									<td align="right">&nbsp;<%=UtilidadesNumero.formato(anticipado)%>&nbsp;&euro;</td>
									<td align="right">&nbsp;<%=UtilidadesNumero.formato(importe)%>&nbsp;&euro;</td>
								</siga:FilaConIconos>	
								<%recordNumber++;%>
								<%	}
							}%>	
						</siga:Table>
						
					</td></tr></table>			
				</div>
				
				<div align="center" >
				<td class="labelText">
						<siga:Idioma key="Información Original"/>
					</td>	
								<table width="50%"  border="0" ><tr><td>
						<siga:Table 
							name="tablaDatosOriginal"
							border="1"
							columnNames="pys.solicitudCompra.literal.formaPago, facturacion.consultamorosos.literal.nfacturas,facturacion.datosFactura.literal.PagosAnticipado, facturacion.lineasFactura.literal.importeTotal"
							columnSizes="40,20,20,20"
							fixedHeight="10">
							   
						<!-- Campo obligatorio -->
						<% if (datosInformeFacOriginal==null || datosInformeFacOriginal.size()==0){%>
						 		<tr class="notFound">
									<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
								</tr>
						<%}else{%>
							<%	int recordNumber=1;
								while ((recordNumber) <= datosInformeFacOriginal.size()){	 
									Hashtable hash = (Hashtable)datosInformeFacOriginal.get(recordNumber-1);
									String formapago=(String)hash.get("FORMA_PAGO");
									String importe=(String)hash.get("IMPORTE");
									String numfac=(String)hash.get("NUM_FACTURAS");
									String anticipado=(String)hash.get("ANTICIPADO");
								%>	
									  	
							  	<siga:FilaConIconos fila="1" botones="" visibleEdicion="no" visibleConsulta="no" visibleBorrado="no" pintarEspacio="no" clase="listaNonEdit">
									<td>&nbsp;<%=formapago%></td>
									<td align="right">&nbsp;<%=numfac%></td>
									<td align="right">&nbsp;<%=UtilidadesNumero.formato(anticipado)%>&nbsp;&euro;</td>
									<td align="right">&nbsp;<%=UtilidadesNumero.formato(importe)%>&nbsp;&euro;</td>
								</siga:FilaConIconos>	
								<%recordNumber++;%>
								<%	}
							}%>	
						</siga:Table>
						
					</td></tr></table>
				</div>
			</siga:ConjCampos>		
		<% } %>
	
		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<% if (modoAction.equals("editar") ||  modoAction.equals("nuevaPrevision")) { %>
			<% if( idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERADA.toString()) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA.toString()) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.ERROR_CONFIRMACION.toString())){ %>
				<siga:ConjBotonesAccion botones="V,RF,G" clase="botonesDetalle"/>
			<% } else { %>
				<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle"/>
			<% } %>

		<% } else { %>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>
		<% } %>
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</html:form>
</body>
</html>			