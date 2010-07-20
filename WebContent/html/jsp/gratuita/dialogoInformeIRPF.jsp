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
	String desdeFicha=(String)request.getAttribute("desdeFicha");
	String anyoIRPF=(String)request.getAttribute("anyoIRPF");
	String botones="EN,GF";
	if (desdeFicha!=null && !desdeFicha.equals("")){
		botones+=",C";
	}
%>

<html>

<head>

<link id="default" rel="stylesheet" type="text/css"
	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page="/html/js/SIGA.js"/>"
	type="text/javascript"></script>

<!-- Calendario -->
<script src="<html:rewrite page="/html/js/calendarJs.jsp"/>"
	type="text/javascript"></script>

<!-- VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
<script src="<html:rewrite page="/html/js/validacionStruts.js"/>"
	type="text/javascript"></script>

</head>

<body>

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
		<html:hidden name="RetencionesIRPFForm" property="idInstitucion"
			value="<%=idInstitucion%>" />
		<html:hidden name="RetencionesIRPFForm" property="idPersona" />

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
					<td><siga:ComboBD nombre="idioma" tipo="cmbIdioma"
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

<siga:ConjBotonesAccion botones="<%=botones%>" modal="P" />

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
		
		function generarFichero()
		{
			enviarFormulario("0");
		}
		function accionEnviar()
		{
			enviarFormulario("1");
		}
		function enviarFormulario(enviar) {
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
		   	
			var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
			formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucion %>'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='IRPF'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='" + enviar + "'>"));
			
			document.appendChild(formu);
			formu.datosInforme.value=datos;
			formu.submit();
		}
	</script>

<!-- SUBMIT AREA: Obligatoria en todas las p�ginas-->
<iframe name="submitArea"
	src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
	style="display: none"></iframe>

</body>

</html>
