<!-- datosPrision.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"      prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

<!-- IMPORTS -->
<%@page import="com.siga.gratuita.form.MantenimientoPrisionForm"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<% 
		String app=request.getContextPath(); 
		
		String estilo="box", estiloCombo="boxCombo";
		String comboLectura = "false";
		boolean desactivado = true;
		String accion = "";
		String modo = (String)request.getAttribute("modo");	

		ArrayList provinciaSel = new ArrayList();
		ArrayList poblacionSel = new ArrayList();
		String parametro[] = new String[1];

	// Formulario
	MantenimientoPrisionForm formulario = (MantenimientoPrisionForm) request.getAttribute("MantenimientoPrisionForm");

		if (modo.equalsIgnoreCase("EDITAR")) {
			desactivado  = false;
			estilo = "box";
			estiloCombo = "boxCombo";
			accion = "modificar";
			comboLectura = "false";
			// Datos seleccionados de los combos:
			provinciaSel.add(formulario.getIdProvincia());
			poblacionSel.add(formulario.getIdPoblacion());
			parametro[0] = formulario.getIdProvincia();
		} else {
				if (modo.equalsIgnoreCase("NUEVO")) {
						desactivado = false;
						accion = "insertar";
						comboLectura = "false";
				} else { //MODO=VER
						desactivado  = true;
						estilo = "boxConsulta";
						estiloCombo = "boxConsulta";
						accion = "ver";
						comboLectura = "true";
						// Datos seleccionados de los combos:
						provinciaSel.add(formulario.getIdProvincia());
						poblacionSel.add(formulario.getIdPoblacion());
						parametro[0] = formulario.getIdProvincia();
				}
		}
%>

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>


	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoPrisionForm" staticJavascript="false" />  
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
		}	

		//Asociada al boton Reset -->
		function accionRestablecer(){ 
			MantenimientoPrisionForm.reset();
		}	
	
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			sub();
		    document.MantenimientoPrisionForm.telefono1.value=eliminarBlancos(trim(document.MantenimientoPrisionForm.telefono1.value));
		  document.MantenimientoPrisionForm.telefono2.value=eliminarBlancos(trim(document.MantenimientoPrisionForm.telefono2.value));
		  document.MantenimientoPrisionForm.fax1.value=eliminarBlancos(trim(document.MantenimientoPrisionForm.fax1.value));
			if (validateMantenimientoPrisionForm(document.MantenimientoPrisionForm)){
				MantenimientoPrisionForm.modo.value = "<%=accion%>";
				MantenimientoPrisionForm.submit();
			}else{
			
				fin();
				return false;
			}
		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.prision"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/JGR_MantenimientoPrisiones.do" method="POST" target="submitArea">
		<html:hidden property = "modo" />
		<html:hidden name="MantenimientoPrisionForm" property="idPrision" />
		
		<table class="tablaCentralCamposMedia" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.prision">
						<table class="tablaCampos" border="0">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>&nbsp;(*)
								</td>
								<td class="labelText" colspan="3">
									<html:text name="MantenimientoPrisionForm" property="nombre" size="20" maxlength="200" readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.direccion"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoPrisionForm" property="direccion" size="20" maxlength="100" readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoPostal"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoPrisionForm" property="codigoPostal" size="5" maxlength="5" readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.provincia"/>
								</td>
								<td class="labelText" >
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoPrisionForm" property="provincia" styleClass="boxConsulta" readonly="<%=desactivado %>" size="20"></html:text>
									<% } else { %>
											<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="<%=estiloCombo%>" obligatorio="false"  elementoSel="<%=provinciaSel %>" accion="Hijo:idPoblacion" />
									<% } %>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoPrisionForm" property="codigoExt" size="10" maxlength="10" readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.poblacion"/>
								</td>
								<td class="labelText" colspan="3">
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoPrisionForm" property="poblacion" styleClass="boxConsulta" readonly="<%=desactivado %>" size="20"></html:text>
									<% } else { %>
												<% if (modo.equalsIgnoreCase("NUEVO")) { %>
														<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="<%=estiloCombo%>" elementoSel="<%=poblacionSel %>" hijo="t" />
												<% } else { %>
														<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="<%=estiloCombo%>" elementoSel="<%=poblacionSel %>" hijo="t" parametro="<%=parametro%>" />
												<% } %>
									<% } %>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.telefono2"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoPrisionForm" property="telefono1" size="20" maxlength="20" readonly="<%=desactivado %>" styleClass="<%=estilo%>" ></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.telefono1"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoPrisionForm" property="telefono2" size="20" maxlength="20" readonly="<%=desactivado %>" styleClass="<%=estilo%>" ></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.fax1"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoPrisionForm" property="fax1" size="20" maxlength="20" readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
					</table>
				</siga:ConjCampos>	
				</td>
			</tr>
		</html:form>
		</table>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones="C,Y,R" modo="<%=accion%>" modal="M" />

</div>


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>