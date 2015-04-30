<!DOCTYPE html>
<html>
<head>
<!-- exito.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%> 

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	// SUFIJO = Permite anhadir una cadena de texto al mensaje a mostrar
	String sufijo = (String)request.getAttribute("sufijo");
	// MODAL = caso de ventana modal (cierra ventana despues con retorno "MODIFICADO" siempre)
	// si no queremos cerrar la ventana no ponemos este atributo
	String modal= (String)request.getAttribute("modal");
	// SINREFRESCO = para el caso que no queramos refrescar nada
	String sinrefresco = (String)request.getAttribute("sinrefresco");
	 
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script type="text/javascript">		
		function reloadPage() {				
		
				
				<%  if (modal!=null){%>
						<%  	if (sinrefresco!=null){%>
									window.top.returnValue=""; 
									
						<%  	} else { %>
									window.top.returnValue="MODIFICADO"; 
									
						<%  	} %>
						
								window.top.close();
				<%  }else{%>	
						<%  	if (sinrefresco==null){%>		
								if(typeof window.parent.refrescarLocalConError == 'function') { 
									window.parent.refrescarLocalConError();
								}else
								{
									window.parent.refrescarLocal();
								}
									
						<%  	} %>
				<%  } %>
				
				<%  if (mensaje!=null){
							String msg=UtilidadesString.escape(UtilidadesString.getMensajeIdioma(userBean.getLanguage(),mensaje));
				%>
					alert(unescape("<%=msg %>"),"error");
							
				<%  } %>
				return false;
		}
	</script>

</head>

<body onload="reloadPage();">
</body>
</html>
