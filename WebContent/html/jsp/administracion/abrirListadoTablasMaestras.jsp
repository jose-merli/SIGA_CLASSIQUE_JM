<!DOCTYPE html>
<html>
<head>
<!-- abrirListadoTablasMaestras.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="java.util.Properties"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	boolean esComision = userBean.isComision();
	boolean esComisionMultiple = userBean.getInstitucionesComision()!=null &&userBean.getInstitucionesComision().length>1;
	
	String sTipoCombo = CenVisibilidad.getNivelInstitucion(userBean.getLocation());
	if(esComision || esComisionMultiple){
		sTipoCombo = "getTablasMaestrasComisionMultiple";
	}else{
		String intitucionComisionMultiple = (String)request.getAttribute("intitucionComisionMultiple");
		if(intitucionComisionMultiple.equals(ClsConstants.DB_TRUE)){
			sTipoCombo = "getTablasMaestrasInstitucionComisionMultiple";
		}else{
			sTipoCombo = sTipoCombo!=null && sTipoCombo.equals("1") ? "getTablasMaestrasAdmin" : "getTablasMaestras";
		}
	}
	String refrescar="";
	String tabla="";
	if(ses.getAttribute("refrescar")!=null){
		refrescar=(String)ses.getAttribute("refrescar");
		tabla=(String)ses.getAttribute("tabla");
		request.getSession().removeAttribute("refrescar");
		request.getSession().removeAttribute("tabla");
	}	
	
	
	
%>	
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<siga:Titulo titulo="administracion.catalogos.titulo" localizacion="menu.administracion"/>

	<script language="JavaScript">
		var bBuscado=false;
		
		jQuery(function(){
			jQuery("#nombreTablaMaestra").on("change", function(){
				bBuscado=false;
				buscar();
			});
			
			<%if((refrescar!=null)&&(refrescar.equals("S"))){%>
				bBuscado=true;
				listadoTablasMaestrasForm.modo.value="buscarInicio";
				listadoTablasMaestrasForm.nombreTablaMaestra.value="<%=tabla%>";
				listadoTablasMaestrasForm.submit();
			<%}%>

		});
		
		// Funcion asociada a boton buscar
		function buscar() {
			sub();
			if (listadoTablasMaestrasForm.nombreTablaMaestra.value=="") {
				alert("<siga:Idioma key="administracion.catalogos.elegirCatalogo"/>");
				fin();
				bBuscado=false;
			} else {
				bBuscado=true;
				listadoTablasMaestrasForm.modo.value="buscarInicio";
				listadoTablasMaestrasForm.submit();
			}
		}
		
		// Asociada al boton Nuevo
		function nuevo()  {
			if (!bBuscado) {
				alert("<siga:Idioma key="administracion.catalogos.realizarBusqueda"/>");
			} else {
				listadoTablasMaestrasForm.action="./ADM_GestionarTablasMaestras.do";	
				listadoTablasMaestrasForm.modo.value="nuevo";
				listadoTablasMaestrasForm.target="mainWorkArea"; 
				listadoTablasMaestrasForm.submit(); 
			}
		}
	</script>
</head>

<body onLoad="ajusteAlto('resultado');">
	<fieldset>
		<table class="tablaCentralCampos" align="center">
			<html:form action="/ADM_GestionarTablasMaestras.do" method="POST" target="resultado">
				<input type="hidden" name="modo" value="inicio">
				<html:hidden property="actionModal" value=""/>
				<html:hidden property="nombreCampoCodigo"/>
				<html:hidden property="nombreCampoCodigoExt"/>
				<html:hidden property="nombreCampoDescripcion"/>
				<html:hidden property="local"/>
				<html:hidden property="aliasTabla"/>
				<html:hidden property="longitudCodigo"/>
				<html:hidden property="longitudCodigoExt"/>
				<html:hidden property="longitudDescripcion"/>
				<html:hidden property="tipoCodigo"/>
				<html:hidden property="tipoCodigoExt"/>
				
				<html:hidden property="idTablaRel"/>
				<html:hidden property="idCampoCodigoRel"/>
				<html:hidden property="descripcionRel"/>
				<html:hidden property="queryTablaRel"/>
				<html:hidden property="numeroTextoPlantillas"/>
				<html:hidden property="textoPlantillas"/>
				
				<input type="hidden" name="limpiarFilaSeleccionada" value="">

				<tr>				
					<td class="labelText">
						Seleccionar Catálogo&nbsp;(*)
					</td>				
					<td>
						<siga:Select id="nombreTablaMaestra" queryId="<%=sTipoCombo%>" required="true"/>
					</td>
					<td class="labelText">
						<siga:Idioma key="general.codeext"/>
					</td>
					<td class="labelText">
						<html:text property="codigoBusqueda" styleClass="box"/>
					</td>
					<td class="labelText">
						<siga:Idioma key="general.description"/>
					</td>
					<td class="labelText">
						<html:text property="descripcionBusqueda" styleClass="box"/>
					</td>
				</tr>
			</html:form>
		</table>
	</fieldset>

	<!-- B Buscar,N Nuevo registro -->
	<siga:ConjBotonesBusqueda botones="B,N" titulo=""/>

	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
		id="resultado"
		name="resultado" 
		scrolling="no"
		frameborder="0"
		marginheight="0"
		marginwidth="0"					 
		class="frameGeneral"></iframe>
		
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html>