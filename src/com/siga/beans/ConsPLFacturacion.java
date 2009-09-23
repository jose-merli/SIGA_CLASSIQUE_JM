
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
/**
 * Implementa las funciones para el acceso mediante PL
 * 
 * @author carlos.vidal
 * @since 1/3/2005 
 */ 
  
public class ConsPLFacturacion {
	/**
	 * Constantes PETICION
	 */
	public static final String FACTURA 	= "0";
	public static final String ABONO 	= "1";
	/**
	 * Constantes ESTADOS
	 */
	public static final int PAGADA 							= 1;
	public static final int PENDIENTE_COBRO 				= 2;
	public static final int RENEGOCIADA 					= 3;
	public static final int DEVUELTA 						= 4;
	public static final int PENDIENTE_ABONO_BANCO 			= 5;
	public static final int PENDIENTE_ABONO_CAJA			= 6;
	public static final int EN_REVISION						= 7;
	public static final int ANULADA						= 8;
	/**
	 * Constantes IDIOMAS
	 */
	private static final String CASTELLANO 		= "0";
	private static final String EUSKERA 		= "1";
	private static final String CATALAN 		= "2";
	private static final String GALLEGO 		= "3";
	/**
	 * Constantes TEXTOS RECURSOS IDIOMAS
	 */
	public static final String TX_PAGADO 				 	= "general.literal.pagado";
	public static final String TX_PENDIENTE_COBRO 			= "general.literal.pendientecobro";
	public static final String TX_RENEGOCIADA	 		 	= "general.literal.renegociada";
	public static final String TX_DEVUELTA 			 	 	= "general.literal.devuelta";
	public static final String TX_PENDIENTE_ABONO_BANCO 	= "general.literal.pendienteabonobanco";
	public static final String TX_PENDIENTE_ABONO_CAJA  	= "general.literal.pendienteabonocaja";
	public static final String TX_EN_REVISION  				= "general.literal.enRevision";
	public static final String TX_ANULADA  				= "general.factura.anulada";
	/**
	 * Constructor de la clase. 
	 */
	private Integer user 	= null;
	private String  idioma 	= null;
	private UsrBean usrbean = null;
	
	public ConsPLFacturacion () {
	}

	public ConsPLFacturacion (UsrBean us) {
		user 	= new Integer(us.getUserName());
		idioma 	= us.getLanguage();
		usrbean = us;
	}


	public String obtenerEstadoFacAbo(int idinstitucion, long id, String cons) throws ClsExceptions{
		int estado		 		= 0;
		String retorno		 	= null;
		String consulta			= "";
		Hashtable htRecurso = new Hashtable();
		//String where = " WHERE UPPER(IDLENGUAJE) = '"+idioma.toUpperCase()+"'";
		htRecurso.put("IDLENGUAJE",idioma);
		GenRecursosAdm genRecursosAdm = new GenRecursosAdm(this.usrbean);
		try 
		{
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),new Integer(idinstitucion).toString());
		    codigos.put(new Integer(2),new Long(id).toString());

			if(cons.equals(FACTURA))
				consulta = "SELECT  ESTADO FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2";
			else
				consulta = "SELECT  ESTADO FROM FAC_ABONO WHERE IDINSTITUCION=:1 AND IDABONO=:2";

			Vector resultado = genRecursosAdm.selectTablaBind(consulta,codigos);
			
