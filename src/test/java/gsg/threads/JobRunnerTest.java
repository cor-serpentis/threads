package gsg.threads;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author Ivan Panfilov, folremo@axmor.com
 * Created: 12.07.15 22:35
 */
public class JobRunnerTest {

	@Test
	public void testRunning() {
		final long millis = System.currentTimeMillis();
		final JobRunnerConfiguration configuration = new JobRunnerConfiguration();
		IJob job = new IJob() {
			@Override
			public void doJob(JobRunnerConfiguration c, JobRunnerData d) {
				c.setActive(false);
				if (System.currentTimeMillis() - millis > 5000L) {
					throw new RuntimeException("too much");
				}
			}
		};
		final JobRunner runner = new JobRunner(configuration, job);
		runner.run();
		assertEquals("expect run cycle only once", 1L, runner.getData().getFinishedTicksCount());
	}

	@Test
	public void testRunningSeveralTimes() {
		final long millis = System.currentTimeMillis();
		final JobRunnerConfiguration configuration = new JobRunnerConfiguration();
		final AtomicInteger count = new AtomicInteger(0);
		IJob job = new IJob() {
			@Override
			public void doJob(JobRunnerConfiguration c, JobRunnerData d) {
				if (count.getAndIncrement() >= 4) {
					c.setActive(false);
				}
				if (System.currentTimeMillis() - millis > 5000L) {
					throw new RuntimeException("too much");
				}
			}
		};
		final JobRunner runner = new JobRunner(configuration, job);
		runner.run();
		assertEquals("expect run cycle five times", 5L, count.get());
	}
}
