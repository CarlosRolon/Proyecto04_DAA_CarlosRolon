/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyecto04_ArbolExpansionMinima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Carlos Rolon
 */
public class ArbolExpansionMinima {
    
    // Kruskal Directo
    
    public static  HashMap<Integer,HashMap<Integer, Integer>>  KruskalD( HashMap<Integer,HashMap<Integer, Integer>> grafo){
        HashMap<Integer , Integer> conjuntosArbol  = new HashMap<>();
        HashMap<Integer,HashMap<Integer,Integer>> arbol =new HashMap<>();
        int nodoActual , conjuntoActual , conjuntoOrigen, conjuntoDestino;
        
        // Inicializa el grafo e inicializa los conjuntos de cada nodo
        for (Map.Entry n : grafo.entrySet()) {
            nodoActual = (Integer) n.getKey();
            conjuntosArbol.put(nodoActual,nodoActual);
            HashMap<Integer , Integer> ini  = new HashMap<>();
            arbol.put(nodoActual ,ini);
        }

        //  obtiene las aristas ordenadas
        Queue<Arista> aristas = ObtenerAristasAsc(grafo);

        // Recorre cada arista
        while(!aristas.isEmpty())
        {
            Arista aristaActual = aristas.peek();
            aristas.poll();
            conjuntoOrigen = conjuntosArbol.get(aristaActual.nodoOrigen);
            conjuntoDestino = conjuntosArbol.get(aristaActual.nodoDestino);
            
            // Verifica si ambos aristas estan en distintos conjuntos
            if (conjuntoOrigen != conjuntoDestino) {
               // Unifica
                for (Map.Entry n : conjuntosArbol.entrySet()) {
                    nodoActual = (Integer) n.getKey();
                    conjuntoActual = (Integer) n.getValue();
                    if (conjuntoActual == conjuntoDestino) {                        
                        conjuntosArbol.put(nodoActual,conjuntoOrigen);
                    }
                }
                arbol.get(aristaActual.nodoOrigen).put(aristaActual.nodoDestino,aristaActual.peso);
                arbol.get(aristaActual.nodoDestino).put(aristaActual.nodoOrigen,aristaActual.peso);
            }            
        }
        
        return arbol;
    }
    
    public static  HashMap<Integer,HashMap<Integer, Integer>>  KruskalI( HashMap<Integer,HashMap<Integer, Integer>> grafo){
        HashMap<Integer,HashMap<Integer,Integer>> arbol =  copiarGrafo(grafo);        
        HashMap<Integer,HashMap<Integer,Integer>> arbolBase  ;      
        Queue<Arista> aristas = ObtenerAristasDesc(grafo);
      
        while(!aristas.isEmpty())
        {
            Arista aristaActual = aristas.peek();
            aristas.poll();
            arbolBase =  copiarGrafo(arbol);  
            //System.out.println(aristaActual.peso + "-");
            if (arbol.containsKey(aristaActual.nodoOrigen)) {
                
                HashMap<Integer, Integer> nodoOrigen  = arbol.get(aristaActual.nodoOrigen);
               
                if (nodoOrigen.containsKey(aristaActual.nodoDestino)) {
                    
                    nodoOrigen.remove(aristaActual.nodoDestino);
                    if ( !BFS_Metodo( arbol , aristaActual.nodoOrigen , aristaActual.nodoDestino  )  ) {
                        arbol = arbolBase;
                    }
                }
            } 
           
        }
        
        return arbol;
    }
    
    public static  HashMap<Integer,HashMap<Integer, Integer>>  Prim( HashMap<Integer,HashMap<Integer, Integer>> grafo){
        HashMap<Integer,HashMap<Integer, Integer>> arbol  = new HashMap<>();
        HashMap<Integer, Integer> vertices  = new HashMap<>();
        List <Integer> nodosSinVisitar = new ArrayList<Integer>();
        List <Arista> aristasDisponibles = new ArrayList<Arista>();
        int nodoActual , pos;
        
        // Inicializa el grafo e inicializa los conjuntos de cada nodo
        for (Map.Entry n : grafo.entrySet()) {
            nodoActual = (Integer) n.getKey();
            HashMap<Integer , Integer> ini  = new HashMap<>();
            arbol.put(nodoActual ,ini);
            nodosSinVisitar.add(nodoActual);
        }
        
        pos = (int)(Math.random()*nodosSinVisitar.size());
        nodoActual = nodosSinVisitar.get(pos);
        nodosSinVisitar.remove(pos);
        vertices = grafo.get( nodoActual );
  
        for (Map.Entry n : vertices.entrySet()) {
            Arista conexion = new Arista();
            conexion.nodoOrigen = nodoActual;
            conexion.nodoDestino = (Integer) n.getKey();
            conexion.peso = (Integer) n.getValue();
            aristasDisponibles.add(conexion);
        }

        List <Arista> aristasPorVisitar = ordenarAristasAsc(aristasDisponibles);
        Arista nodo = new Arista();

        
        while(aristasPorVisitar.size() > 0){
            nodo = aristasPorVisitar.get(0);
            aristasPorVisitar.remove(0);
            
            pos  = nodosSinVisitar.indexOf(nodo.nodoDestino);

            if ( pos >= 0) {
                
                arbol.get( nodo.nodoOrigen ).put( nodo.nodoDestino , nodo.peso );
                arbol.get( nodo.nodoDestino ).put( nodo.nodoOrigen , nodo.peso );
                nodosSinVisitar.remove(pos);
                vertices = grafo.get( nodo.nodoDestino );
                
                for (Map.Entry n : vertices.entrySet()) {
                    Arista conexion = new Arista();
                    conexion.nodoOrigen = nodo.nodoDestino;
                    conexion.nodoDestino = (Integer) n.getKey();
                    conexion.peso = (Integer) n.getValue();
                    aristasDisponibles.add(conexion);
                }
                 
                aristasPorVisitar = ordenarAristasAsc(aristasDisponibles);
                
                
            }
        }
        
        return arbol;
    }
    
    
    private static Queue<Arista> ObtenerAristasAsc(HashMap<Integer,HashMap<Integer,Integer>> _grafo) 
    {
        HashMap<Integer,HashMap<Integer,Integer>> grafo = copiarGrafo(_grafo);
        Queue<Arista> aristasOrdenadasAsc = new LinkedList<Arista>();
        int nodoOrigen = 0 , nodoDestino = 0 , peso ;
        int nAristasDisponibles = 0;
                
        do{ 
            Arista arista = new Arista() ;
            nAristasDisponibles = 0;
            arista.peso = (int)Float.POSITIVE_INFINITY;
            
            for (Map.Entry n : grafo.entrySet()) {
                nodoOrigen = (Integer) n.getKey();
                HashMap<Integer , Integer> aristas  = (HashMap<Integer , Integer>) n.getValue();
                for (Map.Entry a: aristas.entrySet()) {
                    nodoDestino = (Integer) a.getKey();
                    peso = (Integer) a.getValue();
                    if ( peso < arista.peso ) {
                        arista.peso = peso;
                        arista.nodoDestino = nodoDestino;
                        arista.nodoOrigen = nodoOrigen;                        
                    }
                    nAristasDisponibles++;
                }
            }

            if (nAristasDisponibles> 0)
            {
                grafo.get(arista.nodoOrigen).remove(arista.nodoDestino);
                aristasOrdenadasAsc.add(arista);
            }

        }while(nAristasDisponibles > 0);
        
        return aristasOrdenadasAsc;        
    }
    
