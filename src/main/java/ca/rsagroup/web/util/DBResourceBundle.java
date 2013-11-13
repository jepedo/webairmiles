package ca.rsagroup.web.util;

import java.io.IOException;
import java.util.*;

public class DBResourceBundle extends ResourceBundle {   
     Properties props = null;   
     DBResourceBundle(Properties props) throws IOException {   
         this.props = props;            
     }   
     protected Object handleGetObject(String key) {   
         return props.getProperty(key);   
     }   
     public Enumeration<String> getKeys() {   
       Set<String> handleKeys = props.stringPropertyNames();   
       return Collections.enumeration(handleKeys);   
     }   
 }
