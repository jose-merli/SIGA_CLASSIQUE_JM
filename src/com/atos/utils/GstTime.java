/**
 * <p>Title: GstTime </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public class GstTime {
	double dResult;
	float fResult;
	Float f=null;
	String sResult="";
	String sHours="";
	String sMinutes="";
	String sSeconds="";
	String sFlat="mas";

  public GstTime() {

  }

  public boolean setHoras(String st)throws ClsExceptions{
	boolean flag=true;
	try{
		f=new Float(st);
//		if(f.intValue()<60){
			fResult=f.floatValue()*60;
//		}else flag=false;
	  }catch(Exception e){
		flag=false;
		throw new ClsExceptions("error en Time     :"+e.toString());
	   }
	return flag;
  }

	public boolean setHorasMinutos(String st)throws ClsExceptions{
			boolean flag=true;
			try{
			int i=st.indexOf(":");
			String horas= st.substring(0,i);

			String minutos=st.substring(i+1,st.length());

			flag=setHoras(horas);
			if (flag){
				Float flo=null;
				f=null;
				flo=Float.valueOf(minutos);
				if(flo.intValue()<60){
					fResult=fResult+flo.floatValue();
				}else flag=false;
			}
		}catch(Exception e){
			flag=false;
			fResult=fResult+0;
			throw new ClsExceptions("error en Time     :"+e.toString());
		}
		return flag;
	}


	public boolean setMinutesSecond(String st)throws ClsExceptions{
			boolean flag=true;
			try{
			int i=st.indexOf(":");
			String minutes= st.substring(0,i);
			String seconds=st.substring(i+1,st.length());
			flag=setMinutes(minutes);
			if (flag){
				Float flo=null;
				f=null;
				flo=Float.valueOf(seconds);
				if(flo.intValue()<60){
					fResult=fResult+flo.floatValue()/60;
				}else flag=false;
			}
		}catch(Exception e){
			  flag=false;
			 fResult=fResult+0;
			throw new ClsExceptions("error en Time     :"+e.toString());
		}
		return flag;
	}

	public boolean setMinutes(String st)throws ClsExceptions{
	  boolean flag=true;
	  try{
			f=new Float(st);
				fResult=f.floatValue();
		}catch(Exception e){
		  flag=false;
			throw new ClsExceptions("error into Time     :"+e.toString());
		 }
	  return flag;
  }


	public boolean setHorasMinSegundos(String st)throws ClsExceptions{
	  boolean flag=true;
	  try{
		int i=st.indexOf(":");
		String minusegun= st.substring(i+1,st.length());

		int a=minusegun.indexOf(":");
		String horas= st.substring(0,i+a+1);

		flag=setHorasMinutos(horas);

		if(flag){
			String segundos=st.substring(i+a+2,st.length());
			Float flo=null;
			f=null;
			flo=Float.valueOf(segundos);
			if(flo.intValue()<60){
				fResult=fResult+(flo.floatValue()/60);
			}else flag=false;

		}
	  }catch(Exception e){
		 flag=false;
		 fResult=fResult+0;
		throw new ClsExceptions("error en Time     :"+e.toString());
	  }
	  return flag;
	}

	public float getFloatMinutos(){
		return fResult;
	}

	public String getMinutos(){
		return new Float(fResult).toString();
	}

	public String getStringTimeHorasMinuSeconds(float f)throws ClsExceptions{
		try{
			if(putTimeHorasMinu(f)){
				sResult="";
				if(sFlat.equals("menos"))sResult="-";
				sResult+=this.sHours+":"+this.sMinutes+":"+this.sSeconds;


			}else sResult="error";
		}catch(Exception e){
			sResult="error";
			throw new ClsExceptions("error en Time     :"+e.toString());
		}
		return sResult;
	}


	public String getStringTimeMinuSecond(float f)throws ClsExceptions{
		try{
		  f= f*60;
			if(putTimeHorasMinu(f)){
				sResult="";
				if(sFlat.equals("menos"))sResult="-";
				sResult+=this.sHours+":"+this.sMinutes;
			}else sResult="error";
		}catch(Exception e){
			sResult="error";
			throw new ClsExceptions("error en Time     :"+e.toString());
		}
		return sResult;
	}

	public String getStringTimeHorasMinu(float f)throws ClsExceptions{
		try{

			  if(putTimeHorasMinu(f)){
				sResult="";
				if(sFlat.equals("menos"))sResult="-";
				  sResult+=this.sHours+":"+this.sMinutes;
			  }else sResult="error";
		}catch(Exception e){
			  sResult="error";
			throw new ClsExceptions("error en Time     :"+e.toString());
		}
		return sResult;
	}

	private boolean putTimeHorasMinu(float f){
		boolean flag=true;
		sFlat="mas";
		try{
//			Long lo=new Long(Math.round(f));
// 			float numSeconds=(f-lo.floatValue());
//			Long lSeconds=new Long(Math.round((numSeconds%100)*60/100));
			if(f<0){
			sFlat="menos";
			f=Math.abs(f);
			}

				  Float fSec = new Float(f);
						Long lo=new Long(fSec.intValue());

					float numSeconds=(f-lo.floatValue());
						Long lSeconds=new Long(Math.round(numSeconds* 60));

			this.sSeconds=lSeconds.toString();
// 			Long lMinutes= new Long(Math.round((f%60)));
						Long lMinutes= new Long(Math.round((lo.floatValue() % 60)));

			this.sMinutes=lMinutes.toString();

			f=f-lMinutes.floatValue();

			Long lHours= new Long(Math.round(f/60));
		//				Long lHours= new Long(Math.round(f.floatValue()/60));
			this.sHours=lHours.toString();

// control 60 second
			 if(sSeconds.equals("60")){
			  sSeconds = "00";
			  lSeconds = new Long("00");
			  lMinutes = new Long(lMinutes.longValue() + Long.parseLong("1"));
			  this.sMinutes=lMinutes.toString();
			}
// control 60 minutes
			if(sMinutes.equals("60")){
			  sMinutes = "00";
			  lMinutes = new Long("0");
			  lHours = new Long(lHours.longValue() + Long.parseLong("1"));
			  this.sHours=lHours.toString();
			}
// control 61 minutes
			if(sMinutes.equals("61")){
			  sMinutes = "01";
			  lMinutes = new Long("1");
			  lHours = new Long(lHours.longValue() + Long.parseLong("1"));
			  this.sHours=lHours.toString();
			}


			if(this.sSeconds.length()<2){
				this.sSeconds="0"+this.sSeconds;
			}
			if(this.sMinutes.length()<2){
				this.sMinutes="0"+this.sMinutes;
		   }
			if(this.sHours.length()<2){
				this.sHours="0"+this.sHours;
		  }

		}catch(Exception e){
			flag=false;
			this.sSeconds="00";
			this.sMinutes="00";
			this.sHours="00";
		}
		return flag;
	}
}