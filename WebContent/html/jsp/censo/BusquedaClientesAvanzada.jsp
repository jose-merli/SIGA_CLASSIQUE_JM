<!DOCTYPE html>
<html>
<head>
<!-- BusquedaClientesAvanzada.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<%  
	HttpSession ses=request.getSession();
	

	// locales
	BusquedaClientesForm formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");
	if (formulario==null) {
		formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesForm");
	}
	// datos seleccionados Combo
	
	ArrayList tipoColeg = new ArrayList();	
	if (formulario.getEstadoFormulario()==null||formulario.getEstadoFormulario().equals("")||formulario.getEstadoFormulario().equals("0")){
	 	tipoColeg.add(String.valueOf(ClsConstants.ESTADO_COLEGIAL_ALTA));
	} else {
	 	tipoColeg.add(formulario.getTipoColegiacion());
	} 
	
	ArrayList tipoApunte = new ArrayList();	
	String paramIdTipoApunte = "";
	if (formulario.getTipoApunte()==null||formulario.getTipoApunte().equals("")){
	 	tipoApunte.add("");
	}else{
	 	tipoApunte.add(formulario.getTipoApunte());
	 	paramIdTipoApunte = "{\"idtipocv\":\""+formulario.getTipoApunte()+"\"}";
	} 
	
	ArrayList comision = new ArrayList();	
	if (formulario.getComision()==null||formulario.getComision().equals("")){
	 	comision.add("");
	}else{
	 	comision.add(formulario.getComision());
	} 
	
	ArrayList colegioSel = new ArrayList();
	colegioSel.add(formulario.getNombreInstitucion());
	
	ArrayList grupoClienteSel = new ArrayList();
	grupoClienteSel.add(formulario.getGrupoClientes());

	// colegiado
	String colegiado = formulario.getColegiado();
	if (colegiado==null) colegiado="";

	String sexo = formulario.getSexo();
	if (sexo==null) sexo="";
	
	String residente = formulario.getResidente();
	if (residente==null) residente="";
	
	String comunitario = formulario.getComunitario();
	if (comunitario==null) comunitario="";
	
	String ejerciente = formulario.getEjerciente();
	if (ejerciente==null) ejerciente="";
	
	String tipoCliente = formulario.getTipoCliente();
	if (tipoCliente==null) tipoCliente="";

	String titu = "";
	String loca = "censo.busquedaClientes.localizacion";

	if (colegiado.equals(ClsConstants.DB_TRUE)) {
		//colegiados
		titu = "censo.busquedaClientesAvanzada.colegiados.titulo";
	} else if(colegiado.equals(ClsConstants.DB_FALSE)){
		//no colegiados
		titu = "censo.busquedaClientesAvanzada.noColegiados.titulo";
	} else { 
		//letrados
		titu = "censo.busquedaClientesAvanzada.letrados.titulo";
	}

	// institucionesVisibles
	String institucionesVisibles = (String) request.getAttribute("CenInstitucionesVisibles");
	if (institucionesVisibles==null) institucionesVisibles="";
	/*String parametro[] = new String[1];
	parametro[0] = institucionesVisibles;*/
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
	
	// MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en más de una institucion
	// Obtengo el UserBean y en consecuencia la institucion a la que pertenece y su nombre
	
	String institucionAcceso=user.getLocation();
	String nombreInstitucionAcceso="";
	if (institucionAcceso.equalsIgnoreCase(institucionesVisibles)){
		CenInstitucionAdm institucionAdm = new CenInstitucionAdm(user);
		nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(institucionAcceso);
	}
	
   String [] institucionParam = {user.getLocation()};
  
   ArrayList idSubtipo1 = new ArrayList();
	ArrayList idSubtipo2 = new ArrayList();
	if (formulario.getIdTipoCVSubtipo1()==null||formulario.getIdTipoCVSubtipo1().equals("")){
	 	idSubtipo1.add("");	 
	} else {
	 	idSubtipo1.add(formulario.getIdTipoCVSubtipo1());	 
	} 
	
	if (formulario.getIdTipoCVSubtipo2()==null||formulario.getIdTipoCVSubtipo2().equals("")){
	 	idSubtipo2.add("");	
	} else {
	 	idSubtipo2.add(formulario.getIdTipoCVSubtipo2());	 
	} 
	
	String fechaNacimientoDesde = "";
	if (formulario.getFechaNacimientoDesde()!=null && !formulario.getFechaNacimientoDesde().equals("")){
		fechaNacimientoDesde = formulario.getFechaNacimientoDesde();
	}
	
	String fechaNacimientoHasta = "";
	if (formulario.getFechaNacimientoHasta()!=null && !formulario.getFechaNacimientoHasta().equals("")){
		fechaNacimientoHasta = formulario.getFechaNacimientoHasta();
	}
	
	String fechaIncorporacionDesde = "";
	if (formulario.getFechaIncorporacionDesde()!=null && !formulario.getFechaIncorporacionDesde().equals("")){
		fechaIncorporacionDesde = formulario.getFechaIncorporacionDesde();
	}
	
	String fechaIncorporacionHasta = "";
	if (formulario.getFechaIncorporacionHasta()!=null && !formulario.getFechaIncorporacionHasta().equals("")){
		fechaIncorporacionHasta = formulario.getFechaIncorporacionHasta();
	}
	
	String fechaAltaDesde = "";
	if (formulario.getFechaAltaDesde()!=null && !formulario.getFechaAltaDesde().equals("")){
		fechaAltaDesde = formulario.getFechaAltaDesde();
	}		
	
	String fechaAltaHasta = "";
	if (formulario.getFechaAltaHasta()!=null && !formulario.getFechaAltaHasta().equals("")){
		fechaAltaHasta = formulario.getFechaAltaHasta();
	}
	
	ArrayList tipoSel = new ArrayList();
	tipoSel.add(formulario.getTipo());
