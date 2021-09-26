import java.io.IOException;
import java.util.*;
import java.io.*; 

public class brutepass1 {
    public static void main( String[] args ) throws IOException {
    
    //Get Support Input
    System.out.print("Input Support, hit Enter, and input Confidence (in %): ");
    Scanner sc = new Scanner(System.in);
    double support = sc.nextDouble();
    int support_actual = (int) (support * 20) / 100;
    int counter = 0;
    int counter1 =0;

    double confidence = sc.nextDouble(); 

    sc.close();

    String filePath = "DB11.csv";
    long startTime = System.nanoTime();
    HashMap<List<String>, Integer> map_master = new HashMap<List<String>, Integer>(); //Hashmap to store frequent itemsets with support values
    HashMap<String, Integer> map = new HashMap<String, Integer>(); //Hashmap to store single itemsets
    HashMap<List<String>, Integer> map_double = new HashMap<List<String>, Integer>(); //Hashmap to store double itemsets
    HashMap<List<String>, Integer> map_triple = new HashMap<List<String>, Integer>(); //Hashmap to store triple itemsets
    Set<List<String>> freq_itemset_brute = new HashSet<List<String>>();
    Set<List<String>> candidateKey = new HashSet<List<String>>();
    ArrayList<List<String>> transactions = new ArrayList<List<String>>();
    HashMap<List<String>, Integer> map_mastery_dummy = new HashMap<List<String>, Integer>();
   
    String line;
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    int max = 0;
    

    //--------------pass through data-----------------//

    while ( (line = reader.readLine()) != null )
        {
            int maxtest = 0;
            String[] parts = line.split(",");
            List<String> holder_list_transactions = new Vector<String>();

            for( int i = 0; i<parts.length; i++ ) {
                holder_list_transactions.add(parts[i]);
                if( !map.containsKey(parts[i]) ) {
                    map.put(parts[i], 1);
                    maxtest++;
                    
                }
                else {
                    map.put(parts[i], map.get(parts[i])+1);
                    maxtest++;
                }
            }
            Collections.sort(holder_list_transactions);

            transactions.add(holder_list_transactions);

            //get  size of largest itemset
            if( max < maxtest ) {
                max = maxtest;
            }

        }

    //iterate through the hashmap to see if the support is upheld
    Set<String> hash_Set_single = new HashSet<String>();
    List<String> hash_Set_single_list = new Vector<String>();
    for (String key : map.keySet()) {

            List<String> single_itemset = new Vector<String>();
            single_itemset.add(key);
            hash_Set_single_list.add(key);
            if( map.get(key) >= support_actual ) {


                //add the qualified itemsets into a set
                //frequent single itemset
                hash_Set_single.add(key);
                map_master.put(single_itemset, map.get(key));
            }
            
        }
    

    reader.close();


    //---------------pass through data-----------------//
    
            //first pointer
            for( int i = 0; i<hash_Set_single_list.size(); i++ ) {

                //second pointer
                for( int j = i+1; j<hash_Set_single_list.size(); j++ ) {
        
                    //create, add, and sort vector to store double itemset
                    List<String> double_itemset = new Vector<String>();
                    double_itemset.add(hash_Set_single_list.get(i));
                    double_itemset.add(hash_Set_single_list.get(j));
                    Collections.sort(double_itemset);

                    map_double.put(double_itemset, 0);
                    counter++;
                }
            
        }

    String filePath2 = "DB11.csv";
    String line2;
    BufferedReader reader2 = new BufferedReader(new FileReader(filePath2));

    while ( (line2 = reader2.readLine()) != null )
    {
   
        String[] parts = line2.split(",");

        //first pointer
        for( int i = 0; i<parts.length; i++ ) {

                //second pointer
                for( int j = i+1; j<parts.length; j++ ) {
        
                        //create, add, and sort vector to store double itemset
                        List<String> double_itemset = new Vector<String>();
                        double_itemset.add(parts[i]);
                        double_itemset.add(parts[j]);
                        Collections.sort(double_itemset);

                        //add vector of double itemset to double hashmap C2
                        if( !map_double.containsKey(double_itemset) ) {
                            map_double.put(double_itemset, 1);
                        }
                            
                        else {
                            map_double.put( double_itemset, map_double.get(double_itemset)+1 );
                        }

                }
            
        }

    }

    //Set to hold frequent two itemsets
    Set<List<String>> hash_Set_double = new HashSet<List<String>>();
    
    for (Map.Entry<List<String>,Integer> entry : map_double.entrySet()) {
        
        freq_itemset_brute.add(entry.getKey());
        if( entry.getValue() >= support_actual ) {
            map_master.put(entry.getKey(), entry.getValue());
            hash_Set_double.add(entry.getKey());
            
        }
    
    }


    reader2.close();


    //---------------------subsequent passes---------------------------//
    Integer ki = 3;

    /*while loop to generate all possible itemsets as long as support is upheld 
    if k-itemsets has no freqent sets, the loop terminates without generating k+1-itemsets*/

    Boolean nomoreadditions = false;
    while( nomoreadditions == false ) {

        
        candidateKey = generateCanKey(freq_itemset_brute, ki);
        map_mastery_dummy = addtomap(candidateKey, transactions);

        HashMap<List<String>, Integer> map_master_temp = new HashMap<List<String>, Integer>();
        for (Map.Entry<List<String>,Integer> entry : map_mastery_dummy.entrySet()) {

            if( entry.getValue() >= support_actual ) {
                map_master.put(entry.getKey(), entry.getValue());
                map_master_temp.put(entry.getKey(), entry.getValue());

            }

        }
        if( map_master_temp.isEmpty() ) {
            nomoreadditions = true;
        }
    
        
        freq_itemset_brute.clear();

        for( List<String> entry : candidateKey ) {
            freq_itemset_brute.add(entry);
        }

        ki++;

    }
    
        //first pointer
        for( int i = 0; i<hash_Set_single_list.size(); i++ ) {

            //second pointer
            for( int j = i+1; j<hash_Set_single_list.size(); j++ ) {

                //third pointer
                    for( int k = j+1; k<hash_Set_single_list.size(); k++ ) {

                            //append to hashtable with triple itemsets C3
                            List<String> triple_itemset = new Vector<String>();
                            triple_itemset.add(hash_Set_single_list.get(i));
                            triple_itemset.add(hash_Set_single_list.get(j));
                            triple_itemset.add(hash_Set_single_list.get(k));
                            Collections.sort(triple_itemset);
                            map_triple.put(triple_itemset, 0);
                            counter1++;
                            
                    }

            }

    }


    String filePath3 = "DB11.csv";
    String line3;
    BufferedReader reader3 = new BufferedReader(new FileReader(filePath3));
    while ( (line3 = reader3.readLine()) != null )
    {
   
        String[] parts = line3.split(",");

        //first pointer
        for( int i = 0; i<parts.length; i++ ) {

                //second pointer
                for( int j = i+1; j<parts.length; j++ ) {

                    //third pointer
                        for( int k = j+1; k<parts.length; k++ ) {

                                //append to hashtable with triple itemsets C3
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

     //Set to hold frequent three itemsets
     Set<List<String>> hash_Set_triple = new HashSet<List<String>>();
    
     for (Map.Entry<List<String>,Integer> entry : map_triple.entrySet()) {
 
         if( entry.getValue() >= support_actual ) {
             //map_master.put(entry.getKey(), entry.getValue());
             hash_Set_triple.add(entry.getKey());
         }
     
     }

     reader3.close();

    // //------------generate association rules for double itemsets-------------//

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

        temp_supp = map_master.get(temp);

        //iterate through the master hashmap to find the support value associated with the double itemsets
        // for( Map.Entry<List<String>,Integer> entry : map_master.entrySet() ) {
            
        //     if( entry.getKey() == temp ) {
        //         temp_supp = entry.getValue();
        //     }
        
        // }

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
    System.out.println("Total execution time for Brute Pass: "
                + elapsedTime/1000000+"ms");

    }

    public static Set<List<String>> generateCanKey(Set<List<String>> seedKey, Integer setSize) {
        int counter2 = 0;
        Set<List<String>> canKey = new HashSet<List<String>>();
        for( List<String> canKeyItemA : seedKey ) {
     
            for( List<String> canKeyItemB : seedKey ) {
   
                List<String> temp = new Vector<String>();
          
                if( canKeyItemA != canKeyItemB ) {
                    temp.addAll(canKeyItemA);
                    temp.addAll(canKeyItemB);
                    Set<String> removedups = new HashSet<>(temp);
                    temp = new Vector<String>(removedups); //to remove duplicates 
                    Collections.sort(temp);
                }
                if( !canKey.contains(temp) && temp.size() == setSize ) {
                    canKey.add(temp);
                    counter2++;
                }
            }
        }

        return canKey;

    }

    public static HashMap<List<String>, Integer> addtomap( Set<List<String>> canKey, ArrayList<List<String>> trans  ) {

        HashMap<List<String>, Integer> map_mastery_dummy = new HashMap<List<String>, Integer>();
        for( List<String> canKeyitem : canKey  ) {
            map_mastery_dummy.put( canKeyitem, 0);
        }

        for( List<String> transitem: trans ) {

            
            for( List<String> canKeyitem : canKey  ) {
                Boolean canKeyitemexists = true;
                for( String indiv_item : canKeyitem ) {
                    if( !transitem.contains(indiv_item) ) {
                        canKeyitemexists = false;
                        break;
                    }
                }

                if( canKeyitemexists ) {
                    map_mastery_dummy.put( canKeyitem, map_mastery_dummy.get(canKeyitem) + 1 );
                }

            }
        }
        return map_mastery_dummy;

    }


}