			if(resultado != null && resultado.size()==1)
			{
				String a = (String) ((Hashtable) resultado.get(0)).get("ESTADO");
				if (!a.trim().equals("")) {
					estado = Integer.parseInt(String.valueOf(a));
					if(estado==EN_REVISION) { 
					    //where = where +" AND IDRECURSO ='"+TX_EN_REVISION+"'";
						htRecurso.put("IDRECURSO",TX_EN_REVISION);
					} else if(estado==PAGADA)  {
					    //where = where +" AND IDRECURSO ='"+TX_PAGADO+"'";
						htRecurso.put("IDRECURSO",TX_PAGADO);
					} else if(estado==PENDIENTE_COBRO) { 
					    //where = where +" AND IDRECURSO ='"+TX_PENDIENTE_COBRO+"'";
						htRecurso.put("IDRECURSO",TX_PENDIENTE_COBRO);
					} else if(estado==RENEGOCIADA) { 
					    //where = where +" AND IDRECURSO ='"+TX_RENEGOCIADA+"'";
						htRecurso.put("IDRECURSO",TX_RENEGOCIADA);
					} else if(estado==DEVUELTA)  {
					    //where = where +" AND IDRECURSO ='"+TX_DEVUELTA+"'";
						htRecurso.put("IDRECURSO",TX_DEVUELTA);
					} else if(estado==PENDIENTE_ABONO_BANCO) { 
					    //where = where +" AND IDRECURSO ='"+TX_PENDIENTE_ABONO_BANCO+"'";
						htRecurso.put("IDRECURSO",TX_PENDIENTE_ABONO_BANCO);
					} else if(estado==PENDIENTE_ABONO_CAJA) {
						//where = where +" AND IDRECURSO ='"+TX_PENDIENTE_ABONO_CAJA+"'";
						htRecurso.put("IDRECURSO",TX_PENDIENTE_ABONO_CAJA);
					}else if(estado==ANULADA) {
						//where = where +" AND IDRECURSO ='"+TX_PENDIENTE_ABONO_CAJA+"'";
						htRecurso.put("IDRECURSO",TX_ANULADA);
					}
				}
				else {
					return "";
				}
			}
			// Hacemos la consulta a gen_recursos.
			resultado = new Vector();
			resultado = genRecursosAdm.select(htRecurso);
			if(resultado != null && resultado.size()>0)
				retorno = ((GenRecursosBean)resultado.get(0)).getDescripcion();
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ConsPLFacturacion.obtenerEstadoFacAbo. Consulta SQL:"+consulta);
		}
		return retorno;
	}

	


	/** 
	 * Actualiza estado abono
	 * 
	 * @param idinstitucion (int) IDINSTITUCION
	 * @param idabono (long) IDABONO
	 * @param usuario (int) USUARIO
	 * @throws ClsExceptions
	 */	
	public void actualizarEstadoAbono(Integer idinstitucion, Long idabono, Integer usuario) throws ClsExceptions{
    	String resultado[] = new String[2];
		try 
		{
        	Object[] param_in = new Object[3];
        	param_in[0] = String.valueOf(idinstitucion);
        	param_in[1] = String.valueOf(idabono);
        	param_in[2] = String.valueOf(usuario);
        	resultado = ClsMngBBDD.callPLProcedure("{call PROC_SIGA_ACTESTADOABONO(?,?,?,?,?)}", 2, param_in);
        	String codretorno = resultado[0];
        	if (!codretorno.equals("0"))
				throw new ClsExceptions("Excepcion en ConsPLFacturacion.actualizarEstadoAbono.  Proc:PROC_SIGA_ACTESTADOABONO "+resultado[1]);
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en ConsPLFacturacion.actualizarEstadoAbono.  Proc:PROC_SIGA_ACTESTADOABONO "+resultado[1]);
		}
	}
	
	public String getFuncionEjecutada (Hashtable codigos, 
			String nombreFuncion) throws ClsExceptions  
	{
		String salida = "";
	    boolean salidaVacia = false;
		try {
			String sql = "select "+nombreFuncion+"(";
			for (int i=1; i<10; i++) {
				if (codigos.containsKey(new Integer(i))) {
				    if (codigos.get(new Integer(i))==null || ((String)codigos.get(new Integer(i))).trim().equals("")) {
				        salidaVacia = true;
				    }
					sql += ":"+new Integer(i).toString()+",";
				} else {
					break;
				}
			}
				
			//QUITAMOS LA ULTIMA , 
			sql = sql.substring(0,sql.length()-1);
			sql += ") AS SALIDA FROM DUAL"; 
			//System.out.println("sql:"+sql);
			
			
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			if (rc.findBind(sql,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					salida = (String) registro.get("SALIDA");
				}
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, e.getMessage());
		}
		return salida;
	}
	
	
}