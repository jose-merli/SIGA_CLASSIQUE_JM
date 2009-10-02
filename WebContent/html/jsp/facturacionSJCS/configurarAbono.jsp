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
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	String idInstitucion="", idPagosJG="";
	
	idInstitucion = request.getParameter("idInstitucion");
	idPagosJG = request.getParameter("idPagosJG");

	String modo = (String)request.getAttribute("modo");
	String accion = (String)request.getAttribute("accion");
	String nombreInstitucion = (String)request.getAttribute("nombreInstitucion");
	String idEstadoPagosJG = request.getAttribute("idEstadoPagosJG")==null?"":(String)request.getAttribute("idEstadoPagosJG");
	
	
	String bdCuenta = (String)request.getAttribute("cuenta");
	String bdConcepto = (String)request.getAttribute("concepto");
	boolean guardable = false;
	
	if ((bdCuenta==null) ||(bdCuenta.equalsIgnoreCase(""))){
		bdCuenta = (String)request.getAttribute("paramIdCuenta");
	}
	if ((bdConcepto==null)||(bdConcepto.equalsIgnoreCase(""))){
		bdConcepto = (String)request.getAttribute("paramConcepto");
	}
	
	if (
			((modo!=null)&&( modo.equalsIgnoreCase("edicion")))
		&&
			((!idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_EJECUTADO)))&&
			 (!idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_CERRADO))))
			) 
	{
		guardable = true;
	}
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<html:javascript formName="configuracionAbonosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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
	

	<siga:ConjCampos leyenda="factSJCS.abonos.configuracion.literal.conceptoTitulo">
		<table class="tablaCampos" >
			<tr>
				<td class="labelText"  align="right" width="200px">
					<siga:Idioma key="factSJCS.abonos.configuracion.literal.concepto"/>
					<% if (guardable) { %>
						<html:text name="configuracionAbonosForm" property="concepto"  size="2"  maxlength= "1" styleClass="box" value="<%= bdConcepto %>" readonly="false"/>
					<%} else {%>
						<html:text name="configuracionAbonosForm" property="concepto" size="2"  styleClass="boxConsulta" value="<%= bdConcepto %>" readonly="true"/>
					<%}%>
				</td>
				<td colspan="1">
					<table>
					<tr align="right">
					<td class="labelText">
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.concepto1"/>
					</td>
					</tr>
					<tr>
					<td class="labelText">
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.concepto9"/>
					</td>
					</tr>
					</table>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.abonos.configuracion.literal.cuentas"/>
		</td>
	</tr>
	<siga:TablaCabecerasFijas 
				   nombre="tablaResultados"
				   borde="1"
				   clase="tableTitle"				   
				   nombreCol="facturacion.devolucionManual.seleccion,facturacion.ficheroBancarioAbonos.literal.banco,censo.consultaDatosBancarios.literal.cuentaBancaria"
				   tamanoCol="10,50,40"
				   alto="100%"
				   modal="g">
				   				   
				<%
	    		if (request.getAttribute("bancosInstitucion") == null || ((Vector)request.getAttribute("bancosInstitucion")).size() < 1 )
		    	{
				%>
					<br><br>
					<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
					<br><br>
				<%
		    	}	    
			    else
		    	{ 
		    		Enumeration en = ((Vector)request.getAttribute("bancosInstitucion")).elements();					
					int recordNumber=1;
					while (en.hasMoreElements())
					{
	            		Row row = (Row) en.nextElement(); 
	            		// Comprobamos que se trate de una cuenta para SJCS
	            		String sjcs = row.getString("SJCS");
	            		boolean bsjcs=false;
	            		if (sjcs!=null && !sjcs.equals("0")) {
	            			bsjcs=true;
	            		}
	            		
	            		// Ademas comprobamos que sea la cuenta por defecto
	            		boolean bsel=false;
	            		if (row.getString("BANCOS_CODIGO")!=null && row.getString("BANCOS_CODIGO").equalsIgnoreCase(bdCuenta)) {
	            			bsel=true;
	            		}           		
					%>
	            		<% if (bsjcs){ %>
							<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones=''
								  modo='<%=accion%>' 	visibleConsulta='no' 	visibleEdicion='no'
								  visibleBorrado='no' 	clase="listaNonEdit" 	pintarespacio='no'>
								<td>
									<% if (guardable) { %>
										<% if (bsel){ %>
											<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>" checked>
										<% }else{ %>
											<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>">
										<% } %>
									<% }else{ %>
										<% if (bsel){ %>
											<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>" checked disabled>
										<% }else{ %>
											<input type="radio" name="cuenta" value="<%=row.getString("BANCOS_CODIGO")%>" disabled>
										<% } %>
									<% } %>
								</td>  	
								<td>
									<%=UtilidadesString.mostrarDatoJSP(row.getString("BANCO"))%>							
								</td>  	
								<td align="right">
									<%=row.getString("CUENTACONTABLE")%>							
								</td>
						</siga:FilaConIconos>
						<% } %>
					<% 
					recordNumber++;
					} 
				} %>
			</siga:TablaCabecerasFijas>	
	</table>

	</html:form>
	<div id="camposRegistro" class="posicionModalPeque" align="center">
	
	<!-- FORMULARIO INICIAL -->
	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="mainWorkArea">
			<html:hidden name="mantenimientoPagoForm" property="modo" value="abrir" />
			<html:hidden name="mantenimientoPagoForm" property="idInstitucion" value="<%=idInstitucion%>" />
			<html:hidden name="mantenimientoPagoForm" property="idPagosJG" value="<%=idPagosJG%>" />
	</html:form>

	<!-- FIN: LISTA DE VALORES -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		//Asociada al boton Volver
		function accionVolver() 
		{		
			var f = document.getElementById("mantenimientoPagoForm");
			f.target = "mainPestanas";
			f.submit();
		}
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardar() 
		{	
			sub();
			if(document.configuracionAbonosForm.cuenta!=null){
				var conceptoAb=document.configuracionAbonosForm.concepto.value;
				if ((conceptoAb=='1')||(conceptoAb=='8')||(conceptoAb=='9')){
					if (validateConfiguracionAbonosForm(document.configuracionAbonosForm)){
						document.configuracionAbonosForm.target = "submitArea";
						document.configuracionAbonosForm.modo.value = "modificar";
						document.configuracionAbonosForm.submit();
					}else{
						fin();
						return false;
					}
				}else{
					fin();
					alert("<siga:Idioma key="administracion.parametrosGenerales.error.conceptoAbonoPago"/>");
				}
			}else{
				fin();
				alert("<siga:Idioma key="factSJCS.abonos.configuracion.literal.cuentaObligatoria"/>");
			}
			
		}

		function refrescarLocal()
		{
			buscar();
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
</div>


	<%
	String bot = "V";
	if (guardable) { 
		bot = "V,G"; 
	}
	%>
	
<siga:ConjBotonesAccion botones="<%=bot%>" clase="botonesDetalle"/>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
