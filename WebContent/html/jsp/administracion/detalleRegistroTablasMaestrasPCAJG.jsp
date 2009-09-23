<!-- detalleRegistroTablasMaestrasPCAJG.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

	Row datos = (Row)request.getAttribute("datos");
	RowsContainer selectAsociacionSIGA = (RowsContainer)request.getAttribute("selectAsociacionSIGA");
	
	String sBloqueo = (String)request.getAttribute("bloqueo");
	String campoFKSIGA = (String)request.getAttribute("campoFKSIGA");
	String tablaRelacion = (String)request.getAttribute("tablaRelacion");
	
	String identificador ="";
	String codigo ="";
	String descripcion ="";
	List campoFKSIGAvalue = (List)request.getAttribute("CAMPOFKSIGAVALUE");
	if (campoFKSIGAvalue == null) {
		campoFKSIGAvalue = new ArrayList();
	}
	String abreviatura = "";
	
	if (datos!=null) {
		identificador = (String)datos.getString("IDENTIFICADOR");
		codigo=(String)datos.getString("CODIGO");
		descripcion=(String)datos.getString("DESCRIPCION");
		abreviatura=(String)datos.getString("ABREVIATURA");
		abreviatura = abreviatura==null?"":abreviatura;				
	}

	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	boolean bNuevo = ((String)request.getAttribute("nuevo")).equals("1");
	String sNombreTabla = (String)request.getAttribute("nombreTabla");
	
	String sAliasTabla = (String)request.getAttribute("aliasTabla");
		
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
		
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() {		
				listadoTablasMaestrasPCAJGForm.reset();
			}
		
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() {
				sub();
				
				if (!trim(listadoTablasMaestrasPCAJGForm.codigo.value)) {
					alert('<siga:Idioma key="messages.consultas.error.codigo"/>');
					fin();
					return false;
				}
				
				if (!trim(listadoTablasMaestrasPCAJGForm.descripcion.value)) {
					alert('<siga:Idioma key="messages.consultas.error.descripcion"/>');
					fin();
					return false;
				}	
				
				//alert("valida codikgo");
				listadoTablasMaestrasPCAJGForm.submit();						
				window.returnValue="MODIFICADO";
				
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() {		
				window.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		
		
	</head>

	<body>
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="administracion.catalogos.titulo"/>
			</td>
		</tr>
	</table>	
		<div id="camposRegistro" class="posicionModalPeque" align="center">
			<html:form action="/PCAJGGestionarTablasMaestras.do" method="POST" target="submitArea">
<%
				String miModo = ((bNuevo) ? "Insertar" : "Modificar");
				
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<input type="hidden" name="identificador" value="<%=identificador%>">
				<input type="hidden" name="nombreTablaMaestra" value="<%=sNombreTabla%>">
				<input type="hidden" name="campoFKSIGA" value="<%=campoFKSIGA!=null?campoFKSIGA:""%>">
				<input type="hidden" name="tablaRelacion" value="<%=tablaRelacion!=null?tablaRelacion:""%>">
				
				<input type="hidden" name="aliasTabla" value="<%=sAliasTabla%>">
				
				<table class="tablaCentralCamposPeque" align="center" border="0">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="administracion.catalogos.titulo">
								<table class="tablaCampos" align="center">
									<tr>
										<td class="labelText">
											<siga:Idioma key="general.literal.tabla"/>
										</td>
										<td class="labelTextValue">
											<%=sAliasTabla%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="general.code"/>&nbsp;(*)
										</td>
										
										<td class="labelTextValue">
<%
										if (bEditable) {%>
											<html:text property="codigo" styleClass="box" value="<%=codigo%>" size="10" maxlength="12"/><%
										} else {%>
											<%=codigo%><%
										} 
%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="general.description"/>&nbsp;(*)
										</td>	
<%
										if (bEditable) {%>
										
										<td class="labelTextValue">
											<input type="text" name="descripcion" class="box" value="<%=descripcion%>" styleClass="boxCombo" size="35" maxlength="200">
										</td>
<%
										} else {%>
										<td class="labelTextValue"">
											<%=descripcion%>
											<html:hidden property="descripcion" value="<%=descripcion%>"/>
										</td>
										<%}%>
                                    
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="general.abreviatura"/>
										</td>	
<%
										if (bEditable) {%>
										
										<td class="labelTextValue">
											<input type="text" name="abreviatura" class="box" value="<%=abreviatura%>" styleClass="boxCombo" size="35" maxlength="10">
										</td>
<%
										} else {%>
										<td class="labelTextValue"">
											<%=abreviatura%>
											<html:hidden property="abreviatura" value="<%=abreviatura%>"/>
										</td>
										<%}%>
                                    
									</tr>
									<%if (selectAsociacionSIGA != null) {%>
										<tr>
											<td class="labelText">
												<siga:Idioma key="general.descripcionSIGA"/>
											</td>
											<td class="labelTextValue">
											<%if (bEditable) {%>
											<select name="campoFKSIGAvalue" style="width:250px" multiple size="6">
											<option value="">--Seleccionar</option>
											<%}%>
											
											<%for (int i = 0; i < selectAsociacionSIGA.size();i++ ) {
												Row row = (Row)selectAsociacionSIGA.get(i);
												if (bEditable) {%>
													<option value="<%=row.getString("ID")%>" <%=(campoFKSIGAvalue.contains(row.getString("ID"))?"selected":"")%>><%=row.getString("DESCRIPCION")%> (<%=row.getString("ID")%>)</option>
												<%} else if (campoFKSIGAvalue.contains(row.getString("ID"))) {%>												
												<input type="text" readonly class="boxConsulta" value="<%=row.getString("DESCRIPCION")%> (<%=row.getString("ID")%>)"/>
												<br>											
												<%
												}%>
											<%}%>
											
											<%if (bEditable) {%>
											</select>
											<%}%>			
											</td>
											<%}%>
										</tr>
									
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
					<%if (sBloqueo!=null && sBloqueo.equals("S") ){%>
					
					<tr>
									<td class="labelText" colspan="6"  >
									    <siga:Idioma key="gratuita.maestro.campoBloqueo.nota"/>
									  </td>
									</tr>
					<%}%>				
				</table>
			</html:form>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
<%
			String botones = bEditable ? "R,Y,C" : "C";
%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="P"/>
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>