<!DOCTYPE html>
<html>
<head>
<!-- resultadoBusquedaProductos.jsp -->
<!-- 
	 Muestra los resultados de la busqueda de productos
	 VERSIONES:
	 miguel.villegas 2-02-2005 Adecuacion a los nuevos standares y necesidades
-->

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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
			
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

%>	


<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoProductosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal () 
			{
				parent.buscar();
			}			
		
		</script>

	</head>

	<body class="tablaCentralCampos">
		<html:form action="/PYS_MantenimientoProductos.do" method="post" target="">
			<html:hidden property="modo" styleId="modo" value=""/>				
		</html:form>	
		
			<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="pys.resultadoBusquedaProductos.literal.tipo,pys.resultadoBusquedaProductos.literal.categoria,
			   			  pys.busquedaProductos.literal.producto,pys.resultadoBusquedaProductos.literal.precio,
			   			  pys.resultadoBusquedaProductos.literal.Estado,"
			   columnSizes="20,20,20,10,20,10"
			   fixedHeight="95%"
			   modal="G">
			   
			<%
	    	if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 )
		    {
			%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
			<%
	    	}	    
		    else
		    { %>
	    		<%Enumeration en = ((Vector)request.getAttribute("container")).elements();
				int recordNumber=1;
				while (en.hasMoreElements())
				{
	            	Row row = (Row) en.nextElement(); 
	            	
					//Precio:
            		double precio = 0.00;
					String sPrecio = row.getString("VALOR");
					
					//try { 
						//if (sPrecio!=null)
						//	precio = Double.parseDouble(sPrecio);
					//} catch(NumberFormatException e){
					//	precio = 0.00;
					//}	
	            	%>
	            	
					<siga:FilaConIconos
						  fila='<%=String.valueOf(recordNumber)%>'
						  botones="C,E,B"
						  modo="edicion"
						  clase="listaNonEdit">
						  
						<td align = "left">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(PysProductosInstitucionBean.C_IDINSTITUCION)%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(PysProductosInstitucionBean.C_IDTIPOPRODUCTO)%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(PysProductosInstitucionBean.C_IDPRODUCTO)%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=row.getString(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION)%>">
					  	<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=row.getString(PysProductosInstitucionBean.C_DESCRIPCION)%>">
							<%=UtilidadesString.mostrarDatoJSP(row.getString("TIPO"))%>
						</td>
						<td align = "left">
							<%=UtilidadesString.mostrarDatoJSP(row.getString("CATEGORIA"))%>
						</td>  	
						<td align = "left">
							<%=UtilidadesString.mostrarDatoJSP(row.getString(PysProductosInstitucionBean.C_DESCRIPCION))%>
						</td>  	
						<td align = "right">
							<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(sPrecio))%>&nbsp;&euro;
						</td>  	
						<td align = "left">
							<!-- DCG ini -->
							<% 
								String a = row.getString(PysProductosInstitucionBean.C_FECHABAJA);
								if (a.trim().equals("")){	%> 
									<siga:Idioma key="pys.busquedaProducto.estado.alta"/>
							<%} 
								else {%>
									<siga:Idioma key="pys.busquedaProducto.estado.baja"/>
							<%}	%>
							<!-- DCG fin -->
						</td>  																			
					</siga:FilaConIconos>
					<% recordNumber++;
				} 
			} %>
			</siga:Table>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>