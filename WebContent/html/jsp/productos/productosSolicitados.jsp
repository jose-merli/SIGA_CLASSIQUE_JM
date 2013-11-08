<!DOCTYPE html>
<html>
<head>
<!-- productosSolicitados.jsp -->

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
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);

	String idInstitucion = String.valueOf((Integer)request.getAttribute("idInstitucion"));	
	String idInstitucionPresentador = String.valueOf((Integer)request.getAttribute("idInstitucionPresentador"));	
	String botones = "";
%>


<!-- HEAD -->
<head  >
 
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->	

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
			function accionContinuar(){
			sub();
			  var existe=0;
			//validamos antes que si se ha elegido un producto de tipo Certificado, el combo de institucion Origen/Presentacion debe estar relleno
			 var tabla=document.getElementById('productosSolicitados');
			
				for(i=0; i<tabla.rows.length-1; i++)
				{
					// Obtengo el estado
					var tipoCertificado=document.getElementById("oculto"+(i+1)+"_5");
					
					
					if (tipoCertificado.value=="C") {
					 existe=1;
					 parent.document.solicitudCompraForm.deCertificado.value="1";
					}
				}
				
				if (existe==1 && (parent.document.solicitudCompraForm.idInstitucionPresentador.value=="" ||parent.document.solicitudCompraForm.idInstitucionPresentador.value==0)){
				var mensaje = "<siga:Idioma key="messages.certificado.error.noExisteColegio"/>" ;
					alert(mensaje);
				  	fin();
				  	return false;
				}
				//aalg: INC_09651. controlar que continua solo si tiene interesado
				if (parent.document.solicitudCompraForm.idPersona != null && parent.document.solicitudCompraForm.idPersona.value == ""){
					var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.seleccionCliente"/>" ;
					alert(mensaje);
				  	fin();
				  	return false;	
				}
			
					parent.document.solicitudCompraForm.modo.value = "continuar";	
					parent.document.solicitudCompraForm.target="mainWorkArea";					
					parent.document.solicitudCompraForm.submit();				
			}			
		
		function refrescarLocal(){
			parent.document.solicitudCompraForm.target="resultado";
			parent.document.solicitudCompraForm.modo.value = "solicitar";
			parent.document.solicitudCompraForm.concepto.value = "";
			parent.document.solicitudCompraForm.submit();
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
			<siga:Table 
			name="productosSolicitados"
			columnNames="pys.solicitudCompra.literal.tipo,pys.solicitudCompra.literal.categoria,pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.cantidad,"  
			columnSizes="28,30,23,7,12">
<% 				
				if(carro == null) { 	
					botones = ""; 
%> 	
			  		<tr class="notFound">
						<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>		 						
<% 					
				} else {
					Vector vArticulos = carro.getListaArticulos();
					if(vArticulos == null || vArticulos.size()<1 ){
						botones = ""; 
%> 	
			  		<tr class="notFound">
						<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>		 						
<% 
					} else 	{ 						
 						botones="CT";
 						for (int i = 0; i < vArticulos.size(); i++) {
 							int fila;
							Articulo a = (Articulo) vArticulos.get(i);
							fila=i+1;
%> 				
								
							<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' botones='B' visibleConsulta='false' visibleEdicion='false' clase="listaNonEdit">
						    <td> 
						    		<input type="hidden" name="idPersona" 	value="<%=carro.getIdPersona()%>">
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
			  				<td align="right">
							    <%=UtilidadesString.mostrarDatoJSP(String.valueOf(a.getCantidad()))%> 
							</td> 										
							</siga:FilaConIconos>
<%		
	 						} // for
	 					} // else
	 				} // else
%>  			
					</siga:Table>
</html:form>
		  			
			<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/> 			

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
