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

 
<html>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String titulo = "Mantenimiento de Duplicados";
	String localizacion = "Censo";
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
	
	
	/**************/
%>

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
	


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
				document.MantenimientoDuplicadosForm.target="mainWorkArea";
			 	document.MantenimientoDuplicadosForm.submit();
			}else{
				fin();
			}
		}		

		function limpiar()
		{
			document.MantenimientoDuplicadosForm.reset();
		}	
		function refrescarLocal(){
			buscar();
		}
		function comprobarFiltros(){
			var error=false;
			var msg="";
			if(!(document.MantenimientoDuplicadosForm.chkApellidos.checked ||
				document.MantenimientoDuplicadosForm.chkNombreApellidos.checked||
				document.MantenimientoDuplicadosForm.chkIdentificador.checked||
				document.MantenimientoDuplicadosForm.chkNumColegiado.checked)){
				
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
			}
			if(error){
				msg= msg + "Si no se rellenan al menos 3 caracteres la consulta devolverá demasiados resultados.\nIntente afinar la búsqueda";
				alert(msg);
				return false;
			}else{
				return true;
			}
		}
		

		</script>
		<siga:Titulo 
			titulo="censo.busquedaDuplicados.titulo" 
			localizacion="censo.busquedaDuplicados.localizacion"/>
</head>

<body onload="ajusteAlto('resultado');">


	

<html:form action="/CEN_MantenimientoDuplicados.do?noReset=true" method="POST" target="mainWorkArea" >
	<input type="hidden" name="modo" value="">
	
	<table  class="tablaCentralCampos"  align="center"><tr><td>
	
		<siga:ConjCampos leyenda="censo.busquedaDuplicados.coincidencias.cabecera">
			<table class="tablaCampos" align="center" width="100%">
				<tr>
					<td colspan="3" class="labelText">
						<siga:Idioma key="censo.busquedaDuplicados.coincidencias.explicacion"/>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<html:checkbox styleid="chkNombreApellidos" name="MantenimientoDuplicadosForm" property="chkNombreApellidos" />
						<label for="chkNombreApellidos"><siga:Idioma key="censo.busquedaDuplicados.coincidencias.nombreApellidos"/></label>
					</td>
					<td class="labelText">
						<html:checkbox styleid="chkApellidos" name="MantenimientoDuplicadosForm" property="chkApellidos" /> 
						<label for="chkApellidos"><siga:Idioma key="censo.busquedaDuplicados.coincidencias.apellidos"/></label>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<html:checkbox styleid="chkIdentificador" name="MantenimientoDuplicadosForm" property="chkIdentificador" /> 
						<label for="chkIdentificador"><siga:Idioma key="censo.busquedaDuplicados.coincidencias.nifCif"/></label>
					</td>
					<td class="labelText">
						<html:checkbox styleid="chkNumColegiado" name="MantenimientoDuplicadosForm" property="chkNumColegiado" /> 
						<label for="chkNumColegiado"><siga:Idioma key="censo.busquedaDuplicados.coincidencias.numeroColegiado"/></label>
					</td>
				</tr>
				<tr>
					<td class="labelText" style="align:right">
						<siga:Idioma key="censo.busquedaDuplicados.coincidencias.ordenacion"/>
						<html:select name="MantenimientoDuplicadosForm" property="campoOrdenacion" styleClass="boxCombo">
							<html:option value="apellidos" key="gratuita.turnos.literal.apellidosSolo"></html:option>
							<html:option value="nif" key="censo.busquedaClientesAvanzada.literal.nif"></html:option>
						</html:select>
						<html:select name="MantenimientoDuplicadosForm" property="sentidoOrdenacion" styleClass="boxCombo">
							<html:option value="asc" key="orden.literal.ascendente"></html:option>						
							<html:option value="desc" key="orden.literal.descendente"></html:option>
						</html:select>
					</td>
					
					<td class="labelText" style="align:right">
						<html:select name="MantenimientoDuplicadosForm" property="tipoConexion" styleClass="boxCombo">
							<html:option value="intersect"><siga:Idioma key="censo.busquedaDuplicados.coincidencias.criterios.todos"/></html:option>
							<html:option value="union"><siga:Idioma key="censo.busquedaDuplicados.coincidencias.criterios.alguno"/></html:option>
						</html:select>
					</td>
					
					<td class="labelText" style="align:right;display:none">
						<html:select name="MantenimientoDuplicadosForm" property="agruparColegiaciones" styleClass="boxCombo">
							<html:option value="s"><siga:Idioma key="Mostrar personas"/></html:option>
							<html:option value="n"><siga:Idioma key="Mostrar colegiaciones"/></html:option>
						</html:select>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	
		<siga:ConjCampos leyenda="censo.busquedaDuplicados.patron.cabecera">
			<table class="tablaCampos" align="center">
			<tr>
				<td colspan="6" class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.explicacion"/></td>
			</tr>
			<tr></tr>
			
			<tr>
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.nif"/> </td>
				<td> <html:text name="MantenimientoDuplicadosForm" property="nifcif" size="15" styleClass="box"></html:text> </td>
				
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.institucion"/> </td>
				<td> <siga:ComboBD nombre = "idInstitucion" tipo="cmbNombreColegiosTodos" parametro="<%=parametro %>" clase="boxCombo" /> </td>
				
				<td class="labelText" width="100px"> <siga:Idioma key="censo.busquedaDuplicados.patron.numeroColegiado"/> </td>
				<td> <html:text name="MantenimientoDuplicadosForm" property="numeroColegiado" size="20" styleClass="box"></html:text> </td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.nombre"/></td>				
				<td><html:text name="MantenimientoDuplicadosForm" property="nombre" size="25" styleClass="box"></html:text></td>
				
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.apellido1"/></td>
				<td><html:text name="MantenimientoDuplicadosForm" property="apellido1" size="35" styleClass="box"></html:text></td>
			
				<td class="labelText"><siga:Idioma key="censo.busquedaDuplicados.patron.apellido2"/></td>
				<td><html:text name="MantenimientoDuplicadosForm" property="apellido2" size="35" styleClass="box"></html:text></td>
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