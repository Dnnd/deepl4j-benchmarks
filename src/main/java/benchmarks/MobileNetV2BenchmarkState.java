package benchmarks;

import org.datavec.image.data.Image;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@State(Scope.Benchmark) public class MobileNetV2BenchmarkState {
    public static String MODEL_PATH = "mobilenetv2/mobilenetv2_imagenet.h5";
    public static String IMAGES_PATH = "mobilenetv2/images";
    public static int IMAGE_HEIGHT = 96;
    public static int IMAGE_WIDTH = 96;
    public static int IMAGE_CHANNELS = 3;
    public INDArray[] images;
    public int current;
    public INDArray currentImage;
    public ComputationGraph mobileNetV2;

    @Setup(Level.Trial) public void setUp()
            throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException,
            URISyntaxException {
        ComputationGraph dl4jModel = KerasModelImport.importKerasModelAndWeights(MODEL_PATH);

        var imreader = new NativeImageLoader(IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_CHANNELS);
        images = Files.walk(Paths.get(IMAGES_PATH)).map(Path::toFile).filter(File::isFile).map(f -> {
            try {
                return imreader.asImageMatrix(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).map(Image::getImage).map(image -> image.permute(0, 2, 3, 1)).toArray(INDArray[]::new);

        mobileNetV2 = dl4jModel;
    }

    @Setup(Level.Invocation) public void setUpInvocation() {
        this.currentImage = images[current++ % images.length];
    }
}
