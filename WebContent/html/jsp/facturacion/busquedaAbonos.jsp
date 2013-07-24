<!DOCTYPE html>
<html>
<head>
<!-- busquedaAbonos.jsp -->
<!-- 
	 Muestra el formulario de busqueda de abonos
	 VERSIONES:
		 miguel.villegas 07/03/2005
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
<%@ page import="com.siga.facturacion.form.GenerarAbonosForm"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");

	// Institucion del usuario de la aplicacion
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion
	String idPersonaBusqueda="";
	String busquedaCliente="";
	String busquedaIdAbono="";
	String busquedaIdFactura="";
	String contabilizadoBusqueda="";
	String formaPagoBusqueda="";
	String pagadoBusqueda="";
	String tipoAbonoBusqueda="";
	String busquedaNumeroAbono="";
	String nifCliente="";
CenPersonaAdm admPersona=new CenPersonaAdm(user);
	// Si el cliente es un letrado, le establezco a el como parte de la busqueda
	if (user.isLetrado()) {
		idPersonaBusqueda=String.valueOf(user.getIdPersona());
		//CenPersonaAdm admPersona=new CenPersonaAdm(user);
		 admPersona=new CenPersonaAdm(user);
		busquedaCliente=admPersona.obtenerNombreApellidos(idPersonaBusqueda);
		nifCliente = admPersona.obtenerNIF(idPersonaBusqueda);
	}
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	GenerarAbonosForm formSession = new GenerarAbonosForm();
	if (buscar!=null) {
		formSession = (GenerarAbonosForm) request.getSession().getAttribute("GenerarAbonosForm");
		idPersonaBusqueda=formSession.getIdPersonaBusqueda();
		busquedaIdAbono=formSession.getBusquedaIdAbono();
		busquedaIdFactura=formSession.getBusquedaIdFactura();
		busquedaCliente=formSession.getBusquedaCliente();
		contabilizadoBusqueda=formSession.getContabilizadoBusqueda();
		formaPagoBusqueda=formSession.getFormaPagoBusqueda();
		pagadoBusqueda=formSession.getPagadoBusqueda();
		tipoAbonoBusqueda=formSession.getTipoAbonoBusqueda();
		busquedaNumeroAbono=formSession.getNumeroAbono();
		nifCliente = admPersona.obtenerNIF(idPersonaBusqueda);
		funcionBuscar = "buscarPaginador()";
	}

    // Botones a mostrar
	String botones = "B,N,L";
