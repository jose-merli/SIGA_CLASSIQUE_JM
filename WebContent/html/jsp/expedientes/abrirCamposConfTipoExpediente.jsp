<!-- abrirCamposConfTipoExpediente.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUE�A -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES: 
-->

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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.ExpCampoConfBean"%>
<%@ page import="com.siga.expedientes.form.CamposConfigurablesForm"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%  

	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	CamposConfigurablesForm form = (CamposConfigurablesForm) request.getAttribute("camposConfigurablesForm");
 	boolean bLectura=(form.getAccion().equals("ver"))?true:false;

	Vector vDatos = (Vector) request.getAttribute("datos");

 	
 	String botonesAccion = "N,C";
 	String botonesRegistro = "E,B";
 	if (bLectura) {
 		botonesAccion = "C";
 		botonesRegistro = "";
 	}
 	
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="camposConfigurablesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">		
	
			<!-- Refresco -->
			<!-- esta funci�n es llamada desde exito.jsp tras mostrar el mensaje de �xito --!
			function refrescarLocal() 
			{		
				parent.document.forms[0].submit();
			}			
			
			var bLectura = <%=""+bLectura %>;
			
			<!-- Asociada al boton Volver -->
			function accionCerrar() 
			{		
				window.close();
			}
			
	
			<!-- Asociada al boton Guardar -->
			function accionNuevo(){
				var numeroCampos=<%=vDatos!=null?vDatos.size():0%>;

				if (numeroCampos>=5){
					alert('<siga:Idioma key="messages.expedientes.limiteCamposSuperado.error"/>');
					return false;
				}
				sub();		
				document.camposConfigurablesForm.modo.value = "nuevo";
	 			var resultado=ventaModalGeneral(document.camposConfigurablesForm.name,"P");
	 			refrescarLocal();
	 			fin();
			}



		</script>
</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<html:form action="/EXP_TiposExpedientes_CamposConf.do" method="POST" target="submitArea">
				<html:hidden property = "hiddenFrame" value = "1"/>
				<input type="hidden" name="tablaDatosDinamicosD">
				<html:hidden property = "actionModal" value = ""/>
				<html:hidden property = "idInstitucion" />
				<html:hidden property = "accion" />
				<html:hidden property = "idTipoExpediente" />
				<html:hidden property = "idCampo" />
				<html:hidden property = "modo" />
				<html:hidden property = "idPestanaConf" />
				<html:hidden property = "nombre" />
				<html:hidden property = "orden" />
		</html:form>
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.literal.tiposexpedientes.camposConf"/>
			</td>
		</tr>
	</table>	



			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="expedientes.literal.tipoExpediente.campoConf.seleccionado,expedientes.literal.tipoExpediente.campoConf.nombre,expedientes.literal.tipoExpediente.campoConf.orden,"
		   		  tamanoCol="15,60,15,10"
		   			alto="100%"
		   			ajusteBotonera="true"		
		   		  modal="P"
		   		  >

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}

				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		ExpCampoConfBean bean = (ExpCampoConfBean)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' pintarEspacio="no" botones="<%=botonesRegistro%>" clase="listaNonEdit" visibleConsulta="no">
					<td align="center">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdCampoConf()%>">						
						
						<%=(bean.getSeleccionado().intValue()==1)?UtilidadesString.getMensajeIdioma(user,"messages.si"):UtilidadesString.getMensajeIdioma(user,"messages.no") %>
					</td>					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(bean.getNombre())%>
					</td>					
					<td align="right">
						<%=UtilidadesString.mostrarDatoJSP(bean.getOrden())%>
					</td>					
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>




	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="<%=botonesAccion%>" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
