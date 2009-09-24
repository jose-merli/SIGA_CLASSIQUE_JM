<!-- consultaTramosLEC.jsp -->
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
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.facturacionSJCS.form.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	//recoger de request el vector con los registros resultado
	Vector resultado = (Vector) request.getAttribute("resultado");

	//campos a mostrar en la tabla
	String maximo ="", minimo="", retencion="";

	//campos ocultos
	String idTramo="";

%>

<html>
<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<html:javascript formName="DatosMantenimientoTramosLECForm" staticJavascript="false" />
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="FactSJCS.mantTramosLEC.cabecera" 
		localizacion="FactSJCS.mantTramosLEC.ruta"/>
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	<script language="JavaScript">	
	
		function refrescarLocal() 
		{
			parent.buscar();
		}

	</script>
</head>

<body class="tablaCentralCampos">

		<html:form action="/FCS_MantenimientoLEC.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = "" />	
		
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">		
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="FactSJCS.mantTramosLEC.literal.minimo,FactSJCS.mantTramosLEC.literal.maximo,FactSJCS.mantTramosLEC.literal.nombre,"
		   tamanoCol="30,30,30,10"
		   modal="P"
		   alto="100%" >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
			<%	if (resultado==null || resultado.size()==0) { %>			
		 		<br><br>
		   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 		<br><br>	 		
			<%	
				} else {
					Hashtable fila = new Hashtable();
					for (int cont=1;cont<=resultado.size();cont++) {
						fila = (Hashtable) resultado.get(cont-1);
						maximo = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "MAXIMOSMI"));
						minimo = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "MINIMOSMI"));
						retencion = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "RETENCION"));
						idTramo = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(fila, "IDTRAMOLEC"));
			
			%>
  			<siga:FilaConIconos fila="<%=String.valueOf(cont)%>" visibleConsulta="no" botones="E,B" clase="listaNonEdit">
			
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idTramo%>"><%=minimo%></td>
				<td><%=maximo%></td>
				<td align="center"><%=retencion%>&nbsp;&nbsp;%</td>

			</siga:FilaConIconos>		


<%		} // del for
	} // del if 
	%>			

		</siga:TablaCabecerasFijas>


<!-- FIN: LISTA DE VALORES -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
