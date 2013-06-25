<!-- pestanaConfigurables.jsp -->
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
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.expedientes.form.PestanaConfigurableForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.GenTipoCampoBean"%>


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	PestanaConfigurableForm form = (PestanaConfigurableForm) request.getAttribute("PestanaConfigurableForm");
	boolean bLectura = false;
	String estiloCaja = "box";
	String nombrePestana = form.getNombrePestana();
	String accion = form.getAccion();
	String disabled = "false";
	if (accion.equals("consulta")){	//pestanhas:edicion o consulta
		bLectura=true;
		estiloCaja = "boxConsulta";	
		disabled = "true";
	}

	Vector vDatos= (Vector) request.getAttribute("datos");	
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");	
	String action = "/EXP_PestanaConfigurable" + form.getIdPestanaConf() + ".do";
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton Restablecer -->
			function refrescarLocal() {		
				document.location=document.location;
			}
		
			function accionRestablecer(){		
				document.PestanaConfigurableForm.reset();
			}
		
			//Asociada al boton GuardarCerrar -->
			function accionGuardar() {
			    sub();
			    document.PestanaConfigurableForm.modo.value = "modificar";
				document.PestanaConfigurableForm.submit();
			}
			
			//Asociada al boton Volver -->
			function accionVolver() {		
				<% if (busquedaVolver.equals("AB")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% } else if (busquedaVolver.equals("NB")){ %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<% } else if(busquedaVolver.equals("AV")) { %>
					document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
					document.forms[1].modo.value="buscarPor";
					document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
				<% }  else if (busquedaVolver.equals("Al")){%>
					document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
					document.forms[1].modo.value="abrir";
				<% } %>
					document.forms[1].submit();				
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo titulo="<%=nombrePestana %>" localizacion="expedientes.auditoria.localizacion"/>
	</head>
	<body>
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="<%=form.getTituloVentana()%>"/>
				</td>
			</tr>
		</table>	
		<div id="camposRegistro" class="posicionModalPeque" align="center">
			<table class="tablaCentralCamposPeque" align="center" border="0">
				<html:form action="<%=action %>" method="POST" target="submitArea">
					<input type="hidden" name="tablaDatosDinamicosD" value="">
					<html:hidden name="PestanaConfigurableForm" property = "idInstitucion" />
					<html:hidden name="PestanaConfigurableForm" property = "idTipoExpediente" />
					<html:hidden name="PestanaConfigurableForm" property = "idPestanaConf" />
					<html:hidden name="PestanaConfigurableForm" property = "idCampo" />
					<html:hidden name="PestanaConfigurableForm" property = "modo" />
					<html:hidden name="PestanaConfigurableForm" property = "nombrePestana" />
					<html:hidden name="PestanaConfigurableForm" property = "numeroExpediente" />
					<html:hidden name="PestanaConfigurableForm" property = "anioExpediente" />
					<tr>		
						<td>
							<siga:ConjCampos>
								<table class="tablaCampos" align="center">
									<%
										if (vDatos==null || vDatos.size()==0){
											%>
												<tr>
													<td class="labelTextValue">
			   											<p class="titulitos" style="text-align:center">
			   												<siga:Idioma key="messages.noRecordFound"/>
			   											</p>
													</td>
												</tr>
											<%
										} else {
			 								for (int i=0; i<vDatos.size(); i++){
				  								Hashtable registro = (Hashtable)vDatos.elementAt(i);
				  								String tipo = (String)registro.get("TIPO");
				  								String maxLong = (String)registro.get("MAXLONG");
				  								String valor = (String)registro.get("VALOR");
				  								String nombreCampo_1="oculto"+(i+1)+"_1";
				  								String nombreCampo_2="oculto"+(i+1)+"_2";
												%>
													<tr>
														<input type="hidden" name="<%=nombreCampo_1%>" value="<%=(String)registro.get("IDCAMPOCONF")%>">	
														<td class="labelText">
															<%=UtilidadesString.mostrarDatoJSP((String)registro.get("ETIQUETA")) %>
														</td>
													</tr>
													<tr>
														<td>
														<%if (GenTipoCampoBean.ID_TIPO_ALFANUMERICO.toString().equals(tipo)){ %>
															<%if (Integer.valueOf(maxLong) > 100){ %>
																<textarea name="<%=nombreCampo_2%>" onKeyDown="cuenta(this,<%=maxLong%>)" onChange="cuenta(this,<%=maxLong%>)" class="<%=estiloCaja %>" style="width:700px;" cols="90" rows="6"><%=valor%></textarea>
															<%} else { %>
																<input type="text" name="<%=nombreCampo_2%>" value="<%=valor%>"  size="<%=maxLong%>" class ="<%=estiloCaja %>" maxlength="<%=maxLong%>"></input>
															<%} %>															
														<%} else if (GenTipoCampoBean.ID_TIPO_NUMERICO.toString().equals(tipo)){ %>
															<input type="text" name="<%=nombreCampo_2%>" value="<%=valor%>"  size="<%=maxLong%>" class ="<%=estiloCaja %>" maxlength="<%=maxLong%>" onkeypress="return soloDigitos(event)"></input>
														<%} else if (GenTipoCampoBean.ID_TIPO_FECHA.toString().equals(tipo)){ %>
															<% if (valor != null && !"".equals(valor)){ %>
																<siga:Fecha nombreCampo="<%=nombreCampo_2%>" valorInicial="<%=valor%>" disabled="<%=disabled%>" />
															<%} else { %>
																<siga:Fecha nombreCampo="<%=nombreCampo_2%>" disabled="<%=disabled%>" />
															<%} %>
														<%} %>
														</td>
													</tr>
												<%
											} //for
										} // if vdatos
									%>			
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>
			</table>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
			<% if (bLectura) { %>
				<siga:ConjBotonesAccion botones="V" modal="P"/>
			<% } else { %>
				<siga:ConjBotonesAccion botones="V,G,R" modal="P"/>
			<% } %>
		</div>	
		
		<% if (!busquedaVolver.equals("volverNo")) { %>
			<html:form action="/EXP_AuditoriaExpedientes.do" method="POST" target="mainWorkArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "avanzada" value = ""/>		
			</html:form>
		<% } %>	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>