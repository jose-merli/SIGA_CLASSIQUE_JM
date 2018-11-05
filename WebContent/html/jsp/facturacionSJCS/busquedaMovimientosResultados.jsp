<!DOCTYPE html>
<html>
<head>
<!-- busquedaMovimientosResultados.jsp -->
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

<%@ page import="com.siga.Utilidades.paginadores.Paginador"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoMovimientosForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	String elTarget = "mainWorkArea";
	
	//recoger de request el vector con los registros resultado
	String esFicha = "0";
	if(request.getAttribute("noFicha")==null){
		esFicha = (String)request.getParameter("esFichaColegial");
	}
	//campos a mostrar en la tabla
	String nif ="", nombre ="", pagoAsociado="", movimiento="", cantidad="", idMovimiento="", ncolegiado="";
	String cantidadRestante="";
	String cantidadAplicada="";

	//botones a mostrar
	String botones="";
	
	String entrada = (String)ses.getAttribute("entrada");
	
	String checkHistoricoMovimiento = (String)ses.getAttribute("checkHistoricoMovimiento");
	String mosMovimiento = (String)ses.getAttribute("MOSTRARMOVIMIENTOS");
	
	String checkHistorico = (String)ses.getAttribute("checkHistorico");
	
	if (entrada != null && entrada.equalsIgnoreCase("2")){
		elTarget="mainPestanas";
	}	
	
	/** PAGINADOR ***/
	String idioma=usrbean.getLanguage().toUpperCase();
	Vector resultado=null;
	String paginaSeleccionada ="";
	String totalRegistros ="";
	String registrosPorPagina = "";
	HashMap hm=new HashMap();

	if (ses.getAttribute("DATAPAGINADOR")!=null){
		hm = (HashMap)ses.getAttribute("DATAPAGINADOR");
	
	 	if (hm.get("datos")!=null && !hm.get("datos").equals("")){
	  		resultado = (Vector)hm.get("datos");
	  		Paginador paginador = (Paginador)hm.get("paginador");
	
	 		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 	
	 	}else{
	  		resultado = new Vector();
	  		paginaSeleccionada = "0";
		 	totalRegistros = "0";
		 	registrosPorPagina = "0";
		}
   
	} else {
    	resultado =new Vector();
		paginaSeleccionada = "0";
	 	totalRegistros = "0";
	 	registrosPorPagina = "0";
   }
	
	String action=app+"/JGR_MovimientosVariosLetrado.do?noReset=true";
	
%>


<!-- HEAD -->


 	<script language="JavaScript">	
	
		function refrescarLocal(){
			parent.buscar2();
		}

		function comprobarCheckHistorico(valorCheck){
			if (valorCheck.checked){
				document.forms[0].mostrarMovimientos.value = "0";
			} else {
				document.forms[0].mostrarMovimientos.value = "1";				
			}

			document.forms[0].modo.value = "abrir";			
			document.forms[0].submit();			
		}		
	</script>


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_MantenimientoMovimientos.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<% if(esFicha != null && esFicha.equalsIgnoreCase("1")){%>
		<siga:TituloExt 
			titulo="censo.fichaCliente.sjcs.movimientos.titulo"  
			localizacion="censo.fichaCliente.sjcs.movimientos.localizacion"/>
	<%}%>
		
</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	    <html:form action="${path}?noReset=true" method="POST" target="<%=elTarget%>"  style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = "" />
			<input type="hidden" name="mostrarMovimientos" value="">
			<input type="hidden"  id="checkHistoricoMovimiento"  name="checkHistoricoMovimiento" value="">
		</html:form>	
		
	<% if(esFicha != null && esFicha.equalsIgnoreCase("1")){%>
	
		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="factSJCS.datosMovimientos.literal.pago,factSJCS.datosMovimientos.literal.descripcion,factSJCS.datosPagos.literal.facturacion,factSJCS.datosFacturacion.literal.gruposFacturacion,factSJCS.datosMovimientos.literal.cantidad,factSJCS.movimiento.literal.aplica,factSJCS.datosMovimientos.literal.cantidadrestante"
		   columnSizes="15,15,15,15,8,8,8"
		   modal="M">
		   
			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
	   		 <td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
	 		</tr>	 		
<%	
	} else { 
		String nombreFacturacion ="";
		String nombreGrupoFacturacion ="";
		for (int cont=1; cont<=resultado.size(); cont++) {
			Row row = (Row) resultado.elementAt(cont-1);
			Hashtable fila = (Hashtable) row.getRow();			
			
			nif = UtilidadesString.mostrarDatoJSP(fila.get("NIF"));
			nombre = UtilidadesString.mostrarDatoJSP(fila.get("NOMBRE"));
			//En la fila, en NUMERO tenemos el numero de colegiado o comunitario segun proceda:
			ncolegiado = UtilidadesString.mostrarDatoJSP(fila.get("NUMERO"));
			pagoAsociado = UtilidadesString.mostrarDatoJSP(fila.get("PAGOASOCIADO"));
			movimiento = UtilidadesString.mostrarDatoJSP(fila.get("MOVIMIENTO"));
			cantidad = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDAD"));
			cantidadAplicada = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDADAPLICADA"));
			cantidadRestante = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDADRESTANTE"));
			idMovimiento = UtilidadesString.mostrarDatoJSP(fila.get("IDMOVIMIENTO"));
			nombreFacturacion = UtilidadesString.mostrarDatoJSP(fila.get("NOMBREFACTURACION"));
			nombreGrupoFacturacion = UtilidadesString.mostrarDatoJSP(fila.get("NOMBREGRUPOFACTURACION"));

%>
  			<siga:FilaConIconos fila="<%=String.valueOf(cont)%>" 
  			
  				botones="" 
	  			pintarEspacio="no"
	  			visibleConsulta="no"
	  			visibleEdicion = "no"
	  			visibleBorrado = "no"
  				clase="listaNonEdit">
			
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idMovimiento%>">
				<%=pagoAsociado %></td>
				<td><%=movimiento %></td>
				<td><%=nombreFacturacion %></td>
				<td><%=nombreGrupoFacturacion %></td>
				<td align="right"><%=UtilidadesNumero.formatoCampo(cantidad)%></td>
				<td align="right"><%=UtilidadesNumero.formatoCampo(cantidadAplicada)%></td>
				<td align="right"><%=UtilidadesNumero.formatoCampo(cantidadRestante)%></td>

			</siga:FilaConIconos>	

<%		} 
	} // del if 
