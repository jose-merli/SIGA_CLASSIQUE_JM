<!-- insertarMateria.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsMateriaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	//String profile=usr.getProfile();
	Hashtable miHash = (Hashtable)request.getSession().getAttribute("elegido");
	request.getSession().removeAttribute("elegido");

%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">
		<siga:Idioma key="gratuita.insertarArea.literal.insertarMateria"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/DefinirAreasMateriasAction.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "accion" value = "materia"/>
	<html:hidden property = "idArea" value = "<%=miHash.get(ScsMateriaBean.C_IDAREA).toString()%>"/>
	<html:hidden property = "idInstitucion" value ="<%=miHash.get(ScsMateriaBean.C_IDINSTITUCION).toString()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
	
	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.insertarArea.literal.materia">
	<table class="tablaCampos" align="center">
	
	<!-- FILA -->	
	<tr>
	
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaAreas.literal.nombreMateria"/>&nbsp;(*)
	</td>				
	
	<td>
		<html:text name="DefinirAreasMateriasForm" property="nombreMateria" size="30" styleClass="box" value=""></html:text>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.listadoAreas.literal.contenidoMateria"/>
	</td>
	
	<td>
		<textarea name="contenidoMateria" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="5" cols="60" class="box"></textarea>		
	</td>
	</tr>
	</table>

	</siga:ConjCampos>
	
	</td>
	</tr>
	</html:form>
	</table>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			var nombre = document.forms[0].nombreMateria.value;
			var contenido = document.forms[0].contenidoMateria.value;
			sub();
			if ((nombre.length <= 60) && (nombre != "")) {
				if (contenido.length<=4000) {
					window.top.returnValue="MODIFICADO";
					document.forms[0].submit();
					
				}else{
				alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudDescripcion"/>');
				fin();
				return false;
				}
			}else if (nombre == ""){
			 alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoNombre"/>');
			 fin();
			 return false;
			 }else {
			  alert('<siga:Idioma key="gratuita.areasMaterias.message.longitudNombre"/>');
			  fin();
			  return false;
			 }
		} 
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");			
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
