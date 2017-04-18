package com.health.app.search.services;

import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import static javax.ejb.LockType.READ;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Path("/patients")
@Singleton
@Lock(READ)
public class PatientService extends searchAppFunctions {
    
        
    public StringBuffer tokenizeDocument(Patient pat) {
                
        StringBuilder listString = new StringBuilder();
        StringBuffer sb = new StringBuffer();
        
        List<String> document = pat.getDocuments();
        String id = pat.getId().toString();
        String name = pat.getName().replaceAll(" ", "_");
        
        listString.append(id+" ");
        listString.append(name+" ");        
        for (String s : document)
                listString.append(s+" ");
                
        Pattern p = Pattern.compile("\n|[;^=()&%#.!?\\-:,/*]|[0-9]");
 		Matcher m = p.matcher(listString);
 		while (m.find()) {
     		m.appendReplacement(sb, " ");
 		}
 		m.appendTail(sb);
        
        return sb;
           
    }
    
    public String [] processingDocument(Patient pat) {
        StringBuffer tokenizedString = tokenizeDocument(pat);
        String [] words = tokenizedString.toString().split(" ");
        
        //ADD METHOD FOR REMOVING STOP WORDS
        
        return words;
    }
    
    
    public HashMap<String, List<Patient>> createInvertedIndex(List<Patient> thePatientDocs) {
        
        HashMap<String, List<Patient>> hmap = new HashMap<String, List<Patient>>();
        HashSet<String> hs = new HashSet<String>();
    
        //create set of all words in the three documents
        for (Patient pat: thePatientDocs) {
            
            //process document by tokenizing and converting to string array
            String [] words = processingDocument(pat);

            for (String s: words) {
                //if (s.length() >= 2)
                hs.add(s.toLowerCase());
            }
        }
    
        //create the inverted index
        for (String setWord: hs) {
            
            List<Patient> documentList = new ArrayList<Patient>();
            
            for (Patient pat: thePatientDocs) {
                
                Set<String> mySet = new HashSet<String>();
                List<String> wordList = new ArrayList<String>();
                
                //process document by tokenizing and converting to string array
                String [] tokenList = processingDocument(pat);
                
                //convert to set and then list
                Collections.addAll(mySet, tokenList);
                wordList.addAll(mySet);
                
                //lowercase every string
                replace(wordList);
                
                if (wordList.contains(setWord)) {
                    documentList.add(pat);
                }
            }
            
            hmap.put(setWord, documentList);
        }
        
        return hmap;
    }
 
 
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> searchPatients(@QueryParam("query") String query) {
        
        List<Patient> thePatientDocs = getPatientDocs();
        HashMap<String, List<Patient>> invertIndex = createInvertedIndex(thePatientDocs);
        
        //PRINT OUT THE KEYS (LOOKUP TERMS ) FROM THE INVERTED INDEX
        //for ( String key : invertIndex.keySet() ) {
          // System.out.println(key);
        //}
        
        String nQuery = query.toLowerCase();
        System.out.println(nQuery);
        
        if (invertIndex.containsKey(nQuery)) {
            return invertIndex.get(nQuery);
        }else {
            return new ArrayList<Patient>();
        }

    }
    
    

    

}
