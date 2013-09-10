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
				s = document.getElementById("buscarLetrado").value;
				if (s) {
					var inputBusqColegiado = jQuery("input.numeroColegiadoBusqueda[value$='_"+s+"']");
					if (inputBusqColegiado.exists()){
						var inputBusqColegiado_fila = inputBusqColegiado.val().split("_")[0];
						selectRow(parseInt(inputBusqColegiado_fila) + 1, "tablaLetrados");
					} else
						selectRow(-1, "tablaLetrados");
				} else
					selectRow(-1, "tablaLetrados");
			}
		
			function selectRowTablaLetrados(fila) {
			   var tabla;
			   tabla = document.getElementById('tablaLetrados');
			   for (var i = 0; i < tabla.rows.length; i++) {
			     tabla.rows[i].className = 'listaNonEdit';
			   }
			   if (fila >= 0 && fila < tabla.rows.length) {
				   tabla.rows[fila].className = 'listaNonEditSelected';
				   //tabla.rows[fila].scrollIntoView(false);
			   }
			}

			function limpiarTexto(t, limpiar) {
				if (limpiar == 1) {
					t.value= "";
				}
				else {
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

			function anadirFilaLetrado(fila) {			
				table = document.getElementById("tablaLetrados");
				
				if(table.rows.length>0){			
					numFila = table.rows.length;
				
					tr = table.insertRow(numFila);
					if(numFila % 2 == 0){
						tr.className = "filaTablaPar";
					}else{
						tr.className = "filaTablaImPar";
					}
					val = numFila + 1;
					
					td = tr.insertCell();	
					td.id = "check_"+numFila;
					td.innerHTML ='<input type="checkbox" id="checkGrupoOrden" value='+val+' onclick="modificaParametro(this)" checked/>';
					
					td = tr.insertCell();	
					td.id = "grupo_"+numFila;			
					td.innerHTML ='<input type="text" value="" id=grupo_'+val+' style="width:30px" maxlength="4">' +
								  '<input type="hidden" value="" id=grupoOriginal_'+val+'>';			
				
					td = tr.insertCell();	
					td.id = "orden_"+numFila;
					td.innerHTML ='<input type="text" value="" id=orden_'+val+'  style="width:30px" maxlength="4">' +
								  '<input type="hidden" value="" id=ordenOriginal_'+val+'>';
				
					td = tr.insertCell();	
					td.id = "colegiado_"+numFila;			
					numColBusqueda = document.getElementById("numeroColegiadoBusqueda").value;
					person = document.getElementById("idPersona_"+(fila+1)).value;
					fSuscr = document.getElementById("fechaSuscripcion_"+(fila+1)).value;
					//idGrupoGuar = document.getElementById("idGrupoGuardiaColegiado_"+(fila+1)).value;
					td.innerHTML = document.getElementById("colegiado_"+fila).innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'');
				
					td.innerHTML = td.innerHTML +
								   ' <input name="numeroColegiadoBusqueda" type="hidden" class="box" size="10" value="'+numColBusqueda+'" > '+
								   ' <input name="idPersona_'+val+'" type="hidden" class="box" size="10" value="'+person+'" > '+
								   ' <input name="fechaSuscripcion_'+val+'" type="hidden" class="box" size="20" value="'+fSuscr+'" > '+
								   ' <input name="idGrupoGuardiaColegiado_'+val+'" type="hidden" class="box" size="10" value="" >';
				
					td = tr.insertCell();	
					td.id = "nombre_"+numFila;
					td.innerHTML = document.getElementById("nombre_"+fila).innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'');
				
					td = tr.insertCell();	
					td.id = "falta_"+numFila;
					td.innerHTML = document.getElementById("falta_"+fila).innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'');
				
					td = tr.insertCell();	
					td.id = "fbaja_"+numFila;
					td.innerHTML = document.getElementById("fbaja_"+fila).innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'');
				
					td = tr.insertCell();	
					td.id = "iconos_"+numFila;
					td.align="center";
					td.innerHTML = '<img src=/SIGA/html/imagenes/bcambiarusuario.gif name="bcambiarusuario" onClick="fijarUltimoLetrado('+(numFila+1)+')" style="cursor:hand;" alt="<siga:Idioma key="gratuita.turnos.literal.fijarUltimoLetrado"/>" >'+
					   			   '<img src=/SIGA/html/imagenes/icono+.gif          name="banadirlinea"    onClick="anadirFilaLetrado('+numFila+')"  style="cursor:hand;" alt="<siga:Idioma key="gratuita.turnos.literal.anadirFila"/>"         >';
				
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
							<img src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>" style="cursor: hand;" onClick="buscarLetradoEnColaLetrado();" alt="<%=buscarLetrado%>" />
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
		   			columnNames="<%=nombreCol%>">

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
	  								<td>
	  									<input type="checkbox" id="checkGrupoOrden" value="<%=i + 1%>" onclick="modificaParametro(this)"/>
	  								</td>	  					
									<td>
										<input type="text" value="<%=grupo%>" id="grupo_<%=i + 1%>" disabled style="width:30px" maxlength="4"/>
										<input type="hidden" value="<%=grupo%>" id="grupoOriginal_<%=i + 1%>" />
									</td>
						
									<td>
										<input type="text" value="<%=ordenGrupo%>" id="orden_<%=i + 1%>" disabled style="width:30px" maxlength="4"/>
										<input type="hidden" value="<%=ordenGrupo%>" id="ordenOriginal_<%=i + 1%>" />
									</td>
