<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String nombreInstitucion = (String)request.getAttribute("nombreInstitucion");
	String idFacturacion = (String)request.getAttribute("idFacturacion");
	String prevision = (String)request.getAttribute("prevision");
	if (prevision==null || !prevision.equals("S")) {
		prevision = "N";
	}
	
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<html:javascript formName="DatosCriteriosFacturacionForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.datosFacturacion.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
		</td>
	</tr>
	</table>



	<html:javascript formName="DatosGeneralesFacturacionForm" staticJavascript="false" />
	<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "prevision" value = "<%=prevision%>"/>
	<html:hidden property = "idFacturacion" value = "<%=idFacturacion%>"/>
	

	<siga:ConjCampos leyenda="factSJCS.datosGenerales.leyenda">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="factSJCS.datosFacturacion.literal.hitos"/>&nbsp;(*)
		</td>				
		<td><%	String[] dato1 = {"G"};%>
			<siga:ComboBD nombre = "hito" tipo="cmbHitoGeneral" clase="boxCombo" obligatorio="true"/>						
		</td>
		</tr>				

		<!-- FILA -->
		<tr>

		<!-- -->
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosFacturacion.literal.gruposFacturacion"/>&nbsp;(*)
		</td>
		<td><%	String[] dato2 = {usr.getLocation()};%>
			<siga:ComboBD nombre = "grupoF" tipo="grupoFacturacion" clase="boxCombo" obligatorio="true" parametro="<%=dato2%>"/>
		</td>
		</tr>

	
		</table>

	</siga:ConjCampos>



	</html:form>
	

	<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
  

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();		
		    if (document.forms[0].hito.selectedIndex < 1) {
				alert('<siga:Idioma key="factSJCS.datosFacturacion.literal.hitos"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
				fin();
				return false;
			}else{
				if (document.forms[0].grupoF.selectedIndex < 1) {
					alert('<siga:Idioma key="factSJCS.datosFacturacion.literal.gruposFacturacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
					fin();
					return false;
				}
				else{
					document.forms[0].modo.value="insertarCriterio";
					document.forms[0].submit();
					window.returnValue="MODIFICADO";			
				}
			}
		}

		function accionGuardar() 
		{	

		    document.forms[0].modo.value="insertarCriterio";
			document.forms[0].submit();
			window.returnValue="MODIFICADO";			
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
