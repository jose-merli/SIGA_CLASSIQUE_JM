<!-- busquedaSeriesFacturacion.jsp -->
<!-- VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acción sobre los registros debido a que ni siquiera
	 necesita boton volver, ya que esta pagina representa UNA BUSQUEDA PRINCIPAL
-->

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
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	Hashtable datosFormulario = new Hashtable();
	String nombreAbreviado="", descripcion="", tipoProducto="", tipoServicio="", grupoClienteFijo="", grupoClientesDinamico="", iniciarBusqueda="";
	if (request.getSession().getAttribute("DATOSFORMULARIO")!=null) {
		datosFormulario = (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
		nombreAbreviado = datosFormulario.get("NOMBREABREVIADO")==null?"":(String)datosFormulario.get("NOMBREABREVIADO");
		descripcion = datosFormulario.get("DESCRIPCION")==null?"":(String)datosFormulario.get("DESCRIPCION");
		tipoProducto = datosFormulario.get("TIPOPRODUCTO")==null?"":(String)datosFormulario.get("TIPOPRODUCTO");
		tipoServicio = datosFormulario.get("TIPOSERVICIO")==null?"":(String)datosFormulario.get("TIPOSERVICIO");
		grupoClienteFijo = datosFormulario.get("GRUPOCLIENTEFIJO")==null?"":(String)datosFormulario.get("GRUPOCLIENTEFIJO");
		grupoClientesDinamico = datosFormulario.get("GRUPOCLIENTESDINAMICO")==null?"":(String)datosFormulario.get("GRUPOCLIENTESDINAMICO");
		iniciarBusqueda = datosFormulario.get("INICIARBUSQUEDA")==null?"":(String)datosFormulario.get("INICIARBUSQUEDA");		
	}	
%>	
	
<html>

	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="AsignacionConceptosFacturablesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="facturacion.asignacionDeConceptosFacturables.titulo" 
			localizacion="facturacion.busquedaSeriesFacturacion.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	</head>

<body onload="ajusteAlto('resultado');<%if (iniciarBusqueda.equals("SI")) {%> buscar() <% } %>">
	
	<table  class="tablaCentralCampos"  align="center">
	
	<tr>	
			
	<td>
	<siga:ConjCampos leyenda="facturacion.asignacionDeConceptosFacturables.titulo">
	 

		<table class="tablaCampos" align="center">

		<html:form action="/FAC_AsignacionConceptosFacturables.do" method="POST"  target="resultado" focus="nombreAbreviado">
			<html:hidden property="modo" value=""/>
			<input type="hidden" name="limpiarFilaSeleccionada" value="">

			<!-- FILA -->
			<tr> 
    			<td class="labelText" width="20%" style="text-align:left" >
					<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.nombreAbreviado"/>
				</td>
				<td>
					<html:text name="AsignacionConceptosFacturablesForm" property="nombreAbreviado" size="30" maxlength="20" styleClass="boxMayuscula" value="<%=nombreAbreviado%>"></html:text>
				</td>
        		<td class="labelText" style="text-align:left" >
					<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.descripcion"/>
				</td>
				<td>
					<html:text name="AsignacionConceptosFacturablesForm" property="descripcion" size="30" maxlength="100" styleClass="boxCombo" value="<%=descripcion%>"></html:text>
				</td>
			</tr>
				
			<tr>
       			<td class="labelText" style="text-align:left" >
					<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.tipoProducto"/>
				</td>
				<td>
					<html:text name="AsignacionConceptosFacturablesForm" property="tipoProducto" size="30" maxlength="100" styleClass="boxCombo" value="<%=tipoProducto%>"></html:text>
				</td>
        		<td class="labelText" style="text-align:left" >
					<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.tipoServicio"/>
				</td>
				<td>
					<html:text name="AsignacionConceptosFacturablesForm" property="tipoServicio" size="30" maxlength="100" styleClass="boxCombo" value="<%=tipoServicio%>"></html:text>
				</td>
			</tr>
				
			<tr>
        		<td class="labelText" style="text-align:left" >
					<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.grupoClienteFijo"/>
				</td>
				<td>
					<html:text name="AsignacionConceptosFacturablesForm" property="grupoClienteFijo" size="30" maxlength="100" styleClass="boxCombo" value="<%=grupoClienteFijo%>"></html:text>
				</td>
        		<td class="labelText" style="text-align:left" >
					<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.grupoClientesDinamico"/>
				</td>
				<td>
					<html:text name="AsignacionConceptosFacturablesForm" property="grupoClientesDinamico" size="30" maxlength="100" styleClass="boxCombo" value="<%=grupoClientesDinamico%>"></html:text>
				</td>
        	</tr>
	</html:form>        	
		</table>

	</siga:ConjCampos>
    </td>
	</tr>
	</table>


     <siga:ConjBotonesBusqueda botones="B,N" titulo=""/>


		
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">

			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				sub();		
				document.forms[0].modo.value="buscar";
				document.forms[0].target='resultado';						
				document.forms[0].submit();		
			}
		
			<!-- Funcion asociada a boton Nuevo -->
			function nuevo() 
			{		
				document.forms[0].modo.value="nuevo";
				document.forms[0].target='mainWorkArea';						
				document.forms[0].submit();
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
		<iframe	align="center" src = "<%=app%>/html/jsp/general/blank.jsp"
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0"
				marginwidth="0";					 
					class="frameGeneral">
	</iframe>
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
