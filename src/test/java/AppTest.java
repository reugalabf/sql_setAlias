
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.MismatchedTreeNodeException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Before;
import org.junit.Rule;


public class AppTest 
{
    
    private Map<String, String> tablas = new HashMap<>();
    private MyErrorListener err;

    @Before
    public void setUp(){
        tablas.put("Customers", "C");
        tablas.put("Rock", "R");
        tablas.put("Bandas", "B");
        this.err=new MyErrorListener(tablas);
    }

    

    @Test
    public void shouldAnswerWithTrue()
    {
        String sql="Select C.id from Customers as C, Rock, Orden where Customers.name='Pepe' and Customers.id=Rock.id and Customers.id=Orden.id";
        String res= "Select C .id From Customers as C, Rock as R, Orden  where C.name = 'Pepe' and C.id = R.id and C.id = Orden .id ";
        try{
            MyRefactor ref=new MyRefactor(sql, tablas);
            assertEquals(res, ref.visit() );
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldThrowParserException()
    {
        String sql="Insert * from Customers";
        try{
          MyRefactor ref=new MyRefactor(sql, tablas);
        }
        catch(ParseCancellationException e){
            exceptionRule.expect(ParseCancellationException.class);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }        
    }

    @Test
    public void invalidSql(){
        String sql = "Hola";
        try{
          MyRefactor ref=new MyRefactor(sql, tablas);
        }
        catch(ParseCancellationException e){
            exceptionRule.expect(ParseCancellationException.class);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldThrowTableNameException()
    {
        String sql="Select * from Customers";
        tablas.put("Pedidos", "Customers");
        try{
          MyRefactor ref=new MyRefactor(sql, tablas);
        }
        catch(TableAliasException e){
            tablas.remove("Pedidos");
            exceptionRule.expect(TableNameException.class);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldThrowTableAliasException(){
        String sql="Select * from Customers, Pedidos";
        tablas.put("Pedidos", "C");
        try{
            MyRefactor ref=new MyRefactor(sql, tablas);
        }
        catch(TableNameException e){
            tablas.remove("Pedidos");
            exceptionRule.expect(TableAliasException.class);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test

    public void sameAliasInStringAsAnotherTableAlias() throws Exception{
      String sql = "Select * from Customers, Pedidos as C";
      exceptionRule.expect(SqlStringException.class);
      MyRefactor ref=new MyRefactor(sql, tablas); 
    }

    @Test
    public void SameAliasInStringAsAnotherTableName() throws Exception{
      String sql = "Select * from Customers, Pedidos as Customers";
      exceptionRule.expect(SqlStringException.class);
      MyRefactor ref=new MyRefactor(sql, tablas);
    }
}

