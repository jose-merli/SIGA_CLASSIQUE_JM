<!-- guardiasTurno.jsp -->
 <meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) request.getAttribute("resultado");
	String fechainscripcion 		= (String) request.getAttribute("fechainscripcion");
	String observacionesinscripcion = (String) request.getAttribute("observacionesinscripcion");
	String fechabaja 		= (String) request.getAttribute("fechabaja");
	String observacionesbaja = (String) request.getAttribute("observacionesbaja");
	// Obtenemos los datos para el proceso
	String action 	= (String) request.getAttribute("action");	
	String modo		= (String) request.getAttribute("modo");	
	String titulo 	= (String) request.getAttribute("TITULO");	
	String texto 	= (String) request.getAttribute("texto");	
	
%>
<html>
	<head>
	<title><siga:Idioma key="gratuita.listarTurnosDisp.literal.title"/></title>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
</head>
<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="<%=titulo%>"/>
		</td>
	</tr>
	</table>
	<% 	String nC="";
		String tC="";
		String botones="";
		String alto="";
		nC="gratuita.guardiasTurno.literal.guardia,gratuita.guardiasTurno.literal.obligatoriedad,gratuita.guardiasTurno.literal.tipodia,gratuita.guardiasTurno.literal.duracion,gratuita.guardiasTurno.literal.letradosguardia,gratuita.guardiasTurno.literal.letradossustitutos,";
		tC="25,20,15,10,10,10,10";
		botones="";
		alto="370";
	%>
<div id="camposRegistro" class="posicionModalGrande" align="center">

	<table  class="tablaCentralCampos"  align="center">
		<html:form action = "<%=action%>" method="POST" target="submitArea">
		<input type="hidden" name="modo" value="<%=modo%>">
		<input type="hidden" name="guardias" value="1">
		<input type="hidden" name="fechaInscripcion"  value="<%=fechainscripcion%>"/>
		<input type="hidden" name="observacionesInscripcion" value="<%=observacionesinscripcion%>"/>
		<input type="hidden" name="fechaBaja"  value="<%=fechabaja%>"/>
		<input type="hidden" name="observacionesBaja" value="<%=observacionesbaja%>"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
	<tr>
		<td>
	
		<siga:TablaCabecerasFijas 
		   nombre="guardiasTurno"
		   borde="2"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   			alto="100%"
		   			ajusteBotonera="true"		
		   			ajustePaginador="true"

		   modal="G"
		   >
	<%if (obj.size()>0){%>
			<%
	    	int recordNumber=1;
			while ((recordNumber) <= obj.size())
			{	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>"  clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" visibleBorrado="no">
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("NOMBRE")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("GUARDIAS")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=hash.get("DIASGUARDIA")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=hash.get("NUMEROLETRADOSGUARDIA")%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=hash.get("NUMEROSUSTITUTOSGUARDIA")%>'>
					<td><%=hash.get("NOMBRE")%></td>
					<td><%=hash.get("GUARDIAS")%></td>
					<td><%=ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr)%></td>
					<td><%=hash.get("DIASGUARDIA")%></td>
					<td><%=hash.get("NUMEROLETRADOSGUARDIA")%></td>
					<td><%=hash.get("NUMEROSUSTITUTOSGUARDIA")%></td>
				</siga:FilaConIconos>
				<% recordNumber++;
			} %>
		<%}else{%>
		<tr><td colspan="8" align="center">
			<p class="labelText" style="text-align:center">
					<siga:Idioma key="gratuita.retenciones.noResultados"/>
			</p></td>
		</tr>
		<%}%>
		</siga:TablaCabecerasFijas>

		<div style="position:absolute;bottom:45px;"
			<p class="labelText" style="text-align:center">
				<siga:Idioma key="<%=texto%>"/>
			</p>
		</div>

			<siga:ConjBotonesAccion botones="C,F" clase="botonesDetalle"  />	

</div>


	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() 
		{		
	
		}

		function accionCerrar() 
		{		
			window.returnValue="CANCELADO";			
			window.close();
		}
		
		function accionRestablecer() 
		{		
	
		}
		
		function accionGuardar() 
		{		
	
		}
		
		function accionGuardarCerrar() 
		{		
	
		}
		
		function accionSiguiente() 
		{		
		}
		
		function accionContinuar()
		{
		}

		function accionFinalizar() 
		{		
			sub();
			document.forms[0].submit();
		}

	</script>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
