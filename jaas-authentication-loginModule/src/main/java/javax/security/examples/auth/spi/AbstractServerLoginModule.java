package javax.security.examples.auth.spi;

import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public abstract class AbstractServerLoginModule implements LoginModule {
	
	private static final String PASSWORD_STACKING = "password-stacking";
	private static final String USE_FIRST_PASSWORD = "useFirstPass";	
    private static final String PRINCIPAL_CLASS = "principalClass";
	private static final String UNAUTHENTICATED_IDENTITY = "unauthenticatedIdentity";
	private static final String MODULE = "module";
	private static final String SECURITY_DOMAIN_OPTION = "jboss.security.security_domain";
	
	private static final String[] ALL_VALID_OPTIONS = {PASSWORD_STACKING,PRINCIPAL_CLASS,UNAUTHENTICATED_IDENTITY,MODULE,SECURITY_DOMAIN_OPTION};
	
	private HashSet<String> validOptions;
	   
	protected Subject subject;
	protected CallbackHandler callbackHandler; 
	protected Map sharedState; 
	protected Map options;
    /** Flag indicating if the shared credential should be used */
    protected boolean useFirstPass;
	/** Flag indicating if the login phase succeeded. Subclasses that override
	 *     the login method must set this to true on successful completion of login
	 */
	protected boolean loginOk;
	/** An optional custom Principal class implementation */
	protected String principalClassName;
	/** the principal to use when a null username and password are seen */
	protected Principal unauthenticatedIdentity;
	/** jboss module name to load Callback class etc */
	protected String jbossModuleName;

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
		
		this.subject = subject;
	    this.callbackHandler = callbackHandler;
	    this.sharedState = sharedState;
	    this.options = options;
	    
	    if (validOptions != null) {
	    	addValidOptions(ALL_VALID_OPTIONS);
	        checkOptions();
	    }
	    
	    String passwordStacking = (String) options.get(PASSWORD_STACKING);
	    if( passwordStacking != null && passwordStacking.equalsIgnoreCase(USE_FIRST_PASSWORD) ){
	    	useFirstPass = true;
	    }
	    
	    principalClassName = (String) options.get(PRINCIPAL_CLASS);

	    String name = (String) options.get(UNAUTHENTICATED_IDENTITY);
	    if( name != null ){
	    	try {
				unauthenticatedIdentity = createIdentity(name);
				System.out.println("unauthenticated Identity: " + name);
			} catch (Exception e) {
				System.out.println("unauthenticated Identity Failed: " + name + ", " + e.getMessage());
			}
	    }
	    jbossModuleName = (String)options.get(MODULE);
	}

	@Override
	public boolean login() throws LoginException {
		
		loginOk = false;
		if( useFirstPass == true ){
			try {
				Object identity = sharedState.get("javax.security.auth.login.name");
				Object credential = sharedState.get("javax.security.auth.login.password");
				if( identity != null && credential != null ) {
					loginOk = true;
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		return false;
	}

	@Override
	public boolean commit() throws LoginException {
		
		if( loginOk == false ){
			return false;
		}
		
		Set<Principal> principals = subject.getPrincipals();
		Principal identity = getIdentity();
		principals.add(identity);
		
		Group[] roleSets = getRoleSets();
		for(int g = 0; g < roleSets.length; g ++){
			Group group = roleSets[g];
			String name = group.getName();
			Group subjectGroup = createGroup(name, principals);
			Enumeration<? extends Principal> members = group.members();
			while( members.hasMoreElements() ){
				Principal role = (Principal) members.nextElement();
	            subjectGroup.addMember(role);
			}
		}
		
		Group callerGroup = getCallerPrincipalGroup(principals);
		if (callerGroup == null){
			callerGroup = new SimpleGroup("CallerPrincipal");
	           callerGroup.addMember(identity);
	           principals.add(callerGroup);
		}
		
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		return loginOk;
	}

	@Override
	public boolean logout() throws LoginException {
		Principal identity = getIdentity();
	    Set<Principal> principals = subject.getPrincipals();
	    principals.remove(identity);
	    Group callerGroup = getCallerPrincipalGroup(principals);
	    if (callerGroup != null)
	       principals.remove(callerGroup);
		return true;
	}
	
	protected void addValidOptions(final String[] moduleValidOptions){
		if (validOptions == null){
			validOptions = new HashSet<String>(moduleValidOptions.length);
		}
		validOptions.addAll(Arrays.asList(moduleValidOptions));
	}
	
	protected void checkOptions() {
		for (Object key : options.keySet()){
			if (!validOptions.contains(key)){
				System.err.println(key + " Note contained in validOptions");
			}
		}
	}
	
	protected Group getCallerPrincipalGroup(Set<Principal> principals){
		Group callerGroup = null;
	      for (Principal principal : principals)
	      {
	         if (principal instanceof Group)
	         {
	            Group group = Group.class.cast(principal);
	            if (group.getName().equals("CallerPrincipal"))
	            {
	               callerGroup = group;
	               break;
	            }
	         }
	      }
	      return callerGroup;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Principal createIdentity(String username) throws Exception {
		
		Principal p = null;
		if( principalClassName == null ){
			p = new SimplePrincipal(username);
		} else {
			ClassLoader loader = SecurityActions.getContextClassLoader();
			Class clazz = loader.loadClass(principalClassName);
			Class[] ctorSig = {String.class};
			Constructor ctor = clazz.getConstructor(ctorSig);
			Object[] ctorArgs = {username};
			p = (Principal) ctor.newInstance(ctorArgs);
		}
		
		return p;
	}
	
	protected Group createGroup(String name, Set<Principal> principals) {
		
		Group roles = null;
		Iterator<Principal> iter = principals.iterator();
		while(iter.hasNext()){
			Object next = iter.next();
			if( (next instanceof Group) == false ){
				continue;
			}
			Group grp = (Group) next;
			if( grp.getName().equals(name) ){
				roles = grp;
				break;
			}
		}
		
		if( roles == null ) {
			roles = new SimpleGroup(name);
			principals.add(roles);
		}
		
		return roles;
	}
	
	
	abstract protected Principal getIdentity();
	
	abstract protected Group[] getRoleSets() throws LoginException;

}
