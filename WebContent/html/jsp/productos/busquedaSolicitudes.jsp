<!DOCTYPE html>
<html>
<head>
<!-- busquedaSolicitudes.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.productos.form.GestionSolicitudesForm"%>
<%@ page import="java.util.ArrayList"%>

<%  
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	String idPeticion=(String)request.getAttribute("idPeticion");
	GestionSolicitudesForm formGestionSolicitudes = (GestionSolicitudesForm) ses.getAttribute("GestionSolicitudesForm");
	String fechaDesde="", fechaHasta="";
	if (formGestionSolicitudes != null) {
		fechaDesde = formGestionSolicitudes.getBuscarFechaDesde();
		if (fechaDesde != null) {
			fechaDesde = GstDate.getFormatedDateShort("", fechaDesde);
		}
		
		fechaHasta = formGestionSolicitudes.getBuscarFechaHasta();
		if (fechaHasta!=null) {
			fechaHasta = GstDate.getFormatedDateShort("", fechaHasta);
		}
	}	
%>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<siga:Titulo titulo="pys.gestionSolicitudes.cabecera" localizacion="pys.gestionSolicitudes.ruta"/>

	<html:javascript formName="GestionSolicitudesForm" staticJavascript="false" />  
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>		
</head>

<body onLoad="ajusteAlto('resultado');inicio();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	<html:form action="/PYS_GestionarSolicitudes.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">

		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="pys.gestionSolicitudes.leyenda">	
						<table class="tablaCampos" align="center">
							<tr>				
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.tipo"/>
								</td>
								<td>
									<html:select property="buscarTipoPeticion" styleClass="boxCombo">
										<html:option value=""></html:option>
										<html:option value="<%=ClsConstants.TIPO_PETICION_COMPRA_ALTA%>"><siga:Idioma key="pys.tipoPeticion.alta"/></html:option>
										<html:option value="<%=ClsConstants.TIPO_PETICION_COMPRA_BAJA%>"><siga:Idioma key="pys.tipoPeticion.baja"/></html:option>
									</html:select>
								</td>

								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.idPeticion"/>
								</td>
								<td>
									<html:text styleClass="box" property="buscarIdPeticionCompra" maxlength="10" />
								</td>

								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.estadoPeticion"/>
								</td>				
                 				<td>
<% 
									ArrayList listaEstados = new ArrayList();
									listaEstados.add(formGestionSolicitudes.getBuscarEstadoPeticion());
%>
                 				
                 					<siga:ComboBD nombre="buscarEstadoPeticion" tipo="cmbEstadoPeticion" elementoSel="<%=listaEstados%>" clase="boxCombo" />
                 				</td>
							</tr>

							<tr>				
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.fechaDesde"/>
								</td>
								<td>
									<siga:Fecha nombreCampo="buscarFechaDesde" valorInicial="<%=fechaDesde%>"/>
								</td>
		
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.fechaHasta"/>
								</td>
								<td>
									<siga:Fecha nombreCampo="buscarFechaHasta" valorInicial="<%=fechaHasta%>"/>
								</td>
					
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.facturada.literal"/>
								</td>				
                 	
                 				<td>
                 					<html:select name="GestionSolicitudesForm" property="facturada" styleId="facturada" styleClass="boxCombo">
                 						<html:option value=""></html:option>                 			               		
                 						<html:option value="0"><siga:Idioma key="pys.gestionSolicitudes.facturada.opcionSinFacturar"/></html:option>
                 						<html:option value="1"><siga:Idioma key="pys.gestionSolicitudes.facturada.opcionParcial"/></html:option>
                 						<html:option value="2"><siga:Idioma key="pys.gestionSolicitudes.facturada.opcionTotal"/></html:option>  
                 					</html:select>
								</td>
							</tr>
		
							<tr>				
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.nColegiado"/>
								</td>
								<td>
									<html:text styleClass="box" property="buscarNColegiado" maxlength="20"/>
								</td>
						
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.nifcif"/>
								</td>
								<td>
									<html:text styleClass="box" property="buscarNifcif" maxlength="20"/>
								</td>
							</tr>
				
							<tr>				
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.nombre"/>
								</td>
								<td>
									<html:text styleClass="box" property="buscarNombre" maxlength="100"/>
								</td>
						
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.apellido1"/>
								</td>
								<td>
									<html:text styleClass="box" property="buscarApellido1" maxlength="100"/>
								</td>
				
								<td class="labelText">
									<siga:Idioma key="pys.gestionSolicitudes.literal.apellido2"/>
								</td>
								<td>
									<html:text styleClass="box" property="buscarApellido2" maxlength="100"/>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
		</table>
	</html:form>
	
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
		<html:hidden property="idModulo" value="<%=com.siga.beans.ConModuloBean.IDMODULO_PRODUCTOSYSERVICIOS%>"/>
		<html:hidden property="modo" value="inicio"/>
		<html:hidden property="accionAnterior" value="${path}"/>
	</html:form>

	<siga:ConjBotonesBusqueda botones="B,CON" titulo="pys.gestionSolicitudes.titulo"/>

	<script language="JavaScript">
		//Funcion asociada a boton buscar
		function buscar() {
			sub();		
			if (compararFecha (document.GestionSolicitudesForm.buscarFechaDesde, document.GestionSolicitudesForm.buscarFechaHasta) == 1) {
				mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>';
				alert(mensaje);
				fin();
				return false;
			}
	         
			// Validamos los errores
			if (!validateGestionSolicitudesForm(document.GestionSolicitudesForm)){
				return false;
			}
			document.GestionSolicitudesForm.modo.value = "buscarInit";
			document.GestionSolicitudesForm.submit();
		}
		
		function inicio() {
<% 
			if (idPeticion!=null && !idPeticion.equals("")) {
%>
		        GestionSolicitudesForm.buscarIdPeticionCompra.value=<%=idPeticion%>;
<%
			}
			  
			if (request.getParameter("buscar")!=null && request.getParameter("buscar").equals("true")) {
%>
			    buscar();
<%
			}
%>	
		}
		
		function consultas() {		
			document.RecuperarConsultasForm.submit();
		}
	</script>

	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"					 
					class="frameGeneral"></iframe>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>			
</body>
</html>