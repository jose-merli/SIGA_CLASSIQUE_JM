<!-- busquedaEntradaEnvios.jsp -->
<!-- 
	 VERSIONES:
	 Carlos Ruano Versión inicial
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.*, com.siga.envios.form.EntradaEnviosForm, java.util.*"%>
<%@ page import="com.siga.beans.EnvEnviosAdm"%>
<!-- JSP -->

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="envios.bandejaentrada.titulo"	localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<script type="text/javascript">
jQuery.noConflict();

		function cargarCombos() {
  			var estados = ${json}.estado;
  			var tiposIntercambio = ${json}.tiposIntercambio;

  			//Carga combo estados
  			jQuery("#estado").append("<option value=''>&nbsp;</option>");
			jQuery.each(estados, function(i,item2){
		          jQuery("#estado").append("<option value='"+item2.idEstado+"'>"+item2.nombre+"</option>");
		    });
		
		     //Carga como tipo intercambio
			jQuery("#tipoIntercambioTelematico").append("<option  value=''>&nbsp;</option>");
		   	jQuery.each(tiposIntercambio, function(i,item2){
		           jQuery("#tipoIntercambioTelematico").append("<option value='"+item2.idTipoIntercambioTelematico+"'>"+item2.nombre+"</option>");
		    });
	 	}

			
</script>
<body onload="ajusteAltoBotones('resultado');">
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<html:form action="/ENV_EntradaEnvios?noReset=true" method="POST" target="resultado">	
		<html:hidden styleId="modo"          				property="modo" 			value = ""/>
		<html:hidden styleId="actionModal"   				property="actionModal" 		value = ""/>
		<html:hidden styleId="idInstitucion" 				property="idInstitucion" />
		<html:hidden styleId="idTipoIntercambioTelematico" 	property="idTipoIntercambioTelematico" />
		<html:hidden styleId="idEstado" 					property="idEstado" />
		
		<bean:define id="parametrosComboRemitente" name="parametrosComboRemitente" scope="request"/>
		<bean:define id="json" name="json" scope="request"/>

		<tr>				
			<td>
				<siga:ConjCampos>			
					<table class="tablaCampos" align="center">
						<tr>
							<td class="labelText" >
								<bean:message
									key="general.etiqueta.fecha" />&nbsp;<bean:message
									key="general.etiqueta.desde" /> 
							</td>				
							<td width="20%">
								<siga:Fecha nombreCampo="fechaDesde"/>
							</td>
				
							<td class="labelText" width="12%">
								<bean:message
									key="general.etiqueta.fecha" />&nbsp;<bean:message
									key="general.etiqueta.hasta" /> 
							</td>
							<td width="15%">
								<siga:Fecha nombreCampo="fechaHasta"/>
							</td>
							
							<td class="labelText">
								<bean:message
									key="comunicaciones.etiqueta.remitente" />
							</td>
							
							<td >
								<input type="text" name="codigoExtJuzgado" class="box" size="7" style="margin-top: 3px;" maxlength="10" onBlur="obtenerJuzgado();" />
							</td>
							
							<td class="labelText" >
								<siga:ComboBD nombre="remitente" tipo="comboJuzgados" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" hijo="t" parametro="${parametrosComboRemitente}"/>
							</td>							
						</tr>	
	
						<tr>	
							<td class="labelText" >
								<bean:message
									key="comunicaciones.etiqueta.asunto" />
							</td>
							<td width="7%">
								<html:text name="EntradaEnviosForm" property="asunto" size="20" styleClass="box"></html:text>
							</td>
												
							<td class="labelText" width="8%">
								<bean:message
									key="comunicaciones.etiqueta.estado" />
							</td>
							<td width="20%">
								<select id="estado" class="box"/>
							</td>	
							
							<td class="labelText">
								<bean:message
									key="comunicaciones.etiqueta.tipo" />
							</td>
							<td width="20%" colspan="2">
								<select id="tipoIntercambioTelematico" class="box" style="width:250px;"/>
								<script type="text/javascript">
									cargarCombos();
								</script>
							</td>					
						</tr>				
					</table>
				</siga:ConjCampos>
			</td>
		</tr>
	</html:form>
	</table>

	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

	<siga:ConjBotonesBusqueda botones="B"/>


	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		function buscar(){
			sub();
			/*if (compararFecha(document.EntradaEnviosForm.fechaDesde.value,document.EntradaEnviosForm.fechaHasta.value)==1) {
				mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
				alert(mensaje);
				fin();

			} else {*/
				document.EntradaEnviosForm.idEstado.value = document.getElementById("estado").value;
				document.EntradaEnviosForm.idTipoIntercambioTelematico.value = document.getElementById("tipoIntercambioTelematico").value;
				
				document.EntradaEnviosForm.modo.value="buscarInicio";
				document.EntradaEnviosForm.target="resultado";	
				document.EntradaEnviosForm.submit();	
			//}
		}
			
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
