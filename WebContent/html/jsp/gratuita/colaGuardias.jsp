<!DOCTYPE html>
<html>
<head>
<!-- colaGuardias.jsp -->
	 
<!-- CABECERA JSP -->   
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion"%>

<!-- JSP -->
<%
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String nListad = request.getAttribute("NLETRADOSINSCRITOS") != null ? (String) request.getAttribute("NLETRADOSINSCRITOS") : "";

	String idGuardia = (String) request.getAttribute("idGuardia");
	String idInstitucion = (String) request.getAttribute("idInstitucion");
	String idTurno = (String) request.getAttribute("idTurno");

	String buscarLetrado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.buscarLetrado");
	String literalNColegiado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.nColegiado");
	String literalFijarUltimoLetrado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.fijarUltimoLetrado");
	String literalAnadirFila = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.anadirFila");
	String accionTurno = (String)request.getSession().getAttribute("accionTurno");
	boolean porGrupos;
	String tamanoCol;
	String nombreCol;
	String grupos = (String) request.getAttribute("porGrupos");
	if (grupos != null && grupos.equalsIgnoreCase("1")) {
		porGrupos = true;
		tamanoCol = "5,6,6,15,28,14,14,12";
		nombreCol = " ,Gr,Or,gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,F.Val,F.Baja,";
	} else {
		porGrupos = false;
		tamanoCol = "16,38,18,18,10";
		nombreCol = "gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,F.Val,F.Baja,";
	}
%>	

