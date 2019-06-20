package User;

public class User {

    private String nickname;
    private int score;

    /**
     * Constructor
     * @param nickname of the user
     */
    public User(String nickname){
        this.nickname = nickname;
        this.score = 0;
    }

    /**
     * Increase the user's score
     * @param amount
     */
    public void increaseScore(int amount){
        setScore(getScore()+amount);
    }

    /**
     * Decrease the user's score
     * @param amount
     */
    public void decreaseScore(int amount){
        setScore(getScore()-amount);
    }

    /**
     * Getter methods
     */
    public String getNickname(){
        return nickname;
    }

    public int getScore(){
        return score;
    }

    /**
     * Setter methods
     */
    public void setScore(int score){
        this.score = score;
    }
}
