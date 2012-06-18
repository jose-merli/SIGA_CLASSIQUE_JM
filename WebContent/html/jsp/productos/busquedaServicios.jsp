<!-- busquedaServicios.jsp -->
<!-- 
	 Muestra el formulario de busqueda de servicios
	 VERSIONES:
		 miguel.villegas - Creacion 3/2/2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	
	// Institucion del usuario de la aplicacion
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion

    // Botones a mostrar
	String botones = "B,N,CON";
	String titulo = "pys.busquedaServicios.literal.titulo1";	
   
   //Parametro para la busqueda:
   String [] parametroCombo = {idInstitucion};
%>

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			  titulo="pys.busquedaServicios.cabecera" 
			  localizacion="pys.busquedaServicios.ruta"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>


	<body onLoad="ajusteAlto('resultado');">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />

						<siga:ConjCampos leyenda="pys.busquedaServicios.leyenda">
						<table class="tablaCampos" align="center">	
								<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="resultado">
									<html:hidden property = "actionModal" value=""/>
									<html:hidden property = "modo" value = "buscarPor"/>
									<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>								
									<input type="hidden" name="limpiarFilaSeleccionada" value="">
									<input type="hidden" name="limpiarFilaSeleccionada" value="">

									<tr>
										<td class="labelText">
											<siga:Idioma key="pys.busquedaServicios.literal.tipo"/>&nbsp;&nbsp;
										</td>	
										<td>
											<siga:ComboBD nombre = "busquedaTipo" tipo="tipoServicio" ancho="455" clase="boxCombo" obligatorio="false" accion="Hijo:busquedaCategoria"/>
										</td>
										<td class="labelText">
											<siga:Idioma key="pys.busquedaServicios.literal.categoria"/>&nbsp;&nbsp;
										</td>	
										<td>										
											<siga:ComboBD nombre = "busquedaCategoria" ancho="250" tipo="categoriaServicio" parametro="<%=parametroCombo%>" clase="boxCombo" obligatorio="false" hijo="t"/>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="pys.busquedaServicios.literal.servicio"/>&nbsp;&nbsp;
										</td>
										<td>
											<html:text property="busquedaServicio" styleClass="box" size="70" maxlength="100" value=""></html:text>
										</td>
										<td class="labelText">
											<siga:Idioma key="pys.busquedaProductos.literal.formaPago"/>&nbsp;&nbsp;
										</td>	
										<td>										
											<siga:ComboBD nombre = "busquedaPago" tipo="cmbFormaPago" clase="boxCombo" obligatorio="false"/>
										</td>
									</tr>
									
								<!-- DCG 20.01.2006 -->
								<tr>
									<td class="labelText">
										<siga:Idioma key="pys.busquedaServicio.literal.estado"/>&nbsp;&nbsp;
									</td>	
									<td>										
										<select name="busquedaEstado" id="busquedaEstado" class="boxCombo">
											<option value="alta" selected><siga:Idioma key="pys.busquedaServicio.estado.suscripcionAutomaticaManual"/></option>
											<option value="automatica"><siga:Idioma key="pys.busquedaServicio.estado.suscripcionAutomatica"/></option>
											<option value="manual"><siga:Idioma key="pys.busquedaServicio.estado.manual"/></option>
											<option value="baja"><siga:Idioma key="pys.busquedaservicio.estado.baja"/></option>
										</select>
									</td>
								</tr>
								<!-- DCG --> 

								</html:form>	
						</table>
						</siga:ConjCampos>						
<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
	<html:hidden property="idModulo" value="<%=com.siga.beans.ConModuloBean.IDMODULO_PRODUCTOSYSERVICIOS%>"/>
	<html:hidden property="modo" value="inicio"/>
	<html:hidden property="accionAnterior" value="${path}"/>

</html:form>
	
	        <siga:ConjBotonesBusqueda botones="<%=botones%>" titulo="<%=titulo%>"/>
			
			<!-- FIN: BOTONES BUSQUEDA -->
	
		
			<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
			<script language="JavaScript">
	
				<!-- Funcion asociada a boton nuevo -->
				function nuevo() 
				{							
					document.forms[0].modo.value='nuevo';
					document.forms[0].target='submitArea';
					var resultado = ventaModalGeneral(document.forms[0].name,"G");
					//if (resultado=="MODIFICADO")
					//{
						buscar();
					//}
				}

				<!-- Funcion asociada a boton buscar -->
				function buscar() 
				{
					sub();		
					document.forms[0].modo.value='buscarPor';
					document.forms[0].target='resultado';
					document.forms[0].submit();
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

		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>