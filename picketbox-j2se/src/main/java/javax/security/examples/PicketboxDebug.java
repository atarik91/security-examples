package javax.security.examples;

import javax.security.auth.login.Configuration;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.plugins.JBossAuthenticationManager;
import org.picketbox.factories.SecurityFactory;
import org.picketbox.plugins.PicketBoxCallbackHandler;

public class PicketboxDebug {

    public static void main(String[] args) {

        debugPrepare();
        
        getAuthenticationManager();
    }


    /**
     *  Set 'StandaloneConfiguration' as default JAAS Security Configuration
     */
    static void debugPrepare() {

        SecurityFactory.prepare(); 
        Configuration config = Configuration.getConfiguration();
        System.out.println(config);
        
        SecurityFactory.release();
        config = Configuration.getConfiguration();
        System.out.println(config);
    }
    
    /**
     * It not depend on 'SecurityFactory.prepare()', it equals 
     *     AuthenticationManager authManager = new JBossAuthenticationManager(securityDomain, new PicketBoxCallbackHandler());
     */
    static void getAuthenticationManager() {

        AuthenticationManager authManager = SecurityFactory.getAuthenticationManager("Sample");
        System.out.println(authManager);
    }

}
