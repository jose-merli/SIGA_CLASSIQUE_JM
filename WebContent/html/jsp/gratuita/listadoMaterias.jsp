<!-- listadoMaterias.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsMateriaBean"%>
<%@ page import="com.siga.beans.ScsMateriaAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>


<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String accion = (String)ses.getAttribute("accion");
	ses.removeAttribute("accion");
	ScsMateriaBean fila = new ScsMateriaBean();
	
	String botones="";
	
	if (accion == null || accion.equalsIgnoreCase("ver")){
		botones = "C";
	}
	else {
		botones = "C,E,B";
	}
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<title><siga:Idioma key="gratuita.retenciones.listadoRetenciones"/></title>

	<script type="text/javascript">
		function refrescarLocal()
		{
			parent.buscar();
		}
	</script>
</head>

<body>
	
	<% if (obj.size()>0) { %>
	<html:form action="/DefinirAreasMateriasAction.do" method="post" target="submitArea"	 style="display:none">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="accion" value="materia">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		

		<siga:TablaCabecerasFijas
		   nombre="listadoMaterias"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaAreas.literal.nombreMateria,gratuita.listadoAreas.literal.contenidoMateria,"
		   tamanoCol="25,65,10"
		   			alto="100%"
		

		   modal="M"
		  >
  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{			
				fila = (ScsMateriaBean)obj.get(recordNumber-1);
			%>				
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" >
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdArea()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdInstitucion()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getIdMateria()%>"><%=fila.getNombre()%>&nbsp;</td>
					<td><%=fila.getContenido()%>&nbsp;</td>
				</siga:FilaConIconos>		
		<% recordNumber++;		   
		} %>
		</siga:TablaCabecerasFijas>

	<%
	}else {
	%>
	<siga:TablaCabecerasFijas
		   nombre="listadoMaterias"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaAreas.literal.nombreMateria,gratuita.listadoAreas.literal.contenidoMateria,"
		   tamanoCol="25,65,10"
		   			alto="100%"
	

		   modal="P"
		  >	
		  </siga:TablaCabecerasFijas>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
	}
	%>
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>