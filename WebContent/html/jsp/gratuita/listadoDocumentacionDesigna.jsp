<!DOCTYPE html>
<html>
<head>
<!-- listadoDocumentacionDesigna.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="org.redabogacia.sigaservices.app.vo.scs.DocumentacionDesignaVo"%>
<%@ page import="com.siga.gratuita.form.DefinirDocumentacionDesignaForm"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>


<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	DefinirDocumentacionDesignaForm form = (DefinirDocumentacionDesignaForm)request.getAttribute("DefinirDocumentacionDesignaForm");
	
	String idInstitucion = usr.getLocation();
	String anio = form.getAnio();
	String numero	= form.getNumero();
	String idTurno	= form.getIdTurno();
	
	ArrayList obj = (ArrayList) request.getAttribute("resultado");
	String modo = (String) ses.getAttribute("Modo");
	
		
	String botonesFila="";
	
	String	botones="V,N";
	if (modo.equalsIgnoreCase("ver")){
		botonesFila = "C";
	}else {
		botonesFila = "C,E,B";
	}

%>

<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="gratuita.listadoSubzonas.literal.listadoSubzonas"/></title>
	<script type="text/javascript">
		function refrescarLocal(){
			buscar();
		}
	</script>
	<siga:Titulo 
		titulo="gratuita.designacion.documentacion" 
		localizacion="gratuita.designacion.localizacion"/>
</head>

<body class="tablaCentralCampos">	
	<html:form action="/JGR_DocumentacionDesigna" method="post" target="mainPestanas" style="display:none">
		<html:hidden property = "modo" value = ""/>	
		<html:hidden styleId="idInstitucion" property = "idInstitucion" />
		<html:hidden styleId="anio" property = "anio"/>
		<html:hidden styleId="numero" property = "numero"/>
		<html:hidden styleId="idTurno" property = "idTurno"/>
	</html:form>	
		
		<tr>				
	<td width="100%" align="center">

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
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>

		<siga:Table 		   
		   name="listadoDocumentacion"
		   border="1"
		   columnNames="gratuita.documentacionAsistencia.fechaentrada,sjcs.ejg.documentacion.tipoDocumentacion,gratuita.documentacionDesigna.actuacion"
		   columnSizes="10,40,40,10"
		   modal="M">
		   
  	<% if (obj != null && obj.size()>0){
	    	int recordNumber=1;
	    	while (recordNumber-1 < obj.size())	{	
	    		
	    		DocumentacionDesignaVo fila = (DocumentacionDesignaVo) obj.get(recordNumber-1);
														
			%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botonesFila%>" clase="listaNonEdit" >
					
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIddocumentaciondes()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdtipodocumento()%>">
						<%=GstDate.getFormatedDateShort(fila.getFechaentrada())%>&nbsp;
					</td>					
					<td><%=fila.getNombreTipoDoc()%></td>
					<td><%=fila.getDescripcionActuacion()%>&nbsp;</td>
					
				</siga:FilaConIconos>		
		<% recordNumber++;		   
		} %>
	<%
	}else {
	%>
	 	<tr class="notFound">
		   	<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
		</tr>
	<%
	}
	%>
	</siga:Table>	


	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="<%=botones %>" clase="botonesDetalle" modo="<%=modo%>"/>

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
				
		//Asociada al boton Volver
		function accionVolver() {
			document.forms[0].target="mainWorkArea";
			document.forms[0].action = "/SIGA/JGR_Designas.do";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();
		}
		
		//Asociada al boton Cerrar
		function accionGuardar(){
			document.forms[0].submit();
		}
		
		function accionNuevo(){
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "mainPestanas";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if(resultado=='MODIFICADO') 
				buscar();
		}
		
		function buscar(){
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}
		
	
	</script>

	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	

</body>	
</html>