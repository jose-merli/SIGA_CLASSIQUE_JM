<!DOCTYPE html>
<html>
<head>
<!-- estados_Edit.jsp -->

<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL GRANDE -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
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
<%@ page import="com.atos.utils.Row"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.beans.ExpEstadosBean"%>
<%@ page import="com.siga.beans.ExpFasesBean"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector vFases = (Vector)request.getAttribute("vFases");
	Vector vEstados = (Vector)request.getAttribute("vEstados");
	Vector vEstadosSiguientes = (Vector)request.getAttribute("vEstadosSiguientes");
	Vector vPlazos = (Vector)request.getAttribute("vPlazos");
	String editable = (String)request.getAttribute("editable");
	boolean bEditable = editable.equals("1")? true : false;	
	String modo=request.getParameter("modo");	
	
	String[] parametros;												
	ExpEstadosBean estadosBean = new ExpEstadosBean();	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="EstadosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>	
</head>

<body>

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.auditoria.literal.datosgenerales"/>
			</td>
		</tr>
	</table>	
	
	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- dentro de esta capa se tienen que situar los diferentes componentes 
		 que se van a mostrar, para que quepen dentro de la ventana.
		 Los elementos que copieis dentro, que tengan el estilo 
		 "tablaTitulo" se deben modificar por "tablaCentralGrande" 
	-->
	<div id="camposRegistro" class="posicionModalGrande" align="center">

		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCamposGrande"  align="center">
			<html:form action="/EXP_TiposExpedientes_Estados.do" method="POST" target="submitArea">
				<html:hidden property = "hiddenFrame" value = "1"/>
				<input type="hidden" id="datosPlazos" name="datosPlazos" value=""/>

<% 
				if (modo.equalsIgnoreCase("Nuevo")) { 
%>
					<html:hidden property = "modo" value = "Insertar"/>

<% 
				} else {
%>
					<html:hidden property = "modo" value = "Modificar"/>

<% 
				}
				
				String nombreEstado = "";
				if (vEstados!=null && vEstados.size()>0) {
					estadosBean = (ExpEstadosBean) vEstados.elementAt(0);
					nombreEstado = estadosBean.getNombre();
				}
				
				
				ExpFasesBean faseBean = new ExpFasesBean();
				String nombreFase = "";
				if (vFases!=null && vFases.size()>0) {
					faseBean = (ExpFasesBean)vFases.elementAt(0);
					nombreFase = faseBean.getNombre();
				}
				
%>

				<!-- ******************* Datos Generales ******************* -->

				<tr>				
					<td>
						<siga:ConjCampos leyenda="expedientes.auditoria.literal.datosgenerales">
							<table align="left" border="0">
								<!-- FILA -->
								<tr>				
									<td class="labelText" >
										<siga:Idioma key="expedientes.auditoria.literal.fase"/>&nbsp(*)
									</td>				
									<td width="550px">
<% 
										parametros = new String[2];
					 					parametros[0] = estadosBean.getIdInstitucion().toString();
					 					parametros[1] = estadosBean.getIdTipoExpediente().toString();
											 	
										if (modo.equalsIgnoreCase("Nuevo")) { 	
%>				 	
											<siga:ComboBD nombre = "idInst_idExp_idFase" tipo="cmbFases" clase="boxCombo" parametro="<%=parametros%>" ancho="450" obligatorio="true"/>
<% 
										} else { 
										    ArrayList faseSel = new ArrayList ();
											String faseSeleccionada = parametros[0] + "," + parametros[1] + "," + estadosBean.getIdFase().toString();
											faseSel.add(faseSeleccionada);
%>
											<html:text name="EstadosForm" property="no_utilizado" style="width:450px;" maxlength="30" styleClass="boxConsulta" readonly="true" value="<%=nombreFase%>" />
											<input type="hidden" name="idInst_idExp_idFase" value="<%=faseSeleccionada%>"/>
<% 
										} 
%>			
										<input type="hidden" name="idTipoExpediente" value="<%=estadosBean.getIdTipoExpediente()%>"/>
										<input type="hidden" name="idInstitución" value="<%=estadosBean.getIdInstitucion()%>"/>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="expedientes.tiposexpedientes.literal.automatico"/>
									</td>
									<td>
										<html:checkbox name="EstadosForm" property="automatico" styleId="automatico" disabled="<%=!bEditable%>"/>
									</td>									
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.estado"/>&nbsp(*)
									</td>				
									<td>
