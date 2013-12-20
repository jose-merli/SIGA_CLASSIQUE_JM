<!DOCTYPE html>
<html>
<head>
<!-- seguimiento_Edit.jsp -->
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
<%@ page import="com.siga.expedientes.form.ExpSeguimientoForm"%>
<%@ page import="com.siga.expedientes.form.TiposAnotacionesForm"%>
<%@ page import="java.util.ArrayList"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	String accion = (String)request.getAttribute("accion");
	boolean bLectura=(accion.equals("consulta"));
	boolean bEdicion=(accion.equals("edicion"));
	boolean bNuevo=(accion.equals("nuevo"));
	String boxStyle = (bLectura)?"boxConsulta":"box";
	String boxStyleFecha = (bLectura || bEdicion)?"boxConsulta":"box";
	 
	String botones = "C";
	if (bNuevo){	
		botones = "Y,C";
	}else if (bEdicion){
		botones = "Y,R,C";
	}
	
	String idInstitucionTipoexpediente=(String)request.getAttribute("idInstitucion_TipoExpediente");
	String idTipoExpediente=(String)request.getAttribute("idTipoExpediente");
	String idFase = (String)request.getAttribute("idFase");
	String idEstado = (String)request.getAttribute("idEstado");
	ArrayList aIdTipoAnotacion=new ArrayList();
	
	
	String dato[] = null;
	String comboAnotacion = "";
	
	String autom = (String)request.getAttribute("automatico");
	boolean isAnotacionAutomatica = false;
	isAnotacionAutomatica = (autom!=null && autom.equals("S"));
	if (idFase!=null && !idFase.equals("") && idEstado!=null && !idEstado.equals("")) {
		dato = new String[]{idInstitucionTipoexpediente,idTipoExpediente,idFase,idEstado, idFase};
		comboAnotacion="cmbTipoAnotacionSinNull";
	} else {
		dato = new String[]{idInstitucionTipoexpediente,idTipoExpediente};
		comboAnotacion="cmbTipoAnotacionTodosNull";
		//isAnotacionAutomatica = true;
	}
	
	aIdTipoAnotacion.add((String)request.getAttribute("idTipoAnotacion"));
	
	if(dato==null)	
		dato = new String[]{idInstitucionTipoexpediente,idTipoExpediente,idInstitucionTipoexpediente,idTipoExpediente,idFase,idInstitucionTipoexpediente,idTipoExpediente,idFase,idEstado};
	request.removeAttribute("accion");
	
	ExpSeguimientoForm form = (ExpSeguimientoForm)request.getAttribute("ExpSeguimientoForm");
	Boolean editable = form.getEditable();
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpSeguimientoForm" staticJavascript="false" /> 
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>

