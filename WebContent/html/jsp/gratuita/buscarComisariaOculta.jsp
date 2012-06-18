<!-- buscarComisariaOculta.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga" %>
<%@ taglib uri = "struts-bean.tld"   prefix="bean" %>
<%@ taglib uri = "struts-html.tld"   prefix="html" %>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
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
	String obj = (String)request.getAttribute("resultadoComisaria");

	String nombreObjetoDestino = (String)request.getAttribute("nombreObjetoDestino");
	
	

%>
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script language="JavaScript">

	
	var aux = new Array();
	
	
<%
	if (obj!=null && !obj.equals(""))
	{

%>	   

       aux[0]="<%=obj%>";
       
<%     if (nombreObjetoDestino != null && !nombreObjetoDestino.equals("")) { %>
          aux[1]="<%=nombreObjetoDestino%>";
<%     } %>
       
	<%}%>

	<%if (nombreObjetoDestino != null && !nombreObjetoDestino.equals("")&& !nombreObjetoDestino.equals("DOSFRAMES")) { %>//venimos de una ventana principa�
	 
  	  top.frames[0].traspasoDatosComisaria(aux);
	 <%}else{
	    if (nombreObjetoDestino != null && !nombreObjetoDestino.equals("")&& nombreObjetoDestino.equals("DOSFRAMES")){%>
		
	        window.parent.parent.traspasoDatosComisaria(aux);
		  <%}else{// venimos de una ventana modal%>
	  
	      window.parent.traspasoDatosComisaria(aux);
	  <% }
	   }%>   
	
	
	
	</script>
</head>

<body>
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>