<% 
										if (bEditable) { 
%>
											<html:text name="EstadosForm" property="estado" style="width:450px;" maxlength="60" styleClass="box" value="<%=nombreEstado%>" />
<% 
										} else { 
%>
											<html:text name="EstadosForm" property="estado" style="width:450px;" maxlength="60" styleClass="boxConsulta" readonly="true" value="<%=nombreEstado%>" />
<% 
										} 
%>
									</td>									
									
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.ejecucionsancion"/>
									</td>
									<td>
										<html:checkbox name="EstadosForm" property="ejecucionSancion" styleId="ejecucionSancion" disabled="<%=!bEditable%>"/>
									</td>
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.tiposexpedientes.literal.siguienteestado"/>
									</td>	
									<td>
<% 
										parametros = new String[4];
			 							parametros[0] = estadosBean.getIdInstitucion().toString();
			 							parametros[1] = estadosBean.getIdTipoExpediente().toString();
			 							
			 							String siguienteestado="",siguientefase="";
			 							if (estadosBean.getIdEstadoSiguiente()!=null) { 
			 								siguienteestado = estadosBean.getIdEstadoSiguiente().toString();
			 							}
			 							if (estadosBean.getIdFaseSiguiente()!=null) {
			 								siguientefase = estadosBean.getIdFaseSiguiente().toString();
			 							}
			 	
										ArrayList estadoSel = null;
										if (!modo.equalsIgnoreCase("Nuevo")){
											parametros[2] = estadosBean.getIdEstado().toString();
											parametros[3] = estadosBean.getIdFase().toString();
				 							estadoSel = new ArrayList ();
				 							estadoSel.add(parametros[0] + "," + parametros[1] + "," + siguientefase + "," + siguienteestado); 
				 	
				 							String readOnly="true", clase="boxConsulta";
				 							if (bEditable) {
				 								readOnly = "false";
				 								clase = "boxCombo";
				 							} 
%>
				 							<siga:ComboBD nombre="idInst_idExp_idFase_idEstadoSig" tipo="cmbEstadosSiguientes" elementoSel="<%=estadoSel%>" clase="<%=clase%>" ancho="450" parametro="<%=parametros%>" obligatorio="false" readonly="<%=readOnly%>" />
<% 
										} else { 
%>
											<siga:ComboBD nombre="idInst_idExp_idFase_idEstadoSig" tipo="cmbEstadosSiguientesTodos" clase="boxCombo" parametro="<%=parametros%>" ancho="450" obligatorio="false"/>
<% 
										} 
%>				
									</td>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.estadoFinal"/>
									</td>
									<td>
										<html:checkbox name="EstadosForm" property="estadoFinal" styleId="estadoFinal" disabled="<%=!bEditable%>"/>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
	

				<!-- ******************* Plazos Clasificación  ******************* -->
	
				<tr>
					<td>
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.plazosclasificacion">
							<table width="60%" align="center" >
								<tr>
									<td> 
										<siga:Table 
					   						name="tablaCabeceras"
					   						border="1"
					   						columnNames="expedientes.auditoria.literal.clasificacion,expedientes.auditoria.literal.plazo"
					   						columnSizes="50,50"
					   						fixedHeight="100">	
											
<%
											if (vPlazos==null || vPlazos.size()==0) {
%>		
										 		<tr class="notFound">
										   			<td class="titulitos">
										   				<siga:Idioma key="messages.noRecordFound"/>
										   			</td>
												</tr>
<%
											} else {
		 										for (int i=0; i<vPlazos.size(); i++) {
		   											Row fila = (Row) vPlazos.elementAt(i);
			  										String clasificacion = fila.getString("CLASIFICACION");
			  										String idclasificacion=fila.getString("IDCLASIFICACION");
			  										String plazo = fila.getString("PLAZO").trim();				  		
			  										if (plazo.equalsIgnoreCase("NULO")) {
			  											plazo="0";
			  										}				 
%>
	
													<tr class="<%=((i+1)%2==0?"filaTablaPar":"filaTablaImpar")%>">
														 
														<td><%=clasificacion%></td>
														<td align="center">
<% 			
															if (bEditable) { 
%>																
																<input type="hidden" id="idclasificacion_<%=i%>" value="<%=idclasificacion%>">
																<input type='text' name='idplazo_<%=i%>'  name="idplazo" size="6" maxlength="6" class="boxNumber" value="<%=plazo%>" />						
<% 
															} else { 
%>
																<html:text name="plazosForm" property="no_utilizado" styleClass="boxConsultaNumber" readonly="true" value="<%=plazo%>" />				
<% 
															} 
%>
														</td>
													</tr>
<%
												}
											}
