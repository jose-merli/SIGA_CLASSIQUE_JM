<!-- datosCV.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenDatosCVBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String idUsr = (String)usr.getUserName();
	boolean bOcultarHistorico = usr.getOcultarHistorico();
	
	
	
	

	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	boolean editarCampos = false;
	boolean desactivado = false;
	String  clase = "box";
  String  botones = "C,Y,R";
  
	String DB_TRUE  = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;
	
	String nombreUsu =(String)request.getAttribute("nombrePersona");
	String idPersona = String.valueOf((Long)request.getAttribute("idPersona"));
	String numero = (String)request.getAttribute("numero");
	
	Hashtable htData=null;
	String fechaInicio = "";
	String fechaFin = "";
	String fechaCertificado = "";
	String creditos = "";
	String tipoApunte = "";
	String descTipo1 = "";
	String descTipo2 = "";
	String descripcion = "";
	String certificado = "";
	String idCV = "";
	String idInstitucion = "";
	String fechaBaja = "";
	ArrayList idTipoCV = new ArrayList();
	ArrayList idSubtipo1 = new ArrayList();
	ArrayList idSubtipo2 = new ArrayList();
	
	String modo = (String)request.getAttribute("modoConsulta");	
	if (modo.equals("ver") || modo.equals("editar")) {
			htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			if (htData != null) {
				fechaInicio = GstDate.getFormatedDateShort("",(String)htData.get(CenDatosCVBean.C_FECHAINICIO));
				fechaFin = GstDate.getFormatedDateShort("",(String)htData.get(CenDatosCVBean.C_FECHAFIN));
				fechaCertificado = GstDate.getFormatedDateShort("",(String)htData.get(CenDatosCVBean.C_FECHAMOVIMIENTO));
				creditos = String.valueOf(htData.get(CenDatosCVBean.C_CREDITOS));
				descripcion = String.valueOf(htData.get(CenDatosCVBean.C_DESCRIPCION));
				certificado = String.valueOf(htData.get(CenDatosCVBean.C_CERTIFICADO));
				tipoApunte = String.valueOf(htData.get("TIPOAPUNTE"));
				descTipo1 = String.valueOf(htData.get("DESCSUBTIPO1"));
				descTipo2 = String.valueOf(htData.get("DESCSUBTIPO2"));
				
				fechaBaja = String.valueOf(htData.get(CenDatosCVBean.C_FECHABAJA));
				if ((fechaBaja != null) && !fechaBaja.equals(""))
					fechaBaja = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fechaBaja));
				else 
					fechaBaja = "";
	
				if (modo.equals("editar")) {
					editarCampos = true;	
					desactivado = false;
					idCV 					= String.valueOf(htData.get(CenDatosCVBean.C_IDCV));
					idInstitucion = String.valueOf(htData.get(CenDatosCVBean.C_IDINSTITUCION));
					idTipoCV.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCV)));
					idSubtipo1.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO1)+"@"+htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT1)));
					idSubtipo2.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO2)+"@"+htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT2)));
				}
				else {
					desactivado = true;
					clase = "boxConsulta";			
				}
			}
	} 
	else  {
		if (modo.equals("nuevo")) {
			editarCampos = true;	
			desactivado = false;
			idInstitucion = String.valueOf((Integer)request.getAttribute("idInstitucion"));
		}
	}
	
	String parametro[] = new String[4];
    parametro[0] = (String)usr.getLanguage().toUpperCase();
	
	parametro[1] = String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCV));
	parametro[2] = String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCV));
	parametro[3] = (String)usr.getLocation();
	
%>	
<html>

