package runner;

import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.HotspotMemoryProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarksRunner {
    public static void main(String[] args) throws Exception {
        var opts = new OptionsBuilder().addProfiler(GCProfiler.class).build();
        new Runner(opts).run();
    }
}
