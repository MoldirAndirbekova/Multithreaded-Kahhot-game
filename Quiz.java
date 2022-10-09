import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {

    private String name;
    private ArrayList<Question> questions = new ArrayList<>();
    String[] option = new String[4];



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }
    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public static Quiz LoadFromFile(String path) throws FileNotFoundException {
        Quiz quiz = new Quiz();

        Scanner s = new Scanner(new File(path));
        while(s.hasNextLine()){
            String description = s.nextLine();
            if(description.contains("{blank}")){
                FillIn fillin = new FillIn();
                fillin.setDescription(description);
                fillin.setAnswer(s.nextLine());
                //questions.add(fillin);
                quiz.addQuestion(fillin);

            }
            else{
                Test test = new Test();
                test.setDescription(description);

                String[] options = new String[4];


                for (int i = 0; i < 4; i++) {
                    options[i] = s.nextLine();
                }

                test.setAnswer(options[0]);
                test.setOptions(options);
                //questions.add(test);
                quiz.addQuestion(test);

                s.nextLine();
            }
        }
        return quiz;
    }
    @Override
    public String toString() {
        return  "----------------------------------------------------\n" +
                "Welcome to \"" + getName() + "\" QUIZ!\n";
    }
    public String[] ret(int index){
        String[] options = new String[questions.size()];
        if(questions.get(index) instanceof Test) {
            options = ((Test) questions.get(index)).getOptions();
        }
        return options;

    }




}
