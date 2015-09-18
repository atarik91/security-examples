package org.jboss.security.examples.ldap.jndi;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ReferralException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jdniClient")
public class JNDIClientServlet extends HttpServlet {

    private static final long serialVersionUID = -4631104373864950088L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            jdniSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jdniSearch() throws NamingException {

        String user = "kylin";
        String baseDN = "ou=Customers,dc=example,dc=com";
        String filter = "(uid={0})";

        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.66.218.46:389");
        env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=example,dc=com");
        env.put(Context.SECURITY_CREDENTIALS, "redhat");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        //additional
        env.put("baseCtxDN", "ou=Customers,dc=example,dc=com");
        env.put("roleAttributeID", "cn");
        env.put("roleFilter", "(uniqueMember={1})");
        env.put("rolesCtxDN", "ou=Tester,dc=example,dc=com");
        env.put("baseFilter", "(uid={0})");
        env.put("bindCredential", "redhat");
        env.put("bindDN", "cn=Manager,dc=example,dc=com");
        env.put("jboss.security.security_domain", "test-security");
        InitialLdapContext ctx = new InitialLdapContext(env, null);
        
        int searchTimeLimit = 10000 ;
        String distinguishedNameAttribute = "distinguishedName";
        
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        constraints.setTimeLimit(searchTimeLimit);
        String attrList[] = {distinguishedNameAttribute};
        constraints.setReturningAttributes(attrList);
        
        NamingEnumeration results = null;
        
        Object[] filterArgs = {user};
        
        LdapContext ldapCtx = ctx;
        
        boolean referralsLeft = true;
        SearchResult sr = null;
        while (referralsLeft) {
            try {
                results = ldapCtx.search(baseDN, filter, filterArgs, constraints);
                while (results.hasMore()) {
                    sr = (SearchResult) results.next();
                    break;
                 }
                 referralsLeft = false;
            } catch (ReferralException e) {
                ldapCtx = (LdapContext) e.getReferralContext();
                if (results != null) {
                   results.close();
                }
            }
        }
        
        System.out.println(sr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
