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
	<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>	
	
	<siga:Titulo titulo="sjcs.actas.titulo" localizacion="sjcs.actas.localizacion"  />
</head>

<body>
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<html:form action="/JGR_ActasComision" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idInstitucion" value = ""/>
		<html:hidden property = "idActa" value = "<%=idActa %>"/>
		<html:hidden property = "actionModal" value = ""/>

		<siga:ConjCampos leyenda="general.criterios">	
			<table class="tablaCampos" border="0" align="left">
			<tr>
				<td class="labelText" width="18%"><siga:Idioma key="sjcs.actas.numeroActa" />/<siga:Idioma key="sjcs.actas.anio" /> (*)</td>
				<td class="labelText">
					<html:text name="ActaComisionForm" property="anioActa" size="2" maxlength="4" styleClass="boxConsulta" readonly="true" onkeypress="return soloDigitos(event)" value="<%=anioActa%>"></html:text>
					&nbsp;/&nbsp;
					<html:text name="ActaComisionForm" property="numeroActa" size="4" maxlength="8" styleClass="boxConsulta" readonly="true" value="<%=numeroActa%>"></html:text>
				</td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.fechaResolucion" /> (*)</td>
				<td>
				<%if(readOnly){%>
					<html:text property="fechaResolucion" size="10" styleClass="boxConsulta" value="<%=fechaResolucionCAJG%>" disabled="false" readonly="true"></html:text>
				<%}else{%>
					<siga:Fecha nombreCampo="fechaResolucion" valorInicial="<%=fechaResolucionCAJG%>"/> 
					<a onClick="return showCalendarGeneral(fechaResolucion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
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
					<a onClick="return showCalendarGeneral(fechaReunion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				<%}%>
				</td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.horaInicio" /></td>
				<td class="labelText"><html:text name="ActaComisionForm" property="horaIni" value="<%=horaInicioReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuIni" value="<%=minInicioReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.horaFin" /></td>
				<td class="labelText"><html:text name="ActaComisionForm" property="horaFin" value="<%=horaFinReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuFin"  value="<%=minFinReunion %>" readonly="<%=readOnly%>" maxlength="2" styleClass="<%=estilo%>" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.presidente"/></td>
				<td colspan="5"><siga:ComboBD nombre="idPresidente"  tipo="tipoPonente" parametro="<%=dato%>" clase="<%=estiloCombo%>" readonly="<%=readOnlySt%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="400" elementoSel="<%=presidenteSel%>"/></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.secretario"/></td>
				<td colspan="5"><siga:ComboBD nombre="idSecretario"  tipo="tipoPonente" parametro="<%=dato%>" clase="<%=estiloCombo%>" readonly="<%=readOnlySt%>"filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="400" elementoSel="<%=secretarioSel%>"/></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.miembrosComision"/></td>
				<td colspan="5"><html:textarea styleClass="<%=claseTextArea%>" property="miembros" style="width:700; height:60" value="<%=miembrosComision%>" readonly="<%=readOnly%>"></html:textarea></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.observaciones"/></td>
				<td colspan="5"><html:textarea styleClass="<%=claseTextArea%>" property="observaciones" style="width:700; height:60" value="<%=observaciones%>" readonly="<%=readOnly%>"></html:textarea></td>
			</tr>
			</table>
		</siga:ConjCampos>	
		
			<siga:TablaCabecerasFijas 		   
			   nombre="listadoActas"
			   borde="1"
			   clase="tableTitle"		   
			   nombreCol="gratuita.busquedaEJG.literal.anyo, gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.turnoGuardiaEJG, gratuita.listadoActuacionesAsistencia.literal.fecha, gratuita.busquedaEJG.literal.solicitante"
			   tamanoCol="8,8,40,14,20"
			   alto="100%"
			   ajusteBotonera="true" >
		   <%Row fila;%>
		   <%Hashtable hash;%>
		   <%for(int i=0;i<ejgs.size();i++){%>
			   <%fila=(Row)ejgs.get(i);%>
			   <%hash=(Hashtable)fila.getRow();%>
			   <tr class="<%=((i+1)%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">
				   <td><%=hash.get("ANIO")%>&nbsp;</td>
				   <td><%=hash.get("NUMERO")%>&nbsp;</td>
				   <td><%=hash.get("TURNO")%> / <%=hash.get("GUARDIA")%>&nbsp;</td>
				   <td><%=UtilidadesString.formatoFecha((String)hash.get("FECHAAPERTURA"), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH)%>&nbsp;</td>
				   <td><%=hash.get("SOLICITANTE")%>&nbsp;</td>
			   </tr>
		   <%}%>
		   </siga:TablaCabecerasFijas>

	
	</html:form>
	<%if(readOnly){%>
		<siga:ConjBotonesAccion botones="C,GA" modal="G"/>		
	<%}else{%>
		<siga:ConjBotonesAccion botones="Y,C,GA" modal="G"/>		
	<%}%>

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
		function accionCerrar(){
			window.close();
		}
		
		function accionGuardarCerrar(){
			sub();
			var errores = "";
			var error = false;
			if(document.ActaComisionForm.numeroActa.value=="" || 
			   document.ActaComisionForm.anioActa.value==""){
				error = true;
				errores += "<siga:Idioma key='errors.required' arg0='sjcs.actas.numeroActa'/>"+ '\n';
			}if (document.ActaComisionForm.fechaResolucion.value==""){
				error = true;
				errores += "<siga:Idioma key='errors.required' arg0='sjcs.actas.fechaResolucion'/>"+ '\n';
			}if (document.ActaComisionForm.fechaResolucion.value!=""&&document.ActaComisionForm.fechaReunion.value!=""&&document.ActaComisionForm.fechaReunion.value>document.ActaComisionForm.fechaResolucion.value){
				error = true;
				errores += "<siga:Idioma key='sjcs.actas.fechasErroneas'/>"+ '\n';
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
			//idInstitucion  = document.MaestroDesignasForm.idInstitucion;
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
		
	</script>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
</body>
</html>