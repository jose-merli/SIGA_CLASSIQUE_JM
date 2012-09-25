<!-- busquedaModalPorTipoSJCS.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.BusquedaPorTipoSJCSForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
 <%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	

	BusquedaPorTipoSJCSForm formulario = (BusquedaPorTipoSJCSForm)request.getAttribute("BusquedaPorTipoSJCSForm");

	String tipo = formulario.getTipo();
	String titu = "", busc = "", leyenda = "";
	
	// EJG
	if (tipo != null && tipo.equalsIgnoreCase("EJG")) {
		titu    = "gratuita.busquedaPorTipoSJCS.EJG.literal.titulo";
		busc    = "gratuita.busquedaPorTipoSJCS.EJG.literal.titulo";
		leyenda = "gratuita.busquedaPorTipoSJCS.EJG.literal.leyenda";
	}
	
	// Designa
	if (tipo != null && tipo.equalsIgnoreCase("DESIGNA")) {
		titu    = "gratuita.busquedaPorTipoSJCS.Designa.literal.titulo";
		busc    = "gratuita.busquedaPorTipoSJCS.Designa.literal.titulo";
		leyenda = "gratuita.busquedaPorTipoSJCS.Designa.literal.leyenda";
	}
	//SOJ
	if (tipo != null && tipo.equalsIgnoreCase("SOJ")) {
		titu    = "gratuita.busquedaPorTipoSJCS.SOJ.literal.titulo";
		busc    = "gratuita.busquedaPorTipoSJCS.SOJ.literal.titulo";
		leyenda = "gratuita.busquedaPorTipoSJCS.SOJ.literal.leyenda";
	}

	java.util.ResourceBundle rp=java.util.ResourceBundle.getBundle("SIGA");
	String idordinario = rp.getString("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[]={idordinario,idordinario};	

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo
		titulo="<%=titu%>" 
		localizacion="<%=titu%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BusquedaPersonaJGForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="ajusteAltoBotones('resultadoModal');">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="<%=titu%>"/>
			</td>
		</tr>
	</table>

	<table  class="tablaCentralCampos"  align="center">
		<tr>				
			<td>
		
			<siga:ConjCampos leyenda="<%=leyenda%>">
		
				<table class="tablaCampos" align="center">
			
					<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="resultadoModal">
						<html:hidden name="BusquedaPorTipoSJCSForm" property="modo" value = ""/>
						<html:hidden name="BusquedaPorTipoSJCSForm" property="tipo" />
						<html:hidden name="BusquedaPorTipoSJCSForm" property="idInstitucion" />

						<!-- FILA -->
						<tr>				
							
					<% 	if (tipo != null && tipo.equalsIgnoreCase("EJG")) { %>
							<td class="labelText">
								<siga:Idioma key="gratuita.busquedaPorTipoSJCS.literal.tipo"/>
							</td>				
							<td>
								<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG"  parametro="<%=datoTipoOrdinario%>" clase="boxCombo" obligatorio="false"/>
							</td>
					<% } %>
					<% if (tipo != null && tipo.equalsIgnoreCase("DESIGNA")) { 
							String[] dato = {usr.getLocation()};
					%>
							<td class="labelText">
								<siga:Idioma key="gratuita.busquedaPorTipoSJCS.literal.turno"/>
							</td>				
							<td>
 								<siga:ComboBD nombre="turnoDesigna" tipo="turnos" clase="boxCombo" parametro="<%=dato%>" obligatorio="false" ancho="550"/>
							</td>
					<% } %>
					<% 	if (tipo != null && tipo.equalsIgnoreCase("SOJ")) { %>
							<td class="labelText">
								<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoSOJ"/>
							</td>				
							<td>
								<siga:ComboBD nombre="idTipoSOJ" tipo="tipoSOJ" clase="boxCombo" obligatorio="false"/>
							</td>
					<% } %>
					
							<td class="labelText">
								<siga:Idioma key="gratuita.busquedaPorTipoSJCS.literal.anio"/>
							</td>				
							<td>
								<html:text name="BusquedaPorTipoSJCSForm" property="anio" size="10" styleClass="box"></html:text>
							</td>
						
							<td class="labelText">
								<siga:Idioma key="gratuita.busquedaPorTipoSJCS.literal.numero"/>
							</td>				
							<td>
								<html:text name="BusquedaPorTipoSJCSForm" property="numero" size="10" styleClass="box"></html:text>
							</td>

						</tr>
				
					</html:form>
				</table>
		
			</siga:ConjCampos>
		
			</td>
		</tr>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesBusqueda botones="B"  modal="G" titulo="<%=busc%>" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{		
			sub();
			document.forms[0].modo.value="buscarPor";
			document.forms[0].submit();			
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			var aux = new Array();
			aux[0] = "cerrar";
			top.cierraConParametros(aux);
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultadoModal"
					name="resultadoModal" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>


	<siga:ConjBotonesAccion botones="C" modal="G" clase="botonesDetalle"/>

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>