<!-- operarDocumentacionEJG.jsp -->
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
<%@ page import="com.siga.beans.ScsDocumentacionEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	Hashtable miHash = (Hashtable)ses.getAttribute("DATABACKUP");
	String accion = (String)request.getAttribute("accionModo");
		
	String anio= "", numero="", idTipoEJG = "", fechaEntrega="", fechaLimite="", regEntrada="", regSalida="", documentacion="", idDocumentacion="";	
	String presentador="", tipodocu="",documento="",nombredocumento="";
	try {
		idDocumentacion = miHash.get("IDDOCUMENTACION").toString();
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
		documentacion = miHash.get("DOCUMENTACION").toString();
		fechaEntrega = GstDate.getFormatedDateShort("",miHash.get("FECHAENTREGA").toString());
		fechaLimite = GstDate.getFormatedDateShort("",miHash.get("FECHALIMITE").toString());
		regEntrada = miHash.get("REGENTRADA").toString();
		regSalida = miHash.get("REGSALIDA").toString();
		if (accion.equalsIgnoreCase("ver")) {
			nombredocumento=(String)ses.getAttribute("NOMBREDOCUMENTO");
		}
	}
	catch(Exception e){};
	
	ArrayList listapresentador    = new ArrayList();
	ArrayList listatipodocu    = new ArrayList();
	ArrayList listadocu    = new ArrayList();

	if(miHash.containsKey("PRESENTADOR")){
		presentador=miHash.get("PRESENTADOR").toString();
		listapresentador.add(presentador);
	}
	if(miHash.containsKey("IDTIPODOCUMENTO")){
		tipodocu=miHash.get("IDTIPODOCUMENTO").toString();
		listatipodocu.add(tipodocu+","+usr.getLocation());
	}
	if(miHash.containsKey("IDDOCUMENTO")){
		documento=miHash.get("IDDOCUMENTO").toString();
		listadocu.add(documento+","+usr.getLocation());
	}
	
	String[] dato = {usr.getLocation()};
	String[] datos = {tipodocu,usr.getLocation()};
	String[] datoPresentador={usr.getLocation(),idTipoEJG,anio,numero};		
%>

