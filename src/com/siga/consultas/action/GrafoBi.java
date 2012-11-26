package com.siga.consultas.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.exceptions.ConstraintViolationException;
import edu.uci.ics.jung.graph.impl.SimpleSparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.utils.UserDataContainer;

public class GrafoBi implements Grafo
{
	private UndirectedSparseGraph g = new UndirectedSparseGraph();
	private Hashtable htVertexes = new Hashtable();
    static public final String NUMERO_TABLAS = "NUMERO_TABLAS";

   
    /**
     * GrafoBi
     * Constructor
     * @throws SIGAException
     */
    public GrafoBi() throws SIGAException 
	{
		try {
		    if (!crearGrafoBD()) {
		    	ClsLogging.writeFileLog("ERROR EN CONSULTAS: Error en obtenerNodos()",1);
		    }
		}
		catch (SIGAException e) {
			throw e;
		}
	}

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
				            " FROM all_constraints C1, all_constraints C2 " +
				            " WHERE C1.constraint_type = 'R' AND " +
				                  " C1.owner = '" + dbuser + "' AND " + 
								  " C1.r_owner = C2.owner AND " + 
								  " C1.r_constraint_name = C2.constraint_name AND " +
								  " C1.table_name != C2.table_name " +
							" ORDER BY MADRE";

