<!-- listadoPagoColegiados.jsp -->
<!-- 
	 VERSIONES:
	 jtacosta 2009 
-->

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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitiveBind"%>
<%@page import="com.siga.beans.CenColegiadoBean"%>



<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idiomaLabel=userBean.getLanguage().toUpperCase();
	String imprimir = UtilidadesString.getMensajeIdioma(userBean, "general.boton.generarInforme");
	String comunicar = UtilidadesString.getMensajeIdioma(userBean, "general.boton.enviar");
	
	/** PAGINADOR ***/
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	 
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	Vector resultado=null;
	
 if (ses.getAttribute("DATAPAGINADOR")!=null){
	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");

	
	
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
}else{
      resultado =new Vector();
	  paginaSeleccionada = "0";
	
	 	totalRegistros = "0";
	
	 	registrosPorPagina = "0";
}	 
	
	
	String action=app+"/INF_CartaPago.do";
    /**************/
	
	
%>	

	


<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="informes.sjcs.pagos.literal.titulo" 
			localizacion="facturacion.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body class="tablaCentralCampos">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/INF_CartaPago.do" method="POST" target="submitArea" style="display:none">			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property = "idInstitucion"/>
			<html:hidden property = "idPago"/>
			<html:hidden property = "idioma"/>
			<html:hidden property = "idPersona"/>
			
			
			
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD" value = "">
			<input type="hidden" name="actionModal" value="">
			
			
		</html:form>
		
		<!-- Formulario para la creacion de envio -->
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea" style="display:none">
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>
			<html:hidden property = "subModo" value = ""/>
			<html:hidden property = "filaSelD" value=""/>
			<html:hidden property = "idPersona" value=""/>
			<html:hidden property = "descEnvio" value=""/>
			<html:hidden property = "datosEnvios" value=""/>
			
			
		</html:form>
				
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol=" <input type='checkbox' onclick='marcarDesmarcarTodos(this)'/> ,
		   		   	informes.sjcs.pagos.literal.ncolegiado,
				  	informes.sjcs.pagos.literal.colegiado,
				  	factSJCS.datosPagos.literal.importeSJCS,
		   		  	factSJCS.datosPagos.literal.importeMovimientosVarios,
		   		  	informes.sjcs.pagos.literal.importeBruto,
		   		  	informes.sjcs.pagos.literal.importeIRPF,
		   		  	factSJCS.datosPagos.literal.importeRetenciones,
		   		  	factSJCS.detalleFacturacion.literal.importe,"
		   		  tamanoCol="5,9,18,10,10,10,10,10,10,8"
		   		  alto="70%"
		   		  ajusteBotonera="true"
		   		  activarFilaSel="true"	
				  ajustePaginador="true">
	   		     		    		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (resultado==null || resultado.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<resultado.size(); i++)
			   		{
				  		Row fila = (Row)resultado.elementAt(i);
						FilaExtElement[] elemento=new FilaExtElement[2];
				  		elemento[0]=new FilaExtElement("enviar","enviar",SIGAConstants.ACCESS_READ);
				  		elemento[1]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);
						
%>
	  			<siga:FilaConIconos 
	  				fila='<%=""+(i+1)%>'
	  				botones=""
	  				visibleConsulta = "no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				elementos='<%=elemento%>' 
					pintarEspacio = "no"
	  				clase="listaNonEdit">
	  					
	  					
						<input type="hidden" name="idPersona<%=""+(i+1)%>" value="<%=fila.getString("IDPERSONASJCS")%>">
						
						
					<td align="center">
						<input type="checkBox" name="comunica" id="<%="comunica"+(i+1)%>"  >
					</td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(CenColegiadoBean.C_NCOLEGIADO))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRE"))%></td>
					<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("TOTALIMPORTESJCS"),2))%>&nbsp;&euro;</td>
					<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("IMPORTETOTALMOVIMIENTOS"),2))%>&nbsp;&euro;</td>
					<%
					float aux = Float.parseFloat(fila.getString("TOTALIMPORTESJCS")) + Float.parseFloat(fila.getString("IMPORTETOTALMOVIMIENTOS"));
					String importe = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea((new Float(aux)).toString(),2));
					%>
					<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(importe,2))%>&nbsp;&euro;</td>
					<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("TOTALIMPORTEIRPF"),2))%>&nbsp;&euro;</td>
					<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(fila.getString("IMPORTETOTALRETENCIONES"),2))%>&nbsp;&euro;</td>
					<%
					aux = aux + Float.parseFloat(fila.getString("TOTALIMPORTEIRPF"))  + Float.parseFloat(fila.getString("IMPORTETOTALRETENCIONES"));
					importe = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea((new Float(aux)).toString(),2));
					%>
					<td align="right"><%=UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(importe,2))%>&nbsp;&euro;</td>
			
				</siga:FilaConIconos>
<%
					}
				}
