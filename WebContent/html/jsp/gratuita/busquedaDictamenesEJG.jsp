<!DOCTYPE html>
<html>
<head>
<!-- busquedaDictamenesEJG.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	String codigo="", descrip="", busquedaRealizada="0";
	String abre="",descri="",esVolver="0";
	
	try {
		Hashtable miHash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");
		ses.removeAttribute("DATOSFORMULARIO");
		if (miHash != null) {
			codigo = (String)miHash.get("CODIGOEXT");
			descrip = (String)miHash.get("DESCRIPCION");
			busquedaRealizada = (String)miHash.get("BUSQUEDAREALIZADA");
		}
//		Hashtable busqueda = (Hashtable) ses.getAttribute("busqueda");
//		if (busqueda != null) {
//			abre=(String)busqueda.get("ABREVIATURA");
//			descri=(String)busqueda.get("DESCRIPCION");
//		}

		esVolver = (String) ses.getAttribute("esVolver");	
		if (esVolver == null) esVolver = new String ("");
		else ses.removeAttribute("esVolver");

	}
	catch (Exception e){};



%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="menu.SJCS.mantenimientoDictamenesEJG" 
		localizacion="gratuita.dictamenesEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');<%if(esVolver.equals("1")){%>buscar();<%}%>" >


	<fieldset>

		<table class="tablaCentralCampos" align="center">
	
			<html:form action="/JGR_MantenimientoDictamenesEJG.do" method="POST" target="resultado">
			<input type="hidden" name="modo" value="buscarPor"/>
		  		
				<tr>
					<td class="labelText">
						<siga:Idioma key="general.codeext"/>
					</td>
					<td class="labelText">
						<html:text name="DefinirMantenimDictamenesEJGForm" property="codigoExt" size="30" maxlength="60" styleClass="box" value="<%=codigo%>"></html:text>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="general.description"/>
					</td>
					<td class="labelText">
						<html:text name="DefinirMantenimDictamenesEJGForm" property="descripcion" size="30" maxlength="60" styleClass="box"  value="<%=descrip%>"></html:text>
					</td>
				</tr>
			
			</html:form>
		
		</table>
	</fieldset>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.busquedaDictamen.literal.consultaDictamenesEJG" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
		//Funcion asociada a boton buscar
		function buscar() 
		{			
			<%if (esVolver.equals("1")){%>
				document.forms[0].codigoExt.value	= "<%=codigo%>";
				document.forms[0].descripcion.value 		= "<%=descri%>";
			<%}%>
			document.forms[0].target     = "resultado";
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}		
		
		//Funcion asociada a boton limpiar
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		//Funcion asociada a boton Nuevo
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

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>