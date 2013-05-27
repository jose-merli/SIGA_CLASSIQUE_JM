<!-- operarDatosDocumentacion.jsp -->
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
	Hashtable miHash = (Hashtable)ses.getAttribute("DATABACKUP");
	String accion = (String)request.getAttribute("accionModo");
		
	String anio= "", numero="", idTipoSOJ = "", fechaEntrega="", fechaLimite="", documentacion="", idDocumentacion="", regEntrada="", regSalida="" ;	
	try {
		idDocumentacion = miHash.get("IDDOCUMENTACION").toString();
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoSOJ = miHash.get("IDTIPOSOJ").toString();
		documentacion = miHash.get("DOCUMENTACION").toString();
		fechaEntrega = GstDate.getFormatedDateShort("",miHash.get("FECHAENTREGA").toString());
		fechaLimite = GstDate.getFormatedDateShort("",miHash.get("FECHALIMITE").toString());
		regEntrada = miHash.get("REGENTRADA").toString();
		regSalida = miHash.get("REGSALIDA").toString();
	}
	catch(Exception e){};
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="DatosDocumentacionSOJForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo"  cellspacing="0"  border="0">
	<tr>
	<td  class="titulosPeq">	
		<siga:Idioma key="pestana.justiciagratuitasoj.documentacion"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque"  width="100%" border="0">	
	
	<html:form action="/JGR_PestanaSOJDocumentacion" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idTipoSOJ" value ="<%=idTipoSOJ%>"/>
	<html:hidden property = "anio" value ="<%=anio%>"/>
	<html:hidden property = "numero" value ="<%=numero%>"/>
	<html:hidden property = "idDocumentacion" value ="<%=idDocumentacion%>"/>

	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="pestana.justiciagratuitaejg.documentacion">
	<table class="tablaCampos"  width="100%" border="0">
	
	<!-- FILA -->
	<tr>
	
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fechaLimitePresentacion"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<html:text name="DatosDocumentacionSOJForm" property="fechaLimite" size="10" styleClass="boxConsulta" value="<%=fechaLimite%>" readonly="true"></html:text>
		<%} else {%>
			<siga:Fecha nombreCampo="fechaLimite" valorInicial="<%=fechaLimite%>"></siga:Fecha>
		<%}%>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fechaPresentacion"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<html:text name="DatosDocumentacionSOJForm" property="fechaEntrega" size="10" styleClass="boxConsulta" value="<%=fechaEntrega%>" readonly="true"></html:text>
		<%} else {%>
			<siga:Fecha nombreCampo="fechaEntrega" valorInicial="<%=fechaEntrega%>"></siga:Fecha>
		<%}%>
	</td>
		</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.documentacionSOJ.regentrada"/>
	</td>
	<td>
	  <%if (accion.equalsIgnoreCase("ver")) {%>
		<html:text name="DatosDocumentacionSOJForm" property="regEntrada" size="20" styleClass="boxConsulta" value="<%=regEntrada%>" readonly="false"></html:text>
	  <%} else {%>
		<html:text name="DatosDocumentacionSOJForm" property="regEntrada" size="20" styleClass="box" value="<%=regEntrada%>" readonly="false"></html:text>&nbsp;&nbsp;
 	  <%}%> 
	</td>
	</tr>	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.documentacionSOJ.regsalida"/>
	</td>
	<td>
	 <%if (accion.equalsIgnoreCase("ver")) {%>
		<html:text name="DatosDocumentacionSOJForm" property="regSalida" size="20" styleClass="boxConsulta" value="<%=regSalida%>" readonly="false"></html:text>
	 <%} else {%>	
	    <html:text name="DatosDocumentacionSOJForm" property="regSalida" size="20" styleClass="box" value="<%=regSalida%>" readonly="false"></html:text>&nbsp;&nbsp;
	 <%}%> 	
	</td>
	</tr>	
	<tr>
	<td class="labelText">
		<siga:Idioma key="pestana.justiciagratuitaejg.documentacion"/>
	</td>
	</tr>
	<tr>
	<td class="labelText" colspan="4">
	<%if (accion.equalsIgnoreCase("ver")) {%>
		<textarea name="documentacion" rows="4" cols="60" style="width:450" class="boxConsulta" readonly="true"><%=documentacion%></textarea>
	<%} else {%>
		<textarea name="documentacion" rows="4" cols="60" style="width:450" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" class="box"><%=documentacion%></textarea>
	<%}%>
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
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  modo="<%=accion%>"/>
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
	//Asociada al boton Guardar y Cerrar -->
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
		
		//Asociada al boton Cerrar -->
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