<!DOCTYPE html>
<html>
<head>
<!-- busquedaSeriesFacturacion.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="struts-tiles.tld" prefix="tiles" %>

<!-- IMPORTS -->
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<%  
	Hashtable datosFormulario = new Hashtable();
	String nombreAbreviado="", descripcion="", tipoProducto="", tipoServicio="", grupoClienteFijo="", grupoClientesDinamico="", iniciarBusqueda="", sEstado="";
	if (request.getSession().getAttribute("DATOSFORMULARIO")!=null) {
		datosFormulario = (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
		nombreAbreviado = datosFormulario.get("NOMBREABREVIADO")==null?"":(String)datosFormulario.get("NOMBREABREVIADO");
		descripcion = datosFormulario.get("DESCRIPCION")==null?"":(String)datosFormulario.get("DESCRIPCION");
		tipoProducto = datosFormulario.get("TIPOPRODUCTO")==null?"":(String)datosFormulario.get("TIPOPRODUCTO");
		tipoServicio = datosFormulario.get("TIPOSERVICIO")==null?"":(String)datosFormulario.get("TIPOSERVICIO");
		grupoClienteFijo = datosFormulario.get("GRUPOCLIENTEFIJO")==null?"":(String)datosFormulario.get("GRUPOCLIENTEFIJO");
		grupoClientesDinamico = datosFormulario.get("GRUPOCLIENTESDINAMICO")==null?"":(String)datosFormulario.get("GRUPOCLIENTESDINAMICO");
		sEstado = datosFormulario.get("ESTADO")==null ? "" : (String)datosFormulario.get("ESTADO");
		iniciarBusqueda = datosFormulario.get("INICIARBUSQUEDA")==null?"":(String)datosFormulario.get("INICIARBUSQUEDA");		
	}	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
		
	<html:javascript formName="AsignacionConceptosFacturablesForm" staticJavascript="false" />  

	<siga:TituloExt titulo="facturacion.asignacionDeConceptosFacturables.titulo" localizacion="facturacion.busquedaSeriesFacturacion.literal.localizacion"/>
</head>

<body onload="ajusteAlto('resultado'); <% if (iniciarBusqueda.equals("SI")) { %> buscar() <% } %>">

	<html:form action="/FAC_AsignacionConceptosFacturables.do" method="POST"  target="resultado" focus="nombreAbreviado">
		<html:hidden property="modo" value=""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">

		<table  class="tablaCentralCampos"  align="center">	
			<tr>			
				<td>
					<siga:ConjCampos leyenda="facturacion.asignacionDeConceptosFacturables.titulo">	 
						<table class="tablaCampos" align="center">
							<tr> 
    							<td class="labelText" nowrap><siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.nombreAbreviado"/></td>
								<td><html:text name="AsignacionConceptosFacturablesForm" property="nombreAbreviado" size="20" maxlength="20" styleClass="boxMayuscula" value="<%=nombreAbreviado%>"/></td>
								
								<td class="labelText" nowrap><siga:Idioma key="facturacion.datosGenerales.literal.estado"/></td>
								<td>
									<html:select property="estado" name="DatosGeneralesForm" styleClass="boxCombo" style="width:60px;" value="<%=sEstado%>">
										<html:option value="">&nbsp;</html:option>
										<html:option value="A"><siga:Idioma key="facturacion.datosGenerales.literal.estado.alta"/></html:option>
										<html:option value="B"><siga:Idioma key="facturacion.datosGenerales.literal.estado.baja"/></html:option>
									</html:select>			
								</td>									
							</tr>
							
							<tr>        		
        						<td class="labelText" nowrap><siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.descripcion"/></td>
								<td colspan="3"><html:text name="AsignacionConceptosFacturablesForm" property="descripcion" size="100" maxlength="100" styleClass="boxCombo" value="<%=descripcion%>"/></td>
							</tr>
				
							<tr>
       							<td class="labelText" nowrap><siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.tipoProducto"/></td>
								<td><html:text name="AsignacionConceptosFacturablesForm" property="tipoProducto" size="40" maxlength="100" styleClass="boxCombo" value="<%=tipoProducto%>"/></td>
        		
        						<td class="labelText" nowrap><siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.tipoServicio"/></td>
								<td><html:text name="AsignacionConceptosFacturablesForm" property="tipoServicio" size="40" maxlength="100" styleClass="boxCombo" value="<%=tipoServicio%>"/></td>
							</tr>
				
							<tr>
        						<td class="labelText" nowrap><siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.grupoClienteFijo"/></td>
								<td><html:text name="AsignacionConceptosFacturablesForm" property="grupoClienteFijo" size="40" maxlength="100" styleClass="boxCombo" value="<%=grupoClienteFijo%>"/></td>
        		
        						<td class="labelText" nowrap><siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.grupoClientesDinamico"/></td>
								<td><html:text name="AsignacionConceptosFacturablesForm" property="grupoClientesDinamico" size="40" maxlength="100" styleClass="boxCombo" value="<%=grupoClientesDinamico%>"/></td>
        					</tr>	        	
						</table>
					</siga:ConjCampos>
    			</td>
			</tr>
		</table>
	</html:form>		

	<siga:ConjBotonesBusqueda botones="B,N" titulo=""/>

	<script language="JavaScript">

	 	//Funcion asociada a boton buscar
		function buscar() {
			sub();		
			document.forms[0].modo.value="buscar";
			document.forms[0].target='resultado';						
			document.forms[0].submit();	
			fin();
		}
		
		// Funcion asociada a boton Nuevo
		function nuevo() {	
			sub();
			document.forms[0].modo.value="nuevo";
			document.forms[0].target='mainWorkArea';						
			document.forms[0].submit();
			fin();
		}
	</script>

	<iframe	align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral"></iframe>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>