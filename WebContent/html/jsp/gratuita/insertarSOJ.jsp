<!-- insertarSOJ.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String dato[] = {(String)usr.getLocation()};
	String nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
	String fechaHoy = UtilidadesBDAdm.getFechaBD("");
	String anioHoy  = UtilidadesBDAdm.getYearBD("");
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<html:javascript formName="DefinirSOJForm" staticJavascript="false" />  
 	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<script type="text/javascript">
		function buscarCliente ()
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado != null && resultado[2]!=null)
			{
				if (resultado[0]!=null)document.forms[1].idPersona.value=resultado[0];
				if (resultado[2]!=null)document.forms[1].NColegiado.value=resultado[2];
			}
		}
		
		function actualizarFecha(){
		  document.forms[1].fecha.value=DefinirSOJForm.fechaAperturaSOJ.value;
		}
	</script>
</head>

<body>
	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">
		<siga:Idioma key="gratuita.insertarSOJ.literal.insertarSOJ"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposMedia" align="center">	
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	<html:form action = "/JGR_ExpedientesSOJ.do" method="POST" target="submitArea">
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "numero" value = ""/>
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "estadoSOJ" value = "A"/>
	<html:hidden property = "idTurno" value = ""/>
	<html:hidden property = "idPersona" value = ""/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fecha" value="<%=fechaHoy%>"></html:hidden>
	<input type="hidden" name = "flagSalto" value=""/>
	<input type="hidden" name = "flagCompensacion" value=""/>	
	
	<tr>		
	<td>			

	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaSOJ.literal.expedientesSOJ">
	<table class="tablaCampos" align="center" border="0" >

	<tr>
		<td class="labelText" width="">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.anyo"/>	&nbsp;(*)
		</td>
		<td class="labelText" >
			<html:text name="DefinirSOJForm" property="anio" size="4" maxlength="4" styleClass="box"  value="<%=anioHoy%>"></html:text>
		</td>
		
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.fechaApertura"/>&nbsp;(*)
		</td>
		<td class="labelText">		
			<html:text name="DefinirSOJForm" property="fechaAperturaSOJ" size="10" maxlength="10" onChange=""  styleClass="box" value="<%=fechaHoy%>" readOnly="true"></html:text>
			<a onClick=" showCalendarGeneral(fechaAperturaSOJ);actualizarFecha();rellenarComboGuardia();" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
		</td>
	</tr>	
	
	<tr>		
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoSOJ"/>&nbsp;(*)
		</td>
		<td class="labelText">
			<siga:ComboBD nombre="idTipoSOJ" tipo="tipoSOJ" clase="boxCombo" obligatorio="false" />
		</td>
	
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.SOJColegio"/>&nbsp;
		</td>
		<td class="labelText">
			<siga:ComboBD nombre="idTipoSOJColegio" tipo="tipoSOJColegio" clase="boxCombo" obligatorio="false" parametro="<%=dato%>"/>
		</td>
	
	</tr>



	<tr>


	<html:hidden name="DefinirSOJForm" property="NColegiado" value="<%=nColegiado%>" ></html:hidden>			

	</tr>	
	</table>
	</siga:ConjCampos>	
	
	<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.tituloEJG">
		<table class="tablaCampos" align="center" border="0" >
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaSOJ.literal.turno"/>
				</td>
				<td class="labelText" colspan="4">
					<siga:ComboBD nombre = "identificador" tipo="turnosSinBaja"  clase="boxCombo" obligatorio="false" accion="Hijo:idGuardia;" parametro="<%=dato%>" ancho="550"/>
				</td>
			</tr>
			<tr>
				<td class="labelText">	
					<siga:Idioma key="gratuita.busquedaSOJ.literal.guardia"/>
				</td>
				<td class="labelText" colspan="4" >
					<siga:ComboBD nombre = "idGuardia" tipo="guardias" clase="boxCombo" accion="parent.rellenarComboGuardia();" obligatorio="false" hijo="t"/>
				</td>
			</tr>
			<tr>
				<td colspan="5">
					<siga:BusquedaSJCS nombre="DefinirSOJForm" propiedad="buscaLetrado"
				 				   concepto="SOJ" operacion="Asignacion" 
								   campoTurno="identificador" campoGuardia="idGuardia" campoFecha="fecha"
								   campoPersona="idPersona" campoColegiado="numeroColegiado" campoNombreColegiado="nomColegiado"  
								   campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" campoSalto="checkSalto" campoCompensacion="compensacion"
								   diaGuardia="true"
								   modo="nuevo"
		    					   />
		   		</td>
		    </tr>
		 	<tr>
				<td class="labelText">
					<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
				</td>		
				<td>
					<input type="text" name="numeroColegiado" class="boxConsulta" readOnly value="" style="width:'100px';">
				</td>
				<td class="labelText">
					<siga:Idioma key='FactSJCS.listadoRetencionesJ.literal.nombreColegiado'/>
				</td>
				<td colspan="2">
					<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="" style="width:'240px';">
				</td>			
			</tr>	
		</table>			
 	</siga:ConjCampos>	

		</td>
	</tr> 
	</html:form>
	</table>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="M"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[1].reset(); 
		}
		
		//Asociada al boton Guardar y Cerrar
		function accionGuardarCerrar() 		
		{	
			sub();
			/* El idenficiador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
			con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
			el combo hijo de guardias
			*/
			var id = document.forms[1].identificador.value;
			var posicion = 0;
			/* Se recorre hasta encontrar el separador, que es ","*/									
			posicion = id.indexOf(',') + 1;
			/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
			document.forms[1].idTurno.value = id.substring(posicion);

			if (validateDefinirSOJForm(document.forms[1])){
				//if ((document.forms[1].idPersona.value == "") && (document.forms[1].NColegiado.value == "")) {
				//	alert ("<siga:Idioma key='errors.required' arg0=''/>" + "<siga:Idioma key='gratuita.busquedaSOJ.literal.nColegiado'/>");
				//	return;
				//}
				document.forms[1].submit();
				//window.top.returnValue="MODIFICADO";
			}else{
				fin();
				return false;
			}
		}
		
		//Asociada al boton Cerrar
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");			
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>
</html>