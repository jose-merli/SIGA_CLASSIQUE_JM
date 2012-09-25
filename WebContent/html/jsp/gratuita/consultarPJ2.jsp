<!-- consultarPJ2.jsp -->
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
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Properties"%>
<!-- JSP --> 
<% 
	//FORMULARIO
	
	
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
	ArrayList instituciones=request.getAttribute("arrayInstituciones")==null?new ArrayList():(ArrayList)request.getAttribute("arrayInstituciones");
	ArrayList partidoSe= new ArrayList();
	
	if (request.getAttribute("idPartido")!=null)
	{
		partidoSe.add((String)request.getAttribute("idPartido"));
	}
	
	if (accion.equalsIgnoreCase("ver")) { desactivar = true; clase="boxConsulta"; }
	
	String titulo = "gratuita.busquedaPJ.literal.titulo";
	//if (accion.toUpperCase().equals("VER"))localizacion = "gratuita.consultarPJ.literal.localizacion.ver";
	//if (accion.toUpperCase().equals("EDITAR"))localizacion = "gratuita.consultarPJ.literal.localizacion.editar";
	if (accion.toUpperCase().equals("NUEVO"))titulo = "gratuita.consultarPJ.nuevo";
	
	String idInstitucion=usr.getLocation();
	String nomInstitucion = "";
	if (!idInstitucion.equals("2000")) {
		// No es CGAE
		nomInstitucion = (String) request.getAttribute("nomInstitucion");
	}
	
	// TRATAMIENTO DEL COMBO MULTIPLE
	//  tratamiento de readonly
	String estiloCaja = "boxCombo";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	
		if (accion.equalsIgnoreCase("ver"))
		{
			readonly = "true";
			estiloCaja = "boxConsulta";
			breadonly=true;
		}
		
		String[] parametros = new String[1];
		parametros[0] = idInstitucion;
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MantenimientoPJForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="<%=titulo%>" 
		localizacion="gratuita.consultarPJ.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

		<!-- Para el refresco: refrescarLocal() -->
	<script>
		function refrescarLocal() {
			document.forms[0].target="mainWorkArea";	
			document.forms[0].modo.value="editar";	
			document.forms[0].submit();	
		}
		
		function cargarProvincias()
		{
			if ("<%=modo%>"=="nuevo")
			{
				document.forms[0].idPartido.value=document.forms[0].partidoJudicial.value;
			}

				document.forms[0].modo.value = "buscar";		
				document.forms[0].target = "resultado";
				document.forms[0].submit();
		}
	</script>		
	
</head>

<body onload="ajusteAltoBotones('resultado');buscarInicial()">

	<fieldset>	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCampos" align="center">
	<html:form action="/JGR_MantenimientoPartidosJudiciales.do" method="post">
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
		<td class="labelText" width="150">	
			<siga:Idioma key="gratuita.busquedaPJ.literal.partido"/>&nbsp;(*)
		</td>	
		<td class="labelText" align="left">	
		<%if (modo.equalsIgnoreCase("nuevo")){%>
		<%if (idInstitucion.equals("2000")) {%>
			<siga:ComboBD nombre = "partidoJudicial" tipo="partidoJudicial3" clase="<%=estiloCaja%>" obligatorioSinTextoSeleccionar="false" seleccionMultiple="false" readonly="<%=readonly%>" parametro="<%=parametros%>" elementoSel="<%=partidoSe%>" ancho="400" accion="cargarProvincias();"/>
		<%} else {%>
			<siga:ComboBD nombre = "partidoJudicial" tipo="partidoJudicial2" clase="<%=estiloCaja%>" obligatorioSinTextoSeleccionar="false" seleccionMultiple="false" readonly="<%=readonly%>" parametro="<%=parametros%>" elementoSel="<%=partidoSe%>" ancho="400" accion="cargarProvincias();"/>
		<%}%>
		<%}else{%>
			<html:text property="partidoJudicial" size="60" styleClass="boxConsulta" readOnly="true" value="<%=partidojudicial%>" ></html:text>
		<%}%>
		</td>
	</tr>
	<tr>	
		<td class="labelText"  width="150">	
		<% if (!idInstitucion.equals("2000")) { %>
			<!-- no es CGAE -->
			<siga:Idioma key="gratuita.partidosJudiciales.literal.institucion"/>
		<% } else { %>
			<siga:Idioma key="gratuita.partidosJudiciales.literal.instituciones"/>
		<% } %>
		</td>
		<td class="labelText" align="left">
		<% if (!idInstitucion.equals("2000")) { %>
			<!-- no es CGAE -->
			<html:hidden name="MantenimientoPJForm" property="comboInstituciones" readOnly="true" value="<%=idInstitucion %>" ></html:hidden>
			<html:text property="nombreInstitucion" size="100" styleClass="boxConsulta" readOnly="true" value="<%=nomInstitucion %>"></html:text>
		<% } else { %>
			<siga:ComboBD nombre = "comboInstituciones" tipo="cmbInstitucionPartido" clase="<%=estiloCaja %>" obligatorioSinTextoSeleccionar="true" seleccionMultiple="true" readOnly="<%=readonly%>" elementoSel="<%=instituciones%>" ancho="400"/>
		<% } %>
		</td>
	</tr>
	</html:form>
	</table>	
	</fieldset>

