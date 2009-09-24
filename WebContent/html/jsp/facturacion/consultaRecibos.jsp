<!-- consultaRecibos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="com.siga.beans.FacFacturaIncluidaEnDisqueteBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>
<%@ page import="com.siga.facturacion.form.DevolucionesManualesForm"%>



<%
	String app = request.getContextPath(); 
	//Vector vRecibos = (Vector) request.getAttribute("recibos");
    DevolucionesManualesForm formulario = (DevolucionesManualesForm)request.getSession().getAttribute("DevolucionesManualesForm");
	ArrayList motivosSel = new ArrayList();
	motivosSel.add((String) request.getAttribute("motivoDevolucion"));
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma=usrbean.getLanguage().toUpperCase();
	HttpSession ses=request.getSession();
    Vector resultado=null;
	/** PAGINADOR ***/
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
    if (ses.getAttribute("DATAPAGINADOR")!=null){
	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");
	
	 if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  resultado = (Vector)hm.get("datos");
	  PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)hm.get("paginador");
	
	 	paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
		totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
		registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 }
	 else{
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
	String action=app+"/FAC_DevolucionesManual.do?noReset=true";
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
</head>

<script>
	<!-- Refrescar -->
	function refrescarLocal(){ 		
		parent.buscar();
	}	
    
	function verfactura(fila)
	{
		var datos;
	   	datos = document.getElementById('tablaDatosDinamicosD');
	    datos.value = ""; 
	   	var i, j;
	   	
	   	for (i = 0; i < 7; i++)
	   	{
			var tabla;
	      	tabla = document.getElementById('tablaDatos');
	      	
	      	if (i == 0)
	      	{
	        	var flag = true;
	        	j = 1;
	        	
	        	while (flag)
	        	{
	          		var aux = 'oculto' + fila + '_' + j;
	          		var oculto = document.getElementById(aux);
	          		
	          		if (oculto == null)
	          		{
	          			flag = false;
	          		}
	          		else
	          		{
	          			datos.value = datos.value + oculto.value + ',';
	          		}
	          		j++;
				}
	        	datos.value = datos.value + "%"
	      	}
	      	else
	      	{
	      		j = 2;
	      	}
	   	}
	   	
	   	var auxTarget = document.forms[0].target;
	   	document.forms[0].target="mainWorkArea";
	   	document.forms[0].modo.value = "ver";
	   	document.forms[0].submit();
	   	document.forms[0].target=auxTarget;
			
	}

</script>


<body>
		<html:form action="/FAC_DevolucionesManual.do?noReset=true" method="POST" style="display:none">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "recibos"/>
			<html:hidden property = "numeroRecibo"/>
			<html:hidden property = "fechaCargoDesde"/>
			<html:hidden property = "fechaCargoHasta"/>
			<html:hidden property = "titular"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
			
			<siga:TablaCabecerasFijas 
			   nombre = "tablaResultados"
			   borde  = "1"
			   clase  = "tableTitle"
			   nombreCol="facturacion.devolucionManual.seleccion,
			   						facturacion.devolucionManual.numeroRemesa,
			   						facturacion.devolucionManual.numRecibo,
			   						facturacion.devolucionManual.numFactura,
			   						facturacion.devolucionManual.titular,
			   						facturacion.devolucionManual.importe,
			   						facturacion.devolucionManual.motivos,"
			   tamanoCol = "5,10,10,15,15,8,32,5"
			   alto  = "277"
			   ajustePaginador="true"
			   activarFilaSel="true" >

		<%if ((resultado != null) && (resultado.size() > 0)){ %>


	
					<%	 for (int i = 0; i < resultado.size(); i++) { 
							
							  Row fila = (Row)resultado.elementAt(i);
			                    Hashtable recibo = (Hashtable) fila.getRow();
							 if (recibo != null){ 

									String nombre = UtilidadesHash.getString(recibo, CenPersonaBean.C_NOMBRE);
									String apellidos1 = UtilidadesHash.getString(recibo, CenPersonaBean.C_APELLIDOS1);
									String apellidos2 = UtilidadesHash.getString(recibo, CenPersonaBean.C_APELLIDOS2);
									String nom = ((nombre!=null)?nombre:"") +" "+ ((apellidos1!=null)?apellidos1:"") +" "+ ((apellidos2!=null)?apellidos2:"");
									String numRecibo = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO);
									String numRemesa = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS);
									String numFactura = UtilidadesHash.getString(recibo, FacFacturaBean.C_NUMEROFACTURA);
									Double importe = UtilidadesHash.getDouble(recibo, FacFacturaIncluidaEnDisqueteBean.C_IMPORTE);
									String idFactura = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);

									// boton de ver factura
									FilaExtElement[] elems = new FilaExtElement[1];
									elems[0]=new FilaExtElement("verfactura", "verfactura", SIGAConstants.ACCESS_READ);

		   	 		%>
		
							<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" visibleConsulta="false" visibleEdicion="false" visibleBorrado="false" elementos="<%=elems%>" pintarEspacio="no"  clase="listaNonEdit"> 
								<td><input type="checkbox" value="<%=idFactura%>%%<%=numRecibo%>%%<%=importe%>" name="sel"></td>
								<td>
									<!-- Datos ocultos tabla -->
									<input type="hidden" id="oculto<%=(i+1)%>_1" value="<%=idFactura%>">
									<%=UtilidadesString.mostrarDatoJSP(numRemesa)%>
								</td>
								<td>
									<%=UtilidadesString.mostrarDatoJSP(numRecibo)%>
								</td>
								<td><%=UtilidadesString.mostrarDatoJSP(numFactura)%></td>
								<td><%=UtilidadesString.mostrarDatoJSP(nom)%></td>
								<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importe.doubleValue()))%></td>
								<td>
									<siga:ComboBD nombre = "motivoDevolucion" tipo="cmbTipoMotivoDevolucion" clase="boxCombo" elementoSel="<%=motivosSel %>" obligatorio="true" obligatorioSinTextoSeleccionar="true"/>						
								</td>

							</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  %>

	<% } // if  
	else {%>
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
	<% } %>

			</siga:TablaCabecerasFijas>
			
			
							
<!-- Metemos la paginación-->		
	 <%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscar"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
   <%}%>	

<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	

</body>
</html>