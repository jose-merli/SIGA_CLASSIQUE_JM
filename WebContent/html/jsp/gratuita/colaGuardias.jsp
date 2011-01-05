<!-- colaGuardias.jsp -->
	 
 
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
<%@ taglib uri="c.tld" prefix="c"%>


<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>


<!-- JSP -->
<%
	String app = request.getContextPath();

	UsrBean usrbean = (UsrBean) session
			.getAttribute(ClsConstants.USERBEAN);
	String nListad = request.getAttribute("NLETRADOSINSCRITOS") != null
			? (String) request.getAttribute("NLETRADOSINSCRITOS")
			: "";

	String idGuardia = (String) request.getAttribute("idGuardia");
	String idInstitucion = (String) request
			.getAttribute("idInstitucion");
	String idTurno = (String) request.getAttribute("idTurno");

	String buscarLetrado = UtilidadesString.getMensajeIdioma(usrbean,
			"gratuita.turnos.literal.buscarLetrado");
	String literalNColegiado = UtilidadesString.getMensajeIdioma(
			usrbean, "gratuita.turnos.literal.nColegiado");
	String literalFijarUltimoLetrado = UtilidadesString
			.getMensajeIdioma(usrbean,
					"gratuita.turnos.literal.fijarUltimoLetrado");

	boolean porGrupos;
	String tamanoCol;
	String nombreCol;
	String grupos = (String) request.getAttribute("porGrupos");
	if (grupos != null && grupos.equalsIgnoreCase("1")) {
		porGrupos = true;
		tamanoCol = "5,6,6,16,28,16,15,8";
		nombreCol = " ,Gr,Or,gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,F.Val,F.Baja,";
	} else {
		porGrupos = false;
		tamanoCol = "16,38,18,18,10";
		nombreCol = "gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,F.Val,F.Baja,";
	}
%>	


<%@page import="com.siga.gratuita.util.calendarioSJCS.LetradoGuardia"%>

