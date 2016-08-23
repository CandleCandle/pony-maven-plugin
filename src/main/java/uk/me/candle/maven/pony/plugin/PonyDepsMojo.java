package uk.me.candle.maven.pony.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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
		for (Dependency dep : project.getDependencies()) {
			//dep.
			Artifact art1 = localRepository.find(new DefaultArtifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), null, "pony", null, artifactHandler));
			String art2 = localRepository.pathOf(art1);
			String basePath = localRepository.getBasedir();
			getLog().info("found artifact: " + art1 + " at path " + basePath + " -- " + art2);
			try {
				ZipFile zf = new ZipFile(basePath + File.separator + art2 + ".zip");
				new File(basePath + File.separator + art2).mkdirs();
				for (ZipEntry entry : Collections.list(zf.entries())) {
					if (entry.isDirectory()) continue;
					Path destination = Paths.get(basePath, art2, entry.getName());
					getLog().info("Unpacking entry " + entry.getName() + " to " + destination);
					destination.getParent().toFile().mkdirs();
					Files.copy(
							zf.getInputStream(entry),
							destination,
							StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException ex) {
				throw new MojoFailureException(ex.getMessage(), ex);
			}
		}

	}
}
// vi: sw=4 ts=4 sts=4 noet