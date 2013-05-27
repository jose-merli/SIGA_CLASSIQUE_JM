<!-- insertarSubzona.jsp -->
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
<%@ page import="com.siga.beans.ScsSubzonaBean"%>
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
	Hashtable miHash = (Hashtable)request.getSession().getAttribute("elegido");
	request.getSession().removeAttribute("elegido");
	String dato[] = {(String)usr.getLocation(),(String)usr.getLocation()};	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">
		<siga:Idioma key="gratuita.insertarSubzona.literal.insertarSubzona"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/JGR_DefinirZonasSubzonas.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "accion" value = "subzona"/>
	<html:hidden property = "idZona" value = "<%=(String)miHash.get(ScsSubzonaBean.C_IDZONA)%>"/>
	<html:hidden property = "idInstitucionSubzona" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
	
	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaZonas.literal.subzona">
	<table class="tablaCampos" align="center">
	
	<!-- FILA -->	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaZonas.literal.subzona"/>&nbsp;(*)
		</td>
		<td>
			<html:text name="DefinirZonasSubzonasForm" property="nombreSubzona" size="30" styleClass="box" value=""></html:text>
		</td>	
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaZonas.literal.partidoJudicial"/>&nbsp;(*)
		</td>				
		<td>
			<siga:ComboBD nombre="partidosJudiciales" tipo="partidoJudicial" clase="boxCombo"   obligatorioSinTextoSeleccionar="true" seleccionMultiple="true" filasMostrar="9" obligatorio="true" parametro="<%=dato%>"/>
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
			var nombre = document.forms[0].nombreSubzona.value;
			//var municipios = document.forms[0].municipiosSubzona.value;
			sub();
			if (document.forms[0].partidosJudiciales.value == ""){
			 alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoPartido"/>');
			 fin();
			 return false;
			 }else if ((nombre.length <= 60) && (nombre != "")) {
				//if (municipios.length<=4000) {
					window.top.returnValue="MODIFICADO";
					document.forms[0].submit();			
						
				//}
				//else alert('<siga:Idioma key="gratuita.zonasSubzonas.message.longitudMunicipios"/>');
			}
			else if (nombre == ""){
			alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
			fin();
			return false;
			}else{
			 alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
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
