<!DOCTYPE html>
<html>
<head>
<!-- busquedaRecibos.jsp -->
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ConsPLFacturacion"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.facturacion.form.DevolucionesManualesForm"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	
	boolean esLetrado = user.isLetrado();
	boolean esConsejo=false;
	int idConsejo = new Integer(user.getLocation()).intValue();
	if(idConsejo==2000 || idConsejo>=3000)
		esConsejo=true;

	DevolucionesManualesForm form = (DevolucionesManualesForm) request.getSession().getAttribute("DevolucionesManualesForm");
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar!=null && buscar.equals("1")) {
		funcionBuscar = "buscarPaginador()";
	}
	if (form.getHayMotivos().equals("0")) {
		funcionBuscar = "mensaje()";
	}

%>	
	


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="facturacion.devolucionManual.titulo" 
		localizacion="facturacion.devolucionManual.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Validaciones en Cliente -->
	<html:javascript formName="DevolucionesManualesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

</head>

<script language="JavaScript">

	<!-- Funcion asociada a boton buscar -->
	/*function buscar()
	{ 
		document.DevolucionesManualesForm.modo.value="BuscarDevolucionesManuales";
		document.DevolucionesManualesForm.submit();
	}*/
	
	function buscarPaginador() 
	
		{		
	        document.forms[0].modo.value="Buscar";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
	
	function mensaje() {
<% 		
		if (form.getHayMotivos().equals("0")) { 
%>
			// NO HAY MOTIVOS
			var m = '<siga:Idioma key="messages.fact.error.motivosNoCargados"/>';
			alert(m);
			return false;
<% 		
		} 
%>	
	}

</script>
<body onload="ajusteAltoBotones('resultado');<%=funcionBuscar%>">

<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

	<html:form action="/FAC_DevolucionesManual.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property="recibos"/>		
		<html:hidden property="fechaDevolucion"/>		
		<html:hidden property="banco"/>		

		<input type="hidden" name="limpiarFilaSeleccionada" value="">
				
	    <input type="hidden" name="actionModal" value="">
		
			<siga:ConjCampos leyenda="facturacion.devolucionManual.criterios">	
				<table>
					<td class="labelText"><siga:Idioma key="facturacion.devolucionManual.numeroFactura"/></td>
					<td ><html:text styleClass="box" property="numeroFactura" maxlength="12" /></td>
					<%
					if (esConsejo){
					%>	
					<td class="labelText" >
						<siga:BusquedaPersona tipo="personas" idPersona="titular"></siga:BusquedaPersona>
						<html:hidden property="titular" />
						<html:hidden property="nombreTitular" size="37"/>
					</td>
					<%
					}else { 
					%>
					<td class="labelText" >
						<siga:BusquedaPersona tipo="colegiado"  idPersona="titular"></siga:BusquedaPersona>
						<html:hidden property="titular" />
						<html:hidden property="nombreTitular"/>
					</td>
					<% 	
					}
						int idInstitucion = Integer.parseInt(user.getLocation()); 
					
						if (idInstitucion == 2000 || idInstitucion >= 3000) { %>
		
							<td class="labelText" ><siga:Idioma key="facturacion.buscarFactura.literal.Deudor"/>&nbsp;</td>
							<td >
 								<siga:ComboBD nombre="destinatario" tipo="cmbDeudores"  ancho="170" clase="boxCombo" obligatorio="false"/>
							</td>
					
					<% 	} %>
				</table>
			<table>
								
					<td  class="labelText" width="80px"><siga:Idioma key="facturacion.devolucionManual.numeroRecibo"/></td>
					<td><html:text styleClass="box" property="numeroRecibo" maxlength="12" /></td>
					
					<td class="labelText" width="80px"><siga:Idioma key="facturacion.devolucionManual.numeroRemesa"/></td>
					<td><html:text styleClass="box" property="numeroRemesa" size='10' maxlength="12" /></td>

					<td class="labelText"  width="170px"><siga:Idioma key="facturacion.devolucionManual.fechaCargoDesde"/></td>
					<td><siga:Fecha  nombreCampo="fechaCargoDesde" valorInicial="<%=form.getFechaCargoDesde()%>"/></td>

					<td class="labelText" width="60px"><siga:Idioma key="facturacion.devolucionManual.fechaCargoHasta"/></td>
					<td><siga:Fecha  nombreCampo="fechaCargoHasta" valorInicial="<%=form.getFechaCargoHasta()%>"/></td>

				
				</table>

			</table>
		</siga:ConjCampos>	

	</html:form>

	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		
		<siga:ConjBotonesBusqueda botones="B" />

	<!-- FIN: BOTONES BUSQUEDA -->


	<!-- FORMULARIO PARA RECOGER LOS DATOS DE LA BUSQUEDA -->
<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
  <input type="hidden" name="actionModal" value="">
  <input type="hidden" name="modo" value="abrirBusquedaModal">
  <input type="hidden" name="clientes" value="1">

 </html:form>

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		// Funcion asociada a boton buscarCliente
		function refrescarLocal() {
			buscar();
		}

		// Funcion asociada a boton buscar
		function buscar() {
				sub();		
				mensaje();
				// Rango Fechas (desde / hasta)
				document.DevolucionesManualesForm.nombreTitular.value=document.getElementById('nombrePersona').value;
				if (compararFecha (document.DevolucionesManualesForm.fechaCargoDesde, document.DevolucionesManualesForm.fechaCargoHasta) == 1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
					return false;
				}
				if (validateDevolucionesManualesForm(document.DevolucionesManualesForm)){
					document.DevolucionesManualesForm.modo.value = "BuscarInicio";
					document.DevolucionesManualesForm.submit();
				}
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
					marginwidth="0"					 
					class="frameGeneral">
	</iframe>
	
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			// Asociada al boton MarcarTodos
			function accionMarcarTodos() {		
				if (window.frames.resultado.document.getElementById("sel")!=null){
					var dd =  window.frames.resultado.document.getElementsByName("sel");
					if (dd.type != 'checkbox') {
						for (i = 0; i < dd.length; i++){
							dd[i].checked=1;		
						}	
					}
					else{
						dd.checked=1;		
					}
				}	
			}
		
			// Asociada al boton DesmarcarTodos 
			function accionDesmarcarTodos() {		
				if (window.frames.resultado.document.getElementById("sel")!=null){
					var dd =  window.frames.resultado.document.getElementsByName("sel");
					if (dd.type != 'checkbox') {
						for (i = 0; i < dd.length; i++){
							dd[i].checked=0;		
						}
					}
					else{
						dd.checked=0;		
					}
				}	
			}
		
			// Asociada al boton ProcesarDevoluciones
			function accionProcesarDevoluciones() {
				var aDatos = new Array();
				document.DevolucionesManualesForm.recibos.value="";
				
				var checks =  window.frames.resultado.document.getElementsByName("sel");
				for (var i=0; i<checks.length; i++) {
					if (checks[i].checked) {
						var dato = checks[i].value;
						var num = i + 1;
						
						var datoFIED = jQuery("#resultado").contents().find("#devolucionManual_" + num).val();
						var datoMotivo = jQuery("#resultado").contents().find("select[id=motivoDevolucion]")[i].value;
						if (document.DevolucionesManualesForm.recibos.value=="") {
							document.DevolucionesManualesForm.recibos.value += datoFIED + "%%" + datoMotivo;
						} else {
							document.DevolucionesManualesForm.recibos.value += ";" + datoFIED + "%%" + datoMotivo;
						}
							
					}
				}

				if (document.DevolucionesManualesForm.recibos.value=="") {
					var mensaje2 = '<siga:Idioma key="messages.fact.error.noRecibos"/>'
					alert(mensaje2);
					return false;
					
				} else {
					var aux = document.DevolucionesManualesForm.modo.value;
					document.DevolucionesManualesForm.modo.value="modificar";
					var datos = ventaModalGeneral("DevolucionesManualesForm","P");
					if (datos != null) { 
  	 	 				if (datos == "MODIFICADO") {
			  	 	 		refrescarLocal();
  	  					}
  	  				}
					
					document.DevolucionesManualesForm.modo.value=aux;
				}
			}

					
		</script>
		<!-- FIN: SCRIPTS BOTONES -->	 	
	 	
		<!-- BOTONES ACCION: MT Marcar todos, DT Desmarcar todos, PD Procesar devoluciones -->	 
		<siga:ConjBotonesAccion botones="MT,DT,PD" />
		<!-- FIN: BOTONES ACCION -->	 


</div>
			
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>