%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Properties"%>



	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>	
	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo titulo="<%=titu %>" localizacion="<%=loca %>"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="busquedaClientesAvanzadaForm" staticJavascript="false" />  
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->		
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->	
		<script language="JavaScript">
	
			// Funcion asociada a boton buscar
			function buscar(modo) {		
				sub();
							
				if(modo)
					document.forms[0].modo.value = modo;
				else
					document.forms[0].modo.value="buscarInit";
				
				document.forms[0].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();
			}
			
			function seleccionarTodos(pagina) {
				document.forms[0].seleccionarTodos.value = pagina;
				buscar('buscarPor');					
			}
	
			// Funcion asociada a boton busqueda simple
			function buscarSimple() {		
				document.forms[0].modo.value="abrir";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}
			
			// Funcion asociada a boton limpiar
			function limpiar()  {		
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}

			// Funcion asociada a boton nuevo
			function nuevo() {		
				document.forms[0].modo.value="nuevo";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}

			// Funcion asociada a boton Nueva Sociedad
			function nuevaSociedad() {		
				document.forms[0].modo.value="nuevaSociedad";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}			
			
			var tipoCurriculum;			
			function init() {			 
			   	tipoCurriculum=<%=tipoApunte%>;
			  	//jQuery("#tipoApunte").change();
			}
			
			function recargarCombos(tipo) {
				if (tipo) {
			    	if (tipoCurriculum!=tipo.value){
				  		limpiarCombo("idTipoCVSubtipo1");
	     		  		limpiarCombo("idTipoCVSubtipo2");
			    	}
			   	}			
			}
			
			function limpiarCombo(nombre) {
			   iframeCombo = window.top.frames[0].document.getElementById (nombre + "Frame");
				cadenaInicial = iframeCombo.src;
				
				if (cadenaInicial.indexOf("&elementoSel=[0]") > 1)  {
					return;
				}
				
				var ini = cadenaInicial.indexOf('&elementoSel=');
				if (ini < 1) 
					return;
				
				cadenaFinal = cadenaInicial.substring(0,ini) + "&elementoSel=[0]";
				
				var fin = cadenaInicial.indexOf('&', ini+2);
				if (fin > 1) {	
					cadenaFinal = cadenaFinal + cadenaInicial.substring(fin);
				}

				iframeCombo.src = cadenaFinal;
			}
							
			function deshabilitarCombos(o) {
			  	v_subTipo1=o; 
			  	if (o.options.length > 1) {
					 o.disabled = false;
			  	} else {
				 	o.disabled = true;
			  	}			 
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
	</head>


<body onload="init();">
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		<table class="tablaCentralCampos"  align="center">
			<html:form action="/CEN_BusquedaClientesAvanzada.do?noReset=true" method="POST" target="mainWorkArea">
				<html:hidden  name="busquedaClientesAvanzadaForm" property="modo"/>
				<!-- parametro para colegiados o no -->
				<html:hidden name="busquedaClientesAvanzadaForm" property = "colegiado" value="<%=colegiado%>"/>
				<html:hidden name="busquedaClientesAvanzadaForm" property = "avanzada"/>
				<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
				<html:hidden property="seleccionarTodos" />
				<html:hidden property="estadoFormulario" value="1"/>
				
				<tr>				
					<td>
						<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo1">
							<table class="tablaCampos" align="center" border="0">
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nif"/>
									</td>
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="nif" size="15" styleClass="box" />
									</td>
	
									<td class="labelText" width="20%">
										<% if (colegiado.equals(ClsConstants.DB_FALSE)) { %>
											<!-- caso de no colegiado-->
										<siga:Idioma key="censo.busquedaClientes.literal.nombreDenominacion" />
										<% } else { %>
											<!-- casos de letrado y colegiado-->
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nombre"/>
										<% } %>
									</td>				
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="nombrePersona" size="30" styleClass="box" />
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.apellido1"/>
									</td>
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="apellido1" size="30" styleClass="box" />
									</td>
									
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.apellido2"/>
									</td>
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="apellido2" size="30" styleClass="box" />
									</td>								
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaNacimientoDesde"/>
									</td>				
									<td>
										<siga:Fecha nombreCampo= "fechaNacimientoDesde" valorInicial="<%=fechaNacimientoDesde%>"/>										
									</td>
									
							        <td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaNacimientoHasta"/>
									</td>													
									<td>
										<siga:Fecha  nombreCampo= "fechaNacimientoHasta" valorInicial="<%=fechaNacimientoHasta%>" campoCargarFechaDesde="fechaNacimientoDesde"/>										
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.sexo"/>
									</td>
									<td>
										<!-- option select -->
										<html:select name="busquedaClientesAvanzadaForm" property="sexo" style = "null" styleClass = "boxCombo" value="<%=sexo %>">
											<html:option value="" > </html:option>
											<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>" ><siga:Idioma key="censo.sexo.hombre"/></html:option>
											<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>" ><siga:Idioma key="censo.sexo.mujer"/></html:option>
										</html:select>						
									</td>
								
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.domicilio"/>
									</td>
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="domicilio" size="40" styleClass="box" />
									</td>								
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.cp"/>
									</td>
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="codigoPostal" size="5" styleClass="box" />	
									</td>
							
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.telefono"/>
									</td>				
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="telefono" size="20" styleClass="box" />
									</td>
								</tr>
	
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fax"/>
									</td>
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="fax" size="20" styleClass="box" />
									</td>
							
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.correo"/>
									</td>
									<td>
										<html:text name="busquedaClientesAvanzadaForm" property="correo" size="30" styleClass="box" />
									</td>							
								</tr>
								
								<!-- FILA -->
		 						<tr>
								   <td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.grupoCliente"/>
									</td>
									<td colspan="3">
										<siga:Select queryId="getGruposClientes" id="grupoClientes" selectedIds="<%=grupoClienteSel%>"/>
									</td>
								</tr>
								
								<!-- FILA -->						
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.tipoApunteCV"/>
									</td>
									<td>
										<siga:Select id="tipoApunte"
													queryParamId="idtipocv"
													queryId="getCenTiposCV"
													selectedIds="<%=tipoApunte%>" 
													childrenIds="idTipoCVSubtipo1,idTipoCVSubtipo2"/>
									</td>
									
									<td>
										<siga:Select id="idTipoCVSubtipo1" 
													queryId="getCenTiposCVsubtipo1"
													parentQueryParamIds="idtipocv"
													params="<%=paramIdTipoApunte%>"
													selectedIds="<%=idSubtipo1%>"/>
													<!-- parmas = parametro -->
									</td>		
									<td>
										<siga:Select id="idTipoCVSubtipo2"
													queryId="getCenTiposCVsubtipo2" 
													params="<%=paramIdTipoApunte%>"
													selectedIds="<%=idSubtipo2%>"
													parentQueryParamIds="idtipocv"/>
									</td>
							   </tr>
  							</table>
						</siga:ConjCampos>

						<% if (colegiado.equals(ClsConstants.DB_TRUE) || colegiado.equals("2") || (colegiado.equals(ClsConstants.DB_FALSE) && !institucionAcceso.equalsIgnoreCase(institucionesVisibles))) { %>
							<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo2">
								<table class="tablaCampos" align="center" width="100%" border="0">
									<!-- FILA. aalg: se informa del significado de los campos de colegiado en el caso de letrado -->
									<% if (colegiado.equals("2")) { %>
										<tr>
											<td class="labelText" colspan="6">
												<siga:Idioma key="censo.busquedaClientesAvanzada.messages.colegiadoLetrado"/>
											</td>
										</tr>
									<%}%>
				
									<!-- FILA. aalg: se pasan los datos del colegio y número de colegiado al conjunto de campos referentes al colegio  -->
									<!-- MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en más de una institucion -->
									<% if (!institucionAcceso.equalsIgnoreCase(institucionesVisibles)){%> 
										<tr>				
											<td class="labelText" width="19%">
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.colegio"/>
											</td>				
											<td colspan="5">
												<% if (colegiado.equals(ClsConstants.DB_FALSE)) { %>
													<siga:Select id="nombreInstitucion" 
																queryId="getInstituciones"
																selectedIds="<%=colegioSel%>"/>
												<% } else { %>
													<siga:Select id="nombreInstitucion" 
																queryId="getNombreColegiosTodos"
																selectedIds="<%=colegioSel%>"/>
												<% } %>
											</td>
										</tr>		
									<%}%>
												
									<% if (colegiado.equals(ClsConstants.DB_FALSE)) { %>
										<!-- FILA -->
										<tr>
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientes.literal.tipoCliente"/>
											</td>
											<td>
												<!-- TIPO -->
												<siga:Select queryId="getTiposNoColegiado" id="tipo" selectedIds="<%=tipoSel%>"/>
											</td>
					
											<input type="hidden" name="numeroColegiado">
										</tr>
										
									<% } else { %>
										<!-- FILA -->
										<tr>
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/>
											</td>
											<td>
												<html:text name="busquedaClientesAvanzadaForm" property="numeroColegiado" maxlength="20" size="10" styleClass="box"></html:text>
											</td>
						
											<input type="hidden" name="tipoCliente">	
										</tr>	
									<% } %>		
				
									<% if (colegiado.equals(ClsConstants.DB_TRUE) || colegiado.equals("2")){ %>
										<!-- FILA -->
										<tr>					
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.tipoColegiacion"/>
											</td>				
				 							<td colspan="5">
				 								<siga:Select queryId="getTiposColegiacion" id="tipoColegiacion" selectedIds="<%=tipoColeg%>"/>
				 							</td>
										</tr>
										
										<!-- FILA -->
										<tr>				
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.situacion"/>
											</td>				
											<td>
												<!-- option select -->
												<html:select name="busquedaClientesAvanzadaForm" property="ejerciente" style = "null" styleClass = "boxCombo" value="<%=ejerciente %>">
													<html:option value="" > </html:option>
													<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
													<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
												</html:select>						
											</td>
			
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacionDesde"/>
											</td>
											<td>
												<siga:Fecha nombreCampo= "fechaIncorporacionDesde" valorInicial="<%=fechaIncorporacionDesde%>"/>
											</td>
											
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacionHasta"/>
											</td>
											<td>
												<siga:Fecha nombreCampo= "fechaIncorporacionHasta" valorInicial="<%=fechaIncorporacionHasta%>" campoCargarFechaDesde="fechaIncorporacionDesde"/>
											</td>
										</tr>
														
										<!-- FILA -->
										<tr>				
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.residente"/>
											</td>
											<td >
												<!-- option select -->
												<html:select name="busquedaClientesAvanzadaForm" property="residente" style = "null" styleClass = "boxCombo" value="<%=residente %>">
													<html:option value="" > </html:option>
													<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
													<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
												</html:select>						
											</td>
										
											<td class="labelText" >
												<siga:Idioma key="censo.busquedaClientesAvanzada.literal.comunitario"/>
											</td>
											<td colspan="3">
												<!-- option select -->
												<html:select name="busquedaClientesAvanzadaForm" property="comunitario" style = "null" styleClass = "boxCombo" value="<%=comunitario %>">
													<html:option value="" > </html:option>
													<html:option value="<%=ClsConstants.DB_TRUE %>" ><siga:Idioma key="general.yes"/></html:option>
													<html:option value="<%=ClsConstants.DB_FALSE %>" ><siga:Idioma key="general.no"/></html:option>
												</html:select>						
											</td>
										</tr>				
									<%}%>
								</table>		
							</siga:ConjCampos>
	
						<% 
							} else { 
								// campos de validacion que no hacen falta
						%>
							<input type="hidden" name="ejerciente">
							<input type="hidden" name="residente">
							<input type="hidden" name="fechaIncorporacion">
						<% 	} %>
	
						<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo3">
							<table class="tablaCampos" align="center">
								<!-- FILA -->
								<tr>				
									<td class="labelText" >
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaAltaDesde"/>
									</td>
									<td>
										<siga:Fecha nombreCampo= "fechaAltaDesde" valorInicial="<%=fechaAltaDesde%>"/>
									</td>
									
									<td class="labelText" >
										<siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaAltaHasta"/>
									</td>
									<td>
										<siga:Fecha nombreCampo= "fechaAltaHasta" valorInicial="<%=fechaAltaHasta%>" campoCargarFechaDesde="fechaAltaDesde"/>
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
	
		<%  
			String botones = "B,S";
			if (colegiado.equals(ClsConstants.DB_FALSE)) {
				botones += ",N,NS";
			} 
		%>

		<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="" />
		<!-- FIN: BOTONES BUSQUEDA -->
</div>
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
