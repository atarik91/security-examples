package javax.security.examples;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.SimplePrincipal;
import org.picketbox.config.PicketBoxConfiguration;
import org.picketbox.factories.SecurityFactory;

public class LDAPModuleDebug {
    
    public static void main(String[] args) throws LoginException, IOException, InterruptedException {

        SecurityFactory.prepare(); 
        
        final String securityDomain = "Sample-Ldap";
        
        try {
            PicketBoxConfiguration config = new PicketBoxConfiguration();
            config.load(SampleMain.class.getClassLoader().getResourceAsStream("picketbox/authentication-debug.conf"));
            
            AuthenticationManager authManager = SecurityFactory.getAuthenticationManager(securityDomain); 
            
            String userName = "kylin";
            String credString = "password";
            boolean isValid = false;
            final Principal userPrincipal = new SimplePrincipal(userName);
            final Subject subject = new Subject();
            isValid = authManager.isValid(userPrincipal, credString, subject);
            if(isValid){
                System.out.println("Authentication succeeded!");
            } else {
                System.out.println("Authentication failed!");
            }
           
        } finally {
            SecurityFactory.release();
        }
        
//        Configuration config = Configuration.getConfiguration();
//        
//        System.out.println(config);
    }

}
