<!DOCTYPE html>
<html>
<head>
<!-- listadoDocumentacionAsistencia.jsp -->
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
<%@ page import="org.redabogacia.sigaservices.app.vo.scs.DocumentacionAsistenciaVo"%>
<%@ page import="com.siga.gratuita.form.DefinirDocumentacionAsistenciaForm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
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
	
	DefinirDocumentacionAsistenciaForm form = (DefinirDocumentacionAsistenciaForm)request.getAttribute("DefinirDocumentacionAsistenciaForm");
	
	String idInstitucion = usr.getLocation();
	String anio = form.getAnio();
	String numero	= form.getNumero();
	
	ArrayList obj = (ArrayList) request.getAttribute("resultado");
	String accion = (String)request.getSession().getAttribute("accion");
		
	String botonesFila="";
	
	String	botones="V,N";
	if (accion.equalsIgnoreCase("ver")){
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
		titulo="gratuita.busquedaEJG.documentacion" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body class="tablaCentralCampos">	
	<html:form action="/JGR_DocumentacionAsistencia" method="post" target="mainPestanas" style="display:none">
		<html:hidden property = "modo" value = ""/>	
		<html:hidden styleId="idInstitucion" property = "idInstitucion" />
		<html:hidden styleId="anio" property = "anio"/>
		<html:hidden styleId="numero" property = "numero"/>
	</html:form>	
		
		<tr>				
	<td width="100%" align="center">

		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (usr);
					Hashtable hTitulo = adm.getTituloPantallaAsistencia(idInstitucion, anio, numero);
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

		<siga:Table 		   
		   name="listadoDocumentacion"
		   border="1"
		   columnNames="gratuita.documentacionAsistencia.fechaentrada,gratuita.documentacionAsistencia.nombre, "
		   columnSizes="10,50,10"
		   modal="M">
		   
  	<% if (obj != null && obj.size()>0){
	    	int recordNumber=1;
	    	while (recordNumber-1 < obj.size())	{	
	    		
	    		DocumentacionAsistenciaVo fila = (DocumentacionAsistenciaVo) obj.get(recordNumber-1);
														
			%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botonesFila%>" clase="listaNonEdit" >
					
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIddocumentacionasi()%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdtipodocumento()%>">
						<%=GstDate.getFormatedDateShort(fila.getFechaentrada())%>&nbsp;
					</td>					
					<td><%=fila.getNombreTipoDoc()%></td>
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
	<siga:ConjBotonesAccion botones="<%=botones %>" clase="botonesDetalle" modo="<%=accion%>"/>

<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value ="<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="DEJG"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>	

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
				
		//Asociada al boton Volver
		function accionVolver() {
			document.forms[0].target="mainWorkArea";
			document.forms[0].action = "/SIGA/JGR_Asistencia.do";
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