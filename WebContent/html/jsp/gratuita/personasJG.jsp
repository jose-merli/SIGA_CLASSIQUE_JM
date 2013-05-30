<!-- personasJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.gratuita.form.AsistenciasForm"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) request.getAttribute("resultadoNIF");
	FilaExtElement[] elems=new FilaExtElement[1];
	request.getSession().removeAttribute("resultadoTelefonos");
	elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	
%>
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script language="JavaScript">
	<%request.getSession().setAttribute("buscarGuardias","1");%>
	function seleccionar(fila) 
	{
   	    var idPersona 			= 'oculto' + fila + '_' + 1;
   	    var nif 				= 'oculto' + fila + '_' + 2;
   	    var nombre 				= 'oculto' + fila + '_' + 3;
   	    var apellido1 			= 'oculto' + fila + '_' + 4;
   	    var apellido2 			= 'oculto' + fila + '_' + 5;
   	    var direccion 			= 'oculto' + fila + '_' + 6;
   	    var cp 					= 'oculto' + fila + '_' + 7;
   	    var pais				= 'oculto' + fila + '_' + 8;
   	    var provincia 			= 'oculto' + fila + '_' + 9;
   	    var poblacion 			= 'oculto' + fila + '_' + 10;
   	    var ecivil	 			= 'oculto' + fila + '_' + 11;
   	    var rconyugal 			= 'oculto' + fila + '_' + 12;
   	    var fechaNacimiento		= 'oculto' + fila + '_' + 13;
		var aux = new Array();
		aux[0]=document.getElementById(idPersona).value;
		aux[1]=document.getElementById(nif).value;
		aux[2]=document.getElementById(nombre).value;
		aux[3]=document.getElementById(apellido1).value;
		aux[4]=document.getElementById(apellido2).value;
		aux[5]=document.getElementById(direccion).value;
		aux[6]=document.getElementById(cp).value;
		aux[7]=document.getElementById(pais).value;
		aux[8]=document.getElementById(provincia).value;
		aux[9]=document.getElementById(poblacion).value;
		aux[10]=document.getElementById(ecivil).value;
		aux[11]=document.getElementById(rconyugal).value;
		aux[12]=document.getElementById(fechaNacimiento).value;
		top.cierraConParametros(aux);
	}
	function accionNuevo() 
	{
		var aux = new Array();
		aux[0]="";
		top.cierraConParametros(aux);
	}
	
	</script>
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.personaJG.literal.titulo"/>
		</td>
	</tr>
	</table>
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<% 	
		String nC="";
		String tC="";
		String botones="";
		String alto="200";
	  	nC="gratuita.personasJG.literal.nif,gratuita.personaJG.nombreyapellidos,";
		tC="12,70,";
	%>
	<html:form action="/JGR_MantenimientoAsistencia.do" method="post" target="mainWorkArea">
		
		<input type="hidden" name="modo" />
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		<!-- campos a pasar -->
		<siga:Table 
		   name="guardiasColegiadoAsistencia"
		   border="2"
		   columnNames="<%=nC%>"
		   columnSizes="<%=tC%>">
		<%if (obj != null && obj.size()>0){%>
				<%
				String fecha = "";
		    	int recordNumber=1;
				while ((recordNumber) <= obj.size())
				{	 
					ScsPersonaJGBean bean = (ScsPersonaJGBean)obj.get(recordNumber-1);
				%>
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit">
						<td  align="center"><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=bean.getIdPersona()%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=bean.getNif()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=bean.getNombre()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=bean.getApellido1()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=bean.getApellido2()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=bean.getDireccion()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_7' value='<%=bean.getCodigoPostal()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_8' value='<%=bean.getIdPais()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_9' value='<%=bean.getIdProvincia()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_10' value='<%=bean.getIdPoblacion()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_11' value='<%=bean.getIdEstadoCivil()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_12' value='<%=bean.getRegimenConyugal()%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_13' value='<%=GstDate.getFormatedDateShort(usr.getLanguage(),bean.getFechaNacimiento())%>'>
						<%=bean.getNif()%></td>
						<td  align="center"><%=bean.getNombre()%>&nbsp;<%=bean.getApellido1()%>&nbsp;<%=bean.getApellido2()%></td>
					</siga:FilaConIconos>
					<% recordNumber++;
				} %>
		<%}else{%>
		<tr>
			<td colspan="3" align="center">
				<p class="nonEditRed" style="text-align:center">
					<siga:Idioma key="gratuita.retenciones.noResultados"/>
				</p>
			</td>
		</tr>
		<%}%>
		</siga:Table>
			<siga:ConjBotonesAccion botones="n" clase="botonesDetalle" modal="M"/>	

	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		<!-- Funcion asociada a boton limpiar -->
		function accionCerrar() 
		{		
			window.top.close();
		}

	</script>


			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>