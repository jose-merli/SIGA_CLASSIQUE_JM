<!DOCTYPE html>
<html>
<head>
<!-- busquedaDuplicados.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Cache-Control" content="no-cache">
<%@ page contentType="text/html" language="java"errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.RowsContainer"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.CenClienteAdm"%>
<%@ page import="com.siga.censo.form.MantenimientoDuplicadosForm"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>

<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

 


<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	String titulo = "Mantenimiento de Duplicados";
	String localizacion = "Censo";
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	
	/**************/
		
%>



	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	


	<html:javascript formName="mantenimientoDuplicadosForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<script>
	
		function ChequearCriterios(){
			jQuery("#campoOrdenacion").append('<option value="numeroColegiado">Inst/Nº.Col</option>');
		}
		
		function preAccionBusqueda(){
			sub();
		}
		
		function postAccionBusqueda(){
			fin();
		}
		function onClickChkNumColegiado(){
			if(document.MantenimientoDuplicadosForm.chkNumColegiado.value=="1"){
				//Comprobamos si ya existe en la select para no añadirlo más
				if(jQuery("#campoOrdenacion option[value='numeroColegiado']").length == 0){
					jQuery("#campoOrdenacion").append('<option value="numeroColegiado">Inst/Nº.Col</option>');
				}
			}else{
				jQuery("#campoOrdenacion option[value='numeroColegiado']").remove();
			}
			
		}
		function buscar(){
			sub();
			//chequear los criterios en el hidden
			if (document.MantenimientoDuplicadosForm.chkApellidos.value=="1"){
				document.MantenimientoDuplicadosForm.valoresCheck.value = "1";
			}else{
				document.MantenimientoDuplicadosForm.valoresCheck.value = "0";
			}
			if (document.MantenimientoDuplicadosForm.chkNombreApellidos.value=="1"){
				document.MantenimientoDuplicadosForm.valoresCheck.value = document.MantenimientoDuplicadosForm.valoresCheck.value+"1";
			}else{
				document.MantenimientoDuplicadosForm.valoresCheck.value = document.MantenimientoDuplicadosForm.valoresCheck.value+"0";
			}
			if (document.MantenimientoDuplicadosForm.chkIdentificador.value=="1"){
				document.MantenimientoDuplicadosForm.valoresCheck.value = document.MantenimientoDuplicadosForm.valoresCheck.value+"1";
			}else{
				document.MantenimientoDuplicadosForm.valoresCheck.value = document.MantenimientoDuplicadosForm.valoresCheck.value+"0";
			}
			if (document.MantenimientoDuplicadosForm.chkNumColegiado.value=="1"){
				document.MantenimientoDuplicadosForm.valoresCheck.value = document.MantenimientoDuplicadosForm.valoresCheck.value+"1";
			}
			else{
				document.MantenimientoDuplicadosForm.valoresCheck.value = document.MantenimientoDuplicadosForm.valoresCheck.value+"0";
			}
			if(comprobarFiltros()){
				document.MantenimientoDuplicadosForm.modo.value = "buscar";
				document.MantenimientoDuplicadosForm.target="resultado";
			 	document.MantenimientoDuplicadosForm.submit();
			}else{
				fin();
			}
		}		

		function limpiar()
		{
			document.MantenimientoDuplicadosForm.reset();
			//ChequearCriterios();
		}	
		function refrescarLocal(){
			buscar();
		}
		function comprobarFiltros(){
			var error=false;
			var msg="";
			//aalg: modificado para controlar que siempre haya un check marcado al buscar
			if(document.MantenimientoDuplicadosForm.nifcif.value.length ==0 &&
					document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length ==0 &&
					document.MantenimientoDuplicadosForm.listadoInstitucion.value == "" &&
					document.MantenimientoDuplicadosForm.nombreText.value.length ==0 &&
					document.MantenimientoDuplicadosForm.apellido1Text.value.length ==0 &&
					document.MantenimientoDuplicadosForm.apellido2Text.value.length ==0){
				
				error = true;
				msg=msg+"Debe seleccionar alguna opción de busqueda";

			}
			else {
				if(document.MantenimientoDuplicadosForm.nombre.value.length>0&&
				   document.MantenimientoDuplicadosForm.nombre.value.length<3){
					error=true;
				   	msg=msg+"El campo Nombre es demasiado corto\n";
				}
				if(document.MantenimientoDuplicadosForm.apellido1.value.length>0&&
				   document.MantenimientoDuplicadosForm.apellido1.value.length<3){	
					error=true;
					msg=msg+"El campo Apellido 1 es demasiado corto\n";
				}		
				if(document.MantenimientoDuplicadosForm.apellido2.value.length>0&&
				   document.MantenimientoDuplicadosForm.apellido2.value.length<3){
					error=true;
					msg=msg+"El campo Apellido 2 es demasiado corto\n";
				}
				msg= msg + "Si no se rellenan al menos 3 caracteres la consulta devolverá demasiados resultados.\nIntente afinar la búsqueda";
			}
			if(error){
				alert(msg);
				return false;
			}else{
				return true;
			}
		}
		
		function inicio(){
			
			<% if (request.getParameter("buscar")!=null && request.getParameter("buscar").equals("true")) {%>
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			<% } %>
			
		}
		function presionarNif(){
			alertStop("hola");
			if(document.MantenimientoDuplicadosForm.nifcif.value.length >0 ){
				//Deshabilitamso los demás elementos del filtro	
				document.getElementById('numeroColegiadoText').readOnly = true;
				document.getElementById('nombreText').readOnly = true;
				document.getElementById('apellido1Text').readOnly = true;
				document.getElementById('apellido2Text').readOnly = true;
				document.MantenimientoDuplicadosForm.idInstitucion.disabled=true;
				document.MantenimientoDuplicadosForm.chkIdentificador.value="1";
				
				
			}else{
				//Habilitamos los demás elementos del filtro
				if(document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length ==0 && document.MantenimientoDuplicadosForm.listadoInstitucion.value == "" && (document.MantenimientoDuplicadosForm.nombreText.value.length ==0
						&& document.MantenimientoDuplicadosForm.apellido1Text.value.length ==0 && document.MantenimientoDuplicadosForm.apellido2Text.value.length ==0)){
					document.getElementById('numeroColegiadoText').readOnly = false;
					document.getElementById('nombreText').readOnly = false;
					document.getElementById('apellido1Text').readOnly = false;
					document.getElementById('apellido2Text').readOnly = false;
					document.MantenimientoDuplicadosForm.idInstitucion.disabled=false;
					document.MantenimientoDuplicadosForm.chkIdentificador.value="0";
				}
			}
		}
		
		function presionarNumeroColegiado(){
		
			if(document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length >0 || document.MantenimientoDuplicadosForm.listadoInstitucion.value != ""){
				
				document.getElementById('nifcif').readOnly = true;
				document.getElementById('nombreText').readOnly = true;
				document.getElementById('apellido1Text').readOnly = true;
				document.getElementById('apellido2Text').readOnly = true;
				document.getElementById('idInstitucion').disabled = false;
				document.MantenimientoDuplicadosForm.chkNumColegiado.value="1";
				onClickChkNumColegiado();
			}else{
				if(document.MantenimientoDuplicadosForm.nifcif.value.length ==0 && (document.MantenimientoDuplicadosForm.nombreText.value.length ==0
						&& document.MantenimientoDuplicadosForm.apellido1Text.value.length ==0 && document.MantenimientoDuplicadosForm.apellido2Text.value.length ==0)){
				document.getElementById('nifcif').readOnly = false;
				document.getElementById('nombreText').readOnly = false;
				document.getElementById('apellido1Text').readOnly = false;
				document.getElementById('apellido2Text').readOnly = false;
				document.MantenimientoDuplicadosForm.chkNumColegiado.value="0";
				onClickChkNumColegiado();
			}
		 }
		}
		
		function presionarNombreApellidos(){
			
			if(document.MantenimientoDuplicadosForm.nombreText.value.length >0 ||
					document.MantenimientoDuplicadosForm.apellido1Text.value.length >0 ||
					document.MantenimientoDuplicadosForm.apellido2Text.value.length >0){
					document.getElementById('nifcif').readOnly = true;
				document.getElementById('numeroColegiadoText').readOnly = true;
				document.getElementById('idInstitucion').readOnly = true;
				document.getElementById('idInstitucion').disabled = true;
			
				if(document.MantenimientoDuplicadosForm.nombreText.value.length >0 && (document.MantenimientoDuplicadosForm.apellido1Text.value.length >0 ||
						document.MantenimientoDuplicadosForm.apellido2Text.value.length >0)){
					document.MantenimientoDuplicadosForm.chkNombreApellidos.value="1";	
				}else{
					document.MantenimientoDuplicadosForm.chkNombreApellidos.value="0";	
				}
				if((document.MantenimientoDuplicadosForm.apellido1Text.value.length >0 ||
						document.MantenimientoDuplicadosForm.apellido2Text.value.length >0)){
					document.MantenimientoDuplicadosForm.chkApellidos.value="1";	
				}else{
					document.MantenimientoDuplicadosForm.chkApellidos.value="0";	
				}
			}else{
				if(document.MantenimientoDuplicadosForm.nifcif.value.length ==0 && document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length ==0 && document.MantenimientoDuplicadosForm.listadoInstitucion.value == "" ){
						document.getElementById('nifcif').readOnly = false;
						document.getElementById('numeroColegiadoText').readOnly = false;
						document.getElementById('idInstitucion').readOnly = false;
						document.getElementById('idInstitucion').disabled = false;
						
						document.MantenimientoDuplicadosForm.chkNombreApellidos.value="0";
						document.MantenimientoDuplicadosForm.chkApellidos.value="0";
				}
			}
		}
		
		function recargarCamposHabilitados (){
			if(document.MantenimientoDuplicadosForm.nifcif.value.length ==0 &&
					document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length ==0 &&
					document.MantenimientoDuplicadosForm.listadoInstitucion.value == "" &&
					document.MantenimientoDuplicadosForm.nombreText.value.length ==0 &&
					document.MantenimientoDuplicadosForm.apellido1Text.value.length ==0 &&
					document.MantenimientoDuplicadosForm.apellido2Text.value.length ==0){
				
					document.getElementById('nifcif').readOnly = false;
					document.getElementById('numeroColegiadoText').readOnly = false;
					document.getElementById('nombreText').readOnly = false;
					document.getElementById('apellido1Text').readOnly = false;
					document.getElementById('apellido2Text').readOnly = false;
					document.getElementById('idInstitucion').disabled = false;
			}else{
				if(document.MantenimientoDuplicadosForm.nifcif.value.length >0 ){
					//Deshabilitamso los demás elementos del filtro	
					document.getElementById('nifcif').focus();
					document.getElementById('numeroColegiadoText').readOnly = true;
					document.getElementById('nombreText').readOnly = true;
					document.getElementById('apellido1Text').readOnly = true;
					document.getElementById('apellido2Text').readOnly = true;
					document.getElementById('idInstitucion').disabled = true;
					
					document.MantenimientoDuplicadosForm.chkIdentificador.value="1";
				}
				if(document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length >0 || document.MantenimientoDuplicadosForm.listadoInstitucion.value != ""){
					
					
					document.getElementById('numeroColegiadoText').focus();
					document.getElementById('nifcif').readOnly = true;
					document.getElementById('nombreText').readOnly = true;
					document.getElementById('apellido1Text').readOnly = true;
					document.getElementById('apellido2Text').readOnly = true;
					document.getElementById('idInstitucion').disabled = false;
					
					document.MantenimientoDuplicadosForm.chkNumColegiado.value="1";
					onClickChkNumColegiado();
				}
				if(document.MantenimientoDuplicadosForm.nombreText.value.length >0 ||
						document.MantenimientoDuplicadosForm.apellido1Text.value.length >0 ||
						document.MantenimientoDuplicadosForm.apellido2Text.value.length >0){
				
					document.getElementById('nifcif').readOnly = true;
					document.getElementById('numeroColegiadoText').readOnly = true;
					document.getElementById('idInstitucion').disabled = true;
					document.getElementById('nombreText').focus();
					
					if(document.MantenimientoDuplicadosForm.nombreText.value.length >0 && (document.MantenimientoDuplicadosForm.apellido1Text.value.length >0 ||
							document.MantenimientoDuplicadosForm.apellido2Text.value.length >0)){
						document.MantenimientoDuplicadosForm.chkNombreApellidos.value="1";	
					}else{
						document.MantenimientoDuplicadosForm.chkNombreApellidos.value="0";	
					}
					if((document.MantenimientoDuplicadosForm.apellido1Text.value.length >0 ||
							document.MantenimientoDuplicadosForm.apellido2Text.value.length >0)){
						document.MantenimientoDuplicadosForm.chkApellidos.value="1";	
					}else{
						document.MantenimientoDuplicadosForm.chkApellidos.value="0";	
					}
				}
				
			}
		}
		</script>
		<siga:Titulo 
			titulo="censo.busquedaDuplicados.titulo" 
			localizacion="censo.busquedaDuplicados.localizacion"/>
