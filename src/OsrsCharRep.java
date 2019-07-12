import java.util.HashMap;
import java.util.Map;

 class OsrsCharRep {
    private Map<Skill, Integer> skillStats;

    void setSkillStat(Skill s,int i){
        skillStats.put(s,i);
    }

    Map<Skill,Integer> calcCheapesWayToGoal(double targetCombatLevel){
        Map<Skill,Integer> changesReq = new HashMap<>();

        if (targetCombatLevel <= UtilMath.calcCombatLevel(this))
            return changesReq;



        while (targetCombatLevel >= UtilMath.calcCombatLevel(this)) {
            Skill changedSkill = getMostEfficientLevel();

            if (changedSkill == Skill.MAXSTATERROR){
                return changesReq;
            }
            changesReq.put(changedSkill,changesReq.getOrDefault(changedSkill,0)+1);


        }

        return changesReq;

    }

    Integer getStat(Skill s) {
        return skillStats.get(s);
    }

    OsrsCharRep(){
        skillStats = new HashMap<>();
    }

    private void incrementSkillStat(Skill s) {
        if (s != Skill.MAXSTATERROR)
            skillStats.put(s, skillStats.get(s) + 1);
    }
    private void reduceSkillStat(Skill s) {
        skillStats.put(s, skillStats.get(s) - 1);
    }


    private Skill getMostEfficientLevel(){

        Skill effSkill = Skill.MAXSTATERROR;
        double bestCBLPerExp = 0;
        for (Map.Entry<Skill,Integer> e: skillStats.entrySet()){
            if(e.getKey()!=Skill.MAXSTATERROR) {


                Skill skill = e.getKey();
                Integer level = e.getValue();

                double xpToNextLevel = UtilMath.xpDiff(level, level + 1);
                double preCBL = UtilMath.calcCombatLevel(this);
                incrementSkillStat(skill);
                double postCBL = UtilMath.calcCombatLevel(this);
                reduceSkillStat(skill);

                double cBLdiff = postCBL - preCBL;

                double cBLPerExp = cBLdiff / xpToNextLevel;

                if (level >= 99)
                    cBLPerExp = 0;

                if (bestCBLPerExp < cBLPerExp) {
                    bestCBLPerExp = cBLPerExp;
                    effSkill = skill;
                }
            }
        }
        incrementSkillStat(effSkill);
        return effSkill;
    }

}
