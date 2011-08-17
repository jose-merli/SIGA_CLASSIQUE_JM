<!-- retencionesIRPF.jsp -->
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
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants" %>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%
	Vector vFechaInicio = new Vector();
	Vector vFechaFin = new Vector();
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = new Vector();
	obj = (Vector) request.getAttribute("resultado");
	String sociedad = (String) request.getAttribute("SOCIEDAD");
	String idSociedadLetradoSel = (String) request.getAttribute("idSociedadLetradoSel");
	boolean desactivado = false;
	String DB_TRUE = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;
	String idpersonacol=(String) request.getSession().getAttribute("idPersonaTurno");
	ArrayList idBanco = new ArrayList();
	String clave= ((String)request.getAttribute("idCuenta"))+"*"+idSociedadLetradoSel;
	idBanco.add (clave);
	//idBanco.add ((String) request.getAttribute("idCuenta"));
	ArrayList comboSocSel = new ArrayList();
	comboSocSel.add(idSociedadLetradoSel);

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver == null) || (usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}

	// parametros para la query del combo
	String letrado = "\'" + UtilidadesString.getMensajeIdioma(usr.getLanguage(),"gratuita.retenciones.letrado") + "\'";
	String parametros[] = {(String) usr.getLocation(),(String) request.getSession().getAttribute("idPersonaTurno"), letrado};
%>

<%@page import="java.util.Vector"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<html>
	<head>
	<title><siga:Idioma key="gratuita.retencionesIRPF.literal.title"/></title>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

<script>
	// Creamos un array con las fechas inicio y fin, para ver que los rangos introducidos, no se solapan.

	function refrescarLocal()
	{
		
			document.forms[0].action		= "<%=app%>/JGR_PestanaRetencionesIRPF.do";
			document.forms[0].modo.value	= "buscarPor";			
			document.forms[0].submit();
		
	}

	function refrescarLocalArray(arrayDatos) {
		document.forms[0].action		= "<%=app%>/JGR_PestanaRetencionesIRPF.do";
		document.forms[0].modo.value	= "buscarPor";
		document.forms[0].submit();
		// tambien refresco
		//refrescarLocal();
	}

	function accionNuevo()
	{
		document.forms[0].action		= "<%=app%>/JGR_PestanaRetencionesIRPF.do";
		document.forms[0].modo.value	= "nuevo";		
   		var resultado = ventaModalGeneral(document.forms[0].name,"P");
	    if(resultado == "MODIFICADO") 
	    	refrescarLocal();
	   
	}
	
	function accionGuardar()
	{

		
		//var idPersonaSociedadActual = document.forms[0].sociedadesCliente.value;
		
		//if(idPersonaSociedadActual == idPersonaSociedadInicial)
		//{
		///	alert("Debe cambiar a quien va dirigido el pago");
		//}
		//else
		//{
		//	document.forms[0].action		= "<%=app%>/JGR_PestanaRetencionesIRPF.do";
		//	document.forms[0].modo.value	= "guardarPagoPor";
		//	document.forms[0].submit();
		//}

		
		
		
		// Validamos los errores ///////////		
		if ((document.forms[0].sociedadesCliente.checked) && (document.forms[0].idCuenta.value == "")) {
			alert ("<siga:Idioma key="messages.censo.componentes.errorCuentaObligatoria"/>");
			fin();
			return false;
		}else if ((document.forms[0].sociedadesCliente.checked) && (document.forms[0].idCuenta.value != "")) {
			document.forms[0].idsociedad.value= document.forms[0].idCuenta.value;
		}

		document.forms[0].action		= "<%=app%>/JGR_PestanaRetencionesIRPF.do";
		document.forms[0].modo.value	= "guardarPagoPor";
		document.forms[0].submit();		
	}
	
	function accionInformeRetencionesIRPF() {
			
			document.RetencionesIRPFForm.modo.value = "dialogoInformeIRPF";
			// Abro ventana modal y refresco si necesario
			var resultado = ventaModalGeneral(document.RetencionesIRPFForm.name,"P");
	}

	function cuenta()
	{
		if (document.forms[0].sociedadesCliente.checked==true){
			document.getElementById("sinasteriscoCuenta").disabled=false;	
			document.getElementById("cuentaBancaria").disabled=false;	
					
		}else{			
			document.getElementById("sinasteriscoCuenta").disabled=true;
			document.getElementById("cuentaBancaria").disabled=true;			
		}
		//refrescarLocal();
	}
	
	//Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable
	function rellenarCampos(){	
	  // Obtenemos los valores para el check sociedad.
	  sociedad = "<%=sociedad%>";
	  if(sociedad == "<%=DB_FALSE%>"){
		document.getElementById("sinasteriscoCuenta").disabled=true;	
		document.getElementById("cuentaBancaria").disabled=true;	
	  }else {
		document.forms[0].sociedadesCliente.checked=true;
		document.getElementById("sinasteriscoCuenta").disabled=false;
		document.getElementById("cuentaBancaria").disabled=false;
	  }		
	}

	
