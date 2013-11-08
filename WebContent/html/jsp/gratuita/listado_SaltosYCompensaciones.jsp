<!DOCTYPE html>
<html>
<head>
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
<%@ page import="com.siga.beans.ScsSaltoCompensacionGrupoBean"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
%>

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->

	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	<script>
	 	//Refresco del iframe 	
	 	function refrescarLocal() {
			parent.buscar()
		}
 	</script> 	
</head>

<body>
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="post" target="submitArea" style="display:none">
		<html:hidden property = "modo"  styleId = "modo" value = ""/>
	</html:form>	
		
	<siga:Table 		   
	   name="listado"
	   border="2"
	   columnNames="gratuita.inicio_SaltosYCompensaciones.literal.turno,gratuita.inicio_SaltosYCompensaciones.literal.guardia,gratuita.inicio_SaltosYCompensaciones.literal.nColegiado,gratuita.inicio_SaltosYCompensaciones.literal.letrado,gratuita.inicio_SaltosYCompensaciones.literal.tipo,gratuita.inicio_SaltosYCompensaciones.literal.fecha,gratuita.inicio_SaltosYCompensaciones.literal.fechaUso,"
	   columnSizes="18,18,7,20,11,8,8,10"
	   modal="P">

		<!-- INICIO: RESULTADO -->
<% 
		if ((obj!= null) && (obj.size()>0)) { 

			//Datos ocultos:
			String idInstitucion="", idTurno="", idSaltosTurno="", idGuardia="", idGrupoGuardia = "";
			String saltoOCompensacion="", fecha="", fechaCumplimiento="", tipoManual="";

			//Datos visibles:
			String nombreTurno="", nombreGuardia="", datosLetrado="", nCol="&nbsp;", idSaltoCompensacionGrupo="";
			
			int recordNumber=1;
			while ((recordNumber) <= obj.size()) {	 	
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);

				/* Campos ocultos por cada fila:      
					1- IDINSTITUCION
					2- IDTURNO
					3- IDSALTOSTURNO
					4- IDGUARDIA
					5- IDPERSONA
					6- SALTOOCOMPENSACION
					7- FECHA
					8- MOTIVOS
					9- FECHACUMPLIMIENTO

					Campos visibles por cada fila:
					1- NOMBRE TURNO
					2- NOMBRE GUARDIA
					3- DATOS LETRADO (NUMERO LETRADO + NOMBRE + APELLIDOS)
					4- SALTO O COMPENSACION
					5- FECHA
					6- FECHA COMPENSACION
				*/		
				
				//Datos ocultos
				//Meto un espacio antes el value de los campos que pueden ser vacios
				idInstitucion = hash.get(ScsSaltosCompensacionesBean.C_IDINSTITUCION)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDINSTITUCION);
				idTurno = hash.get(ScsSaltosCompensacionesBean.C_IDTURNO)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDTURNO);
				idSaltosTurno = hash.get(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);
				idSaltoCompensacionGrupo = hash.get(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO)==null?"&nbsp;":(String)hash.get(ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO);
				//idPersona = hash.get(ScsSaltosCompensacionesBean.C_IDPERSONA)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_IDPERSONA);
				saltoOCompensacion = hash.get(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION)==null?"&nbsp;":(String)hash.get(ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
				fecha = hash.get(ScsSaltosCompensacionesBean.C_FECHA)==null?"":(String)hash.get(ScsSaltosCompensacionesBean.C_FECHA);

				//Campos que pueden venir vacios:
				if (hash.get(ScsSaltosCompensacionesBean.C_IDGUARDIA)!=null && ((String)hash.get(ScsSaltosCompensacionesBean.C_IDGUARDIA)).equals(""))
					idGuardia = " ";
				else
					idGuardia = (String)hash.get(ScsSaltosCompensacionesBean.C_IDGUARDIA);
				
				if (hash.get(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA)!=null && ((String)hash.get(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA)).equals(""))
					idGrupoGuardia = " ";
				else
					idGrupoGuardia = (String)hash.get(ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA);
			
				if (hash.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO)!=null && ((String)hash.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO)).equals(""))
					fechaCumplimiento = " ";
				else
					fechaCumplimiento = (String)hash.get(ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
				
				if (hash.get(ScsSaltosCompensacionesBean.C_TIPOMANUAL)!=null)
					tipoManual = (String) hash.get(ScsSaltosCompensacionesBean.C_TIPOMANUAL);
			
				//Datos visibles
				nombreTurno = hash.get("NOMBRETURNO")==null?"&nbsp;":(String)hash.get("NOMBRETURNO");
				if (hash.get("NOMBREGUARDIA")!=null && ((String)hash.get("NOMBREGUARDIA")).equals(""))
					nombreGuardia = " ";
				else
					nombreGuardia = (String)hash.get("NOMBREGUARDIA");
				
				if (hash.get("NUMERO")!=null && ((String)hash.get("NUMERO")).equals("")) {
					datosLetrado = UtilidadesHash.getString(hash,"LETRADO");
				} else {
					nCol = UtilidadesHash.getString(hash,"NUMERO");
					datosLetrado = UtilidadesHash.getString(hash,"LETRADO");
				}
				
				String permisos = "C";
				if(fechaCumplimiento==null || fechaCumplimiento.trim().equals("") || tipoManual.equals("1")){
					permisos += ",E,B";			
				}
				
				//mhg - INC_05540_SIGA Pasamos los valores ocultos necesarios para poder obtener los objetos que necesitamos en el action.
%>
	       		<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=permisos%>" clase="listaNonEdit">
					<td>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idInstitucion%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idTurno%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=idSaltosTurno%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idGrupoGuardia%>'>
						<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=idSaltoCompensacionGrupo%>'>
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
					<td>
						<% if (saltoOCompensacion.equalsIgnoreCase("S")) { %>
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.salto"/>
						<% } else if (saltoOCompensacion.equalsIgnoreCase("C")) {  %>
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.compensacion"/>
						<% } else if (saltoOCompensacion.equalsIgnoreCase("CG")) {  %>
							<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.compensacionGrupo"/>
						<% } else if (saltoOCompensacion.equalsIgnoreCase("SG")) {  %>
							<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.saltoGrupo"/>												
						<% } %>
					</td>
					<td><%=GstDate.getFormatedDateShort(usr.getLanguage(),fecha)%></td>				
					<td>
						<% if(fechaCumplimiento.equals(" ")) { %>
							&nbsp;
						<% } else { %>
							<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaCumplimiento)%>
						<% } %>
					</td>
				</siga:FilaConIconos>		
<% 		
				recordNumber++; 
			} 

		// FIN: RESULTADO
		} else { 
%>		
			<tr class="notFound">
			   		<td class="titulitos">
			   			<siga:Idioma key="messages.noRecordFound"/>
			   		</td>
			</tr>
<% 
		} 
%>
	</siga:Table>
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>