package de.niklasfauth.litewave.analyze;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LiteWaveUtils {

	
	//peak detection code by tiraeth (https://gist.github.com/tiraeth/1306602)
	public static <U> List<Map<U, Double>> peakDetection(List<Double> values,
			Double delta, List<U> indices) {
		assert (indices != null);
		assert (values.size() != indices.size());

		Map<U, Double> maxima = new HashMap<U, Double>();
		Map<U, Double> minima = new HashMap<U, Double>();
		List<Map<U, Double>> results = new ArrayList<Map<U, Double>>();
		results.add(maxima);
		results.add(minima);

		Double maximum = 0.0;
		Double minimum = 0.0;
		U maximumPos = null;
		U minimumPos = null;

		boolean lookForMax = true;
		Integer pos = 0;
		for (Double value : values) {
			if (value > maximum || maximum == null) {
				maximum = value;
				maximumPos = indices.get(pos);
			}

			if (value < minimum || minimum == null) {
				minimum = value;
				minimumPos = indices.get(pos);
			}

			if (lookForMax) {
				if (value < maximum - delta) {
					maxima.put(maximumPos, value);
					minimum = value;
					minimumPos = indices.get(pos);
					lookForMax = false;
				}
			} else {
				if (value > minimum + delta) {
					minima.put(minimumPos, value);
					maximum = value;
					maximumPos = indices.get(pos);
					lookForMax = true;
				}
			}

			pos++;
		}

		return results;
	}

	public static List<Map<Integer, Double>> peakDetection(List<Double> values,
			Double delta) {
		List<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < values.size(); i++) {
			indices.add(i);
		}

		return LiteWaveUtils.peakDetection(values, delta, indices);
	}
}