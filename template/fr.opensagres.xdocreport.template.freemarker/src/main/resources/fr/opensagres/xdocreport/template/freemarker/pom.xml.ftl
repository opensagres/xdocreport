<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>${artifactId}</groupId>
	<artifactId>${artifactId}</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	
	[#if isSnapshot ]
	<repositories>
		<repository>
			<id>sonatype</id>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
			<url>http://oss.sonatype.org/content/repositories/snapshots/</url>
		</repository>
	</repositories>
	[/#if]
		
	<dependencies>
		<dependency>
			<groupId>fr.opensagres.xdocreport</groupId>
			<artifactId>fr.opensagres.xdocreport.template.${templateExtension}</artifactId>
			<version>${version}</version>
		</dependency>
		<dependency>
			<groupId>fr.opensagres.xdocreport</groupId>
			<artifactId>fr.opensagres.xdocreport.document.${documentExtension}</artifactId>
			<version>${version}</version>			
		</dependency>
	</dependencies>
</project>