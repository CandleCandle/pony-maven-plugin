package uk.me.candle.maven.pony.plugin;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

public class DependencyHandler {

	private final MavenProject project;
	private final ArtifactRepository localRepository;
	private final ArtifactHandler artifactHandler;

	public DependencyHandler(MavenProject project, ArtifactRepository localRepository, ArtifactHandler artifactHandler) {
		this.project = project;
		this.localRepository = localRepository;
		this.artifactHandler = artifactHandler;
	}

	List<String> getPathElements(String... scopes) {
		List<String> result = new ArrayList<>();
		List<String> scopeList = Arrays.asList(scopes);
		for (Dependency dep : project.getDependencies()) {
			if (!scopeList.contains(dep.getScope())) continue;

			Artifact art1 = localRepository.find(new DefaultArtifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), null, "pony", null, artifactHandler));
			String art2 = localRepository.pathOf(art1);
			String basePath = localRepository.getBasedir();
			result.add(Paths.get(basePath, art2).toString());
		}
		return result;
	}
}
