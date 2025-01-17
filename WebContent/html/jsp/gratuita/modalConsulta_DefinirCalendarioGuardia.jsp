<!DOCTYPE html>
<html>
<head>
<!-- modalConsulta_DefinirCalendarioGuardia.jsp-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	String profile[] = usr.getProfile();
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma
				key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.tituloGuardia" />
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center">
	
		<!-- Comienzo del formulario con los campos -->
		<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post">
			<html:hidden property="modo" value="" />
	
			<!-- INICIO: CAMPOS DEL REGISTRO -->
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.infoGuardia">
						<table class="tablaCampos" border="0">
							<tr>
								<td class="labelText" width="170px"><siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.periodoGuardia" /></td>
								<td><html:text name="DefinirCalendarioGuardiaForm" property="periodos" styleClass="boxConsulta" readonly="true"/></td>
							</tr>
	
							<tr>
								<td class="labelText"><siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.letrado" /></td>	
								<td> <html:text name="DefinirCalendarioGuardiaForm" property="datosPersSolicitante" size="60" styleClass="boxConsulta" readonly="true"/></td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
	
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.informacionSustitucion">
						<table class="tablaCampos" border="0">
							<tr>
								<td class="labelText" width="170px"><siga:Idioma key="gratuita.literal.fecha.sustitucion" /></td>
								<td><html:text name="DefinirCalendarioGuardiaForm" property="fechaSustituto" styleClass="boxConsulta" readonly="true"/></td>
							</tr>
	
							<tr>
								<td class="labelText"><siga:Idioma key="gratuita.literal.letrado.sustituido" /></td>
								<td><html:text name="DefinirCalendarioGuardiaForm" property="datosPersSustituto" size="60" styleClass="boxConsulta" readonly="true"/></td>
							</tr>
							
							<tr>
								<td class="labelText"><siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos" /></td>
								<td>
									<html:textarea name="DefinirCalendarioGuardiaForm" property="comentarioSustituto" 
										styleClass="boxConsulta" readonly="true"
										style="overflow-y:auto; overflow-x:hidden; width:800px; height:80px; resize:none;"></html:textarea>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.informacionPermutas">
						<table class="tablaCampos" border="0">										
						 	<tr>
								<td class="labelText" width="170px"><siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechaSolicitud" /> </td>
								<td><html:text name="DefinirCalendarioGuardiaForm" property="fechaSolicitud" styleClass="boxConsulta" readonly="true" /></td>
						 	</tr>
						 	
						 	<tr>
								<td class="labelText"><siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechaConfirmacion" /></td>
								<td><html:text name="DefinirCalendarioGuardiaForm" property="fechaConfirmacion" styleClass="boxConsulta" readonly="true"/></td>
							 </tr>
							 
							 <tr>
								<td class="labelText"><siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechapermuta" /></td>
								<td><html:text name="DefinirCalendarioGuardiaForm" property="periodoPermuta" styleClass="boxConsulta" readonly="true"/></td>
							</tr>
		
							<tr>
								<td class="labelText"><siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.Letradopermuta" /></td>
								<td><html:text name="DefinirCalendarioGuardiaForm" property="datosPersPermutada" size="60" styleClass="boxConsulta" readonly="true"/></td>
							</tr>
		
							<tr>
								<td class="labelText"><siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos" /></td>
								<td>
									<html:textarea name="DefinirCalendarioGuardiaForm" property="motivosSolicitante"
										styleClass="boxConsulta" readonly="true"
										style="overflow-y:auto; overflow-x:hidden; width:800px; height:80px; resize:none;"></html:textarea>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.informacionAnulacion">
						<table class="tablaCampos" border="0">												
							<tr>
								<td class="labelText" width="170px"><siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos" /></td>
								<td>
									<html:textarea name="DefinirCalendarioGuardiaForm" property="comenAnulacion"
										styleClass="boxConsulta" readonly="true"
										style="overflow-y:auto; overflow-x:hidden; width:800px; height:80px; resize:none;"></html:textarea>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>			
		</html:form>
	</table>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle"
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->
	<siga:ConjBotonesAccion botones="C" modal="P" />
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
		//Asociada al boton Cerrar
		function accionCerrar() {	
			top.cierraConParametros("NORMAL");
		}		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>