package nl.knaw.huygens.tex.health;

import java.io.File;
import com.codahale.metrics.health.HealthCheck;

public class TmpDirHealthCheck extends HealthCheck {
  private File tmpDir;

  public TmpDirHealthCheck(File tmpDir) {
    this.tmpDir = tmpDir;
  }

  @Override
  protected Result check() throws Exception {
    String path = tmpDir.getAbsolutePath();
    if (!tmpDir.exists()) {
      return Result.unhealthy(path + " not found.");
    }
    if (!tmpDir.isDirectory()) {
      return Result.unhealthy(path + " is not a directory.");
    }
    return Result.healthy(path + " checked out ok.");
  }

}
