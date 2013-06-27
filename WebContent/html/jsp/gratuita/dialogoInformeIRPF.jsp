<!-- dialogoInformeIRPF.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CenColegiadoBean"%>
<%@ page import="java.util.ArrayList"%>

<!-- JSP -->
<%  
	UsrBean userBean = ((UsrBean)session.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	
	ArrayList alElementoSelec = new ArrayList();
	alElementoSelec.add(userBean.getLanguage());
	String idPersona =(String)request.getAttribute("idPersona");
	String desdeFicha=(String)request.getAttribute("desdeFicha");
	String anyoIRPF=(String)request.getAttribute("anyoIRPF");
	String botones="COM";
	if (desdeFicha!=null && !desdeFicha.equals("")){
		botones+=",C";
	}
	String informeUnico =(String) request.getAttribute("informeUnico");
	String idInstitucionIdioma[] = {idInstitucion};
%>

<html>

<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<script src="<html:rewrite page="/html/js/validacionStruts.js"/>"
		type="text/javascript"></script>
		
	<siga:Titulo 	titulo="menu.sjcs.informes.certificadoIRPF"
					localizacion="factSJCS.informes.ruta" />

</head>

<body>
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
<!-- <input type="hidden" id = "enviar" value = "=desdeFicha!=null && !desdeFicha.equals("")?"0":"1"%>"/> -->
<!-- TITULO -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma
			key="gratuita.retencionesIRPF.informe.titulo.gestionInforme" /></td>
	</tr>
</table>

<table class="tablaCentralCamposPeque" align="center">
	
	
	<html:javascript formName="informeRetencionesIRPFForm"
		staticJavascript="false" />
	
	<html:form action="/JGR_PestanaRetencionesIRPF.do" method="POST"
		target="submitArea">
		
		<html:hidden name="RetencionesIRPFForm" property="modo" />
		<html:hidden name="RetencionesIRPFForm" property="idInstitucion" value="<%=idInstitucion%>" />
		<html:hidden name="RetencionesIRPFForm" property="idPersona" value="<%=idPersona%>" />

		<tr>
			<td>
			<fieldset>
			<table class="tablaCampos" align="center">

				<tr>
					<td class="labelText"><siga:Idioma
						key="gratuita.retencionesIRPF.informe.literal.periodo" />&nbsp;(*)</td>
					<td><siga:ComboBD nombre="periodo" tipo="cmbPeriodo"
						clase="boxCombo" obligatorioSinTextoSeleccionar="true" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
						key="gratuita.retencionesIRPF.informe.literal.anyo" />&nbsp;(*)</td>
					<td><html:text name="RetencionesIRPFForm"
						property="anyoInformeIRPF" styleClass="box" style="width:40px" value="<%=anyoIRPF%>">
					</html:text></td>
					<td width="70%">&nbsp;</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
						key="gratuita.retencionesIRPF.informe.literal.idioma" />&nbsp;(*)</td>
					<td><siga:ComboBD nombre="idioma" tipo="cmbIdiomaInstitucion" parametro="<%=idInstitucionIdioma%>"
						clase="boxCombo" obligatorioSinTextoSeleccionar="true"
						elementoSel="<%=alElementoSelec%>" /></td>
					<td>&nbsp;</td>
				</tr>

			</table>
			</fieldset>
			</td>
		</tr>

	</html:form>

</table>

<table class="botonesDetalle" align="center">
<tr>
	<td  style="width:900px;">&nbsp;</td>
	<td class="tdBotones">
		<input type="button" alt="<siga:Idioma key="general.boton.comunicar"/>"  id="idButton" onclick="return accionComunicar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.comunicar"/>">
	</td>
	<%if (desdeFicha!=null && !desdeFicha.equals("")){%>
	<td class="tdBotones">
		<input type="button" alt="<siga:Idioma key="general.boton.close"/>"  id="idButton" onclick="return accionCerrar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.close"/>">
	</td>
	<%}%>
</tr>
</table>

	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="IRPF"/>
	 
	<html:hidden property="enviar" value='<%=(desdeFicha!=null && !desdeFicha.equals(""))?"0":"1"%>' />
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme" styleId="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	
	<input type='hidden' name='actionModal'>
</html:form>	
<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" id="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" id="tablaDatosDinamicosD" value="">

</form>

<!-- INICIO: SCRIPTS BOTONES -->
<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
<script language="JavaScript">

		function refrescarLocal()
		{
		}
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}
		
		function accionComunicar()
		{
			sub();
			if (!validateInformeRetencionesIRPFForm(document.RetencionesIRPFForm)) {
				fin();
				return false;
			}
			
			idPersona = document.RetencionesIRPFForm.idPersona.value;
			periodo = document.RetencionesIRPFForm.periodo.value;
			anyoInformeIRPF = document.RetencionesIRPFForm.anyoInformeIRPF.value;
			idInstitucion = document.RetencionesIRPFForm.idInstitucion.value;
			idioma = document.RetencionesIRPFForm.idioma.value;
		   	datos = "idPersona=="+idPersona + "##periodo==" +periodo + "##anyoInformeIRPF==" +anyoInformeIRPF + "##idInstitucion==" +idInstitucion + "##idioma==" +idioma +"%%%";
		   	
		
			// document.InformesGenericosForm.datosInforme.value =datos;
			$("#datosInforme").val(datos);
			
			if(document.getElementById("informeUnico").value=='1'){
				document.forms["InformesGenericosForm"].submit();
			}else{
			
			
				var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
				if (arrayResultado==undefined||arrayResultado[0]==undefined){
					fin();
				   		
			   	} 
			   	else {
			   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
			   		if(confirmar){
			   			accionCerrar();
			   			var idEnvio = arrayResultado[0];
					    var idTipoEnvio = arrayResultado[1];
					    var nombreEnvio = arrayResultado[2];				    
					    
					   	document.forms["DefinirEnviosForm"].tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
					   	document.forms["DefinirEnviosForm"].modo.value='editar';
					   	document.forms["DefinirEnviosForm"].submit();
			   		}else{
			   			fin();
			   		}
			   	}
			}
		}
		
	</script>

<!-- SUBMIT AREA: Obligatoria en todas las páginas-->
<iframe name="submitArea"
	src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
	style="display: none"></iframe>

</body>

</html>
