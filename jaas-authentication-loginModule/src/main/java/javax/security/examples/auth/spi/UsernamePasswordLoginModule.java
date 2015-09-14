package javax.security.examples.auth.spi;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class UsernamePasswordLoginModule implements LoginModule {
	
	private Subject subject;  
    private CallbackHandler callbackHandler;  
    private Map<String, ?> sharedState;
    private Map<String, ?> options;
    private String username;  
    private char[] password;  
    
    private SimplePrincipal principal;
    
    public UsernamePasswordLoginModule(){
    	System.out.println("UsernamePasswordLoginModule construct");
    }

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;  
        this.callbackHandler = callbackHandler; 
        this.sharedState = sharedState;
        this.options = options;
        System.out.println("UsernamePasswordLoginModule initialized");
	}

	@Override
	public boolean login() throws LoginException {
		
		if (callbackHandler == null){
			throw new LoginException("Error: no CallbackHandler available " + "to garner authentication information from the user");
		}
		
		Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username: ");
        callbacks[1] = new PasswordCallback("Password: ", false);

        try {
			callbackHandler.handle(callbacks);
			username = ((NameCallback)callbacks[0]).getName();
			char[] tmpPassword = ((PasswordCallback)callbacks[1]).getPassword();
			if (tmpPassword == null) {
			    tmpPassword = new char[0];
			}
			password = new char[tmpPassword.length];
			System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
		} catch (IOException e) {
			throw new LoginException(e.getMessage());  
		} catch (UnsupportedCallbackException e) {
			throw new LoginException("Error: No callback available to authentication data: " + e.getMessage()); 
		}
        
        boolean usernameCorrect = false;
        if (username.equals("admin")){
        	usernameCorrect = true;
        }
        if(new String(password).equals("admin")){
        	return true;
        }
        
        if (!usernameCorrect) {
            throw new FailedLoginException("User Name Incorrect");
        } else {
            throw new FailedLoginException("Password Incorrect");
        }
        		
	}

	@Override
	public boolean commit() throws LoginException {
		
		principal = new SimplePrincipal(username);
		if (!subject.getPrincipals().contains(principal)){
			subject.getPrincipals().add(principal);
		}
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		username = null;
		password = null;
		return true;
	}

}
