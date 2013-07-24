<!DOCTYPE html>
<html>
<head>
<!-- asignarPlantillaCertificado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	

	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String sCertificado=(String)request.getAttribute("certificado");
	String idInstitucion=(String)request.getAttribute("idInstitucion");
	String idProducto=(String)request.getAttribute("idProducto");
	String idTipoProducto=(String)request.getAttribute("idTipoProducto");
	String idProductoInstitucion=(String)request.getAttribute("idProductoInstitucion");
	
	String porDefecto=(String)request.getAttribute("porDefecto");
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">

		<!-- Funcion asociada a boton Finalizar -->
		function accionCerrar()
		{
			window.top.close();
		}

		<!-- Asociada al boton Aceptar -->
		function accionAceptar()
		{
			var aux=idPlantilla.value;
			if(aux=="")
			{
				var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/> <siga:Idioma key="messages.campoObligatorio.error"/>";

				alert (mensaje);

				return false;
			}

			top.cierraConParametros(aux);
		}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<%=sCertificado%>
				</td>
			</tr>
		</table>

		<div id="camposRegistro" class="posicionModalPeque" align="center">
			<table class="tablaCentralCamposPeque" align="center">
				<tr>
					<td>
						<table class="tablaCampos" align="center">
							<tr>
								<td class="labelText">
									<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/>&nbsp(*)
								</td>
								<td>
<%
									String parametro[] = new String[4];

	   						 		parametro[0] = idInstitucion;
	   						 		parametro[1] = idProducto;
	   						 		parametro[2] = idTipoProducto;
	   						 		parametro[3] = idProductoInstitucion;

									ArrayList al = new ArrayList();
									al.add(porDefecto);
%>
									<siga:ComboBD nombre="idPlantilla" tipo="cmbCerPlantillas" clase="boxCombo" obligatorio="true" parametro="<%=parametro%>" elementoSel="<%=al%>"/>
								</td>
							</tr>
		   				</table>
					</td>
				</tr>
			</table>

			<siga:ConjBotonesAccion botones="A,C" modal="P"/>
		</div>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>