<!DOCTYPE html>
<html>
<head>
<!-- listadoTelefonosJG.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsTelefonosPersonaJGAdm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.gratuita.form.DefinirTelefonosJGForm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");

	//Accion de la que venimos:
	DefinirTelefonosJGForm formulario = (DefinirTelefonosJGForm) request.getAttribute("DefinirTelefonosJGForm");
	String accion = formulario.getAccion();

	//Calculo los telefonos si la accion no es nuevo:
	Vector vTelefonosJG = (Vector) request.getAttribute("VTELEFONOS");
	
	String columnNames = "gratuita.operarDatosBeneficiario.literal.telefonoUso," +
						 "gratuita.operarDatosBeneficiario.literal.numeroTelefono," +
						 "censo.preferente.sms," +
						 "<input type='button'" + 
							" id='idInsertarTelefonos'" + 
							" class='button'" + 
							" name='idButtoninsertar'" +
							" value='" + UtilidadesString.getMensajeIdioma(usr, "general.boton.insertar") + "'" + 
							" alt='" + UtilidadesString.getMensajeIdioma(usr, "general.boton.insertar") + "'" + 
							" onclick='accioninsertar();'>";
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>
	<!-- CAMPOS DEL REGISTRO -->
	<html:form action="/JGR_TelefonosPersonasJG" method="POST" target="submitArea" style="display:none">
		<html:hidden property="modo" value="" />
		<html:hidden property="idPersona" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="accion" />
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>
	
<%
	int contador = 0;
	if (!accion.equalsIgnoreCase("ver")) {
%>
	
		<siga:Table
			name="tablaTelefonos"
			columnSizes="30,30,10,30"
			columnNames="<%=columnNames%>">
	
			<logic:notEmpty name="DefinirTelefonosJGForm" property="telefonos">
				<logic:iterate name="DefinirTelefonosJGForm" property="telefonos" id="telefono" indexId="index">
					<bean:define id="telefonosJGForm" name="telefono" property="definirTelefonosJGForm"></bean:define>
					<bean:define id="preferenteSms" name="telefono" property="preferenteSms"></bean:define>					
					
					<tr id='fila_<bean:write name="index"/>' class='<%=((++contador + 1) % 2 == 0 ? "filaTablaPar" : "filaTablaImpar")%>'>
						<td>
							<input type='text'
								id='nombreTelefonoJG_<bean:write name="index"/>' 
								class='box'
								maxLength='20' 
								style='width:110px'
								value='<bean:write name="telefonosJGForm" property="nombreTelefonoJG" />' />
						</td>
						
						<td>
							<input type='text'
								id='numeroTelefonoJG_<bean:write name="index"/>' 
								class='box'
								maxLength='20' 
								style='width:110px'
								value='<bean:write name="telefonosJGForm" property="numeroTelefonoJG" />' />
						</td>
																
						<td>
							<input type='checkbox'
								id='preferenteSms_<bean:write name="index"/>'
								name='preferenteSms_<bean:write name="index"/>'
								class='preferenteSms'
								value='<bean:write name="telefonosJGForm" property="preferenteSms" />'
								onClick='checkSms(<bean:write name="index"/>)'
								<%=(preferenteSms.equals("1")) ? "checked" : ""%> />
						</td>
						
						<td>
							<img src='/SIGA/html/imagenes/bborrar_off.gif'
								style='cursor:hand'
								alt='<siga:Idioma key="general.borrar"/>' 
								name='' 
								border='0'
								onclick='borrarFila(<bean:write name="index"/>)' />
						</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</siga:Table>					
					
<%
	} else {
%>
	
		<siga:Table
			name="tablaTelefonos"
			columnSizes="40,40,20"
			columnNames="gratuita.operarDatosBeneficiario.literal.telefonoUso,
					gratuita.operarDatosBeneficiario.literal.numeroTelefono,
					censo.preferente.sms">
	
			<logic:notEmpty name="DefinirTelefonosJGForm" property="telefonos">
				<logic:iterate name="DefinirTelefonosJGForm" property="telefonos" id="telefono" indexId="index">
					<bean:define id="telefonosJGForm" name="telefono" property="definirTelefonosJGForm"></bean:define>
					<bean:define id="preferenteSms" name="telefono" property="preferenteSms"></bean:define>
					
					<tr id='fila_<bean:write name="index"/>' class='<%=((++contador + 1) % 2 == 0 ? "filaTablaPar" : "filaTablaImpar")%>'>
						<td>
							<bean:write name="telefonosJGForm" property="nombreTelefonoJG" />
						</td>
						
						<td>
							<bean:write name="telefonosJGForm" property="numeroTelefonoJG" />
						</td>
										
						<td>							
							<input type='checkbox' 																 
								value='<bean:write name="telefonosJGForm" property="preferenteSms"/>'
								<%=(preferenteSms.equals("1")) ? "checked" : ""%> 
								disabled='disabled'/>
						</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</siga:Table>	
<%
	}
