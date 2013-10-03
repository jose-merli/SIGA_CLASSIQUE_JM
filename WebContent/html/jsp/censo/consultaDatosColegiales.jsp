<!DOCTYPE html>
<html>
<head>
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
		
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	boolean bOcultarHistorico = user.getOcultarHistorico();
	
	// Datos del cliente a visualizar
	Long idPersona=(Long)request.getAttribute("IDPERSONA"); // Obtengo el identificador de la persona
	
	String modo=(String)request.getAttribute("ACCION"); // Obtengo la accion anterior
	String nombre=(String)request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero=(String)request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona
	String estadoColegial=(String)request.getAttribute("ESTADOCOLEGIAL"); // Obtengo el estado colegial de la persona	
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
	String numMutualista="";
	if (request.getAttribute("DATSEGURO") != null){	
		Enumeration enumSel = ((Vector)request.getAttribute("DATSEGURO")).elements();	
		while (enumSel.hasMoreElements()) {
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
	} else {  
	  	if (!busquedaVolver.equals("volverNo")) { 
			botonesAccion="V,N";
	  	}
	}  

	// RGG 05-10-2005 Cambio para modificar el nColegiado si viene parametro
	String editarNColegiado = (String) request.getAttribute("editarNColegiado");
	
	boolean bEditarNColegiado = false;
	if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ 
	  	bEditarNColegiado=false;
	} else {
	  	if (new Integer(idInstUsuario.substring(0,1)).intValue()>=3 || idInstUsuario.equals("2000")){// Siempre dejaremos modificar el nº colegiado al CGAE y Consejos
	    	bEditarNColegiado=true;
	  	} else {
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

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>			

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DatosColegialesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="censo.fichaCliente.datosColegiales.cabecera" localizacion="censo.fichaCliente.datosColegiales.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos" >

	<!-- ******* INFORMACION GENERAL CLIENTE ****** -->

	<table class="tablaCentralCampos" align="center" cellspacing=0>
		<tr>				
			<td class="titulitosDatos">
				<siga:Idioma key="censo.consultaDatosColegiales.literal.titulo1"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>&nbsp;&nbsp;
<% 
				if(!numero.equalsIgnoreCase("")) {
					if (estadoColegial!=null && !estadoColegial.equals("")) {
%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
						 <%= UtilidadesString.mostrarDatoJSP(numeroColegiadoMostrar)  %> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>)
<%
					} else {
%> 
					 	(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>) 
<%
					}
					
				} else { 
%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
<%
				}
%>					
			</td>					
		</tr>
	</table>

	<!-- CAMPOS DEL REGISTRO -->
	<table class="tablaCentralCampos" align="center">		
		<html:form action="/CEN_DatosColegiales.do" method="POST">
			<html:hidden property = "modo"  styleId = "modo" value = ""/>
			<!--html:hidden property = "actionModal" value=""/-->				
			<html:hidden property="motivo" styleId="motivo"  value=""/> 				
			<html:hidden property="idPersona" styleId="idPersona" value="<%=idPersona.toString()%>"/> 				
			<html:hidden property="idInstitucion" styleId="idInstitucion"  value="<%=idInstitucion%>"/> 
			<html:hidden name="DatosColegialesForm" property="id" styleId="id"/> 					
			<html:hidden property="nombre"  styleId="nombre" value="<%=nombre%>"/> 	
			<html:hidden property="numero"  styleId="numero" value="<%=numero%>"/>
    		<input type="hidden" name="pestanaSituacion" id="pestanaSituacion" value="<%=request.getAttribute("PESTANASITUACION")%>">											

			<tr>				
				<td width="100%" align="center">

					<!-- SUBCONJUNTO DE DATOS -->
					<siga:ConjCampos leyenda="censo.busquedaClientesAvanzada.literal.titulo2">

<% 
					if (request.getAttribute("DATCOLEGIAL") == null) {
%>
						<div class="notFound">
							<br><br>
	   						<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
							<br><br>
						</div>
<% 
					} else { 
%>						
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
										<html:text name="DatosColegialesForm" property="numColegiado" maxlength="20" size="20" styleClass="box" value="<%=numeroColegiadoMostrar%>" readOnly="false" />
<% 
									} else { 
%>
										<!-- MAV 25/8/05 Resolucion de incidencia. Ahora NADIE puede editar el num colegiado -->
										<!-- RGG 05-10-05 Te equivocas, YO SI PUEDO, si vengo desde solicitud de incorporación. -->
										<!--html:text name="DatosColegialesForm" property="numColegiado" maxlength="20" size="20" styleClass="box" value="<%=numeroColegiadoMostrar%>"--><!--/html:text-->
										<html:text name="DatosColegialesForm" property="numColegiado" maxlength="20" size="20" styleClass="boxConsulta" value="<%=numeroColegiadoMostrar%>" readOnly="true" />
<% 
									} 
%>											
								</td>
								
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaPresentacion"/>&nbsp;(*)
								</td>
								<td>
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")){ 
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaPresentacion());
										if (fecha.equalsIgnoreCase("")){ 
%>
											<siga:Fecha  nombreCampo= "fechaPresentacion" disabled="true"/>									 	
<% 
										} else { 
%>	
											<siga:Fecha  nombreCampo= "fechaPresentacion" valorInicial="<%=fecha %>"  disabled="true"/>
<% 
										}
									} else {
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaPresentacion());
%>
		  								<siga:Fecha  nombreCampo= "fechaPresentacion" valorInicial="<%=fecha %>"/>
