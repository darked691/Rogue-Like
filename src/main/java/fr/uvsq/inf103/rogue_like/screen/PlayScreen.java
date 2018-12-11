package fr.uvsq.inf103.rogue_like.screen;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import fr.uvsq.inf103.rogue_like.sauvegarde.*;
import fr.uvsq.inf103.rogue_like.world.*;
import fr.uvsq.inf103.rogue_like.creature.*;

import java.util.ArrayList;

/**
 * Classe PlayScreen qui s'affichera quand l'utilisateur sera en train de jouer.
 */
public class PlayScreen implements Screen {
	
	
	/**
	 * Map du monde Rogue Like.
	 */
	private World world;
	
	/**
	 * Difficulte du jeu choisie par le joueur.
	 */
	private Difficulte difficulte;
	
	/**
	 * PJ representant le joueur en train de jouer.
	 */
	private Joueur joueur;
	
	/**
	 * Liste de PNJ (pacifiques ou non).
	 */
	private ArrayList <PNJ> listePNJ;
	
	/**
	 * Level de la partie.
	 */
	private int niveau;
	
	/**
	 * Longueur de la fenetre de jeu.
	 */
	private int screenWidth;
	
	/**
	 * Largeur de la fenetre de jeu.
	 */
	private int screenHeight;

	/**
	 * Message a afficher sur l'ecran de jeu de facon temporaire.
	 */
	private String messageTemporaire;
	
	/**
	 * Constructeur de PlayScreen qui permet de generer la map, le joueur et les PNJ.
	 * @param arme du joueur.
	 * @param sort du joueur.
	 * @param difficulte du joueur. 
	 */
	public PlayScreen(int level, Arme arme, Sort sort, Difficulte difficulte, int vie, int argent){
		screenWidth = 80;
		screenHeight = 21;
		niveau=level;
		this.difficulte=difficulte;

		createWorld();
		joueur=new Joueur(world,arme, sort, vie, argent);

		createPNJ(world, difficulte);
	}

	/**
	 * Methode permettant de savoir si le PNJ a faire spawner est spawnable en (x,y).
	 * @param x coordonnees en abscisse du futur PNJ.
	 * @param y coordonnees en ordonnee du futur PNJ.
	 * @param rang dans la liste du PNJ a faire spawner.
	 * @return true si le PNJ peut etre en (x,y) et false sinon.
	 */
	private boolean testSpawnPossible(int x, int y, int rang){
		if((this.joueur.x==x)&&(this.joueur.y==y)) return false;
		else{
			for(int i=0; i<rang; i++){
				if((this.listePNJ.get(i).x==x)&&(this.listePNJ.get(i).y==y)) return false;
			}
		}
		return true;
	}

	/**
	 * Methode qui permet de faire spawner un certain nombre de PNJ sur la map.
	 */
	private void spawnPNJ(){
		for(int i=0; i<this.listePNJ.size(); i++){
			int x;
			int y;

			do {
				x = (int)(Math.random() * world.getWidth());
				y = (int)(Math.random() * world.getHeight());
			}
			while ((!world.getElement(x,y).isGround())||(!testSpawnPossible(x,y,i)));

			this.listePNJ.get(i).x = x;
			this.listePNJ.get(i).y = y;
		}
	}

	/**
	 * Methode qui permet de creer un certain nombre de PNJ en fonction de la difficulte du jeu.
	 * @param world monde sur lequel il faut faire spawner les PNJ.
	 * @param difficulte du jeu.
	 */
	//a mettre dans world ?
	private void createPNJ(World world, Difficulte difficulte){
		this.listePNJ=new ArrayList<PNJ>();
		int nb_pnj_agressifs;
		if(difficulte==Difficulte.FACILE) nb_pnj_agressifs=5;
		else if(difficulte==Difficulte.INTERMEDIAIRE) nb_pnj_agressifs=7;
		else if(difficulte==Difficulte.DIFFICILE) nb_pnj_agressifs=10;
		else if(difficulte==Difficulte.HARDCORE) nb_pnj_agressifs=20;
		else { //DIFFICULTE PROBLEME
			nb_pnj_agressifs=0;
			System.out.println(difficulte.getNom()+"EXCEPTION A LANCER");
		}

		// ajout des PNJ agressifs
		EnumPNJ pnj_cree; int type_pnj; PNJ pnj;
		for(int i=0;i<nb_pnj_agressifs;i++){
			type_pnj=(int)(Math.random() * EnumPNJ.NB_ENUM_PNJ.ordinal()); // ordinal recupere le nombre d'enum
			if(type_pnj==0) type_pnj=1;
			pnj_cree=EnumPNJ.values()[type_pnj];
			this.listePNJ.add(new PNJ(world, pnj_cree));
		}
		// ajout du PNJ villageois necessaire au niveau
		this.listePNJ.add(new PNJ(world,EnumPNJ.VILLAGEOIS));
		spawnPNJ();
	}


	/**
	 * Methode privee permettant de generer le monde.
	 */
	private void createWorld(){
		world = new WorldBuilder(90, 32)
					.makeCaves()
					.build();
	}
	
	/**
	 * Methode de point de vue de la camera sur l'axe de la longueur.
	 * @return position de la camera en longueur.
	 */
	public int getScrollX() { return Math.max(0, Math.min(joueur.x - screenWidth / 2, world.getWidth() - screenWidth)); }
	
