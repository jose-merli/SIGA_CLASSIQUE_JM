<!-- consultaGenerarFacturacion.jsp -->
<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 10-03-2005 - Inicio
-->		
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="com.siga.beans.FacSerieFacturacionBean"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	Vector vDatos = (Vector)request.getSession().getAttribute("DATABACKUP");	
		
	String fInicialProducto; 
	String fFinalProducto;	
	String fInicialServicio;
	String fFinalServicio;
	String fProgramacion;
	String fPrevistaGeneracion;

%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="facturacion.generarFacturacion.literal.cabecera" 
		localizacion="facturacion.generarFacturacion.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		
		function generarFactura(fila) {		
				subicono('iconoboton_generarFactura'+fila);
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
				  if (oculto == null)  { flag = false; }
				  else { datos.value = datos.value + oculto.value + ','; }
				  j++;
				}
				datos.value = datos.value + "%";
				document.generarFacturacionForm.modo.value = "generarFactura";
				var f = document.generarFacturacionForm.name;				
				document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.generarFacturacion.mensaje.generandoFactura';
				
//		   	document.generarFacturacionForm.submit();
		 }
		
		function refrescarLocal() {
			document.location.reload();
		}
		
	</script>

</head>

<body onLoad="validarAncho_tablaDatos();">	
	<table class="tablaTitulo">		
		<!-- Campo obligatorio -->
		<tr>		
			<td class="titulosPeq">
				<siga:Idioma key="facturacion.generarFacturacion.literal.titulo"/>				    
			</td>				
		</tr>
	</table>		

			<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FAC_GenerarFacturacion.do" method="POST" target="submitArea">
			<input type="hidden" name="modo" value="">

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

			
			<!-- INICIO: LISTA DE VALORES -->
			<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->
	
				<siga:TablaCabecerasFijas 
				   	nombre="tablaDatos"
				   	borde="1"
		   			estilo=""
				   	clase="tableTitle"
				  	nombreCol="facturacion.programarFacturacion.literal.conceptosFacturables,facturacion.programarFacturacion.literal.fechaInicioProductos,facturacion.programarFacturacion.literal.fechaInicioServicios,facturacion.programarFacturacion.literal.fechaProgramacion,facturacion.programarFacturacion.literal.fechaPrevistaGeneracion,"
				  	tamanoCol="43,15,15,10,9,10"
		   			alto="100%"
				    modal="M"> 		  
	 	
<%				if(vDatos == null || vDatos.size()==0 ) { %>
		 					<br><br>
		   		 			<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 					<br><br>	 		
<%		
		 				}
		 				else {	 
				 			Enumeration en = vDatos.elements();	
				 			int i=0;  	 				 			
							 											
							while(en.hasMoreElements()){
								Hashtable htData = (Hashtable)en.nextElement();
								if (htData == null) continue;	
								FilaExtElement[] elems=new FilaExtElement[1];
								elems[0]=new FilaExtElement("generarFactura","generarFactura",SIGAConstants.ACCESS_READ); 				
								i++;
								fInicialProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS)));
								fFinalProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS)));	
								fInicialServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS)));
								fFinalServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS)));
								fProgramacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION)));
								fPrevistaGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION)));								
								String idProgramacion=(String)htData.get("IDPROGRAMACION");								
%> 							
							<tr class="listaNonEdit">
									<td>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION)%>'>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDPROGRAMACION)%>'>	
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_3' value='<%=htData.get(FacFacturacionProgramadaBean.C_USUMODIFICACION)%>'>							
										<%=UtilidadesString.mostrarDatoJSP(htData.get(FacSerieFacturacionBean.C_NOMBREABREVIADO))%>  <%=(idProgramacion.equals("1"))?"":"["+idProgramacion+"]"%>
									</td>
									<td><%=fInicialProducto%>-<%=fFinalProducto%></td> 
									<td><%=fInicialServicio%>-<%=fFinalServicio%></td> 
									<td><%=fProgramacion%></td> 
									<td><%=fPrevistaGeneracion%></td> 
									<td>
										<img src="<%=app%>/html/imagenes/bgenerar_off.gif" style="cursor:hand;" alt="<siga:Idioma key="facturacion.generarFacturacion.literal.generarFactura"/>" name="generar_1" border="0" onClick="selectRow(<%=i%>);  generarFactura(<%=i%>); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('generar_1','','<%=app %>/html/imagenes/bgenerar_on.gif',1)">
									</td>
																						
							 </tr>				
<%						}
	 					} // While %>  			
	  		</siga:TablaCabecerasFijas>  			
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
