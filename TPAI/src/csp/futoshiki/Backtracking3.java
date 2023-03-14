package csp.futoshiki;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Backtracking3 {

	public static String getVariable(ST<String, String> config) {
	//retrieve a variable based on a heuristic or the next 'unfilled' one if there is no heuristic
        for (String s : config) {
        	if(config.get(s).equalsIgnoreCase(""))
        		return s;
        }
        //get variable failed (all variables have been coloured)
		return null;
	}
	
	public static SET<String> orderDomainValue(String variable, ST<String, SET<String>> domain) {
		//return the SET of domain values for the variable
		return domain.get(variable);
	}
	
	public static boolean complete(ST<String, String> config) {
		
		for(String s: config) {
			//if we find a variable in the config with no value, then this means that the config is NOT complete
			if(config.get(s).equalsIgnoreCase(""))
				return false;
		}
		//ALL variables in config have a value, so the configuration is complete
		return true;
	}
	
	public static boolean consistent(String value, String variable, ST<String, String> config, Graph g) {
        for(String adj: g.adjacentTo(variable)) 
        {
            if(!adj.contains("s") && !adj.contains("i")) // Si l'adjacent n'est ni supérieur ni inférieur
            {
                if(config.get(adj).equalsIgnoreCase(value))
                    return false;
           }
            else if(adj.contains("s")) // Si l'adjacent est supérieur
            {
                String noeudsuivant = adj.replace("s", "x"); // On remplace "s" par "x" pour pourvoir chercher dans le graphe (les sommets nommés "x")
                if(!"".equals(config.get(noeudsuivant)) && config.get(noeudsuivant)!=null)
                {
                    int variableNumber = Integer.parseInt(value);
                    int supNumber = Integer.parseInt(config.get(noeudsuivant));

                    if(supNumber <= variableNumber) // On compare avec la valeur à fournir
                        return false;
                }
            }
            else //if(adj.contains("i")) // Si l'adjacent est inférieur
            {
                String noeudsuivant = adj.replace("i", "x");
                if(!"".equals(config.get(noeudsuivant)) && config.get(noeudsuivant)!=null)
                {
                    int variableNumber = Integer.parseInt(value);
                    int infNumber = Integer.parseInt(config.get(noeudsuivant));

                    if(infNumber >= variableNumber)
                        return false;
                }
            }
        }
        return true;
    }
	public static boolean consistent(String value, String variable, ST<String, String> config,
										ST<String, ST<String, ST<String, SET<String>>>> constraintsTable) {
		//we need to get the constraint list for the variable
		for(String constraints: constraintsTable.get(variable)) {
			//if the adjacency list member's value is equal to the variable's selected value, then consistency fails
			if(!config.get(constraints).equals("") && !(constraintsTable.get(constraints).get(value).contains(config.get(constraints)))) {
				return false;
			}
		}
		//consistency check passed according to the variable's adjacancy list
		return true;
	}
	 static void aff(ST<String, String> config){
                System.out.print(" - ");
                if(config ==null)
                    System.out.print("Pas de solution");
                else
                     for (String s : config)
                        System.out.print("("+s + ", "+ config.get(s)+")");
        }
        public static String getVariableMVR(ST<String, SET<String>> domain , ST<String, String> config){
            
            // Stocker (variable, taille du domaine)
            SortedMap<Integer, String> compteParVariable = new TreeMap<>();
            // Table associative triée par ordre croissant
            for (String var : config)                
        	if(config.get(var).equalsIgnoreCase(""))
                    compteParVariable.put(new Integer(domain.get(var).size()),var);
            Iterator it = compteParVariable.entrySet().iterator();
            Map.Entry entree = (Map.Entry)it.next();
           
            return (String)entree.getValue();
        }
       
        public  static SET<String> forwardChecking(String u , String variable , Graph g ,ST<String, String> config ,ST<String, SET<String>> domain )
        {   // Variables touchées
            SET<String> vars = new SET<>();
            for(String adj: g.adjacentTo(variable)){			
		    if(config.get(adj).equalsIgnoreCase("") && domain.get(adj).contains(u)){
                        domain.get(adj).remove(u);
                        vars.add(adj);
                    }
            }     
            return vars;
        }         
        
        public static String degres( Graph g, ST<String, String> config){
            // Stocker (variable, nombre de contraintes)
            SortedMap<Integer, String> compteParVariable = new TreeMap<>();
            
            // Table associative triée par ordre décroissant (à  cause du - )
            for (String var : config)                
        	if(config.get(var).equalsIgnoreCase(""))
                    compteParVariable.put(new Integer(-g.degree(var)),var) ;
            System.out.println(compteParVariable);
            Iterator it = compteParVariable.entrySet().iterator();
            Map.Entry entree = (Map.Entry)it.next();
            return (String)entree.getValue();
        }
        
        public static String getVariableDegresMRV(Graph g, ST<String, SET<String>> domain , ST<String, String> config){
            // Stocker (variable, nombre de contraintes)
            SortedMap<Integer, String> compteParVariable1 = new TreeMap<>();
            // Stocker (variable, nombre de valeurs)
            SortedMap<Integer, String> compteParVariable2 = new TreeMap<>();
            
            // Table associative triée par ordre décroissant (à  cause du - )
            for (String var : config)                
                if(config.get(var).equalsIgnoreCase(""))
                    compteParVariable1.put(-g.degree(var),var) ;
            
            Iterator it = compteParVariable1.entrySet().iterator();
            Map.Entry entree0 = (Map.Entry)it.next();
            String var0 = (String)entree0.getValue();
            Integer compte0 = domain.get(var0).size();
            compteParVariable2.put(compte0,var0);
            
            // Garder les variables avec le nombre de degrés 
            while(it.hasNext()){
                Map.Entry entree = (Map.Entry)it.next();
                if(((Integer)entree.getKey()).equals(compte0)){
                    String var = (String)entree.getValue();
                    compteParVariable2.put(domain.get(var).size(),var);
                }
                else break;
            }
            
            it = compteParVariable2.entrySet().iterator();
            Map.Entry entree = (Map.Entry)it.next();
           
            return (String)entree.getValue();            
        }
        
        public static SET<String> orderDomainValueLCV(String variable,Graph g, ST<String, SET<String>> domain) {
            // Stocker (variable, nombre de contraintes)
           SortedMap<Integer, String> compteParValeur = new TreeMap<>();
           //return the SET of domain values for the variable
           SET<String> vu = domain.get(variable);
           for(String v:vu){
                int n=0; 
                for(String adj: g.adjacentTo(variable))			
                  if(domain.get(adj).contains(v))
                      n++;
                compteParValeur.put(n, v);                        
           }
           SET<String> valeurs = new SET<>();
           Iterator it = compteParValeur.entrySet().iterator();
           
           while(it.hasNext()){
                Map.Entry entree = (Map.Entry)it.next();
                valeurs.add((String)entree.getValue());
           }
              
           return valeurs;
	}
	public static ST<String, String> backtracking(ST<String, String> config, ST<String, SET<String>> domain, Graph g){
		// Arrêter s'il s'agit d'une affectation complete
		if(complete(config))
			return config;
		                
		ST<String, String> result = null;
		// Variable à  affecter, voir aussi degres(g,config), getVariableMRV(domain, config) ou getVariableDegresMRV(domain, config)
                //String v = getVariableMVR(domain,config);
                //String v = getVariableDegresMRV(g,domain,config);
               String v = getVariable(config);
		// Domaine de cette variable (liste des valeurs)
		SET<String> vu = domain.get(v);
		// Parcourir la liste des valeurs
		for(String u: vu) {
		      	if(consistent(u, v, config, g)) { // 
                             	config.put(v, u); //
                               // aff(config);
				result = backtracking(config, domain, g);
				if(result != null)
					return result;
				config.put(v,""); // X config.remove(v)
			}
		}
		return null;
	}

	public static void main(String[] args) {
		Graph G = new Graph();
		int i,j,k;
		String grille [][]={	
				{ "", "" , "", "","" , "" , "" },
				{ "", "" , "", "3","" , "" , "" },
				{ "", "" , "", "","" , "" , "" },
				{ "", "" , "", "","" , "" , "" },
				{ "", "" , "", "","" , "" , "" },
				{ "6", "" , "", "","" , "" , "" },
				{ "", "" , "", "","5" , "" , "" }};

	
		// Contraintes au	niveau	des lignes 
		String var1, var2;
		/*for(	i=1; i<=7; i++) {		// Ligne
			for(    j=1;j<=6;j++) {	// Colonne 
				for(  k=j+1;k<=7;k++){
					 var1 ="x"+i +""+j; 
					 var2 ="x"+i +""+k;
					 G.addEdge(var1, var2);
		}}}
	
		// Contaraintes au	niveau	des colonnes 
		for(  	i=1; i<=7; i++) {		// Colonne
		for(   j=1;j<=6;j++) {	// Ligne 
			for(  k=j+1;k<=7;k++){
				 var1 ="x"+j+""+ i;
				 var2 ="x"+k+""+ i;
				 G.addEdge(var1, var2);
		}}}*/
		 for( i=1;i<=7;i++)       // Ligne 
             for( j=1;j<=7;j++)    // Colonne
                  {
                      String varx ="x"+i+""+j;
                      G.addVertex(varx);

                  }
			
		//  '1'<=> '<', '2' <=> '>', '3' <=>  '^' , '4' <=> 'v'
        String [][] cstV = {	
        		{ "", "" ,	"4", "3", "", "", "",},
        		{ "", "" , "", "","" , "" , "" },
        		{ "", "3" ,	"", "", "", "", ""},
        		{ "4" , "", "4","", "", "" , "" },
        		{ "", "",	"", "", "", "3", ""},
        		{ "", "", "4", "3", "", "3", "4" }};
        
        String [][] cstH = {	
        		{ "", "1" ,	"", "2", "", "1",},
        		{ "1", "" , "2", "",""  , "1" },
        		{ "1", "" ,	"", "", "", ""},
        		{ "" , "" , "", "", ""  , ""  },
        		{ "", "",	"2", "", "", ""},
        		{ "" , "" , "", "" , "" , ""},
        		{ "1", "1", "", "", "", "" }};
        
        for(i=1; i<=7; i++) {
        	for(j=1;j<=6; j++) {
        		if(!cstH[i-1][j-1].equals("")) { 
        			 var1 = "s" + i + "" + j;
                	 var2 = "x" +i + "" + (j+1) ;
                    
                	if(cstH[i-1][j-1] == "1") {
                    var1= "s" + i + "" + (j+1);
                    var2= "x" + i + "" + j;
                    }
                    G.addEdge(var2, var1);
                    //System.out.println("(sup_h: "+var2+","+var1+")");
                    var1 = var1.replace("s", "x");
                    var2 = var2.replace("x","i");
                    G.addEdge(var1, var2);
                    //System.out.println("(inf_h: "+var1+","+var2+")");
        			}
        	}
        }
        for(i=1; i<=6; i++) {
        	for(j=1;j<=7; j++) {
        		if(!cstV[i-1][j-1].equals("")) { 
        			 var1 ="s" + "" +i+""+j;
                	 var2 ="x" + (1+i) + "" + j;
                    if(cstV[i-1][j-1] == "3") {
                     var1 = "s" + (i+1)+ "" + j;
                     var2 ="x" +i + "" + j;
                    }
                    G.addEdge(var2, var1);
                   // System.out.println("(sup_v: "+var2+","+var1+")");
                    var1 = var1.replace("s", "x");
                    var2 = var2.replace("x","i");

                    G.addEdge(var1, var2);
                   //System.out.println("(inf: "+var1+","+var2+")");
                    }
        	}
        }
        

	// Tables des domaines
	ST<String, SET<String>> domainTable = new ST<String, SET<String>>();

	// Remplir les Domaines
	Object[][] domains = new Object[7][7];

	for(  	i=0; i<7; i++)	// Colonne 
		for(    j=0;j<7;j++)	// Ligne
			domains[i][j] = new SET<String>();

	for( i=0; i<7; i++)	// Colonne 
		for( j=0;j<7;j++){	// Ligne
			if(((String)grille[i][j]).length()!=0)
			//  Doma ine  avec	une  seule  valeur  (case  remplie  )
			((SET<String>)domains[i][j]).add(new String((String)grille[i][j])); 
			else{
			// Domaine avec {1, 2, 3, ..., 9} ( case v de) 
				for( k=1;k<=7;k++)
					((SET<String>)domains[i][j]).add(""+k);
			}
	}

	// Ajouter les domaines à la table 
	for( i=1; i<=7; i++)
		for(  j=1;j<=7;j++)
			domainTable.put("x"+i +""+j,((SET<String>)domains[i -1][j-1]));

	// Configurationinitiale
	ST<String, String> config = new ST<String, String>();

	for( i=1; i<=7; i++)	// Ligne
		for(  j=1;j<=7;j++)	// Colonne
			config.put("x"+i +""+j,"");	// Variable non affectées

	System.out.println("\nCalcul  en  cours  ...  ");
	long  temps1  =  new  Date().getTime();
	ST<String, String> result = backtracking(config,domainTable, G);
	long  temps2  =  new  Date().getTime();

	// Afficher la solution
	for( 	i=1; i<=7; i++){	// Ligne 
		System.out.println("");
	for(   j=1;j<=7;j++)	// Colonne
	System.out.print(config.get("x"+ i +""+j)+"  ");
	}
	System.out.println("\n \n Temps  écoulé    :  "+(temps2-temps1)/1000.+"(s)");
	
	}
}