<!-- HEAD -->


	<!-- HEAD -->
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<siga:Titulo titulo="pestana.justiciagratuitaturno.colaGuardia" localizacion="gratuita.turnos.localizacion.colaTurno.manteniento"/>
		
		<script type="text/javascript">			

			function refrescarLocal(){	
				document.location = document.location;
			}
			
			function buscarLetradoEnColaLetrado() {
				var valorBusqueda = document.getElementById("buscarLetrado").value;
				if (valorBusqueda) {
					var inputBusqColegiado = jQuery("#tablaLetrados_BodyDiv tbody tr td input#numeroColegiadoBusqueda[value$='_" + valorBusqueda + "']");
					
					if (inputBusqColegiado.exists()){
						var inputBusqColegiado_fila = inputBusqColegiado.val().split("_")[0];
						selectRow(parseInt(inputBusqColegiado_fila) + numeroElementosNuevos + 1, "tablaLetrados");

					} else
						selectRow(-1, "tablaLetrados");
				} else
					selectRow(-1, "tablaLetrados");
			}			
		
			function selectRowTablaLetrados(fila) {
				var tablaDatos = jQuery("#tablaLetrados_BodyDiv tbody");
				var numTotalElementos = tablaDatos.children().length;

				for (var i=0; i<numTotalElementos; i++) {
					tablaDatos.find("tr:eq(" + i + ")").attr("class", 'listaNonEdit');
			   	}
			   
			   	if (fila>=0 && fila<numTotalElementos) {
			   		tablaDatos.find("tr:eq(" + fila + ")").attr("class", 'listaNonEditSelected');
			   	}
			}

			function limpiarTexto(t, limpiar) {
				if (limpiar == 1) {
					t.value= "";
					
				} else {
					if (!t.value) {
						t.value = "<%=literalNColegiado%>";
					}
				}
			}

			function fijarUltimoLetrado(fila) {
				selectRowTablaLetrados(fila);
			
				var mensaje = "<siga:Idioma key="gratuita.turnos.literal.fijarUltimoLetrado.pregunta"/>";
				if(!confirm(mensaje)) {						
					return;
				}
					
				var idPersona = document.getElementById('idPersona_' + fila).value;
				document.forms[0].idPersona.value = idPersona;
				var fechaSuscripcion = document.getElementById('fechaSuscripcion_' + fila).value;
				document.forms[0].fechaSuscripcion.value = fechaSuscripcion;
				var idGrupoGuardiaColegiado = document.getElementById('idGrupoGuardiaColegiado_' + fila).value;
				document.forms[0].idGrupoGuardiaColegiado.value = idGrupoGuardiaColegiado;
				
				document.forms[0].modo.value = "fijarUltimoLetrado";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
			}

			var numeroElementosNuevos = 0;
			function anadirFilaLetrado(fila) {
				var tablaDatos = jQuery("#tablaLetrados_BodyDiv tbody");
				var numTotalElementos = tablaDatos.children().length;
				var numSiguiente = numTotalElementos + 1;				
				
				if (numTotalElementos > 0) {

					var elementoTr = "<tr";			
					if (tablaDatos.find("tr:eq(0)").attr("class").indexOf("filaTablaPar") >= 0) {
						elementoTr = elementoTr + " class='filaTablaImpar tableTitle'";
					} else {
						elementoTr = elementoTr + " class='filaTablaPar tableTitle'";
					}
					elementoTr = elementoTr + ">";					

					var estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(0)").attr("style");
					var elementoTd = "<td id='check_" + numTotalElementos + "' align='center' style='" + estiloTd + "'>";
					elementoTd = elementoTd + "<input type='checkbox' id='checkGrupoOrden' value='" + numSiguiente + "' onclick='modificaParametro(this)' checked/>";
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;			
					
					
					estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(1)").attr("style");
					elementoTd = "<td id='grupo_" + numTotalElementos + "' align='left' style='" + estiloTd + "'>";	
					elementoTd = elementoTd + "<input type='text' value='' id='grupo_" + numSiguiente + "' style='width:30px' maxlength='4'/>" +
					  						  "<input type='hidden' value='' id='grupoOriginal_" + numSiguiente + "'/>";
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;
					
					
					estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(2)").attr("style");
					elementoTd = "<td id='orden_" + numTotalElementos + "' align='left' style='" + estiloTd + "'>";	
					elementoTd = elementoTd + "<input type='text' value='' id='orden_" + numSiguiente + "' style='width:30px' maxlength='4'/>" +
					  						  "<input type='hidden' value='' id='ordenOriginal_" + numSiguiente + "'/>";
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;
					
					
					numColBusqueda = document.getElementById("numeroColegiadoBusqueda").value;
					person = document.getElementById("idPersona_" + (fila+1)).value;
					fSuscr = document.getElementById("fechaSuscripcion_" + (fila+1)).value;
					estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(3)").attr("style");
					elementoTd = "<td id='colegiado_" + numTotalElementos + "' align='left' style='" + estiloTd + "'>";					
					elementoTd = elementoTd + jQuery("#colegiado_" + fila).text();
					elementoTd = elementoTd + "<input id='numeroColegiadoBusqueda' name='numeroColegiadoBusqueda' type='hidden' class='box' size='10' value='" + numColBusqueda + "'/> " +
					   						  "<input id='idPersona_" + numSiguiente + "' name='idPersona_" + numSiguiente + "' type='hidden' class='box' size='10' value='" + person + "'/> " +
					   						  "<input id='fechaSuscripcion_" + numSiguiente + "' name='fechaSuscripcion_" + numSiguiente + "' type='hidden' class='box' size='20' value='" + fSuscr + "'/> " +
					   						  "<input id='idGrupoGuardiaColegiado_" + numSiguiente + "' name='idGrupoGuardiaColegiado_" + numSiguiente + "' type='hidden' class='box' size='10' value=''/>";
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;
					
					
					estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(4)").attr("style");
					elementoTd = "<td id='nombre_" + numTotalElementos + "' align='left' style='" + estiloTd + "'>";		
					elementoTd = elementoTd + jQuery("#nombre_" + fila).text();
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;
					
					
					estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(5)").attr("style");
					elementoTd = "<td id='falta_" + numTotalElementos + "' align='left' style='" + estiloTd + "'>";		
					elementoTd = elementoTd + jQuery("#falta_" + fila).text();
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;
					
					
					estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(6)").attr("style");
					elementoTd = "<td id='fbaja_" + numTotalElementos + "' align='left' style='" + estiloTd + "'>";		
					elementoTd = elementoTd + jQuery("#fbaja_" + fila).text();
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;
					
					estiloTd = tablaDatos.find("tr:eq(0)").find("td:eq(7)").attr("style");
					elementoTd = "<td id='iconos_" + numTotalElementos + "' align='center' style='" + estiloTd + "'>";
					elementoTd = elementoTd + "<img src='/SIGA/html/imagenes/bcambiarusuario.gif'" +
													" name='bcambiarusuario'" +
													" onClick='fijarUltimoLetrado(" + numSiguiente + ")'" +
													" style='cursor:hand;'" +
													" title='<siga:Idioma key="gratuita.turnos.literal.fijarUltimoLetrado"/>' " +
													" alt='<siga:Idioma key="gratuita.turnos.literal.fijarUltimoLetrado"/>' > " +
		   			   						  "<img src='/SIGA/html/imagenes/icono+.gif'" +
		   			   						  		" name='banadirlinea'" +
		   			   						  		" onClick='anadirFilaLetrado(" + numTotalElementos + ")'" +
		   			   						  		" style='cursor:hand;'" +
		   			   						  		" title='<siga:Idioma key="gratuita.turnos.literal.anadirFila"/>'" + 
		   			   						  		" alt='<siga:Idioma key="gratuita.turnos.literal.anadirFila"/>'>";
					elementoTd = elementoTd + "</td>";
					elementoTr = elementoTr + elementoTd;
					
					elementoTr = elementoTr + "</tr>";	

					tablaDatos.prepend(elementoTr);
					numeroElementosNuevos = numeroElementosNuevos + 1;
				}
			}				
		</script>
	</head>

