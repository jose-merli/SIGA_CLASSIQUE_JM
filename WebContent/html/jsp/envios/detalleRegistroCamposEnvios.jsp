<!-- detalleRegistroCamposEnvios.jsp -->
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
	boolean bNuevo = ((String)request.getAttribute("nuevo")).equals("1");

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
	String sIdTipoEnvio=(String)htDatos.get("idTipoEnvio");
	String sIdPlantillaEnvios=(String)htDatos.get("idPlantillaEnvios");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				if(PlantillasEnviosCamposForm.cmbCampo.value=="")
				{
					var mensaje = "<siga:Idioma key='certificados.mantenimiento.literal.campo'/> <siga:Idioma key='messages.campoObligatorio.error'/>";
	
					alert (mensaje);
					fin();
					return false;
				}

				var oCampo = document.getElementById("cmbCampo");
				
				if (<%=bNuevo%>)
				{
					PlantillasEnviosCamposForm.idCampo.value=oCampo.value;
				}

				var oFormato = document.getElementById("cmbFormato");
				PlantillasEnviosCamposForm.idFormato.value=oFormato.value;
				PlantillasEnviosCamposForm.tipoCampo.value=oCampo.value;

				PlantillasEnviosCamposForm.submit();
	
				//window.top.returnValue="MODIFICADO";
			}
			
			//<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.parent.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		
		<script>
			function comprobarCapturarDatos()
			{
				var miSplitVar = PlantillasEnviosCamposForm.cmbCampo.value.split(",");
				miSplitVar = miSplitVar[2];
				
				if (miSplitVar=='S' || "<%=sCapturarDatos%>"=='S')
				{
					PlantillasEnviosCamposForm.valor.readOnly=false;
					jQuery("#valor").removeAttr("disabled");
					PlantillasEnviosCamposForm.valor.cols="125";
					PlantillasEnviosCamposForm.valor.rows="12";
					PlantillasEnviosCamposForm.valor.className="box";
				}
				
				else
				{
					//alert('<siga:Idioma key="certificados.mantenimiento.literal.aviso"/>');
					PlantillasEnviosCamposForm.valor.value="";
					jQuery("#valor").attr("disabled","disabled");
					PlantillasEnviosCamposForm.valor.readOnly=true;
					PlantillasEnviosCamposForm.valor.cols="1";
					PlantillasEnviosCamposForm.valor.rows="11";
					PlantillasEnviosCamposForm.valor.className="boxConsulta";


				}	
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
		
			<html:form action="/ENV_Campos_Plantillas.do" method="POST" target="submitArea">
<%
				String miModo = bNuevo ? "Insertar" : "Modificar";
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				
				<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
				<html:hidden property="idTipoEnvio" value="<%=sIdTipoEnvio%>"/>
				<html:hidden property="idPlantillaEnvios" value="<%=sIdPlantillaEnvios%>"/>
				
				<html:hidden property="idCampo" value="<%=sIdCampo%>"/>
				<html:hidden property="idFormato" value="<%=sIdFormato%>"/>
				<html:hidden property="tipoCampo" value="<%=sTipoCampo%>"/>

				<table class="tablaCentralCamposMedia" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="certificados.mantenimiento.literal.campo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td  width ="20%" class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.campo"/>&nbsp;(*)
										</td>
										<td  width ="80%" class="boxConsulta">
<%
										String[] parametros = new String[3];
										parametros[0] = sIdInstitucion;
										parametros[1] = sIdTipoEnvio;
										parametros[2] = sIdPlantillaEnvios;

										if (bNuevo)
										{
%>
											<siga:ComboBD nombre="cmbCampo" tipo="envios.plantillas.campos.campo" parametro="<%=parametros%>" ancho="300" clase="boxCombo" accion="Hijo:cmbFormato; comprobarCapturarDatos();"/>
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
										<td  class="boxConsulta">
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
%>
											<%=sDescFormato%>
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
											<html:textarea property="valor" styleId="valor" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" value="" style="width=600px" styleClass="boxCombo" cols="125" rows="12" value="<%=sValor%>" readonly="<%=bReadOnly%>"/>
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
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="M"/>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>