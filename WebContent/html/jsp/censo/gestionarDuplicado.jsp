<!-- SolicitudIncorporacionDatos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudInstituBean" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="utils.system"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.censo.form.SolicitudIncorporacionForm" %>
<%@ page import="com.atos.utils.*"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="fmt.tld" prefix="fmt"%>

<%

	String app=request.getContextPath();
	Hashtable datos = (Hashtable)request.getAttribute("datos");
	Hashtable htPersona0 = (Hashtable)datos.get("persona0");
	Hashtable htPersona1 = (Hashtable)datos.get("persona1");
	CenPersonaBean persona0 = (CenPersonaBean)htPersona0.get("datosPersonales");
	CenPersonaBean persona1 = (CenPersonaBean)htPersona1.get("datosPersonales");
	String idPersona0 =	persona0.getIdPersona().toString();
	String idPersona1 = persona1.getIdPersona().toString();
	String colegiaciones0 = htPersona0.get("colegiaciones").toString();
	String colegiaciones1 = htPersona1.get("colegiaciones").toString();


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

	<style>
		.ocultar {display:none}
	</style>	


<html>

<head>
	<title><siga:Idioma key="censo.SolicitudIncorporacionDatos.titulo"/></title>

  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	
</head>

<body onload="ajusteAlto('divScroll')" class="tablaCentralCampos" >

	
<html:form action="/CEN_MantenimientoDuplicados.do" method="POST" target="submitArea">
<html:hidden property = "idPersonaOrigen" value = ""/>
<html:hidden property = "idPersonaDestino" value = ""/>
<html:hidden property = "idInstOrigen" value = ""/>
<html:hidden property = "modo" value = ""/>
<html:hidden property = "listaDirecciones" value=""/>
	
	<table class="tablaTitulo" align="center" width="100%">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="censo.fusionDuplicados.explicacion"/>
			</td>
		</tr>
		<tr>
			<td class="titulitosDatos">
				<img src="/SIGA/html/imagenes/blopd_disable.gif" align="middle" border="0" >
				<siga:Idioma key="censo.fusionDuplicados.advertencia"/>
				<img src="/SIGA/html/imagenes/blopd_disable.gif" align="middle" border="0" >
			</td>
		</tr>
	</table>

	<bean:define id="data" name="datos" scope="request"/>
	
	<c:choose>
		<c:when test="${empty data}">
			<br>
			<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			<br>
		</c:when>
		<c:otherwise>
		
				<table width="100%"> 
					<tr>
						<td class="tableTitle" colspan="3"><siga:Idioma key="censo.fusionDuplicados.datosPersonales.titulo"/></td>
					</tr>
					<tr>
						<td class="labelText" width="30%">
							<siga:Idioma key="censo.fusionDuplicados.datosPersonales.identificador"/> | Última Modificación</td>
						<td class="labelTextValue" width="30%">
							<c:out value="${datos.persona0.datosPersonales.idPersona}"/>
							| <c:out value="${datos.persona0.datosPersonales.fechaMod}"/></td>
						<td class="labelTextValue" width="30%">
							<c:out value="${datos.persona1.datosPersonales.idPersona}"/>
							| <c:out value="${datos.persona1.datosPersonales.fechaMod}"/></td>
					</tr>
					<tr>
						<td class="labelText">NIF</td>
						<td class="labelTextValue">
							<c:out value="${datos.persona0.datosPersonales.NIFCIF}"/></td>
						<td class="labelTextValue">
							<c:out value="${datos.persona1.datosPersonales.NIFCIF}"/></td>
					</tr>
					<tr>
						<td class="labelText">Nombre | Apellido 1 | Apellido 2</td>
						<td class="labelTextValue">
							<c:out value="${datos.persona0.datosPersonales.nombre}"/>
							|<c:out value="${datos.persona0.datosPersonales.apellido1}"/>
							|<c:out value="${datos.persona0.datosPersonales.apellido2}"/></td>
						<td class="labelTextValue">
							<c:out value="${datos.persona1.datosPersonales.nombre}"/>
							|<c:out value="${datos.persona1.datosPersonales.apellido1}"/>
							|<c:out value="${datos.persona1.datosPersonales.apellido2}"/></td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="censo.fusionDuplicados.datosPersonales.fechaNacimiento"/>
							|<siga:Idioma key="censo.fusionDuplicados.datosPersonales.lugarNacimiento"/></td>
						<td class="labelTextValue">
							<c:out value="${datos.persona1.datosPersonales.fechaNacimiento}"/>
							|<c:out value="${datos.persona1.datosPersonales.naturalDe}"/></td>
						<td class="labelTextValue">
							<c:out value="${datos.persona1.datosPersonales.fechaNacimiento}"/>
							|<c:out value="${datos.persona1.datosPersonales.naturalDe}"/></td>
					</tr>
					<tr style="border-top:1px solid">
						<td class="labelText"></td>
						<!-- Aqui se meten los radio buttons para seleccionar el colegiado base -->
						<td class="labelTextValue">
							<input name="idPersonaSel" value="${datos.persona0.datosPersonales.idPersona}" type="radio" onclick="seleccionar(0, <%=idPersona0%>, <%=idPersona1%>, '<%=colegiaciones1%>');"/> </td>
						<td class="labelTextValue">
							<input name="idPersonaSel" value="${datos.persona0.datosPersonales.idPersona}" type="radio" onclick="seleccionar(1, <%=idPersona1%>, <%=idPersona0%>, '<%=colegiaciones0%>');"/></td>
					</tr>
				</table>
		<div name="divScroll" style="overflow:auto;height:450;">
				<table width="100%">
					<tr>
						<td class="tableTitle" colspan="3">
							<siga:Idioma key="censo.fusionDuplicados.colegiaciones.titulo"/>
						</td>
					</tr>
								
					<tr><td width="30%">&nbsp;</td><td width="30%">&nbsp;</td><td width="30%">&nbsp;</td></tr>
					<tr>
						<td>
							<table>
								<tr><td class="labelText"> 
									<siga:Idioma key="Colegio"/> 
									| <siga:Idioma key="NºColegiado"/> 
									| <siga:Idioma key="Fecha Incorporación"/>
								</td></tr>
								<tr><td class="labelText"> 
									<siga:Idioma key="Residente"/> 
									| <siga:Idioma key="Inscrito"/>
								</td></tr>
								<tr><td class="labelText">
									<siga:Idioma key="Estado Colegial"/>
								</td></tr>
								<tr><td class="labelText">
									<siga:Idioma key="Gestión"/>
								</td></tr>
							</table>
						</td>
						<td>
							<table>
								<c:forEach items="${datos.persona0.datosColegiales}" var="datosCol"  varStatus="status">
									<tr>
										<td class="labelTextValue" colspan="2">
											<c:out value="${datosCol.institucionColegiacion}"/> 
											| <c:out value="${datosCol.datosColegiacion.NColegiado}"/><c:out value="${datosCol.datosColegiacion.NComunitario}"/>
											| <c:out value="${datosCol.datosColegiacion.fechaIncorporacion}"/>
										</td>
									</tr>
									<tr>
									<c:choose>
										<c:when test="${datosCol.datosColegiacion.situacionResidente=='1'}">
											<td class="labelText">
											<c:out value="Residente"/>
										</c:when>
										<c:otherwise>
											<td class="labelTextValue">
											<c:out value="No Residente"/>
										</td>
										</c:otherwise>
									</c:choose>
									<td class="labelTextValue">
									<c:choose>
										<c:when test="${datosCol.datosColegiacion.comunitario=='1'}">
											<c:out value="Inscrito"/>
										</c:when>
										<c:otherwise>
											<c:out value="No Inscrito"/>
										</c:otherwise>
									</c:choose>
									</td>
									</tr>
									<tr>
										<td class="labelTextValue" colspan="2">
										<c:if test="${datosCol.estadoColegiacion!=null}">
												<c:out value="${datosCol.estadoColegiacion.row.DESCRIPCION}"/>
												desde <c:out value="${datosCol.estadoColegiacion.row.FECHAESTADO}"/>
										</c:if>
										<c:if test="${datosCol.estadoColegiacion==null}">
											Sin estado colegial
										</c:if>
										</td>
									</tr>
									<tr>
									<c:if test="${datosCol.fechaProduccion!=''}">
										<td class="labelTextValue">Se autogestiona</td>
									</c:if>
									<c:if test="${datosCol.fechaProduccion==''}">
										<td class="labelText">Gestionado por CGAE</td>
									</c:if>
									</tr>
									<tr><td>&nbsp;</td></tr>
								</c:forEach>
							</table>
						</td>
						<td>
							<table>
								<c:forEach items="${datos.persona1.datosColegiales}" var="datosCol"  varStatus="status">
									<tr>
										<td class="labelTextValue" colspan="2">
											<c:out value="${datosCol.institucionColegiacion}"/> 
											| <c:out value="${datosCol.datosColegiacion.NColegiado}"/><c:out value="${datosCol.datosColegiacion.NComunitario}"/>
											| <c:out value="${datosCol.datosColegiacion.fechaIncorporacion}"/>
										</td>
									</tr>
									<tr>
									<c:choose>
										<c:when test="${datosCol.datosColegiacion.situacionResidente=='1'}">
											<td class="labelText">
											<c:out value="Residente"/>
										</c:when>
										<c:otherwise>
											<td class="labelTextValue">
											<c:out value="No Residente"/>
										</td>
										</c:otherwise>
									</c:choose>
									<td class="labelTextValue">
									<c:choose>
										<c:when test="${datosCol.datosColegiacion.comunitario=='1'}">
											<c:out value="Inscrito"/>
										</c:when>
										<c:otherwise>
											<c:out value="No Inscrito"/>
										</c:otherwise>
									</c:choose>
									</td>
									</tr>
									<tr>
										<td class="labelTextValue" colspan="2">
										<c:if test="${datosCol.estadoColegiacion!=null}">
												<c:out value="${datosCol.estadoColegiacion.row.DESCRIPCION}"/>
												desde <c:out value="${datosCol.estadoColegiacion.row.FECHAESTADO}"/>
										</c:if>
										<c:if test="${datosCol.estadoColegiacion==null}">
											Sin estado colegial
										</c:if>
										</td>
									</tr>
									<tr>
									<c:if test="${datosCol.fechaProduccion!=''}">
										<td class="labelTextValue">Se autogestiona</td>
									</c:if>
									<c:if test="${datosCol.fechaProduccion==''}">
										<td class="labelText">Gestionado por CGAE</td>
									</c:if>
									</tr>
									<tr><td>&nbsp;</td></tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					
					<tr>
						<td class="tableTitle" colspan="3">
							<siga:Idioma key="censo.fusionDuplicados.direcciones.cabecera"/>
						</td>
					</tr>
								
					<tr><td></td></tr>
					<tr>
						<td>
							<table>
									<tr><td class="labelText">Domicilio </td></tr>
									<tr><td class="labelText">Poblacion | Provincia</td></tr>
									<tr><td class="labelText">Pais</td></tr>
									<tr><td class="labelText">Telefono | Móvil | Fax</td></tr>
									<tr><td class="labelText">Correo</td></tr>
									<tr><td class="labelText">Última modificación</td></tr>
							</table>
						</td>
						<td>
							<table>
								<c:forEach items="${datos.persona0.datosDirecciones}" var="datosDir"  varStatus="status">
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.DOMICILIO}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.POBLACION}"/> | <c:out value="${datosDir.PROVINCIA}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.PAIS}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.TELEFONO1}"/> | <c:out value="${datosDir.MOVIL}"/> | <c:out value="${datosDir.FAX1}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.CORREOELECTRONICO}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.FECHAMODIFICACION}"/></td></tr>
									<tr><td class="labelTextValue"><input name="checkDireccion" id="${datosDir.IDPERSONA}" value="${datosDir.IDINSTITUCION}&&${datosDir.IDPERSONA}&&${datosDir.IDDIRECCION}" type="checkBox"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;</td></tr>
								</c:forEach>
							</table>
						</td>
						<td>
							<table>
								<c:forEach items="${datos.persona1.datosDirecciones}" var="datosDir"  varStatus="status">
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.DOMICILIO}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.POBLACION}"/> | <c:out value="${datosDir.PROVINCIA}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.PAIS}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.TELEFONO1}"/> | <c:out value="${datosDir.MOVIL}"/> | <c:out value="${datosDir.FAX1}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.CORREOELECTRONICO}"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;<c:out value="${datosDir.FECHAMODIFICACION}"/></td></tr>
									<tr><td class="labelTextValue"><input name="checkDireccion" id="${datosDir.IDPERSONA}" value="${datosDir.IDINSTITUCION}&&${datosDir.IDPERSONA}&&${datosDir.IDDIRECCION}" type="checkBox"/></td></tr>
									<tr><td class="labelTextValue">&nbsp;</td></tr>
								</c:forEach>
							</table>
						</td>
					</tr>
			</table>
		</c:otherwise>
	</c:choose>

