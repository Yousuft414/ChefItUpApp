package project.group3tztechcorp.chefitupapp;

public class UserInformation {
    String username, fullName;
    int level, experience, rewards, recipesCompleted, achievementsCompleted;

    public UserInformation() {
    }

    public UserInformation(String username, String fullName, int level, int experience, int rewards, int recipesCompleted, int achievementsCompleted) {
        this.username = username;
        this.fullName = fullName;
        this.level = level;
        this.experience = experience;
        this.rewards = rewards;
        this.recipesCompleted = recipesCompleted;
        this.achievementsCompleted = achievementsCompleted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getRewards() {
        return rewards;
    }

    public void setRewards(int rewards) {
        this.rewards = rewards;
    }

    public int getRecipesCompleted() {
        return recipesCompleted;
    }

    public void setRecipesCompleted(int recipesCompleted) {
        this.recipesCompleted = recipesCompleted;
    }

    public int getAchievementsCompleted() {
        return achievementsCompleted;
    }

    public void setAchievementsCompleted(int achievementsCompleted) {
        this.achievementsCompleted = achievementsCompleted;
    }

    public void checkLevel(){
        if (this.experience <= 300){
            this.level = 1;
        } else if (this.experience > 300 && this.experience <= 700){
            this.level = 2;
        } else if (this.experience > 700 && this.experience <= 1300){
            this.level = 3;
        } else if (this.experience > 1300 && this.experience <= 2100){
            this.level = 4;
        } else if (this.experience > 2100){
            this.level = 5;
        }
    }

    public void increaseExp(int exp){
        this.experience = this.experience + exp;
    }
}
