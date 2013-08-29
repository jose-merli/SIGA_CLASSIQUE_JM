<!DOCTYPE html>
<html>
<head>
<!-- listadoEjgDesigna.jsp -->
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
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String modo = (String)request.getSession().getAttribute("Modo");
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	ScsPersonaJGAdm benefAdm = new ScsPersonaJGAdm(usr);
	Hashtable beneficiario = new Hashtable();
	String consultaBeneficiario = "", descripcionTipo = "", descripcionTipoEjg="", descripcionTurno="", estado="";
	String anio="",numero="", idTurno="";
	Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
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
	<siga:TituloExt 
		titulo="gratuita.ejgDesigna.literal.titulo" 
		localizacion="gratuita.ejgDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body>


	<html:form action="/JGR_EJG.do" method="post" target="mainWorkArea" style="display:none">

		<html:hidden property="modo" value = ""/>
		<html:hidden property="desdeDesigna" value= "si"/>

		<!-- Campo obligatorio -->
		<!-- RGG: cambio a formularios ligeros -->
		
		<input type="hidden" name="actionModal" value="">
	</html:form>	
	
	<html:form action="/JGR_EJG_Designa.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		
		<html:hidden property = "numero" value="<%=numero%>"/>
		<html:hidden property = "anio" value="<%=anio%>"/>
		<html:hidden property = "idTurno" value="<%=idTurno%>"/>

		<html:hidden property = "ejgIdInstitucion" value=""/>
		<html:hidden property = "ejgAnio" value=""/>
		<html:hidden property = "ejgNumero" value=""/>
		<html:hidden property = "ejgIdTipoEjg" value=""/>
		<html:hidden property ="desdeEJG"      value= "si"/>
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
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
					
					
			</td>
		</tr>
		</table>


			<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="gratuita.listadoAsistencias.literal.anio,gratuita.ejgDesigna.literal.tipoEjg,gratuita.listadoAsistencias.literal.numero,gratuita.listaTurnosLetrados.literal.turno,pestana.tiposexpedientes.estado,gratuita.ejgDesigna.literal.beneficiario,"
			   columnSizes="5,20,10,25,10,20,10">
		
		<% if (obj==null || obj.size()==0){%>
	 		<tr class="notFound">
	   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		<%}else{%>
		
		
		
		
  <%
		  int recordNumber=1;
			while ((recordNumber) <= obj.size()){	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
					descripcionTipoEjg = (String)hash.get("DESCRIPCIONTIPOEJG");
					if (descripcionTipoEjg!=null){
						descripcionTipo = ((descripcionTipoEjg.length()>25)?descripcionTipoEjg.substring(0,25):descripcionTipoEjg);
					}
					descripcionTurno = (String)hash.get("TURNO");
					descripcionTurno = ((descripcionTurno.length()>25)?descripcionTurno.substring(0,25):descripcionTurno);
					estado = (String)hash.get("ESTADO");
					estado = ((estado.length()>20)?estado.substring(0,20):estado);
					
					String anioEJG          = (String)hash.get("ANIOEJG");
					String idTipoEJG        = (String)hash.get("IDTIPOEJG");
					String numeroEJG        = (String)hash.get("NUMEROEJG");
					String idInstitucionEJG = (String)hash.get("IDINSTITUCION");
	%>	

						<siga:FilaConIconos fila="<%=String.valueOf(recordNumber)%>" botones="C,E, B" clase="listaNonEdit" visibleBorrado="no" visibleEdicion="no"  modo="<%=modo%>"	>

				  	<%	consultaBeneficiario = " select (per.nombre||' '||per.apellido1||' '||per.apellido2) beneficiario  "+
														" from scs_personajg per "+
														" where per.idinstitucion ="+ (String)usr.getLocation()+
														" and per.idpersona = (select uni.idpersonajg"+
									                    " from scs_ejg uni"+
                      			                        " where uni.idtipoejg = "+(String)hash.get("IDTIPOEJG")+
                      			                        " and uni.idinstitucion="+(String)usr.getLocation()+
								                        " and uni.anio = "+(String)hash.get("ANIOEJG")+
								                        " and uni.numero = "+(String)hash.get("NUMEROEJG")+
								                        " )";
						try{
							beneficiario = (Hashtable)((Vector)benefAdm.selectGenerico(consultaBeneficiario)).get(0);
						}catch(Exception e){
							beneficiario.put("BENEFICIARIO"," ");
						}
				  	%>
						<td >
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=idTipoEJG%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=idInstitucionEJG%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=anioEJG%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=numeroEJG%>">
							&nbsp;<%=(String)hash.get("ANIOEJG")%>
						</td>
						<td >&nbsp;<%=descripcionTipo%></td>
						<td >&nbsp;<%=(String)hash.get("CODIGO")%></td>
						<td >&nbsp;<%=descripcionTurno%></td>
						<td >&nbsp;<%=estado%></td>
						<td >&nbsp;<%=(String)beneficiario.get("BENEFICIARIO")%></td>
					</siga:FilaConIconos>

				<%recordNumber++;%>
				<%}%>	
		<%}%>
			</siga:Table>
	

	<script>
	
	
	 function borrar(fila) {
	   var datos;
	   if (confirm('¿Está seguro de que desea eliminar el registro?')){
	   	datos = document.getElementById('tablaDatosDinamicosD');
	
		var oculto = 'oculto' + fila + '_' + 1;
      	var tipo = document.getElementById(oculto);
      	var oculto = 'oculto' + fila + '_' + 2;
      	var institucion = document.getElementById(oculto);
      	var oculto = 'oculto' + fila + '_' + 3;
      	var anioejg = document.getElementById(oculto);
      	var oculto = 'oculto' + fila + '_' + 4;
      	var numeroejg = document.getElementById(oculto);
	 		
		document.forms[1].ejgIdInstitucion.value = institucion.value;
		document.forms[1].ejgAnio.value = anioejg.value;
		document.forms[1].ejgNumero.value = numeroejg.value;		
		document.forms[1].ejgIdTipoEjg.value = tipo.value;	
 
	   	document.forms[1].target="submitArea";
	   	document.forms[1].modo.value = "borrarRelacionConEJG";
	   	document.forms[1].submit();
	 	}
 }
	
	</script>
	
</body>
</html>