package csp.futoshiki2;

/*************************************************************************
 *  Compilation:  javac Graph.java
 *  Dependencies: ST.java SET.java In.java
 *  
 *  Undirected graph data type implemented using a symbol table
 *  whose keys are vertices (String) and whose values are sets
 *  of neighbors (SET of Strings).
 *
 *  Remarks
 *  -------
 *   - Parallel edges are not allowed
 *   - Self-loop are allowed
 *   - Adjacency lists store many different copies of the same
 *     String. You can use less memory by interning the strings.
 *
 *************************************************************************/

public class Graph {

    // symbol table: key = string vertex, value = set of neighboring vertices
    private ST<String, SET<CNT>> st;

    // number of edges
    private int E;

    // create an empty graph
    public Graph() {
        st = new ST<String, SET<CNT>>();
    }


    // return number of vertices and edges
    public int V() { return st.size(); }
    public int E() { return E;         }

    // return the degree of vertex v 
    public int degree(String v) {
        if (!st.contains(v)) return 0;
        else return st.get(v).size();
    }

    // add w to v's set of neighbors, and add v to w's set of neighbors
    public void addEdge(String v, String w, String c) {
        if (!hasEdge(v, w,c)) E++;
        if (!hasVertex(v)) addVertex(v);
        if (!hasVertex(w)) addVertex(w);
        st.get(v).add(new CNT(w,c));
         if("<".equals(c))
        	// Si Xij< Xi(j+1) alors inserer en même temps Xi(j+1)>Xij
             st.get(w).add(new CNT(v,">"));
         else // Si Xij> Xi(j+1) alors inserer en même temps Xi(j+1)<Xij
        	 st.get(w).add(new CNT(v,"<"));
         
    }

    // add a new vertex v with no neighbors (if vertex does not yet exist)
    public void addVertex(String v) {
        if (!hasVertex(v)) st.put(v, new SET<CNT>());
    }

    // return iterator over all vertices in graph
    public Iterable<String> vertices() {
        return st;
    }

    // return an iterator over the neighbors of vertex v
    public Iterable<CNT> adjacentTo(String v) {
        // return empty set if vertex isn't in graph
        if (!hasVertex(v)) return null;
        else               return st.get(v);
    }

    // is v a vertex in the graph?
    public boolean hasVertex(String v) {
        return st.contains(v);
    }

    // is v-w an edge in the graph?
    public boolean hasEdge(String v, String w, String c) {
        if (!hasVertex(v)) return false;
        return st.get(v).contains(new CNT(w,c));
    }

    // not very efficient, intended for debugging only
    public String toString() {
        String s = "";
        for (String v : st) {
            s += v + ": ";
            for (CNT w : st.get(v)) {
                s += w.getVar()+"("+w.getCh()+")"+" ";
            }
            s += "\n";
        }
        return s;
    }
/*
    public static void main(String[] args) {
        Graph G = new Graph();
        G.addEdge("A", "B","i");
        G.addEdge("A", "D","i");
       // G.addEdge("B", "A","s");
        G.addVertex("C");
        //G.addEdge("E", "","");
        G.addEdge("D", "E","i");
        G.addEdge("E", "F","s");
          
        // print out graph
        System.out.println(G);

        // print out graph again by iterating over vertices and edges
        for (String v : G.vertices()) {
        	System.out.print(v + ": ");
            for (CNT w : G.adjacentTo(v)) {
            	System.out.print(w.getVar() + " ");
            }
            System.out.println();
        }

    }*/

}

