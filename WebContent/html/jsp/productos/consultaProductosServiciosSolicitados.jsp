<!-- consultaProductosServiciosSolicitados.jsp -->
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
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>
<%@ page import = "com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import = "com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import = "com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import = "com.siga.beans.PysProductosInstitucionBean"%>
<%@ page import = "com.siga.beans.PysServiciosInstitucionBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<%@ page import="java.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
	boolean esLetrado=user.isLetrado();
	String idPersona = "";
	
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	String nif=(String)request.getAttribute("nif");
	String aprobarSolicitud=(String)request.getAttribute("paramAprobarSolicitud");	
	
	Vector vProductos = new Vector();	
	Vector vServicios = new Vector();
  
	Hashtable htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	
	if(htData != null){
	        
		 vProductos = (Vector) htData.get("vProductos");
		    
		 vServicios = (Vector) htData.get("vServicios");	
		  
		 if(UtilidadesHash.getLong(htData, "idPersona")!=null){
		     
			 idPersona = UtilidadesHash.getLong(htData, "idPersona").toString();
		 }
	}		
	
	int CLASE_PRODUCTO = 1;
	  
	int CLASE_SERVICIO = 2;
	  
	
	String DB_TRUE=ClsConstants.DB_TRUE;
	
	String DB_FALSE=ClsConstants.DB_FALSE;
	   
	String pPendiente=ClsConstants.PRODUCTO_PENDIENTE;
	String pAceptado=ClsConstants.PRODUCTO_ACEPTADO;
	String aceptado;
	String solicitarBaja;
	String fechaEfectiva = ""; //UtilidadesBDAdm.getFechaBD("");;
		
	double precio=0;
	float iva=0;	
	boolean aprobarSolicitudBaja;
	if((aprobarSolicitud!=null)&&(aprobarSolicitud.equalsIgnoreCase("S"))){
		aprobarSolicitudBaja = true;
	}else{
		aprobarSolicitudBaja = false;
	}
