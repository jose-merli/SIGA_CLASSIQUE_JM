<!-- consultaComponentesJuridicos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
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
<%@ page import="com.siga.beans.CenComponentesBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.CenNoColegiadoBean"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idUsr=(String)usr.getUserName();
	//String idUsr="1000";	
	String institucion=String.valueOf((Integer)request.getAttribute("idInstitucion"));
	String nombreUsu=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	String idPersona=String.valueOf((Long)request.getAttribute("idPersona"));
	Vector vDatos=(Vector)request.getAttribute("vDatos");		
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;
	String iconos = "C,E,B";
	String botones = "V,N";
	String modo = String.valueOf(request.getAttribute("accion"));

	Integer idTipoIden = (Integer) request.getAttribute(CenPersonaBean.C_IDTIPOIDENTIFICACION);
	String tipoNoCol = (String) request.getAttribute(CenNoColegiadoBean.C_TIPO);

	// Gestion de Volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null) {
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
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->		
		<html:javascript formName="componentesJuridicosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->		
		<siga:Titulo 
			  titulo="pestana.fichaCliente.componentesJuridicos" 
			  localizacion="censo.fichaCliente.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
		

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		function accionNuevo() {		
			document.componentesJuridicosForm.modo.value = "nuevo";
  	  		var rc = ventaModalGeneral(document.componentesJuridicosForm.name, "G");
			if (rc != null) { 
  	 	 		if (rc == "MODIFICADO") {
  	 	 			refrescarLocal();
  	  			}
  	  		}
		}
		
		function refrescarLocal() {
			//document.location.reload();
			document.componentesJuridicosForm.modo.value = "abrir";
			document.componentesJuridicosForm.submit();
		}

	</script>

</head>

<body class="tablaCentralCampos">

		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
    <table class="tablaTitulo" align="center" cellspacing=0>
    	<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_ComponentesJuridicos.do" method="POST" styleId="componentesJuridicosForm">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo"  styleId = "modo"  value = ""/>
			<input type="hidden" id="nombreUsuario" name="nombreUsuario" value="<%=nombreUsu%>" />
			<input type="hidden" id="numeroUsuario" name="numeroUsuario" value="<%=numero%>" />
			<input type='hidden' id="idPersona" name="idPersona" value="<%=idPersona%>" />	
			<input type='hidden' id="idInstitucion" name="idInstitucion" value="<%=institucion%>" />
			<input type='hidden' id="accion" name="accion" value="<%=modo%>" />		
			<input type="hidden" id="actionModal" name="actionModal" value="" />
			<input type="hidden" id="fechaCargo" name="fechaCargo" value="" />
		</html:form>
		
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.consultaComponentesJuridicos.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%> &nbsp;&nbsp;
					<%if(!numero.equalsIgnoreCase("")){%>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
		
	
	<% // Si soy cliente juridico tengo acceso a los datos, sino no (Osea cuando tenemos el attributo)
// 		 if ((idTipoIden != null)&& (idTipoIden.intValue() == ClsConstants.TIPO_IDENTIFICACION_CIF))	
 		 if (tipoNoCol != null)	{ %>

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   clase="tableTitle"
		   nombreCol=",censo.consultaComponentesJuridicos.literal.nifcif,censo.consultaComponentesJuridicos.literal.nombre,censo.consultaComponentesJuridicos.literal.cargo,censo.consultaComponentesJuridicos.literal.fechaCargo,censo.consultaComponentesJuridicos.literal.sociedad,censo.consultaComponentesJuridicos.literal.ejerciente,censo.consultaComponentesJuridicos.literal.ParticipacionSociedad%,"
		   tamanoCol="10,25,15,10,10,10,10,10"
		   alto="100%"
		   ajusteBotonera="true"		

		   modal="G">  
		 
	<% if(vDatos == null || vDatos.size()<1 ) {  %>
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
	 <%	} else {	 
			Enumeration en = vDatos.elements();		
 			int i=0;  									
			while(en.hasMoreElements()){
				Hashtable htData = (Hashtable)en.nextElement();
				if (htData == null) continue;
				i++;
				String nombreCompleto = htData.get(CenPersonaBean.C_NOMBRE) + " " + htData.get(CenPersonaBean.C_APELLIDOS1) + " " + htData.get(CenPersonaBean.C_APELLIDOS2);
 				String sociedad= "";
 				if(((String)htData.get(CenComponentesBean.C_SOCIEDAD)).equals(DB_FALSE)){	
 					sociedad = UtilidadesString.getMensajeIdioma(usr,"general.no");
 				}else{		   	 					
 					sociedad = UtilidadesString.getMensajeIdioma(usr,"general.yes");							   	 				
 				} 
 				String ejerciente= "";
 				if(((String)htData.get("EJERCIENTE")).equals("1")){	
 					ejerciente = UtilidadesString.getMensajeIdioma(usr,"censo.consultaDatosGenerales.literal.ejerciente");
 				}else{		   	 					
 					ejerciente = "";							   	 				
 				} 
								
	%> 		
	<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->

			<!-- REGISTRO  -->		
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones='<%=iconos%>' modo='<%=modo%>' clase="listaNonEdit" visibleConsulta="no">
					<td>
					
					<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(CenComponentesBean.C_IDCOMPONENTE)%>'>
					  	<%=UtilidadesString.mostrarDatoJSP(htData.get(CenPersonaBean.C_NIFCIF))%>
	  				</td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(nombreCompleto)%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenComponentesBean.C_CARGO))%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(com.atos.utils.GstDate.getFormatedDateShort("",htData.get(CenComponentesBean.C_FECHACARGO)))%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(sociedad)%></td>  			
	  			   	<td><%=UtilidadesString.mostrarDatoJSP(ejerciente)%></td> 
					<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenComponentesBean.C_CAPITALSOCIAL))%></td> 										
				</siga:FilaConIconos>
				<!-- FIN REGISTRO -->
 <%		}
 	}%>  			
  			</siga:TablaCabecerasFijas>
  				<!-- FIN: ZONA DE REGISTROS -->
  				
	 <%		} else 	{	  // Cliente juridico
	 			botones = "V"; 
	 	%>
		 	<script> alert ("<siga:Idioma key="message.censo.componentes.noJuridico"/>");</script>	 	 
	 	<% }  
	 %>
		<siga:ConjBotonesAccion botones="<%=botones%>" modo='<%=modo%>' clase="botonesDetalle"/>		

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</table>
	</body>
</html>
