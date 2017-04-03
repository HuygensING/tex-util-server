package nl.knaw.huygens.tex;

import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codahale.metrics.health.HealthCheck.Result;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.knaw.huygens.tex.core.ManagedPeriodicTask;
import nl.knaw.huygens.tex.core.RemoveExpiredFilesTask;
import nl.knaw.huygens.tex.health.CommandHealthCheck;
import nl.knaw.huygens.tex.health.TmpDirHealthCheck;
import nl.knaw.huygens.tex.resources.SVGResource;
import nl.knaw.huygens.tex.resources.TeX2SVGResource;

public class TeXUtilServerApplication extends Application<TeXUtilServerConfiguration> {
  Logger LOG = LoggerFactory.getLogger(getClass());

  public static void main(final String[] args) throws Exception {
    new TeXUtilServerApplication().run(args);
  }

  @Override
  public String getName() {
    return "TeXUtilServer";
  }

  @Override
  public void initialize(final Bootstrap<TeXUtilServerConfiguration> bootstrap) {
    // Enable variable substitution with environment variables
    bootstrap.setConfigurationSourceProvider(//
        new SubstitutingSourceProvider(//
            bootstrap.getConfigurationSourceProvider(), //
            new EnvironmentVariableSubstitutor()));
  }

  @Override
  public void run(final TeXUtilServerConfiguration configuration, final Environment environment) {
    environment.jersey().register(new TeX2SVGResource(configuration));
    environment.jersey().register(new SVGResource(configuration.getTempDir()));
    environment.healthChecks().register("command", new CommandHealthCheck(configuration.getTeX2SVGCommand()));
    environment.healthChecks().register("tmpdir", new TmpDirHealthCheck(configuration.getTempDir()));

    SortedMap<String, Result> results = environment.healthChecks().runHealthChecks();
    AtomicBoolean healthy = new AtomicBoolean(true);
    LOG.info("Healthchecks:");
    results.forEach((name, result) -> {
      LOG.info("{}: {}, message='{}'", name, result.isHealthy() ? "healthy" : "unhealthy", result.getMessage());
      healthy.set(healthy.get() && result.isHealthy());
    });
    if (!healthy.get()) {
      throw new RuntimeException("Failing health check(s)");
    }

    final RemoveExpiredFilesTask periodicTask = new RemoveExpiredFilesTask(configuration.getTempDir());
    final Managed managedImplementer = new ManagedPeriodicTask(periodicTask);
    environment.lifecycle().manage(managedImplementer);
  }

}
