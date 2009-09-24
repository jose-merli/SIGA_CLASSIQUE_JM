<!-- consultaDatosColegiales.jsp -->
<!-- 
	 Muestra los datos colegiales generales de un cliente
	 VERSIONES:
	 miguel.villegas 28-12-2004 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String fecha="";
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	boolean bOcultarHistorico = user.getOcultarHistorico();
	

	// Datos del cliente a visualizar
	Long idPersona=(Long)request.getAttribute("IDPERSONA"); // Obtengo el identificador de la persona
	
	String modo=(String)request.getAttribute("ACCION"); // Obtengo la accion anterior
	String nombre=(String)request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero=(String)request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona	
	String idInstitucion=(String)request.getAttribute("IDINSTITUCIONPERSONA"); // Obtengo el identificador de la institucion	
	String residencia=(String)request.getAttribute("RESIDENTE"); // Obtengo si es editable la residencia o no
	String botonesAccion="N";

	// Institucion del usuario de la aplicacion
	String idInstUsuario=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion
	

	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String acceso=usr.getAccessType();
	boolean accesoAdm=acceso.equals(SIGAPTConstants.ACCESS_FULL);
	String modoSig = request.getAttribute("modelo").toString(); // Obtengo la operacion (consulta,modificar o insertar)a realizar		
	
	// Obtencion de la informacon relacionada con el colegiado
	CenColegiadoBean datosCol = (CenColegiadoBean) request.getAttribute("DATCOLEGIAL");		
	
	
	// Obtencion del valor del seguro (caso que exista)
	ArrayList arraySel= new ArrayList();
	String seguro="";
	String cuentaSJCS="";
	if (request.getAttribute("DATSEGURO") != null){	
		Enumeration enumSel = ((Vector)request.getAttribute("DATSEGURO")).elements();	
		while (enumSel.hasMoreElements())
		{
			Row row = (Row) enumSel.nextElement();
			arraySel.add(row.getString(CenTiposSeguroBean.C_IDTIPOSSEGURO));
			seguro=row.getString(CenTiposSeguroBean.C_NOMBRE);
		} 
	}

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (request.getAttribute("PESTANASITUACION")!=null && request.getAttribute("PESTANASITUACION").equals(ClsConstants.DB_TRUE)){
	  botonesAccion="N,C";
	}else{  
	  if (!busquedaVolver.equals("volverNo")) { 
		botonesAccion="V,N";
	  }
	}  

	// RGG 05-10-2005 Cambio para modificar el nColegiado si viene parametro
	String editarNColegiado = (String) request.getAttribute("editarNColegiado");
	
	boolean bEditarNColegiado = false;
	if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ 
	  bEditarNColegiado=false;
	}else{
	  if (new Integer(idInstUsuario.substring(0,1)).intValue()>=3 || idInstUsuario.equals("2000")){// Siempre dejaremos modificar el n� colegiado al CGAE y Consejos
	    bEditarNColegiado=true;
	  }else{
	    if (editarNColegiado!=null && !editarNColegiado.equals("")) {
		bEditarNColegiado = true;
	    }
	  } 
	} 
	

	// RGG 14-02-2005 Muevo siempre el valor de ncolegiado o ncomunitario 
	// a NCOLEGIADO para que se muestre correctamente
	// Lo hago en funcion de COMUNITARIO
	String numeroColegiadoMostrar = "";
	if (datosCol.getComunitario().equals(ClsConstants.DB_TRUE)) {
		numeroColegiadoMostrar = datosCol.getNComunitario();	
	} else {
		numeroColegiadoMostrar = datosCol.getNColegiado();	
	}
	

%>	

