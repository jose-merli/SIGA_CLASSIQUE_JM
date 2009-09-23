<!-- busquedaDocumentoEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
	String tipoDocumentoB="", documentoB="", busquedaRealizada="0";
	String abre="",descri="",abreDoc="",descriDoc="" ,esVolver="0";
	
	try {
		//Hashtable miHash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");
		
		
		Hashtable busqueda = (Hashtable) ses.getAttribute("busqueda");
		

		esVolver = (String) ses.getAttribute("esVolver");	
		
		if (esVolver == null) {
		request.getSession().removeAttribute("busqueda");	
		esVolver = new String ("");
		}else{
		 if (busqueda != null) {
			abre=(String)busqueda.get("ABREVIATURA");
			descri=(String)busqueda.get("DESCRIPCION");
			abreDoc=(String)busqueda.get("DOCUMENTOABREVIADO");
			descriDoc=(String)busqueda.get("DESCRIPCIONDOC");
		 }
		}
		//else ses.removeAttribute("esVolver");

	}
	catch (Exception e){};



%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="menu.facturacionSJCS.mantenimientoDocumentacionEJG" 
		localizacion="gratuita.documentacionEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');<%if(esVolver.equals("1")){%>buscar();<%}%>" >

			<table class="tablaCentralCampos" align="center">
			<html:form action="/JGR_MantenimientoDocumentacionEJG.do" method="POST" target="resultado">
			<input type="hidden" name="modo" value="buscarPor"/>
			<tr>
			<td>
			 <siga:ConjCampos leyenda="sjcs.ejg.documentacion.tipoDocumentacion">
		  		<table class="tablaCentralCampos" align="center">
				<tr>
					<td class="labelText">
						<siga:Idioma key="sjcs.ejg.documentacion.Abreviatura"/>
					</td>
					<td class="labelText">
						<html:text name="DefinirMantenimDocumentacionEJGForm" property="abreviatura" size="30" value="<%=abre%>" maxlength="60" styleClass="box"  ></html:text>
					</td>
					</tr>
					<tr>
					<td class="labelText">
						<siga:Idioma key="sjcs.ejg.documentacion.Descripcion"/>
					</td>
					
					<td class="labelText">
						<html:text name="DefinirMantenimDocumentacionEJGForm" property="descripcion" size="60" value="<%=descri%>" maxlength="200" styleClass="box" ></html:text>
					</td>
				</tr>
				
				</table>
				</siga:ConjCampos>
              </td>
              <td>
              <siga:ConjCampos leyenda="sjcs.ejg.documentacion.Documento">
				<table class="tablaCentralCampos" align="center">
				<tr>
				<td class="labelText">
						<siga:Idioma key="sjcs.ejg.documentacion.Abreviatura"/>
					</td>
					<td class="labelText">
						<html:text name="DefinirMantenimDocumentacionEJGForm" property="documentoAbreviado" size="30" value="<%=abreDoc%>" maxlength="60" styleClass="box" ></html:text>
					</td>
					
				</tr>
				<tr>
				<td class="labelText">
						<siga:Idioma key="sjcs.ejg.documentacion.Descripcion"/>
					</td>
					<td class="labelText">
						<html:text name="DefinirMantenimDocumentacionEJGForm" property="doc" size="50" value="<%=descriDoc%>" maxlength="300" styleClass="box"  ></html:text>
					</td>
					
				</tr>
				</table>
				</siga:ConjCampos>
				</td>
				</tr>
				</html:form>
			
		</table>

	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.busquedaAreas.literal.consultaDocumentacionEJG" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();		
			<%if (esVolver.equals("1")){%>

				//document.forms[0].tipoDocumento.value	= "<%=abre%>";
				//document.forms[0].documento.value 		= "<%=descri%>";
			<%}%>
			document.forms[0].target     = "resultado";
			document.forms[0].modo.value = "buscarPor";
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

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>