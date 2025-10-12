package SpringBot;

import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import Graphe.Graphe;
import Graphe.Resultat;
import SpringBot.GrapheDeSortie.AreteDeSortie;
import SpringBot.GrapheDeSortie.GrapheDeSortie;

@RestController 
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api") 
public class Controller {


	
	
	@GetMapping("/ping")
    public ResponseEntity ping() {
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
	
    /**
     * Renvoie le graphe de départ
     * @return une ResponseEntity contenant le statut http ainsi que le graphe sous le format d'un json
     * @throws JsonProcessingException
     */
    @GetMapping("/getDefaultGraphe")
    public ResponseEntity getDefaultValue() throws JsonProcessingException {
    	AreteDeSortie.setCompteur(1); // en cas de nouvel appel, remise à 1
    	Map<String, Object> response = new HashMap<>();
    	response.put("graphe", GrapheDeSortie.getDefaultGraphe());
    	return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(response);
    }
    
    /**
     * Renvoie le résultat d'un parcours en largeur du graphe de départ en partant depuis Rennes
     * @return une ResponseEntity contenant le statut http ainsi que le graphe BFS et le chemin sous le format d'un json ou l'exception en cas d'erreur
     * @throws JsonProcessingException
     */
    @GetMapping("/getBFS")
    public ResponseEntity getBFS() throws JsonProcessingException {
    	AreteDeSortie.setCompteur(1);
    	Map<String, Object> response = new HashMap<>();
    	
    	try {
    		Graphe graphe = Graphe.getDefaultGraphe();
        	Resultat reponse = graphe.getBFS("Rennes");
        	
        	response.put("chemin", reponse.getChemin());
        	response.put("graphe", reponse.getGraphe().getGrapheSortie());
        	
        	return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(response);
    	}
    	
    	catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(e);
    	}
    	
    }
    
    /**
     * Renvoie le résultat d'un parcours en profondeur du graphe de départ en partant depuis Rennes
     * @return une ResponseEntity contenant le statut http ainsi que le graphe DFS et le chemin sous le format d'un json ou l'exception en cas d'erreur
     * @throws JsonProcessingException
     */
    @GetMapping("/getDFS")
    public ResponseEntity getDFS() throws JsonProcessingException {
    	AreteDeSortie.setCompteur(1);
    	Map<String, Object> response = new HashMap<>();
    	
    	try {
    		Graphe graphe = Graphe.getDefaultGraphe();
        	Resultat reponse = graphe.getDFS("Rennes");
        	
        	response.put("chemin", reponse.getChemin());
        	response.put("graphe", reponse.getGraphe().getGrapheSortie());
        	
        	return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(response);
    	}
    	
    	catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(e);
    	}
    	
    }

    
}
