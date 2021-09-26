import java.io.IOException;
import java.util.*;
import java.io.*; 

public class firstpass {

    public static void main( String[] args ) throws IOException {
    
    //Get Support and Confidence Input from user
    System.out.print("Input Support, hit Enter, and input Confidence (in %): ");
    Scanner sc = new Scanner(System.in);
    double support = sc.nextDouble();
    int support_actual = (int) (support * 20) / 100;
    int counter = 0;
    double confidence = sc.nextDouble();
    sc.close();

    //System.out.println(support_actual);
    System.out.println("Association Rules for Apriori Algorithm: ");

    if( support == 0 ) {
        System.out.println("INVALID SUPPORT");
        System.exit(0);
    }

    if( confidence == 0 ) {
        System.out.println("INVALID CONFIDENCE");
        System.exit(0);
    }

    String filePath = "DB11.csv";
    long startTime = System.nanoTime();
    //Master Hashmap to store frequent itemsets as keys with support values as values
    HashMap<List<String>, Integer> map_master = new HashMap<List<String>, Integer>(); 
    //Hashmap to store 1-itemsets
    HashMap<String, Integer> map = new HashMap<String, Integer>(); 
    //Hashmap to store 2-itemsets
    HashMap<List<String>, Integer> map_double = new HashMap<List<String>, Integer>(); 
    //Hashmap to store 3-itemsets
    HashMap<List<String>, Integer> map_triple = new HashMap<List<String>, Integer>(); 

   
    String line;
    BufferedReader reader = new BufferedReader(new FileReader(filePath));

    //---------------pass through data-----------------//

    while ( (line = reader.readLine()) != null ) {
            
            String[] parts = line.split(",");

            /* Append to Hashmap to store single Candidate sets */

            for( int i = 0; i<parts.length; i++ ) {
                if( !map.containsKey(parts[i]) ) {
                    map.put(parts[i], 1);
                    counter++;
                }
                else {
                    map.put(parts[i], map.get(parts[i])+1);
                }
            }
 
        }


    //iterate through the hashmap to see if the support is upheld
    Set<String> hash_Set_single = new HashSet<String>();
    for (String key : map.keySet()) {

            List<String> single_itemset = new Vector<String>();
            single_itemset.add(key);


            if( map.get(key) >= support_actual ) {
        
                /*add the qualified itemsets into a set and 
                update master Hashmap*/
                hash_Set_single.add(key);
                map_master.put(single_itemset, map.get(key));

            }
            
        }

        reader.close();

    //---------------pass through data-----------------//
    String filePath2 = "DB11.csv";
    String line2;
    BufferedReader reader2 = new BufferedReader(new FileReader(filePath2));
    while ( (line2 = reader2.readLine()) != null )
    {
   
        String[] parts = line2.split(",");

        for( int i = 0; i<parts.length; i++ ) {

            //check if element is in frequent 1-itemset 
            if( !hash_Set_single.contains(parts[i]) ) {
                continue;
            }

            else {

                for( int j = i+1; j<parts.length; j++ ) {

                    //check if element in frequent 1-itemset 
                    if( !hash_Set_single.contains(parts[j]) ) {
                        continue;
                    }
                    
                    else {

                        //create, add, and sort vector to store 2-itemset
                        List<String> double_itemset = new Vector<String>();
                        double_itemset.add(parts[i]);
                        double_itemset.add(parts[j]);
                        Collections.sort(double_itemset);

                        //add vector of double itemset to double hashmap
                        if( !map_double.containsKey(double_itemset) )
                            map_double.put(double_itemset, 1);
                        else 
                            map_double.put( double_itemset, map_double.get(double_itemset)+1 );
                    }

                }

            }
            
        }

    }

    //Set to hold frequent 2-itemsets
    Set<List<String>> hash_Set_double = new HashSet<List<String>>();
    
    for (Map.Entry<List<String>,Integer> entry : map_double.entrySet()) {

        if( entry.getValue() >= support_actual ) {
            map_master.put(entry.getKey(), entry.getValue());
            hash_Set_double.add(entry.getKey());
        }
    
    }

    reader2.close();


    //---------------------pass through data---------------------------//

    String filePath3 = "DB11.csv";
    String line3;
    BufferedReader reader3 = new BufferedReader(new FileReader(filePath3));
    while ( (line3 = reader3.readLine()) != null )
    {
   
        String[] parts = line3.split(",");

        //first pointer
        for( int i = 0; i<parts.length; i++ ) {

            //check if in frequent 1-itemset 
            if( !hash_Set_single.contains(parts[i]) ) {
                continue;
            }

            else {

                //second pointer
                for( int j = i+1; j<parts.length; j++ ) {

                    //check if in frequent 1-itemset 
                    if( !hash_Set_single.contains(parts[j]) ) {
                        continue;
                    }
                        //create pair of first two items
                        List<String> first_double_itemset = new Vector<String>();
                        first_double_itemset.add(parts[i]);
                        first_double_itemset.add(parts[j]);
                        Collections.sort(first_double_itemset);
                    
                    //check if in frequent 2-itemset
                    if( !hash_Set_double.contains(first_double_itemset) ) {
                        continue;
                    }
                    
                    
                    else {
                        
                        //third pointer
                        for( int k = j+1; k<parts.length; k++ ) {

                            if( !hash_Set_single.contains(parts[k]) ) {
                                continue;
                            }

                            //create pair of second two items
                            List<String> second_double_itemset = new Vector<String>();
                            second_double_itemset.add(parts[k]);
                            second_double_itemset.add(parts[j]);
                            Collections.sort(second_double_itemset);

                            if( !hash_Set_double.contains(second_double_itemset) ) {
                                continue;
                            }

                            //create pair of first and third items
                            List<String> third_double_itemset = new Vector<String>();
                            third_double_itemset.add(parts[k]);
                            third_double_itemset.add(parts[i]);
                            Collections.sort(third_double_itemset);

                            if( !hash_Set_double.contains(third_double_itemset) ) {
                                continue;
                            }

                            else {
                                
                                //append to hashtable with triple itemsets
                                List<String> triple_itemset = new Vector<String>();
                                triple_itemset.add(parts[k]);
                                triple_itemset.add(parts[i]);
                                triple_itemset.add(parts[j]);
                                Collections.sort(triple_itemset);

                                if( !map_triple.containsKey(triple_itemset) )
                                    map_triple.put(triple_itemset, 1);
                                else 
                                    map_triple.put( triple_itemset, map_triple.get(triple_itemset)+1 );
                                
                            }

                        }

                    }

                }

            }

        }

    }

     //Set to hold frequent 3-itemsets
     Set<List<String>> hash_Set_triple = new HashSet<List<String>>();
    
     for (Map.Entry<List<String>,Integer> entry : map_triple.entrySet()) {
 
         if( entry.getValue() >= support_actual ) {
             map_master.put(entry.getKey(), entry.getValue());
             hash_Set_triple.add(entry.getKey());
         }
     
     }

    reader3.close();

    //------------generate association rules for double itemsets-------------//

    for( List<String> temp: hash_Set_double ) {

        //temp holds a list of two objects

        int temp_supp = 0; //holds temp's support value
        int left_val = 0; //hold temp's left support value
        int temp_conf = 0; //holds temp's confidence value

        //iterate through the master hashmap to find the support value associated with the double itemsets
        for( Map.Entry<List<String>,Integer> entry : map_master.entrySet() ) {

            if( entry.getKey() == temp ) {
                temp_supp = entry.getValue();
            }
        
        }

        //iterate through temp to find the support values associated with the single items within the double itemset
        for( int i = 0; i<temp.size(); i++ ) {

            //create a list to hold the values in the temp 
            //list is used instead of string for type agreement
            List<String> temp_objects = new Vector<String>();
            temp_objects.add(temp.get(i));

            //assign the appropriate support values to the items within the double itemset
            left_val = map_master.get(temp_objects);

            //calculate confidence
            temp_conf = (temp_supp * 100)/left_val;

            //perform confidence check with user inputted values
            if( temp_conf >= confidence ) {

                //for the first association rule
                if( i == 0 )
                    System.out.print(temp.get(i)+" -> "+temp.get(i+1));
                //for the second association rule
                else if( i == 1 )
                    System.out.print(temp.get(i)+" -> "+temp.get(i-1));

                System.out.println(" ["+(temp_supp*100)/20+"%"+", "+temp_conf+"%] ");
            }
        
        }

    }

    //--------------generate association rules for triple itemsets-------------//
    for( List<String> temp: hash_Set_triple ) {

        //temp holds a list of two objects

        int temp_supp = 0; //holds temp's support value
        int left_val = 0; //hold temp's left support value
        int temp_conf = 0; //holds temp's confidence value

        //iterate through the master hashmap to find the support value associated with the double itemsets
        for( Map.Entry<List<String>,Integer> entry : map_master.entrySet() ) {

            if( entry.getKey() == temp ) {
                temp_supp = entry.getValue();
            }
        
        }

        //iterate through temp to find the support values associated with the single items within the double itemset
        for( int i = 0; i<temp.size(); i++ ) {

            //create a list to hold the values in the temp 
            //list is used instead of string for type agreement
            List<String> temp_objects = new Vector<String>();
            if( i == 0 ) {
                temp_objects.add(temp.get(i));
                temp_objects.add(temp.get(i+1));
                left_val = map_master.get(temp_objects);
                temp_conf = (temp_supp * 100)/left_val;
                if( temp_conf >= confidence ) {
                    System.out.print(temp.get(i)+" "+temp.get(i+1)+" -> "+temp.get(i+2));
                    System.out.println(" ["+(temp_supp*100)/20+"%"+", "+temp_conf+"%] ");
                }
            }

            if( i == 1 ) {
                temp_objects.add(temp.get(i));
                temp_objects.add(temp.get(i+1));
                left_val = map_master.get(temp_objects);
                temp_conf = (temp_supp * 100)/left_val;
                if( temp_conf >= confidence ) {
                    System.out.print(temp.get(i)+" "+temp.get(i+1)+" -> "+temp.get(i-1));
                    System.out.println(" ["+(temp_supp*100)/20+"%"+", "+temp_conf+"%] ");
                }
            }

            if( i == 2 ) {
                temp_objects.add(temp.get(i-2));
                temp_objects.add(temp.get(i));
                left_val = map_master.get(temp_objects);
                temp_conf = (temp_supp * 100)/left_val;
                if( temp_conf >= confidence ) {
                    System.out.print(temp.get(i-2)+" "+temp.get(i)+" -> "+temp.get(i-1));
                    System.out.println(" ["+(temp_supp*100)/20+"%"+", "+temp_conf+"%] ");
                }
            }
        
        }

    }

    long elapsedTime = System.nanoTime() - startTime;
    System.out.println("Total execution time for Apriori Algorithm: "
                + elapsedTime/1000000+"ms");

    }

}