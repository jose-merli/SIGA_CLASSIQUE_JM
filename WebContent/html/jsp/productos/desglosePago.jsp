<!-- desglosePago.jsp -->
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
<%@ page import = "com.siga.general.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	Integer idInstitucion=Integer.valueOf(user.getLocation());
		
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	
	CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);
	Vector vArticulos = carro.getListaArticulos();	
	
	int tarjeta = ClsConstants.TIPO_FORMAPAGO_TARJETA;
		
	float varIvaTotalTarjeta = 0;
	double varPrecioTotalTarjeta = 0;
	
	float varIvaTotalOtro = 0;
	double varPrecioTotalOtro = 0;
	
	String sPrecio;
	String sPeriodicidad;
	
	String pagoConTarjeta = request.getAttribute("PAGOTARJETA")==null?"N":(String)request.getAttribute("PAGOTARJETA");
	boolean visibilidad = true;
	String estilo="box", visibilidadTarjeta="hidden";
	if (pagoConTarjeta.equals("S")) {
		visibilidad = false;
		visibilidadTarjeta="visible";
		//estilo = "boxConsulta";
	}
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="pys.solicitudCompra.cabecera" 
		localizacion="pys.solicitudCompra.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->	
	<script language="JavaScript">	
		
 		function accionCerrar(){
 			window.close();
 		}
 		
	    function validarAnchos()
	    {
	        validarAncho_tarjeta();
	        validarAncho_otro();
	    }
 		
	</script>	
</head>

<body onLoad=validarAnchos();>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="mainWorkArea" type="">
  <html:hidden name="solicitudCompraForm" property="modo" value="" />
  <html:hidden name="solicitudCompraForm" property="pan" value="" />
  <html:hidden name="solicitudCompraForm" property="fechaCaducidad" value="" />
  <input type="hidden" name="pagoConTarjeta" value="<%=pagoConTarjeta%>" />
  
  	<table class="tablaTitulo" align="center" cellspacing="0" height="20">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="pys.solicitudCompra.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP((String)request.getAttribute("nombrePersona"))%> &nbsp;&nbsp;
		
			</td>
		</tr>
	</table>
  

	<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="pys.desgloseCesta.literal.pagoTarjeta"/>
				</td>
			</tr>
	</table>						
	<div  style="position:relative;width=100%;height:200px;">
					<siga:TablaCabecerasFijasExt 
	  				nombre="tarjeta"
	  				borde="2"
	  				clase="tableTitle"
	  				nombreCol="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.iva"  
	   				tamanoCol="50,20,20,10"
				    alto="160"
	   				ajusteAlto="false"
	   				variasTablasEnLaMismaPagina="true"
	   				>
<%				
 						for (int i = 0; i < vArticulos.size(); i++) {
							Articulo a = (Articulo) vArticulos.get(i);
								a.getIdFormaPago();													
								if(a.getIdFormaPago() != null && a.getIdFormaPago().intValue() == tarjeta){			
									double precio = (double)a.getPrecio().doubleValue();
									float iva = (float)a.getValorIva().floatValue();
									varIvaTotalTarjeta = varIvaTotalTarjeta +  (a.getCantidad() * ((float)(precio / 100)) * iva);
									varPrecioTotalTarjeta = varPrecioTotalTarjeta + (a.getCantidad() * (precio * (1 + (iva / 100))));
									sPeriodicidad = "";
									sPrecio = "-";
									if(a.getPrecio()!= null) {										
										sPrecio = UtilidadesString.mostrarDatoJSP(a.getPrecio());
										if(a.getClaseArticulo()==Articulo.CLASE_SERVICIO){
											sPeriodicidad = " / " + UtilidadesString.mostrarDatoJSP(a.getPeriodicidad());
										}											
								}
%> 											
								<tr class="listaNonEdit">
									<td>
				  					<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloInstitucionDescripcion())%>
				  				</td>					  				
				  				<td>
				  					<%=a.getCantidad()%>
				  				</td>
				  				<td>
				  					<%=UtilidadesNumero.formatoCampo(sPrecio)%>&nbsp;&euro;&nbsp;<%=sPeriodicidad%>
				  				</td>
				  				<td>
				  					<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(a.getValorIva().floatValue()))%>&nbsp;% 
				  				</td>					  				
		  				 </tr>							

