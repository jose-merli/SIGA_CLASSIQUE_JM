<!-- listarContrariosAsistencia.jsp -->
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
<%@ page import="com.siga.gratuita.form.ContrariosAsistenciaForm"%>
<%@ page import="com.siga.gratuita.action.PersonaJGAction"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	ContrariosAsistenciaForm miForm = (ContrariosAsistenciaForm) request.getAttribute("contrariosAsistenciaForm");
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
	Vector obj = (Vector)request.getAttribute("vContrariosAsistencias");

	String modoPestanha = (String) ses.getAttribute("Modo");

	boolean esFichaColegial = false;

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null)
			&& ((sEsFichaColegial.equalsIgnoreCase("1"))||(sEsFichaColegial.equalsIgnoreCase("true"))  )) {
		esFichaColegial = true;
	}
%>	

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<% if(esFichaColegial){ %>
		<siga:Titulo titulo="gratuita.contrariosAsistencia.literal.titulo" 
				 localizacion="censo.gratuita.asistencias.literal.localizacion"/>
	<% } else { %>
		<siga:Titulo titulo="gratuita.contrariosAsistencia.literal.titulo" 
				 localizacion="gratuita.mantAsistencias.literal.localizacion"/>
	<% } %>	

	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">	
			function refrescarLocal(){
					parent.buscarContrarios();
			}	
	</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
<%
		String sAction = esFichaColegial ? "/JGR_ContrariosAsistenciaPerJGLetrado.do" : "/JGR_ContrariosAsistenciaPerJG.do" ;
%>
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="<%=sAction%>" method="post" target="submitArea" style="display:none" styleId="PersonaJGForm">
			<input type="hidden" id="modo" name="modo" value="abrirPestana">
			
			<input type="hidden" id="idInstitucionJG" name="idInstitucionJG" value="<%=usr.getLocation() %>">
			<input type="hidden" id="idPersonaJG" name="idPersonaJG" value="">
	
			<input type="hidden" id="idInstitucionASI" name="idInstitucionASI" value="<%=usr.getLocation() %>">
			<input type="hidden" id="anioASI" name="anioASI" value="<%=miForm.getAnio() %>">
			<input type="hidden" id="numeroASI" name="numeroASI" value="<%=miForm.getNumero() %>">
	
			<input type="hidden" id="conceptoE" name="conceptoE" value="<%=PersonaJGAction.ASISTENCIA_CONTRARIOS %>">
			<input type="hidden" id="tituloE" name="tituloE" value="gratuita.mantAsistencias.literal.tituloCO">
			<input type="hidden" id="localizacionE" name="localizacionE" value="">
			<input type="hidden" name="accionE" id="accionE" value="nuevo">
			<input type="hidden" name="actionE" id="actionE" value="<%=sAction%>">
			<input type="hidden" id="pantallaE" name="pantallaE" value="M">
		</html:form>	
		

		<!-- Campo obligatorio -->
		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="gratuita.defendidosDesigna.literal.nif,gratuita.defendidosDesigna.literal.nombreApellidos,"
		   columnSizes="15,70,15"
		   modal="G">

		<% if (obj==null || obj.size()==0) { %>
					<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
		<% } else { %>
		
			  <%
		    	int recordNumber=1;
				while ((recordNumber) <= obj.size()) {
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			 	%>	
				  	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="E,C,B" clase="listaNonEdit" modo="<%=modoPestanha%>">
						<td>
<!--						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDPERSONA")%>'> -->

							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=PersonaJGAction.ASISTENCIA_CONTRARIOS%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="gratuita.contrariosAsistencia.literal.titulo">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="gratuita.contrariosAsistencia.literal.titulo">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_4" name="oculto<%=String.valueOf(recordNumber)%>_4" value="editar">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_5" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=usr.getLocation()%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_6" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=hash.get("IDPERSONA")%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_7" name="oculto<%=String.valueOf(recordNumber)%>_7" value="<%=usr.getLocation()%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_8" name="oculto<%=String.valueOf(recordNumber)%>_8" value="<%=miForm.getAnio() %>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_9" name="oculto<%=String.valueOf(recordNumber)%>_9" value="<%=miForm.getNumero() %>">
					
						
								&nbsp;<%=hash.get("NIF")%></td>
						<td>&nbsp;<%=hash.get("NOMBRE")%></td>
					</siga:FilaConIconos>	
				<%recordNumber++;%>
				<% } %>
		<% } %>

		</siga:Table>


<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
	
</html>