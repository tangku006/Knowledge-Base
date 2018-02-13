package lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A simple KNN classifier
 *
 * @author Fabian Suchanek
 *
 */
public class KNN<Label> {
    /** Holds the feature vectors of the examples */
    protected List<double[]> exampleFeatures = new ArrayList<>();
    /** Holds the labels of the examples */
    protected List<Label> exampleLabels = new ArrayList<>();
    /** Well, k */
    protected final int k;
    /** Length of the vectors*/
    protected int vectorLength = -1;

    /** Creates a KNN classifier */
    public KNN(int k) {
        this.k = k;
    }

    /** Adds a training example */
    public void addTrainingExample(Label c, double... features) {
        if (vectorLength == -1) vectorLength = features.length;
        if (vectorLength != features.length)
            throw new RuntimeException("Traing example vectors must have the same length, " + vectorLength);
        exampleFeatures.add(features);
        exampleLabels.add(c);
    }

    /** Computes the distance between two feature vectors */
    protected double distance(double[] features1, double[] features2) {
        double result = 0;
        for (int i = 0; i < features1.length; i++) {
            result += (features1[i] - features2[i]) * (features1[i] - features2[i]);
        }
        return (Math.sqrt(result));
    }

    /**
     * Holds the distance to the closest point after the last call to classify()
     */
    protected double closestDistance = Double.MAX_VALUE;

    /**
     * Returns the distance to the closest point after the last call to
     * classify()
     */
    public double closestDistance() {
        return (closestDistance);
    }

    /** Proposes a label for a feature vector */
    public Label classify(double... features) {
        if (vectorLength != features.length)
            throw new RuntimeException("Traing example vectors must have the same length, " + vectorLength);
        @SuppressWarnings("unchecked")
        Label[] bestK = (Label[]) new Object[k];
        double[] bestKscores = new double[k];
        Arrays.fill(bestKscores, Double.MAX_VALUE);
        for (int i = 0; i < exampleFeatures.size(); i++) {
            double score = distance(features, exampleFeatures.get(i));
            if (score > bestKscores[k - 1]) continue;
            int pos = Arrays.binarySearch(bestKscores, score);
            if (pos < 0) pos = -pos - 1;
            System.arraycopy(bestKscores, pos, bestKscores, pos + 1, k - pos - 1);
            System.arraycopy(bestK, pos, bestK, pos + 1, k - pos - 1);
            bestK[pos] = exampleLabels.get(i);
            bestKscores[pos] = score;
        }
        closestDistance = Arrays.stream(bestKscores).min().getAsDouble();
        if (closestDistance == Double.MAX_VALUE) return (null);
        return (Arrays.stream(bestK).filter(s -> s != null)
                .collect(Collectors.groupingBy(a -> a, Collectors.counting())).entrySet().stream()
                .max(Comparator.comparing(Entry::getValue)).get().getKey());
    }

    /** Test method */
    public static void main(String[] args) {
        KNN<Nerc.Class> knn = new KNN<Nerc.Class>(3);
        knn.addTrainingExample(Nerc.Class.ARTIFACT, 1, 1);
        knn.addTrainingExample(Nerc.Class.ARTIFACT, 1, 2);
        knn.addTrainingExample(Nerc.Class.ARTIFACT, 1, 3);
        knn.addTrainingExample(Nerc.Class.EVENT, 2, 0);
        knn.addTrainingExample(Nerc.Class.EVENT, 2, 1);
        knn.addTrainingExample(Nerc.Class.EVENT, 3, 0);
        knn.addTrainingExample(Nerc.Class.EVENT, 3, 1);
        knn.addTrainingExample(Nerc.Class.GEO, 1, 4);
        knn.addTrainingExample(Nerc.Class.GEO, 4, 4);
        knn.addTrainingExample(Nerc.Class.GEO, 5, 4);
        System.out.println(knn.classify(1, 10));
        System.out.println(knn.closestDistance());
    }
}