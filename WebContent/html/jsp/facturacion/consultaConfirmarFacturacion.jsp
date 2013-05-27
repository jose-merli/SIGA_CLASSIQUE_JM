<!-- consultaConfirmarFacturacion.jsp -->
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
<%@ page import="com.siga.beans.FacEstadoConfirmFactAdm"%>
<%@ page import="com.siga.beans.FacEstadoConfirmFactBean"%>
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
	String fRealConfirmacion;
	String fPrevistaConfirmacion;
	String sEstadoConfirmacion;
	String sEstadoPDF;
	String sEstadoEnvios;
	String sCheckArchivado;
	String fCargo=null;
	Hashtable htEstados = (Hashtable)request.getAttribute("ESTADOS");
	FacEstadoConfirmFactAdm admEstados = new FacEstadoConfirmFactAdm(usr);
	
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	

	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">

		function refrescarLocal() {
			parent.buscar();
		}		
			
		function programarConfirmacion(fila) 
		{		
			
			var datos;
			var fechaCargoNecesaria = '0';//Si vale '0' no es necesaria.
			
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
		
			//Calculo el valor de la fecha de cargo (numero de 0 a N. Si 0 no es necesario)
			var aux2 = 'oculto' + fila + '_' + 4;
			fechaCargoNecesaria = document.getElementById(aux2).value;
		
			//Para ver si todo va bien:
			var ok = true;
		
			// Voy al Action si me pide la fecha de cargo:
			if (fechaCargoNecesaria != '0') {
				document.all.confirmarFacturacionForm.modo.value = "nuevo";
				var resultado = ventaModalGeneral(document.all.confirmarFacturacionForm.name,"P");

				// Compruebo que si necesito la fecha de cargo la he obtenido de la modal:
				if (resultado==undefined || (resultado!=undefined && resultado=='')) {
						//alert('<siga:Idioma key="censo.consultaComponentesJuridicos.literal.fechaCargo"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
						return false;				
				} else {
					// Almaceno la fecha de Cargo de la modal:
					document.all.confirmarFacturacionForm.fechaCargo.value = resultado;
				}
			} 

			// Si todo ha ido bien y confirmo la facturacion:
			if (ok) {
				document.confirmarFacturacionForm.modo.value = "confirmarFactura";
				var f = document.confirmarFacturacionForm.name;	
				// Abro la ventana de las tuercas:
				//document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.generarFacturacion.mensaje.generandoFactura';
				document.confirmarFacturacionForm.submit();
			} 				
		 }
		
		function archivar(fila) 
		{
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
		
			document.confirmarFacturacionForm.modo.value = "archivarFactura";
			document.confirmarFacturacionForm.submit();
		 }

		function enviar(fila) 
		{	
			sub();
			if(confirm('<siga:Idioma key="facturacion.confirmacionFacturacion.literal.confirmarEnvioFacturas"/>')){
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
				document.confirmarFacturacionForm.modo.value = "enviar";
				document.confirmarFacturacionForm.submit();
			}else{
				fin();
			}
		 }
		
		function download(fila)
		{
			if(confirm('<siga:Idioma key="facturacion.confirmacionFacturacion.literal.confirmarDescargaFactura"/>')) 
			{
					var datos;
					datos = document.getElementById('tablaDatosDinamicosD');
					datos.value = ""; 
					var i, j;
  						var tabla;
  						tabla = document.getElementById('tablaDatos');
    						var flag = true;
    						j = 1;
    						while (flag) 
    						{
      							var aux = 'oculto' + fila + '_' + j;
      							var oculto = document.getElementById(aux);
      							if (oculto == null)  
      							{ 
      								flag = false; 
      							}
     							else 
     							{ 
     								datos.value = datos.value + oculto.value + ','; 
     							}
      							j++;
    						}
    						datos.value = datos.value + "%";
				document.confirmarFacturacionForm.modo.value = "download";
				document.confirmarFacturacionForm.submit();
			}
		}
		
		/*
		function generarFacturaSolo(fila){
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var i, j;
					var tabla;
					tabla = document.getElementById('tablaDatos');
					var flag = true;
					j = 1;
					while (flag) 
					{
							var aux = 'oculto' + fila + '_' + j;
							var oculto = document.getElementById(aux);
							if (oculto == null)  
							{ 
								flag = false; 
							}
							else 
							{ 
								datos.value = datos.value + oculto.value + ','; 
							}
							j++;
					}
					datos.value = datos.value + "%";
					document.confirmarFacturacionForm.modo.value = "generarFacturaSolo";
					document.confirmarFacturacionForm.submit();
		}
		*/
		
		function descargaLog(fila) 
		{
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
		
			document.confirmarFacturacionForm.modo.value = "descargaLog";
			document.confirmarFacturacionForm.submit();
		 }
		 
		 
		function confirmacionInmediata(fila) 
		{
			var mensaje = "<siga:Idioma key="facturacion.confirmarFacturacion.pregunta.confirmacion"/>";
			if(!confirm(mensaje)) {						
				return;
			}
			subicono('iconoboton_confirmacionInmediata'+fila);
			
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
		
			// Abro la ventana de las tuercas:
		//	document.confirmarFacturacionForm.modo.value = "confirmacionInmediata";
		//	var f = document.confirmarFacturacionForm.name;	
		//	window.frames.submitArea.location = '/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=facturacion.confirmarFacturacion.literal.confirmandoFacturacion';
		
			document.confirmarFacturacionForm.modo.value = "confirmarFactura";
			document.all.confirmarFacturacionForm.facturacionRapida.value = "1";
			var f = document.confirmarFacturacionForm.name;	
			// Abro la ventana de las tuercas:
			document.confirmarFacturacionForm.submit();		
		
		
		}

		//Funcion asociada al boton Consultar
				
		
			function consultarfactura(fila) {

				//alert('probando');
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
				
				   document.confirmarFacturacionForm.modo.value = "consultarfactura";
				   ventaModalGeneral(document.confirmarFacturacionForm.name,"M");
				   
			 }			

			

		
					
				
		
	</script>
