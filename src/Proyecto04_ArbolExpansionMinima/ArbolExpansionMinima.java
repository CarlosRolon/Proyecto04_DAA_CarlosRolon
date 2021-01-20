/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyecto04_ArbolExpansionMinima;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Carlos Rolon
 */
public class ArbolExpansionMinima {
    
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
        int nodoActual;
        int conjuntoActual;
        
        /*for (Map.Entry n : grafo.entrySet()) {
            nodoActual = (Integer) n.getKey();
            HashMap<Integer , Integer> ini  = new HashMap<>();
            arbol.put(nodoActual ,ini);
        }*/

        Queue<Arista> aristas = ObtenerAristasDesc(grafo);

        /*
        for (Arista elemento : aristas)
        {
            System.out.println(elemento.peso + "-");
        }
        */

        
        while(!aristas.isEmpty())
        {
            Arista aristaActual = aristas.peek();
            aristas.poll();
            //System.out.println(aristaActual.peso + "-");
            if (arbol.containsKey(aristaActual.nodoOrigen)) {
                HashMap<Integer, Integer> nodoOrigen  = arbol.get(aristaActual.nodoOrigen);
                if (nodoOrigen.size() > 1) {
                    if (nodoOrigen.containsKey(aristaActual.nodoDestino)) {
                        nodoOrigen.remove(aristaActual.nodoDestino);
                        //System.out.println(aristaActual.nodoOrigen + "-" + aristaActual.nodoDestino );
                    }
                    
                }
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
