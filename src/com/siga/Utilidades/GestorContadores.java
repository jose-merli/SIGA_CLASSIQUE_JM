package com.siga.Utilidades;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmContadorAdm;
import com.siga.beans.AdmContadorBean;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.general.SIGAException;


/**
 * Clase GestorContadores
 * <BR>
 * Realiza automaticamente la gestion de contadores para formar un numero de registro valido
 * en sociedades juridicas.
 */
public class GestorContadores {

	private UsrBean usu = null;
	
	public GestorContadores(UsrBean _usu) {
		usu = _usu;
	}
	
	
	public Hashtable getContador(Integer idInstitucion, String idContador) throws ClsExceptions, SIGAException {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			htData.put("IDINSTITUCION",idInstitucion);
			htData.put("IDCONTADOR",idContador);
			AdmContadorAdm admContador =new AdmContadorAdm(this.usu);
			Vector vAdmContador=(Vector)(admContador.selectByPK(htData));
			if (vAdmContador!=null && vAdmContador.size()>0){
				AdmContadorBean contador = (AdmContadorBean) vAdmContador.get(0);
				htData.put("NOMBRE",contador.getNombre());
				htData.put("DESCRIPCION",contador.getDescripcion());
				htData.put("MODIFICABLECONTADOR",contador.getModificableContador());
				htData.put("MODO",contador.getModoContador());
				htData.put("CONTADOR",contador.getContador());
				htData.put("PREFIJO",contador.getPrefijo());
				htData.put("SUFIJO",contador.getSufijo());
				htData.put("LONGITUDCONTADOR",contador.getLongitudContador());
				htData.put("FECHARECONFIGURACION",contador.getFechaReconfiguracion());
				htData.put("RECONFIGURACIONCONTADOR",contador.getReconfiguracionContador());
				htData.put("RECONFIGURACIONPREFIJO",contador.getReconfiguracionPrefijo());
				htData.put("RECONFIGURACIONSUFIJO",contador.getReconfiguracionSufijo());
				htData.put("IDTABLA",contador.getIdTabla());
				htData.put("IDCAMPOCONTADOR",contador.getIdCampoContador());
				htData.put("IDCAMPOPREFIJO",contador.getIdCampoPrefijo());
				htData.put("IDCAMPOSUFIJO",contador.getIdCampoSufijo());
				
				
			}else{
				throw new SIGAException("messages.contador.error.contadorNoConfigurado");
			}
			
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al obtener los datos del contador");
		}
		return htData;	
	}
	
	public Hashtable getContador(Integer idInstitucion, String idContador, String prefijo, String sufijo) throws ClsExceptions, SIGAException {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			htData.put("IDINSTITUCION",idInstitucion);
			htData.put("IDCONTADOR",idContador);
			AdmContadorAdm admContador =new AdmContadorAdm(this.usu);
			Vector vAdmContador=(Vector)(admContador.selectByPK(htData));
			if (vAdmContador!=null){
				AdmContadorBean contador = (AdmContadorBean) vAdmContador.get(0);
				htData.put("NOMBRE",contador.getNombre());
				htData.put("DESCRIPCION",contador.getDescripcion());
				htData.put("MODIFICABLECONTADOR",contador.getModificableContador());
				htData.put("MODO",contador.getModoContador());
				htData.put("CONTADOR",contador.getContador());
				htData.put("PREFIJO",prefijo);
				htData.put("SUFIJO",sufijo);
				htData.put("LONGITUDCONTADOR",contador.getLongitudContador());
				htData.put("FECHARECONFIGURACION",contador.getFechaReconfiguracion());
				htData.put("RECONFIGURACIONCONTADOR",contador.getReconfiguracionContador());
				htData.put("RECONFIGURACIONPREFIJO",contador.getReconfiguracionPrefijo());
				htData.put("RECONFIGURACIONSUFIJO",contador.getReconfiguracionSufijo());
				htData.put("IDTABLA",contador.getIdTabla());
				htData.put("IDCAMPOCONTADOR",contador.getIdCampoContador());
				htData.put("IDCAMPOPREFIJO",contador.getIdCampoPrefijo());
				htData.put("IDCAMPOSUFIJO",contador.getIdCampoSufijo());
				
				
			}else{
				throw new SIGAException("messages.contador.error.contadorNoConfigurado");
			}	
			
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al obtener los datos del contador");
		}
		return htData;	
	}
	public void setContador(Hashtable datosContadorNuevo, String numRegNuevo) throws ClsExceptions, SIGAException {
		Hashtable htData = null;
		Hashtable gcOriginal = null;//registro contador que se obtiene de la tabla ADM_CONTADORES con el idinstitucion y el idcontador
		try {
			gcOriginal=getContador((Integer)datosContadorNuevo.get("IDINSTITUCION"),(String)datosContadorNuevo.get("IDCONTADOR"));
		// Solo actualizamos la tabla Adm_Contador si el contador introducido por pantalla es mayor que el almacenado en la tabla de contadores
		 Long numReg =new Long(numRegNuevo);
		 
		 if (numReg.longValue()>((Long)gcOriginal.get("CONTADOR")).longValue() ){	
			datosContadorNuevo.put("CONTADOR",numRegNuevo);
			
			if ((gcOriginal.get("PREFIJO").equals(datosContadorNuevo.get("PREFIJO")))&&(gcOriginal.get("SUFIJO").equals(datosContadorNuevo.get("SUFIJO")))){
				//Solo en el caso de que el usuario no ha modificado el prefijo y sufijo que se le propone cuando se inserta una nueva sociedad, entonces
				//se llama al metod actualizarNumContador para actualizar el campo contador en la tabla ADM_CONTADORES con el
				//último utilizado
				actualizarNumContador(datosContadorNuevo,gcOriginal);
				
			}
		 }	
			
		}
		catch (SIGAException e) {
			
			throw e;
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
	
	}
	
	public int getSiguienteNumContador(Hashtable contador) throws ClsExceptions, SIGAException {
		int contadorSiguiente=0;
		try {
			contadorSiguiente=buscarSiguienteNumContadorLibre(contador);
			
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
		return contadorSiguiente;	
	}
	
	private void actualizarNumContador(Hashtable contadorNuevo, Hashtable contadorOld) throws ClsExceptions {
		
		try {
		AdmContadorAdm admContador=new AdmContadorAdm(this.usu);
		admContador.update(contadorNuevo,contadorOld);
			
		}
		catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
		
	}

	private int buscarSiguienteNumContadorLibre(Hashtable contador) throws ClsExceptions, SIGAException {
		int contadorSiguiente=0;
		RowsContainer rc=null;
		Hashtable gcOriginal=null;
		Integer longitud;
		
		String contadorlongitud="";
	
		
		try {
			gcOriginal=getContador((Integer)contador.get("IDINSTITUCION"),(String)contador.get("IDCONTADOR"));
			if ((gcOriginal.get("PREFIJO").equals(contador.get("PREFIJO")))&&(gcOriginal.get("SUFIJO").equals(contador.get("SUFIJO")))){ 
				contadorSiguiente=Integer.parseInt((contador.get("CONTADOR")).toString());
				
				//Comprobamos que el contador que se sugiere no supera la longitud definida
			
				validarLogitudContador(contadorSiguiente,contador);
					
				//Comprobamos la unicidad de este contador junto con el prefijo y sufijo guardado en la hash contador
				while(this.comprobarUnicidadContador(contadorSiguiente,contador)){
					contadorSiguiente++;
//					Comprobamos que el contador que se sugiere no supera la longitud definida
					
					validarLogitudContador(contadorSiguiente,contador);
				}
					
			
			}else{
			
				String select="SELECT max("+contador.get("IDCAMPOCONTADOR")+")+1 CONTADOR FROM "+contador.get("IDTABLA")+ 
	              " WHERE IDINSTITUCION = "+contador.get("IDINSTITUCION")+
	              " and "+contador.get("IDCAMPOPREFIJO")+"= '"+contador.get("PREFIJO")+"'"+
	              " and "+contador.get("IDCAMPOSUFIJO")+"= '"+contador.get("SUFIJO")+"'";
	             
					
					rc = new RowsContainer();
					
					if (rc.find(select)) {
						Hashtable htRow=((Row)rc.get(0)).getRow();
						if(!((String)htRow.get("CONTADOR")).equals("")) {
							Integer contadorSugerido = Integer.valueOf((String)htRow.get("CONTADOR"));
							
						    contadorSiguiente=contadorSugerido.intValue();
						   
						
					} else{
						contadorSiguiente=1;
						
					}
						
					}
					
				//	Comprobamos que el contador que se sugiere no supera la longitud definida
					
					validarLogitudContador(contadorSiguiente,contador);
					 
				
				
			}
			
			
			
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
		return contadorSiguiente;
	}
	
	
	/**
	 * Método que devuelve el siguiente contador siguiente al insertado en la tabla ADM_CONTADOR sin
	 * validar su unicidad en el sistema
	 * 
	 */
	private int buscarSiguienteContador(Hashtable contador) throws ClsExceptions, SIGAException {
		int contadorSiguiente=0;
		RowsContainer rc=null;
		Hashtable gcOriginal=null;
		Integer longitud;
		
		String contadorlongitud="";
	
		
		try {
			gcOriginal=getContador((Integer)contador.get("IDINSTITUCION"),(String)contador.get("IDCONTADOR"));
		 
				contadorSiguiente=(Integer.parseInt((contador.get("CONTADOR")).toString())+1);
				
				//Comprobamos que el contador que se sugiere no supera la longitud definida
			
				validarLogitudContador(contadorSiguiente,contador);
					
				
			
			
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
		return contadorSiguiente;
	}
	
	public String getNuevoContador (Hashtable contenidoAdmContador) throws ClsExceptions, SIGAException 
	{

		String contadorFinalSugerido="";
		Hashtable contadorTablaHash=new Hashtable();
		try { 
//			Obtenemos los datos de la tabla de contadores 
			
			
			Integer longitud= new Integer((contenidoAdmContador.get("LONGITUDCONTADOR").toString()));
			int longitudContador=longitud.intValue();
			int contadorNuevo=buscarSiguienteContador(contenidoAdmContador);
		  	Integer contadorSugerido=new Integer(contadorNuevo);
		  	
		  	 contadorFinalSugerido=UtilidadesString.formatea(contadorSugerido,longitudContador,true);
		}	


		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return contadorFinalSugerido;
	}
	public String getNuevoContadorConPrefijoSufijo (Hashtable contenidoAdmContador) throws ClsExceptions, SIGAException 
	{

		
		String contadorFinalSugerido="";
		Hashtable contadorTablaHash=new Hashtable();
		try { 

			String numeroAbono=getNuevoContador (contenidoAdmContador);
		  	contadorFinalSugerido=contenidoAdmContador.get("PREFIJO")+numeroAbono+contenidoAdmContador.get("SUFIJO");
		}	


		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return contadorFinalSugerido;
	}

	
	public boolean comprobarUnicidadContador(int contadorSiguiente,Hashtable contador) throws ClsExceptions {
		
		RowsContainer rc=null;
		boolean existe=false;
		String select="";
		try {
			if (contador.get("PREFIJO")!=null && !contador.get("PREFIJO").equals("")&& contador.get("SUFIJO")!=null && !contador.get("SUFIJO").equals("")){
			//Comprobamos la unicidad de este contador junto con el prefijo y sufijo guardado en la hash contador
			      select=" SELECT *  FROM "+contador.get("IDTABLA")+ 
			              " WHERE IDINSTITUCION = "+contador.get("IDINSTITUCION")+
			              " and "+contador.get("IDCAMPOPREFIJO")+"= '"+contador.get("PREFIJO")+"'"+
			              " and "+contador.get("IDCAMPOSUFIJO")+"= '"+contador.get("SUFIJO")+"'"+
			              " and "+contador.get("IDCAMPOCONTADOR")+"= "+contadorSiguiente;
			}else{
				if (contador.get("PREFIJO")!=null && !contador.get("PREFIJO").equals("")){
					 select=" SELECT *  FROM "+contador.get("IDTABLA")+ 
		              " WHERE IDINSTITUCION = "+contador.get("IDINSTITUCION")+
		              " and "+contador.get("IDCAMPOPREFIJO")+" is null "+
		              " and "+contador.get("IDCAMPOSUFIJO")+"= '"+contador.get("SUFIJO")+"'"+
		              " and "+contador.get("IDCAMPOCONTADOR")+"= "+contadorSiguiente;
				}else if (contador.get("SUFIJO")!=null && !contador.get("SUFIJO").equals("")){
					select=" SELECT *  FROM "+contador.get("IDTABLA")+ 
		              " WHERE IDINSTITUCION = "+contador.get("IDINSTITUCION")+
		              " and "+contador.get("IDCAMPOPREFIJO")+"= '"+contador.get("PREFIJO")+"'"+
		              " and "+contador.get("IDCAMPOSUFIJO")+" IS NULL "+
		              " and "+contador.get("IDCAMPOCONTADOR")+"= "+contadorSiguiente;
				
				}else{
					select=" SELECT *  FROM "+contador.get("IDTABLA")+ 
		              " WHERE IDINSTITUCION = "+contador.get("IDINSTITUCION")+
		              " and "+contador.get("IDCAMPOPREFIJO")+" is null "+
		              " and "+contador.get("IDCAMPOSUFIJO")+" is null "+
		              " and "+contador.get("IDCAMPOCONTADOR")+"= "+contadorSiguiente;
				}
			}
			
			rc = new RowsContainer();
			
			if(rc.find(select)){
				existe=true;
				
			}else{
				existe=false;
			}
				
			
			
			
		}
		catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
		return existe;
	}

	
	public boolean comprobarUnicidadContadorProdCertif(int contadorSiguiente,Hashtable contador, String idTipoProducto, String idProducto, String idProductoInstitucion) throws ClsExceptions {
		
		RowsContainer rc=null;
		boolean existe=false;
		try {
			
			//Comprobamos la unicidad de este contador junto con el prefijo y sufijo guardado en la hash contador
			String select="SELECT *  FROM "+contador.get("IDTABLA")+ 
			              " WHERE IDINSTITUCION = "+contador.get("IDINSTITUCION")+
						  " and "+contador.get("IDTABLA")+"."+CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO+"= "+ idTipoProducto+
						  " and "+contador.get("IDTABLA")+"."+CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO+"= "+ idProducto+
						  " and "+contador.get("IDTABLA")+"."+CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION+"= "+ idProductoInstitucion+
			              " and TRIM(NVL("+contador.get("IDCAMPOPREFIJO")+",'1')) = TRIM(NVL('"+contador.get("PREFIJO")+"','1'))"+
			              " and TRIM(NVL("+contador.get("IDCAMPOSUFIJO") +",'1')) = TRIM(NVL('"+contador.get("SUFIJO") +"','1'))"+
			              " and "+contador.get("IDCAMPOCONTADOR")+"= "+contadorSiguiente;
			
			rc = new RowsContainer();
			
			if(rc.find(select)){
				existe=true;
				
			}else{
				existe=false;
			}
				
			
			
			
		}
		catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
		return existe;
	}

	
	public void validarLogitudContador(int contadorSiguiente, Hashtable contador) throws ClsExceptions,SIGAException {
		
		try {
			String contadorlongitud =new Integer(contadorSiguiente).toString();
			Integer longitud =(Integer)contador.get("LONGITUDCONTADOR");
			if (contadorlongitud.length()>longitud.intValue()){
				if 	(contador.get("MODO").toString().equals("1")){
					throw new SIGAException("messages.contador.error.longitudSuperadaH");
				}else{
					throw new SIGAException("messages.contador.error.longitudSuperada");
				}
		    }
			
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			
			throw new ClsExceptions (e, "Error al modificar los datos del contador");
		}
		
	}

}
