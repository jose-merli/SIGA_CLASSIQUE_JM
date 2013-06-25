<!-- detalleRegistroCamposCertificado.jsp -->
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
	
	Vector datos = (Vector)request.getAttribute("datos");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	boolean bNuevo = ((String)request.getAttribute("nuevo")).equals("1");
	
	request.removeAttribute("datos");
	request.removeAttribute("editable");
	request.removeAttribute("nuevo");
   
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Hashtable htDatos = (Hashtable)datos.elementAt(0);
	
	String sIdCampo=(String)htDatos.get("idCampo");
	String sDescCampo=(String)htDatos.get("descCampo");
	String sIdFormato=(String)htDatos.get("idFormato");
	String sDescFormato=(String)htDatos.get("descFormato");

	String sTipoCampo=(String)htDatos.get("tipoCampo");
	String sCapturarDatos=(String)htDatos.get("capturarDatos");
	String sValor=(String)htDatos.get("valor");
	
	String sIdInstitucion=(String)htDatos.get("idInstitucion");
	String sIdTipoProducto=(String)htDatos.get("idTipoProducto");
	String sIdProducto=(String)htDatos.get("idProducto");
	String sIdProductoInstitucion=(String)htDatos.get("idProductoInstitucion");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				var oCampo = document.getElementById("cmbCampo");
				
				if (<%=bNuevo%>)
				{
					MantenimientoCertificadosCamposForm.idCampoCertificado.value=oCampo.value;
				}

				var oFormato = document.getElementById("cmbFormato");
				MantenimientoCertificadosCamposForm.idFormato.value=oFormato.value;
				MantenimientoCertificadosCamposForm.tipoCampo.value=oCampo.value;

				MantenimientoCertificadosCamposForm.submit();
	
				window.top.returnValue="MODIFICADO";
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		
		<script>
			function comprobarCapturarDatos()
			{
				var miSplitVar = MantenimientoCertificadosCamposForm.cmbCampo.value.split(",");
				miSplitVar = miSplitVar[2];
				
				if (miSplitVar=='S' || "<%=sCapturarDatos%>"=='S')
				{
					MantenimientoCertificadosCamposForm.valor.readOnly=false;
					MantenimientoCertificadosCamposForm.valor.className="box";
					MantenimientoCertificadosCamposForm.valor.rows="12";
					MantenimientoCertificadosCamposForm.valor.cols="125";
				}
				
				else
				{
					MantenimientoCertificadosCamposForm.valor.value="";
				   	jQuery("#valor").attr("disabled","disabled");
					MantenimientoCertificadosCamposForm.valor.className="boxConsulta";
					MantenimientoCertificadosCamposForm.valor.rows="11";
					MantenimientoCertificadosCamposForm.valor.cols="100";
				}	
			}
		</script>
	</head>

	<body>
	
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="certificados.campos.editar.literal"/>
				</td>
			</tr>
		</table>
				
		<div id="camposRegistro" class="posicionModalMedia" align="center">

			<table class="tablaCentralCamposMedia" align="center">

			<html:form action="/CER_Campos.do" method="POST" target="submitArea">
<%
				String miModo = bNuevo ? "Insertar" : "Modificar";
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				
				<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
				<html:hidden property="idTipoProducto" value="<%=sIdTipoProducto%>"/>
				<html:hidden property="idProducto" value="<%=sIdProducto%>"/>
				<html:hidden property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
				
				<html:hidden property="idCampoCertificado" value="<%=sIdCampo%>"/>
				<html:hidden property="idFormato" value="<%=sIdFormato%>"/>
				<html:hidden property="tipoCampo" value="<%=sTipoCampo%>"/>

					<tr>		
						<td>
							<siga:ConjCampos leyenda="certificados.mantenimiento.titulo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td width="10%" class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.campo"/>&nbsp(*)
										</td>
										<td width="85%" class="boxConsulta">
<%
										String[] parametros = new String[4];
										parametros[0] = sIdInstitucion;
										parametros[1] = sIdTipoProducto;
										parametros[2] = sIdProducto;
										parametros[3] = sIdProductoInstitucion;

										if (bNuevo)
										{
%>
											<siga:ComboBD nombre="cmbCampo" tipo="certificados.mantenimiento.campos.campo" parametro="<%=parametros%>" ancho="300" clase="boxCombo" accion="Hijo:cmbFormato; comprobarCapturarDatos();"/>
<%
										}
										
										else
										{
%>
											<%=sDescCampo%>
											<input type="hidden" id="cmbCampo" ancho="300" value="<%=sIdCampo%>">
<%
										}
%>
										</td>
									</tr>
									<tr>				
										<td class="labelText">										
											<siga:Idioma key="certificados.mantenimiento.literal.formato"/>
										</td>				
										<td class="boxConsulta">
<%
										if (bNuevo)
										{
%>
											<siga:ComboBD nombre="cmbFormato" tipo="certificados.mantenimiento.campos.formato" clase="boxCombo" ancho="300" accion="" hijo="true"/>
<%
										}
										
										else
										{
											if (bEditable)
											{
												parametros = new String[1];
												parametros[0] = sTipoCampo;
												
												ArrayList al = new ArrayList();
												al.add(sIdFormato + "," + sTipoCampo);
%>
											<siga:ComboBD nombre="cmbFormato" tipo="certificados.mantenimiento.campos.formato" parametro="<%=parametros%>" ancho="300" elementoSel="<%=al%>" clase="boxCombo" accion="" hijo="true"/>
<%
											}
											
											else
											{
%>											<%=sDescFormato%>
											
<%										
											}
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
											boolean bReadOnly=sCapturarDatos.equals("S") ? bEditable ? false : true : true;
%>
											<html:textarea  styleId="valor" property="valor" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" value="" styleClass="boxCombo" cols="125" rows="12" value="<%=sValor%>" readonly="<%=bReadOnly%>"/>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
			</html:form>
				</table>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
<%
			String botones = bEditable ? "Y,C" : "C";
%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="M"/>
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>