<% 
									} 
%>												
								</td>
								
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaIncorporacion"/>&nbsp;(*)
								</td>
								<td>									
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaIncorporacion());
										if (fecha.equalsIgnoreCase("")){ 
%>	
											<siga:Fecha  nombreCampo= "fechaIncorporacion" disabled="true"/>														 	
<% 
										} else { 
%>
											<siga:Fecha  nombreCampo= "fechaIncorporacion" disabled="true" valorInicial="<%=fecha %>"/>																 										 	
<% 
										}
									} else { 
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaIncorporacion());
%>
										<siga:Fecha  nombreCampo= "fechaIncorporacion" valorInicial="<%=fecha%>"/>				  																							
<% 
									} 
%>																							
								</td>									
							</tr>	
							
							<tr>												
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaJura"/>
								</td>
								<td>
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaJura());
										if (fecha.equalsIgnoreCase("")) { 
%>	
											<siga:Fecha  nombreCampo= "fechaJura" disabled="true"/>										 	
<% 
										} else { 
%>										 	
											<siga:Fecha  nombreCampo= "fechaJura" disabled="true" valorInicial="<%=fecha %>"/>		
<% 
										}
									} else {
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaJura());
%>
										<siga:Fecha  nombreCampo= "fechaJura"  valorInicial="<%=fecha %>"/>	
<% 
									} 
%>																																		
								</td>						
								
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaTitulacion"/>
								</td>
								<td>
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {	
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaTitulacion());
										if (fecha.equalsIgnoreCase("")) { 
%>
											<siga:Fecha  nombreCampo= "fechaTitulacion" disabled="true"/>										 	
<% 
										} else { 
%>										 								
											<siga:Fecha  nombreCampo= "fechaTitulacion" disabled="true" valorInicial="<%=fecha %>"/>			 	
<% 
										}
									} else {
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaTitulacion());
%>
										<siga:Fecha  nombreCampo= "fechaTitulacion"  valorInicial="<%=fecha %>"/>	
<% 
									} 
%>																																													
								</td>	
								
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.fechaDentologico"/>
								</td>
								<td>
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaDeontologia());
										if (fecha.equalsIgnoreCase("")){ %>									 	
											<siga:Fecha  nombreCampo= "fechaDeontologia" disabled="true"/>	
<% 
										} else { 
%>										 										 	
											<siga:Fecha  nombreCampo= "fechaDeontologia" disabled="true" valorInicial="<%=fecha %>"/>	
<% 
										}
									} else {
										fecha=GstDate.getFormatedDateShort("",datosCol.getFechaDeontologia());
%>
											<siga:Fecha  nombreCampo= "fechaDeontologia" valorInicial="<%=fecha %>"/>	
<% 
									} 
