<!-- listadoCalendario.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsCalendarioLaboralBean"%>
<%@ page import="com.siga.beans.ScsCalendarioLaboralAdm"%>
<%@ page import="com.siga.beans.CenPartidoJudicialAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>


<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.calendario.literal.calendario"/></title>
	
	<script type="text/javascript">
		function mostrarCalendario(numfila){
			var resultado;			
			var tabla;
			tabla = document.getElementById('listadoCalendario');
			resultado = showCalendarGeneral(tabla.rows[numfila].cells[0].all[4]);
		}		
		
		function refrescarLocal()
		{
			parent.buscar();
		}
		
	</script>
	
</head>

<body>
<%if (obj.size()>0){%>
	<html:form action="/CalendarioLaboralAction.do" method="post" target="submitArea" style="display:none">
		<input type="hidden" name="modo" value="">

		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoCalendario"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.listadoCalendario.literal.fecha,gratuita.listadoCalendario.literal.nombre,gratuita.listadoCalendario.literal.fiestaLocalPartidoJudicial,"
		   tamanoCol="10,45,25,10"
		   alto="100%"
		   modal="P" 
		   activarFilaSel="true" >

  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{			
				ScsCalendarioLaboralBean fila = (ScsCalendarioLaboralBean)obj.get(recordNumber-1);
				String nombrePartidoJudicial = "TODO EL COLEGIO";
				try {
					CenPartidoJudicialAdm partidoJudicialAdm = new CenPartidoJudicialAdm(usr);
					Vector resultado = new Vector();
					resultado = partidoJudicialAdm.selectGenerico(partidoJudicialAdm.getNombrePartido((fila.getIdPartido()).toString()));
									
					nombrePartidoJudicial = (String)(((Hashtable)resultado.elementAt(0)).get("NOMBRE"));
				}
				catch (Exception e) {};
				
			%>
		<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' visibleConsulta="no" pintarEspacio="no" botones="E,B" clase="listaNonEdit">
			<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdentificativo()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getIdInstitucion()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getUsuMod()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=fila.getFechaMod()%>"><%=GstDate.getFormatedDateShort(usr.getLanguage(),fila.getFecha())%></td>
			<td><%=fila.getNombreFiesta()%>&nbsp;</td>
			<td><%=nombrePartidoJudicial%></td>
		</siga:FilaConIconos>		
		<% recordNumber++;		   
		} %>		
		</siga:TablaCabecerasFijas>

	<%
	}else {
	%>
	<siga:TablaCabecerasFijas 		   
		   nombre="listadoCalendario"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.listadoCalendario.literal.fecha,gratuita.listadoCalendario.literal.nombre,gratuita.listadoCalendario.literal.fiestaLocalPartidoJudicial,"
		   tamanoCol="10,45,25,10"
		   			alto="100%"


		   modal="P"
  	    >
  	    </siga:TablaCabecerasFijas>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
	}
	%>	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
</body>	
</html>