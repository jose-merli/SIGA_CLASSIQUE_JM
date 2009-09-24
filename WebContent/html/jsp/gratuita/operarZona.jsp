<!-- operarZona.jsp -->
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
<%@ page import="com.siga.beans.ScsZonaBean"%>
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
	String accion = (String)ses.getAttribute("accion");	
	ses.removeAttribute("elegido");

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.zonaSubzona.titulo.mantenimientoZona" 
		localizacion="gratuita.grupoZonas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onLoad="ajusteAlto('resultado');">
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">
	
	<!-- CAMPOS DEL REGISTRO -->
	<table align="center"  width="100%" class="tablaCentralCampos">

	<html:form action="/JGR_DefinirZonasSubzonas.do" method="POST" target="resultado">
	
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "accion" value = ""/>
	<html:hidden property = "idZona" value = "<%=(String)miHash.get(ScsZonaBean.C_IDZONA)%>"/>
	<html:hidden property = "idInstitucionZona" value = "<%=(String)miHash.get(ScsZonaBean.C_IDINSTITUCION)%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
		
	<tr>				
	<td width="100%" align="center">

	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaZonas.literal.zona">

	<table align="center" width="100%">
	<tr>				
	<td class="labelText" valign="top" width="100">
		<siga:Idioma key="gratuita.busquedaZonas.literal.zona"/>&nbsp;(*)
	</td>				
	<td valign="top">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirZonasSubzonasForm" property="nombreZona" size="60" styleClass="boxConsulta" value="<%=(String)miHash.get(ScsZonaBean.C_NOMBRE)%>" readOnly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirZonasSubzonasForm" property="nombreZona" size="60" styleClass="box" value="<%=(String)miHash.get(ScsZonaBean.C_NOMBRE)%>"></html:text>
		<%}%>
	</td>	
<!--
	<td class="labelText" valign="top">
		<siga:Idioma key="gratuita.insertarZona.literal.municipios"/>
	</td>
	<td valign="top">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<textarea name="municipiosZona" rows="4" cols="60" readOnly="true" class="boxConsulta"><%=(String)miHash.get(ScsZonaBean.C_MUNICIPIOS)%></textarea>
		<%} else {%>
			<textarea name="municipiosZona" rows="4" cols="60" class="box"><%=(String)miHash.get(ScsZonaBean.C_MUNICIPIOS)%></textarea>
		<%}%>
	</td>
-->
	</tr>
	
	</table>
	</siga:ConjCampos>
	</td>
	</tr>	
	</html:form>
	</table>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ConjBotonesAccion botones="" clase="botonesSeguido" titulo="gratuita.busquedaZonas.literal.zonas"/>
		<%} else {%>
			<siga:ConjBotonesAccion botones="G,R" clase="botonesSeguido" titulo="gratuita.busquedaZonas.literal.zonas"/>
		<%}%>

	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		function buscar() 
		{		
			document.forms[0].target = "resultado";
			document.forms[0].modo.value = "editar";
			document.forms[0].accion.value = "zona";
			document.forms[0].submit();
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{				
			//sub();
			var nombre = document.forms[0].nombreZona.value;
			//var municipios = document.forms[0].municipiosZona.value;
			if ((nombre.length <= 60) && (nombre != "")) {
				//if (municipios.length<=4000) {
					document.forms[0].target="submitArea";
					document.forms[0].accion.value = "zona";
					document.forms[0].modo.value="Modificar";
					document.forms[0].submit();
				//}
				//else alert('<siga:Idioma key="gratuita.zonasSubzonas.message.longitudMunicipios"/>');
			}
			else if (nombre == "") {
			 	alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
			 	//fin();
				return false;
			}
			else{
				 alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
				 //fin();
				 return false;
			}	
		}			

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

						
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/gratuita/listadoSubzonas.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>		
		<%} else {%>
			<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"/>
		<%}%>
	<!-- FIN: BOTONES REGISTRO -->

</div>	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Nuevo -->		
		function accionNuevo() 
		{				
			document.forms[0].accion.target="submitArea";
			document.forms[0].modo.value = "nuevo";
			document.forms[0].accion.value = "subzona";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();	
		}

		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	

	</body>
</html>
