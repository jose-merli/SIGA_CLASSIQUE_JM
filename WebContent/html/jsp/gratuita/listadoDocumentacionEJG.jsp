<!-- listadoDocumentacionEJG.jsp -->
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
<%@ page import="com.siga.beans.ScsDocumentacionEJGBean"%>
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
	String idInstitucion = usr.getLocation();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector) request.getAttribute("resultado");
	String accion = (String)request.getSession().getAttribute("accion");
		
	String botonesFila="", anio= "", numero="", idTipoEJG = "" ;
	String	botones="V,N,i";
	if (accion.equalsIgnoreCase("ver")){
		botonesFila = "C";
	}
	else {
		botonesFila = "C,E,B";
	}

	ScsDocumentacionEJGBean fila = new ScsDocumentacionEJGBean();
	
	ArrayList documentoSel    = new ArrayList();
	ArrayList presentadorSel    = new ArrayList();

		
	
	try {
		
		anio = request.getParameter("ANIO").toString();
		numero = request.getParameter("NUMERO").toString();
		idTipoEJG = request.getParameter("IDTIPOEJG").toString();	
	}catch(Exception e){
		Hashtable miHash = (Hashtable)request.getAttribute("DATOSEJG");
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
		
	};
	String[] datoPresentador={usr.getLocation(),idTipoEJG,anio,numero};
	String informeUnico =(String) request.getAttribute("informeUnico");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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
	<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<html:form action="/JGR_DocumentacionEJG" method="post" target="mainPestanas" style="display:none">
		<input type="hidden" name="modo" value="<%=accion%>">
		<input type="hidden" name="idTipoEJG" value="<%=idTipoEJG%>">
		<input type="hidden" name="anio" value="<%=anio%>">
		<input type="hidden" name="numero" value="<%=numero%>">
	</html:form>	
		
		<tr>				
	<td width="100%" align="center">

		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG="";;
						ScsEJGAdm adm = new ScsEJGAdm (usr);
							
						Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(), anio, numero,idTipoEJG);

						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsEJGBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsEJGBean.C_NUMEJG);
							t_tipoEJG   = (String)hTitulo.get("TIPOEJG");
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
		   columnNames="gratuita.operarEJG.literal.fechaLimitePresentacion,sjcs.ejg.documentacion.presentador,expedientes.auditoria.literal.documento,gratuita.documentacionEJG.regentrada,gratuita.documentacionEJG.regsalida,gratuita.operarEJG.literal.fechaPresentacion,"
		   columnSizes="10,25,25,10,10,10"
		   modal="M">
		   
  	<% if (obj.size()>0){
	    	int recordNumber=1;
	    	String regentrada=null;
	    	String regsalida=null;
	    	while (recordNumber-1 < obj.size())
			{			
				fila = (ScsDocumentacionEJGBean)obj.get(recordNumber-1);
				
				if(fila.getRegEntrada().equals("")||fila.getRegEntrada()==null)
					regentrada="&nbsp";
				else
					regentrada=fila.getRegEntrada();
					
				if(fila.getRegSalida().equals("")||fila.getRegSalida()==null)
					regsalida="&nbsp";
				else
					regsalida=fila.getRegSalida();
					
				if(fila.getRegSalida().equals("")||fila.getRegSalida()==null)
					regsalida="&nbsp";
				else
					regsalida=fila.getRegSalida();
					
				if(!fila.getPresentador().equals("")||fila.getPresentador()!=null){
					presentadorSel.clear();
					presentadorSel.add(fila.getPresentador());
				}
				
					
				documentoSel    = new ArrayList();			
				if(!fila.getIdDocumento().equals("")||fila.getIdDocumento()!=null){
					documentoSel.clear();
					documentoSel.add(fila.getIdDocumento()+','+usr.getLocation());
					
					}
				String[] datoDocumento={fila.getIdTipoDocumento(),usr.getLocation()};	
										
			%>				
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botonesFila%>" clase="listaNonEdit" >
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdDocumentacion()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdDocumento()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getIdTipoDocumento()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=fila.getPresentador()%>">
							<%=GstDate.getFormatedDateShort("",fila.getFechaLimite().toString())%>&nbsp;
					</td>
					<td>
						<siga:ComboBD nombre = "presentador" tipo="cmbPresentador" ancho="220" clase="boxComboEnTabla" pestana="t" obligatorio="false" parametro="<%=datoPresentador%>" elementoSel="<%=presentadorSel%>" readOnly="true"/>
					</td>
					<td> 
						<%if (fila.getDocumentacion()!=null || !fila.getDocumentacion().equals("") ){%>
						<siga:ComboBD nombre = "idDocumento" tipo="cmbDocumentoEdit" ancho="220" clase="boxComboEnTabla" obligatorio="false" parametro="<%=datoDocumento%>" elementoSel="<%=documentoSel%>" readOnly="true"/>
						<idDocumento="&nbsp"/>
						<%}else{%>
						&nbsp;
						<%}%>
						&nbsp;	
					</td>
					<td><%=regentrada%></td>					
					<td><%=regsalida%></td>
					<td><%=GstDate.getFormatedDateShort("",fila.getFechaEntrega().toString())%>&nbsp;</td>
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
		function accionVolver()
		{
			document.forms[0].action="./JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 

		}
		
		//Asociada al boton Cerrar
		function accionGuardar()
		{
			document.forms[0].submit();
		}
		
		function accionNuevo()
		{
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "mainPestanas";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if(resultado=='MODIFICADO') buscar();
		}
		function buscar()
		{
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();
		}
		
		function accionImprimir(){
			sub();
			var idInstitucion= document.getElementById ('idInstitucion').value;
			var idTipoEJG= document.getElementById ('idTipoEJG').value;
			var anio= document.getElementById ('anio').value;
			var numero= document.getElementById ('numero').value;
			var datos = "idInstitucion=="+idInstitucion + "##idtipo==" +idTipoEJG+"##anio=="+anio +"##numero==" +numero+"##idTipoInforme==DEJG%%%";
			document.InformesGenericosForm.datosInforme.value =datos;

			if(document.getElementById("informeUnico").value=='1'){
				document.InformesGenericosForm.submit();
			}else{
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
				fin();	
		   	} 
		   	else {
		   		fin();
		   	}			
			
			}
		}
		
	
	</script>

	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	

</body>	
</html>