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
	//aalg:controlar el acceso en modo consulta
	String accion = (String)request.getAttribute("accion");
	
	Hashtable fila = new Hashtable();
	
	String botones = "C,E,B";
	String fechaHoy = UtilidadesBDAdm.getFechaBD("");
	String fechaNotificacionFin = "";
	
	
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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
		<input type="hidden" id="modo"  name="modo" value="">		
	</html:form>	

		<siga:Table 		   
		   name="listadoRetencionesJudiciales"
		   border="2"
		   columnNames="FactSJCS.mantRetencionesJ.literal.nColegiado,FactSJCS.mantRetencionesJ.literal.nombre,FactSJCS.mantRetencionesJ.literal.tipoRetencion,FactSJCS.mantRetencionesJ.literal.importe,FactSJCS.mantRetencionesJ.literal.fechaInicioRJ,FactSJCS.mantRetencionesJ.literal.fechaFinNotificacion,FactSJCS.mantRetencionesJ.literal.destinatario,"
		   columnSizes="10,15,10,10,10,10,25,10"
		   modal="M">
		   
		  <%if (obj.size()>0){%>
  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{	
				botones = "C,E,B";		
				fila = (Hashtable)obj.get(recordNumber-1);
				if(fila.get("RETENCIONAPLICADA").equals("1")){
					botones = "C,E";
				}
				fechaNotificacionFin = GstDate.getFormatedDateShort("",UtilidadesHash.getString(fila, "FECHAFIN"));
				if (fechaNotificacionFin!=null && !fechaNotificacionFin.equals("")){
	 				java.text.SimpleDateFormat sdfNew = new java.text.SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);			
					 Date dateHoy = sdfNew.parse(fechaHoy);	
					 Date dateFin = sdfNew.parse(fechaNotificacionFin);
					
					 if (!dateHoy.before(dateFin) && !dateHoy.equals(dateFin)){
					     
						 botones = "C";
					 }
				} 
				//aalg:controlar el acceso en modo consulta
				if (accion.equalsIgnoreCase("ver"))
					botones = "C";
			%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" >
					<td><input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDRETENCION")%>">
					    <input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.get("RETENCIONAPLICADA")%>">
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
			<tr class="notFound">
			<td colspan="8" class="titulitos">
				<input type="hidden" name="actionModal" value="">
				<siga:Idioma key="messages.noRecordFound"/>
			</td>
			</tr>
		<%}%> 		
		</siga:Table>		

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>	
</html>
	