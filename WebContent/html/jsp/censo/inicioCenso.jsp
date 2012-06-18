<!-- inicioCenso.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.UsrBean"%>

<% String app=request.getContextPath(); %>

<html>
	<head>
		<title><siga:Idioma key="menu.censo.titulo"/></title>
		
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
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
