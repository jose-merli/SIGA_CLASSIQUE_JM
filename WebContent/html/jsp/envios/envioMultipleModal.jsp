

<!-- envioMultipleModal.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CerSolicitudCertificadosBean"%>
<%@ page import="com.siga.beans.EnvEnviosBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	String idInstitucion[] = { user.getLocation() };

	String idSolicitud = (String) request
			.getAttribute(CerSolicitudCertificadosBean.C_IDSOLICITUD);
	String idPersona = (String) request
			.getAttribute(CerSolicitudCertificadosBean.C_IDPERSONA_DES);
	String idFactura = (String) request
			.getAttribute(FacFacturaBean.C_IDFACTURA);
	String idEnvio = (String) request
			.getAttribute(EnvEnviosBean.C_IDENVIO);
	String subModo = (String) request.getAttribute("subModo");
	String datosEnvios = (String) request.getAttribute("datosEnvios");
	String clavesIteracion = (String) request
			.getAttribute("clavesIteracion");
	String envioSms = (String) request.getAttribute("smsHabilitado");
	String Datos = (String) request.getAttribute("exitenDatos");
	String noExistDatos = "";
	if (subModo != null && subModo.equals("LIGUA") && Datos != "") {
		noExistDatos = "noExisteDatos('" + Datos + "');";
	} else {
		noExistDatos = "";
	}

	ArrayList comboTipoEnvio = (ArrayList) request
			.getAttribute("comboTipoEnvio");
	String recargarCombos = "";
	if (comboTipoEnvio != null) {
		recargarCombos = "recargarCombos()";
	} else {
		comboTipoEnvio = new ArrayList();
	}
	String sComDil = (String) request.getAttribute("ComDil");
	boolean bComDil = (sComDil != null) && (sComDil.equals("true"));
	String fecha = UtilidadesString.getMensajeIdioma(user,
			"envios.definir.literal.fechaprogramada");
	//ATENCION Nueva funcionalidad añadida. Si trae el atributo isEditarEnvio 
	//se vera si se quiere que el check de editar envio esta habilitado o no. 
	//Si no viene este atributo lo dejamos como estaba antes de meterlo, esto es habilitado
	Boolean editarEnvio = (Boolean) request
			.getAttribute("isEditarEnvio");
	boolean isEditarEnvio = editarEnvio == null
			|| editarEnvio.booleanValue();

	Boolean descargar = (Boolean) request.getAttribute("isDescargar");
	boolean isDescargar = descargar != null && descargar.booleanValue();

	String consultaPlantillas = "";
	if (envioSms != null && envioSms.equalsIgnoreCase("S")) {
		consultaPlantillas = "cmbTipoEnviosInstSms";
	} else {
		consultaPlantillas = "cmbTipoEnviosInst";
	}

	String obligatorio = UtilidadesString.getMensajeIdioma(user,
			"messages.campoObligatorio.error");
%>	
<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<html>
	<style>
		.ocultar {display:none}
	</style>	
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

<body onload="inicio();<%=noExistDatos%>">

<bean:define id="plantillasList" name="plantillasList" type="java.util.Collection" scope="request" />


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
				   <siga:Idioma key="envios.certificados.literal.confEnvio"/>
				</td>
			</tr>
		</table>

