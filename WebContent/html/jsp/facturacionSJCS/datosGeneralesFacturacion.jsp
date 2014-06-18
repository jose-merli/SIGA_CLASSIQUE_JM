<!DOCTYPE html>
<html>
<head>
<!-- datosGeneralesFacturacion.jsp -->

<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES: 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 	prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.atos.utils.ClsConstants"%>
<%@ page import = "org.redabogacia.sigaservices.app.AppConstants.ESTADO_FACTURACION"%>
<%@ page import = "org.redabogacia.sigaservices.app.autogen.model.FcsFacturacionEstadoEnvio"%>
<%@ page import = "org.redabogacia.sigaservices.app.AppConstants.FCS_MAESTROESTADOS_ENVIO_FACT"%>
<%@ page import = "java.util.Properties"%>
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.ws.CajgConfiguracion"%>
<%@ page import = "com.siga.Utilidades.UtilidadesNumero"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	request.setAttribute("nuevoCriterio","si");
		
	String regularizacion = (String) request.getAttribute("FcsRegularizacion");
	boolean bRegularizacion = (regularizacion==null?false:true);
	
	Boolean yaHaSidoEjecutada = (Boolean) request.getAttribute("yaHaSidoEjecutada");
	boolean bYaHaSidoEjecutada = (yaHaSidoEjecutada!=null)?yaHaSidoEjecutada.booleanValue():false;
	boolean bBorrar = false;
	try {
		Boolean borrar = (Boolean)request.getAttribute("borrar");
		 bBorrar = (borrar!=null)?borrar.booleanValue():false;
	} catch(Exception e) {		
		 bBorrar = false;
	}

	// Para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("SJCSBusquedaFacturacionTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}

	// meto en session el indicador de que estamos NO en prevision
	//request.getSession().removeAttribute("prevision");

	// Campos de la factura a mostrar en la jsp
	String nombreInstitucion = "", nombre="", fechaInicio="", fechaFin="", importe="", idFacturacion="";
	String estado="", botones = "",botonesAbajo="V", fechaEstado="", destino="mainWorkArea";
	String idInstitucion="", modo="", nombreFacturacion="", strutTrans = "", prevision="";
	Integer idEstado = new Integer(0);

	//string que dirá cual es el modo en el que se envie el form
	String accion="Insertar";
	
	//para los tipos
	String clase = "box", desactivado="false";
	boolean consulta = false;
	boolean readonly = false;
	
	try {
		modo = (String)request.getAttribute("modo");
		idInstitucion = (String)request.getAttribute("idInstitucion");
		idFacturacion = (String)request.getAttribute("idFacturacion");
			
		nombreInstitucion = (String)request.getAttribute("nombreInstitucion");
		strutTrans = (String)request.getAttribute("strutTrans");
		prevision = ((String)request.getSession().getAttribute("prevision"));
		
		// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
		FcsFacturacionJGBean facturaBean = null;
		
		if (!modo.equalsIgnoreCase("nuevo")) {
			//Al estar en edicion o consulta:
			facturaBean = (FcsFacturacionJGBean)request.getSession().getAttribute("DATABACKUP");
			
			accion="Modificar";
			destino = "submitArea2";
			estado = (String)request.getAttribute("estado");
			idEstado = (Integer)request.getAttribute("idEstado");
			fechaEstado = GstDate.getFormatedDateShort("",(String)request.getAttribute("fechaEstado"));
			nombreInstitucion = (String)request.getAttribute("nombreInstitucion");
			if (facturaBean!=null){
				nombreFacturacion = (String)request.getAttribute("NOMBREFACTURACION");
				nombre = (String)facturaBean.getNombre();
				idFacturacion = ((Integer)facturaBean.getIdFacturacion()).toString();
				fechaInicio = GstDate.getFormatedDateShort("",(String)facturaBean.getFechaDesde());
				fechaFin = GstDate.getFormatedDateShort("",(String)facturaBean.getFechaHasta());
				importe = (String)request.getAttribute("importe");
				String parteEntera = importe.substring(0,importe.indexOf("."));
			}
		}	
		 
	} catch(Exception e){}
	
	//Para los modos de presentar los objetos de html
	if (bRegularizacion || modo.equalsIgnoreCase("consulta")) {
		consulta = true;
		desactivado = "true";
		readonly = true;
		clase = "boxConsulta";		
		botones ="GM";
	}
	
	if ((idEstado!=null) && (idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_LISTA_CONSEJO.getCodigo())) {
		consulta = true;
		desactivado = "true";
		readonly = true;
		clase = "boxConsulta";		
		botones ="GM";
		
		if (CajgConfiguracion.TIPO_CAJG_XML_SANTIAGO == CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion))) {
			//recuperamos el último estado de envío a la Xunta
			FCS_MAESTROESTADOS_ENVIO_FACT ultimoEstadoEnvio = (FCS_MAESTROESTADOS_ENVIO_FACT)request.getAttribute(FcsFacturacionEstadoEnvio.C_IDESTADOENVIOFACTURACION);
			if (ultimoEstadoEnvio != null) {
				if (FCS_MAESTROESTADOS_ENVIO_FACT.CERTIFICACION_VERIFICADA.equals(ultimoEstadoEnvio)) {
					botones += ",ER,EJ";//Envío Reintegro y Envío Justificación		
				} else if (FCS_MAESTROESTADOS_ENVIO_FACT.REINTEGRO_VERIFICADO.equals(ultimoEstadoEnvio)) {
					botones += ",EJ";//Envío Justificación
				}
			}
		}
		
	} else if ((idEstado!=null) && (
				idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_PROGRAMADA.getCodigo() || 
				idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_EN_EJECUCION.getCodigo())) {
		consulta = true;
		desactivado = "true";
		readonly = true;
		clase = "boxConsulta";		
		botones ="";
		
	} else if ((idEstado!=null) && (idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo())) {
		botones = "G,R,EF";
		
		if (!bRegularizacion) 
			botonesAbajo = "V,N"; 
		else 
			botonesAbajo = "V";
		
		if (bYaHaSidoEjecutada) botones += ",LF";
		
	} else if ((idEstado!=null) && (idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo())
			//|| idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_EN_PROCESO.getCodigo()
			|| idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_VALIDACION_NO_CORRECTA.getCodigo()
			|| idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_DISPONIBLE.getCodigo()
			|| idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_ACEPTADO.getCodigo()
			) {
		if (strutTrans.equalsIgnoreCase("FCS_MantenimientoPrevisiones")) {
			botones = "RF,GM"; //en las previsiones ejecutadas se permite reejecutar y descargar el informe
			
		} else {
			consulta = true;
			desactivado = "true";
			readonly = true;
			clase = "boxConsulta";
			
			//en las facturaciones ejecutadas se permite reejecutar y descargar el informe			
			if (CajgConfiguracion.TIPO_CAJG_XML_SANTIAGO == CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion))) {
				botones = "LC2,GM";
				
				FCS_MAESTROESTADOS_ENVIO_FACT ultimoEstadoEnvio = (FCS_MAESTROESTADOS_ENVIO_FACT)request.getAttribute(FcsFacturacionEstadoEnvio.C_IDESTADOENVIOFACTURACION);
				if (ultimoEstadoEnvio != null) {
					if (FCS_MAESTROESTADOS_ENVIO_FACT.CERTIFICACION_ENVIADA.equals(ultimoEstadoEnvio)) {
						botones = "LC2,";
					} else if (FCS_MAESTROESTADOS_ENVIO_FACT.CERTIFICACION_VERIFICADA.equals(ultimoEstadoEnvio) ||
							FCS_MAESTROESTADOS_ENVIO_FACT.REINTEGRO_ENVIADO.equals(ultimoEstadoEnvio)) {
						botones = "ER,EJ,";//Envío Reintegro y Envío Justificación		
					} else if (FCS_MAESTROESTADOS_ENVIO_FACT.REINTEGRO_VERIFICADO.equals(ultimoEstadoEnvio)) {
						botones = "EJ,";//Envío Justificación
					} else if (FCS_MAESTROESTADOS_ENVIO_FACT.JUSTIFICACION_ENVIADA.equals(ultimoEstadoEnvio)) {
						botones = "";//Envío Justificación
					}
					botones += "GM";
				}
				
				if (idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_VALIDACION_NO_CORRECTA.getCodigo()
						|| idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_ACEPTADO.getCodigo()) {				
					botonesAbajo = "V,II";
				}
				
			} else {
				botones = "LC,GM";
			}
			
			if(idEstado.intValue() == ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo() && bBorrar)
				botones += ",RF";
		}
		
	} else {
		botones ="G";
	}	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="DatosGeneralesFacturacionForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<% if(strutTrans.equalsIgnoreCase("FCS_MantenimientoPrevisiones")){  %>
		<siga:Titulo titulo="factSJCS.mantenimientoFacturacion.datosGenerales" localizacion="factSJCS.previsiones.ruta.localizacion"/>
	<% } else if(strutTrans.equalsIgnoreCase("CEN_MantenimientoFacturacion")) { %>
		<siga:Titulo titulo="factSJCS.mantenimientoFacturacion.datosGenerales" localizacion="factSJCS.mantenimientoFacturacion.localizacion"/>
	<% }  %>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			document.all.DatosGeneralesFacturacionForm.reset();
			<% if ((estado != null) && (!estado.equals(""))) { %>
				document.forms[0].estado.value = "<siga:Idioma key='<%=estado%>'/>";
			<% } %>
		}			
		
		// Asociada al boton GuardarCerrar
		function accionGuardar() {
			sub();
			if (validateDatosGeneralesFacturacionForm(document.DatosGeneralesFacturacionForm)){

				<% if(strutTrans.equalsIgnoreCase("CEN_MantenimientoFacturacion")) { %>
					if (compararFecha(document.forms[0].fechaFin, document.forms[0].fechaHoy) ==1) {
						alert('<siga:Idioma key="factSJCS.datosFacturacion.fechas.errorPosteriorActual"/>');
						fin();
						return false;
					}
				<% } %>
				
				if ((compararFecha(document.forms[0].fechaInicio,document.forms[0].fechaFin)==2)) {
						document.forms[0].modo.value = "<%=accion%>";
						document.forms[0].target = "submitArea2";
						document.forms[0].submit();
				} else {
					alert('<siga:Idioma key="gratuita.altaRetencionesIRPF.literal.alert1"/>');
					fin();
					return false;
				}
				
			} else {
				fin();
				return false;
			}
		}		
		
		function accionNuevo() {
			document.forms[0].modo.value = "nuevoCriterio";
            var resultado = showModalDialog("<%=app%>/html/jsp/facturacionSJCS/datosCriteriosFacturacion.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");                   
            //var resultado=ventaModalGeneral(document.forms[0].name,"P");
            if(resultado=='MODIFICADO'){
                   buscar();
            }
		}
		
		function buscar() {	
			document.forms[0].modo.value = "abrir";
			document.forms[0].target = "mainPestanas";
			document.forms[0].submit();
		}
		
		function refrescarLocal() {
			buscar();
		}
		
		function accionListaConsejo() {
			sub();
			document.forms[0].modo.value="listaConsejo";
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
		}
		
		function descargaLogFacturacion() {
			document.forms[0].modo.value="descargarLog";
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
		}
		
		function accionRecalcularFacturacion() {
			sub();
			<% if (regularizacion==null) { %>
				document.forms[0].modo.value="ejecutarFacturacion";
			<% } else { %>
				document.forms[0].modo.value="ejecutarRegularizacion";
			<% } %>
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
		}

		function accionEjecutaFacturacion() {
			sub();
			<% if (regularizacion==null) { %>
				document.forms[0].modo.value="ejecutarFacturacion";
			<% } else { %>
				document.forms[0].modo.value="ejecutarRegularizacion";
			<% } %>
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
		}

		// Funcion asociada a boton Generar Excels
		function accionGenerarExcels() {
			sub();
			var f = document.getElementById("DatosGeneralesFacturacionForm");
			idFactIni = document.getElementById("idFacturacion").value;			
			f.idFacturacionIniDownload.value = idFactIni;
			f.idFacturacionFinDownload.value = idFactIni;
			f.submit();
		}

		function accionGenerarInforme() {
			sub();
			document.forms[0].modo.value="descargaFicheroFact";
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
			fin();
			/*sub();
			var f = document.getElementById("InformesGenericosForm");
			idFactIni = document.getElementById("idFacturacion").value;
			idFactFin = document.getElementById("idFacturacion").value;
			f.datosInforme.value = "idFacturacionIni" + "==" + idFactIni + "##"
					+ "idFacturacionFin" + "==" + idFactFin;
			f.seleccionados.value = "1";
			f.submit();*/
		}

		function accionInformeIncidencias() {
			document.forms[0].modo.value="descargarInformeIncidencias";
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
		}
		
		function accionXuntaEnvioReintegro() {
			sub();
			document.forms[0].modo.value="accionXuntaEnvioReintegro";
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
		}
		
		function accionXuntaEnvioJustificacion() {
			sub();
			document.forms[0].modo.value="accionXuntaEnvioJustificacion";
			document.forms[0].target = "submitArea2";
			document.forms[0].submit();
		}
	</script>	
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="factSJCS.datosFacturacion.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
				<% if (bRegularizacion) { %> 
					- <%=nombreFacturacion%>
				<% } %>
			</td>
		</tr>
	</table>
	
	<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="submitArea2">
		<html:hidden property="modo" value=""/>
		<html:hidden property ="actionModal" value = ""/>
		<html:hidden property ="idFacturacion" value = "<%=idFacturacion%>"/>
		<html:hidden property ="idInstitucion" value = "<%=idInstitucion%>"/>
		<html:hidden property ="fechaHoy"	   value = '<%=UtilidadesBDAdm.getFechaBD("")%>'/>
		
		<siga:ConjCampos leyenda="factSJCS.datosGenerales.cabecera">				
			<table class="tablaCampos" align="center" >	
				<tr>		
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosFacturacion.literal.nombre"/>&nbsp(*)
					</td>
					<td colspan="3">
						<html:text name="DatosGeneralesFacturacionForm" property="nombre" value='<%=nombre%>' size="60" maxlength="100" styleClass="<%=clase%>" readOnly="<%=readonly%>" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosFacturacion.literal.fechaInicio"/>&nbsp(*)
					</td>
					<td>
						<siga:Fecha nombreCampo="fechaInicio" valorInicial="<%=fechaInicio%>"></siga:Fecha>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosFacturacion.literal.fechaFin"/>&nbsp(*)
					</td>	
					<td>								
						<siga:Fecha nombreCampo="fechaFin" valorInicial="<%=fechaFin%>"></siga:Fecha>
					</td>	
				</tr>
				
				<tr>						
					<td class="labelText" >
						<siga:Idioma key="factSJCS.datosFacturacion.literal.estado"/>&nbsp;
					</td>				
					<td>
						<html:text name="DatosGeneralesFacturacionForm" property="estado" value="" maxlength="50" size="50" styleClass="boxConsulta" readOnly="true" />
					</td>
					
					<td class="labelText">
						<siga:Idioma key="factSJCS.datosFacturacion.literal.fechaEstado"/>&nbsp;
					</td>				
					<td>
						<html:text name="DatosGeneralesFacturacionForm" property="fechaEstado" value='<%=fechaEstado%>' maxlength="10" size="10" styleClass="boxConsulta" readOnly="true" />
					</td>
				</tr>
			</table>
		</siga:ConjCampos>				
	</html:form>
	
	<siga:ConjBotonesAccion clase="botonesSeguido" botones='<%=botones%>' modo='<%=modo%>'/>	
			
	<!-- PARA LA FUNCION VOLVER -->
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
<% 
	Vector obj = (Vector)request.getAttribute("vHito");
	
	boolean bPrevision = false;
	if (prevision!=null && prevision.equals("S")) {
		bPrevision = true;
		request.getSession().setAttribute("prevision","S");
	} else {
		prevision = (String) request.getSession().getAttribute("prevision");
		if (prevision!=null && prevision.equals("S")) {
			bPrevision = true;
		}
	}
		
	botones = "";
	if (idEstado < 20) botones = "B";
	
	if ((regularizacion != null) && (regularizacion.equalsIgnoreCase("true"))) botones = "";
