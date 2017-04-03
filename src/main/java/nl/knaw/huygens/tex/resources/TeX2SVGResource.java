package nl.knaw.huygens.tex.resources;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;
import nl.knaw.huygens.tex.TeXUtilServerConfiguration;

@Path("2svg")
public class TeX2SVGResource {

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
      FileUtils.write(texFile, tex, Charset.forName("UTF-8"));
      convert2svg(uuid);

      URI svgURI = URI.create(config.getBaseURI() + "/svg/" + uuid + ".svg");
      return Response.created(svgURI).build();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private void convert2svg(UUID uuid) {
    ProcessBuilder processBuilder = new ProcessBuilder(config.getTeX2SVGCommand(), uuid.toString());
    try {
      processBuilder.start();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }
}
