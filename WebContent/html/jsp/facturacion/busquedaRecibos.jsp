<!-- busquedaRecibos.jsp -->
<!-- EJEMPLO DE VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acción sobre los registros debido a que nisiquiera
	 necesita boton volver, ya que esta pagina representa UNA BSQUEDA PRINCIPAL
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");

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
	
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="facturacion.devolucionManual.titulo" 
		localizacion="facturacion.devolucionManual.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

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
<% 		if (form.getHayMotivos().equals("0")) { %>
			// NO HAY MOTIVOS
			var m = '<siga:Idioma key="messages.fact.error.motivosNoCargados"/>';
			alert(m);
			return false;
<% 		} %>
		
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
		<input type="hidden" name="tablaDatosDinamicosD">		
	    <input type="hidden" name="actionModal" value="">
		
			<siga:ConjCampos leyenda="facturacion.devolucionManual.criterios">	
			<table align="center">
				<tr>				
					<td  class="labelText" width="130px"><siga:Idioma key="facturacion.devolucionManual.numeroRecibo"/></td>
					<td><html:text styleClass="box" property="numeroRecibo" maxlength="12" /></td>

					<td class="labelText"  width="170px"><siga:Idioma key="facturacion.devolucionManual.fechaCargoDesde"/></td>
					<td><html:text styleClass="box" property="fechaCargoDesde" cols="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(fechaCargoDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
		
					<td class="labelText"><siga:Idioma key="facturacion.devolucionManual.fechaCargoHasta"/></td>
					<td><html:text styleClass="box" property="fechaCargoHasta" cols="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(fechaCargoHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
				</tr>
				<tr>				
					<td class="labelText" width="130px"><siga:Idioma key="facturacion.devolucionManual.numeroRemesa"/></td>
					<td><html:text styleClass="box" property="numeroRemesa" maxlength="12" /></td>

					<td class="labelText" width="170px"><siga:Idioma key="facturacion.devolucionManual.titularDomiciliacion"/></td>
					<td>
						<html:button property="idButton" onclick="return buscarCliente();" styleClass="button"><siga:Idioma key="general.boton.search"/> </html:button>
						<html:button property="idButton" onclick="return limpiarCliente();" styleClass="button"><siga:Idioma key="general.boton.clear"/> </html:button>
						<html:hidden property="titular" />
					</td>
					<td colspan="2">
						<html:text styleClass="boxConsulta" property="nombreTitular" size="50" readOnly="true"/>
					</td>
				</tr>
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
		<!-- Funcion asociada a boton buscarCliente -->
		function buscarCliente() 
		{
			var datos = new Array();
			datos[0] = ""; 			// idpersona
			datos[1] = ""; 			// idInstitucion
			datos[2] = ""; 			// Numero Colegiado
			datos[3] = ""; 			// NIF
			datos[4] = ""; 			// Nombre
			datos[5] = ""; 			// Apellido1
			datos[6] = ""; 			// Apellido2

			var datos = ventaModalGeneral("busquedaClientesModalForm","G");
			if ((datos == null) || (typeof datos[0] == "undefined"))  {
				return false;
			}
			
			document.DevolucionesManualesForm.titular.value 		 = datos[0];
			document.DevolucionesManualesForm.nombreTitular.value = datos[4] + " " + datos[5] + " " + datos[6];
		}

		<!-- Funcion asociada a boton buscar -->
		function limpiarCliente() 
		{
				document.DevolucionesManualesForm.titular.value="";
				document.DevolucionesManualesForm.nombreTitular.value= "";
		}

		function refrescarLocal() 
		{
			buscar();
		}

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
				sub();		
				mensaje();
				// Rango Fechas (desde / hasta)
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
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			<!-- Asociada al boton MarcarTodos -->
			function accionMarcarTodos() 
			{		
				if (document.frames.resultado.document.getElementById("sel")!=null){
					var dd = document.frames.resultado.document.getElementsByName("sel");
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
		
			<!-- Asociada al boton DesmarcarTodos -->
			function accionDesmarcarTodos() 
			{		
				if (document.frames.resultado.document.getElementById("sel")!=null){
					var dd = document.frames.resultado.document.getElementsByName("sel");
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
		
			<!-- Asociada al boton ProcesarDevoluciones -->
			function accionProcesarDevoluciones() 
			{
				var aDatos = new Array();
				document.DevolucionesManualesForm.recibos.value="";
				
				var oCheck = document.frames.resultado.document.getElementsByName("sel");
				var oCheck2 = document.frames.resultado.document.getElementsByName("motivoDevolucion");

				/* RGG 08/01/2007 */
				for(i=0; i<oCheck.length; i++)
				{
					if (oCheck[i].checked)
					{
						var dato = oCheck[i].value;
						var datoMotivo = oCheck2[i].value;
						document.DevolucionesManualesForm.recibos.value += ";" + datoMotivo + "%%" + dato;
					}
				}

				if (document.DevolucionesManualesForm.recibos.value=="")
				{
					var mensaje2 = '<siga:Idioma key="messages.fact.error.noRecibos"/>'
					alert(mensaje2);
					return false;
				}
				else
				{
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