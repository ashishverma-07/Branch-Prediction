package tournamentPredictor;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BranchPrediction {
	private FileProcessor fInp, fOut, fStat;

	private HashMap<Integer, Predictor> globalMap = new LinkedHashMap<Integer, Predictor>();
	private HashMap<Integer, Predictor> localMap = new LinkedHashMap<Integer, Predictor>();
	private HashMap<Integer, Predictor> selctorMap = new LinkedHashMap<Integer, Predictor>();

	public int g_index = 0;
	public int next_g_index = 0;
	public String read, write;
	private int local_predicted = 0;
	private int global_predicted = 0;
	private int tournament_predicted = 0;

	public BranchPrediction(FileProcessor fpInp, FileProcessor fpOut,
			FileProcessor fpStat) {
		fInp = fpInp;
		fOut = fpOut;
		fStat = fpStat;
	}

	public void fileOperation() {
		while ((read = fInp.readLineFromFile()) != null) {
			write = prediction(read);
			fOut.writeLineToFile(write);
		}
		fOut.close();
		writestatistics();
	}

	private void writestatistics() {
		String statistics = " Local Correct Prediction from 10000: "
				+ local_predicted + "\n Global Correct Prediction from 10000: "
				+ global_predicted
				+ "\n Tournament Correct Prediction from 10000: "
				+ tournament_predicted;
		fStat.writeLineToFile(statistics);
		fStat.close();
	}

	private String prediction(String read) {
		int current_inst = Integer.parseInt(Character.toString(read.charAt(0)));
		String actual_prediction = Character.toString(read.charAt(1));
		int next_inst = Integer.parseInt(Character.toString(read.charAt(2)));

		String local_prediction = localPredictor(current_inst,
				actual_prediction, next_inst);
		String global_prediction = globalPredictor(current_inst,
				actual_prediction, next_inst);
		String selector_prediction = selector(current_inst, actual_prediction,
				next_inst);

		String final_prediction;
		if (selector_prediction.equalsIgnoreCase("l")) {
			final_prediction = local_prediction;
		} else {
			final_prediction = global_prediction;
		}
		String output = current_inst + local_prediction + global_prediction
				+ selector_prediction + final_prediction + actual_prediction;

		if (local_prediction.equalsIgnoreCase(actual_prediction)) {
			local_predicted++;
		}
		if (global_prediction.equalsIgnoreCase(actual_prediction)) {
			global_predicted++;
		}
		if (final_prediction.equalsIgnoreCase(actual_prediction)) {
			tournament_predicted++;
		}

		return output;
	}

	private String localPredictor(int current_inst, String actual_prediction,
			int next_inst) {
		String local_pred = "n";
		if (localMap.containsKey(current_inst)) {
			Predictor localP = localMap.get(current_inst);
			localP.setOld_value(localP.getNew_value());
			if (localP.getOld_value() > 1) {
				local_pred = "t";
				localP.setPredictedValue(local_pred);
			} else {
				local_pred = "n";
				localP.setPredictedValue(local_pred);
			}
			if (actual_prediction.equalsIgnoreCase("t")) {
				if (localP.getNew_value() < 3) {
					localP.setNew_value(localP.getNew_value() + 1);
				}
			} else {
				if (localP.getNew_value() > 0) {
					localP.setNew_value(localP.getNew_value() - 1);
				}
			}
		} else {
			int new_branch = 0;
			if (actual_prediction.equalsIgnoreCase("t")) {
				new_branch += 1;
			}
			localMap.put(current_inst, new Predictor(current_inst, 0,
					new_branch, local_pred));
		}
		return local_pred;
	}

	private String globalPredictor(int current_inst, String actual_prediction,
			int next_inst) {
		g_index = next_g_index;
		String global_pred = "n";

		if (globalMap.containsKey(g_index)) {
			Predictor globalP = globalMap.get(g_index);
			globalP.setOld_value(globalP.getNew_value());
			if (globalP.getOld_value() > 1) {
				global_pred = "t";
				globalP.setPredictedValue(global_pred);
			} else {
				global_pred = "n";
				globalP.setPredictedValue(global_pred);
			}
			if (actual_prediction.equalsIgnoreCase("t")) {
				if (globalP.getNew_value() < 3) {
					globalP.setNew_value(globalP.getNew_value() + 1);
				}
			} else {
				if (globalP.getNew_value() > 0) {
					globalP.setNew_value(globalP.getNew_value() - 1);
				}
			}
		} else {
			int new_branch = 0;
			if (actual_prediction.equalsIgnoreCase("t")) {
				new_branch += 1;
			}
			globalMap.put(g_index, new Predictor(g_index, 0, new_branch,
					global_pred));
		}

		if (actual_prediction.equalsIgnoreCase("t")) {
			next_g_index = g_index << 1;
			next_g_index += 1;
		} else if (actual_prediction.equalsIgnoreCase("n")) {
			next_g_index = g_index << 1;
		}
		if (next_g_index > 63) {
			next_g_index = (next_g_index & 63);
		}
		return global_pred;

	}

	private String selector(int current_inst, String actual_prediction,
			int next_inst) {
		String selector_pred = "l";
		if (selctorMap.containsKey(current_inst)) {
			Predictor selectorP = selctorMap.get(current_inst);
			selectorP.setOld_value(selectorP.getNew_value());

			if (!localMap
					.get(current_inst)
					.getPredictedValue()
					.equalsIgnoreCase(
							globalMap.get(g_index).getPredictedValue())) {
				if (globalMap.get(g_index).getPredictedValue()
						.equalsIgnoreCase(actual_prediction)) {
					if (selectorP.getNew_value() < 3) {
						selectorP.setNew_value(selectorP.getNew_value() + 1);

					}
				} else {
					if (selectorP.getNew_value() > 0) {
						selectorP.setNew_value(selectorP.getNew_value() - 1);
					}
				}
			}
			if (selectorP.getOld_value() > 1) {
				selector_pred = "g";
				selectorP.setPredictedValue(selector_pred);
			} else {
				selector_pred = "l";
				selectorP.setPredictedValue(selector_pred);
			}
		} else {
			selctorMap.put(current_inst, new Predictor(current_inst, 0, 0,
					selector_pred));
		}
		return selector_pred;
	}
}
