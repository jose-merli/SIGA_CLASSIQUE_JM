<!DOCTYPE html>
<html>
<head>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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

<%@ page import="com.atos.utils.ClsConstants"%>
<!-- BusquedaLetrados.jsp -->

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">	
	<css:css relativePath="/html/jsp/general/" files="stylesheet2.jsp" />
	<css:css relativePath="/html/css/" files="ajaxtags.css" />
	<css:css relativePath="/html/css/themes/" files="default.css,mac_os_x.css" />
	<js:javascript 	relativePath="/html/js/overlibmws/"		files="overlibmws.js" />
	<js:javascript 	relativePath="/html/js/"				files="jquery-1.7.1.js,jquery.custom.js,jquery.msgbox.js,SIGA.js" />
	<siga:Titulo titulo="censo.busquedaClientes.letrados.titulo" localizacion="censo.busquedaClientes.localizacion" />

<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="<html:rewrite page='/html/css/ajaxtags.css'/>" />
	<html:base />
	
</head>

<body onload="ajusteAltoDisplayTag('divWindowDataScroll'); selectRow('<bean:write name='BusquedaLetradosForm' property='tableName' scope='request' />',
                        '<bean:write name='BusquedaLetradosForm' property='id' scope='request' />', '0');">
<html:errors />

<bean:define id="accessType" name="USRBEAN" property="accessType" scope="session"/>
<bean:define id="modo" name="BusquedaLetradosForm" property="modo" scope="request" />

<html:form action="/CEN_BusquedaLetradosNew.do" method="POST">	
	<html:hidden property="accion" value="buscar" />
	<html:hidden property="id" value="" />
	<html:hidden property="idInstitucion" name="BusquedaLetradosForm" />
	
	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
		<table style="width: 100%;">
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.noResidente" />
				</td>
				<td align="left">
					<html:select name="BusquedaLetradosForm" property="residencia" styleClass="boxCombo">
						<html:option value="">&nbsp;</html:option>
						<html:option value="<%=ClsConstants.COMBO_SIN_RESIDENCIA%>"	key="censo.busquedaClientes.sinResidencia"></html:option>
						<html:option value="<%=ClsConstants.COMBO_RESIDENCIA_MULTIPLE%>" key="censo.busquedaClientes.residenciaMultiple"></html:option>
					</html:select>
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda" />
				</td>
				<td>
					<html:checkbox property="busquedaExacta" name="BusquedaLetradosForm" />
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.nif" />
				</td>
				<td>
					<html:text property="nif" name="BusquedaLetradosForm" styleId="nifCif" size="15" styleClass="box" />
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.nombre" />
				</td>
				<td>
					<html:text property="nombre" name="BusquedaLetradosForm"	styleId="nombre" size="30" styleClass="box" />
				</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma
					key="censo.busquedaClientes.literal.apellido1" /></td>
				<td><html:text property="apellidos1" name="BusquedaLetradosForm"
					size="30" styleClass="box" /></td>
				<td class="labelText"><siga:Idioma
					key="censo.busquedaClientes.literal.apellido2" /></td>
				<td><html:text property="apellidos2" name="BusquedaLetradosForm"
					size="30" styleClass="box" /></td>
			</tr>
		</table>
	</siga:ConjCampos>
</html:form>

<siga:ConjBotonesBusqueda
	botonera="${BusquedaLetradosForm.botonesBusqueda}"
	titulo="censo.busquedaClientes.letrados.titulo" />

<logic:empty property="table" name="BusquedaLetradosForm">	
	<logic:equal property="accion" value="buscar" name="BusquedaLetradosForm">
		<div class="labelText" id="empty" style="font-weight:bold; text-align:center; vertical-align:bottom; height:150px; margin-top:50px;">
			<siga:Idioma key="messages.noRecordFound" />
		</div>
	</logic:equal>
</logic:empty>

