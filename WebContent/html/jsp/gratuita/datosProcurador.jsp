<!-- datosProcurador.jsp -->

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
<%@page import="com.siga.gratuita.form.MantenimientoProcuradorForm"%>
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
		ArrayList colegioSel = new ArrayList();
		String parametro[] = new String[1];
		Integer PCAJG_ACTIVADO =(Integer) (request.getAttribute("PCAJG_ACTIVO"));
		String pintarAsterisco="";
		if (PCAJG_ACTIVADO!=null && PCAJG_ACTIVADO.intValue()>1){
			pintarAsterisco="&nbsp;(*)";
			
		}

	// Formulario
	MantenimientoProcuradorForm formulario = (MantenimientoProcuradorForm) request.getAttribute("MantenimientoProcuradorForm");

		if (modo.equalsIgnoreCase("EDITAR")) {
			desactivado  = false;
			estilo = "box";
			estiloCombo = "boxCombo";
			accion = "modificar";
			comboLectura = "false";
			// Datos seleccionados de los combos:
			provinciaSel.add(formulario.getIdProvincia());
			poblacionSel.add(formulario.getIdPoblacion());
			colegioSel.add(formulario.getIdColProcurador());
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
						colegioSel.add(formulario.getIdColProcurador());
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
	<html:javascript formName="MantenimientoProcuradorForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
		}	

		//Asociada al boton Reset -->
		function accionRestablecer(){ 
			MantenimientoProcuradorForm.reset();
		}	
	
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
		  	sub();
		  	
		   
			if (validateMantenimientoProcuradorForm(document.MantenimientoProcuradorForm)){
			  <%if (PCAJG_ACTIVADO!=null && PCAJG_ACTIVADO.intValue()>1){%>
			   if (document.MantenimientoProcuradorForm.NColegiado.value==""){
			    fin();
			    alert('<siga:Idioma key="gratuita.datosProcurador.message.requeridoNColegiado"/>');
			    return false;
		       }
		   <%}%> 
		   
		   if (document.MantenimientoProcuradorForm.idColProcurador.value==""){
			    fin();
			    alert('<siga:Idioma key="gratuita.datosProcurador.message.requeridoColegio"/>');
			    return false;
		       }
			  
				MantenimientoProcuradorForm.modo.value = "<%=accion%>";
				MantenimientoProcuradorForm.submit();
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
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="gratuita.datosProcurador.literal.procurador"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->


	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/JGR_MantenimientoProcuradores.do" method="POST" target="submitArea">
		<html:hidden property = "modo" />
		<html:hidden name="MantenimientoProcuradorForm" property="idProcurador" />
		
		<table class="tablaCentralCamposMedia" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.datosProcurador.literal.procurador">
						<table class="tablaCampos" border="0" width="100%">	
						  <tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codProcurador"/>
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="codProcurador" size="10" maxlength="10" styleClass="<%=estilo%>" ></html:text>
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>&nbsp;(*)
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="nombre" size="30" maxlength="200" styleClass="<%=estilo%>"></html:text>
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.apellido1"/>&nbsp(*)
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="apellido1" size="30" maxlength="100" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.apellido2"/>
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="apellido2" size="30" maxlength="100" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>

							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.colegioprocurador"/><%=pintarAsterisco%>
								</td>
								<td colspan="5">
									<siga:ComboBD nombre="idColProcurador" tipo="comboColegioProcuradores"  clase="<%=estiloCombo%>" obligatorio="true" elementoSel="<%=colegioSel%>"/>						
								</td>						
							</tr>
								
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.colegiado"/><%=pintarAsterisco%>
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="NColegiado" size="30" maxlength="100" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
								
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.direccion"/>
								</td>
								<td colspan="3">
									<html:text name="MantenimientoProcuradorForm" property="direccion" size="50" maxlength="100" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.provincia"/>
								</td>
								<td>
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoProcuradorForm" property="provincia" styleClass="boxConsulta" size="20"></html:text>
									<% } else { %>
											<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="<%=estiloCombo%>" obligatorio="false"  elementoSel="<%=provinciaSel %>" accion="Hijo:idPoblacion" />
									<% } %>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoPostal"/>
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="codigoPostal" size="5" maxlength="5" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.poblacion"/>
								</td>
								<td colspan="3">
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoProcuradorForm" property="poblacion" styleClass="boxConsulta" size="30"></html:text>
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
								<td>
									<html:text name="MantenimientoProcuradorForm" property="telefono1" size="30" maxlength="20" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.fax1"/>
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="fax1" size="30" maxlength="20" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.telefono1"/>
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="telefono2" size="30" maxlength="20" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.email"/>
								</td>
								<td>
									<html:text name="MantenimientoProcuradorForm" property="email" size="30" maxlength="100" styleClass="<%=estilo%>"></html:text>
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


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>