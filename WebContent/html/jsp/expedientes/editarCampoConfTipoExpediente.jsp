<!DOCTYPE html>
<html>
<head>
<!-- editarCampoConfTipoExpediente.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.expedientes.form.CamposConfigurablesForm"%>


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	CamposConfigurablesForm form = (CamposConfigurablesForm) request.getAttribute("camposConfigurablesForm");
	boolean bLectura = false;
	String estiloCaja = "box";
	
%>	


	
	
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<!-- Validaciones en Cliente -->
		<html:javascript formName="camposConfigurablesForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">

			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				camposConfigurablesForm.reset();
			}
		
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				if (validateCamposConfigurablesForm(document.camposConfigurablesForm)) {
					document.camposConfigurablesForm.submit();
				} else {
					fin();
				}
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
			
			function comboTipoChange(){
				if ($("#comboTipo").val()==3){
					$("#maxLong").val("10");
					$("#maxLong").attr("readonly", "readonly");
				} else if ($("#maxLong").is("[readonly]")) {					
					$("#maxLong").val("");
					$("#maxLong").removeAttr("readonly");
				}
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		
	</head>

	<body>
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="expedientes.tipoExpediente.ecicion.camposConfigurables"/>
			</td>
		</tr>
	</table>	
		<div id="camposRegistro" class="posicionModalPeque" align="center">
				<table class="tablaCentralCamposPeque" align="center" border="0">
					<html:form action="/EXP_TiposExpedientes_CamposConf.do" method="POST" target="submitArea">
						<html:hidden property = "hiddenFrame" value = "1"/>
						<html:hidden name="camposConfigurablesForm" property = "idInstitucion" />
						<html:hidden name="camposConfigurablesForm" property = "accion" />
						<html:hidden name="camposConfigurablesForm" property = "idTipoExpediente" />
						<html:hidden name="camposConfigurablesForm" property = "idCampo" />
						<html:hidden name="camposConfigurablesForm" property = "modo" value="<%=form.getAccion()%>" />
						<html:hidden name="camposConfigurablesForm" property = "idPestanaConf" />
						<html:hidden name="camposConfigurablesForm" property = "idCampoConf" />

					<tr>		
						<td>
							<siga:ConjCampos>
								<table class="tablaCampos" align="center">
									<tr>
										<td class="labelText">
											<siga:Idioma key="expedientes.tipoExpediente.ecicion.literal.seleccionado"/>
										</td>
										<td>
											<html:checkbox name="camposConfigurablesForm" property="seleccionado" value="1" disabled="<%=bLectura%>"/>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="expedientes.tipoExpediente.ecicion.literal.nombre"/> (*)
										</td>
										<td>
											<html:text  name="camposConfigurablesForm" property="nombre" styleClass="<%=estiloCaja%>" maxlength="30" readonly="<%=bLectura%>" />
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.tipo"/> (*)
										</td>
										<td>
											<html:select name="camposConfigurablesForm" styleClass="boxCombo" property="tipo" onchange="comboTipoChange()" styleId="comboTipo">
												<bean:define id="tiposcampo" name="camposConfigurablesForm"
														property="tiposcampo" type="java.util.Collection" />
												<html:optionsCollection name="tiposcampo" value="idTipoCampo" label="recursoDescripcion" />
											</html:select>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="expedientes.auditoria.literal.maxlong"/> (*)
										</td>
										<td>
											<html:text  name="camposConfigurablesForm" property="maxLong" styleId="maxLong" styleClass="<%=estiloCaja%>" maxlength="30" readonly="<%=bLectura%>" />
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="expedientes.tipoExpediente.ecicion.literal.orden"/> (*)
										</td>
										<td>
											<html:text  name="camposConfigurablesForm" property="orden" size="2" styleClass="<%=estiloCaja%>" maxlength="2" readonly="<%=bLectura%>" />
										</td>
									</tr>
										<%if(form.getIdCampo().equals("14")){ %>
										<tr>
										<td class="labelText">
											<siga:Idioma key="expedientes.tipoExpediente.ecicion.literal.general"/>
										</td>
										<td>
											<html:checkbox name="camposConfigurablesForm" property="general" value="1" disabled="<%=bLectura%>"/>
										</td>	
									</tr>									
										<%}%>
									
									
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
					</html:form>
				</table>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="R,Y,C" modal="P"/>
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>