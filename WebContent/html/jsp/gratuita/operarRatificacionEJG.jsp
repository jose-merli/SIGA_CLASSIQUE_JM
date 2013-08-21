<!DOCTYPE html>
<html>
<head>
<!-- operarRatificacionEJG.jsp -->
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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	
	Hashtable miHash = (Hashtable)ses.getAttribute("DATABACKUP");
	String accion = (String)ses.getAttribute("accion");
	//aalg: INC_10624
	if(usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) accion="ver";
	String dato[] = {(String)usr.getLocation(),(String)usr.getLocation()};	
	String datoTipoResolucion[] = new String[3];
	String datoFundamentosResolucion[] = new String[3];		
	String anio= "", numero="", idTipoEJG = "", observaciones = "",refA="",docResolucion="";
	String fechaRatificacion = "", fechaResolucionCAJG= "", fechaNotificacion= "", fechaPresentacionPonente="";
	String numeroCAJG="", anioCAJG="";
	boolean requiereTurnado= false,requiereNotificarProc=false;
	ArrayList vFundamentoJuridico= new ArrayList(), vTipoRatificacion= new ArrayList(), vPonente = new ArrayList(), vActa = new ArrayList();
	String idActa = "";
	ArrayList vOrigenCAJGSel = new ArrayList();
	
	boolean accesoActas=false;
	if(request.getAttribute("accesoActa")!=null){
		String accesoActasST = (String)request.getAttribute("accesoActa");
		accesoActas = accesoActasST.equalsIgnoreCase("true")?true:false; 
	}	
	try {
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
		if (miHash.containsKey("RATIFICACIONDICTAMEN")) observaciones = miHash.get("RATIFICACIONDICTAMEN").toString();
		if (miHash.containsKey("REFAUTO")) refA = miHash.get("REFAUTO").toString();

		if (miHash.containsKey("FECHARATIFICACION")) fechaRatificacion = GstDate.getFormatedDateShort("",miHash.get("FECHARATIFICACION").toString()).toString();
		if (miHash.containsKey("FECHARESOLUCIONCAJG")) fechaResolucionCAJG = GstDate.getFormatedDateShort("",miHash.get("FECHARESOLUCIONCAJG").toString()).toString();
		if (miHash.containsKey("FECHANOTIFICACION")) fechaNotificacion = GstDate.getFormatedDateShort("",miHash.get("FECHANOTIFICACION").toString()).toString();
		if (miHash.containsKey("FECHAPRESENTACIONPONENTE")) fechaPresentacionPonente = GstDate.getFormatedDateShort("",miHash.get("FECHAPRESENTACIONPONENTE").toString()).toString();
		if (miHash.containsKey(ScsEJGBean.C_ANIO_CAJG)) anioCAJG = miHash.get(ScsEJGBean.C_ANIO_CAJG).toString();
		if (miHash.containsKey(ScsEJGBean.C_NUMERO_CAJG)) numeroCAJG = miHash.get(ScsEJGBean.C_NUMERO_CAJG).toString();
		if (miHash.containsKey("TURNADORATIFICACION")){
		 if (!miHash.get("TURNADORATIFICACION").toString().equals("") && !miHash.get("TURNADORATIFICACION").toString().equals("0")){
		  requiereTurnado=true;
		 }
		} 
		if (miHash.containsKey("REQUIERENOTIFICARPROC")){
		 if (!miHash.get("REQUIERENOTIFICARPROC").toString().equals("") && !miHash.get("REQUIERENOTIFICARPROC").toString().equals("0")){
		  requiereNotificarProc=true;
		 }
		} 
		
		datoTipoResolucion[0]=(String) usr.getLocation();
		if (miHash.containsKey("IDFUNDAMENTOJURIDICO")) {
			String idFundamentoJuridico=miHash.get("IDFUNDAMENTOJURIDICO").toString();
			vFundamentoJuridico.add(idFundamentoJuridico.equals("")? "-1": idFundamentoJuridico);
			datoFundamentosResolucion[2]=(String) vFundamentoJuridico.get(0);
			datoTipoResolucion[1]=datoFundamentosResolucion[2];
		}else{
			datoFundamentosResolucion[2]="-1";
			datoTipoResolucion[1]="-1";
			
		}
		
		
		String resolucionSel = "";
		if (miHash.containsKey("IDTIPORATIFICACIONEJG")) {
			String idTipoRatificacionEjg = miHash.get("IDTIPORATIFICACIONEJG").toString();
			resolucionSel = idTipoRatificacionEjg.equals("")? "-1": idTipoRatificacionEjg + "," + (String)usr.getLocation()+ ","+datoTipoResolucion[1];
			vTipoRatificacion.add(resolucionSel);
			datoTipoResolucion[2]=idTipoRatificacionEjg;
			datoFundamentosResolucion[0]=idTipoRatificacionEjg;
			datoFundamentosResolucion[1]=(String) usr.getLocation();
			
		}else{
			datoTipoResolucion[2]="-1";
			
			
		}
			
			
		//datoTipoResolucion[1]=(String)usr.getLocation();
		if (miHash.containsKey("DOCRESOLUCION") && miHash.get("DOCRESOLUCION") != null) {
			docResolucion = miHash.get("DOCRESOLUCION").toString();
		}
		if (miHash.containsKey("IDPONENTE") && miHash.get("IDPONENTE") != null) {
			vPonente.add(miHash.get("IDPONENTE").toString());
		}
		String vOrigenCAJG = (String) miHash.get("IDORIGENCAJG");
		if (vOrigenCAJG != null && vOrigenCAJG != null){
			vOrigenCAJGSel.add(0, vOrigenCAJG);
		}
		
		if (miHash.containsKey(ScsEJGBean.C_IDACTA) && miHash.get(ScsEJGBean.C_IDACTA) != null 
				&& miHash.containsKey(ScsEJGBean.C_IDINSTITUCIONACTA) && miHash.get(ScsEJGBean.C_IDINSTITUCIONACTA) != null 
				&& miHash.containsKey(ScsEJGBean.C_ANIOACTA) && miHash.get(ScsEJGBean.C_ANIOACTA) != null) {
			idActa = miHash.get(ScsEJGBean.C_IDINSTITUCIONACTA).toString() + "," + miHash.get(ScsEJGBean.C_ANIOACTA) + "," + miHash.get(ScsEJGBean.C_IDACTA);
			vActa.add(idActa);
		}
		
	}catch(Exception e){e.printStackTrace();};
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script type="text/javascript">
		function refrescarLocal()
		{
			document.forms[0].modo.value="abrir";
			document.forms[0].target="mainPestanas";		   	
			document.forms[0].submit();
		}

		function descargar() {
			document.forms[0].modo.value="download";
			document.forms[0].target="submitArea";		   	
			document.forms[0].submit();
		}
	</script>
	<siga:Titulo 
		titulo="pestana.justiciagratuitaejg.ratificacion" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
		
	<style type="text/css">
		table.fixed{table-layout:fixed}
		td.bordes{border:1px solid black;border-collapse:collapse}
		tr.sinbordes td{border: none !important;}
		 
	</style>
