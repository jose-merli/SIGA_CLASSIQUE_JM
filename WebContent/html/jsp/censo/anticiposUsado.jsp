<!-- anticiposUsado.jsp -->
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
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.censo.form.AnticiposClienteForm"%>
<%@ page import="com.siga.beans.PysAnticipoLetradoBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	

	//recogemos los datos
	Hashtable resultado = (Hashtable)request.getAttribute("resultado");
	Vector v = (Vector)request.getAttribute("lineasAnticipo");

	//variables que se van a mostrar en la jsp
	String descripcion="", importeAnticipado="", importeRestante="", idPersona="", idAnticipo="", fecha="" ;
	String importeUsado="";

	//inicializamos los valores
	try{
		descripcion = (String)resultado.get(PysAnticipoLetradoBean.C_DESCRIPCION);
		importeAnticipado = (String)resultado.get(PysAnticipoLetradoBean.C_IMPORTEINICIAL);
		importeRestante = (String)resultado.get("IMPORTERESTANTE");
		importeUsado=(String)resultado.get("IMPORTEUSADO");
		idPersona = (String)resultado.get(PysAnticipoLetradoBean.C_IDPERSONA);
		idAnticipo = (String)resultado.get(PysAnticipoLetradoBean.C_IDANTICIPO);
		fecha = GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(PysAnticipoLetradoBean.C_FECHA));
	}catch(Exception e){
	}

	//recuperamos el modo de acceso
	String modo ="Modificar";
	if ((descripcion==null)||(descripcion.equals(""))) modo="Insertar";

%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="AnticiposClienteForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="censo.anticipos.titulos.facturasConAnticipo"/>
		</td>
	</tr>
	</table>



	<!-- INICIO: CAMPOS -->

	<table  class="tablaCentralCamposMedia" cellspacing=0 cellpadding=0 align="center" border="0">

	<html:form action="/CEN_AnticiposCliente.do" method="POST" target="submitArea">
	<html:hidden name="AnticiposClienteForm" property = "modo" value = ""/>
	<html:hidden name="AnticiposClienteForm" property = "idInstitucion" value="<%=usr.getLocation() %>" />
	<html:hidden name="AnticiposClienteForm" property = "idPersona"  value="<%=idPersona%>"/>
	<html:hidden name="AnticiposClienteForm" property = "idAnticipo"   value="<%=idAnticipo%>"/>
	
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.anticipos.leyenda">

		<table class="tablaCampos" align="center" border="0" width="100%">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.descripcion"/>&nbsp;(*)
		</td>				
		<td class="labelText" colspan="3">
			<html:text name="AnticiposClienteForm" property="descripcion" size="70" maxlength="200" styleClass="boxConsulta" readonly="true"  value="<%=descripcion%>"/>
		</td>
		</tr>

		</tr>				

<% if (!modo.equals("Insertar"))  { %>
		<tr>				

		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.fecha"/>&nbsp;(*)
		</td>				
		<td class="labelText" colspan="3">
			<html:text name="AnticiposClienteForm" property="fecha" size="12" maxlength="10" styleClass="boxConsulta" readonly="true"  value="<%=fecha%>"/>
		</td>
		</tr>

		</tr>				
<% } %>



		<tr>
		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.importeAnticipado"/>&nbsp;(*)
		</td>
		<td class="labelText">
			<html:text name="AnticiposClienteForm" property="importeAnticipado"  size="12" maxlength="13" styleClass="boxConsultaNumber" readonly="true" value="<%=UtilidadesNumero.formatoCampo(importeAnticipado)%>"/>&nbsp;&euro; 
		</td>
<% if (!modo.equals("Insertar"))  { %>

		<td class="labelText">
			<siga:Idioma key="censo.anticipos.literal.importeUsado"/>&nbsp;(*)
		</td>
		<td class="labelText">
			<html:text name="AnticiposClienteForm" property="importeUsado"  size="12" maxlength="13" styleClass="boxConsultaNumber" readonly="true" value="<%=UtilidadesNumero.formatoCampo(importeUsado)%>"/>&nbsp;&euro; 
		</td>
<% } %>

		</tr>


		
		</table>

	</siga:ConjCampos>

	</td> 
	</tr>
			
			<!-- RGG: cambio a formularios ligeros -->

		</html:form>	
	</table>
	
<siga:ConjBotonesAccion botones="C" clase="botonesSeguido" titulo="censo.anticipos.titulos.facturas"/>

	<siga:Table 
			   name="tablaResultados"
			   border="1"
			   columnNames="censo.anticipos.literal.numeroFactura,censo.anticipos.literal.importeUsado,censo.anticipos.literal.fechaFactura"
			   columnSizes="60,20,20"
			   modal="M">

<% if ((v != null) && (v.size() > 0)) {
		for (int i = 0; i < v.size(); i++) { 
			Hashtable hash = (Hashtable)v.get(i);
			if (hash != null) {
				String idInstitucionReg = UtilidadesHash.getString (hash, PysLineaAnticipoBean.C_IDINSTITUCION);
				String idPersonaReg = UtilidadesHash.getString(hash, PysLineaAnticipoBean.C_IDPERSONA);
				String idAnticipoReg = UtilidadesHash.getString(hash, PysLineaAnticipoBean.C_IDANTICIPO);
				String importeFactura = UtilidadesNumero.formatoCampo(UtilidadesHash.getString(hash, PysLineaAnticipoBean.C_IMPORTEANTICIPADO));
				String numeroFactura =  UtilidadesHash.getString(hash, "NUMEROFACTURA");
				String fechaEmision =  GstDate.getFormatedDateShort(usr.getLanguage(),UtilidadesHash.getString(hash, "FECHAEMISION"));
%>
				<tr class="listaNonEdit">
						<td>
							<%=UtilidadesString.mostrarDatoJSP(numeroFactura)%>
						</td>
						<td align="right">
							<%=UtilidadesString.mostrarDatoJSP(importeFactura)%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(fechaEmision)%>
						</td>
				</tr>
<%
			}
		}
	}
	else { // No hay registros 
	%>
		<table align="center">
			<tr>
				<td>
					<br>
					<font class="labelText" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></font>
				</td>
			</tr>
			</table>
<% } %>

	</siga:Table>
	 

	




	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.parent.close();
		}

		function refrescarLocal()
		{
			document.forms[0].target="modal";
			document.forms[0].refresco.value="refresco";
			document.forms[0].modo.value="editar";
			document.forms[0].submit();
		}			


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
