<!-- listarDesignasTurno.jsp -->	 
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
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	request.getSession().removeAttribute("pestanasG");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector resultado = (Vector)request.getAttribute("resultado");
	Vector campos = (Vector)request.getSession().getAttribute("campos");
	String orden = (String)request.getAttribute("orden");
	ScsTurnoBean beanTurno= (ScsTurnoBean) request.getAttribute("datosTurno");
	String anio = (String)request.getAttribute("anio");
	anio = anio!=null ? anio : "";
	String accionTurno = (String)request.getSession().getAttribute("accionTurno");
	String botones="C,E";
	if (accionTurno.equalsIgnoreCase("Ver"))botones="C";
%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="Busqueda Designas" 
		localizacion="gratuita.turnos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>
<body class="tablaCentralCampos">

	<html:form action="JGR_DesignasRealizadas.do" method="post" target="mainWorkArea" style="display:none">
		
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = "abrirAvanzada"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="orden" value="">
			<input type="hidden" name="anio" value="<%=anio%>">
		</html:form>	
		
	<!--INICIO INFORMACION DEL TURNO SELECCIONADO-->
	<fieldset>
		<table border="0" align="center">
		<tr>
			<td width="10%" class="labelText" >
				<siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>
			</td>
			<td width="20%" >
				<input name="abreviatura" type="text" class="boxConsulta" size="30" maxlength="30" value="<%=beanTurno.getAbreviatura()%>" readonly="true">
			</td>
			<td class="labelText" width="10%">
				<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>
			</td>
			<td colspan="2" width="50%" >
				<input name="nombre" type="text" class="boxConsulta" size="60" maxlength="100" value="<%=beanTurno.getNombre()%>" readonly="true" >
			</td>
		</tr>
		</table>
	</fieldset>


		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="gratuita.listarDesignasTurno.literal.nColegiado,administracion.certificados.literal.nombre,facturacion.ano,gratuita.busquedaSOJ.literal.numero,gratuita.listarDesignasTurno.literal.codigo,gratuita.listarDesignasTurno.literal.fechaEntrada,facturacion.estado,gratuita.listarDesignasTurnos.literal.resumen,"
		   tamanoCol="10,15,10,10,7,8,15,15,10"
		   			alto="100%"
		   			ajuste="100"		
		  >

	<%if ((resultado==null)||(resultado.size()==0)){%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<% } else{%>

	<!-- FIN INFORMACION DEL TURNO SELECCIONADO-->
	
		<%
	    	int contador=1;
			while ((contador) <= resultado.size())
			{	 
				Hashtable hash = (Hashtable)resultado.get(contador-1);
		%>
				<siga:FilaConIconos fila='<%=String.valueOf(contador)%>' visibleBorrado="no" botones="<%=botones%>" clase="listaNonEdit">
				<td><input type='hidden' name='oculto<%=String.valueOf(contador)%>_1' value='<%=hash.get("IDTURNO")%>'><%=hash.get("NCOLEGIADO")%>&nbsp;</td>
				<td><%=hash.get("NOMBRECOLEGIADO")%> <%=hash.get("AP1")%> <%=hash.get("AP2")%>&nbsp;</td>
				<td><%=hash.get("ANIO")%>&nbsp;</td>
				<td><%=hash.get("NUMERODES")%>&nbsp;</td>
				<td><%=hash.get("CODIGO")%>&nbsp;</td>
				<td><%=GstDate.getFormatedDateShort("",(String)hash.get("FECHAENTRADA"))%>&nbsp;</td>
				<% if (hash.get("ESTADO").equals("V")){%>
				  <td ><siga:Idioma key="gratuita.estadoDesignacion.activo"/></td>
				<% }else if (hash.get("ESTADO").equals("F")){%>
				  <td ><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></td>
				<%}else{%>
				  <td ><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></td>
				<%}
				%> 
				<td><%=(hash.get("RESUMEN")==null || hash.get("RESUMEN").equals(""))?"&nbsp;":hash.get("RESUMEN")%></td>
				</siga:FilaConIconos>
		<%	contador++;}%>
	<%}%>	
		</siga:TablaCabecerasFijas>
	
	<div style="position:absolute; left:400px;bottom:35px;z-index:3">
		<table align="center">
		<tr>
			<td class="labelText">
				<input name="nombre" type="text" class="boxConsulta" size="12" maxlength="12" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.ordenar"/>' readonly="true" >
				<Select name = "orden1" style = "true" class = "box" onChange="accionOrdenar(this);">
							<option value="0" <%if ((orden!=null)&&(orden.equalsIgnoreCase("0"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.nColegiado"/></option>
							<option value="1" <%if ((orden!=null)&&(orden.equalsIgnoreCase("1"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.nombre"/></option>
							<option value="2" <%if ((orden!=null)&&(orden.equalsIgnoreCase("2"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.anioNumero"/></option>
							<option value="3" <%if ((orden==null)||(orden.equalsIgnoreCase("3"))){%>selected<%}%>><siga:Idioma key="gratuita.listarDesignasTurno.literal.fechaEntrada"/></option>
				</Select>
			</td>
		</tr>
		</table>				
	</div>
	
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />

<script language="JavaScript">
	
		function accionVolver() 
		{		
			document.forms[0].action="JGR_DefinirTurnos.do";
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].submit();
		}

		function accionOrdenar(ob) 
		{		

			document.forms[0].orden.value=ob.value;
			document.forms[0].action="JGR_DesignasRealizadas.do";
			document.forms[0].target="_self";
			document.forms[0].modo.value="buscarPor";
			document.forms[0].submit();
		}

</script>
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>