%>		

	<script type="text/javascript">	
		function refrescarLocal(){
			buscarTelefonos();
		}	
		
		function buscarTelefonos() {		
			document.forms[0].target = "_self";
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}

		//función que inserta una fila para introducir un numero de telefono
		function accioninsertar() {		
			var tablaDatos = jQuery("#tablaTelefonos_BodyDiv tbody");
			var tablaHead = jQuery("#tablaTelefonos_HeaderDiv thead");
			var numTotalElementos = tablaDatos.children().length;
			var numSiguiente = numTotalElementos + 1;				
			
			var tieneDatos = (numTotalElementos>0);
			
			var elementoTr = "<tr id='fila_" + numTotalElementos + "'";
			if (tieneDatos && tablaDatos.find("tr:eq(0)").attr("class").indexOf("filaTablaPar") >= 0) {
				elementoTr = elementoTr + " class='filaTablaImpar tableTitle'";
			} else {
				elementoTr = elementoTr + " class='filaTablaPar tableTitle'";
			}
			elementoTr = elementoTr + ">";					

			var estiloTd = "";
			
			if (tieneDatos)
				estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(0)").attr("style");
			else 
				estiloTd = tablaHead.find("tr:eq(0)").find("th:eq(0)").attr("style");			
			var elementoTd = "<td style='" + estiloTd + "'>";
			elementoTd = elementoTd + "<input type='text' id='nombreTelefonoJG_" + numTotalElementos + "' class='box' maxLength='20' style='width:110px' value='' />";
			elementoTd = elementoTd + "</td>";
			elementoTr = elementoTr + elementoTd;		
			
			if (tieneDatos)
				estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(1)").attr("style");
			else 
				estiloTd = tablaHead.find("tr:eq(0)").find("th:eq(1)").attr("style");
			elementoTd = "<td style='" + estiloTd + "'>";
			elementoTd = elementoTd + "<input type='text' id='numeroTelefonoJG_" + numTotalElementos + "' class='box' maxLength='20' style='width:110px' value='' />";
			elementoTd = elementoTd + "</td>";
			elementoTr = elementoTr + elementoTd;	
			
			if (tieneDatos)
				estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(2)").attr("style");
			else 
				estiloTd = tablaHead.find("tr:eq(0)").find("th:eq(2)").attr("style");
			elementoTd = "<td style='" + estiloTd + "'>";
			elementoTd = elementoTd + "<input type='checkbox' id='preferenteSms_" + numTotalElementos + "' name='preferenteSms_" + numTotalElementos + "' class='preferenteSms' value='0' onClick='checkSms(" + numTotalElementos + ")' />";
			elementoTd = elementoTd + "</td>";
			elementoTr = elementoTr + elementoTd;	
			
			if (tieneDatos)
				estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(3)").attr("style");
			else 
				estiloTd = tablaHead.find("tr:eq(0)").find("th:eq(3)").attr("style");
			elementoTd = "<td style='" + estiloTd + "'>";
			elementoTd = elementoTd + "<img src='/SIGA/html/imagenes/bborrar_off.gif'" +
											" style='cursor:hand'" +
											" alt='<siga:Idioma key="general.borrar"/>'" +
											" name=''" +
											" border='0'" +
											" onclick='borrarFila(" + numSiguiente + ")' />";	
			elementoTd = elementoTd + "</td>";
			elementoTr = elementoTr + elementoTd;	
			
			elementoTr = elementoTr + "</tr>";	
			tablaDatos.prepend(elementoTr);	
		}
		
		//funcion de marcar un telefono para enviar sms
		function checkSms(fila){
			var idFila = "preferenteSms_" + fila;
			
			jQuery("input:checkbox").each(function(){
				if (this.id == idFila) {
					this.value = '1';
				} else {
					this.value = '0';
					this.checked = false;
				}
			});
	   	}
	
		//borrar un telefono se borra la fila.		
		function  borrarFila (fila) {
			var tablaDatos = jQuery("#tablaTelefonos_BodyDiv tbody");
			var idFila = "fila_" + fila;
			var filaDatos = tablaDatos.find("#" + idFila);
			
			
			if (fila == 0) {				
				tablaDatos.find("tr").each(function(){
					if (this.id != idFila) {
						jQuery(this).find("td:eq(0)").attr("style", tablaDatos.find("tr:eq(0)").find("td:eq(0)").attr("style"));
						jQuery(this).find("td:eq(1)").attr("style", tablaDatos.find("tr:eq(0)").find("td:eq(1)").attr("style"));
						jQuery(this).find("td:eq(2)").attr("style", tablaDatos.find("tr:eq(0)").find("td:eq(2)").attr("style"));
						jQuery(this).find("td:eq(3)").attr("style", tablaDatos.find("tr:eq(0)").find("td:eq(3)").attr("style"));
					}
				});
			}
			
			filaDatos.remove();
		}					
	</script>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>