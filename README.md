# Final_Objetos2

Trabajo final para la materia Orientación de Objetos 2 en la Facultad de Informatica de la Universidad Nacional de La Plata. 
Consigna otorgada por el profesor Federico Balaguer. 

# Features

Este proyecto consiste en relizar un refactoring que cambie los nombres de las tablas en una consulta sql por sus alias.

Esto se realiza utilizando una clase MyRefactor, que al momento de su creación recibe una consulta sql y una lista con los nombres y alias de diferentes tablas, en un formato Map<String,String>.
Cuando se instancia esta clase, se recorre la lista y la consulta sql para verificar que no exista ningún conflicto de alias, ya sea alias iguales para diferentes tablas o una tabla que tenga como alias el nombre de otra tabla.

Para el manejo de errores se definieron algunas excepciones personalizadas como, por ejemplo, TableNameException, que ocurre cuando una tabla posee el mismo alias que el nombre de otra tabla.

Para desarrollar este proyecto se utilizó el lenguaje Java y Antlr4 para generar las distintas clases necesarias para representar y explorar las consultas sql en forma de árbol.

# Ejemplos

```Java
    public void main(){
      String sql = "Select * from Customers, Pedidos";
      Map<String,String> lista = new HashMap<>()
      tablas.put("Customers", "C");
      tablas.put("Rock", "R");
      tablas.put("Bandas", "B");
      MyRefactor ref=new MyRefactor(sql, tablas); 
      System.out.println(ref.visit());
    }
```
Éste ejemplo va a imprimir en pantalla "Select * from Customers as C, Pedidos".

```Java
    public void main(){
      String sql = "Select * from Customers, Pedidos";
      Map<String,String> lista = new HashMap<>()
      tablas.put("Customers", "C");
      tablas.put("Rock", "R");
      tablas.put("Bandas", "B");
      tablas.put("Pedidos", "Customers");
      MyRefactor ref=new MyRefactor(sql, tablas); 
      System.out.println(ref.visit());
    }
```
Éste ejemplo levanta una excepción ya que la tabla Pedidos tiene como alias el nombre de otra tabla.

```Java
    public void main(){
      String sql = "Select * from Customers, Pedidos as Customers";
      Map<String,String> lista = new HashMap<>()
      tablas.put("Customers", "C");
      tablas.put("Rock", "R");
      tablas.put("Bandas", "B");
      MyRefactor ref=new MyRefactor(sql, tablas); 
      System.out.println(ref.visit());
    }
```
Éste ejemplo, al igual que el anterior, levanta una excepción ya que la tabla Pedidos tiene como alias el nombre de otra tabla, con la diferencia de que esto ocurre en la propia consulta sql, y no en la lista de tablas.
# Autores

Ian Petraccaro Cantero, [@Sectusius](https://github.com/Sectusius)

# Links

-Link del repositorio: https://github.com/Sectusius/Final_Objetos2.

-Antlr: https://www.antlr.org/.
