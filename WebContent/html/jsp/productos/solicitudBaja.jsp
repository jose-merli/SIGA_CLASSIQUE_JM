<!-- solicitudBaja.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
	boolean esLetrado=user.isLetrado();
	String idPersona="";
	String nombre="";
	String numero="";
	String nif="";	
	boolean esConsejo=false;
	int idConsejo = new Integer(user.getLocation()).intValue();
	if(idConsejo==2000 || idConsejo>=3000)
		esConsejo=true;
	
	Hashtable htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
		if(htData != null){	
			if(UtilidadesHash.getLong(htData, "idPersona")!=null){
			 idPersona = UtilidadesHash.getLong(htData, "idPersona").toString();
			 }
		}		
	
	nombre=(String)request.getAttribute("nombrePersona");
	numero=(String)request.getAttribute("numero");
	nif=(String)request.getAttribute("nif");		
%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="pys.solicitudBaja.cabecera" 
		localizacion="pys.solicitudCompra.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		              
			function buscarCliente(){		
			 	var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
				if(resultado!= undefined && resultado[0]!=undefined){ 
					if(document.all.solicitudBajaForm.idPersona.value == "") {
								document.all.solicitudBajaForm.idPersona.value = resultado[0];
								document.all.solicitudBajaForm.numeroColegiado.value = resultado[2];
								document.all.solicitudBajaForm.nif.value = resultado[3];
								document.all.solicitudBajaForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	
							 	document.busquedaClientesModalForm.numeroColegiado.value = resultado[2];
							 	document.busquedaClientesModalForm.nif.value = resultado[3];
							 	document.busquedaClientesModalForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	
					}else if(document.all.solicitudBajaForm.idPersona.value != resultado[0]) {							
								document.all.solicitudBajaForm.idPersona.value = resultado[0];
								document.all.solicitudBajaForm.numeroColegiado.value = resultado[2];
								document.all.solicitudBajaForm.nif.value = resultado[3];
								document.all.solicitudBajaForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	
							 	document.busquedaClientesModalForm.numeroColegiado.value = resultado[2];
							 	document.busquedaClientesModalForm.nif.value = resultado[3];
							 	document.busquedaClientesModalForm.nombrePersona.value =resultado[4] + " " + resultado[5] +  " " + resultado[6];	
					}			
								document.all.solicitudBajaForm.modo.value = "buscarArticulos";						
								document.all.solicitudBajaForm.submit();											
				}			 	
			}
		
			function buscarProductosYServicios(){

						
						document.all.solicitudBajaForm.target="resultado";
						document.all.solicitudBajaForm.modo.value = "buscarArticulos";		
						//window.setTimeout ("document.all.solicitudBajaForm.submit();", 5000); 						
						document.all.solicitudBajaForm.submit();											
			}
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body onLoad="ajusteAlto('resultado');">

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<div class="tablaCentralCampos">
 
			<siga:ConjCampos leyenda="pys.solicitudCompra.leyenda.cliente">	
				<table class="tablaCampos" align="center">
				<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
  				<input type="hidden" name="actionModal" value="">
  				<input type="hidden" name="clientes"	value="1">
  				<input type="hidden" name="modo" value="abrirBusquedaModal">
								
							<%
							if (esConsejo){
						 		
						 	%>	<tr>
								
								<td >
								<siga:BusquedaPersona tipo="personas" idPersona="idPersona" postFunction="buscarProductosYServicios"></siga:BusquedaPersona>
								</td>
								</tr>

							<%
							 	}else if (!esLetrado){ 
							%>
								 <tr>
								<td>
								<siga:BusquedaPersona tipo="colegiado" idPersona="idPersona" postFunction="buscarProductosYServicios"></siga:BusquedaPersona>
								</td>
								</tr>
	
							<%
							 	}else{ 
							%>
								<!-- FILA -->
								<tr>	
									<td class="labelText" nowrap><siga:Idioma key="pys.solicitudCompra.literal.nombre"/></td>				
									<td colspan=3>
										<html:text name="busquedaClientesModalForm"  property="nombrePersona" value="<%=nombre%>" size="110" styleClass="boxConsulta" readOnly="true"></html:text></td>
									</td>
									<td>
	<%								if(!esLetrado)	{
	%>
											<html:button property="buscar" onclick="return buscarCliente();" styleClass="button"> 
												<siga:Idioma key="general.boton.search"/> 
										</html:button> 			
	<%}%>
									</td>
								</tr>
								<tr>
									<td class="labelText" nowrap><siga:Idioma key="pys.solicitudCompra.literal.nColegiado"/></td>				
									<td>
										<html:text name="busquedaClientesModalForm" property="numeroColegiado" value="<%=numero%>" size="10" maxlength="20" styleClass="boxConsulta" readOnly="true"></html:text>
									</td>	
									<td class="labelText" nowrap><siga:Idioma key="pys.solicitudCompra.literal.nifcif"/></td>				
									<td>
										<html:text name="busquedaClientesModalForm" property="nif" value="<%=nif%>" size="30" styleClass="boxConsulta" readOnly="true"></html:text></td>
									</td>	
									<td>&nbsp;</td>
								</tr>
							<%
							}
						 	%>
					</html:form>
							</table>
						</siga:ConjCampos>
				<br>
</div>

				<html:form action="/PYS_Bajas.do" method="POST" target="resultado" style="display:none">
					<html:hidden name="solicitudBajaForm" property = "modo" 						value = ""/>
					<html:hidden name="solicitudBajaForm" property = "idPersona" 				value = "<%=idPersona%>"/>	
					<html:hidden name="solicitudBajaForm" property = "numeroColegiado"  value = "<%=numero%>"/>	
					<html:hidden name="solicitudBajaForm" property = "nombrePersona" 		value = "<%=nombre%>"/>
					<html:hidden name="solicitudBajaForm" property = "nif" 							value = "<%=nif%>"/>				
				</html:form>
					
	
		
		<!-- INICIO: IFRAME LISTA RESULTADOS -->
		<iframe align="center" src="<%=app%>/html/jsp/productos/consultaProductosServiciosSolicitados.jsp"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0";					 
						class="frameGeneral">					
		</iframe>
	
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>
