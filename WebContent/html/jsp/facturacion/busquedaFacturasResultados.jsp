<!-- busquedaFacturasResultados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri	="struts-tiles.tld" 			prefix="tiles" 		%>
<%@taglib uri = "struts-html.tld" 			prefix="html" 		%>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"			%>

<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.FacFacturaAdm"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitiveBind"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Hashtable"%>


<%
	String app = request.getContextPath(); 
	//RowsContainer vFacturas = (RowsContainer) request.getAttribute("facturas");
	
	request.getSession().setAttribute("EnvEdicionEnvio","FAC");	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma=usrbean.getLanguage().toUpperCase();
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	
	
	
	
 if (request.getSession().getAttribute("DATAPAGINADOR")!=null){
	 hm = (HashMap)request.getSession().getAttribute("DATAPAGINADOR");

	
	
	 if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  resultado = (Vector)hm.get("datos");
	  PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind)hm.get("paginador");
	
	 	paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 }else{
	  resultado =new Vector();
	  paginaSeleccionada = "0";
	 	totalRegistros = "0";
	 	registrosPorPagina = "0";
	 }
}
else{
      resultado =new Vector();
	  paginaSeleccionada = "0";
	 	totalRegistros = "0";
	 	registrosPorPagina = "0";
}	 
	
	String action=app+"/FAC_BusquedaFactura.do?noReset=true";

//	FacFacturaAdm admFac = new FacFacturaAdm(usrbean);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.struts.action.ActionMapping"%>
<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
</head>

<script>
	<!-- Refrescar -->
	function refrescarLocal(){ 		
		parent.buscar();
	}	
	
	function enviar(fila)
	{
	   	var auxFac = 'oculto' + fila + '_2';
	    var idFac = document.getElementById(auxFac);			          		
	    
	    document.DefinirEnviosForm.idFactura.value=idFac.value;
	   	
	   	document.DefinirEnviosForm.modo.value='envioModal';		   	
	   	document.DefinirEnviosForm.subModo.value='envioFactura';		   	
	   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
	   	if (resultado==undefined||resultado[0]==undefined||resultado[0]=="M"){
	   				   		
	   	} 
	   	else {
	   		var idEnvio = resultado[0];
		    var idTipoEnvio = resultado[1];
		    var nombreEnvio = resultado[2];				    
		    
		   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
		   	document.DefinirEnviosForm.modo.value='editar';
		   	document.DefinirEnviosForm.submit();
	   	}
	}

	
</script>

<body>
		<html:form action="/FAC_BusquedaFactura.do?noReset=true" method="POST" target="mainWorkArea">

			<html:hidden property = "modo"  styleId = "modo" value = ""/>
			<input type="hidden" id="actionModal"  name="actionModal" value="">
		</html:form>

			<siga:Table 
			   name = "tablaResultados"
			   border  = "1"
			   columnNames="facturacion.buscarFactura.literal.NumeroFactura,
			   						facturacion.buscarFactura.literal.descripcion,
			   						facturacion.buscarFactura.literal.Fecha,
			   						facturacion.buscarFactura.literal.Cliente,
			   						facturacion.buscarFactura.literal.Total,
			   						facturacion.buscarFactura.literal.Estado,"

			   columnSizes = "12,18,8,27,10,15,10">

		<%if ((resultado != null) && (resultado.size() > 0)){ %>


	
					<%	 for (int i = 1; i <=resultado.size(); i++) { 
							
							// Hashtable factura = (Hashtable)((Row) vFacturas.get(i)).getRow();
							   Row fila = (Row)resultado.elementAt(i-1);
			                    Hashtable factura = (Hashtable) fila.getRow();
							 if (factura != null){ 


									Integer idInstitucion = UtilidadesHash.getInteger(factura, FacFacturaBean.C_IDINSTITUCION);
									String idFactura = UtilidadesHash.getString(factura, FacFacturaBean.C_IDFACTURA);
									String numFactura = UtilidadesHash.getString(factura, FacFacturaBean.C_NUMEROFACTURA);
									Long idPersona = UtilidadesHash.getLong(factura, FacFacturaBean.C_IDPERSONA);
									String nombreCompleto = UtilidadesHash.getString(factura, CenPersonaBean.C_NOMBRE) + " " +
																					UtilidadesHash.getString(factura, CenPersonaBean.C_APELLIDOS1) + " "  +
																					UtilidadesHash.getString(factura, CenPersonaBean.C_APELLIDOS2);
									Double total = UtilidadesHash.getDouble(factura, "TOTAL");
									//Double total = admFac.getTotalFactura(idInstitucion,idFactura);
									String sEstado = UtilidadesHash.getString(factura, "DESCRIPCION_ESTADO");
									//String sEstado =  admFac.getDescEstadoFactura(idInstitucion,idFactura);
									String fecha = UtilidadesHash.getString(factura, FacFacturaBean.C_FECHAEMISION);
									String descripcion = UtilidadesHash.getString(factura, FacFacturacionProgramadaBean.C_DESCRIPCION);
									fecha = GstDate.getFormatedDateShort("", fecha);

									// boton de envios
									FilaExtElement[] elems = new FilaExtElement[1];
									if (numFactura!=null && !numFactura.trim().equals("")) {
										// si esta confirmada
										elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
									}


   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' elementos="<%=elems%>"  botones="E,C" visibleBorrado="false" pintarEspacio="no"  clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idInstitucion%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idFactura%>">											
											<input type="hidden" id="oculto<%=i%>_3" value="<%=idPersona%>">
											<%=UtilidadesString.mostrarDatoJSP(numFactura)%>
									</td>
									<td><%=UtilidadesString.mostrarDatoJSP(descripcion)%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(fecha)%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(nombreCompleto)%></td>
									<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(total.doubleValue()))%>&nbsp;&euro;</td>
									<td><%=sEstado%></td>
									</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  %>

	<% } // if  
	else {%>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>		
	<% } %>
			</siga:Table>

		
		<!-- Formulario para la creacion de envio -->
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>
			<html:hidden property = "subModo" value = ""/>
			
			<html:hidden property = "idFactura" value = ""/>
			
		</html:form>
		
	 <%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	   
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
      
	 <%}%>				
		
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>