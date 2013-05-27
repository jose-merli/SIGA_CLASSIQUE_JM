<!-- contabilidad.jsp -->

<!-- VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
     Utilizando tags pinta una lista con cabeceras fijas -->
	 
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
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	FacSerieFacturacionBean serie = (FacSerieFacturacionBean)request.getAttribute("serieFacturacion");
	String cuentaClienteConfiguracion = "", cuentaIngresosConfiguracion = "", cuentaClienteGenerica = "", cuentaIngresosGenerica = "";
	if (serie != null) {
		cuentaClienteConfiguracion  = serie.getConfigDeudor();
		cuentaIngresosConfiguracion = serie.getConfigIngresos();
		cuentaClienteGenerica       = serie.getCuentaClientes();
		cuentaIngresosGenerica      = serie.getCuentaIngresos();
	}
	
	String botones = "V";
	String editable = (String)ses.getAttribute("editable");
	if (editable!=null && !editable.equals("")) {
		if (editable.equals("1")) {
			botones += ",G";
		}
	}	

%>

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="ComprobarPoblacionForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="facturacion.asignacionConceptos.contabilidad.cabecera" 
			localizacion="facturacion.asignacionConceptos.contabilidad.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
	</head>

	<body class="tablaCentralCampos">

		<br>

		<html:form action="/FAC_ContabilidadPestana.do" method="POST" target="submitArea">
			<html:hidden property="modo" value=""/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
			

		<table border="0" align="center" width="90%" class="labelText">
			<tr>
				<td width="45%" valign="top" class="labelText"">
					<siga:ConjCampos leyenda="facturacion.asignacionConceptos.contabilidad.cuentaClientes.literal">

					<table border="0" align="center" width="90%" class="labelText">
					<tr>
					<td>
						<siga:ConjCampos leyenda="facturacion.asignacionConceptos.contabilidad.configuracion.literal">
							<input type="radio" name="cuentaClienteConfiguracion" value="<%=ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO%>" 
								<% if(cuentaClienteConfiguracion.equals(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO))out.print("checked"); %>		>
							<siga:Idioma key="facturacion.asignacionConceptos.contabilidad.fijo.literal"/>
							<br>
							<input type="radio" name="cuentaClienteConfiguracion" value="<%=ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_CLIENTE%>"	
								<% if(cuentaClienteConfiguracion.equals(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_CLIENTE))out.print("checked"); %>		>
							<siga:Idioma key="facturacion.asignacionConceptos.contabilidad.anadirSubcuentaCliente.literal"/>
						</siga:ConjCampos>
						
						<br>
						<br>
						<siga:ConjCampos leyenda="facturacion.asignacionConceptos.contabilidad.cuentaGenerica.literal">
							<center>
								<input type="text" name="cuentaClienteGenerica" value="<%=cuentaClienteGenerica%>" class="box" size="20" maxLength="10">
							</center>
						</siga:ConjCampos>
						<br>
					</td>
					</tr>
					</table>

					</siga:ConjCampos>
				</td>

				
				<td width="10%"> &nbsp;</td>
				<td width="45%" valign="top" class="labelText">
					<siga:ConjCampos leyenda="facturacion.asignacionConceptos.contabilidad.cuentaIngresos.literal">

					<table border="0" align="center" width="90%" class="labelText">
					<tr>
					<td>
						<siga:ConjCampos leyenda="facturacion.asignacionConceptos.contabilidad.configuracion.literal">
							<input type="radio" name="cuentaIngresosConfiguracion" value="<%=ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO%>"
								<% if(cuentaIngresosConfiguracion.equals(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_FIJO))out.print("checked"); %>		>
							<siga:Idioma key="facturacion.asignacionConceptos.contabilidad.fijo.literal"/>
							<br>
							<input type="radio" name="cuentaIngresosConfiguracion" value="<%=ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_CLIENTE%>"
								<% if(cuentaIngresosConfiguracion.equals(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_CLIENTE))out.print("checked"); %>		>
							<siga:Idioma key="facturacion.asignacionConceptos.contabilidad.anadirSubcuentaCliente.literal"/>
							<br>
							<input type="radio" name="cuentaIngresosConfiguracion" value="<%=ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_PYS%>"
								<% if(cuentaIngresosConfiguracion.equals(ClsConstants.ASIGNACION_CONCEPTOS_CONTABILIDAD_CONFIGURACION_INCORPORAR_SUBCUENTA_PYS))out.print("checked"); %>		>
							<siga:Idioma key="facturacion.asignacionConceptos.contabilidad.anadirSubcuentaPyS.literal"/>
						</siga:ConjCampos>
						
						<br>
						<siga:ConjCampos leyenda="facturacion.asignacionConceptos.contabilidad.cuentaGenerica.literal">
							<center>
								<input type="text" name="cuentaIngresosGenerica" value="<%=cuentaIngresosGenerica%>" class="box" size="20" maxLength="10">
							</center>
						</siga:ConjCampos>
						<br>
					</td>
					</tr>
					</table>
						
					</siga:ConjCampos>
				</td>
			</tr>
		</table>

		</html:form>
			
		<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle" />

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				document.forms[0].action = "<%=app%>/FAC_AsignacionConceptosFacturables.do";	
				document.forms[0].target = "mainWorkArea";
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].submit();	
			}

			function accionGuardar() 
			{
				sub();		
				document.forms[0].modo.value = "modificar";
				document.forms[0].submit();	
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
