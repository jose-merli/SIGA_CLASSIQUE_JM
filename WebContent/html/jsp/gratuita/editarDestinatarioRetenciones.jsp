<!-- editarDestinatarioRetenciones.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

<!-- IMPORTS -->
<%@page import="com.siga.gratuita.form.MantenimientoDestinatariosRetencionesForm" %>
<%@ page import="com.siga.beans.FcsDestinatariosRetencionesBean" %>
<%@page import="com.siga.Utilidades.UtilidadesHash" %>
<%@page import="com.siga.Utilidades.UtilidadesString" %>
<%@page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.*"%>
<%@page import="utils.system"%>

<!-- JSP -->
<% 
		String app=request.getContextPath(); 
		String modo = "";
		String estilo=""; 
		String accion = "";
		boolean soloLectura = true;
		modo = request.getParameter("modo");		
		// Usamos una variable para definir el modo lectura de los boxes
		if (modo.equalsIgnoreCase("ver")) {
			soloLectura = true;
			estilo = "boxConsulta";
		}else{
			soloLectura = false;
			estilo = "box";
		}
		MantenimientoDestinatariosRetencionesForm formulario = 
			(MantenimientoDestinatariosRetencionesForm) request.getAttribute("MantenimientoDestinatariosRetencionesForm");
		
%>

<%@page import="com.siga.beans.FcsDestinatariosRetencionesAdm"%>
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoDestinatariosRetencionesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
		}

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			if (validateMantenimientoDestinatariosRetencionesForm(document.MantenimientoDestinatariosRetencionesForm)){
				// Una vez validados los datos comprobamos que tipo de accion vamos a realizar para cambiar el modo	
				if(<%=modo.equalsIgnoreCase("nuevo")%>){
					MantenimientoDestinatariosRetencionesForm.modo.value = "insertar";
				}else{
					MantenimientoDestinatariosRetencionesForm.modo.value = "modificar";
				}
				MantenimientoDestinatariosRetencionesForm.submit();
			}
		}
		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" height="32">
		<tr>
			<td id="titulitosDatos" class="titulitosDatos">
				<siga:Idioma key="gratuita.maestros.destinatariosRetenciones.titulo"/>
			</td>
		</tr>
	</table>


	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/JGR_MantenimientoDestinatariosRetenciones.do" method="POST" target="submitArea">
		<html:hidden property="modo" />
		<html:hidden name="MantenimientoDestinatariosRetencionesForm" property="idInstitucion" />
		<input type="hidden" name="actionModal" value="">
		
		<table class="tablaCentralCamposMedia" align="center" cellspacing="0" >
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.maestros.destinatariosRetenciones.literal.destinatario">
						<table class="tablaCampos" border="0">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.maestros.destinatariosRetenciones.literal.descripcion"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<html:text 	name="MantenimientoDestinatariosRetencionesForm" 
											   	property="nombre" 
											   	size="30" 
											   	maxlength="250" 
											   	styleClass="<%=estilo%>"
											   	readonly="<%=soloLectura%>">
									</html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.maestros.destinatariosRetenciones.literal.orden"/>&nbsp;
								</td>
								<td class="labelText">
									<html:text 	name="MantenimientoDestinatariosRetencionesForm" 
												property="orden" 
												size="30" 
												maxlength="4" 
												styleClass="<%=estilo%>" 
												readonly="<%=soloLectura%>">
									</html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.maestros.destinatariosRetenciones.literal.cuentaContable"/>&nbsp;
								</td>
								<td class="labelText">
									<html:text 	name="MantenimientoDestinatariosRetencionesForm" 
												property="cuentaContable" 
												size="30" 
												maxlength="20" 
												styleClass="<%=estilo%>" 
												readonly="<%=soloLectura%>">
									</html:text>
								</td>
							</tr>
						</table>
						
				</siga:ConjCampos>	
				</td>
			</tr>	
		</table>
	</html:form>
	<div style="position:absolute; bottom:50px;">
		<table align="center" border="0">
			<tr>
				<td class="labelText">&nbsp;&nbsp;
					<siga:Idioma key="gratuita.maestros.destinatariosRetenciones.alerta"/>
				</td>
			</tr>
		</table>
	</div>
		
		
	
	<% if (modo.equalsIgnoreCase("ver")) {%>
		<siga:ConjBotonesAccion botones="C" modal="G" modo="<%=modo%>" clase="botonesDetalle" />
	<% } else { %>
		<siga:ConjBotonesAccion botones="C,Y" modal="G" modo="<%=modo%>" clase="botonesDetalle" />
	<% } %>
	<!-- FIN: CAMPOS -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
