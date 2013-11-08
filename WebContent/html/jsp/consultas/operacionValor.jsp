<!DOCTYPE html>
<html>
<head>
<!-- exitoInsercion.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");		

	String tipocampo = (String)request.getAttribute("tipocampo");
	int max = 10;
	Integer longitud = (Integer)request.getAttribute("longitud");
	if (longitud!=null){
		// +1 para dejar que introduzcan signo
		max = longitud.intValue()+1;		
	}
	Integer escala = (Integer)request.getAttribute("escala");
	if (escala!=null){
		String sEscala = String.valueOf(escala);
		if (!sEscala.equals("0")){
			// +2 para dejar que introduzcan signo y punto decimal
			max = longitud.intValue()+escala.intValue()+2;
		}
	}
	
	boolean fecha = (tipocampo!=null && tipocampo.equals(SIGAConstants.TYPE_DATE))?true:false;
	boolean numerico = (tipocampo!=null && (tipocampo.equals(SIGAConstants.TYPE_NUMERIC)|| tipocampo.equals(SIGAConstants.TYPE_LONG)))?true:false;
	
	if (tipocampo!=null && !tipocampo.equals("")){
		if (tipocampo.equals(SIGAConstants.TYPE_NUMERIC) || tipocampo.equals(SIGAConstants.TYPE_LONG)){
			tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valornumerico");
		}else if (tipocampo.equals(SIGAConstants.TYPE_ALPHANUMERIC)){
			tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
		}else if (tipocampo.equals(SIGAConstants.TYPE_DATE)){
			tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valorfecha");
		}
	}else{
		tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
	}	
	Vector datosValor = (Vector)request.getAttribute("datosValor");
	Vector datosOperacion = (Vector)request.getAttribute("datosOperacion");
	
%>



	
	    <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	</head>

	<body class="BodyIframe">
	<table align="center" >
		<tr>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.operacion"/>
			</td>
			<td>			
				<select name = "operacion" id="operacion"  class = "boxCombo">
					<% if (datosOperacion!=null){
						for (int i=0; i<datosOperacion.size(); i++){
							Row fila = (Row)datosOperacion.elementAt(i);
							String id = fila.getString("ID");
							String desc = fila.getString("DESCRIPCION");				
						%>
						<option value="<%=id%>"><%=desc%></option>
					<%	}
					}%>
				</select>			
			</td>
			<td class="labelText">
				<%=tipocampo%>
			</td>		
			<td>
			<% if (datosValor!=null){%>
				<select name = "valor" id="valor"  class = "boxCombo">
					<%
						for (int i=0; i<datosValor.size(); i++){
							Row fila = (Row)datosValor.elementAt(i);
							String id2 = fila.getString("ID");
							String desc2 = fila.getString("DESCRIPCION");				
						%>
						<option value="<%=id2%>"><%=desc2%></option>
					<%	}%>					
				</select>	
			<%}else{%>		
			<%if (fecha){%>				
				<siga:Fecha nombreCampo="valor"></siga:Fecha>
				<input type="hidden" name="numerico" value="false"></input>
			<%}else if (numerico){%>
				<input type="text" name="valor" class="box" maxlength="<%=max%>"></input>
				<input type="hidden" name="numerico" value="true"></input>
				<input type="hidden" name="enteros" value="<%=longitud%>"></input>
				<input type="hidden" name="decimales" value="<%=escala%>"></input>
			<%}else{%>
				<input type="text" name="valor" class="box" maxlength="<%=max-1%>"></input>	
				<input type="hidden" name="numerico" value="false"></input>			
			<%}%>
			<%}%>
			</td>
		</tr>
	</table>	
			
	</body>
</html>