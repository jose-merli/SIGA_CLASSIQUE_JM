package com.siga.pki;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
//import siga.engineMaintenance.assetManagement.assetLocation.importExportLB.PSSCPot;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;

public class SIGASignPot extends SIGAPkiDataProducer
{
  String lit="none";
  String sFileName="";
  Hashtable hash= new Hashtable();
  Vector    vec = new Vector();
  public static String [] keys = new String[] {
"ACTION",
"TESTSPECNO",
"TESTSPECISSUE",
"TESTBEDID",
"locationPOT",
"dtDatePOT",
"fuelSpec",
"oilSpec",
"dryCruiseThrust",
"dryCruiseSFC",
"maxDryThrust",
"maxDrySOT",
"maxDryTBT",
"maxDryNL",
"maxDryNH",
"rhCruiseSFC",
"maxRhThrust",
"maxRhTBT",
"maxRhNL",
"maxRhNH",
"oilConsumption",
"totalRunningTime",
"starts"
  };
  public SIGASignPot() {
  }

  public void setParameters(Hashtable params) {
	Enumeration e=params.keys();
	while (e.hasMoreElements()) {
	  String key=(String)e.nextElement();
//      System.out.println("SIGASignPot K : " + key.toUpperCase() + " -> " + params.get(key));
	  hash.put(key.toUpperCase(),params.get(key));
	}
  }

  public void setFileName() {
	  if(this.sFileName.equals("")){
		SimpleDateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
		String nowDate=(String)  df.format(new Date());
		  String sFileName="Pot"+nowDate;
		  this.sFileName=sFileName;
	}
  }

  public String getFileName() {
	if(this.sFileName.equals("")){
		setFileName();
	}
	return this.sFileName;
}

