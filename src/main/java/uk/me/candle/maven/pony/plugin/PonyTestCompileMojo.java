package uk.me.candle.maven.pony.plugin;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "test-compile")
public class PonyTestCompileMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("maybe compiling tests " + project.getArtifactId());
		File target = new File(project.getBuild().getDirectory());
		target.toPath().resolve("test").toFile().mkdirs();
		//--path "${basedir}/src/main/pony/" 
		List<String> command = Lists.newArrayList("ponyc",
					"--output", target.toPath().resolve("test").toString(),
					"--path", project.getBasedir().toPath().resolve("src").resolve("main").resolve("pony").toString()); // XXX get the src directory from the project.
		// dependencies as more --path elements.
		getLog().info("Command:" + command);
		try {
			Process p = new ProcessBuilder(command)
					.directory(new File("src/test/pony/" + project.getGroupId() + "/" + project.getArtifactId()))
					.inheritIO()
					.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new MojoExecutionException("Failed to clean", e);
		}
	}
}
// vi: sw=4 ts=4 sts=4 noet
