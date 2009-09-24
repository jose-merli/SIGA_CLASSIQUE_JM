<!-- accesoTiempos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.atos.utils.*"%>

<% String app=request.getContextPath(); %>

<% 
Connection con = null;
Statement st = null;
ResultSet rs = null;
		
try {
	
	java.util.Date ini = new java.util.Date();
	String cadena = request.getParameter("cadena");
	if (cadena==null || cadena.trim().equals("")) cadena = "jdbc:oracle:thin:@10.60.3.81:1526:SIGAPRE";
	String sql = request.getParameter("sql");
	if (sql==null) sql = "";
	String usr = request.getParameter("usr");
	if (usr==null) usr = "";
	String pwd = request.getParameter("pwd");
	if (pwd==null) pwd = "";
	String driver_texto ="Sin definir";
	if (sql!=null && !sql.trim().equals("")) {
		// existe consulta. La ejecutamos.
        Class.forName("oracle.jdbc.OracleDriver");
		if (usr==null || pwd==null) {
			throw new Exception ("Usuario o contraseña incorrectos");
		}
        con = DriverManager.getConnection(cadena, usr.trim(), pwd.trim());
        driver_texto = ClsMngBBDD.getDriverInformation(con);
        st = con.createStatement();
        rs = st.executeQuery(sql);
	}
	java.util.Date fin = new java.util.Date();
	long tiempo = fin.getTime()-ini.getTime();
%>

<html>
<head>
    <title>accesoTiempos</title>
</head>

<body>
	<h2>SQL siga Conexion Tiempos</h2>
	<table>
		<form name="consulta" action="<%=app%>/html/jsp/general/accesoTiempos.jsp" method="POST">
	<tr>
		<td>Cadena: </td>
		<td><input type="text" size="50"  name="cadena" value="<%=cadena %>"/></td>
	</tr>
	<tr>
		<td>Usuario: </td>
		<td><input type="text" name="usr" value="<%=usr %>"/></td>
	</tr>
	<tr>
		<td>Contraseña: </td>
		<td><input type="password" name="pwd" value="<%=pwd %>"/></td>
	</tr>
	<tr>
		<td colspan="2">Consulta: </td>
	</tr>
	<tr>
		<td colspan="2"><textarea name="sql" cols="40" rows="10"><%=sql %></textarea></td>
	</tr>
	<tr>
		<td><input type="reset" name="limpiar" value="Limpiar"/></td>
		<td><input type="submit" name="ejecutar" value="Ejecutar"/></td>
	</tr>
		</form>
	</table>
	<p><b>Tiempo ms:</b> <%=""+tiempo %> </p>
	<p><b>Driver utilizado:</b> <%=driver_texto%> </p>
<%

} catch (Exception e) {
%>
<p><b>Excepcion:</b> <%e.printStackTrace(new java.io.PrintWriter(out)); %></p>

<%	

	try {
		st.close();
	} catch (Exception e1) {}
	try {
		rs.close();
	} catch (Exception e1) {}
	try {
		con.close();
	} catch (Exception e1) {}
	//throw e;
}
			
%>
	
</body>
</html>