<div id="camposRegistro" class="posicionModalPeque" align="center">
<html:form action="/ENV_DefinirEnvios.do" method="POST" target="submitArea">
		<html:hidden property = "hiddenFrame" value = "1"/>
		<html:hidden property = "modo" value = "insertarEnvioModal"/>
		<html:hidden property = "datosEnvios" value="<%=datosEnvios%>"/>
		<html:hidden property = "subModo" value="<%=subModo%>"/>
		<html:hidden property = "idPersona" value="<%=idPersona%>"/>
		<html:hidden property = "idFactura" value="<%=idFactura%>"/>
		<html:hidden property = "idSolicitud" value = "<%=idSolicitud%>"/>
		<html:hidden property = "idEnvio" value = "<%=idEnvio%>"/>
		<html:hidden property = "idEnvioBuscar" value=""/>
		<html:hidden property = "descargar"/>
		<html:hidden property="clavesIteracion" value="<%=clavesIteracion%>"/>
		<html:hidden property = "idTipoEnvio"/>
		<html:hidden property = "acuseRecibo" value="0"/>

	<table class="tablaCampos" border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		<c:choose >
			
   			<c:when test="${empty plantillasList}">
   				<tr>
   					<td width='20%'></td>
   					<td width='20%'></td>
					<td width='10%'></td>
					<td width='10%'></td>
					<td width='15%'></td>
					<td width='12%'></td>
					<td width='13%'></td>
				</tr>
   				
				<tr class ='titulitos' id="noRecordFound">
					<td class="titulitos" style="text-align:center" colspan = "7">
						<siga:Idioma key="messages.noRecordFound"/>
	   		 		
	   		 		</td>
	 			</tr>
   			</c:when>
   			<c:otherwise>
		<tr>
			<td width="30%"></td>
			<td width="20%"></td>
			<td width="20%"></td>
			<td width="20%"></td>
			<td width="10%"></td>
		</tr>
		<tr>
			<td class="labelText" style="vertical-align: middle;">Informe</td>
			<td class="labelText" style="vertical-align: center">Tipo de envío</td>
			<td class="labelText" style="vertical-align: center">Tipo de plantilla</td>
			<td class="labelText" style="vertical-align: center">Plantilla</td>
			<td class="labelText" style="vertical-align: center">Acuse</td>
		</tr>
		
		<c:forEach items="${plantillasList}"  var="plantilla"  varStatus="status">
		<bean:define id="idTipoEnvio" name="plantilla" property="idTipoEnvio" type="java.lang.String"/>
			<bean:define id="idPlantillaEnvio" name="plantilla" property="idPlantillaEnvio" type="java.lang.String"/>
			<bean:define id="idInstitucionPlantillaEnvio" name="plantilla" property="idInstitucion" type="java.lang.String"/>
		<tr>
			<td class="labelTextValor">
  				<c:out value="${plantilla.descripcion}"/>
			</td>
			<td class="labelText" width="137">
			
			<%
			ArrayList alIdsTipoEnvio = new ArrayList();
			alIdsTipoEnvio.add(idInstitucionPlantillaEnvio+","+idTipoEnvio);
			%>
			
			
				<siga:ComboBD nombre = "comboTipoEnvio${status.count}" tipo="<%=consultaPlantillas %>" clase="boxCombo" obligatorio="false"
									parametro="<%=idInstitucion%>"
									elementoSel="<%=alIdsTipoEnvio%>"
									accion="Hijo:comboPlantillaEnvio${status.count}"
									 />
				</td>
			<td class="labelText" width="150">
			<%
			ArrayList alIdsPlantillaEnvio = new ArrayList();
			alIdsPlantillaEnvio.add(idPlantillaEnvio+","+idInstitucionPlantillaEnvio+","+idTipoEnvio);
			 %>
			<siga:ComboBD	nombre="comboPlantillaEnvio${status.count}" tipo="cmbPlantillaEnvios3"
									clase="boxCombo" obligatorio="FALSE" hijo="t"
									elementoSel="<%=alIdsPlantillaEnvio%>"
									accion="Hijo:idPlantillaGeneracion${status.count}" />
			
			
			</td>
			<td width="139">
			<siga:ComboBD	nombre="idPlantillaGeneracion${status.count}" tipo="cmbPlantillaGeneracion"
									clase="boxCombo" obligatorio="false" hijo="t"  />
			</td>
			<td class="labelText" width="22"><input type="checkbox"
				id="idCheckAcuseRecibo" disabled="disabled" value="ON">
			</td>

		</tr>
		
		</c:forEach>
		
		
		<tr>
			<td width="30%">&nbsp;</td>
			<td width="20%">&nbsp;</td>
			<td width="20%">&nbsp;</td>
			<td width="20%">&nbsp;</td>
			<td width="10%">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="5">
				<table>
					<tr>
						<td width="87" class="labelText">Comunicacion</td>
						<td class="labelTextValor"><html:text name="DefinirEnviosForm" property="nombre" size="50" maxlength="100" styleClass="box"></html:text></td>
						
						<td class="labelText"><siga:Idioma
								key="envios.definir.literal.editarenvio" />
						</td>
						<td>
						<%
 	if (isEditarEnvio) {
 %>
									<html:checkbox name="DefinirEnviosForm" property="editarEnvio"
										value="true" /> <%
 	} else {
 %> <html:checkbox
										name="DefinirEnviosForm" property="editarEnvio" value="true"
										disabled="true" /> <%
 	}
 %>
							
							</td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
								key="envios.definir.literal.fechaprogramada" />
								</td><td colspan="3"><html:text name="DefinirEnviosForm"
										property="fechaProgramada" size="10" maxlength="10"
										styleClass="box" readonly="true" /> <a href='javascript://'
									onClick="return showCalendarGeneral(fechaProgramada);"><img
										src="<%=app%>/html/imagenes/calendar.gif" border="0">
								</a>
						</td>
					</tr>


				</table></td>


		</tr>
		
		
		
				
				
				
				
			</c:otherwise>
	</c:choose>

