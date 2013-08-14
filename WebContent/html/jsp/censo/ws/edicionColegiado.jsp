<!DOCTYPE html>
<!-- edicionColegiado.jsp -->

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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.ws.form.EdicionColegiadoForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ConModuloBean"%>



<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.ws.gestioncolegiado.titulo" localizacion="censo.ws.gestioncolegiado.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="EdicionColegiadoForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
		function buscar() {
			if (!${EdicionColegiadoForm.historico}) {
				sub();
				document.forms[0].modo.value="buscarInit";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
			}
		}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
</head>



<body onLoad="ajusteAlto('resultado');buscar();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		
		<html:form action="/CEN_EdicionColegiado.do?noReset=true" method="POST" target="resultado">	
		<c:set var="htmlTextReadOnly" value="false" />
		<c:set var="htmlTextClass" value="box" />
		<c:if test="${EdicionColegiadoForm.modo=='Ver' || EdicionColegiadoForm.modo=='ver'}">
			<c:set var="htmlTextReadOnly" value="true" />
			<c:set var="htmlTextClass" value="boxConsulta" />
		</c:if>
					
			<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.datosColegiado">			
				<table class="tablaCampos" align="center">							
					
					<input type="hidden" name="modo" value="<bean:write name="EdicionColegiadoForm" property="modo"/>"/>
					<input type="hidden" name="idcensodatos" value="${EdicionColegiadoForm.idcensodatos}"/>
					
					<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">										
							<siga:Idioma key="censo.ws.literal.publicarInformacion"/>
						</td>
						<td>
							<html:checkbox name="EdicionColegiadoForm" property="publicarcolegiado"  value="${EdicionColegiadoForm.publicarcolegiado}"  disabled="${htmlTextReadOnly}"/>							
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.numeroSolicitud"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="numsolicitudcolegiacion" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.numColegiado"/>
						</td>
						
						<td>
							<html:text name="EdicionColegiadoForm" property="ncolegiado" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.nombre"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="nombre" styleId="nombreColegiado" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.primerApellido"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="apellido1" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.segundoApellido"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="apellido2" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.sexo"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="sexo" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.fechanacimiento"/>
						</td>
						<td>							
							<siga:Fecha  nombreCampo= "fechanacimiento" valorInicial="${EdicionColegiadoForm.fechanacimiento}" disabled="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.tipoIdentificacion"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="idcensotipoidentificacion" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.identificacion"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="numdocumento" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicarTelefono"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicartelefono" value="${EdicionColegiadoForm.publicartelefono}" disabled="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.telefono"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="telefono" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicarTelefonoMovil"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicartelefonomovil" value="${EdicionColegiadoForm.publicartelefonomovil}" disabled="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.telefonoMovil"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="telefonomovil" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicarfax"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicarfax" value="${EdicionColegiadoForm.publicarfax}" disabled="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.fax"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="fax" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicaremail"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicaremail" value="${EdicionColegiadoForm.publicaremail}" disabled="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.email"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="email" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.situacionEjer"/>
						</td>
						<td>							
							<html:text name="EdicionColegiadoForm" property="idecomcensosituacionejer" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.fechasituacion"/>
						</td>
						<td>
							<siga:Fecha  nombreCampo= "fechasituacion" valorInicial="${EdicionColegiadoForm.fechasituacion}" disabled="${htmlTextReadOnly}"/>							
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.residente"/>
						</td>
						<td colspan="3">							
							<html:checkbox name="EdicionColegiadoForm" property="residente" value="${EdicionColegiadoForm.residente}" disabled="${htmlTextReadOnly}"/>						
						</td>											
					</tr>
					
					
				</table>			
			</siga:ConjCampos>
			<br/>
			<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.direccion">			
				<table class="tablaCampos" align="center">
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.publicardireccion"/>
						</td>
						<td>							
							<html:checkbox name="EdicionColegiadoForm" property="publicardireccion" value="${EdicionColegiadoForm.publicardireccion}" disabled="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.tipovia"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="desctipovia" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>		
					</tr>	
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.domicilio"/>
						</td>  
						<td>							
							<html:text name="EdicionColegiadoForm" property="domicilio" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.codigopostal"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="codigopostal" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>		
					</tr>	
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.pais"/>
						</td>  
						<td>							
							<html:text name="EdicionColegiadoForm" property="codigopaisextranj" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.provincia"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="codigoprovincia" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>		
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.poblacion"/>
						</td>  
						<td>							
							<html:text name="EdicionColegiadoForm" property="codigopoblacion" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.poblacionRecibida"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="descripcionpoblacion" size="30" styleClass="boxConsulta" readonly="true"/>
						</td>		
					</tr>
				</table>			
			</siga:ConjCampos>	
			<br/>
			<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.incidencias">
						
				<table class="tablaCampos" align="center">
					<c:forEach items="${EdicionColegiadoForm.incidencias}" var="incidencia" varStatus="i">							
						<tr class="labelTextValue">
							<td id="${i.index}">&nbsp;*&nbsp;<siga:Idioma key="${incidencia}"/></td>
						</tr>
					</c:forEach>
				</table>
			</siga:ConjCampos>			
		</html:form>
			
		<!-- formulario para el botón volver -->
		
		<c:choose>
			<c:when test="${EdicionColegiadoForm.historico}">
				<html:form action="/CEN_EdicionColegiado.do?noReset=true" method="POST" target="mainWorkArea">
					<html:hidden property="volver" value="true"/>
					<html:hidden property="modo" value="editar"/>
										
					<html:hidden property="idcensodatosPadre" value="${EdicionColegiadoForm.idcensodatosPadre }"/>
				</html:form>		
			</c:when>
			<c:otherwise>
				<html:form action="/CEN_EdicionRemesas.do?noReset=true" method="POST" target="mainWorkArea">
					<html:hidden property="volver" value="true"/>
					<html:hidden property="modo" value="editar"/>
				</html:form>
			</c:otherwise>
		</c:choose>
		
			
					

		<!-- FIN: CAMPOS DE BUSQUEDA-->
	
		<!-- INICIO: BOTONES BUSQUEDA -->
		<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
		-->
		<%  
			String botones = "B";
							 
		%>
		
		<c:choose>
			<c:when test="${!EdicionColegiadoForm.historico}">
				<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="censo.ws.gestioncolegiado.historicoCambios" />
			</c:when>
		</c:choose>
		
		
		<!-- FIN: BOTONES BUSQUEDA -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
	
	   		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0"
				marginwidth="0";					 
				class="frameGeneral">
			<!--style="position:absolute; width:964; height:297; z-index:2; top: 177px; left: 0px"> -->
	  		</iframe>
	  	
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	</div>	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
	<script type="text/javascript">
			 				 	
			 	function accionVolver() {
					document.forms[1].submit();
				}
				
			 </script>
			 
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />

</body>
</html>