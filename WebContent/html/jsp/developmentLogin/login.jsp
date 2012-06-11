<!-- login.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>

<%@ page import="java.util.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	if (src==null) {
	  src=new Properties();
	}
	
   	ArrayList idADM = new ArrayList();
   	idADM.add(0,"ADM");
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<script>
		function entrar()
		{
			if (validarCampos())
			{
				var urlGet=document.frmLogin.action+"?location="+frmLogin.location.value+"&profile="+frmLogin.profile.value+"&user="+frmLogin.user.value+"&letrado="+frmLogin.letrado.value+"&tmpLoginInstitucion="+frmLogin.tmpLoginInstitucion.value+"&posMenu="+frmLogin.posMenu.value;
				//window.open(urlGet,'sigaTop','channelmode=no;directories=no;fullscreen=no;height=760;width=1024;location=no;menubar=no;resizable=no;scrollbars=no;status=no;titlebar=no;toolbar=no');
				frmLogin.submit();
			}
		}

		function entradaDirecta()
		{
			frmLogin.location.value="2040";
			frmLogin.profile.value="ADM";
			frmLogin.user.value="";
			frmLogin.letrado.value="N";
			var urlGet=document.frmLogin.action+"?location="+frmLogin.location.value+"&profile="+frmLogin.profile.value+"&user="+frmLogin.user.value+"&letrado="+frmLogin.letrado.value+"&tmpLoginInstitucion="+frmLogin.tmpLoginInstitucion.value+"&posMenu="+frmLogin.posMenu.value;
			frmLogin.submit();
		}
 
		function entradaDirectaGen()
		{
			frmLogin.location.value="2000";
			frmLogin.profile.value="ADM";
			frmLogin.user.value="";
			frmLogin.letrado.value="N";
			var urlGet=document.frmLogin.action+"?location="+frmLogin.location.value+"&profile="+frmLogin.profile.value+"&user="+frmLogin.user.value+"&letrado="+frmLogin.letrado.value+"&tmpLoginInstitucion="+frmLogin.tmpLoginInstitucion.value+"&posMenu="+frmLogin.posMenu.value;
			frmLogin.submit();
		}
		
		
		function validarCampos()
		{
			frmLogin.location.value="";
			frmLogin.profile.value="";
			frmLogin.user.value="";
			frmLogin.letrado.value="";

			if (document.frmLogin.tmpLoginInstitucion.value=="")
			{
				alert("Debe seleccionar una Institución");
				
				return false;
			}
			
			else
			{
				frmLogin.location.value=document.frmLogin.tmpLoginInstitucion.value;
			}

			if (document.frmLogin.tmpLoginPerfil.value=="")
			{
				alert("Debe seleccionar un Perfil");
				
				return false;
			}
			
			else
			{
				frmLogin.profile.value=document.frmLogin.tmpLoginPerfil.value;
			}

/*			if (document.frmLogin.tmpLoginUsuario.value=="")
			{
				alert("Debe seleccionar un Usuario");
				
				return false;
			}
			
			else
			{
				frmLogin.user.value=document.frmLogin.tmpLoginUsuario.value;
			}
*/
			frmLogin.letrado.value=document.frmLogin.sLetrado.value;
			
			return true;
		}
		</script>
	</head>
	
	<body vlink=black alink=black>
		<form name="frmLogin" method="POST" action="<%=app%>/developmentLogin.do" target="_top">
		<br>
		
		<p class="titulos"><siga:Idioma key="index.bienvenida"/></p>
		
		<table border="1" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="labelText">Institución</td>
				<td ><siga:ComboBD nombre="tmpLoginInstitucion" tipo="tmpLoginInstitucion" clase="boxCombo" accion="Hijo:tmpLoginPerfil"/></td>
			</tr>
			<tr>
				<td class="labelText">Perfil</td>
				<td>
					<siga:ComboBD nombre="tmpLoginPerfil" tipo="tmpLoginPerfil" clase="box" filasMostrar="20" elementoSel="<%=idADM%>" seleccionMultiple="true" hijo="t" obligatorioSinTextoSeleccionar="true"/>
				</td>		
			</tr>
<!--			<tr>
				<td class="labelText">Usuario</td>
				<td ><siga:ComboBD nombre="tmpLoginUsuario" tipo="tmpLoginUsuario" clase="boxCombo" hijo="t"/></td>
			</tr>
-->			<tr>
				<td class="labelText">¿Letrado?</td>
				<td >
					<select name="sLetrado" class="boxCombo">
						<option value=N>NO, no soy Letrado</option>
						<option value=S>SÍ, soy Letrado</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<br>
					<input type="button" class="button" value="Entrar" onClick="entrar()" title="Entrar con los datos de los combos">
					<input type="button" class="button" value="León" onClick="entradaDirecta()" title="Entrar a LEON como ADMINistrador NO colegiado">
					<input type="button" class="button" value="General" onClick="entradaDirectaGen()"  title="Entrar a GENERAL como ADMINistrador NO colegiado">
				</td>
			</tr>

			<tr>
			<td colspan="2" class="labelText">
				<CENTER>
					<a href="/SIGA/CEN_SolicitudesIncorporacionAccesoDirecto.do?idInstitucion=2032"	onClick="false" target="_top">
						<siga:Idioma key="censo.busquedaSolicitudesIncorporacion.literal.cabecera"/>
					</a>
				</CENTER>
			</td>
			</tr>
			
		</table>
		
			<input type="hidden" name="location">
			<input type="hidden" name="user">
			<input type="hidden" name="profile">
			<input type="hidden" name="letrado">
			<input type="hidden" name="posMenu" value="0">
		</form>
	</body>
</html>
