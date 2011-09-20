<!-- productosEncontrados.jsp -->
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
<%@ page import = "com.siga.beans.PysProductosInstitucionBean"%>
<%@ page import = "com.siga.beans.PysProductosAdm"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Enumeration" %>
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
	Vector resultado=null;
	if (request.getSession().getAttribute("resultBusqueda")!=null){
	  resultado=(Vector)request.getSession().getAttribute("resultBusqueda");	
	}
		
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->	

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
			function accionContinuar(){
					parent.solicitudCompraForm.modo.value = "continuar";	
					parent.solicitudCompraForm.target="mainWorkArea";					
					parent.solicitudCompraForm.submit();				
			}			
		
		function refrescarLocal(){
			parent.solicitar("");
		}	
		
		function solicitaralta(fila){
		    var fpadre = parent.document.solicitudCompraForm;
		 	var f = document.forms[0];
			f.concepto.value=fpadre.concepto.value;
			f.catalogo.value=fpadre.catalogo.value;
			f.idPersona.value=fpadre.idPersona.value;
			f.idInstitucion.value=fpadre.idInstitucion.value;
			f.idInstitucionPresentador.value=fpadre.idInstitucionPresentador.value;
			
			f.categoriaAux.value=eval("f.oculto"+fila+"_1").value;
		    f.tipoAux.value = eval("f.oculto"+fila+"_2").value;
		    f.productoAux.value = eval("f.oculto"+fila+"_3").value;
			if(f.idPersona.value == ""){
					var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.seleccionCliente"/>";
					alert (mensaje);					
					return false;
			}
				
			f.target="resultado";
			f.modo.value = "solicitar";
			if (f.catalogo.value!='S'){
			  f.concepto.value="Producto";
			}else{
  			  f.concepto.value="Servicio";
			}
			fpadre.catalogo.disabled=true;
			parent.document.all.solicitudCompraForm.idInstitucionPresentador.disabled=true;
			f.submit();
		}
		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body class="tablaCentralCampos">			

	<html:form action="/PYS_GenerarSolicitudes.do" method="POST" >
		<input type="hidden" name="modo" 		value="abrirBusquedaModal">	
		<input type="hidden" name="ventana" 	value="solicitud">	
		<input type="hidden" name="categoriaAux" 	value="">	
		<input type="hidden" name="tipoAux" 		value="">	
		<input type="hidden" name="productoAux" 	value="">
		<input type="hidden" name="concepto" 	value="">	
		<input type="hidden" name="catalogo" 	value="">		
    	<input type="hidden" name="idPersona" 	value="">	
		<input type="hidden" name="idInstitucion" 	value="">	
		<input type="hidden" name="idInstitucionPresentador" 	value="">	
		<input type="hidden" name="resultBusqueda" 	value="1">	
		
					
		<table  class="tablaTitulo" cellpadding="0" cellspacing="0">
		 <tr>
		   <td class="titulosPeq">
         <siga:Idioma key="cen.consultaProductos.literal.busqueda"/>
		 </td>
		 </tr>
		 </table>
		
			<siga:TablaCabecerasFijas 
			nombre="tablaDatos"
			borde="1"
			estilo=""
	   		clase="tableTitle"
			nombreCol="pys.solicitudCompra.literal.tipo,pys.solicitudCompra.literal.categoria,pys.solicitudCompra.literal.concepto,"  
			tamanoCol="30,30,30,10"
			   alto="100%"
			   		   
			>

<%
			if(resultado==null || resultado.size()<1){								
				botones = ""; 
%>
			  		<br><br>
				   		 <p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			 		<br><br>  			 						
<% 
					}else{ 					 							 			
 						botones="CT";
 						Enumeration enumSel = ((Vector)request.getSession().getAttribute("resultBusqueda")).elements();	
						int fila=1;
						while (enumSel.hasMoreElements()){
							Row row = (Row) enumSel.nextElement();
				
							String seguro=row.getString("TIPO");
							
				            FilaExtElement[] elems=new FilaExtElement[1];
							elems[0]=new FilaExtElement("solicitaralta","solicitaralta",SIGAConstants.ACCESS_READ);
%> 							
							
							<siga:FilaConIconos fila='<%=String.valueOf(fila)%>' botones='' elementos='<%=elems%>' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' pintarEspacio='false' clase="listaNonEdit">
								<td> 
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_1' value='<%=row.getString("IDTIPOPRODUCTO")%>'>	 							
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_2' value='<%=row.getString("IDPRODUCTO")%>'>	
									<input type='hidden' name='oculto<%=String.valueOf(fila)%>_3' value='<%=row.getString("IDPRODUCTOINSTITUCION")%>'>
									
			  					<%=row.getString("CATEGORIA")%>  						  								
			  				</td>
			  				<td>
			  					<%=row.getString("TIPO")%> 
			  				</td>
			  				<td>
			  					<%=row.getString("DESCRIPCION")%> 
			  				</td>
							 
							</siga:FilaConIconos>
<%		                    fila++;   
	 						} // for
	 					} // else
	 				
%>  			
					</siga:TablaCabecerasFijas>
	</html:form>	  			


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
