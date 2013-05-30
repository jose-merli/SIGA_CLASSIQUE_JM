<!-- busquedaComisariaResultados.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-bean.tld" 		prefix="bean"	 %>
<%@taglib uri = "struts-html.tld" 		prefix="html"  %>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"	 %>

<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsComisariaBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>

<%	String app = request.getContextPath();
    HttpSession ses=request.getSession();
    UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	Vector vComisarias = (Vector) request.getAttribute("vComisarias");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script>
		<!-- Refrescar -->
		function refrescarLocal(){ 		
			parent.buscar();
		}	
	</script>	
	
</head>

<body>
		<html:form action="/JGR_MantenimientoComisarias.do" method="POST" target="submitArea" style="display:none">
			<html:hidden property = "modo"  styleId = "modo" value = ""/>
		</html:form>	
		
			<siga:Table 
			   name = "tablaResultados"
			   border  = "1"
			   columnNames="administracion.auditoria.institucion,
			              gratuita.mantenimientoTablasMaestra.literal.codigoext,
			   			  gratuita.mantenimientoTablasMaestra.literal.nombre,
			   			  gratuita.mantenimientoTablasMaestra.literal.direccion,
			   			  gratuita.mantenimientoTablasMaestra.literal.poblacion,
			   			  gratuita.mantenimientoTablasMaestra.literal.telefono2,
			   			  gratuita.mantenimientoTablasMaestra.literal.estado,"
			   columnSizes = "10,8,17,17,18,8,12,10"
			   modal = "m">

		<%  if ((vComisarias != null) && (vComisarias.size() > 0)){ %>
	
				<%	 for (int i = 1; i <= vComisarias.size(); i++) { 
							
							 Hashtable hashComisaria = (Hashtable) vComisarias.get(i-1);
							 if (hashComisaria != null){ 
									String idInstitucionProcurador = (String)hashComisaria.get("IDINSTITUCION");
									String nombreInstitucion = (String)hashComisaria.get("INSTITUCION");
									String idComisaria = (String)hashComisaria.get(ScsComisariaBean.C_IDCOMISARIA);
									String codigoext = (String)hashComisaria.get(ScsComisariaBean.C_CODIGOEXT);
									String idPoblacion = (String)hashComisaria.get(ScsComisariaBean.C_IDPOBLACION);
									String idProvincia = (String)hashComisaria.get(ScsComisariaBean.C_IDPROVINCIA);
									String poblacion = (String)hashComisaria.get("POBLACION");
									String provincia = (String)hashComisaria.get("PROVINCIA");
									String nombre = (String)hashComisaria.get(ScsComisariaBean.C_NOMBRE);
									String direccion = (String)hashComisaria.get(ScsComisariaBean.C_DIRECCION);
									String codigoPostal = (String)hashComisaria.get(ScsComisariaBean.C_CODIGOPOSTAL);
									String telefono1 = (String)hashComisaria.get(ScsComisariaBean.C_TELEFONO1);
									String telefono2 = (String)hashComisaria.get(ScsComisariaBean.C_TELEFONO2);
									String fax1 = (String)hashComisaria.get(ScsComisariaBean.C_FAX1);
									String estado = "Alta";
									if (hashComisaria.get(ScsComisariaBean.C_FECHABAJA) != null && !((String)hashComisaria.get(ScsComisariaBean.C_FECHABAJA)).equals("")){
										estado="Baja desde: "+GstDate.getFormatedDateShort("", (String)hashComisaria.get(ScsComisariaBean.C_FECHABAJA)); 
									}
									String botones=(idInstitucion.equals(idInstitucionProcurador)?"E,C,B":"C");
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones='<%=botones%>' visibleConsulta="false" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idInstitucionProcurador%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idComisaria%>">
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
									<td><%=UtilidadesString.mostrarDatoJSP(poblacion) +"("+UtilidadesString.mostrarDatoJSP(provincia)+")"%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(telefono1)%></td>
									<td align="center"><%=estado%></td>
									</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  
			%>
	
	<% } else { %>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<% } %>
			</siga:Table>

		
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>