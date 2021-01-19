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
        int nodoActual;
        int conjuntoActual;
        
        for (Map.Entry n : grafo.entrySet()) {
            nodoActual = (Integer) n.getKey();
            conjuntosArbol.put(nodoActual,nodoActual);
            HashMap<Integer , Integer> ini  = new HashMap<>();
            arbol.put(nodoActual ,ini);
        }

        Queue<Arista> aristas = ObtenerAristasAsc(grafo);
        int conjuntoOrigen, conjuntoDestino;
        
        while(!aristas.isEmpty())
        {
            Arista aristaActual = aristas.peek();
            aristas.poll();
            conjuntoOrigen = conjuntosArbol.get(aristaActual.nodoOrigen);
            conjuntoDestino = conjuntosArbol.get(aristaActual.nodoDestino);

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
    
    public static Queue<Arista> ObtenerAristasAsc(HashMap<Integer,HashMap<Integer,Integer>> grafo)
    {
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
}
