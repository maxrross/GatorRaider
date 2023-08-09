package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;

import java.util.List;

public final class StudentAttackerController implements AttackerController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int update(Game game,long timeDue)
	{
		int action = 1;
		Node closestPill;
		Node closestPP = null;

		List<Node> powerPills = game.getPowerPillList();
		List<Node> pills = game.getPillList();

		if (!powerPills.isEmpty()) {
			closestPP = game.getAttacker().getTargetNode(powerPills, true);
		}
		closestPill = game.getAttacker().getTargetNode(pills, true);

		//need to get distance of all defenders to see which one is the closest
		int min = 1000;
		Defender closestD= (Defender) game.getAttacker().getTargetActor(game.getDefenders(), true);
		//need to get distance from this defender to see
		min = game.getAttacker().getLocation().getPathDistance(closestD.getLocation());

		// in its own if statement if power pills available
		if (!powerPills.isEmpty()) {
			//if enemy is vulnerable, chase them
			if (closestD.isVulnerable()) {
				action = game.getAttacker().getNextDir(closestD.getLocation(), true);
			//if there are no vulnerable enemies, go chill by big pill until enemies get close
			}else if (game.getAttacker().getLocation().getPathDistance(closestPP) < 2) {
				action = game.getAttacker().getReverse();
				if (min < 6) {
					action = game.getAttacker().getNextDir(closestPP, true);
				}
			} else {
					action = game.getAttacker().getNextDir(closestPP, true);
			}
		}else {
			if (closestD.isVulnerable()) {
				action = game.getAttacker().getNextDir(closestD.getLocation(), true);
			} else if (min < 10) {
				action = game.getAttacker().getNextDir(closestD.getLocation(), false);
			} else {
				//if there are no power pills, eat closest pill
				action = game.getAttacker().getNextDir(closestPill, true);
			}
		}
		return action;
	}
}