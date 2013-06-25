<!-- buscarPrevisionesFacturacion.jsp -->
<!-- VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
     Utilizando tags pinta una lista con cabeceras fijas 
     VERSIONES:
	yolanda.garcia 26-01-2005 Creación
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
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Vector vDatosPrev = (Vector)request.getAttribute("datosPrev");
	request.removeAttribute("datosPrev");	
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="facturacion.previsionesFacturacion.literal.titulo" 
			localizacion="facturacion.previsionesFacturacion.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
		<script>
			function download(fila)
			{
				if(confirm('<siga:Idioma key="facturacion.previsionesFacturacion.literal.confirmarDescargaFichero"/>')) 
				{
					preparaDatos(fila, 'tabladatos');
					document.forms[0].modo.value = "download";
					document.forms[0].submit();
				}
			}
			
			function programacion(fila)
			{
				if(confirm('<siga:Idioma key="facturacion.previsionesFacturacion.literal.confirmarProgramacion"/>')) 
				{
					preparaDatos(fila, 'tabladatos');
					document.forms[0].modo.value = "programar";
					document.forms[0].submit();
				}
						
			}
			
			function refrescarLocal()
			{
				parent.buscar();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
		
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FAC_PrevisionesFacturacion.do" method="POST" target="submitArea" style="display:none">
			<html:hidden property="modo" value=""/>
			

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
				<siga:Table 
			   		name="tabladatos"
	   				border="1"
	   				columnNames="facturacion.previsionesFacturacion.literal.fechaEjecuacionPrevision,
	   							facturacion.nuevaPrevisionFacturacion.literal.descripcion,
	   							facturacion.previsionesFacturacion.literal.seriesFacturacion, 
	   							facturacion.previsionesFacturacion.literal.fechaInicioProductos, 
	   							facturacion.previsionesFacturacion.literal.fechaInicioServicios, "
	   				columnSizes="9,27,22,16,16,10">
			<%
			if (vDatosPrev==null || vDatosPrev.size()==0)
			{
			%>
			<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>		
			<%
			}
			else
			{%>
						<!-- INICIO: ZONA DE REGISTROS -->
						<!-- Aqui se iteran los diferentes registros de la lista -->
				
						<%
						for (int i=0; i<vDatosPrev.size(); i++)
			   			{
							Hashtable miHash = (Hashtable)vDatosPrev.elementAt(i);
							
							String idprevision=(String)miHash.get("IDPREVISION");
							String existeProgramacion=(String)miHash.get("EXISTEPROGRAMACION");
							String botones="";
							boolean programada = existeProgramacion.trim().equals("1");
							FilaExtElement[] elems=new FilaExtElement[2];
							elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);
							if (!programada) {
								elems[1]=new FilaExtElement("programacion","programacion",SIGAConstants.ACCESS_READ);
								botones="B";
							} else {
								botones="";
							}
							
							
							%>
							
							<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" elementos='<%=elems%>' visibleConsulta="false" pintarEspacio="false" visibleEdicion="false" clase="listaNonEdit">
								<td>
									<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=miHash.get("IDSERIEFACTURACION")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=miHash.get("IDPREVISION")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=miHash.get("FECHAINICIOPRODUCTOS")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=miHash.get("FECHAFINPRODUCTOS")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=miHash.get("FECHAINICIOSERVICIOS")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=miHash.get("FECHAFINSERVICIOS")%>">
									<%=miHash.get("FECHA_EJECUCION")%>
								</td>
								<td>
		 							<%=miHash.get("DESCRIPCION")%>
									<%=(programada)?"&nbsp;("+UtilidadesString.getMensajeIdioma(usr.getLanguage().toUpperCase(),"messages.facturacion.yaProgramada")+")":""%>
								</td>
								<td>
									<%=miHash.get("NOMBREABREVIADO")%>
								 	<%=(idprevision.equals("1"))?"":"["+idprevision+"]"%>
								</td>
								<td><%=miHash.get("FECHAINICIOPRODUCTOS")%> - <%=miHash.get("FECHAFINPRODUCTOS")%></td>
								<td><%=miHash.get("FECHAINICIOSERVICIOS")%> - <%=miHash.get("FECHAFINSERVICIOS")%></td>
							</siga:FilaConIconos>
						<%}%>
						
						<!-- FIN REGISTRO -->
						<!-- FIN: ZONA DE REGISTROS -->	
			<%}%>
					</siga:Table>
			

		<!-- FIN: LISTA DE VALORES -->
		
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
