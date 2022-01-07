# O que é o DDD-Reflection ?


DDD reflection tem o propósito de facilitar que seu dominio se torne idependente das entidades do hibernate.
Para isso foi desenvolvido dois utilitários que permite mapear seu modelo com as entidades do hibernate.

Funciona parecido com o BeanUtils, entretando ele sabe converter uma entidade na outra, respeitando as referencias
e suportando referencias circulares.

##Indice
* [Instalação](#instalação)
* [Entidades de exemplo](#entidades-de-exemplo)
* [Mapeamento das entidades](#mapeamento-das-entidades)
* [Convertendo um objeto em um novo objeto](#convertendo-um-objeto-em-um-novo-objeto)
* [Fazendo merge de um objeto no outro](#fazendo-merge-de-um-objeto-no-outro)
* [Criando proxy de um objeto do hibernate ou qualquer outra fonte de dados](#criando-proxy-de-um-objeto-do-hibernate-ou-qualquer-outra-fonte-de-dados)
* [Copiando somente attributos informados](#copiando-somente-attributos-informados)


##Instalação:
```xml
    <dependency>
      <groupId>com.github.mmaico</groupId>
      <artifactId>ddd-reflection</artifactId>
      <version>1.8.5</version>
    </dependency>
```

```xml
    compile("com.github.mmaico:ddd-reflection:1.8.5")
```

##Entidades de exemplo

  Hibernate entities

  ```javascript
       public class BusinessProposal {
            private Long id
            private User user;
            private List<ProposalSaleableItem> items;
            private ProposalTemperature temperature;
    
            //getters and setters
       }
    
       public class ProposalSaleableItem {
            private Long id;
            private BigDecimal price;
            private Product product;
            private Integer quantity;
    
            //getters and setters
       }
    
    
       public class User {
            private Long id;
            private String name;
            private List<Document> documents;
    
            //getters and setters
       }
    
       public class Document {
            public enum DocumentTypeEnum {PASSPORT, SOCIAL_SECURITY_CARD}
            private String document;
            private DocumentTypeEnum type;
    
            //getters and setters
       }
    
       public class Product {
           private Long id;
           private String name;
           
           //getters and setters
       }
   ```
 
## Mapeamento das entidades

  Essas entidades respeitam o banco de dados e eu preciso que meu dominio fale de negócio e não de banco,
  também não posso deixar que uma alteração em uma tabela ou conjuntos de tabelas alterem a forma como meu
  negócio fala, então de forma nenhuma é uma boa prática usar entidades do hibernate como parte do seu dominio.

  Criando minhas entidades de dominio com os mapeamentos para a entidade de Banco de dados.

  ```javascript
      @Model
      public class Negotiation {
    
        private Long id;
    
        @Attribute(destinationName = "user")
        private Seller seller;
    
        @Attribute(destinationName = "items")
        private List<SaleableNegociated> itemsNegotiated;
    
        private Status status;
    
        //getters and setters
      }
    
      @Model
      public class Seller {
        private Long id;
        private String name;
    
        //getters and setters
      }
    
      @Model
      public class SaleableNegociated {
            private Long id;
            private BigDecimal price;
                       
            @Attribute(destinationName = "product")
            private Saleable saleable;
            
            private Integer quantity;
    
            //getters and setters
       }
    
      @Model
      public class Saleable {
        private Long id;
        private String name;
        
        //getters and setters
      }
  ```
## Convertendo um objeto em um novo objeto

  Tendo as entidades devidamente mapeadas agora eu posso receber os valores da interface, passar pelo meu serviço
  e antes de persistir eu preciso fazer a conversão para o objeto do banco de dados(hibernate)
  exemplo:

  ```javascript
      public class NegotiationService {
    
          private NegotiationRepository repository;
    
          public void save(Negotiation negotiation) {
    
              repository.save(negotiation)
          }
    
      }
    
    
      public interface NegotiationRepository {
    
          public void save(Negotiation negotiation);
      }
    
      public class NegotiationRepositoryHibernate implements NegotiationRepository {
    
          private BusinessProposalRepository repository;
    
          public void save(Negotiation negotiation) {
              BusinessProposal businessProposal = BusinessModelClone.from(negotiation).convertTo(BusinessProposal.class);
              repository.save(businessProposal)
          }
    
      }
  ```
  
## Fazendo merge de um objeto no outro
    
  Agora suponha que já existe uma entidade BusinessProposal no bando de dados com o ID 1 então precisamos apenas
  atualizar-la para isso a implementacao do nosso repository usa o BusinessModelClone.from(negotiation).merge(businessProposalLoaded)
  esse metodo suporta circular reference.

  ```javascript
      //Implementacao para suporta updates
      public class NegotiationRepositoryHibernate implements NegotiationRepository {
    
          private BusinessProposalRepository repository;
    
          public void save(Negotiation negotiation) {
              if (negotiation.isNew()) {
                BusinessProposal businessProposal = BusinessModelClone.from(negotiation).convertTo(BusinessProposal.class);
                repository.save(businessProposal)
              } else {
                  BusinessProposal businessProposalLoaded = repository.findOne(negotiation.getId());
                  BusinessModelClone.from(negotiation).merge(businessProposalLoaded);
    
                  repository.save(businessProposalLoaded);
              }
    
          }
    
      }
  ```
  
## Criando proxy de um objeto do hibernate ou qualquer outra fonte de dados

  Proxy: Um grande problema que faz com que os usuários acabem por usar entidades do hibernade dentro de seus dominos
  e o tornando um pojo é a dificuldade em criar uma camada anti corrupção. Esse camada deve ser criada idependente
  se o sistema é novo ou legado, você não pode ficar refem de alterações no banco de dados.

  O modulo PROXY dessa api permite que você busque objetos de negocio e em Runtime esses objetos de negocio peguem
  as informações da entidade do Hibernate usando o Lazzy Load

  Exemplo:

  ```javascript
      //Implementacao para o findOne
      public class NegotiationRepositoryHibernate implements NegotiationRepository {
    
          private BusinessProposalRepository repository;
    
          public Negotiation findOne(Long id) {
              BusinessProposal businessProposalLoaded = repository.findOne(id);
    
              Negotiation negotiationProxy = BusinessModelProxy.from(businessProposalLoaded).proxy(Negotiation.class);
    
              return negotiationProxy;
          }
    
      }
  ```

  Esse trecho de código irá buscar as informações no objeto do banco de dados(BusinessProposal) usando o lazzy load
  do hibernate;

  Quando eu fizer um negotitationProxy.getId() o proxy irá fazer um businessProposal.getId() e retornará o resultado.
  Se você fizer um negotitationProxy.getSeller() o proxy internamente irá ler as anotacoes, converter o resultado para um
  objeto de dominio e coloca-lo num proxy, como o exemplo abaixo:

   > Internamente o proxy faz algo parecido com isso <br />
   > User user = businessProposal.getUser(); <br />
   > Seller sellerProxy =  BusinessModelProxy.from(user).proxy(Seller.class); <br />
   > return sellerProxy;

  No seu código irá aparece somente isso Seller seller = negotiationProxy.getSeller();


  Listas: O sistema de proxy suporta listas também, então ao fazer um negotiationProxy.getItemsNegotiated()
  o proxy irá pegar a lista de List<ProposalSaleableItem> items; do objeto do hibernate e converter para
  uma lista de List<SaleableNegociated> itemsNegotiated e assim você terá a sua lista do seu objeto de dominio
  de forma magica.

  Setter: Quando a entidade do hibernate é carregada usando um repository.findOne(1l) essa entidade está
  sendo gerenciada pelo hibernate e qualquer alteração que ela sofrer será refletida no banco de dados.
  O proxy do ddd-reflection também irá fornecer a mesma facilidade que o hibernate fornece.

  Quando você carrega o objeto do banco de dados e ele está dentro do proxy, exemplo:

  ```javascript
      //Implementacao para o findOne
      public class NegotiationRepositoryHibernate implements NegotiationRepository {
    
          private BusinessProposalRepository repository;
    
          public Negotiation findOne(Long id) {
              BusinessProposal businessProposalLoaded = repository.findOne(id);
    
              Negotiation negotiationProxy = BusinessModelProxy.from(businessProposalLoaded).proxy(Negotiation.class);
    
              return negotiationProxy;
          }
    
      }
  ```

  Quando eu carrego o objeto Negotitation negotiationLoaded = negotiationRepository.findOne(1l)
  e faço um negotiationLoaded.getSeller().setName("Jon Snow") o proxy irá fazer algo como

  businessProposalLoaded.getUser().setName("Jon Snow") então essa alteração irá refletir no banco
  de dados assim que seu framework fizer o commit antes de fechar a sessao do hibernate.

  O set do proxy também suporta objetos do seu dominio e listas, tudo isso é refletido na entidade do hibernate,
  mais um exemplo:

  > Negotitation negotiationLoaded = negotiationRepository.findOne(1l)

##Copiando somente attributos informados

Você pode usar a annotação @UpdateAttributes para informar quais atributos você deseja copiar.
Isso se torna muito util quando você tem um formulario na tela onde pede somente dados parciais de um objeto, 
tipo: 
    Você tem uma tela que pede todas as informacoes de um usuário como nome, telefone, data de nascimento, CPF etc.
    Porem em outra tela você quer atualizar somente o nome do usuário, então você irá querer que só esse dado
    seja alterado na entidade do hibernate ou objeto que faz referencia a qualquer outra fonte de dados.

Essa técnica é muito usada no Ruby On Rails onde ele armazena os attributos que estavam no formulário HTML 
e altera ou persiste somente esses atributos na entidade do banco de dados.

Exemplo
   ```javascript
        public class AbstractEntity {
            private Long id;
            
            @UpdateAttributes 
            private Set<String> updateAttributes;
            
            //getters and setters
        }
    
        public class User extends AbstractEntity {
            private String name;
            private Date birthDate;
            private String lastName;
            private Address address;
            
            //getters and setters
        }   
   ```
No seu controller você irá recuperar do HttpServletRequest os attributos que vieram do formulario
como exemplo:

   ```javascript
        //Exemplo usando Spring MVC
        public  @ResponseBody String save(@ModelAttribute User user) { 
          user.setUpdateAttributes(new HashSet("name", "birthDate");
          
          User userLoaded = repository.findOne(user.getId());
          
          BusinessModelClone.from(user).merge(userLoaded);
        }    
   ```
No exemplo acima somente o atributo name e birthDate serão mergeado para o objeto do banco de dados.
Para tornar essa ação automatica, adicionando os campos que vieram do formulario HTML, faça os seguinte:
   
   * Campos do formulario HTML
    * introduction
    * careOf
    * seller.name
    * seller.id    
    * itemsNegotiated[0].id
    * itemsNegotiated[0].quantity
    * itemsNegotiated[0].price
    * itemsNegotiated[0].product
    * itemsNegotiated[0].product.id
   
   ```javascript
    
          public class AbstractEntity {                
                
            @UpdateAttributes 
            private Set<String> updateAttributes;
                
            //getters and setters
          }
    
          @Model
          public class Negotiation  extends AbstractEntity {
        
            private Long id;
            private String introduction;
            private String careOf;            
                  
            @Attribute(destinationName = "user")
            private Seller seller;
                   
            @Attribute(destinationName = "items")
            private List<SaleableNegociated> itemsNegotiated;
        
            private Status status;
        
            //getters and setters
          }
          
          @Model
          public class SaleableNegociated extends AbstractEntity {
            private Long id;
            private BigDecimal price;
              
            private Saleable product;
              
            private Integer quantity;
      
            //getters and setters
          }
              
          @Model
          public class Saleable extends AbstractEntity {
            private Long id;
            private String name;
                  
            //getters and setters
          }
        
          //Exemplo usando Spring MVC
          public  @ResponseBody String save(@ModelAttribute Negotiation negotiation, HttpServletRequest request) {
            Set<String> attributes = request.getParameterMap().keySet()
            new NormalizeAttributesUpdate().addAttributesToUpdate(negotiation, attributes);
              
            Negotiation negotiationLoaded = repository.findOne(negotiation.getId());
              
            // Nesse merge somente os campos de Campos do formulario HTML serao mergeados
            BusinessModelClone.from(negotiation).merge(negotiationLoaded);
         }    
   ```
Os parametros que foram postados no formulario HTML serão recuperados no HTTPServletRequest
e os mesmos passados para o utilitário  NormalizeAttributesUpdate().addAttributesToUpdate
que fará a magica e adicionará os dados em cada entidade. Lembrando que todas as entidades
deverão tem uma collection anotada com @UpdateAttributes.

Quando for chamado o BusinessModelClone.from(negotiation).merge(negotiationLoaded); somente 
os atributos informados para atualizarem serão alterados.

> Se a lista de UpdateAttributes não for informada todos os dados serão mergeados. 
