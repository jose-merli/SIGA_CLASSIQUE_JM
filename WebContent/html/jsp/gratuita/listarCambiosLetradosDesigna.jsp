<!DOCTYPE html>
<html>
<head>
<!-- listarCambiosLetradosDesigna.jsp -->

<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
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

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	request.getSession().removeAttribute("resultado");
	String boton="";
	boolean botonNuevo = (Boolean)request.getSession().getAttribute("botonNuevo");
	Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
	String modo = (String) ses.getAttribute("Modo");
	String anio="",numero="", idTurno="";
	anio = (String)designaActual.get("ANIO");
	numero = (String)designaActual.get("NUMERO");
	idTurno = (String)designaActual.get("IDTURNO");
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo  titulo="gratuita.cambiosLetradoDesigna.literal.titulo" localizacion="gratuita.cambiosLetradoDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="JGR_CambiosLetradosDesigna.do" method="POST" target="mainPestanas" style="display:none">
		<html:hidden property = "modo" value = ""/>
	</html:form>	
	
	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">	
<% 
			    String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
				ScsDesignaAdm adm = new ScsDesignaAdm (usr);
				Hashtable hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(), anio, numero,idTurno);

				if (hTitulo != null) {
					t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
					t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
					t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
					t_anio      = (String)hTitulo.get(ScsDesignaBean.C_ANIO);
					t_numero    = (String)hTitulo.get(ScsDesignaBean.C_CODIGO);
				}
%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>
					/
					<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- 
					<%=UtilidadesString.mostrarDatoJSP(t_nombre)%> 
					<%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> 
					<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>
	
	<table width="100%" border="0">
		<tr>
			<td>
				<siga:Table 
			   		name="tablaDatos"
			   		border="1"
			   		columnNames="censo.resultadosSolicitudesModificacion.literal.fecha,gratuita.busquedaSOJ.literal.nColegiado,gratuita.defendidosDesigna.literal.nombreApellidos,gratuita.cambiosProcuradoresDesigna.literal.fechaRenuncia,"
			   		columnSizes="15,20,45,10,10"
			   		modal="M">
			   
<% 
					if (obj==null || obj.size()==0) {
%>
						<tr class="notFound">
							<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
						</tr>
<%
					} else {
						int recordNumber=1;
						while ((recordNumber) <= obj.size()){	 
							Hashtable hash = (Hashtable)obj.get(recordNumber-1);

							String fDesigna=(String)hash.get("FECHADESIGNA");
							String fRenuncia=(String)hash.get("FECHARENUNCIA");
							String idInstitucionOrigen=(String)hash.get("IDINSTITUCIONORIGEN");
							String ncolegiado=(String)hash.get("NCOLEGIADO");
							String ncolegiadoOrigen  = (String) hash.get("NCOLEGIADOORIGEN");
							String institucionOrigen ="";
							if(idInstitucionOrigen!=null && !idInstitucionOrigen.equals("")) {
								institucionOrigen = CenVisibilidad.getAbreviaturaInstitucion(idInstitucionOrigen);
								if (ncolegiadoOrigen==null || ncolegiadoOrigen.equals("")) { // No colegiado 
									ncolegiado=" ";
								} else {
									ncolegiado=ncolegiadoOrigen+ " ( "+institucionOrigen+" )";
								}	
								
							} else {
								if (ncolegiado==null || ncolegiado.equals("")) // No colegiado 
									ncolegiado=" ";
							}
				
							String rn=String.valueOf(recordNumber);
							boton=(fRenuncia==null || fRenuncia.equals("")?"C,E":"C");
%>	
				  			<siga:FilaConIconos fila='<%=rn%>' botones="<%=boton%>" visibleEdicion="no" visibleBorrado="no" clase="listaNonEdit"  modo="<%=modo%>">
								<td>
						    		<input type='hidden' name='oculto<%=rn%>_1' value='<%=hash.get("IDPERSONA")%>'>
						    		<input type='hidden' name='oculto<%=rn%>_2' value='<%=ncolegiado%>'>
									<input type='hidden' name='oculto<%=rn%>_3' value='<%=fDesigna%>'>
<%
									if(fDesigna!=null && !fDesigna.equals("")) {
%>
										<%=GstDate.getFormatedDateShort("",fDesigna)%>
<%
									}
%>
								</td>
								<td>&nbsp;<%=ncolegiado%></td>
								<td>&nbsp;<%=hash.get("NOMBRE")+" "+hash.get("APELLIDOS1")+" "+hash.get("APELLIDOS2")%></td>
								<td>&nbsp;
<%
									if (fRenuncia!=null && !fRenuncia.equals("")) {
%>
										<%=GstDate.getFormatedDateShort("",fRenuncia)%>
<%
									}
%>
								</td>
							</siga:FilaConIconos>	
<%
							recordNumber++;
						}
					}
%>	
				</siga:Table>
			</td>
		</tr>
	</table>
	
	<div style="position:absolute; left:150px;bottom:5px;z-index:2;">
		<table align="center" border="0">
			<tr>
				<td class="labelText">
					<%if(botonNuevo){%>
						<siga:Idioma key="gratuita.cambiosLetradoDesigna.art27.texto1"/>
					<%}else{%>
						<siga:Idioma key="gratuita.cambiosLetradoDesigna.art27.texto2"/>
					<%}%>
				</td>
			</tr>
		</table>
	</div>
	<!-- FIN: LISTA DE VALORES -->
	
	<script language="JavaScript">
	
		function accionNuevo() {	
			document.forms[0].target="submitArea";
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if(resultado=='MODIFICADO') buscar();
		}
		
		function buscar() {
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value="";
			document.forms[0].submit();
		}
		
		function refrescarLocal() {
			buscar();
		}
	</script>		

	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>