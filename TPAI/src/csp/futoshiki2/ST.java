package csp.futoshiki2;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ST<Key extends Comparable<Key>, Val> implements Iterable<Key> {
    private TreeMap<Key, Val> st;

    public ST() {
        st = new TreeMap<Key, Val>();      
    }
    
    public ST(TreeMap<Key, Val> map) {
        st = new TreeMap<Key, Val>(map);
    }

    public void put(Key key, Val val) {
        if (val == null) st.remove(key);
        else            
        	st.put(key, val);
    }
    
    public Val get(Key key)             { return st.get(key);            }
    public Val remove(Key key)          { return st.remove(key);         }
    public boolean contains(Key key)    { return st.containsKey(key);    }
    public int size()                   { return st.size();              }
    public Iterator<Key> iterator()     { return st.keySet().iterator(); }
    public String toString() {
           Set set = st.entrySet();
           String s = "";
           Iterator it = set.iterator();
           while(it.hasNext()) {
               Map.Entry entree = (Map.Entry)it.next();
              s += entree.getKey() + " : "+entree.getValue()+"\n";
           }
           return s;
       }
}