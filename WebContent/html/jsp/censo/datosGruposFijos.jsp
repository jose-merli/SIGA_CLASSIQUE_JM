<!DOCTYPE html>
<html>
<head>
<!-- datosGruposFijos.jsp -->

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
<%@ taglib uri = "struts-logic.tld"  	prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.siga.censo.form.MantenimientoGruposFijosForm"%>
<%@page import="org.redabogacia.sigaservices.app.vo.cen.CenGruposFicherosVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	String estilo="box";
	boolean desactivado = true;
	String accion = "";
	String modo = (String)request.getAttribute("modo");	
	String parametro[] = new String[1];
	String botones="";
	
	// Formulario
	MantenimientoGruposFijosForm formulario = (MantenimientoGruposFijosForm) request.getAttribute("MantenimientoGruposFijosForm");
	if (modo.equalsIgnoreCase("EDITAR")) {
		desactivado  = false;
		estilo = "box";
		accion = "modificar";
		botones="V,G";
	} else {
		if (modo.equalsIgnoreCase("NUEVO")) {
				desactivado = false;
				accion = "insertar";
			    botones="V,G";
		} else { //MODO=VER
				desactivado  = true;
				estilo = "boxConsulta";
				accion = "ver";
				botones="V";
		}
	}

%>

<!-- HEAD -->
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoGruposFijosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<siga:Titulo titulo="censo.datosGruposFijos.datosgenerales" localizacion="censo.GruposFijos.localizacion"/>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function accionVolver(){ 
			sub();
			document.MantenimientoGruposFijosForm.target="mainWorkArea";
			document.MantenimientoGruposFijosForm.modo.value = "abrirAlvolver";
			document.MantenimientoGruposFijosForm.submit();
			fin();
		}	
		
		function refrescarLocal(){
			sub();
			document.MantenimientoGruposFijosForm.target="mainWorkArea";
			document.MantenimientoGruposFijosForm.modo.value = "editar";
			document.MantenimientoGruposFijosForm.submit();
			fin();
			
		}

		function accionGuardar(){
			sub();
			if (validateMantenimientoGruposFijosForm(document.getElementById("MantenimientoGruposFijosForm"))) {
				document.getElementById("MantenimientoGruposFijosForm").modo.value = "<%=accion%>";
				document.getElementById("MantenimientoGruposFijosForm").submit();
			} else {
				fin();
				return false;
			}
		}		
		
	</script>	
</head>

<body class="tablaCentralCampos" >
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/CEN_MantenimientoGruposFijos.do" method="POST" target="submitArea" styleId="MantenimientoGruposFijosForm" enctype="multipart/form-data">
		<html:hidden property = "modo" />
		<html:hidden property = "directorio" />
		<html:hidden property = "nombrefichero" />
		<html:hidden property = "busquedaNombre" />
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.datosGruposFijos.literal.GruposFijos">
						<table class="tablaCampos" border="0" width="100%">
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre" />&nbsp;(*)
								</td>
								<td>
									<html:text name="MantenimientoGruposFijosForm" property="nombre" size="80" maxlength="100" readonly="<%=desactivado %>" styleClass="<%=estilo%>" />
								</td>
							</tr>
							<%if (!modo.equalsIgnoreCase("NUEVO")) { %>
							<tr>
								<td class="labelText"><siga:Idioma key="censo.gestion.grupos.literal.identificador" /></td>
								<td>
									<html:text name="MantenimientoGruposFijosForm" property="idGrupo" size="80" maxlength="100" readonly="true"	styleClass="boxConsulta"/>
								</td>
							</tr>
							<%}%>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	</html:form>
	<!-- FIN: CAMPOS -->		
	
	<siga:ConjBotonesAccion botones="<%=botones%>" modo="<%=accion%>" clase="botonesDetalle"/>

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>