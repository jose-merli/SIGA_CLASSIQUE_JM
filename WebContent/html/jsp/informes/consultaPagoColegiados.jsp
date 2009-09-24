<!-- consultaPagoColegiados.jsp -->
<!-- 
	 VERSIONES:
	 jtacosta 2009
-->

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

<!-- IMPORTS -->

<!-- JSP -->

<html>

<!-- HEAD -->
<head>

<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">

<script src="<html:rewrite page="/html/js/SIGA.js"/>"	type="text/javascript"></script>
<script src="<html:rewrite page="/html/js/validation.js"/>"	type="text/javascript"></script>
<script src=""	type="text/javascript"></script>
<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>


<!-- INICIO: TITULO Y LOCALIZACION -->
<!-- Escribe el título y localización en la barra de título del frame principal -->
<siga:Titulo titulo="informes.sjcs.pagos.literal.titulo"
	localizacion="facturacion.localizacion" />
<!-- FIN: TITULO Y LOCALIZACION -->

<!-- Calendario -->


</head>

<body onLoad="ajusteAlto('resultado');">

<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


<html:javascript formName="mantenimientoInformesForm" staticJavascript="false" />

<html:form action="/INF_CartaPago.do" method="POST"
	target="resultado">
<fieldset>
	<html:hidden property="modo" value="" />
	<html:hidden property="idInstitucion"/>
	<input type="hidden" name="limpiarFilaSeleccionada" value="">
	<html:hidden property="letrado"/>


<table class="tablaCampos" align="center">
		
	<tr>
		<td class="labelText" width="10%">
			<siga:Idioma key="informes.sjcs.pagos.literal.idioma"/>&nbsp;(*)
		</td>				
		<td width="10%">
			<siga:ComboBD nombre="idioma" tipo="cmbIdioma"  clase="boxCombo" obligatorioSinTextoSeleccionar="true" />
		</td>				
		<td class="labelText" width="10%">
			<siga:Idioma key="informes.sjcs.pagos.literal.pago"/>&nbsp;(*)
		</td>				
		<td width="40%">
			<bean:define id="parametrosComboPago" name="mantenimientoInformesForm" property="parametrosComboPago"/>
			<siga:ComboBD nombre="idPago" tipo="cmb_PagosCerrados" parametro="<%=(String[])parametrosComboPago%>" clase="boxCombo" obligatorio="false" accion=";buscar();"/>
			
		</td>
		<td width="30%">
			&nbsp;
			</td>
	</tr>

	<tr>
		<td colspan="5">
		<siga:BusquedaPersona tipo="colegiado"
			titulo='informes.sjcs.pagos.literal.colegiado'
			idPersona="letrado">
		</siga:BusquedaPersona>
		
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<siga:ConjCampos leyenda="informes.sjcs.pagos.literal.cliente">
				<table width="100%">
				<tr>
					<td class="labelText">
						<siga:Idioma key="informes.sjcs.pagos.literal.nif"/>
					</td>
					<td>	
						<html:text  property="interesadoNif" size="15" maxlength="20" styleClass="box" ></html:text>
					</td>
				
					<td class="labelText">
						<siga:Idioma key="informes.sjcs.pagos.literal.nombre"/>
					</td>
					<td>	
						<html:text  property="interesadoNombre" size="15" maxlength="100" styleClass="box" ></html:text>
					</td>
					<td width="30%">
					&nbsp;
					</td>
				</tr>
				<tr>	
					<td class="labelText">
						<siga:Idioma key="informes.sjcs.pagos.literal.apellido1"/>
					</td>
					<td >	
						<html:text  property="interesadoApellido1" size="30" maxlength="100" styleClass="box" ></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="informes.sjcs.pagos.literal.apellido2"/>
					</td>
					<td >	
						<html:text  property="interesadoApellido2" size="30" maxlength="100" styleClass="box" ></html:text>
					</td>	
					<td>
					&nbsp;
					</td>

				</tr>
				</table>
			</siga:ConjCampos>
		
		</td>
	</tr>

</table>
</fieldset>

</html:form>


<!-- FIN: CAMPOS DE BUSQUEDA-->


<!-- INICIO: BOTONES BUSQUEDA -->
<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

<siga:ConjBotonesBusqueda botones="B" />

<!-- FIN: BOTONES BUSQUEDA -->

<!-- Formularios auxiliares para la busqueda de persona-->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<html:hidden property="actionModal" value=""/>
		<html:hidden property="modo" value="abrirBusquedaModal"/>
		
	</html:form>


<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
<script language="JavaScript">
		
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();	
			var f=document.getElementById("mantenimientoInformesForm");
		    if (validateMantenimientoInformesForm(f)) {
			
				document.forms[0].modo.value="buscarInicio";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
			}else{
				fin();
			}
				
		}
		
		
       function buscarPaginador() 
		{
			sub();	
			var f=document.getElementById("mantenimientoInformesForm");
		    if (validateMantenimientoInformesForm(f)) {
			
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
			}else{
				fin();
			}
				
		}
			
	</script>
<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

<!-- INICIO: IFRAME LISTA RESULTADOS -->
<iframe align="center" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
	id="resultado" name="resultado" scrolling="no" frameborder="0"
	marginheight="0" marginwidth="0" ;					 
					class="frameGeneral">
</iframe>

<!-- FIN: IFRAME LISTA RESULTADOS -->

<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>
	style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
