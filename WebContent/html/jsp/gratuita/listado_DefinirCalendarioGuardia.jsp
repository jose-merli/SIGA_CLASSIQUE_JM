<!-- listado_DefinirCalendarioGuardia.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String profile[]=usr.getProfile();
	
	String modopestanha = request.getSession().getAttribute("modo")==null?"":(String)request.getSession().getAttribute("modo");
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<script>
		function refrescarLocal(){
			parent.refrescarLocal();
		}
	</script>
</head>

<body  class="tablaCentralCampos">

    <table width="100%" cellspacing="0"><tr class="titulitosDatos">
      <td width="50%">
        <siga:Idioma key="gratuita.confGuardia.literal.turno"/>:&nbsp;
        <%= (String)request.getAttribute ("NOMBRETURNO") %>
      </td>
      <td width="50%">
        <siga:Idioma key="gratuita.confGuardia.literal.guardia"/>:&nbsp;
        <%= (String)request.getAttribute ("NOMBREGUARDIA") %>
      </td>
    </tr></table>
    
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" target="resultado" style="display:none">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "accion" value = ""/>
	<html:hidden property = "modoPestanha" value = "<%=modopestanha%>"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		

		<siga:TablaCabecerasFijas 		   
			   nombre="listadoInicial"
			   borde="1"
			   clase="tableTitle"		   
			   nombreCol="gratuita.listado_DefinirCalendarioGuardia.literal.fechaInicio,gratuita.listado_DefinirCalendarioGuardia.literal.fechaFin,gratuita.listado_DefinirCalendarioGuardia.literal.observaciones,"
			   tamanoCol="20,20,50,10"
		   			alto="100%"
			   modal="G"			   
		>
		
	<!-- INICIO: RESULTADO -->
	<% if ((obj!= null) && (obj.size()>0)) { %>
				<%
				int recordNumber=1;
				String fechaInicio="", fechaFin="", observaciones="", idcalendarioguardias="", idturno="", idguardia="", idinstitucion="";
				while ((recordNumber) <= obj.size())
				{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				%>
			<!-- Campos ocultos por cada fila:      
				1- IDCALENDARIOGUARDIAS
				2- IDTURNO
				3- IDGUARDIA
				4- IDINSTITUCION
			-->
			<!-- Campos visibles por cada fila:
				1- FECHAINICIO=FECHADESDE para mi formulario
				2- FECHAFIN=FECHAHASTA para mi formulario
				3- OBSERVACIONES
			-->
			<%
				fechaInicio = ((String)hash.get("FECHAINICIO")).equals("")?"&nbsp;":(String)hash.get("FECHAINICIO");
				fechaFin = ((String)hash.get("FECHAFIN")).equals("")?"&nbsp;":(String)hash.get("FECHAFIN");
				observaciones = ((String)hash.get("OBSERVACIONES")).equals("")?"&nbsp;":(String)hash.get("OBSERVACIONES");
				idcalendarioguardias = ((String)hash.get("IDCALENDARIOGUARDIAS")).equals("")?"&nbsp;":(String)hash.get("IDCALENDARIOGUARDIAS");
				idturno = ((String)hash.get("IDTURNO")).equals("")?"&nbsp;":(String)hash.get("IDTURNO");
				idguardia = ((String)hash.get("IDGUARDIA")).equals("")?"&nbsp;":(String)hash.get("IDGUARDIA");
				idinstitucion = ((String)hash.get("IDINSTITUCION")).equals("")?"&nbsp;":(String)hash.get("IDINSTITUCION");
				
			%>
		       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="E,C,B" clase="listaNonEdit" modo="<%=modopestanha%>"  >
				<td align="center"><input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idcalendarioguardias%>' ><input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idturno%>' ><input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=idguardia%>' ><input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idinstitucion%>' /><%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%></td>
				<td align="center"><%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin)%></td>
				<td align="center"><%=observaciones%></td>
			</siga:FilaConIconos>
				<% 		recordNumber++; %>
				<% } %>
		<!-- FIN: RESULTADO -->
	<% } else { %>
		<br>
   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
		<br>
	<% } %>
	</siga:TablaCabecerasFijas>

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>