%>																																																								
								</td>											
							</tr>
							
							<tr>	
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.comunitario"/>
								</td>
								<td>
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) { 
										if (datosCol.getComunitario().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
											<input type="checkbox" name="comunitario" value="1" checked disabled>
<% 
										} else { 
%>
											<input type="checkbox" name="comunitario" value="1" disabled>
<% 
										}
										
									} else {
										if (datosCol.getComunitario().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>										
			  								<input type="checkbox" name="comunitario" value="1" checked>
<% 
										} else { 
%>
			  								<input type="checkbox" name="comunitario" value="1" disabled>
<% 
										}
									} 
%>											
								</td>
									
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.titulacion"/>
								</td>
								<td>	
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										if (datosCol.getIndTitulacion().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
			  								<input type="checkbox" name="indTitulacion" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" name="indTitulacion" value="1" disabled>
<% 
										}
									} else {
										if (datosCol.getIndTitulacion().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>										
			  								<input type="checkbox" name="indTitulacion" value="1" checked>
<% 
										} else { 
%>
			  								<input type="checkbox" name="indTitulacion" value="1">
<% 
										}
									} 
%>																						
								</td>
																						
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.otrosColegios"/>
								</td>
								<td>
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										if (datosCol.getOtrosColegios().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
			  								<input type="checkbox" id="otrosColegios" name="otrosColegios" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" id="otrosColegios" name="otrosColegios" value="1" disabled>
<% 
										} 
									} else {
										if (datosCol.getOtrosColegios().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>										
			  								<input type="checkbox" id="otrosColegios" name="otrosColegios" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" id="otrosColegios" name="otrosColegios" value="1" disabled>
<% 
										}
									} 
%>																																	
								</td>
							</tr>
								
							<tr>	
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.residente"/>
								</td>
								<td>	
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										if (datosCol.getSituacionResidente().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
			  								<input type="checkbox" name="situacionResidente" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" name="situacionResidente" value="1" disabled>
<% 
										}
										
									} else {
										if (datosCol.getSituacionResidente().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
			  								<input type="checkbox" name="situacionResidente" value="1" checked>
<% 
										} else { 
%>
			  								<input type="checkbox" name="situacionResidente" value="1">
<% 
										}
									} 
%>										
								</td>
																	
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.ejerciente"/>
								</td>
								<td>	
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										if (datosCol.getSituacionEjercicio().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
			  								<input type="checkbox" id="situacionEjercicio" name="situacionEjercicio" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" id="situacionEjercicio" name="situacionEjercicio" value="1" disabled>
<% 
										}
									} else {
										if (datosCol.getSituacionEjercicio().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>										
			  								<input type="checkbox" id="situacionEjercicio" name="situacionEjercicio" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" id="situacionEjercicio" name="situacionEjercicio" value="1" disabled>
<% 
										}
									} 
%>																					
								</td>
								
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.empresa"/>
								</td>
								<td>	
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										if (datosCol.getSituacionEmpresa().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
			  								<input type="checkbox" name="situacionEmpresa" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" name="situacionEmpresa" value="1" disabled>
<% 
										}
										
									} else {
										if (datosCol.getSituacionEmpresa().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>										
			  								<input type="checkbox" name="situacionEmpresa" value="1" checked>
<% 
										} else {
%>
			  								<input type="checkbox" name="situacionEmpresa" value="1">
<% 
										}
									} 
%>																					
								</td>
							</tr>
								
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.jubilacion"/>
								</td>
								<td>
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										if (datosCol.getJubilacionCuota().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>
			  								<input type="checkbox" name="jubilacionCuota" value="1" checked disabled>
<% 
										} else { 
%>
			  								<input type="checkbox" name="jubilacionCuota" value="1" disabled>
<% 
										}
										
									} else {
										if (datosCol.getJubilacionCuota().equalsIgnoreCase(ClsConstants.DB_TRUE)) { 
%>										
			  								<input type="checkbox" name="jubilacionCuota" value="1" checked>
<% 
										} else { 
%>
			  								<input type="checkbox" name="jubilacionCuota" value="1">
<% 
										}
									} 
%>																																												
								</td>
								
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.seguro"/>
								</td>
								<td colspan="3">
