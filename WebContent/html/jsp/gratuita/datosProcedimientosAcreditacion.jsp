<!DOCTYPE html>
<html>
<head>
<!-- datosProcedimientosAcreditacion.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->

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
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.ScsAcreditacionProcedimientoBean"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();

	String idProcedimiento = (String)request.getAttribute("idProcedimiento");
	Integer idInstitucion  = (Integer)request.getAttribute("idInstitucion");

	String modo = (String)request.getAttribute("modo");
	// Vector datosModificacion = (Vector)request.getAttribute("datosAcreditacion");
	Vector datosModificacion = (Vector) request.getSession().getAttribute("datosAcreditacionOriginal");
	ScsAcreditacionProcedimientoBean bean = null;
	if ((datosModificacion != null) && (datosModificacion.size() > 0)) {
		bean = (ScsAcreditacionProcedimientoBean) datosModificacion.get(0);
	}

%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
			return;
		}	
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			if (document.forms[0].acreditacion.value == "") {
				var mensaje = "<siga:Idioma key="gratuita.procedimientos.acreditacion.literal.acreditacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return;
			}

			porcentaje = document.forms[0].porcentajeTexto.value;
			if (isFinite(porcentaje)) {
				a = parseInt(porcentaje);
				if ((a < 0) || (a > 100)) {
					var mensaje = "<siga:Idioma key="gratuita.procedimientos.acreditacion.porcentaje.error"/>";
					alert (mensaje);
					return;
				}
			}
			else {
				var mensaje = "<siga:Idioma key="gratuita.procedimientos.acreditacion.literal.porcentaje"/> <siga:Idioma key="messages.campoNumerico.error"/>";
				alert (mensaje);
				return;
			}
			
			//Comprobamos si esta checkeado
			if(document.getElementById('nigNumeroProcedimientoCheck').checked){
				document.forms[0].nigNumProcedimiento.value = 1;
			}else{
				document.forms[0].nigNumProcedimiento.value = 0;
			}
	
	
	<% if (modo.equalsIgnoreCase("editar")) { %>
			document.forms[0].idAcreditacion.value = document.forms[0].acreditacion.value;
			document.forms[0].porcentaje.value = porcentaje;
			document.forms[0].modo.value = "modificarAcreditacion";
			document.forms[0].submit();
//			window.top.close();
//			return;
	<% } else { %>
			var datos = new Array();
			datos[0] = 1;
			datos[1] = document.forms[0].acreditacion.value;
			datos[2] = porcentaje;
			datos[3] =  document.forms[0].nigNumProcedimiento.value;
			window.top.returnValue = datos;
			window.top.close();
		<% } %>
		
		}		

	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="gratuita.procedimientos.acreditacion.cabecera"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/JGR_MantenimientoProcedimientos.do" method="POST" target="submitArea">
	
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idAcreditacion" value = ""/>
		<html:hidden property = "porcentaje" value = ""/>
		<html:hidden property = "nigNumProcedimiento" value = ""/>
		
		<table class="tablaCampos" align="center">	
			<tr>
				<td width="25%"></td>
				<td width="65%"></td>
				<td width="10%"></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="gratuita.procedimientos.acreditacion.literal.acreditacion"/>&nbsp;(*)</td>
				<td class="labelTextValor">
				
<%										if (modo.equalsIgnoreCase("editar")) {
									if (bean != null) {
										ArrayList seleccion = new ArrayList ();
										seleccion.add (String.valueOf(bean.getIdAcreditacion()));
										String [] parametroCombo = {String.valueOf(bean.getIdInstitucion()), 
																	String.valueOf(bean.getIdProcedimiento()), 
																	String.valueOf(bean.getIdAcreditacion())};	%>
										<siga:ComboBD nombre="acreditacion" tipo="acreditacionSCJS_2" clase="boxConsulta" elementoSel="<%=seleccion%>" parametro="<%=parametroCombo%>" readonly="true"/>
						<%			}
							} else { 
									String [] parametroCombo = {String.valueOf(idInstitucion), idProcedimiento};	%>												
									<siga:ComboBD nombre="acreditacion" tipo="acreditacionSCJS_1" clase="boxCombo" obligatorio="true" parametro="<%=parametroCombo%>"/>
						<% } %>
				</td>
				<td></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="gratuita.procedimientos.acreditacion.literal.porcentaje"/></td>
				<td class="labelTextValor">
<%										Integer porcentajeValor = new Integer(0);
								if (modo.equalsIgnoreCase("editar")) {
									if (bean != null){
										porcentajeValor = bean.getPorcentaje();
									}
								}
%>
				<input type="text" name="porcentajeTexto" size="10" maxlength="3" value="<%=porcentajeValor.intValue()%>" class="box"> <font class="labelText">%</font></td>
				<td></td>
			</tr>
			<tr>
				<td class="labelText" nowrap><siga:Idioma key="gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento"/></td>
				<td class="labelText">
					<%		Integer checkValor = new Integer(0);
								if (modo.equalsIgnoreCase("editar")) {
									if (bean != null){
										checkValor = bean.getNigNumeroProcedimiento();
										if(checkValor == 1){ %>
											<input type="checkbox" id ="nigNumeroProcedimientoCheck" name="nigNumeroProcedimientoCkeck" checked="checked" />
									<% 	}else{ %>
											<input type="checkbox" id ="nigNumeroProcedimientoCheck" name="nigNumeroProcedimientoCkeck" />
									<% 	}
									}
								}else{%>
									<input type="checkbox" id ="nigNumeroProcedimientoCheck" name="nigNumeroProcedimientoCkeck" />
								<%} %>
				</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	
	</html:form>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='C,Y' modo=''  modal="P"/>

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
