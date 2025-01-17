<!DOCTYPE html>
<html>
<head>
<!-- contAnulDeliJuriAsistencia.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>


<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	String accion = (String) request.getSession().getAttribute("accion");
	
	boolean esFichaColegial = false;

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null)
			&& ((sEsFichaColegial.equalsIgnoreCase("1"))||(sEsFichaColegial.equalsIgnoreCase("true"))  )) {
		esFichaColegial = true;
	}
	
	// Obtenemos datos generales
	String ACTION 			= (String) request.getAttribute("ACTION");
	String TITULO 			= (String) request.getAttribute("TITULO");
	String TITULOTEXTAREA 	= (String) request.getAttribute("TITULOTEXTAREA");
	String NOMBRETEXTAREA 	= (String) request.getAttribute("NOMBRETEXTAREA");
	String DATO				= (String) request.getAttribute("DATO");
	// Obtenemos el resultado
	Vector resultado = (Vector) request.getAttribute("resultado");
	Hashtable hash =  (Hashtable) resultado.get(0);
	String fanulacion = (String) hash.get("FECHAANULACION");
	String ANIO 				= (String) hash.get("ANIO");
	String NUMERO 				= (String) hash.get("NUMERO");
	String VALORDATO			= (String) hash.get(DATO);
	String FECHAANULACION = "";
	if(fanulacion!=null)
	{
		FECHAANULACION = GstDate.getFormatedDateShort("",fanulacion);
	}
	else
	{
		java.text.SimpleDateFormat formador = new java.text.SimpleDateFormat("dd/MM/yyyy");
		FECHAANULACION = formador.format(new Date());
	}
		
	String botonesAccion;
	if(accion != null && accion.equalsIgnoreCase("modificar")){
 		botonesAccion = esFichaColegial ? "g,r" : "g,r,v";
	}else{
 		botonesAccion = esFichaColegial ? "" : "v";
	}
		
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<% if(esFichaColegial){ %>
		<siga:Titulo titulo="<%=TITULO%>" 
				 localizacion="censo.gratuita.asistencias.literal.localizacion"/>
	<% } else { %>
		<siga:Titulo titulo="<%=TITULO%>" 
				 localizacion="gratuita.contAnulDeliJuriAsistencia.literal.localizacion"/>
	<% } %>		
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body>

    <table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
			<td class="titulitosDatos">
			
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (usr);
					Hashtable hTitulo = adm.getTituloPantallaAsistencia(usr.getLocation(), ANIO, NUMERO);
					if (hTitulo != null) {
						t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
						t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
						t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
						t_anio      = (String)hTitulo.get(ScsAsistenciasBean.C_ANIO);
						t_numero    = (String)hTitulo.get(ScsAsistenciasBean.C_NUMERO);
					}
				%>
				<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> 
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>	


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action = "<%=ACTION%>" method="POST" target="mainWorkArea">
		<html:hidden property = "anio" styleId="anio" value = "<%=ANIO%>"/>	
		<html:hidden property = "numero" styleId="numero" value = "<%=NUMERO%>"/>	
		<html:hidden property = "modo" value = ""/>	
		<html:hidden styleId="jsonVolver" property = "jsonVolver"  />
		<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>
		<fieldset>
			<table width="100%" border="0">
				<%if(DATO.equals("MOTIVOSANULACION")){%>
				<tr>
					<%if(accion != null && accion.equalsIgnoreCase("modificar")){%>
					<td class="labelText" width="150">	
						<siga:Idioma key='gratuita.contAnulDeliJuriAsistencia.literal.fanulacion'/>
					</td>
					<td>						
						<siga:Fecha nombreCampo="fechaAnulacion" valorInicial="<%=FECHAANULACION%>"></siga:Fecha>
					</td>	
					<%}else{%>
					<td class="labelText" width="150">	
						<siga:Idioma key='gratuita.contAnulDeliJuriAsistencia.literal.fanulacion'/>
					</td>
					<td>
						<%=FECHAANULACION%>
					</td>
					<%}%>
				</tr>
				<%}%>
					<%if(accion != null && accion.equalsIgnoreCase("modificar")){%>
						<tr align="center">
						<td class="labelText"  width="150" >	
							<siga:Idioma key='<%=TITULOTEXTAREA%>'/>
						</td>	
						<td class="labelTextValor" >	
							<html:textarea name="AsistenciasForm" property="<%=NOMBRETEXTAREA%>" 
								onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)"
								style="overflow-y:auto; overflow-x:hidden; width:750px; height:400px; resize:none;"  
								styleClass="boxCombo" value="<%=VALORDATO%>" readOnly="false" styleId="<%=NOMBRETEXTAREA%>"></html:textarea>
						</td>
						</tr>
					<%}else{%>
						<tr align="center">
						<td class="labelText"  width="150">	
							<siga:Idioma key='<%=TITULOTEXTAREA%>'/>
						</td>	
						<td class="labelTextValor">	
							<html:textarea name="AsistenciasForm" property="<%=NOMBRETEXTAREA%>" 
								style="overflow-y:auto; overflow-x:hidden; width:750px; height:400px; resize:none;"
								styleClass="boxConsulta" value="<%=VALORDATO%>" readOnly="true" property="<%=NOMBRETEXTAREA%>"></html:textarea>
						</td>
						</tr>
					<%}%>
			</table>
		</fieldset>
	
	</html:form>
	<html:form action="/JGR_GestionSolicitudesAceptadasCentralita.do"  method="POST" target="mainWorkArea">
		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idSolicitudAceptada"/>
	</html:form>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton limpiar -->
		function limpiar() {		
			document.forms[0].reset();
		}
		
		function accionRestablecer() {		
			parent.buscar();
		}
		
		function refrescarLocal() {
			parent.buscar();
		}

		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() { }
 		
		function accionGuardar() {
			sub();		
			document.forms[0].modo.value = "modificar";
			document.forms[0].target = "submitArea";							
			document.forms[0].submit();
		}

		
		function accionVolver() {
			if(document.forms[0].jsonVolver && document.forms[0].jsonVolver.value!=''){
				jSonVolverValue = document.forms[0].jsonVolver.value;
				jSonVolverValue = replaceAll(jSonVolverValue,"'", "\"");
				var jSonVolverObject =  jQuery.parseJSON(jSonVolverValue);
				nombreFormulario = jSonVolverObject.nombreformulario; 
				if(nombreFormulario == 'SolicitudAceptadaCentralitaForm'){
					document.forms['SolicitudAceptadaCentralitaForm'].idSolicitudAceptada.value =  jSonVolverObject.idsolicitudaceptada;
					document.forms['SolicitudAceptadaCentralitaForm'].idInstitucion.value = jSonVolverObject.idinstitucion;
					document.forms['SolicitudAceptadaCentralitaForm'].modo.value="consultarSolicitudAceptada";
					document.forms['SolicitudAceptadaCentralitaForm'].target = "mainWorkArea";
					document.forms['SolicitudAceptadaCentralitaForm'].submit();
				}
			}else{
			
				<%
				// indicamos que es boton volver
				ses.setAttribute("esVolver","1");
				%>
	<%
				String sAction2 = esFichaColegial ? "JGR_AsistenciasLetrado.do" : "JGR_Asistencia.do";
	%>
				document.forms[0].action = "<%=sAction2%>";
				document.forms[0].modo.value= "abrir";
				document.forms[0].submit();
			}
		}
	</script>

	<!-- INICIO: BOTONES BUSQUEDA -->	

		<siga:ConjBotonesAccion botones="<%=botonesAccion %>" clase="botonesDetalle"  />	
	<!-- FIN: BOTONES BUSQUEDA -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>