<!-- productosSolicitados.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);

	String idInstitucion = String.valueOf((Integer)request.getAttribute("idInstitucion"));	
	String idInstitucionPresentador = String.valueOf((Integer)request.getAttribute("idInstitucionPresentador"));	
	String botones = "";
%>
<html>

<!-- HEAD -->
<head  >
 
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->	

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
			function accionContinuar(){
			sub();
			  var existe=0;
			//validamos antes que si se ha elegido un producto de tipo Certificado, el combo de institucion Origen/Presentacion debe estar relleno
			 var tabla=document.getElementById('cabecera');
			
				for(i=0; i<tabla.rows.length-1; i++)
				{
					// Obtengo el estado
					var tipoCertificado=document.getElementById("oculto"+(i+1)+"_5");
					
					
					if (tipoCertificado.value=="C") {
					 existe=1;
					 parent.solicitudCompraForm.deCertificado.value="1";
					}
				}
				
				if (existe==1 && (parent.solicitudCompraForm.idInstitucionPresentador.value=="" ||parent.solicitudCompraForm.idInstitucionPresentador.value==0)){
				var mensaje = "<siga:Idioma key="messages.certificado.error.noExisteColegio"/>" ;
					alert(mensaje);
				  	fin();
				  	return false;
				}
			
					parent.solicitudCompraForm.modo.value = "continuar";	
					parent.solicitudCompraForm.target="mainWorkArea";					
					parent.solicitudCompraForm.submit();				
			}			
		
		function refrescarLocal(){
			parent.solicitar("");
		}	
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body class="tablaCentralCampos">			

	<html:form action="/PYS_GenerarSolicitudes.do" method="POST"  >
		<input type="hidden" name="modo" 				value="abrirBusquedaModal">	
		<input type="hidden" name="ventana" 		value="solicitud">			
		<input type="hidden" name="idInstitucion" 	value="">	
		<input type="hidden" name="idInstitucionPresentador" 	value="">
		
		
		
          
		
		<table class="tablaTitulo" cellpadding="0" cellspacing="0">
		 <tr>
		   <td class="titulosPeq">
         <siga:Idioma key="cen.consultaProductos.literal.solicitar"/>
		 </td>
		 </tr>
		 </table>
			<siga:TablaCabecerasFijas 
			nombre="cabecera"
			borde="1"
			estilo=""
	   		clase="tableTitle"
			nombreCol="pys.solicitudCompra.literal.tipo,pys.solicitudCompra.literal.categoria,pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.cantidad,"  
			tamanoCol="30,30,23,7,10"
			alto="160"
			ajusteBotonera="true"	
			activarFilaSel="true" >
<% 				
				if(carro == null) 
				{ 	
					botones = ""; 
					
				}else {
					Vector vArticulos = carro.getListaArticulos();
					if(vArticulos == null || vArticulos.size()<1 ){
						botones = ""; 
%> 	
			  		<br><br>
				   		 <p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			 		<br><br>  			 						
<% 
					}else 
					{ 						
 						botones="CT";
 						for (int i = 0; i < vArticulos.size(); i++) {
 							int fila;
							Articulo a = (Articulo) vArticulos.get(i);
							fila=i+1;
%> 				
							<input type="hidden" name="idPersona" 	value="<%=carro.getIdPersona()%>">	
							<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' botones='B' visibleConsulta='false' visibleEdicion='false' clase="listaNonEdit">
						    <td> 
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_1' value='<%=String.valueOf((Integer)a.getIdTipo())%>'>	 							
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_2' value='<%=String.valueOf((Long)a.getIdArticulo())%>'>	
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_3' value='<%=String.valueOf((Long)a.getIdArticuloInstitucion())%>'>
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_4' value='<%=String.valueOf(a.getClaseArticulo())%>'>	
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_5' value='<%=String.valueOf(a.getTipoCertificado())%>'>																														
			  					<%=UtilidadesString.mostrarDatoJSP(a.getIdTipoDescripcion())%>  						  								
			  				</td>
			  				<td>
			  					<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloDescripcion())%> 
			  				</td>
			  				<td>
			  					<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloInstitucionDescripcion())%> 
			  				</td>
			  				<td>
							    <%=UtilidadesString.mostrarDatoJSP(String.valueOf(a.getCantidad()))%> 
							</td> 										
							</siga:FilaConIconos>
<%		
	 						} // for
	 					} // else
	 				} // else
%>  			
					</siga:TablaCabecerasFijas>
</html:form>
		  			
			<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/> 			

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
