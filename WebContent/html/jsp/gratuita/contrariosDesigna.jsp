<!DOCTYPE html>
<html>
<head>
<!-- contrariosDesigna.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.ContrariosDesignasForm"%>
<%@ page import="com.siga.gratuita.action.PersonaJGAction"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.general.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)request.getAttribute("resultado");
	
	String modo = (String) ses.getAttribute("Modo");
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Hashtable designaActual = (Hashtable) ses.getAttribute("designaActual");
	
	String anio="",numero="", idturno="";	
	anio = (String)designaActual.get("ANIO");
	numero = (String)designaActual.get("NUMERO");
	idturno = (String)designaActual.get("IDTURNO");	
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="gratuita.contrariosDesigna.literal.titulo" localizacion="gratuita.contrariosDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">

	<html:form action="/JGR_ContrariosDesignasPerJG.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" value="abrirPestana">
		
		<input type="hidden" id="idInstitucionJG"  name="idInstitucionJG" value="<%=usr.getLocation() %>">
		<input type="hidden" id="idPersonaJG" name="idPersonaJG" value="">

		<input type="hidden" id="idInstitucionDES" name="idInstitucionDES" value="<%=usr.getLocation() %>">
		<input type="hidden" id="idTurnoDES" name="idTurnoDES" value="<%=idturno %>">
		<input type="hidden" id="anioDES" name="anioDES" value="<%=anio %>">
		<input type="hidden" id="numeroDES" name="numeroDES" value="<%=numero %>">

		<input type="hidden" id="conceptoE" name="conceptoE" value="<%=PersonaJGAction.DESIGNACION_CONTRARIOS %>">
		<input type="hidden" id="tituloE" name="tituloE" value="gratuita.contrariosDesigna.literal.titulo">
		<input type="hidden" id="localizacionE" name="localizacionE" value="">
		<input type="hidden" id="accionE"  name="accionE" value="nuevo">
		<input type="hidden" id="actionE" name="actionE" value="/JGR_ContrariosDesignasPerJG.do">
		<input type="hidden" id="pantallaE" name="pantallaE" value="M">
	</html:form>	
	
	
	<html:form action="/JGR_ContrariosDesignas.do" method="post" target="mainPestanas" style="display:none" styleId="formVolver">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
	</html:form>
	
	<html:form action="/JGR_ContrariosDesignasPerJG.do" method="post" target="submitArea" styleId="formNuevo">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirPestana">
		
		<input type="hidden" name="idInstitucionJG" value="<%=usr.getLocation() %>">
		<input type="hidden" name="idPersonaJG" value="">

		<input type="hidden" name="idInstitucionDES" value="<%=usr.getLocation() %>">
		<input type="hidden" name="idTurnoDES" value="<%=idturno %>">
		<input type="hidden" name="anioDES" value="<%=anio %>">
		<input type="hidden" name="numeroDES" value="<%=numero %>">

		<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.DESIGNACION_CONTRARIOS %>">
		<input type="hidden" name="tituloE" value="gratuita.contrariosDesigna.literal.titulo">
		<input type="hidden" name="localizacionE" value="">
		<input type="hidden" name="accionE" value="nuevo">
		<input type="hidden" name="actionE" value="/JGR_ContrariosDesignasPerJG.do">
		<input type="hidden" name="pantallaE" value="M">						
	</html:form>
	
	<table class="tablaTitulo" cellspacing="0" heigth="310">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
						ScsDesignaAdm adm = new ScsDesignaAdm (usr);
						Hashtable hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(), anio, numero,idturno);

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
	   columnNames="gratuita.defendidosDesigna.literal.nif,gratuita.defendidosDesigna.literal.nombreApellidos,envios.etiquetas.tipoCliente.abogado,gratuita.personaJG.literal.procurador,"
	   columnSizes="10,30,30,20,10"
	   modal="G">

		<% if (obj==null || obj.size()==0){%>
 			<tr class="notFound">
		   		<td class="titulitos">
		   			<siga:Idioma key="messages.noRecordFound"/>
		   		</td>
			</tr>
			
		<% } else {
	    	int recordNumber=1;
			while ((recordNumber) <= obj.size()){	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				
				String infoOrigen = "";	
				String idInstitucionOrigen=(String)hash.get("IDINSTITUCIONORIGEN");
				String ncolegiadoOrigen  = (String) hash.get("NCOLEGIADOORIGEN");
				String institucionOrigen ="";
				if(idInstitucionOrigen!=null && !idInstitucionOrigen.equals("")) {
					institucionOrigen = CenVisibilidad.getAbreviaturaInstitucion(idInstitucionOrigen);
					if (ncolegiadoOrigen==null || ncolegiadoOrigen.equals("")) { // No colegiado 
						infoOrigen = "";
					} else {
						infoOrigen = " - " + ncolegiadoOrigen+ " ("+institucionOrigen+")";
					}	
					
				}
		 %>	
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="E,C,B" clase="listaNonEdit" modo="<%=modo%>">						
										
				
					<td>
					<!--<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDPERSONA")%>'> -->				
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=PersonaJGAction.DESIGNACION_CONTRARIOS%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="gratuita.contrariosDesigna.literal.titulo">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="gratuita.contrariosDesigna.literal.titulo">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="editar">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=hash.get("IDPERSONA")%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_7" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_8" value="<%=idturno %>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_9" value="<%=anio %>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_10" value="<%=numero %>">	
					&nbsp;<%=hash.get("NIF")%></td>
					<td>&nbsp;<%=hash.get("NOMBRE")%></td>						
					<td>&nbsp;<%=(String)hash.get("NOMBREABOGADOCONTRARIO") + infoOrigen%></td>
					<td>&nbsp;<%=(String)hash.get("PROCURADOR")%></td>
				</siga:FilaConIconos>	
				
			<%recordNumber++;%>
			<%}%>	
		<%}%>
	</siga:Table>
			
	<!-- INICIO: SUBMIT AREA -->
	<script language="JavaScript">

		// Asociada al boton Volver
		function accionVolver() {	
			var formVolver = document.getElementById("formVolver");
			formVolver.target = "mainWorkArea";	
			formVolver.action="JGR_Designas.do";
			formVolver.modo.value="volverBusqueda";
			formVolver.submit();
		}			
		
		function accionNuevo() {
			var formVolver = document.getElementById("formVolver");
			formVolver.modo.value="nuevo";
			formVolver.target="submitArea";
			var resultado=ventaModalGeneral("formNuevo","G");
			if (resultado=="MODIFICADO"){
				buscar();
			}
		}
		
		function buscar(){
			formVolver.target="mainPestanas";
			formVolver.modo.value="";
			formVolver.submit();
		}

		function refrescarLocal(){
			buscar();
		}	
	</script>		

	<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" modo="<%=modo%>" />

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/JGR_ContrariosDesignasPerJG.do" method="post" target="submitArea">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirPestana">
		
		<input type="hidden" name="idInstitucionJG" value="<%=usr.getLocation() %>">
		<input type="hidden" name="idPersonaJG" value="">

		<input type="hidden" name="idInstitucionDES" value="<%=usr.getLocation() %>">
		<input type="hidden" name="idTurnoDES" value="<%=idturno %>">
		<input type="hidden" name="anioDES" value="<%=anio %>">
		<input type="hidden" name="numeroDES" value="<%=numero %>">

		<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.DESIGNACION_CONTRARIOS %>">
		<input type="hidden" name="tituloE" value="gratuita.contrariosDesigna.literal.titulo">
		<input type="hidden" name="localizacionE" value="">
		<input type="hidden" name="accionE" value="nuevo">
		<input type="hidden" name="actionE" value="/JGR_ContrariosDesignasPerJG.do">
		<input type="hidden" name="pantallaE" value="M">						
	</html:form>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>