<!-- remesa_EJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsMaestroEstadosEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.siga.beans.CajgRemesaEstadosAdm"%>
<%@ page import="com.atos.utils.*"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>

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
	
	String idioma=usr.getLanguage().toUpperCase();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	//Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String valor="";
	
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	Vector ejgSeleccionados=null;
	//Datos de la remesa
	String idremesa="";
	String prefijo="";
	String numero="";
	String sufijo="";
	String descripcion="";
	Hashtable r=(Hashtable) request.getAttribute("REMESA");
	if (r.get("IDREMESA")!=null){
		idremesa=(String)r.get("IDREMESA");
	}
	if (r.get("PREFIJO")!=null){
		prefijo=(String)r.get("PREFIJO");
	}
	if (r.get("NUMERO")!=null){
		numero=(String)r.get("NUMERO");
	}
	if (r.get("SUFIJO")!=null){
		sufijo=(String)r.get("SUFIJO");
	}
	if (r.get("DESCRIPCION")!=null){
		descripcion=(String)r.get("DESCRIPCION");
	}
	
	CajgRemesaEstadosAdm admBean =  new CajgRemesaEstadosAdm(usr);	
	int idEstado = admBean.UltimoEstadoRemesa(usr.getLocation(), idremesa);
	
	String modo=(String)request.getSession().getAttribute("accion");
	
	String estilocaja="";
	
	boolean breadonly = false;
	if (modo.equals("consultar")) {		
		estilocaja = "boxConsulta";
		breadonly = true;		
	} else {		
		estilocaja = "box";		
	}
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
	<script type="text/javascript">
		
		var descargar = false;
		
		function refrescarLocal() {			
			cargadatosRemesa()
			
			if (descargar) {
				document.DefinicionRemesas_CAJG_Form.modo.value="descargar";
				document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
				document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
				document.DefinicionRemesas_CAJG_Form.target="mainWorkArea";
				document.DefinicionRemesas_CAJG_Form.submit();
			}
			
			descargar = false;
		}
		
		function accionDownload() {
			descargar = true;
			document.DefinicionRemesas_CAJG_Form.modo.value="marcarEnviada";				
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function buscarGrupos() {		
			document.EstadosRemesaForm.modo.value="buscar";
			document.EstadosRemesaForm.modoAnterior.value="<%=modo%>";				
			document.EstadosRemesaForm.idRemesa.value=document.forms[0].idRemesa.value;	
			document.EstadosRemesaForm.idInstitucion.value=document.forms[0].idInstitucion.value;

			document.EstadosRemesaForm.target="resultado";	
			document.EstadosRemesaForm.submit();	
		}
		function cargadatosRemesa(){
					
			document.DefinicionRemesas_CAJG_Form.modo.value="buscarPorEJG";
					
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="resultado1";	
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function aniadirExpedientes(){
			document.DefinicionRemesas_CAJG_Form.modo.value="AniadirExpedientes";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="mainWorkArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function generarFichero(){
			document.DefinicionRemesas_CAJG_Form.modo.value="generarFichero";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function generaXML(){
			document.DefinicionRemesas_CAJG_Form.modo.value="generaXML";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		
		function accionGuardar(){
			sub();
			document.DefinicionRemesas_CAJG_Form.modo.value="modificar";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		
	</script>
</head>

<body onload="buscarGrupos();cargadatosRemesa();ajusteAlto('resultado1');">
	
	<html:form action="/JGR_E-Comunicaciones_Gestion.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "idRemesa" value = "<%=idremesa%>"/>
		<html:hidden property = "idEstado" value = "<%=String.valueOf(idEstado)%>"/>
		<html:hidden  name="DefinicionRemesas_CAJG_Form" property="accion"/>
	
		

	<fieldset name="fieldset1" id="fieldset1">
	<legend>
		<span  class="boxConsulta" >
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.datos"/>
		</span>
	</legend>
	
	<table align="center" width="100%" border="0">
		<tr>	
			<td>	
				<table align="center" width="100%" border="0">
					<tr>
						<td class="labelText" >
							<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.nRegistro"/>	
						</td>
				
						<td class="labelText">	
							<html:text name="DefinicionRemesas_CAJG_Form" property="prefijo"  size="5" maxlength="10" styleClass="boxConsulta" value="<%=prefijo%>" style="width:55px" readonly="true" ></html:text>
							<html:text name="DefinicionRemesas_CAJG_Form" property="numero"  size="5" maxlength="10" styleClass="boxConsulta" value="<%=numero%>" style="width:55px" readonly="true" ></html:text>
							<html:text name="DefinicionRemesas_CAJG_Form" property="sufijo"  size="5" maxlength="10" styleClass="boxConsulta" value="<%=sufijo%>" style="width:55px" readonly="true" ></html:text>
							<html:hidden  name="DefinicionRemesas_CAJG_Form" property="accion"/>	
						</td>	
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.Descripcion"/>	
						</td>
						<td class="labelText">	
							<html:text name="DefinicionRemesas_CAJG_Form" property="descripcion"  size="60" maxlength="200" styleClass="<%=estilocaja%>" value="<%=descripcion%>" readonly="<%=breadonly%>" ></html:text>
						</td>
					</tr>
				</table>
			</td>
			<td>
				<table align="center" width="100%" border="0">
					<tr>
						<td  style="width:400px" rowspan="2">											
							<!-- INICIO: IFRAME ESTADOS REMESA -->
							<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
								id="resultado"
								name="resultado" 
								scrolling="no"
								frameborder="0"
								marginheight="0"
								marginwidth="0";					 
								style="width:90%; height:100%;">
							</iframe>						
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	</fieldset>
	
	</html:form>
	
	<html:form action="/JGR_EstadosRemesa.do" method="POST" target="resultado">
		<html:hidden name="EstadosRemesaForm" property="modo" value="buscar"/>
		<html:hidden name="EstadosRemesaForm" property="idRemesa" />
		<html:hidden name="EstadosRemesaForm" property="idInstitucion" />
		<html:hidden name="EstadosRemesaForm" property="modoAnterior" />
	</html:form>
		
	
	
	<!-- FIN: BOTONES BUSQUEDA -->

<!-- INICIO: IFRAME LISTA RESULTADOS -->
			<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado1"
							name="resultado1"
							scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0";					 
							style="width:100%; height:100%;">
			</iframe>
			<!-- FIN: IFRAME LISTA RESULTADOS -->
	 
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	
	
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	