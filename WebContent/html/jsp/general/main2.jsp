<!DOCTYPE html>
<html>
<head>
<!-- main2.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="org.redabogacia.sigaservices.app.util.ReadProperties"%>
<%@ page import="org.redabogacia.sigaservices.app.util.SIGAReferences"%>
<% 
String app=request.getContextPath();
String logo=(String)request.getSession().getAttribute(SIGAConstants.PATH_LOGO);
ReadProperties rproperties=new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
String pathInicio = rproperties.returnProperty("general.paginaInicio");
%>


	
		<title><siga:Idioma key="index.title"/></title>
		
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<script type="text/javascript">
			function inicio()
			{
				MM_preloadImages('<%=app%>/html/imagenes/botonSession_ilum.gif',
								 '<%=app%>/html/imagenes/botonSession_activo.gif',
								 '<%=app%>/html/imagenes/botonCerrar_ilum.gif',
								 '<%=app%>/html/imagenes/botonCerrar_activo.gif');
			}

			var user, psswd, profile, loc;
			
			<%UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");%>
			
			user='<%=userBean.getUserName()%>';
			psswd='clavecita';
			profile='<%=userBean.getProfile()%>';
			loc='<%=userBean.getLocation()%>';

			function cerrarSession()
			{
				MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_activo.gif',1);
				top.location='<%=pathInicio%>';
				
				return false;
			}
	
			function cerrarAplicacion()
			{
				MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_activo.gif',1);
				
				if(confirm('<siga:Idioma key="general.cerrarAplicacion"/>'))
				{
					window.top.close(); 
				}
				
				return false;
			}
		</script>
	</head>

	<body onLoad="inicio();">
		<%--script language="JavaScript1.2" src="<%=app%>/html/js/coolmenus4.js"></script--%>
		<script src="<%=app%>/html/js/coolmenus4.jsp" type="text/javascript"></script>
		
		<div style="position:absolute; z-index:50;">
			<tiles:insert page="/menu_izq.do" flush="true"/>
		</div>
		
		<div style="position:absolute; left:0px; top:0px;height:79px;width:170px; text-align:center">
			<img id="logoImg" src="<%=logo%>">
		</div>
		<div id="img2" style="text-align:center; height:79px;width:170px; z-index:1; background-color:transparent;">
			<img id="logoSIGA" align="bottom" src="<%=app%>/html/imagenes/logoSIGA.png"> 
		</div>
		<a href="javascript://" class="imageLink" onclick="return cerrarAplicacion();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_ilum.gif',1)">
			<img src="<%=app%>/html/imagenes/botonCerrar.gif" alt="<siga:Idioma key="general.cerrarAplicacion"/>" align="middle" name="closeApp" width="27" height="27" border="0">
			&nbsp;<siga:Idioma key="general.cerrarAplicacion"/>
		</a>
		<br>
		<a href="javascript://" class="imageLink" onclick="return cerrarSession();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_ilum.gif',1)">
			<img src="<%=app%>/html/imagenes/botonSession.gif" alt="<siga:Idioma key="general.cerrarSesion"/>" align="middle" name="closeSession" width="27" height="27" border="0">
			&nbsp;<siga:Idioma key="general.cerrarSesion"/>
		</a>

		<iframe src="<%=app%>/html/jsp/general/blank.jsp"
				id="mainWorkArea"
				name="mainWorkArea"
				scrolling="no"
				frameborder="0"
				style="position:absolute; width:83%; height:95%; z-index:2; top: 10px; left: 170px">
		</iframe>
		
		<script language="JavaScript" type="text/javascript">
			initStyles();
	
			if(profile=='NCL')
			{
				mainWorkArea.location='<%=app%>/html/jsp/censo/solIncorporacion.jsp';
			}
			
			else
			{
				mainWorkArea.location='<%=app%>/html/jsp/censo/inicioCenso.jsp';
			} 
		</script>
	</body>
</html>