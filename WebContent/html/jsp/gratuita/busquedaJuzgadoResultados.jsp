<!-- busquedaJuzgadoResultados.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-bean.tld" 		prefix="bean"	 %>
<%@taglib uri = "struts-html.tld" 		prefix="html"  %>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"	 %>

<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsJuzgadoBean"%>
<%@ page import="com.atos.utils.UsrBean"%>

<%	String app = request.getContextPath();
    HttpSession ses=request.getSession();
    UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	Vector vJuzgados = (Vector) request.getAttribute("vJuzgados");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<script>
		<!-- Refrescar -->
		function refrescarLocal(){ 		
			parent.buscar();
		}	
	</script>	
	
</head>

<body>
		<html:form action="/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea" style="display:none">
			<html:hidden property = "modo" value = ""/>
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
			<siga:TablaCabecerasFijas 
			   nombre = "tablaResultados"
			   borde  = "1"
			   clase  = "tableTitle"
			   nombreCol="administracion.auditoria.institucion,
						  gratuita.mantenimientoTablasMaestra.literal.codigoext,
			   			  gratuita.mantenimientoTablasMaestra.literal.nombre,
			   			  gratuita.mantenimientoTablasMaestra.literal.direccion,
			   			  gratuita.mantenimientoTablasMaestra.literal.provincia,
			   			  gratuita.mantenimientoTablasMaestra.literal.poblacion,
			   			  gratuita.mantenimientoTablasMaestra.literal.telefono2,
			   			  gratuita.mantenimientoTablasMaestra.literal.fax1,"
			   tamanoCol = "10,8,17,15,12,12,8,8,10"
			   alto="100%"
			   modal = "G" 
			   activarFilaSel="true">

		<%  if ((vJuzgados != null) && (vJuzgados.size() > 0)){ %>
	
				<%	 for (int i = 1; i <= vJuzgados.size(); i++) { 
							
							 Hashtable hashPrision = (Hashtable) vJuzgados.get(i-1);
							 if (hashPrision != null){ 
									String idInstitucionJuzgado = (String)hashPrision.get("IDINSTITUCION");
									String nombreInstitucion = (String)hashPrision.get("INSTITUCION");
									String idJuzgado = (String)hashPrision.get(ScsJuzgadoBean.C_IDJUZGADO);
									String codigoext = (String)hashPrision.get(ScsJuzgadoBean.C_CODIGOEXT);
									String idPoblacion = (String)hashPrision.get(ScsJuzgadoBean.C_IDPOBLACION);
									String idProvincia = (String)hashPrision.get(ScsJuzgadoBean.C_IDPROVINCIA);
									String poblacion = (String)hashPrision.get("POBLACION");
									String provincia = (String)hashPrision.get("PROVINCIA");
									String nombre = (String)hashPrision.get(ScsJuzgadoBean.C_NOMBRE);
									String direccion = (String)hashPrision.get(ScsJuzgadoBean.C_DIRECCION);
									String codigoPostal = (String)hashPrision.get(ScsJuzgadoBean.C_CODIGOPOSTAL);
									String telefono1 = (String)hashPrision.get(ScsJuzgadoBean.C_TELEFONO1);
									String telefono2 = (String)hashPrision.get(ScsJuzgadoBean.C_TELEFONO2);
									String fax1 = (String)hashPrision.get(ScsJuzgadoBean.C_FAX1);
									String botones=(idInstitucion.equals(idInstitucionJuzgado)?"E,C,B":"C");
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones='<%=botones%>' visibleConsulta="false" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idInstitucionJuzgado%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idJuzgado%>">
											<input type="hidden" id="oculto<%=i%>_3" value="<%=idProvincia%>">
											<input type="hidden" id="oculto<%=i%>_4" value="<%=idPoblacion%>">
											<%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(codigoext)%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(nombre)%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(direccion)%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(provincia)%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(poblacion)%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(telefono1)%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(fax1)%></td>
									</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  
			%>
	
	<% } else { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>

	<% } %>
			</siga:TablaCabecerasFijas>

		
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>