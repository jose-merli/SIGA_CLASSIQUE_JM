
/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsPagoColegiadoAdm;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.general.SIGAException;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF para los Pagos a Colegiados.
 */
public class InformeColegiadosFacturaciones extends MasterReport {
	
	protected String formatoImportes="999,999,999,999,990.00";
	
	public Hashtable getDatosInformeColegiado(UsrBean usr, Hashtable htDatos) throws ClsExceptions, SIGAException{
		UtilidadesHash.set(htDatos,"FECHA",UtilidadesBDAdm.getFechaEscritaBD((String)htDatos.get("idiomaExt")));
		String idioma = usr.getLanguage().toUpperCase();
		String institucion =usr.getLocation();
		String idFacturacion =(String)htDatos.get("idFacturacion");
		Hashtable htAux=null;
		//firmas
		htDatos.put("SECRETARIO","XXXXXXX XXXXXXXXXXXXXXX XXXXXXXXXX");
		htDatos.put("FIRMA","xxxxxxxxx xxxxxxxxx xxxxxxxx");
		htDatos.put("NDUPLICADO","XXXXXXXX");
		
		//datos colegio
		CenInstitucionAdm institAdm= new CenInstitucionAdm(usr);
		String nombreInstit=institAdm.getNombreInstitucion(institucion);
		if(nombreInstit!=null)htDatos.put("NOMBRE_INSTITUCION",nombreInstit);
		
		//datos cabecera
		String idPersona=(String) htDatos.get("idPersona");
		
		//Datos Persona
		CenPersonaAdm perAdm = new CenPersonaAdm(usr);
		CenPersonaBean beanPersona = perAdm.getPersonaColegiado(new Long(idPersona), new Integer(institucion));
		htDatos.put("NCOLEGIADO", beanPersona.getColegiado().getNColegiado());
		htDatos.put("NIF", beanPersona.getNIFCIF());
		
		//Datos Cabecera
		htAux=this.obtenerDatosPersonaSociedad(institucion,idPersona,usr, idFacturacion);
		htDatos.putAll(htAux);
				
		Hashtable htFacturacion=null;
		FcsFacturacionJGAdm factAdm = new FcsFacturacionJGAdm(usr);
		htFacturacion = factAdm.obtenerDetalleFacturacion(institucion, idFacturacion);
		htDatos.putAll(htFacturacion);
		
		return htDatos;
	}
	
	
	protected String reemplazarDatos(UsrBean usr, String plantillaFO, Hashtable htDatos) throws ClsExceptions, SIGAException{
		
		String cuenta=(String)htDatos.get("CUENTA_CORRIENTE");
		if (cuenta==null || cuenta.equals("")) {
			String delimIni=CTR+"INI_TODO_CUENTA"+CTR;
			String delimFin=CTR+"FIN_TODO_CUENTA"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}

	
		//Datos de las Asistencias
		Vector datosAsistencias = (Vector)htDatos.get("VASISTENCIAS");
		plantillaFO = this.reemplazaRegistros(plantillaFO,datosAsistencias,htDatos,"ASISTENCIA");
		
		//Datos de los Oficios
		Vector datosOficios = (Vector)htDatos.get("VOFICIOS");
		plantillaFO = this.reemplazaRegistros(plantillaFO,datosOficios,htDatos,"OFICIOS");
		
		//Datos del Pago y Totales
		String total2=(htDatos.get("TOTAL_MOVIMIENTOS")!=null)?(String)htDatos.get("TOTAL_MOVIMIENTOS"):"0";
		if (total2.length()>2) total2=total2.substring(0,total2.length()-2);
		if (total2.equals("0")) {
			String delimIni=CTR+"INI_TODO_MOVIMIENTOS"+CTR;
			String delimFin=CTR+"FIN_TODO_MOVIMIENTOS"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}
		total2=(htDatos.get("TOTAL_RETENCIONES")!=null)?(String)htDatos.get("TOTAL_RETENCIONES"):"0";
		if (total2.length()>2) total2=total2.substring(0,total2.length()-2);
		if (total2.equals("0")) {
			String delimIni=CTR+"INI_TODO_RETENCIONES"+CTR;
			String delimFin=CTR+"FIN_TODO_RETENCIONES"+CTR;
			String sAux="";
			plantillaFO=UtilidadesString.reemplazaEntreMarcasCon(plantillaFO, delimIni, delimFin,sAux);
		}
		
		return this.reemplazaVariables(htDatos,plantillaFO);
	} //reemplazarDatos()
	