%>


	<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="GenerarAbonosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			  titulo="facturacion.busquedaAbonos.literal.cabecera" 
			  localizacion="facturacion.busquedaAbonos.ruta"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>


	<body onLoad="ajusteAlto('resultado');desahabilitaBonotesLetrado();inicio();<%=funcionBuscar %>">
	
		<div id="camposRegistro" class="posicionBusquedaSolo" align="center">	

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->

			<table class="tablaCentralCampos" align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="facturacion.busquedaAbonos.literal.busquedaAbonos">
						<table class="tablaCampos" align="center" border="0">	
							<html:form action="/FAC_GenerarAbonos.do?noReset=true" method="POST" target="resultado">
								<html:hidden property = "actionModal" value=""/>
								<html:hidden property = "modo" value = ""/>
								<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
								<html:hidden property = "idPersonaBusqueda" value = "<%=idPersonaBusqueda%>"/>
								<input type="hidden" name="limpiarFilaSeleccionada" value="">
								<tr>
									<td class="labelText" width="8%">
									<siga:Idioma key="expedientes.auditoria.literal.tipo"/>
									</td>
									<td class="labelText" width="8%" style="text-align: right">
									<html:select name="datosGeneralesForm" property="tipoAbonoBusqueda" style = "null" styleClass = "box" value="<%=tipoAbonoBusqueda%>">
											<html:option value="" ></html:option>
											<html:option value="<%=ClsConstants.TIPO_ABONO_FACTURACION %>" ><siga:Idioma key="factSJCS.datosPagos.literal.facturacion"/></html:option>
											<html:option value="<%=ClsConstants.TIPO_ABONO_JUSTICIA_GRATUITA %>" ><siga:Idioma key="modulo.gratuita"/></html:option>
										</html:select>
									</td>
									<td class="labelText" width="15%" style="display:none">
										<html:text property="busquedaIdAbono" styleClass="box" size="12" maxlength="10" value="<%=busquedaIdAbono%>"></html:text>
									</td>
									<td class="labelText" width="20%" colspan="2">	
										<siga:Idioma key="facturacion.busquedaAbonos.literal.numeroAbono"/>
									</td>
									<td class="labelText">	
										<html:text property="numeroAbono" styleClass="box" size="12" maxlength="20" value="<%=busquedaNumeroAbono%>"></html:text>
									</td>
									<td class="labelText" colspan="2" style="text-align: right">	
										<siga:Idioma key="facturacion.busquedaAbonos.literal.fechaAbonoEntre"/>
										<siga:Fecha  nombreCampo="fechaAbonoDesde" valorInicial="<%=formSession.getFechaAbonoDesde()%>"/>&nbsp;&nbsp;																											
										<siga:Idioma key="facturacion.consultamorosos.literal.y"/>&nbsp;&nbsp;
										<siga:Fecha  nombreCampo="fechaAbonoHasta" valorInicial="<%=formSession.getFechaAbonoHasta()%>" campoCargarFechaDesde="fechaAbonoDesde"/>										
										</a>
									</td>
								</tr>
								<tr>
									<td class="labelText" colspan="2" width="8%">
									
									</td>
									<td class="labelText" colspan="2">									
										<siga:Idioma key="facturacion.busquedaAbonos.literal.numeroFactura"/>
									</td>
									<td class="labelText">
										<html:text property="busquedaIdFactura" styleClass="box" size="12" maxlength="20" value="<%=busquedaIdFactura%>"></html:text>
									</td>
									<td class="labelText" colspan="2" style="text-align: right">	
										<siga:Idioma key="facturacion.busquedaAbonos.literal.fechaFacturaEntre"/>
										<siga:Fecha  nombreCampo="fechaFacturaDesde" valorInicial="<%=formSession.getFechaFacturaDesde()%>"/>&nbsp;&nbsp;
										<siga:Idioma key="facturacion.consultamorosos.literal.y"/>&nbsp;&nbsp;
										<siga:Fecha  nombreCampo="fechaFacturaHasta" valorInicial="<%=formSession.getFechaFacturaHasta()%>" campoCargarFechaDesde="fechaFacturaDesde"/>										
									</td>
								</tr>
								<tr>
								<td class="labelText" colspan="2" width="8%">
									
								</td>
								<td class="labelText" width="20%" colspan="3">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.formaPagoAbono"/>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<html:select name="datosGeneralesForm" property="formaPagoBusqueda" style = "null" styleClass = "box" value="<%=formaPagoBusqueda%>">
										<html:option value="" ></html:option>
										<html:option value="<%=ClsConstants.TIPO_CARGO_BANCO %>" ><siga:Idioma key="censo.tipoAbono.banco"/></html:option>
										<html:option value="<%=ClsConstants.TIPO_CARGO_CAJA %>" ><siga:Idioma key="censo.tipoAbono.caja"/></html:option>
										</html:select>
					   	 		</td>
					   	 		<td class="labelText">
					   	 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<siga:Idioma key="facturacion.busquedaAbonos.literal.pagadoAbono"/>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<html:select name="datosGeneralesForm" property="pagadoBusqueda" style = "null" styleClass = "box" value="<%=pagadoBusqueda%>">
											<html:option value="" ></html:option>
											<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
											<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
										</html:select>
									</td>
									<td class="labelText">
										<siga:Idioma key="facturacion.busquedaAbonos.literal.contabilizadoAbono"/>&nbsp;&nbsp;&nbsp;
										<html:select name="datosGeneralesForm" property="contabilizadoBusqueda" style = "null" styleClass = "box" value="<%=contabilizadoBusqueda%>">
											<html:option value="" ></html:option>
											<html:option value="<%=ClsConstants.FACTURA_ABONO_CONTABILIZADA %>" ><siga:Idioma key="general.yes"/></html:option>
											<html:option value="<%=ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA %>" ><siga:Idioma key="general.no"/></html:option>
										</html:select>
									</td>
							 	</tr>
							 	
							 	<tr><td colspan="10">							 
									<siga:BusquedaPersona tipo="personas" anchoNum="10" anchoDesc="50"  titulo="gratuita.seleccionColegiadoJG.literal.titulo" idPersona="idPersonaBusqueda">
									</siga:BusquedaPersona>
								</td></tr>
							 	
					   	  </html:form>	
				        	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
								<html:hidden property = "actionModal" value=""/>
								<html:hidden property = "modo" value = "abrirBusquedaModal"/>
								<input type="hidden" name="clientes" value="1">
							</html:form>
							<html:form action="/FAC_AltaAbonos.do" method="POST" target="mainWorkArea" type="">
								<html:hidden property = "actionModal" value=""/>
								<html:hidden property = "modo" value = "abrir"/>
								
							</html:form>
				        </table>
					 </siga:ConjCampos>
					</td>
				</tr>
			</table>
			<!-- FIN: CAMPOS DE BUSQUEDA-->									
									
			<!-- INICIO: BOTONES BUSQUEDA -->
			<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
			-->
	
	        <siga:ConjBotonesBusqueda botones="<%=botones%>" titulo=""/>
			
			<!-- FIN: BOTONES BUSQUEDA -->
	
		
			<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
			<script language="JavaScript">

			function inicio(){		
			
				// Ajustamos el numero del colegiado y simulamos la busqueda
				<%if(!idPersonaBusqueda.equalsIgnoreCase("")){%>
				document.getElementById('numeroNifTagBusquedaPersonas').value="<%=nifCliente%>";				
				obtenerPersonas();
				<%}%>
			}
	
				// Funcion asociada a boton nuevo
				function nuevo() 
				{							
					var resultado = ventaModalGeneral("AltaAbonosForm","M");
					if (resultado=="MODIFICADO")
					{
						buscar();
					}
				}
				
				// Oculta los botones de busqueda para la consulta de un letrado
				function desahabilitaBonotesLetrado(){
					<% if(user.isLetrado()){%>
						 // Si es letrado fijamos los valores del tag a los valores entrantes
						 // Nif
		 				 document.getElementById('numeroNifTagBusquedaPersonas').readOnly=true;
		 				 document.getElementById('numeroNifTagBusquedaPersonas').value="<%=nifCliente%>";
		 				 document.getElementById('numeroNifTagBusquedaPersonas').className="boxConsulta";
		 				 // Nombre
		 				 document.getElementById('nombrePersona').value="<%=busquedaCliente%>";
		 				 document.getElementById('nombrePersona').className="boxConsulta";
		 				 
		 				 // Quitamos los botones (No se pueden hacer por id porque en ambos es 'idButton')
						 var bLimpiar = document.getElementsByName("limpiar"); 
						 bLimpiar[0].style.display='none';
						 var bBuscar = document.getElementsByName("buscarCliente")
						 bBuscar[0].style.display='none';
					<%}%> // Si no es letrado todo queda igual
				}

				// Funcion asociada a boton buscar
				function buscar()
				{
					sub();
					if (validateGenerarAbonosForm(document.GenerarAbonosForm)){
						document.GenerarAbonosForm.modo.value='buscarInit';
						document.GenerarAbonosForm.target='resultado';
						document.GenerarAbonosForm.submit();
					}	
				}
				function buscarPaginador()
				{
					if (validateGenerarAbonosForm(document.GenerarAbonosForm)){
						document.GenerarAbonosForm.modo.value='buscarPor';
						document.GenerarAbonosForm.target='resultado';
						document.GenerarAbonosForm.submit();
					}	
				}
				
				// Asociada al boton Restablecer
				function limpiar() 
				{
					document.GenerarAbonosForm.idPersonaBusqueda.value="<%=idPersonaBusqueda%>";
					document.GenerarAbonosForm.busquedaIdAbono.value="";
					document.GenerarAbonosForm.busquedaIdFactura.value="";
					document.GenerarAbonosForm.tipoAbonoBusqueda.value="";
					document.GenerarAbonosForm.numeroAbono.value="";
					document.GenerarAbonosForm.contabilizadoBusqueda.value="";
					document.GenerarAbonosForm.formaPagoBusqueda.value="";
					document.GenerarAbonosForm.pagadoBusqueda.value="";
					document.GenerarAbonosForm.fechaAbonoDesde.value="";
					document.GenerarAbonosForm.fechaAbonoHasta.value="";
					document.GenerarAbonosForm.fechaFacturaDesde.value="";
					document.GenerarAbonosForm.fechaFacturaHasta.value="";
					document.GenerarAbonosForm.numeroNifTagBusquedaPersonas.value="";
					document.GenerarAbonosForm.nombrePersona.value="";
				}
				
				function buscarCliente() {

				 	var resultado = ventaModalGeneral("busquedaClientesModalForm","G");

				 	// Si he recuperado datos
					if((resultado!=undefined)&&(resultado[0]!=undefined)) {
						document.all.GenerarAbonosForm.idPersonaBusqueda.value = resultado[0];
						document.all.GenerarAbonosForm.busquedaCliente.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	
						//document.location.reload();
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
		</div>
		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>