<% 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) {
										if (request.getAttribute("DATSEGURO") == null) {
%>
											<html:text property="tipoSeguro" size="" styleClass="boxConsulta" value="" readOnly="true" />
<% 
										} else { 
%>
											<html:text property="tipoSeguro"  size="50" styleClass="boxConsulta" value="<%=seguro%>" readOnly="true" />											
<% 
										}
									} else { 
%>
										<siga:Select queryId="getTiposSeguros" id="cmbTipoSeguro" selectedIds="<%=arraySel%>"/>
<% 
									} 
%>												
								</td>									
							</tr>
								
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.cuentaSJCS"/>
								</td>
							 	<td>
<% 
									cuentaSJCS=datosCol.getCuentaContableSJCS(); 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) { 
										if (cuentaSJCS.equalsIgnoreCase("")) { 
%>
											<html:text property="cuentaContableSJCS" size="20" styleClass="boxConsulta" value="" readOnly="true" />
<% 
										} else { 
%>
											<html:text property="cuentaContableSJCS" size="20" styleClass="boxConsulta" value="<%=cuentaSJCS%>" readOnly="true" />											
<% 
										}
									} else { 
%>
										<html:text property="cuentaContableSJCS" size="20" maxlength="20" styleClass="box" value="<%=cuentaSJCS%>" />
<%
									}
%>	
								</td>
								
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiales.literal.nMutualista"/>
								</td>
								<td colspan="3">
<% 
									numMutualista=datosCol.getNMutualista(); 
									if (modo.equalsIgnoreCase("consulta")||modo.equalsIgnoreCase("ver")) { 
										if (numMutualista.equalsIgnoreCase("")) { 
%>
											<html:text property="NMutualista" size="20" styleClass="boxConsulta" value="" readOnly="true" />
<% 
										} else { 
%>
											<html:text property="NMutualista" size="20" styleClass="boxConsulta" value="<%=numMutualista%>" readOnly="true" />											
<% 
										}
									} else { 
%>
										<html:text property="NMutualista" size="20" maxlength="20" styleClass="box" value="<%=numMutualista%>" />
<%
									}
%>	
								</td>	
							</tr>
						</table>								
<% 
					} 
