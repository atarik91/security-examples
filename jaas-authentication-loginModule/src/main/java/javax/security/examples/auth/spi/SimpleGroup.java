package javax.security.examples.auth.spi;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class SimpleGroup extends SimplePrincipal implements Group, Cloneable {
	
	private HashMap members;

	public SimpleGroup(String groupName) {
		super(groupName);
		members = new HashMap(3);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addMember(Principal user) {
		boolean isMember = members.containsKey(user);
		if(!isMember){
			members.put(user, user);
		}
		return isMember == false;
	}

	@Override
	public boolean removeMember(Principal user) {
		Object prev = members.remove(user);
		return prev != null;
	}

	@Override
	public boolean isMember(Principal member) {
		boolean isMember = members.containsKey(member);
		if( isMember == false ){
			Collection values = members.values();
			Iterator iter = values.iterator();
			while( isMember == false && iter.hasNext() ){
				Object next = iter.next();
				if( next instanceof Group ){
					Group group = (Group) next;
					isMember = group.isMember(member);
				}
			}
		}
		return isMember;
	}

	@Override
	public Enumeration<? extends Principal> members() {
		return Collections.enumeration(members.values());
	}
	
	public String toString()
	   {
	      StringBuffer tmp = new StringBuffer(getName());
	      tmp.append("(members:");
	      Iterator iter = members.keySet().iterator();
	      while( iter.hasNext() )
	      {
	         tmp.append(iter.next());
	         tmp.append(',');
	      }
	      tmp.setCharAt(tmp.length()-1, ')');
	      return tmp.toString();
	   }
	   
	   public synchronized Object clone() throws CloneNotSupportedException  
	   {  
	      SimpleGroup clone = (SimpleGroup) super.clone();  
	      if(clone != null) 
	        clone.members = (HashMap)this.members.clone();   
	      return clone;  
	   }

}
