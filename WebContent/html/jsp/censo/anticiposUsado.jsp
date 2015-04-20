<!DOCTYPE html>
<html>
<head>
<!-- anticiposUsado.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.PysAnticipoLetradoBean"%>
<%@ page import="com.siga.beans.PysLineaAnticipoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<%  
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
	
	//recogemos los datos
	Hashtable resultado = (Hashtable)request.getAttribute("resultado");
	String descripcion="", importeAnticipado="", fecha="", importeUsado="";
	try{
		descripcion = (String)resultado.get(PysAnticipoLetradoBean.C_DESCRIPCION);
		importeAnticipado = (String)resultado.get(PysAnticipoLetradoBean.C_IMPORTEINICIAL);
		importeUsado = (String)resultado.get("IMPORTEUSADO");
		fecha = GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(PysAnticipoLetradoBean.C_FECHA));
	}catch(Exception e){
	}
%>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	
	<html:javascript formName="AnticiposClienteForm" staticJavascript="false"/>
	
	<script language="JavaScript">
		// Asociada al boton Cerrar
		function accionCerrar() {		
			window.parent.close();
		}		
	</script>	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.anticipos.titulos.facturasConAnticipo"/>
			</td>
		</tr>
	</table>
	
	<table class="tablaCentralCamposMedia" cellspacing=0 cellpadding=0 align="center" border="0">
		<tr>				
			<td>
				<siga:ConjCampos leyenda="censo.anticipos.leyenda">
					<table class="tablaCampos" align="center" border="0" width="100%">
						<tr>				
							<td class="labelText"><siga:Idioma key="censo.anticipos.literal.descripcion"/></td>				
							<td colspan="3">
								<html:text name="AnticiposClienteForm" property="descripcion" size="70" maxlength="200" styleClass="boxConsulta" readonly="true" value="<%=descripcion%>"/>
							</td>
						</tr>

						<tr>				
							<td class="labelText"><siga:Idioma key="censo.anticipos.literal.fecha"/></td>				
							<td colspan="3">
								<html:text name="AnticiposClienteForm" property="fecha" size="12" maxlength="10" styleClass="boxConsulta" readonly="true" value="<%=fecha%>"/>
							</td>
						</tr>

						<tr>
							<td class="labelText"><siga:Idioma key="censo.anticipos.literal.importeAnticipado"/></td>								
							<td align="right"><%=UtilidadesString.formatoImporte(importeAnticipado)%>&nbsp;&euro;</td>

							<td class="labelText"><siga:Idioma key="censo.anticipos.literal.importeUsado"/></td>
							<td align="right"><%=UtilidadesString.formatoImporte(importeUsado)%>&nbsp;&euro;</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</td> 
		</tr>		
	</table>
	
	<siga:ConjBotonesAccion botones="C" clase="botonesSeguido" titulo="censo.anticipos.titulos.facturas"/>

	<siga:Table 
		name="tablaResultados"
		border="1"
		columnNames="censo.anticipos.literal.numeroFactura,censo.anticipos.literal.importeUsado,censo.anticipos.literal.fechaFactura"
		columnSizes="60,20,20"
		modal="M">
<% 
		Vector<Hashtable> vLineasAnticipo = (Vector<Hashtable>) request.getAttribute("lineasAnticipo");
		if (vLineasAnticipo!=null && vLineasAnticipo.size()>0) {
			for (int i=0; i<vLineasAnticipo.size(); i++) { 
				Hashtable hash = (Hashtable)vLineasAnticipo.get(i);
				if (hash != null) {
					String importeFactura = UtilidadesHash.getString(hash, PysLineaAnticipoBean.C_IMPORTEANTICIPADO);
					String numeroFactura =  UtilidadesHash.getString(hash, "NUMEROFACTURA");
					String fechaEmision =  GstDate.getFormatedDateShort(usr.getLanguage(),UtilidadesHash.getString(hash, "FECHAEMISION"));
%>
					<tr class="listaNonEdit">
						<td><%=UtilidadesString.mostrarDatoJSP(numeroFactura)%></td>
						<td align="right"><%=UtilidadesString.formatoImporte(importeFactura)%>&nbsp;&euro;</td>
						<td><%=UtilidadesString.mostrarDatoJSP(fechaEmision)%></td>
					</tr>
<%
				}
			}
		} else { // No hay registros 
%>
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<% 
		} 
%>
	</siga:Table>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>