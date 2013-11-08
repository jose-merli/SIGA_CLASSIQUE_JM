<!DOCTYPE html>
<html>
<head>
<!-- colaOficios.jsp -->	 
 
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

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion"%>

<!-- JSP -->
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String nListad =request.getAttribute("NLETRADOSTURNO") != null?(String)request.getAttribute("NLETRADOSTURNO"):"";
	
	String buscarLetrado             = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.buscarLetrado");
	String literalNColegiado         = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.nColegiado");
	String literalFijarUltimoLetrado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.fijarUltimoLetrado");
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
				
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	<siga:Titulo titulo="pestana.justiciagratuitaturno.colaOficio" localizacion="gratuita.turnos.localizacion.colaTurno.manteniento"/>
	
	<script>
		function refrescarLocal() {	
			document.location=document.location;
		}		
		
		function buscarLetradoEnColaLetrado () {
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
			   tabla.rows[fila].scrollIntoView(false);
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
		 	
			var idPersona  = document.getElementById('idPersona_' + fila).value;
			var fechaSuscripcion  = document.getElementById('fechaSuscripcion_' + fila).value;
			
			
			document.forms[0].idPersona.value = idPersona;
			document.forms[0].fechaSuscripcion.value = fechaSuscripcion;
			document.forms[0].modo.value = "fijarUltimoLetrado";
			document.forms[0].target = "submitArea";
			document.forms[0].submit();
		}
	</script>
</head>

<body>
	<html:form action="/JGR_ColaOficio" method="get" >
		<html:hidden property = "modo"/>
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="idPersona" value="">
		<input type="hidden" name="fechaSuscripcion" value="">
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
								<html:text name="ColaOficiosForm" property="NColegiado" styleClass="boxConsulta" />
			  				</td>
			  				
			  				<td class="labelText" nowrap >
			  					<siga:Idioma key="gratuita.turnos.literal.nombreSolo"/>: 
			  				</td>
			  				<td class="labelText" >
								<html:text name="ColaOficiosForm" property="nombre" styleClass="boxConsulta" />
			  				</td>
			  				
			  				<td class="labelText" nowrap>
			  					<siga:Idioma key="gratuita.turnos.literal.apellidosSolo"/>: 
			  				</td>
			  				<td class="labelText" >
								<html:text name="ColaOficiosForm" property="apellido1" styleClass="boxConsulta" size="50"/>
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
						<td width='32%'>
							<input id="buscarLetrado" type="text" class="box" size="8" value="<%=literalNColegiado%>" onfocus="limpiarTexto(this, 1);" onblur="limpiarTexto(this, 0);buscarLetradoEnColaLetrado();"/>
							<img src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>" style="cursor: hand;" onClick="buscarLetradoEnColaLetrado();" alt="<%=buscarLetrado%>" />
						</td>

						<td align='center' width='68%'>
							<siga:Idioma key="gratuita.colaGuardia.literal.letradosInscritos" />:&nbsp;&nbsp;<%=nListad%>
						</td>
					</tr>
				</table>
				
				<siga:Table
		   			name="tablaLetrados"
		   			border="1"
		   			columnSizes="16,38,18,18,10"
		   			columnNames="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,F.Val,F.Baja,"
		   			width="650px">				

					<!-- INICIO: ZONA DE REGISTROS -->
					<!-- Aqui se iteran los diferentes registros de la lista -->
							
