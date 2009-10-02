<!-- dialogoInformeIRPF.jsp -->
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

<!-- IMPORTS -->

<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@page import="com.siga.beans.CenColegiadoBean"%>
<!-- JSP -->
<%  
	//CenColegiadoBean beanCol = (CenColegiadoBean)request.getAttribute("colegiado");	
//	String nombre = (String)request.getAttribute("nombre");
	//String idInstitucion = beanCol.getIdInstitucion().toString(); 
	
	UsrBean userBean = ((UsrBean)session.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	
	ArrayList alElementoSelec = new ArrayList();
	alElementoSelec.add(userBean.getLanguage());
	String desdeFicha=(String)request.getAttribute("desdeFicha");
	String botones="EN,GF";
	if (desdeFicha!=null && !desdeFicha.equals("")){
		botones+=",C";
	}
	

	  

%>	

<%@page import="java.util.ArrayList"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>


	<!-- Calendario -->
	<script src="<html:rewrite page="/html/js/calendarJs.jsp"/>" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
			
	  	<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
		<siga:Idioma key="gratuita.retencionesIRPF.informe.titulo.gestionInforme" />
			
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

<table class="tablaCentralCamposPeque" align="center">
	<html:javascript formName="informeRetencionesIRPFForm" staticJavascript="false" />
	<html:form action="/JGR_PestanaRetencionesIRPF.do" method="POST" target="submitArea">
		<html:hidden name="RetencionesIRPFForm" property="modo"  />
		<html:hidden name="RetencionesIRPFForm" property="idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden name="RetencionesIRPFForm" property="idPersona" />

		<!-- FILA -->
		<tr>
			<td>

			<fieldset>

			<table class="tablaCampos" align="center">

				<!-- FILA -->
				<tr>

					<td class="labelText" ><siga:Idioma
						key="gratuita.retencionesIRPF.informe.literal.anyo" />&nbsp;(*)</td>
					<td ><html:text name="RetencionesIRPFForm" 
						property="anyoInformeIRPF" styleClass="box" style="width:40px">
					</html:text></td>
					<td width="70%">&nbsp;</td>


				</tr>
				<tr>
					<td class="labelText"><siga:Idioma
						key="gratuita.retencionesIRPF.informe.literal.idioma" />&nbsp;(*)</td>
					<td><siga:ComboBD nombre="idioma" tipo="cmbIdioma"
						clase="boxCombo"  obligatorioSinTextoSeleccionar="true" elementoSel="<%=alElementoSelec%>"/></td>
					<td>&nbsp;</td>
				</tr>
				


			</table>

			</fieldset>


			</td>
		</tr>

	</html:form>

</table>

<!-- Formulario para la creacion de envio -->
	

<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

<!-- INICIO: BOTONES REGISTRO -->




<siga:ConjBotonesAccion botones="<%=botones%>" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

 
		//Asociada al boton GuardarCerrar -->
		

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}

		function generarFichero() {
		
		   
			
				
			sub();
			if (!validateInformeRetencionesIRPFForm(document.RetencionesIRPFForm)){
				fin();
				return false;
			
			}	
			idPersona = document.RetencionesIRPFForm.idPersona.value;
			anyoInformeIRPF = document.RetencionesIRPFForm.anyoInformeIRPF.value;
			idInstitucion = document.RetencionesIRPFForm.idInstitucion.value;
			idioma = document.RetencionesIRPFForm.idioma.value;
		   	datos = "idPersona=="+idPersona + "##anyoInformeIRPF==" +anyoInformeIRPF + "##idInstitucion==" +idInstitucion + "##idioma==" +idioma +"%%%";
			
			var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
			formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucion %>'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='IRPF'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='0'>"));
			document.appendChild(formu);
			formu.datosInforme.value=datos;
			formu.submit();
		} 	
		function accionEnviar()
		{
		
			sub();
			if (!validateInformeRetencionesIRPFForm(document.RetencionesIRPFForm)){
				fin();
				return false;
			
			}	
			idPersona = document.RetencionesIRPFForm.idPersona.value;
			anyoInformeIRPF = document.RetencionesIRPFForm.anyoInformeIRPF.value;
			idInstitucion = document.RetencionesIRPFForm.idInstitucion.value;
			idioma = document.RetencionesIRPFForm.idioma.value;
		   	
		   	
		   	datos = "idPersona=="+idPersona + "##anyoInformeIRPF==" +anyoInformeIRPF + "##idInstitucion==" +idInstitucion + "##idioma==" +idioma +"%%%"; 
			
			var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
			formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucion %>'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='IRPF'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
			
			document.appendChild(formu);
			formu.datosInforme.value=datos;
			formu.submit();
			
			
			
      	    					
					
				
		}
		function refrescarLocal(){
			//alert("cierrate:");
			//cierraConParametros("NORMAL");
			//window.close();
		}
		
		
		
		

		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
