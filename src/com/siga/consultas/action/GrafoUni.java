
package com.siga.consultas.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/*
 * Created on 11-abr-2005
 *
 */

/**
 * 
 * @author daniel.campos
 * Clase que representa una grafo. En nuetro caso el grafo contendra toda la estructura de B.D.
 */
public class GrafoUni implements Grafo
{
//    static public final String ERROR_NO_HAY_CAMINO = "NO SE HA LOCALIZADO UN CAMINO PARA RELACCIONAR LAS TABLAS";
//    static public final String OK_HAY_CAMINO = "OK";
//    static public final String ESTADO 	= "ESTADO";
//    static public final String FROM 	= "FROM";
//    static public final String WHERE 	= "WHERE";

    private Vector vNodos = new Vector();
	
    /**
     * GrafoUni
     * Constructor
     * @throws SIGAException
     */
    public GrafoUni() throws SIGAException 
	{
		try {
		    if (!crearGrafoBD()) {
		    	ClsLogging.writeFileLog("ERROR EN CONSULTAS: Error en obtenerNodos()",1);
		    }
//		    this.pintarGrafo(this.vNodos);
		}
		catch (SIGAException e) {
			throw e;
		}
	}
	
    /**
     * getVNodos
     * Recupera los elementos del grafo 
     * @return Vector con las tablas
     */
	private Vector getVNodos () {
		return (Vector)this.vNodos.clone(); 
	}
	
	/**
	 * CrearGrafoBD
	 * Crea una estructura en memoria de la B.D.
	 * @return true si ok, false si error
	 * @throws SIGAException
	 */
	private boolean crearGrafoBD() throws SIGAException 
	{
	    try
	    {
			// Acceso a BBDD
			RowsContainer rc = null;

			rc = new RowsContainer(); 

			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String dbuser = rp.returnProperty("db.esquema.owner");
			
			
			String select = " SELECT C1.table_name AS MADRE, C2.table_name AS HIJA " +
//			String select = " SELECT C1.table_name AS HIJA, C2.table_name AS MADRE " +
				            " FROM all_constraints C1, all_constraints C2 " +
				            " WHERE C1.constraint_type = 'R' AND " +
				                  " C1.owner = '" + dbuser + "' AND " + 
				                  " C1.r_owner = C2.owner AND " + 
				                  " C1.r_constraint_name = C2.constraint_name AND " +
				                  " C1.table_name != C2.table_name " +
				            " ORDER BY MADRE ";

 			if (rc.query(select)) {
 				
				Hashtable htAux = new Hashtable();

				// Primera pasada para obtener todos los nombres de las tablas con alguna relación.
				int numRegistros = rc.size();
				for (int i = 0; i < numRegistros; i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) {
						String sMadre = UtilidadesHash.getString(registro, "MADRE");
						String sHija = UtilidadesHash.getString(registro, "HIJA");
						UtilidadesHash.set(htAux, sMadre, sMadre);
						UtilidadesHash.set(htAux, sHija, sHija);
					}
				}
				
				Enumeration enumAux = htAux.keys();
				while (enumAux.hasMoreElements())
				{
				    String sAux = (String)enumAux.nextElement();
				    Nodo nodo = new Nodo(sAux);
				    this.vNodos.add(nodo);
				}

				// Tamanho del Vector de Nodos.
				int iSizeVector = this.vNodos.size();
				
				// Segunda pasada para crear la jerarquía de objetos Router.
				for (int cont = 0; cont < numRegistros; cont++) {
					
					Row fila = (Row) rc.get(cont);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) {
						
						String sMadre = UtilidadesHash.getString(registro, "MADRE");
						String sHija = UtilidadesHash.getString(registro, "HIJA");
					
						// Primero se busca la tabla madre.
						for (int i=0; i<iSizeVector; i++)
						{
						    if (((Nodo)this.vNodos.elementAt(i)).getNombre().equals(sMadre))
						    {
						        // Después se busca la tabla hija.
						        for (int j=0; j<iSizeVector; j++)
						        {
						        	if (((Nodo)this.vNodos.elementAt(j)).getNombre().equals(sHija))
						            {
						                // Se crea un enlace entre madre-hija.
						            	Arco a = new Arco (((Nodo)this.vNodos.elementAt(i)).getNombre(), 
						            					   ((Nodo)this.vNodos.elementAt(j)).getNombre(),
														   false,true);	
						            	((Nodo)this.vNodos.elementAt(i)).insertarArco(a);
						                j = this.vNodos.size();
						            }
						        }
						        i = this.vNodos.size();
						    }
						}
					}
				}
 			}
 			else 
 				return false;
	        