<% if (!idInstitucion.equals("2000") && !accion.equalsIgnoreCase("nuevo")) {%>
		<siga:ConjBotonesAccion botones="V"  clase="botonesSeguido" modo="<%=accion%>"/>
<%} else {%>
		<siga:ConjBotonesAccion botones="V,G,R"  clase="botonesSeguido" modo="<%=accion%>"/>
<%}%>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		<!-- Para cargar los datos inicialmente: buscarInicial() -->		
		function buscarInicial() {
			var aux = "";
			if (document.forms[0].modo.value != "nuevo") {
				//document.forms[0].modo.value = "buscar";		
				//document.forms[0].target = "resultado";
				//document.forms[0].submit();
				
				cargarProvincias();
			}
		}
	</script>

	<script language="JavaScript">

		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{		
			sub();
			if (validateMantenimientoPJForm(document.MantenimientoPJForm)){
					if (document.forms[0].partidoJudicial.value != "")
					{
						if(document.forms[0].comboInstituciones.value != "")
						{
							//Si el modo era nuevo es que vengo del boton de pulsar en el boton Nuevo.
							if (document.forms[0].accion.value == "nuevo") {
								document.forms[0].target = "submitArea";						
								document.forms[0].modo.value = "insertar"; //Creo un nuevo partido judicial
								document.forms[0].submit();
							}
							//Sino solo quiero modificar el nombre del partido.
							else 
							{					
								document.forms[0].modo.value = "modificar"; //Cambio el nombre del partido judicial
								document.forms[0].target = "submitArea";						
								document.forms[0].submit();
							}
						}
						else{ 
							alert("Intruduzca al menos una institución");
							fin();
							return false;
						}
					}
					else{ 
						alert('<siga:Idioma key="gratuita.consultarPJ.literal.introducir"/>');
						fin();
						return false;
					}
				}else{
					fin();
					return false;
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

	<div style="position:absolute; width:100%;left:0px;bottom:0px;">
		<% if (modo.equals("nuevo") || modo.equals("ver")) { %>
			<siga:ConjBotonesBusqueda  modal="G" titulo="" />
		<% }else {%>
			<siga:ConjBotonesBusqueda botones="AN"  modal="G" titulo="" />
		<% }%>
	</div>
	

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

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton volver -->
		function accionVolver() 
		{
			document.forms[0].target = "mainWorkArea";
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}
		
		<!-- Funcion asociada a boton Anhadir poblaciones -->
		function anadirPob() 
		{		
			//document.forms[0].accion.value = "modalNuevo";
			document.forms[0].modo.value = "mostrarListaPob";
			/*document.forms[0].target="_blank";
			document.forms[0].submit();*/
			var salida = ventaModalGeneral(document.forms[0].name,"P"); 			
			if (salida && salida == "MODIFICADO") 
				refrescarLocal();
		}			
			
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->		




<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
</body>
</html>