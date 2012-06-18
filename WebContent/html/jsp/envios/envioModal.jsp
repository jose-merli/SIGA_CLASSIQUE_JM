<!-- envioModal.jsp -->
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
	
		
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="DefinirEnviosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

</head>

<body onload="recargarCombos();inicio();<%=noExistDatos%>">

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

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

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
		
		
			
	<tr>				
	<td>

		<table class="tablaCampos" align="center" border="0">
		<tr>	
				<td width="25%"></td>
				<td width="40%"></td>
				<td width="30%"></td>
				<td width="10%"></td>
				
						
		</tr>	
			<tr>	
				<td class="labelText">
					<siga:Idioma key="envios.definir.literal.nombre"/>&nbsp;(*)
				</td>
				<td class="labelText" colspan="3"> 
					<html:text name="DefinirEnviosForm" property="nombre" size="50" maxlength="100" styleClass="box"></html:text>
				</td>		
			</tr>	
			
			<tr>	
				<td class="labelText">
						<siga:Idioma key="envios.definir.literal.tipoenvio"/>&nbsp;(*)
				</td>
				<td class="labelText" colspan="3">
						<siga:ComboBD nombre = "comboTipoEnvio" tipo="<%=consultaPlantillas %>" clase="boxCombo" obligatorio="true" parametro="<%=idInstitucion%>" elementoSel="<%=comboTipoEnvio%>" accion="Hijo:comboPlantillaEnvio;onChangeTipoEnvio()"/>
				</td>				
			</tr>
			<c:choose>
			
				<c:when test="${DefinirEnviosForm.idTipoEnvio!='4'&&DefinirEnviosForm.idTipoEnvio!='5'}">
									<tr>
										<td class="labelText"><siga:Idioma
												key="envios.plantillas.literal.plantilla" />&nbsp;(*)</td>
										<td class="labelText"><siga:ComboBD
												nombre="comboPlantillaEnvio" tipo="cmbPlantillaEnvios2"
												clase="boxCombo" obligatorio="true" hijo="t"
												accion="Hijo:idPlantillaGeneracion;parent.onChangePlantillaEnvio()" />
										</td>
										<td  class="labelText"  ><siga:Idioma
												key="envios.definir.literal.acuseRecibo" />
										</td>
										<td> <input
											type="checkbox" id="idCheckAcuseRecibo" disabled="disabled"></td>


									</tr>

									<tr>
					<td class="labelText" >
						<siga:Idioma key="envios.definir.literal.plantillageneracion"/>
					</td>
					<td class="labelText" colspan="3">
						<siga:ComboBD nombre="idPlantillaGeneracion" tipo="cmbPlantillaGeneracion" clase="boxCombo" obligatorio="false" hijo="t" pestana="true"/>
							
					</td>				
				</tr>
			</c:when>
			<c:otherwise>
				<input	type="hidden" id="idCheckAcuseRecibo">
				<html:hidden property="comboPlantillaEnvio" value="predefinida"/>
				</c:otherwise>
			</c:choose>
							<tr>
								<td class="labelText" colspan="2">
								<siga:Idioma
										key="envios.definir.literal.fechaprogramada" />
									&nbsp;&nbsp;&nbsp;&nbsp;
								<% String hoy = UtilidadesBDAdm.getFechaBD(""); %>	
								<siga:Fecha nombreCampo="fechaProgramada" posicionX="10" posicionY="10" valorInicial="<%= hoy %>"></siga:Fecha>
								</td>
								<td class="labelText"><siga:Idioma
										key="envios.definir.literal.editarenvio" /></td>
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
						</table>

	</td>
	</tr>

	</html:form>
	
	</table>

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
			window.top.close();
		}
		
		function noExisteDatos(mensaje) 
		{			
 			alert('<siga:Idioma key="messages.general.error.noExistenDatos"/>');
			window.top.close();
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

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>