	/**
	 * Methode de point de vue de la camera sur l'axe de la largeur.
	 * @return position de la camera en largeur.
	 */
	public int getScrollY() { return Math.max(0, Math.min(joueur.y - screenHeight / 2, world.getHeight() - screenHeight)); }
	
	/**
     * Methode qui affiche les interactions possibles avec l'utilisateur.
     * @param terminal represente l'ecran du jeu.
     */
	//@Override
	public void displayOutput(AsciiPanel terminal) {
		
		int left = getScrollX();
		int top = getScrollY(); 
		
		displayTilesCreatures(terminal, left, top);
		
		terminal.write(joueur.getCaractere(), joueur.x - left, joueur.y - top, joueur.getColor());

		terminal.write((char)3, 1, 0, AsciiPanel.brightRed);
		terminal.write(""+joueur.getVie()+"/10", 3, 0);
		terminal.write(joueur.getArme().getNom()+ " - " + joueur.getSort().getNom(), 0, 21);
		terminal.write("$" , 0, 22, AsciiPanel.brightGreen);
		terminal.write(""+this.joueur.getArgent() , 2, 22);
		if(joueur.getClef()==true) terminal.write((char)213, 0, 23, AsciiPanel.brightYellow);
		terminal.writeCenter("Level "+this.niveau, 21, AsciiPanel.blue);
		if(this.messageTemporaire!=null) terminal.writeCenter(this.messageTemporaire, 22, AsciiPanel.white);
		this.messageTemporaire=null;
	}

	/**
	 * Affichage des élements. 
	 * @param terminal ou les elements sont affiches;
	 * @param left longueur de la fenetre.
	 * @param top hauteur de la fenetre.
	 */
	private void displayTilesCreatures(AsciiPanel terminal, int left, int top) {
		PNJ pnj; int xx; int yy;
		for (int x = 0; x < screenWidth; x++){
			for (int y = 0; y < screenHeight; y++){
				int wx = x + left;
				int wy = y + top;

				terminal.write(world.getCaractere(wx, wy), x, y, world.getColor(wx, wy));
				for(int ii=0;ii<this.listePNJ.size();ii++){
					pnj=this.listePNJ.get(ii);
					if(((x+left)==pnj.x)&&((y+top)==pnj.y)){ //mettre x+left et y+top
						terminal.write(pnj.getClasse().getCaractere(), x, y, pnj.getClasse().getColor());
					}
				}
			}
		}
	}

	/**
	 * Methode permettant de savoir si le joueur peut changer de niveau.
	 * La condition est le fait d'avoir la clef pour ouvrir la porte.
	 * @return true si il peut changer de niveau et false sinon.
	 */
	// mettre dans world?
	private boolean testeChangerNiveau(){
		if(joueur.getClef()){
			boolean test=false;
			if(world.getElement(joueur.x+1,joueur.y)==Element.DOOR){
				test=true;
			}
			else if(world.getElement(joueur.x,joueur.y+1)==Element.DOOR){
				test=true;
			}
			else if(world.getElement(joueur.x,joueur.y-1)==Element.DOOR){
				test=true;
			}
			else if(world.getElement(joueur.x-1,joueur.y)==Element.DOOR){
				test=true;
			}
			if(test==true){
				joueur.laisserClef();
				return true;
			}

		}
		return false;
	}

	/**
	 * Methode permettant
	 * @param listePNJ liste des PNJ a faire se deplacer.
	 * @param joueur joueur du jeu.
	 */
	private void actionPNJ(ArrayList<PNJ> listePNJ, Joueur joueur){
		//deplacement PNJ
		PNJ pnj;
		for(int i=0; i<listePNJ.size(); i++) {
			pnj = listePNJ.get(i);
			pnj.seDeplacer(joueur, listePNJ);
		}
	}

	
	/**
     * Methode qui permet a l'utilisateur d'interagir avec l'utilisateur.
     * @param key touche que l'utilisateur tape sur le clavier.
     * @return nouvel ecran a afficher apres l'interaction avec l'utilisateur.
     */
	//@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()){
			case KeyEvent.VK_LEFT: joueur.playerMoveBy(-1, 0, listePNJ); break;
			case KeyEvent.VK_RIGHT: joueur.playerMoveBy( 1, 0, listePNJ); break;
			case KeyEvent.VK_UP: joueur.playerMoveBy( 0,-1, listePNJ); break;
			case KeyEvent.VK_DOWN: joueur.playerMoveBy( 0, 1, listePNJ); break;
			case KeyEvent.VK_R: joueur.ramasserObjet(world); break; //mettre dans la classe Player
			case KeyEvent.VK_O:
				if(testeChangerNiveau()) return new PlayScreen(niveau+1, joueur.getArme(), joueur.getSort(), this.difficulte, joueur.getVie(), joueur.getArgent());
			case KeyEvent.VK_P:
				messageTemporaire=joueur.faireEchangeVillageois(this.listePNJ); break;
			case KeyEvent.VK_A:
				messageTemporaire=joueur.attaquerPNJ(this.listePNJ); break;
			case KeyEvent.VK_S: new Sauvegarde(world); break;
		}
		actionPNJ(this.listePNJ, joueur);
		if(joueur.getVie()==0) return new LoseScreen();

		return this;
	}
    public World getWorld() {
    	return world;
    }
}
