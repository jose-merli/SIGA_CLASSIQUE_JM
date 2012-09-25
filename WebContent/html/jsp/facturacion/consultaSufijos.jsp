<!-- consultaSufijos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-html.tld" 	prefix="html" %>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga"	%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.FacSufijoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%
	String app = request.getContextPath(); 
	Vector Vsufijos = null;
	try {
		Vsufijos = (Vector) request.getAttribute("Vsufijos");
	} catch (Exception e){
		Vsufijos = null;
	}
%>

<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<script>
		<!-- Refrescar -->
		function refrescarLocal() {
			parent.buscar();
		}	
	</script>

</head>

<body>
		<html:form action="/FAC_Sufijos.do" method="POST" target="submitArea">
			<html:hidden name="sufijosForm" styleId="modo"  property="modo" value = ""/>
		</html:form>
		

			<siga:TablaCabecerasFijas 
			   nombre = "tablaResultados"
			   borde  = "1"
			   clase  = "tableTitle"
			   nombreCol="facturacion.sufijos.literal.sufijo,facturacion.datosGenerales.literal.descripcion,"
			   tamanoCol = "30,55,15"
			   alto  = "100%"
			   modal = "p" 
			   activarFilaSel="true" >

		<% if ((Vsufijos != null) && (Vsufijos.size() > 0)){ %>
	
				<%	 for (int i = 1; i <= Vsufijos.size(); i++) { 
							
							 FacSufijoBean sufijoBean = (FacSufijoBean) Vsufijos.get(i-1);
							 if (sufijoBean != null){ 

									Integer idInstitucion = sufijoBean.getIdInstitucion();
									String sufijo = sufijoBean.getSufijo();
									String concepto = sufijoBean.getConcepto();
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones="C,E,B" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=String.valueOf(i)%>_1" value="<%=idInstitucion.toString()%>">
											<input type="hidden" id="oculto<%=String.valueOf(i)%>_2" value="<%=sufijo%>">
											<%=UtilidadesString.mostrarDatoJSP(sufijo)%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(concepto)%></td>
									</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  
			%>
	
	<% } else { %>
			<br><br><br>
				<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
			<br><br>
	<% } %>
			</siga:TablaCabecerasFijas>


<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>