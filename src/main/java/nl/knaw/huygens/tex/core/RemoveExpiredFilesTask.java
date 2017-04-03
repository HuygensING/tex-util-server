package nl.knaw.huygens.tex.core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.util.concurrent.AbstractScheduledService;

public class RemoveExpiredFilesTask extends AbstractScheduledService {
  private final Logger LOG = LoggerFactory.getLogger(getClass());
  private final Path tmpDir;

  public RemoveExpiredFilesTask(File tmpDir) {
    this.tmpDir = tmpDir.toPath();
  }

  @Override
  protected void runOneIteration() throws Exception {
    Files.newDirectoryStream(tmpDir).forEach(path -> {
      File file = path.toFile();
      Instant lastModified = Instant.ofEpochMilli(file.lastModified());
      Instant expirationTime = lastModified.plus(1, ChronoUnit.HOURS);
      if (Instant.now().isAfter(expirationTime)) {
        LOG.info("removing expired file {}", file);
        file.delete();
      }
    });
  }

  @Override
  protected Scheduler scheduler() {
    return AbstractScheduledService.Scheduler.newFixedRateSchedule(0, 15, TimeUnit.MINUTES);
  }

}
