package model;

import java.util.HashMap;
import java.util.Observable;

import javax.swing.ImageIcon;

public class GameModel extends Observable{
	private Integer maxScore;
	private ImageIcon scoreImage;
	private HashMap <String, Integer> scoreModules;
	
	public GameModel(Integer maxScore){
		scoreModules = new HashMap<String, Integer>();
		scoreModules.put("main", 0);
		this.maxScore = maxScore;
		scoreImage=getScoreImage();
	}
	public GameModel(Model model){
		scoreModules = new HashMap<String, Integer>();
		scoreModules.put("main", 0);
		
		// Kompiler
		maxScore=1;
		
		// Reihenfolgentests
		maxScore += model.getGroupMatrix().size();
		
		// Falls kein Reihenfolgentest
		if(maxScore==1)
			maxScore=2;
		
		// JUnitTests
		if(model.getJUnitCode()!=null && !model.getJUnitCode().trim().isEmpty())
			maxScore+=1;
		scoreImage = getScoreImage();
	}
	
	public void score(String module, int points){
		if(scoreModules.containsKey(module))
			scoreModules.put(module, scoreModules.get(module)+points);
		else
			scoreModules.put(module, points);
		
		scoreModules.put("main", scoreModules.get("main")+points);
		
		scoreImage = getScoreImage();
		setChanged();
		notifyObservers("score");
	}
	public void score(String module){
		if(scoreModules.get(module)==null || scoreModules.get(module)==0)
		score(module, 1);
	}
	public void loose(String module, int points){
		if(scoreModules.containsKey(module))
			scoreModules.put(module, scoreModules.get(module)-points);
		else
			scoreModules.put(module, points);
		
		scoreModules.put("main", scoreModules.get("main")-points);
		
		scoreImage = getScoreImage();
		setChanged();
		notifyObservers("score");

	}
	public void loose(String module){
		if(scoreModules.get(module)==null || scoreModules.get(module)==0)
		loose(module, 1);
	}
	public Integer getScore(String module){
		if(scoreModules.containsValue(module))
			return scoreModules.get(module);
		else 
			return null;
	}
	public ImageIcon getScoreImage(){
		ImageIcon[] images = { new ImageIcon("rsc/icon/Smiley/face-crying.png"), 
				new ImageIcon("rsc/icon/Smiley/face-sad.png"),
				new ImageIcon("rsc/icon/Smiley/face-plain.png"),
				new ImageIcon("rsc/icon/Smiley/face-smile.png"),
				new ImageIcon("rsc/icon/Smiley/face-grin.png"),
				new ImageIcon("rsc/icon/Smiley/face-glasses.png"),
				new ImageIcon("rsc/icon/Smiley/face-wink.png")};
		if(scoreModules.get("main")<-1)
			return images[0];
		else if(scoreModules.get("main")<0)
			return images[1];
		else if(scoreModules.get("main")<1)
			return images[2];
		else if(scoreModules.get("main")<2)
			return images[3];
		else if(scoreModules.get("main")<3)
			return images[4];
		else 
			return images[5];
	}
	
	public void reset(){
		for(String module : scoreModules.keySet())
			scoreModules.put(module, 0);
		
		scoreImage = getScoreImage();
		setChanged();
		notifyObservers("score");
	}
}
