<!DOCTYPE html>
<html>
<head>
<!-- configurarAbono.jsp -->

<!-- VERSIONES:
	  jose.barrientos - 26-02-2008 - Creacion
-->
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.facturacionSJCS.form.ConfiguracionAbonosForm"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.List"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	String idInstitucion="", idPagosJG="";
	
	idInstitucion = request.getParameter("idInstitucion");
	idPagosJG = request.getParameter("idPagosJG");

	String modo = (String)request.getAttribute("modo");
	String accion = (String)request.getAttribute("accion");
	String nombreInstitucion = (String)request.getAttribute("nombreInstitucion");
	String idEstadoPagosJG = request.getAttribute("idEstadoPagosJG")==null?"":(String)request.getAttribute("idEstadoPagosJG");
	
	String bdCuenta = (String)request.getAttribute("cuenta");
	boolean guardable = false;
	
	boolean combodeshabilitado =false;
	if (modo!=null && 
		modo.equalsIgnoreCase("edicion") &&
		!idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_EJECUTADO)) &&
		!idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_CERRADO))) {
		guardable = true;
		combodeshabilitado =false;
	}else{
		
		combodeshabilitado =true;

	}	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->

	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<html:javascript formName="configuracionAbonosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="factSJCS.pagos.confabonos" localizacion="factSJCS.Pagos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="factSJCS.datosPagos.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
			</td>
		</tr>
	</table>
	
	<html:form action="/FCS_ConfiguracionAbonos.do" method="POST" target="mainPestanas">
		<html:hidden name="configuracionAbonosForm" property="modo" value="<%=modo%>" />
		<html:hidden name="configuracionAbonosForm" property="idInstitucion" value="<%=idInstitucion%>" />
		<html:hidden name="configuracionAbonosForm" property="idPagosJG" value="<%=idPagosJG%>" />
		<html:hidden name="configuracionAbonosForm" property="idsufijo"/>
		<siga:ConjCampos leyenda="factSJCS.abonos.configuracion.literal.conceptoTitulo">
			<table class="tablaCampos" >
				<tr>
					<td class="labelText"  align="right">
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.proposito.SEPA"/>
					</td>
					<td>
						<bean:define id="listaPropositosSEPA" name="listaPropositosSEPA" scope="request"/>
						<html:select styleId="comboPropositosSEPA" property="idpropSEPA" value="${configuracionAbonosForm.idpropSEPA}" styleClass="boxCombo" style="width:200px;" disabled="<%=combodeshabilitado%>">
						<c:if test="${configuracionAbonosForm.idpropSEPA eq 0}">
							<html:option value=""><c:out value=""/></html:option>
						</c:if>
						<c:forEach items="${listaPropositosSEPA}" var="propSEPACmb">
							<html:option value="${propSEPACmb.idProposito}"><c:out value="${propSEPACmb.codigo.trim().length()>0?propSEPACmb.codigo:'	'} ${propSEPACmb.nombre}"/></html:option>
						</c:forEach>
						</html:select>	
					
					</td>
					<td class="labelText"  align="right">
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.proposito.otras"/>
					</td>
					<td>
						<bean:define id="listaPropositosOtros" name="listaPropositosOtros" scope="request"/>
						<html:select styleId="comboPropositosOtros" property="idpropOtros" value="${configuracionAbonosForm.idpropOtros}" styleClass="boxCombo" style="width:200px;" disabled="<%=combodeshabilitado%>">
						<c:if test="${configuracionAbonosForm.idpropOtros eq 0}">
							<html:option value=""><c:out value=""/></html:option>
						</c:if>
						<c:forEach items="${listaPropositosOtros}" var="propOtrosCmb">
							<html:option value="${propOtrosCmb.idProposito}"><c:out value="${propOtrosCmb.codigo.trim().length()>0?propOtrosCmb.codigo:'	'} ${propOtrosCmb.nombre}"/></html:option>
						</c:forEach>
						</html:select>	
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="factSJCS.abonos.configuracion.literal.cuentas"/>
				</td>
			</tr>
			<siga:Table 
				name="tablaResultados"
				border="1"
				columnNames="facturacion.devolucionManual.seleccion,
					facturacion.ficheroBancarioAbonos.literal.banco,
					facturacion.sufijos.literal.sufijo,
					facturacion.cuentasBancarias.IBAN"
				columnSizes="5,35,22,38"
				modal="g">
				   				   
