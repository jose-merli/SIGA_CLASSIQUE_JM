<!-- insertarDocumentacionSOJ.jsp -->
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	Hashtable miHash = (Hashtable)ses.getAttribute("SOJ");
	ses.removeAttribute("SOJ");
	
	String anio= "", numero="", idTipoSOJ = "" ;	
	try {
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoSOJ = miHash.get("IDTIPOSOJ").toString();
	}
	catch(Exception e){};
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<html:javascript formName="DatosDocumentacionSOJForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
		<siga:Idioma key="pestana.justiciagratuitasoj.documentacion"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/JGR_PestanaSOJDocumentacion" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idTipoSOJ" value ="<%=idTipoSOJ%>"/>
	<html:hidden property = "anio" value ="<%=anio%>"/>
	<html:hidden property = "numero" value ="<%=numero%>"/>

	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="pestana.justiciagratuitaejg.documentacion">
	<table class="tablaCampos" align="center" width="100%" border="0">
	
	<!-- FILA -->
	<tr>
	
	<td class="labelText" >
		<siga:Idioma key="gratuita.operarEJG.literal.fechaLimitePresentacion"/>&nbsp;(*)
	</td>
	<td >
		<html:text name="DatosDocumentacionSOJForm" property="fechaLimite" size="10" styleClass="box" value="" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaLimite);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fechaPresentacion"/>
	</td>
	<td >
		<html:text name="DatosDocumentacionSOJForm" property="fechaEntrega" size="10" styleClass="box" value="" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaEntrega);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.documentacionSOJ.regentrada"/>
	</td>
	<td >
		<html:text name="DatosDocumentacionSOJForm" property="regEntrada" size="20" maxlength="30" styleClass="box" value="" readonly="false"></html:text>&nbsp;&nbsp;
	</td>
	</tr>	
	<tr>
	<td class="labelText" >
		<siga:Idioma key="gratuita.documentacionSOJ.regsalida"/>
	</td>
	<td >
		<html:text name="DatosDocumentacionSOJForm" property="regSalida" size="20" maxlength="30" styleClass="box" value="" readonly="false"></html:text>&nbsp;&nbsp;
	</td>
    </tr>	
	
	
	
		
	<tr>
	<td class="labelText">
		<siga:Idioma key="pestana.justiciagratuitaejg.documentacion"/>
	</td>
	</tr>
	<tr>
	<td class="labelText" colspan="4">
		<textarea name="documentacion" rows="4" cols="300"  style="width:450" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" class="box"></textarea>
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
	
		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar y Cerrar
		function accionGuardarCerrar() 
		{
			sub();
			if (validateDatosDocumentacionSOJForm(document.forms[0])){
		       	document.forms[0].submit();
				window.top.returnValue="MODIFICADO";
			}else{
				fin();
				return false;
			}
		}		
		
		//Asociada al boton Cerrar
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