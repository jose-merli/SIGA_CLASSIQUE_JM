<!-- abrirListadoTablasMaestrasPCAJG.jsp -->
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
<%@page import="java.util.Properties"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String sTipoCombo = CenVisibilidad.getNivelInstitucion(userBean.getLocation());
	sTipoCombo = sTipoCombo!=null && sTipoCombo.equals("1") ? "tablasMaestrasPCAJG" : "tablasMaestrasPCAJG";
%>	
	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<siga:Titulo titulo="menu.justiciaGratuita.e_comunicaciones.TablasMaestras" localizacion="gratuita.eComunicaciones.localizacion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
			var bBuscado=false;
			
			jQuery(function(){
				jQuery("#nombreTablaMaestra").on("change", function(){
					bBuscado=false;
					buscar();
				});
				buscar();
			});
			
			<!-- Funcion asociada a boton buscar -->
			function buscar(){
				sub();
				if (listadoTablasMaestrasPCAJGForm.nombreTablaMaestra.value=="") {
					alert("<siga:Idioma key="administracion.catalogos.elegirCatalogo"/>");
					fin();
					bBuscado=false;
				} else {
					bBuscado=true;
					listadoTablasMaestrasPCAJGForm.modo.value="buscarInicio";
					listadoTablasMaestrasPCAJGForm.submit();
				}
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function nuevo() 
			{
				if (!bBuscado){
					alert("<siga:Idioma key="administracion.catalogos.realizarBusqueda"/>");
				} else {
					listadoTablasMaestrasPCAJGForm.modo.value="nuevo";
					var resultado=ventaModalGeneral("listadoTablasMaestrasPCAJGForm","P");
					
					if (resultado=="MODIFICADO") {
						buscar();
					}
				}
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

	</head>

	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/PCAJGGestionarTablasMaestras.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<html:hidden property="actionModal" value=""/>
					<html:hidden property="aliasTabla"/>
					<input type="hidden" name="limpiarFilaSeleccionada" value="">

					<tr>				
						<td class="labelText">
							Seleccionar Catálogo&nbsp;(*)
						</td>				
						<td>
							<siga:Select id="nombreTablaMaestra" queryId="<%=sTipoCombo%>" required="true"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="general.code"/>
						</td>
						<td class="labelText">
							<html:text property="codigo" styleClass="box"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="general.description"/>
						</td>
						<td class="labelText">
							<html:text property="descripcion" styleClass="box"/>
						</td>
					</tr>
				</html:form>
			</table>
			</fieldset>
			

			<!-- V Volver, B Buscar, A Avanzada, S Simple, N Nuevo registro, L Limpiar, R Borrar Log -->
			<siga:ConjBotonesBusqueda botones="B,N" titulo=""/>

			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado"
							name="resultado" 
					   		scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0";					 
					class="frameGeneral">
	</iframe>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
