<!DOCTYPE html>
<html>
<head>
<!-- seleccionModalPersona.jsp -->
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	ScsPersonaJGBean myBean = (ScsPersonaJGBean) request.getAttribute("datosPersonaJGModal");
	request.getSession().removeAttribute("resultadoTelefonos");
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script language="JavaScript">

	var bNuevo = "0";
	var aux = new Array();
	
	for (i=0; i<17; i++)	
	{
		aux[i]="";
	}
<%
	if (myBean!=null)
	{
%>
		bNuevo = "1";
		aux[0]="<%=myBean.getTipoIdentificacion()%>";
		aux[1]="<%=myBean.getIdPersona()%>";
		aux[2]="<%=myBean.getNif()%>";
		aux[3]=unescape("<%=UtilidadesString.escape(myBean.getNombre())%>");  
		aux[4]=unescape("<%=UtilidadesString.escape(myBean.getApellido1())%>");  
		aux[5]=unescape("<%=UtilidadesString.escape(myBean.getApellido2())%>");  
		aux[6]=unescape("<%=UtilidadesString.escape(myBean.getDireccion())%>");
		aux[7]="<%=myBean.getCodigoPostal()%>";
		aux[8]="<%=myBean.getIdPais()%>";
		aux[9]="<%=myBean.getIdProvincia()%>";
		aux[10]="<%=myBean.getIdPoblacion()%>";
		aux[11]="<%=myBean.getIdEstadoCivil()%>";
		aux[12]="<%=myBean.getRegimenConyugal()%>";
		aux[13]="<%=GstDate.getFormatedDateShort(usr.getLanguage(),myBean.getFechaNacimiento())%>";
		aux[14]="<%=myBean.getIdProfesion()%>";
		aux[15]="<%=(myBean.getIdRepresentanteJG()!=null?myBean.getIdRepresentanteJG().toString():"")%>";
		aux[18]="<%=myBean.getHijos()%>";
		aux[19]="<%=myBean.getSexo()%>";	
		aux[20]="<%=myBean.getFax()%>";	
		aux[21]="<%=myBean.getCorreoElectronico().trim()%>";		
		aux[22]="<%=myBean.getTipo()%>"; //Este es El tipo persona F o J 	
		aux[23]="<%=myBean.getIdMinusvalia()%>";
		aux[24]="<%=myBean.getEdad()%>";
		
		aux[26]="<%=myBean.getNumeroDir()%>";
		aux[27]="<%=myBean.getEscaleraDir()%>";
		aux[28]="<%=myBean.getPisoDir()%>";
		aux[29]="<%=myBean.getPuertaDir()%>";
		aux[30]="<%=myBean.getIdTipoVia()%>";
		
		aux[31]="<%=myBean.getIdPaisDireccion()%>";
		
		<% 		
		String nom = (String) request.getAttribute("nombreRepresentante");
		if (nom==null) nom="";
		%>
		aux[16]="<%=nom %>";
		// aqui guardo el nuevo
		aux[17]=bNuevo;
		
		direccionExtranjera = "<%=myBean.getDireccionExtranjera()%>";
		aux[32]=encodeURI(direccionExtranjera);
		aux[33]="<%=myBean.getPoblacion()!=null?(myBean.getPoblacion().getNombre()!=null?myBean.getPoblacion().getNombre():""):""%>";
		

<%
	}
%>
	top.cierraConParametros(aux);
	</script>
</head>

<body>
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>