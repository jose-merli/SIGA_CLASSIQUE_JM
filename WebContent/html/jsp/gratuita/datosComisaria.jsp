<!-- datosComisaria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	    prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

<!-- IMPORTS -->
<%@page import="com.siga.gratuita.form.MantenimientoComisariaForm"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.atos.utils.*"%>

<!-- JSP -->
<% 
		String app=request.getContextPath(); 
		
		String estilo="box", estiloCombo="boxCombo";
		String comboLectura = "false";
		boolean desactivado = true;
		String accion = "";
		String modo = (String)request.getAttribute("modo");	
		String fechaBaja = "";

			
		ArrayList provinciaSel = new ArrayList();
		ArrayList poblacionSel = new ArrayList();
		String parametro[] = new String[1];
		
		String idInstitucionComisaria = "";
		
		// Formulario
		MantenimientoComisariaForm formulario = (MantenimientoComisariaForm) request.getAttribute("MantenimientoComisariaForm");
		if(formulario.getDatos().get("FECHABAJA")!=null && !((String)formulario.getDatos().get("FECHABAJA")).equals("")){
			fechaBaja = GstDate.getFormatedDateShort("", (String)formulario.getDatos().get("FECHABAJA"));
		}
		if(formulario.getDatos().get("IDINSTITUCIONCOMISARIA")!=null && !((String)formulario.getDatos().get("IDINSTITUCIONCOMISARIA")).equals(""))
			idInstitucionComisaria = (String)formulario.getDatos().get("IDINSTITUCIONCOMISARIA"); 
		String ponerBaja = "N";
		if(fechaBaja !=null && !fechaBaja.equals("")){
			ponerBaja = "S";
		}
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
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoComisariaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		var idProvinciaOriginal = <%=provinciaSel%>;
		var idPoblacionOriginal = <%=poblacionSel%>;
	
		//Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
		}	

		//Asociada al boton Reset -->
		function accionRestablecer(){ 			
			MantenimientoComisariaForm.reset();
			jQuery("#idProvincia").val(idProvinciaOriginal);
			jQuery("#idProvincia").change();
			jQuery("#idPoblacionSel").val(idPoblacionOriginal);
		}	
	
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			sub()
			if (validateMantenimientoComisariaForm(document.MantenimientoComisariaForm)){
				MantenimientoComisariaForm.modo.value = "<%=accion%>";
				MantenimientoComisariaForm.submit();
			}else{
			
				fin();
				return false;
			
			}
		}	


 		function darDeBaja (o) {
 			if (o.checked) {
 				MantenimientoComisariaForm.ponerBaja.value = "S";
			} else {
				MantenimientoComisariaForm.ponerBaja.value = "N";
			}
 		}	
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.comisaria"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
		<html:hidden property = "modo" />
		<html:hidden name="MantenimientoComisariaForm" property="idComisaria" />
		<html:hidden name="MantenimientoComisariaForm" property="idInstitucionComisaria" value="<%=idInstitucionComisaria %>"/>
		
		<table class="tablaCentralCamposMedia" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.comisaria">
						<table class="tablaCampos" border="0">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>&nbsp;(*)
								</td>
								<td class="labelText" >
									<html:text name="MantenimientoComisariaForm" property="nombre" size="20" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoComisariaForm" property="codigoExt" size="10" maxlength="10"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.direccion"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoComisariaForm" property="direccion" size="20" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoPostal"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoComisariaForm" property="codigoPostal" size="5" maxlength="5" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.provincia"/>&nbsp;(*)
								</td>
								<td class="labelText" colspan="3">
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoComisariaForm" property="provincia" styleClass="boxConsulta" size="20"></html:text>
									<% } else { %>
											<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="<%=estiloCombo%>" obligatorio="false"  elementoSel="<%=provinciaSel %>" accion="Hijo:idPoblacion" />
									<% } %>
								</td>
								
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.poblacion"/>&nbsp;(*)
								</td>
								<td class="labelText" colspan="3">
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoComisariaForm" property="poblacion" styleClass="boxConsulta" size="20"></html:text>
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
									<html:text name="MantenimientoComisariaForm" property="telefono1" size="20" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.telefono1"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoComisariaForm" property="telefono2" size="20" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.fax1"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoComisariaForm" property="fax1" size="20" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="general.baja"/>
	
								</td>
								<td class="labelTextValue">
									<input type="checkbox" name="ponerBaja" style="" onclick="darDeBaja(this);" <% if (modo.equalsIgnoreCase("VER")) { %>disabled<%}%> value="<%=ponerBaja%>" <%if (fechaBaja !=null && !fechaBaja.equals("")) {%>checked<%}%>>
									<%if (fechaBaja !=null && !fechaBaja.equals("")) {%>
										&nbsp;&nbsp;&nbsp; Baja desde: <%=fechaBaja%>
									<%}%>								
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