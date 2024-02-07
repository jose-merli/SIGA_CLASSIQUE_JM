<!DOCTYPE html>
<html>
<head>
<!-- nuevoTurno.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsTurnoAdm"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasAdm"%>
<%@ page import="com.siga.beans.GenParametrosAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.DefinirTurnosForm"%>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	//recogemos los datos
	Hashtable resultado = (Hashtable) request.getAttribute("resultado");
	
	String localiz="";
	String entrada =(String)request.getSession().getAttribute("entrada");
	
	String asterisco = "&nbsp;(*)&nbsp;";
	int pcajgActivo = 0;
    boolean obligatorioCodigoExterno = false;
	if (request.getAttribute("pcajgActivo") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute(
				"pcajgActivo").toString());	
		System.out.println("pcajgActivo: "+pcajgActivo);
	}
	
	if (pcajgActivo == 4 || pcajgActivo==11){		
		obligatorioCodigoExterno = true;
	}

	DefinirTurnosForm miform = (DefinirTurnosForm) request.getAttribute("DefinirTurnosForm");
	
	String classCombo="boxCombo";
	if(entrada.equalsIgnoreCase("1")){
		localiz="SJCS > Turnos > Inserci�n Turno";
	} else {
		localiz="Censo > Buscar Colegiado > SJCS > Turnos > Inserci�n Turno";
	}
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	 	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DefinirTurnosFormNuevo" staticJavascript="false" />  	  	
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	 	
 	
	<!-- script de salto a partidos -->
	<script language="JavaScript">		
		// Asociada al boton Volver
		function accionVolver() {		
<%
			if (entrada.equalsIgnoreCase("1")) {
%>
				document.forms[0].action="JGR_DefinirTurnos.do";
				document.forms[0].target="mainWorkArea";
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].submit();
<%
			} else {
%>
				document.forms[0].action="JGR_DefinirTurnosLetrado.do";
				document.forms[0].target="mainPestanas";
				document.forms[0].modo.value="abrir";
				document.forms[0].submit();
<%
			}
