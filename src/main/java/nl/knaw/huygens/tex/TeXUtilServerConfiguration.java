package nl.knaw.huygens.tex;

import java.io.File;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.Files;
import io.dropwizard.Configuration;

public class TeXUtilServerConfiguration extends Configuration {

  private File tempDir;

  public TeXUtilServerConfiguration() {
    super();
    tempDir = Files.createTempDir();
  }

  @NotEmpty
  private String baseURI="http://localhost:8080";

  @NotEmpty
  private String tex2svgCommand;

  @JsonProperty
  public void setBaseURI(String baseURI) {
    this.baseURI = baseURI.replaceFirst("/$", "");
  }

  @JsonProperty
  public String getBaseURI() {
    return baseURI;
  }

  @JsonProperty
  public void setTex2svgCommand(String tex2svgCommand) {
    this.tex2svgCommand = tex2svgCommand;
  }

  @JsonProperty
  public String getTeX2SVGCommand() {
    return tex2svgCommand;
  }

  public File getTempDir() {
    return tempDir;
  }
}
