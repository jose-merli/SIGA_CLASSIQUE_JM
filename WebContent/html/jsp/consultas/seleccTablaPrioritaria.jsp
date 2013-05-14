<!-- seleccTablaPrioritaria.jsp -->
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
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Vector tablas = (Vector)request.getAttribute("tablas");
		
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="NuevoExpedienteForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
			
</head>

<body>

		<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tablaprioritaria"/>
			</td>
		</tr>
	</table>	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">
	
	<tr>	
<% if (tablas!=null){%>				
	<td>


	<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.tablaprioritaria">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tablaprioritaria"/>
			</td>				
			<td>		
				<select name="tablaPrioritaria" id="tablaPrioritaria" class="boxCombo" />
<%
					for (int j=0; j<tablas.size(); j++){
						String myId = (String)((Vector)tablas.elementAt(j)).elementAt(0);
						String myDesc = (String)((Vector)tablas.elementAt(j)).elementAt(1);
%>
					<option value="<%=myId%>"><%=myDesc%></option>
<%
					}
%>
				</select>		
			</td>	
		</tr>				
		
		</table>

	</siga:ConjCampos>
	</td>
<%}else{%>	
	<td class="labelText">
		<siga:Idioma key="messages.consultas.error.TablaPrioritaria"/>
	</td>	
<%}%>
</tr>


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
			var select = document.getElementById("tablaPrioritaria");
			if (select==undefined){
				top.cierraConParametros("VACIO");
			}else{
			
			
			var a = new Array();
				a[0] = select.options[select.selectedIndex].text;
				a[1] = select.options[select.selectedIndex].value;
				top.cierraConParametros(a);
				
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


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>