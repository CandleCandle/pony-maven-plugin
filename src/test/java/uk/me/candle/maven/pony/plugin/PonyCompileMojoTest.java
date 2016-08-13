package uk.me.candle.maven.pony.plugin;

import java.io.File;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class PonyCompileMojoTest {

//	@Rule
//	public MojoRule rule = new MojoRule() {
//		@Override protected void before() throws Throwable { } 
//		@Override protected void after() { }
//	};
	private MojoTestCase test = new MojoTestCase();
	static class MojoTestCase extends AbstractMojoTestCase {
		protected Mojo lookupMojo(String goal, File pom) throws Exception {
			return super.lookupMojo(goal, pom);
		}
	};

	@Ignore
	@Test
	public void testSomething() throws Exception {
		File pom = AbstractMojoTestCase.getTestFile("src/test/resources/basic_packaging/pom.xml");
		assertNotNull(pom);
		assertTrue(pom.exists());

		PonyCompileMojo myMojo = (PonyCompileMojo) test.lookupMojo("compile", pom);
		assertNotNull(myMojo);
	}
}

// vi: set sw=4 ts=4 noet