</div>
</html>
	<siga:ConjBotonesAccion botones="A,C" clase="botonesDetalle" />
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		function accionCerrar(){	
			top.cierraConParametros("NOMODIFICADO");
		}

		// jbd // helpers para recorrer radios y checks
		var incluido;
		var seleccionado = false;
		
		function getSelectedRadio(buttonGroup) {
		   // returns the array number of the selected radio button or -1 if no button is selected
		   if (buttonGroup[0]) { // if the button group is an array (one button is not an array)
		      for (var i=0; i<buttonGroup.length; i++) {
		         if (buttonGroup[i].checked) {
		            return i
		         }
		      }
		   } else {
		      if (buttonGroup.checked) { return 0; } // if the one button is checked, return zero
		   }
		   // if we get to this point, no radio button is selected
		   return -1;
		}
		
		function getSelectedRadioValue(buttonGroup) {
		   // returns the value of the selected radio button or "" if no button is selected
		   var i = getSelectedRadio(buttonGroup);
		   if (i == -1) {
		      return "";
		   } else {
		      if (buttonGroup[i]) { // Make sure the button group is an array (not just one button)
		         return buttonGroup[i].value;
		      } else { // The button group is just the one button, and it is checked
		         return buttonGroup.value;
		      }
		   }
		}
		
		function getSelectedCheckbox(buttonGroup) {
		   // Go through all the check boxes. return an array of all the ones
		   // that are selected (their position numbers). if no boxes were checked,
		   // returned array will be empty (length will be zero)
		   var retArr = new Array();
		   var lastElement = 0;
		   if (buttonGroup[0]) { // if the button group is an array (one check box is not an array)
		      for (var i=0; i<buttonGroup.length; i++) {
		         if (buttonGroup[i].checked && buttonGroup[i].disabled==false) {
		            retArr.length = lastElement;
		            retArr[lastElement] = i;
		            lastElement++;
		         }
		      }
		   } else { // There is only one check box (it's not an array)
		      if (buttonGroup.checked && buttonGroup.disabled==false) { // if the one check box is checked
		         retArr.length = lastElement;
		         retArr[lastElement] = 0; // return zero as the only array value
		      }
		   }
		   return retArr;
		}

		function getSelectedCheckboxValue(buttonGroup) {
		   // return an array of values selected in the check box group. if no boxes
		   // were checked, returned array will be empty (length will be zero)
		   var retArr = new Array(); // set up empty array for the return values
		   var selectedItems = getSelectedCheckbox(buttonGroup);
		   if (selectedItems.length != 0) { // if there was something selected
		      retArr.length = selectedItems.length;
		      for (var i=0; i<selectedItems.length; i++) {
		         if (buttonGroup[selectedItems[i]]) { // Make sure it's an array
		            retArr[i] = buttonGroup[selectedItems[i]].value;
		         } else { // It's not an array (there's just one check box and it's selected)
		            retArr[i] = buttonGroup.value;// return that value
		         }
		      }
		   }
		   return retArr;
		}

		function seleccionarChecks(buttonGroup, id){

			if (buttonGroup[0]) { // Cogemos el grupo de checks
			      for (var i=0; i<buttonGroup.length; i++) {
				      if(buttonGroup[i].id==id){
				      		buttonGroup[i].checked=true;
				      		buttonGroup[i].disabled=true;
				      }else{
				    	  	buttonGroup[i].checked=false;
				    	  	buttonGroup[i].disabled=false;
				      }
			      }
		   } else {
			   if(buttonGroup.id==id){
		      		buttonGroup.checked=true;
		      		buttonGroup.disabled=true;
		      }else{
		    	  	buttonGroup.checked=false;
		    	  	buttonGroup.disabled=false;
		      }
		   }
		}
		// jbd // fin helpers
			
		function accionAceptar(){
			if(incluido){
				alert("<siga:Idioma key="censo.fusionDuplicados.error.existeColegiacion"/>");
			}else if (seleccionado){
				<%if(idPersona1.equalsIgnoreCase(idPersona0)){%>
					alert("<siga:Idioma key="censo.fusionDuplicados.error.mismaPersona"/>");
				<%}else{%>
					sub();
					if(document.forms[0].checkDireccion){
						var direcciones = getSelectedCheckboxValue(document.forms[0].checkDireccion);
						document.forms[0].listaDirecciones.value = direcciones;
					}
					if(confirm("Se van a combinar los datos a una sola persona.")){
						document.forms[0].modo.value = "aceptar";
						document.forms[0].target="submitArea";	
					 	document.forms[0].submit();
					}else{
						fin();
					}
				 <%}%>
			}else{
				alert("<siga:Idioma key="censo.fusionDuplicados.error.seleccionePersona"/>");
			}
		}

		function seleccionar(perso, idD, idO, col){
			document.forms[0].idPersonaDestino.value = idD;
			document.forms[0].idPersonaOrigen.value = idO;
			document.forms[0].idInstOrigen.value = col;
			if(document.forms[0].checkDireccion!=null){
				seleccionarChecks(document.forms[0].checkDireccion, idD);
			}
			seleccionado=true;
		}


	</script>
	</html:form>
</body>
</html>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>