<logic:notEmpty property="table" name="BusquedaLetradosForm">
<div class="displayTag">
  <cb:checkboxDecorator
	formBean="${BusquedaLetradosForm}" formName="displ"
	submitUrl="/SIGA/CEN_BusquedaLetradosNew.do"
	decoratorName="checkboxDecorator" checkboxName="_chk"
	selectAllName="${BusquedaLetradosForm.selectAllName}"
	selectAllPagesName="${BusquedaLetradosForm.selectAllPagesName}"
	backupSelectedName="${BusquedaLetradosForm.backupSelectedName}"
	message="paginador.message.marcarDesmarcar">
	<dts:css height="0" />
	<display:table name="BusquedaLetradosForm.table"
		uid="${BusquedaLetradosForm.tableName}"
		id="${BusquedaLetradosForm.tableName}" export="false" defaultsort="2"
		sort="external" defaultorder="ascending"

		pagesize="${BusquedaLetradosForm.pageSize}"
		size="BusquedaLetradosForm.totalTableSize" partialList="true"
		requestURI="/CEN_BusquedaLetradosNew.do" form="displ"
		excludedParams="${BusquedaLetradosForm.selectParameterName} ${BusquedaLetradosForm.selectAllName} ${BusquedaLetradosForm.selectAllPagesName} ${BusquedaLetradosForm.backupSelectedName} page deleteForm"
		class="dataScroll">
		<display:setProperty name="decorator.media.html" value="com.siga.censo.decorator.LetradoActionsDecorator" />
		<display:column property="id" class="hidden" headerClass="hidden" />
		<display:column property="id" title="<input type='checkbox' name='${BusquedaLetradosForm.selectAllName}' id='${BusquedaLetradosForm.selectAllName}'/>"
			decorator="checkboxDecorator" style="width:15px;" />
		<display:column property="idPersona" titleKey="censo.busquedaClientes.idPersona" 
				style="width:110px;" />
		<display:column property="nif" titleKey="censo.busquedaClientesAvanzada.literal.nif" 
				sortable="true" nulls="false" style="width:140px;" />
		<display:column property="apellidos" titleKey="gratuita.turnos.literal.apellidosSolo" 
				sortable="true" nulls="false" style="width:300px; white-space: nowrap;" />
		<display:column property="nombre" titleKey="censo.busquedaClientesAvanzada.literal.nombre" 
				sortable="true" nulls="false" style="width:250px; white-space: nowrap;" />
		<display:column property="fechaNacimiento" titleKey="censo.busquedaClientesAvanzada.literal.fechaNacimiento" 
				sortable="true" nulls="false" decorator="com.siga.comun.decorator.ShortDateDecorator"
				style="width:90px; white-space: nowrap;" />
		<display:column property="actions" title="" nulls="false" style=" text-align:left;" />
		<display:column property="idInstitucion" class="hidden"	headerClass="hidden" />
	</display:table>
  </cb:checkboxDecorator>
</div>

	<siga:ConjBotonesAccion botonera="${BusquedaLetradosForm.botonesAccion}" />
</logic:notEmpty>


<iframe name="submitArea" src="/SIGA/html/jsp/general/blank.jsp" style="display: none"></iframe>

<script language="JavaScript">
	function refrescarLocal() {
	}
	function vueltaEnvio() {
	}
	function accionVolver() {
	}
	function accionCerrar() {
	}
	
	function buscar(){
		sub();
		document.forms[0].appendChild(document.createElement("<input type='hidden' name='backupForm' value='true'>"));
		document.forms[0].submit();
	}

	function accionGenerarExcels(){
   		sub();
   		if (existsSelected("displ")){
			document.getElementById("displ").accion.value = 'generaExcel';
			document.getElementById("displ").submit();
   		}
   		else{
   			alert ('<siga:Idioma key="general.message.seleccionar"/>');
   		}
		fin();
	}

	function lopd(fila) {
		alert('<siga:Idioma key="general.boton.lopd"/>');
	}

	/**
	 * Funcion generica de consulta de un registro
	 * @param formName Nombre del formulario  
	 * @param id PK del registro sobre el que se va a realizar la accion
	 */
  	function comunicar(idValue){
		sub();
		var accion = document.forms[0].accion.value;
		document.forms[0].accion.value="comunicar";
		document.forms[0].target="submitArea";
		document.forms[0].id.value=idValue;
		document.forms[0].submit();	
		document.forms[0].accion.value=accion;
		document.forms[0].target="";
	}
	/**
	 * Funcion generica de consulta de un registro
	 * @param formName Nombre del formulario  
	 * @param id PK del registro sobre el que se va a realizar la accion
	 */
  	function accionComunicar(){
		if (!existsSelected("displ")){
   			alert ('<siga:Idioma key="general.message.seleccionar"/>');
   			return;
   		}
		sub();
		var accion = document.forms[0].accion.value;
		document.forms[0].accion.value="comunicarVarios";
		document.forms[0].target="submitArea";
		var backupSelectedChild = document.createElement("<input type='hidden' name='backupSelected' value='"+getSelected()+"'>");
		var selectAllChild = document.createElement("<input type='hidden' name='selectAll' value='"+document.forms[1].selectAll.value+"'>");
		document.forms[0].appendChild(backupSelectedChild);
		document.forms[0].appendChild(selectAllChild);
		document.forms[0].submit();	
		document.forms[0].removeChild(backupSelectedChild);
		document.forms[0].removeChild(selectAllChild);
		document.forms[0].accion.value=accion;
		document.forms[0].target="";
	}
	
	function getSelected(){
		//recupera los seleccionados de otras paginas
		var seleccionados = document.getElementById("backupSelected").value;
		//recupera los datos de los registros seleccionados en la pagina actual
		var elements = document.getElementsByName("_chk");
		var seleccionados2 = "";
		for (i=0; i<elements.length; i++){
			if(elements[i].type == "checkbox" && elements[i].checked){
				seleccionados2 += "," + elements[i].value
			}
		}
		return seleccionados + seleccionados2;
	}
	

</script>
</body>



</html>