<!-- HEAD -->
<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<siga:Titulo titulo="pestana.justiciagratuitaturno.colaGuardia" localizacion="gratuita.turnos.localizacion.colaTurno.manteniento"/>
		
	<SCRIPT>
		function validaTabla(){
			  //Ajusto tablas de letrados en cola:
			  if (document.all.tablaLetrados.clientHeight < document.all.tablaLetradosDiv.clientHeight) {
				   document.all.tituloTablaLetrados.width='100%';
				   document.all.tablaLetradosCabeceras.width='100%';
			  } else {
				   document.all.tituloTablaLetrados.width='96.5%';
				   document.all.tablaLetradosCabeceras.width='96.5%';
			  }
		
			  //Ajusto tabla de compensaciones:
			  if (document.all.tablaCompensaciones.clientHeight < document.all.tablaCompensacionesDiv.clientHeight) {
				   document.all.tituloTablaCompensaciones.width='100%';
				   document.all.tablaCompensacionesCabeceras.width='100%';
			  } else {
				   document.all.tituloTablaCompensaciones.width='96.5%';
				   document.all.tablaCompensacionesCabeceras.width='96.5%';
			  }
			  
			  //Ajusto tabla de saltos:
			  if (document.all.tablaSaltos.clientHeight < document.all.tablaSaltosDiv.clientHeight) {
				   document.all.tituloTablaSaltos.width='100%';
				   document.all.tablaSaltosCabeceras.width='100%';
			  } else {
				   document.all.tituloTablaSaltos.width='96.5%';
				   document.all.tablaSaltosCabeceras.width='96.5%';
			  }	  		  
		}

		function refrescarLocal()
		{	
			document.location = document.location;
		}
			
		function buscarLetradoEnColaLetrado () 
		{
			s = document.getElementById("buscarLetrado").value;
			if (s) {
				var ele = document.getElementsByName ("numeroColegiadoBusqueda");
				for (i = 0; i < ele.length; i++) {
					var a = ele[i].value.split("_");
					if (a) {
						var fila = a[0];
						var nColegiado = a[1];
//						alert ("Texto: " + s + " Fila: " + fila + " nColegiado: " + nColegiado);
						if(nColegiado == s) {
							selectRowTablaLetrados(eval(fila)+1);
							return;
						}
					}
				}
			}
			selectRowTablaLetrados(-1);
		}
		
		function selectRowTablaLetrados(fila) 
		{
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

		function limpiarTexto(t, limpiar) 
		{
			if (limpiar == 1) {
				t.value= "";
			}
			else {
				if (!t.value) {
					t.value = "<%=literalNColegiado%>";
				}
			}
		}

	 function fijarUltimoLetrado(fila) 
	 {
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
			
			
	</script>
</head>

<body class="tablaCentralCampos" onload="validaTabla();">

 	<html:form action="/JGR_ColaGuardias" method="get" >
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="modo">
		<input type="hidden" name="tablaDatosDinamicosD">
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
<table border="0" style="table-layout:fixed;" cellpadding="2" cellspacing="2" width="99%" align="center">
  	<tr style="display:none">
	  <td width="59%" style="vertical-align: top"></td>
	  <td width="1%"  style="vertical-align: top"></td>
	  <td width="40%" style="vertical-align: top"></td>
	</tr>
  	<tr>
	  <td colspan="3" style="vertical-align: top">
	  	<siga:ConjCampos leyenda="gratuita.turnos.literal.ultimo">
		  <table border="0" align="center" width="100%">
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
  	<tr >
	  <td rowspan="2" style="vertical-align: top; height:450px">
	  
	  
<!-------------------------------------------------------------------------------------------------->	
<!---------- Letrados en Cola ---------------------------------------------------------------------->	
<!-------------------------------------------------------------------------------------------------->


		<table id="tituloTablaLetrados" border='1' width='100%'
			cellspacing='0' cellpadding='0' style="border-bottom: none">
			<tr class='tableTitle'>
				<%
					if (porGrupos) {
				%>
				<td class="tdBotones" width='17%'><input type="button"
					alt="Guardar" id="idButton" onclick="return accionGuardar();"
					class="button" name="idButton" value="Guardar"></td>
				<%
					}
				%>
				<td width='22%'>
					<input id="buscarLetrado" type="text" class="box" size="8" value="<%=literalNColegiado%>"
						onfocus="limpiarTexto(this, 1);" onblur="limpiarTexto(this, 0);buscarLetradoEnColaLetrado();">
					<img src="<%=app%>/html/imagenes/bconsultar_off.gif"
						style="cursor: hand;" onClick="buscarLetradoEnColaLetrado();" alt="<%=buscarLetrado%>">
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

		<siga:TablaCabecerasFijas
		   nombre="tablaLetrados"
		   borde="1"
		   clase="tableTitle"
		   tamanoCol="<%=tamanoCol%>"
		   nombreCol="<%=nombreCol%>"
		   alto="400"
		   ajusteAlto="">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
		

	<%
				ArrayList letradosColaGuardiaList = (ArrayList) request
							.getAttribute("letradosColaGuardiaList");
					if (letradosColaGuardiaList == null
							|| letradosColaGuardiaList.size() == 0) {
			%>			
	 		<tr>
	 		
			  <td colspan="4" height="225px">
	   		 	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			  </td>
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
	 						LetradoGuardia letradoGuardia = (LetradoGuardia) letradosColaGuardiaList
	 								.get(i);

	 						// calculo de campos
	 						apellido1 = letradoGuardia.getPersona().getApellido1();
	 						apellido2 = letradoGuardia.getPersona().getApellido2();
	 						nombre = letradoGuardia.getPersona().getNombre();
	 						ncolegiado = letradoGuardia.getPersona().getColegiado()
	 								.getNColegiado();
	 						idPersona = letradoGuardia.getIdPersona().toString();
	 						fechaSuscripcion = letradoGuardia.getInscripcionGuardia().getFechaSuscripcion();
	 						numeroColegiadoBusqueda = "" + i + "_" + ncolegiado;
	 						grupo = letradoGuardia.getNumeroGrupo() != null
	 								? letradoGuardia.getNumeroGrupo().toString()
	 								: "";
	 						if (porGrupos) {
	 							if (!grupo.equalsIgnoreCase(grupoAnt)) {
	 								nFila++;
	 								grupoAnt = grupo;
	 							}
	 						} else {
	 							nFila++;
	 						}
	 						ordenGrupo = letradoGuardia.getOrdenGrupo() != null
	 								? letradoGuardia.getOrdenGrupo().toString()
	 								: "";
	 						idGrupoGuardiaColegiado = letradoGuardia
	 								.getIdGrupoGuardiaColegiado() != null
	 								? letradoGuardia.getIdGrupoGuardiaColegiado()
	 										.toString()
	 								: "";
	 		%>
	
			<!-- REGISTRO  -->
  			<tr class="<%=((nFila + 1) % 2 == 0
								? "filaTablaPar"
								: "filaTablaImpar")%>">
  				<%
  					if (porGrupos) {
  				%>
  					<td><input type="checkbox" id="checkGrupoOrden" value="<%=i + 1%>" onclick="modificaParametro(this)"/></td>
					<td>
						<input type="text" value="<%=grupo%>" id="grupo_<%=i + 1%>" disabled size="1">
						<input type="hidden" value="<%=grupo%>" id="grupoOriginal_<%=i + 1%>" >
					</td>
					<td>
						<input type="text" value="<%=ordenGrupo%>" id="orden_<%=i + 1%>" disabled size="1">
						<input type="hidden" value="<%=ordenGrupo%>" id="ordenOriginal_<%=i + 1%>" >
					</td>
				<%
					}
				%>	
				<td>
					<input name="numeroColegiadoBusqueda" type="hidden" class="box" size="10" value="<%=numeroColegiadoBusqueda%>" >
					<input name="idPersona_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=idPersona%>" >
					<input name="fechaSuscripcion_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=fechaSuscripcion%>" >
					<input name="idGrupoGuardiaColegiado_<%=i + 1%>" type="hidden" class="box" size="10" value="<%=idGrupoGuardiaColegiado%>" >
					<%=ncolegiado%>
				</td>
				<td>
					<%=nombre + " " + apellido1 + " " + apellido2%>
				</td>
				<td>
				<%
					if (letradoGuardia.getInscripcionGuardia().getFechaValidacion() != null
						&& !letradoGuardia.getInscripcionGuardia().getFechaValidacion().equals("")) {
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
				<td>
				<%
					if (letradoGuardia.getInscripcionGuardia().getFechaBaja() != null
						&& !letradoGuardia.getInscripcionGuardia().getFechaBaja().equals("")) {
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
			
				<td align="center">
					<img src="<%=app%>/html/imagenes/bcambiarusuario.gif" name="bcambiarusuario" style="cursor:hand;" onClick="fijarUltimoLetrado(<%=i + 1%>)" alt="<%=literalFijarUltimoLetrado%>">
				</td>
			</tr>		
			<!-- FIN REGISTRO -->
<%
	} // del for
%>			
			<!-- FIN: ZONA DE REGISTROS -->
<%
	} // del if
%>			

		</siga:TablaCabecerasFijas>


	  
<!-------------------------------------------------------------------------------------------------->	
	  </td>

	  <td rowspan="2"><!--margen--></td>
	  <td style="vertical-align: top; height:220px">
	  
	  
<!-------------------------------------------------------------------------------------------------->	
<!---------- Compensaciones ------------------------------------------------------------------------>	
<!-------------------------------------------------------------------------------------------------->	
		<table id="tituloTablaCompensaciones" border='1' width='98.43%' cellspacing='0' cellpadding='0' style="border-bottom:none">
		  <tr class = 'tableTitle'>
			<td align='center' width='100%'><siga:Idioma key="gratuita.turnos.literal.compensaciones"/></td>
		  </tr>
		</table> 
		<siga:TablaCabecerasFijas
		   nombre="tablaCompensaciones"
		   borde="1"
		   clase="tableTitle"
		   tamanoCol="22,50,28"
		   nombreCol="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.compensaciones"
		   alto="170"
		   ajusteAlto="">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%
				Vector resultado = (Vector) request
							.getAttribute("vCompensaciones");
					if (resultado == null || resultado.size() == 0) {
			%>			
	 		<tr>
			  <td colspan="4" height="75px">
	   		 	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			  </td>
	 		</tr>	 		
<%
	 			} else {
	 					// recorro el resultado
	 					for (int i = 0; i < resultado.size(); i++) {
	 						Row registro = (Row) resultado.elementAt(i);

	 						// calculo de campos
	 						String apellido1 = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenPersonaBean.C_APELLIDOS1));
	 						String apellido2 = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenPersonaBean.C_APELLIDOS2));
	 						String nombre = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenPersonaBean.C_NOMBRE));
	 						String ncolegiado = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenColegiadoBean.C_NCOLEGIADO));
	 						String numero = UtilidadesString
	 								.mostrarDatoJSP(registro.getString("NUMERO"));
	 		%>
			<!-- REGISTRO  -->
  			<tr class="listaNonEdit">
				<td>
					<%=ncolegiado%>
				</td>
				<td>
					<%=nombre + " " + apellido1 + " " + apellido2%>
				</td>
				<td>
					<%=numero%>
				</td>
			</tr>		
			<!-- FIN REGISTRO -->
