<!-- editarActaComision.jsp -->

<%@page import="com.siga.beans.ScsActaComisionBean"%>
<%@page import="java.util.Hashtable"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>

<%
	HttpSession ses = request.getSession();
	String app = request.getContextPath();
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
	String 	dato[] = {(String)usr.getLocation()};
	
	Hashtable datosActa = (Hashtable)request.getAttribute("datosActa");
	Vector ejgs = (Vector)request.getAttribute("ejgsRelacionados");
	
	String idActa = datosActa.get(ScsActaComisionBean.C_IDACTA)!=null?(String)datosActa.get(ScsActaComisionBean.C_IDACTA):"";
	String idInstitucion = datosActa.get(ScsActaComisionBean.C_IDINSTITUCION)!=null?(String)datosActa.get(ScsActaComisionBean.C_IDINSTITUCION):"";
	String idPresidente = datosActa.get(ScsActaComisionBean.C_IDPRESIDENTE)!=null?(String)datosActa.get(ScsActaComisionBean.C_IDPRESIDENTE):"";
	String idSecretario = datosActa.get(ScsActaComisionBean.C_IDSECRETARIO)!=null?(String)datosActa.get(ScsActaComisionBean.C_IDSECRETARIO):"";
	String anioActa = datosActa.get(ScsActaComisionBean.C_ANIOACTA)!=null?(String)datosActa.get(ScsActaComisionBean.C_ANIOACTA):"";
	String numeroActa = datosActa.get(ScsActaComisionBean.C_NUMEROACTA)!=null?(String)datosActa.get(ScsActaComisionBean.C_NUMEROACTA):"";
	String fechaResolucionCAJG = datosActa.get(ScsActaComisionBean.C_FECHARESOLUCION)!=null?UtilidadesString.formatoFecha((String)datosActa.get(ScsActaComisionBean.C_FECHARESOLUCION),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH):"";
	String fechaReunion = datosActa.get(ScsActaComisionBean.C_FECHAREUNION)!=null?UtilidadesString.formatoFecha((String)datosActa.get(ScsActaComisionBean.C_FECHAREUNION),ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH):"";
	String observaciones = datosActa.get(ScsActaComisionBean.C_OBSERVACIONES)!=null?(String)datosActa.get(ScsActaComisionBean.C_OBSERVACIONES):"";
	String miembrosComision = datosActa.get(ScsActaComisionBean.C_MIEMBROSCOMISION)!=null?(String)datosActa.get(ScsActaComisionBean.C_MIEMBROSCOMISION):"";
	String pendientes = datosActa.get(ScsActaComisionBean.C_PENDIENTES)!=null?(String)datosActa.get(ScsActaComisionBean.C_PENDIENTES):"";
	String horaInicioReunion = datosActa.get(ScsActaComisionBean.C_HORAINICIOREUNION)!=null?UtilidadesString.formatoFecha((String)datosActa.get(ScsActaComisionBean.C_HORAINICIOREUNION),ClsConstants.DATE_FORMAT_JAVA, "HH"):"";;
	String minInicioReunion = datosActa.get(ScsActaComisionBean.C_HORAINICIOREUNION)!=null?UtilidadesString.formatoFecha((String)datosActa.get(ScsActaComisionBean.C_HORAINICIOREUNION),ClsConstants.DATE_FORMAT_JAVA, "mm"):"";;
	String horaFinReunion = datosActa.get(ScsActaComisionBean.C_HORAFINREUNION)!=null?UtilidadesString.formatoFecha((String)datosActa.get(ScsActaComisionBean.C_HORAFINREUNION),ClsConstants.DATE_FORMAT_JAVA, "HH"):"";;
	String minFinReunion = datosActa.get(ScsActaComisionBean.C_HORAFINREUNION)!=null?UtilidadesString.formatoFecha((String)datosActa.get(ScsActaComisionBean.C_HORAFINREUNION),ClsConstants.DATE_FORMAT_JAVA, "mm"):"";;
	
	ArrayList presidenteSel = new ArrayList();
	presidenteSel.add(idPresidente);
	ArrayList secretarioSel = new ArrayList();
	secretarioSel.add(idSecretario);
	
	String modo = request.getAttribute("modo")!=null?(String)request.getAttribute("modo"):"";
	String estiloNumber = "boxNumber";
	String estilo = "box";
	String estiloCombo = "boxCombo";
	String claseTextArea = "labelTextArea";
	boolean readOnly = false;
	String readOnlySt = "false";
	if(modo.equalsIgnoreCase("consulta")){
		estilo = "boxConsulta";
		estiloNumber = "boxConsulta";
		estiloCombo = "boxConsulta";
		readOnly = true;
		readOnlySt="true";
		claseTextArea="boxConsulta";
	}
	String informeUnico =(String) request.getAttribute("informeUnico");
%>
<html>

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>" />
	<link rel="stylesheet" href="<html:rewrite page='/html/js/themes/base/jquery.ui.all.css'/>" />
		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/js/calendarJs.jsp"></script>
	<script type="text/javascript" src="<%=app%>/html/js/validation.js"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>	
	
	<siga:Titulo titulo="sjcs.actas.titulo" localizacion="sjcs.actas.localizacion"  />
