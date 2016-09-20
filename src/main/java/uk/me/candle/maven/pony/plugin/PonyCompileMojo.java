package uk.me.candle.maven.pony.plugin;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name="compile", requiresDependencyResolution=ResolutionScope.COMPILE_PLUS_RUNTIME)
public class PonyCompileMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter( defaultValue = "${localRepository}", readonly = true, required = true )
	protected ArtifactRepository localRepository;

	@Component
	protected ArtifactHandler artifactHandler;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		DependencyHandler dependencyHandler = new DependencyHandler(project, localRepository, artifactHandler);

		getLog().info("compiling " + project.getArtifactId());
		File target = new File(project.getBuild().getDirectory());
		target.toPath().resolve("build").toFile().mkdirs();
		//--path "${basedir}/src/main/pony/"
		List<String> command = Lists.newArrayList("ponyc",
					"--output", target.toPath().resolve("build").toString()
		); // XXX get the src directory from the project.
		// dependencies as more --path elements.
		for (String s : dependencyHandler.getPathElements("compile", "provided", "runtime")) {
			command.add("--path");
			command.add(s);
		}
		getLog().info("Command:" + command);
		try {
			Process p = new ProcessBuilder(command)
					.directory(new File("src/main/pony/" + project.getGroupId() + "/" + project.getArtifactId()))
					.inheritIO()
					.start();
			p.waitFor();
			switch (p.exitValue()) {
			case 0:
				return;
			default:
				throw new MojoFailureException("compile failed");
			}
		} catch (IOException | InterruptedException e) {
			throw new MojoExecutionException("Failed to clean", e);
		}
	}
}
// vim: sw=4 ts=4 sts=4 noet