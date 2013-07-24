<!DOCTYPE html>
<html>
<head>
<!-- datosMovimientos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoMovimientosForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	

	//Accion de la que venimos:
	String modo = "";
	
	//Calculo los telefonos si la accion no es nuevo:
	Vector resultado = (Vector) request.getAttribute("resultado");
	
	String pagoAsociado="", importeAplicado="";
	
%>	

<!-- JSP -->


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="MantenimientoMovimientosForm" staticJavascript="false" />
 	
	<!--TITULO Y LOCALIZACION -->

	<siga:Titulo 
		titulo="factSJCS.datosMovimientos.cabecera"
		localizacion="factSJCS.datosMovimientos.ruta"/>
		
</head>

<body>

	<!-- INICIO: CAMPOS -->

	<html:form action="/CEN_MantenimientoMovimientos.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "insertar"/>
	<html:hidden property = "actionModal" value = ""/>
	
	</html:form>

			 <div style="position:absolute;top:0px;z-index:3;left:0px;width:350px;height:60px;" >		
			
				<table width="140%"  border="0"  height="60px"  cellpadding="0" cellspacing="0">
				
					<tr>
						<td class = 'labelText'>
						
							<div>
							
								<table id='tabAplicacionesCabeceras'  border='1' width='80%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
								
									<tr class = 'tableTitle'>
									
										<td align='center' width='6%' >
											<siga:Idioma key="factSJCS.datosMovimientos.literal.pago"/>
										</td>
										
										<td align='center' width='6%'>
											<siga:Idioma key="factSJCS.datosMovimientos.literal.cantidad"/>
										</td>
									
									</tr>	
								
								</table>
							
							</div>
							
							<div id="divAplicacion" style='height:60px;position:absolute;width:80%; overflow-y:auto' >
							
								<table id='tablaAplicacion' border='1' align='center'  width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
								
									<%	if (resultado!=null || resultado.size()>0) { 	
									
										for (int cont=1;cont<=resultado.size();cont++) {
											Hashtable fila = (Hashtable) resultado.get(cont-1);

											pagoAsociado = UtilidadesString.mostrarDatoJSP(fila.get("PAGOASOCIADO"));
											importeAplicado = UtilidadesString.mostrarDatoJSP(fila.get("IMPORTEAPLICADO"));
										%>
							  				<siga:FilaConIconos fila="<%=String.valueOf(cont)%>" visibleConsulta="no" 
							  						visibleEdicion = "no" visibleBorrado = "no" botones=""
							  						pintarEspacio="yes" 
							  						clase="listaNonEdit" pintarespacio='no'>	
							  				<tr>								
												<td class = 'labelText'><%=pagoAsociado %></td>
												<td class = 'labelText' align="right"><%=UtilidadesNumero.formatoCampo(importeAplicado)%></td>
											</tr>
											</siga:FilaConIconos>	
							<%		} 
								} // del if 
%>
							
								
								</table>
							
							</div>
						
						</td>
						
					</tr>
				
				</table>
		
		
			</div>								  						



	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->

<!-- FIN: SUBMIT AREA -->

</body>
</html>
