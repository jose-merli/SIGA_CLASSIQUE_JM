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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
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

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
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
						<html:text name="AsistenciasForm" property="fechaAnulacion" styleId="fechaAnulacion" 
							size="10" maxlength="10" styleClass="box" value="<%=FECHAANULACION%>" readOnly="true">
						</html:text>
						&nbsp;&nbsp;
						<a onClick="return showCalendarGeneral(fechaAnulacion);" onMouseOut="MM_swapImgRestore();" 
							onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
							<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" />
						</a>
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
							<html:textarea name="AsistenciasForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" 
								property="<%=NOMBRETEXTAREA%>" cols="200" rows="25" style="overflow:auto" styleClass="boxCombo" 
								value="<%=VALORDATO%>" readOnly="false" styleId="<%=NOMBRETEXTAREA%>"></html:textarea>
						</td>
						</tr>
					<%}else{%>
						<tr align="center">
						<td class="labelText"  width="150">	
							<siga:Idioma key='<%=TITULOTEXTAREA%>'/>
						</td>	
						<td class="labelTextValor">	
							<html:textarea name="AsistenciasForm" property="<%=NOMBRETEXTAREA%>" cols="200" rows="25" 
								style="overflow:auto" styleClass="boxConsulta" value="<%=VALORDATO%>" readOnly="true" property="<%=NOMBRETEXTAREA%>">
							</html:textarea>
						</td>
						</tr>
					<%}%>
			</table>
		</fieldset>
	
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
	</script>

<%
		String sClasePestanas = esFichaColegial ? "botonesDetalle3" : "botonesDetalle";
%>
	<!-- INICIO: BOTONES BUSQUEDA -->	

		<siga:ConjBotonesAccion botones="<%=botonesAccion %>" clase="<%=sClasePestanas%>"  />	
	<!-- FIN: BOTONES BUSQUEDA -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>