<body class="tablaCentralCampos">

 	<html:form action="/JGR_ColaGuardias" method="get" >
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="modo">
		
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="idPersona" >
		<input type="hidden" name="fechaSuscripcion" >
		<input type="hidden" name="idGuardia" value="<%=idGuardia%>" >
		<input type="hidden" name="idGrupoGuardiaColegiado">
		<input type="hidden" name="datosModificados">
		<input type="hidden" name="idInstitucion" value="<%=idInstitucion%>">
		<input type="hidden" name="idTurno" value="<%=idTurno%>">
		<html:hidden property="fechaConsulta"/>
	</html:form>	
		
<!-------------------------------------------------------------------------------------------------->	
<!---------- Ultimo letrado ------------------------------------------------------------------------>	
<!-------------------------------------------------------------------------------------------------->	
	<table width="100%" border="0" cellpadding="5" cellspacing="0">		
  		<tr>
	  		<td colspan="2">
	  			<siga:ConjCampos leyenda="gratuita.turnos.literal.ultimo">
		  			<table width="100%" border="0" cellpadding="5" cellspacing="0">
						<tr>
			  				<td class="labelText" nowrap>
			  					<siga:Idioma key="gratuita.turnos.literal.nColegiado"/>: 
			  				</td>
			  				<td class="labelText" >
								<html:text name="ColaGuardiasForm" property="NColegiado" styleClass="boxConsulta" />
			  				</td>
			  				
			  				<td class="labelText" nowrap >
			  					<siga:Idioma key="gratuita.turnos.literal.nombreSolo"/>: 
			  				</td>
			  				<td class="labelText" >
								<html:text name="ColaGuardiasForm" property="nombre" styleClass="boxConsulta" />
			  				</td>
			  				
			  				<td class="labelText" nowrap>
			  					<siga:Idioma key="gratuita.turnos.literal.apellidosSolo"/>: 
			  				</td>
			  				<td class="labelText" >
								<html:text name="ColaGuardiasForm" property="apellido1" styleClass="boxConsulta" size="50"/>
			  				</td>
						</tr>
		  			</table>
	 			</siga:ConjCampos>
	  		</td>
		</tr>
		
  		<tr>
	  		<td width="66%">	  
	  
<!-------------------------------------------------------------------------------------------------->	
<!---------- Letrados en Cola ---------------------------------------------------------------------->	
<!-------------------------------------------------------------------------------------------------->

				<table id="tituloTablaLetrados" width="100%" border="1" cellpadding="5" cellspacing="0">
					<tr class='tableTitle'>
<%
						if (porGrupos) {
%>
							<td class="tdBotones" width='17%'>
								<input type="button" alt="Guardar" id="idButton" onclick="return accionGuardar();" class="button" name="idButton" value="Guardar">
							</td>
<%
						}
%>
						<td width='22%'>
							<input id="buscarLetrado" type="text" class="box" size="8" value="<%=literalNColegiado%>" onfocus="limpiarTexto(this, 1);" onblur="limpiarTexto(this, 0);buscarLetradoEnColaLetrado();"/>
							<img src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>" style="cursor: hand;" onClick="buscarLetradoEnColaLetrado();" alt="<%=buscarLetrado%>" title="<%=buscarLetrado%>"/>
						</td>
							
<%
						if (porGrupos) {
%>
							<td align='center' width='61%'>
<%
						} else {
%>
							<td align='center' width='78%'>
<%
						}
