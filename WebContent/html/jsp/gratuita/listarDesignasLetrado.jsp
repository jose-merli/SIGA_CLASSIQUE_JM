<!-- listarDesignasLetrado.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	request.getSession().removeAttribute("pestanasG");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector resultado = (Vector)request.getAttribute("resultado");
	String orden = (String)request.getAttribute("orden");
	
	//Datos del Colegiado si procede:
	String nombrePestanha = (String)request.getAttribute("NOMBRECOLEGPESTAÑA");
	String numeroPestanha = (String)request.getAttribute("NUMEROCOLEGPESTAÑA");
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
	//Si venimos del menu de Censo tenemos un alto menor ya que ponemos el nombre del colegiado:
	String alto = "315";
	if (entrada!=null && entrada.equals("2"))
		alto = "272";
		
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
		
		
%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="censo.fichaCliente.sjcs.designas.cabecera" 
			localizacion="censo.fichaCliente.sjcs.designas.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->			
</head>

<body class="tablaCentralCampos">

	<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { %>
   		 <table class="tablaTitulo" align="center" cellspacing=0>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.designasLetrado.pestana.titulito"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				    <% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
					<% } else { %>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% } %>
				</td>
			</tr>
		</table>
	<% } %>

<html:form action="JGR_PestanaDesignas.do" method="post" target="mainPestanas" style="display:none">
		<!-- Datos del Colegiado seleccionado -->
		<html:hidden property = "nombreColegiadoPestanha" value = "<%=nombrePestanha%>"/>
		<html:hidden property = "numeroColegiadoPestanha" value = "<%=numeroPestanha%>"/>
		
	<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "abrirAvanzada"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="orden" value="">
		</html:form>	
		

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="gratuita.definirTurnosIndex.literal.abreviatura,censo.SolicitudIncorporacion.literal.nombre,facturacion.ano,gratuita.busquedaSOJ.literal.numero,gratuita.listarDesignasTurno.literal.fechaEntrada,facturacion.estado,gratuita.listarDesignasTurnos.literal.resumen,gratuita.listarDesignasTurno.literal.actPendValidar,"
		   tamanoCol="15,20,8,5,8,8,10,10,9"
		   			alto="100%"
		   			ajuste="70"		

		  >

	<%if ((resultado==null)||(resultado.size()==0)){%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%}else{%>
	
		<%
		    	
	    String nC="";
		String tC="";
		String botones="E";
	    	int contador=1;
			while ((contador) <= resultado.size())
			{	 
				Hashtable hash = (Hashtable)resultado.get(contador-1);
				
			
				
		%>
			<siga:FilaConIconos fila='<%=String.valueOf(contador)%>' botones="<%=botones%>"  visibleConsulta='no' visibleBorrado='no' clase="listaNonEdit">
				<td><input type='hidden' name='oculto<%=String.valueOf(contador)%>_1' value='<%=hash.get("IDINSTITUCION")%>'>
				<input type='hidden' name='oculto<%=String.valueOf(contador)%>_2' value='<%=hash.get("IDTURNO")%>'>			  
				<input type='hidden' name='oculto<%=String.valueOf(contador)%>_3' value='<%=hash.get("NUMERODES")%>'>		  
				<input type='hidden' name='oculto<%=String.valueOf(contador)%>_4' value='<%=hash.get("ANIO")%>'>
				<input type='hidden' name='oculto<%=String.valueOf(contador)%>_5' value='<%=hash.get("IDPERSONA")%>'>	
				<%=hash.get("ABREVIATURA")%>&nbsp;</td>
				<td ><%=hash.get("NOMBRE")%></td>
				<td ><%=hash.get("ANIO")%></td>
				<td><%=hash.get("CODIGO")%>&nbsp;</td>
				<td ><%=GstDate.getFormatedDateShort("",(String)hash.get("FECHAENTRADA"))%></td>
				<% if (hash.get("ESTADO").equals("V")){%>
				  <td ><siga:Idioma key="gratuita.estadoDesignacion.activo"/></td>
				<% }else if (hash.get("ESTADO").equals("F")){%>
				  <td ><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></td>
				<%}else{%>
				  <td ><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></td>
				<%}%> 
				<td ><%=hash.get("RESUMEN")%>&nbsp;</td>
				<td><%=hash.get("ACTNOVALIDADAS")%>&nbsp;</td>
				
			</siga:FilaConIconos>
				
		<%	contador++;}%>
	<%}%>	

		</siga:TablaCabecerasFijas>
	
	<div style="position:absolute; left:400px;bottom:35px;z-index:3">
				<table align="center">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.ordenacion"/>:
						&nbsp;
						<Select name = "orden1" style = "true" class = "box" onChange="document.forms[0].orden.value=this.value;document.forms[0].submit();">
							<option value="0" <%if ((orden!=null)&&(orden.equalsIgnoreCase("0"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.nColegiado"/></option>
							<option value="1" <%if ((orden!=null)&&(orden.equalsIgnoreCase("1"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.nombre"/></option>
							<option value="2" <%if ((orden!=null)&&(orden.equalsIgnoreCase("2"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.anioNumero"/></option>
							<option value="3" <%if ((orden==null)||(orden.equalsIgnoreCase("3"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.fechaEntrada"/></option>
						</Select>
					</td>
				</tr>
				</table>
	</div>

	

<% if (!busquedaVolver.equals("volverNo")) { %>
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
<% } %>
<script language="JavaScript">
	
		//function accionVolver() 
		//{		
		//	document.forms[0].action="JGR_DefinirTurnos.do";
		//	document.forms[0].target="mainWorkArea";
		//	document.forms[0].modo.value="abrirAvanzada";
		//	document.forms[0].submit();
		//}

</script>
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

</body>
</html>
