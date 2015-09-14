package javax.security.examples;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 * Hello world!
 *
 */
public class App  {
	
    public static void main( String[] args ) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    	
    	System.setProperty("java.security.auth.login.config", "jaas.config");
    	
    	String config_class = java.security.Security.getProperty("login.configuration.provider");
    	
    	Class<? extends Configuration> implClass = Class.forName(config_class, false, Thread.currentThread().getContextClassLoader()).asSubclass(Configuration.class);
    	Configuration configuration = implClass.newInstance();
    	
    	AppConfigurationEntry[] entries = configuration.getAppConfigurationEntry("UsernamePassword");
    	
        System.out.println(entries.length);
    }
}
