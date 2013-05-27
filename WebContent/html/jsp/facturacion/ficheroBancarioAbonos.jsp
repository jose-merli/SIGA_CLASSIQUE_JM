<!-- ficheroBancarioAbonos.jsp -->
<!-- 
	 VERSIONES : 
	 	nuria.rgonzalez 29-03-2005 - Inicio
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
<%@ page import="com.siga.beans.FacDisqueteAbonosBean"%>
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
	String 	importe;
	
	boolean abonosSJCS = false;
	String sjcs = request.getParameter("sjcs");
	if ((sjcs!=null) && (sjcs.equals("1"))){
		abonosSJCS = true;
	}
%>	

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
	<% if(abonosSJCS){ %>
		<siga:Titulo
			titulo="facturacion.ficheroBancarioAbonos.literal.cabecera" 
			localizacion="factSJCS.Pagos.localizacion"/>
	<%}else{ %>
		<siga:Titulo
			titulo="facturacion.ficheroBancarioAbonos.literal.cabecera" 
			localizacion="facturacion.localizacion"/>
	<% }%>
	<!-- FIN: TITULO Y LOCALIZACION -->
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		
		function download(fila) {
				if(!confirm('<siga:Idioma key="facturacion.ficheroBancarioAbonos.literal.confirmarDescargaFichero"/>')) {
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
				document.all.ficheroBancarioAbonosForm.modo.value = "download";
		   	document.ficheroBancarioAbonosForm.submit();
			 }
				
		function generarFichero() {
			if(!confirm('<siga:Idioma key="facturacion.ficheroBancarioAbonos.literal.confirmarFicheroAbonos"/>')) {
					return false;
				}			
			document.all.ficheroBancarioAbonosForm.modo.value = "generarFichero";
			var f = document.all.ficheroBancarioAbonosForm.name;	
			window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.ficheroBancarioAbonos.mensaje.generandoFicheros';
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
			document.ficheroBancarioAbonosForm.modo.value = "informeRemesa";
			ventaModalGeneral(document.ficheroBancarioAbonosForm.name,"P");
		}
		
		function refrescarLocal() {
			document.location.reload();
		}
		
		
		
	</script>

</head>

<body>	
	
	<table class="tablaTitulo">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="facturacion.ficheroBancarioAbonos.literal.cabecera"/>				    
			</td>				
		</tr>
	</table>		

		<html:form action="/FAC_EnvioAbonosABanco.do" method="POST" target="submitArea" style="display:none">		
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = ""/>				
			<% if (abonosSJCS){ %>
				<input type="hidden" name="sjcs" value="<%=sjcs %>">
			<%} else { %>
				<input type="hidden" name="sjcs" value="">
			<%} %>
		</html:form>
		
	
				<siga:Table 
				   	name="tablaDatos"
				   	border="1"
				  	columnNames="facturacion.ficheroBancarioAbonos.literal.fecha,facturacion.ficheroBancarioAbonos.literal.banco,facturacion.ficheroBancarioAbonos.literal.nAbonos,facturacion.ficheroBancarioPagos.literal.importeTotalRemesa,"
				  	columnSizes="10,50,15,15,10"
				    modal="M"> 		  
	 	
<%	
						if(vDatos == null || vDatos.size()<1 ) { 
%>
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
								FilaExtElement[] elems=new FilaExtElement[2];
								elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ); 	
								// JBD INC_CAT_017
								elems[1]=new FilaExtElement("versolicitud", "versolicitud", "Informe remesa", SIGAConstants.ACCESS_READ);
								
								i++;
								
								fecha 	= UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)htData.get(FacDisqueteAbonosBean.C_FECHA)));								
								banco 	= UtilidadesString.mostrarDatoJSP((String)htData.get("BANCO"));
								recibos = UtilidadesString.mostrarDatoJSP((String)htData.get("NUMRECIBOS"));	
								importe = UtilidadesString.mostrarDatoJSP((String)htData.get("IMPORTE"));
%> 							
								<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit">
									<td>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=(String)htData.get(FacDisqueteAbonosBean.C_IDDISQUETEABONO)%>'>
										<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=(String)htData.get(FacDisqueteAbonosBean.C_NOMBREFICHERO)%>'>	
										<%=fecha%>
									</td>
									<td><%=banco%></td> 
									<td><%=recibos%></td>
									<td align="right"><%=UtilidadesNumero.formato(importe)%>&nbsp;&euro;</td> 
								
								</siga:FilaConIconos>
<%						}
	 					} // While %>  			
	  		</siga:Table>  			

				<table class="botonesDetalle">
					<tr>
						<td class="tdBotones">
							<html:button property="abono" onclick="return generarFichero();" styleClass="button"><siga:Idioma key="facturacion.ficheroBancarioAbonos.boton.ficheroAbonos"/>    </html:button>
						</td>	
					</tr>
				</table>
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->		


	
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
