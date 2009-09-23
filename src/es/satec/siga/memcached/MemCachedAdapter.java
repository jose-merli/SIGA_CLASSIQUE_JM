package es.satec.siga.memcached;
import java.util.ArrayList;
import java.util.List;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.siga.tlds.ParejaNombreID;

public class MemCachedAdapter {

	public MemCachedAdapter(String dir, int port){
		String[] servers ={dir+":"+port};
		Integer[] weights = {new Integer(3)};

		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers(servers);
		pool.setWeights(weights);
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaxIdle(1000*60*60*6);
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(3000);
		pool.setHashingAlg( SockIOPool.NEW_COMPAT_HASH );
		pool.initialize();
	}

	public MemCachedClient getConnection(ClassLoader cl){
		MemCachedClient mcc = new MemCachedClient(cl);

		mcc.setCompressEnable(true);
		mcc.setCompressThreshold(64*1024);
		mcc.setPrimitiveAsString(false);

		return mcc;
	}
	
	public static void main (String args[]) throws Exception {
		MemCachedAdapter tmc = new MemCachedAdapter("192.168.66.128", 11211);
		MemCachedClient conn=tmc.getConnection(tmc.getClass().getClassLoader());

		ParejaNombreID pni;
		List lista=new ArrayList();
		for (int i=0;i<10;i++){
			pni= new ParejaNombreID();
			pni.setIdNombre(new Integer(i));
			pni.setNombre(""+i);
			lista.add(pni);
		}
		conn.set("pni", lista);

		System.out.println(conn.keyExists("pni"));
		ParejaNombreID pni2;
		List lista2=(List)conn.get("pni");
		for (int i=0;i<lista2.size(); i++){
			pni2=(ParejaNombreID)lista2.get(i);
			System.out.println(pni2.getIdNombre()+"="+pni2.getNombre());
		}
	}
}