<html>

	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DatosColegialesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
		<siga:TituloExt 
			titulo="censo.fichaCliente.datosColegiales.cabecera" 
			localizacion="censo.fichaCliente.datosColegiales.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

	
	</head>

	<body class="tablaCentralCampos" >

		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
	
		<table class="tablaCentralCampos" align="center" cellspacing=0>

			<tr>				
				<td class="titulitosDatos">
					<siga:Idioma key="censo.consultaDatosColegiales.literal.titulo1"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>&nbsp;&nbsp;
					<% if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<% }else{ %>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>					
				</td>					
			</tr>
			
		</table>

			<!-- CAMPOS DEL REGISTRO -->
			<table class="tablaCentralCampos" align="center">		

				<html:form action="/CEN_DatosColegiales.do" method="POST">

				<html:hidden property = "modo" value = ""/>
				<!--html:hidden property = "actionModal" value=""/-->				
				<html:hidden property="motivo" value=""/> 				
				<html:hidden property="idPersona" value="<%=idPersona.toString()%>"/> 				
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/> 
				<html:hidden property="nombre" value="<%=nombre%>"/> 	
				<html:hidden property="numero" value="<%=numero%>"/>
				 <input type="hidden" name="pestanaSituacion" value="<%=request.getAttribute("PESTANASITUACION")%>">											

				<tr>				
					<td width="100%" align="center">

						<!-- SUBCONJUNTO DE DATOS -->
						<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo2">

						<% if (request.getAttribute("DATCOLEGIAL") == null){%>
							<br><br>
								<p class="Title" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
							<br><br>
						<% } else { %>						
							<table align="center" width="100%">
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.resultadosSolicitudesModificacion.literal.nColegiado"/>&nbsp;
									</td>				
									<td>
										<% 
											//if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ 
											if (bEditarNColegiado){ 
										%>
											<html:text name="DatosColegialesForm" property="numColegiado" maxlength="20" size="20" styleClass="box" value="<%=numeroColegiadoMostrar%>" readOnly="false"></html:text>
										<% } else { %>
											<!-- MAV 25/8/05 Resolucion de incidencia. Ahora NADIE puede editar el num colegiado -->
											<!-- RGG 05-10-05 Te equivocas, YO SI PUEDO, si vengo desde solicitud de incorporaci�n. -->
											<!--html:text name="DatosColegialesForm" property="numColegiado" maxlength="20" size="20" styleClass="box" value="<%=numeroColegiadoMostrar%>"--><!--/html:text-->
											<html:text name="DatosColegialesForm" property="numColegiado" maxlength="20" size="20" styleClass="boxConsulta" value="<%=numeroColegiadoMostrar%>" readOnly="true"></html:text>
										<% } %>											
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaPresentacion"/>&nbsp;(*)
									</td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
										 	<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaPresentacion());%>
											<% if (fecha.equalsIgnoreCase("")){ %>									 	
												<html:text property="fechaPresentacion" size="10" styleClass="boxConsulta" value="" readOnly="true"></html:text>
											<% }else{ %>	
												<html:text property="fechaPresentacion" size="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true"></html:text>										
											<% } %>
										<% } else { %>
											<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaPresentacion());%>
			  								<html:text property="fechaPresentacion" size="10" styleClass="box" value="<%=fecha%>" readOnly="true"></html:text>										
											<a href='javascript://' onClick="return showCalendarGeneral(fechaPresentacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
											</a>																	
										<% } %>												
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaIncorporacion"/>&nbsp;(*)
									</td>
									<td>									
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
										 	<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaIncorporacion());%>																			
											<% if (fecha.equalsIgnoreCase("")){ %>									 	
												<html:text property="fechaIncorporacion" size="10" styleClass="boxConsulta" value="" readOnly="true"></html:text>
											<% }else{ %>										 										 	
												<html:text property="fechaIncorporacion" size="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true"></html:text>
											<% } %>											
										<% } else { %>
											<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaIncorporacion());%>
			  								<html:text property="fechaIncorporacion" size="10" styleClass="box" value="<%=fecha%>" readOnly="true"></html:text>										
											<a href='javascript://' onClick="return showCalendarGeneral(fechaIncorporacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
											</a>																	
										<% } %>																							
									</td>									
								</tr>	
								<tr>												
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaJura"/>&nbsp;
									</td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
										 	<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaJura());%>										
											<% if (fecha.equalsIgnoreCase("")){ %>									 	
												<html:text property="fechaJura" size="10" styleClass="boxConsulta" value="" readOnly="true"></html:text>
											<% }else{ %>										 	
												<html:text property="fechaJura" size="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true"></html:text>
											<% } %>	
										<% } else { %>
											<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaJura());%>
			  								<html:text property="fechaJura" size="10" styleClass="box" value="<%=fecha%>" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaJura);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
											</a>																	
										<% } %>																																		
									</td>						
									
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaTitulacion"/>&nbsp;
									</td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>									
									 		<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaTitulacion());%>
											<% if (fecha.equalsIgnoreCase("")){ %>									 	
												<html:text property="fechaTitulacion" size="10" styleClass="boxConsulta" value="" readOnly="true"></html:text>
											<% }else{ %>										 										 	
												<html:text property="fechaTitulacion" size="20" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true"></html:text>
											<% } %>											
										<% } else { %>
											<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaTitulacion());%>
			  								<html:text property="fechaTitulacion" size="10" styleClass="box" value="<%=fecha%>" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaTitulacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
											</a>																	
										<% } %>																																													
									</td>	
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaDentologico"/>&nbsp;
									</td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
										 	<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaDeontologia());%>
											<% if (fecha.equalsIgnoreCase("")){ %>									 	
												<html:text property="fechaDeontologia" size="10" styleClass="boxConsulta" value="" readOnly="true"></html:text>
											<% }else{ %>										 										 	
												<html:text property="fechaDeontologia" size="20" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true"></html:text>
											<% } %>											
										<% } else { %>
											<% fecha=GstDate.getFormatedDateShort("",datosCol.getFechaDeontologia());%>
			  								<html:text property="fechaDeontologia" size="10" styleClass="box" value="<%=fecha%>" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaDeontologia);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
											</a>																	
										<% } %>																																																								
									</td>											
								</tr>
								<tr>	
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.comunitario"/>&nbsp;
									</td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (datosCol.getComunitario().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
													<input type="checkbox" name="comunitario" value="1" checked disabled>
											<% }else{ %>
													<input type="checkbox" name="comunitario" value="1" disabled>
											<% } %>																												
										<% } else { %>
											<% if (datosCol.getComunitario().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>										
					  								<input type="checkbox" name="comunitario" value="1" checked>
											<% }else{ %>
					  								<input type="checkbox" name="comunitario" value="1" disabled>
											<% } %>
										<% } %>											
									</td>	
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.titulacion"/>&nbsp;
									</td>
									<td>	
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (datosCol.getIndTitulacion().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
				  								<input type="checkbox" name="indTitulacion" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="indTitulacion" value="1" disabled>
											<% } %>																																					
										<% } else { %>
											<% if (datosCol.getIndTitulacion().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>										
				  								<input type="checkbox" name="indTitulacion" value="1" checked>
											<% }else{ %>
				  								<input type="checkbox" name="indTitulacion" value="1">
											<% } %>
										<% } %>																						
									</td>														
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.otrosColegios"/>&nbsp;
									</td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (datosCol.getOtrosColegios().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
				  								<input type="checkbox" name="otrosColegios" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="otrosColegios" value="1" disabled>
											<% } %>																												
										<% } else { %>
											<% if (datosCol.getOtrosColegios().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>										
				  								<input type="checkbox" name="otrosColegios" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="otrosColegios" value="1" disabled>
											<% } %>
										<% } %>																																	
									</td>
								<tr>	
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.residente"/>&nbsp;
									</td>
									<td>	
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (datosCol.getSituacionResidente().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
				  								<input type="checkbox" name="situacionResidente" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="situacionResidente" value="1" disabled>
											<% } %>																																														
										<% } else { %>
												<% if (datosCol.getSituacionResidente().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
					  								<input type="checkbox" name="situacionResidente" value="1" checked>
												<% }else{ %>
					  								<input type="checkbox" name="situacionResidente" value="1">
												<% } %>
										<% } %>										
									</td>									
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.ejerciente"/>&nbsp;
									</td>
									<td>	
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (datosCol.getSituacionEjercicio().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
				  								<input type="checkbox" name="situacionEjercicio" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="situacionEjercicio" value="1" disabled>
											<% } %>									
										<% } else { %>
											<% if (datosCol.getSituacionEjercicio().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>										
				  								<input type="checkbox" name="situacionEjercicio" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="situacionEjercicio" value="1" disabled>
											<% } %>
										<% } %>																					
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.empresa"/>&nbsp;
									</td>
									<td>	
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (datosCol.getSituacionEmpresa().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
				  								<input type="checkbox" name="situacionEmpresa" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="situacionEmpresa" value="1" disabled>
											<% } %>																																				
										<% } else { %>
											<% if (datosCol.getSituacionEmpresa().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>										
				  								<input type="checkbox" name="situacionEmpresa" value="1" checked>
											<% }else{ %>
				  								<input type="checkbox" name="situacionEmpresa" value="1">
											<% } %>
										<% } %>																					
									</td>
								</tr>	
								<tr>
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.jubilacion"/>&nbsp;
									</td>
									<td>
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (datosCol.getJubilacionCuota().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>
				  								<input type="checkbox" name="jubilacionCuota" value="1" checked disabled>
											<% }else{ %>
				  								<input type="checkbox" name="jubilacionCuota" value="1" disabled>
											<% } %>																																					
										<% } else { %>
											<% if (datosCol.getJubilacionCuota().equalsIgnoreCase(ClsConstants.DB_TRUE)){ %>										
				  								<input type="checkbox" name="jubilacionCuota" value="1" checked>
											<% }else{ %>
				  								<input type="checkbox" name="jubilacionCuota" value="1">
											<% } %>
										<% } %>																																												
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.seguro"/>&nbsp;
									</td>
									<td  colspan="2">
										<% if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ %>
											<% if (request.getAttribute("DATSEGURO") == null){%>
												<html:text property="tipoSeguro" size="" styleClass="boxConsulta" value="" readOnly="true"></html:text>
											<% }else{ %>
												<html:text property="tipoSeguro" size="20" styleClass="boxConsulta" value="<%=seguro%>" readOnly="true"></html:text>											
											<% } %>
										<% }else{ %>
											<siga:ComboBD nombre = "cmbTipoSeguro" tipo="cmbTipoSeguro" clase="boxCombo" elementoSel="<%=arraySel%>" obligatorio="false"/>
										<% } %>	
									</td>									
								</tr>	
								<tr>
								<td class="labelText">
										<siga:Idioma key="censo.consultaDatosColegiales.literal.cuentaSJCS"/>&nbsp;
								</td>
								 <td  colspan="3">
										<% cuentaSJCS=datosCol.getCuentaContableSJCS(); 
										  if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ 
										    if (cuentaSJCS.equalsIgnoreCase("")){ %>
											<html:text property="cuentaContableSJCS" size="20" styleClass="boxConsulta" value="" readOnly="true"></html:text>
									     <% }else{ %>
												<html:text property="cuentaContableSJCS" size="20" styleClass="boxConsulta" value="<%=cuentaSJCS%>" readOnly="true"></html:text>											
									     <% } %>
									  <% } else { %>
											<html:text property="cuentaContableSJCS" size="20" styleClass="box" value="<%=cuentaSJCS%>" ></html:text>
										<%}%>	
										
									</td>			
								</tr>
							</table>								
						<% } %>																				
						</siga:ConjCampos>

					</td>
				</tr>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
			</table>		

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->
		
		<siga:ConjBotonesAccion botones="G,R" modo="<%=modo%>" clase="botonesSeguido"/>
		
		
			
		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
		
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.forms[0].reset();	
			}
		
			<!-- Asociada al boton Guardar -->
			function accionGuardar() 
			{		
	
				sub();
				
				//Comprobar numColegiado:
				if (document.forms[0].numColegiado.value==""){
					mensaje='<siga:Idioma key="censo.resultadosSolicitudesModificacion.literal.nColegiado"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					fin();
					return false;
				}
				//Chequeo de que numColegiado sea numerico:
				var number = parseInt(document.forms[0].numColegiado.value);
				if (number!=document.forms[0].numColegiado.value || isNaN(number)) {
					mensaje='<siga:Idioma key="censo.resultadosSolicitudesModificacion.literal.nColegiado"/> <siga:Idioma key="messages.campoNumerico.error"/>';
					alert(mensaje);
					fin();
					return false;
				}

				//Comprobar fechaPresentacion
				if (document.forms[0].fechaPresentacion.value==""){
					mensaje='<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaPresentacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					fin();
					return false;
				}
				//Comprobar fechaIncorporacion
				if (document.forms[0].fechaIncorporacion.value==""){
					mensaje='<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaIncorporacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					fin();
					return false;
				}				
               
				
				
					<% if (!bOcultarHistorico) { %>
							var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
					<% } else { %>
							var datos = new Array();
							datos[0] = 1;
							datos[1] = "";
					<% } %>
					if (datos[0] == 1) { // Boton Guardar
						document.forms[0].motivo.value = datos[1];
						document.forms[0].modo.value = "modificarDatos";
					  
					    document.forms[0].target = "submitArea";
					
						// Como estan disabled, salvo que los habilitemos de nuevo no van a pasar el valor adecuado en el formulario
						if (document.forms[0].situacionEjercicio.disabled){
							document.forms[0].situacionEjercicio.disabled =false;
						}
						if (document.forms[0].otrosColegios.disabled){
							document.forms[0].otrosColegios.disabled =false;
						}

						document.forms[0].submit();
					}else{
						fin();
					}
				
			}

			function refrescarLocal () {
				// document.location.reload();

             
				<%if (request.getAttribute("PESTANASITUACION")!=null && request.getAttribute("PESTANASITUACION").equals(ClsConstants.DB_TRUE)){%>
				
				// Si visualizamos los datos de colegiacion desde la pestanya de situacion
					document.forms[0].modo.value="editar";
					document.forms[0].action="<%=app%>/CEN_DatosColegiacion.do";
					document.forms[0].target="";
					
				 <%}else{%>
				 // Si visualizamos los datos de colegiacion desde la ficha colegial
				 				
				    document.forms[0].modo.value="abrir";
					document.forms[0].target="mainPestanas";
			 	<%}%>

					document.forms[0].idPersona.value="<%=idPersona%>";
					document.forms[0].idInstitucion.value="<%=idInstitucion%>";
					document.forms[0].submit();

				
			}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


					
		<!-- INICIO: LISTA RESULTADOS -->

			<% if ((modo.equalsIgnoreCase("editar"))||(modo.equalsIgnoreCase("edicion"))||(modo.equalsIgnoreCase("insertar"))||(modo.equalsIgnoreCase("modificar"))){ %>
			<siga:TablaCabecerasFijas 
				   nombre="tablaResultados"
				   borde="1"
				   clase="tableTitle"				   
				   nombreCol="censo.consultaDatosGenerales.literal.fechaEstado,censo.consultaDatosGenerales.literal.estado,censo.consultaDatosColegiales.literal.observaciones,"
				   tamanoCol="10,20,60,10"
		   alto="100%"

	

				   modal="P">
				   				   
				<%
	    		if (request.getAttribute("DATESTADO") == null || ((Vector)request.getAttribute("DATESTADO")).size() < 1 )
		    	{
				%>
					<br><br>
					<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
					<br><br>
				<%
		    	}	    
			    else
		    	{ %>
		    		<%		    		
		    		String iconosRegistro="E,B";
		    		if (((Vector)request.getAttribute("DATESTADO")).size() == 1){
			    		iconosRegistro="E";
		    		}
		    		Enumeration en = ((Vector)request.getAttribute("DATESTADO")).elements();					
					int recordNumber=1;
					String botones="";
					while (en.hasMoreElements())
					{
	            		Row row = (Row) en.nextElement(); 
	            		if (recordNumber>1){
	            			modo="consulta";
	            		}%>
	            		
						<siga:FilaConIconos
							  fila='<%=String.valueOf(recordNumber)%>'
							  botones='<%=iconosRegistro%>'
							  modo='<%=modo%>'							  
							  visibleConsulta='no'
							  clase="listaNonEdit">
						  
							<td>
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(CenDatosColegialesEstadoBean.C_IDPERSONA)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(CenDatosColegialesEstadoBean.C_IDINSTITUCION)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=nombre%>">
						  		<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=numero%>">
								<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO)))%>
							</td>
							<td>
								<%=row.getString(CenEstadoColegialBean.C_DESCRIPCION)%>							
							</td>  	
							<td>
								<%=UtilidadesString.mostrarDatoJSP(row.getString(CenDatosColegialesEstadoBean.C_OBSERVACIONES))%>
							</td>  								
						</siga:FilaConIconos>
					<% recordNumber++;
					} 
				} %>
			</siga:TablaCabecerasFijas>												

			<% } else {	%>
			<siga:TablaCabecerasFijas 
				   nombre="tablaResultados"
				   borde="1"
				   clase="tableTitle"				   
				   nombreCol="censo.consultaDatosGenerales.literal.fechaEstado,censo.consultaDatosGenerales.literal.estado,censo.consultaDatosColegiales.literal.observaciones"
				   tamanoCol="10,20,70"
		   alto="100%"


	
>
				   
					<%if (request.getAttribute("DATESTADO") == null || ((Vector)request.getAttribute("DATESTADO")).size() < 1 ){ %>
						<br><br>
						<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
						<br><br>
					<% } else { %>
				    	<%Enumeration en = ((Vector)request.getAttribute("DATESTADO")).elements();
						while (en.hasMoreElements())
						{
		            		Row row = (Row) en.nextElement(); 
							%>
		            		<tr>						  
								<td class="listaNonEdit" align="center">
									<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO)))%>
								</td>
								<td class="listaNonEdit" align="center">
									<%=UtilidadesString.mostrarDatoJSP(row.getString(CenEstadoColegialBean.C_DESCRIPCION))%>
								</td>  	
								<td class="listaNonEdit" align="center">
									<% if (row.getString(CenDatosColegialesEstadoBean.C_OBSERVACIONES).equalsIgnoreCase("")){%>
										&nbsp;
									<% } else { %>
										<%=UtilidadesString.mostrarDatoJSP(row.getString(CenDatosColegialesEstadoBean.C_OBSERVACIONES))%>
									<% } %>
								</td>  								
							</tr>	
						<% } 
					} %>
				</siga:TablaCabecerasFijas>												
			<% } %>
		
<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

		<!-- FIN: LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->

		<% modo=(String)request.getAttribute("ACCION"); %>		
		
		<siga:ConjBotonesAccion botones="<%=botonesAccion%>" modo="<%=modo%>" clase="botonesDetalle" />

		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
		
			<!-- Asociada al boton Restablecer -->
			function accionNuevo() 
			{		
				document.forms[0].modo.value='nuevo';
				var resultado = ventaModalGeneral(document.forms[0].name,"P");
				if (resultado=="MODIFICADO")
				{
					refrescarLocal();
				}
			}
			
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("MODIFICADO");
			}
			
		
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

		<!--/div-->
		
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