<html>
<!-- HEAD -->
<head>
	<html:javascript formName="DefinirDocumentacionEJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
		<siga:Idioma key="gratuita.insertarDocumentacion.literal.editarDocumentacion"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/JGR_DocumentacionEJG" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idTipoEJG" value ="<%=idTipoEJG%>"/>
	<html:hidden property = "anio" value ="<%=anio%>"/>
	<html:hidden property = "numero" value ="<%=numero%>"/>
	<html:hidden property = "idDocumentacion" value ="<%=idDocumentacion%>"/>
	
	<html:hidden property = "presentador" 	value ="<%=presentador%>"/>
	<html:hidden property = "idTipoDocumento" value ="<%=tipodocu%>"/>
	<html:hidden property = "idDocumento" 	value ="<%=documento%>"/>
	
	<html:hidden property = "presentadorAnterior" 	value ="<%=presentador%>"/>
	<html:hidden property = "idTipoDocumentoAnterior" value ="<%=tipodocu%>"/>
	<html:hidden property = "idDocumentoAnterior" 	value ="<%=documento%>"/>

	<table class="tablaCampos" align="center" border ="0" style="width:50">
	<tr><td>			
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="pestana.justiciagratuitaejg.documentacion">
	<table class="tablaCampos" align="center" border ="0">
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fechaLimitePresentacion"/>
		
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<html:text name="DefinirDocumentacionEJGForm" property="fechaLimitePresentacion" size="10" styleClass="boxConsulta" value="<%=fechaLimite%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirDocumentacionEJGForm" property="fechaLimitePresentacion" size="10" styleClass="box" value="<%=fechaLimite%>" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaLimitePresentacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		<%}%>
				
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fechaPresentacion"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<html:text name="DefinirDocumentacionEJGForm" property="fechaPresentacion" size="10" styleClass="boxConsulta" value="<%=fechaEntrega%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirDocumentacionEJGForm" property="fechaPresentacion" size="10" styleClass="box" value="<%=fechaEntrega%>" readonly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaPresentacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"/></a>
		<%}%>	
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.documentacionEJG.regentrada"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<html:text name="DefinirDocumentacionEJGForm" property="regEntrada" size="20" styleClass="boxConsulta" value="<%=regEntrada%>" readonly="true"></html:text>&nbsp;&nbsp;
		<%} else {%>
			<html:text name="DefinirDocumentacionEJGForm" property="regEntrada" size="20" styleClass="box" value="<%=regEntrada%>" readonly="false"></html:text>&nbsp;&nbsp;
		<%}%>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.documentacionEJG.regsalida"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<html:text name="DefinirDocumentacionEJGForm" property="regSalida" size="20" styleClass="boxConsulta" value="<%=regSalida%>" readonly="true"></html:text>&nbsp;&nbsp;
		<%} else {%>
			<html:text name="DefinirDocumentacionEJGForm" property="regSalida" size="20" styleClass="box" value="<%=regSalida%>" readonly="false"></html:text>&nbsp;&nbsp;
		<%}%>
	</td>
	</tr>	
	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.ejg.documentacion.presentador'/>&nbsp;(*)
		</td>
		<td colspan="3">	
			<%if (accion.equalsIgnoreCase("ver")) {%>
				<siga:ComboBD nombre = "presentador" tipo="cmbPresentador"  ancho="300" clase="boxConsulta" pestana="t" obligatorio="true" hijo="t" parametro="<%=datoPresentador%>" elementoSel="<%=listapresentador%>" readOnly="true" obligatoriosintextoseleccionar="true"/>									
			<%} else {%>
				<siga:ComboBD nombre = "presentador" tipo="cmbPresentador"  ancho="300" clase="boxCombo" pestana="t" obligatorio="true" hijo="t" parametro="<%=datoPresentador%>" elementoSel="<%=listapresentador%>" obligatoriosintextoseleccionar="true"/>									
			<%}%>		
		</td>
	</tr>
	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.ejg.documentacion.tipoDocumentacion'/>&nbsp;(*)
		</td>
		<td colspan="3">
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<siga:ComboBD nombre = "idTipoDocumento" tipo="cmbTipoDocumentacion" ancho="300" clase="boxConsulta" obligatorio="true" hijo="t" accion="Hijo:idDocumento;" parametro="<%=dato%>" elementoSel="<%=listatipodocu%>" readOnly="true" obligatoriosintextoseleccionar="true"/>
		<%} else {%>
			<siga:ComboBD nombre = "idTipoDocumento" tipo="cmbTipoDocumentacion" ancho="300" clase="boxCombo" obligatorio="true" hijo="t" accion="Hijo:idDocumento;" parametro="<%=dato%>" elementoSel="<%=listatipodocu%>" obligatoriosintextoseleccionar="true"/>
		<%}%>
		</td>
	</tr>					
	<tr>
		<td class="labelText">	
			<siga:Idioma key='sjcs.ejg.documentacion.documentacion'/>&nbsp;(*)
		</td>
		<td colspan="3">
		<%if (accion.equalsIgnoreCase("ver")) {%>
			<siga:ComboBD nombre = "idDocumento" tipo="cmbDocumentoEdit" ancho="300" clase="boxConsulta" obligatorio="true" hijo="t" parametro="<%=datos%>" elementoSel="<%=listadocu%>" readOnly="true" obligatoriosintextoseleccionar="true"/>
		<%} else {%>
			<siga:ComboBD nombre = "idDocumento" tipo="cmbDocumentoEdit" ancho="300" clase="boxCombo" obligatorio="true" pestana="t" hijo="t" elementoSel="<%=listadocu%>" obligatoriosintextoseleccionar="true"/>
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
				<textarea name="documentacion" rows="6" cols="60" style="width:600" class="boxConsulta" readonly="true"><%=documentacion%></textarea>
			<%} else {%>
				<textarea name="documentacion" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="6" cols="60" style="width:600" class="box"><%=documentacion%></textarea>
			<%}%>
		</td>	
	</tr>
	</table>
	</siga:ConjCampos>
	</td></tr>
	</table>
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
			if (validateDefinirDocumentacionEJGForm(document.forms[0])){
		       	document.forms[0].submit();
				window.returnValue="MODIFICADO";		
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
		
		function inicio()
		{
			//var nombre = document.forms[0].idTipoDocumento;
			//alert("TIPODOCUMENTO->"+DefinirDocumentacionEJGForm.idTipoDocumento);
			//nombre.readOnly=true;		
			//DefinirDocumentacionEJGForm.idTipoDocumento.readOnly=true;
			//DefinirDocumentacionEJGForm.idTipoDocumento.onchange();
		}		
		</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>