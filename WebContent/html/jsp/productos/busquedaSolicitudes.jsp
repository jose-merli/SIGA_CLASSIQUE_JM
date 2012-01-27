<!-- busquedaSolicitudes.jsp -->
<!-- EJEMPLO DE VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acción sobre los registros debido a que nisiquiera
	 necesita boton volver, ya que esta pagina representa UNA BSQUEDA PRINCIPAL
-->

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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.productos.form.GestionSolicitudesForm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String idPeticion=(String)request.getAttribute("idPeticion");
	String buscar = (String) request.getAttribute("buscar");
	if (buscar==null) {
		buscar = "";
	} else {
		buscar = "busqueda();";
	}
	
	GestionSolicitudesForm formuAux = (GestionSolicitudesForm) request.getSession().getAttribute("GestionSolicitudesForm"); 
	String fechaDesde = "";
	if (formuAux!=null) {
		fechaDesde = formuAux.getBuscarFechaDesde();
		if (fechaDesde!=null) {
			fechaDesde=	GstDate.getFormatedDateShort("", fechaDesde);
		}
	}
	String fechaHasta = "";
	if (formuAux!=null) {
		fechaHasta = formuAux.getBuscarFechaHasta();
		if (fechaHasta!=null) {
			fechaHasta=	GstDate.getFormatedDateShort("", fechaHasta);
		}
	}
%>	
	
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="pys.gestionSolicitudes.cabecera" 
		localizacion="pys.gestionSolicitudes.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- Validaciones en Cliente -->
	<html:javascript formName="GestionSolicitudesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
</head>


<body onLoad="ajusteAlto('resultado');inicio();">
<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />

<table class="tablaCentralCampos" align="center">

	<html:form action="/PYS_GestionarSolicitudes.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">

		<tr><td>
			<siga:ConjCampos leyenda="pys.gestionSolicitudes.leyenda">	
			<table class="tablaCampos" align="center">
				<tr>				
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.tipo"/></td>
					<td><html:select property="buscarTipoPeticion" styleClass="boxCombo">
								<html:option value=""></html:option>
								<html:option value="<%=ClsConstants.TIPO_PETICION_COMPRA_ALTA%>"><siga:Idioma key="pys.tipoPeticion.alta"/></html:option>
								<html:option value="<%=ClsConstants.TIPO_PETICION_COMPRA_BAJA%>"><siga:Idioma key="pys.tipoPeticion.baja"/></html:option>
						</html:select>
					</td>

					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.idPeticion"/></td>
					<td><html:text styleClass="box" property="buscarIdPeticionCompra" maxlength="10" /></td>
					<% ArrayList listaEstados = new ArrayList();
					   if (request.getParameter("buscar")!=null && request.getParameter("buscar").equals("true")){
					     listaEstados.add("");
					   }else{
					     listaEstados.add("10");
					   }%><!-- 10=estado PENDIENTE, que es el estado que queremos que aparezca seleccionado por defecto 
					                            en el combo-->
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.estadoPeticion"/></td>				
                 	<td><siga:ComboBD nombre = "buscarEstadoPeticion" tipo="cmbEstadoPeticion" elementoSel="<%=listaEstados%>" clase="boxCombo" /></td>
				</tr>

				<tr>				
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.fechaDesde"/></td>
					<td><html:text styleClass="box" property="buscarFechaDesde" maxlength="10" readonly="true" value="<%=fechaDesde%>"/><a href='javascript://'onClick="return showCalendarGeneral(buscarFechaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
		
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.fechaHasta"/></td>
					<td><html:text styleClass="box" property="buscarFechaHasta" maxlength="10" readonly="true" value="<%=fechaHasta%>"/><a href='javascript://'onClick="return showCalendarGeneral(buscarFechaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
					
				</tr>
		
				<tr>				
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.nColegiado"/></td>
					<td><html:text styleClass="box" property="buscarNColegiado" maxlength="20"/></td>
			
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.nifcif"/></td>
					<td><html:text styleClass="box" property="buscarNifcif" maxlength="20"/></td>
				</tr>
	
				<tr>				
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.nombre"/></td>
					<td><html:text styleClass="box" property="buscarNombre" maxlength="100"/></td>
			
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.apellido1"/></td>
					<td><html:text styleClass="box" property="buscarApellido1" maxlength="100"/></td>
	
					<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.apellido2"/></td>
					<td><html:text styleClass="box" property="buscarApellido2" maxlength="100"/></td>
				</tr>
				
			</table>
		</siga:ConjCampos>	

		</td></tr>

	</html:form>
	
	</table>
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
	<html:hidden property="idModulo" value="<%=com.siga.beans.ConModuloBean.IDMODULO_PRODUCTOSYSERVICIOS%>"/>
	<html:hidden property="modo" value="inicio"/>
	<html:hidden property="accionAnterior" value="${path}"/>

</html:form>
	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		
		<siga:ConjBotonesBusqueda botones="B,CON" titulo="pys.gestionSolicitudes.titulo"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
	

		<!-- Funcion asociada a boton buscar -->
		
		function buscar() 
		{
				sub();		
				if (compararFecha (document.GestionSolicitudesForm.buscarFechaDesde, document.GestionSolicitudesForm.buscarFechaHasta) == 1) {
				
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					
					alert(mensaje);
					return false;
				}
	         
				// Validamos los errores ///////////
				if (!validateGestionSolicitudesForm(document.GestionSolicitudesForm)){
					return false;
				}
				document.GestionSolicitudesForm.modo.value = "buscarInit";
				document.GestionSolicitudesForm.submit();
		}
		function busqueda() 
		{		
			buscar();
		}
		
		function inicio(){
			  <% if (idPeticion!=null && !idPeticion.equals("")) {%>
			     
			        GestionSolicitudesForm.buscarIdPeticionCompra.value=<%=idPeticion%>;
				 
			   <%}
			  if (request.getParameter("buscar")!=null&&request.getParameter("buscar").equals("true")){%>
			    buscar();
			  <%}%>	
					
				 
			 
			  
			 
			}
		function consultas() 
		{		
			document.RecuperarConsultasForm.submit();
		
		}
	</script>
	
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

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
			
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>