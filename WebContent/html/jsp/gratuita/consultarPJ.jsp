<!-- consultarPJ.jsp -->
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties" %>
<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:
	String partidojudicial = request.getAttribute("partidoJudicial") == null?"":((String) request.getAttribute("partidoJudicial"));
	String idpartido = request.getAttribute("idPartido") == null?"":((String) request.getAttribute("idPartido"));
	String idprovincia = request.getAttribute("idProvincia") == null?"":((String) request.getAttribute("idProvincia"));
	String usuMod = request.getAttribute("usuMod") == null?"":((String) request.getAttribute("usuMod"));

	String accion = request.getSession().getAttribute("accion") == null?"":(String)request.getSession().getAttribute("accion");
	String cambiar = request.getAttribute("cambiar") == null?"":(String)request.getAttribute("cambiar");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");
	boolean desactivar = false;
	String clase = "box";
	String botones = "E";
	if (accion.equalsIgnoreCase("ver")) { desactivar = true; clase="boxConsulta"; }
	
	String localizacion = "gratuita.consultarPJ.literal.localizacion";
	if (accion.toUpperCase().equals("VER"))localizacion = "gratuita.consultarPJ.literal.localizacion.ver";
	if (accion.toUpperCase().equals("EDITAR"))localizacion = "gratuita.consultarPJ.literal.localizacion.editar";
	if (accion.toUpperCase().equals("NUEVO"))localizacion = "gratuita.consultarPJ.literal.localizacion.nuevo";
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MantenimientoPJForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.busquedaPJ.literal.titulo" 
		localizacion="<%=localizacion%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

		<!-- Para el refresco: refrescarLocal() -->
	<script>
		function refrescarLocal() {
			if (document.forms[0].accion.value != "nuevo") {
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";
				document.forms[0].submit();	
			}
			//Si entro desde nuevo para insertar y he refrescado actualizo el campo accion para que modifique en vez de insertar.
			else {
				//document.forms[0].partidoJudicial.value = "";
				document.forms[0].accion.value = "modificar";
			}
		}			
	</script>		
	
</head>

<body onload="buscarInicial()">

<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
<div id="camposRegistro" class="posicionBusquedaSolo" align="center">
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCampos" align="center">
	<html:form action="/JGR_MantenimientoPartidosJudiciales.do" method="post" target="new">
		<html:hidden property = "usuMod" value = "<%=usuMod%>"/>
		<html:hidden property = "modo" value = "<%=modo%>"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idPartido" value = "<%=idpartido%>"/>
		<html:hidden property = "idProvincia" value = "<%=idprovincia%>"/>		
		<html:hidden property = "idPoblacion" value = ""/>
		<html:hidden property = "poblacion" value = ""/>
		<html:hidden property = "cambiar" value = "<%=cambiar%>"/>	
		<html:hidden property = "accion" value = "<%=accion%>"/>	
		<html:hidden property = "actionModal" value = ""/>							

	<tr>
		<td class="labelText" width="90">	
			<siga:Idioma key="gratuita.busquedaPJ.literal.partido"/>
		</td>	
		<td class="labelText">	
			<% if (modo.equals("nuevo")) { %>
			<html:text name="MantenimientoPJForm" property="partidoJudicial" size="150" styleClass="<%=clase%>" value="" readOnly="<%=desactivar%>" ></html:text>
			<% } else { %>
			<html:text name="MantenimientoPJForm" property="partidoJudicial" size="150" styleClass="<%=clase%>" value="<%=partidojudicial%>" readOnly="<%=desactivar%>" ></html:text>			
			<% } %>
		</td>
	</tr>
	</table>	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
	<div style="position:absolute; left:0px;top:45px;z-index:4">
		<siga:ConjBotonesAccion botones="G,R" clase=""  modo="<%=accion%>"/>
	</div>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		<!-- Para cargar los datos inicialmente: buscarInicial() -->		
		function buscarInicial() {
			if (document.forms[0].modo.value != "nuevo") {
				document.forms[0].modo.value = "buscar";		
				document.forms[0].target = "resultado";
				document.forms[0].submit();
			}
		}

		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{		
			if (validateMantenimientoPJForm(document.MantenimientoPJForm)){
				if (document.forms[0].partidoJudicial.value != "")
				{
					//Si el modo era nuevo es que vengo del boton de pulsar en el boton Nuevo.
					if (document.forms[0].accion.value == "nuevo")
						document.forms[0].modo.value = "insertar"; //Creo un nuevo partido judicial
					//Sino solo quiero modificar el nombre del partido.
					else
						document.forms[0].modo.value = "modificar"; //Cambio el nombre del partido judicial
					document.forms[0].target = "submitArea";						
					document.forms[0].submit();
				}
				else 
					confirm('<siga:Idioma key="gratuita.consultarPJ.literal.introducir"/>');
			}
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
				document.forms[0].reset();
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
					style="position:absolute; width:964; height:388; z-index:2; top:70px; left: 0px">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<div style="position:absolute; left:0px;top:458px;z-index:3">
	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesAccion botones="V"  clase="" modo="<%=accion%>"/>
	<!-- FIN: BOTONES BUSQUEDA -->
	</div>
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton volver -->
		function accionVolver() 
		{
			document.forms[0].target = "mainWorkArea";
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}		
			
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->		


	</html:form>
</div>


<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
</body>
</html>