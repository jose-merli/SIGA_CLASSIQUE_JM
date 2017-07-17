<!DOCTYPE html>
<html>
<head>
<!-- consultaConfirmarFacturacion.jsp -->
<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 10-03-2005 - Inicio
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
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="com.siga.beans.FacEstadoConfirmFactAdm"%>
<%@ page import="com.siga.beans.FacEstadoConfirmFactBean"%>
<%@ page import="com.siga.beans.FacSerieFacturacionBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String fInicialProducto;
	String fFinalProducto;
	String fInicialServicio;
	String fFinalServicio;
	String fProgramacion;
	String fPrevistaGeneracion;
	String fRealGeneracion;
	String fRealConfirmacion;
	String fPrevistaConfirmacion;
	String sEstadoConfirmacion;
	String sEstadoPDF;
	String sEstadoEnvios;
	String sCheckArchivado;
	String fCargo=null;
	Hashtable htEstados = (Hashtable)request.getAttribute("ESTADOS");
	FacEstadoConfirmFactAdm admEstados = new FacEstadoConfirmFactAdm(usr);
%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	

	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">

		function refrescarLocal() {
			parent.buscar();
		}
		
		function programarConfirmacion(fila)
		{
			var datos;
			var fechaCargoNecesaria = '0';//Si vale '0' no es necesaria.
			
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = "";
			var j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
			var flag = true;

			j = 1;
			while (flag) {
				  var aux = 'oculto' + fila + '_' + j;
				  var oculto = document.getElementById(aux);
				  if (oculto == null)  { flag = false; }
				  else { datos.value = datos.value + oculto.value + ','; }
				  j++;
			}
			datos.value = datos.value + "%";
			
			//Calculo el valor de la fecha de cargo (numero de 0 a N. Si 0 no es necesario)
			var aux2 = 'oculto' + fila + '_' + 4;
			fechaCargoNecesaria = document.getElementById(aux2).value;
		
			//Para ver si todo va bien:
			var ok = true;
		
			// Voy al Action si me pide la fecha de cargo:
			if (fechaCargoNecesaria != '0') {
				document.all.confirmarFacturacionForm.modo.value = "nuevo";
				var resultado = ventaModalGeneral(document.all.confirmarFacturacionForm.name,"P");

				// Compruebo que si necesito la fecha de cargo la he obtenido de la modal:
				if (resultado==undefined || (resultado!=undefined && resultado=='')) {
						//alert('<siga:Idioma key="censo.consultaComponentesJuridicos.literal.fechaCargo"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
						return false;
				} else {
					// Almaceno la fecha de Cargo de la modal:
					document.all.confirmarFacturacionForm.fechaCargo.value = resultado;
				}
			}

			// Si todo ha ido bien y confirmo la facturacion:
			if (ok) {
				document.confirmarFacturacionForm.modo.value = "confirmarFactura";
				var f = document.confirmarFacturacionForm.name;
				// Abro la ventana de las tuercas:
				document.confirmarFacturacionForm.submit();
			}
		 }
		
		function archivar(fila)
		{
			var datos;
			
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = "";
			var j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
			var flag = true;

			j = 1;
			while (flag) {
			  var aux = 'oculto' + fila + '_' + j;
			  var oculto = document.getElementById(aux);
			  if (oculto == null)  { flag = false; }
			  else { datos.value = datos.value + oculto.value + ','; }
			  j++;
			}
			datos.value = datos.value + "%";
		
			var auxTarget = document.confirmarFacturacionForm.target;
		    document.confirmarFacturacionForm.target="submitArea";
		    document.confirmarFacturacionForm.modo.value = "archivarFactura";
		    document.confirmarFacturacionForm.submit();
		    document.confirmarFacturacionForm.target=auxTarget;
		 }

		function enviar(fila)
		{
			sub();
			if(confirm('<siga:Idioma key="facturacion.confirmacionFacturacion.literal.confirmarEnvioFacturas"/>')){
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = "";
				var j;
				var tabla;
				tabla = document.getElementById('tablaDatos');
				var flag = true;
		
				j = 1;
				while (flag) {
				  var aux = 'oculto' + fila + '_' + j;
				  var oculto = document.getElementById(aux);
				  if (oculto == null)  { flag = false; }
				  else { datos.value = datos.value + oculto.value + ','; }
				  j++;
				}
				datos.value = datos.value + "%";
				var auxTarget = document.confirmarFacturacionForm.target;
			    document.confirmarFacturacionForm.target="submitArea";
			    document.confirmarFacturacionForm.modo.value = "enviar";
			    document.confirmarFacturacionForm.submit();
			    document.confirmarFacturacionForm.target=auxTarget;
			}else{
				fin();
			}
		 }
		
		function download(fila)
		{
			if(confirm('<siga:Idioma key="facturacion.confirmacionFacturacion.literal.confirmarDescargaFactura"/>')) 
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = "";
				var i, j;
				var tabla;
				tabla = document.getElementById('tablaDatos');
				var flag = true;
				j = 1;
				while (flag)
				{
					var aux = 'oculto' + fila + '_' + j;
					var oculto = document.getElementById(aux);
					if (oculto == null)
					{
						flag = false;
					}
					else
					{
						datos.value = datos.value + oculto.value + ',';
					}
					j++;
				}
				datos.value = datos.value + "%";
				
				var auxTarget = document.confirmarFacturacionForm.target;
			    document.confirmarFacturacionForm.target="submitArea";
			    document.confirmarFacturacionForm.modo.value = "download";
			    document.confirmarFacturacionForm.submit();
			    document.confirmarFacturacionForm.target=auxTarget;
			}
		}
		
		function descargarInformeGeneracion(fila)
		{
			if(confirm('<siga:Idioma key="facturacion.generacionFacturacion.literal.confirmarDescargaFichero"/>'))
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = "";
				var i, j;
				var tabla;
				tabla = document.getElementById('tablaDatos');
				var flag = true;
				j = 1;
				while (flag) 
				{
					var aux = 'oculto' + fila + '_' + j;
					var oculto = document.getElementById(aux);
					if (oculto == null)
					{
						flag = false;
					}
					else
					{
						datos.value = datos.value + oculto.value + ',';
					}
					j++;
				}
				datos.value = datos.value + "%";

				var auxTarget = document.confirmarFacturacionForm.target;
			    document.confirmarFacturacionForm.target="submitArea";
			    document.confirmarFacturacionForm.modo.value = "descargarInformeGeneracion";
			    document.confirmarFacturacionForm.submit();
			    document.confirmarFacturacionForm.target=auxTarget;
			}
		}
		
		function descargaLog(fila)
		{
			var datos;
			
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = "";
			var j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
			var flag = true;

			j = 1;
			while (flag) {
			  var aux = 'oculto' + fila + '_' + j;
			  var oculto = document.getElementById(aux);
			  if (oculto == null)  { flag = false; }
			  else { datos.value = datos.value + oculto.value + ','; }
			  j++;
			}
			datos.value = datos.value + "%";
			
		   var auxTarget = document.confirmarFacturacionForm.target;
		   document.confirmarFacturacionForm.target="submitArea";
		   document.confirmarFacturacionForm.modo.value = "descargaLog";
		   document.confirmarFacturacionForm.submit();
		   document.confirmarFacturacionForm.target=auxTarget;
		 }
		 		
		function editarFacturacion(fila) {
			var datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = "";
			var flag = true;
			var j = 1;
			while (flag) {
			  	var aux = 'oculto' + fila + '_' + j;
			  	var oculto = document.getElementById(aux);
			  	if (oculto == null)  {
				  	flag = false;
				} else {
					datos.value = datos.value + oculto.value + ',';
				}
			  	j++;
			}
			datos.value = datos.value + "%";
			
			document.confirmarFacturacionForm.modo.value = "editarFechas";
			document.confirmarFacturacionForm.submit();
		}

		//Funcion asociada al boton Consultar
		function consultarfactura(fila) {
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = "";
			var j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
			var flag = true;
			j = 1;
			while (flag) {
			  var aux = 'oculto' + fila + '_' + j;
			  var oculto = document.getElementById(aux);
			  if (oculto == null)  { flag = false; }
			  else { datos.value = datos.value + oculto.value + ','; }
			  j++;
			}
			datos.value = datos.value + "%";
			
			document.confirmarFacturacionForm.modo.value = "consultarfactura";
			document.confirmarFacturacionForm.submit();
		 }
		
		function eliminiarfact(fila) {
			var datos;
			var type = '<siga:Idioma key="messages.deleteConfirmation"/>';
			subicono('iconoboton_eliminiarfact'+fila);
			
			if (confirm(type)){
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = "";
				preparaDatos(fila,'tablaDatos', datos);
				
				var auxTarget = document.confirmarFacturacionForm.target;
				document.confirmarFacturacionForm.target="submitArea";
				document.confirmarFacturacionForm.modo.value = "Borrar";
				document.confirmarFacturacionForm.submit();
				document.confirmarFacturacionForm.target=auxTarget;
			}
		}
		
		function traspasarFacturas(fila)
		{
			if (confirm('<siga:Idioma key="facturacion.generacionFacturacion.literal.confirmarTraspasoFacturas"/>'))
		    {
		    	datos = document.getElementById('tablaDatosDinamicosD');
		    	datos.value = "";
		    	preparaDatos(fila,'tablaDatos', datos);
		    	
			    var auxTarget = document.confirmarFacturacionForm.target;
			    document.confirmarFacturacionForm.target = "submitArea";
			    document.confirmarFacturacionForm.modo.value = "traspasarFacturas";
			    document.confirmarFacturacionForm.submit();
			    document.confirmarFacturacionForm.target = auxTarget;
		    }
		}
		
	</script>