</head>

<body>
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<html:form action="/JGR_EJG.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
	</html:form>
	
	<html:form action="/JGR_ActasComisionEd" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idInstitucion" value = "<%=idInstitucion%>"/>
		<html:hidden property = "idActa" value = "<%=idActa %>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property="anioActa" value="<%=anioActa%>"/>
		<html:hidden property="numeroActa" value="<%=numeroActa%>"/>

		<siga:ConjCampos leyenda="general.criterios">	
			<table class="tablaCampos" border="0" align="left">
			<tr>
				<td class="labelText" width="18%"><siga:Idioma key="sjcs.actas.anio" />/<siga:Idioma key="sjcs.actas.numeroActa" />(*)</td>
				<td class="labelTextValue">
					<%=anioActa %>/<%=numeroActa%>
				</td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.fechaResolucion" /></td>
				<td>
				<%if(readOnly){%>
					<html:text property="fechaResolucion" size="10" styleClass="boxConsulta" value="<%=fechaResolucionCAJG%>" disabled="false" readonly="true"></html:text>
				<%}else{%>
					<siga:Fecha nombreCampo="fechaResolucion" valorInicial="<%=fechaResolucionCAJG%>"/> 
				<%}%>					
				</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.fechaReunion" /></td>
				<td>
				<%if(readOnly){%>
					<html:text property="fechaReunion" size="10" styleClass="boxConsulta" value="<%=fechaReunion%>" disabled="false" readonly="true"></html:text>
				<%}else{%>
					<siga:Fecha nombreCampo="fechaReunion" valorInicial="<%=fechaReunion%>"/> 
				<%}%>
				</td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.horaInicio"/></td>
				<td class="labelText"><html:text name="ActaComisionForm" property="horaIni" value="<%=horaInicioReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuIni" value="<%=minInicioReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.horaFin"/></td>
				<td class="labelText"><html:text name="ActaComisionForm" property="horaFin" value="<%=horaFinReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuFin"  value="<%=minFinReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.presidente"/></td>
				<td colspan="5"><siga:ComboBD nombre="idPresidente"  tipo="tipoPonente" parametro="<%=dato%>" clase="<%=estiloCombo%>" readonly="<%=readOnlySt%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="700" elementoSel="<%=presidenteSel%>"/></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.secretario"/></td>
				<td colspan="5"><siga:ComboBD nombre="idSecretario"  tipo="tipoPonente" parametro="<%=dato%>" clase="<%=estiloCombo%>" readonly="<%=readOnlySt%>"filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="700" elementoSel="<%=secretarioSel%>"/></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.miembrosComision"/></td>
				<td colspan="5"><html:textarea styleClass="<%=claseTextArea%>" property="miembros" style="width:700px; height:60px" value="<%=miembrosComision%>" readonly="<%=readOnly%>"></html:textarea></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.observaciones"/></td>
				<td colspan="5"><html:textarea styleClass="<%=claseTextArea%>" property="observaciones" style="width:700px; height:60px" value="<%=observaciones%>" readonly="<%=readOnly%>"></html:textarea></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.ejgsPendientes"/></td>
				<td colspan="5"><html:textarea styleClass="<%=claseTextArea%>" property="pendientes" style="width:700px; height:60px" value="<%=pendientes%>" readonly="<%=readOnly%>"></html:textarea></td>
			</tr>
			<%if(!readOnly){%>
				<tr>
					<td colspan="6" align="right"><input type="button" alt="Retirar"  id="idButtonRetirar" onclick="return accionRetirarPendientes();" class="button" name="idButton" value="<siga:Idioma key='sjcs.actas.retirarEJGs'/>"></input></td>
				</tr>
			<%}%>
			</table>
		</siga:ConjCampos>	
		
		<div id="listadoEJGs" style="height:100%">
		<siga:TablaCabecerasFijas 		   
			   nombre="listadoActas"
			   borde="1"
			   clase="tableTitle"		   
			   nombreCol="gratuita.busquedaEJG.literal.anyo, gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.turnoGuardiaEJG, gratuita.listadoActuacionesAsistencia.literal.fecha, gratuita.busquedaEJG.literal.solicitante,"
			   tamanoCol="8,8,40,14,20,"
			   alto="100%"
			   ajusteBotonera="true" >
		   <%Row fila;%>
		   <%Hashtable hash;%>
		   <%String filaSt; %>
		   <%String botones = readOnly?"C":"C,E";%>
		   <%for(int i=0;i<ejgs.size();i++){%>
		   		<%filaSt=""+(i+1);%>
			   	<%fila=(Row)ejgs.get(i);%>
			   	<%hash=(Hashtable)fila.getRow();%>
			   <siga:FilaConIconos fila="<%=filaSt%>" botones="<%=botones%>" clase="listaNonEdit" visibleBorrado="no" >
			   		<input type="hidden" name="oculto<%=filaSt%>_1" id="oculto<%=filaSt%>_1" value="<%=hash.get("IDTIPOEJG")%>">
					<input type="hidden" name="oculto<%=filaSt%>_2" id="oculto<%=filaSt%>_2" value="<%=hash.get("IDINSTITUCION")%>">
					<input type="hidden" name="oculto<%=filaSt%>_3" id="oculto<%=filaSt%>_3" value="<%=hash.get("ANIO")%>">
					<input type="hidden" name="oculto<%=filaSt%>_4" id="oculto<%=filaSt%>_4" value="<%=hash.get("NUM")%>">
				   <td><%=hash.get("ANIO")%>&nbsp;</td>
				   <td><%=hash.get("NUMERO")%>&nbsp;</td>
				   <td><%=hash.get("TURNO")%> / <%=hash.get("GUARDIA")%>&nbsp;</td>
				   <td><%=UtilidadesString.formatoFecha((String)hash.get("FECHAAPERTURA"), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH)%>&nbsp;</td>
				   <td><%=hash.get("SOLICITANTE")%>&nbsp;</td>
			   </siga:FilaConIconos>
		   <%}%>
		   </siga:TablaCabecerasFijas>
		   </div>
			<%if(readOnly){%>
				<siga:ConjBotonesAccion botones="V,GA" modal="G"/>		
			<%}else{%>
				<siga:ConjBotonesAccion botones="V,G,GA" modal="G"/>		
			<%}%>
	
	</html:form>

