<!-- configurarSalida.jsp -->
<!-- VERSIONES:
	 raul.ggonzalez 22-04-2005 creacion
-->
	 
 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPrevisionesForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.previsiones.titulo2"/>
		</td>
	</tr>
	</table>


	<div id="camposRegistro" class="posicionModalPeque" align="center">
	<table  class="tablaCentralCamposPeque"  align="center">
	<!-- Formulario de la lista de detalle multiregistro -->
	<form name="formulario" action="" method="POST" target="submitArea">
	<tr>
	<td  height="110">

			<siga:ConjCampos leyenda="factSJCS.previsiones.literal.salida">
			<table class="tablaCampos" align="center">	
				<tr>		
					<td class="labelText" >
  					  <input type="radio" name="ficheros" value="uno" checked> 
					  <siga:Idioma key="factSJCS.previsiones.literal.salida1"/>&nbsp
					</td>
				</tr>
				<tr>		
					<td class="labelText" >
					  <input type="radio" name="ficheros" value="varios"> 
					  <siga:Idioma key="factSJCS.previsiones.literal.salida2"/>&nbsp
					</td>
				</tr>
			</table>
			</siga:ConjCampos>
	</td>				
	</tr>
	</form>
	</table>


	<div id="botonesAccion" style="position:absolute;top=180;left:0">	
		<siga:ConjBotonesAccion botones="Y,C" clase="botonesDetalle" modal="P"/>
	</div>
		
	<!-- FIN: LISTA DE VALORES -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			var objeto = document.formulario.ficheros[0];
			if (objeto.checked) {
				top.cierraConParametros("uno");
			} else {
				top.cierraConParametros("varios");
			}
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.close();
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
</div>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