 			return true;
	    }
	    catch(Exception e) {
			throw new SIGAException ("Error al crear la base de datos de consultas"); 
	    }
	}

	public void pintarGrafo () throws SIGAException {
		pintarGrafo(this.vNodos);
	}
	
	/**
	 * PintarGrafo
	 * Pinta sobre la consola la representacion en memoria de la B.D.
	 * @param datos con la represntacion de B.D.
	 * @throws SIGAException
	 */
	public void pintarGrafo (Vector datos) throws SIGAException {
		try {
			ClsLogging.writeFileLog("--------------- GRAFO CONSULTAS --------------",7);
			int nNodos = datos.size();
			for (int i = 0 ; i < nNodos; i++) {
				Nodo nodo = (Nodo)datos.get(i);
				ClsLogging.writeFileLog("Nodo: " + nodo.getNombre(),7);
				ClsLogging.writeFileLog("Arcos:",7);
				
				int nArcos = nodo.getArcos().size();
				for (int j = 0; j < nArcos; j++) {
					ClsLogging.writeFileLog("  Arco ("+(j+1)+")",7);
					Arco a = (Arco) nodo.getArcos().get(j);
					String aux = "     T_Origen: " + a.getNodoOrigen();
					for (int k = 0; k < a.getClavesOrigen().size(); k++) {
						aux += " FK " + (k+1) + ": " +(a.getClavesOrigen()).get(k);
					}
					ClsLogging.writeFileLog(aux,7);
					ClsLogging.writeFileLog("",7);

					aux = "     T_Destino: " + a.getNodoDestino();
					for (int k = 0; k < a.getClavesDestino().size(); k++) {
						aux += " FK " + (k+1) + ": " +(a.getClavesDestino()).get(k);
					}
					ClsLogging.writeFileLog(aux,7);
					ClsLogging.writeFileLog("",7);
				}
				ClsLogging.writeFileLog("",7);
			}
		}
		catch (Exception e) {
			throw new SIGAException (e);
		}
	}

	/**
	 * getGrafoMinimo
	 * Obtiene la distancia minima entre dos tablas
	 * @param Nombre de la tabla de partida
	 * @return Conjunto de tablas relacionadas con la tabla de partida
	 * @throws SIGAException
	 */
	private Vector getGrafoMinimo (String sOrigen) throws SIGAException {
		try {
		    Nodo nOrigen = new Nodo(sOrigen);
			Vector vRutaMinima = new Vector (); 
			Cola cola = new Cola(this.getVNodos(), nOrigen);
			
			for (Nodo n = cola.extraerMinimoElemento(); n!= null; n = cola.extraerMinimoElemento()) {
				Vector hijos = n.getHijos();
				for (int i = 0; i < hijos.size(); i++) {
					Hashtable h = (Hashtable)hijos.get(i);
					Nodo hijo = (Nodo) h.get("NODO");
					long peso = ((Long) h.get("PESO")).longValue();
					if (!cola.actualizarNodo(hijo, n, peso))
						return null;
				}
				vRutaMinima.add(n);
			}
			return (Vector)vRutaMinima.clone();
		}
		catch (Exception e) {
			new SIGAException (e);
			return null;
		}
	}
	
	/**
	 * getNodo
	 * Obtiene los datos de una tabla
	 * @param conjunto de datos sobre el que buscar
	 * @param nombre de la tabla a buscar
	 * @return la tabla
	 * @throws SIGAException
	 */
	private Nodo getNodo (Vector nodos, String sBuscar) throws SIGAException {
		try {
			return this.getNodo(nodos, new Nodo (sBuscar));
		}
		catch (SIGAException e){
			throw e;
		}
	}

	/**
	 * getNodo
	 * Obtiene los datos de una tabla
	 * @param Conjunto de datos sobre el que buscar
	 * @param Tabla a buscar
	 * @return La tabla
	 * @throws SIGAException
	 */
	private Nodo getNodo (Vector nodos, Nodo nBuscar) throws SIGAException {
		try {
			for (int i = 0; i < nodos.size(); i++) {
				Nodo n = (Nodo)nodos.get(i);
				if (n.getNombre().equalsIgnoreCase(nBuscar.getNombre()))
					return (Nodo)n;
			}
			return null;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al recuperar datos de la tabla");
		}
	}
	