</head>

<body>
<input type="hidden" id="isObligatorioResolucion" value='${ISOBLIGATORIORESOLUCION}'>



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
			
			<td>
				<%
					if (!accion.equalsIgnoreCase("ver")) {
				%>
					<table>
						<tr>
							<td>
								<input 	type="button" 
									alt="UtilidadesString.getMensajeIdioma(usrbean,general.boton.cartaInteresados)"  
							       	id="idButton"  
							       	onclick="return generarCarta();" 
							       	class="button" 
							       	value='<%=UtilidadesString.getMensajeIdioma(usr,"gratuita.EJG.botonComunicaciones")%>' />
							</td>
						</tr>
					</table>
				<%
					}
				%>
			</td>
		</tr>
	</table>

	<siga:ConjCampos leyenda="gratuita.dictamenEJG.literal.datosRatificacion">	
		<table align="center" class="fixed" width="100%" border="0">	
			<html:form action="/JGR_RatificacionEJG" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "Modificar"/>
				<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
				<html:hidden property = "idTipoEJG" value ="<%=idTipoEJG%>"/>
				<html:hidden property = "anio" value ="<%=anio%>"/>
				<html:hidden property = "numero" value ="<%=numero%>"/>
				<html:hidden property = "docResolucion" value ="<%=docResolucion%>"/>	
				<html:hidden property = "anioActa" value =""/>
				<html:hidden property = "idActa" value =""/>
				<html:hidden property = "idInstitucionActa" value =""/>
	
				<!-- FILA -->
				<tr style="align:left" width="100%" >
					<td class="labelText" width="220px">	
						<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/> <siga:Idioma key='gratuita.operarEJG.literal.anio'/> / <siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
					</td>
				   	<td class="labelTextValue" width="230px">	
				   		<%if(usr.getLocation().equalsIgnoreCase("2027")){%>G<%} %>
						<% if (accion.equalsIgnoreCase("ver")) {%>
						  	<html:text name="DefinirEJGForm"  onkeypress="filterChars(this,false,true);" onkeyup="filterCharsUp(this);"  onblur="filterCharsNaN(this);" property="anioCAJG" size="4" maxlength="4" styleClass="boxConsulta"  value="<%=anioCAJG%>" readonly="true"></html:text> / 
			                <html:text name="DefinirEJGForm" property="numeroCAJG" size="10" maxlength="20" styleClass="boxConsulta" value="<%=numeroCAJG%>" readonly="true"></html:text>
						<%} else {%>
							<html:text name="DefinirEJGForm"  onkeypress="filterChars(this,false,true);" onkeyup="filterCharsUp(this);"  onblur="filterCharsNaN(this);" property="anioCAJG" size="4" maxlength="4" styleClass="boxNumber"  value="<%=anioCAJG%>" ></html:text> / 
			                <html:text name="DefinirEJGForm" property="numeroCAJG" size="10" maxlength="20" styleClass="boxNumber" value="<%=numeroCAJG%>"></html:text>
						<%}%>
				  	</td>
				  	
					<td class="labelText" width="220px">	
						<siga:Idioma key='gratuita.operarRatificacion.literal.origen'/>
					</td>
					<td width="250px">
						<% if (accion.equalsIgnoreCase("ver")) {%> 
							<siga:ComboBD nombre="idOrigenCAJG" tipo="origenCAJG" clase="boxConsulta" ancho="240"  filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=vOrigenCAJGSel%>" readOnly="true"/>
						<%}else{ %>
					    	<siga:ComboBD nombre="idOrigenCAJG" tipo="origenCAJG" clase="boxCombo" ancho="240" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=vOrigenCAJGSel%>" />
					 	<%}%>		
					</td>
	  			</tr>

				<tr>
					<td colspan="4">
						<siga:ConjCampos>
							<table align="left" class="fixed" border="0">
	  							<tr>
									<%if(accesoActas){%>
									<input type="hidden"  id="actasActivado"  />
										<td class="labelText" width="212px">
											<siga:Idioma key="sjcs.actas.anio" />/<siga:Idioma key="sjcs.actas.numeroActa" /> - <siga:Idioma key="sjcs.actas.fechaResolucion" />
										</td>
										<td width="300px">
											<%if (accion.equalsIgnoreCase("ver")){%>
												<siga:ComboBD nombre="idActaComp"  tipo="cmbActaComision" clase="boxConsulta" ancho="200" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vActa %>" readonly="true"/>
											<%}else{%>
												<siga:ComboBD nombre="idActaComp"  tipo="cmbActaComision" clase="boxCombo" ancho="200" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vActa %>" accion="setFechaResolucionCAJG();"/>
											<%}%>
											<input type="button" alt="abrir"  id="botonAbrirActa" onclick="return abrirActa();" class="button" name="idButton" value='<%=UtilidadesString.getMensajeIdioma(usr,"general.boton.abrirActa")%>' />
											
										</td>
									<%} else {%>
										<html:hidden property="idActaComp" value ="<%=idActa%>"/>
									<%}%>
									
									<td class="labelText" width="220px">
										<siga:Idioma key="gratuita.operarRatificacion.literal.fechaResolucionCAJG"/>
									</td>
									<td width="200px">
										<%if (accion.equalsIgnoreCase("ver")){%>
											<siga:Fecha nombreCampo="fechaResolucionCAJG" valorInicial="<%=fechaResolucionCAJG%>" disabled="true" ></siga:Fecha>
										<%} else {%>
											<siga:Fecha nombreCampo="fechaResolucionCAJG" valorInicial="<%=fechaResolucionCAJG%>" postFunction="resetActa();"></siga:Fecha>
										<%}%>
									</td>
									
									<%if(!accesoActas){%>
										<td colspan="2" width="65%">&nbsp;</td>
									<%} %>
								</tr>
	
								<tr>
									<td class="labelTextValue" colspan="4">
											Importante: El expediente se considera "Resuelto" cuando se consigne, al menos, la Fecha de Resolución y el sentido de la Resolución
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>

				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.operarRatificacion.literal.ponente"/>
					</td>
					<td colspan="3">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<siga:ComboBD nombre="idPonente"  ancho="700" tipo="tipoPonente" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vPonente%>" readOnly="true"/>
						<%} else {%>
							<siga:ComboBD nombre="idPonente"  ancho="700" tipo="tipoPonente" clase="boxCombo"  	  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vPonente%>" />
						<%}%>
					</td>
					
				</tr>

				<tr>					
					
					<td class="labelText">
						<siga:Idioma key="gratuita.operarEJG.literal.presentacionPonente"/>
					</td>
					<td width="300px">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<siga:Fecha nombreCampo="fechaPresentacionPonente" valorInicial="<%=fechaPresentacionPonente%>" disabled="true" readonly="true"></siga:Fecha>
						<%} else {%>
							<siga:Fecha nombreCampo="fechaPresentacionPonente" valorInicial="<%=fechaPresentacionPonente%>" readonly="true"></siga:Fecha>
						<%}%>
					</td>
				</tr>
	
				<tr>
					<td class="labelText">
					  <siga:Idioma key="gratuita.operarRatificacion.literal.tipoRatificacion"/>
					</td>
					<td  colspan="3">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<siga:ComboBD nombre="idTipoRatificacionEJG" ancho="700" tipo="tipoResolucionActivosConParametroBaja" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=datoTipoResolucion%>" elementoSel="<%=vTipoRatificacion%>" readOnly="true" pestana="t" accion="Hijo:idFundamentoJuridico"/>
						<%} else {%>
							<siga:ComboBD nombre="idTipoRatificacionEJG" ancho="700" tipo="tipoResolucionActivosConParametroBaja" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=datoTipoResolucion%>" elementoSel="<%=vTipoRatificacion%>" pestana="t" accion="Hijo:idFundamentoJuridico"/>
						<%}%>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.operarRatificacion.literal.fundamentoJuridico"/>
					</td>
					<td colspan="3">
						<%if (accion.equalsIgnoreCase("ver")){%>				
							<siga:ComboBD nombre="idFundamentoJuridico" ancho="700" tipo="tipoFundamentosActivosConParametroBaja" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=vFundamentoJuridico%>" parametro="<%=datoFundamentosResolucion%>" hijo="t" pestana="t" readOnly="true" />
						<%} else {%>
							<siga:ComboBD nombre="idFundamentoJuridico" ancho="700" tipo="tipoFundamentosActivosConParametroBaja" clase="boxCombo"     filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=vFundamentoJuridico%>" parametro="<%=datoFundamentosResolucion%>"  hijo="t" pestana="t" />
						<%}%>
					</td>
				</tr>
	
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.operarRatificacion.literal.fechaNotificacion"/>
					</td>
					<td>
						<%if (accion.equalsIgnoreCase("ver")){%>
							<siga:Fecha nombreCampo="fechaNotificacion" valorInicial="<%=fechaNotificacion%>" disabled="true" readonly="true"></siga:Fecha>
						<%} else {%>
							<siga:Fecha nombreCampo="fechaNotificacion" valorInicial="<%=fechaNotificacion%>" readonly="true"></siga:Fecha>
						<%}%>
					</td>
					
					<td class="labelText"  width="120px">
						<siga:Idioma key="gratuita.EJG.resolucion.refAuto"/>&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirEJGForm" property="refAuto" size="10" styleClass="boxConsulta" value="<%=refA%>" readonly="false" disabled="false"></html:text>
						<%} else {%>
							<html:text name="DefinirEJGForm" property="refAuto" size="10" styleClass="box" value="<%=refA%>" readonly="false" disabled="false"></html:text>
						<%}%>
					</td>
				</tr>
	
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.operarRatificacion.literal.fechaRatificacion"/>
					</td>	
					<td colspan="3">
					<%if (accion.equalsIgnoreCase("ver")){%>
						<siga:Fecha nombreCampo="fechaRatificacion" valorInicial="<%=fechaRatificacion%>" disabled="true" readonly="true"></siga:Fecha>
					<%} else {%>
						<siga:Fecha nombreCampo="fechaRatificacion" valorInicial="<%=fechaRatificacion%>" readonly="true"></siga:Fecha>
					<%}%>
					</td>	
				</tr>
	
				<%if (docResolucion != null && !docResolucion.trim().equals("")) {%>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.literal.docResolucion"/>
						</td>	
						<td colspan="3">	
							<html:link href="#" onclick="descargar()"><%=docResolucion%></html:link>
						</td>
					<tr>
				<%}%>
				
				<tr>
					<td class="labelText" colspan="2">
						<siga:Idioma key="gratuita.operarRatificacion.literal.requiereTurnado"/>&nbsp;&nbsp;
						<%if (accion.equalsIgnoreCase("ver")){%>
							<input type="Checkbox" name="turnadoRatificacion" <%=(requiereTurnado?"checked":"")%> disabled>
						<%} else {%>
							<input type="Checkbox" name="turnadoRatificacion" <%=(requiereTurnado?"checked":"")%>>
						<%}%>
			
					</td>

					<td class="labelText" colspan="2">
						<siga:Idioma key="gratuita.operarRatificacion.literal.requiereNotificarProc"/>&nbsp;&nbsp;
						<%if (accion.equalsIgnoreCase("ver")){%>
							<input type="Checkbox" name="requiereNotificarProc" <%=(requiereNotificarProc?"checked":"")%> disabled>
						<%} else {%>
							<input type="Checkbox" name="requiereNotificarProc" <%=(requiereNotificarProc?"checked":"")%>>
						<%}%>			
					</td>
				</tr>	
				
				<tr>
					<td class="labelText" colspan="1">
						<siga:Idioma key="gratuita.operarRatificacion.literal.observacionRatificacion"/>
					</td>
					<td colspan="3">	
						<%if (accion.equalsIgnoreCase("ver")) {%>	
							<textarea name="ratificacionDictamen" class="boxConsulta"
							style="overflow-y:auto; overflow-x:hidden; width:750px; height:50px; resize:none;" 
							readOnly="true"><%=observaciones%></textarea>
						<%} else {%>
							<textarea name="ratificacionDictamen" class="box" 
							onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" 							
							style="overflow-y:auto; overflow-x:hidden; width:730px; height:200px; resize:none;"><%=observaciones%></textarea>
						<%}%>
					</td>		
				</tr>
			</html:form>
		</table>	
	</siga:ConjCampos>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<siga:ConjBotonesAccion botones="V,R,G" modo="<%=accion%>"/>
	
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
		<html:hidden property="idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property="idTipoInforme" value='<%= usr.isComision() ?"CAJG":"EJG"%>'/>
		<html:hidden property="enviar"  value="1"/>
		<html:hidden property="descargar" value="1"/>
		<html:hidden property="datosInforme"/>
		<html:hidden property="modo" value = "preSeleccionInformes"/>
		<input type='hidden' name='actionModal'>
	</html:form>	
	
	<!-- Formulario para la edicion del envio -->
	<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="tablaDatosDinamicosD" value="">
	</form>
	
	<html:form action="/JGR_ActasComision" target="mainWorkArea" method="post">
		<html:hidden property="modo" value="<%=accion %>"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="anioActa"/>
		<html:hidden property="idActa"/>
		<input type='hidden' name='oculto0_1' value='<%=idActa%>'>
		<input type='hidden' name='oculto0_2' value='<%=usr.getLocation()%>'>
		<input type='hidden' name='oculto0_3' value='<%=anio%>'>
	</html:form>
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
				
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
			sub();
			error = "";
			if( !((document.forms[0].anioCAJG.value!="" && document.forms[0].numeroCAJG.value!="" && document.forms[0].idOrigenCAJG.value!="")
			    || (document.forms[0].anioCAJG.value=="" && document.forms[0].numeroCAJG.value=="" && document.forms[0].idOrigenCAJG.value=="")) ){
				error += '<siga:Idioma key="gratuita.operarEJG.message.anioNumeroOrigen.obligatorios"/>'+ '\n';
			   
			}
			if(document.forms[0].fechaResolucionCAJG.value!=''&&document.getElementById("isObligatorioResolucion").value=='true' ){
				if(document.forms[0].idTipoRatificacionEJG.value=='')
					error += "<siga:Idioma key='errors.required' arg0='gratuita.operarRatificacion.literal.tipoRatificacion'/>"+ '\n';
				if(document.forms[0].idFundamentoJuridico.value=='')
					error += "<siga:Idioma key='errors.required' arg0='gratuita.operarRatificacion.literal.fundamentoJuridico'/>"+ '\n';
			}
			if(document.getElementById("idActaComp")&&document.getElementById("idActaComp").value!=""){
				var actaComp= document.getElementById("idActaComp").value.split(',');
				document.forms[0].idInstitucionActa.value=actaComp[0];
				document.forms[0].anioActa.value=actaComp[1];
				document.forms[0].idActa.value=actaComp[2];
			}
			if(error!=''){
				fin();
			    alert(error);
			    return false;
				
			}
			document.forms[0].submit();
		}
		
		function generarCarta() {
		
			//idInstitucion  = document.MaestroDesignasForm.idInstitucion;
			var idInstitucion  = <%=usr.getLocation()%>;
			
			var anio  = <%=anio%>;
			var idTipo  = <%=idTipoEJG%>;
			var numero = <%=numero%>;
			var idTipoInforme  = document.InformesGenericosForm.idTipoInforme.value;
			var datos = "idinstitucion=="+idInstitucion + "##idtipo==" +idTipo+"##anio=="+anio +"##numero==" +numero+"##idTipoInforme=="+idTipoInforme+"%%%";
			
			document.InformesGenericosForm.datosInforme.value=datos;
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			   		
		   	} 
		   	else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
		   		}
		   	}
		} 	
		
		function setFechaResolucionCAJG(){
			if(document.getElementById("actasActivado")){
				var acta =jQuery("#idActaComp option:selected").text().split(" - ");
				jQuery("#fechaResolucionCAJG").val(acta[1]);
				if(acta[1]&& acta[1]!=''){
					document.forms[0].fechaResolucionCAJG.className="boxConsulta";
					document.forms[0].fechaResolucionCAJG.readOnly="true";
					jQuery("#calendario_fechaResolucionCAJG").hide();
				}else{
					document.forms[0].fechaResolucionCAJG.className="box";
					document.forms[0].fechaResolucionCAJG.readOnly="";
					if (jQuery("#calendario_fechaResolucionCAJG").length > 0){
						jQuery("#calendario_fechaResolucionCAJG").show();
					} else {
						jQuery("#fechaResolucionCAJG").after('<a href="javascript://" onclick="return showCalendarGeneral(fechaResolucionCAJG);"><img id="calendario_fechaResolucionCAJG" src="/SIGA/html/imagenes/calendar.gif" border="0"></a>');
					}
				}
			}
		}
		
		function resetActa(){
			jQuery("#idActaComp").val('');
		}
		
		function abrirActa(){
			if(document.getElementById("idActaComp")&&document.getElementById("idActaComp").value!=""){
				var actaComp= document.getElementById("idActaComp").value.split(',');
				document.ActaComisionForm.idInstitucion.value=actaComp[0];
				document.ActaComisionForm.anioActa.value=actaComp[1];
				document.ActaComisionForm.idActa.value=actaComp[2];
				document.ActaComisionForm.submit();
			}
		}
		
		jQuery(document).ready(function(){
			// Añadimos un listener al combo del acta para habilitar el boton de "abrir" 
			jQuery("#idActaComp").change(
					function() {
						if(jQuery("#idActaComp option:selected").val()==0)
							jQuery("#botonAbrirActa").attr('disabled','disabled');
						else
							jQuery("#botonAbrirActa").removeAttr('disabled');
					});
		});
		setFechaResolucionCAJG();
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>