%>	
	<!-- INICIO TABLA -->	
	<siga:Table 
		name="tablaDatos"
	   	border="1"
		columnNames="factSJCS.datosFacturacion.literal.gruposFacturacion,factSJCS.datosFacturacion.literal.hitos,"
		columnSizes="35,45,10"
		fixedHeight="50%"
		modal="P">
		
<% 
		if (obj==null || obj.size()==0) {
%>
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		} else { 
	    	int recordNumber=1;
			while ((recordNumber) <= obj.size()) {	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
%>	
			  	<siga:FilaConIconos visibleEdicion='no' visibleConsulta='no' fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" modo="<%=modo%>">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idFacturacion%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDGRUPOFACTURACION")%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("IDHITOGENERAL")%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idInstitucion%>'><%=UtilidadesString.mostrarDatoJSP((String)hash.get("NOMBRE"))%>
					</td>
					<td><siga:Idioma key='<%=(String)hash.get("DESCRIPCION")%>'/></td>
				</siga:FilaConIconos>	
<%
				recordNumber++;
			}
		}
%>
	</siga:Table>

	<!-- FIN TABLA -->
	<iframe align="center" src="<%=app%>/html/jsp/facturacionSJCS/consultaDetallesCriteriosFacturacion.jsp?idInstitucion=<%=idInstitucion%>&idFacturacion=<%=idFacturacion%>&modo=<%=modo%>&regularizacion=<%=bRegularizacion%>"
		id="resultado10"
		name="resultado10" 
		scrolling="no"
		frameborder="0"
		marginheight="0"
		marginwidth="0"		
		style="height:100%;width:100%;"></iframe>
	
	<siga:ConjBotonesAccion botones='<%=botonesAbajo%>' modo='<%=modo%>'/>

	<script>
		<% if ((estado != null) && (!estado.equals(""))) { %>
			document.forms[0].estado.value = "<siga:Idioma key='<%=estado%>'/>";
		<% } %>	
	</script>
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea2"  src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>