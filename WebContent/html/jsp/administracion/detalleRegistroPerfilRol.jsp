<!-- detalleRegistroPerfilRol.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userbean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	Vector datos = (Vector)request.getAttribute("datos");
	
	request.removeAttribute("datos");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");

	Hashtable htDatos = (Hashtable)datos.elementAt(0);
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				listadoPerfilRolForm.reset();
			}
			
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				var oPerfilRol = document.getElementsByName("perfilRol");
				sub();
				listadoPerfilRolForm.grupoPorDefecto.value=oPerfilRol[0].value;
				listadoPerfilRolForm.submit();
	
				window.top.returnValue="MODIFICADO";
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="administracion.perfilRol.titulo"/>
			</td>
		</tr>
	</table>		
		
		<div id="camposRegistro" class="posicionModalPeque" align="center">
			<html:form action="/ADM_GestionarPerfilRol.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "Modificar"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "grupoPorDefecto" value = ""/>
				<input type="hidden" name="idRol" value="<%=htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_IDROL)%>">
				
				<table class="tablaCentralCamposPeque" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="administracion.perfilRol.titulo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.perfilRol.literal.Rol"/>
										</td>
										<td class="labelTextValue">
											<%=htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_DESCRIPCION)%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.perfilRol.literal.grupoPorDefecto"/>
										</td>				
										<td class="labelTextValue">
<%
											ArrayList al = new ArrayList();
											al.add(htDatos.get(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_IDPERFIL));
										
											String[] parametros = new String[3];
											parametros[0] = userbean.getLocation();
											parametros[1] = userbean.getLocation();
											parametros[2] = (String)htDatos.get(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_IDROL);
%>
											<siga:ComboBD nombre = "perfilRol" tipo="perfilRol" elementoSel="<%=al%>" parametro="<%=parametros%>" clase="boxCombo" obligatorio="false"/>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
<%
			String botones = "R,Y,C";
%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="P"/>
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>