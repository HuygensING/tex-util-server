package nl.knaw.huygens.tex.resources;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.UUID;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nl.knaw.huygens.tex.TeXUtilServerConfiguration;

@Path("2svg")
public class TeX2SVGResource {
  Logger LOG = LoggerFactory.getLogger(getClass());

  private static final String SVG = ".svg";
  private static final String OUT = ".out";
  private static final String ERR = ".err";
  private static final Charset UTF8 = Charset.forName("UTF-8");
  private TeXUtilServerConfiguration config;

  public TeX2SVGResource(TeXUtilServerConfiguration config) {
    this.config = config;
  }

  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  public Response postTeX(String tex) {
    UUID uuid = UUID.randomUUID();

    File tmpDir = config.getTempDir();
    File texFile = new File(tmpDir, uuid + ".tex");
    try {
      FileUtils.write(texFile, tex, UTF8);
      int returnCode = convert2svg(uuid);
      LOG.info("returnCode={}",returnCode);

      File svgFile = new File(tmpDir, uuid + SVG);
      if (!svgFile.exists()) {
        String out = fileContent(tmpDir, uuid, OUT);
        String err = fileContent(tmpDir, uuid, ERR);
        StringBuilder message = new StringBuilder("SVG could not be generated:\n")//
            .append("\n## stdout:\n\n")//
            .append(out)//
            .append("\n## stderr:\n\n")//
            .append(err)//
            .append("\n");
        throw new BadRequestException(message.toString());
      }

      URI svgURI = URI.create(config.getBaseURI() + "/svg/" + uuid + SVG);
      return Response.created(svgURI).build();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private String fileContent(File tmpDir, UUID uuid, String ext) throws IOException {
    return FileUtils.readFileToString(new File(tmpDir, uuid + ext), UTF8);
  }

  private int convert2svg(UUID uuid) {
    File tmp = config.getTempDir();
    ProcessBuilder processBuilder = new ProcessBuilder(config.getTeX2SVGCommand(), tmp.getAbsolutePath().toString(), uuid.toString())//
        .redirectError(new File(tmp, uuid + ERR))//
        .redirectOutput(new File(tmp, uuid + OUT))//
        ;
    try {
      Process process = processBuilder.start();
      process.waitFor();
      return process.exitValue();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }
}