<%
								}
%>
								
								<td id="colegiado_<%=i%>">
									<input name="numeroColegiadoBusqueda" type="hidden" class="box, numeroColegiadoBusqueda" size="10" value="<%=numeroColegiadoBusqueda%>" />
									<input name="idPersona_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=idPersona%>" />
									<input name="fechaSuscripcion_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=fechaSuscripcion%>" />
									<input name="idGrupoGuardiaColegiado_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=idGrupoGuardiaColegiado%>" />
									<%=ncolegiado%>
								</td>
							
								<td id="nombre_<%=i%>">
									<%=apellido1 + " " + apellido2 + ", " + nombre%>
								</td>
							
								<td id="falta_<%=i%>">
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
							
								<td id="fbaja_<%=i%>">
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
										<img src="<html:rewrite page='/html/imagenes/bcambiarusuario.gif'/>" id="bcambiarusuario" name="bcambiarusuario" style="cursor:hand;" onClick="fijarUltimoLetrado(<%=i + 1%>)" alt="<%=literalFijarUltimoLetrado%>"/>
<% 
										if (porGrupos) {	
%>
											<img src="<html:rewrite page='/html/imagenes/icono+.gif'/>" id="banadirlinea" name="banadirlinea" style="cursor:hand;" onClick="anadirFilaLetrado(<%=i%>)" alt="<%=literalAnadirFila%>"/>
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
	  		var bcambiarusuario =document.getElementsByName("bcambiarusuario");
	  		if(valor)
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
			var datos = "";
			var ele = document.getElementsByName("checkGrupoOrden");
			for (i = 0; i < ele.length; i++) {
				if (ele[i].checked) {						
						if( (document.getElementById("grupo_" + ele[i].value).value.length<1 &&
						   	 document.getElementById("orden_" + ele[i].value).value.length>=1)||
						   	(document.getElementById("grupo_" + ele[i].value).value.length>=1 &&
							 document.getElementById("orden_" + ele[i].value).value.length<1) ){						 
								alert ("<siga:Idioma key="administracion.parametrosGenerales.error.valorParametro"/> "+
								 	  "<siga:Idioma key="gratuita.turnos.literal.orden"/> " + 
									   "<siga:Idioma key="general.y"/> " + 
								 	  "<siga:Idioma key="gratuita.turnos.literal.grupo"/> ");
								return;
	
						}else{
							
							if(document.getElementById("orden_" + ele[i].value).value.length>=1 && 
							   document.getElementById("orden_" + ele[i].value).value<=0){
								alert ("El orden debe ser un número comprendido entre 1 y 9999");
								return;					
							}
						}
						
						if (datos.length > 0) datos = datos + "#;;#";
						datos = datos + document.getElementById("idGrupoGuardiaColegiado_" + ele[i].value).value + "#;#" + 	// idgrupoguardiacolegiado
							            document.getElementById("grupo_" + ele[i].value).value + "#;#" +	// grupo
							            document.getElementById("orden_" + ele[i].value).value + "#;#" + 	// orden
								        document.getElementById("idPersona_" + ele[i].value).value + "#;#"+ 	// idPersona
								        document.getElementById("fechaSuscripcion_" + ele[i].value).value + "#;#"; 	// fechaSuscripcion
				}
			}
			
			if (datos.length < 1) {
				alert ("<siga:Idioma key="administracion.parametrosGenerales.alert.seleccionarElementos"/>");
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