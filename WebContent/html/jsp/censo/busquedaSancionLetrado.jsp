<!DOCTYPE html>
<html>
<head>
<!-- busquedaSancionLetrado.jsp -->

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
<%@ page import="com.siga.censo.form.SancionesLetradoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
 
<!-- JSP -->
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	SancionesLetradoForm formulario = (SancionesLetradoForm)request.getAttribute("SancionesLetradoForm");
	String tienepermisoArchivo = (String) request.getAttribute("tienepermiso");
	String volver = (String) request.getAttribute("volver");
	
	String idPersonaBusqueda="";
	String busquedaCliente="";
	String nifCliente="";
	String fechaInicioArchivada = "";
	String fechaFinArchivada="";
	
	// Si el cliente es un letrado, le establezco a el como parte de la busqueda
	if (user.isLetrado()) {
		idPersonaBusqueda=String.valueOf(user.getIdPersona());
		CenPersonaAdm admPersona=new CenPersonaAdm(user);
		busquedaCliente=admPersona.obtenerNombreApellidos(idPersonaBusqueda);
		nifCliente = admPersona.obtenerNIF(idPersonaBusqueda);
	} else if (formulario.getColegiadoBuscar()!= null && !formulario.getColegiadoBuscar().equals("")){
		idPersonaBusqueda=String.valueOf(formulario.getColegiadoBuscar());
		CenPersonaAdm admPersona=new CenPersonaAdm(user);
		busquedaCliente=admPersona.obtenerNombreApellidos(idPersonaBusqueda);
		nifCliente = admPersona.obtenerNIF(idPersonaBusqueda);		
	}

	ArrayList selSancion = new ArrayList();
	ArrayList selColegio = new ArrayList();
	selSancion.add (formulario.getTipoSancionBuscar());	
	selColegio.add (formulario.getNombreInstitucionBuscar());	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="censo.busquedaSancionesLetrado.titulo" localizacion="censo.busquedaSancionesLetrado.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="SancionesLetradoForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->		
	
	<script language="JavaScript">	
		function limpiarCliente () {
			document.getElementById('colegiadoBuscar').value = "";
			document.getElementById('nombreMostrado').value = "";
		}	
	</script>	
</head>