%>						
							<siga:Idioma key="gratuita.colaGuardia.literal.letradosInscritos" />:&nbsp;&nbsp;<%=nListad%>
						</td>
					</tr>
				</table>

				<siga:Table
		   			name="tablaLetrados"
		   			columnSizes="<%=tamanoCol%>"
		   			columnNames="<%=nombreCol%>"
		   			width="650px">

					<!-- INICIO: ZONA DE REGISTROS -->
					<!-- Aqui se iteran los diferentes registros de la lista -->		

<%
					ArrayList letradosColaGuardiaList = (ArrayList) request.getAttribute("letradosColaGuardiaList");
					if (letradosColaGuardiaList == null || letradosColaGuardiaList.size() == 0) {
%>				 		
	 					<tr class="notFound">
			   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
						</tr>	 		

<%
					} else {
	 					// recorro el resultado
	 					String grupoAnt = "";
	 					String ordenAnt = "";
	 					String apellido1 = "";
	 					String apellido2 = "";
	 					String nombre = "";
	 					String ncolegiado = "";
	 					String idPersona = "";
	 					String fechaSuscripcion = "";
	 					String numeroColegiadoBusqueda = "";
	 					String grupo = "";
	 					String ordenGrupo = "";
	 					String idGrupoGuardiaColegiado = "";
	 					int nFila = 0;
	 					
	 					for (int i = 0; i < letradosColaGuardiaList.size(); i++) {
	 						LetradoInscripcion letradoGuardia = (LetradoInscripcion) letradosColaGuardiaList.get(i);

	 						// calculo de campos
	 						apellido1 = letradoGuardia.getPersona().getApellido1();
	 						apellido2 = letradoGuardia.getPersona().getApellido2();
	 						nombre = letradoGuardia.getPersona().getNombre();
	 						ncolegiado = letradoGuardia.getPersona().getColegiado().getNColegiado();
	 						idPersona = letradoGuardia.getIdPersona().toString();
	 						fechaSuscripcion = letradoGuardia.getInscripcionGuardia().getFechaSuscripcion();
	 						numeroColegiadoBusqueda = "" + i + "_" + ncolegiado;
	 						grupo = letradoGuardia.getNumeroGrupo() != null ? letradoGuardia.getNumeroGrupo().toString() : "";
	 						if (porGrupos) {
	 							if (!grupo.equalsIgnoreCase(grupoAnt)) {
	 								nFila++;
	 								grupoAnt = grupo;
	 							}
	 						} else {
	 							nFila++;
	 						}
	 						ordenGrupo = letradoGuardia.getOrdenGrupo() != null ? letradoGuardia.getOrdenGrupo().toString() : "";
	 						idGrupoGuardiaColegiado = letradoGuardia.getIdGrupoGuardiaColegiado() != null ? letradoGuardia.getIdGrupoGuardiaColegiado().toString() : "";
%>
	
							<!-- REGISTRO  -->
  							<tr class="<%=((nFila + 1) % 2 == 0 ? "filaTablaPar" : "filaTablaImpar")%>">
<%
								if (porGrupos) {
%>
	  								<td align="center">
	  									<input type="checkbox" id="checkGrupoOrden" value="<%=i + 1%>" onclick="modificaParametro(this)"/>
	  								</td>	  					
									<td align="left">
										<input type="text" value="<%=grupo%>" id="grupo_<%=i + 1%>" disabled style="width:30px" maxlength="4"/>
										<input type="hidden" value="<%=grupo%>" id="grupoOriginal_<%=i + 1%>" />
									</td>
						
									<td align="left">
										<input type="text" value="<%=ordenGrupo%>" id="orden_<%=i + 1%>" disabled style="width:30px" maxlength="4"/>
										<input type="hidden" value="<%=ordenGrupo%>" id="ordenOriginal_<%=i + 1%>" />
									</td>
<%
								}
%>
								
								<td id="colegiado_<%=i%>" align="left">
									<input id="numeroColegiadoBusqueda" name="numeroColegiadoBusqueda" type="hidden" class="box, numeroColegiadoBusqueda" size="10" value="<%=numeroColegiadoBusqueda%>" />
									<input id="idPersona_<%=i + 1%>" name="idPersona_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=idPersona%>" />
									<input id="fechaSuscripcion_<%=i + 1%>" name="fechaSuscripcion_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=fechaSuscripcion%>" />
									<input id="idGrupoGuardiaColegiado_<%=i + 1%>" name="idGrupoGuardiaColegiado_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=idGrupoGuardiaColegiado%>" />
									<%=ncolegiado%>
								</td>
							
								<td id="nombre_<%=i%>" align="left">
									<%=apellido1 + " " + apellido2 + ", " + nombre%>
								</td>
							
								<td id="falta_<%=i%>" align="left">
<%
									if (letradoGuardia.getInscripcionGuardia().getFechaValidacion() != null && !letradoGuardia.getInscripcionGuardia().getFechaValidacion().equals("")) {
%>
										<%=letradoGuardia.getInscripcionGuardia().getFechaValidacion()%>
<%
									} else {
%>
										&nbsp;
<%
									}
%>
								</td>
							
								<td id="fbaja_<%=i%>" align="left">
<%
									if (letradoGuardia.getInscripcionGuardia().getFechaBaja() != null && !letradoGuardia.getInscripcionGuardia().getFechaBaja().equals("")) {
%>
										<%=letradoGuardia.getInscripcionGuardia().getFechaBaja()%>
<%
									} else {
%>
										&nbsp;
<%
									}
%>
								</td>
			
								<td id="iconos_<%=i%>" align="center">
									<!-- aalg: INC_10634_SIGA -->
<%
									if (!accionTurno.equalsIgnoreCase("Ver")) {
%>
										<img src="<html:rewrite page='/html/imagenes/bcambiarusuario.gif'/>" id="bcambiarusuario" name="bcambiarusuario" style="cursor:hand;" onClick="fijarUltimoLetrado(<%=i + 1%>)" alt="<%=literalFijarUltimoLetrado%>" title="<%=literalFijarUltimoLetrado%>"/>
<% 
										if (porGrupos) {	
%>
											<img src="<html:rewrite page='/html/imagenes/icono+.gif'/>" id="banadirlinea" name="banadirlinea" style="cursor:hand;" onClick="anadirFilaLetrado(<%=i%>)" alt="<%=literalAnadirFila%>" title="<%=literalAnadirFila%>"/>
<% 
										}
									} 
%>	
								</td>
							</tr>		
							<!-- FIN REGISTRO -->
							<!-- FIN: ZONA DE REGISTROS -->
<%		
						} // del for			
					} // del else
