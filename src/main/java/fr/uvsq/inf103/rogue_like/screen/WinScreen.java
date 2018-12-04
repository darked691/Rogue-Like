package fr.uvsq.inf103.rogue_like.screen;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import fr.uvsq.inf103.rogue_like.world.Arme;
import fr.uvsq.inf103.rogue_like.world.Sort;

/**
 * Classe WinScreen qui s'affichera quand l'utilisateur aura demarrer le jeu.
 */
public class WinScreen implements Screen {

    /**
     * Methode qui affiche les interactions possibles avec l'utilisateur.
     * @param terminal represente l'ecran du jeu.
     */
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You won.", 1, 1);
        terminal.writeCenter("-- press [enter] to restart --", 22);
    }

    /**
     * Methode qui permet a l'utilisateur d'interagir avec l'utilisateur.
     * @param key touche que l'utilisateur tape sur le clavier.
     * @return nouvel ecran a afficher apres l'interaction avec l'utilisateur.
     */
    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen(Arme.BATTE_BASEBALL, Sort.INVISIBILITE) : this;
    }
}