/*
	public Hashtable resuelveGrafo (String sTablas[]) throws SIGAException {
		try {
		
			Hashtable h = this.getFromWhereConsulta(sTablas);
			if (((String)h.get(Grafo.ESTADO)).equalsIgnoreCase(Grafo.OK_HAY_CAMINO)){
				return h;
			}

			Hashtable hError = new Hashtable();
			hError.put(Grafo.ESTADO, Grafo.ERROR_NO_HAY_CAMINO);

			if (sTablas.length <= 2) {
				return hError;
			}
			
			// Copiamos los elementos menos el ultimo
			String aux[] = new String [sTablas.length-1];
			for (int i = 0; i < sTablas.length - 1; i++) {
				aux[i] = sTablas[i];
			}
			h = resuelveGrafo(aux);
			if (!((String)h.get(Grafo.ESTADO)).equalsIgnoreCase(Grafo.OK_HAY_CAMINO)){
				return h;
			}
			
			Hashtable hAux = null;
			for (int i = 0; i < aux.length; i++) {
				
				String pareja[] = {aux[i], sTablas[sTablas.length-1]};
				hAux = this.getFromWhereConsulta(sTablas);
				if (((String)hAux.get(Grafo.ESTADO)).equalsIgnoreCase(Grafo.OK_HAY_CAMINO)){

					// Introducimos la tabla nueva
					h.put(Grafo.FROM, (h.get(Grafo.FROM) + ", " + pareja[1]));
					
					// Introducimos las nuevas condiciones tabla nueva
					h.put(Grafo.WHERE, (h.get(Grafo.WHERE) + " AND " + hAux.get(Grafo.WHERE)));
					return h;
				}
			}

			return hError;
		}
		catch (Exception e) {
			throw new SIGAException(e);
		}
	}
*/	
	/**
	 * getFromWhereConsulta
	 * Obtiene el From y el Where necesarios que relaciona el conjunto de tablas que recibe
	 * @param Conjuento de tablas a obtener como consulta
	 * @return Hashtable con los datos ESTADO, FROM y WHERE
	 * @throws SIGAException
	 */
	public Hashtable getFromWhereConsulta (String sTablas[]) throws SIGAException {
		try {
			int i = 0;
			Vector vRutaMinima = null;
			for (i = 0; i < sTablas.length; i++) {
				vRutaMinima = this.getGrafoMinimo(sTablas[i]);
				if (vRutaMinima.size() > 0) {
					int j = 0;
					for (j = 0; j < sTablas.length; j++) {
						Nodo n = this.getNodo(vRutaMinima, sTablas[j]);
						if (n == null) {
							break;
						}
					}
					if (j == sTablas.length) {
						break;
					}
				}
			}
			
			Hashtable h = new Hashtable();
			h.put(Grafo.ESTADO, Grafo.ERROR_NO_HAY_CAMINO);
			if (i == sTablas.length) {
				return h;
			}
			
			// Limpiamos la ruta de los nodos que no nos interesan
			vRutaMinima = this.limpiarRuta (vRutaMinima, sTablas);
			if (vRutaMinima == null) {
				return h;
			}
			
			// Llegados aqui vRutaMinima tiene la ruta que se pide
			String from = "", where = "";
			
			for (i = 0; i < vRutaMinima.size(); i++) {
				Nodo n = (Nodo)vRutaMinima.get(i);
				from += n.getNombre() + ", ";

				Vector vArcos = n.getArcos();
				for (int k = 0; k < vArcos.size(); k++) {
					if (!where.equalsIgnoreCase("") && !where.endsWith(" AND ")) 
						where += " AND ";
					Arco a = (Arco)vArcos.get(k);
					String tOrigen = a.getNodoOrigen();
					String tDestino = a.getNodoDestino();

					// DCG
					// Cambiamos el orden por el cambio de Optimizacion -> no leiamos de B.D. (Arco (O,D, false))  
					// Arco aux = new Arco (a.getNodoOrigen(), a.getNodoDestino(), true);
					Arco aux = new Arco (a.getNodoDestino(), a.getNodoOrigen(), true,true);
					////////////////////
					
					Vector vCO = aux.getClavesOrigen();
					Vector vCD = aux.getClavesDestino();
					for (int c = 0; c < vCD.size(); c++) {
						where += tOrigen + "." + vCO.get(c) + " = " + tDestino + "." + vCD.get(c) + " AND ";  
					}
					where = where.substring(0, where.length()-5);
				}
			}
			from = from.substring(0, from.length()-2);
			
			h.put(Grafo.ESTADO, Grafo.OK_HAY_CAMINO);
			h.put(Grafo.FROM, from);
			h.put(Grafo.WHERE, where);
			return h;
		}
		catch (SIGAException e) {
			throw e;
		}
	}
	
	/**
	 * limpiarRuta
	 * Limpia los datos asociados a una tabla 
	 * @param datos a limpiar
	 * @param Tablas a limpiar
	 * @return devuleve los datos limpios
	 * @throws SIGAException
	 */
	private Vector limpiarRuta (Vector vRuta, String sNodos[]) throws SIGAException {
		try {
			Nodo aux = (Nodo) vRuta.get(0);
			aux.setPadre("NULL");
			vRuta.setElementAt(aux, 0);
			
			int i;
			for (i = 0; i < sNodos.length; i++) {
				Nodo n = this.getNodo(vRuta, sNodos[i]);
				this.marcarNodosHaciaArriba (vRuta, n);
			}
			
			// Quitamos las relacciones que no vamos a acceder
			for (i = 0; i < vRuta.size(); i++) {
				Nodo n = (Nodo)vRuta.get(i);
				if (!n.isVisitado()) {
					vRuta.remove(n);
					i--;
					continue;
				}
			}
			for (i = 0; i < vRuta.size(); i++) {
				Nodo n = (Nodo)vRuta.get(i);
				Vector arcos = n.getArcos();
				for (int k = 0; k < arcos.size(); k++) {
					Arco a = (Arco)arcos.get(k);
					aux = this.getNodo(vRuta, a.getNodoDestino());
					if (aux == null) {
						arcos.removeElement(a);
						k--;
					}
				}
				vRuta.setElementAt(n,i);
			}
			return vRuta;
		}
		catch (SIGAException e) {
			throw e;
		}
	}
	
	/**
	 * marcarNodosHaciaArriba
	 * Marca las tablas que preceden a la que se pasa como vistos
	 * @param vRuta
	 * @param tabla a partir de la cual se marcan el resto de tablas
	 * @return
	 * @throws SIGAException
	 */
	private boolean marcarNodosHaciaArriba (Vector vRuta, Nodo n) throws SIGAException {
		try {
			n.setVisitado(true);
			int i = vRuta.indexOf(n);
			vRuta.setElementAt(n,i);
			Nodo padre = this.getNodo(vRuta, n.getPadre());
			if (padre == null) return true;
			return this.marcarNodosHaciaArriba(vRuta, padre);
		}
		catch (SIGAException e) {
			throw e;
		}
	}
}