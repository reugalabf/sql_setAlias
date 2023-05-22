
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
        String sql="Select Customers.id from Customers, Rock, Orden where Customers.name='Pepe' and Customers.id=Rock.id and Customers.id=Orden.id";
        String res= "Select C.id From Customers as C, Rock as R, Orden  where C.name = 'Pepe' and C.id = R.id and C.id = Orden .id ";
        MyRefactor ref=new MyRefactor(sql, tablas);
        assertEquals(res, ref.visit() );
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /*@Test
    public void shoulThrowParserException()
    {
        String sql="Insert * from Customers";
        MyRefactor ref=new MyRefactor(sql, tablas);
        exceptionRule.expect(ParseCancellationException.class);
        ref.visit();
    }*/

    /*@Test
    public void shoulThrowTableException()
    {
        String sql=" * from Customers";
        MySqlLexer lex= new MySqlLexer(CharStreams.fromString(sql));
        MySqlParser pars= new MySqlParser(new CommonTokenStream(lex));
        pars.addErrorListener(err);
        MyTreeVisitor visitor= new MyTreeVisitor(tablas);
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage("A table has the same alias as another");
        visitor.visit(pars.root());
    }*/
    
}
