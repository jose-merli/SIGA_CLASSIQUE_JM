<!DOCTYPE html>
<html>
<head>
<!-- consultaMorosos.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 21-3-2005 Versión inicial
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*,java.util.*"%>
<%@ page import="com.siga.beans.ConModuloBean"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));
	ArrayList tipoColeg = new ArrayList();
 	tipoColeg.add(String.valueOf(ClsConstants.ESTADO_COLEGIAL_ALTA));
%>

<%@page import="com.siga.Utilidades.UtilidadesString"%>


<!-- HEAD -->


<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>

<!-- INICIO: TITULO Y LOCALIZACION -->
<!-- Escribe el título y localización en la barra de título del frame principal -->
<siga:Titulo titulo="facturacion.consultamorosos.literal.titulo"
	localizacion="facturacion.localizacion" />
<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');">

<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />

<html:form action="/FAC_ConsultaMorosos.do" method="POST"
	target="resultado">
	<html:hidden property="modo" value="" />
	<input type="hidden" name="limpiarFilaSeleccionada" value="">
	<html:hidden property="letrado"/>
	<html:hidden property="seleccionarTodos" />

	<fieldset>
	<table class="tablaCampos" align="center" border="0">
		<tr>

			<td width ="25%" class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.fechadesde" /></td>
			<td>
			<siga:Fecha nombreCampo="fechaDesde" /> 
			</td>
			<td width ="10%" class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.hasta" /></td>
			<td>
			 <siga:Fecha nombreCampo="fechaHasta" /> 
			</td>
			<td width ="15%" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Estado"/></td>
					<td colspan ="3"><siga:ComboBD nombre="cmbEstadosFactura" tipo="cmbEstadosFacturaMorosos"  clase="boxCombo" obligatorio="false" />
			
				
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.facturasimpagadas" /></td>
			<td><html:text name="ConsultaMorososForm"
				property="facturasImpagadasDesde" style="width:80px" maxlength="10"
				styleClass="box"></html:text></td>
			<td class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.hasta" /></td>
			<td><html:text name="ConsultaMorososForm"
				property="facturasImpagadasHasta" style="width:80px" maxlength="10"
				styleClass="box"></html:text></td>
			<td  class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.factura" /></td>
			<td colspan ="3"><html:text name="ConsultaMorososForm"
				property="numeroFactura" style="width:160px" maxlength="20"
				styleClass="box"></html:text></td>
				
			

		</tr>
		<!-- FILA -->
		<tr>
			<td class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.importeadeudado" />(&euro;)</td>
			<td><html:text name="ConsultaMorososForm"
				property="importeAdeudadoDesde" style="width:80px" maxlength="10"
				styleClass="box">&nbsp;</html:text></td>
			<td  class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.hasta" />(&euro;)</td>
			<td ><html:text name="ConsultaMorososForm"
				property="importeAdeudadoHasta" style="width:80px" maxlength="10"
				styleClass="box">&nbsp;</html:text></td>
			
			<td class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.comunicaciones.numero.desde" /></td>
			<td><html:text name="ConsultaMorososForm"
				property="numeroComunicacionesDesde" style="width:80px" maxlength="10"
				styleClass="box">&nbsp;</html:text></td>
			<td class="labelText"><siga:Idioma
				key="facturacion.consultamorosos.literal.hasta" /></td>
			<td><html:text name="ConsultaMorososForm"
				property="numeroComunicacionesHasta" style="width:80px" maxlength="10"
				styleClass="box">&nbsp;</html:text></td>
			

		</tr>




		<tr>
			<td colspan="8">
				<fieldset>
				<legend><bean:message key="facturacion.consultamorosos.literal.deudor"/> </legend>
				
					<table>
					<tr>
						<td ><siga:BusquedaPersona tipo="colegiado"	idPersona="letrado" preFunction="preFunctionDeudor" postFunction="postFunctionDeudor">
							</siga:BusquedaPersona>
						</td>
							<td>
								<table>
									<tr>
										<td  class="labelText">
							
											<siga:Idioma
												key="censo.consultaDatosColegiacion.literal.estadoCol" /></td>
										<td>
											<siga:ComboBD nombre = "cmbEstadoColegial" tipo="cmbTipoColegiacion" ancho="5" clase="boxCombo" obligatorio="false" elementoSel="<%=tipoColeg %>"/>				
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
			
				</fieldset>
			</td>
		</tr>
		<tr>
					<td colspan="8">
						<siga:ConjCampos leyenda="facturacion.consultamorosos.literal.cliente">
							<table width="100%">
							<tr>
								
								
								<td class="labelText">
									<siga:Idioma key="facturacion.consultamorosos.literal.nombre"/>
								</td>
								<td>	
									<html:text  property="interesadoNombre" size="15" maxlength="100" styleClass="box" ></html:text>
								</td>	
								<td class="labelText">
									<siga:Idioma key="facturacion.consultamorosos.literal.apellidos"/>
								</td>
								<td >	
									<html:text  property="interesadoApellidos" size="30" maxlength="100" styleClass="box" ></html:text>
								</td>	
								<td width="50%">
								</td>
		
							</tr>
							</table>
						</siga:ConjCampos>
					
					</td>

	</table>
	</fieldset>