%>																				
					</siga:ConjCampos>
				</td>
			</tr>
		<!-- RGG: cambio a formularios ligeros -->
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
		// Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();	
		}
		
		// Asociada al boton Guardar
		function accionGuardar() {		
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
				
<% 
			if (!bOcultarHistorico) { 
%>
				var datos = showModalDialog("<%=app%>/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
				window.top.focus();
<% 
			} else { 
%>
				var datos = new Array();
				datos[0] = 1;
				datos[1] = "";
<% 
			} 
%>				
			if (datos[0] == 1) { // Boton Guardar
				document.forms[0].motivo.value = datos[1];
				document.forms[0].modo.value = "modificarDatos";
				document.forms[0].target = "submitArea";
				// Como estan disabled, salvo que los habilitemos de nuevo no van a pasar el valor adecuado en el formulario
				jQuery("#situacionEjercicio").removeAttr("disabled");
				jQuery("#otrosColegios").removeAttr("disabled");
				document.forms[0].submit();
			} else {
				fin();
			}
			
		}

		function refrescarLocal () {
			// document.location.reload();
            
<%
			if (request.getAttribute("PESTANASITUACION")!=null && request.getAttribute("PESTANASITUACION").equals(ClsConstants.DB_TRUE)) {
%>
			
			// Si visualizamos los datos de colegiacion desde la pestanya de situacion
				document.forms[0].modo.value="editar";
				document.forms[0].action="<%=app%>/CEN_DatosColegiacion.do";
				document.forms[0].target="";
				
<%
			} else { %>
			 // Si visualizamos los datos de colegiacion desde la ficha colegial
			 				
			    document.forms[0].modo.value="abrir";
				document.forms[0].target="mainPestanas";
<%
			}
%>

			document.forms[0].idPersona.value="<%=idPersona%>";
			document.forms[0].idInstitucion.value="<%=idInstitucion%>";
			document.forms[0].submit();
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
					
	<!-- INICIO: LISTA RESULTADOS -->
<% 
	if (modo.equalsIgnoreCase("editar") || 
		modo.equalsIgnoreCase("edicion") || 
		modo.equalsIgnoreCase("insertar") || 
		modo.equalsIgnoreCase("modificar")) { 
%>
		<siga:Table 
			name="tablaResultados"
			border="1"
			columnNames="censo.consultaDatosGenerales.literal.fechaEstado,censo.consultaDatosGenerales.literal.estado,censo.consultaDatosColegiales.literal.observaciones,"
			columnSizes="10,20,60,10"
			modal="P">
				   				   
<%
    		if (request.getAttribute("DATESTADO") == null || ((Vector)request.getAttribute("DATESTADO")).size() < 1 ) {
%>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
<%
	    	} else { 		    		
		    	String iconosRegistro="E,B";
		    	if (((Vector)request.getAttribute("DATESTADO")).size() == 1) {
			    	iconosRegistro="E";
		    	}
		    	Enumeration en = ((Vector)request.getAttribute("DATESTADO")).elements();					
				int recordNumber=1;
				String botones="";
				while (en.hasMoreElements()) {
	            	Row row = (Row) en.nextElement(); 
	            	if (recordNumber>1){
	            		modo="consulta";
	            	}
%>
	            		
					<siga:FilaConIconos
						fila='<%=String.valueOf(recordNumber)%>'
						botones='<%=iconosRegistro%>'
						modo='<%=modo%>'							  
						visibleConsulta='no'
						clase="listaNonEdit">
						  
						<td>
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_1' name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(CenDatosColegialesEstadoBean.C_IDPERSONA)%>">
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_2' name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(CenDatosColegialesEstadoBean.C_IDINSTITUCION)%>">
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_3' name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO)%>">
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_4' name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=nombre%>">
					  		<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_5' name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=numero%>">
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO)))%>
						</td>
						<td>
							<%=row.getString(CenEstadoColegialBean.C_DESCRIPCION)%>							
						</td>  	
						<td>
							<%=UtilidadesString.mostrarDatoJSP(row.getString(CenDatosColegialesEstadoBean.C_OBSERVACIONES))%>
						</td>  								
					</siga:FilaConIconos>
<% 
					recordNumber++;
				} 
			} 
%>
		</siga:Table>												

<% 
	} else {	
%>
		<siga:Table 
			name="tablaResultados"
			border="1"
			columnNames="censo.consultaDatosGenerales.literal.fechaEstado,censo.consultaDatosGenerales.literal.estado,censo.consultaDatosColegiales.literal.observaciones"
			columnSizes="10,20,70">
				   
<%
			if (request.getAttribute("DATESTADO") == null || ((Vector)request.getAttribute("DATESTADO")).size() < 1 ) { 
%>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
<% 
			} else { 
				Enumeration en = ((Vector)request.getAttribute("DATESTADO")).elements();
				int fila = 1;
				while (en.hasMoreElements()) {
					Row row = (Row) en.nextElement();
%>
            		<tr class="<%=(fila%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">					  
						<td align="center">
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(CenDatosColegialesEstadoBean.C_FECHAESTADO)))%>
						</td>
						<td align="center">
							<%=UtilidadesString.mostrarDatoJSP(row.getString(CenEstadoColegialBean.C_DESCRIPCION))%>
						</td>  	
						<td>
<% 
							if (row.getString(CenDatosColegialesEstadoBean.C_OBSERVACIONES).equalsIgnoreCase("")) {
%>
								&nbsp;
<% 
							} else { 
%>
								<%=UtilidadesString.mostrarDatoJSP(row.getString(CenDatosColegialesEstadoBean.C_OBSERVACIONES))%>
<% 
							} 
%>
						</td>  								
					</tr>	
<% 
					fila++; 
				} 
			} 
%>
		</siga:Table>												
<% 
	} 
%>
		
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
		// Asociada al boton Nuevo
		function accionNuevo() {		
			document.forms[0].modo.value='nuevo';
			var resultado = ventaModalGeneral(document.forms[0].name,"P");
			if (resultado=="MODIFICADO") {
				refrescarLocal();
			}
		}
		
		function accionCerrar() {		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("MODIFICADO");
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>