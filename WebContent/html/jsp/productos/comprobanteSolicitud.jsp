<!-- comprobanteSolicitud.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-store">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import = "com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import = "com.siga.beans.PysServiciosInstitucionBean"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>
<%@ page import = "java.util.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usrbean=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String cer = (String)request.getAttribute("CERTIFICADO_noFactura");
	
	boolean cer_nofactura  = (cer!=null);
	
		
	//Variable que me indica si vengo de un error. En tal caso muestro el error.
	String error  = request.getAttribute("error")==null?"NO":(String)request.getAttribute("error");

	//Si viene del action mostrarCompra su valor será "mostrarCompra" y sirve para 
	//no mostrar el mensaje de "compra realizada" que se muestra cuando se viene 
	//de los actions finalizarCompra y OkCompraTPV
	String mostrarCompra  = "S".equals(request.getAttribute("mostrarCompra"))?"S":"N";

	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	int tarjeta = ClsConstants.TIPO_FORMAPAGO_TARJETA;
	float varIvaTotal = 0;
	double varPrecioTotal = 0;
	
	//Datos del Action:
	Hashtable htData = new Hashtable();
	Vector vProductos = new Vector();
	Vector vServicios = new Vector();
	String idPeticion="", idPersona="", nombrePersona="", numero="", nif="", fecha="";
	//Si no hay error recupero los datos del request:
	if (error.equals("NO")) {
		htData = (Hashtable)request.getAttribute("resultados");
		vProductos = (Vector)htData.get("vProductos");
		vServicios = (Vector)htData.get("vServicios");
		idPeticion = String.valueOf((Long)htData.get("idPeticion"));
		idPersona=String.valueOf((Long)htData.get("idPersona"));
		nombrePersona=(String)htData.get("nombrePersona");
		numero=(String)htData.get("numeroColegiado");
		nif=(String)htData.get("nif");	
		fecha = (String)htData.get("fecha"); 	
	}
	
	Boolean noFact = (Boolean)request.getAttribute("noFacturable");
	if(noFact == null){
		noFact = Boolean.FALSE;
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
	<siga:Titulo 
		titulo="pys.solicitudCompra.cabecera" 
		localizacion="pys.solicitudCompra.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	
	<script language="JavaScript">		
		var error = '<%=error%>';
		
 		function resultado(){
 			if (error == 'NO') {
	 			f = document.solicitudCompraForm; 
	 			if ("S" != "<%=mostrarCompra%>"){
	 			  if (f.modo.value==""){
	 				var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.compraRealizada"/>";						
					alert(mensaje);	
					f.modo.value="Imprimir";
					validarAncho_cabecera();
				  }
	 			}
			}
 		}
 		
 		function accionImprimirApaisado(){
			document.solicitudCompraForm.modo.value = "imprimirCompra";
		 	var resultado = ventaModalGeneralScrollAuto("solicitudCompraForm","G");			
		 	//document.solicitudCompraForm.submit();
 		}
 		
 		function accionFacturacionRapida(){
 			sub();
			document.solicitudCompraForm.modo.value = "facturacionRapidaCompra";
		 	document.solicitudCompraForm.target="submitArea";
		 	tableCabecera = document.getElementById("cabecera");
		 	filas = tableCabecera.rows.length;
		 	numColumnas = tableCabecera.rows[0].cells.length - 1;
		 	
		 	for (a = 1; a < filas ; a++) {
		 		tableCabecera.rows[a].cells[numColumnas].innerHTML = "";
		 	}

			var divAsistencias = document.getElementById("divAsistencias");
			if(divAsistencias) 
				 divAsistencias.innerHTML="";
		 	
		 	document.solicitudCompraForm.submit();
		 	window.setTimeout("fin()",5000,"Javascript");
 		}
 		
 		function anticiparImporte (fila) {
 			var aux = 'oculto' + fila + '_idTipoClave'; 
 	 		var idTipoClave = document.getElementById(aux).value;

 	 		var aux = 'oculto' + fila + '_idArticulo'; 
 	 		var idClave = document.getElementById(aux).value;

 	 		var aux = 'oculto' + fila + '_idArticuloInstitucion'; 
 	 		var idClaveInstitucion = document.getElementById(aux).value;

 	 		var aux = 'oculto' + fila + '_idPeticion'; 
 	 		var idPeticion = document.getElementById(aux).value;

 	 		var tipo = document.getElementById('oculto' + fila + '_tipo').value;
	
 	 		//actualizar los datos del formulario de mostrar anticipos
 	 		document.getElementById("MostrarAnticiposForm").idTipoClave.value = idTipoClave;
 	 		document.getElementById("MostrarAnticiposForm").idClave.value = idClave;
 	 		document.getElementById("MostrarAnticiposForm").idClaveInstitucion.value = idClaveInstitucion;
 	 		document.getElementById("MostrarAnticiposForm").idPeticion.value = idPeticion;
 	 		document.getElementById("MostrarAnticiposForm").tipo.value = tipo;

 	 		var resultado = ventaModalGeneral("MostrarAnticiposForm","P");		
 	 		if (resultado == "MODIFICADO"){
 	 			refrescarLocal();
 	 		}
 	   	}
 		
 		function refrescarLocal(){
			document.solicitudCompraForm.modo.value = "mostrarCompra";
		 	document.solicitudCompraForm.target="mainWorkArea";			
		 	document.solicitudCompraForm.submit();
 		}
 		
 		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body onLoad="resultado();">
<%  //INICIO DE LA PARTE DE ERROR O KO
	if (!error.equals("NO")) { %>
<div id="titulo" style="width:100%; z-index: 5">
	<table align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="labelText">
				<siga:Idioma key="messages.solicitudCompra.errorCompra"/>
			</td>
		</tr>
	</table>
</div>

<%  //FIN DE LA PARTE DE ERROR O KO
	} else { 
	//INICIO DE LA PARTE DEL COMPROBANTE OK
%>

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="pys.solicitudCompra.titulo2"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;
			    <%if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<%} %>			
			</td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="_blank" type=""> 
	<input type="hidden" name="modo" value=""> 
	<input type="hidden" name="actionModal" value=""> 

				<siga:ConjCampos leyenda="pys.solicitudCompra.literal.datosSolicitud">	
					<table class="tablaCampos" align="center">	
						<!-- FILA -->
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.idPeticion"/></td>				
							<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(idPeticion)%></td>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.fechaSolicitud"/></td>				
							<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(fecha)%></td>	
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.tipoSolicitud"/></td>				
							<td class="labelTextValue"><siga:Idioma key="pys.tipoPeticion.alta"/></td>
						</tr>
						<!-- FILA -->
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nombreSolicitante"/></td>				
							<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(nombrePersona)%></td>	
						</tr>
					</table>
				</siga:ConjCampos>

				<siga:Table 
		  				name="cabecera"
		  				border="2"
		  				columnNames="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.formaPago,pys.solicitudCompra.literal.nCuenta,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.iva,pys.solicitudCompra.literal.estadoPago,pys.solicitudCompra.literal.importeAnticipado,"  
		   				columnSizes="20,15,17,8,8,6,10,11,5">

				
<% 				
				int i = -1;
				if(vProductos != null && vProductos.size()>0 ) 
				{
 						Enumeration en = vProductos.elements();

						while(en.hasMoreElements()){
							i++;
							Hashtable hash = (Hashtable)en.nextElement();  
							String cuenta=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_CUENTA"));
														
							double precio = UtilidadesHash.getDouble(hash, PysProductosSolicitadosBean.C_VALOR).doubleValue();
							
							//float iva = UtilidadesHash.getFloat(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA).floatValue();
							float iva = UtilidadesHash.getFloat(hash,"VALORIVA").floatValue();
							
							
							int cantidad = UtilidadesHash.getInteger(hash, PysProductosSolicitadosBean.C_CANTIDAD).intValue();

							if((UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO))!=null){
								varIvaTotal = varIvaTotal +  (cantidad * ((float)(precio / 100)) * iva);
								varPrecioTotal = varPrecioTotal + (cantidad * (precio * (1 + (iva / 100))));
							}

							//recupera el flag para mostrar/ocultar el botón de anticipar y el importe anticipado
							boolean anticipar = UtilidadesHash.getBoolean(hash, "ANTICIPAR").booleanValue();
							Double aux = UtilidadesHash.getDouble(hash, "IMPORTEANTICIPADO");
							double importeAnticipado = aux != null ? aux.doubleValue() : 0.0;
							FilaExtElement[] elementos=new FilaExtElement[1];
							if(anticipar){
								elementos[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", SIGAConstants.ACCESS_FULL);
							}
							else{
								elementos = null;
							}
				%>
					    <input type="hidden" name="oculto<%=i+1%>_idArticulo" value="<%=UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPRODUCTO)%>">
					    <input type="hidden" name="oculto<%=i+1%>_idArticuloInstitucion" value="<%=UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION)%>">
					    <input type="hidden" name="oculto<%=i+1%>_idPeticion" value="<%=UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPETICION)%>">
					    <input type="hidden" name="oculto<%=i+1%>_idTipoClave" value="<%=UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO)%>">
					    <input type="hidden" name="oculto<%=i+1%>_tipo" value="P">

						<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" clase="listaNonEdit" elementos='<%=elementos%>' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>
							
							<td>
			  					<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO"))%>						  								
			  				</td>
			  				<td>
									<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_FORMAPAGO"))%> 
			  				</td>
			  				<td>
			  					<%=cuenta%>
			  				</td>
			  				<td align="right">
			  					<%=cantidad%>
			  				</td>
			  				<td align="right">
			  					<%=UtilidadesNumero.formatoCampo(precio)%>&nbsp;&euro;
			  				</td>
			  				<td align="right">
			  					<%=UtilidadesNumero.formatoCampo(iva)%>&nbsp;% 
			  				</td>
			  				<td>
	
							<%try{
								int estado = Integer.parseInt(UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO));%> 
								<%if(estado==tarjeta) { %>
										<siga:Idioma key="pys.estadoPago.pagado"/>
								<%} else { %>
										<siga:Idioma key="pys.estadoPago.pendiente"/>
								<%} %>
							<%}catch(Exception e){%>
								<siga:Idioma key="pys.estadoPago.noFacturable"/>	
							<%}%>
			  				</td>
			  				<td align="right">
		  						<%=UtilidadesNumero.formatoCampo(importeAnticipado)%>&nbsp;&euro;
		  					</td>
			  				
						</siga:FilaConIconos>

	 <%		}
	 }

					if(vServicios != null && vServicios.size()>0 ) 
 					{
 						Enumeration en = vServicios.elements();	

 						while(en.hasMoreElements()){
 							i++;
							Hashtable hash = (Hashtable)en.nextElement();  
							
							String cuenta = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_CUENTA"));
							double precio = UtilidadesHash.getDouble(hash, "PRECIOSSERVICIOS").doubleValue();
							// Modificacion MAV 24/08/2005 muestra correctamente el IVA
							// float iva = 1;//UtilidadesHash.getFloat(hash, "IVA").floatValue();
							//float iva = UtilidadesHash.getFloat(hash, PysServiciosInstitucionBean.C_PORCENTAJEIVA).floatValue();
							float iva = UtilidadesHash.getFloat(hash,"VALORIVA").floatValue();
							int cantidad = UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_CANTIDAD).intValue();
							if((UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO))!=null){
								varIvaTotal = varIvaTotal +  (cantidad * ((float)(precio / 100)) * iva);
								varPrecioTotal = varPrecioTotal + (cantidad * (precio * (1 + (iva / 100))));						
							}
							
							//recupera el flag para mostrar/ocultar el botón de anticipar y el importe anticipado
							boolean anticipar = UtilidadesHash.getBoolean(hash, "ANTICIPAR").booleanValue();
							Double aux = UtilidadesHash.getDouble(hash, "IMPORTEANTICIPADO");
							double importeAnticipado = aux != null ? aux.doubleValue() : 0.0;
							FilaExtElement[] elementos=new FilaExtElement[1];
							if(anticipar){
								elementos[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", SIGAConstants.ACCESS_FULL);
							}
							else{
								elementos = null;
							}
				%>
			    <input type="hidden" name="oculto<%=i+1%>_idArticulo" value="<%=UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDSERVICIO)%>">
			    <input type="hidden" name="oculto<%=i+1%>_idArticuloInstitucion" value="<%=UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION)%>">
			    <input type="hidden" name="oculto<%=i+1%>_idPeticion" value="<%=UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDPETICION)%>">
			    <input type="hidden" name="oculto<%=i+1%>_idTipoClave" value="<%=UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS)%>">
			    <input type="hidden" name="oculto<%=i+1%>_tipo" value="S">
				
						<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" clase="listaNonEdit" elementos='<%=elementos%>' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>
							<td>
			  					<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO"))%>&nbsp;
			  						<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "SERVICIO_DESCRIPCION_PRECIO"))%>				  								
			  				</td>
			  				<td>	
								<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_FORMAPAGO"))%> 
			  				</td>
			  				<td>
			  					<%=cuenta%>
			  				</td>
			  				<td align="right">
			  					<%=cantidad%>
			  				</td>
			  				<td align="right">
			  					<%=UtilidadesNumero.formatoCampo(precio)%>&nbsp;&euro;&nbsp;/&nbsp;<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "PERIODICIDAD"))%> 
			  				</td>
			  				<td align="right">
			  					<%=UtilidadesNumero.formatoCampo(iva)%>&nbsp;% 
			  				</td>
			  				<td>
	<%
							try{
								int estado = Integer.parseInt(UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO)); 
								
								if(estado==tarjeta) { %>
										<siga:Idioma key="pys.estadoPago.pagado"/>
		<%						} else { %>
										<siga:Idioma key="pys.estadoPago.pendiente"/>
		<%						} 
							}catch(Exception e){ %>
								<siga:Idioma key="pys.estadoPago.noFacturable"/>
							
		<%					}%>
			  				</td>
			  				<td align="right">
	  							<%=UtilidadesNumero.formatoCampo(importeAnticipado)%>&nbsp;&euro;
	  						</td>
						</siga:FilaConIconos>
	 <%		}
	 } %>
	  			</siga:Table>

	<div id="camposRegistro2" style="position:absolute; width:280; height:70; z-index:2; bottom: 70px; left: 550px" align="center">
	<%
		varIvaTotal = UtilidadesNumero.redondea (varIvaTotal, 2);
		varPrecioTotal = UtilidadesNumero.redondea (varPrecioTotal, 2);
	%>
			<fieldset>
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>
					<td class="labelTextValue">					
						<input type='text' name='ivaTotal' value="<%=UtilidadesNumero.formatoCampo(varIvaTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20">
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
					<td class="labelTextValue">
						<input type='text' name='precioTotal' value="<%=UtilidadesNumero.formatoCampo(varPrecioTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20">
					</td>
				</tr>
			</table>
			</fieldset>
	</div>
	<div id="camposRegistro2" style="position:absolute; width:100%; height:35px; z-index:2; bottom: 35px; left: 0px" align="center">
			<table width="100%" align="center">
				<tr>
					<td class="labelTextCentro" colspan="2" align="center"><siga:Idioma key="messages.servicios.precioServicios"/></td>
				</tr>
				<!-- mhg - Mostramos el siguiente mensaje si tenemos productos no facturables -->
				<%if (noFact){ %>
				<tr>
					<td class="labelTextCentro" colspan="2" align="center"><siga:Idioma key="messages.pys.solicitudCompra.compraNoFacturable"/></td>	
				</tr>
				<%}%>
			</table>
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	</div>

</html:form>

<!-- Este formulario se emplea para abrir la ventana modal de modificación de los anticipos-->
<html:form styleId="MostrarAnticiposForm" action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea" style="display:none">
    <input type="hidden" name="idTipoClave" value="">
	<input type="hidden" name="idClave" value=""> 
	<input type="hidden" name="idClaveInstitucion" value=""> 
	<input type="hidden" name="idPeticion" value=""> 
	<input type="hidden" name="idPersona" value="<%=idPersona%>"> 
	<input type="hidden" name="tipo" value=""> 
	<input type="hidden" name="modo" value="mostrarAnticipos"> 
	<input type="hidden" name="actionModal" value="">
</html:form>	


<% if (usrbean.isLetrado() || cer_nofactura) { %>
			<siga:ConjBotonesAccion botones="IA" clase="botonesDetalle"/>			
<% } else { %>
			<siga:ConjBotonesAccion botones="FR,IA" clase="botonesDetalle"/>			
<% } %>

<% } //FIN DE LA PARTE DEL COMPROBANTE OK %> 

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
