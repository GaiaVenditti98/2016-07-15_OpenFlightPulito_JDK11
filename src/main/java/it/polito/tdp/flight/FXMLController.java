package it.polito.tdp.flight;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.Model;
import it.polito.tdp.flight.model.PasseggeriNegliAeroporti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller del turno A --> modificare per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtDistanzaInput;

    @FXML
    private TextField txtPasseggeriInput;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Integer km;
    	try {
    		km= Integer.parseInt(this.txtDistanzaInput.getText());
    		this.model.creaGrafo(km);
    		txtResult.appendText("creato grafo con "+this.model.numVertici()+" vertici e "+this.model.numArchi()+" archi\n");
    		if(this.model.isConnect()) {
    			txtResult.appendText("da ogni aeroporto è possibile raggiungere ogni altro aeroporto\n");
    		}else
    			txtResult.appendText("il grafo non è connesso\n");
    		txtResult.appendText("L'aeroporto più lontano da Fiumicino è: "+this.model.piuLontano()+"\n");
    		
    	}catch(NumberFormatException e) {
    		txtResult.appendText("la distanza deve essere un numero intero!!\n");
    		return;
    	}

    }

    @FXML
    void doSimula(ActionEvent event) {
    	Integer k;
    	try {
    		k=Integer.parseInt(this.txtPasseggeriInput.getText());
    		for(PasseggeriNegliAeroporti p:this.model.simula(k)) {
    			txtResult.appendText(p+"\n");
    		}
    		
    	}catch (NumberFormatException e) {
			txtResult.appendText("Devi inserire un numero intero!\n");
			return;
		}

    }

    @FXML
    void initialize() {
        assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Flight.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
