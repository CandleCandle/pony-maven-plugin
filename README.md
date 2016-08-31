Maven Plugin for Pony[lang]
===========================

Getting Started
---------------

Create a pom.xml, this plugin *must* be in the plugins list.

'''xml
<plugin>
	<groupId>uk.me.candle</groupId>
	<artifactId>pony-maven-plugin</artifactId>
	<version>.. latest ..</version>
	<extensions>true</extensions>
</plugin>
'''
(there is a sample, minimal pom.xml towards the end)

To select a version, either ommit the version element, to pick the latest version from Maven Central or use https://search.maven.org to find the available versions and select one.


Following the standard Maven layout, the directory structure should look a little like this, note that the `target` directory is created by the plugin/maven
'''
.
├── pom.xml
├── src
│   ├── main
│   │   └── pony
│   │       └── ${groupId}
│   │           └── ${artifactId}
│   │               └── something.pony
│   └── test
│       └── pony
│           └── ${groupId}
│               └── ${artifactId}
│                   ├── \_test\_something.pony
│                   └── test.pony
└── target
    └── pony-eventbus-0.6-SNAPSHOT.zip
'''
Note that some of these directories can be configured in the `pom.xml`, however, for convention's sake it is advised that this layout is used.

`mvn clean` will delete the `target` directory, thus giving you a fresh start.
`mvn pony-test` will do a `pony-test-compile` and then run the tests.
`mvn pony-compile` will compile the main code without the tests, generating a binary in the `target` directory.
`mvn install` will do a `package` then copy the artifact to your local maven repository, making it available as a library, at the version specified in the pom to other pony projects on your local computer.


If you have created some code that can be used as a library, then you can add a dependency in another project as per the standard maven way. In your pony code, you should be able to `use "${groupId}/${artifactId}"` to import the public classes from that library.


Appendicies
===========

Sample `pom.xml`
----------------

'''xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>.. YOUR GROUP ID HERE ..</groupId>
	<artifactId>.. THE NAME OF YOUR PROJECT ..</artifactId>
	<version>.. SYMANTIC VERSIONING ..</version>
	<packaging>pony</packaging><!-- reqired, tells maven to use the pony plugin -->
	<build>
		<plugins>
			<plugin>
				<groupId>uk.me.candle</groupId>
				<artifactId>pony-maven-plugin</artifactId>
				<version>0.4</version>
				<extensions>true</extensions><!-- tells maven to scan this plugin for additional packaging types -->
			</plugin>
		</plugins>
	</build>
</project>
'''

Rationale for various decisions
-------------------------------

Using the full groupId and artifactId as directory names; the alternative is to mimic the maven repository layout and have lots and lots of nested directories.
* makes the `use` statements easier; `use "${groupId}/${artifactId}"` is easier than `use "com/bar/arbitrary/artifact/name/components"`


Tests in a directory named the same as the main code (src/main/*${groupId}/${artifactId}* and src/test/*${groupId}/${artifactId}*) This is for clarity, I hope that it does not cause issues in the future with actor/class/etc name clashes.


Using a .zip format, java supports it, it's use should be transparant.
