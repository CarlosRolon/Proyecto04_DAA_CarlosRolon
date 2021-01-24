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

       // Obtiene las aristas del grafo y las ordena de manera ascendiente
        Queue<Arista> aristas = ObtenerAristasAsc(grafo);

        // Recorre cada arista
        while(!aristas.isEmpty())
        {
            // Obtiene la arista
            Arista aristaActual = aristas.peek();
            // Elimina de la pila de aristas
            aristas.poll();
            // Obtiene los conjuntos de cada nodo de la arista
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
                // Agrega la arista al nodo
                arbol.get(aristaActual.nodoOrigen).put(aristaActual.nodoDestino,aristaActual.peso);
                arbol.get(aristaActual.nodoDestino).put(aristaActual.nodoOrigen,aristaActual.peso);
            }            
        }
        
        return arbol;
    }
    
    public static  HashMap<Integer,HashMap<Integer, Integer>>  KruskalI( HashMap<Integer,HashMap<Integer, Integer>> grafo){
        HashMap<Integer,HashMap<Integer,Integer>> arbol =  copiarGrafo(grafo);        
        HashMap<Integer,HashMap<Integer,Integer>> arbolBase  ;    
        
        // Obtiene las aristas del grafo y las ordena de manera descendiente
        Queue<Arista> aristas = ObtenerAristasDesc(grafo);
      
        // Recorre cada arista
        while(!aristas.isEmpty())
        {
            // Obtiene la arista dentro de la cola
            Arista aristaActual = aristas.peek();
            // Se elimina de la cola
            aristas.poll();
            // Se realiza una copia del arbol 
            arbolBase =  copiarGrafo(arbol);  
            
            if (arbol.containsKey(aristaActual.nodoOrigen)) {
                // Obtiene las aristas del nodo donde inicia la arista
                HashMap<Integer, Integer> nodoOrigen  = arbol.get(aristaActual.nodoOrigen);
                if (nodoOrigen.containsKey(aristaActual.nodoDestino)) {
                    // Elimina la arista
                    nodoOrigen.remove(aristaActual.nodoDestino);
                    // Verifica si existe camino sin la arista que se eliminó
                    if ( !Existe_Camino( arbol , aristaActual.nodoOrigen , aristaActual.nodoDestino  )  ) {
                        // Si no existe, regresa al arbol como estaba
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
        List <Arista> aristasPorVisitar = new ArrayList<Arista>();
        Arista aristaNodo = new Arista();
        int nodoActual , pos;
        
        // Inicializa el grafo e inicializa la lista de nodos sin visitar
        for (Map.Entry n : grafo.entrySet()) {
            nodoActual = (Integer) n.getKey();
            HashMap<Integer , Integer> ini  = new HashMap<>();
            arbol.put(nodoActual ,ini);
            nodosSinVisitar.add(nodoActual);
        }
        // Genera un nodo de inicio de manera aleatorio
        pos = (int)(Math.random()*nodosSinVisitar.size());
        nodoActual = nodosSinVisitar.get(pos);
        // Se elimina el nodo de nodoSinVisitar
        nodosSinVisitar.remove(pos);
        // Obtiene las aristas del nodo aleatorio donde inicia el algoritmo
        vertices = grafo.get( nodoActual );
  
        // Recorre cada arista
        for (Map.Entry n : vertices.entrySet()) {
            Arista conexion = new Arista();
            conexion.nodoOrigen = nodoActual;
            conexion.nodoDestino = (Integer) n.getKey();
            conexion.peso = (Integer) n.getValue();
            // Se agrega a la lista de aristas sin visitar
            aristasPorVisitar.add(conexion);
        }

        // Se ordena la lista de aristas de manera ascendente
        aristasPorVisitar = ordenarAristasAsc(aristasPorVisitar);

        // Recorre cada arista
        while(aristasPorVisitar.size() > 0){
            // Obtiene la cima de las aristas por visitar
            aristaNodo = aristasPorVisitar.get(0);
            // Y la elimina 
            aristasPorVisitar.remove(0);
            
            // Verifica que el nodo no se visitado
            pos  = nodosSinVisitar.indexOf(aristaNodo.nodoDestino);

            if ( pos >= 0) {
                // Agrega la conexion al arbol
                arbol.get( aristaNodo.nodoOrigen ).put( aristaNodo.nodoDestino , aristaNodo.peso );
                arbol.get( aristaNodo.nodoDestino ).put( aristaNodo.nodoOrigen , aristaNodo.peso );
                // Se elimina el nodo de nodoSinVisitar
                nodosSinVisitar.remove(pos);
                // Obtiene las aristas del nodo 
                vertices = grafo.get( aristaNodo.nodoDestino );
                
                // Recorre cada arista
                for (Map.Entry n : vertices.entrySet()) {
                    Arista conexion = new Arista();
                    conexion.nodoOrigen = aristaNodo.nodoDestino;
                    conexion.nodoDestino = (Integer) n.getKey();
                    conexion.peso = (Integer) n.getValue();
                    // Se agrega a la lista de aristas sin visitar
                    aristasPorVisitar.add(conexion);
                }
                 
                // Reordena la lista
                aristasPorVisitar = ordenarAristasAsc(aristasPorVisitar);
                
            }
        }
        
        return arbol;
    }
    
    public static Integer MST( HashMap<Integer,HashMap<Integer, Integer>>  arbol){
        int valor = 0;
        
        // Recore el grafo
        for (Map.Entry n : arbol.entrySet()) {
            // Obtiene el nodo
            HashMap<Integer , Integer> aristas  = (HashMap<Integer , Integer>) n.getValue();
            // Recorre cada conexion
            for (Map.Entry a: aristas.entrySet()) {
                // Suma los pesos de la arista
                valor += (Integer) a.getValue();
            }
        }
        // Divide el valor entre 2 ya que es un arbol  con doble conexion no dirigido
        return valor  / 2;
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
            
            // Recore el grafo
            for (Map.Entry n : grafo.entrySet()) {
                nodoOrigen = (Integer) n.getKey();
                // Obtiene el nodo
                HashMap<Integer , Integer> aristas  = (HashMap<Integer , Integer>) n.getValue();
                // Recorre cada conexion
                for (Map.Entry a: aristas.entrySet()) {
                    nodoDestino = (Integer) a.getKey();
                    peso = (Integer) a.getValue();
                    // Verifica si es la arista con menor peso
                    if ( peso < arista.peso ) {
                        // Obtiene la información de la arista
                        arista.peso = peso;
                        arista.nodoDestino = nodoDestino;
                        arista.nodoOrigen = nodoOrigen;                        
                    }
                    // Incrementa el contador de aristas del grafo
                    nAristasDisponibles++;
                }
            }
            
             // Verifica si se encontró una arista
            if (nAristasDisponibles> 0)
            {
                // Remueve la arista del grafo
                grafo.get(arista.nodoOrigen).remove(arista.nodoDestino);
                // Agrega la arista a la cola
                aristasOrdenadasAsc.add(arista);
            }

        // Verifica si existe aristas en el grafo
        }while(nAristasDisponibles > 0);
        
        // Regresaba la cola de las aristas ordenadas
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
            
            // Recore el grafo
            for (Map.Entry n : grafo.entrySet()) {
                nodoOrigen = (Integer) n.getKey();
                 // Obtiene el nodo
                HashMap<Integer , Integer> aristas  = (HashMap<Integer , Integer>) n.getValue();
                // Recorre cada conexion
                for (Map.Entry a: aristas.entrySet()) {
                    nodoDestino = (Integer) a.getKey();
                    peso = (Integer) a.getValue();
                    // Verifica si es la arista con mayor peso
                    if ( peso > arista.peso ) {
                        // Obtiene la información de la arista
                        arista.peso = peso;
                        arista.nodoDestino = nodoDestino;
                        arista.nodoOrigen = nodoOrigen;                        
                    }
                    // Incrementa el contador de aristas del grafo
                    nAristasDisponibles++;
                }
            }

            // Verifica si se encontró una arista
            if (nAristasDisponibles> 0)
            {
                // Remueve la arista del grafo
                grafo.get(arista.nodoOrigen).remove(arista.nodoDestino);
                // Agrega la arista a la cola
                aristasOrdenadasAsc.add(arista);
            }

         // Verifica si existe aristas en el grafo
        }while(nAristasDisponibles > 0);
        
         // Regresaba la cola de las aristas ordenadas
        return aristasOrdenadasAsc;        
    }
    
    // Hace un recorrido BFS para verificar si existe un camino entre dos nodos
    private static Boolean  Existe_Camino ( HashMap<Integer,HashMap<Integer,Integer>> grafo, int nodoInicio , int nodoFin) {      
        
        List<Integer> visitado = new ArrayList<>();
        int nodo;
        // Agrega el nodo de inicio como nodo por viistar
        visitado.add(nodoInicio);
        
        if ( nodoFin == nodoInicio ) {
            return true;
        }
               
        for( int i = 0 ; i < visitado.size() ; i++){           
  
            // Obtiene las aristas del nodo por visitar
            HashMap<Integer,Integer> adyacentes = grafo.get(visitado.get(i));
            // Recorre cada aristas del nodo que se esta visitando
            for(Map.Entry a: adyacentes.entrySet()){   
                nodo = (int) a.getKey();
                // Verifica que si el nodo que se tiene conexion no se ha visitado
                if( !visitado.contains(nodo)){
                    // Se agrega el nodo a nodos por visitar
                    visitado.add(nodo);
                    // Verifica que el nodo de la arista es nodo destino
                    if ( nodoFin == nodo ) {
                        // Existe conexion
                        return true;
                    }
                }
            }
            
        }
        // Si se recorrió todo el grafo y no encontro el nodo destino, 
        // significa qe no existe camion y por lo tanto regresa false
        return false;
    }
    
    // Ordena las aristas de una lista
    private static List<Arista> ordenarAristasAsc(List<Arista> _aristasDesordenadas){
        List<Arista> aristasDesordenadas = new ArrayList<Arista>(_aristasDesordenadas);
        List<Arista> aristasOrdenadas = new ArrayList<Arista>();
        int nodoMenor = 0;
        
        // Recorre cada arista de la lista desordenada
        while(aristasDesordenadas.size() > 0){
            // Inicializa la busqueda con la primera arista
            Arista a = aristasDesordenadas.get(0);
            nodoMenor = 0;
            // Recorre el resto de la lista buscando una arista con menor peso
            for (int i = 1; i < aristasDesordenadas.size(); i++) {
                Arista temp = aristasDesordenadas.get(i);
                // Verifica si la arista es la de menor peso
                if (temp.peso < a.peso) {
                    // Obtiene la informacion de la arista como la menor
                    a = temp;
                    nodoMenor = i;
                }
            }
            //Remueve la arista menor de la lista
            aristasDesordenadas.remove(nodoMenor);
            // Agrega la arista a la arista ordenada
            aristasOrdenadas.add(a);
        }
        //Regresa la arista ordenada
        return aristasOrdenadas;
    }
     
    // Realiza una copia profuna del grafo
    public static HashMap<Integer,HashMap<Integer, Integer>>  copiarGrafo( HashMap<Integer,HashMap<Integer, Integer>> _grafo){
        HashMap<Integer,HashMap<Integer,Integer>> grafo = new HashMap<Integer,HashMap<Integer,Integer>>();
        int nodoOrigen ;
        
        // Recorre el grafo
        for (Map.Entry<Integer, HashMap<Integer, Integer>> n : _grafo.entrySet()) {
            nodoOrigen = n.getKey();
            // Obtiene el nodo
            HashMap<Integer , Integer> aristas  =  n.getValue();
            // Hace copia profunda de las conexionex
            grafo.put(nodoOrigen, (HashMap<Integer, Integer>) aristas.clone());
        }
        
        // Regresa la copia profunda del grafo
        return grafo;
    }

}
