package isd.graphql.playground;

import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.language.EnumTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.ScalarTypeDefinition;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.schema.idl.RuntimeWiring.Builder;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

  private GraphQL graphQL;
  @Autowired
  GraphQLDataFetchers graphQLDataFetchers;

  @Bean
  public GraphQL graphQL() {
    return graphQL;
  }

  @PostConstruct
  public void init() throws IOException {
    GraphQLDataFetchers.initData();
    URL url = Resources.getResource("schema.graphqls");
    String sdl = Resources.toString(url, Charsets.UTF_8);
    GraphQLSchema graphQLSchema = buildSchema(sdl); 
    this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  private GraphQLSchema buildSchema(String sdl) {   
    ProgrammaticSchemaBuilder builder = new ProgrammaticSchemaBuilder();
    ScalarTypeDefinition dateTimeScalarType = builder.dateTimeScalar();
    EnumTypeDefinition authorEnum = builder.authorEnum();
    ObjectTypeDefinition author = builder.author(authorEnum);
    ObjectTypeDefinition book = builder.book(author);
    ObjectTypeDefinition query = builder.query(book);
    TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    typeRegistry.add(query);
    typeRegistry.add(dateTimeScalarType);
    typeRegistry.add(authorEnum);
    typeRegistry.add(book);
    typeRegistry.add(author);
    
//    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
    
    RuntimeWiring runtimeWiring = buildWiring();
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
    Builder builder = RuntimeWiring.newRuntimeWiring();   
    builder.type(newTypeWiring("Query").dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
        .type(newTypeWiring("Mutation").dataFetcher("createBook", graphQLDataFetchers.createBook()))
        .type(newTypeWiring("Book").dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()));
    builder.scalar(CustomScalars.DateTime);
    return builder.build();
  }
}
