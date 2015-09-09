## What's this

This is a sample project for [JAAS Authentication Tutorial](http://docs.oracle.com/javase/7/docs/technotes/guides/security/jaas/tutorials/GeneralAcnOnly.html).

## Run

~~~
$ java -Djava.security.auth.login.config=jaas.config -jar target/jaas-authentication-tutorial.jar 
user name: testUser
password: testPassword
		[SampleLoginModule] user entered user name: testUser
		[SampleLoginModule] user entered password: testPassword
		[SampleLoginModule] authentication succeeded
		[SampleLoginModule] added SamplePrincipal to Subject
Authentication succeeded!
~~~
