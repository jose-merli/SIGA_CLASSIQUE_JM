<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
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
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CerSolicitudCertificadosBean"%>
<%@ page import="com.siga.beans.PysProductosInstitucionAdm"%>
<%@ page import="com.siga.beans.EnvEnviosBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String idInstitucion[] = {user.getLocation()};	

	String fecha = UtilidadesString.getMensajeIdioma(user,"envios.definir.literal.fechaprogramada");
	String obligatorio = UtilidadesString.getMensajeIdioma(user,"messages.campoObligatorio.error");
	
	String envioSms = (String)request.getAttribute("smsHabilitado");
	String consultaPlantillas="";
	if (envioSms!=null && envioSms.equalsIgnoreCase("S")){
		consultaPlantillas = "cmbTipoEnviosInstSms";
	}else{
		consultaPlantillas = "cmbTipoEnviosInst";
	}
	
	
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="DefinirEnviosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="envios.certificados.literal.confEnvioCertificado"/>
				</td>
			</tr>
		</table>

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/ENV_DefinirEnvios.do" method="POST" target="submitArea">
		<html:hidden property = "hiddenFrame" value = "1"/>
		<html:hidden property = "modo" value = "insertarEnvioModalCertificado"/>
		<html:hidden property = "subModo" value=""/>
		<html:hidden property = "idPersona" value=""/>
		<html:hidden property = "idFactura" value=""/>
		<html:hidden property = "idSolicitud" value = ""/>
		<html:hidden property = "idEnvio" value = ""/>
		<html:hidden property = "nombre" value = "kk"/>

		<html:hidden property = "idEnvioBuscar" value=""/>

		<html:hidden property = "idsParaEnviar"/>

	<tr>				
	<td>
	<siga:ConjCampos leyenda="envios.certificados.literal.destinatario">
		<table class="tablaCampos" align="center" border="0">
			<tr>	
				<td class="labelText" width="180">
					<siga:Idioma key="envios.definir.literal.institucionOrigen"/>
				</td>
				<td align="left">
						<input type="radio" name="colegio" value="o" checked>
				</td>		
			</tr>	
			<tr>	
				<td class="labelText">
						<siga:Idioma key="envios.definir.literal.institucionDestino"/>
				</td>
				<td align="left">
						<input type="radio" name="colegio" value="d">
				</td>				
			</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="envios.certificados.literal.confEnvio">
		<table class="tablaCampos" align="center" border="0">
			<tr>	
				<td class="labelText">
						<siga:Idioma key="envios.definir.literal.tipoenvio"/>&nbsp;(*)
				</td>
				<td>
						<siga:ComboBD nombre = "comboTipoEnvio" tipo="<%=consultaPlantillas%>" clase="boxCombo" obligatorio="true" parametro="<%=idInstitucion%>"  accion="Hijo:comboPlantillaEnvio"/>
				</td>				
			</tr>
			
			<tr>	
				<td class="labelText">
						<siga:Idioma key="envios.plantillas.literal.plantilla"/>&nbsp;(*)
				</td>
				<td>
						<siga:ComboBD nombre = "comboPlantillaEnvio" tipo="cmbPlantillaEnvios2" clase="boxCombo" obligatorio="true" hijo="t" accion="Hijo:idPlantillaGeneracion"/>
				</td>				
			</tr>
			
			<tr>
				<td class="labelText">
					<siga:Idioma key="envios.definir.literal.plantillageneracion"/>
				</td>
				<td>
					<siga:ComboBD nombre="idPlantillaGeneracion" tipo="cmbPlantillaGeneracion" clase="boxCombo" obligatorio="false" hijo="t" pestana="true"/>
				</td>				
			</tr>

			<tr>	
				<td class="labelText">
					<siga:Idioma key="envios.definir.literal.fechaprogramada"/>
				</td>
				<td>
					<html:text name="DefinirEnviosForm" property="fechaProgramada" size="10" maxlength="10" styleClass="box" readonly="true"/>					
					<a href='javascript://' onClick="return showCalendarGeneral(fechaProgramada);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	</td>
	</tr>

	</html:form>
	
	</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />


	<!-- INICIO: SCRIPTS BOTONES -->
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			sub();	
			if (validateDefinirEnviosForm(document.DefinirEnviosForm)){
				var insTipoEnvio = document.forms[0].comboTipoEnvio.value;
				var opcion_array=insTipoEnvio.split(",");
				var f = document.DefinirEnviosForm.name;
				document.frames.submitArea.location = '<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
			} else {
				fin();
			}
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.close();
		}
	

	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>