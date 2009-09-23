
package com.siga.consultas.action;

import java.util.Vector;


public class Permutacion
{
   public static Vector generarPermutaciones (String [] cadena)
   {
       Vector v = new Vector ();
       for (int i = 0; i < cadena.length; i++) {
           v.add (cadena[i]);
       }
       Vector vv = generarPermutaciones(v);
       
       Vector salida = new Vector();
       for (int i = 0; i < vv.size(); i++) {
           Vector v1 = (Vector)vv.get(i);
           String []s = new String[v1.size()];
           for (int j = 0; j < v1.size(); j++) {
               s[j] = (String)v1.get(j);
           }
           salida.add(s);
       }
       return salida;
   }

   public static Vector generarPermutaciones(Vector cadena)
   {
       Vector salida = new Vector();
       
       if (cadena.size() == 1)  {
           salida.add(cadena);
           return salida;
       }
       
       Vector[] permutaciones = permutacion(cadena);
       
       for (int i = 0; i < permutaciones.length; i++) {
           
           Vector v = permutaciones[i];
           Object o = v.get(0);
           v.remove(0);
           
           Vector vP = generarPermutaciones (v);
           for (int j = 0; j < vP.size(); j++) {
               Vector aa = (Vector)vP.get(j);
               aa.add(0, o);
               salida.add(aa);
           }
       }
       return salida;       
   }

   private static Vector[] permutacion(Vector cadena)
   {
     int n = cadena.size();
     Vector temporal =new Vector();
     Vector[] vector = new Vector[n];
     vector[0]=cadena;
     for(int i=1;i<n;i++)
     {
        for(int j=0;j<n;j++)
        {
          if(j==n-1)
            temporal.add(0, cadena.get(j));

          else temporal.add(cadena.get(j));
        }
        cadena=(Vector)temporal.clone();
        vector[i]=(Vector)temporal.clone();
        temporal.clear();
     }
     return vector;
   }

   public static void mostrar (Vector vector)
   {
      for(int i= 0; i< vector.size();i++)
      {
         //System.out.println("" + i + ":" + vector.get(i));
      }
   }
   
// public static void main ( String args [])
// {
//     Vector p = new Vector();
//     p.add("CEN_PERSONA");
//     p.add("CEN_CLIENTE");
//     p.add("EL_CRACK");
//     p.add("ANA_VE_LA_CABRA");
//     
//     Vector ne = permutar(p);
//     mostrar( ne );
// }

}