	/**
	 * Obtienes nombre y direccion del letrado o Sociedad
	 * @param idInstitucion Identificador de la institucion
	 * @param idPersona Identificador del letrado o Sociedad
	 * @return
	 * @throws SIGAException
	 */	
	protected Hashtable obtenerDatosPersonaSociedad(String idInstitucion, String idPersona, UsrBean user, String idFacturacion) throws ClsExceptions {
		Hashtable result= new Hashtable();
		RowsContainer rc=null;
		
		String idSociedad=null;
		//UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		try {
			 //buscamos el nombre de la persona
			String sql=
				 "select "+UtilidadesMultidioma.getCampoMultidiomaSimple("T.DESCRIPCION",user.getLanguage())+"||' '||P.NOMBRE||' '||P.APELLIDOS1||' '||P.APELLIDOS2 NOMBRE_PERSONA" +
				 "  from CEN_PERSONA P, CEN_CLIENTE C, CEN_TRATAMIENTO T " +
				 " where C.IDPERSONA = P.IDPERSONA  " +
				 "   and C.IDTRATAMIENTO = T.IDTRATAMIENTO " +
				 "   and C.IDPERSONA = " +idPersona+
				 "   and C.IDINSTITUCION ="+idInstitucion;
			 rc = new RowsContainer();
			 rc.find(sql);
			 if(rc!=null && rc.size()>0){
				 Row r=(Row)rc.get(0);
				 result.putAll(r.getRow());
			 }
			 
			 //compruebo si actua en nombre de una sociedad
			 sql=
				 "select C.IDPERSONA, C.SOCIEDAD " +
				 "  from CEN_PERSONA P, CEN_COMPONENTES C " +
				 " where C.IDPERSONA = P.IDPERSONA " +
				 "   and C.CEN_CLIENTE_IDPERSONA = " +idPersona+
				 "   and C.CEN_CLIENTE_IDINSTITUCION = "+idInstitucion;
			 rc = new RowsContainer(); 
			 rc.find(sql);
			 if(rc!=null && rc.size()>0){
				 Row r=(Row)rc.get(0);
				 String sSociedad= r.getString("SOCIEDAD");
				 if(sSociedad!=null && sSociedad.equals(ClsConstants.DB_TRUE)){
					 idSociedad=r.getString("IDPERSONA");
				 }
			 }

			 
			 if(idSociedad==null){
				 //Si no existe sociedad sacamos los datos relativos a la persona
				 idSociedad=idPersona;
				 
			 }else{
				 //nombre de la sociedad
				 sql=
					 "select P.NOMBRE||' '||P.APELLIDOS1||' '||P.APELLIDOS2 NOMBRE_SOCIEDAD" +
					 "  from CEN_PERSONA P, CEN_CLIENTE C" +
					 " where C.IDPERSONA = P.IDPERSONA  " +
					 "   and C.IDPERSONA = " +idSociedad+
					 "   and C.IDINSTITUCION ="+idInstitucion;
				 rc = new RowsContainer();
				 rc.find(sql);
				 if(rc!=null && rc.size()>0){
					 Row r=(Row)rc.get(0);
					 result.putAll(r.getRow());
				 }
			 }
			
			
			//Direccion de la sociedad o persona
			 String direccion="";
				 direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"C");// buscamos una direccion preferente correo
				 rc = new RowsContainer();
					rc.find(direccion);
					if(rc!=null && rc.size()>0){
						Row r=(Row)rc.get(0);
						result.putAll(r.getRow());
					}
				 
				 if (rc==null || rc.size()==0 ) {
				 	direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"2");// si no hay direccion preferente Correo, buscamos cualquier direccion de despacho.
				 	 rc = new RowsContainer();
						rc.find(direccion);
						if(rc!=null && rc.size()>0){
							Row r=(Row)rc.get(0);
							result.putAll(r.getRow());
						}
				 	if (rc==null || rc.size()==0 ) {
				 		direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"3");// si no hay direccion de despacho, buscamos cualquier direccion de correo.
				 		 rc = new RowsContainer();
							rc.find(direccion);
							if(rc!=null && rc.size()>0){
								Row r=(Row)rc.get(0);
								result.putAll(r.getRow());
							}
				 		if (rc ==null || rc.size()==0 ){
				 			direccion=this.getDireccionCartaPago(idSociedad,idInstitucion,"");// si no hay direccion de correo, buscamos cualquier direccion.
				 			 rc = new RowsContainer();
								rc.find(direccion);
								if(rc!=null && rc.size()>0){
									Row r=(Row)rc.get(0);
									result.putAll(r.getRow());
								}				 		   
				 		}  
				 	}
				 }
				
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe");
		}

		return result;
	}		
	
	public String getDireccionCartaPago (String idSociedad, String idInstitucion, String idDireccion) throws ClsExceptions, SIGAException {
		   Hashtable datos=new Hashtable();
		   String sql="";
		   Row result=new Row();
	      
	            RowsContainer rc = new RowsContainer(); 
	            sql=
			    	"select D.DOMICILIO, D.CODIGOPOSTAL," +
			    	"(select p.nombre from cen_poblaciones p where p.idpoblacion=d.idpoblacion) POBLACION," +
			    	"(select p.nombre from cen_provincias p where p.idprovincia=d.idprovincia) PROVINCIA" +
			    	"  from CEN_DIRECCIONES D, CEN_DIRECCION_TIPODIRECCION DTD" +
			    	" where D.IDDIRECCION = DTD.IDDIRECCION  " +
			    	"   and D.IDINSTITUCION = DTD.IDINSTITUCION   " +
			    	"   and D.IDPERSONA = DTD.IDPERSONA    " +
			    	"   and d.fechabaja is null"+
			    	"   and D.IDPERSONA = " +idSociedad+
	                "   and D.IDINSTITUCION="+idInstitucion;
	                
							try{
							 if (idDireccion!=null){
								if ((new Integer(idDireccion)) != null){ 
									sql+=  " AND DTD." + CenDireccionTipoDireccionBean.C_IDTIPODIRECCION + "=" +idDireccion;
								}	
								else {
									throw new Exception ("");
								}
							 }	
							 	
							}	catch(Exception e){
								sql+=  " AND upper(D.PREFERENTE) like upper('%" +idDireccion+"%') ";
							}
							
	       return sql;                        
	    }
}