</head>

<body class="tablaCentralCampos">
<bean:define id="path" name="org.apache.struts.action.mapping.instance"
	property="path" scope="request" />
<bean:define id="paginaSeleccionada" name="paginaSeleccionada"
		scope="request"></bean:define>
	<bean:define id="totalRegistros" name="totalRegistros" scope="request"></bean:define>
	<bean:define id="registrosPorPagina" name="registrosPorPagina"
		scope="request"></bean:define>
		<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="mainWorkArea">
			<html:hidden name="confirmarFacturacionForm" property="modo" styleId="modo" value = ""/>
			<html:hidden name="confirmarFacturacionForm" property="fechaCargo" styleId="fechaCargo" value = ""/>
			<html:hidden name="confirmarFacturacionForm" property="facturacionRapida" styleId="facturacionRapida" value = ""/>
			<html:hidden name="confirmarFacturacionForm" property="generarEnvios" styleId="generarEnvios" value = ""/>
		</html:form>
		

			<!-- INICIO: LISTA DE VALORES -->
			<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->
				<siga:Table 
				   	name="tablaDatos"
				   	border="1"
				  	columnNames="facturacion.confirmarFacturacion.literal.conceptosFacturables, 
				  				general.description, 
				  				facturacion.programarFacturacion.literal.fechaInicioProductos,
				  				facturacion.programarFacturacion.literal.fechaInicioServicios, 
				  				facturacion.confirmarFacturacion.literal.fechaRealGeneracion,
				  				facturacion.confirmarFacturacion.literal.fechaConfirmacion,facturacion.estado,"
				  	columnSizes="20,21,9,9,8,8,12,15"
				    modal="M">
					<%if(vDatos == null || vDatos.size()<1 ) { %>
		 				<tr class="notFound">
			   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
						</tr>
					<%
						} else {
							
				 			Enumeration en = vDatos.elements();
				 			int i=0;
							
							while(en.hasMoreElements()){
								Row row = (Row)en.nextElement();
								Hashtable htData = row.getRow();
								if (htData == null) continue;
								fInicialProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS)));
								fFinalProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS)));
								fInicialServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS)));
								fFinalServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS)));
								fProgramacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION)));
								fPrevistaGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION)));
								fPrevistaConfirmacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM)));
								fRealConfirmacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHACONFIRMACION)));
								fRealGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAREALGENERACION)));
								fCargo =  UtilidadesString.mostrarDatoJSP((String)htData.get("FECHACARGO"));
								if(htEstados!=null){
									sEstadoConfirmacion =  UtilidadesString.mostrarDatoJSP(htEstados.get((String)htData.get("IDESTADOCONFIRMACION")));
								}else{
									sEstadoConfirmacion =  UtilidadesString.mostrarDatoJSP(admEstados.getDescripcion((String)htData.get("IDESTADOCONFIRMACION"), "C", usr.getLanguage()));
								}
								
								sCheckArchivado = (String)htData.get("ARCHIVARFACT");
								String idProgramacion = (String)htData.get("IDPROGRAMACION");
								Integer idEstadoConfirmacion = new Integer((String)htData.get("IDESTADOCONFIRMACION"));
								
								String logError = "0";
								if(htData.get("LOGERROR")!= null && !((String)htData.get("LOGERROR")).equals("")){
									logError=(String)htData.get("LOGERROR");
								}
								
								String logTraspasoFacturas = "0";
								if(htData.get("LOGTRASPASO")!= null && !((String)htData.get("LOGTRASPASO")).equals("")){
									logTraspasoFacturas = (String)htData.get("LOGTRASPASO");
								}
								
								String nombreFichero = "0";
								if(htData.get("NOMBREFICHERO")!= null && !((String)htData.get("NOMBREFICHERO")).equals("")){
									nombreFichero=(String)htData.get("NOMBREFICHERO");
								}
		
								FilaExtElement[] elems = new FilaExtElement[7];
								
								//TODOS LOS ESTADOS
								elems[0] = new FilaExtElement("consultar", "consultarfactura", SIGAConstants.ACCESS_READ);
								
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERACION_PROGRAMADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.ERROR_GENERACION) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.ERROR_CONFIRMACION)) {									
									elems[1]=new FilaExtElement("editar", "editarFacturacion", SIGAConstants.ACCESS_READ);
								}
								
								//ESTADO GENERADA
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERADA)) {
									elems[2]=new FilaExtElement("download", "descargarInformeGeneracion", SIGAConstants.ACCESS_READ);
								}
								
								//ESTADO DE ERROR CON LOG ERROR 
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.ERROR_GENERACION) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.ERROR_CONFIRMACION)) {
									elems[2]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ); 
									elems[3]=new FilaExtElement("eliminiarfact", "eliminiarfact", SIGAConstants.ACCESS_READ);
								}							
								
								//ESTADO CONFIRMACION PROGRAMADA (PUEDE TENER INFORME GENERACION Y LOGERROR DE CONFIRMACION)
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA)) {									
									elems[2]=new FilaExtElement("download", "descargarInformeGeneracion", SIGAConstants.ACCESS_READ);					
								}	
								
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERACION_PROGRAMADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.GENERADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA)) {									
									elems[4]=new FilaExtElement("eliminiarfact", "eliminiarfact", SIGAConstants.ACCESS_READ);
								}								
								
								// ESTADO CONFIRMADA
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA)) {
									elems[1] = new FilaExtElement("archivar", "archivar", SIGAConstants.ACCESS_READ);
									elems[2] = new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
									if((sCheckArchivado== null || !sCheckArchivado.equals("1")) && (htData.get("IDESTADOPDF")!= null && !((String)htData.get("IDESTADOPDF")).equals(String.valueOf(FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES.intValue())) )){
										elems[3] = new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_READ);
									}
									if(htData.get("IDESTADOTRASPASO")!= null && ((String)htData.get("IDESTADOTRASPASO")).equals(String.valueOf(FacEstadoConfirmFactBean.TRASPASO_FINALIZADAERRORES.intValue())) ){
										elems[6] = new FilaExtElement("traspasarFacturas", "traspasarFacturas", SIGAConstants.ACCESS_READ);
									}
								
									//TRASPASO FACTURAS: 
									if(htData.get("IDESTADOTRASPASO")!= null){
										if(((String)htData.get("IDESTADOTRASPASO")).equals(String.valueOf(FacEstadoConfirmFactBean.TRASPASO_FINALIZADAERRORES.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nErrores Traspaso");
											elems[5] = new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
										}else if(((String)htData.get("IDESTADOTRASPASO")).equals(String.valueOf(FacEstadoConfirmFactBean.TRASPASO_FINALIZADA.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nTraspaso Finalizado");
										}else if(((String)htData.get("IDESTADOTRASPASO")).equals(String.valueOf(FacEstadoConfirmFactBean.TRASPASO_PROCESANDO.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nGenerando Traspaso");
										}else if(((String)htData.get("IDESTADOTRASPASO")).equals(String.valueOf(FacEstadoConfirmFactBean.TRASPASO_PROGRAMADA.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nGenerando Traspaso");
										}
									}
									
									if(htData.get("IDESTADOPDF")!= null){ 
										if(((String)htData.get("IDESTADOPDF")).equals(String.valueOf(FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nErrores PDF");
											elems[5] = new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ); 
										}else if(((String)htData.get("IDESTADOPDF")).equals(String.valueOf(FacEstadoConfirmFactBean.PDF_FINALIZADA.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nPDF Finalizado");
										}else if(((String)htData.get("IDESTADOPDF")).equals(String.valueOf(FacEstadoConfirmFactBean.PDF_PROCESANDO.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nGenerando PDF");
										}else if(((String)htData.get("IDESTADOPDF")).equals(String.valueOf(FacEstadoConfirmFactBean.PDF_PROGRAMADA.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nGenerando PDF");
										}
									}
									
									if(htData.get("IDESTADOENVIO")!= null && (htData.get("IDESTADOPDF")!= null && !((String)htData.get("IDESTADOPDF")).equals(String.valueOf(FacEstadoConfirmFactBean.PDF_FINALIZADAERRORES.intValue())))){
										if(((String)htData.get("IDESTADOENVIO")).equals(String.valueOf(FacEstadoConfirmFactBean.ENVIO_FINALIZADAERRORES.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nErrores Envío");
											elems[5]=new FilaExtElement("descargaLog","descargaLog", SIGAConstants.ACCESS_READ);
										}else if(((String)htData.get("IDESTADOENVIO")).equals(String.valueOf(FacEstadoConfirmFactBean.ENVIO_FINALIZADA.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nEnvio Finalizado");
										}else if(((String)htData.get("IDESTADOENVIO")).equals(String.valueOf(FacEstadoConfirmFactBean.ENVIO_PROCESANDO.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nGenerando Envío");
										}else if(((String)htData.get("IDESTADOENVIO")).equals(String.valueOf(FacEstadoConfirmFactBean.ENVIO_PROGRAMADA.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nEnvío programado");
										}else if(((String)htData.get("IDESTADOENVIO")).equals(String.valueOf(FacEstadoConfirmFactBean.ENVIO_PENDIENTE.intValue()))){
											sEstadoConfirmacion +=  UtilidadesString.mostrarDatoJSP("\nEnvío pendiente");
										}
									}
								}
								
								i++;
								%>
								<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit">
									<td>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION)%>'>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_2' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDPROGRAMACION)%>'>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_3' name='oculto<%=String.valueOf(i)%>_3' value='<%=htData.get(FacFacturacionProgramadaBean.C_USUMODIFICACION)%>'>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_4' name='oculto<%=String.valueOf(i)%>_4' value='<%=fCargo%>'>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_5' name='oculto<%=String.valueOf(i)%>_5' value='<%=nombreFichero%>'>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_6' name='oculto<%=String.valueOf(i)%>_6' value='<%=logError%>'>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_7' name='oculto<%=String.valueOf(i)%>_7' value='<%=logTraspasoFacturas%>'>
										<%=UtilidadesString.mostrarDatoJSP(htData.get(FacSerieFacturacionBean.C_NOMBREABREVIADO))%> <%=(idProgramacion.equals("1"))?"":"["+idProgramacion+"]"%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(htData.get(FacFacturacionProgramadaBean.C_DESCRIPCION))%></td>
									<td><%=fInicialProducto%> -<br><%=fFinalProducto%></td>
									<td><%=fInicialServicio%> -<br><%=fFinalServicio%></td>
									<td><%=fRealGeneracion%></td>
									<td><%=fRealConfirmacion%></td>
									<td><%=sEstadoConfirmacion%></td>
								</siga:FilaConIconos>
						<%}
						} // While %>
	  		</siga:Table>

<siga:Paginador totalRegistros="${totalRegistros}"
	registrosPorPagina="${registrosPorPagina}"
	paginaSeleccionada="${paginaSeleccionada}" idioma="${usrBean.language}"
	modo="buscarPor" clase="paginator" 
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
	distanciaPaginas="" action="${pageContext.request.contextPath}${path}.do?noReset=true"
	 />

	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>

<!-- FIN: SUBMIT AREA -->

	</body>
</html>