%>			
				</siga:Table>	
	  		</td>
	  			  	  
			<td width="34%">	  			  	  
<!-------------------------------------------------------------------------------------------------->	
<!---------- Compensaciones ------------------------------------------------------------------------>	
<!-------------------------------------------------------------------------------------------------->			
				<table id="tituloTablaCompensaciones" width="100%" border="1" cellpadding="5" cellspacing="0">
		  			<tr class='tableTitle'>
						<td align='center' width='100%'><siga:Idioma key="gratuita.turnos.literal.compensaciones"/></td>
		  			</tr>
				</table> 
				 
<% 
				if (porGrupos) {	
%>
		 			<siga:Table
		    			name="tablaCompensaciones"
		    			border="1"
	 					columnSizes="10,80,10"
	     				columnNames="Gr,gratuita.turnos.literal.nombreSolo,Nº"
 		    			fixedHeight="48%">
<%
						Vector resultado = (Vector) request.getAttribute("vCompensaciones");
						if (resultado == null || resultado.size() == 0) {
%>			
	 						<tr class="notFound">
			   					<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
							</tr> 		
<%
 						} else {
 							for (int i = 0; i < resultado.size(); i++) {
 								String  nombre="", ncolegiado="", numero="";
 								Hashtable registro = (Hashtable) resultado.get(i);
								if (registro != null){
									nombre =  UtilidadesString.mostrarDatoJSP(registro.get("LETRADO"));
	 								ncolegiado = UtilidadesString.mostrarDatoJSP(registro.get("NUMERO"));
	 								numero = UtilidadesString.mostrarDatoJSP(registro.get("REP"));									
								}
%>
  								<tr class="listaNonEdit">
									<td align="center"><%=ncolegiado%></td>
									<td><%=nombre%></td>
									<td align="center"><%=numero%></td>
								</tr>		
<%
							} // for
						} // else
%>			
					</siga:Table>
		
<% 
				} else { 
%>					
					<siga:Table
		    			name="tablaCompensaciones"
		    			border="1"
						columnSizes="22,50,28"			
						columnNames="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.compensaciones"
		    			fixedHeight="48%">
<%
						Vector resultado = (Vector) request.getAttribute("vCompensaciones");
						if (resultado == null || resultado.size() == 0) {
%>			
	 						<tr class="notFound">
			   					<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
							</tr>
<%
 						} else {
 							for (int i = 0; i < resultado.size(); i++) {
	 							String apellido1="", apellido2="", nombre="", ncolegiado="", numero="";
 							
		 						Row registro = (Row) resultado.elementAt(i);
		 						// calculo de campos
		 						apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
		 						apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
		 						nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
		 						ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
		 						numero = UtilidadesString.mostrarDatoJSP(registro.getString("NUMERO"));
%>
  								<tr class="listaNonEdit">
									<td><%=ncolegiado%></td>
									<td><%=nombre + " " + apellido1 + " " + apellido2%></td>
									<td><%=numero%></td>
								</tr>		
<%
							} // for
						} // else
%>			
					</siga:Table>
<% 
				} 
