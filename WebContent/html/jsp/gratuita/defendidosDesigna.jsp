<!DOCTYPE html>
<html>
<head>
<!-- defendidosDesigna.jsp -->

<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
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
<%@ page import="com.siga.gratuita.form.DefendidosDesignasForm"%>
<%@ page import="com.siga.gratuita.action.PersonaJGAction"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	HttpSession ses = request.getSession();
		
	Vector obj = (Vector)request.getAttribute("resultado");
	
	String modo = (String) ses.getAttribute("Modo");
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
	
	String anio="",numero="", idturno="",idInstitucion="";	
	anio = (String)designaActual.get("ANIO");
	numero = (String)designaActual.get("NUMERO");
	idturno = (String)designaActual.get("IDTURNO");	
	idInstitucion = (String)designaActual.get("IDINSTITUCION");
	
	String idInstitucionLocation = usr.getLocation();
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="gratuita.defendidosDesigna.literal.titulo" localizacion="gratuita.defendidosDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">

	<html:form action="/JGR_DefendidosDesignasPerJG.do" method="post" target="submitArea" style="display:none">
			<input type="hidden" id="modo"  name="modo" value="abrirPestana">
			
			<input type="hidden" id="idInstitucionJG" value="<%=idInstitucion %>">
			<input type="hidden" id="idInstitucionJG" name="idPersonaJG" value="">
	
			<input type="hidden" id="idInstitucionDES" name="idInstitucionDES" value="<%=idInstitucion %>">
			<input type="hidden" id="idTurnoDES" name="idTurnoDES" value="<%=idturno %>">
			<input type="hidden" id="anioDES" name="anioDES" value="<%=anio %>">
			<input type="hidden" id="numeroDES"  name="numeroDES" value="<%=numero %>">
	
			<input type="hidden" id="conceptoE" name="conceptoE" value="<%=PersonaJGAction.DESIGNACION_INTERESADO %>">
			<input type="hidden" id="tituloE"  name="tituloE" value="gratuita.defendidosDesigna.literal.titulo">
			<input type="hidden" id="localizacionE"  name="localizacionE" value="">
			<input type="hidden" id="localizacionE"  name="accionE" value="nuevo">
			<input type="hidden" id="actionE" name="actionE" value="/JGR_DefendidosDesignasPerJG.do">
			<input type="hidden" id="pantallaE" name="pantallaE" value="M">
		</html:form>
	
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					ScsDesignaAdm adm = new ScsDesignaAdm (usr);
					Hashtable hTitulo = adm.getTituloPantallaDesigna(idInstitucion, anio, numero,idturno);

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
		   name="listadoDefendidos"
		   border="2"
		   columnNames="gratuita.defendidosDesigna.literal.nif,gratuita.defendidosDesigna.literal.nombreApellidos,gratuita.defendidosDesigna.literal.representante,"
		   columnSizes="10,45,30,15"
		   modal="G">

			<% if (obj==null || obj.size()==0){%>
	 			<tr class="notFound">
			   		<td class="titulitos">
			   			<siga:Idioma key="messages.noRecordFound"/>
			   		</td>
				</tr>
				
			<% } else { 
				int recordNumber=1;
			  	FilaExtElement[] elems = null;
			  
			  	if(!modo.equalsIgnoreCase("ver")){
			 		elems = new FilaExtElement[4];
			 	 	elems[3]=new FilaExtElement("comunicar", "comunicar", SIGAConstants.ACCESS_READ);
			  	} else {
				  	elems = new FilaExtElement[3];  
			  	}			  
			  
				while ((recordNumber) <= obj.size()) {	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			 %>	
				  	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="E,C,B" elementos="<%=elems%>" clase="listaNonEdit" modo="<%=modo%>">						
						<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=PersonaJGAction.DESIGNACION_INTERESADO%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="gratuita.contrariosDesigna.literal.titulo">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="gratuita.contrariosDesigna.literal.titulo">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="editar">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=idInstitucion%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=hash.get("IDPERSONA")%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_7" value="<%=idInstitucion%>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_8" value="<%=idturno %>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_9" value="<%=anio %>">
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_10" value="<%=numero %>">
						&nbsp;<%=hash.get("NIF")%></td>
						<td>&nbsp;<%=hash.get("NOMBRE")%></td>
						<td>&nbsp;<%=(String)hash.get("REPRESENTANTE")%></td>
					</siga:FilaConIconos>	
				<%recordNumber++;%> 
				<%}%>	
		<%}%>
	</siga:Table>
	<!-- FIN: LISTA DE VALORES -->
		
	<html:form action="/JGR_DefendidosDesignas.do" method="post" target="mainPestanas" style="display:none" styleId="formVolver">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
	</html:form>
	
	<html:form action="/JGR_DefendidosDesignasPerJG.do" method="post" target="submitArea" styleId="formNuevo">
		<input type="hidden" name="modo" value="abrirPestana">
		<input type="hidden" name="actionModal" value="">
		
		<input type="hidden" name="idInstitucionJG" value="<%=idInstitucion %>">
		<input type="hidden" name="idPersonaJG" value="">

		<input type="hidden" name="idInstitucionDES" value="<%=idInstitucion%>">
		<input type="hidden" name="idTurnoDES" value="<%=idturno %>">
		<input type="hidden" name="anioDES" value="<%=anio %>">
		<input type="hidden" name="numeroDES" value="<%=numero %>">

		<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.DESIGNACION_INTERESADO %>">
		<input type="hidden" name="tituloE" value="gratuita.defendidosDesigna.literal.titulo">
		<input type="hidden" name="localizacionE" value="">
		<input type="hidden" name="accionE" value="nuevo">
		<input type="hidden" name="actionE" value="/JGR_DefendidosDesignasPerJG.do">
		<input type="hidden" name="pantallaE" value="M">
	</html:form>
	
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
			var resultado=ventaModalGeneral("formNuevo","G");
			if (resultado=="MODIFICADO") {
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
		
		function comunicar(fila) {
			//subicono('iconoboton_comunicar'+fila);
			var idPersonaJG = document.getElementById( 'oculto' + fila + '_6');
			var idInstitucion = document.getElementById( 'oculto' + fila + '_7');
			var idTurno = document.getElementById( 'oculto' + fila + '_8');
			var anio = document.getElementById( 'oculto' + fila + '_9');
			var numero = document.getElementById( 'oculto' + fila + '_10');
			
			datos = "idInstitucion==" +idInstitucion.value +
				"##anio==" + anio.value + 
				"##idTurno==" + idTurno.value +
				"##numero==" +numero.value + 
				"##idPersonaJG==" + idPersonaJG.value +
				"##idTipoInforme==OFICI%%%";

			document.InformesGenericosForm.datosInforme.value=datos;
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined || arrayResultado[0]==undefined){
		
		   	} else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
		   		}
		   	}
		}
		
		function accionCerrar() {		
		}
	</script>		

	<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" modo="<%=modo%>" />

	<html:form action="/JGR_DefendidosDesignasPerJG.do" method="post" target="submitArea">
		<input type="hidden" name="modo" value="abrirPestana">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="idInstitucionJG" value="<%=idInstitucion%>">
		<input type="hidden" name="idPersonaJG" value="">

		<input type="hidden" name="idInstitucionDES" value="<%=idInstitucion %>">
		<input type="hidden" name="idTurnoDES" value="<%=idturno %>">
		<input type="hidden" name="anioDES" value="<%=anio %>">
		<input type="hidden" name="numeroDES" value="<%=numero %>">

		<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.DESIGNACION_INTERESADO %>">
		<input type="hidden" name="tituloE" value="gratuita.defendidosDesigna.literal.titulo">
		<input type="hidden" name="localizacionE" value="">
		<input type="hidden" name="accionE" value="nuevo">
		<input type="hidden" name="actionE" value="/JGR_DefendidosDesignasPerJG.do">
		<input type="hidden" name="pantallaE" value="M">
	</html:form>	
	
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
		<html:hidden property="idInstitucion" value = "<%=idInstitucionLocation%>"/>
		<html:hidden property="idTipoInforme" value="OFICI"/>
		<html:hidden property="enviar" value="1"/>
		<html:hidden property="descargar" value="1"/>
		<html:hidden property="destinatarios" value="S"/>
		<html:hidden property="datosInforme"/>
		<html:hidden property="modo" value = "preSeleccionInformes"/>
		<input type='hidden' name='actionModal'>
	</html:form>
	
	<!-- Formulario para la edicion del envio -->
	<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="tablaDatosDinamicosD" value="">	
	</form>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>