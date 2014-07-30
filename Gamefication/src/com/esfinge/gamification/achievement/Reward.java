package com.esfinge.gamification.achievement;

public class Reward implements Achievement{
	
	private String name;
	private boolean used;
	
	public Reward(String name) {
		this.name = name;
		this.used = false;
	}
	
	public Reward(String name, boolean used) {
		this.name = name;
		this.used = used;
	}

	public String getName() {
		return name;
	}

	public boolean isUsed() {
		return used;
	}

	public void incrementAchievement(Achievement a) {
		
	try{
		if(!a.getName().equals(getName()) || !(a instanceof Reward))
			throw new RuntimeException("The achievement should be of the same type");
	}catch (RuntimeException e) {
			this.name = ((Reward)a).getName();
			this.used = ((Reward)a).isUsed();
		}

	}
	
	public void removeAchievement(Achievement r) {
		try{
			if(!r.getName().equals(getName()) || r instanceof Reward)
				throw new RuntimeException("The achievement should be of the same type");
		}catch (RuntimeException e) {
				this.used = ((Reward)r).isUsed() ? false : true;
			}
		
	}
	
	public String toString() {
		return "Reward Achieved - " + name + ": "+ used;
	}

}
