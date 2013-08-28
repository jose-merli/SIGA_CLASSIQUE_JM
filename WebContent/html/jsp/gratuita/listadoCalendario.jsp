<!DOCTYPE html>
<html>
<head>
<!-- listadoCalendario.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsCalendarioLaboralBean"%>
<%@ page import="com.siga.beans.ScsCalendarioLaboralAdm"%>
<%@ page import="com.siga.beans.CenPartidoJudicialAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>


<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<title><siga:Idioma key="gratuita.calendario.literal.calendario"/></title>
	
	<script type="text/javascript">			
		function refrescarLocal() {
			parent.buscar();
		}
	</script>
	
</head>

<body>
<%if (obj.size()>0){%>
	<html:form action="/CalendarioLaboralAction.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
	</html:form>	
		
		<siga:Table 		   
		   name="listadoCalendario"
		   border="2"
		   columnNames="gratuita.listadoCalendario.literal.fecha,gratuita.listadoCalendario.literal.nombre,gratuita.listadoCalendario.literal.fiestaLocalPartidoJudicial,"
		   columnSizes="10,45,25,10"
		   modal="P">
  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{			
				ScsCalendarioLaboralBean fila = (ScsCalendarioLaboralBean)obj.get(recordNumber-1);
				String nombrePartidoJudicial = "TODO EL COLEGIO";
				try {
					CenPartidoJudicialAdm partidoJudicialAdm = new CenPartidoJudicialAdm(usr);
					Vector resultado = new Vector();
					resultado = partidoJudicialAdm.selectGenerico(partidoJudicialAdm.getNombrePartido((fila.getIdPartido()).toString()));
									
					nombrePartidoJudicial = (String)(((Hashtable)resultado.elementAt(0)).get("NOMBRE"));
				}
				catch (Exception e) {};
				
			%>
		<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' visibleConsulta="no" pintarEspacio="no" botones="E,B" clase="listaNonEdit">
			<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdentificativo()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdInstitucion()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getUsuMod()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=fila.getFechaMod()%>"><%=GstDate.getFormatedDateShort(usr.getLanguage(),fila.getFecha())%></td>
			<td><%=fila.getNombreFiesta()%>&nbsp;</td>
			<td><%=nombrePartidoJudicial%></td>
		</siga:FilaConIconos>		
		<% recordNumber++;		   
		} %>		
		</siga:Table>

	<%
	}else {
	%>
	<siga:Table 		   
		   name="listadoCalendario"
		   border="2"
		   columnNames="gratuita.listadoCalendario.literal.fecha,gratuita.listadoCalendario.literal.nombre,gratuita.listadoCalendario.literal.fiestaLocalPartidoJudicial,"
		   columnSizes="10,45,25,10"
		   modal="P">  	    
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	 </siga:Table>
	<%
	}
	%>	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
</body>	
</html>