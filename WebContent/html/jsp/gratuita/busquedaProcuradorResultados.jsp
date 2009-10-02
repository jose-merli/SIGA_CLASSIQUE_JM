<!-- busquedaProcuradorResultados.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-tiles.tld" 		prefix="tiles"  %>
<%@taglib uri = "struts-bean.tld" 		prefix="bean"	 %>
<%@taglib uri = "struts-html.tld" 		prefix="html"  %>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"	 %>

<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsProcuradorBean"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<%	String app = request.getContextPath();
    HttpSession ses=request.getSession();
    UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	Vector vProcuradores = (Vector) request.getAttribute("vProcuradores");
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
		<html:form action="/JGR_MantenimientoProcuradores.do" method="POST" target="submitArea" style="display:none">
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
			   nombreCol="gratuita.mantenimientoTablasMaestra.literal.defendidoPor,
			              gratuita.mantenimientoTablasMaestra.literal.codProcurador,
						  gratuita.mantenimientoTablasMaestra.literal.apellidos,
			   			  gratuita.mantenimientoTablasMaestra.literal.nombre,
			   			  gratuita.mantenimientoTablasMaestra.literal.direccion,
			   			  gratuita.mantenimientoTablasMaestra.literal.provincia,
			   			  gratuita.mantenimientoTablasMaestra.literal.poblacion,
			   			  gratuita.mantenimientoTablasMaestra.literal.telefono2,
			   			  gratuita.mantenimientoTablasMaestra.literal.fax1,"
			   tamanoCol = "10,7,13,7,10,12,12,8,8,10"
			   alto="100%"
			   modal = "m" 
			   activarFilaSel="true" >

		<%  if ((vProcuradores != null) && (vProcuradores.size() > 0)){ %>
	
				<%	 for (int i = 1; i <= vProcuradores.size(); i++) { 
							
							 Hashtable hashProcurador = (Hashtable) vProcuradores.get(i-1);
							 if (hashProcurador != null){ 
									String idInstitucionProcurador = (String)hashProcurador.get("IDINSTITUCION");
									String nombreInstitucion = (String)hashProcurador.get("INSTITUCION");
									String idProcurador = (String)hashProcurador.get(ScsProcuradorBean.C_IDPROCURADOR);
									String idPoblacion = (String)hashProcurador.get(ScsProcuradorBean.C_IDPOBLACION);
									String idProvincia = (String)hashProcurador.get(ScsProcuradorBean.C_IDPROVINCIA);
									String poblacion = (String)hashProcurador.get("POBLACION");
									String provincia = (String)hashProcurador.get("PROVINCIA");
									String nombre = (String)hashProcurador.get(ScsProcuradorBean.C_NOMBRE);
									String apellido1 = (String)hashProcurador.get(ScsProcuradorBean.C_APELLIDO1);
									String apellido2 = (String)hashProcurador.get(ScsProcuradorBean.C_APELLIDO2);
									String direccion = (String)hashProcurador.get(ScsProcuradorBean.C_DIRECCION);
									String codigoPostal = (String)hashProcurador.get(ScsProcuradorBean.C_CODIGOPOSTAL);
									String telefono1 = (String)hashProcurador.get(ScsProcuradorBean.C_TELEFONO1);
									String telefono2 = (String)hashProcurador.get(ScsProcuradorBean.C_TELEFONO2);
									String fax1 = (String)hashProcurador.get(ScsProcuradorBean.C_FAX1);
									String codProcurador = (String)hashProcurador.get(ScsProcuradorBean.C_CODPROCURADOR);
									String botones=(idInstitucion.equals(idInstitucionProcurador)?"E,C,B":"C");
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones='<%=botones%>' visibleConsulta="false" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idInstitucionProcurador%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idProcurador%>">
											<input type="hidden" id="oculto<%=i%>_3" value="<%=idProvincia%>">
											<input type="hidden" id="oculto<%=i%>_4" value="<%=idPoblacion%>">
											<%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(codProcurador)%></td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(apellido1+" "+apellido2)%>
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