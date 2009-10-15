<!-- ejecucionSancion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user = ((UsrBean)ses.getAttribute(("USRBEAN")));
		
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
</head>

<body>

	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/EXP_Auditoria_EjecucionSancion.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.acciones">

		<table class="tablaCampos" align="center">
		
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.bajaturno"/>
			</td>				
			<td>		
				<html:checkbox name="EjecucionSancionForm" property="bajaTurno"></html:checkbox>
			</td>			
		</tr>
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.bajacolegial"/>
			</td>				
			<td>		
				<html:checkbox name="EjecucionSancionForm" property="bajaColegial"></html:checkbox>
			</td>			
		</tr>
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.bajaejercicio"/>
			</td>				
			<td>		
				<html:checkbox name="EjecucionSancionForm" property="bajaEjercicio"></html:checkbox>
			</td>			
		</tr>
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.inhabilitacionperpetua"/>
			</td>				
			<td>		
				<html:checkbox name="EjecucionSancionForm" property="inhabilitacion"></html:checkbox>
			</td>			
		</tr>
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.suspension"/>
			</td>				
			<td>		
				<html:checkbox name="EjecucionSancionForm" property="suspension"></html:checkbox>
			</td>			
		</tr>
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.motivo"/><br>
			</td>	
			<td>		
				<html:text name="EjecucionSancionForm" property="motivo" styleClass="box" size="50" maxlength="50"></html:text>
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
			document.forms[0].modo.value="modificar";
			document.forms[0].submit();
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