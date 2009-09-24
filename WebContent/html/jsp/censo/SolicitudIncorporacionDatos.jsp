<!-- SolicitudIncorporacionDatos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri	=	"struts-tiles.tld" 			prefix="tiles" 		%>
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri = "struts-html.tld" 			prefix="html" 		%>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"			%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CenSolicitudIncorporacionBean" %>
<%@ page import="com.siga.Utilidades.UtilidadesString" %>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudInstituBean" %>
<%@ page import="com.atos.utils.ClsConstants" %>
<%@ page import="java.util.ArrayList" %>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");

	Vector documentos = (Vector) request.getAttribute("datosDocumentacion");
	CenSolicitudIncorporacionBean datosPersonales = (CenSolicitudIncorporacionBean) request.getAttribute("datosPersonales");
	ArrayList idEstadoSolicitud = null;

	String  tipoSolicitud   = (String) request.getAttribute("TipoSolicitud");
	String  tipoColegiacion = (String) request.getAttribute("TipoColegiacion");
	String  tipoTratamiento = (String) request.getAttribute("TipoTratamiento");
	String  tipoEstadoCivil = (String) request.getAttribute("TipoEstadoCivil");
	String  provincia       = (String) request.getAttribute("Provincia");
	String  poblacion       = (String) request.getAttribute("Poblacion");
	String  pais  = (String) request.getAttribute("Pais");
	String  estadoSolicitud = (String) request.getAttribute("EstadoSolicitud");
	String  editar			= (String) request.getAttribute("Editar");
	String  modoAnterior	= (String) request.getAttribute("ModoAnterior");
	String  modalidadDocumentacion = (String) request.getAttribute("ModalidadDocumentacion");
	
	if (tipoSolicitud   == null) tipoSolicitud = "";
	if (tipoColegiacion == null) tipoColegiacion = "";
	if (tipoTratamiento == null) tipoTratamiento = "";
	if (tipoEstadoCivil == null) tipoEstadoCivil = "";
	if (provincia       == null) provincia = "";
	if (poblacion       == null) poblacion = "";
	if (pais       == null) pais = "";
	if (estadoSolicitud == null) estadoSolicitud = "";
	if (editar          == null) editar = "false"; 
	else {
		idEstadoSolicitud = new ArrayList();
		idEstadoSolicitud.add(datosPersonales.getIdEstado().toString());
	}
	
	
	String sexo  = datosPersonales.getSexo();
	String ssexo = "";
	if (sexo.equals(ClsConstants.TIPO_SEXO_HOMBRE)) 
		ssexo = com.siga.Utilidades.UtilidadesString.getMensajeIdioma(user, "censo.sexo.hombre");
	else 
		ssexo = com.siga.Utilidades.UtilidadesString.getMensajeIdioma(user, "censo.sexo.mujer");

	String altoTabla = "90";		
	if (editar!=null && editar.equalsIgnoreCase("true"))
		altoTabla = "85";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><siga:Idioma key="censo.SolicitudIncorporacionDatos.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.solicitudIncorporacion.cabecera3" 
							 localizacion="censo.solicitudIncorporacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body  class="tablaCentralCampos">


<table align="center" width="100%">
<tr>
	<td class="labelText" >

					<center>
							<siga:Idioma key="messages.censo.solicitudIncorporacion.guardarCodigo"/>
							:&nbsp;
							<siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.numeroSolicitud"/>:
							&nbsp;&nbsp;
							<% if(datosPersonales.getIdSolicitud()!=null)
									out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getIdSolicitud()));
							%>
					</center>

	</td>
