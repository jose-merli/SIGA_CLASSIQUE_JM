<!-- consultaProcedimientos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.gratuita.form.*"%>
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
	String nombre ="", precio="", puntos="";

	//campos ocultos
	String idProc="";

%>

<html>
<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<html:javascript formName="MantenimientoProcedimientosForm" staticJavascript="false" />
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.procedimientos.cabecera" 
		localizacion="gratuita.procedimientos.ruta"/>
	
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

	<html:form action="/JGR_MantenimientoProcedimientos.do" method="POST" target="mainWorkArea" style="display:none">
	<html:hidden property = "modo" value = "" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="gratuita.procedimientos.literal.nombre,gratuita.procedimientos.literal.importe,gratuita.procedimientos.literal.puntos,"
		   tamanoCol="50,20,20,10"
		   modal="M"
		   			alto="100%"

		  >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
<%	
	} else { 
		for (int cont=1;cont<=resultado.size();cont++) {
			Hashtable fila = (Hashtable) resultado.get(cont-1);

			nombre = UtilidadesString.mostrarDatoJSP((String)fila.get(ScsProcedimientosBean.C_NOMBRE));
			precio = (String)fila.get(ScsProcedimientosBean.C_PRECIO) + " &euro;";
			try{
				puntos = UtilidadesString.mostrarDatoJSP((String)fila.get(ScsProcedimientosBean.C_PUNTOS));
			}catch(Exception e){puntos="&nbsp;";}
			idProc = UtilidadesString.mostrarDatoJSP(((String)fila.get(ScsProcedimientosBean.C_IDPROCEDIMIENTO)));

%>
  			<siga:FilaConIconos fila="<%=String.valueOf(cont)%>" visibleConsulta="no" botones="E,B" clase="listaNonEdit">
			
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idProc%>"><%=nombre%></td>
				<td><%=precio%></td>
				<td><%=puntos%></td>

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