%>

           
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:TablaCabecerasFijas>
			<table class="botonesDetalle" >
				<tr>
					<td style="text-align:right" >
					<input type="button" class="button" alt="<%=imprimir%>" name="idButton"  onclick="accionDownload();" value="<%=imprimir%>"/>&nbsp;
					<input type="button" class="button" alt="<%=comunicar%>" name="idButton"  onclick="enviarPagos();" value="<%=comunicar%>"/>&nbsp;
					</td>
				</tr>
			</table>
			
  
			

<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idiomaLabel%>"
								modo="buscar"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
      <%}%>
		<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">

		
		function refrescarLocal()
		{			
			parent.buscarPaginador() ;			
		}
		function download(fila)
		{
			
			sub();
			var idPersona = "idPersona"+fila;
			document.mantenimientoInformesForm.idPersona.value = document.getElementById(idPersona).value;
			document.mantenimientoInformesForm.idPago.value = parent.document.mantenimientoInformesForm.idPago.value;
			document.mantenimientoInformesForm.modo.value = "download";
			document.mantenimientoInformesForm.submit();
			return true;
				
		}
		
		
		function accionDownload()
		{
			sub();
			document.mantenimientoInformesForm.idPersona.value = "";
			document.mantenimientoInformesForm.modo.value = "download";
			document.mantenimientoInformesForm.idPago.value = parent.document.mantenimientoInformesForm.idPago.value;
			document.mantenimientoInformesForm.submit();
			return true;
			
		}
		
		
		//Guardo los campos seleccionados
		function seleccionarFila(fila){
		    var idPersona = "idPersona"+fila;
			document.mantenimientoInformesForm.idPersona.value = document.getElementById(idPersona).value;
			datos = idPersona + "," +idFactura + "#"; 
			document.forms[0].tablaDatosDinamicosD.value = datos;
		}
		

		function enviar(fila)
		{
			var idPers = "idPersona"+fila;
			idPersona = document.getElementById(idPers).value;
			idPago = parent.document.mantenimientoInformesForm.idPago.value;
			idInstitucion = document.mantenimientoInformesForm.idInstitucion.value;
			idioma = parent.document.mantenimientoInformesForm.idioma.value;
		   	datos = "idPersona=="+idPersona + "##idPago==" +idPago + "##idInstitucion==" +idInstitucion + "##idioma==" +idioma +"%%%"; 
			
			document.DefinirEnviosForm.filaSelD.value = fila;
			
      	    document.DefinirEnviosForm.datosEnvios.value = datos;
		    
		    
		   	
		   	document.DefinirEnviosForm.modo.value='envioModal';		   	
		   	document.DefinirEnviosForm.subModo.value='pagoColegiados';		   	

		   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
		   	if (resultado==undefined||resultado[0]==undefined){			   		
		   		refrescarLocal();
		   	} else {
		   		var idEnvio = resultado[0];
			    var idTipoEnvio = resultado[1];
			    var nombreEnvio = resultado[2];				    
			   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.DefinirEnviosForm.modo.value='editar';
			   	document.DefinirEnviosForm.submit();
		   	}
					
					
				
		}
		
		
	function enviarPagos()
	{
		sub();
	   	filas = 0;
			
			<% if (resultado!=null) { %>
				filas = eval("<%=resultado.size()%>");
			<% } %>
			datos = "";
						
			for (i = 0; i < filas; i++) {
			
				if(document.getElementById ('comunica'+(i+1)).checked){
					
					idPersona = document.getElementById ('idPersona'+(i+1)).value;
					idPago = parent.document.mantenimientoInformesForm.idPago.value;
					idInstitucion = document.mantenimientoInformesForm.idInstitucion.value;
					idioma = parent.document.mantenimientoInformesForm.idioma.value;
				   	datos = datos +  "idPersona=="+idPersona + "##idPago==" +idPago + "##idInstitucion==" +idInstitucion + "##idioma==" +idioma +"%%%";
						
				} 
								 
			}
			
			
			if (datos == '') {
				
				alert ('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
				return;
			}
			document.DefinirEnviosForm.datosEnvios.value = datos;
		    
		    
		   	
		   	
		   	document.DefinirEnviosForm.modo.value='envioModal';		   	
		   	document.DefinirEnviosForm.subModo.value='pagoColegiados';		   	

		   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
		   	if (resultado==undefined||resultado[0]==undefined){			   		
		   		refrescarLocal();
		   	} else {
		   		var idEnvio = resultado[0];
			    var idTipoEnvio = resultado[1];
			    var nombreEnvio = resultado[2];				    
			   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.DefinirEnviosForm.modo.value='editar';
			   	document.DefinirEnviosForm.submit();
		   	}
	}
	
	
			
		
			function marcarDesmarcarTodos(o,numeroChekBox) 
			{
				var ele = document.getElementsByName('comunica');
				for (i = 0; i < ele.length; i++) {
					ele[i].checked = o.checked;
					
				}
			}

	</script>
	
	</body>
</html>