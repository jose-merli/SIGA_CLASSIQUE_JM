<!-- consultaDatosBancarios.jsp -->
<!-- Historico modificaciones:
		miguel.villegas: implementacion boton volver	1-2-2005
-->
		
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
<%@ page import="com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import="com.siga.beans.CenComponentesBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idUsr=Long.toString(usr.getIdPersona());
//	String idUsr="2032123474";
	String institucion=String.valueOf((Integer)request.getAttribute("idInstitucion"));
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	String idPersona=String.valueOf((Long)request.getAttribute("idPersona"));
	Vector vDatos=(Vector)request.getAttribute("vDatos");		
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;
	String botones="N"; 
	String accion = (String)request.getAttribute("accion");
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getAttribute("bIncluirRegistrosConBajaLogica"));	
	String sTipo = request.getParameter("tipo");

	// Gestion de Volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botones="V,N";
	}
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
    document.cuentasBancariasForm.modo.value = "solicitarModificacion";

    ventaModalGeneral(document.cuentasBancariasForm.name,"M");   
	 }

		function accionNuevo() {		
		  document.cuentasBancariasForm.modo.value = "nuevo";
  	  var rc = ventaModalGeneral(document.cuentasBancariasForm.name, "M");
  	  if (rc != null) { 
  	 	 	if (rc == "MODIFICADO") {
  	 	 		refrescarLocal();
  	  	}
  	  }
		}

		function refrescarLocal() {
            document.cuentasBancariasForm.modo.value = "abrir";
			document.cuentasBancariasForm.submit();
		}
		
		function incluirRegBajaLogica(o)
		{
			if (o.checked) {
				document.cuentasBancariasForm.incluirRegistrosConBajaLogica.value = "s";
			}
			else {
				document.cuentasBancariasForm.incluirRegistrosConBajaLogica.value = "n";
			}
			
			document.cuentasBancariasForm.modo.value = "abrir";
			document.cuentasBancariasForm.submit();
			
			
			
		}
		
	</script>
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
		<% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.bancos.cabecera"
			localizacion="censo.fichaLetrado.localizacion"/>
		<%}else{%>
		 <siga:TituloExt 
			titulo="censo.fichaCliente.bancos.cabecera" 
			localizacion="censo.fichaCliente.bancos.localizacion"/>
		<%}%>
		
		<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">

		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
	    <table class="tablaTitulo" align="center" cellspacing=0>
		<!-- Formulario de la lista de detalle multiregistro -->
		<form name="cuentasBancariasForm" method="post" action="<%=app%>/CEN_CuentasBancarias.do">
		
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = ""/>
			<input type="hidden" name="nombreUsuario" value= "<%=nombre%>"/>
			<input type="hidden" name="numeroUsuario" value= "<%=numero%>"/>
			<input type='hidden' name="idPersona" 		value= "<%=idPersona%>"/>	
			<input type='hidden' name="idInstitucion" value= "<%=institucion%>"/>
			<input type='hidden' name="accion" 				value= "<%=String.valueOf(request.getAttribute("accion"))%>">

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
		</form>
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
	    <%if(!numero.equalsIgnoreCase("")){%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
			<%} 
			else {%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
			<%}%>
		</td>
		</tr>
		</table>	
		
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

		<siga:TablaCabecerasFijas 
		   	nombre="tablaDatos"
		   	borde="1"
   			estilo=""
		   	clase="tableTitle"
		  	nombreCol=",censo.consultaDatosBancarios.literal.titular,censo.consultaDatosBancarios.literal.tipoCuenta,censo.datosCuentaBancaria.literal.abonoSJCS,censo.consultaDatosBancarios.literal.sociedad,censo.consultaDatosBancarios.literal.cuenta,censo.consultaDatosBancarios.literal.fechaBaja,"
		  	tamanoCol="30,9,9,9,19,8,10"
		    alto="100%"
		    ajuste="70"		
		    modal="M"> 
 	
 	<%	if(vDatos == null || vDatos.size()<1 ) { %>
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
	 <%		
	 		}
	 	else
	 		{	 
 			Enumeration en = vDatos.elements();	
 			int i=0;  	
			 											
			while(en.hasMoreElements()){
				Hashtable htData = (Hashtable)en.nextElement();
				if (htData == null) continue;
				String accionBanco = "";
				i++;
				FilaExtElement[] elems = new FilaExtElement[1];
  			    if(((String)htData.get(CenCuentasBancariasBean.C_IDPERSONA)).equals(idPersona) ){
					accionBanco = accion;
 				}
 				else {
 					    accionBanco = "ver";		
				}
				if((idPersona.equals(idUsr)) && (((String)htData.get(CenCuentasBancariasBean.C_IDPERSONA)).equals(idPersona))  && usr.isLetrado()){			 
  		 			elems[0]=new FilaExtElement("solicitar","solicitar",SIGAConstants.ACCESS_READ);  	
				}
				
		    String sociedad = "";
				String sociedadLiteral = "";
				if(((String)htData.get(CenCuentasBancariasBean.C_IDPERSONA)).equals(idPersona)){	
					sociedad = DB_FALSE;
					sociedadLiteral = UtilidadesString.getMensajeIdioma(usr,"general.no");							   	 				
				}
				else{		   	 					
					sociedad = DB_TRUE;
					sociedadLiteral = UtilidadesString.getMensajeIdioma(usr,"general.yes");
				} 

				String tipoCuenta = "";
				if(((String)htData.get(CenCuentasBancariasBean.C_ABONOCARGO)).equals(ClsConstants.TIPO_CUENTA_ABONO)){	
					tipoCuenta = UtilidadesString.getMensajeIdioma(usr,"censo.tipoCuenta.abono");		
				}else if(((String)htData.get(CenCuentasBancariasBean.C_ABONOCARGO)).equals(ClsConstants.TIPO_CUENTA_ABONO_CARGO)){
					tipoCuenta = UtilidadesString.getMensajeIdioma(usr,"censo.tipoCuenta.abonoCargo");		
				}else if(((String)htData.get(CenCuentasBancariasBean.C_ABONOCARGO)).equals(ClsConstants.TIPO_CUENTA_CARGO)){
					tipoCuenta = UtilidadesString.getMensajeIdioma(usr,"censo.tipoCuenta.cargo");			   	 				
				} 
				
				String abonosjcs = "";
 				if(((String)htData.get(CenCuentasBancariasBean.C_ABONOSJCS)).equals(DB_FALSE)){			   	 				
 					abonosjcs = UtilidadesString.getMensajeIdioma(usr,"general.no");
 				}
 				else{
 					abonosjcs = UtilidadesString.getMensajeIdioma(usr,"general.yes");							   	 				
	 			} 

				String numeroCuentaCompleto = htData.get(CenCuentasBancariasBean.C_CBO_CODIGO) 		 + " - " + 
																			htData.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL) + " - " +  
																			htData.get(CenCuentasBancariasBean.C_DIGITOCONTROL)  + " - " + 
																			UtilidadesString.mostrarNumeroCuentaConAsteriscos((String)htData.get(CenCuentasBancariasBean.C_NUMEROCUENTA));

				String fechaBaja = (String)htData.get(CenCuentasBancariasBean.C_FECHABAJA);
				
				if (fechaBaja == null || "".equals(fechaBaja)){
					fechaBaja = "";	
				}
				else{
					fechaBaja = UtilidadesString.formatoFecha(fechaBaja,"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy");
				}
				
				String iconos = "C";
				String f = (String)htData.get(CenCuentasBancariasBean.C_FECHABAJA);
				if ((f == null) || (f.equals(""))) {
					iconos += ",E,B";
				}
	%> 			
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=iconos%>' elementos='<%=elems%>' modo = "<%=accionBanco%>" clase="listaNonEdit">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(CenCuentasBancariasBean.C_IDCUENTA)%>'>
						<input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=htData.get(CenCuentasBancariasBean.C_IDPERSONA)%>'>
						<input type='hidden' name='oculto<%=String.valueOf(i)%>_3' value='<%=htData.get(CenCuentasBancariasBean.C_IDINSTITUCION)%>'>
						<input type='hidden' name='oculto<%=String.valueOf(i)%>_4' value='<%=sociedad%>'>
						<%=UtilidadesString.mostrarDatoJSP(htData.get(CenCuentasBancariasBean.C_TITULAR))%>
					</td>
					<td><%=UtilidadesString.mostrarDatoJSP(tipoCuenta)%></td> 
					<td><%=UtilidadesString.mostrarDatoJSP(abonosjcs)%></td> 
					<td><%=UtilidadesString.mostrarDatoJSP(sociedadLiteral)%></td> 
					<td><%=UtilidadesString.mostrarDatoJSP(numeroCuentaCompleto)%></td> 
					<td><%=UtilidadesString.mostrarDatoJSP(fechaBaja)%></td> 
					
					
				</siga:FilaConIconos>
 <%		}
 	} // While %>  			
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

	<siga:ConjBotonesAccion botones="<%=botones%>" modo = "<%=accion%>" clase="botonesDetalle"/>		
		
<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<!-- FIN: BOTONES BUSQUEDA -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
