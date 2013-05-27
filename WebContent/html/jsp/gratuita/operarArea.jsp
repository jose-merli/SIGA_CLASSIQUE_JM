<!-- operarArea.jsp -->
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
<%@ page import="com.siga.beans.ScsAreaBean"%>
<%@ page import="com.siga.beans.ScsMateriaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
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

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="menu.justiciaGratuita.maestros.MantenimientoAreasYMaterias" 
		localizacion="gratuita.areas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');">
	

	<html:form action="/DefinirAreasMateriasAction.do" method="POST" target="resultado">
	
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "accion" value = ""/>
	<html:hidden property = "idArea" value = "<%=(String)miHash.get(ScsAreaBean.C_IDAREA)%>"/>
	<html:hidden property = "idInstitucion" value = "<%=(String)miHash.get(ScsAreaBean.C_IDINSTITUCION)%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
		
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.insertarArea.literal.area">

	<table align="center" width="100%">
	<tr>				
	<td class="labelText" valign="top">
		<siga:Idioma key="gratuita.insertarArea.literal.area"/>&nbsp;(*)
	</td>				
	<td valign="top">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirAreasMateriasForm" property="nombreArea" size="30" styleClass="boxConsulta" value="<%=(String)miHash.get(ScsAreaBean.C_NOMBRE)%>" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirAreasMateriasForm" property="nombreArea" size="30" styleClass="box" value="<%=(String)miHash.get(ScsAreaBean.C_NOMBRE)%>"></html:text>
		<%}%>
	</td>	

	<td class="labelText" valign="top">
		<siga:Idioma key="gratuita.listadoAreas.literal.contenidoArea"/>
	</td>
	<td valign="top">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<textarea name="contenidoArea" rows="4" cols="60" readOnly="true" class="boxConsulta"><%=(String)miHash.get(ScsAreaBean.C_CONTENIDO)%></textarea>
		<%} else {%>
			<textarea name="contenidoArea" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="4" cols="60" class="box"><%=(String)miHash.get(ScsAreaBean.C_CONTENIDO)%></textarea>
		<%}%>
	</td>
	</tr>
	
	</table>
	</siga:ConjCampos>
	</html:form>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ConjBotonesAccion botones="" clase="botonesSeguido" titulo="gratuita.busquedaAreas.literal.materias"/>
		<%} else {%>
			<siga:ConjBotonesAccion botones="G,R" clase="botonesSeguido" titulo="gratuita.busquedaAreas.literal.materias"/>
		<%}%>

	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		//Refresco
		function refrescarLocal(){
			buscar();
		}
	
		function buscar() 
		{		
			document.forms[0].target="resultado";
			document.forms[0].modo.value = "editar";
			document.forms[0].accion.value = "area";
			document.forms[0].submit();
		}

		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar
		function accionGuardar() 
		{
			//sub();
			document.forms[0].target="submitArea";
			var nombre = document.forms[0].nombreArea.value;
			var contenido = document.forms[0].contenidoArea.value;
			if ((nombre.length <= 60) && (nombre != "")) {
				if (contenido.length<=4000) {
					document.forms[0].accion.value = "area";
					document.forms[0].modo.value="Modificar";
					document.forms[0].submit();
				}
				else{
					alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudDescripcion"/>');
					//fin();
					return false;
				}
			}
			else if (nombre == ""){ 
				alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoNombre"/>');
				//fin();
				return false;
				
			}
			else{
				alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudNombre"/>');
				//fin();
				return false;
			}

			

		}			

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

						
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/gratuita/listadoMaterias.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";
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
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		//Asociada al boton Nuevo		
		function accionNuevo() 
		{				
			document.forms[0].modo.value = "nuevo";
			document.forms[0].accion.value = "materia";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();	
		}

		//Asociada al boton Volver
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