<%						}
	 					}
%>  			
	  			</siga:TablaCabecerasFijasExt>		
	</div>

	<%
			varIvaTotalTarjeta = UtilidadesNumero.redondea (varIvaTotalTarjeta, 2);
			varPrecioTotalTarjeta = UtilidadesNumero.redondea (varPrecioTotalTarjeta, 2);
	%>
					<div style="visibility:visible;width:100%; height:60;" align="center">
						<table>
							<tr>
								<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>					
								<td class="labelText"><%=UtilidadesNumero.formatoCampo(varIvaTotalTarjeta)%>&nbsp;&euro;</td>
								<td>&nbsp;&nbsp;</td>
								<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
								<td class="labelText"><%=UtilidadesNumero.formatoCampo(varPrecioTotalTarjeta)%>&nbsp;&euro;</td>
							</tr>
						 </table>
						 
					<div id="datosTarjeta" style="visibility:<%=visibilidadTarjeta%>;position:relative;">
						 <table>
							<tr>
								<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nTarjeta"/>&nbsp;(*)</td>
								<td>
									<html:text name="solicitudCompraForm" property="tarjeta1" maxlength="4" size="4" styleClass="<%=estilo%>" readOnly="<%=visibilidad%>" value=""></html:text>
									&nbsp;-
									<html:text name="solicitudCompraForm" property="tarjeta2" maxlength="4" size="4" styleClass="<%=estilo%>" readOnly="<%=visibilidad%>" value=""></html:text>
									&nbsp;-
									<html:text name="solicitudCompraForm" property="tarjeta3" maxlength="4" size="4" styleClass="<%=estilo%>" readOnly="<%=visibilidad%>" value=""></html:text>
									&nbsp;-
									<html:text name="solicitudCompraForm" property="tarjeta4" maxlength="4" size="4" styleClass="<%=estilo%>" readOnly="<%=visibilidad%>" value=""></html:text>
								</td>
								<td>&nbsp;&nbsp;</td>
								<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.caducidad"/> (<siga:Idioma key="pys.solicitudCompra.literal.formatoCaducidad"/>)&nbsp;(*)</td>
								<td>
									<html:text name="solicitudCompraForm" property="tarjetaMes" maxlength="2" size="2" styleClass="<%=estilo%>" readOnly="<%=visibilidad%>" value=""></html:text>
									&nbsp;/
									<html:text name="solicitudCompraForm" property="tarjetaAnho" maxlength="4" size="4" styleClass="<%=estilo%>" readOnly="<%=visibilidad%>" value=""></html:text>
								</td>
							</tr>
						</table>
					</div>
					</div>		

					
						<table class="tablaTitulo" cellspacing="0" heigth="32">
							<tr>
								<td id="titulo" class="titulosPeq">
									<siga:Idioma key="pys.desgloseCesta.literal.otrosPagos"/>
								</td>
							</tr>
						</table>								
						
						<siga:TablaCabecerasFijasExt
		  				nombre="otro"
		  				borde="2"
				   		clase="tableTitle"
		  				nombreCol="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.iva"  
		   				tamanoCol="50,20,20,10"
		   				ajuste="90"
	   					variasTablasEnLaMismaPagina="true">
	<%				
	 						for (int i = 0; i < vArticulos.size(); i++) {
								Articulo a = (Articulo) vArticulos.get(i);		
								a.getIdFormaPago();							
								if(a.getIdFormaPago() == null || a.getIdFormaPago().intValue() != tarjeta){						
								
									double precio = (double)a.getPrecio().doubleValue();
									float iva = (float)a.getValorIva().floatValue();
									if(a.getIdFormaPago() != null){
										varIvaTotalOtro = varIvaTotalOtro +  (a.getCantidad() * ((float)(precio / 100)) * iva);
										varPrecioTotalOtro = varPrecioTotalOtro + (a.getCantidad() * (precio * (1 + (iva / 100))));
									}
									sPeriodicidad = "";
									sPrecio = "-";
									if(a.getPrecio()!= null) {										
										sPrecio = UtilidadesString.mostrarDatoJSP(a.getPrecio());
										if(a.getClaseArticulo()==Articulo.CLASE_SERVICIO){
											sPeriodicidad = " / " + UtilidadesString.mostrarDatoJSP(a.getPeriodicidad());
										}											
									}
	%> 											
									<tr class="listaNonEdit">
										<td>
					  					<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloInstitucionDescripcion())%>
					  				</td>					  				
					  				<td>
					  					<%=a.getCantidad()%>
					  				</td>
					  				<td>
					  					<%=UtilidadesNumero.formatoCampo(sPrecio)%>&nbsp;&euro;&nbsp;<%=sPeriodicidad%>
					  				</td>
					  				<td>
					  					<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(a.getValorIva().floatValue()))%>&nbsp;%  
					  				</td>					  				
			  				 </tr>							
	
	<%						}
		 					}
	%>  			
		  			</siga:TablaCabecerasFijasExt>					

				<div style="position:absolute;bottom:30px;width:100%; height:60; z-index:2;" align="center">
