<!-- insertarCalendario.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsCalendarioLaboralBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String dato[] = {(String)usr.getLocation()};	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	
	<html:javascript formName="CalendarioLaboralForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
		<siga:Idioma key="gratuita.operarCalendario.literal.insertarCalendario"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">
	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/CalendarioLaboralAction.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "identificativo" value = ""/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>

	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="gratuita.calendario.literal.calendario">
	<table class="tablaCampos" align="center">
	
	<!-- FILA -->
	<tr>
	
	<td class="labelText">
		<siga:Idioma key="gratuita.listadoCalendario.literal.fecha"/>&nbsp;(*)
	</td>
	<td>
		<html:text name="CalendarioLaboralForm" property="fecha" size="10" styleClass="box" value="" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fecha);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
	</td>
	</tr>
	
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.listadoCalendario.literal.fiestaLocalPartidoJudicial"/>
	</td>				
	<td>
		<siga:ComboBD nombre="idPartido" tipo="partidoJudicial" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>"/>
	</td>	
	</tr>
	<tr>
	<td class="labelText">
		&nbsp;
	</td>				
	<td class="labelTextValue">
		<siga:Idioma key="gratuita.listadoCalendario.literal.avisoPartido"/>		
	</td>		
	</tr>
		
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.listadoCalendario.literal.nombre"/>&nbsp;
	</td>				
	<td>
		<html:text name="CalendarioLaboralForm" property="nombreFiesta" size="30" styleClass="box" value=""></html:text>
	</td>		
	</tr>

	</table>

	</siga:ConjCampos>
	
	</td>
	</tr>
	</html:form>
	</table>
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
	<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{
			sub();	
			if (validateCalendarioLaboralForm(document.CalendarioLaboralForm)){
	        	document.forms[0].submit();	        
				window.top.returnValue="MODIFICADO";
			}else{
			
				fin();
				return false;
			}
		}		
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");			
		}	
		</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>