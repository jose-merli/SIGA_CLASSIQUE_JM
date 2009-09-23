<!-- altaTurno_2.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<% 	String app		= request.getContextPath();%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<html:javascript formName="busquedaClientesFiltrosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
</head>

<body onload="init();">
  <html:form action="JGR_BusquedaClientesFiltros.do" method="post" target="submitArea">
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="gratuita.editartelefonosGuardia.literal.telefonosGuardia"/>
			</td>
		</tr>
	</table>
	<table class="tablaCentralCamposPeque" align="center" border="0" height="200px">
		<tr>				
			<td>
				<siga:ConjCampos leyenda="gratuita.editartelefonosGuardia.literal.telefonosGuardia">
					<table class="tablaCampos" align="center">	
						<tr>
		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.telefono1"/>&nbsp(*)</td>				
		   					<td><html:text name="busquedaClientesFiltrosForm" property="telefono1"  maxlength="20" size="10" styleClass="box"></html:text></td>
		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.fax1"/>&nbsp</td>
		   					<td><html:text name="busquedaClientesFiltrosForm" property="fax1"  maxlength="20" size="10" styleClass="box"></html:text></td>
		 				</tr>
	 					<tr>
		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.telefono2"/>&nbsp</td>				
		   					<td><html:text name="busquedaClientesFiltrosForm" property="telefono2"  maxlength="20" size="10" styleClass="box"></html:text></td>	
		  				 	<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.fax2"/>&nbsp</td>				
		   					<td><html:text name="busquedaClientesFiltrosForm" property="fax2"  maxlength="20" size="10" styleClass="box"></html:text></td>
	 					</tr>
	 					<tr>
		   					<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.movil"/>&nbsp</td>				
		   					<td><html:text name="busquedaClientesFiltrosForm" property="movil"  maxlength="20" size="10" styleClass="box"></html:text></td>	
		  				</tr>
		   			</table>
				</siga:ConjCampos>
			</td>
		</tr>
		<tr>
			<td height="80px">&nbsp;</td>
		</tr>
	</table>	
	<siga:ConjBotonesAccion botones="C,Y" modal="P"/>	
	<html:hidden name="busquedaClientesFiltrosForm" property="modo"/>
	<html:hidden name="busquedaClientesFiltrosForm" property="idPersona" />
	<html:hidden name="busquedaClientesFiltrosForm" property="idDireccion" />
	<html:hidden name="busquedaClientesFiltrosForm" property="idInstitucion" />
	<html:hidden name="busquedaClientesFiltrosForm" property="sustituta" />
  </html:form>

	<!-- ******* BOTONES DE ACCIONES ****** -->

	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">
	
	function init(){
		//Guardamos los valores de los campos para poder comprobar si hubo cambios
		saveValuesOfFields(document.forms[0]);
		window.returnValue="";
	}
	
	function accionCerrar() {		
		window.close();
	}
		
	function accionGuardarCerrar() 	{		
	      document.busquedaClientesFiltrosForm.telefono1.value=eliminarBlancos(trim(document.busquedaClientesFiltrosForm.telefono1.value));
		  document.busquedaClientesFiltrosForm.telefono2.value=eliminarBlancos(trim(document.busquedaClientesFiltrosForm.telefono2.value));
		  document.busquedaClientesFiltrosForm.fax1.value=eliminarBlancos(trim(document.busquedaClientesFiltrosForm.fax1.value));
		  document.busquedaClientesFiltrosForm.fax2.value=eliminarBlancos(trim(document.busquedaClientesFiltrosForm.fax2.value));
		  document.busquedaClientesFiltrosForm.movil.value=eliminarBlancos(trim(document.busquedaClientesFiltrosForm.movil.value));
		if(validateBusquedaClientesFiltrosForm(document.busquedaClientesFiltrosForm)){
			if(checkElementsForChanges(document.forms[0])){ //Si hubo cambios los envio
				document.forms[0].submit();
			}else{											//Si no hubo cambios digo que ok y cierro
				//no se si habria que mostrar mensaje
				window.returnValue="MODIFICADO";
				window.close();
			}
		}
	}
		
	function validaTelefono1() 	{	
		var ok=true;	
		with(document.forms[0]){
			if(trim(telefono1.value) ==""){
				var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.telefono1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert(mensaje);
				ok=false;
			}
		}
		return ok;
	}

	</script>

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
	</body>
</html>
