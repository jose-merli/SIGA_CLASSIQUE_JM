<!-- actuacionesDesigna.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getAttribute("resultado");
	String mod2 = (String)ses.getAttribute("modo");
	String deDonde= request.getParameter("deDonde");
	String anulada= (String)request.getAttribute("anulada");
	String renuncia= (String)request.getAttribute("renuncia");
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String accion = (String)request.getAttribute("accion");
	String boton="";
	String modo = (String) ses.getAttribute("Modo");
	boolean botonNuevo = (Boolean)request.getSession().getAttribute("botonNuevo");
	
	
%>	
											
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.actuacionesDesigna.literal.titulo" 
		localizacion="gratuita.actuacionesDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	</script>

</head>

<body  onLoad="ajusteAlto('resultado');" class="tablaCentralCampos">

<!-- FIN: LISTA DE VALORES -->

<% String aDonde="";

if (deDonde!=null && deDonde.equals("ficha") && usr.isLetrado()){
    aDonde="/JGR_ActuacionDesignaLetrado.do";
 }else{
    //deDonde="";
    aDonde="/JGR_ActuacionesDesigna.do";
}
	if (deDonde==null) deDonde="";

%>	
		<html:form action="<%=aDonde%>" method="post" target="mainWorkArea" style="display:none">

		<html:hidden property = "modo"/>
		<html:hidden property = "actionModal" value=""/>
		<html:hidden property = "deDonde" value="<%=deDonde%>"/>
		</html:form>
		
<iframe align="center" src="<%=app%>/html/jsp/gratuita/listadoActuacionesDesignas.jsp?deDonde=<%=deDonde%>"
					id="resultado"
					name="resultado" 
					height="100%"
					width="1017px"
					frameborder="0"
					marginheight="0"
					marginwidth="0"
					style="margin:0"
>

</iframe>
	
				
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
<script language="JavaScript">
		//Asociada al boton Volver
		function accionVolver() {	
			document.forms[0].target = "mainWorkArea";	
			document.forms[0].action="JGR_Designas.do";
			document.forms[0].modo.value="volverBusqueda";
			document.forms[0].submit();
		}
		
		function accionNuevo() 
		{	
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"G");
			if(resultado=='MODIFICADO') buscar();
		}
		function buscar(){
			document.forms[0].modo.value="";
			document.forms[0].target="mainPestanas";
			document.forms[0].submit();
		}
		function refrescarLocal (){
			buscar();
		}
		
</script>		

  <% if (deDonde!=null && deDonde.equals("ficha")){  %>
		<%-- No muestro el boton nuevo si la designa esta anulada o tiene fecha renuncia distinto de null--%>
  		<% if(anulada!=null && anulada.equals("1") || (renuncia!=null) && renuncia.equals("1")) {%>
			<siga:ConjBotonesAccion botones="" clase="botonesDetalle" modo="<%=modo%>"/>
		<% } else {%>
			<siga:ConjBotonesAccion botones="N" clase="botonesDetalle" modo="<%=modo%>"/>
		<% } %>		
  <% }else{%>
  		<%-- No muestro el boton nuevo si la designa esta anulada o tiene fecha renuncia distinto de null--%>
  		<% if(anulada!=null && anulada.equals("1") || (renuncia!=null) && renuncia.equals("1")) {%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" modo="<%=modo%>"/>
		<% } else {%>
		  	<% if(!botonNuevo){%>
				<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" modo="<%=modo%>"/>
			<% } else {%>
				<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" modo="<%=modo%>"/>
		<% 	   }
		  }%>		
  <%}%>		
	
	</body>
	
</html>
		  
		
