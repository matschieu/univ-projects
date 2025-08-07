
package com.github.matschieu.game.characters;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;

import com.github.matschieu.game.actions.Action;
import com.github.matschieu.game.actions.Attack;
import com.github.matschieu.game.actions.DisplayMap;
import com.github.matschieu.game.actions.Interact;
import com.github.matschieu.game.actions.MakeWish;
import com.github.matschieu.game.actions.Move;
import com.github.matschieu.game.actions.Options;
import com.github.matschieu.game.actions.PickUp;
import com.github.matschieu.game.actions.Quit;
import com.github.matschieu.game.actions.Rest;
import com.github.matschieu.game.actions.Status;
import com.github.matschieu.game.actions.Use;
import com.github.matschieu.game.actions.View;
import com.github.matschieu.game.strategy.HumanStrategy;
import com.github.matschieu.game.strategy.Strategy;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Hero extends GameCharacter {

	/** the default energy of a hero */
	private static final int DEFAULT_ENERGY = 100;
	/** the default strength of a hero */
	private static final int DEFAULT_STRENGTH = 10;

	/**
	 * Constructs a new hero (human player)
	 */
	public Hero() {
		this(Localization.SINGLETON.getElement("HERO_NAME"));
	}

	/**
	 * Constructs a new hero (human player)
	 * @param name : the name of this hero
	 */
	public Hero(String name) {
		super(name, DEFAULT_ENERGY, DEFAULT_ENERGY, DEFAULT_STRENGTH, DEFAULT_STRENGTH, new HumanStrategy());
		this.actionsList = new LinkedList<>();
		this.actionsList.add(new Quit(this));
		this.actionsList.add(new Rest(this));
		this.actionsList.add(new Status(this));
		this.actionsList.add(new View(this));
		this.actionsList.add(new DisplayMap(this));
		this.actionsList.add(new Move(this));
		this.actionsList.add(new PickUp(this));
		this.actionsList.add(new Use(this));
		this.actionsList.add(new Interact(this));
		this.actionsList.add(new Attack(this));
		this.actionsList.add(new MakeWish(this));
		this.actionsList.add(new Options(this));
		this.checkActionsToAdd();
	}

	/**
	 * Initializes the hero with some values
	 * @param args : an array of parameters<br />
	 *		args[0]=class name<br />
	 *		args[1]=hero's name<br />
	 *		args[2]=hero's energy<br />
	 *		args[3]=hero's strength<br />
	 *		args[4]=hero's strategy<br />
	 *		args[x>4]=hero's actions
	 */
	@Override
	public void init(String[] args) {
		this.name = args[1];
		try {
			this.maxEnergy = this.energy = Integer.parseInt(args[2]);
			this.maxStrength = this.strength = Integer.parseInt(args[3]);
			this.strategy = (Strategy)Class.forName(args[4]).newInstance();
		}
		catch(final Exception e) {
			System.err.println(e.getMessage());
		}
		this.actionsList = new LinkedList<>();
		for(int i = 5; i < args.length; i++) {
			try {
				final Constructor<?> cst = Class.forName(args[i]).getConstructor(GameCharacter.class);
				this.actionsList.add((Action)cst.newInstance(this));
			}
			catch(final Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Checks if there are actions in the package game.actions to add to the list
	 */
	private void checkActionsToAdd() {
		final String[] classFiles = (new File(Thread.currentThread().getContextClassLoader().getResource("com/github/matschieu/game/actions").getFile())).list();
		final String classPackage = "com.github.matschieu.game.actions";
		final String ext = ".class";
		Class<?> actionClass = null;
		Class<?> heroActionClass = null;
		try {
			actionClass = Class.forName(classPackage + ".Action");
			heroActionClass = Class.forName(classPackage + ".HeroAction");
		}
		catch(final ClassNotFoundException e) {
			return;
		}
		for(int i = 0; i < classFiles.length; i++) {
			try {
				if (classFiles[i].matches("[a-zA-Z0-9]*" + ext)) {
					classFiles[i] = classFiles[i].substring(0, classFiles[i].length() - ext.length());
					final Class<?> cls = Class.forName(classPackage + "." + classFiles[i]);
					if (actionClass.isAssignableFrom(cls) && heroActionClass.isAssignableFrom(cls) && !cls.isInterface()) {
						final Constructor<?> cst = cls.getConstructor(GameCharacter.class);
						final Action act = (Action)cst.newInstance(this);
						boolean toAdd = true;
						final Iterator<Action> it = this.actionsList.iterator();
						while(it.hasNext()) {
							final Action act_tmp = it.next();
							if (act_tmp.getDescription().equals(act.getDescription())) {
								toAdd = false;
								break;
							}
						}
						if (toAdd) this.actionsList.add(act);
					}
				}
			}
			catch(final ClassNotFoundException e) { }
			catch(final Exception e) { }
		}
	}

	/**
	 * Checks if this character can be attacked
	 * @return true
	 */
	@Override
	public boolean canBeAttacked() {
		return true;
	}

	/**
	 * Actions this character can do on a tour
	 */
	@Override
	public void action() {
		System.out.println("* " + this.name);
		boolean finished = false;
		Action a = null;
		while(!finished) {
			System.out.println(Localization.SINGLETON.getElement("HERO_ACTION"));
			a = this.strategy.choiceAction(this.actionsList);
			if (a != null) {
				System.out.println(a.getDescription());
				finished = a.execute();
			}
		}
	}

	/**
	 * Returns a string symbole of this character
	 * @return String : the symbole of this character
	 */
	@Override
	public String getSymbole() {
		return Localization.SINGLETON.getElement("HERO_SYMBOLE");
	}

}
