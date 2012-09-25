<!-- listadoAreas.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsAreaBean"%>
<%@ page import="com.siga.beans.ScsAreaAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) ses.getAttribute("resultado");
	Hashtable fila = new Hashtable();
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<title><siga:Idioma key="gratuita.listadoAreas.literal.listadoAreas"/></title>
	
	<script type="text/javascript">
		function refrescarLocal()
		{
			parent.buscar();
		}
	</script>
	
</head>

<body>
	<%if (obj.size()>0){%>
	<html:form action="/DefinirAreasMateriasAction.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo" styleId = "modo" value = ""/>
		<html:hidden property = "accion"  styleId = "accion" value = "area"/>
		<input type="hidden" name="actionModal"  id="actionModal" value="">
		</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoAreas"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaAreas.literal.nombreArea,gratuita.busquedaAreas.literal.nombreMateria,gratuita.listadoAreas.literal.contenidoArea,"
		   tamanoCol="30,30,30,10" 
		   alto="100%" 
		   activarFilaSel="true">
		   
  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size()) {
				fila = (Hashtable)obj.get(recordNumber-1);
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDINSTITUCION")%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.get("IDAREA")%>"><%=fila.get("NOMBRE")%>&nbsp;</td>
					<td><%=fila.get("MATERIAS")%>&nbsp;</td>
					<td><%=fila.get("CONTENIDO")%>&nbsp;</td>
				</siga:FilaConIconos>		
			<%  
				recordNumber++; 
			}
			%>
		</siga:TablaCabecerasFijas>

	<%
	}else {
	%>
	<siga:TablaCabecerasFijas 		   
		   nombre="listadoAreas"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaAreas.literal.nombreArea,gratuita.busquedaAreas.literal.nombreMateria,gratuita.listadoAreas.literal.contenidoArea,"
		   tamanoCol="30,30,30,10"
		   			alto="100%"

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