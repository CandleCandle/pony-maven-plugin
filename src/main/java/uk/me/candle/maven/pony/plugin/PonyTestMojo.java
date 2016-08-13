package uk.me.candle.maven.pony.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name="test")
public class PonyTestMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter( defaultValue = "${localRepository}", readonly = true, required = true )
	protected ArtifactRepository localRepository;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		File target = new File(project.getBuild().getDirectory());
		getLog().info("cleaning " + target.getAbsolutePath());
		try {
			Path executable = target.toPath().resolve("test").resolve(project.getArtifactId());
			Process p = new ProcessBuilder(executable.toString())
					.redirectErrorStream(true)
					.inheritIO()
					.start();
			p.waitFor();
			switch (p.exitValue()) {
			case 0:
				return;
			default:
				throw new MojoFailureException("tests failed");
			}
		} catch (IOException | InterruptedException e) {
			throw new MojoExecutionException("Failed to clean", e);
		}
	}
}
// vi: sw=4 ts=4 sts=4 noet