<!DOCTYPE html>
<html>
<head>
<!-- consultaProcedimientos.jsp -->
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
<%@ page import="com.siga.gratuita.form.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	//recoger de request el vector con los registros resultado
	Vector resultado = (Vector) request.getAttribute("resultado");

	//campos a mostrar en la tabla
	String nombre ="", codigo ="", precio="";

	//campos ocultos
	String idProc="", fechaInicio="", fechaFin="";

%>

 
<!-- HEAD -->

	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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

	<html:form action="/JGR_MantenimientoProcedimientos.do" method="POST" target="mainWorkArea">
		<html:hidden property = "modo"  styleId = "modo"  value = "" />
	</html:form>	
		

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="gratuita.mantenimientoTablasMaestra.literal.codigoext,gratuita.procedimientos.literal.nombre,gratuita.procedimientos.literal.fechainicio,gratuita.procedimientos.literal.fechafin,gratuita.procedimientos.literal.importe,"
		   columnSizes="10,45,10,10,13,12"
		   modal="M">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%	
	} else { 
		for (int cont=1;cont<=resultado.size();cont++) {
			Hashtable fila = (Hashtable) resultado.get(cont-1);

			nombre = UtilidadesString.mostrarDatoJSP((String)fila.get(ScsProcedimientosBean.C_NOMBRE));
			codigo = UtilidadesString.mostrarDatoJSP((String)fila.get(ScsProcedimientosBean.C_CODIGO));
			precio = (String)fila.get(ScsProcedimientosBean.C_PRECIO);
			idProc = UtilidadesString.mostrarDatoJSP(((String)fila.get(ScsProcedimientosBean.C_IDPROCEDIMIENTO)));
			fechaInicio = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),(String)fila.get(ScsProcedimientosBean.C_FECHADESDEVIGOR)));
			fechaFin = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),(String)fila.get(ScsProcedimientosBean.C_FECHAHASTAVIGOR)));
%>
  			<siga:FilaConIconos fila="<%=String.valueOf(cont)%>" visibleConsulta="no" pintarEspacio="no" botones="E,B" clase="listaNonEdit">
			
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idProc%>"><%=codigo%></td>
				<td><%=nombre%></td>
				<td align="center"><%=fechaInicio%></td>
				<td align="center"><%=fechaFin%></td>
				<td align="right"><%=UtilidadesNumero.formatoCampo(precio)+ " &euro;"%></td>
				

			</siga:FilaConIconos>		


<%		} // del for
	} // del if 
	%>			

		</siga:Table>


<!-- FIN: LISTA DE VALORES -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
