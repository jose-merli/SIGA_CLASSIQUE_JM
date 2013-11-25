<!DOCTYPE html>
<html>
<head>
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


<!-- HEAD -->


		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

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

	<style type="text/css">
		.registroBaja{font-style:italic}
		
	</style>		

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
		
		function incluirRegBajaLogica(o){
			if (o.checked) {
				jQuery(".registroBaja").show();
			} else {
				jQuery(".registroBaja").hide();
			}
			pintaFilas();
		}
		
		jQuery(document).ready(function() {
			if(jQuery(".registroBaja")[0]){
				jQuery("#checkHistorico").show();
			}
			jQuery(".registroBaja").hide();
			pintaFilas();
		});
		
		function pintaFilas(){
			jQuery(".filaTablaPar").removeClass("filaTablaPar").addClass("filaTabla");
			jQuery(".filaTablaImpar").removeClass("filaTablaImpar").addClass("filaTabla");
			jQuery(".filaTabla:visible:odd").addClass("filaTablaPar");
			jQuery(".filaTabla:visible:even").addClass("filaTablaImpar");
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

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="censo.consultaComponentesJuridicos.literal.nifcif,censo.consultaComponentesJuridicos.literal.nombre,censo.consultaComponentesJuridicos.literal.cargo,censo.consultaComponentesJuridicos.literal.fechaCargoBaja,censo.consultaComponentesJuridicos.literal.sociedad,censo.consultaComponentesJuridicos.literal.ejerciente,censo.consultaComponentesJuridicos.literal.ParticipacionSociedad%,"
		   columnSizes="10,25,15,10,10,10,10,10"
		   modal="G">  
		 
	<% if(vDatos == null || vDatos.size()<1 ) {  %>
	 		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	 		
	 <%	} else {	 
			Enumeration en = vDatos.elements();		
 			int i=0;  									
			String claseBaja = "";
			String fechaCargo ="";
			String fechaBaja ="";
			String ejerciente= "";
			String sociedad= "";
			String nombreCompleto="";
			while(en.hasMoreElements()){
				Hashtable htData = (Hashtable)en.nextElement();
				if (htData == null) continue;
				i++;
				fechaCargo = com.atos.utils.GstDate.getFormatedDateShort("",htData.get(CenComponentesBean.C_FECHACARGO));
				fechaBaja = com.atos.utils.GstDate.getFormatedDateShort("",htData.get(CenComponentesBean.C_FECHABAJA));
				if(fechaBaja.equalsIgnoreCase("")){
					claseBaja = "";
					iconos = "C, E, B";
				}else{
					fechaCargo = fechaCargo.equalsIgnoreCase("")?"":fechaCargo+" - ";
					fechaCargo += "("+fechaBaja+")";
					claseBaja ="registroBaja";
					iconos = "C";
				}
				nombreCompleto = htData.get(CenPersonaBean.C_NOMBRE) + " " + htData.get(CenPersonaBean.C_APELLIDOS1) + " " + htData.get(CenPersonaBean.C_APELLIDOS2);
 				
 				if(((String)htData.get(CenComponentesBean.C_SOCIEDAD)).equals(DB_FALSE)){	
 					sociedad = UtilidadesString.getMensajeIdioma(usr,"general.no");
 				}else{		   	 					
 					sociedad = UtilidadesString.getMensajeIdioma(usr,"general.yes");							   	 				
 				} 
 				
 				if(((String)htData.get("EJERCIENTE")).equals("1")){	
 					ejerciente = UtilidadesString.getMensajeIdioma(usr,"censo.consultaDatosGenerales.literal.ejerciente");
 				}else{		   	 					
 					ejerciente = "";							   	 				
 				} 
 				%> 		
			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->

			<!-- REGISTRO  -->		
				<siga:FilaConIconos fila='<%=String.valueOf(i)%>' clase="<%=claseBaja%>" botones='<%=iconos%>' modo='<%=modo%>' visibleConsulta="no">
					<td> <input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=htData.get(CenComponentesBean.C_IDCOMPONENTE)%>'>
					  	<%=UtilidadesString.mostrarDatoJSP(htData.get(CenPersonaBean.C_NIFCIF))%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(nombreCompleto)%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenComponentesBean.C_CARGO))%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(fechaCargo)%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(sociedad)%></td>  			
	  			   	<td><%=UtilidadesString.mostrarDatoJSP(ejerciente)%></td> 
					<td><%=UtilidadesString.mostrarDatoJSP(htData.get(CenComponentesBean.C_CAPITALSOCIAL))%></td> 										
				</siga:FilaConIconos>
				<!-- FIN REGISTRO -->
 			<%
 		}
 	}%>  			
  			</siga:Table>
  				<!-- FIN: ZONA DE REGISTROS -->
  				
	 <%		} else 	{	  // Cliente juridico
	 			botones = "V"; 
	 	%>
		 	<script> alert ("<siga:Idioma key="message.censo.componentes.noJuridico"/>");</script>	 	 
	 	<% }  
	 %>
	 
	 <div id="checkHistorico" style="position: absolute; left: 400px; bottom: 5px; z-index: 99; display:none">
		<table align="center" border="0">
			<tr>
				<td class="labelText"><label for="bajaLogica"><siga:Idioma key="censo.consultaRegistrosBajaLogica.literal" /></label>
					<input type="checkbox" name="bajaLogica" id="bajaLogica" onclick="incluirRegBajaLogica(this);">
				</td>
			</tr>
		</table>
	</div>
	
	<siga:ConjBotonesAccion botones="<%=botones%>" modo='<%=modo%>' clase="botonesDetalle"/>		

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</table>
	</body>
</html>
