<!-- listadoIncompatibilidadesGuardias.jsp -->

<!---------- CABECERA JSP ---------->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<!---------- TAGLIBS ---------->
<%@ taglib uri = "struts-bean.tld" prefix="bean"%> 
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>


<!---------- IMPORTS ---------->
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.*"%>


<!---------- PRE-JAVASCRIPT ---------->
<% 	
    //Controles globales
    String app = request.getContextPath ();
    HttpSession ses = request.getSession ();
    UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
    Properties src = (Properties) ses.getAttribute (SIGAConstants.STYLESHEET_REF);
    
    Vector listaResultados = (Vector) request.getAttribute ("resultado");
    boolean soloIncompatibilidades = 
    	((Boolean) request.getAttribute ("soloIncompatibilidades")).
    	toString ().equals("true") ? true : false;
%>


<html>


<head>
    <link id="default" rel="stylesheet" type="text/css"
          href="<%=app%>/html/jsp/general/stylesheet.jsp" />
    <script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
</head>


<body class="tablaCentralCampos">

  <!-- INICIO: CAMPOS DE LINEAS ENCONTRADAS -->
  <% 	
    String nC = " ," +
                "gratuita.incompatibilidadesGuardias.literal.turno1," +
                "gratuita.incompatibilidadesGuardias.literal.guardia1," +
                "gratuita.incompatibilidadesGuardias.literal.turno2," +
                "gratuita.incompatibilidadesGuardias.literal.guardia2," +
                "gratuita.incompatibilidadesGuardias.literal.motivos," +
                "gratuita.incompatibilidadesGuardias.literal.diasSeparacionGuardias";
    String tC = "3,19,19,19,19,14,7";
    int recordNumber = 1;
    int registrosMarcados = 0;
  %>
  
  <siga:TablaCabecerasFijas nombre="listarAsistencias" borde="2"
                            clase="tableTitle" nombreCol="<%=nC%>"
                            tamanoCol="<%=tC%>" alto="100%"
                            activarFilaSel="true" >
  
    <%
      if (listaResultados != null && listaResultados.size()>0)
      {
        while ((recordNumber) <= listaResultados.size ()) {	 
          Hashtable hash = (Hashtable) listaResultados.get (recordNumber-1);
          
          if (! (soloIncompatibilidades && hash.get("EXISTE").equals("") ) ) {
    %>
    <tr class=<%if (recordNumber % 2 == 0) {%>
                'filaTablaPar'
              <%}else{%>
                'filaTablaImpar'
              <%}%>>
      <td><input type='checkbox' name='chkInc'
                 value='<%=hash.get("IDTURNO")%>,<%=hash.get("IDGUARDIA")%>,<%=hash.get("IDTURNO_INCOMPATIBLE")%>,<%=hash.get("IDGUARDIA_INCOMPATIBLE")%>'
                 <%if (! hash.get("EXISTE").equals("")) { registrosMarcados++; %>checked<% }%>>
      </input></td>
      <td><%=hash.get("TURNO")%></td>
      <td><%=hash.get("GUARDIA")%></td>
      <td><%=hash.get("TURNO_INCOMPATIBLE")%></td>
      <td><%=hash.get("GUARDIA_INCOMPATIBLE")%></td>
      <td><%=hash.get("MOTIVOS")%>&nbsp;</td>
      <td align="center"><%=hash.get("DIASSEPARACIONGUARDIAS")%>&nbsp;</td>
    </tr>
    <%
          } //if
          recordNumber++;
        } //while
    
        if ( registrosMarcados == 0 && soloIncompatibilidades )
        {
    %>
    <tr><td colspan="8" align="center">
      <p class="labelText" style="text-align:center">
        <siga:Idioma key="gratuita.retenciones.noResultados" />
      </p>
    </td></tr>
    <%
        }
      }
      else
      {
    %>
    <tr><td colspan="8" align="center">
      <p class="labelText" style="text-align:center">
        <siga:Idioma key="gratuita.retenciones.noResultados" />
      </p>
    </td></tr>
    <%
      }
    %>
      
    </siga:TablaCabecerasFijas>
    <!-- FIN: CAMPOS DE LINEAS ENCONTRADAS -->

    <script>
    	alert ("<siga:Idioma key="messages.gratuita.incompatibilidadesGuardias.encontradas" />" + <%=registrosMarcados%>+" / "+<%=recordNumber-1%>);
    </script>

</body>
</html>