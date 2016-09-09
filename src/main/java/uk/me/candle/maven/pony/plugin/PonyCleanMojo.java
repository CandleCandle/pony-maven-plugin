package uk.me.candle.maven.pony.plugin;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name="clean")
public class PonyCleanMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		File target = new File(project.getBuild().getDirectory());
		getLog().info("cleaning " + target.getAbsolutePath());
		try { // XXX use a pure java way to clean.
			Process p = new ProcessBuilder("rm", "-rvf", target.getAbsolutePath())
					.redirectErrorStream(true)
					.inheritIO()
					.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new MojoExecutionException("Failed to clean", e);
		}
	}
}
// vim: sw=4 ts=4 sts=4 noet