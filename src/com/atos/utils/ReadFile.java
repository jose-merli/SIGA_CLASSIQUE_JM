/**
 * <p>Title: ReadFile </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;




public class ReadFile {

  public ReadFile() {
  }

public String line[]= new String[200000]; 
public StringBuffer text=new StringBuffer(""); 

public String ReadSource (String name){ 

try { 
java.io.FileReader file = new java.io.FileReader(name); 
java.io.BufferedReader buff = new java.io.BufferedReader(file); 
boolean eof = false; 
int i=0; 
while (!eof) { 
i++; 
Object obj =buff.readLine();
if (obj!=null)
{
	line[i]=(String)obj; 
	if (line[i] == null) 
	{ 
	eof = true; 
	} 
	else { 
	eof=false; 
	} 
}
else 
{ 
eof = true; 
} 
} 
buff.close(); 
int j=0; 

while (j<=i){ 
j++; 
if (line[j]!=null){
	String lin=line[j]+ "\n";
	text.append(lin); 
	}
} 


} catch (java.io.IOException e) {
	ClsLogging.writeFileLogWithoutSession("Error leyendo archivo " + name + " -> " + e.toString());
} 

return text+""; 
} 
   

}