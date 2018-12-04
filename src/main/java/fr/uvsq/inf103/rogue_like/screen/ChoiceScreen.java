package fr.uvsq.inf103.rogue_like.screen;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import fr.uvsq.inf103.rogue_like.world.Difficulte;
import fr.uvsq.inf103.rogue_like.world.Sort;
import fr.uvsq.inf103.rogue_like.world.Arme;

/**
 * Classe StartScreen qui s'affichera quand l'utilisateur aura demarrer le jeu.
 */
public class ChoiceScreen implements Screen {
	
	private Difficulte difficulte;
	private Arme arme;
	private Sort sort;
	
	public ChoiceScreen (Difficulte difficulte, Arme arme, Sort sort){
		this.difficulte=difficulte;
		this.arme=arme;
		this.sort=sort;
	}

    /**
     * Methode qui affiche les interactions possibles avec l'utilisateur.
     * @param terminal represente l'ecran du jeu.
     */
    public void displayOutput(AsciiPanel terminal) {
        terminal.writeCenter("Choix de la partie Rogue Like", 1);
        terminal.writeCenter("Choix selectionnes : ", 2);
        terminal.writeCenter(difficulte.getNom() + " - " +arme.getNom() + " - "+ sort.getNom(), 3, AsciiPanel.brightRed);
        terminal.writeCenter("-- Difficulte de la partie --", 10);
        terminal.writeCenter("-- [A] FACILE - [B] INTERMEDIAIRE - [C] DIFFICILE - [D] HARDCORE --", 11);
        terminal.writeCenter("-- Choix de l'arme --", 13);
        terminal.writeCenter("-- [E] AUCUNE - [F] COUTEAU - [G] EPEE - [H] BATTE DE BASEBALL --", 14);
        terminal.writeCenter("-- Choix du sort --", 16);
        terminal.writeCenter("-- [I] AUCUN SORT - [J] INVISIBILITE - [K] FORCE --", 17);
        terminal.writeCenter("-- Appuie sur [ENTREE] pour demarrer la partie --", 19);

    }

    /**
     * Methode qui permet a l'utilisateur d'interagir avec l'utilisateur.
     * @param key touche que l'utilisateur tape sur le clavier.
     * @return nouvel ecran a afficher apres l'interaction avec l'utilisateur.
     */
    public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()){
            case KeyEvent.VK_A: this.difficulte=Difficulte.FACILE; break;
            case KeyEvent.VK_B: this.difficulte=Difficulte.INTERMEDIAIRE; break;
            case KeyEvent.VK_C: this.difficulte=Difficulte.DIFFICILE; break;
            case KeyEvent.VK_D: this.difficulte=Difficulte.HARDCORE; break;
            case KeyEvent.VK_E: this.arme=Arme.AUCUNE_ARME; break;
            case KeyEvent.VK_F: this.arme=Arme.COUTEAU; break;
            case KeyEvent.VK_G: this.arme=Arme.EPEE; break;
            case KeyEvent.VK_H: this.arme=Arme.BATTE_BASEBALL; break;
            case KeyEvent.VK_I: this.sort=Sort.AUCUN_SORT; break;
            case KeyEvent.VK_J: this.sort=Sort.INVISIBILITE; break;
            case KeyEvent.VK_K: this.sort=Sort.FORCE; break;
            case KeyEvent.VK_ENTER: return new PlayScreen(arme, sort);
        }
        return this;
    }
}
