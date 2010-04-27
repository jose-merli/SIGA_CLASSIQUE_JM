
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="displaytag.tld" 		prefix="display"%>
<%@ taglib uri="struts-html.tld" 		prefix="html"%>
<%@ taglib uri="struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri="libreria_SIGA.tld"		prefix="siga"%>
<%@ taglib uri="struts-logic.tld" 		prefix="logic"%>
<%@ taglib uri="c.tld" 					prefix="c"%>
<%@ taglib uri="CheckboxDisplaytag.tld" prefix="cb"%>
<%@ taglib uri="javascript.tld" 		prefix="js"%>
<%@ taglib uri="css.tld" 				prefix="css"%>
<%@ taglib uri="displaytagScroll.tld" 	prefix="dts"%>
<%@ taglib uri="ajaxtags.tld" 			prefix="ajax" %>

<%@ page import="com.atos.utils.ClsConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- BusquedaColegiados.jsp -->
<html:html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<meta http-equiv="Content-Type" 	content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Expires" 			content="0">
	<meta http-equiv="Pragma" 			content="no-cache">
	<meta http-equiv="Cache-Control" 	content="no-cache">
	<siga:Titulo titulo="censo.busquedaClientes.colegiados.titulo" localizacion="censo.busquedaClientes.localizacion" />
	<css:css 		relativePath="/html/jsp/general/" 		files="stylesheet2.jsp" />
	<css:css 		relativePath="/html/css/" 				files="ajaxtags.css" />
	<css:css 	   	relativePath="/html/css/themes/" 		files="default.css,mac_os_x.css" />
	<js:javascript 	relativePath="/html/js/"				files="prototype.js,window.js" />
	<js:javascript 	relativePath="/html/js/scriptaculous/"	files="scriptaculous.js" />
	<js:javascript 	relativePath="/html/js/overlibmws/"		files="overlibmws.js" />
	<js:javascript 	relativePath="/html/js/"				files="SIGA.js,ajaxtags.js," />
	<js:javascript 	relativePath="/html/jsp/censo/"			files="busquedaColegiados.js.jsp" />
	<html:base />
</head>

<body onload="ajusteAltoDisplayTag('divWindowDataScroll'); selectRow('<bean:write name='BusquedaColegiadosForm' property='tableName' scope='request' />',
                        '<bean:write name='BusquedaColegiadosForm' property='id' scope='request' />', '0');">
<html:errors />

<bean:define id="accessType" name="USRBEAN" property="accessType" scope="session"/>
<bean:define id="modo" name="BusquedaColegiadosForm" property="modo" scope="request" />

<html:form action="/CEN_BusquedaColegiados.do" method="POST">	
	<html:hidden property="accion" value="buscar" />
	<html:hidden property="id" value="" />
	<html:hidden property="idInstitucion" name="BusquedaColegiadosForm"  />
	<html:hidden property="tipoBusqueda" name="BusquedaColegiadosForm" />
	
	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
		<table style="width: 100%;">
			<tr>
				<td class="labelText"><siga:Idioma key="censo.busquedaClientes.literal.colegio" /></td>
				<td align="left" colspan="3">
				<!-- Si la lista de instituciones esta vacia significa que estamos en un colegio -->
				<logic:empty property="instituciones" name="BusquedaColegiadosForm">
					<html:text property="nombreInstitucion" name="BusquedaColegiadosForm" size="70" styleClass="boxComboConsulta" readonly="true"/>
				</logic:empty>
				<logic:notEmpty property="instituciones" name="BusquedaColegiadosForm">
					<html:select property="idInstitucion" name="BusquedaColegiadosForm" styleClass="boxCombo">
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="BusquedaColegiadosForm" property="instituciones" value="id" label="nombre"></html:optionsCollection>
					</html:select>
				</logic:notEmpty>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado" />
				</td>
				<td>
					<html:text property="nColegiado" name="BusquedaColegiadosForm" styleId="nColegiado" size="10" styleClass="box" />
				</td>				
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda" />
				</td>
				<td>
					<html:checkbox property="busquedaExacta" name="BusquedaColegiadosForm" />
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.nif" />
				</td>
				<td>
					<html:text property="nif" name="BusquedaColegiadosForm" styleId="nifCif" size="15" styleClass="box" />
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.nombre" />
				</td>
				<td>
					<html:text property="nombre" name="BusquedaColegiadosForm"	styleId="nombre" size="30" styleClass="box" />
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.apellido1" />
				</td>
				<td>
					<html:text property="apellidos1" name="BusquedaColegiadosForm" size="30" styleClass="box" />
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.apellido2" />
				</td>
				<td>
					<html:text property="apellidos2" name="BusquedaColegiadosForm" size="30" styleClass="box" />
				</td>
			</tr>
		</table>
	</siga:ConjCampos>
</html:form>

<siga:ConjBotonesBusqueda
	botonera="${BusquedaColegiadosForm.botonesBusqueda}"
	titulo="censo.busquedaClientes.colegiados.titulo" />

<%@ include file="/html/jsp/censo/ResultadoBusquedaColegiados.jsp" %>

<siga:ConjBotonesAccion botonera="${BusquedaColegiadosForm.botonesAccion}" />
	
<ajax:autocomplete
  baseUrl="/SIGA/CEN_BusquedaColegiados.do?accion=ajaxGetNifList"
  source="nifCif" target="nifCif"
  parameters="nifCif={nifCif}" className="autocomplete"
  minimumCharacters="1"
/>

</body>



</html:html>



