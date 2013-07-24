<!DOCTYPE html>
<html>
<head>
<!-- ejgDesigna.jsp -->
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
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Hashtable" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)request.getAttribute("resultado");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	request.setAttribute("resultado",obj);
	Hashtable designaActual = (Hashtable) request.getAttribute("designaActual");
	String modo = (String)request.getSession().getAttribute("Modo");
	String numero="", anio="", idTurno="";
	try{
		numero = (String)designaActual.get("NUMERO");
		anio = (String)designaActual.get("ANIO");
		idTurno = (String)designaActual.get("IDTURNO");
	}catch(Exception e){
		Hashtable actual = (Hashtable) request.getSession().getAttribute("designaActual");
		if (numero==null) numero=(String)actual.get("NUMERO");
		if (anio==null) anio=(String)actual.get("ANIO");
		if (idTurno==null) idTurno=(String)actual.get("IDTURNO");
	}

	
%>	
											

<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.ejgDesigna.literal.titulo" 
		localizacion="gratuita.ejgDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	</script>

</head>

<body  onLoad="ajusteAltoBotones('resultado');">

		<html:form action="/JGR_EJG_Designa.do" method="post" target="mainWorkArea" style="display:none">
			<html:hidden property = "modo" value = "nuevo"/>
			<html:hidden property = "actionModal" value = ""/>
		</html:form>
	
		<html:form action="/JGR_EJG_Designa.do" method="POST" target="resultado" style="display:none">
			<html:hidden property = "numero" value="<%=numero%>"/>
			<html:hidden property = "anio" value="<%=anio%>"/>
			<html:hidden property = "idTurno" value="<%=idTurno%>"/>
			<html:hidden property = "modo"/>
			<html:hidden property = "actionModal" value=""/>
			
			<html:hidden property = "ejgIdInstitucion" value=""/>
			<html:hidden property = "ejgAnio" value=""/>
			<html:hidden property = "ejgNumero" value=""/>
			<html:hidden property = "ejgIdTipoEjg" value=""/>
		</html:form>
		
		<html:form action="/JGR_EJG.do" method="post" target="mainWorkArea" style="display:none">
			<html:hidden property = "modo" 			value= ""/>
			<html:hidden property = "anio"    		value= ""/>
			<html:hidden property = "numero"  		value= ""/>
			<html:hidden property = "idTipoEJG" 	value= ""/>
			<html:hidden property = "idInstitucion" value= "<%=usr.getLocation()%>"/>
		</html:form>	

<iframe align="center" src="<%=app%>/html/jsp/gratuita/listadoEjgDesigna.jsp"
			id="resultado"
			name="resultado" 
			height="100%"
			width="1017px"
			frameborder="0"
			marginheight="0"
			marginwidth="0"
			style="padding:0;margin:0"
>
</iframe>
	

<!-- formulario para buscar Tipo SJCS -->
	<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea"  style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrir">
		<input type="hidden" name="tipo"        value="">
		
	</html:form>

<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[0].target = "mainWorkArea";	
			document.forms[0].action="JGR_Designas.do";
			document.forms[0].modo.value="volverBusqueda";
			document.forms[0].submit();
		}
		
		function accionNuevo() 
		{	
			document.forms[1].modo.value = "nuevo";
		
			var resultado=ventaModalGeneral(document.forms[1].name,"M");
//			buscar();
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.DefinirEJGForm){
					numero.value        = resultado[1];
					idTipoEJG.value     = resultado[2];
					idInstitucion.value = resultado[3];
					anio.value          = resultado[4];
					modo.value          = "editar";
//					target.value		= "mainWorkArea";
			   		submit();
				}
			}
		}
		function relacionarConEJG() 
		{
			document.BusquedaPorTipoSJCSForm.tipo.value="EJG";
			var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	

			if (resultado != null && resultado.length >= 4)
			{
				document.forms[1].ejgIdInstitucion.value=resultado[0];
				document.forms[1].ejgAnio.value=resultado[1];
				document.forms[1].ejgNumero.value=resultado[2];
				document.forms[1].ejgIdTipoEjg.value=resultado[3];

				document.forms[1].modo.value= "relacionarConEJG";
				document.forms[1].target = "submitArea";
				document.forms[1].submit();
			}
		}
		
		function buscar(){
			document.forms[0].modo.value="";
			document.forms[0].target="mainPestanas";
			document.forms[0].submit();
		}
		function refrescarLocal()
		{
			buscar();
		}
		
</script>		

		<siga:ConjBotonesAccion botones="V,N,re" clase="botonesDetalle" modo="<%=modo%>"/>
	
</body>
</html>		  		