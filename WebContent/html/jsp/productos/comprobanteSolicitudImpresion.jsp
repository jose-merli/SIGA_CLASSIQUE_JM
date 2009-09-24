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
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
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
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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


<!-- 1. Pintamos la cabecera de la tabla con los contenidos -->
<table id='cabeceraCabeceras' border='2' width='100%' cellspacing='0' cellpadding='0'>
	<tr class = 'tableTitle'>
<td align='center' width='30%'><siga:Idioma key="pys.solicitudCompra.literal.concepto"/></td>
<td align='center' width='15%'><siga:Idioma key="pys.solicitudCompra.literal.formaPago"/></td>
<td align='center' width='19%'><siga:Idioma key="pys.solicitudCompra.literal.nCuenta"/></td>
<td align='center' width='8%'><siga:Idioma key="pys.solicitudCompra.literal.cantidad"/></td>
<td align='center' width='11%'><siga:Idioma key="pys.solicitudCompra.literal.precio"/></td>
<td align='center' width='7%'><siga:Idioma key="pys.solicitudCompra.literal.iva"/></td>
<td align='center' width='10%'><siga:Idioma key="pys.solicitudCompra.literal.estadoPago"/></td>
	</tr>

<% 				if(vProductos != null && vProductos.size()>0 ) 
				{
 						Enumeration en = vProductos.elements();		 							
						while(en.hasMoreElements()){
							Hashtable hash = (Hashtable)en.nextElement();  
							String cuenta=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_CUENTA"));
														
							double precio = UtilidadesHash.getDouble(hash, PysProductosSolicitadosBean.C_VALOR).doubleValue();
							float iva = UtilidadesHash.getFloat(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA).floatValue();
							int cantidad = UtilidadesHash.getInteger(hash, PysProductosSolicitadosBean.C_CANTIDAD).intValue();
							varIvaTotal = varIvaTotal +  (cantidad * ((float)(precio / 100)) * iva);
							varPrecioTotal = varPrecioTotal + (cantidad * (precio * (1 + (iva / 100))));
				%>
							<tr class="listaNonEdit">
								<td>
			  					<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO"))%>  						  								
			  				</td>
			  				<td>
									<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_FORMAPAGO"))%> 
			  				</td>
			  				<td>
			  					<%=cuenta%>
			  				</td>
			  				<td>
			  					<%=cantidad%>
			  				</td>
			  				<td>
			  					<%=UtilidadesNumero.formato(precio)%>&nbsp;&euro;
			  				</td>
			  				<td>
			  					<%=UtilidadesNumero.formato(iva)%>&nbsp;% 
			  				</td>
			  				<td>
	<%
							int estado = Integer.parseInt(UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO)); 
							if(estado==tarjeta) { %>
									<siga:Idioma key="pys.estadoPago.pagado"/>
	<%						} else { %>
									<siga:Idioma key="pys.estadoPago.pendiente"/>
	<%						} %>
			  				</td>
	  				 </tr>

	 <%		}
	 }

					if(vServicios != null && vServicios.size()>0 ) 
 					{
 							Enumeration en = vServicios.elements();		 							
							while(en.hasMoreElements()){
							Hashtable hash = (Hashtable)en.nextElement();  
							
							String cuenta = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_CUENTA"));
							double precio = UtilidadesHash.getDouble(hash, "PRECIOSSERVICIOS").doubleValue();
							// Modificacion MAV 24/08/2005 muestra correctamente el IVA
							// float iva = 1;//UtilidadesHash.getFloat(hash, "IVA").floatValue();
							float iva = UtilidadesHash.getFloat(hash, PysServiciosInstitucionBean.C_PORCENTAJEIVA).floatValue();
							int cantidad = UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_CANTIDAD).intValue();
							varIvaTotal = varIvaTotal +  (cantidad * ((float)(precio / 100)) * iva);
							varPrecioTotal = varPrecioTotal + (cantidad * (precio * (1 + (iva / 100))));						
					%>
							<tr class="listaNonEdit">
								<td>
			  					<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO"))%>  						  								
			  				</td>
			  				<td>	
								<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_FORMAPAGO"))%> 
			  				</td>
			  				<td>
			  					<%=cuenta%>
			  				</td>
			  				<td>
			  					<%=cantidad%>
			  				</td>
			  				<td>
			  					<%=UtilidadesNumero.formato(precio)%>&nbsp;&euro;&nbsp;/&nbsp;<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "PERIODICIDAD"))%> 
			  				</td>
			  				<td>
			  					<%=UtilidadesNumero.formato(iva)%>&nbsp;% 
			  				</td>
			  				<td>
	<%
							int estado = Integer.parseInt(UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO)); 
							if(estado==tarjeta) { %>
									<siga:Idioma key="pys.estadoPago.pagado"/>
	<%						} else { %>
									<siga:Idioma key="pys.estadoPago.pendiente"/>
	<%						} %>
			  				</td>
	  				 </tr>
	 <%		}
	 } %>

	  			</table>

	<div id="camposRegistro2" style="position:absolute; width:280; height:70; z-index:2; bottom: 70px; left:200px" align="center">

	<%
		varIvaTotal = UtilidadesNumero.redondea (varIvaTotal, 2);
		varPrecioTotal = UtilidadesNumero.redondea (varPrecioTotal, 2); 
	%>
		<fieldset>
			<table align="center">
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
	</div>
	
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
