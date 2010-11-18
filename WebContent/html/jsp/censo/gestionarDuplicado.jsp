<!-- SolicitudIncorporacionDatos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudInstituBean" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="utils.system"%>
<%@page import="java.util.Vector"%>
<%@page import="com.atos.utils.Row"%>
<%@ page import="com.siga.censo.form.SolicitudIncorporacionForm" %>

<%

	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	Hashtable datos = (Hashtable)request.getAttribute("datos");
	
	CenPersonaBean p1 = (CenPersonaBean)datos.get("persona0");
	CenPersonaBean p2 = (CenPersonaBean)datos.get("persona1");
	String idPersona1 = p1.getIdPersona().toString();
	String idPersona2 = p2.getIdPersona().toString();
	
	String fechaNac1="";
	String fechaNac2="";
	if(p1.getFechaNacimiento()!=null&&!p1.getFechaNacimiento().equalsIgnoreCase("")){
		fechaNac1=UtilidadesString.formatoFecha( p1.getFechaNacimiento(),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy");
	}
	if(p2.getFechaNacimiento()!=null&&!p2.getFechaNacimiento().equalsIgnoreCase("")){
		fechaNac2=UtilidadesString.formatoFecha( p2.getFechaNacimiento(),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy");
	}
	
	CenColegiadoBean c1 = (CenColegiadoBean)datos.get("colegiacion0");
	CenColegiadoBean c2 = (CenColegiadoBean)datos.get("colegiacion1");
	
	String inst1="2000";
	String inst2="2000";
	if(c1!=null&&c1.getIdInstitucion()!=null){
		inst1=String.valueOf(c1.getIdInstitucion());
	}
	if(c2!=null&&c2.getIdInstitucion()!=null){
		inst2=String.valueOf(c2.getIdInstitucion());
	}
	
	String fechaInc1="";
	String fechaInc2="";
	String nombreInstitucion1="";
	String nombreInstitucion2="";
	if(c1!=null&&c1.getFechaIncorporacion()!=null&&!c1.getFechaIncorporacion().equalsIgnoreCase("")){
		fechaInc1=UtilidadesString.formatoFecha( c1.getFechaIncorporacion(),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy");
		nombreInstitucion1=(String)datos.get("nombreInstitucion0");
	}
	if(c2!=null&&c2.getFechaIncorporacion()!=null&&!c2.getFechaIncorporacion().equalsIgnoreCase("")){
		fechaInc2=UtilidadesString.formatoFecha( c2.getFechaIncorporacion(),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy");
		nombreInstitucion2=(String)datos.get("nombreInstitucion1");
	}
	
	Vector vDirecciones = (Vector)datos.get("direcciones0");
	vDirecciones.addAll((Vector)datos.get("direcciones1"));
	
	Row e1 = (Row)datos.get("estado0");
	Row e2 = (Row)datos.get("estado1");
	
	Vector colegiaciones1 = (Vector)datos.get("colegiaciones0");
	Vector colegiaciones2 = (Vector)datos.get("colegiaciones1");
	
	boolean incluido1en2=false;
	boolean incluido2en1=false;
	boolean incluido=false;
	if(colegiaciones1.contains(Integer.valueOf(inst2))){
		incluido1en2=true;
	}
	if(colegiaciones2.contains(Integer.valueOf(inst1))){
		incluido2en1=true;
	}

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

	<style>
		.ocultar {display:none}
	</style>	


<html>

<head>
	<html:javascript formName="SolicitudIncorporacionForm" staticJavascript="false" />  
	
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	<title><siga:Idioma key="censo.SolicitudIncorporacionDatos.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.solicitudIncorporacion.cabecera3" 
							 localizacion="censo.solicitudIncorporacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script language="JavaScript">

		// jbd // helpers para recorrer radios y checks
		var incluido;
		var seleccionado = false;
		
		function getSelectedRadio(buttonGroup) {
		   // returns the array number of the selected radio button or -1 if no button is selected
		   if (buttonGroup[0]) { // if the button group is an array (one button is not an array)
		      for (var i=0; i<buttonGroup.length; i++) {
		         if (buttonGroup[i].checked) {
		            return i
		         }
		      }
		   } else {
		      if (buttonGroup.checked) { return 0; } // if the one button is checked, return zero
		   }
		   // if we get to this point, no radio button is selected
		   return -1;
		}
		
		function getSelectedRadioValue(buttonGroup) {
		   // returns the value of the selected radio button or "" if no button is selected
		   var i = getSelectedRadio(buttonGroup);
		   if (i == -1) {
		      return "";
		   } else {
		      if (buttonGroup[i]) { // Make sure the button group is an array (not just one button)
		         return buttonGroup[i].value;
		      } else { // The button group is just the one button, and it is checked
		         return buttonGroup.value;
		      }
		   }
		}
		
		function getSelectedCheckbox(buttonGroup) {
		   // Go through all the check boxes. return an array of all the ones
		   // that are selected (their position numbers). if no boxes were checked,
		   // returned array will be empty (length will be zero)
		   var retArr = new Array();
		   var lastElement = 0;
		   if (buttonGroup[0]) { // if the button group is an array (one check box is not an array)
		      for (var i=0; i<buttonGroup.length; i++) {
		         if (buttonGroup[i].checked && buttonGroup[i].disabled==false) {
		            retArr.length = lastElement;
		            retArr[lastElement] = i;
		            lastElement++;
		         }
		      }
		   } else { // There is only one check box (it's not an array)
		      if (buttonGroup.checked && buttonGroup.disabled==false) { // if the one check box is checked
		         retArr.length = lastElement;
		         retArr[lastElement] = 0; // return zero as the only array value
		      }
		   }
		   return retArr;
		}

		function getSelectedCheckboxValue(buttonGroup) {
		   // return an array of values selected in the check box group. if no boxes
		   // were checked, returned array will be empty (length will be zero)
		   var retArr = new Array(); // set up empty array for the return values
		   var selectedItems = getSelectedCheckbox(buttonGroup);
		   if (selectedItems.length != 0) { // if there was something selected
		      retArr.length = selectedItems.length;
		      for (var i=0; i<selectedItems.length; i++) {
		         if (buttonGroup[selectedItems[i]]) { // Make sure it's an array
		            retArr[i] = buttonGroup[selectedItems[i]].value;
		         } else { // It's not an array (there's just one check box and it's selected)
		            retArr[i] = buttonGroup.value;// return that value
		         }
		      }
		   }
		   return retArr;
		}

		function seleccionarChecks(buttonGroup, id){
			if (buttonGroup[0]) { // Cogemos el grupo de checks
			      for (var i=0; i<buttonGroup.length; i++) {
				      if(buttonGroup[i].id==id){
				      		buttonGroup[i].checked=true;
				      		buttonGroup[i].disabled=true;
				      }else{
				    	  	buttonGroup[i].checked=false;
				    	  	buttonGroup[i].disabled=false;
				      }
			      }
		   } else {
			   if(buttonGroup.id==id){
		      		buttonGroup.checked=true;
		      		buttonGroup.disabled=true;
		      }else{
		    	  	buttonGroup.checked=false;
		    	  	buttonGroup.disabled=false;
		      }
		   }
		}
		// jbd // fin helpers

			
		function accionAceptar(){
			if(incluido){
				alert("<siga:Idioma key="censo.fusionDuplicados.error.existeColegiacion"/>");
			}else if (seleccionado){
				<%if(inst1.equalsIgnoreCase(inst2)){%>
					alert("<siga:Idioma key="censo.fusionDuplicados.error.mismaInstitucion"/>");
				<%}else if(idPersona1.equalsIgnoreCase(idPersona2)){%>
					alert("<siga:Idioma key="censo.fusionDuplicados.error.mismaPersona"/>");
				<%}else if(incluido){%>
				<%}else{%>
					sub();
					if(document.forms[0].checkDireccion){
						var direcciones = getSelectedCheckboxValue(document.forms[0].checkDireccion);
						document.forms[0].listaDirecciones.value = direcciones;
					}
					document.forms[0].modo.value = "aceptar";
					document.forms[0].target="submitArea";	
				 	document.forms[0].submit();
				 <%}%>
			}else{
				alert("<siga:Idioma key="censo.fusionDuplicados.error.seleccionePersona"/>");
			}
		}

		function seleccionar(perso, idD, idO, idInstO){
			document.forms[0].idPersonaDestino.value = idD;
			document.forms[0].idPersonaOrigen.value = idO;
			document.forms[0].idInstOrigen.value = idInstO;
			if(document.forms[0].checkDireccion!=null){
				seleccionarChecks(document.forms[0].checkDireccion, idD);
			}
			if(perso==1){
				incluido=<%=incluido1en2%>;
				document.forms[0].radioCol[0].checked=true;
				document.forms[0].radioCol[1].checked=false;
			}else{
				incluido=<%=incluido2en1%>;
				document.forms[0].radioCol[1].checked=true;
				document.forms[0].radioCol[0].checked=false;
			}
			seleccionado=true;
			/*if(document.forms[0].checkColegiacion!=null){
				seleccionarChecks(document.forms[0].checkColegiacion, id);
			}*/
		}


		
	</script>


</head>

<body onload="ajusteAlto('divScroll')" class="tablaCentralCampos" >



	<html:form action="/CEN_MantenimientoDuplicados.do" method="POST" target="submitArea">
	<input type="hidden" name="modo" value="">
	<html:hidden property = "idPersonaOrigen" value = ""/>
	<html:hidden property = "idPersonaDestino" value = ""/>
	<html:hidden property = "idInstOrigen" value = ""/>
	<html:hidden property = "listaDirecciones" value=""/>
	
	<table class="tablaTitulo" align="center" width="100%">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="censo.fusionDuplicados.explicacion"/>
			</td>
		</tr>
		<tr>
			<td class="titulitosDatos">
				<img src="/SIGA/html/imagenes/blopd_disable.gif" align="middle" border="0" >
				<siga:Idioma key="censo.fusionDuplicados.advertencia"/>
				<img src="/SIGA/html/imagenes/blopd_disable.gif" align="middle" border="0" >
			</td>
		</tr>
	</table>

	<table width="100%" height="140">
		<tr>
		<td>
			<table  width="100%">
			<tr>
			<td class="labelText"  width="100%">
			
			<siga:ConjCampos leyenda="censo.fusionDuplicados.datosPersonales.titulo">
				<table> 
					<tr>
						<td class="labelText" style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.datosPersonales.identificador"/></td>
						<td class="labelTextValue" style="text-align:right;"><%=p1.getIdPersona()%></td>
						<td class="labelTextValue"><%=p2.getIdPersona()%></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align:right;">NIF</td>
						<td class="labelTextValue" style="text-align:right;"><%=p1.getNIFCIF()%></td>
						<td class="labelTextValue"><%=p2.getNIFCIF()%></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align:right;">Nombre | Apellido 1 | Apellido 2</td>
						<td class="labelTextValue" style="text-align:right;"><%=p1.getNombre()%> | <%=p1.getApellido1()%> | <%=p1.getApellido2()%></td>
						<td class="labelTextValue"><%=p2.getNombre()%> | <%=p2.getApellido1()%> | <%=p2.getApellido2()%></td>
					</tr>
					<tr>
						<td class="labelText" style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.datosPersonales.fechaNacimiento"/>|<siga:Idioma key="censo.fusionDuplicados.datosPersonales.lugarNacimiento"/></td>
						<td class="labelTextValue" style="text-align:right;"><%=fechaNac1%> | <%=p1.getNaturalDe()%></td>
						<td class="labelTextValue"><%=fechaNac2%> | <%=p2.getNaturalDe()%></td>
					</tr>

					<tr style="border-top:1px solid">
						<td class="labelText" style="text-align:right;"></td>
						<!-- Aqui se meten los radio buttons para seleccionar el colegiado base -->
						<td class="labelTextValue" style="text-align:center;"><input name="idPersonaSel" value="<%=p1.getIdPersona() %>" type="radio" onclick="seleccionar(1,<%=p1.getIdPersona() %>,<%=p2.getIdPersona() %>,<%=inst2 %>)"/> </td>
						<td class="labelTextValue" style="text-align:center;"><input name="idPersonaSel" value="<%=p2.getIdPersona() %>" type="radio" onclick="seleccionar(2,<%=p2.getIdPersona() %>,<%=p1.getIdPersona() %>,<%=inst1 %>)"/></td>
					</tr>
				</table>
			</siga:ConjCampos>
		</td>
		</tr>
		</table>
		</td>
		</tr>
		</table>
<div name="divScroll" style="overflow:auto;height:450;">
	<table width="100%">
			<tr>
			<td>
				<table  width="100%">
				<tr>
				<td class="labelText"  width="100%">
				<siga:ConjCampos leyenda="censo.fusionDuplicados.colegiaciones.titulo">
					<br>
						<siga:Idioma key="censo.fusionDuplicados.colegiaciones.explicacion"/>
					<br>
					<table>
					<%String estilo ="filaTablaImpar";
					  if(c1!=null){%>
					  	<tr class="<%=estilo %>">
							<td style="text-align:right;"><input name="radioCol" type="radio" disabled="true"/><%=c1.getIdPersona()!=null?c1.getIdPersona():""%></td>
							<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.inscrito"/></td>
							<td><%=c1.getComunitario().equals("0")?"No":"Si"%></td>	
							<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.numeroColegiado"/></td>
							<td><%=c1.getNColegiado()!=null?c1.getNColegiado():""%><%=c1.getNComunitario()!=null?c1.getNComunitario():""%></td>
							<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.fechaIncorporacion"/></td>
							<td><%=fechaInc1%></td>
						</tr>
						<%if(e1!=null){%>
							<tr class="<%=estilo %>">
								<!-- Este check se deberia cargar solo cuando se seleccione el radio del colegiado -->
								<td style="text-align:right;"><%=nombreInstitucion1%></td>	
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.estado"/></td>
								<td><%=e1.getString("DESCRIPCION")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.fechaEstado"/></td>
								<td><%=UtilidadesString.formatoFecha( e1.getString("FECHAESTADO"),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.observaciones"/></td>
								<td><%=e1.getString("OBSERVACIONES")!=null?e1.getString("OBSERVACIONES"):""%></td>
							</tr>
							<%}else{%>
							<tr class="<%=estilo %>">
								<td></td>
								<td class="labelTextValue" colspan="6">
								<siga:Idioma key="censo.fusionDuplicados.error.faltaEstado"/>
								</td>
							</tr>
								
						<%}%>
						<tr >
							<td>&nbsp;</td>
						</tr>
						<%}else{%>
							<input name="radioCol" type="radio" disabled="true" style="visibility:hidden"/>
						<%}
						estilo ="filaTablaPar";
						if(c2!=null){%>
						<tr class="<%=estilo %>">
							<td style="text-align:right;"><input name="radioCol" type="radio" disabled="true"/><%=c2.getIdPersona()!=null?c2.getIdPersona():""%></td>
							<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.inscrito"/></td>
							<td><%=c2.getComunitario().equals("0")?"No":"Si"%></td>	
							<td style="text-align:right;">NºColegiado:</td>
							<td><%=c2.getNColegiado()%><%=c2.getNComunitario()%></td>
							<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.fechaIncorporacion"/></td>
							<td><%=fechaInc2%></td>
						</tr>
						<%if(e2!=null){%>
							<tr class="<%=estilo %>">
								<!-- Este check se deberia cargar solo cuando se seleccione el radio del colegiado -->
								<td style="text-align:right;"><%=nombreInstitucion2%>&nbsp;</td>	
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.estado"/></td>
								<td ><%=e2.getString("DESCRIPCION")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.fechaEstado"/></td>
								<td><%=UtilidadesString.formatoFecha( e2.getString("FECHAESTADO"),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.colegiaciones.observaciones"/></td>
								<td><%=e2.getString("OBSERVACIONES")!=null?e2.getString("OBSERVACIONES"):""%></td>
							</tr>
							<%}else{%>
							<tr class="<%=estilo %>">
								<td></td>
								<td class="labelTextValue" colspan="6">
								<siga:Idioma key="censo.fusionDuplicados.error.faltaEstado"/>
								</td>
							</tr>
						<%}%>
						<%}else{%>
							<input name="radioCol" type="radio" disabled="true" style="visibility:hidden"/>
						<%}%>
						<tr >
							<td>&nbsp;</td>
						</tr>
					</table>
				</siga:ConjCampos>
				
				<siga:ConjCampos leyenda="censo.fusionDuplicados.direcciones.cabecera">
					<br>
					<siga:Idioma key="censo.fusionDuplicados.direcciones.explicacion"/>
					<br>
					<table>
					<%String estilo ="";
					if(vDirecciones.size()>0){
						  for (int i=0; i<vDirecciones.size(); i++){
							estilo =(i+1)%2==0?"filaTablaPar":"filaTablaImpar";
						  	Hashtable reg = (Hashtable)vDirecciones.get(i);%>
							<tr class="<%=estilo %>">
								<td style="text-align:right;"><%=reg.get("IDPERSONA")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.fechaModificacion"/></td>
								<td><%=UtilidadesString.formatoFecha( (String)reg.get("FECHAMODIFICACION"),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy")%></td>	
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.domicilio"/></td>
								<td colspan="3"><%=reg.get("DOMICILIO")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.poblacion"/></td>
								<td><%=reg.get("POBLACION")%><%=reg.get("POBLACIONEXTRANJERA")%></td>
							</tr>
							<tr class="<%=estilo %>">
								<td style="text-align:right;"><input name="checkDireccion" id="<%=reg.get("IDPERSONA")%>" value="<%=reg.get("IDINSTITUCION")+"&&"+reg.get("IDPERSONA")+"&&"+reg.get("IDDIRECCION")%>" type="checkBox"/></td>	
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.codigoPostal"/></td>
								<td><%=reg.get("CODIGOPOSTAL")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.provincia"/></td>
								<td colspan="3"><%=reg.get("PROVINCIA")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.pais"/></td>
								<td><%=reg.get("PAIS")%></td>
							</tr>
							<tr class="<%=estilo%>">
								<td></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.telefono"/></td>
								<td><%=reg.get("TELEFONO1")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.movil"/></td>
								<td><%=reg.get("MOVIL")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.fax1"/></td>
								<td><%=reg.get("FAX1")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.fax2"/></td>
								<td ><%=reg.get("FAX2")%></td>
							</tr>
							<tr class="<%=estilo%>">
								<td></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.correo"/></td>
								<td><%=reg.get("CORREOELECTRONICO")%></td>
								<td style="text-align:right;"><siga:Idioma key="censo.fusionDuplicados.direcciones.tipo"/></td>
								<td colspan="7"><%=reg.get("CEN_TIPODIRECCION.DESCRIPCION")%></td>
								
							</tr>
							<tr >
								<td>&nbsp;</td>
							</tr>
						<%}%>
					<%}else{%>
						<tr >
							<td class="labelText" textalign="center"><siga:Idioma key="censo.fusionDuplicados.error.noDatos"/></td>
						</tr>
					<%}%>
					</table>
				</siga:ConjCampos>
				
				</td>
			</tr>
		</table>
		</td>
		</tr>
	</table>
	<!-- TABLA -->

	


	</html:form>
</div>
		<siga:ConjBotonesAccion botones="A,C" clase="botonesDetalle" />
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		function accionCerrar(){	
			//window.close();
			top.cierraConParametros("NOMODIFICADO");
		}
	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
</body>
</html>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

