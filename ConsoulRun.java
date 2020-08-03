/**
 * Run is the class to run logical part of Insomnia Project
 */
public class ConsoulRun {
    
public static void main(String[] args) {
    CommandParser parser=new CommandParser();
    // System.out.println(makeArgstoCommand(args));    
    parser.getCommand(makeArgstoCommand(args),true);

    while (true) {
        parser.getCommand(null,true);
        
    }
}
    private static String makeArgstoCommand(String[] args) {
        String command="";
        if (args.length==0){
            return null;
        }
        for (String string : args) {
            command+="\n"+string;
        }
        

        return command;

    }
}