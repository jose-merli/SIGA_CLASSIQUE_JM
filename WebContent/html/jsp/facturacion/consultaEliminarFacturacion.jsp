<!-- consultaEliminarFacturacion.jsp -->
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
<%@ page import="com.siga.tlds.FilaExtElement"%>

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
	String fRealGeneracion;

%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo
		titulo="facturacion.eliminarFacturacion.literal.cabecera" 
		localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
			
		function refrescarLocal() {
			document.location.reload();
		}

function eliminiarfact(fila) {
   var datos;
    var type = '<siga:Idioma key="messages.deleteConfirmation"/>';
	subicono('iconoboton_eliminiarfact'+fila);
			
   if (confirm(type)){
   	datos = document.getElementById('tablaDatosDinamicosD');
       datos.value = ""; 
   	var i, j;
   	for (i = 0; i < 6; i++) {
      		var tabla;
      		tabla = document.getElementById('tablaDatos');
      		if (i == 0) {
        		var flag = true;
        		j = 1;
        		while (flag) {
          			var aux = 'oculto' + fila + '_' + j;
          			var oculto = document.getElementById(aux);
          			if (oculto == null)  { flag = false; }
          			else { datos.value = datos.value + oculto.value + ','; }
          			j++;
        		}
        		datos.value = datos.value + "%"
      		} else { j = 2; }
      		if ((tabla.rows[fila].cells)[i].innerText == "")
        		datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
      		else
        		datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
   	}
   	var auxTarget = document.forms[0].target;
   	document.forms[0].target="submitArea";
   	document.forms[0].modo.value = "Borrar";
	var f = document.forms[0].name;				
	document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.mensaje.eliminandoFacturacion';
   	//document.forms[0].submit();
   	//document.forms[0].target=auxTarget;
 	}
 }		
	</script>

</head>

<body onLoad="validarAncho_tablaDatos();">	
	<table class="tablaTitulo">		
		<!-- Campo obligatorio -->
		<tr>		
			<td class="titulosPeq">
				<siga:Idioma key="facturacion.eliminarFacturacion.literal.titulo"/>				    
			</td>				
		</tr>
	</table>		

			<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FAC_EliminarFacturacion.do" method="POST" target="submitArea" style="display:none">		
		<input type=hidden name=modo value="">

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
		

	
				<siga:TablaCabecerasFijas 
				   	nombre="tablaDatos"
				   	borde="1"
		   			estilo=""
				   	clase="tableTitle"
				  	nombreCol="facturacion.programarFacturacion.literal.conceptosFacturables,facturacion.programarFacturacion.literal.fechaInicioProductos,facturacion.programarFacturacion.literal.fechaInicioServicios,facturacion.programarFacturacion.literal.fechaProgramacion,facturacion.programarFacturacion.literal.fechaPrevistaGeneracion,facturacion.programarFacturacion.literal.fechaRealGeneracion,"
				  	tamanoCol="32,15,15,9,9,10,10"
		   			alto="100%"
		   			> 		  
	 	
<%				if(vDatos == null || vDatos.size()<1 ) { %>
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
								i++;
								fInicialProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS)));
								fFinalProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS)));	
								fInicialServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS)));
								fFinalServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS)));
								fProgramacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION)));
								fPrevistaGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION)));								
								fRealGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAREALGENERACION)));
								String idProgramacion=(String)htData.get("IDPROGRAMACION");

								FilaExtElement[] elems=new FilaExtElement[1];
								elems[0]=new FilaExtElement("eliminiarfact","eliminiarfact",SIGAConstants.ACCESS_READ);
								
%> 							
								<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones="" elementos='<%=elems%>' pintarEspacio='false' visibleConsulta='false' visibleBorrado='false' visibleEdicion='false' clase="listaNonEdit">
									<td>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION)%>'>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDPROGRAMACION)%>'>	
										<%=UtilidadesString.mostrarDatoJSP(htData.get(FacSerieFacturacionBean.C_NOMBREABREVIADO))%> <%=(idProgramacion.equals("1"))?"":"["+idProgramacion+"]"%>
									</td>
									<td><%=fInicialProducto%> - <%=fFinalProducto%></td> 
									<td><%=fInicialServicio%> - <%=fFinalServicio%></td> 
									<td><%=fProgramacion%></td> 
									<td><%=fPrevistaGeneracion%></td> 
									<td><%=fRealGeneracion%></td> 	
								</siga:FilaConIconos>
<%						}
	 					} // While %>  			
	  		</siga:TablaCabecerasFijas>  			
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
