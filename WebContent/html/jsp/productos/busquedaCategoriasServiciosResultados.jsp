<!-- busquedaCategoriasServiciosResultados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri	=	"struts-tiles.tld" 			prefix="tiles" 		%>
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri = "struts-html.tld" 			prefix="html" 		%>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"			%>

<%@ page import="com.siga.beans.PysServiciosBean"%>
<%@ page import="com.siga.beans.PysTipoServiciosBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Vector"%>
<%
	String app = request.getContextPath(); 
	Vector servicios = (Vector) request.getAttribute("servicios");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<script>
	<!-- Refrescar -->
	function refrescarLocal(){ 		
		parent.buscar();
	}	
</script>

<body>
		<html:form action="/PYS_MantenimientoCategoriasServicios.do" method="POST" target="submitArea">
			<html:hidden property = "modo"  styleId = "modo"  value = ""/>
		</html:form>	
		
			<siga:Table 
			   name = "tablaResultados"
			   border  = "1"
			   columnNames="pys.mantenimientoCategorias.literal.tipoServicio,pys.busquedaServicios.literal.categoria,"
			   columnSizes = "30,55,15"
			   modal = "p">

		<%if ((servicios != null) && (servicios.size() > 0)){ %>
	
				<%	 for (int i = 1; i <= servicios.size(); i++) { 
							
							 PysServiciosBean servicio = (PysServiciosBean) servicios.get(i-1);
							 if (servicio != null){ 

									Long idServicio = servicio.getIdServicio();
									Integer idTipoServicio = servicio.getIdTipoServicios();
									String descripcionServicio = servicio.getDescripcion();
									String descripcionTipoServicio = ((PysTipoServiciosBean)servicio.getTipoServicio()).getDescripcion();
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones="E,B" visibleConsulta="false" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idServicio%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idTipoServicio%>">
											<%=UtilidadesString.mostrarDatoJSP(descripcionTipoServicio)%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(descripcionServicio)%></td>
									</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  %>
	

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
