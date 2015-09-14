package javax.security.examples.auth.spi;

import java.security.Principal;

public class SimplePrincipal implements Principal {
	
	private final String name;

	public SimplePrincipal(String name) {
		super();
		this.name = name;
	}

	@Override
	public int hashCode() {
		return (name == null ? 0 : name.hashCode());
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object another) {
		
		if (!(another instanceof Principal))
	         return false;
		
		String anotherName = ((Principal) another).getName();
		boolean equals = false;
		if(name == null){
			equals = anotherName == null;
		} else {
			equals = name.equals(anotherName);
		}
		
		return equals;
	}

	@Override
	public String getName() {
		return name;
	}

}
