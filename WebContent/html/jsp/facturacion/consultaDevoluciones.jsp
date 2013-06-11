<!-- consultaDevoluciones.jsp -->
<!-- 
	 Muestra las devoluciones asociadas a una institucion
	 VERSIONES:
	 miguel.villegas 21-03-2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacDisqueteDevolucionesBean"%>
<%@ page import="com.siga.beans.CenBancosBean"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	// Datos del cliente a visualizar
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion	
	
	Vector devoluciones=new Vector();

	// Obtencion de la informacon relacionada con el abono
	if (request.getAttribute("container") != null){	
		devoluciones = (Vector)request.getAttribute("container");	
	}

	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar!=null) {
		funcionBuscar = "refrescarLocal()";
	}

%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DevolucionesForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			  titulo="facturacion.consultaDevolucion.cabecera" 
			  localizacion="facturacion.datosDevoluciones.ruta"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	
			<script>

			function download(fila)
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
			  datos.value = ""; 
			  
	          preparaDatos(fila,'tablaDatos', datos);
			   	
			  document.forms[0].target = "submitArea";
			  document.forms[0].modo.value = "download";
			  document.forms[0].submit();
			}
		</script>
	
	</head>

	<body>

<!-- TITULO INFORMATIVO -->
	<table class="tablaTitulo">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.consultaDevolucion.cabecera"/>				    
			</td>				
		</tr>
	</table>				
				<html:form action="/FAC_Devoluciones.do" method="POST" target="mainWorkArea" style="display:none">
					<html:hidden styleId = "modo"  property = "modo"  value = ""/>
					<html:hidden styleId="idInstitucion" property="idInstitucion" value="<%=idInstitucion%>"/>
					<input type="hidden" id="actionModal"  name="actionModal" value="">
				</html:form>

							<siga:Table 
							   name="tablaDatos"
							   border="1"
							   columnNames="facturacion.consultaDevolucion.literal.fecha,facturacion.consultaDevolucion.literal.identificador,
							   			  facturacion.consultaDevolucion.literal.bancoEmisor,facturacion.consultaDevolucion.literal.nFacturas,
							   			  facturacion.consultaDevolucion.literal.comisiones,"
							   columnSizes="14,14,34,14,10,14">
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
					            	FilaExtElement[] elems = new FilaExtElement[1];
					            	elems[0]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);

					  %>
							        
									<siga:FilaConIconos
										  fila='<%=String.valueOf(recordNumber)%>'
										  botones="E"
										  visibleConsulta="no"
										  visibleBorrado="no"
										  elementos="<%=elems%>"
										  modo="edicion"
										  clase="listaNonEdit">
										  
										<td align="center">
											<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=idInstitucion%>">
											<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES)%>">
											<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(FacDisqueteDevolucionesBean.C_NOMBREFICHERO)%>">
											<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(FacDisqueteDevolucionesBean.C_FECHAGENERACION)))%>
										</td>
										<td align="center">
											<%=UtilidadesString.mostrarDatoJSP(row.getString(FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES))%>
										</td>
										<td>
											<%=UtilidadesString.mostrarDatoJSP(row.getString(CenBancosBean.C_NOMBRE))%>
										</td>
										<td align="center">
											<%=UtilidadesString.mostrarDatoJSP(row.getString("FACTURAS"))%>
										</td>
										<td align="center">
											<% if (row.getString("COMISION").equalsIgnoreCase("2")){ %>
												<input type="checkbox" name="comision" value="1" checked disabled>
											<% }else{ %>
												<input type="checkbox" name="comision" value="1" disabled>
											<% } %>
										</td>
									</siga:FilaConIconos>
									<% recordNumber++;%>
								<%	} %>
							<%	} %>
						</siga:Table>

					<table class="botonesDetalle">
						<tr>
							<td class="tdBotones">
								<input type="button" alt="procesarNuevoFichero"  id="deleteButton" onclick="return procesarNuevoFichero();" class="button" value='<siga:Idioma key="facturacion.consultaDevolucion.boton.nuevoFichero"/>'>
							</td>
						</tr>
					</table>
	
	
				<!-- INICIO: SCRIPTS BOTONES -->
				<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
				<script language="JavaScript">

					<!-- Funcion asociada a boton procesarNuevoFichero -->
					function procesarNuevoFichero()
					{
						document.forms[0].modo.value='nuevo';							
						var resultado = ventaModalGeneral("DevolucionesForm","M");
						if (resultado=="MODIFICADO")
						{
							refrescarLocal();
						}
					}
				
					<!-- Funcion asociada a boton buscar -->
					function refrescarLocal()
					{
						document.forms[0].modo.value='abrir';
						document.forms[0].submit();
					}

					<!-- Funcion asociada a boton buscar -->
					function editar(fila)
					{
						var datos;
						datos = document.getElementById('tablaDatosDinamicosD');
						  datos.value = ""; 
						  
						preparaDatos(fila,'tablaDatos', datos);
						
						document.forms[0].target = "mainWorkArea";
						document.forms[0].modo.value='Editar';
						document.forms[0].submit();
					}
				
				</script>
			
				<!-- INICIO: SUBMIT AREA -->
				<!-- Obligatoria en todas las páginas-->
				<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
				<!-- FIN: SUBMIT AREA -->

	</body>
</html>
