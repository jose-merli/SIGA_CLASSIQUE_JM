<!DOCTYPE html>
<html>
<head>
<!-- BusquedaRemesas_CAJG.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	

	String prefijo="", numero="", sufijo="", descripcion="", busquedaRealizada="", fechaInicioBuscar="", fechaFinBuscar="", tipoFecha="";
	ArrayList idEstado = new ArrayList();
	Hashtable miHash = new Hashtable();
	
	String volver=(String)request.getAttribute("VOLVER");
	
	if (ses.getAttribute("DATOSBUSQUEDA") instanceof Hashtable) {
		miHash = (Hashtable) ses.getAttribute("DATOSBUSQUEDA");
		ses.removeAttribute("DATOSBUSQUEDA"); 
	
		try {
			busquedaRealizada = miHash.get("BUSQUEDAREALIZADA").toString();
			
			if (busquedaRealizada!=null){	
				if (miHash.get("PREFIJO")!=null)
					prefijo = miHash.get("PREFIJO").toString();
				
				if (miHash.get("NUMERO")!=null)
					numero = miHash.get("NUMERO").toString();
				
				if (miHash.get("SUFIJO")!=null)
					sufijo = miHash.get("SUFIJO").toString();
				
				if (miHash.get("DESCRIPCION")!=null)
					descripcion = miHash.get("DESCRIPCION").toString();
				
				if (miHash.get("TIPOFECHA")!=null)
					tipoFecha = miHash.get("TIPOFECHA").toString();
				
				if (miHash.get("FECHAINICIOBUSCAR")!=null)
					fechaInicioBuscar = miHash.get("FECHAINICIOBUSCAR").toString();
				
				if (miHash.get("FECHAFINBUSCAR")!=null)
					fechaFinBuscar = miHash.get("FECHAFINBUSCAR").toString();
				
				if (miHash.get("IDESTADO")!=null)
					idEstado.add(miHash.get("IDESTADO").toString());
			}
		} catch (Exception e) {};
	}
%>



	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<script type="text/javascript">	
			function refrescarLocal(){
				buscar();
			}
			
			function inicio() {		
				<%if (volver.equals("1")) {%>
		      		buscar();
		 		<%}%>		
			}
		</script>
		
		
	</head>

<body  onload="inicio(); ajusteAlto('resultado');">


	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	
	
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<html:form action="${path}?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "idRemesa" value = ""/>
		<html:hidden property = "idTipoRemesa" />
<c:choose>
	<c:when test="${DefinicionRemesas_CAJG_Form.idTipoRemesa==1 }">
	<siga:Titulo titulo="menu.sjcs.ecomunicaciones.InfEconomico" localizacion="gratuita.BusquedaRemesas.localizacion"/>
	</c:when>
	<c:otherwise>
		<siga:Titulo titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa" localizacion="gratuita.BusquedaRemesas.localizacion"/>
	</c:otherwise>
	</c:choose>
	
		
		<!-- FIN: TITULO Y LOCALIZACION -->
