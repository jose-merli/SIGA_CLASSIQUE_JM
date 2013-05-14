<!-- listadoTurnosDisponiblesBaja.jsp -->

 <meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Colegio cole = (Colegio)ses.getAttribute("colegio");
	Vector obj = (Vector) request.getAttribute("resultado");
	String entrada = (String)ses.getAttribute("entrada");
	// Creo el boton de validar
	FilaExtElement[] elems=new FilaExtElement[1];
	elems[0]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);	
%>

<html>

<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script language="JavaScript">
		
		function solicitarbaja(fila) {
		   	document.FormAValidar.idInstitucion.value = document.getElementById('idInstitucionSolicitud').value;
		   	document.FormAValidar.idPersona.value = document.getElementById('idPersonaSolicitud').value;
		   	
		   	var idTurno = 'oculto' + fila + '_' + 1;		
		   	document.FormAValidar.idTurno.value = document.getElementById(idTurno).value;
		   	
		   	var fSolicitud = 'oculto' + fila + '_' + 21;
		   	document.FormAValidar.fechaSolicitud.value = document.getElementById(fSolicitud).value;
		   	
		   	var fValidacion = 'oculto' + fila + '_' + 22;
		   	document.FormAValidar.fechaValidacion.value = document.getElementById(fValidacion).value;
		   	document.FormAValidar.fechaValidacionTurno.value = document.getElementById(fValidacion).value;
		   	
		   	document.FormAValidar.modo.value = "sbtConsultaTurno";
		   	document.FormAValidar.target="_parent";
		   	document.FormAValidar.submit();	   	
		 }
		
		
		//SCRIPTS BOTONES
		function accionCancelar() {		
			window.top.close();
		}
		
		function accionContinuar()	{
			//ventaModalGeneral(document.forms[0].name,"G"); 
			document.forms[0].target="_parent";
		}
		
	   function marcarDesmarcarTodos(o,numeroChekBox) {
			var ele = document.getElementsByName('bajaTurno');
			for (i = 0; i < ele.length; i++) {
			    if(!ele[i].disabled){
				 ele[i].checked = o.checked;
				}
			}
		}
	   
		function darDeBajaEnTurnosSel(mostrarMensaje) { 
	    	sub();
			var ele = document.getElementsByName('bajaTurno');			 
			var encontrado=false;
			var datos='';
			for (i = 0; i < ele.length; i++) {
			    if(ele[i].checked){
				  encontrado=true;
				  var idTurno=  'oculto' + (i+1) + '_'+1 ;
				  var validar='oculto'+(i+1)+'_'+4;
				  var fSolicitud ='oculto'+(i+1)+'_'+21;		
				  var fValidacion = 'oculto' + (i+1) + '_' + 22;	
				  var datos = datos + "," + document.getElementById(idTurno).value + 
				  	"##" + document.getElementById(validar).value + 
				  	"##" + document.getElementById (fSolicitud).value +
				  	"##" + document.getElementById (fValidacion).value; 
				}
			}
			datos=datos.substring(1);
			
			if (!encontrado){
				alert("<siga:Idioma key="censo.fichaCliente.turnoInscrito.avisoSeleccion"/>");
				fin();
				return;
			}
			
			if (mostrarMensaje) {
				var mensaje = "<siga:Idioma key="censo.fichaCliente.turnoInscrito.pregunta.bajaTodosLosTurnos"/>";
				if(!confirm(mensaje)) {
                	fin();
					return;
				}
			}			
			document.FormAValidar.idInstitucion.value = document.getElementById('idInstitucionSolicitud').value;
	   		document.FormAValidar.idPersona.value = document.getElementById('idPersonaSolicitud').value;
			document.FormAValidar.turnosSel.value=datos;
			document.FormAValidar.modo.value='smitDatosBaja';
			document.FormAValidar.target = "_parent";
    		document.FormAValidar.submit();
		}
		
	</script>
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table>
	<tr>
		<td  class="labelText">
				<siga:Idioma key="gratuita.listarTurnosDisp.literal.avisoBaja"/>
		</td>
	</tr>   
	 </table>
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	
	
	
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.listarTurnosDisp.listado.titulo"/>
		</td>
	</tr>    
	</table>


		<html:form action = "/JGR_SolicitarBajaTurno.do" method="POST" target="_parent" style="display:none" name="FormAValidar" type ="com.siga.gratuita.form.InscripcionTGForm">
			<input type="hidden" id="origen" name="origen" value="listarTurnosDisp">
			<input type="hidden" id="modo" name="modo" value="ver">
			<input type="hidden" id="paso" name="paso" value="turno"/>
			<input type="hidden" id="idInstitucion" name="idInstitucion" />
			<input type="hidden" id="idPersona" name="idPersona" />
			<input type="hidden" id="idTurno" name="idTurno" />
			<input type="hidden" id="fechaSolicitud" name="fechaSolicitud"/>
			<input type="hidden" id="observacionesSolicitud" name="observacionesSolicitud"/>
			<input type='hidden' id="turnosSel" name='turnosSel'>
			<input type='hidden' id="validarTurno" name='validarTurno'>
			<input type='hidden' id="fechaValidacion" name='fechaValidacion'>
			<input type='hidden' id="fechaValidacionTurno" name='fechaValidacionTurno'>			
		</html:form>	
		
	<% 	String nC="";
		String tC="";
		String botones="";
		String alto="";
		nC="<input type='checkbox' onclick='marcarDesmarcarTodos(this)'/> ,gratuita.listarTurnosDisp.literal.nombre,gratuita.listarTurnosDisp.literal.area,gratuita.listarTurnosDisp.literal.materia,gratuita.listarTurnosDisp.literal.zona,gratuita.listarTurnosDisp.literal.subzona,gratuita.listarTurnosDisp.literal.partidojudicial,Solic. Baja";
		tC="5,17,14,17,11,11,17,8";
		botones="";
		alto="420";
	%>

		<siga:Table 
		   name="listarTurnosDisp"
		   border="1"
		   columnNames="<%=nC%>"
		   columnSizes="<%=tC%>"
		   modal="G" >
	<%
	String idPersona = (String)request.getAttribute("idPersonaTurno");
	if (obj.size()>0){%>
			<%
	    	int recordNumber=1;
			while ((recordNumber) <= obj.size())
			{	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				String o1,o2,o3,o4,o5,o6="";
				
				try{
					if(((String)hash.get("DESCRIPCION")).equals(""))o1="-";
					else o1=(String)hash.get("DESCRIPCION");
				}catch(Exception e){o1="-";}
				try {
					if(((String)hash.get("REQUISITOS")).equals(""))o2="-";
					else o2=(String)hash.get("REQUISITOS");
				}catch(Exception e){o2="-";}
				
				try {
					if(((String)hash.get("SUBZONA")).equals(""))o3="-1";
					else o3=(String)hash.get("SUBZONA");
				}catch(Exception e){o3="-1";}
				
				try {
					if(((String)hash.get("PARTIDAPRESUPUESTARIA")).equals(""))o4="-1";
					else o4=(String)hash.get("PARTIDAPRESUPUESTARIA");
				}catch(Exception e){o4="-1";}
				
				try {
					if(((String)hash.get("PERSONAULTIMO")).equals(""))o5="-1";
					else o5=(String)hash.get("PERSONAULTIMO");
				}catch(Exception e){o5="-1";}
				
				try {
					if(((String)hash.get("IDSUBZONA")).equals(""))o6="-1";
					else o6=(String)hash.get("IDSUBZONA");
				}catch(Exception e){o6="-1";}
				
				String sGuardias=(String)hash.get("GUARDIAS");
				String nGuardiasTurno=(String)hash.get("NGUARDIAS");
				String deshabilitarCheck="";
				
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>"  elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" visibleBorrado="no" pintarEspacio="false">
					<input type='hidden' id='idPersonaSolicitud' name='idPersonaSolicitud' value='<%=idPersona%>'>
					<input type='hidden' id='idInstitucionSolicitud' name='idInstitucionSolicitud' value='<%=hash.get("IDINSTITUCION")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_1' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDTURNO")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("GUARDIAS")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_3' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("VALIDARJUSTIFICACIONES")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_4' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=hash.get("VALIDARINSCRIPCIONES")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_5' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=hash.get("DESIGNADIRECTA")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_6' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=hash.get("REPARTOPORPUNTOS")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_7' name='oculto<%=String.valueOf(recordNumber)%>_7' value='<%=o1%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_8' name='oculto<%=String.valueOf(recordNumber)%>_8' value='<%=o2%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_9' name='oculto<%=String.valueOf(recordNumber)%>_9' value='<%=hash.get("IDORDENACIONCOLAS")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_10' name='oculto<%=String.valueOf(recordNumber)%>_10' value='<%=o5%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_11' name='oculto<%=String.valueOf(recordNumber)%>_11' value='<%=hash.get("IDAREA")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_12' name='oculto<%=String.valueOf(recordNumber)%>_12' value='<%=hash.get("IDMATERIA")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_13' name='oculto<%=String.valueOf(recordNumber)%>_13' value='<%=hash.get("IDZONA")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_14' name='oculto<%=String.valueOf(recordNumber)%>_14' value='<%=o3%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_15' name='oculto<%=String.valueOf(recordNumber)%>_15' value='<%=o4%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_16' name='oculto<%=String.valueOf(recordNumber)%>_16' value='<%=o5%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_17' name='oculto<%=String.valueOf(recordNumber)%>_17' value='<%=hash.get("IDGRUPOFACTURACION")%>'>
					
					<%
					// Para posibles valores nulos.
					String partidaPresupuestaria = " ";
					if(hash.get("PARTIDAPRESUPUESTARIA")!=null && !hash.get("PARTIDAPRESUPUESTARIA").equals(""))
						partidaPresupuestaria = (String) hash.get("PARTIDAPRESUPUESTARIA");
					%>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_18' name='oculto<%=String.valueOf(recordNumber)%>_18' value='<%=partidaPresupuestaria%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_19' name='oculto<%=String.valueOf(recordNumber)%>_19' value='<%=hash.get("GRUPOFACTURACION")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_20' name='oculto<%=String.valueOf(recordNumber)%>_20' value='<%=o6%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_21' name='oculto<%=String.valueOf(recordNumber)%>_21' value='<%=hash.get("FECHASOLICITUD")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_21' name='oculto<%=String.valueOf(recordNumber)%>_22' value='<%=hash.get("FECHAVALIDACION")%>'>
					<td align="center">
						<input type="checkBox" name="bajaTurno" id="<%="bajaTurno"+String.valueOf(recordNumber)%>" <%=deshabilitarCheck%> >
					</td>
					<td><%=UtilidadesString.mostrarDatoJSP(hash.get("NOMBRE"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(hash.get("AREA"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(hash.get("MATERIA"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(hash.get("ZONA"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(hash.get("SUBZONA"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(hash.get("PARTIDOS"))%></td>
				</siga:FilaConIconos>
				<% recordNumber++;
			} %>
		<%}else{%>
	 		<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
		<%}%>
		</siga:Table>
<%-- 	<html:form action="/JGR_SolicitarBajaTurno"  name="FormAValidar" type ="com.siga.gratuita.form.InscripcionTGForm"> --%>
<%-- 		<html:hidden property="modo"/> --%>
<%-- 		<html:hidden property="idInstitucion" /> --%>
<%-- 		<html:hidden property="idPersona" /> --%>
<%-- 		<html:hidden property="idTurno" /> --%>
<%-- 		<html:hidden property="fechaSolicitud" /> --%>
<%-- 		<html:hidden property="fechaValidacion" /> --%>
<%-- 		<html:hidden property="turnosSel" /> --%>
		
<!-- 		<input type="hidden" name="actionModal" /> -->
<%-- 	</html:form> --%>


				
			<siga:ConjBotonesAccion botones="bajaEnTurnosSel,X" clase="botonesDetalle"  />	

	</body>

</html>