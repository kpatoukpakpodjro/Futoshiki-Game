package csp.futoshiki2;

import java.util.Date;

public class Backtrackingv2 {
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
					 for(CNT adj: g.adjacentTo(variable)) { 
						 if(!config.get(adj.getVar()).equals("")){ // Voir si la variable est affectée 
						 
						 int val2 = Integer.parseInt(config.get(adj.getVar())); 
						 
						 if(adj.getVar().charAt(1) == variable.charAt(1)){ // Contrainte sur une ligne
 						 
 						 // System.out.println("i "+i+", j1 "+j1); 
						 if(adj.getCh()==">") {
						 if(val1 <= val2) return false; }
						  
						 if(adj.getCh()=="<") {
							 if(val1 >= val2) return false; }
 						 } 
						 
						 if(adj.getVar().charAt(2) == variable.charAt(2)){ // Contrainte sur une colonne
	 						 
	 						 // System.out.println("i "+i+", j1 "+j1); 
							 if(adj.getCh()==">") {
							 if(val1 <= val2) return false; }
							  
							 if(adj.getCh()=="<") {
								 if(val1 >= val2) return false; }
 							 } 
						 
					 }}}
                // A compléter 
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
	
	public static ST<String, String> backtracking(ST<String, String> config, ST<String, SET<String>> domain, Graph g){
		
		//recursion base case - check configuration completeness
		if(complete(config))
			return config;
		       
		ST<String, String> result = null;
		
		//get a variable
		String v = getVariable(config);
		
		//get a SET of all the variable's values
		SET<String> vu = orderDomainValue(v, domain);
		
		//loop through all the variable's values
		for(String u: vu) {
			//if(consistent(u, v, config, g)) {
                    
			if(consistent(u, v, config, g)) {
                             	config.put(v, u);
                                 
				result = backtracking(config, domain, g);
				if(result != null)
					return result;
				config.put(v,"");
                               
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
            for(  i=0;i<6;i++){  // Ligne 
                System.out.println("");    
                for(  j=0;j<6;j++)   {
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
                for(  j=0;j<7;j++)   {
                    if(((String)grilleCV[i][j])!="")
                       System.out.print(grilleCV[i][j]+" ");
                    else 
                        System.out.print("  ");
                };
            }
            
            System.out.println("");    
            for(  j=0;j<6;j++)   {
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
                    System.out.print(".");
           
            //  ------------------- Affichage de la grille initiale ------------------------
              
         // Ajouter les contraintes horizontales 
            for(  i=1;i<=7;i++) // Ligne 
            for(  j=1;j<=6;j++) // Colonne 
            if(grilleCH[i-1][j-1] !=""){ 
              var1 ="x"+i+""+j; 
              var2 ="x"+i+""+(j+1); 
            G.addEdge(var1, var2,grilleCH[i-1][j-1] ); 
            } 
            // Ajouter les contraintes verticales 
            for(  i=1;i<=6;i++) // Ligne 
            for(  j=1;j<=7;j++) // Colonne 
            if(grilleCV[i-1][j-1] !=""){ 
              var1 ="x"+i+""+j; 
              var2 ="x"+(i+1)+""+j; 
            G.addEdge(var1, var2,grilleCV[i-1][j-1]); 
            } 
            
            // Affichage du graphe des contraintes
            System.out.println("\nContraintes de la grille: \n"+G);
            
            // Tables des domaines
            ST<String, SET<String>> domainTable = new ST<String, SET<String>>();

            // Remplir les Domaines
            Object[][] domains = new Object[7][7];

            for(  i=0;i<7;i++)       // Colonne 
                for(  j=0;j<7;j++)      // Ligne
                    domains[i][j] = new SET<String>();

            for(  i=0;i<7;i++)       // Colonne 
                for(  j=0;j<7;j++){     // Ligne

                    if(((String)grille[i][j])!="")
                        ((SET<String>)domains[i][j]).add(new String((String)grille[i][j])); // Domaine avec une seule valeur (case remplie )
                    else{

                        for(  k=1;k<=7;k++)
                            ((SET<String>)domains[i][j]).add(""+k);// Domaine avec {1, 2, 3, ..., 7} ( case vide)
                    }                    
                }

            // Ajouter les domaines Ã  la table       
            for( i=1;i<=7;i++)       
                for( j=1;j<=7;j++)
                    domainTable.put("x"+i+""+j, ((SET<String>)domains[i-1][j-1]));

            // Configuration initiale
            ST<String, String> config = new ST<String, String>();

           for(  i=1;i<=7;i++)       // Ligne 
           for( j=1;j<=7;j++)   // Colonne
               
               if(((String)grille[i-1][j-1])!="")           
                   config.put("x"+i+""+j,grille[i-1][j-1]);
               else
                   config.put("x"+i+""+j,""); // Variables non affectÃ©es
        
           System.out.println("\nCalcul en cours ... ");   
           long temps1 = new Date().getTime();
           ST<String, String> result = backtracking(config, domainTable, G);
           long temps2 = new Date().getTime();           
        
           for( i=1;i<=7;i++){  // Ligne 
                System.out.println("");    
                for( j=1;j<=7;j++)   // Colonne
                    System.out.print(config.get("x"+i+""+j)+" ");
            };   
            
            System.out.println("\nTemps Ã©coulÃ© : "+(temps2-temps1)/1000.+"(s)");
     
	}
        
}