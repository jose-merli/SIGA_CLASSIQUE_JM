<!-- detalleRegistroCertificado.jsp -->
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
	Vector datosRol = (Vector)request.getAttribute("datosRol");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	request.removeAttribute("datos");
	request.removeAttribute("datosRol");
	request.removeAttribute("editable");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">

			//Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				listadoCertificadosForm.reset();
			}
			
			//Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{	
				listadoCertificadosForm.submit();
				
				window.top.returnValue="MODIFICADO";
			}
			
			//Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
		<div id="camposRegistro" class="posicionModalMedia" align="center">
			<html:form action="/ADM_GestionarCertificados.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "Modificar"/>
				<html:hidden property = "hiddenFrame" value = "1"/>

<%
			AdmCertificadosBean bean = (AdmCertificadosBean)datos.elementAt(0);
%>
				<table class="tablaCentralCamposMedia" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="administracion.certificados.titulo">
								<table class="tablaCampos" align="center">
									<tr>
										<td class="labelText">
											<siga:Idioma key="administracion.certificados.literal.nombre"/>
										</td>				
										<td class="labelTextvalue">
											<%=(String)datos.elementAt(1)%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="administracion.certificados.literal.NIF"/>
										</td>				
										<td class="labelTextvalue">
											<%=bean.getNIF()%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.certificados.literal.numeroSerie"/>
										</td>				
										<td class="labelTextvalue">
											<%=bean.getNumSerie()%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="administracion.certificados.literal.fechaCaducidad"/>
										</td>				
										<td class="labelTextvalue">
											<%=GstDate.getFormatedDateShort(userBean.getLanguage(), bean.getFechaCad())%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="administracion.certificados.literal.pendienteRevocar"/>
										</td>				
										<td class="labelTextvalue">
<%
											if (!bEditable)
											{
												if (bean.getRevocacion().equals("S"))
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
										  		<input type="checkbox" name="revocacion" <%if (bean.getRevocacion().equals("S")) {%>checked<%}%> value="S">
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
													<td class="labelTextvalue">
														<%=bean.getRoles()%>
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