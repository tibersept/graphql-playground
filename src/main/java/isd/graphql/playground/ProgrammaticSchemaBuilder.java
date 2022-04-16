package isd.graphql.playground;

import graphql.schema.GraphQLObjectType;
import graphql.schema.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import graphql.Scalars;
import graphql.language.EnumTypeDefinition;
import graphql.language.EnumValueDefinition;
import graphql.language.FieldDefinition;
import graphql.language.InputValueDefinition;
import graphql.language.NonNullType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.OperationTypeDefinition;
import graphql.language.ScalarTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeName;

public class ProgrammaticSchemaBuilder {
  
  //  type Query {
  //    bookById(id: ID!): Book 
  //  }
  public ObjectTypeDefinition query(ObjectTypeDefinition book) {
    InputValueDefinition bookIdParam = InputValueDefinition.newInputValueDefinition().name("id").type(new NonNullType(new TypeName(Scalars.GraphQLID.getName()))).build();
    FieldDefinition bookByIdDefinition = FieldDefinition.newFieldDefinition().name("bookById").type(new TypeName(book.getName())).inputValueDefinition(bookIdParam).build();
    return ObjectTypeDefinition.newObjectTypeDefinition().name("Query").fieldDefinition(bookByIdDefinition).build();
  }
  
  // scalar DateTime
  public ScalarTypeDefinition dateTimeScalar() {
    return ScalarTypeDefinition.newScalarTypeDefinition().name("DateTime").build();
  }
  
  // type Book {
  // id: ID!
  // name: String
  // # the total number of pages
  // pageCount: Int
  // publicationDate: DateTime
  // author: Author
  // }
  public ObjectTypeDefinition book(ObjectTypeDefinition author) {
    TypeName idType = new TypeName(Scalars.GraphQLID.getName());
    TypeName stringType = new TypeName(Scalars.GraphQLString.getName());
    TypeName intType = new TypeName(Scalars.GraphQLInt.getName());
    TypeName dateTimeType = new TypeName(CustomScalars.DateTime.getName());
    TypeName authorType = new TypeName(author.getName());
    
    FieldDefinition idField = FieldDefinition.newFieldDefinition().name("id").type(idType).build();
    FieldDefinition nameField = FieldDefinition.newFieldDefinition().name("name").type(stringType).build();
    FieldDefinition pageCountField = FieldDefinition.newFieldDefinition().name("pageCount").type(intType).build();
    FieldDefinition publicationDateField = FieldDefinition.newFieldDefinition().name("publicationDate").type(dateTimeType).build();
    FieldDefinition authorField = FieldDefinition.newFieldDefinition().name("author").type(authorType).build();
    ObjectTypeDefinition.Builder b = ObjectTypeDefinition.newObjectTypeDefinition().name("Book").fieldDefinition(idField).fieldDefinition(nameField).fieldDefinition(pageCountField)
    .fieldDefinition(publicationDateField).fieldDefinition(authorField);
    return b.build();
  }

  // enum AuthorType {
  // GREAT
  // MEH
  // }
  public EnumTypeDefinition authorEnum() {
    List<EnumValueDefinition> enumValues = Arrays.asList(new EnumValueDefinition[] {new EnumValueDefinition("GREAT"), new EnumValueDefinition("MEH")});
    EnumTypeDefinition.Builder b = EnumTypeDefinition.newEnumTypeDefinition().name("AuthorType").enumValueDefinitions(enumValues);
    return b.build();
  }

  // type Author {
  // id: ID!
  // firstName: String
  // lastName: String
  // aType: AuthorType
  // }
  public ObjectTypeDefinition author(EnumTypeDefinition authorEnum) {
    TypeName idType = new TypeName(Scalars.GraphQLID.getName());
    TypeName stringType = new TypeName(Scalars.GraphQLString.getName());
    TypeName authorEnumType = new TypeName(authorEnum.getName());
    
    FieldDefinition idField = FieldDefinition.newFieldDefinition().name("id").type(idType).build();
    FieldDefinition firstNameField = FieldDefinition.newFieldDefinition().name("firstName").type(stringType).build();
    FieldDefinition lastNameField = FieldDefinition.newFieldDefinition().name("lastName").type(stringType).build();
    FieldDefinition authorTypeField = FieldDefinition.newFieldDefinition().name("aType").type(authorEnumType).build();
    ObjectTypeDefinition.Builder b = ObjectTypeDefinition.newObjectTypeDefinition().name("Author").fieldDefinition(idField).fieldDefinition(firstNameField).fieldDefinition(lastNameField)
        .fieldDefinition(authorTypeField);
    return b.build();
  }

}
