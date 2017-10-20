package tournamentPredictor;

public class Predictor {
	private int index;
	private int old_value;
	private int new_value;
	private String predictedValue;
	
	public int getPosition() {
		return index;
	}
	public void setPosition(int position) {
		this.index = position;
	}
	public int getOld_value() {
		return old_value;
	}
	public void setOld_value(int old_value) {
		this.old_value = old_value;
	}
	public int getNew_value() {
		return new_value;
	}
	public void setNew_value(int new_value) {
		this.new_value = new_value;
	}
	public String getPredictedValue() {
		return predictedValue;
	}
	public void setPredictedValue(String predictedValue) {
		this.predictedValue = predictedValue;
	}
	public Predictor(int position, int old_value, int new_value,String predictedValue) {
		this.index = position;
		this.old_value = old_value;
		this.new_value = new_value;
		this.predictedValue = predictedValue;
	}
	
	
	
}
