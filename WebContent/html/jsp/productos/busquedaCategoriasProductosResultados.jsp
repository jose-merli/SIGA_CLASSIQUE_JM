<!DOCTYPE html>
<html>
<head>
<!-- busquedaCategoriasProductosResultados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri	=	"struts-tiles.tld" 			prefix="tiles" 		%>
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri = "struts-html.tld" 			prefix="html" 		%>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"			%>

<%@ page import="com.siga.beans.PysProductosBean"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.PysTiposProductosBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="java.util.Vector"%>
<%
	String app = request.getContextPath(); 
	Vector productos = (Vector) request.getAttribute("productos");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<script>
	<!-- Refrescar -->
	function refrescarLocal(){ 		
		parent.buscar();
	}	
</script>

<body>
		<html:form action="/PYS_MantenimientoCategoriasProductos.do" method="POST" target="submitArea">
			<html:hidden property = "modo"  styleId = "modo"  value = ""/>
		</html:form>	
		
		<siga:Table 
			   name = "tablaResultados"
			   border  = "1"
			   columnNames="pys.mantenimientoCategorias.literal.tipoProducto,pys.busquedaProductos.literal.categoria,"
			   columnSizes = "30,55,15"
			   modal = "P">
		<% if ((productos != null) && (productos.size() > 0)){ %>
	
				<%	 for (int i = 1; i <= productos.size(); i++) { 
							
							 PysProductosBean producto = (PysProductosBean) productos.get(i-1);
							 if (producto != null){ 

									Long idProducto = producto.getIdProducto();
									Integer idTipoProducto = producto.getIdTipoProducto();
								String descripcionProducto = producto.getDescripcion();
									String descripcionTipoProducto = ((PysTiposProductosBean)producto.getTipoProducto()).getDescripcion();
									descripcionTipoProducto=UtilidadesMultidioma.getDatoMaestroIdioma(descripcionTipoProducto,usr);
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones="E,B" visibleConsulta="false" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idProducto%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idTipoProducto%>">
											<%=UtilidadesString.mostrarDatoJSP(descripcionTipoProducto)%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(descripcionProducto)%></td>
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