</head>

<body onload="inicio();ajusteAlto('resultado');recargarCamposHabilitados();">



<html:form action="/CEN_MantenimientoDuplicados.do?noReset=true" method="POST" target="mainWorkArea"  >
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="chkNombreApellidos" id="chkNombreApellidos" value="0">
	<input type="hidden" name="chkApellidos" id="chkApellidos" value="0">
	<input type="hidden" name="chkIdentificador" id="chkIdentificador" value="0">
	<input type="hidden" name="chkNumColegiado" id="chkNumColegiado" value="0">
	<input type="hidden" name="tipoConexion" id="tipoConexion" value="intersect">
	<input type="hidden" name="agruparColegiaciones" id="agruparColegiaciones" value="s">
	<input type="hidden" name="valoresCheck" id="valoresCheck" value="0">
	
	
	<table  class="tablaCentralCampos"  align="center"><tr><td>
		<siga:ConjCampos leyenda="censo.busquedaDuplicados.patron.cabecera">
			<table class="tablaCampos" align="center">		
			<tr></tr>
			<tr>
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.nif"/> </td>
				<td> <html:text styleId="nifcif" name="MantenimientoDuplicadosForm" property="nifcif" size="15" styleClass="box" onchange="presionarNif()"  ></html:text> </td>
				
				<td class="labelText" width="100px">
					<siga:Idioma key="censo.busquedaDuplicados.coincidencias.ordenacion"/>
				</td>
				<td>
					<html:select name="MantenimientoDuplicadosForm" styleId="campoOrdenacion" property="campoOrdenacion" styleClass="boxCombo">
						<html:option value="apellidos" key="gratuita.turnos.literal.apellidosSolo"></html:option>
						<html:option value="nif" key="censo.busquedaClientesAvanzada.literal.nif"></html:option>
					</html:select>
					<html:select name="MantenimientoDuplicadosForm" property="sentidoOrdenacion" styleClass="boxCombo">
						<html:option value="asc" key="orden.literal.ascendente"></html:option>						
						<html:option value="desc" key="orden.literal.descendente"></html:option>
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.institucion"/> </td>
				<td>
					<html:select styleId="listadoInstitucion" property="idInstitucion" styleClass="boxCombo" onchange="presionarNumeroColegiado()">
							<html:option value="">&nbsp;</html:option>
							<c:forEach items="${listadoInstituciones}" var="inst">
								<html:option value="${inst.idInstitucion}">${inst.abreviatura}</html:option>
							</c:forEach>
					</html:select>
				</td>		
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.numeroColegiado"/> </td>
				<td> <html:text styleId="numeroColegiadoText" name="MantenimientoDuplicadosForm" property="numeroColegiado" size="20" styleClass="box" onchange="presionarNumeroColegiado()"></html:text> </td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.nombre"/></td>				
				<td><html:text styleId="nombreText" name="MantenimientoDuplicadosForm" property="nombre" size="25" styleClass="box" onchange="presionarNombreApellidos()" ></html:text></td>
				
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.apellido1"/></td>
				<td><html:text styleId="apellido1Text" name="MantenimientoDuplicadosForm" property="apellido1" size="35" styleClass="box" onchange="presionarNombreApellidos()" ></html:text></td>
			
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.apellido2"/></td>
				<td><html:text styleId="apellido2Text" name="MantenimientoDuplicadosForm" property="apellido2" size="35" styleClass="box" onchange="presionarNombreApellidos()" ></html:text></td>
			</tr>
			</table>
		</siga:ConjCampos>
	</td></tr></table>
	<siga:ConjBotonesBusqueda botones="B,L"/>

	</html:form>  
   <iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
			id="resultado"
			name="resultado" 
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0"
			class="frameGeneral"/>



	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"/>
	
</body>
</html>