%>		
	

		</siga:Table>	
		
		<div style="position:absolute; left:400px;bottom:0px;z-index:99;">
			<table align="center" border="0" class="botonesSeguido">
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
							<% if (mosMovimiento != null && mosMovimiento.equals("0")) { %>
								<input type="checkbox" id="idCheckHistorico" name="checkHistorico" onclick="comprobarCheckHistorico(this);"checked>
							<% } else { %>
								<input type="checkbox" id="idCheckHistorico" name="checkHistorico" onclick="comprobarCheckHistorico(this);">
							<% } %>										
					</td>
				</tr>
			</table>
		</div>
		<siga:ConjBotonesAccion botones=""	clase="botonesDetalle" />
					
	
	<%} else {%>	

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="factSJCS.datosMovimientos.literal.nifCif,factSJCS.busquedaRetAplicadas.literal.colegiado,factSJCS.datosMovimientos.literal.nColegiado,factSJCS.datosMovimientos.literal.pago,factSJCS.datosMovimientos.literal.descripcion,factSJCS.datosMovimientos.literal.cantidad,factSJCS.movimiento.literal.aplica,factSJCS.datosMovimientos.literal.cantidadrestante,"
		   columnSizes="10,15,8,15,15,8,8,8"
		   modal="M">
		   
			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>		
<%	
	} else { 

		for (int cont=1;cont<=resultado.size();cont++) {
			Row row = (Row) resultado.elementAt(cont-1);
			Hashtable fila = (Hashtable) row.getRow();	

			nif = UtilidadesString.mostrarDatoJSP(fila.get("NIF"));
			nombre = UtilidadesString.mostrarDatoJSP(fila.get("NOMBRE"));
			//En la fila, en NUMERO tenemos el numero de colegiado o comunitario segun proceda:
			ncolegiado = UtilidadesString.mostrarDatoJSP(fila.get("NUMERO"));
			pagoAsociado = UtilidadesString.mostrarDatoJSP(fila.get("PAGOASOCIADO"));
			movimiento = UtilidadesString.mostrarDatoJSP(fila.get("MOVIMIENTO"));
			cantidad = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDAD"));
			cantidadAplicada = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDADAPLICADA"));
			cantidadRestante = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDADRESTANTE"));
			idMovimiento = UtilidadesString.mostrarDatoJSP(fila.get("IDMOVIMIENTO"));
			botones = (String)fila.get("IDESTADOPAGOSJG");
			if ((botones!=null) && (botones.equals("10")||botones.equals("")))
			{
				if(botones.equals("10")) botones="C,E,B";
				if(botones.equals("")) botones="C,E,B";
			}
			else botones = "C";
%>
  			<siga:FilaConIconos fila="<%=String.valueOf(cont)%>" botones="<%=botones%>" clase="listaNonEdit">
			
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idMovimiento%>"><%=nif%></td>
				<td><%=nombre %></td>
				<td><%=ncolegiado %></td>
				<td><%=pagoAsociado %></td>
				<td><%=movimiento %></td>
				<td align="right"><%=UtilidadesNumero.formatoCampo(cantidad)%></td>
				<td align="right"><%=UtilidadesNumero.formatoCampo(cantidadAplicada)%></td>
				<td align="right"><%=UtilidadesNumero.formatoCampo(cantidadRestante)%></td>

			</siga:FilaConIconos>		


<%		} 
	} // del if 
%>		
	

		</siga:Table>
		
<%}%>		
		<!-- FIN: LISTA DE VALORES -->
		
<!-- Metemos la paginaci�n-->		
	 <%if (hm.get("datos")!=null && !hm.get("datos").equals("")){%>
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPaginador"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
   <%}%>			
	
	
	
<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

<!-- FIN: SUBMIT AREA -->

	</body>
</html>
