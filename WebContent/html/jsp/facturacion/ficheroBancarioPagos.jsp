<!-- ficheroBancarioPagos.jsp -->
<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 18-03-2005 - Inicio
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
<%@ page import="com.siga.beans.FacDisqueteCargosBean"%>
<%@ page import="com.siga.beans.FacSerieFacturacionBean"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	Vector vDatos = (Vector)request.getSession().getAttribute("DATABACKUP");	
		
	String 	fecha; 
	String 	banco;	
	String 	recibos;
	String 	origen;
	String 	descripcion;
	String 	totalRemesa;
	
	String tieneFechaCargo = request.getAttribute("TIENEFECHACARGO")==null?"NO":(String)request.getAttribute("TIENEFECHACARGO");
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo
		titulo="facturacion.ficheroBancarioPagos.literal.cabecera" 
		localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		
		function download(fila) {		
				if (!confirm('<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.confirmarDescargaFichero"/>')) {
					return false;
				}
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
				document.all.ficheroBancarioPagosForm.modo.value = "download";
		   		document.ficheroBancarioPagosForm.submit();
		 }
				
		function generarFichero() {	
		    sub();	
			var ok = true;
			var aviso = false;
			var fechaCargo = "<%=tieneFechaCargo%>"

			//Si debo ir a la modal para obtener la fecha de cargo:
			if (fechaCargo == 'SI') {
				//Abro la modal para calcular la fecha de cargo:
				var resultado = ventaModalGeneral(document.all.confirmarFacturacionForm.name,"P");

				// Compruebo que si necesito la fecha de cargo la he obtenido de la modal:
				
				if (resultado==undefined || (resultado!=undefined && resultado=='')) {
				
				fin(document);
						//alert('<siga:Idioma key="censo.consultaComponentesJuridicos.literal.fechaCargo"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
						ok = false;		
								
				} else
					// Almaceno la fecha de Cargo de la modal:
					
					document.all.ficheroBancarioPagosForm.fechaCargo.value = resultado;
			}else{
			 fin();
			} 

			// Si todo ha ido bien y Acepto genero el fichero:
			if (ok && confirm('<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.confirmarFicheroRenegociaciones"/>')) {
			
					document.all.ficheroBancarioPagosForm.modo.value = "generarFichero";
					var f = document.all.ficheroBancarioPagosForm.name;	
			      	document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.ficheroBancarioPagos.mensaje.generandoFicheros';
			}else{
		
			 fin();
			} 
		}

		// Informe remesa
		function versolicitud(fila) 
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
			document.ficheroBancarioPagosForm.modo.value = "informeRemesa";
			ventaModalGeneral(document.ficheroBancarioPagosForm.name,"P");
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
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.cabecera"/>				    
			</td>				
		</tr>
	</table>		


		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FAC_DisqueteCargos.do" method="POST" target="submitArea" style="display:none">		
			<html:hidden property = "modo" value = ""/>
			<html:hidden name="ficheroBancarioPagosForm" property="fechaCargo" value = ""/>

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
				  	nombreCol= "facturacion.ficheroBancarioPagos.literal.fecha,
				  				facturacion.ficheroBancarioPagos.literal.descripcion,
				  				facturacion.ficheroBancarioPagos.literal.banco,
				  				facturacion.ficheroBancarioPagos.literal.nRecibos,
				  				facturacion.ficheroBancarioPagos.literal.Origen,
				  				facturacion.ficheroBancarioPagos.literal.importeTotalRemesa,"
				  				
				  	tamanoCol="9,19,23,8,23,10,10"
		   			alto="100%"
		   			ajusteBotonera="true"		

				    modal="M"> 		  
	 	
<%	
						if(vDatos == null || vDatos.size()<1 ) { 
%>
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
								FilaExtElement[] elems=new FilaExtElement[2];
								elems[0]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ); 	
								elems[1]=new FilaExtElement("versolicitud", "versolicitud", "Informe remesa", SIGAConstants.ACCESS_READ);
											
								i++;
								
								fecha 	= UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacDisqueteCargosBean.C_FECHACREACION)));								
								banco 	= UtilidadesString.mostrarDatoJSP((String)htData.get("BANCO"));
								recibos = UtilidadesString.mostrarDatoJSP((String)htData.get("NUMRECIBOS"));	
								//origen = (String)htData.get(FacSerieFacturacionBean.C_NOMBREABREVIADO);
								origen = (String)htData.get("NOMBREABREVIADO");	
								
								descripcion = UtilidadesString.mostrarDatoJSP((String)htData.get("DESCRIPCION_PROGRAMACION"));	
								totalRemesa = UtilidadesString.mostrarDatoJSP((String)htData.get("TOTAL_REMESA"));	
			
%> 							
								<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit">
									<td>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=(String)htData.get(FacDisqueteCargosBean.C_IDDISQUETECARGOS)%>'>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=(String)htData.get(FacDisqueteCargosBean.C_NOMBREFICHERO)%>'>											
										<%=fecha%>
									</td>
									<td><%=descripcion%></td> 
									<td><%=banco%></td> 
									<td><%=recibos%></td> 
									<td>
									
									<%	if(origen == null || origen.equals("")){  %>
											&nbsp;<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.renegociacion"/>
									<%	}else{
											UtilidadesString.mostrarDatoJSP(origen);
									%>	
											&nbsp;<siga:Idioma key="facturacion.ficheroBancarioPagos.literal.facturacion"/>
									<%	} %>
											
										<%=origen%>
									</td>

									<td align="right"><%=UtilidadesNumero.formato(totalRemesa)%></td> 
									
								</siga:FilaConIconos>
<%						}
	 					} // While %>  			
	  		</siga:TablaCabecerasFijas>  			
		
			<table class="botonesDetalle" align="center" border="0">
				<tr>
					<td class="tdBotones">					
						<html:button property="renegociaciones" property="idButton" onclick="return generarFichero();" styleClass="button" ><siga:Idioma key="facturacion.ficheroBancarioPagos.boton.renegociacion"/>    </html:button>
					</td>	
				</tr>
			</table>
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->		



		<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">		
			<html:hidden name="confirmarFacturacionForm" property="modo" value = "nuevo"/>
			<html:hidden name="confirmarFacturacionForm" property="actionModal" value = ""/>
  	    </html:form>

	
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>