<!-- contrariosAsistencia.jsp -->

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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.form.ContrariosAsistenciaForm" %>
<%@ page import="com.siga.gratuita.action.PersonaJGAction" %>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String modoPestanha = (String)request.getAttribute("modoPestanha");
	String anio = (String)request.getAttribute("anio");
	String numero = (String)request.getAttribute("numero");
	
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

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<%--siga:Titulo 
		titulo="gratuita.contrariosDesigna.literal.titulo" 
		localizacion="gratuita.contrariosDesigna.literal.location"/--%>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>
		function buscarContrarios()
		{
<%			if (esFichaColegial) {%>
				document.forms[0].action = "/SIGA/JGR_ContrariosAsistenciaLetrado.do";
<%			} else { %>
				document.forms[0].action = "/SIGA/JGR_ContrariosAsistencia.do";
<%			}%>
			document.forms[0].target = "resultado";
			document.forms[0].modo.value = "buscarContrarios";
			document.forms[0].submit();
		}
	</script>

</head>

<body class="tablaCentralCampos" onload="ajusteAltoBotones('resultado');buscarContrarios()">

    <table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
			<td class="titulitosDatos">
			
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (usr);
					Hashtable hTitulo = adm.getTituloPantallaAsistencia(usr.getLocation(), anio, numero);
					if (hTitulo != null) {
						t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
						t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
						t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
						t_anio      = (String)hTitulo.get(ScsAsistenciasBean.C_ANIO);
						t_numero    = (String)hTitulo.get(ScsAsistenciasBean.C_NUMERO);
					}
				%>
				<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> 
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>	


		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
<%		String sAction1 = esFichaColegial ? "JGR_ContrariosAsistenciaLetrado.do" : "JGR_ContrariosAsistencia.do";%>
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="<%=sAction1%>" method="post" target="mainPestanas" style="display:none">
			<html:hidden property = "actionModal" value = ""/>			
			<html:hidden name="contrariosAsistenciaForm" property = "modo" value = ""/>
			<html:hidden name="contrariosAsistenciaForm" property = "anio" value="<%=anio%>"/>
			<html:hidden name="contrariosAsistenciaForm" property = "numero" value="<%=numero%>"/>
			<html:hidden name="contrariosAsistenciaForm" property = "modoPestanha" value="<%=modoPestanha%>"/>
			<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>
		</html:form>
		
		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					 id="resultado"
					 name="resultado" 
					 scrolling="no"
					 frameborder="0"
				 	 marginheight="0"
				 	 marginwidth="0";					 
					 class="frameGeneral">
		</iframe>
		
		<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionVolver() {
			/*document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();*/
			
		
			<%
			// indicamos que es boton volver
			ses.setAttribute("esVolver","1");
			%>
<%
			String sAction2 = esFichaColegial ? "JGR_AsistenciasLetrado.do" : "JGR_Asistencia.do";
%>          document.forms[0].target="mainWorkArea";
			document.forms[0].action = "<%=sAction2%>";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();
		}
		
		
		function accionNuevo() 
		{	
			document.forms[0].modo.value="nuevo";
			
			document.forms[0].target="submitArea";
			
			var resultado=ventaModalGeneral(document.forms[1].name,"G");
			if (resultado=="MODIFICADO")
				buscarContrarios();
		}

		function refrescarLocal()
		{
			buscarContrarios();
		}
</script>		

<%
		String sClasePestanas = "botonesDetalle";
		String sActionE = esFichaColegial ? "/JGR_ContrariosAsistenciaPerJGLetrado.do" : "/JGR_ContrariosAsistenciaPerJG.do";
%>
<%
		String sBoton = esFichaColegial ? "N" : "V,N";
%>
	<siga:ConjBotonesAccion botones="<%=sBoton%>" clase="<%=sClasePestanas%>" modo="<%=modoPestanha%>" />

<%	String sAction = esFichaColegial ? "JGR_ContrariosAsistenciaPerJGLetrado.do" : "JGR_ContrariosAsistenciaPerJG.do";%>
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="<%=sAction%>" method="post" target="submitArea">
			<html:hidden property = "actionModal" value = ""/>			
			<input type="hidden" name="idInstitucionJG" value="<%=usr.getLocation() %>">
			<input type="hidden" name="idPersonaJG" value="">
	
			<input type="hidden" name="idInstitucionASI" value="<%=usr.getLocation() %>">
			<input type="hidden" name="anioASI" value="<%=anio %>">
			<input type="hidden" name="numeroASI" value="<%=numero %>">
	
			<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.ASISTENCIA_CONTRARIOS %>">
			<input type="hidden" name="tituloE" value="gratuita.mantAsistencias.literal.tituloCO">
			<input type="hidden" name="localizacionE" value="">
			<input type="hidden" name="accionE" value="nuevo">
			<input type="hidden" name="actionE" value="<%=sActionE%>">
			<input type="hidden" name="pantallaE" value="M">
			
			<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>
						
		</html:form>
		
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>
</html>