<!-- BusquedaRemesas_CAJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.ScsEstadoEJGBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGColegioBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->

<% 
	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	String anio="", numero="",numEJG="", fechaApertura="", estado="", busquedaRealizada="", nif="", nombre="", apellido1="", apellido2="", idPersona="", creadoDesde="";
	String idPersonaDefensa="";
	
	String volver=(String)request.getAttribute("VOLVER");

	Hashtable miHash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");
	ses.removeAttribute("DATOSFORMULARIO");
	anio = UtilidadesBDAdm.getYearBD("");

	String anio2="";
	
	String calidad="", procedimiento="", asunto="";
	ArrayList juzgado=new ArrayList();
  ArrayList idTurno = new ArrayList(), idGuardia = new ArrayList(), idTipoEJG = new ArrayList(), idTipoEJGColegio = new ArrayList(), idEstado = new ArrayList();
	try {
		
		busquedaRealizada = miHash.get("BUSQUEDAREALIZADA").toString();
		
		if (busquedaRealizada!=null){
			if (miHash.get(ScsEJGBean.C_ANIO)!=null)
				anio2=miHash.get(ScsEJGBean.C_ANIO).toString();
			if (miHash.get(ScsEJGBean.C_NUMEJG)!=null)
				numEJG = miHash.get(ScsEJGBean.C_NUMEJG).toString();
			if (miHash.get(ScsEJGBean.C_NUMERO)!=null)
				numero = miHash.get(ScsEJGBean.C_NUMERO).toString();
			if (miHash.get(ScsEJGBean.C_FECHAAPERTURA)!=null)
				fechaApertura = miHash.get(ScsEJGBean.C_FECHAAPERTURA).toString();
			if (miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG)!=null)
				estado = miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString();
			if (miHash.get(ScsEJGBean.C_IDPERSONA)!=null)
				idPersona = miHash.get(ScsEJGBean.C_IDPERSONA).toString();	
			if (miHash.get(ScsPersonaJGBean.C_NIF)!=null)
				nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();	
			if (miHash.get(ScsPersonaJGBean.C_NOMBRE)!=null)
				nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();	
			if (miHash.get(ScsPersonaJGBean.C_APELLIDO1)!=null)
				apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();	
			if (miHash.get(ScsPersonaJGBean.C_APELLIDO2)!=null)
				apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();
			if (miHash.get("CREADODESDE")!=null)
				creadoDesde = miHash.get("CREADODESDE").toString();
			if (miHash.get("CALIDAD")!=null)	
				calidad = miHash.get("CALIDAD").toString();
			if (miHash.get("JUZGADO")!=null){
				juzgado.add(miHash.get("JUZGADO").toString());
				
			}	
			if (miHash.get("PROCEDIMIENTO")!=null)
				procedimiento = miHash.get("PROCEDIMIENTO").toString();
			if (miHash.get("ASUNTO")!=null)
				asunto = miHash.get("ASUNTO").toString();		
			if (miHash.get(ScsTurnoBean.C_IDTURNO)!=null){
				idTurno.add(miHash.get(ScsTurnoBean.C_IDTURNO).toString());
					
			}	
		}
	}catch (Exception e){					};
	
	//String idTurnos = new String("");

	
	try {
		idTurno.add(miHash.get(ScsTurnoBean.C_IDTURNO).toString());
		
		idGuardia.add(miHash.get(ScsGuardiasTurnoBean.C_IDGUARDIA).toString());
		idTipoEJG.add(miHash.get(ScsTipoEJGBean.C_IDTIPOEJG).toString());
		idTipoEJGColegio.add(miHash.get(ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO).toString());
		idEstado.add(miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString());
	} catch (Exception e) {};
	
	String[] datos={usr.getLocation()};	
	ArrayList juzgadoSel   = new ArrayList();
	String dato[] = {(String)usr.getLocation()};
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script type="text/javascript">	
		function refrescarLocal(){
			buscar();
		}		
		function buscarCliente ()
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");	
			if (resultado != null && resultado[2]!=null)
			{
				document.forms[1].idPersona.value=resultado[2];
			}
		}
		function inicio(){
		<% 
		
		 if (busquedaRealizada.equals("1")) {%>
		      buscarPaginador();
		<%}%>
		}
		function inicio2(){
		
		<%  if (volver.equals("1")) {%>
		      buscar();
		 <%}%>
		
		}
	</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa"
		localizacion="gratuita.BusquedaRemesas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body  onload="inicio();ajusteAlto('resultado');inicio2();">

	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<html:form action="/JGR_E-Comunicaciones_Gestion.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "idRemesa" value = ""/>
	
		

		

	<fieldset name="fieldset2" id="fieldset2" style="display:none">
	<legend>
		<span class="boxConsulta">
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.datos"/>
		</span>
	</legend>
	</fieldset>

	<fieldset name="fieldset1" id="fieldset1">
	<legend>
		<span  class="boxConsulta">
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.datos"/>
		</span>
	</legend>
	
	<table align="center" width="100%" border="0">
	<tr>	
		<td class="labelText" >
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.nRegistro"/>	
		</td>
		
		<td class="labelText" colspan="2">	
			<html:text name="DefinicionRemesas_CAJG_Form" property="prefijo"  size="5" maxlength="10" styleClass="box" style="width:55px" readonly="false" ></html:text>
			<html:text name="DefinicionRemesas_CAJG_Form" property="numero"  size="5" maxlength="10" styleClass="box" style="width:55px" readonly="false" ></html:text>
			<html:text name="DefinicionRemesas_CAJG_Form" property="sufijo"  size="5" maxlength="10" styleClass="box" style="width:55px" readonly="false" ></html:text>	
		</td>
		
		<td class="labelText">
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.Descripcion"/>	
		</td>
		
		<td class="labelText" colspan="3">	
			<html:text  name="DefinicionRemesas_CAJG_Form" property="descripcion"  size="56" maxlength="200" styleClass="box"  readonly="false" ></html:text>
				
		</td>
		
		<td>
		</td>
		
		
		
	</tr>
	<tr>
		<td class="labelText" >
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.estado"/>	
		</td>
		<td class="labelText" colspan="2">		
			<siga:ComboBD nombre="idEstado" tipo="cmbEstadosRemesa" estilo="true" clase="boxCombo" obligatorio="false"/>			
				
		</td>
		<td class="labelText" >
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fecha"/>
		</td>
		<td class="labelText">
		<html:select  name="DefinicionRemesas_CAJG_Form" property="tipoFecha" styleClass="boxCombo" >			
				<html:option value=""></html:option>
				<html:option value="<%=ClsConstants.COMBO_MOSTRAR_GENERACION%>"><siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fGeneracion"/></html:option>
				<html:option value="<%=ClsConstants.COMBO_MOSTRAR_ENVIO%>" ><siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fEnvio"/></html:option>
				<html:option value="<%=ClsConstants.COMBO_MOSTRAR_RECEPCION%>" ><siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fRecepcion"/></html:option>				
		</html:select>	
		
		</td>
		<td class="labelText" >
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.desde"/>&nbsp;&nbsp;<html:text name="DefinicionRemesas_CAJG_Form" property="fechaInicioBuscar" size="10" styleClass="box" value=""></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaInicioBuscar);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.hasta"/>&nbsp;&nbsp;<html:text name="DefinicionRemesas_CAJG_Form" property="fechaFinBuscar" size="10" styleClass="box" value=""></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaFinBuscar);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		</td>
	</tr>
	
	
	

	</table>

	

	</fieldset>
	
	</html:form>	
	
	
	
	
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="B,N,CON"  titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa" />
	
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
	<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_SJCS%>"/>
	<html:hidden property="modo" value="inicio"/>
	<html:hidden property="accionAnterior" value="${path}"/>

