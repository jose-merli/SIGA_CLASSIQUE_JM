<!-- operarTipo.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Hashtable miHash = (Hashtable) ses.getAttribute("elegido");
	String accion = (String)request.getAttribute("accionModo");	
	ses.removeAttribute("elegido");

	String descri=(String)miHash.get("DESCRIPCION");
	String abrev=(String)miHash.get("ABREVIATURA");
	String dictamen=(String)miHash.get("IDDICTAMEN");
	String cod=(String)miHash.get("CODIGOEXT");
	ses.setAttribute("idDictamen",dictamen);
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.dictamenesEJG.localizacion.mantenimiento" 
		localizacion="gratuita.dictamenesEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado'); ">
	
	<html:form action="/JGR_MantenimientoDictamenesEJG.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idDictamen" value = "<%=dictamen%>"/>
	
	<html:hidden property = "actionModal" value = ""/>

	
		
		<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="gratuita.busquedaEJG.dictamen">
			<table align="center" width="100%">
				<tr>				
					<td class="labelText" valign="top">
						<siga:Idioma key="general.codeext"/>&nbsp;(*)
					</td>				
					<td valign="top">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirMantenimDictamenesEJGForm" property="codigoExt" size="30" styleClass="boxConsulta" value="<%=cod%>"></html:text>
						<%} else {%>
							<html:text name="DefinirMantenimDictamenesEJGForm" property="codigoExt" size="30" styleClass="box" value="<%=cod%>"></html:text>
						<%}%>
					</td>	
				</tr>
				<tr>	
					<td class="labelText" valign="top">
						<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>&nbsp;(*)
					</td>				
					<td valign="top">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirMantenimDictamenesEJGForm" property="abreviatura" size="30" styleClass="boxConsulta" value="<%=abrev%>"></html:text>
						<%} else {%>
							<html:text name="DefinirMantenimDictamenesEJGForm" property="abreviatura" size="30" styleClass="box" value="<%=abrev%>"></html:text>
						<%}%>
					</td>				
					<td class="labelText" valign="top">
						<siga:Idioma key="general.description"/>
					</td>
					<td valign="top">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<textarea name="descripcion" rows="5" cols="60" class="boxConsulta"><%=descri%></textarea>
						<%} else {%>
							<textarea name="descripcion" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="5" cols="60" class="box"><%=descri%></textarea>
						<%}%>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		//Refresco
		         
		function refrescarLocal(){
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();
		}
	
		function buscar() 
		{		
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.DefinirMantenimDictamenesEJGForm.reset();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{
			document.forms[0].target = "submitArea";
			document.forms[0].modo.value="modificar";
			document.forms[0].submit();

		}			

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>
		<%} else {%>
			<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle"/>
		<%}%>
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			<%
			// indicamos que es boton volver
			ses.setAttribute("esVolver","1");
			%>
			document.forms[0].modo.value="abrir";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->		

	</body>
</html>