<%
	} // del for
%>			
			<!-- FIN: ZONA DE REGISTROS -->
<%
	} // del if
%>			

		</siga:TablaCabecerasFijas>
	  
<!-------------------------------------------------------------------------------------------------->	
	  </td>
	</tr>
  	<tr>
	  <td style="vertical-align: top; height:220px">
	  
	  
<!-------------------------------------------------------------------------------------------------->	
<!---------- Saltos -------------------------------------------------------------------------------->	
<!-------------------------------------------------------------------------------------------------->	
		<table id="tituloTablaSaltos" border='1' width='98.43%' cellspacing='0' cellpadding='0' style="border-bottom:none">
		  <tr class = 'tableTitle'>
			<td align='center' width='100%'><siga:Idioma key="gratuita.turnos.literal.saltos"/></td>
		  </tr>
		</table>
		<siga:TablaCabecerasFijas
		   nombre="tablaSaltos"
		   borde="1"
		   clase="tableTitle"
		   tamanoCol="22,50,28"
		   nombreCol="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.saltos"
		   alto="170"
		   ajusteAlto="">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%
				Vector resultado = (Vector) request.getAttribute("vSaltos");
					if (resultado == null || resultado.size() == 0) {
			%>			
	 		<tr>
			  <td colspan="4" height="75px">
	   		 	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			  </td>
	 		</tr>	 		
<%
	 			} else {
	 					// recorro el resultado
	 					for (int i = 0; i < resultado.size(); i++) {
	 						Row registro = (Row) resultado.elementAt(i);

	 						// calculo de campos
	 						String apellido1 = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenPersonaBean.C_APELLIDOS1));
	 						String apellido2 = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenPersonaBean.C_APELLIDOS2));
	 						String nombre = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenPersonaBean.C_NOMBRE));
	 						String ncolegiado = UtilidadesString
	 								.mostrarDatoJSP(registro
	 										.getString(CenColegiadoBean.C_NCOLEGIADO));
	 						String numero = UtilidadesString
	 								.mostrarDatoJSP(registro.getString("NUMERO"));
	 		%>
			<!-- REGISTRO  -->
  			<tr class="listaNonEdit">
				<td>
					<%=ncolegiado%>
				</td>
				<td>
					<%=nombre + " " + apellido1 + " " + apellido2%>
				</td>
				<td>
					<%=numero%>
				</td>
			</tr>		
			<!-- FIN REGISTRO -->
<%
	} // del for
%>			
			<!-- FIN: ZONA DE REGISTROS -->
<%
	} // del if
%>			

		</siga:TablaCabecerasFijas>


<!-------------------------------------------------------------------------------------------------->	
	  </td>
  	</tr>
  </table>
  
  <iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
  <script>
  	function habilitarCambiarUsuario(valor){
  		var bcambiarusuario =document.getElementsByName("bcambiarusuario");
		for (i=0;i<bcambiarusuario.length;i++) {
			bcambiarusuario[i].disabled=valor;
		}
	}
	habilitarCambiarUsuario(document.getElementById('fechaConsulta').value=='');

	function modificaParametro(o){
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


	function accionGuardar(){
		var datos = "";
		var ele = document.getElementsByName("checkGrupoOrden");
		for (i = 0; i < ele.length; i++) {
			if (ele[i].checked) {
				if ((document.getElementById("grupo_" + ele[i].value).value.length < 1)||(document.getElementById("orden_" + ele[i].value).value.length < 1)){
					alert ("<siga:Idioma key="administracion.parametrosGenerales.error.valorParametro"/> " + document.getElementById("oculto" + ele[i].value + "_3").value);
					return;
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
