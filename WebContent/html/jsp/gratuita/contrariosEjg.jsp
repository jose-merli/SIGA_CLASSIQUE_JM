<!-- contrariosEjg.jsp -->

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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.form.ContrariosEjgForm" %>
<%@ page import="com.siga.gratuita.action.PersonaJGAction" %>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String modo =request.getSession().getAttribute("accion")==null?"":(String)request.getSession().getAttribute("accion");
	String modoPestanha = (String)request.getAttribute("modoPestanha");
	String anio = (String)request.getAttribute("anio");
	String numero = (String)request.getAttribute("numero");
	String idTipoEJG = (String)request.getAttribute("idTipoEJG");
	
	boolean esFichaColegial = false;
	

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null) && (sEsFichaColegial.equalsIgnoreCase("1"))) {
		esFichaColegial = true;
	}
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<%--siga:Titulo 
		titulo="gratuita.contrariosDesigna.literal.titulo" 
		localizacion="gratuita.contrariosDesigna.literal.location"/--%>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>
		function buscarContrarios()
		{
<%			if (esFichaColegial) {%>
				document.forms[0].action = "/SIGA/JGR_ContrariosEjgLetrado.do";
<%			} else { %>
				document.forms[0].action = "/SIGA/JGR_ContrariosEjg.do";
<%			}%>
			document.forms[0].target = "resultado";
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
		}
	</script>

</head>

<body class="tablaCentralCampos" onload="ajusteAltoBotones('resultado');buscarContrarios()">

   

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
<%		String sAction1 = esFichaColegial ? "JGR_ContrariosEjgLetrado.do" : "JGR_ContrariosEjg.do";%>
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="<%=sAction1%>" method="post" target="mainPestanas" style="display:none">
			<html:hidden property = "actionModal" value = ""/>			
			<html:hidden name="ContrariosEjgForm" property = "modo" value = ""/>
			<html:hidden name="ContrariosEjgForm" property = "anio" value="<%=anio%>"/>
			<html:hidden name="ContrariosEjgForm" property = "numero" value="<%=numero%>"/>
			<html:hidden name="ContrariosEjgForm" property = "idTipoEJG" value="<%=idTipoEJG%>"/>
			<html:hidden name="ContrariosEjgForm" property = "modoPestanha" value="<%=modoPestanha%>"/>
			<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>
		</html:form>
		
		<%if(modo.equals("editar")){%>
			<siga:ConjBotonesBusqueda botones="N"  titulo="gratuita.mantEjg.literal.tituloCO" />
		<%}else{%>
			<siga:ConjBotonesBusqueda botones=""  titulo="gratuita.mantEjg.literal.tituloCO" />
		<%}%>
		
			
		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					 id="resultado"
					 name="resultado" 
					 scrolling="no"
					 frameborder="0"
				 	 marginheight="0"
				 	 marginwidth="0";					 
					 class="frameGeneral">
		</iframe>
		
		<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionVolver() {
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();
		}
		
		function nuevo() 
		{
			document.forms[0].modo.value="nuevo";
			
			document.forms[0].target="submitArea";
			
			var resultado=ventaModalGeneral(document.forms[1].name,"G");
			if (resultado=="MODIFICADO")
				buscarContrarios();
		}

		function refrescarLocal()
		{
			buscarContrarios();
		}
</script>		

<%
		String sClasePestanas = esFichaColegial ? "botonesDetalle3" : "botonesDetalle";
		String sActionE = esFichaColegial ? "/JGR_ContrariosEjgPerJGLetrado.do" : "/JGR_ContrariosEjgPerJG.do";
%>
<%
		String sBoton = esFichaColegial ? "N" : "N";
%>



<%	String sAction = esFichaColegial ? "JGR_ContrariosEjgPerJGLetrado.do" : "JGR_ContrariosEjgPerJG.do";%>
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="<%=sAction%>" method="post" target="submitArea">
			<html:hidden property = "actionModal" value = ""/>			
			<input type="hidden" name="idInstitucionJG" value="<%=usr.getLocation() %>">
			<input type="hidden" name="idPersonaJG" value="">
	
			<input type="hidden" name="idInstitucionEJG" value="<%=usr.getLocation() %>">
			<input type="hidden" name="anioEJG" value="<%=anio %>">
			<input type="hidden" name="numeroEJG" value="<%=numero %>">
			<input type="hidden" name="idTipoEJG" value="<%=idTipoEJG %>">
			
			
	
			<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.EJG_CONTRARIOS %>">
			<input type="hidden" name="tituloE" value="gratuita.mantEjg.literal.tituloCO">
			<input type="hidden" name="localizacionE" value="">
			<input type="hidden" name="accionE" value="nuevo">
			<input type="hidden" name="actionE" value="<%=sActionE%>">
			<input type="hidden" name="pantallaE" value="M">
			
			<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>
						
		</html:form>
		
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>
</html>