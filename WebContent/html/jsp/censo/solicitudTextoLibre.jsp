<!-- solicitudTextoLibre.jsp -->
<!-- 
	 Proporciona un formulario para realizar una solicitud de modificacion
	 VERSIONES:
	 miguel.villegas 04-01-2005 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	
	// Recojo la institucion y el usuario
	Long idPersona=new Long((String)request.getAttribute("IDPERSONA")); 
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION");
	String idPersonaSol=(String)request.getAttribute("IDPERSONASOL");
	String idTipoSol=(String)request.getAttribute("TIPO");
	
%>	
	
<html>

	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="SolicitudesModificacionForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="censo.solicitudTextoLibre.literal.cabeceraSolicitud" 
			localizacion="censo.solicitudTextoLibre.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>
		<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->

		<% if (idPersona.compareTo(new Long('0'))<0){ %>
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.censo.errorNoAcceso"/></p>
	 		<br><br>	 		
		<% }else{ %>
			<!-- INICIO: CAMPOS DEL REGISTRO -->
			<fieldset>
			<!-- Comienzo del formulario con los campos -->
			<table class="tablaCentralCampos" align="center">
				<html:form action="/CEN_ModificacionDatos.do" method="POST" target="submitArea">
					<html:hidden property = "modo" value = "insertar"/>
					<tr>		
						<td>		
							<table table style="border:0px; width:100%;">
								<!-- FILA -->
								<tr>				
									<td class="labelText" style="width:20%;">
										<siga:Idioma key="censo.solicitudTextoLibre.literal.tipoModificacion"/>&nbsp;(*)
									</td>
									<% if (idTipoSol == null){ %>
									<td>	
										<siga:ComboBD nombre ="cmbTipoModificacion" tipo="cmbTipoModificacion" clase="boxCombo" obligatorio="true"/>
									</td>	
									<% } else { %>
									<td>
										<siga:ComboBD nombre ="cmbTipoModificacion" elementoSelstring="10" readonly="true" tipo="cmbTipoModificacion" clase="boxCombo" obligatorio="true"/>
									</td>
									<% }%>	
								</tr>
								<!-- FILA -->
								<tr>				
									<td class="labelText" style="width:20%;">
										<siga:Idioma key="censo.solicitudTextoLibre.literal.descripcion"/>&nbsp;(*)
									</td colspan="1" style="width:50%;">				
									<td>
										<textarea name="descripcion" class="box" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" style="width:100%;"  rows="24" cols="180"></textarea>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<html:hidden name="SolicitudesModificacionForm" property="idInstitucion"/>
					<input type="hidden" name="idPersona" value="<%=idPersonaSol%>">
				</html:form>
			</table>
			</fieldset>
			<!-- FIN: CAMPOS DEL REGISTRO -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
				 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
				 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
			-->
			<% if (idTipoSol == null){ %>
				<siga:ConjBotonesAccion botones="G,R" clase="botonesDetalle"  />
			<% } else { %>
				<siga:ConjBotonesAccion botones="Y,R,C" clase="botonesDetalle"  />
			<% }%>
		<% } %>
		<!-- FIN: BOTONES REGISTRO -->
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
		
			//Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}
		
			//Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();	
				if (validateSolicitudesModificacionForm(document.SolicitudesModificacionForm)){
					document.forms[0].submit();	
					top.cierraConParametros("NORMAL");
				} else{
					fin();
					return false;
				}
			}
			
			//Asociada al boton Guardar -->
			function accionGuardar() 
			{	
				sub();	
				if (validateSolicitudesModificacionForm(document.SolicitudesModificacionForm)){
					document.forms[0].submit();
				}else{
					fin();
				}		
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
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
