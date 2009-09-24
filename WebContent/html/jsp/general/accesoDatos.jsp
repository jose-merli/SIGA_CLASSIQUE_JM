<!-- accesoDatos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ page import="java.sql.*"%>
<% String app=request.getContextPath(); %>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<% 
Connection con = null;
Statement st = null;
ResultSet rs = null;
		
try {
	
	String cadena = request.getParameter("cadena");
	if (cadena==null || cadena.trim().equals("")) cadena = "jdbc:oracle:thin:@10.60.3.81:1526:SIGAPRE";
	String sql = request.getParameter("sql");
	if (sql==null) sql = "";
	String usr = request.getParameter("usr");
	if (usr==null) usr = "";
	String pwd = request.getParameter("pwd");
	if (pwd==null) pwd = "";
	if (sql!=null && !sql.trim().equals("")) {
		// existe consulta. La ejecutamos.
        Class.forName("oracle.jdbc.driver.OracleDriver");
		if (usr==null || pwd==null) {
			throw new Exception ("Usuario o contraseña incorrectos");
		}
        con = DriverManager.getConnection(cadena, usr.trim(), pwd.trim());
        st = con.createStatement();
        rs = st.executeQuery(sql);
	}
	
%>

<html>
<head>
    <title>accesoDatos</title>
</head>

<body>
	<h2>SQL siga Conexion</h2>
	<table>
		<form name="consulta" action="<%=app%>/html/jsp/general/accesoDatos.jsp" method="POST">
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
<%
int contador = 0;
if (rs!=null) {
%>
	<p><b>Resultados:</b></p>
	<table border="1">
<% 
int numberOfColumns = 0;
Vector v = new Vector();
while (rs.next()) {
%>
	<tr>
<%        
	if (contador==0) {
        ResultSetMetaData rsmd = rs.getMetaData();
        numberOfColumns = rsmd.getColumnCount();
		for (int i=0;i<numberOfColumns;i++) {
        	String columna = rsmd.getColumnName(i+1);
        	v.add(columna);
%>
	   <td><b><%=columna.trim().toUpperCase() %></b></td>
<%
		}
%>
		</tr><tr>
<%
	}
	contador++;
	for (int j=0;j<numberOfColumns;j++) {
        	String valor = rs.getString(j+1);
        	if (valor==null) valor = "";
%>
	   <td><%=valor.trim() %></td>
<%
	}
%>
	</tr>
<%

} // while
%>
	   </table>
<%
}
%>
	   <p><b><%=new Integer(contador).toString()%> registros</b></p> 
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