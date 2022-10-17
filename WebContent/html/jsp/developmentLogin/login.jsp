<!DOCTYPE html>
<html>
<head>
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
<%@ taglib uri="c.tld" prefix="c"%>


<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>
<%@ page import="com.siga.Utilidades.UtilidadesString" %>
<%@ page import="java.util.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	String[] duplaRoles = request.getAttribute("CAS-roles");
	
	boolean isAdminGenFromCGae = false;
	List<String> codExternoInstituciones = new ArrayList<String>(); 
	for (int i = 0; i < duplaRoles.length; i++) {
		String duplaRol = duplaRoles[i];
		String[] dupla = duplaRol.split(" ");
		String codExternoInstitucion = dupla[0];
		
		String rol = dupla[1];
		if((codExternoInstitucion.equals("AC0000") || codExternoInstitucion.equals("AC9999")) && rol.equalsIgnoreCase("SIGA-Admin")){
			isAdminGenFromCGae = true;
//			break;
		}
		if(!codExternoInstituciones.contains(codExternoInstitucion))
			codExternoInstituciones.add(codExternoInstitucion);
		
	}
	StringBuilder codExternoBuilder = new StringBuilder();
	for (String codExternoInstitucion : codExternoInstituciones) {
		codExternoBuilder.append(codExternoInstitucion);
		codExternoBuilder.append(",");
		
	}
	codExternoBuilder.deleteCharAt(codExternoBuilder.length()-1);
	String parametro[] = new String[1];
	parametro[0] = codExternoBuilder.toString();
   	ArrayList idADM = new ArrayList();
   	idADM.add(0,"ADG");
%>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>

		<script src="<%=app%>/html/js/SIGA.js?v=${sessionScope.VERSIONJS}" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

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

		function entradaDirectaCaceres()
		{
			frmLogin.location.value="2014";
			frmLogin.profile.value="ADG";
			frmLogin.user.value="";
			frmLogin.letrado.value="N";
			var urlGet=document.frmLogin.action+"?location="+frmLogin.location.value+"&profile="+frmLogin.profile.value+"&user="+frmLogin.user.value+"&letrado="+frmLogin.letrado.value+"&tmpLoginInstitucion="+frmLogin.tmpLoginInstitucion.value+"&posMenu="+frmLogin.posMenu.value;
			frmLogin.submit();
		}
		function entradaDirectaJaen()
		{
			frmLogin.location.value="2035";
			frmLogin.profile.value="ADG";
			frmLogin.user.value="";
			frmLogin.letrado.value="N";
			var urlGet=document.frmLogin.action+"?location="+frmLogin.location.value+"&profile="+frmLogin.profile.value+"&user="+frmLogin.user.value+"&letrado="+frmLogin.letrado.value+"&tmpLoginInstitucion="+frmLogin.tmpLoginInstitucion.value+"&posMenu="+frmLogin.posMenu.value;
			frmLogin.submit();
		}
		
		function entradaDirectaReus()
		{
			frmLogin.location.value="2057";
			frmLogin.profile.value="ADG";
			frmLogin.user.value="";
			frmLogin.letrado.value="N";
			var urlGet=document.frmLogin.action+"?location="+frmLogin.location.value+"&profile="+frmLogin.profile.value+"&user="+frmLogin.user.value+"&letrado="+frmLogin.letrado.value+"&tmpLoginInstitucion="+frmLogin.tmpLoginInstitucion.value+"&posMenu="+frmLogin.posMenu.value;
			frmLogin.submit();
		}
		function entradaDirecta(idinstitucion,profile)
		{
			frmLogin.location.value=idinstitucion;
			frmLogin.profile.value="ADG";
			if(profile)
				frmLogin.profile.value=profile;
			
			frmLogin.user.value="";
			frmLogin.letrado.value="N";
			var urlGet=document.frmLogin.action+"?location="+frmLogin.location.value+"&profile="+frmLogin.profile.value+"&user="+frmLogin.user.value+"&letrado="+frmLogin.letrado.value+"&tmpLoginInstitucion="+frmLogin.tmpLoginInstitucion.value+"&posMenu="+frmLogin.posMenu.value;
			frmLogin.submit();
		}
		
		
 
		function entradaDirectaGen()
		{
			frmLogin.location.value="2000";
			frmLogin.profile.value="ADG";
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

			frmLogin.letrado.value=document.frmLogin.sLetrado.value;
			
			return true;
		}
		</script>
	</head>
	
	<body vlink=black alink=black>
		<form name="frmLogin" method="POST" action="<%=app%>/developmentLogin.do" target="_top">
		<br>
		
		<p class="titulos" style="text-align:center"><siga:Idioma key="index.bienvenida"/></p>
		
		<table align="center">
			<tr>
				<td class="labelText">Institución</td>
				<td>
				<c:choose>
					<c:when test="${isAdminGenFromCGae == true}">
						<siga:ComboBD nombre="tmpLoginInstitucion" tipo="tmpLoginInstitucion" clase="boxCombo" accion="Hijo:tmpLoginPerfil"  estilo="width:300px" />	
					</c:when>
					<c:otherwise>
						<siga:ComboBD nombre="tmpLoginInstitucionExterna" tipo="tmpLoginInstitucionExterna" parametro="<%=parametro%>" clase="boxCombo" accion="Hijo:tmpLoginPerfil"  estilo="width:300px" />
					</c:otherwise>
				</c:choose>
				
				
				</td>
				<td valign="middle" align="center" ><input type="button" class="button" value="Entrar" onClick="entrar()" title="Entrar con los datos de los combos"></td>
			</tr>
			<tr>
				<td class="labelText">¿Letrado?</td>
				<td colspan="2">
					<select name="sLetrado" class="boxCombo" style="width:300px;">
						<option value=N>NO, no soy Letrado</option>
						<option value=S>SÍ, soy Letrado</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="labelText">Perfil</td>
				<td colspan="2">
					<siga:ComboBD nombre="tmpLoginPerfil" tipo="tmpLoginPerfil" clase="box" ancho="300" filasMostrar="20" elementoSel="<%=idADM%>" seleccionMultiple="true" hijo="t" obligatorioSinTextoSeleccionar="true"/>
				</td>		
			</tr>
			
			<tr>
				<td colspan="3" valign="middle" align="center" >
					<input type="button" class="button" value="Colegio Cáceres" onClick="entradaDirectaCaceres()" title="Entrar a Caceres como ADMINistrador NO colegiado">
					&nbsp;
					<input type="button" class="button" value="Colegio Jaen" onClick="entradaDirectaJaen()" title="Entrar a Jaen como ADMINistrador NO colegiado">
					&nbsp;
					<input type="button" class="button" value="Colegio Zaragoza" onClick="entradaDirecta('2083','ADG')" title="Entrar a Colegio Zaragoza">
					&nbsp;
					<input type="button" class="button" value="Colegio Reus" onClick="entradaDirectaReus()" title="Entrar a Reus como ADMINistrador NO colegiado">
					&nbsp;
					<input type="button" class="button" value="Consejo Calalunya" onClick="entradaDirecta('3001')" title="Entrar a Consejo Catalunya como ADMINistrador NO colegiado">
					
					&nbsp;
					<input type="button" class="button" value="GENERAL" onClick="entradaDirectaGen()"  title="Entrar a GENERAL como ADMINistrador NO colegiado">
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
