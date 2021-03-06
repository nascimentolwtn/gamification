package com.esfinge.gamefication.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.esfinge.gamefication.mechanics.FakeUser;
import com.esfinge.gamification.achievement.Achievement;
import com.esfinge.gamification.achievement.Point;
import com.esfinge.gamification.achievement.Ranking;
import com.esfinge.gamification.achievement.Reward;
import com.esfinge.gamification.achievement.Trophy;
import com.esfinge.gamification.mechanics.GameMemoryStorage;
import com.esfinge.gamification.proxy.GameInvoker;
import com.esfinge.gamification.proxy.GameProxy;
import com.esfinge.gamification.user.UserStorage;

public class TestDynamicProxy {
	
	GameMemoryStorage gs;
	ITestPointAnn p;
	ITestRankingAnn r;
	ITestRewardAnn rw;
	ITestTrophiesAnn t;
	
	@Before
	public void setupGame(){
		UserStorage.setUserID("Spider");
		p = (ITestPointAnn) GameProxy.createProxy(new TestPointAnnotation());
		r = (ITestRankingAnn) GameProxy.createProxy(new TestRankingAnnotation());
		rw = (ITestRewardAnn) GameProxy.createProxy(new TestRewardAnnotation());
		t = (ITestTrophiesAnn) GameProxy.createProxy(new TestTrophiesAnnotation());
		gs = new GameMemoryStorage();
		GameInvoker gi = GameInvoker.getInstance();
		gi.setGame(gs);
	}
	

	@Test
	public void testAddPoints() {
		p.doSomething();
		p.doSomething();
		p.doSomething();

		Achievement ach = gs.getAchievement("Spider", "GOLD");
		assertEquals(new Integer(3000), ((Point) ach).getQuantity());
	}
	
	@Test
	public void testInvocationWithException() {
		p.doSomething();
		try {
			p.doWrong();
			fail();
		} catch (Exception e) {
			Achievement ach = gs.getAchievement("Spider", "GOLD");
			assertEquals(new Integer(1000), ((Point) ach).getQuantity());
		}
	}
	
	@Test
	public void testRemovePoints() {
		p.doSomething();
		p.doRemoveSomething();

		Achievement ach = gs.getAchievement("Spider", "GOLD");
		assertEquals(new Integer(500), ((Point) ach).getQuantity());
	}
	
	@Test
	public void testInvocationWithExceptionToRemove() {
		p.doSomething();
		try {
			p.doRemoveWrong();
			fail();
		} catch (Exception e) {
			Achievement ach = gs.getAchievement("Spider", "GOLD");
			assertEquals(new Integer(1000), ((Point) ach).getQuantity());
		}
	}
	
	@Test
	public void addPointsToParam(){
		p.niceComment("OK! Thank you!", "Thor");
		Achievement ach = gs.getAchievement("Thor", "SILVER");
		assertEquals(new Integer(100), ((Point) ach).getQuantity());
	}
	
	@Test
	public void addPointsToParamProperty(){
		p.niceComment(new Comment(new FakeUser("Thor"), "OK! Thank you!"));
		Achievement ach = gs.getAchievement("Thor", "SILVER");
		assertEquals(new Integer(300), ((Point) ach).getQuantity());
	}
	
	@Test
	public void testAddRanking() {
		r.doSomething();
		Achievement ach = gs.getAchievement("Spider", "Noob");
		//casting 
		assertEquals(1, gs.getAchievements("Spider").size());
		assertEquals("level 1", ((Ranking) ach).getLevel());
	}
	
	@Test
	public void testRemoveRanking() {
		r.doSomething();
		r.doRemoveSomething();
		Achievement ach = gs.getAchievement("Spider", "Noob");
		assertNull(ach);
		assertEquals(0, gs.getAchievements("Spider").size());
	}
	
	@Test
	public void testAddReward() {
		rw.doSomething();
		Achievement ach = gs.getAchievement("Spider", "lunch");
		assertEquals(1, gs.getAchievements("Spider").size());
		assertFalse(((Reward) ach).isUsed());	
	}
	
	@Test
	public void testAddSameReward() {
		rw.doSomething();
		rw.doSomething();
		Achievement ach = gs.getAchievement("Spider", "lunch");
		assertEquals(1, gs.getAchievements("Spider").size());
		assertFalse(((Reward) ach).isUsed());	
	}
	
	@Test
	public void testAddTwoReward() {
		rw.doSomething();
		rw.doSomething2();
		Achievement ach = gs.getAchievement("Spider", "lunch");
		assertEquals(2, gs.getAchievements("Spider").size());
		assertFalse(((Reward) ach).isUsed());	
	}
	
	@Test
	public void testRemoveReward() {
		rw.doSomething();
		rw.doRemoveSomething();
		Achievement ach = gs.getAchievement("Spider", "lunch");
		assertEquals(1, gs.getAchievements("Spider").size());
		assertTrue(((Reward) ach).isUsed());	
	}
	
	@Test
	public void testAddTrophies() {
		t.doSomething();
		Achievement ach = gs.getAchievement("Spider", "champion");
		assertEquals(1, gs.getAchievements("Spider").size());
		assertEquals("champion", ((Trophy) ach).getName());
	}
	
	@Test
	public void testAddSameTrophies() {
		t.doSomething();
		t.doSomething();
		Achievement ach = gs.getAchievement("Spider", "champion");
		assertEquals(1, gs.getAchievements("Spider").size());
		assertEquals("champion", ((Trophy) ach).getName());
	}
	
	@Test
	public void testAddTwoDiferentesTrophies() {
		t.doSomething();
		t.doSomething2();
		Achievement ach = gs.getAchievement("Spider", "champion");
		assertEquals(2, gs.getAchievements("Spider").size());
		assertEquals("champion", ((Trophy) ach).getName());
	}
	
	@Test
	public void testRemoveTrophies() {
		t.doSomething();
		t.doRemoveSomething();
		Achievement ach = gs.getAchievement("Spider", "champion");
		assertNull(ach);
		assertEquals(0, gs.getAchievements("Spider").size());
	}
	

	
	// usar reward
	//remover ranking
	//remover reward
	

}