%>  
	  
<!-------------------------------------------------------------------------------------------------->	
<!---------- Saltos -------------------------------------------------------------------------------->	
<!-------------------------------------------------------------------------------------------------->	
				<table id="tituloTablaSaltos" width="100%" border="1" cellpadding="5" cellspacing="0">
			  		<tr class='tableTitle'>
						<td align='center' width='100%'><siga:Idioma key="gratuita.turnos.literal.saltos"/></td>
			  		</tr>
				</table>		
			
<% 
				if (porGrupos) {	
%>		
					<siga:Table
			   			name="tablaSaltos"
			   			border="1"
			   			columnSizes="10,80,10"
		       			columnNames="Gr,gratuita.turnos.literal.nombreSolo,Nº">
<%
						Vector resultado = (Vector) request.getAttribute("vSaltos");
						if (resultado == null || resultado.size() == 0) {
%>			
		 					<tr class="notFound">
			   					<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
							</tr> 		
<%
	 					} else {
	 						for (int i = 0; i < resultado.size(); i++) {
	 							String  nombre="", ncolegiado="", numero="";
	 							Hashtable registro = (Hashtable) resultado.get(i);
								if (registro != null){
									nombre =  UtilidadesString.mostrarDatoJSP(registro.get("LETRADO"));
		 							ncolegiado = UtilidadesString.mostrarDatoJSP(registro.get("NUMERO"));
		 							numero = UtilidadesString.mostrarDatoJSP(registro.get("REP"));									
								}
%>
	  							<tr class="listaNonEdit">
									<td align="center"><%=ncolegiado%></td>
									<td><%=nombre%></td>
									<td align="center"><%=numero%></td>
								</tr>		
<%
							} // for
						} // else
%>			
					</siga:Table>
			
<%	
				} else { 
%>			
					<siga:Table
					   name="tablaSaltos"
					   border="1"
					   columnSizes="22,50,28"
					   columnNames="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.saltos">
<%
						Vector resultado = (Vector) request.getAttribute("vSaltos");
						if (resultado == null || resultado.size() == 0) {
%>			
		 					<tr class="notFound">
			   					<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
							</tr>		 						 		
<%
		 				} else {
		 					for (int i = 0; i < resultado.size(); i++) {
		 						String apellido1="", apellido2="", nombre="", ncolegiado="", numero="";
		 							
			 					Row registro = (Row) resultado.elementAt(i);
			 					// calculo de campos
			 					apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
			 					apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
			 					nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
			 					ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
			 					numero = UtilidadesString.mostrarDatoJSP(registro.getString("NUMERO"));
%>
				  				<tr class="listaNonEdit">
									<td><%=ncolegiado%></td>
									<td><%=nombre + " " + apellido1 + " " + apellido2%></td>
									<td><%=numero%></td>
								</tr>		
<%
							} // for
						} // else
%>			
					</siga:Table>		
<% 
				} 
