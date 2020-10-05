package controller.sceneControllers;

import controller.PrimaryController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import service.FXMLService;

public class DrawerController extends Controller {
	@FXML
	private void initialize() {	}
	
	@FXML
	private void startGame(ActionEvent e) {	
		PrimaryController.app.addNewScene(FXMLService.FXMLNames.QUESTIONBOARD);	
	}
	
	@FXML
	private void viewScore() {
		PrimaryController.app.addNewScene(FXMLService.FXMLNames.WINNINGS);	
	}
	
	@FXML
	private void practice() {
		PrimaryController.app.addNewScene(FXMLService.FXMLNames.PRACTICESELECTOR);	
	}
	
	@FXML
	public void reset() {
		HomeController hmCtrl = (HomeController) PrimaryController.getInstance().currentController;
		hmCtrl.closeMenu();
		try {
			PrimaryController.app.showResetAlert();
		} catch (Exception e) {	}
		
	}
	
	@FXML 
	private void quit() {
		Platform.exit();
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTextIndividual() {
		// TODO Auto-generated method stub
		
	}
	
}