<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="ACTAC"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>		


<!-- Formulario para la edicion del envio -->


	<script language="JavaScript">
	
		function refrescarLocal(){
			document.ActaComisionForm.target="mainWorkArea";
			document.ActaComisionForm.modo.value="editar";
			document.ActaComisionForm.submit();
		}
	
		function accionVolver(){
			document.ActaComisionForm.action="./JGR_ActasComisionEd.do";
			document.ActaComisionForm.target="mainWorkArea";
			document.ActaComisionForm.modo.value="";
			document.ActaComisionForm.submit();
		}
		
		function accionCerrar(){
			window.top.close();
		}
		
		function accionGuardar(){
			sub();
			var errores = "";
			var error = false;
			if(document.ActaComisionForm.numeroActa.value=="" || 
			   document.ActaComisionForm.anioActa.value==""){
				error = true;
				errores += "<siga:Idioma key='errors.required' arg0='sjcs.actas.numeroActa'/>"+ '\n';
			}
			/*
			if (document.ActaComisionForm.fechaResolucion.value==""){
				error = true;
				errores += "<siga:Idioma key='errors.required' arg0='sjcs.actas.fechaResolucion'/>"+ '\n';
			}
			*/
			if (document.ActaComisionForm.fechaResolucion.value!=""&&document.ActaComisionForm.fechaReunion.value!=""&&validarFecha(document.ActaComisionForm.fechaReunion.value)&&validarFecha(document.ActaComisionForm.fechaResolucion.value)&&compararFecha(document.ActaComisionForm.fechaReunion.value, document.ActaComisionForm.fechaResolucion.value)==1){
				error = true;
				errores += "<siga:Idioma key='sjcs.actas.fechasErroneas'/>"+ '\n';
			}
			if(document.ActaComisionForm.horaIni.value!=""){
				if(document.ActaComisionForm.horaIni.value>23){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaInicioError01'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.minuIni.value==""){
						document.ActaComisionForm.minuIni.value=0;
					}
				}			
			}
			if(document.ActaComisionForm.minuIni.value!=""){
				if(document.ActaComisionForm.minuIni.value>59){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaInicioError02'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.horaIni.value==""){
						document.ActaComisionForm.horaIni.value=0;
					}
				}	
			}
			if(document.ActaComisionForm.horaFin.value!=""){
				if(document.ActaComisionForm.horaFin.value>23){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaFinError01'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.minuFin.value==""){
						document.ActaComisionForm.minuFin.value=0;
					}
				}			
			}
			if(document.ActaComisionForm.minuFin.value!=""){
				if(document.ActaComisionForm.minuFin.value>59){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaFinError02'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.horaFin.value==""){
						document.ActaComisionForm.horaFin.value=0;
					}
				}	
			}					
			if(error==false){
				document.ActaComisionForm.target.value="submitArea";
				document.ActaComisionForm.modo.value="modificar";
				document.ActaComisionForm.submit();
			}else{
				alert(errores);
				fin();
			}
		}
		
		function generarActa() {
			sub();
			var idInstitucion  = <%=idInstitucion%>;
			var datos = "idInstitucion=="+<%=idInstitucion%>+"##idActa=="+<%=idActa%>+"##anioActa=="+<%=anioActa%>+"##numeroActa=="+<%=numeroActa%>;
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
		
		function accionRetirarPendientes(){
			if(confirm("<siga:Idioma key='sjcs.actas.confirmacionRetirar'/>")){
				document.ActaComisionForm.target.value="submitArea";
				document.ActaComisionForm.modo.value="procesarRetirados";
				document.ActaComisionForm.submit();
			}
		}

		</script>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
</body>
</html>