<!DOCTYPE html>
<html>
<head>
<!-- listarCambiosProcuradoresDesigna.jsp -->
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<%	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	request.getSession().removeAttribute("resultado");
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String boton="";
	String modo = (String) ses.getAttribute("Modo");
	String anio="",numero="", idTurno="",idInstitucion ="";
	Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
	anio = (String)designaActual.get("ANIO");
	numero = (String)designaActual.get("NUMERO");
	idTurno = (String)designaActual.get("IDTURNO");
	idInstitucion = (String)designaActual.get("IDINSTITUCION");
	
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="gratuita.cambiosProcuradorDesigna.literal.titulo" 
		localizacion="gratuita.cambiosLetradoDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->


</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="JGR_CambioProcuradorDesigna.do" method="POST" target="mainPestanas" style="display:none">
			<html:hidden property = "modo" value = ""/>
		</html:form>	
		<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%
					    String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
						ScsDesignaAdm adm = new ScsDesignaAdm (usr);
						Hashtable hTitulo = adm.getTituloPantallaDesigna(idInstitucion, anio, numero,idTurno);

						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsDesignaBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsDesignaBean.C_CODIGO);
							
						}
					
					%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
					
					
			</td>
		</tr>
		</table>
		<table width="100%" border ="0"><tr><td>
		<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="censo.resultadosSolicitudesModificacion.literal.fecha,
			   gratuita.cambiosProcuradoresDesigna.literal.numeroDesigna,
			   gratuita.busquedaSOJ.literal.nColegiado,
			   gratuita.defendidosDesigna.literal.nombreApellidos,
			   gratuita.cambiosProcuradoresDesigna.literal.fechaRenuncia,"
			   columnSizes="12,12,12,42,12,10"
			   modal="M">
			   
		<!-- Campo obligatorio -->
	<% if (obj==null || obj.size()==0){%>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<%}else{%>
		<%	int recordNumber=1;
			while ((recordNumber) <= obj.size()){	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				String fDesigna=(String)hash.get("FECHADESIGNA");
				String fRenuncia=(String)hash.get("FECHARENUNCIA");
				String rn=String.valueOf(recordNumber);
				boton=(fRenuncia==null || fRenuncia.equals("")?"C,E":"C");
			%>	
				  	
				  		<siga:FilaConIconos fila='<%=rn%>' botones="<%=boton%>" visibleEdicion="no"  visibleBorrado="no" clase="listaNonEdit" modo="<%=modo%>">
						<td>
							<input type='hidden' name='oculto<%=rn%>_1' value='<%=hash.get("IDINSTITUCION_PROC")%>'>
							<input type='hidden' name='oculto<%=rn%>_2' value='<%=hash.get("IDPROCURADOR")%>'>
							<input type='hidden' name='oculto<%=rn%>_3' value='<%=fDesigna%>'>
							<%if(fDesigna!=null && !fDesigna.equals("")){%>
								<%=GstDate.getFormatedDateShort("",fDesigna)%>
							<%}%>
						</td>
						<td>&nbsp;<%=hash.get("NUMERODESIGNACION")%></td>
						<td>&nbsp;<%=hash.get("NCOLEGIADO")%></td>
						<td>&nbsp;<%=hash.get("NOMBRE")+" "+hash.get("APELLIDOS1")+" "+hash.get("APELLIDOS2")%></td>
						<td>&nbsp;
						<%if(fRenuncia!=null && !fRenuncia.equals("")){%>
							<%=GstDate.getFormatedDateShort("",fRenuncia)%>
						<%}%>
						</td>
					</siga:FilaConIconos>	
			<%recordNumber++;%>
			<%	}
			}%>	
		</siga:Table>
		</td></tr></table>

<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->

<script language="JavaScript">

	
	//Asociada al boton Volver -->
		
		function accionNuevo() 
		{	
			document.forms[0].target="submitArea";
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if(resultado=='MODIFICADO')buscar();
		}
		
		function buscar()
		{
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value="";
			document.forms[0].submit();
		}
		
		function refrescarLocal()
		{
			buscar();
		}
				
</script>		

<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>