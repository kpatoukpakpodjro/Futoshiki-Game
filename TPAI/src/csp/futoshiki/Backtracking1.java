package csp.futoshiki;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Backtracking1 {
    private static String grille [][]={    { "", "" ,  "", "", "", "", ""},
                            { "", "" ,  "", "3", "", "", ""},
                            { "", "" ,  "", "", "", "", ""},
                            { "", "" ,  "", "", "", "", ""},
                            { "", "" ,  "", "", "", "", ""},
                            { "6", "" ,  "", "", "", "", ""},
                            { "", "" ,  "", "", "5", "", ""}
                        };
            
    private static String grilleCH [][]={  
                            { "", "<" ,  "", ">", "", "<"},
                            { "<", "" ,  ">", "", "", "<"},
                            { "<", "" ,  "", "", "", ""},
                            { "", "" ,  "", "", "", ""},
                            { "", "" ,  ">", "", "", ""},
                            { "", "" ,  "", "", "", ""},
                            { "<", "<" ,  "", "", "", ""}
                         };
             

    private static String grilleCV [][]={  
                            { "", "" ,  ">", "<", "", "", ""},
                            { "", "" ,  "", "", "", "", ""},
                            { "", "<" ,  "", "", "", "", ""},
                            { ">", "" ,  ">", "", "", "", ""},
                            { "", "" ,  "", "", "", "<", ""},
                            { "", "" ,  ">", "<", "", "<", ">"}
                         };

	public static String getVariable(ST<String, String> config) {
		
	// Retrieve a variable based on a heuristic or the next 'unfilled' one if there is no heuristic
        for (String s : config) {
        	if(config.get(s).equalsIgnoreCase(""))
        		return s;
        }
		
        // Get variable failed (all variables have been coloured)
		return null;
	}
	
	public static SET<String> orderDomainValue(String variable, ST<String, SET<String>> domain) {
		
		// Return the SET of domain values for the variable
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
		// Paramètres de la variable 
		 int val1 = Integer.parseInt(value); 
		 int i = variable.charAt(1)-48; 
		 int j = variable.charAt(2)-48; 
		 
		 // Voir les contraintes avec d'égalité sur la ligne 
		 for(int jj=1;jj<=7;jj++) 
		 if(jj!=j){ 
		 String var ="x"+i+""+jj; 
		 if(config.get(var)!=null && config.get(var).equalsIgnoreCase(value)) 
		 return false; 
		 } 
		 
		 // Voir les contraintes avec d'égalité sur la colonne 
		 for(int ii=1;ii<=7;ii++) 
		 if(ii!=i){ 
		 String var ="x"+ii+""+j; 
		 if(config.get(var)!=null && config.get(var).equalsIgnoreCase(value)) 
		 return false; 
		 } 
		 
		 
		 if(g.adjacentTo(variable) != null){ 
		 //Traitement en cas de présence de contrainte
			 for(String adj: g.adjacentTo(variable)) { 
				 if(!config.get(adj).equals("")){ // Voir si la variable est affectée 
				 
				 int val2 = Integer.parseInt(config.get(adj)); 
				 
				 if(adj.charAt(1) == variable.charAt(1)){ // Contrainte sur une ligne
				 int j1 = variable.charAt(2)-48; 
				 int j2 = adj.charAt(2)-48; 
				 
				 if(j1 < j2){ 
				 // System.out.println("i "+i+", j1 "+j1); 
				 if(((String)grilleCH[i-1][j1-1])==">") 
				 if(val1 <= val2) return false; 
				 if(((String)grilleCH[i-1][j1-1])=="<") 
				 if(val1 >= val2) return false; 
				 } 
				 else { 
				 if(((String)grilleCH[i-1][j2-1])==">") 
				 if(val1 >= val2) return false; 
				 if(((String)grilleCH[i-1][j2-1])=="<") 
				 if(val1 <= val2) return false; 
				 } 
				 } 
				 
				 if(adj.charAt(2) == variable.charAt(2)){ // Contrainte sur la colonne 
				 int i1 = variable.charAt(1)-48; 
				 int i2 = adj.charAt(1)-48; 
				 
				 if(i1 < i2){ 
				 if(((String)grilleCV[i1-1][j-1])==">") 
				 if(val1 <= val2) return false; 
				 
				 if(((String)grilleCV[i1-1][j-1])=="<") 
				 if(val1 >= val2) return false; 
				 } 
				 else{ 
				 if(((String)grilleCV[i2-1][j-1])==">") 
				 if(val1 >= val2) return false; 
				 
				 if(((String)grilleCV[i2-1][j-1])=="<") 
				 if(val1 <= val2) return false; 
				 } }}}}
				
		//consistency check passed according to the variable's adjacancy list
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
            String v = getVariableMVR(domain,config);
            //String v = getVariableDegresMRV(g,domain,config);
            //String v = getVariable(config);
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
       
            Graph G = new Graph();
            int i,j,k;
            String var1,var2;
            //  ------------------- Affichage de la grille initiale ------------------------
           /* for( i=0;i<6;i++){  // Ligne 
                System.out.println("");    
                for( j=0;j<6;j++)   {
                    if(((String)grille[i][j])!="")
                       System.out.print(grille[i][j]);
                    else 
                        System.out.print(".");
                    
                    if(((String)grilleCH[i][j])!="")
                       System.out.print(grilleCH[i][j]);
                    else 
                        System.out.print(" ");
                };
                if(((String)grille[i][6])!="")
                       System.out.print(grille[i][6]);
                    else 
                        System.out.print(".");
                System.out.println(""); 
                for( j=0;j<7;j++)   {
                    if(((String)grilleCV[i][j])!="")
                       System.out.print(grilleCV[i][j]+" ");
                    else 
                        System.out.print("  ");
                };
            }
            
            System.out.println("");    
            for( j=0;j<6;j++)   {
                 if(((String)grille[6][j])!="")
                    System.out.print(grille[6][j]);
                 else 
                     System.out.print(".");

                 if(((String)grilleCH[6][j])!="")
                    System.out.print(grilleCH[6][j]);
                 else 
                     System.out.print(" ");
            };
                
            if(((String)grille[6][6])!="")
                   System.out.print(grille[6][6]);
                else 
                    System.out.print(".");*/
           
            //  ------------------- Affichage de la grille initiale ------------------------

            // Ajouter les variables au graphe
            for( i=1;i<=7;i++)       // Ligne 
               for( j=1;j<=7;j++)    // Colonne
                    {
                        var1 ="x"+i+""+j;
                        G.addVertex(var1);

                    }
            
         // Ajouter les contraintes horizontales 
            for(  i=1;i<=7;i++) // Ligne 
            for(  j=1;j<=6;j++) // Colonne 
            if(grilleCH[i-1][j-1] !=""){ 
              var1 ="x"+i+""+j; 
              var2 ="x"+i+""+(j+1); 
            G.addEdge(var1, var2); 
            } 
            // Ajouter les contraintes verticales 
            for(  i=1;i<=6;i++) // Ligne 
            for(  j=1;j<=7;j++) // Colonne 
            if(grilleCV[i-1][j-1] !=""){ 
              var1 ="x"+i+""+j; 
              var2 ="x"+(i+1)+""+j; 
            G.addEdge(var1, var2); 
            } 
            // Affichage du graphe des contraintes 
            System.out.println("\nContraintes de la grille: \n"+G); 
             
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
