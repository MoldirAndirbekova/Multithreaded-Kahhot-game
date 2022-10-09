public class FillIn extends Question{
    public String toString(){
        return getDescription().replace("{blank}","_____");
    }
}
