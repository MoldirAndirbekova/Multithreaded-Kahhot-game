import java.util.ArrayList;
import java.util.Random;

public class Test extends Question {
    String[] options = new String[4];
    int numOfOptions;
    ArrayList<String> labels = new ArrayList<String>(4);
       
    public void setLabels() {

        this.labels = labels;

    }

    Random random = new Random();

    public Test(){

    }
    public void setOptions(String[] options){
        ArrayList<String> inList = new ArrayList<>();
        for(int i = options.length - 1; i > 0; i--){
            int j = random.nextInt(i+1);
            String temp = options[i];
            options[i] = options[j];
            options[j] = temp;

            
        }
        
        for(int i = 0; i < 4;i++){
            this.options[i] = options[i];
        }
    }
    public String getOptionsAt(int index){
        return options[index];
    }
    public String toString(){
        String rdtest = getDescription() + "\n";
        for(int i = 0 ; i < 4; i++){
            rdtest += getOptionsAt(i) + "\n";
        }
        return rdtest;
    }
    
    
}
