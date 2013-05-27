<!-- busquedaRetencionesJudiciales.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>

<!-- JSP -->
<% 	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	String dato[] = {(String)usr.getLocation()};
%>

<html>

	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>


		<!-- INICIO: TITULO Y LOCALIZACION -->
		<siga:Titulo titulo="FactSJCS.mantRetencionesJ.cabecera" localizacion="FactSJCS.mantRetencionesJ.ruta"/>	
		<!-- FIN: TITULO Y LOCALIZACION -->	
	</head>

<body onLoad="ajusteAlto('resultado');">
	<table width="100%" align="center">
		<tr>
			<td>

				<!-- INICIO: CAMPOS DE BUSQUEDA-->
				<siga:ConjCampos leyenda="FactSJCS.mantRetencionesJ.leyendaRetenciones">
					<table align="left" border="0"   width="100%">
					
    					<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
							<input type="hidden" name="actionModal" value="">
							<input type="hidden" name="modo" value="abrirBusquedaModal">
						</html:form>
	
						<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
						
    					<html:form action="${path}" method="POST" target="resultado">
							<html:hidden property = "modo" value = "inicio"/>
							<html:hidden property = "actionModal" value = ""/>
							<input type="hidden" name="limpiarFilaSeleccionada" value="">
							<html:hidden property = "idPersona" />
							<html:hidden property = "ncolegiado" />
  		
							<tr>
	 							<td class="labelText" colspan="5">
									<html:checkbox   property="checkEsDeTurno"   onclick="activarLetrado(this);" />&nbsp;&nbsp;<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.aplicableLetrados"/>
								</td>
							</tr>
							
							<tr>
								<td id="busquedaLetrado" colspan="5">
									<siga:BusquedaPersona tipo="colegiado" titulo="gratuita.seleccionColegiadoJG.literal.titulo" idPersona="idPersona" >
									</siga:BusquedaPersona>
								</td>	
							</tr>
	
							<tr>
								<td class="labelText" nowrap>
									<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaNotificacionDesde"/>
								</td>
								<td class="labelText" >
									<siga:Fecha nombreCampo="fechaNotificacionDesde" />
								</td>
								
								<td class="labelText" nowrap>
									<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaNotificacionHasta"/>
								</td>
								<td class="labelText" >
									<siga:Fecha nombreCampo="fechaNotificacionHasta" />
								</td>	
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tipoRetencion"/>
								</td>
								<td class="labelText">	
									<select name="tipoRetencion" class="boxCombo">
										<option value="" selected></option>
										<option value="<%=ClsConstants.TIPO_RETENCION_PORCENTAJE%>"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.porcentual"/></option>
										<option value="<%=ClsConstants.TIPO_RETENCION_IMPORTEFIJO%>"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.importeFijo"/></option>
										<option value="<%=ClsConstants.TIPO_RETENCION_LEC%>"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.tramosLEC"/></option>
									</select>
								</td>
	
								<td class="labelText">
									<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.destinatario"/>
								</td>
								<td class="labelText" >
									<siga:ComboBD nombre="idDestinatario" tipo="destinatariosRetencionesFCS" ancho="300" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>"/>
								</td>
								
								<td class="labelText"   valign="bottom" >
									<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.checkHistorico"/>
									<input type="checkbox" name="checkHistorico" />
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
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="FactSJCS.mantRetencionesJ.cabeceraExt" />	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		// Funcion asociada a boton buscar
		function buscar() {	
			if((validarFecha(document.forms[1].fechaNotificacionDesde.value)) &&
			   (validarFecha(document.forms[1].fechaNotificacionHasta.value))) {
				var ncolegiado = document.getElementById('numeroNifTagBusquedaPersonas').value;
				sub();
				if (!isNaN(ncolegiado)) {
				    document.forms[1].ncolegiado.value=ncolegiado;
				    document.forms[1].checkEsDeTurno.value = document.forms[1].checkEsDeTurno.checked;
					document.forms[1].modo.value = "buscarPor";
					document.forms[1].submit();
					
				} else { 
					alert('<siga:Idioma key="FactSJCS.busquedaRetencionesJ.literal.errorNumerico"/>');
				}
				
			} else {
				setFocusFormularios();
			}
		}		
		
		// Funcion asociada a boton limpiar
		function limpiar() {		
			document.forms[0].reset();
		}
		
		// Funcion asociada a boton Nuevo 
		function nuevo() {
			document.forms[1].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[1].name,"M");
			if(resultado=='MODIFICADO') 
				buscar();
		}
		
		function activarLetrado(valorCheck) {
	 		if (!valorCheck.checked) {
	 			jQuery("#busquedaLetrado").show();
	 			
	 		} else {
	 			jQuery("#busquedaLetrado").hide();
	   			limpiarPersona();
	 		}	
	 		ajusteAltoPaginador('resultado');
	 		ajusteAlto('resultado');
	 		ajusteAlto('mainWorkarea');
		}		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		id="resultado"
		name="resultado" 
		scrolling="no"
		frameborder="0"
		marginheight="0"
		marginwidth="0"					 
		class="frameGeneral">
	</iframe>
			
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>