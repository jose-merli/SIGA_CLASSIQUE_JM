<!-- listadoRetenciones.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsRetencionesBean"%>
<%@ page import="com.siga.beans.ScsRetencionesAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.beans.ScsRetencionesBean"%>
<%@ page import="com.siga.beans.ScsRetencionesAdm"%>
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
	ScsRetencionesAdm retencionesAdm = new ScsRetencionesAdm(usr);
	Hashtable sociedades = (Hashtable)request.getAttribute("sociedades");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.retenciones.listadoRetenciones"/></title>

	<script language="JavaScript">	
		function refrescarLocal()
		{
			parent.buscar();
		}		
	</script>


</head>

<body>
	<%if (obj.size()>0){%>
	<html:form action="/SolicitudRetencioAction.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="hiddenframe" value="1">
		
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoRetenciones"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.retenciones.descripcion,gratuita.retenciones.retencion,gratuita.retenciones.tipoSociedad,"
		   tamanoCol="60,10,20,10" 
		   alto="100%"
		   modal="P" >
		    
  		<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{			
				Hashtable registro = (Hashtable)obj.get(recordNumber-1);
				ScsRetencionesBean fila = (ScsRetencionesBean)retencionesAdm.hashTableToBean(registro);
				String nomSociedad=sociedades.get(fila.getLetraNifSociedad()) == null?"":(String)sociedades.get(fila.getLetraNifSociedad());
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdRetencion()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getUsuMod()%>">										   <input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getFechaMod()%>"><%=fila.getDescripcion()%></td>
					<td align="center"><%=fila.getRetencion()%></td>
					
					<td align="left"><%=nomSociedad%>&nbsp;</td>
				</siga:FilaConIconos>		
		<% recordNumber++;		   
		} %>
		</siga:TablaCabecerasFijas>

	<%
	}else {
	%>
	<siga:TablaCabecerasFijas 		   
		   nombre="listadoRetenciones"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.retenciones.descripcion,gratuita.retenciones.retencion,gratuita.retenciones.tipoSociedad,"
		   tamanoCol="60,10,20,10"
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

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	