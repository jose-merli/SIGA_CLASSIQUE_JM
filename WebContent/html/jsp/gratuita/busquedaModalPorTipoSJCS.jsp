<!DOCTYPE html>
<html>
<head>
<!-- busquedaModalPorTipoSJCS.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri="struts-bean.tld" 	prefix="bean"%>
<%@ taglib uri="struts-html.tld" 	prefix="html"%>
<%@ taglib uri="c.tld" 				prefix="c"%>

<!-- IMPORTS -->
<%@page import="org.redabogacia.sigaservices.app.util.SIGAReferences"%>
<%@page import="org.redabogacia.sigaservices.app.util.ReadProperties"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.BusquedaPorTipoSJCSForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String[] dato = {usr.getLocation()};
	ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	String idordinario = rp.returnProperty("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[]={idordinario,idordinario};	
	
	String anioFiltro = UtilidadesBDAdm.getYearBD("");
%>	
	
	<bean:define id="formulario" name="BusquedaPorTipoSJCSForm"  scope="request"/>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
		<c:choose>
			<c:when test="${not empty formulario.tipo && formulario.tipo=='EJG'}">
				<c:set var="titu" value="gratuita.busquedaPorTipoSJCS.EJG.literal.titulo" />
				<c:set var="busc" value="gratuita.busquedaPorTipoSJCS.EJG.literal.titulo" />
				<c:set var="leyenda" value="gratuita.busquedaPorTipoSJCS.EJG.literal.leyenda" />				
			</c:when>
			
			<c:when test="${not empty formulario.tipo && formulario.tipo=='SOJ'}">
				<c:set var="titu" value="gratuita.busquedaPorTipoSJCS.SOJ.literal.titulo" />
				<c:set var="busc" value="gratuita.busquedaPorTipoSJCS.SOJ.literal.titulo" />
				<c:set var="leyenda" value="gratuita.busquedaPorTipoSJCS.SOJ.literal.leyenda" />	
			</c:when>
			
			<c:when test="${not empty formulario.tipo && formulario.tipo=='DESIGNA'}">
				<c:set var="titu" value="gratuita.busquedaPorTipoSJCS.Designa.literal.titulo" />
				<c:set var="busc" value="gratuita.busquedaPorTipoSJCS.Designa.literal.titulo" />
				<c:set var="leyenda" value="gratuita.busquedaPorTipoSJCS.Designa.literal.leyenda" />	
			</c:when>			
		</c:choose>
	<siga:Titulo titulo="${titu}" localizacion="${titu}"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="BusquedaPersonaJGForm" staticJavascript="false" />  		
	<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
</head>

<body onLoad="ajusteAltoBotones('resultadoModal');">
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="${titu}"/>
			</td>
		</tr>
	</table>

	<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="resultadoModal">
		<html:hidden name="BusquedaPorTipoSJCSForm" property="modo" value = ""/>
		<html:hidden name="BusquedaPorTipoSJCSForm" property="tipo" />
		<html:hidden name="BusquedaPorTipoSJCSForm" property="idInstitucion" />

		<!-- FILA -->
		<tr>		
			<c:choose>
				<c:when test="${not empty formulario.tipo && formulario.tipo=='EJG'}">	
					<siga:ConjCampos leyenda="${leyenda}">									
						<table align="center" width="100%" border="0" >
							<tr>
								<td class="labelText" width="16%">
									<siga:Idioma key="gratuita.busquedaEJG.literal.anyo" />
									/
									<siga:Idioma key="gratuita.busquedaEJG.literal.codigo" />
								</td>
								<td width="15%">
									<html:text name="BusquedaPorTipoSJCSForm" styleClass="box" property="anio" style="width:40px" maxlength="4" value="<%=anioFiltro%>" />
									/
									<html:text name="BusquedaPorTipoSJCSForm" styleClass="box" property="numero" size="8" maxlength="10" />
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG" />
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG" parametro="<%=datoTipoOrdinario%>"clase="boxCombo" obligatorio="false" ancho="200"/>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaEJG.literal.EJGColegio" />
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="idTipoEJGColegio" tipo="tipoEJGColegio" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" ancho="200"/>
								</td>
							</tr>
						</table>	
					</siga:ConjCampos>	
						
					<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.solicitante">						
						<table align="center" width="100%">
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaEJG.literal.nif" />
								</td>
								<td class="labelText">
									<html:text name="BusquedaPorTipoSJCSForm" property="nif" size="10" maxlength="20" styleClass="box" />
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaEJG.literal.nombre" /> <html:text name="BusquedaPorTipoSJCSForm" property="nombre" size="26" maxlength="100" styleClass="box" />
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido1" /> <html:text name="BusquedaPorTipoSJCSForm" property="apellido1" size="26" maxlength="100" styleClass="box" />
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido2" /> <html:text name="BusquedaPorTipoSJCSForm" property="apellido2" size="26" maxlength="100" styleClass="box" />
								</td>
							</tr>
						</table>
					</siga:ConjCampos>								
				</c:when>
				
				<c:when test="${not empty formulario.tipo && formulario.tipo=='SOJ'}">
					<siga:ConjCampos leyenda="${leyenda}">
						<table  align="center" width="100%" border="0">  		
							<tr>	
								<td class="labelText"  width="100px" >
									<siga:Idioma key="gratuita.busquedaSOJ.literal.anyo"/> 
									/ 
									<siga:Idioma key="gratuita.busquedaSOJ.literal.codigo"/>	
								</td>
								<td colspan="3">		
									<html:text name="BusquedaPorTipoSJCSForm" property="anio" size="4" maxlength="4" styleClass="box" value="<%=anioFiltro%>" /> 
									/ 
									<html:text name="BusquedaPorTipoSJCSForm" property="numero" size="5" maxlength="10" styleClass="box" />
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoSOJ"/>
								</td>				
								<td>
									<siga:ComboBD nombre="idTipoSOJ" tipo="tipoSOJ" clase="boxCombo" obligatorio="false"/>
								</td>	
							</tr>
						</table>
					</siga:ConjCampos>
						
					<siga:ConjCampos leyenda="gratuita.busquedaSOJ.literal.solicitante">
						<table  align="center" width="100%">
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaSOJ.literal.nif"/>		
								</td>
								<td class="labelText">
									<html:text name="BusquedaPorTipoSJCSForm" property="nif" size="10" maxlength="20" styleClass="box" />
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
								</td>
								<td class="labelText">
									<html:text name="BusquedaPorTipoSJCSForm" property="nombre" size="15" maxlength="100" styleClass="box" />
								</td>
								
								<td class="labelText">	
									<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido1"/>
								</td>		
								<td class="labelText">
									<html:text name="BusquedaPorTipoSJCSForm" property="apellido1" size="15" maxlength="100" styleClass="box" />
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido2"/>
								</td>
								<td class="labelText"  colspan="2">
									<html:text name="BusquedaPorTipoSJCSForm" property="apellido2" size="15" maxlength="100" styleClass="box" />
								</td>	
							</tr>	
						</table>
					</siga:ConjCampos>						
				</c:when>
				
				<c:when test="${not empty formulario.tipo && formulario.tipo=='DESIGNA'}">
					<siga:ConjCampos leyenda="${leyenda}">
						<table width="100%" border="0" >
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaSOJ.literal.anyo"/> / <siga:Idioma key="gratuita.busquedaSOJ.literal.codigo"/>
								</td>
								<td>	
									<html:text name="BusquedaPorTipoSJCSForm" property="anio"  style="width:40px" maxlength="4" styleClass="box" value="<%=anioFiltro%>" /> 
									/ 
									<html:text name="BusquedaPorTipoSJCSForm" property="numero" size="5" maxlength="10" styleClass="box" /> 
								</td>
								
								<td>
									&nbsp;
								</td>
								<td>	
									&nbsp;
								</td>
									
								<td class="labelText">
									<siga:Idioma key="gratuita.busquedaPorTipoSJCS.literal.turno"/>
								</td>				
								<td>
	 								<siga:ComboBD nombre="turnoDesigna" tipo="turnos" clase="boxCombo" parametro="<%=dato%>" obligatorio="false" ancho="550"/>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>	
					
					<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.defendido">
						<table width="100%">
							<tr>
								<td class="labelText">	
									<siga:Idioma key="expedientes.auditoria.literal.nif"/>
								</td>	
								<td>
									<html:text name="BusquedaPorTipoSJCSForm" property="nif" size="10" maxlength="10" styleClass="box" />
								</td>
								
								<td class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
								</td>
								<td>	
									<html:text name="BusquedaPorTipoSJCSForm" property="nombre" size="15" maxlength="100" styleClass="box" />
								</td>	
								
								<td class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.primerapellido"/>
								</td>
								<td>	
									<html:text name="BusquedaPorTipoSJCSForm" property="apellido1" size="15" maxlength="100" styleClass="box" />
								</td>	
								
								<td class="labelText">	
									<siga:Idioma key="expedientes.auditoria.literal.segundoapellido"/>
								</td>
								<td>
									<html:text name="BusquedaPorTipoSJCSForm" property="apellido2" size="15" maxlength="100" styleClass="box" />
								</td>
							</tr>
						</table>
					</siga:ConjCampos>									
				</c:when>			
			</c:choose>						
		</tr>				
	</html:form>
	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
	<siga:ConjBotonesBusqueda botones="B"  modal="G" titulo="${busc}" />
	<!-- FIN: BOTONES BUSQUEDA -->

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		// Funcion asociada a boton buscar
		function buscar() {		
			sub();
			document.forms[0].modo.value="buscarPor";
			document.forms[0].submit();			
		}

		function accionCerrar() {		
			var aux = new Array();
			aux[0] = "cerrar";
			top.cierraConParametros(aux);
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 
		id="resultadoModal"
		name="resultadoModal" 
		scrolling="no"
		frameborder="0"
		marginheight="0"
		marginwidth="0"					 
		class="frameGeneral"></iframe>

	<siga:ConjBotonesAccion botones="C" modal="G" clase="botonesDetalle"/>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>