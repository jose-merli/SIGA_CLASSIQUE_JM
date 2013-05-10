<!-- insertarEJG.jsp -->

<!-- CABECERA JSP -->
<%@page import="org.redabogacia.sigaservices.app.util.ReadProperties"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="org.redabogacia.sigaservices.app.util.SIGAReferences"%>

<!-- JSP -->
<% 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String dato[] = {(String)usr.getLocation()};
	String designaNumero ="", designaAnio="", designaIdTurno="", nColegiado="",asistenciaAnio="",asistenciaNumero="";

	try{
		designaNumero = (String)request.getAttribute("designaNumero");
		designaAnio = (String)request.getAttribute("designaAnio");
		designaIdTurno = (String)request.getAttribute("designaIdTurno");
		nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
		asistenciaNumero = (String)request.getAttribute("asistenciaNumero");
		asistenciaAnio = (String)request.getAttribute("asistenciaAnio");
	}catch(Exception e){}
	
	String fecha = UtilidadesBDAdm.getFechaBD("");
	
	ReadProperties rp=new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	String idordinario = rp.returnProperty("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[]={idordinario,idordinario};	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<html:javascript formName="DefinirEJGForm" staticJavascript="false" />
	
	<script type="text/javascript">
		function buscarCliente () {
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado != null && resultado[2]!=null)
			{
				if (resultado[0]!=null) document.forms[2].idPersona.value=resultado[0];
				if (resultado[2]!=null)document.forms[2].NColegiado.value=resultado[2];
			}
		}

		// Modif Carlos
		function busquedaAutomatica () {
			document.forms[1].idTurno.value 	= document.forms[2].identificador.value;
			document.forms[1].idGuardia.value 	= document.forms[2].identificador2.value;
			document.forms[1].modo.value		= "buscarPor";
			document.forms[1].action			= "/SIGA/JGR_MantenimientoAsistencia.do";
			
			if(document.forms[2].identificador.value == "") {
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1' />");
				return false;
			}
			
			if(document.forms[2].identificador2.value == "") {
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert4' />");
				return false;
			}
			
			var resultado = ventaModalGeneral(document.forms[1].name,"M");
			if (resultado != null && resultado[0]!=null)
				document.forms[2].NColegiado.value = resultado[0]; 
			else
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert5' />");
		}
		
		// Fin Modif Carlos
		function actualizarFecha(){
		  document.forms[1].fechaAperturaEJG.value=DefinirEJGForm.fechaApertura.value;
		}
	</script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/>
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
				<html:hidden property = "actionModal" value = ""/>
				<html:hidden property = "modo" value = ""/>
			</html:form>
	
			<html:form action="/JGR_EJG.do" method="POST" target="submitArea" type="">	
				<!-- Para seleccion automatica -->
				<html:hidden property = "idTurno" value="" />
				<html:hidden property = "idGuardia" value=""  />	
	
				<!-- ************************ //-->
				<html:hidden property = "actionModal" value = ""/>
				<html:hidden property = "modo" value = "Insertar"/>
				<html:hidden property = "guardiaTurnoIdTurno" value = ""/>
				<html:hidden property = "guardiaTurnoIdGuardia" value = ""/>	
				<html:hidden property = "anio" value = ""/>
				<html:hidden property = "idPersona" value = ""/>
				<html:hidden property = "designaNumero" value ="<%=designaNumero%>"/>
				<html:hidden property = "designaAnio" value ="<%=designaAnio%>"/>
				<html:hidden property = "designaIdTurno" value ="<%=designaIdTurno%>"/>
				<html:hidden property = "numero" value = ""/>
				<html:hidden property = "origenApertura" value = "M"/>
				<html:hidden property = "tipoLetrado" value = "M"/>	
				<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
				<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
				<html:hidden property = "fechaAperturaEJG" value = "sysdate"/>
				<html:hidden  property="asistenciaAnio" value ="<%=asistenciaAnio%>" />
				<html:hidden  property="asistenciaNumero" value ="<%=asistenciaNumero%>" />
				<html:hidden  property="origen" />
	
				<input type="hidden" name = "flagSalto" value=""/>
				<input type="hidden" name = "flagCompensacion" value=""/>	
				<html:hidden name="DefinirEJGForm" property="NColegiado" value="<%=nColegiado%>"/>	
				
				<tr>		
					<td>			
						<!-- SUBCONJUNTO DE DATOS -->
						<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.expedientesEJG">
							<table class="tablaCampos" border="0" width="100%">		
								<tr>		
									<td class="labelText">
										<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG"/>&nbsp;(*)
									</td>
									<td>
										<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG"  parametro="<%=datoTipoOrdinario%>" clase="boxCombo" obligatorio="false"  obligatorioSinTextoSeleccionar="true" />
									</td>	
								</tr>
		
								<tr>
									<td class="labelText">
										<siga:Idioma key="gratuita.busquedaEJG.literal.fechaApertura"/>&nbsp;(*)
									</td>
									<td>		
										<siga:Fecha nombreCampo="fechaApertura" valorInicial="<%=fecha%>"/>
									</td>
								</tr>		
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
			
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.tituloEJG"> 
						<table class="tablaCampos" border="0" width="100%">
							<tr>
								<td class="labelText" >
									<siga:Idioma key="gratuita.busquedaEJG.literal.turno"/>&nbsp;
								</td>
								<td class="labelText" colspan="3" >
									<siga:ComboBD nombre = "identificador" tipo="turnosTramitacionAlta" clase="boxCombo" obligatorio="false" accion="Hijo:identificador2" ancho="550" parametro="<%=dato%>"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">	
									<siga:Idioma key="gratuita.busquedaEJG.literal.guardia"/>&nbsp;
								</td>
								<td class="labelText" colspan="3">
									<siga:ComboBD nombre = "identificador2" tipo="guardias" clase="boxCombo"  accion="parent.rellenarComboGuardia();" obligatorio="false" hijo="t"/>
								</td>
							</tr>	

							<tr>
								<td colspan="4">
									<siga:BusquedaSJCS nombre="DefinirEJGForm" propiedad="buscaLetrado"
					 				   concepto="EJG" operacion="Asignacion" 
									   campoTurno="identificador" campoGuardia="identificador2" campoFecha="fechaApertura"
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
								<td>
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
	
		// Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[1].reset();
		}
		
		// Asociada al boton Guardar y Cerrar
		function accionGuardarCerrar() {							
			sub();
			/* El identificador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
			 	con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
			 	el combo hijo de guardias
			*/
			var id = document.forms[1].identificador.value;
			var id2 = document.forms[1].identificador2.value;
			var posicion = 0;
			/* Se recorre hasta encontrar el separador, que es ","*/									
			posicion = id.indexOf(',') + 1;
			/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
			document.forms[1].guardiaTurnoIdTurno.value = id.substring(posicion);
			document.forms[1].guardiaTurnoIdGuardia.value = id2;	

			if (validateDefinirEJGForm(document.forms[1])){
				document.forms[1].modo.value="Insertar";
				document.forms[1].submit();			
				window.top.returnValue="MODIFICADO";
			}else{
				fin();
				return false;
			}
		}
		
		// Asociada al boton Cerrar
		function accionCerrar() {
			top.cierraConParametros("NORMAL");			
		}		 
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>