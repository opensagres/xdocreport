# Xdocreport with simple Spring service #

Declare a template directory with your templates and call to this service.

## [TemplatesService.java](https://github.com/alediator/xdocreport/blob/master/thirdparties-extension/org.springframework.xdocreport.core/src/main/java/org/springframework/xdocreport/core/TemplatesService.java) ##

Simplify use to obtain a report.

## [applicationContext.xml](https://github.com/alediator/xdocreport/blob/master/thirdparties-extension/org.springframework.xdocreport.core/src/main/resources/applicationContext.xml) ##

Only needs reference to templates directory 

<pre>
    &lt;bean class="java.lang.String" id="templatesDirectory"&gt;
      &lt;constructor-arg type="String" value="target/classes/templates/" /&gt;
    &lt;/bean&gt;
    
    &lt;bean id="velocityTest" class="org.springframework.xdocreport.core.impl.TemplatesServiceImpl"&gt;
    	&lt;property name="templatesDirectory"&gt;&lt;ref bean="templatesDirectory"/&gt;&lt;/property&gt;
    &lt;/bean&gt;
</pre>

## Use  ##

### From pom.xml ###

Only adds the dependency:

<pre>
		&lt;!-- XDOCREPORT DEPENDENCIES --&gt;
		&lt;dependency&gt;
			&lt;groupId&gt;fr.opensagres.xdocreport&lt;/groupId&gt;
			&lt;artifactId&gt;org.springframework.xdocreport.core&lt;/artifactId&gt;
			&lt;version&gt;${xdocreport.version}&lt;/version&gt;
		&lt;/dependency&gt;
</pre>

### From a bean ([TemplatesServiceImplTest.java](https://github.com/alediator/xdocreport/blob/master/thirdparties-extension/org.springframework.xdocreport.core/src/test/java/org/springframework/xdocreport/core/impl/TemplatesServiceImplTest.java)) ###

To use from another service reference the service from a bean:

<pre>
	@Resource
	private TemplatesService velocityTest;
</pre>

, create a parameter map:

<pre>
		Map&lt;String, String&gt; values = new HashMap&lt;String, String&gt;();
		values.put(HELLO_WORLD_NAME, HELLO_WORLD_NAME_VALUE);
</pre>

and call to the service:

<pre>
InputStream is = velocityTest.generateTemplate(values, HELLO_WORLD);
</pre>

## More ##

I'm adapting some code with in a Spring MVC evolution of [xdocreport-demo](https://github.com/pascalleclercq/xdocreport-demo) with this functionality:

* Simply mashup with spring security and a database saving data from reports generated
* Connectors model for auto load data from:
 * Databases
 * Third-party WS
 * Redmine
* Rest service for report generation