</tr>
</table>


	<siga:ConjCampos leyenda="censo.SolicitudIncorporacionDatos.titulo">

	<table width="100%" border="0" cellspacing="0" cellpadding="0">

	<html:form action="/CEN_SolicitudesIncorporacion.do" method="POST">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "editarIdSolicitud" value = "<%=datosPersonales.getIdSolicitud().toString()%>"/>
		<html:hidden property = "continuarAprobacion" value = ""/>

		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.solicitudDe"/></td>
			<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(tipoSolicitud)%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.tipoColegiacion"/></td>
			<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(tipoColegiacion)%></td>
			
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaSolicitud"/></td>
			<td class="labelTextValor"><%if(datosPersonales.getFechaSolicitud()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getFechaSolicitud()));%></td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.nColegiado"/></td>
			<td class="labelTextValor"><%if(datosPersonales.getNColegiado()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getNColegiado()));%></td>
			
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nifcif"/></td>
			<td class="labelTextValor" colspan="3"><%if(datosPersonales.getNumeroIdentificador()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getNumeroIdentificador()));%></td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/></td>
			<td class="labelTextValor"><%if(datosPersonales.getSexo()!=null)out.print(UtilidadesString.mostrarDatoJSP(ssexo));%></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.tratamiento"/></td>
			<td class="labelTextValor" colspan="3"><%=UtilidadesString.mostrarDatoJSP(tipoTratamiento)%></td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getNombre()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getNombre()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido1"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getApellido1()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getApellido1()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido2"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getApellido2()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getApellido2()));%></td>
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaNacimiento"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getFechaNacimiento()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getFechaNacimiento()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.estadoCivil"/></td>
			<td class="labelTextValor" ><%=UtilidadesString.mostrarDatoJSP(tipoEstadoCivil)%></td>

			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.naturalDe"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getNaturalDe()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getNaturalDe()));%></td>
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.datosDireccion.literal.pais2"/></td>
			<td class="labelTextValor" ><%=UtilidadesString.mostrarDatoJSP(pais)%></td>

			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/></td>
			<td class="labelTextValor" ><%=UtilidadesString.mostrarDatoJSP(provincia)%></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/></td>
			<td class="labelTextValor" ><%=UtilidadesString.mostrarDatoJSP(poblacion)%></td>
		<tr>
		</tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.domicilio"/></td>
			<td colspan="3" class="labelTextValor"><%if(datosPersonales.getDomicilio()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getDomicilio()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.codigoPostal"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getCodigoPostal()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getCodigoPostal()));%></td>
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono1"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getTelefono1()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getTelefono1()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono2"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getTelefono2()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getTelefono2()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono3"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getMovil()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getMovil()));%></td>
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax1"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getFax1()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getFax1()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax2"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getFax2()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getFax2()));%></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getCorreoElectronico()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getCorreoElectronico()));%></td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.observaciones"/></td>
			<td class="labelTextValor" colspan="3"><label id="observaciones"><%if(datosPersonales.getObservaciones()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getObservaciones()));%></label></td>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.documentacion"/></td>
			<td class="labelTextValor" ><%=UtilidadesString.mostrarDatoJSP(modalidadDocumentacion)%></td>
		</tr>
		
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.estado"/></td>
			<%if (editar.equalsIgnoreCase("false") || datosPersonales.getIdEstado().intValue()==ClsConstants.ESTADO_SOLICITUD_APROBADA) {%>
			<td class="labelTextValor" >
				<%=UtilidadesString.mostrarDatoJSP(estadoSolicitud)%>
				<html:hidden property="editarEstadoSolicitud" value="<%=datosPersonales.getIdEstado().toString()%>"  />
			</td>
			<%} else {%>
			<td >
				<siga:ComboBD nombre = "editarEstadoSolicitud" tipo="estadoSolicitud" clase="boxCombo" elementoSel="<%=idEstadoSolicitud%>" obligatorioSinTextoSeleccionar="true" />
			</td>
			<%}%>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.fechaEstado"/></td>
			<td class="labelTextValor" ><%if(datosPersonales.getFechaEstado()!=null)out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getFechaEstado()));%></td>
		</tr>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

	</table>
	
	</siga:ConjCampos>
		
	

	<siga:TablaCabecerasFijas nombre="documentoAPresentar" 
			borde="1" 
			estilo="" 
			clase="tableTitle" 
			nombreCol="censo.SolicitudIncorporacionDatos.literal.estado,censo.SolicitudIncorporacionDatos.literal.documento" 
			tamanoCol="10,90"
			alto = "<%=altoTabla%>"
			ajusteBotonera="true"
			>
			
		<% if (documentos != null) {
			 for (int i = 0; i < documentos.size(); i++){ 
			 		Vector v = (Vector) documentos.get(i);
			 		if (v != null) {
			 	  	CenDocumentacionSolicitudInstituBean documento = (CenDocumentacionSolicitudInstituBean) v.get(0); 
						String estado = (String) v.get(1);
		%>
		   <tr class="listaNonEdit">
		   		<td align="center">
		   			<input type="hidden" id="oculto<%=(i+1)%>_1" value="<%=documento.getDocumentacionSolicitud().getIdDocumentacion()%>">
		   			<input type="checkbox" <%if (editar.equalsIgnoreCase("false")) { out.print(" disabled "); } %><%if(estado.equalsIgnoreCase("true"))out.print("checked");%>>
		   		</td>
		   		<td>
		   			<%if(documento.getDocumentacionSolicitud().getDescripcion()!=null)out.print(UtilidadesString.mostrarDatoJSP(documento.getDocumentacionSolicitud().getDescripcion()));%>
		   		</td>
		   </tr>
	  <%	       
	  				}
	       	 }
	       } else {
	  %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" >
	   		 	<!-- PENDIENTE No tiene documentos adjuntos. Pendiente de Validar-->
	   		 	<siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.aviso"/>
	   		 </p>
	 		<br>
	  <%	       
	       }
	  %>
	</siga:TablaCabecerasFijas>





	<%if (editar!=null && editar.equalsIgnoreCase("true")) {%>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<siga:ConjBotonesAccion botones="Y,C,R" clase="botonesDetalle" />


	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
				document.SolicitudIncorporacionForm.reset();
		}
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();
			if(document.forms[0].editarEstadoSolicitud.value==<%=ClsConstants.ESTADO_SOLICITUD_APROBADA%>)
			{
				alert('<siga:Idioma key="censo.SolicitudIncorporacionDatos.aviso.cuentaBancariaSuscripcionAutomatica"/>');
			}

		   var datos;
		   var size=0;
		   <% if (documentos!=null) { %>
		   		size=<%=documentos.size() %>;
		   <% } %>
					 
		   if (size>0) {  
			   datos = document.getElementById('tablaDatosDinamicosD');
			   datos.value = ""; 
			   var j, fila;
			   for (fila = 1; fila < size+1; fila++) {
			      var tabla;
			      tabla = document.getElementById('documentoAPresentar');
		        var flag = true;
	    	    j = 1;
		        while (flag) {
		          var aux = 'oculto' + fila + '_' + j;
	    	      var oculto = document.getElementById(aux);
	        	  if (oculto == null)  { flag = false; }
		          else { datos.value = datos.value + oculto.value + ','; }
		          j++;
		        }
		        datos.value = datos.value + "%"
	    	    datos.value = datos.value + (tabla.rows[fila].cells)[0].all[j-2].checked + ',';
	        	datos.value = datos.value + "#"
			   }
			}
			
			document.SolicitudIncorporacionForm.modo.value = "Modificar";
	 		document.SolicitudIncorporacionForm.target = "submitArea";
			document.SolicitudIncorporacionForm.submit();
		}
		
		</script>
	<%} else 
		if (modoAnterior.equalsIgnoreCase("VER")) {%>
			<siga:ConjBotonesAccion botones="C" clase="botonesDetalle" />
		<%}
		else {%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
			
			<script>
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				window.location = "<%=app%>/html/jsp/censo/SolicitudIncorporacionValidacion.jsp";
			}
		</script>

		<%}%>
		
		<script>
			function accionCerrar() 
			{		
				window.close();	
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

