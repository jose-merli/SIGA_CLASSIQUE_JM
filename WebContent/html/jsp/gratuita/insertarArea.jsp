<!DOCTYPE html>
<html>
<head>
<!-- insertarArea.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsAreaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="menu.justiciaGratuita.maestros.NuevoAreasYMaterias" 
		localizacion="gratuita.areas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>

		//Refresco
		function refrescarLocal(){
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].accion.value = "areaEdicion";
			document.forms[0].submit();
		}	

	</script>
	
</head>

<body onLoad="ajusteAlto('resultado');">


	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<html:form action="/DefinirAreasMateriasAction.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "accion" value = "area"/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "idArea" value =""/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
	
	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.insertarArea.literal.area">
	<table align="center" width="100%">
	
	<!-- FILA -->	
	<tr>	
	<td class="labelText" valign="top">
		<siga:Idioma key="gratuita.busquedaAreas.literal.nombreArea"/>&nbsp;(*)
	</td>
	<td valign="top">
		<html:text name="DefinirAreasMateriasForm" property="nombreArea" size="30" styleClass="box" value=""></html:text>
	</td>
	<td class="labelText" valign="top">
		<siga:Idioma key="gratuita.listadoAreas.literal.contenidoArea"/>
	</td>
	<td valign="top">
		<textarea name="contenidoArea" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="5" cols="60" class="box"></textarea>
	</td>
	</tr>
	</table>
	</siga:ConjCampos>
	</html:form>
		
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="G,R" clase="botonesSeguido" titulo="gratuita.busquedaAreas.literal.materias"/>		
	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function buscar() 
		{		
			document.forms[0].target = "mainWorkArea";
			document.forms[0].accion.value = "areaEdicion";
			document.forms[0].modo.value = "Editar";
			document.forms[0].submit();				
		}
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		<!-- funcion recargar pagibna -->
		function paginaInicial()
		{
		    
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea";
			document.forms[0].submit(); 
		
		}
		
		function refrescarLocal() {
			paginaInicial();
		}
		
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{
			sub();
			var nombre = document.forms[0].nombreArea.value;
			var contenido = document.forms[0].contenidoArea.value;
			if ((nombre.length <= 60) && (nombre != "")) {
				if (contenido.length<=4000) {
				
						document.forms[0].submit();
            
//						paginaInicial();  <!-- se encarga de recargar la pagina -->
					
					 		 		 
					
				}
				else{ 
					alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudDescripcion"/>');
					fin();
					return false;
				}
			}
			else if (nombre == ""){
			 	alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoNombre"/>');
			 	fin();
				return false;
			}
			else{
				 alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudNombre"/>');
				 fin();
				 return false;
				
			}
		}
		
				
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
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

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"/>

	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Nuevo -->		
		function accionNuevo() 
		{
			alert('<siga:Idioma key="gratuita.insertarArea.message.insetarAreaPrimero"/>');
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
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
	
	</body>
</html>
