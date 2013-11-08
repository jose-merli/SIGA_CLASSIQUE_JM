<!DOCTYPE html>
<html>
<head>
<!-- modalNuevoDelitoAsistencia.jsp-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri ="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.form.PestanaDelitoAsistenciaForm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.Utilidades.*"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	
	
	PestanaDelitoAsistenciaForm formulario = (PestanaDelitoAsistenciaForm)request.getAttribute("pestanaDelitoAsistenciaForm");
	String anio = formulario.getAnio().toString();
	String numero = formulario.getNumero().toString();
	String parametro[] = {usr.getLocation(), usr.getLocation(), numero, anio};
	
	boolean esFichaColegial = false;

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null) && ( (sEsFichaColegial.equalsIgnoreCase("1") || (sEsFichaColegial.equalsIgnoreCase("true")) ))) {
		esFichaColegial = true;
	}
%>



<!-- HEAD -->


	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="pestanaDelitoAsistenciaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
		<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.delito"/>
	</td>
</tr>
</table>
	
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DelitosAsistencia.do" method="post">
		<html:hidden property = "modo" value = ""/>
		<!-- Datos de la pestanha -->
		<html:hidden name="pestanaDelitoAsistenciaForm" property="anio" />
		<html:hidden name="pestanaDelitoAsistenciaForm" property="numero" />
		<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>

	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
		<td>		
		<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.delito">
			<table class="tablaCampos" align="center">
			<tr>
				<td class="labelText" nowrap>
						<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.delito"/>&nbsp;(*)
				</td>
				<td>
					<%
					HashMap<String, String> hmDeligosparams = new HashMap<String, String>();
					hmDeligosparams.put("numero", numero);
					hmDeligosparams.put("anio", anio);
					String deligosparamsJSON = UtilidadesString.createJsonString(hmDeligosparams);
					%>
					<siga:Select queryId="getDelitosAsistencia" id="idDelito" params="<%=deligosparamsJSON%>" required="true" width="520"/>
				</td>
			</tr>
			</table>
		</siga:ConjCampos>		
		</td>
	</tr>
	</table>
	</html:form>			
	
		<siga:ConjBotonesAccion botones="Y,C" modal="P"/>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	//Asociada al boton Cerrar -->
		function accionCerrar() {
			top.cierraConParametros("NORMAL");
		}

		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();	
<%			if (esFichaColegial) {%>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistenciaLetrado.do";
<%			} else { %>
				document.forms[0].action = "/SIGA/JGR_DelitosAsistencia.do";
<%			}%>

				if (validatePestanaDelitoAsistenciaForm(document.pestanaDelitoAsistenciaForm)) {
					document.forms[0].modo.value = "insertar";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
					window.top.returnValue="MODIFICADO";
				}else{
					fin();
					return false;
				}
		}

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