<!-- HEAD -->
<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="datosCVForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
  <script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//Asociada al boton Volver
		function accionCerrar(){ 		
			window.top.close();
		}	
		
		// Asociada al boton Restablecer 
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.datosCVForm.reset();
				rellenarCampos();
			}
		}			

		// Asociada al boton GuardarCerrar 
		function accionGuardarCerrar() {
			sub();
			// Validamos los errores ///////////
			if (!validateDatosCVForm(document.datosCVForm)){
				fin();
				return false;
			}

			if (compararFecha (document.datosCVForm.fechaInicio, document.datosCVForm.fechaFin) == 1) {
				aux = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
				alert(aux);
				fin();
				return false;
			}
			////////////////////////////////////

			<% if (!bOcultarHistorico) { %>
				var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
				window.top.focus();
			<% } else { %>
				var datos = new Array();
				datos[0] = 1;
				datos[1] = "";
			<% } %>

			if (datos == null){ 
				fin();
				return false;
			}
			
			if (datos[0] == 1) { // Boton Guardar
				document.datosCVForm.motivo.value = datos[1];
				<%if (modo.equals("editar")) {%>
					document.datosCVForm.modo.value = "modificar";
				<%}	else { %>
					document.datosCVForm.modo.value = "insertar";
				<%}%>
				document.datosCVForm.target = "submitArea";
				document.datosCVForm.submit();
			}
		}		
		
		//Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable 
		function rellenarCampos(){
				// Obtenemos los valores para el check certificado.
				certificado="<%=certificado%>"
			  if(certificado=="<%=DB_TRUE%>"){
			  	document.all.datosCVForm.certificado.checked = true;	
			  } 		
		}
	</script>	
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.comisiones.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%> &nbsp;&nbsp;
			  <%if ((numero != null) && (!numero.equalsIgnoreCase(""))){%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<%} 
				else {%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
				<%}%>
			</td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

		<html:form action="/CEN_DatosCV.do" method="POST" target="submitArea">
			<html:hidden property="modo" value="cerrar"/>
			
			<%if (editarCampos) {%>
				<html:hidden property="idPersona" 			value="<%=idPersona%>"/> 
				<html:hidden property="idCV" 						value="<%=idCV%>"/> 
				<html:hidden property="idInstitucion" 	value="<%=idInstitucion%>"/> 
				<html:hidden property="motivo" 					value=""/> 
			<%}%>
			
			<table  class="tablaCentralCamposMedia"  align="center">			
				<tr>				
					<td>
						<siga:ConjCampos leyenda="censo.datosCV.cabecera">
					
						<table class="tablaCampos" align="center" border="0">
						
							<% if (!fechaBaja.equals("")) { %>
				   			    <tr>
					   		      <td colspan="3">&nbsp;</td>
					   		      <td class="labelText">
					   		          <siga:Idioma key="censo.consultaDatos.literal.fechaBaja"/>&nbsp;&nbsp;&nbsp;<%=fechaBaja%>
					   		      </td>
				   			    </tr>
			   			    <% } %>
						
							<tr>		
								<td class="labelText"  style="display:none"><siga:Idioma key="censo.datosCV.literal.tipo"/>&nbsp(*)</td>
								<td style="display:none">
									<%if (editarCampos) { %>
											<siga:ComboBD nombre="tipoApunte" tipo="curriculum" clase="boxCombo" obligatorio="true" elementoSel = "<%=idTipoCV%>" accion="Hijo:idTipoCVSubtipo1,Hijo:idTipoCVSubtipo2;recargarCombos(this);" />
									<%} else {%>
										<html:text name="datosCVForm" property="tipoApunte" value='<%=tipoApunte%>'  styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
									<%}%>	
								</td>	
								<td class="labelText" ><siga:Idioma key="censo.datosCV.literal.comision"/></td>	
								<td >
									<%if (editarCampos) { 
									 
									%>
											<siga:ComboBD nombre="idTipoCVSubtipo1" tipo="cmbComision1"  parametro="<%=parametro%>" clase="boxCombo"  obligatorio="true" elementoSel = "<%=idSubtipo1%>" hijo="t" accion="parent.deshabilitarCombos(this);"/>
									<%} else {%>
										<html:text name="datosCVForm" property="idTipoCVSubtipo1" value='<%=descTipo1%>'  styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
									<%}%>	
								</td>	
								<td class="labelText"  ><siga:Idioma key="censo.datosCV.literal.cargo"/></td>	
								<td >
									<%if (editarCampos) { %>
											<siga:ComboBD nombre="idTipoCVSubtipo2" tipo="cmbCargos1" parametro="<%=parametro%>" clase="boxCombo" obligatorio="true" elementoSel = "<%=idSubtipo2%>" hijo="t" accion="parent.deshabilitarCombos(this);"/>
									<%} else {%>
										<html:text name="datosCVForm" property="idTipoCVSubtipo2" value='<%=descTipo2%>'  styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
									<%}%>
								</td>								
							</tr>
							
							<tr>
								<td class="labelText" ><siga:Idioma key="censo.datosCV.literal.fechaInicio"/>&nbsp(*)</td>
								<td>
									<%if (editarCampos) {%>
									<siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>" />
									<%}else{%>	
									<siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>" disabled="true" />
									<%}%>	
								</td>
								
								<td class="labelText" ><siga:Idioma key="censo.datosCV.literal.fechaFin"/>&nbsp(*)</td>	
								<td>
										<%if (editarCampos) {%>
										<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>" />
										<%}else{%>	
										<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>" disabled="true" />
										<%}%>	
								</td>	
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma key="censo.datosCV.literal.certificado"/></td>
								<td><html:checkbox name="datosCVForm" property="certificado" disabled="<%=desactivado%>"/></td>

								<td class="labelText"><siga:Idioma key="censo.datosCV.literal.fechaCertificado"/>&nbsp</td>
								<td>
									<%if (editarCampos) {%>
									<siga:Fecha  nombreCampo= "fechaMovimiento" valorInicial="<%=fechaCertificado%>" />
									<%} else { %>
									<siga:Fecha  nombreCampo= "fechaMovimiento" valorInicial="<%=fechaCertificado%>"  disabled="true"/>
									<%}%>	
								</td>
							</tr>
							
							<tr>						
								<td class="labelText" ><siga:Idioma key="censo.datosCV.literal.creditos"/>&nbsp</td>				
								<td>
									<html:text name="datosCVForm" property="creditos" value='<%=creditos%>' maxlength="10" size="10" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
								</td>
							</tr>

							<tr>
								<td class="labelText" ><siga:Idioma key="censo.datosCV.literal.descripcion"/>&nbsp(*)</td>
									<td colspan="3">
										<textarea cols="70" rows="5" name="descripcion" class="<%=clase%>" style="width:550" readOnly="<%=desactivado%>"><%=descripcion%></textarea>
									</td>			   	
							</tr>
						</table>
						</siga:ConjCampos>
				</td>
				</tr>
			</table>
			<script>
				rellenarCampos();
			</script>
		</html:form>
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->		
		<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=modo%>'  modal="M"/>
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
