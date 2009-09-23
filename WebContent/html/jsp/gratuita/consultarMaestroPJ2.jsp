<!-- consultarMaestroPJ2.jsp -->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.ArrayList"%>

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
	String idInstitucionPropietario = request.getAttribute("idInstitucionPropietario") == null?"":(String)request.getAttribute("idInstitucionPropietario");

	ArrayList aInstitucionPropietario = new ArrayList();
		
	String accion = request.getSession().getAttribute("accion") == null?"":(String)request.getSession().getAttribute("accion");
	String cambiar = request.getAttribute("cambiar") == null?"":(String)request.getAttribute("cambiar");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");
	boolean desactivar = false;
	String clase = "box";
	String botones = "E";
	
	//if (accion.equalsIgnoreCase("ver")) { desactivar = true; clase="boxConsulta"; }
	if (!accion.equalsIgnoreCase("nuevo")) { desactivar = true; clase="boxConsulta"; }
	
	String titulo = "gratuita.busquedaMaestroPJ.literal.titulo";
	if (accion.toUpperCase().equals("NUEVO"))titulo = "gratuita.consultarMaestroPJ.nuevo";
	
	String idInstitucion=usr.getLocation();
	
	boolean bCGAE = false;
	if (idInstitucion.equals("2000"))
	{
		// Es CGAE
		bCGAE = true;
		
		if (accion.equalsIgnoreCase("nuevo"))
		{
			aInstitucionPropietario.add("2000");
		}
		
		else
		{
			aInstitucionPropietario.add(idInstitucionPropietario);
		}
	}
	
	else
	{
		aInstitucionPropietario.add(idInstitucionPropietario);
	}
	
	// TRATAMIENTO DEL COMBO MULTIPLE
	//  tratamiento de readonly
	String estiloCaja = "";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	// caso de accion
	
		estiloCaja = "boxCombo";
		readonly = "false";
		breadonly = false;
		checkreadonly = " ";
		
		if (accion.equalsIgnoreCase("ver"))
		{
			readonly = "true";
			estiloCaja = "boxConsulta";
		}
		
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MantenimientoMaestroPJForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="<%=titulo%>" localizacion="gratuita.consultarPJ.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

		<!-- Para el refresco: refrescarLocal() -->
	<script>
		function refrescarLocal() {
			document.forms[0].target="mainWorkArea";	
			document.forms[0].modo.value="editar";	
			document.forms[0].submit();	
		}			
	</script>		
	
</head>

<body onload="ajusteAltoBotones('resultado');buscarInicial()">

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<fieldset>
	
	<table class="tablaCentralCampos" align="center">
	<html:form action="/JGR_MantenimientoMaestroPJ.do" method="post">
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
		<td class="labelText" >	
			<siga:Idioma key="gratuita.busquedaMaestroPJ.literal.partido"/>&nbsp;(*)
		</td>	
		<td class="labelText" align="left">	
			<% if (modo.equals("nuevo")) { %>
			<html:text name="MantenimientoMaestroPJForm" property="partidoJudicial" size="100" styleClass="<%=clase%>" value="" readOnly="<%=desactivar%>" ></html:text>
			<% } else { %>
			<html:text name="MantenimientoMaestroPJForm" property="partidoJudicial" size="100" styleClass="<%=clase%>" value="<%=partidojudicial%>" readOnly="<%=desactivar%>" ></html:text>			
			<% } %>
		</td>
	</tr>
	<tr>	
		<td class="labelText" >	
			<siga:Idioma key="gratuita.busquedaMaestroPJ.literal.institucion"/>&nbsp;(*)
		</td>
		<td class="labelText" align="left">
			<siga:ComboBD nombre = "idInstitucionPropietario" tipo="cmbInstitucionPartido" clase="<%=estiloCaja%>" obligatorioSinTextoSeleccionar="true" seleccionMultiple="false" readonly="<%=readonly %>"  elementoSel="<%=aInstitucionPropietario%>" />
		</td>
	</tr>
	</html:form>
	</table>	
	</fieldset>
	
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
		<siga:ConjBotonesAccion botones="V,G,R" clase="botonesSeguido"  clase="" modo="<%=accion%>"/>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		<!-- Para cargar los datos inicialmente: buscarInicial() -->		
		function buscarInicial() {
			var aux = "";
			if (document.forms[0].modo.value != "nuevo") {
				document.forms[0].modo.value = "buscar";		
				document.forms[0].target = "resultado";
				document.forms[0].submit();
				
			}
		}
	</script>

	<script language="JavaScript">

		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{		
			sub();
			if (validateMantenimientoMaestroPJForm(document.MantenimientoMaestroPJForm)){
					if (document.forms[0].partidoJudicial.value != "")
					{
						if(document.forms[0].idInstitucionPropietario.value != "")
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
						else {
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
		<% if (modo.equals("nuevo") || modo.equals("ver")) { %>
			<siga:ConjBotonesBusqueda  modal="G" titulo=""/>
		<% }else if (idInstitucion.equals("2000") || idInstitucion.equals(idInstitucionPropietario)){%>
		<siga:ConjBotonesBusqueda botones="AN"  modal="G" titulo=""/>
		<% } else {%>
		<siga:ConjBotonesBusqueda  modal="G" titulo="" />
		<% }%>

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