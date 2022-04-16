package isd.graphql.playground;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import graphql.language.TypeName;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLScalarType.Builder;

public class CustomScalars {
  
  public static final GraphQLScalarType DateTime = getDateTimeScalar();

  private static class DateTimeCoercing implements Coercing<String, String> {
    private static final String PISA_DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    private static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @Override
    public String serialize(Object input) throws CoercingSerializeException {
      if (input == null) {
        return null;
      }
      if (!(input instanceof String)) {
        throw new CoercingSerializeException("Expected type String but was '" + input.getClass().getSimpleName() + "'.");
      }
      try {
        Date date = new SimpleDateFormat(PISA_DATE_TIME_FORMAT).parse((String) input);
        return new SimpleDateFormat(ISO_DATE_TIME_FORMAT).format(date);
      } catch (ParseException e) {
        // Could not parse the input as a date
        throw new CoercingSerializeException(
            "The value '" + input + "' could not be parsed as a date of the format '" + PISA_DATE_TIME_FORMAT + "'.");
      }
    }

    @Override
    public String parseValue(Object input) throws CoercingParseValueException {
      return input.toString() + "b";
    }

    @Override
    public String parseLiteral(Object input) throws CoercingParseLiteralException {
      return input.toString() + "c";
    }

  }
  private static GraphQLScalarType getDateTimeScalar() {
    Builder builder = GraphQLScalarType.newScalar();
    builder.name("DateTime");
    builder.description("Date-Time custom scalar");
    builder.coercing(new DateTimeCoercing());
    // builder.definition(new ScalarTypeDefinition("DateTime"));
    // GraphQLDirective.Builder directiveBuilder = GraphQLDirective.newDirective();
    // directiveBuilder.
    return builder.build();
  }

}
