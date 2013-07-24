<!DOCTYPE html>
<html>
<head>
<!-- inicioCenso.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.UsrBean"%>

<% String app=request.getContextPath(); %>


	
		<title><siga:Idioma key="menu.censo.titulo"/></title>
		
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<script language="javascript">
			function inicio()
			{
				<%UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");%>
	
				var accion = getParameterValue('action');
				var profile = '<%=userBean.getProfile()%>';
	
				if(accion=='')
				{
					if(profile=='ADM' || profile=='AGE')
					{
						accion='<%=app%>/html/jsp/censo/botoneraBusqueda.jsp';
					}
					
					else
					{
						accion='<%=app%>/html/jsp/censo/botoneraFicha.jsp';
					}
				}
				
				var tipo=getParameterValue('tipo');
				
				if(tipo!='')
				{
					accion+='?tipo='+tipo;
				}
				
				botoneraGestion.location=accion;
			}
		</script>
	</head>

	<frameset onLoad="inicio();" rows="36,*" frameborder="NO" border="0" framespacing="0">
		<frame name="botoneraGestion" scrolling="NO" noresize src="<%=app%>/html/jsp/general/blank.jsp">
		<frame name="CuerpoCentralGestion" scrolling="NO" src="<%=app%>/html/jsp/general/blank.jsp">
	</frameset>
	
	<noframes></noframes> 
</html>
