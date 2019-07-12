

class UtilMath {
    private  static double xp(double level){
        double sum = 0;
        for(double i=1; i < level; i++){
            sum += Math.floor(i+300d*Math.pow(2d,i/7d));
        }

        return Math.floor(.25*sum);
    }
    static double xpDiff(int from, int to){
        return xp(to) - xp(from);
    }

    static double calcCombatLevel(OsrsCharRep c){
        /*
         * the result of prayer should be floored, as it is in the calculations of the game.
         * however this would cause every other prayerpoint to not touch the
         * combat level. To help the program quantify the value of an increase in prayer, it
         * is not floored but instead taken for the half increment value that it, in the bigger
         * picture repesent
         */
        double base = .25*(c.getStat(Skill.defence)+c.getStat(Skill.hitpoints)+(double)c.getStat(Skill.prayer)/2);
        double melee = 0.325*(c.getStat(Skill.attack)+c.getStat(Skill.strength));
        double range = 0.325*(Math.floor(c.getStat(Skill.ranged)/2)+c.getStat(Skill.ranged));
        double mage = 0.325*(Math.floor(c.getStat(Skill.magic)/2)+c.getStat(Skill.magic));

        return base+Math.max(Math.max(melee,range),mage);
    }

}
