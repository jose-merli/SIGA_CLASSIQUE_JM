<!-- insertarRetencion.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<% 
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	
		<html:javascript formName="SolicitudRetencionForm" staticJavascript="false"/>	
	</head>

	<body>
		<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
		<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="gratuita.insertarRetencion.literal.insertarRetencion"/>
				</td>
			</tr>
		</table>
		<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

		<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
		<div id="campos" class="posicionModalPeque" align="center">

			<!-- INICIO: CAMPOS DEL REGISTRO -->
			<!-- Comienzo del formulario con los campos -->
			<table class="tablaCentralCamposPeque" align="center">		
				<html:form action="/SolicitudRetencioAction.do" method="POST" target="submitArea">
					<html:hidden property = "modo" value = "Insertar"/>
					<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
					<html:hidden property = "fechaMod" value = "sysdate"/>
	
					<tr>		
						<td>			
	
							<!-- SUBCONJUNTO DE DATOS -->
							<siga:ConjCampos leyenda="gratuita.retenciones.retencion">
								<table class="tablaCampos" align="center">
	
									<!-- FILA -->
									<tr>
										<td class="labelText">
											<siga:Idioma key="gratuita.retenciones.descripcion"/>&nbsp;(*)
										</td>	
										<td align="left">
											<html:text name="SolicitudRetencionForm" property="descripcion" size="50" maxlength="60" styleClass="box" value="" />
										</td>										
									</tr>
										
									<tr>	
										<td class="labelText">
											<siga:Idioma key="gratuita.retenciones.retencion"/>&nbsp;(*)
										</td>				
										<td align="left">
											<html:text name="SolicitudRetencionForm" property="retencion" size="5" maxlength="5" styleClass="boxNumber" value="0.0" />
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											<siga:Idioma key="gratuita.retenciones.tipoSociedad"/>
										</td>					
										<td align="left">
											<siga:ComboBD nombre="comboSociedades" tipo="tipoSociedad" clase="boxCombo" ancho="320"/>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>
			</table>
			<!-- FIN: CAMPOS DEL REGISTRO -->
		</div>

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">	
	
			//Asociada al boton Restablecer
			function accionRestablecer() {		
				document.forms[0].reset();			
			}
		
			//Asociada al boton Guardar y Cerrar
			function accionGuardarCerrar() {	
				document.SolicitudRetencionForm.retencion.value = document.SolicitudRetencionForm.retencion.value.replace(",", ".");
				if (validateSolicitudRetencionForm(document.SolicitudRetencionForm)){
	        		document.forms[0].submit();	        
					window.top.returnValue="MODIFICADO";				
				}
			}	
						
			//Asociada al boton Cerrar
			function accionCerrar() {
				top.cierraConParametros("NORMAL");			
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>