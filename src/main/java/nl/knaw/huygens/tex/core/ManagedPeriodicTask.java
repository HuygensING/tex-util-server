package nl.knaw.huygens.tex.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.util.concurrent.AbstractScheduledService;
import io.dropwizard.lifecycle.Managed;

public class ManagedPeriodicTask implements Managed {
  private final Logger LOG = LoggerFactory.getLogger(ManagedPeriodicTask.class);
  private final AbstractScheduledService periodicTask;

  public ManagedPeriodicTask(AbstractScheduledService periodicTask) {
    this.periodicTask = periodicTask;
  }

  @Override
  public void start() throws Exception {
    periodicTask.startAsync().awaitRunning();
  }

  @Override
  public void stop() throws Exception {
    periodicTask.stopAsync().awaitTerminated();
  }
}