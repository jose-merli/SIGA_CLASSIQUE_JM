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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	String elTarget = "mainWorkArea";
	
	//recoger de request el vector con los registros resultado
	Vector resultado = (Vector) request.getAttribute("resultado");
	String esFicha = (String)request.getParameter("esFichaColegial");
	
	//campos a mostrar en la tabla
	String nif ="", nombre ="", pagoAsociado="", movimiento="", cantidad="", idMovimiento="", ncolegiado="";
	String cantidadRestante="";

	//botones a mostrar
	String botones="";
	
	String entrada = (String)ses.getAttribute("entrada");
	
	String checkHistoricoMovimiento = (String)ses.getAttribute("checkHistoricoMovimiento");
	
	String checkHistorico = (String)ses.getAttribute("checkHistorico");
	
	if (entrada != null && entrada.equalsIgnoreCase("2")){
		elTarget="mainPestanas";
	}	
	
	
	
%>

<html>
<!-- HEAD -->
<head>

 	<script language="JavaScript">	
	
		function refrescarLocal() 
		{
			parent.buscar2();
		}

		function comprobarCheckHistorico(valorCheck){
			if (!valorCheck.checked){
				document.forms[0].checkHistoricoMovimiento.value="false";
			} else document.forms[0].checkHistoricoMovimiento.value="true";

			document.forms[0].modo.value = "abrir";
			
			document.forms[0].submit();			
		}		
	</script>


	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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
	
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			
			<input type="hidden" name="checkHistoricoMovimiento" value="">
		</html:form>	
		
	<% if(esFicha != null && esFicha.equalsIgnoreCase("1")){%>
	
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="factSJCS.datosMovimientos.literal.nifCif,factSJCS.busquedaRetAplicadas.literal.colegiado,factSJCS.datosMovimientos.literal.nColegiado,factSJCS.datosMovimientos.literal.pago,factSJCS.datosMovimientos.literal.descripcion,factSJCS.datosMovimientos.literal.cantidad,factSJCS.datosMovimientos.literal.cantidadrestante,"
		   tamanoCol="10,20,10,15,15,10,10"
		   alto="310"
		   modal="M" 
		   activarFilaSel="true" >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
<%	
	} else { 

		for (int cont=1;cont<=resultado.size();cont++) {
			Hashtable fila = (Hashtable) resultado.get(cont-1);

			nif = UtilidadesString.mostrarDatoJSP(fila.get("NIF"));
			nombre = UtilidadesString.mostrarDatoJSP(fila.get("NOMBRE"));
			//En la fila, en NUMERO tenemos el numero de colegiado o comunitario segun proceda:
			ncolegiado = UtilidadesString.mostrarDatoJSP(fila.get("NUMERO"));
			pagoAsociado = UtilidadesString.mostrarDatoJSP(fila.get("PAGOASOCIADO"));
			movimiento = UtilidadesString.mostrarDatoJSP(fila.get("MOVIMIENTO"));
			cantidad = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDAD"));
			cantidadRestante = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDADRESTANTE"));
			idMovimiento = UtilidadesString.mostrarDatoJSP(fila.get("IDMOVIMIENTO"));

%>
  			<siga:FilaConIconos fila="<%=String.valueOf(cont)%>" 
  			
  				botones="" 
	  			pintarEspacio="no"
	  			visibleConsulta="no"
	  			visibleEdicion = "no"
	  			visibleBorrado = "no"
  				clase="listaNonEdit">
			
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idMovimiento%>"><%=nif%></td>
				<td><%=nombre %></td>
				<td><%=ncolegiado %></td>
				<td><%=pagoAsociado %></td>
				<td><%=movimiento %></td>
				<td><%=UtilidadesNumero.formatoCampo(cantidad)%></td>
				<td><%=UtilidadesNumero.formatoCampo(cantidadRestante)%></td>

			</siga:FilaConIconos>	

<%		} 
	} // del if 
%>		
	

		</siga:TablaCabecerasFijas>	
	
	<%} else {%>	

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="factSJCS.datosMovimientos.literal.nifCif,factSJCS.busquedaRetAplicadas.literal.colegiado,factSJCS.datosMovimientos.literal.nColegiado,factSJCS.datosMovimientos.literal.pago,factSJCS.datosMovimientos.literal.descripcion,factSJCS.datosMovimientos.literal.cantidad,factSJCS.datosMovimientos.literal.cantidadrestante,"
		   tamanoCol="10,20,10,15,15,10,10,10"
		   alto="310"
		   modal="M" 
		   activarFilaSel="true" >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
<%	
	} else { 

		for (int cont=1;cont<=resultado.size();cont++) {
			Hashtable fila = (Hashtable) resultado.get(cont-1);

			nif = UtilidadesString.mostrarDatoJSP(fila.get("NIF"));
			nombre = UtilidadesString.mostrarDatoJSP(fila.get("NOMBRE"));
			//En la fila, en NUMERO tenemos el numero de colegiado o comunitario segun proceda:
			ncolegiado = UtilidadesString.mostrarDatoJSP(fila.get("NUMERO"));
			pagoAsociado = UtilidadesString.mostrarDatoJSP(fila.get("PAGOASOCIADO"));
			movimiento = UtilidadesString.mostrarDatoJSP(fila.get("MOVIMIENTO"));
			cantidad = UtilidadesString.mostrarDatoJSP(fila.get("CANTIDAD"));
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
				<td><%=UtilidadesNumero.formatoCampo(cantidad)%></td>
				<td><%=UtilidadesNumero.formatoCampo(cantidadRestante)%></td>

			</siga:FilaConIconos>		


<%		} 
	} // del if 
%>		
	

		</siga:TablaCabecerasFijas>
		
<%}%>		

<% if(esFicha != null && esFicha.equalsIgnoreCase("1")){%>
			<!-- Introducción del check de histórico -->
	
		<div style="position:absolute; left:200px;bottom:50px;z-index:2;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
							<% if (checkHistoricoMovimiento != null && checkHistoricoMovimiento.equals("true")) { %>
								<input type="checkbox" name="checkHistorico" property="checkHistorico" onclick="comprobarCheckHistorico(this);" checked>
							<% } else { %>						
						<input type="checkbox" name="checkHistorico" property="checkHistorico" onclick="comprobarCheckHistorico(this);">
							<% } %>										
					</td>
				</tr>
			</table>
		</div>	
	
	<%}%>		

		<!-- FIN: LISTA DE VALORES -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<!-- 	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>-->  


	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

<!-- FIN: SUBMIT AREA -->

	</body>
</html>
