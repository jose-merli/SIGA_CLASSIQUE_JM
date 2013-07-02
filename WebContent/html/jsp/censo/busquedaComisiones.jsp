<!-- busquedaComisiones.jsp -->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsZonaBean"%>
<%@ page import="com.siga.beans.ScsSubzonaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>

<!-- JSP -->

<% 	
	
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	String app=request.getContextPath();
	
		
	// institucionesVisibles
	String institucionesVisibles = (String) request.getAttribute("CenInstitucionesVisibles");
	if (institucionesVisibles==null) institucionesVisibles="";
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	String parametro[] = new String[2];
	parametro[0] = (String)usr.getLocation();
	parametro[1] = (String)usr.getLanguage().toUpperCase();
	
	String institucionAcceso=usr.getLocation();
	String nombreInstitucionAcceso="";
	if (institucionAcceso.equalsIgnoreCase(institucionesVisibles)){
		CenInstitucionAdm institucionAdm = new CenInstitucionAdm(usr);
		nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(institucionAcceso);
	}
	
	

%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="censo.comisiones.literal.comisiones" 
		localizacion="censo.comisiones.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
</head>

<body  onLoad="ajusteAlto('resultado');" >

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<fieldset>
	<table class="tablaCentralCampos" align="center">

	<html:form action="/CEN_GestionarComisiones.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = "inicio"/>
	<html:hidden property = "idInstitucionCargo" value=''/>
	<html:hidden property = "idPersona" value=''/>	
	<!-- FILA -->
	<tr>				
	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.colegio"/>
	</td>				
	<td colspan="4">
			<% if (institucionAcceso.equalsIgnoreCase(institucionesVisibles)){%>
				<html:text property="nombreInstitucion" styleClass="boxConsulta" size="80" value='<%=nombreInstitucionAcceso%>' readOnly="true"></html:text>
				<html:hidden property="idInstitucion"  value='<%=institucionAcceso%>'></html:hidden>
			
			<% }else{%>
				<siga:Select queryId="getNombreColegiosTodos" id="idInstitucion"/>
			<% } %>
			
	</td>
	</tr>
	<!-- FILA -->
	<tr>				
	<tr>				
	<td class="labelText">
		<siga:Idioma key="censo.busquedaComisiones.literal.comision"/>
	</td>
	<td class="labelText">
		<siga:Select queryId="getCenTiposCVsubtipo1IdTipoCv4" id="comision"/>
	</td>
	
	<td class="labelText">
		<siga:Idioma key="censo.busquedaComisiones.literal.cargos"/>
	</td>
	<td class="labelText">
		<siga:Select queryId="getCenTiposCVsubtipo2IdTipoCv4" id="cargos"/>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="censo.busquedaComisiones.literal.fechaCargo"/>
	</td>
	<td class="labelText">
 <siga:Fecha nombreCampo="fechaCargo"></siga:Fecha>
	</td>
	</tr>
	
	</html:form>
	
	</table>
	</fieldset>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="B"  titulo="censo.comisiones.literal.consultarComisiones" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();			
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "nuevo";
			document.forms[0].submit();
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