%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Enumeration"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->	

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
			
	function solicitarbaja(fila, id){
		if (id == 'productos')
			solicitarbaja_productos(fila);
		else
			solicitarbaja_servicios(fila);
	}
	
		function solicitarbaja_servicios(fila) {		
		        subicono('solicitarbaja_'+fila);
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var j;
				var tabla;
				
				<%if((!esLetrado)&&(aprobarSolicitudBaja)){%>
					var fecha = showModalDialog("/SIGA/html/jsp/productos/ventanaFechaEfectiva.jsp","","dialogHeight:200px;dialogWidth:400px;help:no;scroll:no;status:no;");
					window.top.focus();
					if( fecha!=null){ 
			  	  		// document.forms[0].fechaEfectiva.value=fecha;
			  	  		var campoFecha = 'ocultoS' + fila + '_6'; // Columna oculta con la fecha efectiva
			  	  		document.getElementById(campoFecha).value = fecha;
			    	}else{
			    		refrescarLocal();
			    		finsubicono('solicitarbaja_'+fila);
			    		return false;
			    	} 
		    	<%}else{%>
		    		if (!confirm("<siga:Idioma key="pys.solicitarBaja.literal.confirmaSolicitud"/>")){
		    			finsubicono('solicitarbaja_'+fila);
		    			return false;
		    		}
		    	<%}%>
				tabla = document.getElementById('servicios');
				var flag = true;
				j = 1;
				while (flag) {
				  var aux = 'ocultoS' + fila + '_' + j;
				  var oculto = document.getElementById(aux);
				  if (oculto == null)  { flag = false; }
				  else { datos.value = datos.value + oculto.value + ','; }
				  j++;
				}
				
				datos.value = datos.value + "%"
				
				document.all.solicitudBajaForm.modo.value = "solicitar";
				
				
		   	document.solicitudBajaForm.submit();
			
		 }
		 
		function solicitarbaja_productos(fila) {
				subicono('solicitarbaja_'+fila);
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var j;
				var tabla;
				//mhg - INC_08066_SIGA Cuando se da de baja una solicitud no hace falta pedir la fecha efectiva por lo que se ha quitado.
				if (!confirm("<siga:Idioma key="pys.solicitarBaja.literal.confirmaSolicitud"/>")){
	    			finsubicono('solicitarbaja_'+fila);
	    			return false;
				}
				tabla = document.getElementById('productos');
				
				var flag = true;
				j = 1;
				while (flag) {
				  var aux = 'ocultoP' + fila + '_' + j;
				 
				  var oculto = document.getElementById(aux);
				  if (oculto == null)  { flag = false; }
				  else { datos.value = datos.value + oculto.value + ','; }
				  j++;
				}
				datos.value = datos.value + "%";
				
		   	document.all.solicitudBajaForm.modo.value = "solicitar";
		  	document.solicitudBajaForm.submit() ;
		  	
			
		 }
		 
		 function refrescarLocal() {
				 parent.buscarProductosYServicios();
		}			
		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body class="tablaCentralCampos">			

		<html:form action="/PYS_Bajas.do" method="POST" target="submitArea" style="display:none">
			<input type="hidden" name="modo" 				value="abrirBusquedaModal">		
			<html:hidden name="solicitudBajaForm" property="idPersona" value="<%=idPersona%>"/>			 
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
<%			
			if(idPersona.equals("")){
			}
			else{
%>
			
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="pys.solicitudBaja.titulo1"/>
			</td>
		</tr>
	</table>			
				<siga:Table
	  				name="productos"
	  				columnNames="pys.solicitudCompra.literal.fechaSolicitud,pys.solicitudCompra.literal.idPeticion,pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.formaPago,pys.solicitudCompra.literal.nCuenta,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.estadoPago,pys.solicitarBaja.literal.estadoCompra,"
					columnSizes="10,7,15,13,17,6,11,10,6,5"
			   		fixedHeight="37%">
 <% 				if(vProductos == null || vProductos.size()<1 )
 							{  
 	%> 	
				  		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>			
  <% 
	 						}else{
	 							Enumeration en = vProductos.elements();		
	 							int i=0; 
	 							int k=0;   							
								while(en.hasMoreElements()){
									i++;
									FilaExtElement[] elems=new FilaExtElement[1];
									Hashtable hash = (Hashtable)en.nextElement();
									aceptado=(String)hash.get(PysProductosSolicitadosBean.C_ACEPTADO);
									solicitarBaja=(String)hash.get(PysProductosInstitucionBean.C_SOLICITARBAJA);
									
									/*if(aceptado.equalsIgnoreCase(pPendiente) &&
										((solicitarBaja.equalsIgnoreCase(DB_TRUE) && esLetrado) || (!esLetrado))){*/
										
									if((aceptado.equalsIgnoreCase(pPendiente) || aceptado.equalsIgnoreCase(pAceptado)) && 
										((solicitarBaja.equalsIgnoreCase(DB_TRUE) && esLetrado) || (!esLetrado))){
												k++;
												String tipopeticion = (String)hash.get("ESTADO_BAJA");
		  										if (tipopeticion!=null && tipopeticion.equals("SI")) {
		  											elems=null;
		  										}else{
		  											elems[0]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_READ);
		  										}
												if (hash.get(PysProductosSolicitadosBean.C_VALOR)!=null){	
												  precio = UtilidadesHash.getDouble(hash, PysProductosSolicitadosBean.C_VALOR).doubleValue();
												}  
												//if (hash.get(PysProductosSolicitadosBean.C_PORCENTAJEIVA)!=null){	
												  //iva = UtilidadesHash.getFloat(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA).floatValue();
												//}  
												if (hash.get("VALORIVA")!=null){	
												  iva = UtilidadesHash.getFloat(hash, "VALORIVA").floatValue();
												}
												
												precio = UtilidadesNumero.redondea ((precio * (1 + (iva / 100))), 2);
												String estadoPago	= UtilidadesProductosServicios.getEstadoPago(UtilidadesHash.getString(hash, "ESTADOPAGO"));		
												
												
																											
	%> 				
									<siga:FilaConIconosExtExt fila='<%=String.valueOf(k)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit" nombreTablaPadre="productos">
											<td>
												<input type='hidden' name='ocultoP<%=String.valueOf(k)%>_1' value='<%=hash.get(PysProductosSolicitadosBean.C_IDPETICION)%>'>	 							
												<input type='hidden' name='ocultoP<%=String.valueOf(k)%>_2' value='<%=hash.get(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO)%>'>	
												<input type='hidden' name='ocultoP<%=String.valueOf(k)%>_3' value='<%=hash.get(PysProductosSolicitadosBean.C_IDPRODUCTO)%>'>	
												<input type='hidden' name='ocultoP<%=String.valueOf(k)%>_4' value='<%=hash.get(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION)%>'>	
												<input type='hidden' name='ocultoP<%=String.valueOf(k)%>_5' value='<%=CLASE_PRODUCTO%>'>
												<input type='hidden' name='ocultoP<%=String.valueOf(k)%>_6' value='<%=fechaEfectiva%>'>	
						 						
						 						<%=UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)hash.get(PysPeticionCompraSuscripcionBean.C_FECHA)))%>  							
		  								</td>
		  								<td>
		  									<%=UtilidadesString.mostrarDatoJSP(hash.get(PysProductosSolicitadosBean.C_IDPETICION))%>  										 																				
		  								</td>  								
		  								<td>
												<%=UtilidadesString.mostrarDatoJSP(hash.get("CONCEPTO"))%>								
											</td>
		  								<td>
		  									<%=UtilidadesString.mostrarDatoJSP(hash.get("FORMAPAGO"))%>  														
		  								</td>
		  								<td>  									
		  									<%=UtilidadesString.mostrarDatoJSP(hash.get("NCUENTA"))%>  														
		  								</td>
		  								<td> 		  																								
		  									<%=UtilidadesString.mostrarDatoJSP(hash.get(PysProductosSolicitadosBean.C_CANTIDAD))%>  														
		  								</td>
		  								<td align="right">  								
		  									<%=UtilidadesNumero.formatoCampo(precio)%>&nbsp;&euro; 																		
		  								</td>		  								
		  								<td>
		  								<siga:Idioma key="<%=estadoPago%>"/>
		  								</td>
		  								<td>
		  									<%
		  										//String tipopeticion = (String)hash.get("ESTADO_BAJA");
		  										if (tipopeticion!=null && tipopeticion.equals("SI")) { %>
				  								<siga:Idioma key="pys.solicitarBaja.literal.bajaSolicitada"/>
		  									<% } else { %>
		  										&nbsp;
		  									<% } %>
		  								</td>
		  							</siga:FilaConIconosExtExt>
 	<%		
 									}
 								} // While  			
			 				} 
	%> 
			</siga:Table>
	
		<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
			<tr>
				<td class="titulosPeq">
					<siga:Idioma key="pys.solicitudBaja.titulo2"/>
				</td>
			</tr>
		</table>			
		
		<siga:Table 
 			name="servicios"
 			columnNames="pys.solicitudCompra.literal.fechaSolicitud,pys.solicitudCompra.literal.idPeticion,pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.formaPago,pys.solicitudCompra.literal.nCuenta,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.estadoPago,pys.solicitarBaja.literal.estadoCompra,"  
			columnSizes="10,7,15,13,17,6,11,10,6,5"
	   		fixedHeight="37%">
			
			<%if(vServicios == null || vServicios.size()<1 ) {%> 	
				<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
							
			<%} else {
				Enumeration en = vServicios.elements();		
				int i=0; 
				int k=0;  							
				while(en.hasMoreElements()) {
					i++;
					FilaExtElement[] elems=new FilaExtElement[1];
					Hashtable hash = (Hashtable)en.nextElement();
					aceptado=(String)hash.get(PysServiciosSolicitadosBean.C_ACEPTADO);
					solicitarBaja=(String)hash.get(PysServiciosInstitucionBean.C_SOLICITARBAJA);
					if((aceptado.equalsIgnoreCase(pPendiente) || aceptado.equalsIgnoreCase(pAceptado)) && ((solicitarBaja.equalsIgnoreCase(DB_TRUE) && esLetrado) || (!esLetrado))){
						k++; 				 	  							
						
						String tipopeticion = (String)hash.get("ESTADO_BAJA");
						if (tipopeticion!=null && tipopeticion.equals("SI")) {
							elems=null;
						}else{
							elems[0]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_READ);
						}
								
						if (hash.get(PysProductosSolicitadosBean.C_VALOR)!=null){					
						  precio = UtilidadesHash.getDouble(hash, PysProductosSolicitadosBean.C_VALOR).doubleValue();
						}
						
						//if (hash.get(PysProductosSolicitadosBean.C_PORCENTAJEIVA)!=null){		
						  //iva = UtilidadesHash.getFloat(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA).floatValue();
						//}
						if (hash.get("VALORIVA")!=null){		
						  iva = UtilidadesHash.getFloat(hash, "VALORIVA").floatValue();
						}
						precio = UtilidadesNumero.redondea ((precio * (1 + (iva / 100))), 2);			
						String estadoPago = UtilidadesProductosServicios.getEstadoPago(UtilidadesHash.getString(hash, "ESTADOPAGO"));																																								
				%> 			
						<siga:FilaConIconosExtExt fila='<%=String.valueOf(k)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit" nombreTablaPadre="servicios" >
							<td>
								<input type='hidden' name='ocultoS<%=String.valueOf(k)%>_1' value='<%=hash.get(PysServiciosSolicitadosBean.C_IDPETICION)%>'>	 							
								<input type='hidden' name='ocultoS<%=String.valueOf(k)%>_2' value='<%=hash.get(PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS)%>'>	
								<input type='hidden' name='ocultoS<%=String.valueOf(k)%>_3' value='<%=hash.get(PysServiciosSolicitadosBean.C_IDSERVICIO)%>'>	
								<input type='hidden' name='ocultoS<%=String.valueOf(k)%>_4' value='<%=hash.get(PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION)%>'>	
		 						<input type='hidden' name='ocultoS<%=String.valueOf(k)%>_5' value='<%=CLASE_SERVICIO%>'>
		 						<input type='hidden' name='ocultoS<%=String.valueOf(k)%>_6' value='<%=fechaEfectiva%>'>		
		 						<%=UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("", (String)hash.get(PysPeticionCompraSuscripcionBean.C_FECHA)))%> 	
							</td>
							
							<td>
								<%=UtilidadesString.mostrarDatoJSP(hash.get(PysServiciosSolicitadosBean.C_IDPETICION))%>  																													
							</td> 
							
							<td>
								<%=UtilidadesString.mostrarDatoJSP(hash.get("CONCEPTO"))%>											
							</td>
		  					
		  					<td>
		  						<%=UtilidadesString.mostrarDatoJSP(hash.get("FORMAPAGO"))%>  																
		  					</td>
		  					
		  					<td>  									
		  						<%=UtilidadesString.mostrarDatoJSP(hash.get("NCUENTA"))%>  														
		  					</td>
		  					
		  					<td>   																
		  						<%=UtilidadesString.mostrarDatoJSP(hash.get(PysServiciosSolicitadosBean.C_CANTIDAD))%>  														
		  					</td>
		  					
		  					<td>  									
		  						<%=UtilidadesNumero.formato(precio)%>&nbsp;&euro;&nbsp;/&nbsp;<%=UtilidadesString.mostrarDatoJSP(hash.get("SERVICIO_DESCRIPCION_PERIODICIDAD"))%>   													
		  					</td>
		  					
		  					<td>
		  						<siga:Idioma key="<%=estadoPago%>"/>																
		  					</td>
		  					
		  					<td>
		  					
		  						<%tipopeticion = (String)hash.get("ESTADO_BAJA");
		  						if (tipopeticion!=null && tipopeticion.equals("SI")) { %>
				  					<siga:Idioma key="pys.solicitarBaja.literal.bajaSolicitada"/>
		  						<% } else { %>
		  							&nbsp;
		  						<% } %>
		  					</td>
		  				</siga:FilaConIconosExtExt>
		  				
					<%	} // IF
 				} // WHILE  			
			} // ELSE  			 				
			%> 
		</siga:Table>	
	<%}%>		

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
