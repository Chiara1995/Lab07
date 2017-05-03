package it.polito.tdp.dizionario.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.dizionario.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DizionarioController {
	
	private Model model;

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private TextArea txtResult;
	@FXML
	private TextField inputNumeroLettere;
	@FXML
	private TextField inputParola;
	@FXML
    private Button btnTrovaTuttiVicini;
	@FXML
	private Button btnGeneraGrafo;
	@FXML
	private Button btnTrovaVicini;
	@FXML
	private Button btnTrovaGradoMax;

	@FXML
	void doReset(ActionEvent event) {
		inputNumeroLettere.clear();
		inputParola.clear();
		txtResult.clear();
		this.btnTrovaVicini.setDisable(true);
		this.btnTrovaGradoMax.setDisable(true);
		this.inputParola.setDisable(true);
		this.inputNumeroLettere.setEditable(true);
		this.btnGeneraGrafo.setDisable(false);
		this.btnTrovaTuttiVicini.setDisable(true);
		return;
	}

	@FXML
	void doGeneraGrafo(ActionEvent event) {
		if(inputNumeroLettere.getText().equals("") || !inputNumeroLettere.getText().matches("[\\d]+")){
			this.txtResult.setText("Inserire un numero.");
			return;
		}		
		int numeroLettere=Integer.parseInt(inputNumeroLettere.getText());
		if(numeroLettere<=0){
			this.txtResult.setText("Errore: inserire un numero di lettere valido.");
			return;
		}
		try {
			model.createGraph(numeroLettere);
			txtResult.setText("Creato grafo!");	
		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}
		this.btnTrovaVicini.setDisable(false);
		this.btnTrovaGradoMax.setDisable(false);
		this.inputParola.setDisable(false);
		this.inputNumeroLettere.setEditable(false);
		this.btnGeneraGrafo.setDisable(true);
		this.btnTrovaTuttiVicini.setDisable(false);
		return;
	}

	@FXML
	void doTrovaGradoMax(ActionEvent event) {
	
		try {
			txtResult.setText(model.findMaxDegree());
			return;
		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}

	}

	@FXML
	void doTrovaVicini(ActionEvent event) {
		int numeroLettere=Integer.parseInt(inputNumeroLettere.getText());
		if(inputParola.getText().equals("") || !inputParola.getText().matches("[a-zA-Z]*")){
			this.txtResult.setText("Inserire una parola.");
			return;
		}
		String parola=inputParola.getText();
		if(parola.length()!=numeroLettere){
			this.txtResult.setText("Errore: parola con lunghezza diversa da quella specificata.");
			return;
		}
		if(!model.parolaPresente(parola)){
			this.txtResult.setText("Errore: parola non presente nel dizionario.");
			return;
		}
	
		try {
			String result="[";
			for(int i=0; i<model.displayNeighbours(parola).size(); i++){
				if(i==model.displayNeighbours(parola).size()-1)
					result+=model.displayNeighbours(parola).get(i)+"]";
				else
					result+=model.displayNeighbours(parola).get(i)+", ";
			}
			if(result.equals("[")){
				txtResult.setText("Nessuna parola direttamente connessa.");
				return;
			}
			else{
				txtResult.setText("Parole direttamente connesse: "+result);
				return;
			}
		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}
	}
	
	@FXML
    void doTrovaTuttiVicini(ActionEvent event) {
		int numeroLettere=Integer.parseInt(inputNumeroLettere.getText());
		if(inputParola.getText().equals("") || !inputParola.getText().matches("[a-zA-Z]*")){
			this.txtResult.setText("Inserire una parola.");
			return;
		}
		String parola=inputParola.getText();
		if(parola.length()!=numeroLettere){
			this.txtResult.setText("Errore: parola con lunghezza diversa da quella specificata.");
			return;
		}
		if(!model.parolaPresente(parola)){
			this.txtResult.setText("Errore: parola non presente nel dizionario.");
			return;
		}
	
		try {
			String result="[";
			for(int i=0; i<model.trovaTuttiVicini(parola).size(); i++){
				if(i==model.trovaTuttiVicini(parola).size()-1)
					result+=model.trovaTuttiVicini(parola).get(i)+"]";
				else
					result+=model.trovaTuttiVicini(parola).get(i)+", ";
			}
			if(result.equals("[")){
				txtResult.setText("Nessuna parola raggiungibile nel grafo a partire dal vertice selezionato.");
				return;
			}
			else{
				txtResult.setText("Parole raggiungibili nel grafo: "+result);
				return;
			}
		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}
    }

	@FXML
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert inputNumeroLettere != null : "fx:id=\"inputNumeroLettere\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert inputParola != null : "fx:id=\"inputParola\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnGeneraGrafo != null : "fx:id=\"btnGeneraGrafo\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnTrovaGradoMax != null : "fx:id=\"btnTrovaTutti\" was not injected: check your FXML file 'Dizionario.fxml'.";
	}

	public void setModel(Model model) {
		this.model=model;
	}
}