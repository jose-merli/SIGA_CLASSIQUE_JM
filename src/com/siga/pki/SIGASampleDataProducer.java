package com.siga.pki;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import com.atos.utils.ClsExceptions;

public class SIGASampleDataProducer extends SIGAPkiDataProducer
{
  public SIGASampleDataProducer() {
  }

  public String getFileName() {
	  return "filename";
  }

  public void setFileName() {
  }

  Hashtable hash= new Hashtable();
  Vector    vec = new Vector();
  public void setParameters(Hashtable params) {
	Enumeration e=params.keys();
	while (e.hasMoreElements()) {
	  String key=(String)e.nextElement();
	  System.out.println("SampleDataProducer::SetParameters -> " + key + "="+(String)params.get(key));
	}
  }
  public void performAction() throws Exception {
	for (int i=0;i<10;i++) {
	  hash.put("name"+i,"value"+i);
	  vec.addElement("name"+i);
	}
  }
  public Hashtable getData() {
	return hash;
  }
  public Vector getOrder() {
	return vec;
  }

  public String getLiteral() {
   return "none";
 }
  public String actionDone(String filename, HttpSession ses) throws ClsExceptions{
  return "";
  }
}