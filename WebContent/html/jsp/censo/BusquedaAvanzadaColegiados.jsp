
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-15"%>

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
<!-- BusquedaAvanzadaColegiados.jsp -->
<html:html>
<head>
	<siga:Titulo titulo="censo.busquedaClientesAvanzada.colegiados.titulo" localizacion="censo.busquedaClientes.localizacion" />
	<meta http-equiv="Content-Type" 	content="text/html; charset=ISO-8859-15">
	<meta http-equiv="Expires" 			content="0">
	<meta http-equiv="Pragma" 			content="no-cache">
	<meta http-equiv="Cache-Control" 	content="no-cache">
	<css:css 		relativePath="/html/jsp/general/" 		files="stylesheet2.jsp" />
	<css:css 		relativePath="/html/css/" 				files="ajaxtags.css" />
	<css:css 	   	relativePath="/html/css/themes/" 		files="default.css,mac_os_x.css" />
	<js:javascript 	relativePath="/html/js/"				files="prototype.js,window.js" />
	<js:javascript 	relativePath="/html/js/scriptaculous/"	files="scriptaculous.js" />
	<js:javascript 	relativePath="/html/js/overlibmws/"		files="overlibmws.js" />
	<js:javascript 	relativePath="/html/js/"				files="SIGA.js,ajaxtags.js,calendarJs.jsp" />
	<js:javascript 	relativePath="/html/jsp/censo/"			files="busquedaColegiados.js.jsp" />
	<html:base />
</head>

<body onload="ajusteAltoDisplayTag('divWindowDataScroll'); 
              selectRow('<bean:write name='BusquedaColegiadosForm' property='tableName' scope='request' />',
                        '<bean:write name='BusquedaColegiadosForm' property='id' scope='request' />', '0');">
<html:errors />

<bean:define id="accessType" name="USRBEAN" property="accessType" scope="session"/>
<bean:define id="modo" name="BusquedaColegiadosForm" property="modo" scope="request" />