<siga:ConjCampos leyenda="gratuita.BusquedaRemesas_CAJG.literal.datos">

		

			<table align="center" width="100%" border="0">
				<tr>	
					<td class="labelText" >
						<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.nRegistro"/>	
					</td>		
					<td class="labelText" colspan="2">	
						<html:text name="DefinicionRemesas_CAJG_Form" property="prefijo" size="5" maxlength="10" styleClass="box" style="width:55px" readonly="false" value="<%=prefijo%>" />
						<html:text name="DefinicionRemesas_CAJG_Form" property="numero" size="5" maxlength="10" styleClass="box" style="width:55px" readonly="false" value="<%=numero%>"/>
						<html:text name="DefinicionRemesas_CAJG_Form" property="sufijo" size="5" maxlength="10" styleClass="box" style="width:55px" readonly="false" value="<%=sufijo%>" />	
					</td>
	
					<td class="labelText">
						<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.Descripcion"/>	
					</td>		
					<td class="labelText" colspan="3">	
						<html:text  name="DefinicionRemesas_CAJG_Form" property="descripcion" size="56" maxlength="200" styleClass="box" readonly="false" value="<%=descripcion%>" />				
					</td>	
					<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.busquedaEJG.literal.anyo" />/<siga:Idioma key="gratuita.busquedaEJG.literal.codigo" />
					</td>
					<td>	
						<html:text name="DefinicionRemesas_CAJG_Form" property="anioEJG" styleId="anioEJG" style="width:40px" maxlength="4" styleClass="box"/>
						&nbsp;/&nbsp;
						<html:text name="DefinicionRemesas_CAJG_Form" property="codigoEJG" styleId="codigoEJG" style="width:90px" maxlength="13" styleClass="box"/> 
					</td>
									
				</tr>
				
				<tr>
					<td class="labelText" >
						<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.estado"/>	
					</td>
					<td class="labelText" colspan="2">		
						<siga:ComboBD nombre="idEstado" tipo="cmbEstadosRemesa" estilo="true" clase="boxCombo" obligatorio="false" elementoSel="<%=idEstado%>"/>										
					</td>
					
					<td class="labelText" >
						<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fecha"/>
					</td>
					<td class="labelText">		
						<select name="tipoFecha" class="boxCombo">
							<option value=""></option>
							
							<option value="<%=ClsConstants.COMBO_MOSTRAR_GENERACION%>" <%if (tipoFecha.equals(ClsConstants.COMBO_MOSTRAR_GENERACION)) {%> selected <%}%> >
								<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fGeneracion"/>
							</option>
							
							<option value="<%=ClsConstants.COMBO_MOSTRAR_ENVIO%>" <%if (tipoFecha.equals(ClsConstants.COMBO_MOSTRAR_ENVIO)) {%> selected <%}%>  >
								<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fEnvio"/>
							</option>
							
							<option value="<%=ClsConstants.COMBO_MOSTRAR_RECEPCION%>" <%if (tipoFecha.equals(ClsConstants.COMBO_MOSTRAR_RECEPCION)) {%> selected <%}%> >
								<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fRecepcion"/>
							</option>				
						</select>							
					</td>
					
					<td class="labelText" >
						<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.desde"/>
					</td>
					<td>
						<siga:Fecha nombreCampo="fechaInicioBuscar" valorInicial="<%=fechaInicioBuscar%>" />
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.hasta"/>
					</td>
					<td>
						<siga:Fecha nombreCampo="fechaFinBuscar" valorInicial="<%=fechaFinBuscar%>" campoCargarFechaDesde="fechaInicioBuscar"/>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>	
	</html:form>	
	
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
		<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_SJCS%>"/>
		<html:hidden property="modo" value="inicio"/>
		<html:hidden property="accionAnterior" value="${path}"/>
	</html:form>		
	<!-- FIN: CAMPOS DE BUSQUEDA-->
			
	<!-- INICIO: BOTONES BUSQUEDA -->		
	<siga:ConjBotonesBusqueda botones="B,N,CON"  titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa" />
	<!-- FIN: BOTONES BUSQUEDA -->			

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">	
		function buscar(idRemesa) {
			//sub();
			document.forms[0].modo.value = "buscarInit";
			//si me pasan una remesa es para imprimir
			if (idRemesa) {
				document.forms[0].idRemesa.value = idRemesa
			} else {
				document.forms[0].idRemesa.value = ""
			}
			
			if(document.forms[0].anioEJG){
				var objRegExp  = /^([0-9]{4})?$/;
				if(!objRegExp.test(document.forms[0].anioEJG.value)){
					alert( "<siga:Idioma key='errors.formato' arg0='gratuita.busquedaEJG.literal.anyo'/>"+ '\n');
					//fin();
					return false;
				}
			}	
			
			if(document.forms[0].anioEJG.value!='' && document.forms[0].codigoEJG.value ==''){
				alert( "<siga:Idioma key='errors.required' arg0='gratuita.busquedaEJG.literal.codigo'/>"+ '\n');
				//fin();
				return false;
				
			}
			
			
			document.forms[0].submit();
		}			
	
		// Funcion asociada a boton Nuevo
		function nuevo() {
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.DefinicionRemesas_CAJG_Form){					
					document.DefinicionRemesas_CAJG_Form.idRemesa.value = resultado[1];
					document.DefinicionRemesas_CAJG_Form.idInstitucion.value = resultado[2];
					modo.value = "editar";
					target = "mainWorkArea";
			   		submit();
				}
			}
		}

		function consultas() {		
			document.RecuperarConsultasForm.submit();				
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		id="resultado"
		name="resultado" 
		scrolling="no"
		frameborder="0"
		marginheight="0"
		marginwidth="0"					 
		class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->	
		
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>