<!DOCTYPE html>
<html>
<head>
<!-- consultaDatosCV.jsp -->
<!-- Historico modificaciones:
		miguel.villegas: implementacion boton volver -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.CenDatosCVBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idUsr=Long.toString(usr.getIdPersona());
	//String idUsr="20321016";	
	String acceso=usr.getAccessType();
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	Vector vDatos=(Vector)request.getAttribute("vDatos");		
	String estadoColegial=(String)request.getAttribute("estadoColegial");
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;
	String botones="N";
	String accion = String.valueOf(request.getAttribute("accion"));
	String sTipo = request.getParameter("tipo");
	
	// Gestion de Volver
	String busquedaVolver = (String)
	request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botones="V,N";
	}

	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getAttribute("bIncluirRegistrosConBajaLogica"));
%>

<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

<!-- SCRIPTS LOCALES -->
<script language="JavaScript">
		function solicitar(fila) {
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var j;
				var tabla;
				tabla = document.getElementById('tablaDatos');
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
		    document.datosCVForm.modo.value = "solicitarModificacion";
		   	ventaModalGeneral(document.forms[0].name,"M");	   
		 }
		 
		function accionSolicitarNuevo(){
			document.datosCVForm.modo.value = "solicitarNuevo";	
   	  		var rc = ventaModalGeneral(document.forms[0].name, "M");
  	 	 	if (rc == "MODIFICADO") {
  	 	 		refrescarLocal();
  	  		}
		}
		 
		function accionNuevo() {		
		  document.datosCVForm.modo.value = "nuevo";
	  	  var rc = ventaModalGeneral(document.datosCVForm.name, "M");
  		  if (rc != null) { 
  	 	 	if (rc == "MODIFICADO") {
  	 	 		refrescarLocal();
  	  		}
	  	  }
		}
		
		function refrescarLocal() {
			//document.location.reload();
			document.datosCVForm.modo.value = "abrir";
			document.datosCVForm.submit();
		}

		function incluirRegBajaLogica(o)
		{
			if (o.checked) {
				document.datosCVForm.incluirRegistrosConBajaLogica.value = "s";
			}
			else {
				document.datosCVForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.datosCVForm.modo.value = "abrir";
			document.datosCVForm.submit();
		}
		

	</script>
<!-- INICIO: TITULO Y LOCALIZACION -->
<!-- Escribe el título y localización en la barra de título del frame principal -->

<% if (sTipo!=null && sTipo.equals("LETRADO")){%>
<siga:Titulo titulo="censo.fichaCliente.datosCV.cabecera"
	localizacion="censo.fichaLetrado.localizacion" />
<%}else{%>
<siga:TituloExt titulo="censo.fichaCliente.datosCV.cabecera"
	localizacion="censo.fichaCliente.datosCV.localizacion" />
<%}%>

<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">

	<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
	<table class="tablaTitulo" align="center" cellspacing=0>
		<html:form method="post" action="/CEN_DatosCV.do" styleId="datosCVForm">
			<!-- Campo obligatorio -->
			<html:hidden property="modo"  styleId="modo" value="" />
			<input type="hidden" id="nombreUsuario"  name="nombreUsuario" value="<%=nombre%>" />
			<input type="hidden" id="numeroUsuario"  name="numeroUsuario" value="<%=numero%>" />
			<input type='hidden' id="idPersona"  name="idPersona" value="<%=String.valueOf((Long)request.getAttribute("idPersona"))%>" />
			<input type='hidden' id="idInstitucion"  name="idInstitucion" value="<%=String.valueOf((Integer)request.getAttribute("idInstitucion"))%>" />
			<input type='hidden' id="accion"  name="accion" value="<%=accion%>" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" id="incluirRegistrosConBajaLogica"  name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
		</html:form>

		<tr>
			<td class="titulitosDatos"><siga:Idioma
					key="censo.consultaDatosCV.literal.titulo1" /> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>
				&nbsp;&nbsp; <%if(!numero.equalsIgnoreCase("")){%> <%if (estadoColegial!=null && !estadoColegial.equals("")){%>
				<siga:Idioma key="censo.fichaCliente.literal.colegiado" /> <%= UtilidadesString.mostrarDatoJSP(numero)  %>
				&nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>) <%}else{%>
				(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial" />)
				<%}%> <%} 
			else {%> <siga:Idioma key="censo.fichaCliente.literal.NoColegiado" />
				<%}%></td>
		</tr>
	</table>

	<siga:Table 
		name="tablaDatos" 
		border="1" 
		columnNames="censo.busquedaClientes.literal.institucion,censo.consultaDatosCV.literal.tipo,,,censo.consultaDatosCV.literal.fechaInicio,censo.consultaDatosCV.literal.fechaFin,censo.consultaDatosCV.literal.descripcion,censo.consultaDatosCV.literal.verificado,"
		columnSizes="10,10,10,10,10,10,20,8,13"
		modal="M">

		<%		if((String.valueOf((Long)request.getAttribute("idPersona"))).equals(idUsr) &&
				usr.isLetrado()){
					botones +=",SN";  
					
					}					
		%>
		<% if(vDatos == null || vDatos.size()<1 )	{ %>
		<tr class="notFound">
   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
		</tr>
		<%		
	 	}
	 	else
	 	{	 
 			Enumeration en = vDatos.elements();		
 			int i=0;  									
			while(en.hasMoreElements()){
				Hashtable htData = (Hashtable)en.nextElement();
				if (htData == null) continue;
				i++;
						
				FilaExtElement[] elems=new FilaExtElement[1];
				if((String.valueOf((Long)request.getAttribute("idPersona"))).equals(idUsr) && usr.isLetrado()){
					if((((String)htData.get(CenDatosCVBean.C_CERTIFICADO)).equals(DB_FALSE))){
	  		 			elems[0]=new FilaExtElement("solicitar","solicitar",SIGAConstants.ACCESS_READ);
	  		 		} 
				}
				
				String certificado="";
 				if(((String)htData.get(CenDatosCVBean.C_CERTIFICADO)).equals(DB_FALSE)){			   	 				
 	 					certificado = UtilidadesString.getMensajeIdioma(usr,"general.no");
 				}
 				else{
 	 					certificado = UtilidadesString.getMensajeIdioma(usr,"general.yes");							   	 				
 	 			} 

				String iconos = "C";
				String f = (String)htData.get(CenDatosCVBean.C_FECHABAJA);
				if ((f == null) || (f.equals(""))) {
					iconos += ",E,B";
				}
				String idInstitucion = usr.getLocation();
				String institucion = CenVisibilidad.getAbreviaturaInstitucion(idInstitucion);
				if(htData.get("IDINSTITUCIONCARGO")!=null && !htData.get("IDINSTITUCIONCARGO").toString().trim().equals(""))
					institucion = CenVisibilidad.getAbreviaturaInstitucion(htData.get("IDINSTITUCIONCARGO").toString());

	%>
		<siga:FilaConIconos fila='<%=String.valueOf(i)%>'
			botones='<%=iconos%>' elementos='<%=elems%>' modo='<%=accion%>'
			clase="listaNonEdit">
			<td><%=UtilidadesString.mostrarDatoJSP(institucion)%></td>
			<td>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(CenDatosCVBean.C_IDCV)%>'> 
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_2' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(CenDatosCVBean.C_IDINSTITUCIONCARGO)%>'>
				<%=UtilidadesString.mostrarDatoJSP(htData.get("TIPOAPUNTE"))%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(htData.get("DESCSUBTIPO1"))%></td>
			<td><%=UtilidadesString.mostrarDatoJSP(htData.get("DESCSUBTIPO2"))%></td>
			<td><%=UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("",htData.get(CenDatosCVBean.C_FECHAINICIO)))%></td>
			<td><%=UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("",htData.get(CenDatosCVBean.C_FECHAFIN)))%></td>
			<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenDatosCVBean.C_DESCRIPCION))%></td>
			<td><%=UtilidadesString.mostrarDatoJSP(certificado)%></td>
		</siga:FilaConIconos>

		<%		}
 	}%>

	</siga:Table>
	<% if (!usr.isLetrado()){%>
	<div style="position: absolute; left: 400px; bottom: 5px; z-index: 99;">
		<table align="center" border="0">
			<tr>
				<td class="labelText"><siga:Idioma
						key="censo.consultaRegistrosBajaLogica.literal" /> <% if (bIncluirBajaLogica) { %>
					<input type="checkbox" name="bajaLogica"
					onclick="incluirRegBajaLogica(this);" checked> <% } else { %>
					<input type="checkbox" name="bajaLogica"
					onclick="incluirRegBajaLogica(this);"> <% } %></td>
			</tr>
		</table>
	</div>
	<%}%>

	<%
	
	%>
	<siga:ConjBotonesAccion botones="<%=botones%>" modo='<%=accion%>'
		clase="botonesDetalle" />

	<%@ include file="/html/jsp/censo/includeVolver.jspf"%>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
