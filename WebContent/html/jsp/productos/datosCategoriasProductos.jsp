<!DOCTYPE html>
<html>
<head>
<!-- datosCategoriasProductos.jsp -->
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
<%@ page import="com.siga.beans.PysProductosBean"%>
<%@ page import="java.util.ArrayList" %>
<!-- JSP -->
<% 
		String app=request.getContextPath(); 
		String claseCombo = "boxCombo";
		boolean desactivado = true;
		
		String descripcion = "";
		ArrayList listaTipos = new ArrayList();

		String accion = "";
		String modo = (String)request.getAttribute("modo");	
		if (modo.equals("editar")) {
			desactivado  = true;
			claseCombo = "boxConsulta";
			accion = "modificar";
			
			PysProductosBean producto = (PysProductosBean)request.getSession().getAttribute("DATABACKUP");
			if (producto != null) {
				listaTipos.add(String.valueOf(producto.getIdTipoProducto()));
				descripcion = producto.getDescripcion();
			}
		} 
		else {
			if (modo.equals("nuevo")) {
				desactivado = false;
				accion = "insertar";
			}
		}
%>



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoCategoriasProductosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
		}	

		<!-- Asociada al boton Reset -->
		function accionRestablecer(){ 
			MantenimientoCategoriasProductosForm.reset();
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			if (!validateMantenimientoCategoriasProductosForm(document.MantenimientoCategoriasProductosForm)){
				return;
			}
			MantenimientoCategoriasProductosForm.modo.value = "<%=accion%>";
			MantenimientoCategoriasProductosForm.submit();
		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="pys.mantenimientoCategorias.cabeceraProductos"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/PYS_MantenimientoCategoriasProductos.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<table class="tablaCentralCamposPeque" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="pys.mantenimientoCategorias.leyenda">	
						<table class="tablaCampos" border="0">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="pys.mantenimientoCategorias.literal.tipoProducto"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="idTipoProducto" tipo="cmbTipoProducto" clase="<%=claseCombo%>" ancho="290" obligatorio="true" elementoSel="<%=listaTipos%>" readonly="<%=String.valueOf(desactivado)%>"/>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="pys.busquedaProductos.literal.categoria"/>&nbsp;(*)
								</td>
								<td class="labelText">
  									<html:text property="descripcionProducto" size="43" maxlength="100" styleClass="box" value="<%=descripcion%>"/>
								</td>
							</tr>
					</table>
				</siga:ConjCampos>	
				</td>
			</tr>		
		</table>
	</html:form>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='C,Y,R' modo='' modal="P" />


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>