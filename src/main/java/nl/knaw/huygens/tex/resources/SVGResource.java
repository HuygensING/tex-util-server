package nl.knaw.huygens.tex.resources;

import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("svg")
public class SVGResource {
  private File tmpDir;

  public SVGResource(File tmpDir) {
    this.tmpDir = tmpDir;
  }

  @GET
  @Path("{name}.svg")
  public Response getSVG(@PathParam("name") String name) {
    File svg = new File(tmpDir, name + ".svg");
    if (!svg.exists()) {
      throw new NotFoundException();
    }
    return Response.ok(svg).type("image/svg+xml").build();
  }
}
