<!DOCTYPE html>
<html>
<head>
<!-- index.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<% String app=request.getContextPath(); %>


	
		<title><siga:Idioma key="index.title"/></title>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/css/SIGA.css">
	
		<style type="text/css">

		BODY
		{
			cursor:url("<%=app%>/html/imagenes/arrow_rl.cur");
		}
	
		.titular 
		{
			color:111180;
			FONT-SIZE:18px; 
			FONT-FAMILY:Verdana, Arial, Helvetica, sans-serif; 
			TEXT-ALIGN:center
		}
		</style>
	
		<script language="JavaScript">
			function redirect(sUserCode, sProfile, sLocation, sAccess) {
				win = self;				
				if(win.opener == null) {
	    			win.opener = top;
	  			}	  			
				frmLogin.user.value=sUserCode;
				frmLogin.profile.value=sProfile;
				frmLogin.location.value=sLocation;
				frmLogin.access.value=sAccess;
				
				for (i=0; i<optPosMenu.length; i++) {
					if (optPosMenu[i].checked) {
						frmLogin.posMenu.value=optPosMenu[i].value;
						i=optPosMenu;
					}
				}				
				frmLogin.submit();
			}
			
			function getLocation(sUsuCod, sProfileCod) {
				var loc = showModalDialog("<%=app%>/html/jsp/general/locationSelector.jsp", "", "center:yes; dialogHeight:150px; dialogWidth:350px; edge:Raised; center:Yes; help:No; scroll:No; resizable:No; status:No; unadorned:Yes;");
				window.top.focus();
				if(loc!=null && loc!='') {
					redirect(sUsuCod, sProfileCod, loc, "DENY");
				}
			}
	  	</script>
	</head>
	
	<body background="<%=app%>/html/imagenes/fondo.jpg">
		<br><br>
		<p class="titular"><siga:Idioma key="index.bienvenida"/></p>
		<div id="errorDiv" style="text-align:center; display:none" class="nonEditRed">
			<script language="JavaScript">
				var correctwidth=1024;
				var correctheight=768;
				
				if (screen.width!=correctwidth || screen.height!=correctheight)
				{
					var errorMsg='<siga:Idioma key="index.resolucion.1" arg0="1024" arg1="768"/>\r\n';
					errorMsg+='<siga:Idioma key="index.resolucion.2"/>';
					errorMsg+= screen.width + "x" + screen.height + ".\r\n";
					errorMsg+='<siga:Idioma key="index.resolucion.3"/>';
					document.write(errorMsg);
					//alert(errorMsg);
					errorDiv.style.display='';
				}
			</script>		
		</div>
		<div style="text-align:center">
			<form name="frmLogin" method="POST" action="<%=app%>/developmentLogin.do">
				<input type="hidden" name="user">
				<input type="hidden" name="profile">
				<input type="hidden" name="location">
				<input type="hidden" name="posMenu">
				<input type="hidden" name="access">
	
				<fieldset style="width:45%; text-align:center">
					<legend class="titlebar" align="center"><siga:Idioma key="index.tipoAcceso" /></legend>
					<br>
					<table width="100%" border="0" align="center">
						<tr>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td nowrap>
								<ol style="text-align:left;">
								<li class="titulitos">
									<a href="javascript://" onclick="redirect('0', 'ADM', '2032', 'Acceso Total'); return false;" class="titulitos">Administrador CGAE ............................................. (Perfil ADM)</a>
								</li><br><br>
								<li class="titulitos">
									<a href="javascript://" onclick="redirect('0', 'ADM', '2074', 'Acceso Total'); return false;" class="titulitos">Administrador Toledo ........................................... (Perfil ADM)</a>
								</li><br><br>					
								<li class="titulitos">
									<a href="javascript://" onclick="redirect('0', 'AGE', '2032', 'Sólo Lectura'); return false;" class="titulitos">Personal Administrativo CGAE ............................. (Perfil AGE)</a>
								</li><br><br>
								<li class="titulitos">
									<a href="javascript://" onclick="redirect('0', 'AGE', '2032', 'Sólo Lectura'); return false;" class="titulitos">Personal Administrativo Consejo Auton&oacute;mico ..... (Perfil AGE)</a>
								</li><br><br>
								<li class="titulitos">
									<a href="javascript://" onclick="redirect('0', 'LEC', '2032'); return false;" class="titulitos">Usuario de sólo Lectura ....................................... (Perfil LEC)</a>
								</li><br><br>
								<li class="titulitos">
									<a href="javascript://" onclick="getLocation('0', 'ADM'); return false;" class="titulitos">Administrador Colegio .......................................... (Perfil ADM)</a>
								</li><br><br>
								<li class="titulitos">
									<a href="javascript://" onclick="getLocation('0', 'AGE'); return false;" class="titulitos">Personal Administrativo Colegio .......................... (Perfil AGE)</a>
								</li><br><br>
								<li class="titulitos">
									<a href="javascript://" onclick="getLocation('0', 'COL'); return false;" class="titulitos">Colegiado .............................................................. (Perfil COL)</a>
								</li><br><br>
								<li class="titulitos">
									<a href="javascript://" onclick="getLocation('0', 'NCL'); return false;" class="titulitos">Nuevo Colegiado .................................................. (Perfil NCL)</a>
								</li>
								</ol>
							</td>
						</tr>
					</table>
				</fieldset>
				<br><br>
			</form>
			<table>
				<tr>
					<td class=titulitos>Menú Arriba</td>
					<td><input type=radio value="0" name="optPosMenu" checked=true></td>
				</tr>
				<tr>
					<td class=titulitos>Menú Izquierda</td>
					<td><input type=radio value="1" name="optPosMenu"></td>
				</tr>
			</table>
		</div>
	</body>
</html>