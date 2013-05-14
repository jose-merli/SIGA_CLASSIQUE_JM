<!-- consultaCertificados.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.AdmCertificadosBean"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");	
	Vector vDatos=(Vector)request.getAttribute("vDatos");	
	String estadoColegial=(String)request.getAttribute("estadoColegial");
	String botones=""; 	
	
	// Gestion de Volver
	String busquedaVolver = (String)
	request.getSession().getAttribute("CenBusquedaClientesTipo");
	String sTipo = request.getParameter("tipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botones="V";
	}
	
%>	
<html>
<!-- HEAD -->
<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">

	</script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
			
	    <% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.certificados.cabecera"
			localizacion="censo.fichaLetrado.localizacion"/>
		<%}else{%>
		<siga:TituloExt 
			titulo="censo.fichaCliente.certificados.cabecera" 
			localizacion="censo.fichaCliente.certificados.localizacion"/>
		<%}%>
		<!-- FIN: TITULO Y LOCALIZACION -->		
</head>

<body class="tablaCentralCampos">

<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
	    <table class="tablaTitulo" align="center" cellspacing=0>

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="certificadosForm" method="post" action="/CEN_DatosCertificados.do">
		
		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = ""/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="censo.consultaCertificados.titulo"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
		    <%if(!numero.equalsIgnoreCase("")){%>
				<%if (estadoColegial!=null && !estadoColegial.equals("")){%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
					 <%= UtilidadesString.mostrarDatoJSP(numero)  %> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>)
				 <%}else{%> 
				 	(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>) 
				 <%}%>
			<%} 
			else {%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
			<%}%>
		</td>
		</tr>
		</table>	

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<siga:Table 
		   	name="tablaDatos"
		   	border="1"
		  	columnNames="censo.consultaCertificados.literal.numero,censo.consultaCertificados.rol,censo.consultaCertificados.fechaCaducidad"
		  	columnSizes="45,35,20">
 	<%	 		
 		if(vDatos == null || vDatos.size()<1 )
 			{ 			
	 %>
	 		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr> 		
	 <%		
	 		}
	 	else
	 		{	
		 		for (int i=0; i<vDatos.size(); i++)
				{
					  AdmCertificadosBean bean = (AdmCertificadosBean)vDatos.elementAt(i); 
	%>
		  			<tr class="<%=((i+1)%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">
						<td>
		  						<%=UtilidadesString.mostrarDatoJSP(bean.getNumSerie())%> 
		  				</td>
		  				<td>
		  						<%=UtilidadesString.mostrarDatoJSP(bean.getRol())%>
		  				</td>
						<td>
		  						<%=UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("",bean.getFechaCad()))%>
		  				</td>	
	  				</tr>			
	 <%		}// for
 		}  %>  			
  			</siga:Table>
  			
  	<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/>		


	<!-- FIN: BOTONES BUSQUEDA -->

	
<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>
</html>
