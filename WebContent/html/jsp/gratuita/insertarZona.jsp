<!-- insertarZona.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
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
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String modoZona = request.getAttribute("modoZona")==null?"insertar":(String)request.getAttribute("modoZona");
	String botones = "V,N";
	if (modoZona.equalsIgnoreCase("INSERTAR"))
		botones = "V";
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.zonaSubzona.titulo.nuevaZona" 
		localizacion="gratuita.grupoZonas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onload="ajusteAlto('resultado');" class="tablaCentralCampos">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	
	<html:form action="/JGR_DefinirZonasSubzonas.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "accion" value = "zona"/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "idZona" value =""/>
	<html:hidden property = "idInstitucionZona" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
	<html:hidden property = "modoZona" value = "<%=modoZona%>"/>
	
	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaZonas.literal.zona">
	<table align="center" width="100%">
	
	<!-- FILA -->	
	<tr>	
	<td class="labelText" valign="top">
		<siga:Idioma key="gratuita.busquedaZonas.literal.zona"/>&nbsp;(*)
	</td>
	<td valign="top">
		<html:text name="DefinirZonasSubzonasForm" property="nombreZona" size="60" styleClass="box" value=""></html:text>
	</td>
	</tr>
	</table>
	</siga:ConjCampos>
	</html:form>

		

	<siga:ConjBotonesAccion botones="G,R" titulo="gratuita.busquedaZonas.literal.zonas" clase="botonesSeguido"/>		

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function buscar() 
		{		
			document.forms[0].target = "mainWorkArea";
			document.forms[0].accion.value = "zonaEdicion";
			document.forms[0].modo.value = "Editar";
			document.forms[0].submit();			
		}

		function listado() 
		{		
			document.forms[0].target = "resultado";
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
		}		
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar() 
		{
						
			sub();
			var nombre = document.forms[0].nombreZona.value;
			//var municipios = document.forms[0].municipiosZona.value;
			
			if ((nombre.length <= 60) && (nombre != "")) {
				//if (municipios.length<=4000) {
					document.forms[0].target="submitArea";
					document.forms[0].accion.value = "zona";
					document.forms[0].modo.value = "insertar";
					document.forms[0].submit();
				//}
				//else alert('<siga:Idioma key="gratuita.zonasSubzonas.message.longitudMunicipios"/>');
			}
			else if (nombre == ""){ 
				alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
				fin();
				return false;
			}
			else{ 
				alert('<siga:Idioma key="gratuita.zonasSubzonas.message.requeridoNombre"/>');
				fin();
				return false;
			}	
		} 
				
		function refrescarLocal(){
			buscar();
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
	
	<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle" />
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		//Asociada al boton Nuevo -->		
		function accionNuevo() 
		{
			alert('<siga:Idioma key="gratuita.insertarZona.message.insetarZonaPrimero"/>');
		}

		//Asociada al boton Volver -->
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