<body onload="ajusteAlto('resultado')">
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<siga:ConjCampos leyenda="censo.busquedaSancionesLetrado.literal.titulo1">
		<html:form action="/CEN_SancionesLetrado.do?noReset=true" method="POST" target="resultado">
			<table class="tablaCampos" align="center" width="100%" border="0">						
				<html:hidden name="SancionesLetradoForm" property = "modo" value = ""/>
				<input type="hidden" name= "actionModal" value = "">
				<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
	
				<tr>				
					<td class="labelText">
						<siga:Idioma key="censo.busquedaSancionesLetrado.literal.colegio"/>
					</td>
					<td>
						<siga:ComboBD nombre = "nombreInstitucionBuscar" tipo="cmbInstitucionesAbreviadas" clase="boxCombo" obligatorio="false" elementoSel="<%=selColegio%>"/>			
					</td>		
					
					<td class="labelText">
						<siga:Idioma key="censo.busquedaSancionesLetrado.literal.tipoSancion"/>
					</td>				
					<td>
						<siga:ComboBD nombre = "tipoSancionBuscar" tipo="cmbTipoSancion"  clase="boxCombo" obligatorio="false" elementoSel="<%=selSancion%>"/>
					</td>
			</tr>
				<tr>
				<% if(!ClsConstants.esColegio(user.getLocation())) { %>		
						<td class="labelText">
							<siga:Idioma key="censo.BusquedaSancionesLetrado.literal.refCGAE"/>															
						</td>						
						<td>				
	   						<html:text property="refCGAE" size="20" maxlength="50" styleClass="box" readOnly="false" value='${SancionesLetradoForm.refCGAE}'/>
						</td>	
				<% } %>
					
				
					<td class="labelText">
						<siga:Idioma key="censo.BusquedaSancionesLetrado.literal.refColegio2"/>
					</td>
					<td >
						<html:text property="refColegio" size="20" maxlength="50" styleClass="box" readOnly="false" value='${SancionesLetradoForm.refColegio}'></html:text>
					</td>
				</tr>	
				

				<tr>
					<td id="busquedaLetrado" class="labelText" colspan="6">
							<!-- Si la busqueda se realiza por idPersona, el campo numeroLetrado no puede modificarse, en cambio
		 					si la busqueda se realiza mediante el campo numeroLetrado se podr�a modificar por pantalla sin
		 					necesidad de seleccionarlo por el bot�n -->
							<html:hidden name="SancionesLetradoForm" property="colegiadoBuscar" size="8" maxlength="80" styleClass="boxConsulta" value = "<%=idPersonaBusqueda%>" readOnly="true"></html:hidden>
							<html:hidden property = "colegiadoBuscar" value = "<%=idPersonaBusqueda%>"/>		
							<script type="text/javascript">
								jQuery(document).ready(function () {		
									<% if(idPersonaBusqueda!=null && !idPersonaBusqueda.trim().equals("")) { %>
											jQuery("#nombrePersona").val("<%=busquedaCliente%>");
											jQuery("#numeroNifTagBusquedaPersonas").val("<%=nifCliente%>");	
									<% } %>
								});	
							</script>	
					
						<% if(user.isLetrado()) { %>
							<siga:BusquedaPersona tipo="personas" titulo="gratuita.seleccionColegiadoJG.literal.titulo" anchoNum="10" anchoDesc="50" idPersona="colegiadoBuscar"></siga:BusquedaPersona>
						<% } else { %>
							<siga:BusquedaPersona tipo="personas" titulo="gratuita.seleccionColegiadoJG.literal.titulo" anchoNum="10" anchoDesc="50" idPersona="colegiadoBuscar"></siga:BusquedaPersona>									
						<% } %>
					</td>
				</tr>
			</table>

			<table class="tablaCampos" align="center" width="100%">
				<tr>
					<td class="labelText" >
						<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.sancionesReahabilitadas"/>		
						<html:checkbox name="SancionesLetradoForm" property="chkRehabilitado" value="0"></html:checkbox>			
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.listadoAsistencias.literal.fecha"/>
					</td>
					<td>
						<html:select  name="SancionesLetradoForm" property="mostrarTiposFechas" styleClass="boxCombo" >			
							<html:option value=""></html:option>
							<html:option value="<%=ClsConstants.COMBO_MOSTRAR_ACUERDO%>"><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.Acuerdo"/></html:option>
							<html:option value="<%=ClsConstants.COMBO_MOSTRAR_FIN%>" ><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fin"/></html:option>
							<html:option value="<%=ClsConstants.COMBO_MOSTRAR_FIRMEZA%>" ><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.firmeza"/></html:option>
							<html:option value="<%=ClsConstants.COMBO_MOSTRAR_IMPOSICION%>" ><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.imposicion"/></html:option>				
							<html:option value="<%=ClsConstants.COMBO_MOSTRAR_INICIO%>" ><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.inicio"/></html:option>										
							<html:option value="<%=ClsConstants.COMBO_MOSTRAR_REHABILITADO%>" ><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.rehabilitado"/></html:option>
							<html:option value="<%=ClsConstants.COMBO_MOSTRAR_RESOLUCION%>" ><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.resolucion"/></html:option>
						</html:select>			
					</td>
					
					<td class="labelText">
						<siga:Idioma key="facturacion.consultamorosos.literal.desde"/>	
					</td>	
					<td>
						<siga:Fecha  nombreCampo= "fechaInicioBuscar" valorInicial="${SancionesLetradoForm.fechaInicioBuscar}"/>
					</td>	

					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaSOJ.literal.hasta"/>
					</td>
					<td>
						<siga:Fecha  nombreCampo= "fechaFinBuscar" valorInicial="${SancionesLetradoForm.fechaFinBuscar}"/>
					</td>			
				</tr>	

				<% if(tienepermisoArchivo.equals("1")) { %>
					<tr>
 						<td class="labelText" >
							<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.mostrarSanciones"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<html:checkbox name="SancionesLetradoForm" property="mostrarSanciones" value="<%=ClsConstants.COMBO_MOSTRAR_SINARCHIVAR%>"></html:checkbox>
						</td>
							
						<td class="labelText" colspan="2" align="center">
							<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fArchivada"/>
						</td>

						<td class="labelText">		
						  	<siga:Idioma key="facturacion.consultamorosos.literal.desde"/>&nbsp;&nbsp;
						</td>
						<td>
						  	<siga:Fecha  nombreCampo= "fechaInicioArchivada" valorInicial="${SancionesLetradoForm.fechaInicioArchivada}"/>
						</td>

						<td class="labelText">
						  	<siga:Idioma key="gratuita.busquedaSOJ.literal.hasta"/>&nbsp;&nbsp;
						</td>
						<td>
						  	<siga:Fecha  nombreCampo= "fechaFinArchivada" valorInicial="${SancionesLetradoForm.fechaFinArchivada}"/>
						</td>
					</tr>
				<% } else { %>
						<html:hidden name="SancionesLetradoForm" property="fechaInicioArchivada" size="10" styleClass="box" value="" readOnly="true"></html:hidden>
						<html:hidden name="SancionesLetradoForm" property="FechaFinArchivada" size="10" styleClass="box" value="" readOnly="true"></html:hidden>
				<% } %>						
			</table>
		</html:form>
	</siga:ConjCampos>
	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->	
	<siga:ConjBotonesBusqueda botones="L,B,N"  titulo="" />
	<!-- FIN: BOTONES BUSQUEDA -->

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		jQuery(document).ready(function () {		
			<%	if(volver!=null && volver.trim().equals("volver")) {  %>
					document.forms[0].modo.value="buscarInit";
					document.forms[0].target="resultado";	
					document.forms[0].submit();	
			<%	}  %>
		});	
		
		function buscar() {		
			sub();
			 if ((document.forms[0].mostrarTiposFechas.value=="") && ((document.forms[0].fechaInicioBuscar.value!="")||(document.forms[0].fechaFinBuscar.value!=""))){ 
				alert ('<siga:Idioma key="general.message.tipoFecha"/>');
				fin();
				return false;
			}
			document.forms[0].modo.value="buscarInit";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}

		function nuevo()  {	
			sub();
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="mainWorkArea";
			document.forms[0].submit();	
		}
				
		// Funcion asociada a boton limpiar
		function limpiar()  {		
			document.forms[0].reset();
			document.getElementById('colegiadoBuscar').value = "";
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
		class="frameGeneral"></iframe>

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE LETRADOS -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes"	value="1">
		<input type="hidden" name="busquedaSancion" value="1">
	</html:form>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>