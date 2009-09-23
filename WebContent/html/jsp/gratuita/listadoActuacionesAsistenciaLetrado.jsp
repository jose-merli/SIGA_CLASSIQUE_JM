<!-- listadoActuacionesAsistenciaLetrado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ taglib uri = "struts-bean.tld" prefix="bean"%> 
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.AsistenciasForm" %>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	Vector obj = (Vector) request.getAttribute("resultado");
	
	//Modo de la pestanha pasado por el formulario siempre:
	AsistenciasForm miForm = (AsistenciasForm)request.getAttribute("FormularioActuacionAsistencia");
	String modoPestanha = miForm.getModoPestanha();
	String esFichaColegial = miForm.getEsFichaColegial();
	String mostrarNuevo = (String)request.getAttribute("botonNuevo");
	
	boolean letradoActuaciones = UtilidadesString.stringToBoolean((String)request.getAttribute("LETRADOACTUACIONES"));

%>
<html>
	<head>
	<title><"listarAsistencias.title"></title>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.listadoActuacionesAsistencia.literal.titulo" 
		localizacion="gratuita.listadoActuacionesAsistencia.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">

	<% 	
		String nC="";
		String tC="";
		String botones = "";
		String alto="325";
	  	nC="gratuita.listadoActuacionesAsistencia.literal.nactuacion,gratuita.listadoActuacionesAsistencia.literal.fecha,gratuita.listadoActuacionesAsistencia.literal.descripcion,gratuita.listadoActuacionesAsistencia.literal.justificado,gratuita.actuacionesDesigna.literal.validada,gratuita.actuacionesDesigna.literal.facturacion,";
		tC="10,8,33,10,7,22,10";
	%>
	
	<html:form action="/JGR_ActuacionAsistenciaLetrado.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" />
		<html:hidden name="AsistenciasForm" property="anio" />
		<html:hidden name="AsistenciasForm" property="numero" />
		<html:hidden name="AsistenciasForm" property="esFichaColegial" />
		<html:hidden name="AsistenciasForm" property="modoPestanha"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
		<!-- campos a pasar -->
		<siga:TablaCabecerasFijas 
		   nombre="listarAsistencias"
		   borde="2"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   modal="G">

		<%if (obj != null && obj.size()>0){%>
				<%
				String fecha = "";
		    	int recordNumber=1;
		    	String select = "";
		    	Vector v = null;
		    	ScsAsistenciasAdm scsAsistenciasAdm = new ScsAsistenciasAdm(usr);
				while ((recordNumber) <= obj.size())
				{	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
					//if (usr.isLetrado()) {
					//	botones = "C";
					//} else {
						String idFacturacion = (String)hash.get("IDFACTURACION");
						if(modoPestanha != null && modoPestanha.equalsIgnoreCase("editar")) {
								// Comprobamos el estado del idfacturacion
								if (idFacturacion==null||idFacturacion.equals(""))
								{
									botones = "C,E,B";
								} else {
									botones = "C";
								}
						} else {
							botones = "C";
						}
						
						if (!letradoActuaciones) {
							botones = "C";
						}
						
					//}
					String anulacion = (String)hash.get("ANULACION");
					String modo="";
				 	if((anulacion!=null)&&(anulacion).equalsIgnoreCase("1"))
				 		modo="VER";
					
				%>
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" modo="<%=modo%>">
						<td><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("ANIO")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("NUMERO")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("IDACTUACION")%>'><%=hash.get("IDACTUACION")%></td>
						<% fecha = GstDate.getFormatedDateShort("",(String) hash.get("FECHA")); %>
						<td><%=fecha%></td>
						<td><%=hash.get("DESCRIPCIONBREVE")%>&nbsp;</td>
						<% fecha = GstDate.getFormatedDateShort("",(String) hash.get("FECHAJUSTIFICACION")); %>
						<td><%=fecha%>&nbsp;</td>
						<td><%=(String) hash.get("VALIDADA")%>&nbsp;</td>
						<td><%=((String) hash.get("NOMBREFACTURACION")==null?"":(String) hash.get("NOMBREFACTURACION"))%>&nbsp;</td>
					</siga:FilaConIconos>
					<% recordNumber++;
				} %>
		<% } else { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
		<% } %>
		</siga:TablaCabecerasFijas>
		
	<% 
			String botonNuevo = "";
			if (esFichaColegial != null && esFichaColegial.equals("1")){
				//if(mostrarNuevo.equalsIgnoreCase("si"))
				//botonNuevo += "N";

				if (!modoPestanha.equalsIgnoreCase("ver")) {
					// Verificamos si el turno permite la modificacion de la asistencia
					if (letradoActuaciones) {
						botonNuevo += "N";
					}
				}
			}
			else {
				if (modoPestanha!=null && (modoPestanha.equalsIgnoreCase("modificar") || modoPestanha.equalsIgnoreCase("editar")))
				{
					if(mostrarNuevo.equalsIgnoreCase("si"))
						botonNuevo += "N,V";
					else
						botonNuevo += "V";	
				}
				else
					botonNuevo += "V";
			}
	%>

		<siga:ConjBotonesAccion botones="<%=botonNuevo%>"  clase="botonesDetalle" />


	<script>
		function accionNuevo()	{
		     document.forms[0].modo.value = "nuevo";
			 resultadoVentanaCondicion = ventaModalGeneral(document.forms[0].name, "G");
			 if (resultadoVentanaCondicion == "MODIFICADO") 
			  		refrescarLocal();
	 	}

		function accionVolver() {
			document.forms[0].target="mainWorkArea";
			document.forms[0].action = "<%=app %>/JGR_Asistencia.do";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();
		}
	 	
		function refrescarLocal() {
			document.forms[0].target="_self";
			document.forms[0].modo.value = "abrir";
			document.forms[0].submit();		
		}
		
	</script>

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
	
	</body>
</html>