    private static Queue<Arista> ObtenerAristasDesc(HashMap<Integer,HashMap<Integer,Integer>> _grafo)
    {
        HashMap<Integer,HashMap<Integer,Integer>> grafo = copiarGrafo(_grafo);
        Queue<Arista> aristasOrdenadasAsc = new LinkedList<Arista>();
        int nodoOrigen = 0 , nodoDestino = 0 , peso ;
        int nAristasDisponibles = 0;
                
        do{ 
            Arista arista = new Arista() ;
            nAristasDisponibles = 0;
            arista.peso = (int)Float.NEGATIVE_INFINITY;
            
            for (Map.Entry n : grafo.entrySet()) {
                nodoOrigen = (Integer) n.getKey();
                HashMap<Integer , Integer> aristas  = (HashMap<Integer , Integer>) n.getValue();
                for (Map.Entry a: aristas.entrySet()) {
                    nodoDestino = (Integer) a.getKey();
                    peso = (Integer) a.getValue();
                    if ( peso > arista.peso ) {
                        arista.peso = peso;
                        arista.nodoDestino = nodoDestino;
                        arista.nodoOrigen = nodoOrigen;                        
                    }
                    nAristasDisponibles++;
                }
            }

            if (nAristasDisponibles> 0)
            {
                grafo.get(arista.nodoOrigen).remove(arista.nodoDestino);
                aristasOrdenadasAsc.add(arista);
            }

        }while(nAristasDisponibles > 0);
        
        return aristasOrdenadasAsc;        
    }
    
    private static Boolean  BFS_Metodo ( HashMap<Integer,HashMap<Integer,Integer>> grafo, int nodoInicio , int nodoFin) {      

        List<Integer> visitado = new ArrayList<>();
        int nodo;
        visitado.add(nodoInicio);
        
         if ( nodoFin == nodoInicio ) {
                return true;
         }
               
        for( int i = 0 ; i < visitado.size() ; i++){           
            Set<Integer> conexiones = new HashSet<>();
            HashMap<Integer,Integer> adyacentes = grafo.get(visitado.get(i));

            for(Map.Entry a: adyacentes.entrySet()){   
                nodo = (int) a.getKey();
                if( !visitado.contains(nodo)){
                    visitado.add(nodo);
                    conexiones.add(nodo);
                    
                    if ( nodoFin == nodo ) {
                        return true;
                    }
                }
            }
            
        }
        return false;
    }
    
    private static List<Arista> ordenarAristasAsc(List<Arista> _aristasDesordenadas){
        List<Arista> aristasDesordenadas = new ArrayList<Arista>(_aristasDesordenadas);
        List<Arista> aristasOrdenadas = new ArrayList<Arista>();
        int nodoMenor = 0;
        
        while(aristasDesordenadas.size() > 0){
            Arista a = aristasDesordenadas.get(0);
            nodoMenor = 0;
            for (int i = 1; i < aristasDesordenadas.size(); i++) {
                Arista temp = aristasDesordenadas.get(i);
                if (temp.peso < a.peso) {
                    a = temp;
                    nodoMenor = i;
                }
            }
            aristasDesordenadas.remove(nodoMenor);
            aristasOrdenadas.add(a);
        }
        return aristasOrdenadas;
    }
     
    public static HashMap<Integer,HashMap<Integer, Integer>>  copiarGrafo( HashMap<Integer,HashMap<Integer, Integer>> _grafo){
        HashMap<Integer,HashMap<Integer,Integer>> grafo = new HashMap<Integer,HashMap<Integer,Integer>>();
        int nodoOrigen ;
        
        for (Map.Entry<Integer, HashMap<Integer, Integer>> n : _grafo.entrySet()) {
            nodoOrigen = n.getKey();
            HashMap<Integer , Integer> aristas  =  n.getValue();
            grafo.put(nodoOrigen, (HashMap<Integer, Integer>) aristas.clone());
        }
        
        return grafo;
    }


   
}
