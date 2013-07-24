<!DOCTYPE html>
<html>
<head>
<!-- busqTipoNuevoExp.jsp -->
<!-- CABECERA JSP -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->


<%@ page import="com.atos.utils.UsrBean"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idinstitucion = user.getLocation();

    String datoTipoExp[] = new String[2];
	
	
    Object[] aPerfiles = new Object[2];
	aPerfiles[0] = user.getProfile();
	aPerfiles[1] = user.getProfile();
	datoTipoExp[0] = idinstitucion;
	datoTipoExp[1] = idinstitucion;
		
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="NuevoExpedienteForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
			
</head>

<body>

	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.nuevoExpediente.cabecera"/>
			</td>
		</tr>
	</table>
	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/EXP_NuevoExpediente.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.tipo">

		<table class="tablaCampos" align="center">
		<!-- FILA -->
		<tr>				
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.tipo"/>&nbsp;(*)
			</td>				
			<td>		
				<siga:ComboBD nombre = "comboTipoExpediente" tipo="cmbTipoExpedienteLocaloGeneralPermisosNuevo" clase="boxCombo" obligatorio="true" parametro="<%=datoTipoExp%>" parametrosIn="<%=aPerfiles%>"/>						
			</td>			
		</tr>				
		</table>

	</siga:ConjCampos>


	</td>
</tr>

</html:form>

</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			sub();	
			if (validateNuevoExpedienteForm(document.NuevoExpedienteForm)) {
				document.forms[0].modo.value="modificar";
				document.forms[0].submit();
			}else{
				fin();
			}
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("VACIO");
		}		
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>