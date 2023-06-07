import java.util.Map;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.CharStreams;
import java.util.Set;
import java.util.HashSet;

public class MyRefactor {
    private MySqlLexer lexer;
    private MySqlParser parser;
    private Map<String,String> tablas;
    private MyTreeVisitor visitor;
    private MyErrorVisitor err;

    public MyRefactor(String sql, Map<String,String> tablas) throws ParseCancellationException, TableNameException, TableAliasException, SqlStringException{
        this.tablas=tablas;

        try{
            this.lexer=new MySqlLexer(CharStreams.fromString(sql));
            this.parser=new MySqlParser(new CommonTokenStream(this.lexer));
            this.err=new MyErrorVisitor(tablas);
            this.checkTablas();
        }
        catch(ParseCancellationException e){
            throw new ParseCancellationException();
        }
        catch(TableNameException e){
            throw new TableNameException(e.getMessage());
        }
        catch(TableAliasException e){
            throw new TableAliasException(e.getMessage());
        }
        catch(SqlStringException e){
            throw new SqlStringException(e.getMessage());
        }
        this.visitor= new MyTreeVisitor(tablas);
    }

    public String visit(){
        return(this.visitor.visit(this.parser.selectStatement()));
    }

    private void checkTablas() throws TableNameException, TableAliasException, SqlStringException{ 
        String error=this.err.visit(this.parser.selectStatement());
        if(error!=null){
          throw new SqlStringException(error);
        }
        Set<String> aliasSeen = new HashSet<String>();
        for(String tabla: this.tablas.keySet()){
            if(this.tablas.values().contains(tabla))
                throw new TableNameException("Error: Alias "+tabla+" es el nombre de otra tabla");
        }
        for(String alias: this.tablas.values()){
            if(aliasSeen.contains(alias))
                throw new TableAliasException("Error: Alias "+alias+" se repite");
            else
                aliasSeen.add(alias);
        }
        
        
    }
}
