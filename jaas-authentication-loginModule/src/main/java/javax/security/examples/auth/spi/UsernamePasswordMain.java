package javax.security.examples.auth.spi;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class UsernamePasswordMain {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {

		LoginContext lc = null;
        try {
            lc = new LoginContext("UsernamePassword", new CLICallbackHandler());
        } catch (LoginException le) {
            System.err.println("Cannot create LoginContext. " + le.getMessage());
            System.exit(-1);
        } catch (SecurityException se) {
            System.err.println("Cannot create LoginContext. " + se.getMessage());
            System.exit(-1);
        }
        
        int i = 0;
        for (i = 0; i < 3; i++) {
        	try {
				lc.login();
				break;
			} catch (LoginException e) {
				System.err.println("Authentication failed:");
                System.err.println("  " + e.getMessage());
                Thread.currentThread().sleep(3000);
			}
        }
        
        if (i == 3) {
            System.out.println("Sorry, Authentication failed");
            System.exit(-1);
        }

        System.out.println("Authentication succeeded!");
	}

}
