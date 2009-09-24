<!-- defensaJuridicaDesigna.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getAttribute("resultado");
	Hashtable hash = (Hashtable)obj.get(0);
	String modo = (String) ses.getAttribute("Modo");
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="gratuita.defensaJuridicaDesigna.literal.titulo" 
		localizacion="gratuita.defensaJuridicaDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		        <table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
						ScsDesignaAdm adm = new ScsDesignaAdm (usr);
						Hashtable hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(), (String)hash.get("ANIO"), (String)hash.get("NUMERO"),(String)hash.get("IDTURNO"));

						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsDesignaBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsDesignaBean.C_CODIGO);
							
						}
					
					%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>
		<fieldset>
		<table  class="tablaCentralCamposPeque"  align="center">
		<html:form action="/JGR_DefensaJuridiciaDesignas.do" method="post" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property="idTurno" value = '<%=(String)hash.get("IDTURNO")%>'/>
		<html:hidden property="numero" value = '<%=(String)hash.get("NUMERO")%>'/>
		<html:hidden property="anio" value = '<%=(String)hash.get("ANIO")%>'/>
		
		<html:hidden property="IDTURNO" value = '<%=(String)hash.get("IDTURNO")%>'/>
		<html:hidden property="NUMERO" value = '<%=(String)hash.get("NUMERO")%>'/>
		<html:hidden property="ANIO" value = '<%=(String)hash.get("ANIO")%>'/>

		<tr>
			<td class="labelText" width="200">
				<siga:Idioma key="pestana.justiciagratuitaasistencia.defensajuridica"/>
			</td>
			<td class="labelText">
				<%if ((modo!=null)&&(!modo.equalsIgnoreCase("ver"))&&(!modo.equalsIgnoreCase("consultar"))){%>
					<textarea class="box"  scroll="none" name="defensaJuridica" rows="25" cols="150" ><%=(String)hash.get("DEFENSAJURIDICA")%></textarea>
				<%}else{%>
					<textarea class="boxConsulta" onKeyDown="cuenta(this,1023)" onChange="cuenta(this,1023)" scroll="none" name="defensaJuridica" rows="25" cols="150" readonly="true"><%=(String)hash.get("DEFENSAJURIDICA")%></textarea>
				<%}%>
			</td>
		</tr>
		</table>
		</html:form>
		</fieldset>

<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
<script language="JavaScript">

	
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[0].target = "mainWorkArea";	
			document.forms[0].action="JGR_Designas.do";
			document.forms[0].modo.value="volverBusqueda";
			document.forms[0].submit();
		}
		
		function accionGuardar() 
		{	
			sub();
			document.forms[0].modo.value = "modificar";
			document.forms[0].submit();
		}
		
		function refrescarLocal()
		{
			document.forms[0].modo.value="";
			document.forms[0].submit();
		}
		
</script>		
		<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle"  modo="<%=modo%>"/>
	
	</body>
</html>
		  
		
