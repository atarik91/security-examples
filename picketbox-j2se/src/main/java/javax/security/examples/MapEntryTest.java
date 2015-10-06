package javax.security.examples;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class MapEntryTest {

    public static void main(String[] args) {

        Map<String,?> value = new HashMap<String,Object>();
        value.put("jboss.security.security_domain", null);
        Map<String,?> options = Collections.unmodifiableMap(value);
        Properties env = new Properties();
        Iterator iter = options.entrySet().iterator();
        while (iter.hasNext()){
           Entry entry = (Entry) iter.next();
           env.put(entry.getKey(), entry.getValue());
        }
    }

}
