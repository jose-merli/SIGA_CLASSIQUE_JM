<!-- definirTurnosIndex.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Colegio cole = new Colegio(request);
	ses.setAttribute("colegio",cole);
	String acceso=((UsrBean)request.getSession().getAttribute("USRBEAN")).getAccessType();
	String entrada="";
	String logo=(String)request.getSession().getAttribute(SIGAConstants.PATH_LOGO);
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	if (acceso.equals(SIGAPTConstants.ACCESS_FULL)) { 
		entrada = "1";
	}else{
		entrada="2";
	}
	ses.setAttribute("entrada",entrada);
%>
	
<html>

<!-- HEAD -->
<head>
	<title><"definirTurnos.title"></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script>
		var tit = "<siga:Idioma key="administracion.auditoria.titulo"/>";
 		var loc = "<siga:Idioma key="administracion.auditoria.titulo"/>";
		//se kitara el dia q seacceda desde el menu
		//setTitulo(tit);
		//setLocalizacion(loc);
	</script>
			<script language="JavaScript" type="text/javascript">
			var user, psswd, profile, loc;
			user='<%=userBean.getUserName()%>';
			psswd='clavecita';
//			profile='<%=userBean.getProfile()%>';
			loc='<%=userBean.getLocation()%>';

			function inicio()
			{
				MM_preloadImages('<%=app%>/html/imagenes/botonSession_ilum.gif',
								 '<%=app%>/html/imagenes/botonSession_activo.gif',
								 '<%=app%>/html/imagenes/botonSession.gif',
								 '<%=app%>/html/imagenes/botonCerrar.gif',
								 '<%=app%>/html/imagenes/botonCerrar_ilum.gif',
								 '<%=app%>/html/imagenes/botonCerrar_activo.gif');
			}

			function cerrarSession()
			{
				MM_swapImage('closeSession','','<%=app%>/html/imagenes/botonSession_activo.gif',1);
				
				/*if(psswd!='')
				{
					top.location='<%=app%>/html/jsp/general/login.jsp'; 
				}
				
				else
				{*/
					top.location='<%=app%>/html/jsp/index.jsp';
				//}
				
				return false;
			}
		
			function cerrarAplicacion()
			{
				MM_swapImage('closeApp','','<%=app%>/html/imagenes/botonCerrar_activo.gif',1);
				
				//if(confirm('¿Está seguro de que desea abandonar la aplicación?'))
				if(confirm('<siga:Idioma key="general.cerrarAplicacion"/>'))
				{
					window.top.close();
				}
				
				return false;
			}
		</script>
	</head>

<body class="tableCabecera" onLoad="inicio();">
	

<%if (entrada.equals("1")){%>
	<!-- IFRAME PESTAÑAS -->
	<iframe src="<%=app%>/html/jsp/gratuita/pestanasTurnos.jsp"
			id="pestanas"
			name="pestanas"
			scrolling="no"
			frameborder="0"
			class="posicionPestanas">
	</iframe>

	<!-- IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/gratuita/definirTurnos.jsp"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			class="frameGeneral"
			style="position:absolute; width:964; z-index:2; top: 79px">
	</iframe>
<%}else{%>
	<html:form action = "/DefinirTurnosAction.do" method="POST" target="resultado" enctype="multipart/form-data" >
		<input type="hidden" name="modo" value="buscarPor">
		<input type="submit" name="aceptar" class="button">
	</html:form>
	
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					style="position:absolute; width:964; height:373; z-index:2; top: 65px">
	</iframe>
<%}%>
<!-- SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html>
