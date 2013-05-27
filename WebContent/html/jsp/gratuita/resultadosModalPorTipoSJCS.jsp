<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
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
 
<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.form.BusquedaPorTipoSJCSForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<%@ page import="com.siga.tlds.FilaExtElement"%>
 <%@ page import="java.util.Properties"%>
 <%@ page import="java.util.ArrayList"%>
 <%@ page import="com.atos.utils.*"%>
 <%@ page import="org.redabogacia.sigaservices.app.util.SIGAReferences" %>
 <%@ page import="org.redabogacia.sigaservices.app.util.ReadProperties" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	Vector vResultado = (Vector) request.getAttribute("resultado");
	
	BusquedaPorTipoSJCSForm formulario = (BusquedaPorTipoSJCSForm)request.getAttribute("BusquedaPorTipoSJCSForm");
	ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	String idordinario = rp.returnProperty("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[]={idordinario,idordinario};	

	String tipo = formulario.getTipo();
	if (tipo == null) {
		tipo = new String ("");
	}
	
%>
 
<html>

<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BusquedaPorTipoSJCSForm" staticJavascript="false" />  
	  	<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	function seleccionar(fila) {
		var datos;
		datos = document.getElementById('tablaDatosDinamicosD');
		datos.value = ""; 
		var j;
		var tabla;
		tabla = document.getElementById('tablaDatos');
		var flag = true;
		j = 1;
		while (flag) {
		  var aux = 'oculto' + fila + '_' + j;
		  var oculto = document.getElementById(aux);
		  if (oculto == null)  { flag = false; }
		  else { 
		  	datos.value = datos.value + oculto.value + ','; 
		  }
		  j++;
		}
		
		datos.value = datos.value + "%";

    	document.BusquedaPorTipoSJCSForm.modo.value = "enviar";
	   	document.BusquedaPorTipoSJCSForm.submit();
	}
	</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista  de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea"  style="display:none">
			<input type="hidden" name="actionModal" value="">
			<html:hidden name="BusquedaPorTipoSJCSForm" property = "modo" value = ""/>
			<html:hidden name="BusquedaPorTipoSJCSForm" property="idInstitucion" />
			<html:hidden name="BusquedaPorTipoSJCSForm" property="tipo" />
		</html:form>	


<%
	String nombresCol="";
	String tamCol="";
	
	if (tipo.equalsIgnoreCase("EJG")) {
		nombresCol="gratuita.busquedaEJG.literal.turnoGuardiaEJG, gratuita.busquedaEJG.literal.turnoDesignacion, gratuita.busquedaEJG.literal.anyo, gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.letradoDesignacion, gratuita.listadoActuacionesAsistencia.literal.fecha, gratuita.busquedaEJG.literal.estadoEJG, gratuita.busquedaEJG.literal.solicitante,";
		tamCol = "14,14,6,7,17,10,11,15,6";
	}
	if (tipo.equalsIgnoreCase("DESIGNA")) {
		nombresCol="gratuita.listarGuardias.literal.turno,facturacion.ano,gratuita.busquedaDesignas.literal.codigo,gratuita.listadoCalendario.literal.fecha,gratuita.listadoCalendario.literal.estado,gratuita.listarDesignasTurno.literal.nColegiado,expedientes.auditoria.literal.nombreyapellidos,pestana.justiciagratuitaejg.interesado,gratuita.busquedaDesignas.literal.validada,";
		tamCol = "12,5,6,8,6,11,14,15,6,6";
	}
	if (tipo.equalsIgnoreCase("SOJ")) {
		nombresCol="gratuita.busquedaSOJ.literal.tipoSOJ,gratuita.busquedaPorTipoSJCS.literal.anio,gratuita.busquedaPorTipoSJCS.literal.numero,";
		tamCol ="40,27,27,6";
	}
%>
		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=nombresCol%>"
		   columnSizes="<%=tamCol%>">
			
<%	if (vResultado == null || vResultado.size() == 0) { %>			
	 		<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
	 		
<%	} else { 		
			String modo = "", anio = "", numero = "", idTipoEJG = "", idTipoSOJ = "", codigo = "", turno= "";
			String idInstitucion = usrbean.getLocation();
			String defendidos="";
	    	String estado = "";
			String turnoGuardia = " ", CODIGO = "";
			int cont=1;			   	
	    	FilaExtElement[] elems=new FilaExtElement[1];
			elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	
			
			while (cont-1 < vResultado.size()){			
			  
			    Hashtable registro = (Hashtable) vResultado.get(cont-1);
				
				if (tipo.equalsIgnoreCase("EJG")) {					
					anio = (String)registro.get(ScsEJGBean.C_ANIO);
					numero = (String)registro.get(ScsEJGBean.C_NUMERO);
					idTipoEJG = (String)registro.get(ScsEJGBean.C_IDTIPOEJG);
					
					// Comprobamos el estado del idfacturacion
			    	ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usrbean);
						
					// Creamos el Turno/Guardia EJG
					turno = ScsTurnoAdm.getNombreTurnoJSP(usrbean.getLocation(),(String)registro.get("GUARDIATURNO_IDTURNO"));
					String guardia = ScsGuardiasTurnoAdm.getNombreGuardiaJSP(usrbean.getLocation(),(String)registro.get("GUARDIATURNO_IDTURNO"),(String)registro.get("GUARDIATURNO_IDGUARDIA")) ;
					
					if ((turno!="")||(guardia!="")){
						turnoGuardia = turno + "/ " + guardia ;
					}
					
					if(registro.get(ScsEJGBean.C_NUMEJG)==null||registro.get(ScsEJGBean.C_NUMEJG).equals(""))
						CODIGO="&nbsp;";
					else
						CODIGO=(String)registro.get(ScsEJGBean.C_NUMEJG);
				
				}else if (tipo.equalsIgnoreCase("DESIGNA")) {					
					defendidos = (String) registro.get("DEFENDIDOS");
					estado = (String) registro.get("ESTADO");
					anio = (String)registro.get(ScsDesignaBean.C_ANIO);
					numero = (String)registro.get(ScsDesignaBean.C_NUMERO);
					turno = (String)registro.get(ScsDesignaBean.C_IDTURNO);					
					if (estado!=null){
						if (estado.equalsIgnoreCase("V")) estado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.designa.estado.abierto");
						else if (estado.equalsIgnoreCase("F")) estado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.designa.estado.finalizado");
					 	else if(estado.equalsIgnoreCase("A")) estado = UtilidadesString.getMensajeIdioma(usrbean, "gratuita.designa.estado.anulado");
						else estado="";
					}
					
				}else if (tipo.equalsIgnoreCase("SOJ")) {
					ScsSOJBean registroSOJ	= (ScsSOJBean) vResultado.get(cont);
					anio   = UtilidadesString.mostrarDatoJSP(registroSOJ.getAnio());
					numero = UtilidadesString.mostrarDatoJSP(registroSOJ.getNumero());
					codigo = UtilidadesString.mostrarDatoJSP(registroSOJ.getNumSOJ());
					idTipoSOJ = "" + registroSOJ.getIdTipoSOJ();
				}
%>
			<siga:FilaConIconos fila="<%=String.valueOf(cont) %>" botones="" modo="<%=modo %>" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit" pintarEspacio="no">
			
				  	<% if (tipo.equalsIgnoreCase("EJG")) { %>
						<input type="hidden" id="oculto<%=cont%>_1" name="oculto<%=cont%>_1" value="<%=anio%>">
						<input type="hidden" id="oculto<%=cont%>_2" name="oculto<%=cont%>_2" value="<%=numero%>">
						<input type="hidden" id="oculto<%=cont%>_3" name="oculto<%=cont%>_3" value="<%=idTipoEJG%>">
						
						<td><%=turnoGuardia%>&nbsp;</td>
						<td><%=registro.get("TURNODESIGNA")%></td>
						<td><%=registro.get(ScsEJGBean.C_ANIO)%></td>
						 <% if (registro.get("SUFIJO")!=null && !registro.get("SUFIJO").equals("")){ %>
							<td><%=CODIGO%>-<%=(String)registro.get(ScsEJGBean.C_SUFIJO)%></td>
							<%}else{%>
							<td><%=CODIGO%></td>
						<% }%>
						
						<td><%=registro.get("LETRADODESIGNA")%></td>
						<td><%=GstDate.getFormatedDateShort("",registro.get(ScsEJGBean.C_FECHAAPERTURA))%>&nbsp;</td>
						<td><%=UtilidadesMultidioma.getDatoMaestroIdioma((String)registro.get("DESC_ESTADO"), usrbean) %>&nbsp;</td>
						<td><%=ScsEJGAdm.getUnidadEJG(usrbean.getLocation(),(String)registro.get(ScsEJGBean.C_IDTIPOEJG),(String)registro.get(ScsEJGBean.C_ANIO),(String)registro.get(ScsEJGBean.C_NUMERO)) %>&nbsp;</td>
						
					<% } else if (tipo.equalsIgnoreCase("DESIGNA")) { %>				  		
						<input type="hidden" id="oculto<%=cont%>_1" name="oculto<%=cont%>_1" value="<%=anio%>">
						<input type="hidden" id="oculto<%=cont%>_2" name="oculto<%=cont%>_2" value="<%=numero%>">
						<input type="hidden" id="oculto<%=cont%>_3" name="oculto<%=cont%>_3" value="<%=turno%>">
						
						
						<td><%=registro.get("TURNODESIG")%>&nbsp;</td>
						<td><%=registro.get("ANIO")%>&nbsp;</td>
				        <% if (registro.get("SUFIJO")!=null && !registro.get("SUFIJO").equals("")){ %>
						<td><%=registro.get("CODIGO")%>-<%=registro.get("SUFIJO")%>&nbsp;</td>
						<%}else{%>
						<td><%=registro.get("CODIGO")%>&nbsp;</td>
						<% }%>
						<td><%=registro.get("FECHAENTRADA")%>&nbsp;</td>
						<td><%=estado%></td>
						<td><%=registro.get("NCOLEGIADO")%>&nbsp;</td>
						<td><%=registro.get("LETRADODESIG")%>&nbsp;</td>
						<td><%=UtilidadesString.mostrarDatoJSP(defendidos)%>&nbsp;</td>
						<td><%=registro.get("ACTNOVALIDA")%>&nbsp;</td>
					
					
					<% } else if (tipo.equalsIgnoreCase("SOJ")) { %>
						<input type="hidden" id="oculto<%=cont%>_1" name="oculto<%=cont%>_1" value="<%=anio%>">
						<input type="hidden" id="oculto<%=cont%>_2" name="oculto<%=cont%>_2" value="<%=numero%>">
						<input type="hidden" id="oculto<%=cont%>_3" name="oculto<%=cont%>_3" value="<%=idTipoSOJ%>">
					<% } %>
				  	
			</siga:FilaConIconos>		

			<!-- FIN REGISTRO -->
			<% 	cont++;		   
			} // fin del while%> 		

<%	} // del if %>			

		</siga:Table>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>

	</body>
</html>