%>
  			</td>
		</tr>
	</table>
  
  	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
  
  	<script>
	  	function habilitarCambiarUsuario(valor) {
	  		if (valor)
	  		   	jQuery("#bcambiarusuario").attr("disabled","disabled");
	  		else
				jQuery("#bcambiarusuario").removeAttr("disabled");
	
		}
	  	
		habilitarCambiarUsuario(document.getElementById('fechaConsulta').value=='');

		function modificaParametro(o) {
			var valorGrupo = jQuery("#grupo_" + o.value);
			var valorOrden = jQuery("#orden_" + o.value);
			
			if (o.checked) {
				valorGrupo.removeAttr("disabled");
				valorOrden.removeAttr("disabled");		
				
			} else {
				var mensaje = "<siga:Idioma key="administracion.parametrosGenerales.alert.restaurarValor"/>";
				if(confirm(mensaje)) {						
					valorGrupo.value = document.getElementById("grupoOriginal_" + o.value).value;
					jQuery("#grupoOriginal_" + o.value).attr("disabled","disabled");
					valorOrden.value = document.getElementById("ordenOriginal_" + o.value).value;
					jQuery("#ordenOriginal_" + o.value).attr("disabled","disabled");
					
				} else {
					o.checked = true;
				}
			}
		}
	
		function modificaParametro(o) {
			valorGrupo = document.getElementById("grupo_" + o.value);
			valorOrden = document.getElementById("orden_" + o.value);
			if (o.checked) {
				valorGrupo.disabled = false;
				valorOrden.disabled=false;
			}
			else {
				var mensaje = "<siga:Idioma key="administracion.parametrosGenerales.alert.restaurarValor"/>";
				if(confirm(mensaje)) {						
					valorGrupo.value = document.getElementById("grupoOriginal_" + o.value).value;
					valorGrupo.disabled = true;
					valorOrden.value = document.getElementById("ordenOriginal_" + o.value).value;
					valorOrden.disabled = true;
				}else{
					o.checked = true;
				}
			}
		}	
	
		function accionGuardar() {
			sub()
			var datos = "";
			var elementosCheck = jQuery("#tablaLetrados_BodyDiv tbody tr td input#checkGrupoOrden");
			
			for (i = 0; i < elementosCheck.length; i++) {
				if (elementosCheck[i].checked) {
					var valorGrupo = jQuery("#tablaLetrados_BodyDiv tbody tr td input#grupo_" + elementosCheck[i].value).val();
					var longitudValorGrupo = valorGrupo.length;
					var valorOrden = jQuery("#tablaLetrados_BodyDiv tbody tr td input#orden_" + elementosCheck[i].value).val();
					var longitudValorOrden = valorOrden.length;
					
					/* if ((longitudValorGrupo<1 || longitudValorOrden<1)) {						 						
						alert ("<siga:Idioma key="administracion.parametrosGenerales.error.valorParametro"/> "+
						 	  "<siga:Idioma key="gratuita.turnos.literal.grupo"/> " + 
							   "<siga:Idioma key="general.y"/> " + 
						 	  "<siga:Idioma key="gratuita.turnos.literal.orden"/> ");
								return;	
					}
					*/
					
					if (valorGrupo!='' && valorGrupo<1) {							
						alert ("El grupo debe ser un número comprendido entre 1 y 9999");
						fin();
						return;					
					}
					
					if (valorGrupo!='' && valorOrden<1) {							
						alert ("El orden debe ser un número comprendido entre 1 y 9999");
						fin();
						return;					
					}
						
					if (datos.length > 0) 
						datos = datos + "#;;#";
						
					var valorGrupoGuardia = jQuery("#tablaLetrados_BodyDiv tbody tr td input#idGrupoGuardiaColegiado_" + elementosCheck[i].value).val();
					var valorPersona = jQuery("#tablaLetrados_BodyDiv tbody tr td input#idPersona_" + elementosCheck[i].value).val();
					var valorFechaSuscripcion = jQuery("#tablaLetrados_BodyDiv tbody tr td input#fechaSuscripcion_" + elementosCheck[i].value).val();
						
					datos = datos + valorGrupoGuardia + "#;#" + // idgrupoguardiacolegiado						
						valorGrupo + "#;#" + // grupo
						valorOrden + "#;#" + // orden
						valorPersona + "#;#"+ // idPersona
						valorFechaSuscripcion + "#;#"; // fechaSuscripcion
				}
			}
			
			if (datos.length < 1) {
				alert ("<siga:Idioma key="administracion.parametrosGenerales.alert.seleccionarElementos"/>");
				fin();
				return;
			}
			
			document.forms[0].datosModificados.value = datos;
			document.forms[0].modo.value = "modificar";
			document.forms[0].target = "submitArea";
			document.forms[0].submit();
		}	
  	</script>
</body>
</html>