 			if (rc.query(select)) {

				// Primera pasada para obtener todos los nombres de las tablas con alguna relación.
				int numRegistros = rc.size();

				for (int i = 0; i < numRegistros; i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) {
						String sMadre = UtilidadesHash.getString(registro, "MADRE");
						String sHija = UtilidadesHash.getString(registro, "HIJA");

						if(!htVertexes.containsKey(sMadre)) {
						    SimpleSparseVertex v=new SimpleSparseVertex(); 
						    v.setUserDatum("tabla",sMadre,new UserDataContainer.CopyAction.Clone());
						    g.addVertex(v);
							htVertexes.put(sMadre,v);
						}
						if(!htVertexes.containsKey(sHija)) {
						    SimpleSparseVertex v=new SimpleSparseVertex();
						    v.setUserDatum("tabla",sHija,new UserDataContainer.CopyAction.Clone());
						    g.addVertex(v);
							htVertexes.put(sHija,v);
						}
						// Anhadimos el arco al grafo
		                UndirectedSparseEdge ed=null;
		                SimpleSparseVertex v1=( SimpleSparseVertex)htVertexes.get(sMadre);
		                SimpleSparseVertex v2=(SimpleSparseVertex)htVertexes.get(sHija);
		                try{
			                 ed=new UndirectedSparseEdge(v1,v2);
			                 g.addEdge(ed);
		                }
		                catch(ConstraintViolationException e){
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

    /* (non-Javadoc)
     * @see com.siga.consultas.action.Grafo#getFromWhereConsulta(java.lang.String[])
     */
    public Hashtable getFromWhereConsulta(String[] sTablasIn) throws SIGAException 
    {
		Hashtable hMinNumeroTablas = null;
        Vector vTablas = Permutacion.generarPermutaciones(sTablasIn); 
		for (int k = 0; k < vTablas.size(); k ++) {
		    String [] sTabla = (String[])vTablas.get(k);

			Hashtable h = this.getFromWhereConsultaInicial(sTabla);
			if (((String)h.get(Grafo.ESTADO)).equalsIgnoreCase(Grafo.OK_HAY_CAMINO)){
			
				if (hMinNumeroTablas == null) 
				    hMinNumeroTablas = (Hashtable)h.clone();
				
				else {
				    if (((Integer)h.get(NUMERO_TABLAS)).intValue() == sTablasIn.length) 
				        return h;
				    
				    if (((Integer)h.get(NUMERO_TABLAS)).intValue() < ((Integer)hMinNumeroTablas.get(NUMERO_TABLAS)).intValue()) {
				        hMinNumeroTablas = (Hashtable)h.clone();
				    }
				}
			}
		}

        return hMinNumeroTablas;
    }
	/* (non-Javadoc)
	 * @see com.siga.consultas.action.Grafo#getFromWhereConsulta(java.lang.String[])
	 */
//	public GrafoBi(String inicio, String fin)
	private Hashtable getFromWhereConsultaInicial(String[] sTablas) throws SIGAException {

		try {
			Hashtable h = new Hashtable();
			h.put(Grafo.ESTADO, Grafo.ERROR_NO_HAY_CAMINO);
			
			if (sTablas.length < 2) return h;
			
			Vector v = new Vector ();
			
			for (int z = 0; z < sTablas.length; z++) sTablas[z] = sTablas[z].toUpperCase();
			
			if (!this.getMiniCamino(sTablas[0], sTablas[1], v, h))
			 	return h;

			for (int i = 2; i < sTablas.length; i++) {

				for (int j = 0; j < v.size(); j++) {
					if (!v.contains(sTablas[i])) {
						if (!this.getMiniCamino((String)v.get(j), sTablas[i], v, h))
						 	return h;
						else 
							break;
					}
					else break;
				}
			}

			// Llegados aqui tengo todas las tablas
			String from = (String)v.get(0);
			for (int j = 1; j < v.size(); j++) {
				from += ", " + (String)v.get(j);  
			}
			h.put(NUMERO_TABLAS, new Integer(v.size()));
			h.put(Grafo.FROM, from);
			h.put(Grafo.ESTADO, Grafo.OK_HAY_CAMINO);
			return h;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al crear la base de datos de consultas");
		}
	}
	
	
	
	public boolean getMiniCamino (String origen, String destino, Vector vRuta, Hashtable h)
	{
		try {
			if (!htVertexes.containsKey(origen) && !htVertexes.containsKey(destino)) {
				return false;
			}
	
		    SimpleSparseVertex vOrigen = null;
		    SimpleSparseVertex vDestino = null;
				    
		    Enumeration enumer = htVertexes.elements();
		    while (enumer.hasMoreElements()){
				SimpleSparseVertex v = (SimpleSparseVertex) enumer.nextElement();
				if(((String)v.getUserDatum("tabla")).equalsIgnoreCase(origen))  vOrigen = v;
				if(((String)v.getUserDatum("tabla")).equalsIgnoreCase(destino)) vDestino = v;
				if ((vOrigen != null) && (vDestino != null))
					break;
		    }
				    
			UnweightedShortestPath sp = new UnweightedShortestPath(g);
			List list = sp.getPath(vOrigen, vDestino);
			
			if (list.size() < 1) return false;
					
			Iterator i = list.iterator();
			while(i.hasNext()){
				UndirectedSparseEdge edg=(UndirectedSparseEdge)i.next();
				Pair par = edg.getEndpoints();
				SimpleSparseVertex v1 = (SimpleSparseVertex)par.getFirst();
				SimpleSparseVertex v2 = (SimpleSparseVertex)par.getSecond();
				
				String tOrigen =  (String)v1.getUserDatum("tabla");
				String tDestino = (String)v2.getUserDatum("tabla");
				
				if (! vRuta.contains(tOrigen)){
					vRuta.add(tOrigen);
//					String from = (String)h.get(Grafo.FROM);
//					if ((from != null) && (from.length() > 1)) from += ", " + tOrigen;
//					else from = tOrigen;
//					h.put(Grafo.FROM, from);

					if (vRuta.size() > 1) {
						Arco aux = new Arco (tOrigen, tDestino, true, false);
						Vector vCO = aux.getClavesOrigen();
						Vector vCD = aux.getClavesDestino();
						String where = (String) h.get(Grafo.WHERE);
	
						if ((where != null) && (where.length() > 1)) where += " AND ";
						else where = new String("");
						for (int c = 0; c < vCD.size(); c++) {
							where += tOrigen + "." + vCO.get(c) + " = " + tDestino + "." + vCD.get(c) + " AND ";;  
						}
						where = where.substring(0, where.length()-5);
						h.put(Grafo.WHERE, where);
					}
				}
				if (! vRuta.contains(tDestino)){
					vRuta.add(tDestino);
					
					Arco aux = new Arco (tOrigen, tDestino, true, false);
					Vector vCO = aux.getClavesOrigen();
					Vector vCD = aux.getClavesDestino();
					String where = (String) h.get(Grafo.WHERE);

					if ((where != null) && (where.length() > 1)) where += " AND ";
					else where = new String("");
					for (int c = 0; c < vCD.size(); c++) {
						where += tOrigen + "." + vCO.get(c) + " = " + tDestino + "." + vCD.get(c) + " AND ";  
					}
					where = where.substring(0, where.length()-5);
					h.put(Grafo.WHERE, where);
				}
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	private boolean estanTablasEnRuta (String sTablas[], List lista) 
	{
		if (lista.size() != sTablas.length) return false;
		
		int total = 0;
		Iterator i = lista.iterator();
		while(i.hasNext()){
			UndirectedSparseEdge edg=(UndirectedSparseEdge)i.next();
			Pair par = edg.getEndpoints();
			String tabla = (String)((SimpleSparseVertex)par.getFirst()).getUserDatum("tabla");

			for (int a = 0; a < sTablas.length; a++) {
				if (sTablas[a].equalsIgnoreCase(tabla)) {
					total ++;
					break;
				}
			}
		}
		if (total == sTablas.length) 
			return true;
		
		return false;
	}
}