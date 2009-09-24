<!-- busquedaClientesFiltros.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.ScsCabeceraGuardiasBean"%>
<%@ page import="com.siga.beans.*"%>

 
<!-- JSP -->
<%  String titu = "censo.busquedaClientes.literal.titulo";
	String busc = "censo.busquedaClientes.literal.titulo";
	String app=request.getContextPath();
	HttpSession ses=request.getSession();

	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");	
	ScsCabeceraGuardiasBean cabeceraGuardias=new ScsCabeceraGuardiasBean();
	ArrayList seleccion = new ArrayList();
	ArrayList seleccion2 = new ArrayList();
	String dato[] = {(String)usr.getLocation()};	
	String idturno=(String)request.getParameter("idTurno");
	String idguardia=(String)request.getParameter("idGuardia");
	String institucion=(String)usr.getLocation();

	// Combo guardias = identificardor 2
	ArrayList elementoSel = new ArrayList();
	String aaaaaqaq = "" + institucion + "," + idguardia;
	elementoSel.add (aaaaaqaq); 

%>
   
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu%>" 
		localizacion="<%=titu%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="busquedaClientesFiltrosForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="ajusteAltoBotones('resultado'); cambiarValor();">

	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="<%=titu %>"/>
		</td>
	</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">

	<table class="tablaCampos" align="center">

	<html:form action="/JGR_BusquedaClientesFiltros.do" method="POST" target="resultado">
	<html:hidden name="busquedaClientesFiltrosForm" property = "modo" value = ""/>

	<!-- campos ocultos -->
	<html:hidden name="busquedaClientesFiltrosForm" property = "concepto" />
	<html:hidden name="busquedaClientesFiltrosForm" property = "operacion" />
	<html:hidden name="busquedaClientesFiltrosForm" property = "idTurno" />
	<html:hidden name="busquedaClientesFiltrosForm" property = "idGuardia" />
	<html:hidden name="busquedaClientesFiltrosForm" property = "fecha" />


	<!-- FILA -->
	<tr>				
		<logic:notEmpty name="busquedaClientesFiltrosForm" property="concepto">					
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSJCS.literal.filtro"/>&nbsp;
		</td>
		<td>
			<select name="idFiltro" class="boxCombo" onChange="cambiarValor();">
			<logic:equal name="busquedaClientesFiltrosForm" property="concepto" value="SALTOSCOMP"  >					
				<option value="1"><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosMismaGuardia"/></option>
				<option value="2"><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosOtrasGuardia"/></option>
				<option value="3" selected><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosTurno"/></option>
				<option value="4"><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosTodosTurnos"/></option>
				<option value="5"><siga:Idioma key="gratuita.busquedaSJCS.literal.censoCompleto"/></option>
			</logic:equal>
			<logic:notEqual name="busquedaClientesFiltrosForm" property="concepto" value="SALTOSCOMP"  >		
				<logic:notEqual name="busquedaClientesFiltrosForm" property="concepto" value="DESIGNACION"  >			
					<option value="1" selected><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosMismaGuardia"/></option>
					<option value="2"><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosOtrasGuardia"/></option>
					<option value="3"><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosTurno"/></option>
					<option value="4"><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosTodosTurnos"/></option>
					<option value="5"><siga:Idioma key="gratuita.busquedaSJCS.literal.censoCompleto"/></option>
				</logic:notEqual>
			</logic:notEqual>
			<logic:equal name="busquedaClientesFiltrosForm" property="concepto" value="DESIGNACION">					
				<option value="6" selected><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosTurno"/></option>
				<option value="7"><siga:Idioma key="gratuita.busquedaSJCS.literal.letradosTodosTurnos"/></option>
				<option value="8"><siga:Idioma key="gratuita.busquedaSJCS.literal.censoCompleto"/></option>
			</logic:equal>
			</select>
		</td>
		</logic:notEmpty>					
		<logic:empty name="busquedaClientesFiltrosForm" property="concepto">					
		<td class="labelText" colspan="2">
		</td>
		</logic:empty>					

		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaEJG.literal.turno"/>
		</td>
		<td>
			<div id="identificadorDiv" style="display:inline">
				<siga:ComboBD nombre = "identificador" tipo="turnos" clase="boxCombo" obligatorio="false" accion="Hijo:identificador2" parametro="<%=dato%>" ancho="240"/>
			</div>
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/>
		</td>
		<td>
			<html:text name="busquedaClientesFiltrosForm" property="numeroColegiado" maxlength="20" size="10" styleClass="box"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaEJG.literal.guardia"/>
		</td>
		<td>
			<siga:ComboBD nombre = "identificador2" tipo="guardiasConSustitucion" clase="boxCombo" elementoSel="<%=elementoSel%>" obligatorio="false" hijo="t" ancho="240"/>
			&nbsp;<img src="<%=app+"/html/imagenes/botonAyuda.gif"%>" width="20" style="cursor:hand" alt="<siga:Idioma key='gratuita.busquedaEJG.tooltip.guardia'/>" >
		</td>
	</tr>
	
	<!-- FILA -->
	<tr>				
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.nif"/>
		</td>
		<td>
			<html:text name="busquedaClientesFiltrosForm" property="nif" size="15" styleClass="box"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.nombre"/>
		</td>				
		<td>
			<html:text name="busquedaClientesFiltrosForm" property="nombrePersona" size="30" styleClass="box"></html:text>
		</td>
	</tr>				
	
	<!-- FILA -->
	<tr>				
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>
		</td>
		<td>
			<html:text name="busquedaClientesFiltrosForm" property="apellido1" size="30" styleClass="box"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>
		</td>
		<td>
			<html:text name="busquedaClientesFiltrosForm" property="apellido2" size="30" styleClass="box"></html:text>
		</td>
	</tr>

	</html:form>
	</table>

	</siga:ConjCampos>

	</td>
	</tr>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
		<siga:ConjBotonesBusqueda botones="B"  modal="G" titulo="<%=busc%>" />
	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();		
			var idFiltro=document.forms[0].idFiltro.value;
			if (idFiltro == 1) {
				comboGuardias = document.getElementById("identificador2").value;
				if (comboGuardias == "") {
					alert("<siga:Idioma key="gratuita.busquedaSJCS.error.guardiaObligatoria"/>");
					fin();
					return;
				}
							
				
			}
			if (idFiltro == 3) {
				comboTurnos   = document.getElementById("identificador").value;
				if (comboTurnos == "") {
					alert("<siga:Idioma key="gratuita.busquedaSJCS.error.turnoObligatorio"/>");
					fin();
					return;
				}
			}
		
			document.forms[0].modo.value="buscar";
			document.forms[0].submit();	
		}

		function cambiarValor()
		{
			var idFiltro=document.forms[0].idFiltro.value;
			
			// Buscamos algo de logica en la pantalla con la informacion que se muestra en los combos
			aplicarLogicaCombos (idFiltro);
			
			var idturno="<%=idturno%>";

			if(idturno.indexOf(",")!=null)idturno=idturno.substring(idturno.indexOf(",")+1);
			
			var clave="<%=institucion%>,"+idturno;
			var a=document.forms[0].identificador.options;

			if (idFiltro=="1") {
				for (i=0;i<a.length;i++){
					if (a[i].value==clave){
						a[i].selected=true;
						document.forms[0].identificador.onchange();

						seleccionComboSiga ("identificador2", "<%=elementoSel.get(0)%>");  // siga.js
						return;
					}
				}
			} 
			
			if (idFiltro=="2"||idFiltro=="3"||idFiltro=="6") {
				for (i=0;i<a.length;i++){
					if (a[i].value==clave){
						a[i].selected=true;
						document.forms[0].identificador.onchange();

						seleccionComboSiga ("identificador2", "");  // siga.js
						return;
					}
				}
			}

			a[0].selected=true;
			document.forms[0].identificador.onchange();
		}
		
		function aplicarLogicaCombos(idFiltro)
		{   
			comboTurnos   = document.getElementById("identificadorDiv");
			comboGuardias = document.getElementById("identificador2Frame");

		    comboTurnos.style.visibility="visible";
			comboGuardias.style.visibility="visible";
			
			if (idFiltro == 3 || idFiltro == 6) {
				 // Todo lo de un turno en concreto
				 comboGuardias.style.visibility="hidden";
			}
			else if (idFiltro == 4 || idFiltro == 7) {
				// Todos los turnos
				comboTurnos.style.visibility="hidden";
				comboGuardias.style.visibility="hidden";
			}
			else if (idFiltro == 5 || idFiltro == 8) {
				// Censo completo
				comboTurnos.style.visibility="hidden";
				comboGuardias.style.visibility="hidden";
			}
			
		}
		

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

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

	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="C" modal="G" clase="botonesDetalle"/>
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}
	

	</script>
	<!-- FIN: SCRIPTS BOTONES -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
