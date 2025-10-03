/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

/**
 *
 * @author dilro
 */
public class EvolutionManager 
{
    //score for evolution stage 1,2(>=500),3(>=1000)
    private int stageFromScore(int score)
    {
        if(score >= 1000) return 3;
        if(score >= 500) return 2;
        return 1;
    }
    
    private void playEvolutionSequence(Pokemon mon, int targetStage) throws InterruptedException
    {
        System.out.println("What? " + mon.getName() + " is evolving!");
        Thread.sleep(900);
        System.out.print("."); Thread.sleep(600);
        System.out.print("."); Thread.sleep(600);
        System.out.println("."); Thread.sleep(600);
        System.out.println("Congratulations! Your " + mon.getName() + " evolved to Stage " + targetStage + "!");
    }
}
