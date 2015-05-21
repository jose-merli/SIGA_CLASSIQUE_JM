<!DOCTYPE html>
<html>
<head>
<!-- criteriosClienteFrame.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.lang.*"%>

<!-- JSP -->
<%
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");		

	String tipocampo = (String)request.getAttribute("tipoCampo");
	boolean fecha = (tipocampo!=null && tipocampo.equals(SIGAConstants.TYPE_DATE));

	Vector datosValor = (Vector)request.getAttribute("datosValor");
	Vector datosOperador = (Vector)request.getAttribute("datosOperador");

	String modo = (String)request.getSession().getAttribute("modo");
	request.getSession().removeAttribute("modo");
	if (modo==null)
		modo="modificar";
%>


	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
</head>

<body class="BodyIframe">
	<table align="center" cellspacing="0" border="0" width="100%">
		<tr>
			<td class="labelText" >
				<siga:Idioma key="pys.mantenimientoServicios.literal.operador"/>
			</td>
			<td>
<%
				if (!modo.equalsIgnoreCase("consulta")) {
%>
					<select name = "operador" id="operador"  class = "boxCombo" onchange="cambiar();">
<% 
						if (datosOperador!=null) {
							for (int i=0; i<datosOperador.size(); i++) {
								ConOperacionConsultaBean fila = (ConOperacionConsultaBean)datosOperador.get(i);
								String id = String.valueOf(((Integer)fila.getIdOperacion()).intValue()) + "," + fila.getSimbolo();
								String desc = fila.getDescripcion();							
%>
								<option value="<%=id%>"><%=desc%></option>
<% 
							} // FOR
						} // IF 
%>
					</select>
<% 
				} else { 
%>
					<html:text property="operador" styleClass="boxConsulta" size="10" value="" readOnly="true" />
<%
				}
%>
			</td>
			
			<td class="labelText" width="25%">
				<siga:Idioma key="pys.mantenimientoServicios.literal.valor"/>
				<html:hidden styleId="valorTexto" property="valorFinal" value=""/>
				<html:hidden styleId="valorId" property="valorFinal" value=""/>
			</td>
			<td>							
<%
				if (!modo.equalsIgnoreCase("consulta")) {
%>					
					<div id="divSinVacio">
<% 
						if (datosValor!=null && datosValor.size()>0) { 
%>				
							<select name="valorCombo" id="valorCombo" style="width:220px" class = "boxCombo" onchange="pasarvalor(this.options[this.selectedIndex].text, this.value);">
<%
								for (int i=0; i<datosValor.size(); i++){
									Hashtable fila = (Hashtable)datosValor.get(i);
									String id2 = (String)fila.get("ID");
									String desc2 = (String)fila.get("DESCRIPCION");
%>
									<option value="<%=id2%>"><%=desc2%></option>
<% 
								} 
%>					
							</select>																			
<% 
						} else if (fecha){ 
%>	
							<siga:Fecha styleId="valorFecha" nombreCampo="valor" anchoTextField="16" readOnly="true" postFunction="pasarvalor(this.value, this.value);"/>
<% 							
						} else { 
%>					
							<input type="text" name="valorTexto" id="valorTexto" class="box" width="300px" onchange="pasarvalor(this.value, this.value);"></input>								
<%
						}
%>
					</div>
					<select name="comboVacio" id="comboVacio" class="boxCombo" onchange="pasarvalor(this.options[this.selectedIndex].text, this.value);">
						<option value="1">SI</option>
						<option value="0">NO</option>
					</select>
<%
				} else {
%>
					<html:text property="valor" style="width:600px" styleClass="boxConsulta" value="" readOnly="true" />
<%
				}
%>
			</td>
		</tr>
	</table>
	
	<script language="JavaScript">
		jQuery(document).ready(function () {
			jQuery("#comboVacio").hide();				
			jQuery("#divSinVacio").show();
			if (jQuery("#valorCombo").exists()) {
				pasarvalor(jQuery("#valorCombo").find("option:selected").text(), jQuery("#valorCombo").val());
			}
		});		
	
		function cambiar(){
			var oper_aux= operador.value.split(",");			
			
			if (oper_aux[0]==<%=ClsConstants.ESVACIO_ALFANUMERICO%> || oper_aux[0]==<%=ClsConstants.ESVACIO_NUMERICO%> || oper_aux[0]==<%=ClsConstants.ESVACIO_FECHA%>) {						
					jQuery("#divSinVacio").hide();
					jQuery("#comboVacio").show();
					pasarvalor(jQuery("#comboVacio").find("option:selected").text(), jQuery("#comboVacio").val());

			} else {
				jQuery("#comboVacio").hide();
				jQuery("#divSinVacio").show();							
				if (jQuery("#valorCombo").exists()) {
					pasarvalor(jQuery("#valorCombo").find("option:selected").text(), jQuery("#valorCombo").val());
				}
			}
		}
		
		function pasarvalor(valorTexto, valorId) {
			jQuery("#valorTexto").val(valorTexto);
			jQuery("#valorId").val(valorId);
		}
		</script>	
</body>
</html>