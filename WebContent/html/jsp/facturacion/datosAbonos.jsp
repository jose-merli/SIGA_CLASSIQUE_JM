<!-- datosAbonos.jsp -->
<!-- 
	 Muestra el formulario de alta de abonos
	 VERSIONES:
		 miguel.villegas 08/03/2005
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
		

	// Institucion, fecha e idAbono provisional 
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion
	String idAbono=(String)request.getAttribute("IDABONO"); // Obtengo el identificador del abono provisional
	String fechaA=(String)request.getAttribute("FECHA"); // Obtengo la fechaActual
	String numeroAbono=(String)request.getAttribute("NUMEROABONO"); // Obtengo el numero del abono provisional
	String idFactura=(String)request.getAttribute("IDFACTURA"); // Obtengo el identificador de la institucion

    // Botones a mostrar
	String botones = "Y,C";
%>

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="AltaAbonosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			  titulo="facturacion.busquedaAbonos.literal.cabecera" 
			  localizacion="facturacion.busquedaAbonos.ruta"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>


	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="facturacion.altaAbonos.literal.cabecera"/>
				</td>
			</tr>
		</table>
	
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		<div id="camposRegistro" class="posicionModalMedia" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposMedia"  align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="facturacion.altaAbonos.literal.datosAbono">
						<table class="tablaCampos" align="center">
							<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="resultado">
								<html:hidden property = "actionModal" value=""/>
								<html:hidden property = "modo" value = "abrirBusquedaModal"/>
								<input type="hidden" name="clientes" value="1">
							</html:form>
							<html:form action="/FAC_AltaAbonos.do" method="POST" target="submitArea">
								<html:hidden property = "actionModal" value=""/>
								<html:hidden property = "modo" value = ""/>
								<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
								<html:hidden property = "idPersona" value = ""/>
								<html:hidden property = "idPersonaFactura" value = ""/>
								<html:hidden property = "idAbono" value ="<%=idAbono%>"/>
								<html:hidden property = "idFactura" value ="<%=idFactura%>"/>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.numeroAbono"/>
									</td>
									<td class="labelTextValor">
										<html:text property="numeroAbono" styleClass="boxConsultaLabelText" size="8" maxlength="10" value="<%=numeroAbono%>" readOnly="true"></html:text>&nbsp;
										<siga:Idioma key="facturacion.altaAbonos.literal.provisional"/>
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.fecha"/>
									</td>	
									<td class="labelTextValor">
										<html:text property="fecha" size="10" maxlength="10" styleClass="boxConsultaLabelText" readOnly="true" value="<%=fechaA%>"></html:text>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.cliente"/>&nbsp;(*)
									</td>
									<td class="labelText" colspan="4">
										<html:button property="buscar" onclick="return buscarCliente();" styleClass="button"> 
											<siga:Idioma key="general.boton.search"/> 
										</html:button>
										&nbsp;
										<html:text property="busquedaCliente" styleClass="boxConsulta" size="65" maxlength="30" value=""></html:text>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.numeroFactura"/>
									</td>
									<td class="labelTextValor">
										<html:text property="idFactura" styleClass="box" size="10" maxlength="20" value="" onchange="return validarFactura();"></html:text>&nbsp;
										<siga:Idioma key="facturacion.altaAbonos.literal.opcional"/>
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.fecha"/>
									</td>	
									<td class="labelText">										
										<html:text property="fechaFactura" size="10" maxlength="10" styleClass="boxConsulta" readOnly="true"></html:text>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.estado"/>
									</td>
									<td class="labelText" colspan="3">
										<html:text property="estado" size="30" maxlength="10" styleClass="boxConsulta" readOnly="true" value=""></html:text>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.altaAbonos.literal.motivos"/>&nbsp;(*)
									</td>
									<td class="labelText" colspan="3">
										<html:textarea property="motivos" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" rows="4" styleClass="box" size="100" value=""></html:textarea>
									</td>
								</tr>
							</html:form>	
						</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->

			<!-- INICIO: BOTONES REGISTRO -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
				 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
				 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
				 La propiedad modal dice el tamanho de la ventana (M,P,G)
			-->
		
			<siga:ConjBotonesAccion botones='<%=botones%>' modal="M"/>
			
			<!-- FIN: BOTONES REGISTRO -->
	
		
			<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
			<script language="JavaScript">
	
				<!-- Asociada al boton GuardarCerrar -->
				function accionGuardarCerrar() 
				{					
					if (validateAltaAbonosForm(document.AltaAbonosForm)){
						//if ((document.AltaAbonosForm.idPersonaFactura.value!="")&&(document.AltaAbonosForm.idPersonaFactura.value!=document.AltaAbonosForm.idPersona.value)) {
							//mensaje='<siga:Idioma key="facturacion.altaAbonos.literal.facturaIncorrecta"/>';
							//alert(mensaje);
							//envio=false;
						//}
						//else{
							document.AltaAbonosForm.modo.value="insertar";
							document.AltaAbonosForm.submit();
						//}
					}	
				}
	
				<!-- Asociada al boton Cerrar -->
				function accionCerrar() 
				{		
					// esta funcion cierra la ventana y devuelve 
					// un valor a la ventana padre (USAR SIEMPRE)
					top.cierraConParametros("NORMAL");
				}
				
				<!-- Asociada al boton Buscar -->
				function buscarCliente() {

				 	var resultado = ventaModalGeneral("busquedaClientesModalForm","G");

				 	// Si he recuperado datos
					if((resultado!=undefined)&&(resultado[0]!=undefined)) {
						document.all.AltaAbonosForm.idPersona.value = resultado[0];
						document.all.AltaAbonosForm.busquedaCliente.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	
						//document.location.reload();
					}
				}
				
				<!-- Asociada al evento onchange idFactura -->
				function validarFactura() {

					if (document.AltaAbonosForm.idFactura.value!=undefined){
						document.forms[1].target="submitArea";
						document.forms[1].modo.value="confirmarFactura";
						document.forms[1].submit();
					}	
				}
			
			</script>
		</div>
		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>