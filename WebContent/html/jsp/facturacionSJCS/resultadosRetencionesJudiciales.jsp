<!-- resultadosRetencionesJudiciales.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="java.util.*"%>


<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector) request.getAttribute("resultado");
	String esFicha = (String)request.getParameter("esFichaColegial");
	
	Hashtable fila = new Hashtable();
	
	String botones = "C,E,B";
	String fechaHoy = UtilidadesBDAdm.getFechaBD("");
	String fechaNotificacionFin = "";
	
	
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="FactSJCS.mantRetencionesJ.cabecera"/></title>
	<script type="text/javascript">
		function refrescarLocal(){
			parent.buscar();
		}
		
	</script>
	
	<% if(esFicha != null && esFicha.equalsIgnoreCase("1")){%>
	<siga:TituloExt 
		titulo="censo.fichaCliente.sjcs.to.facturacion.titulo"  
		localizacion="gratuita.retencionesJudiciales.literal.localizacion"/>
	<%}%>
	
</head>

<body >
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
    <html:form action="${path}" method="post" target="submitArea">
		<input type="hidden" name="modo" value="">		
		
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>	
	
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoRetencionesJudiciales"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="FactSJCS.mantRetencionesJ.literal.nColegiado,FactSJCS.mantRetencionesJ.literal.nombre,FactSJCS.mantRetencionesJ.literal.tipoRetencion,FactSJCS.mantRetencionesJ.literal.importe,FactSJCS.mantRetencionesJ.literal.fechaInicioRJ,FactSJCS.mantRetencionesJ.literal.fechaFinNotificacion,FactSJCS.mantRetencionesJ.literal.destinatario,"
		   tamanoCol="10,15,10,10,10,10,25,10"
		   alto="360"
		   modal="M" 
		   activarFilaSel="true" >
		   
		  <%if (obj.size()>0){%>
  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{	botones = "C,E,B";		
				fila = (Hashtable)obj.get(recordNumber-1);
				fechaNotificacionFin = GstDate.getFormatedDateShort("",UtilidadesHash.getString(fila, "FECHAFIN"));
				if (fechaNotificacionFin!=null && !fechaNotificacionFin.equals("")){
	 				java.text.SimpleDateFormat sdfNew = new java.text.SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);			
					 Date dateHoy = sdfNew.parse(fechaHoy);	
					 Date dateFin = sdfNew.parse(fechaNotificacionFin);
					
					 if (!dateHoy.before(dateFin) && !dateHoy.equals(dateFin)){
					     
						 botones = "C";
					 }
				} 
			%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" >
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDRETENCION")%>">
					    <input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.get("RETENCIONAPLICADA")%>">
					<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "NCOLEGIADO"))%></td>
					<td>
					<%if (UtilidadesHash.getString(fila, "NOMBRE")!=null && !UtilidadesHash.getString(fila, "NOMBRE").equals("")){%>
					 <%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "NOMBRE"))%>
					<%}else{%>
					 <siga:Idioma key="FactSJCS.mantRetencionesJ.literal.aplicableLetrados"/>
					<%}%> 
					 </td>
					
					<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "TIPORETENCION"))%></td>
					<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesHash.getString(fila, "IMPORTE")))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",UtilidadesHash.getString(fila, "FECHAINICIO")))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",UtilidadesHash.getString(fila, "FECHAFIN")))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "NOMBREDESTINATARIO"))%></td>
				</siga:FilaConIconos>		
			<% recordNumber++;}%>
		<%}else {%>			
			<tr>
			<td colspan="8">
			<input type="hidden" name="actionModal" value="">
			<br>
			<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			<br>
			</td>
			</tr>			
		<%}%> 		
		</siga:TablaCabecerasFijas>		

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>	
</html>
	