<%
					ArrayList letradosColaGuardiaList = (ArrayList) request.getAttribute("letradosColaTurnoList");
					if (letradosColaGuardiaList==null || letradosColaGuardiaList.size()==0) {	
%>			
	 					<tr class="notFound">
			   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
						</tr> 		
<%
	 				} else { 
	 					for (int i=0;i<letradosColaGuardiaList.size();i++) {
	 						LetradoInscripcion letradoGuardia = (LetradoInscripcion) letradosColaGuardiaList.get(i);
	 			
				 			// calculo de campos
				 			String apellido1 = letradoGuardia.getPersona().getApellido1();
				 			String apellido2 =letradoGuardia.getPersona().getApellido2();
				 			String nombre = letradoGuardia.getPersona().getNombre();
				 			String ncolegiado = letradoGuardia.getPersona().getColegiado().getNColegiado();
			
				 			String idPersona = letradoGuardia.getIdPersona().toString();
				 			String numeroColegiadoBusqueda = "" + i + "_" + ncolegiado;
%>
							<tr class="<%=((i + 1) % 2 == 0 ? "filaTablaPar" : "filaTablaImpar")%>">
								<td>
									<input name="numeroColegiadoBusqueda" type="hidden" class="box, numeroColegiadoBusqueda" size="10" value="<%=numeroColegiadoBusqueda%>" >
									<input name="idPersona_<%=i+1%>" type="hidden" class="box" size="10" value="<%=idPersona%>" >
									<input name="fechaSuscripcion_<%=i+1%>" type="hidden" class="box" size="10" value="<%=letradoGuardia.getInscripcionTurno().getFechaSolicitud()%>" >
									<%=ncolegiado%>
								</td>
								<td><%=apellido1+" "+apellido2+", "+nombre%></td>
				
								<td>
									<% if (letradoGuardia.getInscripcionTurno().getFechaValidacion()!=null &&!letradoGuardia.getInscripcionTurno().getFechaValidacion().equals("")) { %>
										<%=letradoGuardia.getInscripcionTurno().getFechaValidacion()%>
									<% } else { %>
										&nbsp;
									<% } %>
								</td>
								<td>
									<% if (letradoGuardia.getInscripcionTurno().getFechaBaja()!=null &&!letradoGuardia.getInscripcionTurno().getFechaBaja().equals("")) { %>
										<%=letradoGuardia.getInscripcionTurno().getFechaBaja()%>
									<% } else { %>
										&nbsp;
									<% } %>
								</td>
								<td align="center">
									<img src="<%=app%>/html/imagenes/bcambiarusuario.gif" name = "bcambiarusuario" id="bcambiarusuario" style="cursor:hand;" onClick="fijarUltimoLetrado(<%=i+1%>)" alt="<%=literalFijarUltimoLetrado%>">
								</td>
							</tr>		
<%		
						} // for 
					} // else 
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
				
				<siga:Table
	    			name="tablaCompensaciones"
	    			border="1"
					columnSizes="22,50,28"			
					columnNames="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.compensaciones"
	    			fixedHeight="48%">

					<!-- INICIO: ZONA DE REGISTROS -->
					<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	
					Vector resultado = (Vector) request.getAttribute("vCompensaciones");
					if (resultado==null || resultado.size()==0) { 
%>			
	 					<tr class="notFound">
			   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
						</tr>	 		
<%	
					} else { 
						for (int i=0;i<resultado.size();i++) {
							Row registro = (Row) resultado.elementAt(i);
			
							// calculo de campos
							String apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
							String apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
							String nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
							String ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
							String numero = UtilidadesString.mostrarDatoJSP(registro.getString("NUMERO"));
%>
				  			<tr class="listaNonEdit">
								<td><%=ncolegiado%></td>
								<td><%=nombre+" "+apellido1+" "+apellido2%></td>
								<td><%=numero%></td>
							</tr>		
<%		
						} // for
					} // else 
%>			
				</siga:Table>	  
	  
<!-------------------------------------------------------------------------------------------------->	
<!---------- Saltos -------------------------------------------------------------------------------->	
<!-------------------------------------------------------------------------------------------------->	

				<table id="tituloTablaSaltos" width="100%" border="1" cellpadding="5" cellspacing="0">
			  		<tr class='tableTitle'>
						<td align='center' width='100%'><siga:Idioma key="gratuita.turnos.literal.saltos"/></td>
			  		</tr>
				</table>
				
				<siga:Table
				   	name="tablaSaltos"
				   	border="1"
				   	columnSizes="22,50,28"
				   	columnNames="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.saltos">				
		   
					<!-- INICIO: ZONA DE REGISTROS -->
					<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	
					Vector resultado = (Vector) request.getAttribute("vSaltos");
					if (resultado==null || resultado.size()==0) { 
%>			
	 					<tr class="notFound">
			   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
						</tr> 		
<%	
					} else { 
						for (int i=0;i<resultado.size();i++) {
							Row registro = (Row) resultado.elementAt(i);
			
							// calculo de campos
							String apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
							String apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
							String nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
							String ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
							String numero = UtilidadesString.mostrarDatoJSP(registro.getString("NUMERO"));
%>
				  			<tr class="listaNonEdit">
								<td><%=ncolegiado %></td>
								<td><%=nombre+" "+apellido1+" "+apellido2%></td>
								<td><%=numero%></td>
							</tr>		
<%		
						} // for
					} // else 
%>			
				</siga:Table>
  			</td>
		</tr>
	</table>
	
	<form name="auxForm" action="aux" target="aux" method="post">
		<input type=hidden name="modo">
	</form>

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
	<script>
  		function habilitarCambiarUsuario(valor){
  			if(valor)
				jQuery("#bcambiarusuario").attr("disabled","disabled");
			else
				jQuery("#bcambiarusuario").removeAttr("disabled");
		}
  		
		habilitarCambiarUsuario(document.getElementById('fechaConsulta').value=='');
  	</script>
</body>
</html>