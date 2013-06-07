<!-- listadoRelacionadoConSJCS.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getAttribute("resultado");
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String botones = "C";
	String modo = (String)request.getAttribute("modo");
	//aalg: INC_10624
	if(usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) modo="ver";
	
	if (!modo.equalsIgnoreCase("ver")) {
		botones += ",E,B";
	}
	
	String anio = "", numero = "", idTipoTurno = "";
	String relacionesDe = (String)request.getParameter("conceptoE");
%>	
											
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->

	<% if (relacionesDe == null || relacionesDe.equals("")) { %>
		<siga:Titulo 
			titulo="pestana.SJCS.DesignaRelacionadoCon" 
			localizacion="gratuita.busquedaDesignas.literal.location"/>
	
	<% } else if (relacionesDe.equalsIgnoreCase("EJG")) { %>
		<siga:Titulo 
			titulo="pestana.SJCS.EJGRelacionadoCon" 
			localizacion="gratuita.busquedaEJG.localizacion"/>
	<% } %>
		
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body   class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">

				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					
					try { 
						Hashtable hTitulo = null;

						// Designa
						if (relacionesDe == null || relacionesDe.equals("")) {
							anio          = request.getParameter("ANIO");
							numero        = request.getParameter("NUMERO");
							idTipoTurno   = request.getParameter("IDTURNO");

							ScsDesignaAdm adm = new ScsDesignaAdm (usr);
							hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(), anio, numero, idTipoTurno);

							if (hTitulo != null) {
								t_anio      = (String)hTitulo.get(ScsDesignaBean.C_ANIO);
								t_numero    = (String)hTitulo.get(ScsDesignaBean.C_CODIGO);
							}
						}
						
						// EJG
						else  if (relacionesDe.equalsIgnoreCase("EJG")) {
							anio          = request.getParameter("anioEJG");
							numero        = request.getParameter("numeroEJG");
							idTipoTurno   = request.getParameter("idTipoEJG");
							ScsEJGAdm adm = new ScsEJGAdm (usr);
							hTitulo = adm.getTituloPantallaEJG(usr.getLocation(), anio, numero, idTipoTurno);

							if (hTitulo != null) {
								t_anio      = (String)hTitulo.get(ScsEJGBean.C_ANIO);
								t_numero    = (String)hTitulo.get(ScsEJGBean.C_NUMEJG);
							}
						}

						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
						}
					}
					catch (Exception e) {}
				%>
				
				<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	
	<html:form action = "${path}" method="POST" target="submitArea">
		<html:hidden property ="modo" value= ""/>

		<input type="hidden" name="conceptoE" value="<%=relacionesDe%>"/>
		<input type="hidden" name="anio"      value="<%=anio%>"        />
		<input type="hidden" name="numero"    value="<%=numero%>"      />
		<input type="hidden" name="idTipo"    value="<%=idTipoTurno%>" />

		<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="gratuita.relaciones.SJCS,
			   			gratuita.listadoAsistencias.literal.anio,
			   			gratuita.listadoAsistencias.literal.numero,
			   			gratuita.relaciones.Tipo,
						gratuita.listaTurnosLetrados.literal.turno,
						gratuita.busquedaDesignas.literal.letrado,"
			   columnSizes="10,5,6,20,24,24,12">
		
		<% if (obj==null || obj.size()==0){%>
	 		<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
		<%} else { %>
		
  <%  int recordNumber=1;
			while ((recordNumber) <= obj.size()){	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
					String regSJCS		 	= (String)hash.get("SJCS");
					String regAnio          = (String)hash.get("ANIO");
					String regNumero        = (String)hash.get("NUMERO");
					String regIdTipo   		= (String)hash.get("IDTIPO");
					String regIdTurno   	= (String)hash.get("IDTURNO");
					String regIdInstitucion = (String)hash.get("IDINSTITUCION");
					String regCodigo        = (String)hash.get("CODIGO");
					String regDesTipo       = (String)hash.get("DES_TIPO");
					String regDesTurno      = (String)hash.get("DES_TURNO");
					String regIdTurnoDesigna= (String)hash.get("IDTURNODESIGNA");
					String nombreLetrado= (String)hash.get("NOMBRELETRADO");
					String v_regIdTurno=" ";
					String v_regIdTurnoDesigna=" ";
					if (regIdTurnoDesigna!=null && !regIdTurnoDesigna.equals("")){
					  v_regIdTurnoDesigna= (String)hash.get("IDTURNODESIGNA");
					}
					if (regIdTurno!=null && !regIdTurno.equals("")){
					  v_regIdTurno= (String)hash.get("IDTURNO");
					}
					
					String regSJCS2=regSJCS;
					if (regSJCS != null) {
						if (regSJCS.equalsIgnoreCase("ASISTENCIA")) regSJCS = "gratuita.operarEJG.literal.asistencia";
						if (regSJCS.equalsIgnoreCase("DESIGNA"))    regSJCS = "gratuita.operarEJG.literal.designa";
						if (regSJCS.equalsIgnoreCase("EJG"))        regSJCS = "gratuita.operarEJG.literal.EJG";
						if (regSJCS.equalsIgnoreCase("SOJ"))        regSJCS = "gratuita.operarEJG.literal.SOJ";
					}
					else regSJCS = new String("");
	%>	

			<siga:FilaConIconos fila="<%=String.valueOf(recordNumber)%>" botones = "<%=botones%>" clase="listaNonEdit" pintarEspacio="false" visibleBorrado="no" visibleEdicion="no" modo="<%=modo%>" >

						<td nombre="SJCS">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=regSJCS2%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=regIdInstitucion%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=regAnio%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=regNumero%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=regIdTipo%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=v_regIdTurno%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_7" value="<%=v_regIdTurnoDesigna%>">		
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_8" value="<%=regCodigo%>">
							<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_9" value="<%=regDesTipo%>">																				
							<siga:Idioma key="<%=regSJCS%>"/>
						</td>
						<td><%=regAnio%></td>
						<td><%if(regCodigo==null||regCodigo.equals("")){%>
							&nbsp;
						<%}else{%>
							<%=regCodigo%>
						<%}%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(regDesTipo)%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(regDesTurno)%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(nombreLetrado)%>
						</td>
					</siga:FilaConIconos>

				<%recordNumber++;%>
				<%}%>	
		<%}%>
		</siga:Table>

	</html:form>	

	<!-- Designas -->		
	<html:form action = "/JGR_MantenimientoDesignas.do" method="POST" target="mainWorkArea">
		<html:hidden property ="modo" 	  value= ""/>
		<html:hidden property ="anio"     value= ""/>
		<html:hidden property ="numero"   value= ""/>
		<html:hidden property ="idTurno"  value= ""/>
		<html:hidden property ="desdeEjg" value= "si"/>
	</html:form>		

	<!-- SOJ -->
	<html:form action = "/JGR_ExpedientesSOJ.do" method="POST" target="mainWorkArea">
		<html:hidden property ="modo" 	       value= ""/>
		<html:hidden property ="anio"          value= ""/>
		<html:hidden property ="numero"  	   value= ""/>
		<html:hidden property ="idTipoSOJ"     value= ""/>
		<html:hidden property ="idInstitucion" value= ""/>
		<html:hidden property ="desdeEJG"      value= "si"/>
	</html:form>		

	<!-- EJG -->	
	<html:form action="/JGR_EJG.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property="modo"          value = ""/>
		<html:hidden property="anio"          value = ""/>
		<html:hidden property="numero"        value = ""/>
		<html:hidden property="idTipoEJG"     value = ""/>
		<html:hidden property="idInstitucion" value = ""/>
	</html:form>	

	<!-- ASISTENCIA -->	
	<html:form action = "/JGR_Asistencia.do" method="POST" target="mainWorkArea">
		<html:hidden property ="modo" 	  value= ""/>
		<html:hidden property ="anio"     value= ""/>
		<html:hidden property ="numero"   value= ""/>
		<html:hidden property ="desdeEJG" value= "si"/>
	</html:form>	
	
		<!-- EXPEDIENTES -->	
	<html:form action="/EXP_AuditoriaExpedientes"  method="POST" target="mainWorkArea">
		<html:hidden property ="modo" 	  value= ""/>	
		<html:hidden property = "hiddenFrame" value = "1"/>
		<html:hidden property ="numeroExpediente"  value=""/>
		<html:hidden property ="tipoExpediente"  value=""/>
		<html:hidden property ="idInstitucion_TipoExpediente"  value=""/>		
		<html:hidden property ="anioExpediente"  value=""/>
		<html:hidden property ="institucion" value=""/>	
		<html:hidden property ="soloSeguimiento" value="false"/>
		<html:hidden property ="editable" value="1"/>			
	
					
		<input type="hidden" name="idTipoExpediente" value="">
	</html:form>	
	
	<script>

		function enviarFormulario(fila, modo) 
		{
			var datos = new Array();
		    var tabla = document.getElementById('tablaDatos');
	        var flag = true;
	        var j = 1;
	        while (flag) {
	          var aux = 'oculto' + fila + '_' + j;
	          var oculto = document.getElementById(aux);
	          if (oculto == null)  { flag = false; }
	          else { datos[j-1] = oculto.value; }
	          j++;
	        }
	        
		 	var formulario;
			
		 	if (datos[0] == "DESIGNA") {
				formulario = document.MaestroDesignasForm;
				formulario.anio.value    = datos[2];
				formulario.numero.value  = datos[3];
				formulario.idTurno.value = datos[5];
		 	}

		 	if (datos[0] == "SOJ") {
				formulario = document.DefinirSOJForm;
				formulario.idInstitucion.value = datos[1];
				formulario.anio.value          = datos[2];
				formulario.numero.value        = datos[3];
				formulario.idTipoSOJ.value     = datos[4];
		 	}
		 	
		 	if (datos[0] == "EJG") {
				formulario = document.DefinirEJGForm;
				formulario.idInstitucion.value = datos[1];
				formulario.anio.value          = datos[2];
				formulario.numero.value        = datos[3];
				formulario.idTipoEJG.value     = datos[4];
				//formulario.idTurno.value 	   = datos[5];
		 	}

		 	if (datos[0] == "ASISTENCIA") {
				formulario = document.AsistenciasForm;
				formulario.anio.value          = datos[2];
				formulario.numero.value        = datos[3];
		 	}
		 	
	 	 	if (datos[0] == "EXPEDIENTE") {

	 	 		
				formulario = document.busquedaExpedientesForm;
				document.getElementById("idTipoExpediente").value= datos[4];
				formulario.anioExpediente.value          = datos[2];
				formulario.numeroExpediente.value        = datos[7];
				formulario.idTipoExpediente.value          = datos[4];
				formulario.institucion.value        = datos[1];
				formulario.tipoExpediente.value          = datos[8];
				formulario.idInstitucion_TipoExpediente.value          =<%=usr.getLocation()%> ;
				//document.getElementById("anioExpediente").value = datos[2];
				//document.getElementById("numExpediente").value        = datos[8];
				//document.getElementById("tipoExpediente").value     = datos[7];
				//document.getElementById("idTipoExpediente").value     = datos[7];		
				//document.getElementById("institucion").value     = datos[1];	
				if(modo=="Ver"){
					formulario.modo.value = "verDesdeEjg";
				}else{
					formulario.modo.value = "editarDesdeEjg";
				}
		 	}else{
			 	
		 		formulario.modo.value = modo;
			 }


		 	
		 	formulario.submit();
		 	return;
		}
	
		function consultar(fila) 
		{
			enviarFormulario (fila, "Ver");
		}
		
		function editar(fila) 
		{
			enviarFormulario (fila, "Editar");
		}
		function accionVolver() 
		{		
			<% if(relacionesDe!=null && relacionesDe.equalsIgnoreCase("EJG")) {	%>
				document.forms[2].action="./JGR_EJG.do";
				document.forms[2].modo.value="buscar";
				document.forms[2].target="mainWorkArea"; 				
				document.forms[2].submit();
			<% } else {	%>
				document.forms[0].action="JGR_Designas.do";
				document.forms[0].modo.value="volverBusqueda";
				document.forms[0].target="mainWorkArea"; 
				document.forms[0].submit();
			<% } %>
		}

		function refrescarLocal () {
			document.location.reload();
		}

	</script>
		
	<!-- INICIO: SUBMIT AREA -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
		
		
		 <siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  modo="<%=modo%>"/>
	</body>
</html>