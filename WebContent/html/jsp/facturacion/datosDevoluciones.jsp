<!-- datosDevoluciones.jsp -->
<!-- 
	 Muestra los disquetes de devoluciones
	 VERSIONES:
	 miguel.villegas 04-04-2005
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
<%@ page import="com.siga.beans.FacFacturaIncluidaEnDisqueteBean"%>
<%@ page import="com.siga.beans.FacLineaDevoluDisqBancoBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.beans.CenBancosBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
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

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
	String botonesAccion = "";
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	if (busquedaVolver.equalsIgnoreCase("DEV")) {
		busquedaVolver = "DV";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botonesAccion="V";
	}
	
	request.getSession().setAttribute("CenBusquedaClientesTipo", "DEV_AUTO");
%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		<script type="text/javascript">
			function editarFactura(fila) {
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var j;
				var tabla;
				tabla = document.getElementById('tablaDatos');
				var flag = true;
				j = 1;
				while (flag) {
				  var aux = 'oculto' + fila + '_' + j;
				  var oculto = document.getElementById(aux);
				  if (oculto == null){ 
				  	flag = false; 
				  }
				  else { 
				  	datos.value = datos.value + oculto.value + ','; 
				  }
				  j++;
				}
				datos.value = datos.value + "%"
			    document.forms[0].modo.value = "editarFactura";
			    document.forms[0].submit();
		 	}
		</script>

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
			  titulo="facturacion.datosDevoluciones.cabecera" 
			  localizacion="facturacion.datosDevoluciones.ruta"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>
		
			<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	
			<!-- INICIO: CAMPOS DE BUSQUEDA-->
			<!-- Zona de campos de busqueda o filtro -->
	
			<html:form action="/FAC_Devoluciones.do" method="POST" target="_self"  style="display:none">
					<html:hidden property = "modo" value = ""/>
					<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">

		</html:form>

					
							<siga:TablaCabecerasFijas 
							   nombre="tablaDatos"
							   borde="1"
							   clase="tableTitle"
							   nombreCol="facturacion.datosDevoluciones.literal.fechaFactura,facturacion.datosDevoluciones.literal.nFactura,
							   			  facturacion.datosDevoluciones.literal.nRecibo,facturacion.datosDevoluciones.literal.motivos,
							   			  facturacion.datosDevoluciones.literal.importe,facturacion.datosDevoluciones.literal.gastos,
							   			  facturacion.datosDevoluciones.literal.cargado,"
							   tamanoCol="10,10,14,30,10,10,6,10"
					   			alto="100%"
					   			ajusteBotonera="true"		
							>
							<%
					    	if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 )
						    {
							%>
								<br><br><br>
								<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
							<%
					    	}	    
						    else
						    { %>
					    		<%Enumeration en = ((Vector)request.getAttribute("container")).elements();
								int recordNumber=1;
								while (en.hasMoreElements())
								{
					            	Row row = (Row) en.nextElement();
					            	
									// Control icono solicitud modificacion
									FilaExtElement[] elementos=new FilaExtElement[1];
				  		 			elementos[0]=new FilaExtElement("editarFactura","editarFactura",SIGAConstants.ACCESS_READ);
					            	%>
							        
									<siga:FilaConIconos
										  fila='<%=String.valueOf(recordNumber)%>'
										  botones=""
										  elementos='<%=elementos%>'
										  visibleConsulta="no"
										  visibleEdicion='no'										  
										  visibleBorrado="no"
										  pintarEspacio='no'
										  modo="edicion"
										  clase="listaNonEdit">
										  
										<td align="center">
											<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=idInstitucion%>">
											<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(FacFacturaBean.C_IDFACTURA)%>">
											<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(FacFacturaBean.C_FECHAEMISION)))%>
										</td>
										<td align="center">
											<%=UtilidadesString.mostrarDatoJSP(row.getString(FacFacturaBean.C_NUMEROFACTURA))%>
										</td>
										<td align="center">
											<%=UtilidadesString.mostrarDatoJSP(row.getString(FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO))%>
										</td>
										<td>
											<%=UtilidadesString.mostrarDatoJSP(row.getString(FacLineaDevoluDisqBancoBean.C_DESCRIPCIONMOTIVOS))%>
										</td>
										<td align="center">
											<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(row.getString(FacFacturaIncluidaEnDisqueteBean.C_IMPORTE)))%>&nbsp;&nbsp;&euro;
										</td>
										<td align="center">
											<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(row.getString(FacLineaDevoluDisqBancoBean.C_GASTOSDEVOLUCION)))%>
										</td>
										<td align="center">
											<% if (row.getString(FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE).equalsIgnoreCase(ClsConstants.DB_TRUE) || row.getString(FacLineaDevoluDisqBancoBean.C_CARGARCLIENTE).equalsIgnoreCase("S")){ %>
												<input type="checkbox" name="cargado" value="1" checked disabled>
											<% }else{ %>
												<input type="checkbox" name="cargado" value="1" disabled>
											<% } %>
										</td>
									</siga:FilaConIconos>
									<% recordNumber++;%>
								<%	} %>
							<%	} %>
						</siga:TablaCabecerasFijas>
	
				<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
				<!-- Aqui comienza la zona de botones de acciones -->
		
				<!-- INICIO: BOTONES REGISTRO -->
				<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
					 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
					 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
					 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
					 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
				-->
					<siga:ConjBotonesAccion botones="<%=botonesAccion%>" modo="edicion" clase="botonesDetalle"/>
				<!-- FIN: BOTONES REGISTRO -->

				<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
				<!-- INICIO: SCRIPTS BOTONES -->
				<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
				<script language="JavaScript">
				
				</script>
			
				<!-- INICIO: SUBMIT AREA -->
				<!-- Obligatoria en todas las páginas-->
				<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
				<!-- FIN: SUBMIT AREA -->


	</body>
</html>
