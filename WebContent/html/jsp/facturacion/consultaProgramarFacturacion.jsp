<!-- consultaProgramarFacturacion.jsp -->
<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 07-03-2005 - Inicio
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
<%@ page import="com.siga.beans.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	Vector vDatos = (Vector)request.getSession().getAttribute("DATABACKUP");	
	
	String iconos="";
	String botones="N"; 
	
	String fInicialProducto; 
	String fFinalProducto;	
	String fInicialServicio;
	String fFinalServicio;
	String fProgramacion;
	String fPrevistaGeneracion;
	String fRealGeneracion;
	String fPrevistaConfirmacion;
	String fRealConfirmacion;
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
		titulo="facturacion.programarFacturacion.literal.cabecera" 
		localizacion="facturacion.programarFacturacion.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	
		function accionNuevo() {
			f=document.programarFacturacionForm;			
		  f.modo.value = "nuevo";
  	  var rc = ventaModalGeneral(document.programarFacturacionForm.name, "M");
   	  if (rc != null) {  	  
  	 	 	if (rc == "MODIFICADO") {
  	 	 		refrescarLocal();
  	  	}
  	  }
		}

		function refrescarLocal() {
//			document.location.reload();
			
			document.programarFacturacionForm.modo.value = "buscar";
			document.programarFacturacionForm.submit();
		}
		
	</script>

</head>

<body>	
	
	<!-- TITULO INFORMATIVO -->
	<table class="tablaTitulo">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.programarFacturacion.literal.cabecera"/>				    
			</td>
		</tr>
		
	</table>		
	
		<html:form action="/FAC_ProgramarFacturacion.do" method="POST" style="display:none"> 		
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" styleId = "modo" value = ""/>			
		</html:form>
		
			<!-- INICIO: LISTA DE VALORES -->
			<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->
	
				<siga:Table 
				   	name="tablaDatos"
				   	border="1"
				  	columnNames="facturacion.programarFacturacion.literal.conceptosFacturables,facturacion.seriesFacturacion.literal.descripcion,facturacion.programarFacturacion.literal.fechaInicioProductos,facturacion.programarFacturacion.literal.fechaInicioServicios,facturacion.programarFacturacion.literal.fechaProgramacion,facturacion.programarFacturacion.literal.fechaPrevistaGeneracion,facturacion.programarFacturacion.literal.fechaRealGeneracion,facturacion.seriesFacturacion.literal.fechaPrevistaConfirmacion,facturacion.seriesFacturacion.literal.fechaRealConfirmacion,"
				  	columnSizes="12,9,15,15,8,8,8,7,8,12"
				    modal="G"> 		  
	 	
<%				if(vDatos == null || vDatos.size()<1 ) { %>
		 					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%		
		 				}
		 				else {	 
				 			Enumeration en = vDatos.elements();	
				 			int i=0;  	 				 			
							 											
							while(en.hasMoreElements()){
								Hashtable htData = (Hashtable)en.nextElement();
								if (htData == null) continue;	
								iconos="";						
								i++;
								fInicialProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS)));
								fFinalProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS)));	
								fInicialServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS)));
								fFinalServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS)));
								fProgramacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION)));
								fPrevistaGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION)));
								fPrevistaConfirmacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM)));
								fRealGeneracion = com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAREALGENERACION));
								fRealConfirmacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHACONFIRMACION)));
								String estadoConfirmacion =(String)htData.get(FacFacturacionProgramadaBean.C_IDESTADOCONFIRMACION);
								String idProgramacion=(String)htData.get("IDPROGRAMACION");
								
								// mostrar editar
								if (fRealGeneracion.trim().equals("") || !estadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA.toString())) {
									iconos="C,E";
								}
								// mostrar eliminar
								if (fRealGeneracion.trim().equals("")) {
									if (iconos.equals("")) 
										iconos+="B";
									else
										iconos+=",B";
								}

								fRealGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAREALGENERACION)));
								fRealConfirmacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHACONFIRMACION)));
%> 	
						
								<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=iconos%>' visibleConsulta='false' clase="listaNonEdit" pintarEspacio="no">
									<td>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION)%>'>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDPROGRAMACION)%>'>								
									<%=UtilidadesString.mostrarDatoJSP(htData.get(FacSerieFacturacionBean.C_NOMBREABREVIADO))%> <%=(idProgramacion.equals("1"))?"":"["+idProgramacion+"]"%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(htData.get(FacSerieFacturacionBean.C_DESCRIPCION))%></td>
									<td><%=fInicialProducto%>-<%=fFinalProducto%></td> 
									<td><%=fInicialServicio%>-<%=fFinalServicio%></td> 
									<td><%=fProgramacion%></td> 
									<td><%=fPrevistaGeneracion%></td> 
									<td><%=fRealGeneracion%></td> 								
									<td><%=fPrevistaConfirmacion%></td> 								
									<td><%=fRealConfirmacion%></td> 
																	
								</siga:FilaConIconos>
<%						}
	 					} // While %>  			
	  		</siga:Table>  			
				
			<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/>			
					
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
