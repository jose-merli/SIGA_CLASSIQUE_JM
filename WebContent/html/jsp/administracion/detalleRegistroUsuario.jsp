<!-- detalleRegistroUsuario.jsp -->
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
	Vector datos = (Vector)request.getAttribute("datos");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	request.removeAttribute("datos");
	request.removeAttribute("editable");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">

			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				listadoUsuariosForm.reset();
			}
		
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				listadoUsuariosForm.submit();
				window.returnValue="MODIFICADO";
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="administracion.usuarios.titulo"/>
			</td>
		</tr>
	</table>	
		
		<div id="camposRegistro" class="posicionModalMedia" align="center">
			<html:form action="/ADM_GestionarUsuarios.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "Modificar"/>
				<html:hidden property = "hiddenFrame" value = "1"/>

<%
			AdmUsuariosBean bean = (AdmUsuariosBean)datos.elementAt(0);
%>
				<table class="tablaCentralCamposMedia" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="administracion.usuarios.titulo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.usuarios.literal.nombre"/>
										</td>				
										<td class="labelTextValue">
											<%=(String)datos.elementAt(1)%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.usuarios.literal.NIF"/>
										</td>				
										<td class="labelTextValue">
											<%=bean.getNIF()%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.usuarios.literal.fechaAlta"/>
										</td>				
										<td class="labelTextValue">
											<%=GstDate.getFormatedDateShort(userBean.getLanguage(), bean.getFechaAlta())%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="administracion.usuarios.literal.activo"/>
										</td>				
										<td class="labelTextValue">
<%
											if (!bEditable)
											{
												if (bean.getActivo().equals("S"))
												{
%>
												<siga:Idioma key="general.boton.yes"/>
<%
												}
												
												else
												{
%>
											<siga:Idioma key="general.boton.no"/>
<%
												}
											}
										  
											else
											{
%>
										  		<input type="checkbox" name="activo" <%if (bean.getActivo().equals("S")) {%>checked<%}%> value="S">
<%
											}
%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.certificados.literal.roles"/>
										</td>
										<td>
											<table cellspacing="0" cellpadding="0">
												<tr>
													<td class="labelTextValue">
														<%=bean.getGrupos()%>
													</td>
												</tr>
											</table>
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
			String botones = bEditable ? "R,Y,C" : "C";
%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="M"/>
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>