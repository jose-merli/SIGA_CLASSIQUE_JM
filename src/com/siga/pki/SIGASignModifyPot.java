package com.siga.pki;
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

public class SIGASignModifyPot extends SIGASignPot
{
  public SIGASignModifyPot() {
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

	System.out.println("Action done en modifyMov ");
	System.out.println("   Internal es " + hash.get("internal"));
	System.out.println("   movnumber     es " + hash.get("movnumber"));
	System.out.println("   Nombre file " + getFileName());
   return "";
  }
}