</html:form>	
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

	

	

		<!-- Funcion asociada a boton buscar -->
		
		function buscarPaginador() 
		{
			document.forms[1].modo.value = "buscarPor";
			
			/* El idenficiador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
			con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
			el combo hijo de guardias
			*/
			var id = document.forms[1].identificador.value;
			document.forms[1].descripcionEstado.value = document.forms[1].estadoEJG[document.forms[1].estadoEJG.selectedIndex].text;



			var posicion = 0;
			/* Se recorre hasta encontrar el separador, que es ","*/									
			posicion = id.indexOf(',') + 1;
			/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
			document.forms[1].guardiaTurnoIdTurno.value = id.substring(posicion);
			if (isNaN(document.forms[1].anio.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
			}
			/*else if (isNaN(document.forms[1].numEJG.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
			}  Podemos hacer la busqueda por este campo con comodines*/
			else if (isNaN(document.forms[1].idPersona.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorIdPersona"/>');
			}
			else document.forms[1].submit();
		}		
		
		function buscar(idRemesa) 
		{
			sub();
			document.forms[0].modo.value = "buscarInit";
			//si me pasan una remesa es para imprimir
			if (idRemesa) {
				document.forms[0].idRemesa.value = idRemesa
			} else {
				document.forms[0].idRemesa.value = ""
			}
			
			document.forms[0].submit();
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.DefinicionRemesas_CAJG_Form){
				
					document.DefinicionRemesas_CAJG_Form.idRemesa.value      = resultado[1];
					document.DefinicionRemesas_CAJG_Form.idInstitucion.value = resultado[2];
					modo.value          = "editar";
					target				= "mainWorkArea";
			   		submit();
				}
			}
		}

		function consultas() 
		{		
			document.RecuperarConsultasForm.submit();
			
		}
		
		


		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->	

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>