package benchmarks;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

public class Deepl4jBenchmark {

    @Benchmark @BenchmarkMode(Mode.Throughput) public void testMobileNet(MobileNetV2BenchmarkState state, Blackhole bh) {
        bh.consume(state.mobileNetV2.output(state.currentImage));
    }
}