</head> 

<body class="tablaCentralCampos">	

		<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">		
			<html:hidden name="confirmarFacturacionForm" property="modo" styleId="modo" value = ""/>
			<html:hidden name="confirmarFacturacionForm" property="fechaCargo" styleId="fechaCargo" value = ""/>
			<html:hidden name="confirmarFacturacionForm" property="facturacionRapida" styleId="facturacionRapida" value = ""/>
			<html:hidden name="confirmarFacturacionForm" property="generarEnvios" styleId="generarEnvios" value = ""/>
		</html:form>
		

			<!-- INICIO: LISTA DE VALORES -->
			<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->
	
				<siga:Table 
				   	name="tablaDatos"
				   	border="1"
				  	columnNames="facturacion.confirmarFacturacion.literal.sel,facturacion.confirmarFacturacion.literal.conceptosFacturables,facturacion.programarFacturacion.literal.fechaInicioProductos,facturacion.programarFacturacion.literal.fechaInicioServicios,facturacion.confirmarFacturacion.literal.fechaRealGeneracion,facturacion.confirmarFacturacion.literal.fechaPrevistaConfirmacion,facturacion.confirmarFacturacion.literal.fechaConfirmacion,facturacion.confirmarFacturacion.literal.estadoConfirmacion,facturacion.confirmarFacturacion.literal.estadoPDF,facturacion.confirmarFacturacion.literal.estadoEnvio,"
				  	columnSizes="4,17,9,9,7,7,7,7,7,7,18"
				  	fixedHeight="95%"
				    modal="M">	 	
					<%if(vDatos == null || vDatos.size()<1 ) { %>
		 					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>	
					<%}
		 				else {	 
				 			Enumeration en = vDatos.elements();	
				 			int i=0;  	 				 			
							 											
							while(en.hasMoreElements()){
								Hashtable htData = (Hashtable)en.nextElement();
								if (htData == null) continue;	
								fInicialProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOPRODUCTOS)));
								fFinalProducto = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINPRODUCTOS)));	
								fInicialServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAINICIOSERVICIOS)));
								fFinalServicio = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAFINSERVICIOS)));
								fProgramacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPROGRAMACION)));
								fPrevistaGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTAGENERACION)));								
								fPrevistaConfirmacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAPREVISTACONFIRM)));								
								fRealConfirmacion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHACONFIRMACION)));								
								fRealGeneracion = UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacFacturacionProgramadaBean.C_FECHAREALGENERACION)));
								fCargo =  UtilidadesString.mostrarDatoJSP((String)htData.get("FECHACARGO"));
								if(htEstados!=null){
									sEstadoConfirmacion =  UtilidadesString.mostrarDatoJSP(htEstados.get((String)htData.get("IDESTADOCONFIRMACION")));
									sEstadoPDF =  UtilidadesString.mostrarDatoJSP(htEstados.get((String)htData.get("IDESTADOPDF")));
									sEstadoEnvios =  UtilidadesString.mostrarDatoJSP(htEstados.get((String)htData.get("IDESTADOENVIO")));
								}else{
									sEstadoConfirmacion =  UtilidadesString.mostrarDatoJSP(admEstados.getDescripcion((String)htData.get("IDESTADOCONFIRMACION"),"C",usr.getLanguage()));
									sEstadoPDF =  UtilidadesString.mostrarDatoJSP(admEstados.getDescripcion((String)htData.get("IDESTADOPDF"),"P",usr.getLanguage()));
									sEstadoEnvios =  UtilidadesString.mostrarDatoJSP(admEstados.getDescripcion((String)htData.get("IDESTADOENVIO"),"E",usr.getLanguage()));
								}
								sCheckArchivado =  (String)htData.get("ARCHIVARFACT");
								String idProgramacion=(String)htData.get("IDPROGRAMACION");
								Integer idEstadoConfirmacion = new Integer((String)htData.get("IDESTADOCONFIRMACION"));
								Integer idEstadoPDF = new Integer((String)htData.get("IDESTADOPDF"));

								FilaExtElement[] elems=new FilaExtElement[7];
								elems[0]=new FilaExtElement("consultar","consultarfactura",SIGAConstants.ACCESS_READ);
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_PROGRAMADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_PENDIENTE) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADAERRORES)) {									
									elems[1]=new FilaExtElement("generar",  "confirmacionInmediata", "facturacion.confirmarFacturacion.boton.confirmacionInmediata", SIGAConstants.ACCESS_READ);
									elems[2]=new FilaExtElement("confirmar","programarConfirmacion", "facturacion.confirmarFacturacion.boton.cambiarFechaCargoyConfirmar", SIGAConstants.ACCESS_READ);									
								}
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADAERRORES)) {
									elems[3]=new FilaExtElement("archivar","archivar",SIGAConstants.ACCESS_READ); 				
								}	
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA) || idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADAERRORES)) {
									elems[4]=new FilaExtElement("descargaLog","descargaLog",SIGAConstants.ACCESS_READ); 				
								} 
								if (idEstadoConfirmacion.equals(FacEstadoConfirmFactBean.CONFIRM_FINALIZADA)) {
									elems[5]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ); 				
									elems[6]=new FilaExtElement("enviar","enviar",SIGAConstants.ACCESS_READ); 				
								}

								i++;
								%> 							
								<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit">
								<% if (sCheckArchivado.trim().equals("1")) { %>
									<td><center><input type=checkbox name="sel" value="1" checked disabled></center></td> 
								<% } else { %>
									<td><center><input type=checkbox name="sel" value="1" disabled></center></td> 
								<% } %>
									<td>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION)%>'>
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_2' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(FacFacturacionProgramadaBean.C_IDPROGRAMACION)%>'>	
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_3' name='oculto<%=String.valueOf(i)%>_3' value='<%=htData.get(FacFacturacionProgramadaBean.C_USUMODIFICACION)%>'>							
										<input type='hidden' id='oculto<%=String.valueOf(i)%>_4' name='oculto<%=String.valueOf(i)%>_4' value='<%=fCargo%>'>
										<%=UtilidadesString.mostrarDatoJSP(htData.get(FacSerieFacturacionBean.C_NOMBREABREVIADO))%> <%=(idProgramacion.equals("1"))?"":"["+idProgramacion+"]"%>
									</td>
									<td><%=fInicialProducto%> -<br><%=fFinalProducto%></td> 
									<td><%=fInicialServicio%> -<br><%=fFinalServicio%></td> 
									<td><%=fRealGeneracion%></td> 	
									<td><%=fPrevistaConfirmacion%></td> 	
									<td><%=fRealConfirmacion%></td> 	
									<td><%=sEstadoConfirmacion%></td> 	
									<td><%=sEstadoPDF%></td> 	
									<td><%=sEstadoEnvios%></td> 	
								</siga:FilaConIconos>
						<%}
						} // While %>
	  		</siga:Table>  			
					


	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