<html:form action="/CEN_BusquedaColegiados.do" method="POST">	
	<html:hidden property="accion" value="buscarAvanzada" />
	<html:hidden property="id" value="" />
	<html:hidden property="tipoBusqueda" name="BusquedaColegiadosForm" />
	
	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
		<table style="width: 100%;">
			<tr>
				<td class="labelText"><siga:Idioma key="censo.busquedaClientes.literal.colegio" /></td>
				<td align="left" colspan="3">
					<html:select property="idInstitucion" name="BusquedaColegiadosForm" styleClass="boxCombo">
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="BusquedaColegiadosForm" property="instituciones" value="id" label="nombre"></html:optionsCollection>
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado" />
				</td>
				<td colspan="3">
					<html:text property="nColegiado" name="BusquedaColegiadosForm" styleId="nColegiado" size="10" styleClass="box" />
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
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaNacimientoDesde"/>
				</td>				
				<td>
					<html:text styleClass="box" name="BusquedaColegiadosForm" property="fechaNacimientoDesde" size="10"  readonly="true" />
					<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimientoDesde);"><img src="/SIGA/html/imagenes/calendar.gif" border="0"></a>
				</td>
		        <td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaNacimientoHasta"/>
				</td>				
				<td>
					<html:text styleClass="box" name="BusquedaColegiadosForm" property="fechaNacimientoHasta" size="10"  readonly="true" />
					<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimientoHasta);"><img src="/SIGA/html/imagenes/calendar.gif" border="0"></a>
				</td>
			</tr>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.sexo"/>
				</td>
				<td >
					<html:select name="BusquedaColegiadosForm" property="sexo" styleClass="boxCombo">
						<html:option value="" > </html:option>
						<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>" ><siga:Idioma key="censo.sexo.hombre"/></html:option>
						<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>" ><siga:Idioma key="censo.sexo.mujer"/></html:option>
					</html:select>						
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.domicilio"/>
				</td>
				<td colspan="3">
					<html:text name="BusquedaColegiadosForm" property="domicilio" size="40" styleClass="box"></html:text>
				</td>
			</tr>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.cp"/>
				</td>
				<td>
					<html:text name="BusquedaColegiadosForm" property="codigoPostal" size="5" styleClass="box"></html:text>	
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.telefono"/>
				</td>				
				<td colspan="3">
					<html:text name="BusquedaColegiadosForm" property="telefono" size="20" styleClass="box"></html:text>
				</td>
			</tr>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fax"/>
				</td>
				<td>
					<html:text name="BusquedaColegiadosForm" property="fax" size="20" styleClass="box"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.correo"/>
				</td>
				<td colspan="3">
					<html:text name="BusquedaColegiadosForm" property="correoElectronico" size="30" styleClass="box"></html:text>
				</td>
			</tr>
			 <tr>
		   		<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.grupoCliente"/>
				</td>
				<td colspan="4">
					<html:select property="gruposFijos" name="BusquedaColegiadosForm" styleClass="boxCombo">
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="BusquedaColegiadosForm" property="listaGruposFijos" value="key" label="value"></html:optionsCollection>
					</html:select>
				</td>
			</tr>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.tipoApunteCV"/>
				</td>
				<td>
					<html:select property="tipoApunteCV" name="BusquedaColegiadosForm" styleId="tipoApunteCV" styleClass="boxCombo">
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="BusquedaColegiadosForm" property="listaTipoApunteCV" value="key" label="value"></html:optionsCollection>
					</html:select>
				</td>
				<td >
					<html:select property="tipoApunteCVSubtipo1" name="BusquedaColegiadosForm" styleId="tipoApunteCVSubtipo1" style="width:150px">
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="BusquedaColegiadosForm" property="listaTipoApunteCVSubtipo1" value="key" label="value"></html:optionsCollection>
					</html:select>
				</td>		
				<td colspan="3">
					<html:select property="tipoApunteCVSubtipo2" name="BusquedaColegiadosForm" styleId="tipoApunteCVSubtipo2" style="width:150px">
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="BusquedaColegiadosForm" property="listaTipoApunteCVSubtipo2" value="key" label="value"></html:optionsCollection>
					</html:select>
				</td>
		   </tr>			
		</table>
	</siga:ConjCampos>
	
	<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo2">
		<table class="tablaCampos" align="center" width="100%" border="0">
			<tr>				
				<td class="labelText" >
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.tipoColegiacion"/>
				</td>				
				<td colspan="5">
					<html:select property="tipoColegiacion" name="BusquedaColegiadosForm" styleClass="boxCombo">
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="BusquedaColegiadosForm" property="listaTipoColegiacion" value="key" label="value"></html:optionsCollection>
					</html:select>
				</td>
			</tr>
			<tr>				
				<td class="labelText" >
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.situacion"/>
				</td>				
				<td >
					<html:select name="BusquedaColegiadosForm" property="ejerciente" style = "null" styleClass = "boxCombo" >
						<html:option value="" > </html:option>
						<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
						<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
					</html:select>						
				</td>
				<td class="labelText" >
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacionDesde"/>
				</td>
				<td >
					<html:text name="BusquedaColegiadosForm" property="fechaIncorporacionDesde" styleClass="box" readonly="true">
					</html:text>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaIncorporacionDesde);"><img src="/SIGA/html/imagenes/calendar.gif" border="0"></a>
				</td>
				<td class="labelText" >
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacionHasta"/>
				</td>
				<td >
					<html:text name="BusquedaColegiadosForm" property="fechaIncorporacionHasta" styleClass="box" readonly="true">
					</html:text>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaIncorporacionHasta);"><img src="/SIGA/html/imagenes/calendar.gif" border="0"></a>
				</td>
			</tr>				
			<tr>				
				<td class="labelText" >
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.residente"/>
				</td>
				<td >
					<html:select name="BusquedaColegiadosForm" property="residente" style = "null" styleClass = "boxCombo">
						<html:option value="" > </html:option>
						<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
						<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
					</html:select>						
				</td>
				<td class="labelText" >
					<siga:Idioma key="censo.busquedaClientesAvanzada.literal.comunitario"/>
				</td>
				<td colspan="3">
					<html:select name="BusquedaColegiadosForm" property="comunitario" style = "null" styleClass = "boxCombo" >
						<html:option value="" > </html:option>
						<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
						<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
					</html:select>						
				</td>
			</tr>				
		</table>
	</siga:ConjCampos>
	<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo3">
		<table class="tablaCampos" align="center">
		<tr>				
			<td class="labelText" >
				<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaAltaDesde"/>
			</td>
			<td >
				<html:text name="BusquedaColegiadosForm" property="factFechaAltaDesde" styleClass="box" readonly="true">
				</html:text>
				<a href='javascript://'onClick="return showCalendarGeneral(factFechaAltaDesde);"><img src="/SIGA/html/imagenes/calendar.gif" border="0"></a>
			</td>
			<td class="labelText" >
				<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaAltaHasta"/>
			</td>
			<td >
				<html:text name="BusquedaColegiadosForm" property="factFechaAltaHasta" styleClass="box" readonly="true">
				</html:text>
				<a href='javascript://'onClick="return showCalendarGeneral(factFechaAltaHasta);"><img src="/SIGA/html/imagenes/calendar.gif" border="0"></a>
			</td>
		</tr>
		</table>
	</siga:ConjCampos>	
	
</html:form>

<siga:ConjBotonesBusqueda
	botonera="${BusquedaColegiadosForm.botonesBusqueda}"
	titulo="censo.busquedaClientes.colegiados.titulo" />

<iframe name="submitArea" src="/SIGA/html/jsp/general/blank.jsp" style="display: none"></iframe>

<ajax:autocomplete
  baseUrl="/SIGA/CEN_BusquedaColegiados.do?accion=ajaxGetNifList"
  source="nifCif" target="nifCif"
  parameters="nifCif={nifCif}" className="autocomplete"
  minimumCharacters="1"
/>

<ajax:updateMultipleSelectFromSelect
	baseUrl="/SIGA/CEN_BusquedaColegiados.do?accion=ajaxGetComisionesCargos"
	source="tipoApunteCV" target="tipoApunteCVSubtipo1,tipoApunteCVSubtipo2" 
	parameters="tipoApunteCV={tipoApunteCV}"
	executeOnLoad="true"/>
</body>

</html:html>