<%
	    		if (request.getAttribute("bancosInstitucion") == null || ((Vector)request.getAttribute("bancosInstitucion")).size() < 1 ) {
%>
					<tr class="notFound">
			   			<td class="titulitos">
			   				<siga:Idioma key="messages.noRecordFound"/>
			   			</td>
					</tr>
<%
		    	} else { 

		    		Enumeration en = ((Vector)request.getAttribute("bancosInstitucion")).elements();
		    	
		    				
					int recordNumber=1;
					while (en.hasMoreElements()) {
	            		Row row = (Row) en.nextElement(); 
	            		
	            		String iban = UtilidadesString.mostrarIBANConAsteriscos(row.getString("IBAN"));
	            		if(modo!=null && modo.equalsIgnoreCase("consulta")){
	            		 	iban = UtilidadesString.mostrarIBANConAsteriscos(row.getString("IBAN"));
	            		}

	            		// Ademas comprobamos que sea la cuenta por defecto
	            		boolean bsel=false;
	            		
	            		//Si el colegio solo tiene una cuenta y un sufijo se mostrar� la cuenta marcada
	            		int numCuentas=((Vector)request.getAttribute("bancosInstitucion")).size();
	            		int numSuf=((List)request.getAttribute("listaSufijos")).size();
	            		if((numCuentas==1)&&(numSuf==1))
	            			bsel=true;
	            		
	            		
	            		if ((row.getString("BANCOS_CODIGO")!=null && row.getString("BANCOS_CODIGO").equalsIgnoreCase(bdCuenta))||(row.getString("SELECCIONADO").equalsIgnoreCase("1"))) {
	            			
	            			bsel=true;
	            		}           		
						
	            		String idComboSuf= "comboSufijos_" + row.getString("BANCOS_CODIGO");
	            		//Si el abono ya se configur� previamente se muestra el sufijo que tiene asignado, sino el que tenga la cuenta sjcs asignado
	            		String idsufijoBancoIni=row.getString("IDSUFIJOSJCS");
	            		String  idsufijodefBanco = "idsufijodefBanco_" + row.getString("BANCOS_CODIGO");
%>
							<siga:FilaConIconos 
								fila='<%=String.valueOf(recordNumber)%>' 
								botones=''
								modo='<%=accion%>' 	
								visibleConsulta='no' 	
								visibleEdicion='no'
								visibleBorrado='no' 	
								clase='listaNonEdit' 	
								pintarEspacio='no'>
								<td align="center">
<% 
									if (guardable) {
										
										if (bsel) { 
%>
										<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>" checked onclick="recargarCombo(this)">
										<c:set var="cuentaChecked" value="true"></c:set>
										
<%										}else{								
%>		
											<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>" onclick="recargarCombo(this)">
											<c:set var="cuentaChecked" value="false"></c:set>

	
<%										}									
 

									} else {
										if (bsel) { 
%>
											<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>" checked disabled>
											<c:set var="cuentaChecked" value="true"></c:set>
											
<% 
										} else { 
%>
											<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>" disabled>
											<c:set var="cuentaChecked" value="false"></c:set>
<% 
										}
									} 
%>
								</td>  	
 								<td><%=UtilidadesString.mostrarDatoJSP(row.getString("BANCO"))%></td>  	 
								<td align="center">
								<input type="hidden" id="<%=idsufijodefBanco%>" value="<%=idsufijoBancoIni%>"> <!-- para recargar el combo al seleccionar un banco -->
								<bean:define id="listaSufijos" name="listaSufijos" scope="request"/>	
								<c:set var="idsufijoBanco" value="<%=idsufijoBancoIni%>"></c:set> <!-- idsufijo informado en la cuenta bancaria sjcs -->
								<c:set var="valorSeleccionadoComboExisteAb" value="${configuracionAbonosForm.idsufijo!=null&&cuentaChecked?configuracionAbonosForm.idsufijo:''}"></c:set>
								<c:set var="valorSeleccionadoCombo" value="${configuracionAbonosForm.idsufijo==null&&cuentaChecked?configuracionAbonosForm.idsufijo:valorSeleccionadoComboExisteAb}"></c:set>
								<html:select styleId="<%=idComboSuf%>" name = "sufijoCmb" property="idSufijo" value="${valorSeleccionadoCombo}" styleClass="boxCombo" disabled="<%=combodeshabilitado%>" style="width:200px;">
 								<c:if test="${empty valorSeleccionadoCombo}"> 
 									<html:option value=""><c:out value=""/></html:option> 
 								</c:if> 
								<c:forEach items="${listaSufijos}" var="sufijoCmb">											
									<html:option value="${sufijoCmb.idSufijo}">										
										<c:if	test="${sufijoCmb.sufijo.trim().length()>0}">
											<c:out value="${sufijoCmb.sufijo} ${sufijoCmb.concepto}"/>
										</c:if>
										<c:if	test="${sufijoCmb.sufijo.trim().length()==0}">
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${sufijoCmb.concepto}"/>
										</c:if>
									</html:option>	
								</c:forEach>
								</html:select>		
										
								</td>  	
								<td align="left"><%=iban%></td>
							</siga:FilaConIconos>
<% 
						recordNumber++;
					} 
				} 
