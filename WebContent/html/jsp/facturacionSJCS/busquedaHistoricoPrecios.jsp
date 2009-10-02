<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();	
	String facturacionParams[] = new String[2];
	facturacionParams[0] = usr.getLocation();
   	facturacionParams[1] = String.valueOf(ClsConstants.ESTADO_FACTURACION_LISTA_CONSEJO);
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<html:javascript formName="MantenimientoHistoricoPreciosForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="factSJCS.historicoPrecios.cabecera" 
		localizacion="factSJCS.historicoPrecios.ruta"/>	
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onLoad="ajusteAlto('resultado');">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<siga:ConjCampos leyenda="factSJCS.historicoPrecios.leyenda">
	<table class="tablaCentralCampos" align="center">

	<html:form action="/FCS_HistoricoPrecios.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = "inicio"/>
  		
	<tr>
	<td class="labelText">
		<siga:Idioma key="factSJCS.historicoPrecios.literal.nombreFacturacion"/>
	</td>
	<td class="labelText">
		<siga:ComboBD nombre="idFacturacion" tipo="cmb_facturacionHistoricoPrecios" parametro="<%=facturacionParams%>" clase="boxCombo" obligatorio="true"/> 
	</td>
	
	<td class="labelText">
		<siga:Idioma key="factSJCS.historicoPrecios.literal.hitoFacturable"/>
	</td>
	<td class="labelText">	
		<siga:ComboBD nombre="idHito" tipo="cmbHitoFacturable" clase="boxCombo" obligatorio="false"/> 
	</td>
	</tr>

	<tr>
	<td colspan="4">
		<p class="nonEditRed" style="text-align:left">(<siga:Idioma key="messages.factSJCS.informacionHistoricoPrecios"/>)</p>
	</td>
	</tr>	
	</html:form>
	</table>
	</siga:ConjCampos>
	
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->		
	<siga:ConjBotonesBusqueda botones="B"  titulo="factSJCS.historicoPrecios.cabecera" />	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton buscar -->
		function buscar() 
		{
			if (validateMantenimientoHistoricoPreciosForm(document.MantenimientoHistoricoPreciosForm)){
				document.forms[0].modo.value = "buscarPor";
				document.forms[0].submit();
			}
		}		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->	
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>