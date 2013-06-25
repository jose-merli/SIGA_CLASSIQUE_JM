<!-- resultadoEjecucionConsulta.jsp -->

<!-- METATAGS -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="struts-html.tld"	prefix="html"%>
<%@ taglib uri="struts-bean.tld"	prefix="bean"%>
<%@ taglib uri="libreria_SIGA.tld"	prefix="siga"%>

<!-- IMPORTS FOR SCRIPTS -->
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.lang.Long"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>

<!-- SCRIPTLET -->
<%  
	//Controles generales
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute ("USRBEAN");
	String tipoacceso = user.getAccessType();
	
	//Variables para tomar los datos
	Vector datos =
		(Vector) ((HashMap) ses.getAttribute ("DATABACKUP")).get ("datos");
	String[] cabeceras =
		(String[]) ((HashMap) ses.getAttribute ("DATABACKUP")).get ("cabeceras");	
%>	

<html>

<!-- METATAGS -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
</head>

<body>

    <!-- INICIO: ZONA DE REGISTROS -->
<%
	if (datos==null || datos.size()==0)
	{
%>
	<div class="notFound">
					<br><br>
			   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
					<br><br>
					</div>
<%
	}
	else
	{
%>
	<table border='1' width='100%' cellspacing='0' cellpadding='0' align='center'>
<%
		for (int i=0; i<datos.size(); i++)
		{				
			Row fila = (Row) datos.elementAt(i);
			if (i==0) {
%>
		<tr class="tableTitle">
<%
				for (int j=0; j<cabeceras.length; j++)
				{
%>
			<td><%=UtilidadesString.mostrarDatoJSP(cabeceras[j])%></td>
<%
				}
%>
		</tr>
<%
			} //if
%>
		<tr class="listaNonEdit">
<%
			for (int k=0; k<cabeceras.length;k++)
			{
%>
	  		<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(cabeceras[k]))%></td>
<%
		  	}	
%>
		</tr>		
<%
		} //for
%>
	</table>
<%
	} //if
%>

</body>

</html>