%>
			</siga:Table>	
		</table>
	</html:form>
	
	<div id="camposRegistro" class="posicionModalPeque" align="center">	
		<!-- FORMULARIO INICIAL -->
		<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="mainWorkArea">
			<html:hidden name="mantenimientoPagoForm" property="modo" value="abrir" />
			<html:hidden name="mantenimientoPagoForm" property="idInstitucion" value="<%=idInstitucion%>" />
			<html:hidden name="mantenimientoPagoForm" property="idPagosJG" value="<%=idPagosJG%>" />
		</html:form>
	</div>		

	<!-- FIN: LISTA DE VALORES -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

	function recargarCombo(valorRadio){	
		
		var chk = document.getElementsByName("cuenta");
		
		for (i = 0; i < chk.length; i++){
			if (chk[i].checked==1){
				jQuery("#comboSufijos_" + chk[i].value).val(jQuery("#idsufijodefBanco_" + chk[i].value).val());
			}else{
				jQuery("#comboSufijos_" + chk[i].value).val("");
			}	
		}		
	}
	
		//Asociada al boton Volver
		function accionVolver() {		
			var f = document.getElementById("mantenimientoPagoForm");
			f.target = "mainPestanas";
			f.submit();
		}
		
		// Asociada al boton GuardarCerrar
		function accionGuardar() {	
			sub();
			
			
			var chk = document.getElementsByName("cuenta");
			var cuentaSel="";
			for (i = 0; i < chk.length; i++){
				if (chk[i].checked==1){
					cuentaSel=chk[i].value;
					document.configuracionAbonosForm.cuenta.value=cuentaSel;
				}	
			}	
			
			if(cuentaSel.length>0){
				
				document.configuracionAbonosForm.target = "submitArea";
				document.configuracionAbonosForm.modo.value = "modificar";
			
				//Pasamos el sufijo de esa cuenta
				var idSufijoSel="";
				idSufijoSel=jQuery("#comboSufijos_" + cuentaSel).find("option:selected").val();

				if(idSufijoSel.length==0){
					fin();
					mensaje = "<siga:Idioma key='facturacion.sufijos.message.errorCuentaBancariaSufijoSJCS'/>";
					alert(mensaje);
					return false;	
				}else{
					document.configuracionAbonosForm.idsufijo.value=idSufijoSel;
					document.configuracionAbonosForm.submit();
				}
				
				
			} else {
				fin();
				alert("<siga:Idioma key='factSJCS.abonos.configuracion.literal.cuentaObligatoria'/>");
				return false;	
			}
		}

		function refrescarLocal() {
			buscar();
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<%
	String bot = "V";
	if (guardable) { 
		bot = "V,G"; 
	}
%>
	
	<siga:ConjBotonesAccion botones="<%=bot%>" clase="botonesDetalle"/>
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>