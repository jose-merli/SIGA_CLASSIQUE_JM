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
	
	//Gestiona bot�n volver MD= Mantenimiento Duplicado
	request.getSession().setAttribute("CenBusquedaClientesTipo","MD");
	
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
		
		function preAccionBusqueda(){
			sub();
		}
		
		function postAccionBusqueda(){
			fin();
		}
		function buscar(){
			sub();
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
					document.MantenimientoDuplicadosForm.apellido1Text.value.length ==0){
				
				error = true;
				msg=msg+"Debe seleccionar alguna opci�n de busqueda";

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
				msg= msg + "Si no se rellenan al menos 3 caracteres la consulta devolver� demasiados resultados.\nIntente afinar la b�squeda";
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
			jQuery("#numeroColegiadoText").val("");
			jQuery("#nombreText").val("");
			jQuery("#apellido1Text").val("");
			jQuery('#listadoInstitucion option[value=""]').attr("selected","selected");
			
		}
		
		function presionarNumeroColegiado(){
			jQuery("#nifcif").val("");
			jQuery("#nombreText").val("");
			jQuery("#apellido1Text").val("");
			
		}
		
		function presionarNombreApellidos(){
			jQuery("#nifcif").val("");
			jQuery("#numeroColegiadoText").val("");
			jQuery('#listadoInstitucion option[value=""]').attr("selected","selected");
			
		}
		
		function recargarCamposHabilitados (){
			if(document.MantenimientoDuplicadosForm.nifcif.value.length ==0 &&
					document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length ==0 &&
					document.MantenimientoDuplicadosForm.listadoInstitucion.value == "" &&
					document.MantenimientoDuplicadosForm.nombreText.value.length ==0 &&
					document.MantenimientoDuplicadosForm.apellido1Text.value.length ==0){
				
					jQuery ("#nifcif").val("");
					jQuery ("#numeroColegiadoText").val("");
					jQuery ("#nombreText").val("");
					jQuery ("#apellido1Text").val("");
					jQuery('#listadoInstitucion option[value=""]').attr("selected","selected")
					
			}else{
				if(document.MantenimientoDuplicadosForm.nifcif.value.length >0 ){
					//Deshabilitamso los dem�s elementos del filtro	
					document.getElementById('nifcif').focus();
					
					jQuery("#numeroColegiadoText").val("");
					jQuery("#nombreText").val("");
					jQuery("#apellido1Text").val("");
					jQuery('#listadoInstitucion option[value=""]').attr("selected","selected");
					
					
				}
				if(document.MantenimientoDuplicadosForm.numeroColegiadoText.value.length >0 || document.MantenimientoDuplicadosForm.listadoInstitucion.value != ""){
					document.getElementById('numeroColegiadoText').focus();
					
					jQuery("#nifcif").val();
					jQuery("#nombreText").val();
					jQuery("#apellido1Text").val();
				}
				if(document.MantenimientoDuplicadosForm.nombreText.value.length >0 ||
						document.MantenimientoDuplicadosForm.apellido1Text.value.length >0){
					
					jQuery("#nifcif").val();
					jQuery("#numeroColegiadoText").val();
					jQuery('#listadoInstitucion option[value=""]').attr("selected","selected");
					document.getElementById('nombreText').focus();
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
	<input type="hidden" name="tipoConexion" id="tipoConexion" value="intersect">
	<input type="hidden" name="agruparColegiaciones" id="agruparColegiaciones" value="s">
	
	<table  class="tablaCentralCampos"  align="center"><tr><td>
		<siga:ConjCampos leyenda="censo.busquedaDuplicados.patron.cabecera">
		<table class="tablaCampos" align="center">		
			<tr><td colspan="3" class="labelText"> Elija uno de los tres patrones siguientes para empezar a buscar duplicados: </td></tr>
			<tr>
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.nif"/> </td>
				<td> <html:text styleId="nifcif" name="MantenimientoDuplicadosForm" property="nifcif" size="15" styleClass="box" onkeyup="presionarNif()"   ></html:text> </td>
				
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.institucion"/> </td>
				<td>
					<html:select styleId="listadoInstitucion" property="idInstitucion" styleClass="boxCombo" onchange="presionarNumeroColegiado()">
							<html:option value="">&nbsp;</html:option>
							<c:forEach items="${listadoInstituciones}" var="inst">
								<html:option value="${inst.idInstitucion}">${inst.abreviatura}</html:option>
							</c:forEach>
					</html:select>
				</td>		
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.nombre"/></td>				
				<td><html:text styleId="nombreText" name="MantenimientoDuplicadosForm" property="nombre" size="25" styleClass="box" onkeyup="presionarNombreApellidos()" ></html:text></td>
			</tr>
			<tr>
				<td class="labelText" width="100px">  </td>
				<td>  </td>
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.numeroColegiado"/> </td>
				<td> <html:text styleId="numeroColegiadoText" name="MantenimientoDuplicadosForm" property="numeroColegiado" size="20" styleClass="box" onkeyup="presionarNumeroColegiado()"></html:text> </td>
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.apellido1"/></td>
				<td><html:text styleId="apellido1Text" name="MantenimientoDuplicadosForm" property="apellido1" size="35" styleClass="box" onkeyup="presionarNombreApellidos()" ></html:text></td>
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