<body>
	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<div id="camposRegistro" class="posicionModalMedia" align="center">
		<table class="tablaTitulo" cellspacing="0" height="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<bean:write name="ExpSeguimientoForm" property = "tituloVentana" />
				</td>
			</tr>
		</table>

		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCamposMedia"  align="center">
			<html:form action="/EXP_Auditoria_Seguimiento.do" method="POST" target="submitArea">
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "modo" value = ""/>
				<tr>				
					<td>	
						<siga:ConjCampos leyenda="expedientes.auditoria.literal.anotacion">
							<table class="tablaCampos" align="left">
								<!-- FILA -->
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.usuario"/>
									</td>				
									<td colspan="3">
										<html:text name="ExpSeguimientoForm" property="usuario" size="60" styleClass="boxConsulta" readonly="true" />
									</td>				
								</tr>
		
								<!-- FILA -->
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.fase"/>
									</td>				
									<td>
										<%=form.getFase()%>
										<html:hidden name="ExpSeguimientoForm" property="fase"/>
										<html:hidden property = "idFase"/>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.estado"/>
									</td>				
									<td>										
										<%=form.getEstado()%>
										<html:hidden name="ExpSeguimientoForm" property="estado"/>
										<html:hidden property = "idEstado"/>
									</td>	
								</tr>	
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.fechaInicioEstado"/>
									</td>				
									<td>										
										<%=form.getFechaInicioEstado()%>
										<html:hidden name="ExpSeguimientoForm" property="fechaInicioEstado"/>
									</td>
						
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.fechaFinEstado"/>
									</td>
									<td>										
										<% if (!bLectura && !bEdicion) { %>
											<siga:Fecha nombreCampo="fechaFinEstado" posicionY="100" posicionX="200"></siga:Fecha>
										<% } else { %>											
											<%=form.getFechaFinEstado()%>
											<html:hidden name="ExpSeguimientoForm" property="fechaFinEstado"/>
										<% } %>
									</td>
								</tr>	
								
								<!-- FILA -->
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.tipoanotacion"/> (*)
									</td>				
									<td>
										<% if (!bLectura && !isAnotacionAutomatica) { %>		
											<siga:ComboBD nombre = "idTipoAnotacion" tipo="<%=comboAnotacion%>" clase="<%=boxStyle%>" obligatorio="true" parametro="<%=dato%>" elementoSel="<%=aIdTipoAnotacion%>" accion="actualizarDescripcion(this);"/>						
										<% } else { %>
											<siga:ComboBD nombre = "idTipoAnotacion" tipo="<%=comboAnotacion%>" clase="boxConsulta" obligatorio="true" readonly="true" parametro="<%=dato%>" elementoSel="<%=aIdTipoAnotacion%>"/>
										<% } %>
									</td>
						
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.fechaAnotacion"/> (*)
									</td>
									<td>										
										<% if (!bLectura && !bEdicion) { %>											
											<siga:Fecha nombreCampo="fecha" posicionY="100" posicionX="200"></siga:Fecha>
										<% } else { %>											
											<%=form.getFecha()%>
											<html:hidden name="ExpSeguimientoForm" property="fecha"/>
										<% } %>
									</td>
								</tr>	
								
								<!-- FILA -->
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.descripcion"/>
									</td>				
									<td colspan="3" width="250">
										<% if (!bLectura) { %>
											<html:textarea name="ExpSeguimientoForm" property="descripcion" styleClass="box"
												onkeydown="cuenta(this,1024)" onchange="cuenta(this,1024)"
												style="overflow-y:auto; overflow-x:hidden; width:500px; height:50px; resize:none;"></html:textarea>
										<% } else { %>
											<html:textarea name="ExpSeguimientoForm" property="descripcion" styleClass="boxConsulta"  readonly="true" 
												style="overflow-y:auto; overflow-x:hidden; width:500px; height:50px; resize:none;"></html:textarea>												 
										<% } %>
									</td>				
								</tr>	

								<!-- FILA -->
								<tr>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.regentrada"/>
									</td>				
									<td>
										<% if (!isAnotacionAutomatica) { %>
											<html:text name="ExpSeguimientoForm" property="regentrada" size="15" maxlength="30" styleClass="<%=boxStyle%>" readonly="<%=bLectura%>" />
										<% } else { %>
											<%=form.getRegentrada()%>
											<html:hidden name="ExpSeguimientoForm" property="regentrada"/>
										<% } %>
									</td>
									<td class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.regsalida"/>
									</td>				
									<td>
										<% if (!isAnotacionAutomatica) { %>
											<html:text name="ExpSeguimientoForm" property="regsalida" size="15" maxlength="30" styleClass="<%=boxStyle%>" readonly="<%=bLectura%>" />
										<% } else { %>
											<%=form.getRegsalida()%>
											<html:hidden name="ExpSeguimientoForm" property="regsalida"/>
										<% } %>
									</td>
								</tr>				
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</html:form>
		</table>

     	<html:form action="/EXP_TiposExpedientes_TiposAnotaciones.do" method="POST" target="submitArea">
	    	<html:hidden property = "modo" value = "obtenerMensaje"/>
			<html:hidden property = "idTipoExpediente" value = ""/>
			<html:hidden property = "idInstitucion" value = ""/>
			<html:hidden property = "idTipoAnotacion" value = ""/>
    	</html:form> 
		<!-- FIN: CAMPOS -->

		<!-- INICIO: BOTONES REGISTRO -->	
		<siga:ConjBotonesAccion botones="<%=botones%>" modal="M" />
		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			// Asociada al boton GuardarCerrar
			function accionGuardarCerrar() {  
				sub();		
				 if (validateExpSeguimientoForm(document.ExpSeguimientoForm)){
				 
					<%if (accion.equals("nuevo")){%>
						document.forms[0].modo.value="insertar";
					<%}else{%>
						document.forms[0].modo.value="modificar";
										
					<%}%>
					document.forms[0].submit();	
																
				} else {
					fin();
					return false;
				}
			}
		
			function actualizarDescripcion(elementoSel) {
				if (elementoSel.value!=null && elementoSel.value!=""){
					document.forms[1].idTipoAnotacion.value=elementoSel.value;
					document.forms[1].idTipoExpediente.value="<%=idTipoExpediente%>";
					document.forms[1].idInstitucion.value="<%=idInstitucionTipoexpediente%>";
					document.forms[1].submit();
				} else {
				  	document.forms[0].descripcion.value="";
				}	 			
			}

			// Asociada al boton Cerrar
			function accionCerrar() {		
				window.top.close();
			}
	
			// Asociada al boton Restablecer
			function accionRestablecer() {		
				document.forms[0].reset();
			}
	
			function refrescarLocal() {	
				parent.location.reload();
			}
		
			function traspasoDatos(resultado) {	
				document.forms[0].descripcion.value=resultado[0];
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>	
	<!-- FIN: SUBMIT AREA -->
</body>
</html>