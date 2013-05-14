<!-- abrirDenunciantes.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
-->

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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.expedientes.form.ExpDenuncianteForm"%>

<!-- JSP -->
<% 
	String app           = request.getContextPath();
	HttpSession ses      = request.getSession();
	Properties src       = (Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector vDatos        = (Vector)request.getAttribute("datos");
	Vector vDatosPersona = (Vector)request.getAttribute("datosPersonas");
	UsrBean user         = (UsrBean) ses.getAttribute("USRBEAN");
	
	String editable = (String)request.getParameter("editable");		
	String soloSeguimiento = (String)request.getParameter("soloSeguimiento");	
	boolean bEditable=false;
	if (soloSeguimiento.equals("true")){
		bEditable=false;
	}else{
		bEditable = editable.equals("1")? true : false;
	}
	String botones = bEditable? "C,E,B" : "C";
	String botones2 = bEditable? "V,N" : "V";
	
	String denunciado = (String)request.getAttribute("denunciado");
	String nombreTipoExpediente = (String)request.getParameter("nombreTipoExpediente");

	String idInstitucion    = (String)request.getParameter("idInstitucion");
	String idTipoExpediente = (String)request.getParameter("idTipoExpediente");
	String numExpediente = (String)request.getParameter("numeroExpediente");
	if(numExpediente==null || numExpediente.equals(""))
         numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
	
 	String anioExpediente = (String)request.getParameter("anioExpediente");
	if(anioExpediente==null || anioExpediente.equals(""))
         anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");	
	String idInstitucion_TipoExpediente = (String)request.getParameter("idInstitucion_TipoExpediente");

	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");
		
	request.removeAttribute("datos");
	
	String tituloPagina=(String)request.getAttribute("tituloPagina");
%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="<%=tituloPagina%>" 
			localizacion="expedientes.auditoria.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>
	

		<table class="tablaTitulo" align="center" cellspacing="0">
		<html:form action="/EXP_Auditoria_Denunciante.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			
			<html:hidden property = "idPersona" />
			<html:hidden property = "idDireccion"/>
			<html:hidden property = "numColegiado"/>
			<html:hidden property = "idInstitucionOrigen"/>
			
			<html:hidden property = "idInstitucion"    value = "<%=idInstitucion%>"/>
			<html:hidden property = "idTipoExpediente" value = "<%=idTipoExpediente%>"/>
			<html:hidden property = "numExpediente"    value = "<%=numExpediente%>"/>
			<html:hidden property = "anioExpediente"   value = "<%=anioExpediente%>"/>
			<html:hidden property = "idInstitucion_TipoExpediente" value = "<%=idInstitucion_TipoExpediente%>"/>
			<tr>
				<td id="titulo" class="titulitosDatos">
					<% ExpDenuncianteForm f = (ExpDenuncianteForm)request.getAttribute("ExpDenuncianteForm"); %>
					<%=f.getTituloVentana()%>
					<html:hidden property = "tituloVentana" value = "<%=f.getTituloVentana()%>"/>
				</td>
			</tr>
		</html:form>
		
		</table>
	
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="expedientes.auditoria.literal.nombreyapellidos,
		   		  	expedientes.auditoria.literal.nif,
		   		  	expedientes.auditoria.literal.telefono,"
		   		  columnSizes="60,15,15,10"
		   		  modal="m">
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (vDatos==null || vDatos.size()==0 || vDatosPersona==null || vDatosPersona.size()==0)
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
				  		ExpDenuncianteBean bean = (ExpDenuncianteBean)vDatos.elementAt(i);
				  		CenPersonaBean persona  = (CenPersonaBean)vDatosPersona.get(i*2);
				  		String telefono         = (String)vDatosPersona.get(i*2+1);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdInstitucion_TipoExpediente()%>">						
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getIdTipoExpediente()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getNumeroExpediente()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=bean.getAnioExpediente()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=bean.getIdDenunciante()%>">
						<%=UtilidadesString.mostrarDatoJSP(persona.getNombre())+" "+UtilidadesString.mostrarDatoJSP(persona.getApellido1())+" "+UtilidadesString.mostrarDatoJSP(persona.getApellido2())%>
					</td>
					<td><%=persona.getNIFCIF().equals("")?"&nbsp;":persona.getNIFCIF()%></td>
					<td><%=telefono%>&nbsp;</td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:Table>

		<!-- FIN: LISTA DE VALORES -->

	<!-- INICIO: BOTONES REGISTRO -->

		<siga:ConjBotonesAccion botones="<%=botones2%>" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->


		<html:form action="/EXP_AuditoriaExpedientes.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "avanzada" value = ""/>		
		</html:form>

	
		<html:form action="/CEN_DatosGenerales" method="POST" target="mainWorkArea">
			<input type="hidden" name="actionModal" value="1">
			<input type="hidden" name="modo" value="designarArt27">
			<input type="hidden" name="numIdentificacion">
			<input type="hidden" name="colegiadoen">
			<input type="hidden" name="idDireccion">
		</html:form>
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		
		function refrescarLocal()
		{			
			document.location.reload();
			
		}


		//Asociada al boton Volver -->
		function accionVolver() 
		{					
				<% if (busquedaVolver.equals("AB")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% } else if (busquedaVolver.equals("NB")){ %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<% } else if(busquedaVolver.equals("AV")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% }  else if (busquedaVolver.equals("Al")){%>
					document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<%}%>
				
				document.forms[1].submit();				
		}
		
		//Asociada al boton Nuevo -->
		function accionNuevo() 
		{		
			var resultado=ventaModalGeneral("datosGeneralesForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.ExpDenuncianteForm.idPersona.value=resultado[0];
				document.ExpDenuncianteForm.numColegiado.value=resultado[1];
				document.ExpDenuncianteForm.idInstitucionOrigen.value=resultado[5];
				document.ExpDenuncianteForm.idDireccion.value=resultado[7];
				document.ExpDenuncianteForm.modo.value="insertar";
				
				document.ExpDenuncianteForm.submit();
				refrescarLocal();
			}
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>