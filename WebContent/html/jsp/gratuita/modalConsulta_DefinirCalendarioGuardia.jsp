<!-- modalConsulta_DefinirCalendarioGuardia.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ taglib uri="c.tld" prefix="c"%>


<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	String profile[] = usr.getProfile();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
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
			<td><siga:ConjCampos
				leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.infoGuardia">
				<table class="tablaCampos" border="0">
					<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.periodoGuardia" />
						</td>
						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm" property="periodos"
							styleClass="boxConsulta" readonly="true"></html:text></td>
					</tr>

					<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.letrado" />
						</td>

						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm"
							property="datosPersSolicitante" size="60" styleClass="boxConsulta"
							readonly="true"></html:text>
					</tr>
				</table>
			</siga:ConjCampos></td>
		</tr>

		<tr>
			<td><siga:ConjCampos
				leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.informacionSustitucion">
				<table class="tablaCampos" border="0">
					<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.literal.fecha.sustitucion" /></td>
						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm" property="fechaSustituto"
							styleClass="boxConsulta" readonly="true"></html:text></td>
					</tr>

					<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.literal.letrado.sustituido" /></td>
						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm" property="datosPersSustituto"
							size="60" styleClass="boxConsulta" readonly="true"></html:text></td>

					</tr>
					<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos" />
						</td>
						<td width="70%"><html:textarea
							name="DefinirCalendarioGuardiaForm"
							property="comentarioSustituto" cols="60" rows="3"
							style="width:470" styleClass="boxConsulta" readOnly="true"></html:textarea>
						</td>
					</tr>
				</table>
			</siga:ConjCampos></td>
		</tr>
		<tr>
			<td><siga:ConjCampos
				leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.informacionPermutas">
				<table class="tablaCampos" border="0">				
					
					 	<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechaSolicitud" />
						</td>
						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm" property="fechaSolicitud"
							styleClass="boxConsulta" readonly="true"></html:text></td>
					 </tr>
					 <tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechaConfirmacion" />
						</td>
						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm" property="fechaConfirmacion"
							styleClass="boxConsulta" readonly="true"></html:text></td>
					 </tr>
					 
					 <tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechapermuta" />
						</td>
						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm" property="periodoPermuta"
							styleClass="boxConsulta" readonly="true"></html:text></td>
					</tr>

					<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.Letradopermuta" />
						</td>
						<td width="70%"><html:text
							name="DefinirCalendarioGuardiaForm" property="datosPersPermutada"
							size="60" styleClass="boxConsulta" readonly="true"></html:text></td>
					</tr>

					<tr>
						<td class="labelText" width="30%"><siga:Idioma
							key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos" />
						</td>
						<td width="70%"><html:textarea
							name="DefinirCalendarioGuardiaForm" property="motivosSolicitante"
							cols="60" rows="3" style="width:470" styleClass="boxConsulta"
							readOnly="true"></html:textarea></td>
					</tr>
				</table>
			</siga:ConjCampos></td>
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
	
		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{	
		  //alert("Motivos:"+document.DefinirCalendarioGuardiaForm.motivosSolicitante.value);	
			top.cierraConParametros("NORMAL");
		}		

	</script>
<!-- FIN: SCRIPTS BOTONES -->
<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>