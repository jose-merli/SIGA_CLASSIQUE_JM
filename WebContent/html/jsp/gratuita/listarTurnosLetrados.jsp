<!-- listarTurnosLetrado.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
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
	// Creo el boton de validar
	FilaExtElement[] elems=new FilaExtElement[2];
	elems[0]=new FilaExtElement("validar","validar",SIGAConstants.ACCESS_FULL);
//	elems[1]=new FilaExtElement("validarbaja","validarbaja",SIGAConstants.ACCESS_FULL);
	String titulo = (String) request.getAttribute("titulo");	
	String localizacion = (String) request.getAttribute("localizacion");	
%>
<html>
	<head>
	<title><"listarTurnosLetrados.title"></title>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
<script>

	function validar(fila) 
	{
	 	   var institucion 	= 'oculto' + fila + '_' + 1;
		   var persona 			= 'oculto' + fila + '_' + 2;
		   var turno 				= 'oculto' + fila + '_' + 3;
		   var fsoli 				= 'oculto' + fila + '_' + 4;
		   var osoli 				= 'oculto' + fila + '_' + 5;
		   var fvali 				= 'oculto' + fila + '_' + 6;
		   var ovali 				= 'oculto' + fila + '_' + 7;
		   document.forms[0].idInstitucion.value 	= document.getElementById(institucion).value;
		   document.forms[0].idPersona.value 			= document.getElementById(persona).value;
		   document.forms[0].idTurno.value 				= document.getElementById(turno).value;
		   document.forms[0].fechaSolicitud.value = document.getElementById(fsoli).value;
		   document.forms[0].observacionesSolicitud.value = document.getElementById(osoli).value;
		   document.forms[0].fechaValidacion.value = document.getElementById(fvali).value;
		   document.forms[0].observacionesValidacion.value = document.getElementById(ovali).value;
	
		   document.forms[0].modo.value = "Ver";
		   var resultado = ventaModalGeneral(document.forms[0].name,"G");
	       if(resultado == "MODIFICADO") refrescarLocal("<%=app%>/JGR_ValidarTurnos.do");
	 }
	 
	function validarbaja(fila) 
	{
	 	   var institucion 	= 'oculto' + fila + '_' + 1;
		   var persona 			= 'oculto' + fila + '_' + 2;
		   var turno 				= 'oculto' + fila + '_' + 3;
		   var fsoli 				= 'oculto' + fila + '_' + 4;
		   var osoli 				= 'oculto' + fila + '_' + 5;
		   document.forms[0].idInstitucion.value 	= document.getElementById(institucion).value;
		   document.forms[0].idPersona.value 			= document.getElementById(persona).value;
		   document.forms[0].idTurno.value 				= document.getElementById(turno).value;
		   document.forms[0].fechaSolicitud.value = document.getElementById(fsoli).value;
		   document.forms[0].observacionesSolicitud.value = document.getElementById(osoli).value;
	
		    document.forms[0].modo.value = "Ver";
			document.forms[0].action		= "<%=app%>/JGR_BajaTurnos.do";
		   	var resultado = ventaModalGeneral(document.forms[0].name,"G"); 
	     	if(resultado == "MODIFICADO") refrescarLocal("<%=app%>/JGR_BajaTurnos.do");
	 }

	function refrescarLocal(action)
	{
		document.forms[0].action		= action;
		document.forms[0].modo.value	= "buscarPor";
		document.forms[0].submit();
	}

</script>
		
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="<%=titulo%>" 
		localizacion="<%=localizacion%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>
<body>

<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="<%=titulo%>"/>
	</td>
</tr>
</table>

	<% 	
		String nC="";
		String tC="";
		String botones="";
		String alto="";
	  nC="gratuita.listaTurnosLetrados.literal.numeroletrado,gratuita.listaTurnosLetrados.literal.nombreletrado,gratuita.listaTurnosLetrados.literal.turno,gratuita.listaTurnosLetrados.literal.fechasolicitud,";
		tC="10,25,30,10,15";
		botones="";
		alto="440";
	%>
		<html:form action="JGR_ValidarTurnos.do" method="post" style="display:none">
		
			<input type="hidden" name="modo" />
			<input type="hidden" name="paso" value="turno"/>

			<!-- campos a pasar -->
			<input type="hidden" name="idInstitucion" />
			<input type="hidden" name="idPersona" />
			<input type="hidden" name="idTurno" />
			<input type="hidden" name="fechaSolicitud"/>
			<input type="hidden" name="observacionesSolicitud"/>
			<input type="hidden" name="fechaValidacion"/>
			<input type="hidden" name="observacionesValidacion"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	

		<siga:TablaCabecerasFijas 
		   nombre="listarTurnosLetrados"
		   borde="2"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   alto="100%"
		   activarFilaSel="true" >
		   
		<%if (obj != null && obj.size()>0){%>
				<%
				String fecha = "";
		    	int recordNumber=1;
				while ((recordNumber) <= obj.size())
				{	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				%>
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" visibleBorrado="no">
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDINSTITUCION")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDPERSONA")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("IDTURNO")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=hash.get("FECHASOLICITUD")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=hash.get("OBSERVACIONESSOLICITUD")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=hash.get("FECHAVALIDACION")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_7' value='<%=hash.get("OBSERVACIONESVALIDACION")%>' />
						<td  align="center"><%=UtilidadesString.mostrarDatoJSP(hash.get("NCOLEGIADO"))%></td>
						<td  align="center"><%=UtilidadesString.mostrarDatoJSP(hash.get("NOMBRE_PERSONA"))%> <%=hash.get("APELLIDO1_PERSONA")%> <%=hash.get("APELLIDO2_PERSONA")%></td>
						<td  align="center"><%=UtilidadesString.mostrarDatoJSP(hash.get("NOMBRE_TURNO"))%></td>
						<%
						// Formateamos la fecha
						fecha = GstDate.getFormatedDateShort(usr.getLanguage(),(String)hash.get("FECHASOLICITUD"));
						%>
						<td  align="center"><%=fecha%></td>
					</siga:FilaConIconos>
					<% recordNumber++;
				} %>
		<%}else{%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
		<%}%>
		</siga:TablaCabecerasFijas>

	</body>
</html>
