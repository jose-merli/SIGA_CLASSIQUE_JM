package com.siga.pki;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.GstStringTokenizer;

public abstract class SIGASignCompare
{
  protected String lit="none";
  protected String sFileName="";
  protected String signedData;
  protected String actualData;
  protected Hashtable hash= new Hashtable();
  protected Vector    vec = new Vector();

  abstract public String getActualData();

  public SIGASignCompare() {
  }

  public void setParameters(Hashtable params) {
	hash=params;
	Enumeration e=params.keys();
	while (e.hasMoreElements()) {
	  String key=(String)e.nextElement();
//	  System.out.println("SIGASignCompare::SetParameters -> " + key + "="+(String)params.get(key));
	}
  }

  public void setSignedData(String data) {
	signedData=data;
  }

  public boolean compareData(String[] keys) {
	if (actualData==null || signedData==null)
	  return false;

	if (actualData.equals(signedData)) {
//      System.out.println("***********************SON IGUALES");
	  return true;
	}

	if (getDiferences(keys).size()==0)
	  return true;
	return false;
  }

  public Vector getDiferences(String[] keys) {
	Vector vec=new Vector();
	if (actualData.equals(signedData)) {
//      System.out.println("***********************SON IGUALES");
	  return vec;
	}


	GstStringTokenizer actual = new GstStringTokenizer(actualData,SIGAPkiDataProducer.INIT_VALUE);
	String params[] = new String [actual.countTokens()];
	int i=0;
	while (actual.hasMoreTokens()) {
	  params[i] = actual.nextToken();
	  int index=params[i].indexOf(SIGAPkiDataProducer.END_VALUE);
	  if (index!=-1)
		params[i]=params[i].substring(0,index);
	  i++;
	}

	GstStringTokenizer firmado = new GstStringTokenizer(signedData,SIGAPkiDataProducer.INIT_VALUE);
	String paramsf[] = new String [firmado.countTokens()];
	i=0;
	while (firmado.hasMoreTokens()) {
	  paramsf[i] = firmado.nextToken();
	  int index=paramsf[i].indexOf(SIGAPkiDataProducer.END_VALUE);
	  if (index!=-1)
		paramsf[i]=paramsf[i].substring(0,index);
//      System.out.println(paramsf[i]);
	  i++;
	}

	for (i=0;i<params.length;i++) {
	  if (params[i].equals(paramsf[i])) {
		params[i]="";
		paramsf[i]="";
	  } else {
//        System.out.println("*************"+keys[i] + " = (firmado)="+paramsf[i] + " (actual)="+params[i]);
		String[] diff=new String[] {
			keys[i],
			paramsf[i],
			params[i]
		};
		vec.add(diff);
	  }
	}
	return vec;
  }
}