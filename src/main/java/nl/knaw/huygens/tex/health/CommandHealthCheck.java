package nl.knaw.huygens.tex.health;

import java.io.File;
import com.codahale.metrics.health.HealthCheck;

public class CommandHealthCheck extends HealthCheck {

  private String command;

  public CommandHealthCheck(String commandPath) {
    this.command = commandPath;
  }

  @Override
  protected Result check() throws Exception {
    File commandFile = new File(command);
    if (!commandFile.exists()) {
      return Result.unhealthy(command + " not found.");
    }
    if (!commandFile.canExecute()) {
      return Result.unhealthy(command + " not executable.");
    }
    return Result.healthy(command + " is executable.");
  }

}
