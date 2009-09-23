package com.siga.pki;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
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

public class SIGASignCreatePot  extends SIGASignPot
{
  public SIGASignCreatePot() {
  }

  public void setFileName() {
	if(this.sFileName.equals("")){
	  SimpleDateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
	  String nowDate=(String)  df.format(new Date());
	  String sFileName="createPot"+nowDate;
	  this.sFileName=sFileName;
	}
  }

  public String getFileName() {
	if(this.sFileName.equals("")){
	  setFileName();
	}
	return this.sFileName;
  }

/*  public void performAction() throws Exception {
  Enumeration e=hash.keys();
  while (e.hasMoreElements()) vec.add(e.nextElement());

  String lite=
   "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> "+
	"<tr><td colspan=\"6\" class=\"Title\">Pass Off Test Description</td></tr>" +
	"<tr>"+
   "<td width=\"26%\" class=\"labelText\">Test Specification</td>"+
   "<td width=\"18%\" align=\"right\" class=\"nonEdit\" > "+ putField("testSpecNo")+"</td>"+
   "<td align=\"center\" class=\"labelText\">Test Specification Issue</td>"+
   "<td align=\"center\" class=\"nonEdit\"> "+putField("testSpecIssue")+"</td>"+
	"</tr>"+
	"<tr>"+
   "<td class=\"labelText\">Test Bed Id</td>"+
   "<td align=\"right\"class=\"nonEdit\"> " + putField("testBedId")+"</td>"+
   "<td width=\"21%\" align=\"center\" class=\"labelText\">Location</td>"+
   "<td width=\"27%\" align=\"center\" class=\"nonEdit\">"+putField("locationPOT")+"</td>"+
  "</tr>"+
	"<tr>"+
   "<td width=\"26%\" class=\"labelText\">Date</td>"+
   "<td align=\"right\" class=\"nonEdit\">"+putField("dtDatePOT")+"</td>"+
   "<td class=\"nonEdit\">&nbsp;</td>"+
  "<td class=\"nonEdit\">&nbsp;</td>"+
	"</tr>"+
	"<tr>"+
   "<td class=\"labelText\">Fuel Specification</td>"+
   "<td align=\"right\" class=\"nonEdit\"> " + putField("fuelSpec")+"</td>"+
   "<td align=\"center\" class=\"labelText\">Oil Specification</td>"+
   "<td align=\"center\" class=\"nonEdit\">" + putField("oilSpec")+"</td>"+
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
	   "<td align=\"center\" valign=\"middle\" class=\"nonEdit\"> " + putField("dryCruiseThrust")+"</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\" class=\"nonEdit\"> " + putField("dryCruiseSFC")+"</td>"+
	 "</tr>"+
	 "<tr>"+
	   "<td class=\"tableTitle\">Max. Dry</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxDryThrust")+"</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxDrySOT")+"</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxDryTBT")+"</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxDryNL")+"</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxDryNH")+"</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	 "</tr>"+
	 "<tr>"+
	   "<td class=\"tableTitle\">Reheat Cruise></td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("rhCruiseSFC")+"</td>"+
	 "</tr>"+
	 "<tr>"+
	   "<td class=\"tableTitle\">Max. Reheat</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxRhThrust")+"</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxRhTBT")+"</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxRhNL")+"</td>"+
	   "<td align=\"center\" valign=\"middle\"class=\"nonEdit\"> " + putField("maxRhNH")+"</td>"+
	   "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	 "</tr>"+
	 "<tr>"+
	   "<td colspan=\"2\" align=\"center\" class=\"labelText\">Oil Consumption</td>"+
	   "<td align=\"center\" class=\"nonEdit\"> " + putField("oilConsumption")+"&nbsp;</td>"+
	   "<td align=\"center\" class=\"nonEdit\">I/Hour</td>"+
	   "<td colspan=\"2\" align=\"center\" class=\"labelText\">Total Running Time</td>"+
	   "<td align=\"center\" class=\"nonEdit\">" + putField("totalRunningTime")+"</td>"+
	 "</tr>"+
	 "<tr>"+
	   "<td colspan=\"2\" align=\"center\" class=\"labelText\">Total Starts</td>"+
	   "<td align=\"center\" class=\"nonEdit\">" + putField("starts")+"</td>"+
	   "<td colspan=\"4\" align=\"right\">&nbsp;</td>"+
	 "</tr>"+
	  "</table>"+
	 "</td>"+
	"</tr>"+
	 "</table>";
	lit=lite;

  } */

  public String actionDone(String filename, HttpSession ses) throws ClsExceptions{
	System.out.println("Action done en CreatePot ");
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
		pot.add();
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