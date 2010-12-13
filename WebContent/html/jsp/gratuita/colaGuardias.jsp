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
	String app=request.getContextPath();

	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String nListad =request.getAttribute("NLETRADOSINSCRITOS") != null?(String)request.getAttribute("NLETRADOSINSCRITOS"):"";
	

	String idGuardia = (String)request.getAttribute("idGuardia");

	String buscarLetrado             = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.buscarLetrado");
	String literalNColegiado         = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.nColegiado");
	String literalFijarUltimoLetrado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.turnos.literal.fijarUltimoLetrado");
	
	boolean porGrupos;
	String tamanoCol;
	String nombreCol;
	String grupos = (String)request.getAttribute("porGrupos");
	if(grupos!=null && grupos.equalsIgnoreCase("1")){
		porGrupos=true;
		tamanoCol="14,34,16,16,6,6,8";
		nombreCol="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,F.Val,F.Baja,Gr,Or,";
	}else{
		porGrupos=false;
		tamanoCol="14,40,18,18,10";
		nombreCol="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,F.Val,F.Baja,";
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
	 	
		var idPersona  = document.getElementById('idPersona_' + fila).value;
		document.forms[0].idPersona.value = idPersona;
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
		<input type="hidden" name="idGuardia" value="<%=idGuardia%>" >
		<input type="hidden" name="idGrupoGuardiaColegiado">
		<html:hidden property="fechaConsulta"/>
	</html:form>	

		
<!-------------------------------------------------------------------------------------------------->	
<!---------- Ultimo letrado ------------------------------------------------------------------------>	
<!-------------------------------------------------------------------------------------------------->	
<table border="0" style="table-layout:fixed;" cellpadding="2" cellspacing="2" width="99%" align="center">
  	<tr style="display:none">
	  <td width="46%" style="vertical-align: top"></td>
	  <td width="1%"  style="vertical-align: top"></td>
	  <td width="51%" style="vertical-align: top"></td>
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
    

		<table id="tituloTablaLetrados" border='1' width='98.43%' cellspacing='0' cellpadding='0' style="border-bottom:none">
		  <tr class = 'tableTitle'>
			<td align='center' width='69%'>
				<siga:Idioma key="gratuita.colaGuardia.literal.letradosInscritos"/>:&nbsp;&nbsp;<%=nListad%>
			</td>
			<td align='center' width='31%'>
				<input id="buscarLetrado" type="text" class="box" size="10" value="<%=literalNColegiado%>" onfocus="limpiarTexto(this, 1);" onblur="limpiarTexto(this, 0);buscarLetradoEnColaLetrado();">
				&nbsp;
				<img src="/SIGA/html/imagenes/bconsultar_off.gif" style="cursor:hand;" onClick="buscarLetradoEnColaLetrado();" alt="<%=buscarLetrado%>" >
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
	ArrayList letradosColaGuardiaList = (ArrayList) request.getAttribute("letradosColaGuardiaList");
	if (letradosColaGuardiaList==null || letradosColaGuardiaList.size()==0) { %>			
	 		<tr>
	 		
			  <td colspan="4" height="225px">
	   		 	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			  </td>
	 		</tr>	 		
<%	} else { 
		// recorro el resultado
		for (int i=0;i<letradosColaGuardiaList.size();i++) {
			LetradoGuardia letradoGuardia = (LetradoGuardia) letradosColaGuardiaList.get(i);
			
			// calculo de campos
			String apellido1 = letradoGuardia.getPersona().getApellido1();
			String apellido2 =letradoGuardia.getPersona().getApellido2();
			String nombre = letradoGuardia.getPersona().getNombre();
			String ncolegiado = letradoGuardia.getPersona().getColegiado().getNColegiado();

			String idPersona = letradoGuardia.getIdPersona().toString();
			String numeroColegiadoBusqueda = "" + i + "_" + ncolegiado;
			String grupo = letradoGuardia.getGrupo()!=null?letradoGuardia.getGrupo().toString():"";
			String ordenGrupo = letradoGuardia.getOrdenGrupo()!=null?letradoGuardia.getOrdenGrupo().toString():"";
			String idGrupoGuardiaColegiado = letradoGuardia.getIdGrupoGuardiaColegiado()!=null?letradoGuardia.getIdGrupoGuardiaColegiado().toString():"";
			
	%>
	
			<!-- REGISTRO  -->
  			<tr class="listaNonEdit">
				<td>
					<input name="numeroColegiadoBusqueda" type="hidden" class="box" size="10" value="<%=numeroColegiadoBusqueda%>" >
					<input name="idPersona_<%=i+1%>" type="hidden" class="box" size="10" value="<%=idPersona%>" >
					<input name="idGrupoGuardiaColegiado_<%=i+1%>" type="hidden" class="box" size="10" value="<%=idGrupoGuardiaColegiado%>" >
					<%=ncolegiado%>
				</td>
				<td>
					<%=nombre+" "+apellido1+" "+apellido2%>
				</td>
				<td>
				<%if(letradoGuardia.getFechaValidacion()!=null &&!letradoGuardia.getFechaValidacion().equals("")){ %>
					<%=letradoGuardia.getFechaValidacion()%>
				<%}else{ %>
					&nbsp;
				<%} %>
				</td>
				<td>
				<%if(letradoGuardia.getFechaBaja()!=null &&!letradoGuardia.getFechaBaja().equals("")){ %>
					<%=letradoGuardia.getFechaBaja()%>
				<%}else{ %>
					&nbsp;
				<%} %>

				</td>
				<%if(porGrupos){%>
					<td><%=grupo%></td>
					<td><%=ordenGrupo%></td>
				<%}%>				
				<td align="center">
					<img src="/SIGA/html/imagenes/bcambiarusuario.gif" name="bcambiarusuario" style="cursor:hand;" onClick="fijarUltimoLetrado(<%=i+1%>)" alt="<%=literalFijarUltimoLetrado%>">
				</td>
			</tr>		
			<!-- FIN REGISTRO -->
<%		} // del for %>			
			<!-- FIN: ZONA DE REGISTROS -->
<%	} // del if %>			

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
		   tamanoCol="18,20,40,22" 
		   nombreCol="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.apellidosSolo,gratuita.turnos.literal.compensaciones"
		   alto="170"
		   ajusteAlto="">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	Vector resultado = (Vector) request.getAttribute("vCompensaciones");
	if (resultado==null || resultado.size()==0) { %>			
	 		<tr>
			  <td colspan="4" height="75px">
	   		 	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			  </td>
	 		</tr>	 		
<%	} else { 
		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {
			Row registro = (Row) resultado.elementAt(i);
			
			// calculo de campos
			String apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
			String apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
			String ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
			String numero = UtilidadesString.mostrarDatoJSP(registro.getString("NUMERO"));
%>
			<!-- REGISTRO  -->
  			<tr class="listaNonEdit">
				<td>
					<%=ncolegiado%>
				</td>
				<td>
					<%=nombre%>
				</td>
				<td>
					<%=apellido1+" "+apellido2 %>
				</td>
				<td>
					<%=numero%>
				</td>
			</tr>		
			<!-- FIN REGISTRO -->
<%		} // del for %>			
			<!-- FIN: ZONA DE REGISTROS -->
<%	} // del if %>			

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
		   tamanoCol="18,20,40,22"
		   nombreCol="gratuita.turnos.literal.nColegiado,gratuita.turnos.literal.nombreSolo,gratuita.turnos.literal.apellidosSolo,gratuita.turnos.literal.saltos"
		   alto="170"
		   ajusteAlto="">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	Vector resultado = (Vector) request.getAttribute("vSaltos");
	if (resultado==null || resultado.size()==0) { %>			
	 		<tr>
			  <td colspan="4" height="75px">
	   		 	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			  </td>
	 		</tr>	 		
<%	} else { 
		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {
			Row registro = (Row) resultado.elementAt(i);
			
			// calculo de campos
			String apellido1 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS1));
			String apellido2 = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_APELLIDOS2));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.getString(CenPersonaBean.C_NOMBRE));
			String ncolegiado = UtilidadesString.mostrarDatoJSP(registro.getString(CenColegiadoBean.C_NCOLEGIADO));
			String numero = UtilidadesString.mostrarDatoJSP(registro.getString("NUMERO"));
%>
			<!-- REGISTRO  -->
  			<tr class="listaNonEdit">
				<td>
					<%=ncolegiado %>
				</td>
				<td>
					<%=nombre%>
				</td>
				<td>
					<%=apellido1+" "+apellido2 %>
				</td>
				<td>
					<%=numero%>
				</td>
			</tr>		
			<!-- FIN REGISTRO -->
<%		} // del for %>			
			<!-- FIN: ZONA DE REGISTROS -->
<%	} // del if %>			

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
  </script>
  
</body>
</html>
