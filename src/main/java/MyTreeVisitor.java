import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.tool.Rule;

public class MyTreeVisitor extends MySqlParserBaseVisitor<String> {
    private Map<String, String> tablas;

    public MyTreeVisitor(Map<String, String> tablas){
        super();
        this.tablas=tablas;
    }

    @Override
    public String visit(ParseTree tree) {
        return tree.accept(this);
    }

    @Override
    public String visitChildren(RuleNode node) {
        String ret="";
        for (int i=0; i < node.getChildCount(); i++){
            ret+= node.getChild(i).accept(this);
        }
        return ret;
    }

    @Override
    public String visitTerminal(TerminalNode node) {
        String name = node.getText();
        String alias= tablas.get(name);
        if(alias!=null){
            return alias+"";
        }
        else{
            
            return name+" ";
        }
    }

    @Override
    public String visitFromClause(MySqlParser.FromClauseContext ctx) {
        MySqlParser.TableSourcesContext tableReferences = ctx.tableSources();
        MySqlParser.ExpressionContext where=ctx.whereExpr;
        String res="From ";
        for(int i=0; i< tableReferences.getChildCount(); i++){
          String name="";
          if(tableReferences.getChild(i).getChildCount()>0){
            if(tableReferences.getChild(i).getChild(0).getChildCount()>2){
              name = tableReferences.getChild(i).getChild(0).getChild(0).getText();
            }
            else{
              name = tableReferences.getChild(i).getText();
            }
          }
          else{
            name = tableReferences.getChild(i).getText();
          }
          String alias= tablas.get(name);
          if(alias!=null){
            res+=name+ " as "+alias;
          }
          else{
            res+=name+" ";
          }
        }
        res+=" where ";
        res+=visitChildren(where.getRuleContext());
        return res;
    }

    
    public String toString(MySqlParser.RootContext ctx){
        return ctx.accept(this);
    }

}