</html:form>


<!-- FIN: CAMPOS DE BUSQUEDA-->


<!-- INICIO: BOTONES BUSQUEDA -->
<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
<siga:ConjBotonesBusqueda botones="B, CON" />

<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
	<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_FACTURACION%>"/>
	<html:hidden property="modo" value="inicio"/>
	<html:hidden property="accionAnterior" value="${path}"/>

</html:form>


<!-- FIN: BOTONES BUSQUEDA -->

<!-- Formularios auxiliares para la busqueda de persona-->
<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
	<html:hidden property="actionModal" value=""/>
	<html:hidden property="modo" value="abrirBusquedaModal"/>
	
</html:form>

<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
<script language="JavaScript">
		
		<!-- Funcion asociada a boton buscar -->
		function buscar(modo) 
		{ var importeDesde=document.forms[0].importeAdeudadoDesde.value.replace(/,/,".");
		  var importeHasta=document.forms[0].importeAdeudadoHasta.value.replace(/,/,".");

			if((validarFecha(document.forms[0].fechaDesde.value))&&
				(validarFecha(document.forms[0].fechaHasta.value))){
				var nSolicitud = document.forms[0].facturasImpagadasDesde.value;
					if(!isNumero(document.forms[0].facturasImpagadasDesde.value)){
					  alert("<siga:Idioma key="facturacion.consultamorosos.literal.facturasimpagadas"/>" +" "+ 
								  "<siga:Idioma key="messages.campoNumericoSinDecimales.error"/>");
                     return;	
                     							  
					
					}
					if(!isNumero(document.forms[0].facturasImpagadasHasta.value)){
					  alert("<siga:Idioma key="facturacion.consultamorosos.literal.facturasimpagadashasta"/>" +" "+ 
								  "<siga:Idioma key="messages.campoNumericoSinDecimales.error"/>");
								  return;
								  
						  
					}
					
					if(!isANumber(importeDesde)){
					  alert("<siga:Idioma key="facturacion.consultamorosos.literal.importeadeudado"/>" +" "+ 
								  "<siga:Idioma key="messages.campoNumerico.error"/>");
								 return;
					}
					if(!isANumber(importeHasta)){
					  alert("<siga:Idioma key="facturacion.consultamorosos.literal.importeadeudadohasta"/>" +" "+ 
								  "<siga:Idioma key="messages.campoNumerico.error"/>");
								  return;
					}
					
					if(!isNumero(document.forms[0].numeroComunicacionesDesde.value)){
					  alert("<siga:Idioma key="facturacion.consultamorosos.literal.comunicaciones.numero.desde"/>" +" "+ 
								  "<siga:Idioma key="messages.campoNumericoSinDecimales.error"/>");
								  return;
							  
					}
					
					if(!isNumero(document.forms[0].numeroComunicacionesHasta.value)){
					  alert("<siga:Idioma key="facturacion.consultamorosos.literal.comunicaciones.numero.hasta"/>" +" "+ 
								  "<siga:Idioma key="messages.campoNumericoSinDecimales.error"/>");
								  return;
								  
				     				  
					}
					
					
				sub();	
				document.forms[0].facturasImpagadasDesde.value=document.forms[0].facturasImpagadasDesde.value.replace(/,/,".");
				document.forms[0].facturasImpagadasHasta.value=document.forms[0].facturasImpagadasHasta.value.replace(/,/,".");
				document.forms[0].importeAdeudadoDesde.value=document.forms[0].importeAdeudadoDesde.value.replace(/,/,".");
				document.forms[0].importeAdeudadoHasta.value=document.forms[0].importeAdeudadoHasta.value.replace(/,/,".");
				
				if(modo)
					document.forms[0].modo.value = modo;
				else
					document.forms[0].modo.value="buscarInit";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
			}else{
				setFocusFormularios();
			}	
		}
		function seleccionarTodos(pagina) 
		{
			
			
			document.forms[0].seleccionarTodos.value = pagina;
			buscar('buscarPor');
				
		}		
		
		
		function buscarPaginador() 
		{		
//			if (validateBusquedaClientesForm(document.forms[0])) 
			{  
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
		}

		function consultas() 
		{		
			document.RecuperarConsultasForm.submit();
			
		}
		function preFunctionDeudor() 
		{		
			jQuery("#cmbEstadoColegial").prop("selectedIndex",0);
			jQuery("#cmbEstadoColegial").attr("disabled","disabled");
			
			
		}
		function postFunctionDeudor() 
		{	
			if(document.getElementById('numeroNifTagBusquedaPersonas').value ==''){
				jQuery("#cmbEstadoColegial").removeAttr("disabled");
			}
			
			
		}
		
		
		
			
	</script>
<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

<!-- INICIO: IFRAME LISTA RESULTADOS -->
<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
	id="resultado" name="resultado" scrolling="no" frameborder="0"
	marginheight="0" marginwidth="0" class="frameGeneral">
</iframe>

<!-- FIN: IFRAME LISTA RESULTADOS -->

<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
