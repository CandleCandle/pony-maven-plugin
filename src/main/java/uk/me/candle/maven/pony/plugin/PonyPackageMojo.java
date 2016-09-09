package uk.me.candle.maven.pony.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name="package")
public class PonyPackageMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		File output = new File("target/" + project.getArtifactId() + "-" + project.getVersion() + ".zip"); // XXX find the configurered 'target' directory from the project
		getLog().info("Packaging " + project.getArtifactId() + " into " + output.toString());
		output.delete();
		output.getParentFile().mkdirs();
		try (
				FileOutputStream out = new FileOutputStream(output);
				ZipOutputStream zos = new ZipOutputStream(out)
				) {
			final Path srcBase = Paths.get("src", "main", "pony"); // XXX find the correct source dir from the project
			Files.walkFileTree(srcBase, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					getLog().debug("Adding " + file.toString() + " to archive");
					zos.putNextEntry(new ZipEntry(srcBase.relativize(file).toString()));
					Files.copy(file, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException ioe) {
			throw new MojoExecutionException("Failed to clean", ioe);
		}
		project.getArtifact().setFile(output);
	}
}
// vim: sw=4 ts=4 sts=4 noet