<%
		varIvaTotalOtro = UtilidadesNumero.redondea (varIvaTotalOtro, 2);
		varPrecioTotalOtro = UtilidadesNumero.redondea (varPrecioTotalOtro, 2);
%>
						<table>
							<tr>
								<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>					
								<td class="labelText"><%=UtilidadesNumero.formatoCampo(varIvaTotalOtro)%>&nbsp;&euro;</td>
								<td>&nbsp;&nbsp;</td>
								<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
								<td class="labelText"><%=UtilidadesNumero.formatoCampo(varPrecioTotalOtro)%>&nbsp;&euro;</td>
							</tr>
						</table>
					</div>		
		
			<siga:ConjBotonesAccion botones="V,FC" clase="botonesDetalle"/>

	<script>
			function pagoTarjeta(){
				return (document.forms[0].pagoConTarjeta.value == "S");
			}
			
			function validarDatos(){
		 		var f = document.forms[0];
				var valido = true;

		 		if (f.tarjeta1.value!="" && f.tarjeta2.value!="" && f.tarjeta3.value!="" && f.tarjeta4.value!="") {
	 				if (f.tarjetaAnho.value=="" || f.tarjetaMes.value=="") {
				 		alert('<siga:Idioma key="pys.desgloseCesta.literal.avisoFechaTarjeta"/>');
		 				valido = false;		
	 				}
		 		} else {
			 		alert('<siga:Idioma key="pys.desgloseCesta.literal.avisoTarjeta"/>');
		 			valido = false;
		 		}
				return valido;
			}
	
	 		function accionfinalizarCompra(){
				sub();
		 		var f = document.forms[0];
	 			if (pagoTarjeta()) {
	 				if (validarDatos()) {
		 				var pan = f.tarjeta1.value + f.tarjeta2.value + f.tarjeta3.value + f.tarjeta4.value;
		 				var caducidad = f.tarjetaAnho.value + f.tarjetaMes.value;
		 			    f.pan.value = pan;
		 				f.fechaCaducidad.value = caducidad;
			 			f.modo.value = "pagarConTarjeta";
			 			f.submit();	 		
					
		 			}else{
					  fin();
					} 
	 			} else {
		 			f.modo.value = "finalizarCompra";
		 			f.submit();	 			
					
	 			} 
			}
			
			function accionVolver(){
				history.back();
			}
			
 	</script>
			
	</html:form>	
	


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>