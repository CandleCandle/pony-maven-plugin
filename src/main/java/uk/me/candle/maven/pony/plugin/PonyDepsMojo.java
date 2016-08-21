package uk.me.candle.maven.pony.plugin;

import java.io.File;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name="pony-unpack-deps")
public class PonyDepsMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter( defaultValue = "${localRepository}", readonly = true, required = true )
	protected ArtifactRepository localRepository;

	@Component
	protected ArtifactHandler artifactHandler;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		File target = new File(project.getBuild().getDirectory());
//		ArtifactHandler artifactHandler = project.getArtifact().getArtifactHandler();
		getLog().info("handler: " + artifactHandler);
		for (Dependency dep : project.getDependencies()) {
			//dep.
			Artifact art1 = localRepository.find(new DefaultArtifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), null, "pony", null, artifactHandler));
			getLog().debug("found artifact: " + art1);
			String art2 = localRepository.pathOf(art1);
			getLog().debug("found artifact: " + art1 + " at path " + art2);
		}

	}
}
// vi: sw=4 ts=4 sts=4 noet