package javax.security.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.SimplePrincipal;
import org.picketbox.config.PicketBoxConfiguration;
import org.picketbox.factories.SecurityFactory;

public class MultipleModuleMain {
    
    @SuppressWarnings("static-access")
    public static void run(String securityDomain) throws IOException, InterruptedException, LoginException {

        System.out.println("securityDomain: " + securityDomain);
        
        SecurityFactory.prepare(); 
                
        try {
            PicketBoxConfiguration config = new PicketBoxConfiguration();
            config.load(SampleMain.class.getClassLoader().getResourceAsStream("picketbox/authentication.conf"));
            
            AuthenticationManager authManager = SecurityFactory.getAuthenticationManager(securityDomain); 
            
            String userName = null;
            String credString ;
            boolean isValid = false;
            for(int i = 0 ; i < 3 ; i ++) {
                System.out.print("Username:");
                userName = (new BufferedReader(new InputStreamReader(System.in))).readLine();
                System.out.print("Password:");
                credString = (new BufferedReader(new InputStreamReader(System.in))).readLine();
                final Principal userPrincipal = new SimplePrincipal(userName);
                final Subject subject = new Subject();
                isValid = authManager.isValid(userPrincipal, credString, subject);
                if(isValid){
                    System.out.println("Authentication succeeded!");
                    break;
                } else {
                    System.out.println("Authentication failed!");
                    Thread.currentThread().sleep(3000);
                }
            }
            
            
            if(!isValid){
                throw new LoginException("The username " +  userName + " and/or password could not be authenticated by security domain " + securityDomain + ".");
            }
        } finally {
            SecurityFactory.release();
        }
        
        System.out.println();
    }

    public static void main(String[] args) throws LoginException, IOException, InterruptedException {

        String[] modules = {/*"Sample", "Sample-File",*/ "Sample-Ldap"};
        for(String module : modules) {
            run(module);
        }
    }

}
