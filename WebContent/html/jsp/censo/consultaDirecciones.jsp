<!-- consultaDirecciones.jsp -->
<!-- Historico modificaciones:
		miguel.villegas: implementacion boton volver -->		

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.CenDireccionTipoDireccionBean"%>
<%@ page import="com.siga.beans.CenTipoDireccionBean"%>
<%@ page import="com.siga.beans.CenDireccionesBean"%>
<%@ page import="com.siga.beans.CenPoblacionesBean"%>
<%@ page import="com.siga.beans.CenProvinciaBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.lang.String.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idUsr=Long.toString(usr.getIdPersona());
	//String idUsr="20321016";	
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	Vector vDatos=(Vector)request.getAttribute("vDatos");	
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	String botones="N"; 
	String accion = String.valueOf(request.getAttribute("accion"));	
	String sTipo = request.getParameter("tipo");
	
	// Gestion de Volver
	String busquedaVolver = (String)
	request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver == null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botones="V,N";
	}	else{
		botones="";
	}
	
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getAttribute("bIncluirRegistrosConBajaLogica"));	
%>	
	
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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
    document.consultaDireccionesForm.modo.value = "solicitarModificacion";

    ventaModalGeneral(document.forms[0].name,"G");
 }
 
		function accionNuevo() {		
		  document.consultaDireccionesForm.modo.value = "nuevo";
  	  var rc = ventaModalGeneral(document.consultaDireccionesForm.name, "G");
  	  if (rc != null) { 
  	 	 	if (rc == "MODIFICADO") {
  	 	 		refrescarLocal();
  	  	}
  	  }
		}
		
		function refrescarLocal() {
		
			//document.location.reload();
			document.consultaDireccionesForm.modo.value = "abrir";
			document.consultaDireccionesForm.submit();
		}

		function incluirRegBajaLogica(o)
		{
			if (o.checked) {
				document.consultaDireccionesForm.incluirRegistrosConBajaLogica.value = "s";
			}
			else {
				document.consultaDireccionesForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.consultaDireccionesForm.modo.value = "abrir";
			
			document.consultaDireccionesForm.submit();
		}
		
	</script>
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
		<% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.datosDirecciones.cabecera"
			localizacion="censo.fichaLetrado.localizacion"/>
		<%}else{%>
		 <siga:TituloExt 
			titulo="censo.fichaCliente.datosDirecciones.cabecera" 
			localizacion="censo.fichaCliente.datosDirecciones.localizacion"/>
		<%}%>
		
		<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">
	
		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
    <table class="tablaTitulo" align="center" cellspacing=0>
		<html:form method="post" action="/CEN_ConsultasDirecciones.do">
		
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = ""/>
			<input type="hidden" name="nombreUsuario" value= "<%=(String)request.getAttribute("nombrePersona")%>"/>
			<input type="hidden" name="numeroUsuario" value= "<%=(String)request.getAttribute("numero")%>"/>
			<input type='hidden' name="idPersona" 		value= "<%=String.valueOf((Long)request.getAttribute("idPersona"))%>"/>	
			<input type='hidden' name="idInstitucion" value= "<%=String.valueOf((Integer)request.getAttribute("idInstitucion"))%>"/>
			<input type='hidden' name="accion" 				value= "<%=String.valueOf(request.getAttribute("accion"))%>">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
		</html:form>
		
	<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="censo.consultaDirecciones.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
		    <%if(!numero.equalsIgnoreCase("")){%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
			<%} 
			else {%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
			<%}%>
		</td>
	</tr>
	</table>	

		<siga:TablaCabecerasFijas 
  			nombre="tablaDatos"
   			borde="1"
   			estilo=""
   			clase="tableTitle"
   			nombreCol=",censo.consultaDirecciones.literal.tipoDireccion,censo.consultaDirecciones.literal.direccion,censo.datosDireccion.literal.cp,censo.consultaDirecciones.literal.poblacion,censo.consultaDirecciones.literal.telefono1,censo.datosDireccion.literal.fax1,censo.datosDireccion.literal.movil,censo.consultaDirecciones.literal.correo,censo.consultaDirecciones.literal.preferente,"
			tamanoCol="10,13,5,12,8,8,8,16,7,14" 
  			alto = "360"
  			ajuste="70"
  			modal="G">
 	<%	 		
 		if(vDatos == null || vDatos.size()<1 )
 			{ 	
	 %>
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
	 <%		
	 		}
	 	else
	 		{	 
 			Enumeration en = vDatos.elements();		
 			int i = 0;
 			while(en.hasMoreElements()){
				Hashtable htData = (Hashtable)en.nextElement();
				if (htData == null) continue;
				i++;
				FilaExtElement[] elems=new FilaExtElement[1];
				if((String.valueOf((Long)request.getAttribute("idPersona"))).equals(idUsr)  && usr.isLetrado()){		 
  		 			elems[0]=new FilaExtElement("solicitar","solicitar",SIGAConstants.ACCESS_READ);  	
				}
				String poblacion=(String)htData.get("POBLACION");
				String poblacionExtranjera=(String)htData.get("POBLACIONEXTRANJERA");
				String poblacionFinal=(poblacion==""||poblacion==null)?poblacionExtranjera:poblacion;
				String descripcionTipoDir=" ";
				
				if (htData.get(CenTipoDireccionBean.T_NOMBRETABLA + "." + CenTipoDireccionBean.C_DESCRIPCION)!=null && !((String)htData.get(CenTipoDireccionBean.T_NOMBRETABLA + "." + CenTipoDireccionBean.C_DESCRIPCION)).equals("")  ){
				  descripcionTipoDir=((String)htData.get(CenTipoDireccionBean.T_NOMBRETABLA + "." + CenTipoDireccionBean.C_DESCRIPCION)).replaceAll(",","");
				}
				

				// Dato Preferente				
				boolean coma = false;
				String preferente = "";
				String aux = (String)htData.get(CenDireccionesBean.C_PREFERENTE);
				if (aux != null) {
					if (aux.indexOf("C") >= 0) {
						preferente += UtilidadesString.getMensajeIdioma(usr,"censo.preferente.correo");
						coma = true;
					}
					if (aux.indexOf("E") >= 0) {
						if (coma) preferente += ", ";
						preferente += UtilidadesString.getMensajeIdioma(usr,"censo.preferente.mail");
						coma = true;
					}
					if (aux.indexOf("F") >= 0) {
						if (coma) preferente += ", ";
						preferente += UtilidadesString.getMensajeIdioma(usr,"censo.preferente.fax");
						coma = true; 
					}
					if (aux.indexOf("S") >= 0) {
						if (coma) preferente += ", ";
						preferente += UtilidadesString.getMensajeIdioma(usr,"censo.preferente.sms");
					}
				}
				
				String iconos = "C";
				String f = (String)htData.get(CenDireccionesBean.C_FECHABAJA);
				if ((f == null) || (f.equals(""))) {
					iconos += ",E,B";
				}
	%> 						
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=iconos%>' modo = '<%=accion%>' elementos='<%=elems%>' clase="listaNonEdit">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(CenDireccionesBean.C_IDDIRECCION)%>'>
						<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(CenTipoDireccionBean.C_IDTIPODIRECCION)%>'>
						<input type='hidden' name='oculto<%=String.valueOf(i)%>_3' value='<%=descripcionTipoDir%>'>
						<%=UtilidadesString.mostrarDatoJSP(htData.get(CenTipoDireccionBean.T_NOMBRETABLA + "." + CenTipoDireccionBean.C_DESCRIPCION))%>
  				</td>
  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenDireccionesBean.C_DOMICILIO))%></td>
  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenDireccionesBean.C_CODIGOPOSTAL))%></td>
  				<td><%=UtilidadesString.mostrarDatoJSP(poblacionFinal)%></td>
  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenDireccionesBean.C_TELEFONO1))%></td>
  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenDireccionesBean.C_FAX1))%></td>
  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenDireccionesBean.C_MOVIL))%></td>
  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenDireccionesBean.C_CORREOELECTRONICO))%></td>
  				<td><%=UtilidadesString.mostrarDatoJSP(preferente)%></td> 
  			   										
				</siga:FilaConIconos>
 <%		}
 	}%>  			
  			</siga:TablaCabecerasFijas>   			
  			  
<% if (!usr.isLetrado()){%>
	<div style="position:absolute; left:400px;bottom:35px;z-index:2;">
		<table align="center" border="0">
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
					
					<% if (bIncluirBajaLogica) { %>
						<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked>
					<% } else { %>
						<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);">
					<% } %>
				</td>
			</tr>
		</table>
	</div>
<%}%>	
  			  
	<siga:ConjBotonesAccion botones="<%=botones%>" modo="<%=accion%>" clase="botonesDetalle"/>

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
	</body>
</html>
