<!-- detalleCampo.jsp -->
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
	//Vector datos = (Vector)request.getAttribute("datos");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	EnvCamposEnviosBean beanDatos = (EnvCamposEnviosBean)request.getAttribute("bean");
	
	String sIdCampo=""+beanDatos.getIdCampo();
	String sDescCampo=(String)request.getAttribute("descCampo");
	String sIdFormato=""+beanDatos.getIdFormato();
	String sDescFormato=(String)request.getAttribute("descFormato");

	String sTipoCampo=beanDatos.getTipoCampo();
	String sValor=beanDatos.getValor();
	boolean bCapturar = ((String)request.getAttribute("sCapturar")).equalsIgnoreCase("S");
	
	String sIdInstitucion=(String)request.getAttribute("idInstitucion");
	String sIdEnvio=(String)request.getAttribute("idEnvio");
	
	/*String sIdInstitucion=(String)htDatos.get("idInstitucion");
	String sIdTipoProducto=(String)htDatos.get("idTipoProducto");
	String sIdProducto=(String)htDatos.get("idProducto");
	String sIdProductoInstitucion=(String)htDatos.get("idProductoInstitucion");*/
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				/*var oCampo = document.getElementById("cmbCampo");
				
				if (bNuevo en JSP)
				{
					EnviosDatosGeneralesForm.idCampoCertificado.value=oCampo.value;
				}

				var oFormato = document.getElementById("cmbFormato");
				EnviosDatosGeneralesForm.idFormato.value=oFormato.value;
				EnviosDatosGeneralesForm.tipoCampo.value=oCampo.value;
				*/
				sub();
				var oFormato = document.getElementById("cmbFormato");
				EnviosDatosGeneralesForm.idFormato.value=oFormato.value;
				
				EnviosDatosGeneralesForm.submit();
	
				window.returnValue="MODIFICADO";
			}
			
			//Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		
		<script>
			function comprobarCapturarDatos()
			{
				/*var miSplitVar = MantenimientoCertificadosCamposForm.cmbCampo.value.split(",");
				miSplitVar = miSplitVar[2];
				
				if (miSplitVar=='S' || "sCapturarDatos en JSP"=='S')
				{
					MantenimientoCertificadosCamposForm.valor.readOnly=false;
				}
				
				else
				{
					MantenimientoCertificadosCamposForm.valor.value="";
					MantenimientoCertificadosCamposForm.valor.disabled=true;
				}*/	
			}
		</script>
	</head>

	<body>
	
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="certificados.mantenimiento.literal.campo"/>
			</td>
		</tr>
	</table>	
			

			<html:form action="/ENV_Datos_Generales.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "Modificar"/>
				<html:hidden property = "hiddenFrame" value = "1"/>

				<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
				<html:hidden property="idEnvio" value="<%=sIdEnvio%>"/>
				
				<html:hidden property="idCampo" value="<%=sIdCampo%>"/>
				<html:hidden property="idFormato" value="<%=sIdFormato%>"/>
				<html:hidden property="tipoCampo" value="<%=sTipoCampo%>"/>

				<table class="tablaCentralCamposPeque" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="certificados.mantenimiento.literal.campo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.campo"/>
										</td>
										<td class="boxConsulta">
											<%=sDescCampo%>
											<input type="hidden" id="cmbCampo" value="<%=sIdCampo%>">
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.formato"/>
										</td>				
										<td class="boxConsulta">
<%
										if (bEditable)
										{
											String[] parametros = new String[1];
											parametros[0] = sTipoCampo;
											
											ArrayList al = new ArrayList();
											al.add(sIdFormato + "," + sTipoCampo);
%>
											<siga:ComboBD nombre="cmbFormato" tipo="certificados.mantenimiento.campos.formato" parametro="<%=parametros%>" ancho="300" elementoSel="<%=al%>" clase="boxCombo" accion="" hijo="true"/>
<%
											}
											
										else
										{
%>
											<%=sDescFormato%>
<%
										}
%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.valor"/>
										</td>				
										<td>
<%
										boolean bReadOnly=bEditable && bCapturar ? false : true;
%>
											<html:textarea property="valor" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" value="" styleClass="boxCombo" cols="80" rows="6"  value="<%=sValor%>" readonly="<%=bReadOnly%>"/>
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
			String botones = bEditable ? "Y,C" : "C";
%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="P"/>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>