<!DOCTYPE html>
<html>
<head>
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
<%@ page import = "java.util.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	//UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	//Variable que me indica si vengo de un error. En tal caso muestro el error.
	String error  = request.getAttribute("error")==null?"NO":(String)request.getAttribute("error");

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
%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	
	<script language="JavaScript">		
 		
 		function accionImprimir(){
	 		window.print();
 		}
 		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body onLoad="accionImprimir();">

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="pys.solicitudCompra.titulo2"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;
			    <%if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<%} %>			
			</td>
		</tr>
	</table>

				<siga:ConjCampos leyenda="pys.solicitudCompra.literal.datosSolicitud">	
					<table class="tablaCampos" align="center">	
						<!-- FILA -->
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.idPeticion"/></td>				
							<td class="labelText"><%=UtilidadesString.mostrarDatoJSP(idPeticion)%></td>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.fechaSolicitud"/></td>				
							<td class="labelText"><%=UtilidadesString.mostrarDatoJSP(fecha)%></td>	
						</tr>
						<!-- FILA -->
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nombreSolicitante"/></td>				
							<td class="labelText"><%=UtilidadesString.mostrarDatoJSP(nombrePersona)%></td>	
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.tipoSolicitud"/></td>				
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.cabecera"/></td>
						</tr>
					</table>
				</siga:ConjCampos>

				<siga:Table 
		  				name="cabecera"
		  				border="2"
		  				columnNames="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.formaPago,pys.solicitudCompra.literal.nCuenta,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.iva,pys.solicitudCompra.literal.estadoPago"  
		   				columnSizes="30,15,19,8,11,7,10"
		   				fixedHeight="-1"
		   				modal = "G">
				
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

	 			<% }}

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
					<% }} %>
	  			</siga:Table>

	<%
		varIvaTotal = UtilidadesNumero.redondea (varIvaTotal, 2);
		varPrecioTotal = UtilidadesNumero.redondea (varPrecioTotal, 2); 
	%>
	<table width="280px" align="center">
		<tr>
			<td>
				<fieldset>
					<table>						
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>
							<td class="labelTextValue" >					
								<input type='text' name='ivaTotal' value="<%=UtilidadesNumero.formato(varIvaTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20">
							</td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
							<td class="labelTextValue">
								<input type='text' name='precioTotal' value="<%=UtilidadesNumero.formato(varPrecioTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20">
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	
	<table width="100%" align="center">
		<tr>
			<td class="labelTextCentro" colspan="2" align="center"><siga:Idioma key="messages.servicios.precioServicios"/></td>
		</tr>
	</table>
					

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
