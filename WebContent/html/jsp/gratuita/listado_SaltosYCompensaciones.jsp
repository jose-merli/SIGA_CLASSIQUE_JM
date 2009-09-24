<!-- listado_SaltosYCompensaciones.jsp -->
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
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.beans.ScsSaltosCompensacionesBean"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->

	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

 	<script>
	 	//Refresco del iframe 	
	 	function refrescarLocal()
		{
			parent.buscar()
		}
 	</script>
 	
</head>

<body>

	<html:form action="/JGR_SaltosYCompensaciones.do" method="post" target="submitArea" style="display:none">
		<html:hidden property = "modo" value = ""/>

		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>	
		
	<siga:TablaCabecerasFijas 		   
		   nombre="listado"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.inicio_SaltosYCompensaciones.literal.turno,gratuita.inicio_SaltosYCompensaciones.literal.guardia,gratuita.inicio_SaltosYCompensaciones.literal.nColegiado,gratuita.inicio_SaltosYCompensaciones.literal.letrado,gratuita.inicio_SaltosYCompensaciones.literal.tipo,gratuita.inicio_SaltosYCompensaciones.literal.fecha,gratuita.inicio_SaltosYCompensaciones.literal.fechaUso,"
		   tamanoCol="20,17,10,15,10,8,10,10"
		   alto="100%" 
		   modal="P"
		   activarFilaSel="true" >

	<!-- INICIO: RESULTADO -->
	<% if ((obj!= null) && (obj.size()>0)) { %>
			<%
			//Datos ocultos:
			String idInstitucion="", idTurno="", idSaltosTurno="", idGuardia="", idPersona="";
			String saltoOCompensacion="", fecha="", motivos="", fechaCumplimiento="";
			//Datos visibles:
			String nombreTurno="", nombreGuardia="", datosLetrado="", nCol="&nbsp;";
			
			int recordNumber=1;
			while ((recordNumber) <= obj.size())
			{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			%>
			<!-- Campos ocultos por cada fila:      
				1- IDINSTITUCION
				2- IDTURNO
				3- IDSALTOSTURNO
				4- IDGUARDIA
				5- IDPERSONA
				6- SALTOOCOMPENSACION
				7- FECHA
				8- MOTIVOS
				9- FECHACUMPLIMIENTO
			-->
			<!-- Campos visibles por cada fila:
				1- NOMBRE TURNO
				2- NOMBRE GUARDIA
				3- DATOS LETRADO (NUMERO LETRADO + NOMBRE + APELLIDOS)
				4- SALTO O COMPENSACION
				5- FECHA
				6- FECHA COMPENSACION
			-->		
			<%
			//Datos ocultos
			//Meto un espacio antes el value de los campos que pueden ser vacios
			idInstitucion = hash.get(ScsSaltosCompensacionesBean.C_IDINSTITUCION)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDINSTITUCION);
			idTurno = hash.get(ScsSaltosCompensacionesBean.C_IDTURNO)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDTURNO);
			idSaltosTurno = hash.get(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);
			idPersona = hash.get(ScsSaltosCompensacionesBean.C_IDPERSONA)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDPERSONA);
			saltoOCompensacion = hash.get(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
			fecha = hash.get(ScsSaltosCompensacionesBean.C_FECHA)==null?"":(String)hash.get(ScsSaltosCompensacionesBean.C_FECHA);

			//Campos que pueden venir vacios:
			if (hash.get(ScsSaltosCompensacionesBean.C_MOTIVOS)!=null && ((String)hash.get(ScsSaltosCompensacionesBean.C_MOTIVOS)).equals(""))
				motivos = " ";
			else
				motivos = (String)hash.get(ScsSaltosCompensacionesBean.C_MOTIVOS);
			if (hash.get(ScsSaltosCompensacionesBean.C_IDGUARDIA)!=null && ((String)hash.get(ScsSaltosCompensacionesBean.C_IDGUARDIA)).equals(""))
				idGuardia = " ";
			else
				idGuardia = (String)hash.get(ScsSaltosCompensacionesBean.C_IDGUARDIA);
			if (hash.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO)!=null && ((String)hash.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO)).equals(""))
				fechaCumplimiento = " ";
			else
				fechaCumplimiento = (String)hash.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
			//Datos visibles
			nombreTurno = hash.get("NOMBRETURNO")==null?"&nbsp;":(String)hash.get("NOMBRETURNO");
			if (hash.get("NOMBREGUARDIA")!=null && ((String)hash.get("NOMBREGUARDIA")).equals(""))
				nombreGuardia = " ";
			else
				nombreGuardia = (String)hash.get("NOMBREGUARDIA");
			if (hash.get("NUMERO")!=null && ((String)hash.get("NUMERO")).equals("")) {
				datosLetrado = UtilidadesHash.getString(hash,"LETRADO");
			} else {
			//	datosLetrado = "("+UtilidadesHash.getString(hash,"NUMERO")+") "+UtilidadesHash.getString(hash,"LETRADO");
				nCol = UtilidadesHash.getString(hash,"NUMERO");
				datosLetrado = UtilidadesHash.getString(hash,"LETRADO");
			}

			%>
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
				<td>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idInstitucion%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idTurno%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=idSaltosTurno%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idGuardia%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=idPersona%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=saltoOCompensacion%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_7' value='<%=fecha%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_8' value='<%=motivos%>'>
					<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_9' value='<%=fechaCumplimiento%>'>
					<%=nombreTurno%>
				</td>
				<td><% if(nombreGuardia.equals(" ")) { %>
						&nbsp;
					<% } else { %>
						<%=nombreGuardia%>
					<% } %>
				</td>
				<td><%=nCol%></td>
				<td><%=datosLetrado%></td>
				<td >
					<% if (saltoOCompensacion.equalsIgnoreCase("S")) { %>
						<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.salto"/>
					<% } else { %>
						<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.compensacion"/>
					<% } %>
				</td>
				<td >
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fecha)%>
				</td>				
				<td >
					<% if(fechaCumplimiento.equals(" ")) { %>
						&nbsp;
					<% } else { %>
						<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaCumplimiento)%>
					<% } %>
				</td>
			</siga:FilaConIconos>		
				<% 		recordNumber++; %>
				<% } %>
		<!-- FIN: RESULTADO -->
	<% } else { %>
		<html:hidden property = "actionModal" value = "P"/>
		<p class="labelText" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
	<% } %>
			</siga:TablaCabecerasFijas>


	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>