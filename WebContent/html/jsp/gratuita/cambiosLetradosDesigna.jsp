<!DOCTYPE html>
<html>
<head>
<!-- cambiosLetradosDesigna.jsp -->
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)request.getAttribute("resultado");
	request.getSession().setAttribute("resultado",obj);
	String accion = (String)request.getAttribute("accion");
	String boton="";
	String modo = (String) ses.getAttribute("Modo");
	boolean botonNuevo = (Boolean)request.getSession().getAttribute("botonNuevo");
	Hashtable designaActual = (Hashtable)obj.get(0);
	String anio="",numero="", idTurno="", idInstitucion="", fechaDesigna = "";
	anio = (String)designaActual.get("ANIO");
	numero = (String)designaActual.get("NUMERO");
	idTurno = (String)designaActual.get("IDTURNO");
	fechaDesigna = (String)designaActual.get("FECHADESIGNA");
	idInstitucion = (String)designaActual.get("IDINSTITUCION");

%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.defendidosDesigna.literal.titulo" 
		localizacion="gratuita.defendidosDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	

</head>

<body onLoad="ajusteAltoBotones('resultado');" class="tablaCentralCampos">
	<html:form action="JGR_CambiosLetradosDesigna.do" method="POST" target="mainPestanas" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "anio" value='<%=anio%>'/>
		<html:hidden property = "numero" value='<%=numero%>'/>
		<html:hidden property = "idTurno" value='<%=idTurno%>'/>
		<html:hidden property = "idInstitucion" value='<%=idInstitucion%>'/>
		<html:hidden property = "fechaDesigna" value='<%=GstDate.getFormatedDateShort("",fechaDesigna)%>'/>
		<html:hidden property = "idPersona"/>
		<html:hidden property = "nInstitucionOrigen"/>
	</html:form>	
	
	<html:form  action="/CEN_BusquedaCensoModal" method="POST" target="submitArea"  enctype="multipart/form-data">
			<html:hidden  name="busquedaCensoModalForm" property="modo"/>
			<html:hidden property = "actionModal" value = ""/>
	</html:form>	

	<iframe align="center" src="<%=app%>/html/jsp/gratuita/listarCambiosLetradosDesigna.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	
	<siga:ConjBotonesAccion botones="N,V" clase="botonesDetalle" modo="<%=modo%>" />
	
<!-- INICIO: SUBMIT AREA -->

<script language="JavaScript">

	
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[0].target = "mainWorkArea";	
			document.forms[0].action="JGR_Designas.do";
			document.forms[0].modo.value="volverBusqueda";
			document.forms[0].submit();
		}
		
		function accionNuevo() 
		{	
			document.forms[0].target="submitArea";
			<% if(botonNuevo){ %>
				document.forms[0].modo.value = "nuevo";
				var resultado=ventaModalGeneral(document.forms[0].name,"M");
				if(resultado=='MODIFICADO') 
					buscar();
			<% } else { %>
			busquedaCensoModalForm.modo.value = "designarArt27";
				var resultado=ventaModalGeneral(busquedaCensoModalForm.name,"G");
				if(resultado!=null && resultado[0] != null && resultado[0] != ""){
					document.forms[0].idPersona.value=resultado[0];
					document.forms[0].nInstitucionOrigen.value=resultado[5];
					modificar();// Sustituir el letrado
				}	
			<% } %>


		}
		
		function buscar()
		{
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value="";
			document.forms[0].submit();
		}

		function modificar()
		{
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value="sustituirLetradoPorArt27";
			document.forms[0].submit();
		}		
		
		function refrescarLocal()
		{
			buscar();
		}
		
</script>		

<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
		  
		