</table>
</html:form>

	

<html:form action="/INF_InformesGenericos.do" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<html:hidden property="idInstitucion"/>
	<html:hidden property="idInforme"/> 				
	<html:hidden property = "idTipoInforme" value="<%=subModo%>"/>
	<html:hidden property = "datosInforme" />
	<html:hidden property="clavesIteracion"/>
	<html:hidden property="seleccionados" value="1"/>	
</html:form>

	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO botonesDetalle 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->
<%
	if (isDescargar) {
%>									
		<siga:ConjBotonesAccion botones="D,Y,C" modal="P" />
<%
	} else {
%>
	<siga:ConjBotonesAccion botones="Y,C" modal="P" />
<%
	}
%>

		
</div>

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
				document.DefinirEnviosForm.idTipoEnvio.value = opcion_array[1]; 
				if(document.getElementById("idCheckAcuseRecibo").checked)
					document.DefinirEnviosForm.acuseRecibo.value = "1";
				else
					document.DefinirEnviosForm.acuseRecibo.value = "0";
				
				DefinirEnviosForm.submit();
			} else {
				fin();
			}
		}
		function accionDownload() 
		{		
		    sub();
			document.InformesGenericosForm.datosInforme.value=document.DefinirEnviosForm.datosEnvios.value;
			document.InformesGenericosForm.submit();
		
			
			
		}
		

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{			
			window.close();
		}
		
		function noExisteDatos(mensaje) 
		{			
 			alert('<siga:Idioma key="messages.general.error.noExistenDatos"/>');
			window.close();
		}
		
		function recargarCombos() 
		{		
			var cmbTipoEnvio = document.getElementsByName("comboTipoEnvio")[0];
			cmbTipoEnvio.onchange();
		}
		function inicio() {
			if(document.DefinirEnviosForm.idTipoEnvio.value=="4" ||document.DefinirEnviosForm.idTipoEnvio.value=="5"){
				 document.getElementById("comboTipoEnvio").disabled="disabled";
				 
				 
			}
			
		}
		function onChangePlantillaEnvio(){
			var cmbPlantillaEnvio = document.getElementsByName("comboPlantillaEnvio")[0];
			var opcionArray=cmbPlantillaEnvio.value.split(",");
			var idTipoEnvio = opcionArray[2]; 
			if(idTipoEnvio=='1'){
				var acuseRecibo = opcionArray[3];
				if(acuseRecibo=='1')
					document.getElementById("idCheckAcuseRecibo").checked = "checked";
				else
					document.getElementById("idCheckAcuseRecibo").checked = "";
					
			}else{
				//document.getElementById("tdAcuseRecibo").style.display="none";
				document.getElementById("idCheckAcuseRecibo").checked = "";
			}
			
				
			
		}
		function onChangeTipoEnvio(){
			
			var cmbTipoEnvio = document.getElementsByName("comboTipoEnvio")[0];
			var opcionArray=cmbTipoEnvio.value.split(",");
			idTipoEnvio = opcionArray[1];
			
			
			//alert("i "+idTipoEnvio);
			if(idTipoEnvio!='1'){
				document.getElementById("idCheckAcuseRecibo").disabled="disabled";
					
		      	
				
			}else{
				document.getElementById("idCheckAcuseRecibo").disabled="";
			}
			
		} 

		
		recargarCombos();
		
	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