%>				
										</siga:Table>
									</td>	
								</tr>
	
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.tiposexpedientes.literal.notaclasificaciones"/>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>

				<!-- ******************* Entrada Estado ******************* -->
				<tr>
					<td>
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.entradaestado">
							<table width="100%">
								<tr>
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.sancionado"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="sancionado"	size="1" value="<%=estadosBean.getPreSancionado()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPreSancionado().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPreSancionado().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>
									</td>
				
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.visible"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="preVisible"	size="1" value="<%=estadosBean.getPreVisible()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPreVisible().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPreVisible().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>						
									</td>
				
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.visibleenficha"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 					
										if (bEditable) { 
%>
											<html:select property="preVisibleFicha"	size="1" value="<%=estadosBean.getPreVisibleFicha()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPreVisibleFicha().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPreVisibleFicha().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>		
					</td>
				</tr>
	
				<!-- ******************* Salida Estado ******************* -->
				<tr>
					<td>	
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.salidaestado">
							<table width="100%">
								<tr>
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.actuacionesprescritas"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="actPrescritas" size="1" value="<%=estadosBean.getPostActPrescritas()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPostActPrescritas().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPostActPrescritas().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>
									</td>
				
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.sancionprescrita"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="sancionPrescrita" size="1" value="<%=estadosBean.getPostSancionPrescrita()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPostSancionPrescrita().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPostSancionPrescrita().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>	
									</td>
				
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.sancionfinalizada"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="sancionFinalizada"	size="1" value="<%=estadosBean.getPostSancionFinalizada()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPostSancionFinalizada().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPostSancionFinalizada().equals("N")) {  
%>								
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>											
									</td>
								</tr>
								
								<tr>
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.anotcanceladas"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="anotCanceladas" size="1" value="<%=estadosBean.getPostAnotCanceladas()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPostAnotCanceladas().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPostAnotCanceladas().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>
									</td>
				
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.visible"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="postVisible" size="1" value="<%=estadosBean.getPostVisible()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>	
<% 				
										} else if (estadosBean.getPostVisible().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPostVisible().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>
									</td>
				
									<td class="labelText" width="20%">
										<siga:Idioma key="expedientes.auditoria.literal.visibleenficha"/>
									</td>
									<td class="labelTextValue" width="8%">
<% 				
										if (bEditable) { 
%>
											<html:select property="postVisibleFicha" size="1" value="<%=estadosBean.getPostVisibleFicha()%>" styleClass="boxCombo">
												<html:option value="" key=""/>
												<html:option value="S" key="general.yes"/>
												<html:option value="N" key="general.no"/>
											</html:select>				
<% 				
										} else if (estadosBean.getPostVisibleFicha().equals("S")) {  
%>
											<siga:Idioma  key="general.yes"/>
<%              
										} else if (estadosBean.getPostVisibleFicha().equals("N")) {  
%>
											<siga:Idioma  key="general.no"/>
<%				
										} else {
%>
											-
<%				
										}
%>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>		
					</td>
				</tr>
	
				<tr>
					<td>	
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.alertas">
							<table width="100%">
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.activarAlarma"/>
									</td>
									<td>
										<html:checkbox name="EstadosForm" property="activarAlertas" styleId="activarAlertas" disabled="<%=!bEditable%>" onClick="validarActivarAlarmas();"/>
									</td>
								</tr>
								
								<tr>
									<td class="labelText" width="15%">
										<siga:Idioma key="expedientes.auditoria.literal.diasAntelacion"/>
									</td>	
									<td>
<% 

										String sDiasAntelacion = "";
										if (estadosBean.getDiasAntelacion()!=null) {
											sDiasAntelacion = estadosBean.getDiasAntelacion().toString();
										}
											
					   					if (bEditable) { 
%>
											<html:text name="EstadosForm" property="diasAntelacion" styleId="diasAntelacion" size="3" maxlength="3" styleClass="box" value="<%=sDiasAntelacion%>"/>
<% 
										} else { 
%>
											<html:text name="EstadosForm" property="diasAntelacion" styleId="diasAntelacion" size="3" maxlength="3" styleClass="boxConsulta" disabled="true" value="<%=sDiasAntelacion%>" />
<% 
										} 
%>
									</td>
							
									<td class="labelText" >
										<siga:Idioma key="expedientes.tiposexpedientes.literal.mensaje"/>
									</td>
									<td>
<% 
										if (bEditable) { 
%>
											<html:text name="EstadosForm" property="mensaje" styleId="mensaje" size="100" maxlength="100" styleClass="box" value="<%=estadosBean.getMensaje()%>" />
<% 
										} else { 
%>
											<html:text name="EstadosForm" property="mensaje" styleId="mensaje" size="100" maxlength="100" styleClass="boxConsulta" readonly="true" value="<%=estadosBean.getMensaje()%>" />
<% 
										} 
%>
									</td>
								</tr>		
							</table>
						</siga:ConjCampos>		
					</td>
				</tr>		
			</html:form>	
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
<% 
		String botones = (bEditable ? "Y,R,C" : "C");
%>
		<siga:ConjBotonesAccion botones="<%=botones%>" modal="G" />
		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			// Asociada al boton GuardarCerrar
			function accionGuardarCerrar() {
				sub();						
				if (validateEstadosForm(document.EstadosForm)) {
					var datos;
					var sizePlazos=0;
					<% if (vPlazos!=null) { %>
						sizePlazos=<%=vPlazos.size()%>;
					<% } %>					
					  
					var datosPlazos = "";
					for (i=0; i<sizePlazos; i++) {
						var idPlazo = document.getElementById("idplazo_"+i).value; 
				  	  	var idClasificacion = document.getElementById("idclasificacion_"+i).value;
				  	  	if (i==0)
				  	  		datosPlazos += "idPlazo==" + idPlazo + ",idClasificacion==" + idClasificacion;
				  	  	else
				  	  		datosPlazos += "##idPlazo==" + idPlazo + ",idClasificacion==" + idClasificacion;
					}
					
					document.EstadosForm.datosPlazos.value = datosPlazos;
					document.EstadosForm.submit();			
					window.top.returnValue="MODIFICADO";
					
				} else {
					fin();
					return false;
				}				
			}

			// Asociada al boton Cerrar
			function accionCerrar() {		
				window.top.close();
			}
			
			function validarActivarAlarmas() {
				var mensaje = jQuery("#mensaje");
				var diasAntelacion = jQuery("#diasAntelacion");
				var activarAlertas = jQuery("#activarAlertas");
							
				if (<%=bEditable%>) {
					if (activarAlertas[0].checked) {
						diasAntelacion.removeAttr("readOnly").removeAttr("disabled");
						diasAntelacion.removeClass("boxConsulta").addClass("box");
						mensaje.removeAttr("readOnly").removeAttr("disabled");
						mensaje.removeClass("boxConsulta").addClass("box");
					} else {
						diasAntelacion.attr("readOnly", "readOnly").attr("disabled", "disabled");
						diasAntelacion.removeClass("box").addClass("boxConsulta");
						diasAntelacion.val("");
						mensaje.attr("readOnly", "readOnly").attr("disabled", "disabled");
						mensaje.removeClass("box").addClass("boxConsulta");
						mensaje.val("");
					}
					
				} else {
					diasAntelacion.attr("readOnly", "readOnly").attr("disabled", "disabled");
					diasAntelacion.removeClass("box").addClass("boxConsulta");
					mensaje.attr("readOnly", "readOnly").attr("disabled", "disabled");
					mensaje.removeClass("box").addClass("boxConsulta");
					
					if (!activarAlertas[0].checked) {
						diasAntelacion.val("");
						mensaje.val("");
					}
				}
			}			
			
			// JPT: Funcion que controla el estado de los checks de la pantalla
			function controlChecks() {
				if (<%=estadosBean.getAutomatico().equals("S")%>) {
 					jQuery("#automatico").prop('checked', true);
 				}
 				
 				if (<%=estadosBean.getEjecucionSancion().equals("S")%>) {
 					jQuery("#ejecucionSancion").prop('checked', true);
 				}
 				
 				if (<%=estadosBean.getEstadoFinal().equals("S")%>) {
 					jQuery("#estadoFinal").prop('checked', true);
 				}
 				
 				if (<%=estadosBean.getActivarAlertas().equals("S")%>) {
 					jQuery("#activarAlertas").prop('checked', true);
 				}
 				
 				validarActivarAlarmas();
			}
	
			// Asociada al boton Restablecer
			function accionRestablecer() {				
				document.forms[0].reset();
				
				controlChecks();			
			}		
 			
 			jQuery(document).ready(function(){
<%
				if (!modo.equalsIgnoreCase("Nuevo")) {
%>					
					controlChecks();
<%
				} else {
%>
 				
 					validarActivarAlarmas();
<%
				}
%> 					
 			});
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>