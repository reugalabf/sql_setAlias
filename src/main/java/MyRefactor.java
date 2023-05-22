import java.util.Map;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.CharStreams;

public class MyRefactor {
    private MySqlLexer lexer;
    private MySqlParser parser;
    private Map<String,String> tablas;
    private MyTreeVisitor visitor;

    public MyRefactor(String sql, Map<String,String> tablas) throws ParseCancellationException{
        this.tablas=tablas;

        try{
            this.lexer=new MySqlLexer(CharStreams.fromString(sql));
            this.parser=new MySqlParser(new CommonTokenStream(this.lexer));
            //this.checkTablas();
        }
        catch(ParseCancellationException e){
            throw new ParseCancellationException();
        }

        this.visitor= new MyTreeVisitor(tablas);
    }

    public String visit(){
        return(this.visitor.visit(this.parser.selectStatement()));
    }

    private void checkTablas() throws Exception{

    }
}