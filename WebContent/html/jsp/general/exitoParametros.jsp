<!-- exitoParametros.jsp -->
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Hashtable"%>
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

<html:html>
<head>
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
	
	
	
	String[] parametros = (String[])request.getAttribute("parametrosArray");
	
	
	 
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

<script type="text/jscript" language="JavaScript1.2">
	
	function reloadPage() {
	
			
			<%  if (mensaje!=null){
						String msg=UtilidadesString.escape(UtilidadesString.getMensajeIdioma(userBean.getLanguage(),mensaje));
			%>
						alert(unescape("<%=msg %>"));
						
			<%  } %>
			<%  if (modal!=null){%>
					<%  	if (sinrefresco!=null){%>
								window.returnValue=""; 
								
					<%  	} else {
							%>
					
								var array =new Array(<%=parametros.length%>);
								<%
								for (int i = 0; i < parametros.length; i++) {
									String parametro = parametros[i].trim();%>
									array[<%=i%>]="<%=parametro%>";	
								<%}%>

								window.returnValue=array; 
								
					<%  	} %>
					
							window.close();
			<%  }else{%>	
					<%  	if (sinrefresco==null){%>
					
								parent.refrescarLocal();
					<%  	} %>
			<%  } %>
			return false;
	}
</script>

</head>

<body onload="reloadPage();">
</body>
</html:html>