</script>
		
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt 
		titulo="gratuita.retencionesIRPF.literal.titulo" 
		localizacion="gratuita.retencionesIRPF.literal.localizacion"/>
		
	<siga:TituloExt 
			titulo="censo.fichaCliente.sjcs.retencionesIRPF.cabecera" 
			localizacion="censo.fichaCliente.sjcs.retencionesIRPF.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body>
	
	<%
			String nC = "";
			String tC = "";
			String botones = "";
			String botonesA = "";
			if (sociedad == null || sociedad.equalsIgnoreCase("null")) {
				botonesA = "E,B";
				botones = "IRI,N";
			} else {
				botones = "IRI,N,G";
			}
			String idInstitucion = (String) request.getAttribute("idInstitucion");
			String idPersona = (String) request.getAttribute("idPersona");
			String alto = "";
			nC = "gratuita.retencionesIRPF.literal.fDesde,gratuita.retencionesIRPF.literal.fHasta,gratuita.retencionesIRPF.literal.letra,gratuita.retencionesIRPF.literal.descripcion,gratuita.retencionesIRPF.literal.retencion,";
			tC = "10,10,5,55,10,10";
			alto = "300";
		%>
	
		<html:form action="/JGR_PestanaRetencionesIRPF.do" method="post">
		<html:hidden property = "idpersona" value = ""/>
		<html:hidden property = "idsociedad" value = ""/>	
		<html:hidden property = "idsociedadant" value = ""/>				
		<tr>
			<td>
				<siga:ConjCampos leyenda="censo.busquedaClientes.literal.liquidacionSJCS" >	
				<table class="tablaCampos" align="center" border ="0" >	
					<tr>						
						<td class="labelText" >
							<siga:Idioma key="censo.consultaComponentesJuridicos.literal.liquidarSJCS"/>
						</td>
						
						<td>
							<html:checkbox  property="sociedadesCliente" disabled="<%=desactivado%>" onclick="cuenta()"/>
						</td>
					
						<td id="sinasteriscoCuenta" class="labelText" >
							<siga:Idioma key="censo.consultaComponentesJuridicos.literal.cuenta"/> 
						</td>
						
						<td id="cuentaBancaria">
						<%
						String parametro[] = new String[2];
						parametro[0] = idpersonacol;
						parametro[1] = idInstitucion;
							//cuentaSJCS
					  %>
					   <siga:ComboBD nombre="idCuenta" tipo="cuentasSJCSSociedades" parametro="<%=parametro%>"clase="box" obligatorio="false" elementoSel="<%=idBanco%>" />
						</td>
						
					</tr>						
		   		</table>
				</siga:ConjCampos>
			</td>
		</tr>

	<input type="hidden" name="idInstitucion" value="<bean:write name='idInstitucion'/>"/>
	<input type="hidden" name="idPersona" value="<bean:write name='idPersona'/>"/>
		
		<input type="hidden" name="modo" />
		<input type="hidden" name="idPersonaSociedadInicial" value="<%=idSociedadLetradoSel%>">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="desdeFicha" value="1" />
				<script>
		rellenarCampos();
	</script>
		</html:form>	
	
	<p>
		<!-- campos a pasar -->
		<siga:TablaCabecerasFijas 
		   nombre="retencionesIRPF"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   			alto="100%"
		   			ajusteBotonera="true"		
		   modal="P"
		  >
		  
		<%
		  			if (obj != null && obj.size() > 0) {
		  					String fecha = "";
		  					int recordNumber = 1;
		  					while ((recordNumber) <= obj.size()) {
		  						Hashtable hash = new Hashtable();
		  						hash = (Hashtable) obj.get(recordNumber - 1);
		  						if (sociedad == null
		  								|| sociedad.equalsIgnoreCase("null")) {
		  							botonesA = "E,B";
		  						} else {
		  							if (hash.get("FECHAINICIO") != null
		  									&& !hash.get("FECHAINICIO").equals("")) {
		  								botonesA = "E,B";
		  							} else
		  								botonesA = "";
		  						}
		  		%>
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botonesA%>" clase="listaNonEdit" visibleConsulta="no" >
					<%
						if (sociedad == null
												|| sociedad.equalsIgnoreCase("null")) {
					%>
						
						
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDINSTITUCION")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDPERSONA")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("IDRETENCION")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=hash.get("FECHAINICIO")%>' />
						<%
							if (hash.get("FECHAFIN").equals("")) {
						%>
							<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_5' value=' ' />
						<%
							} else {
						%>
							<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_5' value='<%=hash.get("FECHAFIN")%>' />
						<%
							}
						%>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=hash.get("FECHAMODIFICACION")%>' />
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_7' value='<%=hash.get("USUMODIFICACION")%>' />
						<td ><%=GstDate.getFormatedDateShort(usr
												.getLanguage(), ((String) hash
												.get("FECHAINICIO")))%>&nbsp;</td>
						<td ><%=GstDate.getFormatedDateShort(usr
												.getLanguage(), ((String) hash
												.get("FECHAFIN")))%>&nbsp;</td>
					
					<%
											} else {
										%>
							<!--Si la fecha de inicio no es null indica que aunque sea sociedad hay algun registro en la tabla scs_retencionesirpf que guarda 
							el irpf si el cliente actua como letrado //-->
							<%
								if (hash.get("FECHAINICIO") != null
															&& !hash.get("FECHAINICIO").equals("")) {
							%>
								<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_1' value='<%=hash.get("IDINSTITUCION")%>' />
								<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_2' value='<%=hash.get("IDPERSONA")%>' />
								<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_3' value='<%=hash.get("IDRETENCION")%>' />
								<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_4' value='<%=hash.get("FECHAINICIO")%>' />
								<%
									if (hash.get("FECHAFIN").equals("")) {
								%>
								<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_5' value=' ' />
								<%
									} else {
								%>
								<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=hash.get("FECHAFIN")%>' />
								<%
									}
								%>
								<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_6' value='<%=hash.get("FECHAMODIFICACION")%>' />
								<input type='hidden' name='oculto<%=String
															.valueOf(recordNumber)%>_7' value='<%=hash.get("USUMODIFICACION")%>' />
								<td ><%=GstDate
															.getFormatedDateShort(
																	usr
																			.getLanguage(),
																	((String) hash
																			.get("FECHAINICIO")))%>&nbsp;</td>
								<td ><%=GstDate.getFormatedDateShort(usr
													.getLanguage(),
													((String) hash
															.get("FECHAFIN")))%>&nbsp;</td>
							
							<%
															} else {
														%>
								<td >-</td>
								<td >-</td>
							<%
								}
							%>	
					<%
							}
						%>
						
						
						<td ><%=hash.get("LETRA")%>&nbsp;</td>
						<td ><%=hash.get("DESCRIPCION")%></td>
						<td align="right"><%=hash.get("RETENCION")%></td>
						
						<%
													vFechaInicio.add(hash.get("FECHAINICIO"));
																	vFechaFin.add(hash.get("FECHAFIN"));
												%>
					</siga:FilaConIconos>
					<%
						recordNumber++;
								}
					%>
		<%
			} else {
		%>
				<br>
					<p class="titulitos" style="text-align:center">
					<siga:Idioma key="gratuita.retenciones.noResultados"/></p>
				<br>
		<%
			}
		%>
		</siga:TablaCabecerasFijas>

		
<%
			if (!busquedaVolver.equals("volverNo")) {
				botones = "V," + botones;
		%>
			<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"  />	
<%
		} else {
	%>
			<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"  />	
<%
		}
	%>


<%@ include file="/html/jsp/censo/includeVolver.jspf" %>		

	</body>
</html>