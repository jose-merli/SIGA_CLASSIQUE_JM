<!-- listado_IncompatibilidadesGuardia.jsp -->


<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>


<!-- JSP --> 
<%
	//Variables globales
	String app = request.getContextPath (); 
	HttpSession ses = request.getSession (true);
	UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
	Properties src = (Properties) ses.getAttribute (SIGAConstants.STYLESHEET_REF);
	String profile[] = usr.getProfile ();
	
	//Datos propios del jsp
	Vector obj = (Vector) request.getAttribute("resultado");
	String modo = request.getAttribute("modo") == null ? "" : (String) request.getAttribute ("modo");
	
	//Datos del Colegiado si procede
	String nombrePestanha = null, numeroPestanha = null;
	try {
		Hashtable datosColegiado = (Hashtable) request.getSession ().getAttribute ("DATOSCOLEGIADO");
		nombrePestanha = (String) datosColegiado.get ("NOMBRECOLEGIADO");
		numeroPestanha = (String) datosColegiado.get ("NUMEROCOLEGIADO");
	} catch (Exception e) {
		nombrePestanha = "";
		numeroPestanha = "";
	}
	
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String) ses.getAttribute ("entrada");
	//Si venimos del menu de Censo tenemos un alto menor ya que ponemos el nombre del colegiado:
	String alto = "344";
	if (entrada != null && entrada.equals ("2"))
		alto = "300";
%>


<html>


<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	
	<!---------- INICIO: TITULO Y LOCALIZACION ---------->
	<% if (entrada!=null && entrada.equals ("2")) { %>
	<siga:TituloExt 
		titulo="censo.fichaCliente.sjcs.turnos.guardias.incompatibilidades.cabecera" 
		localizacion="censo.fichaCliente.sjcs.turnos.guardias.incompatibilidades.localizacion"
	/>
	<% } else { %>
	<siga:TituloExt 
		titulo="pestana.justiciagratuitaguardia.incompatibilidadesguardia" 
		localizacion="gratuita.IncompatibilidadesGuardia.literal.localizacion"
	/>
	<% } %>
	<!---------- FIN: TITULO Y LOCALIZACION ---------->
	
	
	<!---------- INICIO: JAVASCRIPT ---------->
	<script>
		function refrescarLocal () {
			parent.refrescarLocal ();
		}
	</script>
	<!---------- FIN: JAVASCRIPT ---------->
	
</head>


<body>

	<!---------- INICIO: CAMPOS OCULTOS ---------->
	<html:form action="/JGR_IncompatibilidadesGuardia.do"
			   method="post" target="resultado" style="display:none"
	>
			<html:hidden property = "modo" value = "<%=modo%>"/>
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
	</html:form>
	<!---------- FIN: CAMPOS OCULTOS ---------->
    
    
    <!---------- INI: TITULO GUARDIA -->
    <table width="100%"><tr class="titulitosDatos">
      <td width="50%">
        <siga:Idioma key="gratuita.confGuardia.literal.turno"/>:&nbsp;
        <%= (String)request.getAttribute ("NOMBRETURNO") %>
      </td>
      <td width="50%">
        <siga:Idioma key="gratuita.confGuardia.literal.guardia"/>:&nbsp;
        <%= (String)request.getAttribute ("NOMBREGUARDIA") %>
      </td>
    </tr></table>
    <!---------- FIN: TITULO GUARDIA -->
	
	
	<!---------- INICIO: ENTRADA DESDE EL MENU DE CENSO ---------->
	<% if (entrada.equalsIgnoreCase("2")) { %>
    <table class="tablaTitulo" align="center" cellspacing=0><tr>
    	<td class="titulitosDatos">
			<siga:Idioma key="censo.consultaDatosGenerales.literal.titulo1"/>
			&nbsp;&nbsp;
			<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>
			&nbsp;&nbsp;
			
		    <% if(numeroPestanha != null && !numeroPestanha.equalsIgnoreCase ("")) { %>
			<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
			&nbsp;&nbsp;
			<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
			<% } else { %>
			<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
			<% } %>
		</td>
	</tr></table>	
	<% } %>
	<!---------- FIN: ENTRADA DESDE EL MENU DE CENSO ---------->
	
	
	<!---------- INICIO: RESULTADO ---------->
    <%
        String nombresColumnas = "" +
            "gratuita.incompatibilidadesGuardias.literal.turno," +
            "gratuita.incompatibilidadesGuardias.literal.guardia," +
            "gratuita.listado_IncompatibilidadesGuardia.literal.dias," +
            "gratuita.incompatibilidadesGuardias.literal.motivos," +
            "gratuita.incompatibilidadesGuardias.literal.diasSeparacionGuardias";
        String tamanyoColumnas = "23,23,23,23,8";
        
		int recordNumber = 1;
		String turno="", guardia="", idinstitucion="";
		String tipodia="", motivos="", diasseparacion="";
    %>
    
	<siga:Table name="listadoInicial"
							  border="1"
							  columnNames="<%=nombresColumnas%>"
							  columnSizes="<%=tamanyoColumnas%>">
	
	<%
		if ((obj != null) && (obj.size () > 0))
		{
			while (recordNumber <= obj.size ())
			{
				Hashtable hash = (Hashtable) obj.get (recordNumber-1);
				
				turno = ((String)hash.get("TURNO")).equals("")?"&nbsp;":(String)hash.get("TURNO");
				guardia = ((String)hash.get("GUARDIA")).equals("")?"&nbsp;":(String)hash.get("GUARDIA");
				tipodia = ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr);
				motivos = ((String)hash.get("MOTIVOS")).equals("")?"&nbsp;":(String)hash.get("MOTIVOS");
				diasseparacion = ((String)hash.get("DIASSEPARACIONGUARDIAS")).equals("")?"&nbsp;":(String)hash.get("DIASSEPARACIONGUARDIAS");
				idinstitucion = ((String)hash.get("IDINSTITUCION")).equals("")?"&nbsp;":(String)hash.get("IDINSTITUCION");
	%>
	<tr class=	<% if (recordNumber % 2 == 0) { %>
					'filaTablaPar'
				<% } else { %>
					'filaTablaImpar'
				<% } %>
	>
		<td><%=turno%></td>
		<td><%=guardia%></td>
		<td><%=tipodia%></td>
		<td><%=motivos%></td>
		<td align="center"><%=diasseparacion%></td>
	</tr>
	<%
				recordNumber++;
			} //while
	%>
	<% } else { %>
	<tr class="notFound"><td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td></tr>
	<% } %>
	</siga:Table>
	<!---------- FIN: RESULTADO ---------->
	
	
	<!---------- INICIO: SUBMIT AREA ---------->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
			style="display:none"
	/>
	<!---------- FIN: SUBMIT AREA ---------->
	
</body>

</html>