 public void performAction() throws Exception {
	 Enumeration e=hash.keys();
	 while (e.hasMoreElements()) vec.add(e.nextElement());


	 String lite=
		 "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> "+
	   "<tr><td colspan=\"6\" class=\"Title\">Pass Off Test Description</td></tr>" +
			 "<tr> "+
			 "<TD width='130' class='labelText'>action</TD>"+
			 "<TD width='100' class='nonEdit'>"+putField(keys[0])+"</TD>"+
			"<td></td><td></td></tr> " +
	   "<tr>"+
		 "<td width=\"26%\" class=\"labelText\">Test Specification</td>"+
		 "<td width=\"18%\" align=\"right\" class=\"nonEdit\" > "+ putField(keys[1])+"</td>"+
		 "<td align=\"center\" class=\"labelText\">Test Specification Issue</td>"+
		 "<td align=\"center\" class=\"nonEdit\"> "+putField(keys[2])+"</td>"+
	   "</tr>"+
	   "<tr>"+
		 "<td class=\"labelText\">Test Bed Id</td>"+
		 "<td align=\"right\"class=\"nonEdit\"> " + putField(keys[3])+"</td>"+
		 "<td width=\"21%\" align=\"center\" class=\"labelText\">Location</td>"+
		 "<td width=\"27%\" align=\"center\" class=\"nonEdit\">"+putField(keys[4])+"</td>"+
		"</tr>"+
	   "<tr>"+
		 "<td width=\"26%\" class=\"labelText\">Date</td>"+
		 "<td align=\"right\" class=\"nonEdit\">"+putField(keys[5])+"</td>"+
		 "<td class=\"nonEdit\">&nbsp;</td>"+
		"<td class=\"nonEdit\">&nbsp;</td>"+
	   "</tr>"+
	   "<tr>"+
		 "<td class=\"labelText\">Fuel Specification</td>"+
		 "<td align=\"right\" class=\"nonEdit\"> " + putField(keys[6])+"</td>"+
		 "<td align=\"center\" class=\"labelText\">Oil Specification</td>"+
		 "<td align=\"center\" class=\"nonEdit\">" + putField(keys[7])+"</td>"+
		"</tr>"+
	   "<tr>"+
		 "<td class=\"labelText\">&nbsp;</td>"+
		 "<td>&nbsp;</td>"+
		 "<td class=\"labelText\">&nbsp;</td>"+
		 "<td>&nbsp;</td>"+
	   "</tr>"+
	 "</table>"+
   " <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"#34244f\">"+
			 "<tr>"+
			   "<td  class=\"Title\">Results</td>"+
			 "</tr>"+
			 "<tr>"+
			   "<td >"+
			   "<table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#dedbe8\">"+
				 "<tr class=\"tableTitle\">"+
				   "<td width=\"18%\">&nbsp;</td>"+
				   "<td width=\"19%\" >Thrusts</td>"+
				   "<td width=\"13%\">SOT (K)</td>"+
				   "<td width=\"13%\" >TBT (K)</td>"+
				   "<td width=\"12%\" >NL %</td>"+
				   "<td width=\"12%\" >NH %</td>"+
				   "<td width=\"13%\" >SFC (g/s/Kn)</td>"+
				 "</tr>"+
				 "<tr>"+
				   "<td class=\"tableTitle\">Dry Cruise</td>"+
				   "<td align=\"center\" valign=\"middle\" class=\"nonEdit\"> " + putField(keys[8])+"</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\" class=\"nonEdit\"> " + putField(keys[9])+"</td>"+
				 "</tr>"+
				 "<tr>"+
				   "<td class=\"tableTitle\">Max. Dry</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[10])+"</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[11])+"</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[12])+"</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[13])+"</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[14])+"</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				 "</tr>"+
				 "<tr>"+
				   "<td class=\"tableTitle\">Reheat Cruise></td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[15])+"</td>"+
				 "</tr>"+
				 "<tr>"+
				   "<td class=\"tableTitle\">Max. Reheat</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[16])+"</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[17])+"</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[18])+"</td>"+
				   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField(keys[19])+"</td>"+
				   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
				 "</tr>"+
				 "<tr>"+
				   "<td colspan=\"2\" align=\"center\" class=\"labelText\">Oil Consumption</td>"+
				   "<td align=\"center\" class=\"nonEdit\"> " + putField(keys[20])+"&nbsp;</td>"+
				   "<td align=\"center\" class=\"nonEdit\">I/Hour</td>"+
				   "<td colspan=\"2\" align=\"center\" class=\"labelText\">Total Running Time</td>"+
				   "<td align=\"center\" class=\"nonEdit\">" + putField(keys[21])+"</td>"+
				 "</tr>"+
				 "<tr>"+
				   "<td colspan=\"2\" align=\"center\" class=\"labelText\">Total Starts</td>"+
				   "<td align=\"center\" class=\"nonEdit\">" + putField(keys[22])+"</td>"+
				   "<td colspan=\"4\" align=\"right\">&nbsp;</td>"+
				 "</tr>"+
			   "</table>"+
				 "</td>"+
			 "</tr>"+
		   "</table>";
	   lit=lite;

  }
  public Hashtable getData() {
	return hash;
  }
  public Vector getOrder() {
	return vec;
  }

  protected String putField(String field) {
	String campo=(String)hash.get(field.toUpperCase());
	if (campo==null) campo="";
	String toRet=SIGASignPot.INIT_VALUE+campo+SIGASignPot.END_VALUE;
	return toRet;
  }

  public String getLiteral() {
	return lit;
  }

  public String actionDone(String filename, HttpSession ses) throws ClsExceptions{
	System.out.println("Action done en ModifyPot ");
	System.out.println("   Internal es " + hash.get("internal"));
	System.out.println("   Date     es " + hash.get("dtDatePOT"));
	System.out.println("   Nombre file " + filename);

	UsrBean usrbean;
	UserTransaction tx = null;
	Hashtable values = new Hashtable();

	try{
		 if (((usrbean=(UsrBean)ses.getAttribute("USRBEAN")) != null)){

/*
			PSSCPot pot = (PSSCPot)ses.getAttribute("pot");
			ses.removeAttribute("pot");

			String lang = usrbean.getLanguage();
			String dateConvert=com.atos.utils.GstDate.getApplicationFormatDate(lang,pot.getSdtDatePOT());
			pot.setSdtDatePOT(dateConvert);
*/
			tx = usrbean.getTransaction();
			tx.begin();
			/*
			pot.setSDigitalSign(getFileName());

			pot.update();
			*/
			tx.commit();
		}else{
			 throw new ClsExceptions("Any parameter from request or session is null","movement","84","GEN00","7");
		}

	}catch(ClsExceptions e){
			throw e;
	}catch(SystemException e){
			throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(NotSupportedException e){
			throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(HeuristicMixedException e){
			throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(HeuristicRollbackException e){
			throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(RollbackException e){
			throw new ClsExceptions(e.getMessage(),"","0","GEN00","15");
	}catch(Exception e){
			throw new ClsExceptions(e.getMessage(),"movement","84","GEN00","7");
	}
return "";
  }
}


