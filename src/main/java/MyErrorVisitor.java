import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.RuleNode;

public class MyErrorVisitor extends MySqlParserBaseVisitor<String>{
  
  private Map<String, String> tablas;

  public MyErrorVisitor(Map<String, String> tablas){
    super();
    this.tablas=tablas;
  } 

  @Override 
  public String visit(ParseTree tree){
    return tree.accept(this);
  }

  public String visitFromClause(MySqlParser.FromClauseContext ctx){
    MySqlParser.TableSourcesContext tableReferences = ctx.tableSources();
    for(int i=0; i< tableReferences.getChildCount(); i++){
      String alias="";
      if(tableReferences.getChild(i).getChildCount()>0){
        if(tableReferences.getChild(i).getChild(0).getChildCount()>2){
          alias = tableReferences.getChild(i).getChild(0).getChild(2).getText();
          if(this.checkAlias(alias)){
            return "Error: Alias "+alias+" se repite";
          }
          if(this.checkName(alias)){
            return "Error: Alias "+alias+" es igual a nombre de tabla";
          }
        }
      }
    }
    return null;
  } 

  private boolean checkAlias(String alias){
    return(this.tablas.values().contains(alias));
  }

  private boolean checkName(String name){
    return(this.tablas.keySet().contains(name));
  }

}
