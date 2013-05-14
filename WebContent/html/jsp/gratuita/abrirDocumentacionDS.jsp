<!-- abrirDocumentacionDS.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 31/01/2005 Versión inicial
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

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.DefinirEJGForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
		
	String editable = (String)request.getParameter("editable");		
	
	
	String botones2 = "V";		
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");
	request.removeAttribute("datos");
	
	DefinirEJGForm f = (DefinirEJGForm)request.getAttribute("DefinirEJGForm");	
	
	String anio = "", numero = "", idTipoEJG = "", observaciones = "";
	Hashtable miHash = (Hashtable) ses.getAttribute("DATABACKUP");
	
	anio = miHash.get("ANIO").toString();
	numero = miHash.get("NUMERO").toString();
	idTipoEJG = miHash.get("IDTIPOEJG").toString();

	String identificadorDS = (String) request.getAttribute("IDENTIFICADORDS");
	
%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.auditoria.documentacion.cabecera" 
			localizacion="expedientes.auditoria.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>
	
	<script type="text/javascript">
		function cargarCollection() {
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="resultado1";	
			document.forms[0].submit();		
		}
		
		
	</script>

	<body class="detallePestanas" onload="cargarCollection()">	

		<table class="tablaTitulo" align="center" cellspacing="0">
		<html:form action="/JGR_EJG_DocumentacionRegTel.do?noReset=true" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			
			<input type="hidden" name="identificadorDs" value="<%=identificadorDS%>">
			<input type="hidden" name="titleDs" value="">
			<input type="hidden" name="posicionDs" value="">
			<input type="hidden" name="creaCollection" value="false">
			
			
			<tr>
				<td id="titulo" class="titulitosDatos">					
					<%
							String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG = "";
							
							ScsEJGAdm adm = new ScsEJGAdm(usr);

							Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(),
									anio, numero, idTipoEJG);

							if (hTitulo != null) {
								t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
								t_apellido1 = (String) hTitulo
										.get(ScsPersonaJGBean.C_APELLIDO1);
								t_apellido2 = (String) hTitulo
										.get(ScsPersonaJGBean.C_APELLIDO2);
								t_anio = (String) hTitulo.get(ScsEJGBean.C_ANIO);
								t_numero = (String) hTitulo.get(ScsEJGBean.C_NUMEJG);
								t_tipoEJG = (String) hTitulo.get("TIPOEJG");
							}
						%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
					
				</td>
			</tr>
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>			

		</table>
	
		<!-- INICIO: IFRAME LISTA RESULTADOS -->
			<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado1"
							name="resultado1"
							scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0";					 
							style="width:100%; height:100%;">
			</iframe>
			<!-- FIN: IFRAME LISTA RESULTADOS -->
			
			

		<siga:ConjBotonesAccion botones="<%=botones2%>" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->


		<html:form action="/JGR_EJG.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "avanzada" value = ""/>		
		</html:form>

	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function refrescarLocal()
		{	
			document.location.reload();
		}

	//Asociada al boton Volver -->
		function accionVolver() 
		{				
			document.forms[0].action="./JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>