%>			
		}

		// Asociada al boton Cancelar
		function accionCancelar() {		
			document.forms[0].reset();
		}
		
		//Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();
		}

		function refrescarLocal() {
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value="editar";
			document.forms[0].submit();
		}
		
		//Asociada al boton Guardar
		function accionGuardar() {	
			sub();			
			var f=document.getElementById("DefinirTurnosForm");

			if (<%=obligatorioCodigoExterno%> && document.forms[0].codigoExterno.value.length<1) {
<%
				String mensajecodigoexterno = UtilidadesString.getMensajeIdioma(usr, "messages.codigoExterno.obligatoria");
%>
				var error = "<%= mensajecodigoexterno%>";
				alert(error);
				fin();
				return false;	
			}	
			
			if (document.forms[0].subzona.value=="") {
				alert('<siga:Idioma key="messages.subzona.obligatoria"/>');
				fin();
				return false;
				
			} else {
				if (document.forms[0].grupoFacturacion.value==''){ 
					alert('<siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
					fin();
					return false;
					
				} else {
					if (validateDefinirTurnosFormNuevo(f)) {
						document.forms[0].target="submitArea";
						document.forms[0].modo.value="insertar";
						document.forms[0].submit();
						
					} else{
						fin();
						return false;
					}
				}
			}
		}

		//Asociada al boton Siguiente
		function accionSiguiente() {		
			document.forms[0].target="_self";
			document.forms[0].action="JGR_AltaTurnosGuardias.do";
			document.forms[0].modo.value="nuevo";
			document.forms[0].submit();
		}

		function mostrarPartido() {
			var ocultar = true;
			try{
				if (document.partidosJud) {
					if (jQuery("#zona").val() != "" && jQuery("#zona").val() != undefined && jQuery("#zona").val() != null){
						var zonaSeleccionadaJSON = jQuery.parseJSON(jQuery("#zona").val());
						var idZona = zonaSeleccionadaJSON.idzona;
						if (typeof idZona != "undefined"){
							var idSubzona = jQuery("#subzona").val();
						
							document.partidosJud.action= "<%=app%>/html/jsp/gratuita/partidosJudiciales.jsp";
							document.partidosJud.idinstitucion.value="<%=usr.getLocation()%>";
							document.partidosJud.idzona.value=idZona;
							document.partidosJud.idsubzona.value=idSubzona;			
							//console.log("Cargando partidos judiciales de idzona="+idZona+" y idsubzona="+idSubzona);
							document.partidosJud.submit();
							jQuery("#partidosjudiciales").show();
							ocultar = false;
						}
					}
				}
			} catch (e){
				console.error("Error al mostrar el partido judicial: "+ e.message);
			}
			if (ocultar){
				jQuery("#partidosjudiciales").hide();
			}
		}
		
 		function darVisibilidadMovil (o) {
 			if (o.checked) {
 				document.forms[0].ponerVisibilidadMovil.value = "S";
			} else {
				document.forms[0].ponerVisibilidadMovil.value = "N";
			}
 		} 			
		
		jQuery(function(){
			jQuery("#zona").on("change", function(){
				mostrarPartido();
			});
			jQuery("#subzona").on("change", function(){
				mostrarPartido();
			});
		});	
	</script>

	<siga:Titulo titulo="gratuita.maestroGuardias.literal.mantenimientoTurnos" localizacion="<%=localiz%>"/>
</head>

<body>
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="gratuita.maestroGuardias.literal.mantenimientoTurnos"/>
			</td>
		</tr>
	</table>
	
	<html:form action = "/DefinirTurnosAction.do" method="POST" target="submitArea" enctype="multipart/form-data" onsubmit="return Buscar()">
		<input type="hidden" name="modo" value="insertar">
	
		<siga:ConjCampos leyenda="gratuita.listarTurnos.literal.estado">
			<table width="30%" border="0" align="left" class="labelText" >
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<INPUT NAME="visibilidad" TYPE=RADIO VALUE="1" checked> <siga:Idioma key="gratuita.maestroTurnos.literal.bajaLogica.alta"/> 
					</td>
	
					<td>
						<INPUT NAME="visibilidad" TYPE=RADIO VALUE="0"> <siga:Idioma key="gratuita.maestroTurnos.literal.bajaLogica.baja"/>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.datosGenerales">
			<table border="0" align="center" width="100%">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>&nbsp;(*)
					</td>
					<td>
						<html:text name="DefinirTurnosForm" property="abreviatura" size="30" maxlength="30" styleClass="box" value="" />
					</td>
					
					<td class="labelText">
						<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>&nbsp;(*) 
					</td>
					<td>
						<html:text name="DefinirTurnosForm" property="nombre" size="50" maxlength="100" styleClass="box" value="" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/>&nbsp;(*) 
					</td>
					<td>
						<siga:Select queryId="getAreas" id="area" queryParamId="idarea" childrenIds="materia"/>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/>&nbsp;(*)
					</td>
					<td>
						<siga:Select queryId="getMaterias" id="materia" parentQueryParamIds="idarea"/>
					</td>
				</tr>
		
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/>&nbsp;(*)
					</td>
					<td>
						<siga:Select queryId="getZonas" id="zona" queryParamId="idzona" childrenIds="subzona"/>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/> &nbsp;(*) 
					</td>
					<td>
						<siga:Select queryId="getSubZonas" id="subzona" parentQueryParamIds="idzona"/>
					</td>
				</tr>
		
				<!-- l�nea de partidos judiciales -->
				<tr>		
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/>
					</td>
					<td>
						<iframe ID="partidosjudiciales" name="partidosjudiciales"  src="<%=app%>/html/jsp/general/blank.jsp" WIDTH="400"  HEIGHT="35"  FRAMEBORDER="0"  MARGINWIDTH="0"  MARGINHEIGHT="1"  SCROLLING="no"></iframe>
					</td>
					
					<td class="labelText" >
						<siga:Idioma key="gratuita.maestroTurnos.literal.codigoExt"/>
<%
						if (obligatorioCodigoExterno) {
%>
							<%=asterisco%> 
<%
	 					}
%>
				  	</td>
					<td>
						<html:text name="DefinirTurnosForm" property="codigoExterno"  size="10" maxlength="10"  styleClass="Box" readonly="false" value="" />			
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.partidaPresupuestaria"/>&nbsp;(*) 
					</td>
					<td>
						<siga:Select queryId="getPartidasPresupuestarias" id="partidaPresupuestaria"/>	
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/>&nbsp;(*)
					</td>
					<td>
						<siga:Select queryId="getGrupoFacturacion" id="grupoFacturacion"/>	
					</td>
				</tr>
		
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroTurnos.literal.descripcion"/>&nbsp;(*)
					</td>
					<td>		
						<textarea name="descripcion"
							onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"
							style="overflow-y:auto; overflow-x:hidden; width:300px; height:50px; resize:none;" 
							class="box"></textarea>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroTurnos.literal.requisitosAcceso"/>&nbsp;(*)
					</td>
					<td>		
						<textarea name="requisitos"
							onKeyDown="cuenta(this,1023)" onChange="cuenta(this,1023)"
							style="overflow-y:auto; overflow-x:hidden; width:300px; height:50px; resize:none;"
							class="box"></textarea>
					</td>
				</tr>
		
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroTurnos.literal.tipoturno"/>&nbsp;
					</td>
					<td>
						<siga:Select queryId="getTiposTurno" id="idTipoTurno"/>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.procedimientos.literal.Jurisdiccion" />&nbsp;
					</td>
					<td>
						<siga:ComboBD nombre="jurisdiccion" ancho="200" tipo="jurisdiccionSCJS" clase="boxCombo" obligatorio="false"/>
					</td>
				</tr>	
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="gratuita.listarTurnos.literal.guardias">
			<table border="0" width="100%" align="center">
				<tr>
					<td colspan="4" style="text-align:right">
						<table width="80%" border="0" align="center" class="labelText">
							<tr>
								<td width="30%" style="text-align:center" >
									<INPUT NAME="guardias" TYPE=RADIO VALUE="0" checked> <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.obligatorias"/> 
								</td>
								<td width="30%" style="text-align:center">
									<INPUT NAME="guardias" TYPE=RADIO VALUE="1" > <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.todasNinguna"/>
								</td>
								<td width="30%" style="text-align:center">
									<INPUT NAME="guardias" TYPE=RADIO VALUE="2" > <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.elegir"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.configuracion">
			<table border="0" width="100%" align="center">
				<tr>
					<td colspan="4" width="16%" style="text-align:right">
						<table border="0" align="center" width="90%" class="labelText" border="1">
							<tr>
								<td style="text-align:left">
									<INPUT NAME="validarJustificaciones" TYPE=CHECKBOX>
									<siga:Idioma key="gratuita.maestroTurnos.literal.validarJustificaciones"/>
								</td>
								<td style="text-align:left" >
									<INPUT NAME="validacionInscripcion" TYPE=CHECKBOX checked>
									<siga:Idioma key="gratuita.maestroTurnos.literal.validarInscripciones"/>
								</td>			
<%
								GenParametrosAdm admGen = new GenParametrosAdm(usr);
								String sActivarRestriccion = admGen.getValor(usr.getLocation(), "SCS", "ACTIVAR_RESTRICCIONES_ACREDITACION", "0");
								boolean bActivarRestriccion = UtilidadesString.stringToBoolean(sActivarRestriccion);
%>
								<td style="text-align:left" >
									<input type="checkbox" name="activarRestriccionActuacion" value="1" <%if (bActivarRestriccion) {%> checked <%}%> > 
									<siga:Idioma key="gratuita.maestroTurnos.literal.activarRestriccion"/>
								</td>
							</tr>
	
							<tr>
								<td style="text-align:left" >
									<input type="checkbox" name="activarAsistenciasLetrado" value="1" checked > 
									<siga:Idioma key="gratuita.maestroTurnos.literal.aniadirAsistenciasLetrado"/>
								</td>
								<td style="text-align:left" >
									<input type="checkbox" name="activarActuacionesLetrado" value="1" checked >
									<siga:Idioma key="gratuita.maestroTurnos.literal.aniadirActuacionesLetrado"/>
								</td>
								<td style="text-align:left" >
									<input type="checkbox" name="ponerVisibilidadMovil" onclick="darVisibilidadMovil(this);">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil"/>
								</td>									
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	
		<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion">
			<table border="0" width="100%" align="center">
				<tr>
					<td class="labelText" style="text-align:left">
						<siga:Idioma key="gratuita.maestroTurnos.literal.primerCriterio"/>
					</td>
					<td style="text-align:right">
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_1">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_1">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					</td>
					
					<td class="labelText" style="text-align:left">
						<siga:Idioma key="gratuita.maestroTurnos.literal.segundoCriterio"/>
					</td>
					<td style="text-align:right">
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_2">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_2">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					</td>
				</tr>
					
				<tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.terceroCriterio"/></td>
					<td style="text-align:right">
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_3">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_3">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					</td>
					
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.cuartoCriterio"/></td>
					<td style="text-align:right">
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_4">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_4">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>

	<siga:ConjBotonesAccion botones="V,G,R" clase="botonesDetalle"  />

	<!-- formulario de partidos judiciales -->
	<form name="partidosJud" action="" target="partidosjudiciales" method="POST">
		<input type=hidden name="idinstitucion">
		<input type=hidden name="idzona">
		<input type=hidden name="idsubzona">
	</form>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>